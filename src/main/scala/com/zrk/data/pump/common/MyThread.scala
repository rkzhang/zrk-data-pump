package com.zrk.data.pump.common

import java.util.Date

class MyThread(target:Runnable, name:String) extends Thread {
  
   var creationDate:Date = _
	
	 var startDate:Date = _
	
	 var finishDate:Date = _
  
   override def run():Unit = {
    
   }
   
   def setCreationDate = {
		creationDate = new Date();
	}
	
	def setStartDate = {
		startDate = new Date();
	}
	
	def setFinishDate = {
		finishDate = new Date();
	}
	
	def getExecutionTime:Long = {
		finishDate.getTime - startDate.getTime
	}
	
	override def toString:String = {
		var buffer = new StringBuilder();
    buffer.append(this.getName());
		buffer.append(": ");
		buffer.append(" Creation Date : ");
		buffer.append(creationDate);
		buffer.append(" : Running time: ");
		buffer.append(getExecutionTime toString);
		buffer.append(" Milliseconds.");
		return buffer.toString();
	}
}