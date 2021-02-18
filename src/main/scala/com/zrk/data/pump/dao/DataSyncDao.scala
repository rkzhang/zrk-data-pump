package com.zrk.data.pump.dao

import org.springframework.stereotype.Repository
import java.util.{ List => JavaList }
import java.util.Date
import com.zrk.data.pump.pojo.DataSyncExample
import com.zrk.data.pump.pojo.DataSync
import com.zrk.data.pump.common.BaseCrudDaoImpl
import com.zrk.data.pump.mapper.DataSyncMapper
import java.lang.Long

@Repository
class DataSyncDao extends BaseCrudDaoImpl[DataSync, DataSyncMapper]{
  
  def findNeedSandData(maxLength:Integer):JavaList[DataSync] = {
    var example = new DataSyncExample
    var critera = example.createCriteria()
    critera andNextExecutionTimeLessThan new Date() andStatEqualTo 1
    example.setOrderByClause(String.format(" priority desc limit 0, %d ", maxLength))
    mapper.selectByExample(example)
  }
  
  def lockByIds(ids:JavaList[Long]):Integer = {
    var example = new DataSyncExample
    var critera = example.createCriteria()
    critera.andIdIn(ids)
    var rec = new DataSync
    rec.setStat(0)
    rec.setUpdateTime(new Date())
    mapper.updateByExampleSelective(rec, example)
  }
  
  def reviseDataBefore(date:Date) = {
    var example = new DataSyncExample
    var critera = example.createCriteria()
    critera andUpdateTimeLessThan date andStatEqualTo 0
    var rec = new DataSync
    rec.setStat(1)
    rec.setUpdateTime(new Date())
    mapper.updateByExampleSelective(rec, example)
  }
  
  def countUntreated:Long = {
    var example = new DataSyncExample
    var critera = example.createCriteria()
    critera andStatEqualTo 0
    mapper.countByExample(example)
  }
}