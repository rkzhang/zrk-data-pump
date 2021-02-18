package com.zrk.data.pump.config.mq

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import scala.beans.BeanProperty
import java.util.Properties
import com.aliyun.openservices.ons.api.PropertyKeyConst

@Configuration
@ConfigurationProperties(prefix = "rocketmq")
class MqConfig {

  @BeanProperty
  var accessKey: String = _
  @BeanProperty
  var secretKey: String = _
  @BeanProperty
  var nameSrvAddr: String = _
  @BeanProperty
  var topic: String = _
  @BeanProperty
  var groupId: String = _
  @BeanProperty
  var tag: String = _

  def getMqPropertie: Properties = {
    val properties: Properties = new Properties()
    properties.setProperty(PropertyKeyConst.AccessKey, this.accessKey)
    properties.setProperty(PropertyKeyConst.SecretKey, this.secretKey)
    properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, this.nameSrvAddr)
    properties;
  }

}