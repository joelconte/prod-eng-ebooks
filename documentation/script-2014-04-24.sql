dummy-utf8;

sET DEFINE OFF;
set escape on;
alter session set CURRENT_SCHEMA = bookscan;
 
 


DROP view S_TF_Problems;  
create or replace view  S_TF_Problems as 
SELECT TF_Notes.TN, Book.Call_#, TF_Notes.Problem_reason, TF_Notes.Problem_Text, TF_Notes.Problem_Date, TF_Notes.Problem_Initials, TF_Notes.Status, Book.DNP, Book.requesting_location, Book.Scanned_by
FROM TF_Notes INNER JOIN Book ON TF_Notes.TN = Book.TN
WHERE (  (status not in ('Notes', 'Problem Fixed') and status is not null) AND ((Book.DNP) Is Null) and book.property_right != 'Denied'  AND ((Book.DNP_deleted_off_line) Is Null)) and 
Book.Files_Sent_to_Orem Is Null 
AND files_received_by_orem is null 
And Book.Date_Released Is Null 
And Book.Date_loaded Is Null;

DROP view S_TF_Solution_found; 
create or replace view  S_TF_Solution_found as 
SELECT TF_Notes.TN, Book.Call_#,   TF_Notes.Solution_Text, TF_Notes.Solution_Date, TF_Notes.Solution_initials, TF_Notes.Status,  TF_Notes.Problem_reason , TF_Notes.Problem_Text , Book.requesting_location,  Book.Scanned_by
FROM TF_Notes INNER JOIN Book ON TF_Notes.TN=Book.TN
WHERE ((TF_Notes.Status='Solution Found') AND ((Book.DNP_Deleted_off_line) Is Null) AND ((Book.DNP) Is Null) and book.property_right != 'Denied' );

  
 DROP view S_01_Ready_scan; 
create or replace view  S_01_Ready_scan as 
SELECT Book.TN, Book.partner_Lib_Call_#, Book.record_number, Book.title, Book.scan_num_of_pages, Book.scan_machine_id, Book.Scan_start_Date, Book.scan_Image_Auditor, Book.scan_IA_start_Date, Book.Files_Sent_to_Orem, Book.requesting_location, Book.scanned_by
FROM Book  
WHERE Book.Scan_start_Date Is  Null 
 And Book.Date_Released Is Null 
 And Book.Files_Sent_to_Orem Is Null
 and book.files_received_by_orem is null
 And Book.Date_loaded Is Null 
 AND Book.DNP Is Null 
and book.property_right != 'Denied' 
ORDER BY Book.Scan_start_Date; 


DROP view S_01B_scan_in_prog; 
create or replace view  S_01B_scan_in_prog as 
SELECT Book.TN,  Book.partner_Lib_Call_#, Book.record_number, Book.title, Book.scan_num_of_pages, Book.scan_machine_id, Book.Scan_start_Date, Book.scan_Image_Auditor, Book.scan_IA_start_Date, Book.Files_Sent_to_Orem,  Book.requesting_location, Book.scanned_by
FROM Book 
WHERE  Book.Scan_start_Date Is not  Null
 And Book.Scan_complete_Date Is Null
 And Book.Date_Released Is Null 
 And Book.Files_Sent_to_Orem Is Null
 and files_received_by_orem is null 
 and Book.Date_loaded Is Null  
AND Book.DNP Is Null 
and book.property_right != 'Denied' 
ORDER BY Book.Scan_start_Date;


DROP view S_02_Ready_image_audit; 
create or replace view  S_02_Ready_image_audit as 
SELECT Book.TN,  Book.partner_Lib_Call_#, Book.record_number, Book.title, Book.scan_num_of_pages, Book.scan_machine_id, Book.Scan_start_Date, Book.scan_Image_Auditor, Book.scan_IA_start_Date, Book.Files_Sent_to_Orem,  Book.requesting_location, Book.scanned_by
FROM Book 
WHERE Book.Scan_complete_Date Is Not Null
 And Book.Scan_IA_Start_Date Is Null
 And Book.Files_Sent_to_Orem Is Null
 and files_received_by_orem is null 
 And  Book.Date_Released Is Null  
 And   Book.Date_loaded Is Null 
 AND Book.DNP Is Null 
and book.property_right != 'Denied' 
ORDER BY Book.Scan_start_Date;



