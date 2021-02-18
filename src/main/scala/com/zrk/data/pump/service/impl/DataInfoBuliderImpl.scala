package com.zrk.data.pump.service.impl

import com.zrk.data.pump.service.IDataInfoBulider
import org.springframework.stereotype.Service
import com.zrk.data.pump.model.DataInfo
import com.zrk.data.pump.pojo.DataSync
import com.zrk.data.pump.constants.OptType
import org.springframework.beans.factory.annotation.Autowired
import com.zrk.data.pump.dao.jdbc.JdbcDateInfoDao
import org.slf4j.LoggerFactory
import java.util.{ List => JavaList }
import java.util.{ Map => JavaMap }
import java.util.ArrayList
import org.apache.commons.collections.CollectionUtils
import org.apache.commons.lang3.StringUtils
import com.zrk.data.pump.exception.SystemException
import com.zrk.data.pump.result.CodeMsg
import com.zrk.data.pump.utils.BeanUtils
import com.zrk.data.pump.model.DataSyncDto
import avro.shaded.com.google.common.base.Objects

@Service
class DataInfoBuliderImpl(
  @Autowired private val dateInfoDao: JdbcDateInfoDao
) extends IDataInfoBulider {

  private final val log = LoggerFactory.getLogger(classOf[DataInfoBuliderImpl])

  override def setDataInfoProperties(dbSync: DataSyncDto): JavaList[DataInfo] = {
    val results: JavaList[DataInfo] = new ArrayList[DataInfo]
    val dbInfos: JavaList[JavaMap[String, Object]] = dateInfoDao.findByDBSync(dbSync)
    if (CollectionUtils.isEmpty(dbInfos) &&
      (dbSync.getOptType == null || Objects.equal(dbSync.getOptType, OptType.Update.getActionName) || Objects.equal(dbSync.getOptType, OptType.New.getActionName))) {
      //记录 无法找到对应表中数据的异常。
      throw new SystemException(CodeMsg.DATA_ERROR)
    }

    if (CollectionUtils.isEmpty(dbInfos) || Objects.equal(dbSync.getOptType(), OptType.Delete.getActionName())) {
      val dataInfo: DataInfo = BeanUtils.createBeanByTarget(dbSync, classOf[DataInfo])
      dataInfo.setOptType(OptType.Delete)
      //删除数据记录
      results add populateDeleteDataInfo(dataInfo)
    } else {
      dbInfos.forEach(infoMap => {
        val dataInfo: DataInfo = BeanUtils.createBeanByTarget(dbSync, classOf[DataInfo])
        dbSync.getOptType match {
          case "New"    => dataInfo.setOptType(OptType.New)
          case "Update" => dataInfo.setOptType(OptType.Update)
          case "Delete" => dataInfo.setOptType(OptType.Delete)
        }
        results add populateData(dataInfo, infoMap)
      })
    }

    results
  }

  private def populateDeleteDataInfo(dataInfo: DataInfo): DataInfo = {
    dataInfo.setColumnValue(dataInfo.getKeyName(), dataInfo.getKeyValue());
    dataInfo
  }

  private def populateData(dataInfo: DataInfo, dbInfos: JavaMap[String, Object]): DataInfo = {
    dbInfos.forEach((key, value) => dataInfo.setColumnValue(key, value))
    dataInfo
  }

}