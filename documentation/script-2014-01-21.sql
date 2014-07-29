dummy-utf8;

sET DEFINE OFF;
set escape on;
alter session set CURRENT_SCHEMA = bookscan;
   

--columns for spence


ALTER TABLE book add fhc_Title varchar2(2047 CHAR); 
ALTER TABLE book add fhc_tn varchar2(255 CHAR); 

ALTER TABLE iabookmetadata add fhc_Title varchar2(2047 CHAR); 
ALTER TABLE iabookmetadata add fhc_tn varchar2(255 CHAR); 



--site goals

DROP TABLE Site_goal CASCADE CONSTRAINTS;
CREATE TABLE Site_goal (
site VARCHAR2(255 CHAR) ,
year  VARCHAR2(255 CHAR),
goal_images_yearly NUMBER(11,0),
 CONSTRAINT site_goal_Key PRIMARY KEY 
  (
    site, year
  )
  ENABLE 
);

ALTER TABLE Site_goal add constraint fk_site_goals foreign key(site) references site(ID);

