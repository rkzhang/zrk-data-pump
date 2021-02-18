package com.zrk.data.pump.task

import com.aliyun.openservices.ons.api.MessageListener
import org.springframework.stereotype.Component
import com.aliyun.openservices.ons.api.Action
import org.slf4j.LoggerFactory
import com.aliyun.openservices.ons.api.ConsumeContext
import com.aliyun.openservices.ons.api.Message
import java.nio.charset.Charset
import com.zrk.data.pump.service.IDataSyncService
import org.springframework.beans.factory.annotation.Autowired
import com.zrk.data.pump.model.DataSyncDto
import com.google.gson.Gson
import com.google.gson.GsonBuilder

@Component
class DataMessageListener @Autowired() (
    private val syncService: IDataSyncService[DataSyncDto]
) extends MessageListener {

  private final val log = LoggerFactory.getLogger(classOf[DataMessageListener])

  override def consume(message: Message, context: ConsumeContext): Action = {
    log.debug("接收到消息: " + message)
    val txt: String = new String(message.getBody(), Charset.forName("UTF-8"))
    log.debug("消息字符串: " + txt);
    try {
      val gson:Gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create
      val data:DataSyncDto = gson.fromJson(txt, classOf[DataSyncDto])
      syncService.syncData(data)
      Action.CommitMessage
    } catch {
      case ex: Exception => {
        //消费失败
        Action.ReconsumeLater
      }
    }
  }
}