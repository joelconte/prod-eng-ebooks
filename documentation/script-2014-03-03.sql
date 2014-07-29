dummy-utf8;

sET DEFINE OFF;
set escape on;
alter session set CURRENT_SCHEMA = bookscan;
    

 
--********************update author len**********************
ALTER TABLE
   book
MODIFY
   (
   author varchar2(512)
   )
;

ALTER TABLE
   bookmetadata 
MODIFY
   (
   author varchar2(512) 
   )
;

ALTER TABLE
   iabookmetadata 
MODIFY
   (
   author varchar2(512)
   )
;

ALTER TABLE
   bookmetadataupdate
MODIFY
   (
   author varchar2(512)
   )
;