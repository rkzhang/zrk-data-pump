package com.zrk.data.pump.queue

import java.util.concurrent.CountDownLatch

trait IQueue[T] {
  
  def getData:T
  
  def putData(data:T)
  
  def setQueueLatch(sendQueueLatch:CountDownLatch)
  
  def getQueueLatch:CountDownLatch
  
}