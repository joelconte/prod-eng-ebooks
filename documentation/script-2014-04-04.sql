dummy-utf8;

sET DEFINE OFF;
set escape on;
alter session set CURRENT_SCHEMA = bookscan;
    
DROP VIEW TF_In_Proc_PDF_Date_no_rel_dat;
CREATE OR REPLACE VIEW TF_In_Proc_PDF_Date_no_rel_dat
AS
SELECT TN ,
       Num_of_pages ,
       filename ,
       scanned_by ,
       PDF_Ready ,
       Date_Released,
	owning_institution
  FROM Book 
  WHERE (  ( (PDF_Ready) IS NOT NULL )
          AND ( (Date_Released) IS NULL ) )
  ORDER BY Book.Site,
           Book.PDF_Ready;
 

ALTER TABLE book add date_republished timestamp; 


 

