create or replace trigger qoh_on_products 
after insert on purchases 
for each row
 begin
     update products set qoh = qoh - :new.qty
     where pid = :new.pid;
	
     update customers set visits_made=visits_made+1, last_visit_date=sysdate where cid=:new.cid;
 end; 
/
 show errors

 create or replace trigger delete_purchases
 after delete on purchases
 for each row
 begin
	update products set qoh=qoh+ :old.qty where pid= :old.pid  ;
	update customers set visits_made=visits_made+1 , last_visit_date=sysdate where  cid= :old.cid;
 end; 

/
show errors

create or replace trigger updateThresholdTrigger
  after update on products
  for each row
 declare
 sup1# supplies.sup#%TYPE;
 sid1 supplies.sid%TYPE;
  begin
        if(:new.qoh < :old.qoh_threshold) then
                select min(sid) into sid1 from supplies where pid= :old.pid;
                select sup_seq.nextval into sup1# from dual;
                insert into supplies values(sup1#,:old.pid,sid1,sysdate,11+ :old.qoh_threshold);
        end if;
 end; 
/
 show errors

 
 create or replace trigger log_insert_customer
after insert on customers
for each row
declare
log1 logs.log#%TYPE;
user1 logs.user_name%TYPE;
begin
		select user into user1 from dual;
                select log_seq.nextval into log1 from dual;
                insert into logs values(log1,user1,'insert',CURRENT_TIMESTAMP,'customers',:new.cid);
end;
/
show errors

create or replace trigger log_insert_purchase
after insert on purchases
for each row
declare
log2 logs.log#%TYPE;
user2 logs.user_name%TYPE;
begin
                select user into user2 from dual;
                select log_seq.nextval into log2 from dual;
                insert into logs values(log2,user2,'insert',CURRENT_TIMESTAMP,'purchases',:new.pur#);
end;
/
show errors


create or replace trigger log_insert_supplies
after insert on supplies
for each row
declare
log3 logs.log#%TYPE;
user3 logs.user_name%TYPE;
begin
                select user into user3 from dual;
                select log_seq.nextval into log3 from dual;
                insert into logs values(log3,user3,'insert',CURRENT_TIMESTAMP,'supplies',:new.sup#);
end;
/
show errors

create or replace trigger log_update_customer
after update of last_visit_date on customers
for each row
declare
log4 logs.log#%TYPE;
user4 logs.user_name%TYPE;
begin
                select user into user4 from dual;
                select log_seq.nextval into log4 from dual;
                insert into logs values(log4,user4,'update',CURRENT_TIMESTAMP,'customers',:old.cid);
end;
/
show errors

create or replace trigger log_update_product
after update of qoh on products
for each row
declare
log5 logs.log#%TYPE;
user5 logs.user_name%TYPE;
begin
                select user into user5 from dual;
                select log_seq.nextval into log5 from dual;
                insert into logs values(log5,user5,'update',CURRENT_TIMESTAMP,'products',:old.pid);
end;
/
show errors

