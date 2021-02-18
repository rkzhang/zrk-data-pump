package com.zrk.data.pump

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext
import com.zrk.data.pump.task.DataSyncTask
import java.util.concurrent.Executors
import java.util.concurrent.ExecutorService
import com.zrk.data.pump.task.DataConsumerThread
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.EnableTransactionManagement
import com.zrk.data.pump.config.SystemConfig
import com.google.common.base.Objects
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import com.zrk.data.pump.service.IDataSyncService
import com.zrk.data.pump.model.DataSyncDto

@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
@SpringBootApplication
@ComponentScan(Array("com.zrk.data.pump"))
class AppConfig

object DataPumpApplication extends App {
    private final val log = LoggerFactory.getLogger(classOf[AppConfig])
  
    val context:ConfigurableApplicationContext = SpringApplication.run(classOf[AppConfig])
    val syncTask:DataSyncTask  = context.getBean(classOf[DataSyncTask])
		val es:ExecutorService = Executors.newCachedThreadPool
		es.execute(syncTask)	//数据同步线程
		log.info("数据同步生产线程 启动")
		val config:SystemConfig = context.getBean(classOf[SystemConfig])
		
		if(Objects.equal(config.model, SystemConfig.SINGLE)) {
    		val consumerThread:DataConsumerThread = context.getBean(classOf[DataConsumerThread])
    		var cosumerNum = 10
    		for(x <- 0 until cosumerNum) {
    			es.execute(consumerThread);
    			log.info("数据同步消费线程 启动")
    		}
    }
    
    val syncService:IDataSyncService[DataSyncDto]  = context.getBean(classOf[IDataSyncService[DataSyncDto]])
    val singleThread:ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor
    singleThread.scheduleAtFixedRate(() => {
      log.info("数据修正 执行")
      syncService.reviseData
    }, 1, 10, TimeUnit.MINUTES)
    log.info("数据修正线程 启动")
    log.info("启动模式 --- " + config.model)
}