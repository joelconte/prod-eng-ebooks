

/* Formatted on 6/16/2014 9:43:05 AM (QP5 v5.185.11230.41888) */
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
  ORDER BY book.scanned_by;

  


/* Formatted on 6/16/2014 9:43:05 AM (QP5 v5.185.11230.41888) */
CREATE OR REPLACE VIEW TF_2_READY_TO_TITLE_CHECK
(
   TN,
   NUM_OF_PAGES,
   TIFF_OREM_DRIVE_NAME,
   TIFF_COPY2_DRIVE_NAME,
   PRIORITY_ITEM,
   SITE,
   LOCATION,
   DNP,
   FILES_RECEIVED_BY_OREM,
   SCANNED_BY
)
AS
     SELECT Book.TN,
            Book.Num_of_pages,
            Book.Tiff_Orem_Drive_Name,
            Book.Tiff_Copy2_Drive_Name,
            Book.Priority_Item,
            Book.Site,
            Book.Location,
            Book.DNP,
            Book.Files_Received_by_Orem,
            Book.scanned_by
       FROM Book
      WHERE     Book.DNP IS NULL
            AND book.property_right != 'Denied'
            AND Book.Files_Received_by_Orem IS NOT NULL
            AND Book.IA_Start_Date IS NULL
            AND Book.Date_Released IS NULL
   ORDER BY Book.Files_Received_by_Orem;

 
/* Formatted on 6/16/2014 9:43:05 AM (QP5 v5.185.11230.41888) */
CREATE OR REPLACE VIEW TF_3_TITLE_CHECK_IN_PROCESS
(
   TN,
   PRIORITY_ITEM,
   IMAGE_AUDIT,
   NUM_OF_PAGES,
   IA_START_DATE,
   IA_COMPLETE_DATE,
   SITE,
   SCANNED_BY
)
AS
     SELECT BOOK.TN,
            BOOK.Priority_Item,
            BOOK.Image_Audit,
            BOOK.Num_of_pages,
            BOOK.IA_Start_Date,
            BOOK.IA_Complete_Date,
            BOOK.Site,
            Book.scanned_by
       FROM Book
      WHERE     IA_Start_Date IS NOT NULL
            AND IA_Complete_Date IS NULL
            AND Book.DNP IS NULL
            AND book.property_right != 'Denied'
            AND Book.Date_Released IS NULL
   ORDER BY Book.Priority_Item, Book.Image_Audit;

 

/* Formatted on 6/16/2014 9:43:05 AM (QP5 v5.185.11230.41888) */
CREATE OR REPLACE VIEW TF_4A_OCR
(
   TN,
   TIFF_OREM_DRIVE_NAME,
   NUM_OF_PAGES,
   FILENAME,
   LOCATION,
   PRIORITY_ITEM,
   SITE,
   IMAGE_AUDIT,
   IA_COMPLETE_DATE,
   OCR_BY,
   OCR_COMPLETE_DATE,
   METADATA_COMPLETE,
   DNP,
   SCANNED_BY
)
AS
     SELECT Book.TN,
            Book.Tiff_Orem_Drive_Name,
            Book.Num_of_pages,
            Book.filename,
            Book.Location,
            Book.Priority_Item,
            Book.Site,
            Book.Image_Audit,
            Book.IA_Complete_Date,
            Book.OCR_by,
            Book.OCR_complete_Date,
            Book.Metadata_Complete,
            Book.DNP,
            Book.scanned_by
       FROM Book
      WHERE     Book.ocr_start_Date IS NOT NULL
            AND book.ocr_complete_date IS NULL
            AND Book.DNP IS NULL
            AND book.property_right != 'Denied'
            AND Book.Date_Released IS NULL
   ORDER BY Book.IA_Complete_Date;

 
