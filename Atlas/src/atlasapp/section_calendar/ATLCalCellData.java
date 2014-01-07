//  ==================================================================================================================
//  ATLCalCellData.java
//  AtlasConsumerAndroid
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-10-22 TAN:    Init Class and port all properties and methods from iOS_IDAHO 
//  ==================================================================================================================
package atlasapp.section_calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import atlasapp.common.ATLConstants;
import atlasapp.common.ATLConstants.EventResponseType;
import atlasapp.model.ATLContactModel;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

public class ATLCalCellData implements Parcelable {

	/**
	 * 
	 */
	// Cell identifier
	String calCellGUID; // Temporary unique identifier for the cell, not saved
	public String calCellEventIdentifier; // Event identifier from eventstore, note
									// that this is read-only in eventstore
	public String calCellAlt2EventIdentifier; // Event identifier from eventstore, note
										// that this is read-only in eventstore
	public String calCellAlt3EventIdentifier; // Event identifier from eventstore, note
										// that this is read-only in eventstore

	// Cell date
	// The date to which the appointment belongs and the time
	// part is 0 or midnight

	public Date calCellDate = new Date(); // 2012-11-26 TAN: Add to add new
											// event
	// The date-time which includes the date and time the
	// appointment starts
	public Date calCellStartDate = new Date(); // 2012-11-26 TAN: Add to add new
												// event

	Date calCellEndDate; // The date-time which includes the date and time the
							// appointment ends
	int calCellDayOfMonth; // Integer day of month
	int calCellHour; // Integer hour of day of cell
	int calCellMinute; // Integer minute of hour of cell
	public int calCellDurationMinutes; // Integer duration, appointment end datetime -
								// start datetime as minutes

	// Cell labels
	public String calCellTitle; // Event (appointment) title
	public String calCellLocation; // Event (appointment) location
	public String calCellCalendar; // Calendar, ie work, home, etc.

	
	public boolean  isEditable = true;
	String calCellNotes;

	public String getCalCellNotes() {
		return calCellNotes;
	}

	public void setCalCellNotes(String calCellNotes) {
		this.calCellNotes = calCellNotes;
	}

	public int calCellCalendarColor; // Calendar color
	public int calCellBackgroundColor = Color.WHITE;
	

	// Cell alarm
	Date calCellAlarm1Datetime; // Alarm 1 datetime
	int calCellAlarm1AdvanceMinutes; // Appointment start datetime - alarm1
										// datetime as integer minutes
	Date calCellAlarm2Datetime; // Alarm 2 datetime
	int calCellAlarm2AdvanceMinutes; // Appointment start datetime - alarm2
										// datetime as integer minutes

	// Cell allies
	// MutableArray *calCellAlliesIDList ; // REMOVED 2012-03-20 RG
	ArrayList<Object> calCellAlliesAtlasIDList;
	ArrayList<Object> calCellAlliesEmailList; // ADDED 2012-03-20 RG
	ArrayList<Object> calCellAlliesNameList;
	ArrayList<Object> calCellAlliesImageList;
	ArrayList<Object> calCellAlliesStatusList; // ADDED 2012-03-06 HR

	// Cell ALT ADDED 2012-05-01 NGHIA
	public Date calCellPreferDateTime = null;// //2013-01-11 TAN add
	public Date calCellALT2Datetime = null;
	public Date calCellALT3Datetime = null;
	public String calCellEventGroupID = null; // use when query with ATL_APPOINTMENT table to
									// select 3 appointment to delete if needed.

	public boolean isShowOffHour = false;
	// PROPERTIES
	public EventResponseType eventResponseType_CellData = EventResponseType.None;

	// Cell Attendee
	public ArrayList<ATLContactModel> attendee;

	// METHODS
	// Display alarm icon?
	public boolean HasAlarm;
	// Does the cell have an appointment?
	public boolean HasAppointment;

	public boolean isBlank = true;
	public long eventId;
	public long calendarId = -1;
	public boolean isShowMoveEvent = false;
	public String inviterAtlasId="";

	public ATLCalCellData() {
		// TODO Auto-generated constructor stub
		this.calCellGUID = "";
		this.calCellTitle = "";
		this.calCellCalendarColor = Color.TRANSPARENT; // Init transparent
		this.calCellAlliesAtlasIDList = new ArrayList<Object>();
		this.calCellAlliesEmailList = new ArrayList<Object>();
		this.calCellAlliesNameList = new ArrayList<Object>();
		this.calCellAlliesImageList = new ArrayList<Object>();
		this.calCellAlliesStatusList = new ArrayList<Object>();
		this.calCellAlarm1AdvanceMinutes = ATLConstants.kAtlasEditCellAlarmOff;
		this.calCellAlarm2AdvanceMinutes = ATLConstants.kAtlasEditCellAlarmOff;
		this.attendee = new ArrayList<ATLContactModel>();
	}

	public boolean HasAlarm() {
		return (this.calCellAlarm1Datetime != null);
	}

	public boolean HasAppointment() {
		return ((!this.calCellTitle.equals("")) && (this.calCellTitle != null));
	}

