dummy-utf8;

sET DEFINE OFF;
set escape on;
alter session set CURRENT_SCHEMA = bookscan;
  

 
alter table IABOOKMETADATA disable  constraint  SYS_C0015380;
 
alter table BOOK disable  constraint  SYS_C0014561;
 
alter table BOOKmetadata disable  constraint  SYS_C0014569;
