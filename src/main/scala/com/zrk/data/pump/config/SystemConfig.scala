package com.zrk.data.pump.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class SystemConfig (
  @Value("${start.model}")
  val model:String,    	    
){
  
}

object SystemConfig {
  
  val SINGLE = "single"
  
  val CLUSTER = "cluster"
}