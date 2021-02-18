package com.zrk.data.pump.common

import java.util.concurrent.ThreadFactory

class MyThreadFactory (
    val prefix:String
) extends ThreadFactory {
   
  @volatile
	private var counter:Long = 1
	
	override def newThread(r:Runnable) : Thread = {
		var myThread = new MyThread(r, prefix + "-" + counter)
		counter = counter + 1;
		myThread.setStartDate
		myThread
	}
}