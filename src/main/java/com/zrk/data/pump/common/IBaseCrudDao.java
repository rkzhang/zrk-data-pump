package com.zrk.data.pump.common;

public interface IBaseCrudDao<T> {
	
	int deleteByPrimaryKey(Long id);

	int insert(T record);
	
	int insertSelective(T record);
	
	T selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(T record);
	
	int updateByPrimaryKey(T record);
	
}
