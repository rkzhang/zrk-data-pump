package com.zrk.data.pump.service.impl

import com.zrk.data.pump.service.IDataQueueManager
import com.zrk.data.pump.model.DataSyncDto
import org.springframework.stereotype.Service
import com.zrk.data.pump.config.mq.MqConfig
import com.aliyun.openservices.ons.api.bean.ProducerBean
import org.springframework.beans.factory.annotation.Autowired
import com.google.gson.GsonBuilder
import java.nio.charset.Charset
import com.google.gson.Gson
import com.aliyun.openservices.ons.api.OnExceptionContext
import com.aliyun.openservices.ons.api.exception.ONSClientException
import com.aliyun.openservices.ons.api.Message
import com.aliyun.openservices.ons.api.SendResult
import com.aliyun.openservices.ons.api.SendCallback
import com.zrk.data.pump.task.DataSyncTask
import org.slf4j.LoggerFactory
import scala.beans.BeanProperty

@Service
class RocketMqQueueManager  @Autowired()(
     
    val producer:ProducerBean,
    
    val mqConfig:MqConfig
    
) extends IDataQueueManager{
  
  private final val log = LoggerFactory.getLogger(classOf[RocketMqQueueManager])
  
  def putData(data:DataSyncDto) = {
    
    val gson: Gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create
    val txt: String = gson.toJson(data)
    val msg: Message = new Message( //
      mqConfig.getTopic,
      mqConfig.getTag,
      txt.getBytes(Charset.forName("UTF-8")))
    msg.setKey(data.getId.toString);
    // 发送消息，只要不抛异常就是成功
    try {
      producer.sendAsync(msg, new SendCallback {

        override def onSuccess(sendResult: SendResult) = {
          log.debug("发送成功 sendResult = " + sendResult)
        }

        override def onException(context: OnExceptionContext) = {
          //出现异常意味着发送失败，为了避免消息丢失，建议缓存该消息然后进行重试。
          log.error("发送异常 context = " + context.getException().getMessage())
          context.getException().printStackTrace()
        }
      });
    } catch {
      case ex: ONSClientException => {
        log.error("发送失败 : " + ex.getMessage())
      }
    }
  
  }
}