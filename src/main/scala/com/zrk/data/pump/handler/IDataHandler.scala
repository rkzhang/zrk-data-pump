package com.zrk.data.pump.handler

import com.zrk.data.pump.model.DataInfo

trait IDataHandler {
  
  def create(dataInfo:DataInfo)
	
	def update(dataInfo:DataInfo)
	
	def delete(dataInfo:DataInfo)

}