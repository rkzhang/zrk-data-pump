package com.zrk.data.pump.common;

import org.springframework.beans.factory.annotation.Autowired;

import cn.hutool.core.util.ReflectUtil;

public class BaseCrudDaoImpl<T, M> implements IBaseCrudDao<T> {

	   @Autowired
	   protected M mapper;
	   
	   public int deleteByPrimaryKey(Long id) {
		   return ReflectUtil.invoke(mapper, "deleteByPrimaryKey", id);
	   }
	
	   public int insert(T record) {
		   return ReflectUtil.invoke(mapper, "insert", record);
	   }
	
	   public int insertSelective(T record) {
		   return ReflectUtil.invoke(mapper, "insertSelective", record);
	   }
	
	   public T selectByPrimaryKey(Long id) {
		   return ReflectUtil.invoke(mapper, "selectByPrimaryKey", id);
	   }
	
	   public int updateByPrimaryKeySelective(T record) {
		   return ReflectUtil.invoke(mapper, "updateByPrimaryKeySelective", record);
	   }
	
	   public int updateByPrimaryKey(T record) {
		   return ReflectUtil.invoke(mapper, "updateByPrimaryKey", record);
	   }
	
}
