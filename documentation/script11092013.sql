dummy-utf8;

sET DEFINE OFF;
set escape on;
alter session set CURRENT_SCHEMA = bookscan;

ALTER TABLE book  add  ocr_start_Date TIMESTAMP;
alter table BOOK rename column IMPORTED_BY to OCR_by;
alter table BOOK rename column IMPORTED_date to OCR_complete_date;
 
alter table iaBOOKmetadata rename column IMPORTED_BY to OCR_by;
alter table iaBOOKmetadata rename column IMPORTED_date to OCR_complete_date;

DROP VIEW TF_4_Ready_to_Import_to_Kofax;
DROP VIEW TF_4_Ready_to_OCR;
CREATE OR REPLACE VIEW TF_4_Ready_to_OCR
AS 
SELECT Book.TN ,
       Book.Tiff_Orem_Drive_Name ,
       Book.Num_of_pages ,
       Book.Batch_Class ,
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
         LEFT JOIN TF_Problems 
          ON Book.TN = TF_Problems.TN
  WHERE (   ( (Book.IA_Complete_Date) IS NOT NULL )
          AND Book.ocr_start_Date IS NULL 
	  And book.ocr_complete_date is null 
          AND ( (Book.DNP) IS NULL )
          AND ( (TF_Problems.TN) IS NULL ) )
	and site = 'Orem Digital Processing Center'
  ORDER BY Book.IA_Complete_Date;

DROP VIEW TF_4a_OCR;
CREATE OR REPLACE VIEW TF_4a_OCR
AS 
SELECT Book.TN ,
       Book.Tiff_Orem_Drive_Name ,
       Book.Num_of_pages ,
       Book.Batch_Class ,
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
         LEFT JOIN TF_Problems 
          ON Book.TN = TF_Problems.TN
WHERE (  ( (Book.ocr_start_Date) IS not NULL )
 	And book.ocr_complete_date is null 
          AND ( (Book.DNP) IS NULL )
          AND ( (TF_Problems.TN) IS NULL ) )
	and site = 'Orem Digital Processing Center'
  ORDER BY Book.IA_Complete_Date;


DROP VIEW tf_4b_pdf_download;
CREATE OR REPLACE VIEW tf_4b_pdf_download
AS 
SELECT Book.TN ,
       Book.Tiff_Orem_Drive_Name ,
       Book.Num_of_pages ,
       Book.Batch_Class ,
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
         LEFT JOIN TF_Problems 
          ON Book.TN = TF_Problems.TN
WHERE (   ( (Book.ocr_complete_Date) IS not NULL )
         And ( (Book.pdf_download_Date) IS NULL )
   AND ( (Book.Kofax_Start_date) IS NULL )
          AND ( (Book.DNP) IS NULL )
          AND ( (TF_Problems.TN) IS NULL ) )
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
       Book.Batch_Class ,
       Book.OCR_complete_Date ,
	Book.scanned_by  
  FROM Book 
         LEFT JOIN TF_Problems 
          ON Book.TN = TF_Problems.TN
  WHERE (  ( (Book.pdf_download_date) IS NOT NULL )
          AND ( (Book.Kofax_Start_date) IS NULL )
          AND ( (Book.DNP) IS NULL )
          AND ( (TF_Problems.TN) IS NULL ) )
	and site = 'Orem Digital Processing Center'
  ORDER BY Book. OCR_complete_Date;



DROP VIEW TF_Release_entry_With_date_alr;
CREATE OR REPLACE VIEW TF_Release_entry_With_date_alr
AS
SELECT TF_Released_entry.TN ,
       Book.Date_Released ,
       Book.Site ,
       Book.Num_of_pages ,
       Book.Scanned_by ,
       Book.Scan_Complete_Date ,
       Book.Files_Received_by_Orem ,
       Book.Image_Audit ,
       Book.IA_Start_Date ,
       Book.IA_Complete_Date ,
       Book.ocr_by ,
       Book.ocr_complete_Date ,
       Book.Kofaxed_by ,
       Book.Kofax_Start_date ,
       Book.PDF_Ready ,
       Book.PDF_Sent_to_Load 
  FROM TF_Released_entry 
         LEFT JOIN Book 
          ON TF_Released_entry.TN = Book.TN;
 