/* Formatted on 6/16/2014 9:43:05 AM (QP5 v5.185.11230.41888) */
CREATE OR REPLACE VIEW TF_4B_PDF_DOWNLOAD
(
   TN,
   TIFF_OREM_DRIVE_NAME,
   NUM_OF_PAGES,
   FILENAME,
   LOCATION,
   PRIORITY_ITEM,
   SITE,
   IMAGE_AUDIT,
   IA_COMPLETE_DATE,
   OCR_BY,
   OCR_COMPLETE_DATE,
   METADATA_COMPLETE,
   DNP,
   SCANNED_BY
)
AS
     SELECT Book.TN,
            Book.Tiff_Orem_Drive_Name,
            Book.Num_of_pages,
            Book.filename,
            Book.Location,
            Book.Priority_Item,
            Book.Site,
            Book.Image_Audit,
            Book.IA_Complete_Date,
            Book.OCR_by,
            Book.OCR_complete_Date,
            Book.Metadata_Complete,
            Book.DNP,
            Book.scanned_by
       FROM Book
      WHERE     Book.ocr_complete_Date IS NOT NULL
            AND Book.pdf_download_Date IS NULL
            AND Book.pdfreview_Start_date IS NULL
            AND Book.DNP IS NULL
            AND book.property_right != 'Denied'
            AND Book.Date_Released IS NULL
   ORDER BY Book.IA_Complete_Date;
 

/* Formatted on 6/16/2014 9:43:05 AM (QP5 v5.185.11230.41888) */
CREATE OR REPLACE VIEW TF_4_READY_TO_OCR
(
   TN,
   TIFF_OREM_DRIVE_NAME,
   NUM_OF_PAGES,
   FILENAME,
   LOCATION,
   PRIORITY_ITEM,
   SITE,
   IMAGE_AUDIT,
   IA_COMPLETE_DATE,
   OCR_BY,
   OCR_COMPLETE_DATE,
   METADATA_COMPLETE,
   DNP,
   SCANNED_BY
)
AS
     SELECT Book.TN,
            Book.Tiff_Orem_Drive_Name,
            Book.Num_of_pages,
            Book.filename,
            Book.Location,
            Book.Priority_Item,
            Book.Site,
            Book.Image_Audit,
            Book.IA_Complete_Date,
            Book.OCR_by,
            Book.OCR_complete_Date,
            Book.Metadata_Complete,
            Book.DNP,
            Book.scanned_by
       FROM Book
      WHERE     Book.IA_Complete_Date IS NOT NULL
            AND Book.ocr_start_Date IS NULL
            AND book.ocr_complete_date IS NULL
            AND Book.DNP IS NULL
            AND book.property_right != 'Denied'
            AND Book.Date_Released IS NULL
   ORDER BY Book.IA_Complete_Date;

 

/* Formatted on 6/16/2014 9:43:05 AM (QP5 v5.185.11230.41888) */
CREATE OR REPLACE VIEW TF_5_READY_TO_PDFREVIEW
(
   PRIORITY_ITEM,
   SITE,
   TN,
   NUM_OF_PAGES,
   IMAGE_AUDIT,
   FILENAME,
   OCR_COMPLETE_DATE,
   SCANNED_BY
)
AS
     SELECT Book.Priority_Item,
            Book.Site,
            Book.TN,
            Book.Num_of_pages,
            Book.Image_Audit,
            Book.filename,
            Book.OCR_complete_Date,
            Book.scanned_by
       FROM Book
      WHERE     Book.pdf_download_date IS NOT NULL
            AND Book.pdfreview_Start_date IS NULL
            AND Book.DNP IS NULL
            AND book.property_right != 'Denied'
            AND Book.Date_Released IS NULL
   ORDER BY Book.OCR_complete_Date;

 

