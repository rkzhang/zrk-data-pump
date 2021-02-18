package com.zrk.data.pump.handler.common

import com.zrk.data.pump.handler.IDataHandler
import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory
import com.zrk.data.pump.model.DataInfo
import com.zrk.data.pump.search.dao.EsBaseDao
import org.springframework.beans.factory.annotation.Autowired
import com.zrk.data.pump.annotation.TableName
import com.google.common.collect.Maps
import java.math.BigInteger
import java.sql.Timestamp
import cn.hutool.core.date.DateUtil
import org.apache.commons.lang3.time.DateUtils
import java.util.Date
import java.util.HashMap
import java.util.{ Map => JavaMap }
import java.lang.Long
import com.zrk.data.pump.exception.SystemException
import com.zrk.data.pump.result.CodeMsg
import com.zrk.data.pump.exception.SystemException
import java.lang.Boolean

@TableName(action="to_es")
@Service
class EsHandler @Autowired()(
    private val esBaseDao:EsBaseDao    
) extends IDataHandler{
  
  private final val log = LoggerFactory.getLogger(classOf[EsHandler])
	
	override def create(dataInfo:DataInfo) = {
		log.debug("create - " + dataInfo.getKeyValue())
		val map:JavaMap[String, Object] = paseData(dataInfo.getProperties)	
		val result:Boolean = esBaseDao.save(dataInfo.getKeyValue(), map, dataInfo.getTableName());
		if(!result) {
		  throw new SystemException(CodeMsg.OPT_ERR)
		}
	}

	override def update(dataInfo:DataInfo) = {
		log.debug("update - " + dataInfo.getKeyValue())
		val map:JavaMap[String, Object] = paseData(dataInfo.getProperties)	
		val result:Boolean = esBaseDao.save(dataInfo.getKeyValue(), map, dataInfo.getTableName());
		if(!result) {
		  throw new SystemException(CodeMsg.OPT_ERR)
		}
	}

	override def delete(dataInfo:DataInfo) = {
		log.debug("delete - " + dataInfo.getKeyValue())	
		val result:Boolean = esBaseDao.delete(dataInfo.getKeyValue(), dataInfo.getTableName())
		if(!result) {
		  throw new SystemException(CodeMsg.OPT_ERR)
		}
	}
	
	private def paseData(properties:JavaMap[String, Object]) = {
	  val map:JavaMap[String, Object] = new HashMap[String, Object]()
		properties.forEach((key, value) => {
		  //log.debug(String.format("toes : key - %s, value - %s", key, value.getClass.getName))
		  if(value.isInstanceOf[BigInteger]) {
		    val date:BigInteger = value.asInstanceOf[BigInteger]
		    map.put(key, new Long(date.longValue))
		  } else if (value.isInstanceOf[Timestamp]) {
		    val date:Timestamp = value.asInstanceOf[Timestamp]
		    map.put(key, new Date(date.getTime))
		  } else if(value.isInstanceOf[Boolean]){
		    map.put(key, if(value.asInstanceOf[Boolean]) new Integer(1) else new Integer(0) )
		  } else {
		    map.put(key, value)
		  }
		})
		map
	}
}