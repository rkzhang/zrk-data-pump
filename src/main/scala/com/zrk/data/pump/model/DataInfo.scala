package com.zrk.data.pump.model

import lombok.Data
import com.zrk.data.pump.constants.OptType
import scala.beans.BeanProperty
import java.util.Date
import lombok.ToString
import java.lang.Long
import java.util.{ Map => JavaMap }
import java.util.HashMap
import org.apache.commons.lang3.StringUtils
import java.lang.Double
import java.text.SimpleDateFormat
import com.zrk.data.pump.exception.SystemException
import com.zrk.data.pump.result.CodeMsg

class DataInfo (
    @BeanProperty
    var id:Long,    
    @BeanProperty
    var tableName:String,    
    @BeanProperty
    var keyName:String,
    @BeanProperty
    var keyValue:String,
    @BeanProperty
    var optType:OptType,
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
) {

  def this() = {
    this(0, null, null, null, null, null, null, 0, null, null, null, false)
  }

  private val columnValues: JavaMap[String, Object] = new HashMap[String, Object]()
  
  val getProperties:JavaMap[String, Object] = columnValues

  def getColumnValue(columnName: String): Object = {
    return columnValues.get(StringUtils.lowerCase(columnName));
  }

  def getColumnStringValue(columnName: String): String = {
    val obj: Object = columnValues.get(StringUtils.lowerCase(columnName))
    if (obj != null) obj.toString().trim() else null
  }

  def getColumnIntegerValue(columnName: String): Integer = {
    val obj: Object = columnValues.get(StringUtils.lowerCase(columnName))
    if (obj != null) Integer.parseInt(obj.toString().trim()) else null
  }

  def getColumnLongValue(columnName: String): Long = {
    val obj: Object = columnValues.get(StringUtils.lowerCase(columnName))
    if (obj != null) Long.parseLong(obj.toString().trim()) else null
  }

  def getColumnDoubleValue(columnName: String): Double = {
    val obj: Object = columnValues.get(StringUtils.lowerCase(columnName))
    if (obj != null) Double.parseDouble(obj.toString().trim()) else null
  }

  def getColumnDateValue(columnName: String, format: String = "yyyy-MM-dd HH:mm:ss"): Date = {
    try {
      val obj: Object = columnValues.get(StringUtils.lowerCase(columnName))
      if (obj.isInstanceOf[Date]) {
        return classOf[Date].cast(obj);
      }
      val dateFormat: SimpleDateFormat = new SimpleDateFormat(format)
      if (obj != null) dateFormat.parse(obj.toString().trim()) else null
    } catch {
      case ex: Throwable => {
        throw new SystemException(CodeMsg.BIND_ERROR)
      }
    }

  }

  def getColumnDateValue(columnName: String): Date = {
    try {
      val obj: Object = columnValues.get(StringUtils.lowerCase(columnName))
      if (obj.isInstanceOf[Date]) {
        return classOf[Date].cast(obj);
      }
      val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      if (obj != null) dateFormat.parse(obj.toString().trim()) else null
    } catch {
      case ex: Throwable => {
        throw new SystemException(CodeMsg.BIND_ERROR)
      }
    }
  }

  def setColumnValue(columnName: String, value: Object) = {
    columnValues.put(StringUtils.lowerCase(columnName), value);
  }

}

object DataInfo {
    
  def apply(id:Long,tableName:String,keyName:String,keyValue:String,optType:OptType,action:String,errorInfo:String,isNotExist:Boolean) 
  = new DataInfo(id, tableName, keyName, keyValue, optType, action, errorInfo, 0, null, null, null, isNotExist)
  
}