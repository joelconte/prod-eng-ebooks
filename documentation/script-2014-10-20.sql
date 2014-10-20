dummy-utf8;

sET DEFINE OFF;
set escape on;
alter session set CURRENT_SCHEMA = bookscan;
 
 alter TABLE book add pull_date timestamp;



DROP TABLE bookviewingstats CASCADE CONSTRAINTS;

CREATE TABLE bookviewingstats
(
PID varchar2(255 CHAR) NOT NULL, 
num_of_views NUMBER(11,0), 
title varchar2(2047 CHAR)  , 
access_rights varchar2(255 CHAR), 
collection varchar2(2047 char),
tn varchar2(255 CHAR) NOT NULL,
Publisher varchar2(255 CHAR),
owning_institution varchar2(255 CHAR),  
ie_url varchar2(255 CHAR),
report_date date);


alter table bookviewingstats 
 add CONSTRAINT pk1 PRIMARY KEY 
  (
    pid, report_date
  )
  ENABLE ; 