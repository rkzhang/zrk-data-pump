package com.zrk.data.pump.task

import com.zrk.data.pump.utils.LogUtil
import org.slf4j.LoggerFactory
import com.zrk.data.pump.model.DataInfo
import com.zrk.data.pump.queue.DataQueueManager
import com.zrk.data.pump.service.IDataSyncService
import com.zrk.data.pump.utils.AnnotationConfigStrategy
import com.zrk.data.pump.handler.IDataHandler
import com.zrk.data.pump.annotation.TableName
import com.google.common.base.Objects
import com.zrk.data.pump.constants.OptType
import com.zrk.data.pump.dao.DataSyncDao
import org.springframework.beans.factory.annotation.Autowired
import com.zrk.data.pump.pojo.DataSync
import com.zrk.data.pump.utils.BeanUtils
import org.apache.commons.lang3.time.DateUtils
import java.util.Date
import org.springframework.stereotype.Service
import com.zrk.data.pump.model.DataSyncDto
import com.zrk.data.pump.service.IDataInfoBulider
import java.util.{ List => JavaList }
import com.zrk.data.pump.model.DataSyncDto

@Service
class DataConsumerThread @Autowired() (
  syncService: IDataSyncService[DataSyncDto]) extends Runnable {

  private final val log = LoggerFactory.getLogger(classOf[DataConsumerThread])

  override def run = {
    while (true) {
      try {
        sendDate
      } catch {
        case ex: Exception => {
          LogUtil.logErrorStackMsg(log, ex)
        }
      }
    }
  }

  def sendDate: Unit = {
    val dataSync = DataQueueManager.fatchData
    if (dataSync == null) {
      Thread.sleep(1000)
      return
    }
    syncService.syncData(dataSync)
  }

}