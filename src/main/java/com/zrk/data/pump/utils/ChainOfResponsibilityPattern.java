package com.zrk.data.pump.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import org.apache.commons.collections.CollectionUtils;

import lombok.Setter;

/**
 * 责任链模式实现
 * @author rkzhang
 * @param <I>
 */
public class ChainOfResponsibilityPattern<I> {

	private static Map<String, Set<String>> beanMap = new ConcurrentHashMap<String, Set<String>>();
	
	@Setter
	private Predicate<Object> isSub;
	
	/**
	 * 执行优先级比较器
	 */
	@Setter
	private Comparator<I> compare;
	
	@Setter
	private String key;
	
	public ChainOfResponsibilityPattern(String key){
		this.key = key;
	}
	
	public ChainOfResponsibilityPattern(String key, Predicate<Object> isSub, Comparator<I> compare){
		this.key = key;
		this.isSub = isSub;
		this.compare = compare;
		init(isSub);
	}

	public void init(Predicate<Object> isSub) {				
		if(CollectionUtils.isEmpty(beanMap.get(key))) {
			beanMap.put(key, new TreeSet<String>());
		}
		
		for(String beanName : SpringUtils.getApplicationContext().getBeanDefinitionNames()){
			Object obj = SpringUtils.getApplicationContext().getBean(beanName);
			if(isSub.test(obj)) {
				Set<String> list = beanMap.get(key);
				list.add(beanName);
			}
		}		
	}
	
	public List<I> lookupAllProcessor() {
		Set<String> processorBeans = beanMap.get(key);
		if(CollectionUtils.isEmpty(processorBeans)) {
			init(isSub);
		}
		List<I> handlerList = new ArrayList<>();
		for(String beanName : processorBeans){
			Object obj = SpringUtils.getApplicationContext().getBean(beanName);
			handlerList.add((I) obj);			
		}
		Collections.sort(handlerList, compare);
		return handlerList;
	}
	
	public void doAll(Predicate<I> doProcessor) {
		List<I> list = lookupAllProcessor();
		if(CollectionUtils.isEmpty(list)) {
			return;
		}
		for(I i : list) {
			if(!doProcessor.test(i)) {
				//不需要执行后续处理器，返回
				return;
			}
		}
	}
	
}
