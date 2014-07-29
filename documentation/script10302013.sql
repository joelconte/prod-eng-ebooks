sET DEFINE OFF;
set escape on;
alter session set CURRENT_SCHEMA = bookscan;

ALTER TABLE book add pdf_download_by varchar2(255 CHAR); 
ALTER TABLE book add pdf_download_date TIMESTAMP; 

 
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
       Book.Imported_by  ,
       Book.Imported_Date ,
       Book.Metadata_Complete ,
       Book.DNP, 
	Book.scanned_by  
  FROM Book 
         LEFT JOIN TF_Problems 
          ON Book.TN = TF_Problems.TN
WHERE (   ( (Book.Imported_Date) IS not NULL )
         And ( (Book.pdf_download_Date) IS NULL )
   AND ( (Book.Kofax_Start_date) IS  NULL )
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
       Book.Imported_Date ,
	Book.scanned_by  
  FROM Book 
         LEFT JOIN TF_Problems 
          ON Book.TN = TF_Problems.TN
  WHERE (  ( (Book.pdf_download_date) IS NOT NULL )
          AND ( (Book.Kofax_Start_date) IS NULL )
          AND ( (Book.DNP) IS NULL )
          AND ( (TF_Problems.TN) IS NULL ) )
	and site = 'Orem Digital Processing Center'
  ORDER BY Book.Imported_Date;

commit;