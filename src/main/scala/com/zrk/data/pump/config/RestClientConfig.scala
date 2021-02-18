package com.zrk.data.pump.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration
import org.springframework.beans.factory.annotation.Value
import org.elasticsearch.client.RestHighLevelClient
import org.springframework.data.elasticsearch.core.EntityMapper
import org.springframework.core.convert.support.DefaultConversionService
import org.springframework.data.elasticsearch.core.ElasticsearchEntityMapper
import org.elasticsearch.client.RestClientBuilder
import org.springframework.context.annotation.Bean
import org.apache.http.HttpHost
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.client.CredentialsProvider
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder
import org.elasticsearch.client.RestClient


@Configuration
class RestClientConfig(
    @Value("${elasticsearch.url}")
    private val elastcUrl:String,    	
    	@Value("${elasticsearch.username}")
    	private val username:String,    	
    @Value("${elasticsearch.password}")
    	private val passwd:String
) extends AbstractElasticsearchConfiguration {

  @Bean
  override def elasticsearchClient:RestHighLevelClient = {
    // 阿里云ES集群需要basic auth验证。
        val credentialsProvider:CredentialsProvider  = new BasicCredentialsProvider()
       //访问用户名和密码为您创建阿里云Elasticsearch实例时设置的用户名和密码，也是Kibana控制台的登录用户名和密码。
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, passwd));

       // 通过builder创建rest client，配置http client的HttpClientConfigCallback。
       // 单击所创建的Elasticsearch实例ID，在基本信息页面获取公网地址，即为ES集群地址。
        val builder:RestClientBuilder = RestClient.builder(new HttpHost(elastcUrl, 9200))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {                  
                    override def customizeHttpClient(httpClientBuilder:HttpAsyncClientBuilder):HttpAsyncClientBuilder = {                    	
                        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
                    }
                }).setRequestConfigCallback(requestConfigBuilder => {
                    requestConfigBuilder.setSocketTimeout(5 * 60 * 1000);
                    requestConfigBuilder
                });
        builder.setMaxRetryTimeoutMillis(5 * 60 * 1000);
		return new RestHighLevelClient(builder);
  }
  
  @Bean
  override def entityMapper:EntityMapper = {
    val entityMapper:ElasticsearchEntityMapper = new ElasticsearchEntityMapper(elasticsearchMappingContext(), new DefaultConversionService())
		entityMapper.setConversions(elasticsearchCustomConversions());
		return entityMapper
  }
}