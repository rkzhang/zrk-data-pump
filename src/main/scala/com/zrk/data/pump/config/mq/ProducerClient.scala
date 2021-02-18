package com.zrk.data.pump.config.mq

import org.springframework.context.annotation.Configuration
import org.springframework.beans.factory.annotation.Autowired
import com.aliyun.openservices.ons.api.bean.ProducerBean
import org.springframework.context.annotation.Bean
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.LinkedBlockingQueue

@Configuration
class ProducerClient @Autowired() (
  private val mqConfig:MqConfig
){
  @Bean(initMethod = "start", destroyMethod = "shutdown")
  def buildProducer:ProducerBean = {
        val producer:ProducerBean = new ProducerBean
        producer setProperties mqConfig.getMqPropertie
        producer
    }
}