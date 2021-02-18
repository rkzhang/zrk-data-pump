package com.zrk.data.pump.tools;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.zrk.data.pump.AppConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes=AppConfig.class)
public class OrderCreatorTest {

	@Autowired
	private OrderCreator orderCreator;
	
	@Test
	public void create() {
		try {
			orderCreator.createInedx("orders");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
