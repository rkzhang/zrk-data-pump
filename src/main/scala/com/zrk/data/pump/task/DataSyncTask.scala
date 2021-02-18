package com.zrk.data.pump.task

import java.util.{ List => JavaList }
import java.util.TimerTask

import scala.collection.JavaConverters.asScalaBufferConverter
import scala.collection.JavaConverters.bufferAsJavaListConverter

import org.apache.commons.collections.CollectionUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import com.aliyun.openservices.ons.api.Message
import com.aliyun.openservices.ons.api.OnExceptionContext
import com.aliyun.openservices.ons.api.SendCallback
import com.aliyun.openservices.ons.api.SendResult
import com.aliyun.openservices.ons.api.bean.ProducerBean
import com.aliyun.openservices.ons.api.exception.ONSClientException
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.zrk.data.pump.config.mq.MqConfig
import com.zrk.data.pump.exception.SystemException
import com.zrk.data.pump.model.DataSyncDto
import com.zrk.data.pump.result.CodeMsg
import com.zrk.data.pump.service.IDataSyncService
import com.zrk.data.pump.utils.BeanUtils
import com.zrk.data.pump.utils.LogUtil

import javax.annotation.Resource
import org.apache.commons.lang3.StringUtils
import java.nio.charset.Charset
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import com.zrk.data.pump.service.IDataQueueManager
import com.zrk.data.pump.service.impl.RocketMqQueueManager
import com.zrk.data.pump.config.SystemConfig
import com.google.common.base.Objects
import com.zrk.data.pump.queue.DataQueueManager

@Service
class DataSyncTask extends TimerTask {

  private final val log = LoggerFactory.getLogger(classOf[DataSyncTask])
  
  @Autowired
  private val queueManager:RocketMqQueueManager = null

  @Resource(name = "dataInfoSyncServiceImpl")
  private val syncService: IDataSyncService[DataSyncDto] = null
  
  @Autowired
  private val config:SystemConfig = null

  override def run = {
    val executor:ThreadPoolExecutor = new ThreadPoolExecutor(10, 20, 60, TimeUnit.SECONDS, new LinkedBlockingQueue[Runnable](100))
    queueManager.producer.setCallbackExecutor(executor)
    while (true) {
      log debug "DBSync send task start"
      try {
        send
      } catch {
        case ex: Throwable => {
          LogUtil.logErrorStackMsg(log, ex)
          throw new SystemException(CodeMsg.SERVER_ERROR)
        }
      } finally {
        try {
          Thread.sleep(1000);
        } catch {
          case ex: InterruptedException => {
            LogUtil.logErrorStackMsg(log, ex)
          }
        }
      }
      log debug "DBSync send task end"
    }
  }

  def send = {
    var infos = syncService findNeedSendData DataSyncTask.MAX_RECORD
    while (CollectionUtils.isNotEmpty(infos)) {
      log.info("处理记录总数 " + infos.size)
      process(infos)
      infos = syncService findNeedSendData DataSyncTask.MAX_RECORD
    }
  }

  def process(infos: JavaList[DataSyncDto]) = {
    //var ids = infos.asScala.map(_.getId)
    //syncService.lockDatas(ids.asJava)
    if(Objects.equal(config.model, SystemConfig.CLUSTER)) {
      infos.forEach(info => {
        val data: DataSyncDto = BeanUtils.createBeanByTarget(info, classOf[DataSyncDto])
        queueManager.putData(data)
      })      
    } else {
      infos.forEach(info => DataQueueManager putData BeanUtils.createBeanByTarget(info, classOf[DataSyncDto]))
    }
  }
 
}

object DataSyncTask {

  def MAX_RECORD: Int = 2000

}