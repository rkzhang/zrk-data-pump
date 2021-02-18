package com.zrk.data.pump.config.mq

import org.springframework.context.annotation.Configuration
import org.springframework.beans.factory.annotation.Autowired
import com.zrk.data.pump.task.DataMessageListener
import org.springframework.context.annotation.Bean
import com.aliyun.openservices.ons.api.PropertyKeyConst
import com.aliyun.openservices.ons.api.bean.ConsumerBean
import java.util.Properties
import com.aliyun.openservices.ons.api.bean.Subscription
import com.aliyun.openservices.ons.api.MessageListener
import java.util.HashMap
import java.util.{ Map => JavaMap }

@Configuration
class ConsumerClient @Autowired() (
  private val mqConfig:        MqConfig,
  private val messageListener: DataMessageListener) {
  
  @Bean(initMethod = "start", destroyMethod = "shutdown")
  def buildConsumer: ConsumerBean = {
      val consumerBean: ConsumerBean = new ConsumerBean
      //配置文件
      val properties: Properties = mqConfig.getMqPropertie
      properties.setProperty(PropertyKeyConst.GROUP_ID, mqConfig.getGroupId())
      //将消费者线程数固定为20个 20为默认值
      properties.setProperty(PropertyKeyConst.ConsumeThreadNums, "20")
      consumerBean.setProperties(properties)
      //订阅关系
      val subscriptionTable: JavaMap[Subscription, MessageListener] = new HashMap[Subscription, MessageListener]()
      val subscription: Subscription = new Subscription
      subscription.setTopic(mqConfig.getTopic())
      subscription.setExpression(mqConfig.getTag())
      subscriptionTable.put(subscription, messageListener)
      //订阅多个topic如上面设置
  
      consumerBean.setSubscriptionTable(subscriptionTable)
      consumerBean
  }
  
}