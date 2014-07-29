dummy-utf8;

sET DEFINE OFF;
set escape on;
alter session set CURRENT_SCHEMA = bookscan;
 
 
 
 

 
DROP VIEW TF_Problems;
CREATE OR REPLACE VIEW TF_Problems
AS
SELECT TF_Notes.TN ,
       Book.Scanned_by ,
       Book.Scan_Complete_Date ,
	TF_Notes.problem_reason, 
       TF_Notes.Problem_Text ,
       TF_Notes.Problem_Date ,
       TF_Notes.Problem_Initials ,
       TF_Notes.Status ,
       Book.Title ,
       Book.Call_# ,
       Book.DNP 
  FROM TF_Notes 
         JOIN Book 
          ON TF_Notes.TN = Book.TN
  WHERE ( (status not in ('Notes', 'Problem Fixed') and status is not null) 
          AND ( (Book.DNP) IS NULL ) )  
and (Book.Files_Sent_to_Orem Is not Null 
 or  files_received_by_orem is not null )
and book.property_right != 'Denied' ;
 
 