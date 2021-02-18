package com.zrk.data.pump.queue

import com.zrk.data.pump.model.DataInfo
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.CountDownLatch
import com.zrk.data.pump.model.DataSyncDto

object DataQueueManager {
  
  private var queue:IQueue[DataSyncDto] = new ArrayQueue(new ArrayBlockingQueue[DataSyncDto](5000))
  
  def putData(data:DataSyncDto) = queue.putData(data)

  def fatchData:DataSyncDto = queue.getData
  
}