dummy-utf8;

sET DEFINE OFF;
set escape on;
alter session set CURRENT_SCHEMA = bookscan;
 
alter TABLE TF_Notes add  solution_owner varchar(255);

ALTER TABLE tf_notes add constraint owner foreign key(solution_owner) references site(ID) ;
 
DROP VIEW TF_AllProblems;
CREATE OR REPLACE VIEW TF_AllProblems
AS
SELECT TF_Notes.TN ,
       Book.Scanned_by ,
       Book.Scan_Complete_Date ,
	TF_Notes.Problem_reason ,
       TF_Notes.Problem_Text ,
       TF_Notes.Problem_Date ,
       TF_Notes.Problem_Initials ,
       TF_Notes.Status ,
       Book.Title ,
       Book.Call_# ,
       Book.DNP, 
       Case WHEN Book.files_sent_to_orem = '' OR Book.files_sent_to_orem IS NULL 
     		THEN Book.requesting_location 
     		ELSE Book.Site 
	END as Problem_Location,
	tf_notes.solution_owner
  FROM TF_Notes 
         JOIN Book 
          ON TF_Notes.TN = Book.TN
  WHERE ( (status not in ('Notes', 'Problem Fixed') and status is not null) 
          AND ( (Book.DNP) IS NULL ) 
and book.property_right != 'Denied' );

 