/* Formatted on 6/16/2014 9:43:05 AM (QP5 v5.185.11230.41888) */
CREATE OR REPLACE VIEW TF_6_PDFREVIEW_IN_PROCESS
(
   TN,
   NUM_OF_PAGES,
   SITE,
   PDFREVIEW_BY,
   PDFREVIEW_START_DATE,
   SCANNED_BY
)
AS
     SELECT BOOK.TN,
            BOOK.Num_of_pages,
            BOOK.Site,
            BOOK.pdfreview_by,
            BOOK.pdfreview_Start_date,
            Book.scanned_by
       FROM Book
      WHERE     pdfreview_Start_date IS NOT NULL
            AND PDF_Ready IS NULL
            AND Book.DNP IS NULL
            AND book.property_right != 'Denied'
            AND Book.Date_Released IS NULL
   ORDER BY Book.pdfreview_by;
 

/* Formatted on 6/16/2014 9:43:05 AM (QP5 v5.185.11230.41888) */
CREATE OR REPLACE VIEW TF_ALLPROBLEMS
(
   TN,
   SCANNED_BY,
   SCAN_COMPLETE_DATE,
   PROBLEM_REASON,
   PROBLEM_TEXT,
   PROBLEM_DATE,
   PROBLEM_INITIALS,
   STATUS,
   TITLE,
   CALL_num,
   DNP,
   SITE,
   PROBLEM_LOCATION,
   SOLUTION_OWNER
)
AS
   SELECT TF_Notes.TN,
          Book.Scanned_by,
          Book.Scan_Complete_Date,
          TF_Notes.Problem_reason,
          TF_Notes.Problem_Text,
          TF_Notes.Problem_Date,
          TF_Notes.Problem_Initials,
          TF_Notes.Status,
          Book.Title,
          Book.Call_num,
          Book.DNP,
          Book.site,
          CASE
             WHEN    
    Book.files_sent_to_orem IS NULL
             THEN
                Book.requesting_location
             ELSE
                Book.Site
          END
             AS Problem_Location,
    tf_notes.solution_owner
     FROM TF_Notes JOIN Book ON TF_Notes.TN = Book.TN
    WHERE (    (    status NOT IN ('Notes', 'Problem Fixed')
                AND status IS NOT NULL)
           AND ( (Book.DNP) IS NULL)
           AND book.property_right != 'Denied');



 

 

/* Formatted on 6/16/2014 9:43:06 AM (QP5 v5.185.11230.41888) */
CREATE OR REPLACE VIEW TFALL_0X_ALL_QUEUES
(
   TN,
   STEP
)
AS
   (SELECT tn, 'A- Ready to Scan' AS Step FROM S_01_Ready_scan
    UNION
    SELECT tn, 'B- Scan in Process' AS Step FROM S_01B_scan_in_prog
    UNION
    SELECT tn, 'C- Ready to Image Audit' AS Step FROM S_02_Ready_image_audit
    UNION
    SELECT tn, 'D- Image Auditing in Process' AS Step
      FROM S_03_image_auditing_in_prog
    UNION
    SELECT tn, 'E- Processed Ready for Orem' AS Step
      FROM S_04_processed_ready_orem
    UNION
    SELECT tn, 'A- Files waiting to be received' AS Step
      FROM TF_1_waiting_for_files
    UNION
    SELECT tn, 'B- Title Check' AS Step FROM TF_2_Ready_to_Title_Check
    UNION
    SELECT tn, 'C- Title Check in Process' AS Step
      FROM TF_3_Title_Check_In_Process
    UNION
    SELECT tn, 'D- OCR Transfer Ready' AS Step FROM TF_4_Ready_to_OCR
    UNION
    SELECT tn, 'E- OCR Transfer in Process' AS Step FROM TF_4a_OCR
    UNION
    SELECT tn, 'F- PDF Download' AS Step FROM tf_4b_pdf_download
    UNION
    SELECT tn, 'G- PDF Review' AS Step FROM TF_5_Ready_to_pdfreview
    UNION
    SELECT tn, 'H- PDF Review in Process' AS Step
      FROM TF_6_pdfreview_In_Process);
