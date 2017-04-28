ALTER TABLE book ADD scan_image_auditor2 character varying(255);
ALTER TABLE book ADD scan_ia_start_date2 timestamp(6) without time zone;
ALTER TABLE book ADD scan_ia_complete_date2 timestamp(6) without time zone;


drop view s_0x_all_queues;
drop VIEW tfall_0x_all_queues;
drop VIEW s_02_ready_image_audit;
drop VIEW s_03_image_auditing_in_prog;
drop VIEW s_04_processed_ready_orem;

CREATE OR REPLACE VIEW s_02_ready_image_audit AS 
 SELECT book.tn,
    book.partner_lib_call_num,
    book.record_number,
    book.title,
    book.scan_num_of_pages,
    book.scan_machine_id,
    book.scan_start_date,
    book.scan_image_auditor,
    book.scan_ia_start_date,
    book.files_sent_to_orem,
    book.requesting_location,
    book.scanned_by
   FROM book
  WHERE book.scan_complete_date IS NOT NULL AND book.scan_ia_complete_date IS NULL AND book.files_sent_to_orem IS NULL AND book.files_received_by_orem IS NULL AND book.date_released IS NULL AND book.date_loaded IS NULL AND book.dnp IS NULL AND book.property_right::text <> 'Denied'::text
  ORDER BY book.scan_start_date;


CREATE OR REPLACE VIEW s_03_ready_image_audit2 AS 
 SELECT book.tn,
    book.partner_lib_call_num,
    book.record_number,
    book.title,
    book.scan_image_auditor,
    book.scan_num_of_pages,
    book.scan_ia_start_date,
    book.scan_ia_complete_date,
    book.scan_image_auditor2,
    book.scan_ia_start_date2,
    book.scan_ia_complete_date2,
    book.site,
    book.scan_machine_id,
    book.requesting_location,
    book.scanned_by
   FROM book
  WHERE book.scan_ia_complete_date IS NOT NULL AND book.scan_ia_complete_date2 IS NULL AND book.files_sent_to_orem IS NULL AND book.files_received_by_orem IS NULL AND book.date_released IS NULL AND book.date_loaded IS NULL AND book.dnp IS NULL AND book.property_right::text <> 'Denied'::text;


 
CREATE OR REPLACE VIEW s_04_processed_ready_orem AS 
 SELECT book.tn,
    book.partner_lib_call_num,
    book.record_number,
    book.title,
    book.scan_num_of_pages,
    book.scan_ia_start_date,
    book.scan_ia_complete_date2,
    book.remarks_from_scan_center,
    book.files_sent_to_orem,
    book.requesting_location,
    book.scanned_by
   FROM book
  WHERE book.scan_ia_complete_date IS NOT NULL AND book.scan_ia_complete_date2 IS NOT NULL AND book.files_sent_to_orem IS NULL AND book.files_received_by_orem IS NULL AND book.date_released IS NULL AND book.date_loaded IS NULL AND book.dnp IS NULL AND book.property_right::text <> 'Denied'::text
  ORDER BY book.scan_ia_start_date, book.site DESC;
 

CREATE OR REPLACE VIEW s_0x_all_queues AS 
 SELECT s_01_ready_scan.tn,
    'A- Ready to Scan'::text AS step
   FROM s_01_ready_scan
UNION
 SELECT s_01b_scan_in_prog.tn,
    'B- Scan in Process'::text AS step
   FROM s_01b_scan_in_prog
UNION
 SELECT s_02_ready_image_audit.tn,
    'C- Image Audit'::text AS step
   FROM s_02_ready_image_audit
UNION
 SELECT s_03_ready_image_audit2.tn,
    'D- Image Audit 2'::text AS step
   FROM s_03_ready_image_audit2
UNION
 SELECT s_04_processed_ready_orem.tn,
    'E- Ready for Processing'::text AS step
   FROM s_04_processed_ready_orem;
 


CREATE OR REPLACE VIEW tfall_0x_all_queues AS 
 SELECT s_01_ready_scan.tn,
    'A- Ready to Scan'::text AS step
   FROM s_01_ready_scan
UNION
 SELECT s_01b_scan_in_prog.tn,
    'B- Scan in Process'::text AS step
   FROM s_01b_scan_in_prog
UNION
 SELECT s_02_ready_image_audit.tn,
    'C- Image Audit'::text AS step
   FROM s_02_ready_image_audit
UNION
 SELECT s_03_ready_image_audit2.tn,
    'D- Image Audit 2'::text AS step
   FROM s_03_ready_image_audit2
UNION
 SELECT s_04_processed_ready_orem.tn,
    'E- Ready for Processing'::text AS step
   FROM s_04_processed_ready_orem
UNION
 SELECT tf_1_waiting_for_files.tn,
    'A- Files waiting to be received'::text AS step
   FROM tf_1_waiting_for_files
UNION
 SELECT tf_2_ready_to_title_check.tn,
    'B- Title Check'::text AS step
   FROM tf_2_ready_to_title_check
UNION
 SELECT tf_3_title_check_in_process.tn,
    'C- Title Check in Process'::text AS step
   FROM tf_3_title_check_in_process
UNION
 SELECT tf_4_ready_to_ocr.tn,
    'D- OCR Transfer Ready'::text AS step
   FROM tf_4_ready_to_ocr
UNION
 SELECT tf_4a_ocr.tn,
    'E- OCR Transfer in Process'::text AS step
   FROM tf_4a_ocr
UNION
 SELECT tf_4b_pdf_download.tn,
    'F- PDF Download'::text AS step
   FROM tf_4b_pdf_download
UNION
 SELECT tf_5_ready_to_pdfreview.tn,
    'G- PDF Review'::text AS step
   FROM tf_5_ready_to_pdfreview
UNION
 SELECT tf_6_pdfreview_in_process.tn,
    'H- PDF Review in Process'::text AS step
   FROM tf_6_pdfreview_in_process;
 