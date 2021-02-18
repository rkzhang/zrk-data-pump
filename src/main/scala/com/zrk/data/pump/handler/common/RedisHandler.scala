package com.zrk.data.pump.handler.common

import org.springframework.stereotype.Service
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.beans.factory.annotation.Autowired
import com.zrk.data.pump.handler.IDataHandler
import com.zrk.data.pump.model.DataInfo
import java.util.regex.Pattern
import org.apache.commons.lang3.StringUtils
import java.util.regex.Matcher
import com.zrk.data.pump.result.CodeMsg
import com.zrk.data.pump.exception.SystemException
import org.springframework.data.redis.core.BoundHashOperations
import java.util.{ Map => JavaMap }
import com.google.common.collect.Maps
import com.zrk.data.pump.annotation.TableName
import org.slf4j.LoggerFactory

@TableName(action="to_redis")
@Service
class RedisHandler  @Autowired()(
    private val redisTemplate:RedisTemplate[String, String]    
) extends IDataHandler {
  
  private final val log = LoggerFactory.getLogger(classOf[IDataHandler])

  private val pattern: Pattern = Pattern.compile("[{](\\S+?)[}]")

  override def create(dataInfo: DataInfo) = {
    val key: String = if (StringUtils.isNotBlank(dataInfo.getKeyExpression)) this.fatchKey(dataInfo) else dataInfo.getTableName + "." + dataInfo.getKeyValue
    val opt:BoundHashOperations[String, String, String] = redisTemplate.boundHashOps(key)
    val prop:JavaMap[String, String] = Maps.newHashMapWithExpectedSize(dataInfo.getProperties.size())
    dataInfo.getProperties.forEach((key, value) => {
      //log.debug(String.format("tord : key - %s, value - %s", key, value.getClass.getName))
      if(value.isInstanceOf[Boolean]){
		    prop.put(key, if(value.asInstanceOf[Boolean]) "1" else "0" )
		  } else {
        prop.put(key, "" + value)
		  }
    })
    opt.putAll(prop)
  }

  override def update(dataInfo: DataInfo) = {
    create(dataInfo)
  }

  override def delete(dataInfo: DataInfo) = {
    //val key: String = if (StringUtils.isNotBlank(dataInfo.getKeyExpression)) this.fatchKey(dataInfo) else dataInfo.getTableName + "." + dataInfo.getKeyValue
    val ok: Boolean = redisTemplate.delete(dataInfo.getKeyExpression)
    if (!ok) {
      throw new SystemException(CodeMsg.OPT_ERR)
    }
  }

  def fatchKey(dataInfo: DataInfo): String = {
    var expression: String = dataInfo.getKeyExpression
    val pramexp = "[{](%s)[}]"
    if (StringUtils.contains(expression, ".")) {
      val keys: Array[String] = StringUtils.split(expression, ".")
      keys.foreach(key => {
        val m: Matcher = pattern.matcher(key)
        if (m.find()) {
          expression = expression.replaceAll(String.format(pramexp, m.group(1)), "" + dataInfo.getColumnValue(m.group(1)))
        }
      })

    } else {
      val m: Matcher = pattern.matcher(expression)
      if (m.find()) {
        expression = expression.replaceAll(String.format(pramexp, m.group(1)), "" + dataInfo.getColumnValue(m.group(1)))
      }
    }
    expression
  }
}