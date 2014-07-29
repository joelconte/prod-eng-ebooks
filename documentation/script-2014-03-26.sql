dummy-utf8;

sET DEFINE OFF;
set escape on;
alter session set CURRENT_SCHEMA = bookscan;
    

ALTER TABLE iaBookmetadata add constraint fk_lang_iabookmd foreign key(language) references LANGUAGES(ID);