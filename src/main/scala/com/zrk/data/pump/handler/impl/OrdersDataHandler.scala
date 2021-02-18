package com.zrk.data.pump.handler.impl

import com.zrk.data.pump.handler.IDataHandler
import com.zrk.data.pump.model.DataInfo
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import com.zrk.data.pump.annotation.TableName

@TableName("orders")
@Service
class OrdersDataHandler extends IDataHandler {

    private final val log = LoggerFactory.getLogger(classOf[OrdersDataHandler])
  
    override def create(dataInfo: DataInfo) = {
      log.debug("create" + dataInfo.getProperties)
      
    }
  
    override def update(dataInfo: DataInfo) = {
      log.debug("update" + dataInfo.getProperties)
      
    }
  
    override def delete(dataInfo: DataInfo) = {
      log.debug("delete" + dataInfo.getProperties)
      
    }
}