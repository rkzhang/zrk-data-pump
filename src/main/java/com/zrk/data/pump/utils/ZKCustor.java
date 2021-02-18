package com.zrk.data.pump.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import cn.hutool.core.lang.UUID;

//@Component
public class ZKCustor {
	
	final static Logger log = LoggerFactory.getLogger(ZKCustor.class);

    private CuratorFramework client = null;
    
    private String localIp = null;

    @Value("${zookeeper.server}")
    private String ZOOKEEPER_SERVER;

    public void init(){
        if (client!=null){
            return;
        }

        //创建重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,5);

        //创建zookeeper客户端
        client = CuratorFrameworkFactory.builder().connectString(ZOOKEEPER_SERVER)
                .sessionTimeoutMs(10000)
                .retryPolicy(retryPolicy)   
                .namespace("data-pump")
                .build();

        client.start();

        try {
         	localIp = localIp();
            client.create()
	                .withMode(CreateMode.EPHEMERAL)
	                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath("/" + localIp);              
            log.info("zookeeper初始化成功");
            
            log.info("#########" + client.getChildren().forPath("/"));
        } catch (Exception e) {
            log.error("zookeeper初始化失败");
            e.printStackTrace();
        }
    }
    
    
    public String localIp() throws SocketException {
    		for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
			NetworkInterface intf = en.nextElement();
			for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
				InetAddress inetAddress = enumIpAddr.nextElement();
				if(isValidAddress(inetAddress)) {
					String localIp = inetAddress.getHostAddress() != null ? inetAddress.getHostAddress().trim() : null;
					return localIp;
				}
			}
		}	
    		return UUID.randomUUID(true).toString(true);
    }
    
    /**
     * 判断是否是IPv4，并且内网地址并过滤回环地址.
     */
    private boolean isValidAddress(InetAddress address) {
        return address instanceof Inet4Address && address.isSiteLocalAddress() && !address.isLoopbackAddress();
    }
    
    public String getLocalIp() {
    		return localIp;
    }
}