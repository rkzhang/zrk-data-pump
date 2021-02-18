package com.zrk.data.pump.queue

import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.CountDownLatch

import com.zrk.data.pump.model.DataInfo
import com.zrk.data.pump.model.DataSyncDto

class ArrayQueue(val sendQueue:ArrayBlockingQueue[DataSyncDto]) extends IQueue[DataSyncDto] {
  
  @volatile
  private var queueLatch:CountDownLatch = _
  
  override def getData:DataSyncDto = {
    sendQueue.poll()
  }
  
  override def putData(data:DataSyncDto) = {
    sendQueue.put(data)
  }
  
  override def setQueueLatch(queueLatch:CountDownLatch) = this.queueLatch = queueLatch
  
  override def getQueueLatch:CountDownLatch = queueLatch
}