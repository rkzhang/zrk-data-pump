package com.zrk.data.pump.generator;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * 
 * @author rkzhang
 *
 */
public class MyGeneratorPlugin extends PluginAdapter {

	public boolean validate(List<String> warnings) {
		return true;
	}
	
	@Override
	public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
		
		for(Element element : document.getRootElement().getElements()) {
			XmlElement el = (XmlElement)element;
			boolean isInsertMethod = false;
			if(!org.springframework.util.CollectionUtils.isEmpty(el.getAttributes())) {
				for(Attribute att : el.getAttributes()) {
					if(att.getValue().equals("insert") || att.getValue().equals("insertSelective")) {
						isInsertMethod = true;
					}
				}
			}
			if(isInsertMethod) {
				el.addAttribute(new Attribute("useGeneratedKeys", "true"));
				el.addAttribute(new Attribute("keyProperty", "id"));
			}
		}
		return super.sqlMapDocumentGenerated(document, introspectedTable);
	}

	@Override
	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

		return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
	}
}
