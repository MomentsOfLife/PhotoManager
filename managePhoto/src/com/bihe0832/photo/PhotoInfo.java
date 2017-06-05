package com.bihe0832.photo;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class PhotoInfo {
	
	private static final String MAKE = "Make";
	private static final String MODEL = "Model";
	private static final String DATE_TIME_ORIGINAL = "Date/Time Original";
	private static final String DATE_TIME = "Date/Time";
	private static final String DATE_STAMP = "GPS Date Stamp";
	private static final String TIME_STAMP = "GPS Time-Stamp";
	private static final String WIDTH = "Image Width";
	private static final String HEIGHT = "Image Height";
	private static final String LATITUDE_REF = "GPS Latitude Ref";
	private static final String LATITUDE_VALUE = "GPS Latitude";
	private static final String LONGTITUDE_REF = "GPS Longitude Ref";
	private static final String LONGTITUDE_VALUE = "GPS Longitude";
	private static final String ALTITUDE_REF = "GPS Altitude Ref";
	private static final String ALTITUDE_VALUE = "GPS Altitude";
	private static final String EXIF_VERSION = "Exif Version";
	
	public HashMap<String, String> valueList = new HashMap<String, String>();
	
	public static final ArrayList<String> keyList = new ArrayList<String>(
			Arrays.asList(DATE_TIME,MAKE, MODEL, DATE_TIME_ORIGINAL, DATE_STAMP, TIME_STAMP, WIDTH, HEIGHT, 
					LATITUDE_REF, LONGTITUDE_REF, LATITUDE_VALUE, LONGTITUDE_VALUE,ALTITUDE_REF,ALTITUDE_VALUE,EXIF_VERSION));
	
	private String getValueByKey(String key) {
		if(valueList.containsKey(key)){
			String value = valueList.get(key);
			return value.trim();
		}else{
			return "";
		}
	}

	public String getMake() {
		return getValueByKey(MAKE);
	}
	public String getModel() {
		return getValueByKey(MODEL);
	}
	
	public String getDateTime() {
		String dateString = getFormatDateTime(getValueByKey( DATE_TIME_ORIGINAL));
		if(dateString.length() < 1){
			dateString = getFormatDateTime(getValueByKey( DATE_TIME));
		}
		return dateString;
	}
	
	public String getFormatDateTime(String timeStr) {
		if(timeStr.contains("/")){
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			ParsePosition pos = new ParsePosition(0);
			Date strtodate = formatter.parse(timeStr, pos);
			SimpleDateFormat formatterafter = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
			return formatterafter.format(strtodate);
		}else{
			return timeStr;
		}
	}
	
	public String getDateTimeStamp() {
		return getValueByKey(DATE_STAMP) + " " + getValueByKey(TIME_STAMP);
	}
	public String getWidth() {
		return getValueByKey( WIDTH);
	}
	public String getHeight() {
		return getValueByKey( HEIGHT);
	}
	public String getLatitudeRef() {
		return getValueByKey(LATITUDE_REF);
	}
	
	public String getLatitudeValue() {
		return getValueByKey( LATITUDE_VALUE);
	}
	public String getLatitude() {
		return getLatitudeValue() + " " + getLatitudeRef();
	}
	
	public String getLongitudeRef() {
		return getValueByKey( LONGTITUDE_REF);
	}
	
	public String getLongitudeValue() {
		return getValueByKey( LONGTITUDE_VALUE);
	}
	
	public String getLongitude() {
		return getLongitudeValue() + " " + getLongitudeRef();
	}
	public String getAltitudeRef() {
		return getValueByKey( ALTITUDE_REF);
	}
	
	public String getAltitudeValue() {
		return getValueByKey( ALTITUDE_VALUE);
	}
	
	public String getAltitude() {
		return  getAltitudeRef() + ":" + getAltitudeValue() ;
	}
}
