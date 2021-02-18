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
public class ProductCreatorTest {

	@Autowired
	private ProductCreator productCreator;
	
	@Autowired
	private GoodsCategoryCreator goodsCategoryCreator;
	
	@Autowired
	private OrderCreator orderCreator;
	
	@Autowired
	private OrderProductCreator orderProductCreator;
	
	@Autowired
	private OrderAddressCreator orderAddressCreator;
	
	@Autowired
	private ThirdPartyGoodsAddressCreator thirdPartyGoodsAddressCreator;

	@Test
	public void createProductCreator() {
		try {
			productCreator.createInedx("zx_goods_product");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void createGoodsCategoryCreator() {
		try {
			goodsCategoryCreator.createInedx("zx_goods_category");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void createOrderCreator() {
		try {
			orderCreator.createInedx("zx_orders");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void createOrderProductCreator() {
		try {
			orderProductCreator.createInedx("zx_orders_product");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void createOrderAddressCreator() {
		try {
			orderAddressCreator.createInedx("zx_user_address");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void createThirdPartyGoodsAddressCreator() {
		try {
			thirdPartyGoodsAddressCreator.createInedx("third_party_goods_address");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
