//  ==================================================================================================================
//  DateTimeUtilities.java
//  ATLAS
//  Copyright (c) 2012 Atlas Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-11-14 NGHIA:  Created
//  ==================================================================================================================
/**
 * 
 */
package atlasapp.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.text.format.DateFormat;


/**
 * @author nghia
 * 
 */
public class DateTimeUtilities {

	/**
	 * 
	 */
	public DateTimeUtilities() {
		// TODO Auto-generated constructor stub
	}
	
	public static Date firstTimeOfDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);

		Date dateBegin = new Date();
		dateBegin.setTime(calendar.getTimeInMillis());
		return dateBegin;
	}

	public static Date dateByAddHoursAndMinutes(Date date, int hours,
			int minutes) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());

		calendar.set(Calendar.HOUR_OF_DAY, hours);
		calendar.set(Calendar.MINUTE, minutes);

		Date dateFinal = new Date();
		dateFinal.setTime(calendar.getTimeInMillis());

		return dateFinal;
	}

	public static Date localDateFromGMTDate(Date dateGMT) {

		Calendar calendarMGT = Calendar.getInstance();
		calendarMGT.setTimeInMillis(dateGMT.getTime());
		Calendar calendarLocal = (Calendar) calendarMGT.clone();

		TimeZone z = TimeZone.getDefault();
		int offset = z.getRawOffset();
		int offsetHrs = offset / 1000 / 60 / 60;
		int offsetMins = offset / 1000 / 60 % 60;

		// if (isInDayLight()) {
		// Log.i("daylight", "in Daylight saving time");
		// offsetHrs++;
		// }
		calendarLocal.add(Calendar.HOUR_OF_DAY, (offsetHrs));
		calendarLocal.add(Calendar.MINUTE, (offsetMins));

		Date dateLocal = new Date();
		dateLocal.setTime(calendarLocal.getTimeInMillis());
		return dateLocal;
	}

	public static boolean isInDayLight() {
		Date date = Calendar.getInstance().getTime();
		TimeZone tz = TimeZone.getDefault();
		return tz.inDaylightTime(date);
	}

	// 2012-12-19 TAN : Convert time local and
	// GMT+00:00=====================================
	public static Date serverTimeStringToLocalDateTime(String serverTimeString,
			String serverTimeFormat) {
		if (serverTimeString.equals("") || serverTimeString.equals(null)) {
			return null;
		}
		SimpleDateFormat sd = new SimpleDateFormat(serverTimeFormat);
		sd.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		Calendar localCal = Calendar.getInstance();
		Date date = new Date();
		try {
			date = sd.parse(serverTimeString);
			
			localCal.setTime(date);
			localCal.setTimeZone(TimeZone.getDefault());
			Date dateLocal = localCal.getTime();
			return dateLocal;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static String localDateTimeToServerTimeString(Date localDateTime, String serverTimeFormat) {
		if (localDateTime == null) {
			return null;
		}
		Calendar localCal = Calendar.getInstance(TimeZone.getTimeZone("GMT+00:00"));
		localCal.setTime(localDateTime);
		
		SimpleDateFormat sd = new SimpleDateFormat(serverTimeFormat);
		sd.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		String serverDateString = sd.format(localCal.getTime());
		return serverDateString;
	}

	// END
	// ==================================================================================
	public final static String kSimpleDateFormat = "yyyy:DD:MM HH:mm:ss";
	public final static Date toGMTDate(Date localDate) {
		return new Date();
	}

	public final static Date toLocalDate(Date gmtDate) {
		
		return new Date();
	}

	public final static String toString(Date date) {
		if(date == null)
			return "";
		SimpleDateFormat lv_formatter;
		lv_formatter = new SimpleDateFormat(kSimpleDateFormat);
		lv_formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		return lv_formatter.format(date);
	}

	public final static Date toDate(String string) {
		return new Date();
	}
}
