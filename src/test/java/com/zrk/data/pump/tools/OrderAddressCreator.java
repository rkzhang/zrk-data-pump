package com.zrk.data.pump.tools;

import java.io.IOException;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderAddressCreator {

private final Logger log = LoggerFactory.getLogger(OrderAddressCreator.class);
	
	@Autowired
	private RestHighLevelClient client;

	public void createInedx(String indexName) throws IOException {
		// 1、创建 创建索引request 参数：索引名mess
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        // 2、设置索引的settings
        request.settings(Settings.builder().put("index.number_of_shards", 5) // 分片数
                .put("index.number_of_replicas", 2) // 副本数
                .put("analysis.analyzer.default.tokenizer", "ik_smart") // 默认分词器
        );
        
        // 3、设置索引的mappings
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.startObject(indexName);
            {
                builder.startObject("properties");
                {
                    builder.startObject("id");
                    {
                        builder.field("type", "long");
                    }
                    builder.endObject();
                    
                    builder.startObject("zx_user_id");
                    {
                        builder.field("type", "long");
                    }
                    builder.endObject();    
                    
                    builder.startObject("username");
                    {
                        builder.field("type", "keyword");
                    }
                    builder.endObject();
                    
                    builder.startObject("phone");
                    {
                        builder.field("type", "keyword");
                    }
                    builder.endObject();                      
                    
                    builder.startObject("province");
                    {
                        builder.field("type", "keyword");
                    }
                    builder.endObject(); 
                    
                    builder.startObject("city");
                    {
                        builder.field("type", "keyword");
                    }
                    builder.endObject(); 

                    builder.startObject("district");
                    {
                        builder.field("type", "keyword");
                    }
                    builder.endObject(); 
                    
                    builder.startObject("address");
                    {
                        builder.field("type", "keyword");
                    }
                    builder.endObject();
                    
                    builder.startObject("isdefult");
                    {
                        builder.field("type", "short");
                    }
                    builder.endObject();
                    
                    builder.startObject("create_at");
                    {
                        builder.field("type", "long");
                    }
                    builder.endObject();
                    
                    builder.startObject("update_at");
                    {
                        builder.field("type", "long");
                    }
                    builder.endObject();
                    
                    builder.startObject("is_delete");
                    {
                        builder.field("type", "integer");
                    }
                    builder.endObject();                  
                    
                }
                builder.endObject();
            }
            builder.endObject();
        }
        builder.endObject();
        request.mapping(indexName, builder);
        log.info(request.mappings().toString());
        
        // 5.1 同步方式发送请求
        try {
			CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
