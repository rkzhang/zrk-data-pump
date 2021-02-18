package com.zrk.data.pump.service.impl

import java.util.{ List => JavaList }

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import com.zrk.data.pump.dao.DataSyncDao
import com.zrk.data.pump.model.DataInfo
import com.zrk.data.pump.service.IDataSyncService
import com.zrk.data.pump.utils.BeanUtils
import java.lang.Long
import java.util.ArrayList
import com.zrk.data.pump.pojo.DataSync
import com.zrk.data.pump.constants.OptType
import com.zrk.data.pump.model.DataSyncDto
import com.zrk.data.pump.utils.AnnotationConfigStrategy
import com.zrk.data.pump.utils.LogUtil
import com.zrk.data.pump.handler.IDataHandler
import com.zrk.data.pump.service.IDataInfoBulider
import com.zrk.data.pump.annotation.TableName
import org.apache.commons.lang3.time.DateUtils
import java.util.Date
import com.google.common.base.Objects
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

import scala.collection.JavaConverters.asScalaBufferConverter
import scala.collection.JavaConverters.bufferAsJavaListConverter
import org.apache.commons.collections.CollectionUtils
import com.zrk.data.pump.lock.ILockService

@Service
class DataInfoSyncServiceImpl @Autowired() (
    private val dataSyncDao:DataSyncDao,
    private val dataBuilder: IDataInfoBulider,
    private val lock:ILockService
) extends IDataSyncService[DataSyncDto]{
  
  private final val log = LoggerFactory.getLogger(classOf[DataInfoSyncServiceImpl])
  
  override def findNeedSendData(maxLength:Integer):JavaList[DataSyncDto] = {
    var javaList: JavaList[DataSyncDto] = null
    val key = "findNeedSendData"
    val oldCurrentTime: String = lock.lock(key, 10, 20);
    try {
      val maxQueueSize = maxLength * 10
      if(dataSyncDao.countUntreated > maxQueueSize) {
        return javaList
      }
      
      var list: JavaList[DataSync] = dataSyncDao findNeedSandData maxLength
      javaList = new ArrayList(list.size())

      list.forEach(rec => {
        var dataInfo: DataSyncDto = BeanUtils.createBeanByTarget(rec, classOf[DataSyncDto])
        javaList add dataInfo
      })
      if (CollectionUtils.isNotEmpty(javaList)) {
        var ids = javaList.asScala.map(_.getId)
        this.lockDatas(ids.asJava)
      }
    } catch {
      case ex: Throwable => {
        val msg = LogUtil.logErrorStackMsg(log, ex)     
      }
    } finally {
      lock.unlock(key, oldCurrentTime);
    }
    javaList
  }
  
  override def lockDatas(ids:JavaList[Long]) = {
    dataSyncDao.lockByIds(ids)
  }
  
  override def reviseData = {
    val curr:Date = new Date
    val before:Date = DateUtils.addMinutes(curr, -20)
    dataSyncDao.reviseDataBefore(before)
  }
	
  override def syncData(dataSync:DataSyncDto) = {
    log.debug("message --- " + dataSync.getId())
    var strategy: AnnotationConfigStrategy[TableName, IDataHandler, DataSyncDto] =
      new AnnotationConfigStrategy(classOf[TableName], "IDataHandler",
        obj => obj.isInstanceOf[IDataHandler],
        (conf, obj) => Objects.equal(conf.value(), obj.getTableName()) && Objects.equal(conf.action(), obj.getAction()))
    var sendProcessor = strategy.lookupProcessor(dataSync)
    if (sendProcessor == null) {
      //公共策略处理器
      strategy = new AnnotationConfigStrategy(classOf[TableName], "ICommonDataHandler",
        obj => obj.isInstanceOf[IDataHandler],
        (conf, obj) => Objects.equal(conf.value(), "common") && Objects.equal(conf.action(), obj.getAction()))
      sendProcessor = strategy.lookupProcessor(dataSync)
    }
    
    try {
      val dataInfos: JavaList[DataInfo] = dataBuilder.setDataInfoProperties(dataSync)

      dataInfos.forEach { dataInfo =>
        {
          dataInfo optType match {
            case OptType.New    => sendProcessor.create(dataInfo)
            case OptType.Update => sendProcessor.update(dataInfo)
            case OptType.Delete => sendProcessor.delete(dataInfo)
          }
        }
      }
      //如有配制处理器,需处理器做特例处理

      processSucessed(dataSync)

    } catch {
      case ex: Throwable => {
        val msg = LogUtil.logErrorStackMsg(log, ex)
        processfail(dataSync, msg)
      }
    }
  }
  
  private def processfail(data: DataSyncDto, result: String) {
    val sync: DataSync = BeanUtils.createBeanByTarget(data, classOf[DataSync])
    val executionCount = data.getExecutionCount + 1
    val add = 3 * Math.pow(2, executionCount)
    val curr = new Date()
    val nextExecDate = DateUtils.addMinutes(curr, add.intValue())
    sync.setExecutionCount(executionCount)
    sync.setNextExecutionTime(nextExecDate)
    sync.setUpdateTime(curr)
    sync.setErrInfo(result)
    sync.setStat(1)
    dataSyncDao.updateByPrimaryKeySelective(sync)
  }

  private def processSucessed(data: DataSyncDto) {
    dataSyncDao.deleteByPrimaryKey(data.getId)
  }
}