package com.zrk.data.pump.service

import com.zrk.data.pump.model.DataSyncDto

trait IDataQueueManager {
  
  def putData(data:DataSyncDto)
  
}