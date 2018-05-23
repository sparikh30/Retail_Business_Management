create or replace package display as
type ref_cursor is ref cursor;
function getproducts return  ref_cursor;
function getpurchases return ref_cursor;
function getcustomers return ref_cursor;
function getemployees return ref_cursor;
function getlogs return ref_cursor;
function getsuppliers return ref_cursor;
function getsupplies return ref_cursor;
function getdiscount return ref_cursor;
procedure addCustomer				/* addCustomer procedure  */
(
        cid in customers.cid%TYPE,
        cname in customers.name%TYPE,
        tele in customers.telephone#%TYPE,
	status out number
);
procedure delete_purchase(			/* delete customer procedure*/
     pur_id in purchases.pur#%type,   
     status out number	
);
procedure employee_Report			/* Employee Report procedure */
(
        eid1 in employees.eid%TYPE,
	prc out sys_refcursor,
	status out number
);
function getSaving ( p_pur# purchases.pur#%TYPE )
return number;
PROCEDURE insert_purchases(
           p_eid in purchases.eid%TYPE,
           p_pid in purchases.pid%TYPE,
           p_cid in purchases.cid%TYPE,
           p_qty in purchases.qty%TYPE,
           p_status out number 
		   );
		  
end;
/
show errors

create or replace package body display as

function getproducts return ref_cursor is rc ref_cursor;
begin
	open rc for select * from products;
	return rc;
end;

function getpurchases return ref_cursor is rc ref_cursor;
begin
        open rc for select * from purchases;
        return rc;
end;

function getlogs return ref_cursor is rc ref_cursor;
begin
        open rc for select log#,user_name,operation,to_char(op_time,'YYYY-MM-DD HH24:MI:SS'),table_name,tuple_pkey 
from logs;
        return rc;
end;

function getcustomers return ref_cursor is rc ref_cursor;
begin
        open rc for select * from customers;
        return rc;
end;

function getemployees return ref_cursor is rc ref_cursor;
begin
        open rc for select * from employees;
        return rc;
end;

function getsupplies return ref_cursor is rc ref_cursor;
begin
        open rc for select * from supplies;
        return rc;
end;

function getsuppliers return ref_cursor is rc ref_cursor;
begin
        open rc for select * from suppliers;
        return rc;
end;

function getdiscount return ref_cursor is rc ref_cursor;
begin
	open rc for select * from discounts;
	return rc;
end;


procedure addCustomer
(
        cid in customers.cid%TYPE,
        cname in customers.name%TYPE,
        tele in customers.telephone#%TYPE,
	status out number
)is
visits_made number;
last_visit Date;
largevalue exception;
begin

	if length(cid)>4 then
		raise largevalue;
	end if;

        visits_made:=1;
        select SYSDATE into last_visit from dual;
        dbms_output.put_line(visits_made || '  ' || last_visit || ' ' || cid || '  '|| cname || '  '|| tele);

	/* adding to customer table current date ,and visits_made=1 */

        insert into customers values(cid,cname,tele,visits_made,last_visit);
	status:=1;

exception

	when dup_val_on_index then	
	status:=2;

	when largevalue then	/* for large value of customer id */
        status:=4;

	when others then
	status:=3;

end;


 procedure delete_purchase(
     pur_id in purchases.pur#%type,   
     status out number	
) is
qty1 number;
begin
	status:=1;
	select qty into qty1 from purchases where pur#=pur_id;

     delete from purchases where pur#=pur_id;        

exception	
	when no_data_found then  /* when no purchase is found  */
		status:=2;		
	when others then
		status:=3;
end;


procedure employee_Report			/* procedure will show Employee monthly report    */
(
        eid1 in employees.eid%TYPE,
	prc out sys_refcursor,
	status out number
)is
begin 
open prc for
select e.name,e.eid,temp.Month,temp.year,temp.total_sale,temp.count,temp.Total_qantity 
from employees e,( Select to_char(ptime, 'MON') as Month ,to_char(ptime,'YYYY') as year, sum(total_price) as 
total_sale,count(*) as count,sum(qty) as Total_qantity,eid 
from purchases where eid=eid1 group by to_char(ptime,'MON'),to_char(ptime,'YYYY'),eid) temp 
where e.eid=temp.eid;      

status:=1;

exception
when no_data_found then status:=2;

end;


function getSaving ( p_pur# purchases.pur#%TYPE )
return number is p_saving number(5,2); p_qty purchases.qty%TYPE; p_pid purchases.pid%TYPE;
begin

     select purchases.qty,purchases.pid into p_qty,p_pid from purchases where purchases.pur#=p_pur#;
     select p_qty*ORIGINAL_PRICE*DISCNT_RATE into p_saving from discounts d, products p where p.pid=p_pid and p.DISCNT_CATEGORY=d.DISCNT_CATEGORY;

     return p_saving;

     exception
	when others then
		return 1;
end;


/* insert into purchases table   */

PROCEDURE insert_purchases(
           p_eid in purchases.eid%TYPE,
           p_pid in purchases.pid%TYPE,
           p_cid in purchases.cid%TYPE,
           p_qty in purchases.qty%TYPE,
           p_status out number ) 
is
 p_ptime Date;
 p_pur# number; 
 p_ptotal purchases.total_price%TYPE; 
 p_qoh number; 
 p_thre number;
insufficient_quantity exception;

BEGIN
  select SYSDATE into p_ptime from dual;
  select pur_seq.nextval into p_pur# from dual;
  select qoh,qoh_threshold into p_qoh,p_thre from products p where p.pid=p_pid;
  p_status:=0;
  if p_qoh<p_qty then
	raise insufficient_quantity;
  else
        select p_qty*ORIGINAL_PRICE*(1-DISCNT_RATE) into p_ptotal from discounts d, products p where p.pid=p_pid 
and p.DISCNT_CATEGORY=d.DISCNT_CATEGORY;
        insert into purchases values (p_pur#,p_eid,p_pid,p_cid,p_qty,p_ptime,p_ptotal);

	if p_qoh-p_qty < p_thre then
	update products set qoh=qoh+ 11+ qoh_threshold where pid= p_pid;
	end if;
  end if;

exception
when insufficient_quantity 	/* when demanded quantity not available */
then p_status:=1;

when no_data_found then			
p_status:=2;

when others then
p_status:=3;

end;

end;
/
show errors
