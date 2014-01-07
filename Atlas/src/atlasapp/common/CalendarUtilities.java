//  ==================================================================================================================
//  CalendarUtilities.java
//  ATLAS
//  Copyright (c) 2012 Atlas Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-11-15 NGHIA:  Created
//  ==================================================================================================================

/**
 * 
 */
package atlasapp.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.ImageView;

import atlasapp.database.EventProperties;
import atlasapp.common.ATLConstants.AlertType;
import atlasapp.common.ATLConstants.EventResponseType;
import atlasapp.common.ATLConstants.event_respondData;
import atlasapp.section_alerts.AlertCellData;
import atlasapp.section_calendar.ATLCalCellData;
import atlasapp.section_calendar.ATLCalendarManager;
import atlasapp.section_calendar.ATLEventGroupDatabaseAdapter;
import atlasapp.section_calendar.ATLEventGroupModel;

public class CalendarUtilities {
	public static String toStringFromDate(Date date) {
		// date formatter
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
		String return_date = sd.format(date);
		return return_date;
	}

	public static Date toDateFromString(String dateString) {
		// 1985-04-12T23:20:50.52Z
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
		// sd.setTimeZone(TimeZone.getDefault());
		Date date = new Date();
		try {
			date = sd.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return date;
	}

	public static void addCellData(ATLCalCellData cellData, Context ctx) {
		// TODO add cell data to database and save to calendar.
		saveEventCalendarWithCellData(cellData, ctx);
		addAttendeeWithCellData(cellData);
	}

	public static void addAttendeeWithCellData(ATLCalCellData cellData) {
		// TODO save attendee to database
	}

	public static void saveEventCalendarWithCellData(ATLCalCellData cellData,
			Context ctx) {
		// Insert Event Prefer time
		long startMillis = 0;
		long endMillis = 0;
		long duration = 0;
		int id1 = -1;
		int id2 = -1;
		int id3 = -1;
		Date preferDay = cellData.calCellPreferDateTime;
		Calendar beginTime = Calendar.getInstance();
		beginTime.setTimeInMillis(preferDay.getTime());
		startMillis = beginTime.getTimeInMillis();
		duration = cellData.getCalCellDurationMinutes() * 60 * 1000;
		endMillis = startMillis + duration;// To long value
		id1 = insertEvent(cellData, startMillis, endMillis, ctx);
		Log.v("CalendarUtilities", "ID  :" + id1);


		// Insert to event group
		Calendar gmt0Cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		gmt0Cal.setTime(beginTime.getTime());
		
		SimpleDateFormat lv_formatter;
		lv_formatter = new SimpleDateFormat("yyyy:DD:MM HH:mm:ss");
		lv_formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		ATLEventGroupModel model = new ATLEventGroupModel();
		model.eventRespondStatus = cellData.eventResponseType_CellData;
		
		model.calCellEventIdentifier = id1 + ":" + gmt0Cal.getTimeInMillis();
	
		Date atl2 = cellData.getCalCellALT3Datetime();
		Date atl1 = cellData.getCalCellALT2Datetime();

		long startMillis1 = 0;
		long endMillis1 = 0;
		long duration1 = 0;
	
		Calendar beginTime1 = Calendar.getInstance();
		Calendar beginTime2 = Calendar.getInstance();
		if (atl1 != null) {

			beginTime1.setTimeInMillis(atl1.getTime());
			startMillis1 = beginTime1.getTimeInMillis();
			duration1 = cellData.getCalCellDurationMinutes() * 60 * 1000;
			endMillis1 = startMillis1 + duration1;// To long value
			id2 = insertEvent(cellData, startMillis1, endMillis1, ctx);
			Log.v("CalendarUtilities", "ID  :" + id2);
			gmt0Cal.setTime(beginTime1.getTime());
			model.calCellAlt2EventIdentifier = id2 + ":" + gmt0Cal.getTimeInMillis();
		}

		long startMillis2 = 0;
		long endMillis2 = 0;
		long duration2 = 0;
		if (atl2 != null) {
			
			beginTime2.setTimeInMillis(atl2.getTime());
			startMillis2 = beginTime2.getTimeInMillis();
			duration2 = cellData.getCalCellDurationMinutes() * 60 * 1000;
			endMillis2 = startMillis2 + duration2;// To long value
			id3 = insertEvent(cellData, startMillis2, endMillis2, ctx);
			Log.v("CalendarUtilities", "ID  :" + id3);
			gmt0Cal.setTime(beginTime2.getTime());
			model.calCellAlt3EventIdentifier = id3 + ":" +  gmt0Cal.getTimeInMillis();
		}

		
		// Insert Group model
		ATLEventGroupDatabaseAdapter.insertCalendarModel(model);
	}

	private static int insertEvent(ATLCalCellData cellData, long startMillis,
			long endMillis, Context ctx) {
		// TODO Auto-generated method stub

		ContentResolver cr = ctx.getContentResolver();
		ContentValues values = new ContentValues();

		values.put(CalendarContract.Events.DTSTART, startMillis);
		values.put(CalendarContract.Events.DTEND, endMillis);
		values.put(CalendarContract.Events.TITLE, cellData.getCalCellTitle());
		values.put(CalendarContract.Events.DESCRIPTION,
				cellData.getCalCellNotes());
		values.put(CalendarContract.Events.EVENT_COLOR,
				cellData.getCalCellCalendarColor());
		values.put(CalendarContract.Events.EVENT_LOCATION,
				cellData.getCalCellLocation());

		values.put(CalendarContract.Events.CALENDAR_ID, cellData.calendarId);
		values.put(CalendarContract.Events.EVENT_TIMEZONE, "eventTimezone");
		Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
		// Retrieve ID for new event
		int id = Integer.parseInt(uri.getLastPathSegment());
		Log.v("CalendarUtilities", "URI  :" + uri.toString());
		return id;
	}

	// ===========================================
	// 2013-01-12 TAN: add update event method
	// ===========================================
	public static void updateCellData(ATLCalCellData cellData, Context ctx) {
		// TODO Auto-generated method stub
		// long startMillis = 0;
		// long endMillis = 0;
		// Calendar beginTime = Calendar.getInstance();
		// beginTime.setTimeInMillis(cellData.getCalCellStartDate().getTime());
		// startMillis = beginTime.getTimeInMillis();
		// endMillis = startMillis +
		// cellData.getCalCellDurationMinutes()*60*1000;
		// updateEvent(cellData, startMillis, endMillis , ctx,
		// cellData.eventId);
		deleteCellData(cellData, ctx);
		saveEventCalendarWithCellData(cellData, ctx);

	}

	public static boolean updateEvent(ATLCalCellData cellData,
			long startMillis, long endMillis, Context ctx, long event_id) {
		// TODO Auto-generated method stub
		ContentResolver cr = ctx.getContentResolver();
		ContentValues event = new ContentValues();
		event.put(CalendarContract.Events.DTSTART, startMillis);
		event.put(CalendarContract.Events.DTEND, endMillis);
		event.put(CalendarContract.Events.TITLE, cellData.getCalCellTitle());
		event.put(CalendarContract.Events.EVENT_LOCATION,
				cellData.getCalCellLocation());
		event.put(CalendarContract.Events.DESCRIPTION,
				cellData.getCalCellNotes());
		event.put(CalendarContract.Events.EVENT_COLOR,
				cellData.getCalCellCalendarColor());
		Uri eventUri = ContentUris.withAppendedId(
				CalendarContract.Events.CONTENT_URI, cellData.eventId);

		int rowDeleted = cr.update(eventUri, event, null, null);
		// Retrieve ID for new event
		Log.v("CalendarUtilities", "Return row:  " + rowDeleted);

		return true;
	}

	// ===========================================
	// 2013-01-12 TAN: end method
	// ===========================================
	// 2013-01-12 TAN: add delete event method
	// ===========================================
	public static void deleteCellData(ATLCalCellData cellData, Context ctx) {
		// TODO Auto-generated method stub
		// Update event
		ContentResolver cr = ctx.getContentResolver();
		Log.v("CalendarUtilities", "Delete ID:  " + cellData.eventId);
		Uri eventUri = ContentUris.withAppendedId(
				CalendarContract.Events.CONTENT_URI, cellData.eventId);

		int rowDeleted = cr.delete(eventUri, null, null);
		// Retrieve ID for new event
		Log.v("CalendarUtilities", "Delete event:  " + eventUri.toString()+ "  "+ rowDeleted);
		
		//Handle Event Group
		if(cellData.calCellEventGroupID != null){
			if(cellData.calCellEventIdentifier != null){
				if(cellData.calCellEventIdentifier.length()>0){
					long id = Long.valueOf(cellData.calCellEventIdentifier);
					if(id >= 0){
						Uri eventUri1 = ContentUris.withAppendedId(
							CalendarContract.Events.CONTENT_URI, Long.valueOf(cellData.calCellEventIdentifier));
						int rowDeleted1 = cr.delete(eventUri1, null, null);
						Log.v("CalendarUtilities", "Delete event 1:  " + eventUri1.toString() + "  "+ rowDeleted1);
					}
					
				}
			}
			if(cellData.calCellAlt2EventIdentifier != null){
				if(cellData.calCellAlt2EventIdentifier.length()>0){
					long id = Long.valueOf(cellData.calCellAlt2EventIdentifier);
					if(id >= 0){
						Uri eventUri2 = ContentUris.withAppendedId(
							CalendarContract.Events.CONTENT_URI, Long.valueOf(cellData.calCellAlt2EventIdentifier));
						int rowDeleted2 = cr.delete(eventUri2, null, null);
						Log.v("CalendarUtilities", "Delete event 2:  " + eventUri2.toString() + "  "+ rowDeleted2);
					}
					
				}
			}
			
			if(cellData.calCellAlt3EventIdentifier != null){
				if(cellData.calCellAlt3EventIdentifier.length()>0){
					long id = Long.valueOf(cellData.calCellAlt3EventIdentifier);
					if(id >= 0){
						Uri eventUri3 = ContentUris.withAppendedId(
								CalendarContract.Events.CONTENT_URI, Long.valueOf(cellData.calCellAlt3EventIdentifier));
						int rowDeleted3 = cr.delete(eventUri3, null, null);
						Log.v("CalendarUtilities", "Delete event 3:  " + eventUri3.toString()+ "  "+ rowDeleted3);
					}
				}
			}
			ATLEventGroupDatabaseAdapter.deleteEventGroupID(cellData.calCellEventGroupID);
		}
	
	}
	
	// ===========================================
	// 2012-12-03 TAN: end delete event method
	// ===========================================
	// ===========================================
	// 2012-01-14 TAN: deleteGroupEventByDate # Start
	// ===========================================
	public static boolean deleteGroupEventByDate(Date date, Context ctx){
		
		ATLEventGroupModel groupEvent = ATLEventGroupDatabaseAdapter.getEventGroupOfEventDate(date.getTime());
		
		ATLEventGroupModel newGroup = new ATLEventGroupModel();
		
		if(groupEvent != null){
			String preferTimeString = ATLEventGroupDatabaseAdapter.getDateStringFromIDString(groupEvent.calCellEventIdentifier);
			String id1 = ATLEventGroupDatabaseAdapter.getEventIDFromIDString(groupEvent.calCellEventIdentifier);
			
			String alt2TimeString = ATLEventGroupDatabaseAdapter.getDateStringFromIDString(groupEvent.calCellAlt2EventIdentifier);
			String id2 = ATLEventGroupDatabaseAdapter.getEventIDFromIDString(groupEvent.calCellAlt2EventIdentifier);
			
			String alt3TimeString = ATLEventGroupDatabaseAdapter.getDateStringFromIDString(groupEvent.calCellAlt3EventIdentifier);
			String id3 = ATLEventGroupDatabaseAdapter.getEventIDFromIDString(groupEvent.calCellAlt3EventIdentifier);
			
			ContentResolver cr = ctx.getContentResolver();
			String dateString = date.getTime() +"";
			if(preferTimeString != null && dateString.equals(preferTimeString)){
				if(groupEvent.calCellAlt2EventIdentifier != null){
					if(groupEvent.calCellAlt2EventIdentifier.length()>0){
						long id = Long.valueOf(id2);
						if (id>=0){
							Uri eventUri2 = ContentUris.withAppendedId(
									CalendarContract.Events.CONTENT_URI, id);
							int rowDeleted2 = cr.delete(eventUri2, null, null);
							Log.v("CalendarUtilities", "Delete event 2:  " + eventUri2.toString() + "  "+ rowDeleted2);
						}
					}
				}
				
				if(groupEvent.calCellAlt3EventIdentifier != null){
					if(groupEvent.calCellAlt3EventIdentifier.length()>0){
						long id = Long.valueOf(id3);
						if (id>=0){
							Uri eventUri3 = ContentUris.withAppendedId(
									CalendarContract.Events.CONTENT_URI, id);
							int rowDeleted3 = cr.delete(eventUri3, null, null);
							Log.v("CalendarUtilities", "Delete event 3:  " + eventUri3.toString()+ "  "+ rowDeleted3);
						}
					}
				}
				newGroup.calCellEventIdentifier = groupEvent.calCellEventIdentifier;
			}
			else if(alt2TimeString != null && dateString.equals(alt2TimeString)){
				Uri eventUri = ContentUris.withAppendedId(
						CalendarContract.Events.CONTENT_URI, Long.valueOf(id1));
	
				int rowDeleted = cr.delete(eventUri, null, null);
				// Retrieve ID for new event
				Log.v("CalendarUtilities", "Delete event:  " + eventUri.toString()+ "  "+ rowDeleted);
				
				if(groupEvent.calCellAlt3EventIdentifier != null){
					if(groupEvent.calCellAlt3EventIdentifier.length()>0){
						Uri eventUri3 = ContentUris.withAppendedId(
								CalendarContract.Events.CONTENT_URI, Long.valueOf(id3));
						int rowDeleted3 = cr.delete(eventUri3, null, null);
						Log.v("CalendarUtilities", "Delete event 3:  " + eventUri3.toString()+ "  "+ rowDeleted3);
					}
				}
				newGroup.calCellEventIdentifier = groupEvent.calCellAlt2EventIdentifier;
			}
			else if(alt3TimeString != null && dateString.equals(alt3TimeString)){
				Uri eventUri = ContentUris.withAppendedId(
						CalendarContract.Events.CONTENT_URI, Long.valueOf(id1));
	
				int rowDeleted = cr.delete(eventUri, null, null);
				// Retrieve ID for new event
				Log.v("CalendarUtilities", "Delete event:  " + eventUri.toString()+ "  "+ rowDeleted);
				
				if(groupEvent.calCellAlt2EventIdentifier != null){
					if(groupEvent.calCellAlt2EventIdentifier.length()>0){
						Uri eventUri2 = ContentUris.withAppendedId(
								CalendarContract.Events.CONTENT_URI, Long.valueOf(id2));
						int rowDeleted2 = cr.delete(eventUri2, null, null);
						Log.v("CalendarUtilities", "Delete event 2:  " + eventUri2.toString() + "  "+ rowDeleted2);
					}
				}
				newGroup.calCellEventIdentifier = groupEvent.calCellAlt3EventIdentifier;
			}
			ATLEventGroupDatabaseAdapter.deleteEventGroupID(groupEvent.calCellGroupEventID);
			newGroup.eventRespondStatus = EventResponseType.Decline;
			ATLEventGroupDatabaseAdapter.insertCalendarModel(newGroup);
		}
		
		return true;
	}
	// ===========================================
	// 2012-01-14 TAN: deleteGroupEventByDate # End
	// ===========================================
	// ===========================================
	// 2012-01-14 TAN: acceptEventByDate # Start
	// ===========================================
    public static boolean acceptEventByDate(Date date, Context ctx){
		ATLEventGroupModel groupEvent = ATLEventGroupDatabaseAdapter.getEventGroupOfEventDate(date.getTime());
		ATLEventGroupModel newGroup = new ATLEventGroupModel();
		
		if(groupEvent != null){
			String preferTimeString = ATLEventGroupDatabaseAdapter.getDateStringFromIDString(groupEvent.calCellEventIdentifier);
			String id1 = ATLEventGroupDatabaseAdapter.getEventIDFromIDString(groupEvent.calCellEventIdentifier);
			
			String alt2TimeString = ATLEventGroupDatabaseAdapter.getDateStringFromIDString(groupEvent.calCellAlt2EventIdentifier);
			String id2 = ATLEventGroupDatabaseAdapter.getEventIDFromIDString(groupEvent.calCellAlt2EventIdentifier);
			
			String alt3TimeString = ATLEventGroupDatabaseAdapter.getDateStringFromIDString(groupEvent.calCellAlt3EventIdentifier);
			String id3 = ATLEventGroupDatabaseAdapter.getEventIDFromIDString(groupEvent.calCellAlt3EventIdentifier);
			
			ContentResolver cr = ctx.getContentResolver();
			String dateString = date.getTime() +"";
			if(preferTimeString != null && dateString.equals(preferTimeString)){
				if(groupEvent.calCellAlt2EventIdentifier != null){
					if(groupEvent.calCellAlt2EventIdentifier.length()>0){
						long id = Long.valueOf(id2);
						if (id>=0){
							Uri eventUri2 = ContentUris.withAppendedId(
									CalendarContract.Events.CONTENT_URI, id);
							int rowDeleted2 = cr.delete(eventUri2, null, null);
							Log.v("CalendarUtilities", "Delete event 2:  " + eventUri2.toString() + "  "+ rowDeleted2);
						}
					}
				}
				
				if(groupEvent.calCellAlt3EventIdentifier != null){
					if(groupEvent.calCellAlt3EventIdentifier.length()>0){
						long id = Long.valueOf(id3);
						if (id>=0){
							Uri eventUri3 = ContentUris.withAppendedId(
									CalendarContract.Events.CONTENT_URI, id);
							int rowDeleted3 = cr.delete(eventUri3, null, null);
							Log.v("CalendarUtilities", "Delete event 3:  " + eventUri3.toString()+ "  "+ rowDeleted3);
						}
					}
				}
				newGroup.calCellEventIdentifier = groupEvent.calCellEventIdentifier;
			}
			else if(alt2TimeString != null && dateString.equals(alt2TimeString)){
				Uri eventUri = ContentUris.withAppendedId(
						CalendarContract.Events.CONTENT_URI, Long.valueOf(id1));
	
				int rowDeleted = cr.delete(eventUri, null, null);
				// Retrieve ID for new event
				Log.v("CalendarUtilities", "Delete event:  " + eventUri.toString()+ "  "+ rowDeleted);
				
				if(groupEvent.calCellAlt3EventIdentifier != null){
					if(groupEvent.calCellAlt3EventIdentifier.length()>0){
						Uri eventUri3 = ContentUris.withAppendedId(
								CalendarContract.Events.CONTENT_URI, Long.valueOf(id3));
						int rowDeleted3 = cr.delete(eventUri3, null, null);
						Log.v("CalendarUtilities", "Delete event 3:  " + eventUri3.toString()+ "  "+ rowDeleted3);
					}
				}
				newGroup.calCellEventIdentifier = groupEvent.calCellAlt2EventIdentifier;
			}
			else if(alt3TimeString != null && dateString.equals(alt3TimeString)){
				Uri eventUri = ContentUris.withAppendedId(
						CalendarContract.Events.CONTENT_URI, Long.valueOf(id1));
	
				int rowDeleted = cr.delete(eventUri, null, null);
				// Retrieve ID for new event
				Log.v("CalendarUtilities", "Delete event:  " + eventUri.toString()+ "  "+ rowDeleted);
				
				if(groupEvent.calCellAlt2EventIdentifier != null){
					if(groupEvent.calCellAlt2EventIdentifier.length()>0){
						Uri eventUri2 = ContentUris.withAppendedId(
								CalendarContract.Events.CONTENT_URI, Long.valueOf(id2));
						int rowDeleted2 = cr.delete(eventUri2, null, null);
						Log.v("CalendarUtilities", "Delete event 2:  " + eventUri2.toString() + "  "+ rowDeleted2);
					}
				}
				newGroup.calCellEventIdentifier = groupEvent.calCellAlt3EventIdentifier;
			}
			ATLEventGroupDatabaseAdapter.deleteEventGroupID(groupEvent.calCellGroupEventID);
			newGroup.eventRespondStatus = EventResponseType.Accepted;
			ATLEventGroupDatabaseAdapter.insertCalendarModel(newGroup);
		}
		
		return true;
	}
	
	// ===========================================
	// 2012-01-14 TAN: acceptEventByDate # End
	// ===========================================
	public static boolean isToday(Date date) {
		Date currentDate = new Date();
		Date firstTimeOfDate = firstTimeOfDate(currentDate);

		float currentSecond = secondFromMilisecond(firstTimeOfDate.getTime());
		float actuallySecond = secondFromMilisecond(date.getTime());
		float delta = actuallySecond - currentSecond;
		if (delta >= 0 && delta < 60 * 60 * 24 * 1) {
			return true;
		}
		return false;
		/*
		 * comment Tan's Code to test Today boolean isToday = false; Date
		 * currentTime = new Date(); Calendar cal = Calendar.getInstance();
		 * cal.setTime(currentTime);
		 * 
		 * Calendar calCurrentView = Calendar.getInstance();
		 * calCurrentView.setTime(date);
		 * 
		 * isToday = (cal.get(Calendar.YEAR) ==
		 * calCurrentView.get(Calendar.YEAR)) && (cal.get(Calendar.MONTH) ==
		 * calCurrentView .get(Calendar.MONTH)) &&
		 * (cal.get(Calendar.DAY_OF_MONTH) == calCurrentView
		 * .get(Calendar.DAY_OF_MONTH)); return isToday;
		 */
	}

	public static boolean isTomorrow(Date date) {
		boolean isTomorrow = false;
		
		Date currentDate = new Date();
		Date firstTimeToday = firstTimeOfDate(currentDate);
		long firstSecondInLong = firstTimeToday.getTime() / 1000;
		
		long firstOfTomorrowDate = firstSecondInLong + 24 * 60 * 60;
		long lastOfTomorrowDate = firstOfTomorrowDate +  24 * 60 * 60;
		
		long secondOfDate = date.getTime() /1000;
		if(secondOfDate >= firstOfTomorrowDate && secondOfDate <= lastOfTomorrowDate){
			isTomorrow = true;
		}
		 
		return isTomorrow;
	}

	public static boolean isPast(Date date) {
		boolean isPast = false;
		Date currentTime = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentTime);

		Calendar calCurrentView = Calendar.getInstance();
		calCurrentView.setTime(date);

		isPast = cal.getTime().after(calCurrentView.getTime());

		return isPast;
	}