	// GETTER AND SETTER METHODS

	public String getCalCellGUID() {
		return calCellGUID;
	}

	public void setCalCellGUID(String calCellGUID) {
		this.calCellGUID = calCellGUID;
	}

	public String getCalCellEventIdentifier() {
		return calCellEventIdentifier;
	}

	public void setCalCellEventIdentifier(String calCellEventIdentifier) {
		this.calCellEventIdentifier = calCellEventIdentifier;
	}

	public String getCalCellAlt2EventIdentifier() {
		return calCellAlt2EventIdentifier;
	}

//	public void setCalCellAlt2EventIdentifier(String calCellAlt2EventIdentifier) {
//		this.calCellAlt2EventIdentifier = calCellAlt2EventIdentifier;
//	}

	public void setInviterAtlasId(String inviterAtlasId)
	{
		this.inviterAtlasId = inviterAtlasId;
	}
	public String getInviterAtlasId()
	{
		return inviterAtlasId;
	}
	public String getCalCellAlt3EventIdentifier() {
		return calCellAlt3EventIdentifier;
	}

	public void setCalCellAlt3EventIdentifier(String calCellAlt3EventIdentifier) {
		this.calCellAlt3EventIdentifier = calCellAlt3EventIdentifier;
	}

	public Date getCalCellDate() {
		return calCellDate;
	}

	public void setCalCellDate(Date calCellDate) {
		this.calCellDate = calCellDate;
	}

	public Date getCalCellStartDate() {
		return calCellStartDate;
	}

	public void setCalCellStartDate(Date calCellStartDate) {
		this.calCellStartDate = calCellStartDate;
	}

	public Date getCalCellEndDate() {
		return calCellEndDate;
	}

	public void setCalCellEndDate(Date calCellEndDate) {
		this.calCellEndDate = calCellEndDate;
	}

	public int getCalCellDayOfMonth() {
		return calCellDayOfMonth;
	}

	public void setCalCellDayOfMonth(int calCellDayOfMonth) {
		this.calCellDayOfMonth = calCellDayOfMonth;
	}

	public int getCalCellHour() {
		return calCellHour;
	}

	public void setCalCellHour(int calCellHour) {
		this.calCellHour = calCellHour;
	}

	public int getCalCellMinute() {
		return calCellMinute;
	}

	public void setCalCellMinute(int calCellMinute) {
		this.calCellMinute = calCellMinute;
	}

	public int getCalCellDurationMinutes() {
		return calCellDurationMinutes;
	}

	public void setCalCellDurationMinutes(int calCellDurationMinutes) {
		this.calCellDurationMinutes = calCellDurationMinutes;
	}

	public String getCalCellTitle() {
		return calCellTitle;
	}

	public void setCalCellTitle(String calCellTitle) {
		this.calCellTitle = calCellTitle;
	}

	public String getCalCellLocation() {
		return calCellLocation;
	}

	public void setCalCellLocation(String calCellLocation) {
		this.calCellLocation = calCellLocation;
	}

	public String getCalCellCalendar() {
		return calCellCalendar;
	}

	public void setCalCellCalendar(String calCellCalendar) {
		this.calCellCalendar = calCellCalendar;
	}

	public int getCalCellCalendarColor() {
		return calCellCalendarColor;
	}

	public void setCalCellCalendarColor(int calCellCalendarColor) {
		this.calCellCalendarColor = calCellCalendarColor;
	}

	public Date getCalCellAlarm1Datetime() {
		return calCellAlarm1Datetime;
	}

	public void setCalCellAlarm1Datetime(Date calCellAlarm1Datetime) {
		this.calCellAlarm1Datetime = calCellAlarm1Datetime;
	}

	public int getCalCellAlarm1AdvanceMinutes() {
		return calCellAlarm1AdvanceMinutes;
	}

	public void setCalCellAlarm1AdvanceMinutes(int calCellAlarm1AdvanceMinutes) {
		this.calCellAlarm1AdvanceMinutes = calCellAlarm1AdvanceMinutes;
	}

	public Date getCalCellAlarm2Datetime() {
		return calCellAlarm2Datetime;
	}

	public void setCalCellAlarm2Datetime(Date calCellAlarm2Datetime) {
		this.calCellAlarm2Datetime = calCellAlarm2Datetime;
	}

	public int getCalCellAlarm2AdvanceMinutes() {
		return calCellAlarm2AdvanceMinutes;
	}

	public void setCalCellAlarm2AdvanceMinutes(int calCellAlarm2AdvanceMinutes) {
		this.calCellAlarm2AdvanceMinutes = calCellAlarm2AdvanceMinutes;
	}

	public ArrayList<Object> getCalCellAlliesAtlasIDList() {
		return calCellAlliesAtlasIDList;
	}

	public void setCalCellAlliesAtlasIDList(
			ArrayList<Object> calCellAlliesAtlasIDList) {
		this.calCellAlliesAtlasIDList = calCellAlliesAtlasIDList;
	}

	public ArrayList<Object> getCalCellAlliesEmailList() {
		return calCellAlliesEmailList;
	}

