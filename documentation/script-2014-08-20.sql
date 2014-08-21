dummy-utf8;

sET DEFINE OFF;
set escape on;
alter session set CURRENT_SCHEMA = bookscan;
 
alter TABLE site add is_inactive_site char(1);
 