	// ===========================================
	// 2012-12-21 TAN: To color task priority icon
	// ===========================================
	// { TODAY, PAST_WEEK, PAST_MONTH, OLDER };
	public static boolean isPastWeek(Date date) {
		Date currentDate = new Date();
		Date firstTimeOfWeek = firstTimeOfWeek(currentDate);

		float currentSecond = secondFromMilisecond(firstTimeOfWeek.getTime());
		float actuallySecond = secondFromMilisecond(date.getTime());
		float delta = actuallySecond - currentSecond;
		Log.v("Time currentDate", "" + currentDate);
		Log.v("Time currentSecond", "" + firstTimeOfWeek);
		Log.v("Time actuallySecond", "" + date);
		Log.v("Time delta", "" + delta);
		if (delta >= 0 && delta < 60 * 60 * 24 * 7) {
			return true;
		}
		return false;
	}

	public static boolean isPastMonth(Date date) {
		Date currentDate = new Date();
		Date firstTimeOfMonth = firstTimeOfMonth(currentDate);

		float currentSecond = secondFromMilisecond(firstTimeOfMonth.getTime());
		float actuallySecond = secondFromMilisecond(date.getTime());
		float delta = actuallySecond - currentSecond;
		Log.v("Time currentDate", "" + currentDate);
		Log.v("Time currentSecond", "" + firstTimeOfMonth);
		Log.v("Time actuallySecond", "" + date);
		Log.v("Time delta", "" + delta);
		if (delta > 0 && delta <= 60 * 60 * 24 * 30) {
			return true;
		}
		return false;
	}

