dummy-utf8;

sET DEFINE OFF;
set escape on;
alter session set CURRENT_SCHEMA = bookscan;
 

 
alter table BOOK rename column KOFAXED_BY to pdfreview_by;
alter table BOOK rename column KOFAX_start_date to pdfreview_start_date;
alter table iaBOOKmetadata rename column KOFAXED_BY to pdfreview_by;
alter table iaBOOKmetadata  rename column KOFAX_start_date to pdfreview_start_date;


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
        AND Book.Date_Released Is Null 
	and site = 'Orem Digital Processing Center'
ORDER BY Book.IA_Complete_Date;

DROP VIEW TF_5_Ready_to_kofax; 
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
	and site = 'Orem Digital Processing Center'
        AND Book.Date_Released Is Null 
  ORDER BY Book. OCR_complete_Date;




DROP VIEW TF_6_kofax_In_Process;
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
        AND Book.Date_Released Is Null 
	and site = 'Orem Digital Processing Center'
  ORDER BY Book.pdfreview_by;





DROP view tf_0x_All_queues; 
create or replace view  tf_0x_All_queues as (
select tn, 'A- Files waiting to be received' as Step from TF_1_waiting_for_files 
union select tn, 'B- Title Check'  as Step from TF_2_Ready_to_Title_Check
union select tn, 'C- Title Check in Process'  as Step from TF_3_Title_Check_In_Process
union select tn, 'D- OCR Transfer Ready'  as Step from TF_4_Ready_to_OCR
union select tn, 'E- OCR Transfer in Process'  as Step from TF_4a_OCR
union select tn, 'F- PDF Download'  as Step from tf_4b_pdf_download
union select tn, 'G- PDF Review'  as Step from TF_5_Ready_to_pdfreview
union select tn, 'H- PDF Review in Process'  as Step from TF_6_pdfreview_In_Process); 
 

DROP view TFALL_0x_All_queues; 
create or replace view  TFALL_0x_All_queues as (
select tn, 'A- Ready to Scan' as Step from S_01_Ready_scan 
union select tn, 'B- Scan in Process'  as Step from S_01B_scan_in_prog
union select tn, 'C- Ready to Image Audit'  as Step from S_02_Ready_image_audit
union select tn, 'D- Image Auditing in Process'  as Step from S_03_image_auditing_in_prog
union select tn, 'E- Processed Ready for Orem'  as Step from S_04_processed_ready_orem
union select tn, 'A- Files waiting to be received' as Step from TF_1_waiting_for_files 
union select tn, 'B- Title Check'  as Step from TF_2_Ready_to_Title_Check
union select tn, 'C- Title Check in Process'  as Step from TF_3_Title_Check_In_Process
union select tn, 'D- OCR Transfer Ready'  as Step from TF_4_Ready_to_OCR
union select tn, 'E- OCR Transfer in Process'  as Step from TF_4a_OCR
union select tn, 'F- PDF Download'  as Step from tf_4b_pdf_download
union select tn, 'G- PDF Review'  as Step from TF_5_Ready_to_pdfreview
union select tn, 'H- PDF Review in Process'  as Step from TF_6_pdfreview_In_Process); 
 

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
       Book.pdfreview_by ,
       Book.pdfreview_Start_date ,
       Book.PDF_Ready ,
       Book.PDF_Sent_to_Load 
  FROM TF_Released_entry 
         LEFT JOIN Book 
          ON TF_Released_entry.TN = Book.TN;




DROP VIEW Stats_Scanned;
CREATE OR REPLACE VIEW Stats_Scanned
AS SELECT Count(Book.TN) AS CountOfTN, Sum(Book.scan_num_of_pages) AS num_of_pages, to_char( Scan_ia_Complete_Date, 'Month') AS Month,  to_char( Scan_ia_Complete_Date, 'yyyy') as Year, Book.scanned_by as scanned_by, (trim(to_char(Scan_ia_Complete_Date, 'Month')) || to_char(Scan_ia_Complete_Date, 'yyyy') || scanned_by) AS ScannedByAndDate 
FROM Book
WHERE (((Book.Scan_ia_Complete_Date) Is Not Null))
GROUP BY to_char( Scan_ia_Complete_Date, 'Month'), to_char( Scan_ia_Complete_Date, 'yyyy'), Book.scanned_by
order by to_char( Scan_ia_Complete_Date, 'Month') , to_char( Scan_ia_Complete_Date, 'yyyy'), Book.scanned_by;

 