package com.zrk.data.pump.model

import com.zrk.data.pump.constants.OptType
import java.util.Date
import scala.beans.BeanProperty
import java.lang.Long

class DataSyncDto (
    @BeanProperty
    var id:Long,    
    @BeanProperty
    var tableName:String,    
    @BeanProperty
    var keyName:String,
    @BeanProperty
    var keyValue:String,
    @BeanProperty
    var optType:String,
    @BeanProperty
    var action:String,
    @BeanProperty
    var errInfo:String,
    @BeanProperty
    var executionCount:Integer,
    @BeanProperty
    var nextExecutionTime:Date,
    @BeanProperty
    var priority:Integer,
    @BeanProperty
    var keyExpression:String,
    @BeanProperty
    var isNotExist:Boolean
){
  
  def this() = {
    this(0, null, null, null, null, null, null, 0, null, null, null, false)
  }
  
}