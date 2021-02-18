package com.zrk.data.pump.tools

import org.stringtemplate.v4.ST

object CreateToEsTrigger {
  
  def main(args: Array[String]): Unit = {
    val sql:StringBuilder = new StringBuilder
    sql append "DROP TRIGGER IF EXISTS `tg_{tableName}_ins`;\n"
    sql append "delimiter ;; \n"
    sql append "CREATE TRIGGER `tg_{tableName}_ins` AFTER INSERT ON `{tableName}` FOR EACH ROW insert into data_sync(table_name,key_name,key_value,opt_type,priority,action,next_execution_time,create_time)\n"
    sql append "values('{tableName}','id',new.id, 'New', {priority}, 'to_es', now(), now());;\n"
    sql append "delimiter ;\n"
    
    sql append "DROP TRIGGER IF EXISTS `tg_{tableName}_upd`;\n"
    sql append "delimiter ;;\n"
    sql append "CREATE TRIGGER `tg_{tableName}_upd` AFTER UPDATE ON `{tableName}` FOR EACH ROW insert into data_sync(table_name,key_name,key_value,opt_type,priority,action,next_execution_time,create_time)\n"
    sql append "values('{tableName}','id', new.id, 'Update', {priority}, 'to_es', now(), now());;\n"
    sql append "delimiter ;\n"
    
    sql append "DROP TRIGGER IF EXISTS `tg_{tableName}_del`;\n"
    sql append "delimiter ;;\n"
    sql append "CREATE TRIGGER `tg_{tableName}_del` BEFORE DELETE ON `{tableName}` FOR EACH ROW insert into data_sync(table_name,key_name,key_value,opt_type,priority,action,next_execution_time,create_time)\n"
    sql append "values('{tableName}','id', old.id, 'Delete', {priority}, 'to_es', now(), now());;\n"
    sql append "delimiter ;\n\n"
    
    var temple:ST = new ST(sql.toString, '{', '}')

    temple.add("tableName", "zx_goods_product")
    temple.add("priority", "7")
    System.out.println(temple.render)
    
    temple = new ST(sql.toString, '{', '}')
    temple.add("tableName", "zx_goods_category")
    temple.add("priority", "3")
    System.out.println(temple.render)
    
    temple = new ST(sql.toString, '{', '}')
    temple.add("tableName", "zx_orders")
    temple.add("priority", "10")
    System.out.println(temple.render)
    
    temple = new ST(sql.toString, '{', '}')
    temple.add("tableName", "zx_orders_product")
    temple.add("priority", "10")
    System.out.println(temple.render)
    
    temple = new ST(sql.toString, '{', '}')
    temple.add("tableName", "zx_user_address")
    temple.add("priority", "8")
    System.out.println(temple.render)
  
    temple = new ST(sql.toString, '{', '}')
    temple.add("tableName", "third_party_goods_address")
    temple.add("priority", "5")
    System.out.println(temple.render)
    
  }
}