	static public void setImageColor(ImageView view, Bitmap sourceBitmap,
			int rgbcolor)// ,Bitmap sourceBitmap)
	{
		if (sourceBitmap != null) {
			float R = Color.red(rgbcolor);
			float G = Color.green(rgbcolor);
			float B = Color.blue(rgbcolor);

			Log.v("R:G:B", R + ":" + G + ":" + B); //

			float[] colorTransform = { R / 255f, 0, 0, 0, 0, // R color
					0, G / 255f, 0, 0, 0 // G color
					, 0, 0, B / 255f, 0, 0 // B color
					, 0, 0, 0, 1f, 0f };

			ColorMatrix colorMatrix = new ColorMatrix();
			colorMatrix.setSaturation(0f); // Remove Colour

			colorMatrix.set(colorTransform);
			ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(
					colorMatrix);
			Paint paint = new Paint();
			paint.setColorFilter(colorFilter);

			Bitmap mutableBitmap = sourceBitmap.copy(Bitmap.Config.ARGB_8888,
					true);
			view.setImageBitmap(mutableBitmap);
			Canvas canvas = new Canvas(mutableBitmap);
			canvas.drawBitmap(mutableBitmap, 0, 0, paint);

		}
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}

		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);

		return bitmap;
	}

	static public void setImageColorPixels(ImageView view, Bitmap myBitmap,
			int rgbcolor)// ,Bitmap sourceBitmap)
	{

		int intArray[];

		intArray = new int[myBitmap.getWidth() * myBitmap.getHeight()];

		// copy pixel data from the Bitmap into the 'intArray' array
		myBitmap.getPixels(intArray, 0, myBitmap.getHeight(), 0, 0,
				myBitmap.getHeight(), myBitmap.getWidth());

		// replace the red pixels with yellow ones
		for (int i = 0; i < intArray.length; i++) {
			// System.out.println("color is--" + i + " " + intArray[i]);
			if (intArray[i] != Color.TRANSPARENT) {

				intArray[i] = rgbcolor;
			}
		}
		myBitmap.setPixels(intArray, 0, myBitmap.getHeight(), 0, 0,
				myBitmap.getHeight(), myBitmap.getWidth());

		view.setImageBitmap(myBitmap);
	}

	public static Date firstTimeOfDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date dateBegin = new Date();
		dateBegin.setTime(calendar.getTimeInMillis());
		return dateBegin;
	}

	public static Date firstTimeOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

		Date dateBegin = new Date();
		dateBegin.setTime(calendar.getTimeInMillis());
		return dateBegin;
	}

	public static Date firstTimeOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);

		Date dateBegin = new Date();
		dateBegin.setTime(calendar.getTimeInMillis());
		return dateBegin;
	}

	// ===========================================
	// 2012-12-21 TAN: END
	// ===========================================
	public static float secondFromMilisecond(float milisecond) {
		return milisecond / 1000;

	}

	public static boolean saveEventByAlertDataAndDate(AlertCellData cellData, Date date, Context ctx){
		
		long startMillis = 0;
		long endMillis = 0;
		long duration = 0;
		
		Date preferDay = date;
		Calendar beginTime = Calendar.getInstance();
		beginTime.setTimeInMillis(preferDay.getTime());
		startMillis = beginTime.getTimeInMillis();
		
		duration = cellData.alertCellEventDuration*60*1000;
		endMillis = startMillis + duration;// To long value
		
		ContentResolver cr = ctx.getContentResolver();
		ContentValues values = new ContentValues();

		values.put(CalendarContract.Events.DTSTART, startMillis);
		values.put(CalendarContract.Events.DTEND, endMillis);
		values.put(CalendarContract.Events.TITLE, cellData.alertCellEventTitle);
		values.put(CalendarContract.Events.DESCRIPTION,
				cellData.alertCellEventLocation);
		values.put(CalendarContract.Events.EVENT_LOCATION,
				cellData.alertCellEventLocation);
		if (ATLCalendarManager.getCalendarList(ctx).size() > 0){
			values.put(CalendarContract.Events.CALENDAR_ID, ATLCalendarManager.getCalendarList(ctx).get(0).id);//default calendar events
			values.put(CalendarContract.Events.EVENT_COLOR,
				ATLCalendarManager.getCalendarList(ctx).get(0).color);
		}
		values.put(CalendarContract.Events.EVENT_TIMEZONE, "eventTimezone");
		
		Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
		// Retrieve ID for new event
		int id = Integer.parseInt(uri.getLastPathSegment());
		Log.v("CalendarUtilities", "URI  :" + uri.toString());
		
		// Insert to event group
		Calendar gmt0Cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		gmt0Cal.setTime(beginTime.getTime());
		
		SimpleDateFormat lv_formatter;
		lv_formatter = new SimpleDateFormat("yyyy:DD:MM HH:mm:ss");
		lv_formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		ATLEventGroupModel model = new ATLEventGroupModel();
		
		switch(cellData.currentType){
			case AlertType.eventAccepted_Sent:
				model.eventRespondStatus = EventResponseType.Accepted;
			    break;
			default:
				break;
		}
		
		model.calCellEventIdentifier = id + ":" + gmt0Cal.getTimeInMillis();
		// Insert Group model
		ATLEventGroupDatabaseAdapter.insertCalendarModel(model);
		return true;
	}
	
	//2013-02-26 TAN: implement save method #START
	public static boolean saveEventByEventProperties(EventProperties eventPropertiesPre, Context ctx){
		
		long startMillis = 0;
		long endMillis = 0;
		long duration = 0;
		
		Date preferDay = eventPropertiesPre.startDateTime;
		Calendar beginTime = Calendar.getInstance();
		beginTime.setTimeInMillis(preferDay.getTime());
		startMillis = beginTime.getTimeInMillis();
		
		duration = eventPropertiesPre.duration*60*1000;
		endMillis = startMillis + duration;// To long value
		
		ContentResolver cr = ctx.getContentResolver();
		ContentValues values = new ContentValues();

		values.put(CalendarContract.Events.DTSTART, startMillis);
		values.put(CalendarContract.Events.DTEND, endMillis);
		values.put(CalendarContract.Events.TITLE, eventPropertiesPre.title);
		values.put(CalendarContract.Events.DESCRIPTION,
				eventPropertiesPre.location);
		values.put(CalendarContract.Events.EVENT_LOCATION,
				eventPropertiesPre.location);
		if (ATLCalendarManager.getCalendarList(ctx).size() > 0){
			values.put(CalendarContract.Events.CALENDAR_ID, ATLCalendarManager.getCalendarList(ctx).get(0).id);//default calendar events
			values.put(CalendarContract.Events.EVENT_COLOR,
				ATLCalendarManager.getCalendarList(ctx).get(0).color);
		}
		values.put(CalendarContract.Events.EVENT_TIMEZONE, "eventTimezone");
		
		Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
		// Retrieve ID for new event
		int id = Integer.parseInt(uri.getLastPathSegment());
		Log.v("CalendarUtilities", "URI  :" + uri.toString());
		
		// Insert to event group
		Calendar gmt0Cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		gmt0Cal.setTime(beginTime.getTime());
		
		SimpleDateFormat lv_formatter;
		lv_formatter = new SimpleDateFormat("yyyy:DD:MM HH:mm:ss");
		lv_formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		ATLEventGroupModel model = new ATLEventGroupModel();
		
//		switch(eventPropertiesPre.status){
//			case THE_ONE:
				model.eventRespondStatus = EventResponseType.Accepted;
//			    break;
//			default:
//				break;
//		}
		
		model.calCellEventIdentifier = id + ":" + gmt0Cal.getTimeInMillis();
		// Insert Group model
		ATLEventGroupDatabaseAdapter.insertCalendarModel(model);
		
		return true;
	}
	//2013-02-26 TAN: implement save method #END
}