DROP view S_03_image_auditing_in_prog;
create or replace view  S_03_image_auditing_in_prog as 
SELECT Book.TN, Book.partner_Lib_Call_#, Book.record_number, Book.title,  Book.scan_Image_Auditor, Book.scan_num_of_pages, Book.scan_IA_start_Date, Book.Site, Book.scan_Machine_id,  Book.requesting_location, Book.scanned_by 
FROM Book
WHERE  Book.scan_Ia_start_date Is not Null  and  Book.scan_Ia_complete_date Is Null And 
Book.Files_Sent_to_Orem Is Null and files_received_by_orem is null And  Book.Date_Released Is Null And   Book.Date_loaded Is Null AND Book.DNP Is Null 
and book.property_right != 'Denied'  ;



DROP view S_04_processed_ready_orem; 
create or replace view  S_04_processed_ready_orem as 
SELECT Book.TN, Book.partner_Lib_Call_#, Book.record_number, Book.title,  Book.scan_num_of_pages, Book.Scan_IA_Start_Date, Book.Remarks_from_Scan_Center , Book.Files_Sent_to_Orem,  Book.requesting_location, Book.scanned_by  
FROM Book 
WHERE Book.Scan_IA_complete_Date Is Not Null AND Book.Files_Sent_to_Orem Is Null
 AND files_received_by_orem is null 
 And Book.Date_Released Is Null  
 And Book.Date_loaded Is Null 
 AND Book.DNP Is Null 
and book.property_right != 'Denied' 
ORDER BY Book.Scan_IA_Start_Date, Book.Site DESC;







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
	END as Problem_Location
  FROM TF_Notes 
         JOIN Book 
          ON TF_Notes.TN = Book.TN
  WHERE ( (status not in ('Notes', 'Problem Fixed') and status is not null) 
          AND ( (Book.DNP) IS NULL ) 
and book.property_right != 'Denied' );
 

 
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
and Book.Files_Sent_to_Orem Is not Null 
and book.property_right != 'Denied' ;
 


 
DROP VIEW TF_1_waiting_for_files;
CREATE OR REPLACE VIEW TF_1_waiting_for_files
AS
SELECT Book.TN ,
       Book.Num_of_pages ,
       Book.Tiff_Orem_Drive_Name ,
       Book.Tiff_Copy2_Drive_Name ,
       Book.Priority_Item ,
	book.scanned_by,
       Book.Site ,
       Book.Location ,
       Book.DNP ,
       Book.Files_Received_by_Orem 
  FROM Book 
  WHERE Book.DNP IS NULL
and book.property_right != 'Denied' 
          AND Book.Files_Received_by_Orem IS NULL 
	  AND Book.Files_sent_to_orem IS NOT NULL 
          AND Book.IA_Start_Date IS NULL 
          AND Book.Date_Released Is Null 
	and site = 'Orem Digital Processing Center'
  ORDER BY book.scanned_by;




DROP VIEW TF_2_Ready_to_Title_Check;
CREATE OR REPLACE VIEW TF_2_Ready_to_Title_Check
AS
SELECT Book.TN ,
       Book.Num_of_pages ,
       Book.Tiff_Orem_Drive_Name ,
       Book.Tiff_Copy2_Drive_Name ,
       Book.Priority_Item ,
       Book.Site ,
       Book.Location ,
       Book.DNP ,
       Book.Files_Received_by_Orem,
	Book.scanned_by   
  FROM Book 
  WHERE Book.DNP IS NULL 
and book.property_right != 'Denied' 
          AND Book.Files_Received_by_Orem IS NOT NULL 
          AND Book.IA_Start_Date IS NULL 
	  AND Book.Date_Released Is Null 
	and site = 'Orem Digital Processing Center'
  ORDER BY Book.Files_Received_by_Orem;

DROP VIEW TF_3_Title_Check_In_Process;
CREATE OR REPLACE VIEW TF_3_Title_Check_In_Process
AS
SELECT BOOK.TN ,
        BOOK.Priority_Item ,
        BOOK.Image_Audit ,
        BOOK.Num_of_pages ,
        BOOK.IA_Start_Date ,
        BOOK.IA_Complete_Date ,
        BOOK.Site, 
	Book.scanned_by   
  FROM Book 
  WHERE IA_Start_Date IS NOT NULL
        AND IA_Complete_Date IS NULL 
	and site = 'Orem Digital Processing Center'
        AND Book.DNP IS NULL 
and book.property_right != 'Denied' 
        AND Book.Date_Released Is Null 
  ORDER BY Book.Priority_Item,
           Book.Image_Audit;

