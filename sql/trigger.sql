DROP TRIGGER IF EXISTS `tg_zx_goods_product_ins`;
delimiter ;; 
CREATE TRIGGER `tg_zx_goods_product_ins` AFTER INSERT ON `zx_goods_product` FOR EACH ROW insert into data_sync(table_name,key_name,key_value,opt_type,priority,action,next_execution_time,create_time)
values('zx_goods_product','id',new.id, 'New', 7, 'to_es', now(), now());;
delimiter ;
DROP TRIGGER IF EXISTS `tg_zx_goods_product_upd`;
delimiter ;;
CREATE TRIGGER `tg_zx_goods_product_upd` AFTER UPDATE ON `zx_goods_product` FOR EACH ROW insert into data_sync(table_name,key_name,key_value,opt_type,priority,action,next_execution_time,create_time)
values('zx_goods_product','id', new.id, 'Update', 7, 'to_es', now(), now());;
delimiter ;
DROP TRIGGER IF EXISTS `tg_zx_goods_product_del`;
delimiter ;;
CREATE TRIGGER `tg_zx_goods_product_del` BEFORE DELETE ON `zx_goods_product` FOR EACH ROW insert into data_sync(table_name,key_name,key_value,opt_type,priority,action,next_execution_time,create_time)
values('zx_goods_product','id', old.id, 'Delete', 7, 'to_es', now(), now());;
delimiter ;


DROP TRIGGER IF EXISTS `tg_zx_goods_category_ins`;
delimiter ;; 
CREATE TRIGGER `tg_zx_goods_category_ins` AFTER INSERT ON `zx_goods_category` FOR EACH ROW insert into data_sync(table_name,key_name,key_value,opt_type,priority,action,next_execution_time,create_time)
values('zx_goods_category','id',new.id, 'New', 3, 'to_es', now(), now());;
delimiter ;
DROP TRIGGER IF EXISTS `tg_zx_goods_category_upd`;
delimiter ;;
CREATE TRIGGER `tg_zx_goods_category_upd` AFTER UPDATE ON `zx_goods_category` FOR EACH ROW insert into data_sync(table_name,key_name,key_value,opt_type,priority,action,next_execution_time,create_time)
values('zx_goods_category','id', new.id, 'Update', 3, 'to_es', now(), now());;
delimiter ;
DROP TRIGGER IF EXISTS `tg_zx_goods_category_del`;
delimiter ;;
CREATE TRIGGER `tg_zx_goods_category_del` BEFORE DELETE ON `zx_goods_category` FOR EACH ROW insert into data_sync(table_name,key_name,key_value,opt_type,priority,action,next_execution_time,create_time)
values('zx_goods_category','id', old.id, 'Delete', 3, 'to_es', now(), now());;
delimiter ;


DROP TRIGGER IF EXISTS `tg_zx_orders_ins`;
delimiter ;; 
CREATE TRIGGER `tg_zx_orders_ins` AFTER INSERT ON `zx_orders` FOR EACH ROW insert into data_sync(table_name,key_name,key_value,opt_type,priority,action,next_execution_time,create_time)
values('zx_orders','id',new.id, 'New', 10, 'to_es', now(), now());;
delimiter ;
DROP TRIGGER IF EXISTS `tg_zx_orders_upd`;
delimiter ;;
CREATE TRIGGER `tg_zx_orders_upd` AFTER UPDATE ON `zx_orders` FOR EACH ROW insert into data_sync(table_name,key_name,key_value,opt_type,priority,action,next_execution_time,create_time)
values('zx_orders','id', new.id, 'Update', 10, 'to_es', now(), now());;
delimiter ;
DROP TRIGGER IF EXISTS `tg_zx_orders_del`;
delimiter ;;
CREATE TRIGGER `tg_zx_orders_del` BEFORE DELETE ON `zx_orders` FOR EACH ROW insert into data_sync(table_name,key_name,key_value,opt_type,priority,action,next_execution_time,create_time)
values('zx_orders','id', old.id, 'Delete', 10, 'to_es', now(), now());;
delimiter ;


