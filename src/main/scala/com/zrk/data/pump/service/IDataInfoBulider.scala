package com.zrk.data.pump.service

import java.util.{ List => JavaList }
import com.zrk.data.pump.model.DataInfo
import com.zrk.data.pump.model.DataSyncDto

trait IDataInfoBulider {
  
  def setDataInfoProperties(dbSync:DataSyncDto):JavaList[DataInfo]
  
}