	public void setCalCellAlliesEmailList(
			ArrayList<Object> calCellAlliesEmailList) {
		this.calCellAlliesEmailList = calCellAlliesEmailList;
	}

	public ArrayList<Object> getCalCellAlliesNameList() {
		return calCellAlliesNameList;
	}

	public void setCalCellAlliesNameList(ArrayList<Object> calCellAlliesNameList) {
		this.calCellAlliesNameList = calCellAlliesNameList;
	}

	public ArrayList<Object> getCalCellAlliesImageList() {
		return calCellAlliesImageList;
	}

	public void setCalCellAlliesImageList(
			ArrayList<Object> calCellAlliesImageList) {
		this.calCellAlliesImageList = calCellAlliesImageList;
	}

	public ArrayList<Object> getCalCellAlliesStatusList() {
		return calCellAlliesStatusList;
	}

	public void setCalCellAlliesStatusList(
			ArrayList<Object> calCellAlliesStatusList) {
		this.calCellAlliesStatusList = calCellAlliesStatusList;
	}

	public Date getCalCellALT2Datetime() {
		return calCellALT2Datetime;
	}

	public void setCalCellALT2Datetime(Date calCellALT2Datetime) {
		this.calCellALT2Datetime = calCellALT2Datetime;
	}

	public Date getCalCellALT3Datetime() {
		return calCellALT3Datetime;
	}

	public void setCalCellALT3Datetime(Date calCellALT3Datetime) {
		this.calCellALT3Datetime = calCellALT3Datetime;
	}

	 public EventResponseType getEventResponseType_CellData() {
	 return eventResponseType_CellData;
	 }

	public void setEventResponseType_CellData(
			EventResponseType eventResponseType_CellData) {
		this.eventResponseType_CellData = eventResponseType_CellData;
	}

	public ArrayList<ATLContactModel> getAttendee() {
		return attendee;
	}

	public void setAttendee(ArrayList<ATLContactModel> attendee) {
		this.attendee = attendee;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public ATLCalCellData clone() {
		ATLCalCellData newCellData = new ATLCalCellData();
		newCellData.setCalCellGUID(new String(this.getCalCellGUID()));
		newCellData.setCalCellTitle(new String(this.getCalCellTitle()));
		return newCellData;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Draft init need to write to attendee
		// dest.writeString(calCellGUID);
		// dest.writeString(calCellEventIdentifier);
		// dest.writeString(calCellAlt1EventIdentifier);
		// dest.writeString(calCellAlt2EventIdentifier);
		// dest.writeLong(calCellDate.getTime());
		// dest.writeLong(calCellStartDate.getTime());
		// dest.writeLong(calCellEndDate.getTime());
		// dest.writeInt(calCellDayOfMonth);
		// dest.writeInt(calCellHour);
		// dest.writeInt(calCellMinute);
		// dest.writeInt(calCellDurationMinutes);
		// dest.writeString(calCellTitle);
		// dest.writeString(calCellLocation);
		// dest.writeString(calCellCalendar);
		// dest.writeInt(calCellCalendarColor);
		// dest.writeLong(calCellAlarm1Datetime.getTime());
		// dest.writeInt(calCellAlarm1AdvanceMinutes);
		// dest.writeLong(calCellAlarm2Datetime.getTime());
		// dest.writeInt(calCellAlarm2AdvanceMinutes);
		//
		// dest.writeString();
		//
		// dest.writeString();
		//
		//
		// dest.writeString();
		//
		// dest.writeString();
		//
		// dest.writeString();
		// dest.writeString();
		// dest.writeString();
		// dest.writeString();
		// dest.writeString();
		// dest.writeString();
		// dest.writeString();
		// dest.writeString();
		// dest.writeString();
		// dest.writeString();
		// dest.writeString();
	}

	public void copyEventOff(ATLCalCellData cellCurrentData) {
		// TODO Auto-generated method stub
		this.calCellTitle = cellCurrentData.getCalCellTitle();
		this.calCellCalendarColor = cellCurrentData
				.getCalCellCalendarColor();

		this.calCellLocation = cellCurrentData.getCalCellLocation();
		this.calCellNotes = cellCurrentData.getCalCellNotes();
		
		this.calCellEventIdentifier = cellCurrentData.calCellEventIdentifier;
		this.calCellAlt2EventIdentifier = cellCurrentData.calCellAlt2EventIdentifier;
		this.calCellAlt3EventIdentifier = cellCurrentData.calCellAlt3EventIdentifier;
		
		this.calCellALT2Datetime = cellCurrentData.calCellALT2Datetime;
		this.calCellALT3Datetime = cellCurrentData.calCellALT3Datetime;
		
		this.isBlank = false;

	}

	public void cleanToBlank() {
		// TODO Auto-generated method stub
		this.calCellCalendarColor = Color.TRANSPARENT;
		this.calCellTitle = "";
		this.calCellLocation = "";
		this.calCellNotes = "";
		this.calCellEventIdentifier = "";
		this.isBlank = true;
	}

}