DROP TRIGGER IF EXISTS `tg_zx_orders_product_ins`;
delimiter ;; 
CREATE TRIGGER `tg_zx_orders_product_ins` AFTER INSERT ON `zx_orders_product` FOR EACH ROW insert into data_sync(table_name,key_name,key_value,opt_type,priority,action,next_execution_time,create_time)
values('zx_orders_product','id',new.id, 'New', 10, 'to_es', now(), now());;
delimiter ;
DROP TRIGGER IF EXISTS `tg_zx_orders_product_upd`;
delimiter ;;
CREATE TRIGGER `tg_zx_orders_product_upd` AFTER UPDATE ON `zx_orders_product` FOR EACH ROW insert into data_sync(table_name,key_name,key_value,opt_type,priority,action,next_execution_time,create_time)
values('zx_orders_product','id', new.id, 'Update', 10, 'to_es', now(), now());;
delimiter ;
DROP TRIGGER IF EXISTS `tg_zx_orders_product_del`;
delimiter ;;
CREATE TRIGGER `tg_zx_orders_product_del` BEFORE DELETE ON `zx_orders_product` FOR EACH ROW insert into data_sync(table_name,key_name,key_value,opt_type,priority,action,next_execution_time,create_time)
values('zx_orders_product','id', old.id, 'Delete', 10, 'to_es', now(), now());;
delimiter ;


DROP TRIGGER IF EXISTS `tg_zx_user_address_ins`;
delimiter ;; 
CREATE TRIGGER `tg_zx_user_address_ins` AFTER INSERT ON `zx_user_address` FOR EACH ROW insert into data_sync(table_name,key_name,key_value,opt_type,priority,action,next_execution_time,create_time)
values('zx_user_address','id',new.id, 'New', 8, 'to_es', now(), now());;
delimiter ;
DROP TRIGGER IF EXISTS `tg_zx_user_address_upd`;
delimiter ;;
CREATE TRIGGER `tg_zx_user_address_upd` AFTER UPDATE ON `zx_user_address` FOR EACH ROW insert into data_sync(table_name,key_name,key_value,opt_type,priority,action,next_execution_time,create_time)
values('zx_user_address','id', new.id, 'Update', 8, 'to_es', now(), now());;
delimiter ;
DROP TRIGGER IF EXISTS `tg_zx_user_address_del`;
delimiter ;;
CREATE TRIGGER `tg_zx_user_address_del` BEFORE DELETE ON `zx_user_address` FOR EACH ROW insert into data_sync(table_name,key_name,key_value,opt_type,priority,action,next_execution_time,create_time)
values('zx_user_address','id', old.id, 'Delete', 8, 'to_es', now(), now());;
delimiter ;


DROP TRIGGER IF EXISTS `tg_third_party_goods_address_ins`;
delimiter ;; 
CREATE TRIGGER `tg_third_party_goods_address_ins` AFTER INSERT ON `third_party_goods_address` FOR EACH ROW insert into data_sync(table_name,key_name,key_value,opt_type,priority,action,next_execution_time,create_time)
values('third_party_goods_address','id',new.id, 'New', 5, 'to_es', now(), now());;
delimiter ;
DROP TRIGGER IF EXISTS `tg_third_party_goods_address_upd`;
delimiter ;;
CREATE TRIGGER `tg_third_party_goods_address_upd` AFTER UPDATE ON `third_party_goods_address` FOR EACH ROW insert into data_sync(table_name,key_name,key_value,opt_type,priority,action,next_execution_time,create_time)
values('third_party_goods_address','id', new.id, 'Update', 5, 'to_es', now(), now());;
delimiter ;
DROP TRIGGER IF EXISTS `tg_third_party_goods_address_del`;
delimiter ;;
CREATE TRIGGER `tg_third_party_goods_address_del` BEFORE DELETE ON `third_party_goods_address` FOR EACH ROW insert into data_sync(table_name,key_name,key_value,opt_type,priority,action,next_execution_time,create_time)
values('third_party_goods_address','id', old.id, 'Delete', 5, 'to_es', now(), now());;
delimiter ;