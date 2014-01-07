//  ==================================================================================================================
//  ATLEventModel.java
//  AtlasConsumerAndroid
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-10-25 TAN:    Init class
//  2012-10-26 TAN:    Change dtstart and dtend to long type for easy to compare with other values
//  ==================================================================================================================

package atlasapp.model;


import android.database.Cursor;
import android.provider.CalendarContract;

public class ATLEventModel{
	public long event_id;
	public long calendar_id;
	public String organizer;
	public String title;
	public String event_location;
	public String description;
	public int event_color;
	public long dtstart;
	public long dtend;
	public String event_timezone;
	public String event_end_timezone;
	public int duration;
	public boolean all_day;
	public String rrule;
	public String rdate;
	public String exrule;
	public String exdate;
	public long original_id;
	public String original_sync_id;
	public String original_instance_time;
	public boolean original_all_day;
	public String access_level;
	public String availability;
	public String guest_can_modify;
	public boolean guests_can_invite_others;
	public boolean guests_can_see_guests;
	public String custom_app_package;
	public String custom_app_uri;
	
	// For set data for event
	public void setEventData(Cursor cursor) {
		// TODO Auto-generated method stub
		this.event_id = cursor.getLong(cursor.getColumnIndex("event_id"));
		this.calendar_id = cursor.getInt(cursor.getColumnIndex(CalendarContract.Events.CALENDAR_ID));
		this.original_id =  cursor.getLong(cursor.getColumnIndex(CalendarContract.Events._ID));
		this.original_sync_id = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.ORIGINAL_ID));
		this.title = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.TITLE));
		this.dtstart = cursor.getLong(cursor.getColumnIndex(CalendarContract.Events.DTSTART));
		this.dtend = cursor.getLong(cursor.getColumnIndex(CalendarContract.Events.DTEND));
		this.event_color = cursor.getInt(cursor.getColumnIndex(CalendarContract.Events.EVENT_COLOR));
		this.event_location = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION));
		this.description = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.DESCRIPTION));
	}
	

}
