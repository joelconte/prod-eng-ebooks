dummy-utf8;

sET DEFINE OFF;
set escape on;
alter session set CURRENT_SCHEMA = bookscan;
  
 
alter table IABOOKMETADATA disable  constraint  SYS_C0017918;
  
alter table BOOK disable  constraint  SYS_C0017925;
 

  
alter table BOOKmetadata disable  constraint  SYS_C0017933;
 