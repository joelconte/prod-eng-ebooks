dummy-utf8;

sET DEFINE OFF;
set escape on;
alter session set CURRENT_SCHEMA = bookscan;
    

 
--********************metadata tables**********************

DROP TABLE bookmetadataupdate CASCADE CONSTRAINTS;

CREATE TABLE bookmetadataupdate 
(
Title varchar2(2047 CHAR) NOT NULL, 
Author varchar2(255 CHAR) , 
Subject varchar2(4000 CHAR)  , 
titleno varchar2(255 CHAR) NOT NULL,
 OCLC_number varchar2(255 CHAR) unique, --worldcat
 ISBN_ISSN varchar2(255 CHAR) unique, 
callno varchar2(255 CHAR)  , 
Partner_lib_Callno varchar2(255 CHAR),
filmno varchar2(255 CHAR) , 
Pages varchar2(255 CHAR) , 
Summary varchar2(2047 CHAR) , 
dgsno varchar2(255 CHAR)  , 
language varchar2(255 CHAR)  , 
Requesting_Location varchar2(255 CHAR),  --site requesting book to be scanned and probably will do the scan
Scanning_Location varchar2(255 CHAR), 
Record_Number varchar2(255 CHAR),
Date_original timestamp,
Publisher_original varchar2(255 CHAR),
date_added timestamp,
metadata_adder  varchar2(255 CHAR), 
check_complete timestamp,
checker  varchar2(255 CHAR),  
sent_to_scan timestamp, 
sender  varchar2(255 CHAR),  
filename VARCHAR2(255 CHAR), 
owning_institution VARCHAR2(255 CHAR), 
 CONSTRAINT titleno_Key2 PRIMARY KEY 
  (
    titleno
  )
  ENABLE 
); 