DROP VIEW TF_4_Ready_to_OCR;
CREATE OR REPLACE VIEW TF_4_Ready_to_OCR
AS 
SELECT Book.TN ,
       Book.Tiff_Orem_Drive_Name ,
       Book.Num_of_pages ,
       Book.filename,
       Book.Location ,
       Book.Priority_Item ,
       Book.Site ,
       Book.Image_Audit ,
       Book.IA_Complete_Date ,
       Book.OCR_by  ,
       Book.OCR_complete_Date ,
       Book.Metadata_Complete ,
       Book.DNP, 
       Book.scanned_by  
  FROM Book 
  WHERE Book.IA_Complete_Date IS NOT NULL 
          AND Book.ocr_start_Date IS NULL 
	  And book.ocr_complete_date is null 
          AND Book.DNP IS NULL 
and book.property_right != 'Denied' 
        AND Book.Date_Released Is Null 
	and site = 'Orem Digital Processing Center'
  ORDER BY Book.IA_Complete_Date;


DROP VIEW TF_4a_OCR;
CREATE OR REPLACE VIEW TF_4a_OCR
AS 
SELECT Book.TN ,
       Book.Tiff_Orem_Drive_Name ,
       Book.Num_of_pages ,
       Book.filename ,
       Book.Location ,
       Book.Priority_Item ,
       Book.Site ,
       Book.Image_Audit ,
       Book.IA_Complete_Date ,
       Book.OCR_by  ,
       Book.OCR_complete_Date ,
       Book.Metadata_Complete ,
       Book.DNP, 
	Book.scanned_by  
  FROM Book 
WHERE Book.ocr_start_Date IS not NULL  
 	And book.ocr_complete_date is null 
        AND Book.DNP IS NULL
and book.property_right != 'Denied' 
        AND Book.Date_Released Is Null 
	and site = 'Orem Digital Processing Center'
  ORDER BY Book.IA_Complete_Date;


DROP VIEW tf_4b_pdf_download;
CREATE OR REPLACE VIEW tf_4b_pdf_download
AS 
SELECT Book.TN ,
       Book.Tiff_Orem_Drive_Name ,
       Book.Num_of_pages ,
       Book.filename ,
       Book.Location ,
       Book.Priority_Item ,
       Book.Site ,
       Book.Image_Audit ,
       Book.IA_Complete_Date ,
       Book.OCR_by  ,
       Book.OCR_complete_Date ,
       Book.Metadata_Complete ,
       Book.DNP, 
	Book.scanned_by  
  FROM Book 
WHERE Book.ocr_complete_Date IS not NULL 
         And Book.pdf_download_Date IS NULL 
   AND Book.pdfreview_Start_date IS NULL 
          AND Book.DNP IS NULL 
and book.property_right != 'Denied' 
        AND Book.Date_Released Is Null 
	and site = 'Orem Digital Processing Center'
ORDER BY Book.IA_Complete_Date;


DROP VIEW TF_5_Ready_to_pdfreview;
CREATE OR REPLACE VIEW TF_5_Ready_to_pdfreview
AS
SELECT Book.Priority_Item ,
       Book.Site ,
       Book.TN ,
       Book.Num_of_pages ,
       Book.Image_Audit ,
       Book.filename ,
       Book.OCR_complete_Date ,
	Book.scanned_by  
  FROM Book 
  WHERE Book.pdf_download_date IS NOT NULL
          AND Book.pdfreview_Start_date IS NULL 
          AND Book.DNP IS NULL 
and book.property_right != 'Denied' 
	and site = 'Orem Digital Processing Center'
        AND Book.Date_Released Is Null 
  ORDER BY Book. OCR_complete_Date;




DROP VIEW TF_6_pdfreview_In_Process;
CREATE OR REPLACE VIEW TF_6_pdfreview_In_Process
AS
SELECT BOOK.TN ,
        BOOK.Num_of_pages ,
        BOOK.Site ,
        BOOK.pdfreview_by ,
        BOOK.pdfreview_Start_date ,
	Book.scanned_by  
  FROM Book 
  WHERE pdfreview_Start_date IS NOT NULL 
          AND PDF_Ready IS NULL 
   AND Book.DNP IS NULL 
and book.property_right != 'Denied' 
        AND Book.Date_Released Is Null 
	and site = 'Orem Digital Processing Center'
  ORDER BY Book.pdfreview_by;
