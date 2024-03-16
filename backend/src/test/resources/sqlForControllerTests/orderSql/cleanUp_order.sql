delete from order_item where item_id > 0;

delete from `eshop-test`.orders where user_id ='userId';
delete from `eshop-test`.orders where number ='orderNumber';

alter table category auto_increment = 0;