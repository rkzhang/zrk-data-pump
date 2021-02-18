package com.zrk.data.pump.service

import java.util.{List => JavaList}
import java.lang.Long

trait IDataSyncService[T] {
  
  def findNeedSendData(maxLength:Integer):JavaList[T]
	
  def lockDatas(ids:JavaList[Long])
  
  def syncData(data:T)
  
  def reviseData()
  
}