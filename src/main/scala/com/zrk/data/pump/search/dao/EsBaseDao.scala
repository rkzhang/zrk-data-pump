package com.zrk.data.pump.search.dao

import org.springframework.beans.factory.annotation.Autowired
import org.elasticsearch.client.RestHighLevelClient
import java.lang.Boolean
import org.springframework.util.Assert
import com.google.common.collect.Maps
import java.util.Map
import org.springframework.cglib.beans.BeanMap
import org.elasticsearch.action.index.IndexRequest
import org.apache.commons.lang3.StringUtils
import cn.hutool.core.util.ReflectUtil
import cn.hutool.core.lang.UUID
import java.io.IOException
import org.elasticsearch.action.index.IndexResponse
import org.elasticsearch.client.RequestOptions
import com.zrk.data.pump.utils.LogUtil
import org.elasticsearch.action.DocWriteResponse.Result
import com.zrk.data.pump.task.DataSyncTask
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.elasticsearch.action.delete.DeleteRequest
import org.elasticsearch.action.delete.DeleteResponse
import org.elasticsearch.action.update.UpdateRequest
import org.elasticsearch.common.unit.TimeValue
import org.elasticsearch.action.support.WriteRequest
import org.elasticsearch.action.update.UpdateResponse

@Service
class EsBaseDao ( 
    @Autowired val client: RestHighLevelClient
) {
  
    private final val log = LoggerFactory.getLogger(classOf[DataSyncTask])
  
    def save(bean: Object, indexName: String): Boolean = {
      Assert.notNull(bean, "must not be null");
      val jsonMap: Map[String, Object] = Maps.newHashMapWithExpectedSize(20);
      val beanMap: BeanMap = BeanMap.create(bean);
  
      beanMap.keySet().forEach(key => jsonMap.put(key + "", beanMap.get(key)))
      val id = ReflectUtil.getFieldValue(bean, "id")
      val indexRequest: IndexRequest = new IndexRequest(indexName, indexName,
        if (id != null) id.toString() else UUID.randomUUID().toString(true)).source(jsonMap)
  
      try {
        val indexResponse: IndexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
  
        indexResponse.getResult() match {
          case Result.CREATED => return true
          case Result.UPDATED => return true
          case _              => return false
        }
      } catch {
        case ex: IOException => {
          val msg = LogUtil.logErrorStackMsg(log, ex);
        }
      }
      false
    }
  
    def save(map: Map[String, Object], indexName: String): Boolean = {
      val id = map.get("id")
      this.save(id, map, indexName)
    }
    
    def save(id:Object,map: Map[String, Object], indexName: String): Boolean = {
      val indexRequest: IndexRequest = new IndexRequest(indexName, indexName,
        if (id != null) id.toString() else UUID.randomUUID().toString(true)).source(map)
  
      try {
        val indexResponse: IndexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
  
        indexResponse.getResult() match {
          case Result.CREATED => return true
          case Result.UPDATED => return true
          case _              => return false
        }
      } catch {
        case ex: IOException => {
          val msg = LogUtil.logErrorStackMsg(log, ex);
        }
      }
      false
    }
  
    def update(id:Object, map: Map[String, Object], indexName: String): Boolean = {
      val id = map.get("id")
      val updateRequest: UpdateRequest = new UpdateRequest(indexName, indexName, id.toString()).doc(map)
  
      try {
        val updateResponse: UpdateResponse = client.update(updateRequest, RequestOptions.DEFAULT)
  
        updateResponse.getResult() match {
          case Result.UPDATED => return true
          case _              => return false
        }
      } catch {
        case ex: IOException => {
          val msg = LogUtil.logErrorStackMsg(log, ex);
        }
      }
      false
    }
  
    def delete(id: Object, indexName: String): Boolean = {
      try {
        val deleteRequest = new DeleteRequest(indexName, indexName, id.toString())
        //deleteRequest.timeout(TimeValue.timeValueMinutes(5)) //TimeValue方式
        //deleteRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL)
        val deleteResponse: DeleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
  
        deleteResponse.getResult() match {
          case Result.DELETED => return true
          case _              => return false
        }
  
      } catch {
        case ex: IOException => {
          val msg = LogUtil.logErrorStackMsg(log, ex);
        }
      }
      false
    }
}