//  ==================================================================================================================
//  ATLCalendarManager.java
//  AtlasConsumerAndroid
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-10-21 TAN:    Init class
//  2012-10-24 TAN:    Implement eventsForDay method
//  2012-20-25 TAN:    Fix bugs in eventsForDate method and implement eventsInHourOfDay method <= still has issues
//  ==================================================================================================================

package atlasapp.section_calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Instances;
import android.util.Log;


import atlasapp.database.EventProperties;
import atlasapp.model.ATLCalendarModel;
import atlasapp.model.ATLEventModel;

@TargetApi(14)
public final class ATLCalendarManager {

	public ATLCalendarManager() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * METHOD FROM IOS VERSION + (UIColor *)colorFromCalendar:(EKCalendar
	 * *)calendar;
	 * 
	 * + (NSArray *)colorsFromEventstore;
	 * 
	 * + (NSArray *)eventsFromEvents:(ArrayList<Object> *)events hour:(int)hour;
	 * 
	 * + (ArrayList<Object> *)eventsForDay:(Date)date;
	 * 
	 * + (NSArray *)eventsForDay:(Date)date hour:(int)hour;
	 * 
	 * + (BOOL)writeEventToCalendarWithTitle:(NSString *)title
	 * location:(NSString *)location startDate:(Date)startDate ;
	 * 
	 * + (BOOL)deleteEventByEventIdentifier:(NSString *)eventIdentifier;
	 * 
	 * + (EKCalendar *)calendarFromCalendarTitle:(NSString *)calendarTitle;
	 * 
	 * + (NSString *)eventIdentifierWithStartDate:(Date)startDate;
	 * 
	 * + (BOOL)deleteEventFromiPhoneCalendarWithEventIdentifier:(NSString
	 * *)eventIdentifier;
	 */

