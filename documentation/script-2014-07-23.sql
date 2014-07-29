dummy-utf8;

sET DEFINE OFF;
set escape on;
alter session set CURRENT_SCHEMA = bookscan;
 
 
 
 
 
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
       Book.Files_sent_to_Orem ,
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

