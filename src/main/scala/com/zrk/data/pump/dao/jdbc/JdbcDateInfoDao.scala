package com.zrk.data.pump.dao.jdbc

import java.util.{ List => JavaList }
import java.util.{ Map => JavaMap }

import org.springframework.jdbc.core.support.JdbcDaoSupport

import com.zrk.data.pump.model.DataSyncDto

class JdbcDateInfoDao extends JdbcDaoSupport {
  
  def findByDBSync(dbSync:DataSyncDto):JavaList[JavaMap[String, Object]] = {	
		val sql:StringBuilder = new StringBuilder("select * from ")
		sql.append(dbSync.getTableName()).append(" where ").append(dbSync.getKeyName()).append(" = '%s' ");
	
		//val params:Array[Object]  = new Array[Object](1)					
		//params(0) = dbSync.getKeyValue()
		
		val rows:JavaList[JavaMap[String, Object]] = getJdbcTemplate.queryForList(String.format(sql.toString(), dbSync.getKeyValue()))
		rows
	}
  
}