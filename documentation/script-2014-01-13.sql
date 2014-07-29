dummy-utf8;

sET DEFINE OFF;
set escape on;
alter session set CURRENT_SCHEMA = bookscan;
  

  

DROP VIEW Stats_Scanned;
CREATE OR REPLACE VIEW Stats_Scanned
AS SELECT Count(Book.TN) AS CountOfTN, Sum(Book.scan_num_of_pages) AS num_of_pages, to_char( Scan_Complete_Date, 'Month') AS Month,  to_char( Scan_Complete_Date, 'yyyy') as Year, Book.scanned_by as scanned_by, (trim(to_char(Scan_Complete_Date, 'Month')) || to_char(Scan_Complete_Date, 'yyyy') || scanned_by) AS ScannedByAndDate 
FROM Book
WHERE (((Book.Scan_Complete_Date) Is Not Null))
GROUP BY to_char( Scan_Complete_Date, 'Month'), to_char( Scan_Complete_Date, 'yyyy'), Book.scanned_by
order by to_char( Scan_Complete_Date, 'Month') , to_char( Scan_Complete_Date, 'yyyy'), Book.scanned_by;


DROP VIEW Stats_Years;
CREATE OR REPLACE VIEW Stats_Years
AS
SELECT to_char(scan_start_date, 'yyyy') AS Year
FROM Book
GROUP BY to_char(scan_start_date, 'yyyy')
HAVING  to_char(scan_start_date, 'yyyy') Is Not Null
union
SELECT to_char(Date_Loaded, 'yyyy') AS Year
FROM Book
GROUP BY to_char(Date_Loaded, 'yyyy')
HAVING  to_char(Date_Loaded, 'yyyy') Is Not Null;