	public static ArrayList<ATLEventModel> eventsForDay(Context ctx, Date date) {
		// get all events for today
		ArrayList<ATLEventModel> allEvents = new ArrayList<ATLEventModel>();
		// start date at GMT 0
		if (date == null) {

		}
		Date startDate;
		if (date == null) {
			startDate = new Date();
		} else {
			startDate = date;
		}

		Calendar cal = new GregorianCalendar(TimeZone.getDefault());
		cal.setTime(startDate);
		Calendar calStart = new GregorianCalendar(TimeZone.getDefault());

		calStart.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DATE), 0, 0, 0);
		long start = calStart.getTimeInMillis();
		Calendar calEnd = new GregorianCalendar(TimeZone.getDefault());

		calEnd.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DATE), 23, 59, 60);
		long end = calEnd.getTimeInMillis();

		// Pass it the default calendar.
		// String[] proj = new String[] { Instances.TITLE, Instances.BEGIN,
		// Instances.END, Instances.EVENT_COLOR };

		final String[] COLS = new String[] { CalendarContract.Events._ID,
				CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART,
				CalendarContract.Events.DTEND,
				CalendarContract.Events.EVENT_COLOR,
				CalendarContract.Events.EVENT_LOCATION,
				CalendarContract.Events.DESCRIPTION,
				CalendarContract.Events.CALENDAR_ID,CalendarContract.Events.ORIGINAL_ID,
				};

		Cursor cursor = Instances.query(ctx.getContentResolver(), null, start,
				end);

		if (cursor.moveToFirst()) {
			do {
				ATLEventModel event = new ATLEventModel();
				try {
					event.setEventData(cursor);
					allEvents.add(event);
				} catch (Exception e) {
					// ignore
				}
			} while (cursor.moveToNext());
		}

		cursor.close();
		Log.v("ATLCalendarManager", "check array");
		return allEvents;
	}

	public static ArrayList<ATLEventModel> eventsInHourOfDay(
			ArrayList<ATLEventModel> events, int hour, Date date) {
		ArrayList<ATLEventModel> eventsForHour = new ArrayList<ATLEventModel>();
		Calendar eventTime = new GregorianCalendar(TimeZone.getDefault());
		for (ATLEventModel event : events) {
			eventTime.setTime(new Date(event.dtstart));
			if (eventTime.get(Calendar.HOUR_OF_DAY) == hour) {
				eventsForHour.add(event);
			}
		}

		return eventsForHour;
	}
	public static ArrayList<EventProperties> eventsPropertiesInHourOfDay(
			ArrayList<EventProperties> events, int hour, Date date) {
		ArrayList<EventProperties> eventsForHour = new ArrayList<EventProperties>();
		Calendar eventTime = new GregorianCalendar(TimeZone.getDefault());
		for (EventProperties event : events) {
			eventTime.setTime(event.startDateTime);
			if (eventTime.get(Calendar.HOUR_OF_DAY) == hour) {
				eventsForHour.add(event);
			}
		}

		return eventsForHour;
	}

	public static String[] getCalendarName(Context ctx) {

		Cursor cursor = ctx.getContentResolver().query(
				CalendarContract.Calendars.CONTENT_URI, null, null, null, null);
		String[] calNames = new String[cursor.getCount()];

		int i = 0;
		if (cursor.moveToFirst()) {
			do {

				try {
					calNames[i] = cursor.getString(cursor
							.getColumnIndex(CalendarContract.Calendars.NAME));
					i++;
				} catch (Exception e) {
					// ignore
				}
			} while (cursor.moveToNext());
		}
		cursor.close();
		return calNames;
	}


	public static int[] getCalendarIds(Context ctx) {

		Cursor cursor = ctx.getContentResolver().query(
				CalendarContract.Calendars.CONTENT_URI, null, null, null, null);
		int[] calIds = new int[cursor.getCount()];
		int i = 0;
		if (cursor.moveToFirst()) {
			do {

				try {
					calIds[i] = cursor.getInt(cursor
							.getColumnIndex(CalendarContract.Calendars._ID));
					i++;
				} catch (Exception e) {
					// ignore
				}
			} while (cursor.moveToNext());
		}
		cursor.close();
		return calIds;
	}

	public static ArrayList<ATLCalendarModel> getCalendars(Context ctx) {
		Cursor cursor = ctx.getContentResolver().query(
				CalendarContract.Calendars.CONTENT_URI, null, null, null, null);
		ArrayList<ATLCalendarModel> cals = new ArrayList<ATLCalendarModel>();
		if (cursor.moveToFirst()) {
			do {
				ATLCalendarModel calendar = new ATLCalendarModel();
				try {
					calendar.name = cursor.getString(cursor
							.getColumnIndex(CalendarContract.Calendars.NAME));
					calendar.id = cursor.getInt(cursor
							.getColumnIndex(CalendarContract.Calendars._ID));
					calendar.color = cursor.getInt(cursor
							.getColumnIndex(CalendarContract.Calendars.CALENDAR_COLOR));
					cals.add(calendar);
				} catch (Exception e) {
					// ignore
				}
			} while (cursor.moveToNext());
		}
		cursor.close();
		return cals;
	}
	public static ArrayList<ATLCalendarModel> getCalendarList(Context ctx) {
		Cursor cursor = ctx.getContentResolver().query(
				CalendarContract.Calendars.CONTENT_URI, null, null, null, null);
		ArrayList<ATLCalendarModel> cals = new ArrayList<ATLCalendarModel>();
		if (cursor.moveToFirst()) {
			do {
				ATLCalendarModel calendar = new ATLCalendarModel();
				try {
					calendar.name = cursor.getString(cursor
							.getColumnIndex(CalendarContract.Calendars.NAME));
					calendar.id = cursor.getInt(cursor
							.getColumnIndex(CalendarContract.Calendars._ID));
					calendar.color = cursor.getInt(cursor
							.getColumnIndex(CalendarContract.Calendars.CALENDAR_COLOR));
					cals.add(calendar);
				} catch (Exception e) {
					// ignore
				}
			} while (cursor.moveToNext());
		}
		cursor.close();
		return cals;
	}

}
