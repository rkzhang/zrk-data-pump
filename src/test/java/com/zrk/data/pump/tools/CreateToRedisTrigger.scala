package com.zrk.data.pump.tools

import org.stringtemplate.v4.ST

object CreateToRedisTrigger {
  
  def main(args: Array[String]): Unit = {
    val sql:StringBuilder = new StringBuilder
    sql append "DROP TRIGGER IF EXISTS `tg_{tableName}_ins`;\n"
    sql append "delimiter ;; \n"
    sql append "CREATE TRIGGER `tg_{tableName}_ins` AFTER INSERT ON `{tableName}` FOR EACH ROW "
    sql append " insert into data_sync(table_name,key_name,key_value,opt_type,priority,action,key_expression,next_execution_time,create_time) values ('{tableName}','id',new.id, 'New', {priority}, 'to_redis', '{keyExpression}', now(), now());\n"
    sql append  ";;\n"
    sql append "delimiter ;\n"
    
    sql append "DROP TRIGGER IF EXISTS `tg_{tableName}_upd`;\n"
    sql append "delimiter ;;\n"
    sql append "CREATE TRIGGER `tg_{tableName}_upd` AFTER UPDATE ON `{tableName}` FOR EACH ROW "
    sql append " insert into data_sync(table_name,key_name,key_value,opt_type,priority,action,key_expression,next_execution_time,create_time) values ('{tableName}','id',new.id, 'Update', {priority}, 'to_redis', '{keyExpression}', now(), now());\n"
    sql append  ";;\n"
    sql append "delimiter ;\n"
    
    sql append "DROP TRIGGER IF EXISTS `tg_{tableName}_del`;\n"
    sql append "delimiter ;;\n"
    sql append "CREATE TRIGGER `tg_{tableName}_del` BEFORE DELETE ON `{tableName}` FOR EACH ROW \n"
    sql append "BEGIN \n"
    sql append "DECLARE rdkey VARCHAR(100);\n"
    sql append "SET rdkey=CONCAT({keyExpressionDel});\n"
    sql append " insert into data_sync(table_name,key_name,key_value,opt_type,priority,action,key_expression,next_execution_time,create_time) values ('{tableName}','id',old.id, 'Delete', {priority}, 'to_redis', rdkey, now(), now()); \n"
    sql append "END \n;;\n"
    sql append "delimiter ;\n"
    
    
    val temple:ST = new ST(sql.toString, '{', '}')
    temple.add("tableName", "zx_goods_sku")
    temple.add("keyExpression", "sku.{sku}")
    temple.add("keyExpressionDel", "'sku.', old.sku")
    temple.add("priority", "5")
    System.out.println(temple.render)
    
    /*
    val temple:ST = new ST(sql.toString, '{', '}')
    temple.add("tableName", "zx_user")
    temple.add("keyExpression", "user.{id}")
    temple.add("keyExpressionDel", "'user.', old.id")
    temple.add("priority", "7")
    System.out.println(temple.render)*/
  }
}