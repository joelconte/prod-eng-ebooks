package org.familysearch.prodeng.model;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/*This class is needed since Spring does not yet support timestamp format in binding*/
public class SqlTimestampPropertyEditor extends PropertyEditorSupport {

	public static final String DEFAULT_BATCH_PATTERN = "yyyy-MM-dd"; 
	public static final String BATCH_PATTERN_24HR = "mm/dd/yyyy HH:mm";
	public static final String BATCH_PATTERN2 = "yyyy-MM-dd HH:mm"; 
	
	private final SimpleDateFormat sdf, sdf24, sdf2;
 
	public SqlTimestampPropertyEditor() {
		this.sdf = new SimpleDateFormat(
				SqlTimestampPropertyEditor.DEFAULT_BATCH_PATTERN);
		this.sdf24 = new SimpleDateFormat(
				SqlTimestampPropertyEditor.BATCH_PATTERN_24HR);
		this.sdf2 = new SimpleDateFormat(
				SqlTimestampPropertyEditor.BATCH_PATTERN2);
	}

	/**
	 * Uses the given pattern for dateparsing, see {@link SimpleDateFormat} for
	 * allowed patterns.
	 * 
	 * @param pattern
	 *            the pattern describing the date and time format
	 * @see SimpleDateFormat#SimpleDateFormat(String)
	 */
	public SqlTimestampPropertyEditor(String pattern) {
		this.sdf = new SimpleDateFormat(pattern);
		this.sdf24 = new SimpleDateFormat(BATCH_PATTERN_24HR); 
		this.sdf2 = new SimpleDateFormat(BATCH_PATTERN2);
	}

	/**
	 * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {

		SimpleDateFormat useThisSDF = this.sdf;
		String ampm = "";
		if(text != null && text.length()>2)
			ampm = text.substring(text.length()-2, text.length());
		
		try {
			if(text.equals(""))
				setValue(null);
			else {
				if(text.length() < 12) {
					//no time added
					text += " 12:00 am";
					ampm = text.substring(text.length()-2, text.length());
				}

				if(ampm.compareToIgnoreCase("am") != 0 && ampm.compareToIgnoreCase("pm") != 0) {
					//24 hour time
					useThisSDF = sdf24;
				}else if(text.substring(3, 4).equals("-")) {
					useThisSDF = sdf2;
				}
		
				setValue(new Timestamp(useThisSDF.parse(text).getTime()));
			}
		} catch (ParseException ex) {
			throw new IllegalArgumentException("Could not parse date: "
					+ ex.getMessage(), ex);
		}
	}

	//same logic as setAsText, but helper method that just returns the Timestamp object
	public Timestamp textToTimestamp(String text) throws IllegalArgumentException {
	
		SimpleDateFormat useThisSDF = this.sdf;
		String ampm = "";
		if(text != null && text.length()>2)
			ampm = text.substring(text.length()-2, text.length());
		
		try {
			if(text.equals(""))
				return null;
			else {
				if(text.length() < 12) {
					//no time added
					text += " 12:00 am";
					ampm = text.substring(text.length()-2, text.length());
				}

				if (ampm.compareToIgnoreCase("am") != 0
						&& ampm.compareToIgnoreCase("pm") != 0) {
					// 24 hour time
					useThisSDF = sdf24;
				} 
				if (text.substring(4, 5).equals("-")) {
					useThisSDF = sdf2;
				}  

		
				return new Timestamp(useThisSDF.parse(text).getTime());
			}
		} catch (ParseException ex) {
			throw new IllegalArgumentException("Could not parse date: "
					+ ex.getMessage(), ex);
		}
	}
	/**
	 * Format the Timestamp as String, using the specified DateFormat.
	 */
	@Override
	public String getAsText() {
		Timestamp value = (Timestamp) getValue();
		return (value != null ? this.sdf.format(value) : "");
	}
}
