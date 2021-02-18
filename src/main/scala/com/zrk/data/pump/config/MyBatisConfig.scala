package com.zrk.data.pump.config

import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.mybatis.spring.annotation.MapperScan
import javax.sql.DataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import com.zrk.data.pump.dao.jdbc.JdbcDateInfoDao

@Configuration
@EnableTransactionManagement
@MapperScan(Array("com.zrk.data.pump.mapper"))
class MyBatisConfig(
  @Autowired private var dataSource: DataSource) {

  @Bean
  def jdbcDateInfoDao: JdbcDateInfoDao = {
    val dao: JdbcDateInfoDao = new JdbcDateInfoDao()
    dao.setDataSource(dataSource)
    dao
  }

}