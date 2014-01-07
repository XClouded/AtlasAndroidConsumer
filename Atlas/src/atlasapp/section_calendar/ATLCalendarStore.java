/**
 * 
 */
package atlasapp.section_calendar;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;

import atlasapp.model.ATLCalendarModel;

/**
 * @author nghia
 * 
 */
public class ATLCalendarStore {

	/**
	 * 
	 */
	public ATLCalendarStore() {
		// TODO Auto-generated constructor stub
	}

	public static boolean save(ArrayList<ATLCalendarModel> calendarList,
			Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		for (ATLCalendarModel calendar : calendarList) {
			editor.putBoolean(calendar.name, calendar.isActive);
		}
		return editor.commit();
	}

	public static ArrayList<ATLCalendarModel> loadCalendarList(Context context) {

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);

		Cursor cursor = context.getContentResolver().query(
				CalendarContract.Calendars.CONTENT_URI, null, null, null, null);
		ArrayList<ATLCalendarModel> calendarList = new ArrayList<ATLCalendarModel>();
		if (cursor.moveToFirst()) {
			do {
				ATLCalendarModel calendar = new ATLCalendarModel();
				try {
					calendar.name = cursor.getString(cursor
							.getColumnIndex(CalendarContract.Calendars.NAME));
					calendar.id = cursor.getInt(cursor
							.getColumnIndex(CalendarContract.Calendars._ID));
					calendar.color = cursor
							.getInt(cursor
									.getColumnIndex(CalendarContract.Calendars.CALENDAR_COLOR));
					calendar.isActive = preferences.getBoolean(calendar.name,
							true);
					calendar.deNull();
					calendarList.add(calendar);
				} catch (Exception e) {
					// ignore
				}
			} while (cursor.moveToNext());
		}
		cursor.close();
		return calendarList;
	}
	public static ArrayList<String> loadInActiveCalendarNameList(Context context) {

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);

		Cursor cursor = context.getContentResolver().query(
				CalendarContract.Calendars.CONTENT_URI, null, null, null, null);
		ArrayList<String> calendarList = new ArrayList<String>();
		if (cursor.moveToFirst()) {
			do {
				ATLCalendarModel calendar = new ATLCalendarModel();
				try {
					calendar.name = cursor.getString(cursor
							.getColumnIndex(CalendarContract.Calendars.NAME));
					calendar.id = cursor.getInt(cursor
							.getColumnIndex(CalendarContract.Calendars._ID));
					calendar.color = cursor
							.getInt(cursor
									.getColumnIndex(CalendarContract.Calendars.CALENDAR_COLOR));
					calendar.isActive = preferences.getBoolean(calendar.name,
							true);
					calendar.deNull();
					if(!calendar.isActive){
						calendarList.add(calendar.name);
					}
				} catch (Exception e) {
					// ignore
				}
			} while (cursor.moveToNext());
		}
		cursor.close();
		return calendarList;
	}
	public static ArrayList<ATLCalendarModel> loadActiveCalendarList(Context context) {

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);

		Cursor cursor = context.getContentResolver().query(
				CalendarContract.Calendars.CONTENT_URI, null, null, null, null);
		ArrayList<ATLCalendarModel> calendarList = new ArrayList<ATLCalendarModel>();
		if (cursor.moveToFirst()) {
			do {
				ATLCalendarModel calendar = new ATLCalendarModel();
				try {
					calendar.name = cursor.getString(cursor
							.getColumnIndex(CalendarContract.Calendars.NAME));
					calendar.id = cursor.getInt(cursor
							.getColumnIndex(CalendarContract.Calendars._ID));
					calendar.color = cursor
							.getInt(cursor
									.getColumnIndex(CalendarContract.Calendars.CALENDAR_COLOR));
					calendar.isActive = preferences.getBoolean(calendar.name,
							true);
					calendar.deNull();
					if(calendar.isActive){
						calendarList.add(calendar);
					}
				} catch (Exception e) {
					// ignore
				}
			} while (cursor.moveToNext());
		}
		cursor.close();
		return calendarList;
	}
}
