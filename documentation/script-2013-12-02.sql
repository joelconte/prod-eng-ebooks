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
       Book.Files_Received_by_Orem 
  FROM Book 
  WHERE Book.DNP IS NULL
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
   AND Book.Kofax_Start_date IS NULL 
          AND Book.DNP IS NULL 
        AND Book.Date_Released Is Null 
	and site = 'Orem Digital Processing Center'
ORDER BY Book.IA_Complete_Date;


DROP VIEW TF_5_Ready_to_Kofax;
CREATE OR REPLACE VIEW TF_5_Ready_to_Kofax
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
          AND Book.Kofax_Start_date IS NULL 
          AND Book.DNP IS NULL 
	and site = 'Orem Digital Processing Center'
        AND Book.Date_Released Is Null 
  ORDER BY Book. OCR_complete_Date;




DROP VIEW TF_6_Kofax_In_Process;
CREATE OR REPLACE VIEW TF_6_Kofax_In_Process
AS
SELECT BOOK.TN ,
        BOOK.Num_of_pages ,
        BOOK.Site ,
        BOOK.Kofaxed_by ,
        BOOK.Kofax_Start_date ,
	Book.scanned_by  
  FROM Book 
  WHERE Kofax_Start_date IS NOT NULL 
          AND PDF_Ready IS NULL 
   AND Book.DNP IS NULL 
        AND Book.Date_Released Is Null 
	and site = 'Orem Digital Processing Center'
  ORDER BY Book.Kofaxed_by;
