//  ==================================================================================================================
//  ATLCalCellList.java
//  AtlasConsumerAndroid
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-10-22 TAN:    Init Class and port all properties and methods from iOS_IDAHO 
//  2012-10-24 TAN:    Handle load(Context ctx) method
//  2012-10-26 TAN:    Finish load method in simulate mode
//					   For normal mode still have issuse, some events can not be loaded.
//  ==================================================================================================================

package com.atlasapp.section_calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.atlasapp.common.DateTimeUtilities;
import com.atlasapp.model.ATLEventModel;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;


public class ATLCalCellList {
	// PROPERTIES
	ArrayList<Object> calListArray = new ArrayList<Object>();
	public ArrayList<Object> calEventsListArray = new ArrayList<Object>();
	int calListCount;
	Date calListDate;
	boolean calListSimulate = false;
	Context mContext;

	public ATLCalCellList() {
		// TODO Auto-generated constructor stub
		// SIMULATION: TURN OFF WHEN REAL DATA IS AVAILABLE
		// ==================================
		// WHEN ON, FAKE CELL DATA WILL BE CREATED
		this.calListSimulate = false;
		this.calListArray = new ArrayList<Object>();
		this.calListDate = new Date();
	}

	// METHODS
	public boolean load(Context ctx) {
		mContext = ctx;
		clear();
		// load data for one day
		if (this.calListSimulate) {
			// fake the cells
			int x = -1;
			// int firstHour, secondHour, thirdHour ;
			for (int ktr = 0; ktr < 240; ktr++) {
				x = x + 1;
				if ((x % 10 == 0) | (x == 100) | (x == 101) | (x == 102)
						| (x == 103) | (x == 110) | (x == 150)) {
					// create a fake cell for each hour
					ATLCalCellData newCell = new ATLCalCellData();

					this.addCell(newCell);

					newCell.calCellDate = this.calListDate;
					newCell.calCellCalendarColor = Color.TRANSPARENT;
					newCell.calCellHour = (int) x / 10;
					newCell.calCellMinute = 0;

					switch (x) {
					case 100:
						newCell.calCellMinute = 10;
						newCell.calCellTitle = "Appt with Destiny";
						newCell.calCellLocation = "Atlas World HQ";
						newCell.calCellCalendar = "Red";
						newCell.calCellCalendarColor = Color.RED;
						newCell.calCellDurationMinutes = 30;
						newCell.calCellAlliesEmailList
								.add("joe.chicago@atlaspowered.com");
						newCell.calCellAlliesNameList.add("Joe Chicago");
						newCell.calCellAlliesImageList.add("Avatar_Ashton.png");
						newCell.calCellAlliesStatusList.add("question");
						break;

					case 101:
						newCell.calCellMinute = 15;
						newCell.calCellTitle = "Appt with History";
						newCell.calCellLocation = "Atlas World HQ";
						newCell.calCellCalendar = "Red";
						newCell.calCellCalendarColor = Color.RED;
						newCell.calCellDurationMinutes = 30;
						newCell.calCellAlliesEmailList
								.add("joe.chicago@atlaspowered.com");
						newCell.calCellAlliesNameList.add("Joe Chicago");
						newCell.calCellAlliesImageList.add("Avatar_Ashton.png");
						newCell.calCellAlliesStatusList.add("question");
						break;

					case 102:
						newCell.calCellMinute = 30;
						newCell.calCellTitle = "Appt with Honor";
						newCell.calCellLocation = "Atlas World HQ";
						newCell.calCellCalendar = "Red";
						newCell.calCellCalendarColor = Color.RED;
						newCell.calCellDurationMinutes = 30;
						newCell.calCellAlliesEmailList
								.add("joe.chicago@atlaspowered.com");
						newCell.calCellAlliesNameList.add("Joe Chicago");
						newCell.calCellAlliesImageList.add("Avatar_Ashton.png");
						newCell.calCellAlliesStatusList.add("question");
						break;

					case 103:
						newCell.calCellMinute = 45;
						newCell.calCellTitle = "Appt with Doctor";
						newCell.calCellLocation = "Atlas World HQ";
						newCell.calCellCalendar = "Red";
						newCell.calCellCalendarColor = Color.RED;
						newCell.calCellDurationMinutes = 30;
						newCell.calCellAlliesEmailList
								.add("joe.chicago@atlaspowered.com");
						newCell.calCellAlliesNameList.add("Joe Chicago");
						newCell.calCellAlliesImageList.add("Avatar_Ashton.png");
						newCell.calCellAlliesStatusList.add("question");
						break;

					case 110:
						newCell.calCellTitle = "All hands on deck";
						newCell.calCellLocation = "1010 Wilshire Roof deck";
						newCell.calCellCalendar = "Green";
						newCell.calCellCalendarColor = Color.GREEN;
						newCell.calCellDurationMinutes = 60;
						newCell.calCellAlliesEmailList
								.add("bob.buffalo@atlaspowered.com");
						newCell.calCellAlliesNameList.add("Bob Buffalo");
						newCell.calCellAlliesImageList.add("Avatar_Tobey.png");
						newCell.calCellAlliesStatusList.add("check");
						newCell.calCellAlliesNameList.add("Joe Chicago");
						newCell.calCellAlliesImageList.add("Avatar_Ashton.png");
						newCell.calCellAlliesStatusList.add("question");
						break;

					case 150:
						newCell.calCellTitle = "Formal Tea";
						newCell.calCellLocation = "Lobby";
						newCell.calCellCalendar = "Blue";
						newCell.calCellCalendarColor = Color.BLUE;
						newCell.calCellDurationMinutes = 45;
						newCell.calCellAlliesEmailList
								.add("joe.chicago@atlaspowered.com");
						newCell.calCellAlliesNameList.add("Joe Chicago");
						newCell.calCellAlliesImageList.add("Avatar_Ashton.png");
						newCell.calCellAlliesStatusList.add("check");
						newCell.calCellAlliesNameList.add("Bob Buffalo");
						newCell.calCellAlliesImageList.add("Avatar_Tobey.png");
						newCell.calCellAlliesStatusList.add("check");
						newCell.calCellAlliesNameList.add("Bob Buffalo");
						newCell.calCellAlliesImageList.add("Avatar_Tobey.png");
						newCell.calCellAlliesStatusList.add("check");
						break;
					} // END SWITCH
					if (x == 100 | x == 101 | x == 102 | x == 103 | x == 110
							| x == 150) {
						// newCell.calCellDurationMinutes = 60 ;
						newCell.calCellAlarm1AdvanceMinutes = 30;
						Calendar cal = new GregorianCalendar(
								TimeZone.getDefault());

						cal.set(cal.get(Calendar.DAY_OF_WEEK) + 1900,
								cal.get(Calendar.MONTH),
								cal.get(Calendar.DATE), 0,
								newCell.calCellAlarm1AdvanceMinutes, 0);
						newCell.calCellAlarm1Datetime = cal.getTime();

					} // END IF
				} // END IF CREATING A SIMULATED APPOITMENT
			} // END FOR LOOP FOR SIMULATED HOURS
		} // END IF SIMULATE
		else {
			// load the cells from the actual database:
			// #OPENTASK
			// Load all events in day
			// this.calListDate = new Date();
			ArrayList<ATLEventModel> allEvents = ATLCalendarManager
					.eventsForDay(mContext, this.calListDate);
			int eventSize = allEvents.size();
			int inactiveSize = CalendarDayView.calendarInActiveNameArray.size();
			int calendarListSize = CalendarDayView.calListModel.size();
			ArrayList<Integer> ignoreList = new ArrayList<Integer>();
			for(int i = 0; i < inactiveSize; i++){
				String calName = CalendarDayView.calendarInActiveNameArray.get(i);
				for(int y = 0; y < calendarListSize; y ++){
					if(calName.trim().toUpperCase().equals(CalendarDayView.calListModel.get(y).name.trim().toUpperCase())){
						ignoreList.add(CalendarDayView.calListModel.get(y).id);
						break;
					}
				}
			}
			
			ArrayList<ATLEventModel>ignoreIndexs = new ArrayList<ATLEventModel>();
			for(int i = 0; i < eventSize; i++){
				
				ATLEventModel event =  allEvents.get(i);
				for(int idx = 0; idx < ignoreList.size(); idx++){
					if(event.calendar_id == ignoreList.get(idx).intValue()){
						ignoreIndexs.add(event);
					}
				}
				
			}
			
			for (ATLEventModel eventModel: ignoreIndexs)
			{
				allEvents.remove(eventModel);
			}
			
			
			for (int i = 0; i < 24; i++) {

				ArrayList<ATLEventModel> eventsInhour = ATLCalendarManager
						.eventsInHourOfDay(allEvents, i, this.calListDate);
				// check: if TRUE=> must add a blank item.
				if (eventsInhour.size() > 0) {

					for (ATLEventModel event : eventsInhour) {
						this.createNewCellWithEvent(event);
					}
				} else {
					this.createBlankCellWithIndex(i, this.calListDate, i);
				}
			}

			// this.sortByDateForCells(this.calListArray);
			// end
		}
		this.calListCount = this.calListArray.size();
		Log.v("ATLCalCellList",
				"this.calListCount =" + this.calListArray.size());
		return true;
	}

	public boolean save() {
		// Not implement yet
		return true;
	}

	public void clear() {
		// Clear the daylist array
		this.calListArray.clear();
		this.calEventsListArray.clear();
	}

	public void addCell(ATLCalCellData calCellData) {
		this.calListArray.add(calCellData);
	}

	public void removeCell(ATLCalCellData calCellData) {
		this.calListArray.remove(calCellData);
	}

	public void loadCalendarEventForToday() {

	}

	public void saveWithCellData(ATLCalCellData cellData) {

	}

	// GETTER AND SETTER

	public ArrayList<Object> getCalListArray() {
		return calListArray;
	}

	public void setCalListArray(ArrayList<Object> calListArray) {
		this.calListArray = calListArray;
	}

	public int getCalListCount() {
		return calListCount;
	}

	public void setCalListCount(int calListCount) {
		this.calListCount = calListCount;
	}

	public Date getCalListDate() {
		return calListDate;
	}

	public void setCalListDate(Date calListDate) {
		this.calListDate = calListDate;
	}

	public boolean isCalListSimulate() {
		return calListSimulate;
	}

	public void setCalListSimulate(boolean calListSimulate) {
		this.calListSimulate = calListSimulate;
	}

	// IMPLEMENT ATLCalCellListInterface METHODS


	public void removeEventWithEventIdentifier(String eventIdentifier) {
		// TODO Auto-generated method stub

	}


	public void addEventWithCellData(ATLCalCellData cellData) {
		// TODO Auto-generated method stub

	}


	public boolean HasEmptyCellInEachHourWithEvents(
			ArrayList<ATLEventModel> events, Date date) {
		// TODO Auto-generated method stub
		Calendar cal = new GregorianCalendar(TimeZone.getDefault());
		cal.setTime(date);
		cal.set(Calendar.MINUTE, 0);

		for (ATLEventModel event : events) {
			// minute == 00
			if (event.dtstart == cal.getTimeInMillis())
				return false;

		}
		return true;
	}


	public void createBlankCellWithHour(int hour, int minute) {
	
		ATLCalCellData newCell = new ATLCalCellData();
		Date firstTime = DateTimeUtilities.firstTimeOfDate(this.calListDate);
		newCell.calCellStartDate = DateTimeUtilities.dateByAddHoursAndMinutes(
				firstTime, hour, minute);
		newCell.calCellPreferDateTime = newCell.calCellStartDate;
		newCell.calCellEndDate = DateTimeUtilities.dateByAddHoursAndMinutes(
				firstTime, hour, minute);
		Calendar cal = Calendar.getInstance();
		cal.setTime(this.calListDate);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
	
		newCell.calCellDate = cal.getTime();
		newCell.calCellCalendarColor = Color.TRANSPARENT;
		newCell.calCellHour = hour;
		newCell.calCellMinute = minute;
		newCell.calCellGUID = this.uuid();
		newCell.calCellTitle = "";
		newCell.calCellLocation = "";
		newCell.calCellEventIdentifier = "";
		newCell.isBlank = true;
		int size = this.calListArray.size();
		for (int index = 0; index < size; index++) {
			ATLCalCellData cellDataOld = (ATLCalCellData) calListArray
					.get(index);
			if (cellDataOld.getCalCellHour() == hour) {
				if (cellDataOld.getCalCellMinute() > minute) {
					calListArray.add(index, newCell);
					break;
				} else if (cellDataOld.getCalCellMinute() == minute) {
					break;
				} else if (hour == 23) {
					if (cellDataOld.getCalCellMinute() > minute) {
						calListArray.add(index, newCell);
						break;
					} else if (cellDataOld.getCalCellMinute() < minute) {
						if (index == size - 1) {
							calListArray.add(newCell);
							break;
						}
						
					} else {
						break;
					}
				}
			} else if (cellDataOld.getCalCellHour() == hour + 1
					&& cellDataOld.getCalCellMinute() == 0) {
				calListArray.add(index, newCell);
				break;
			}
		}
	}

	public void createBlankCellWithIndex(int idx, Date date, int hour) {
		// TODO Auto-generated method stub
		ATLCalCellData newCell = new ATLCalCellData();
		Date firstTime = DateTimeUtilities.firstTimeOfDate(this.calListDate);

		newCell.calCellStartDate = DateTimeUtilities.dateByAddHoursAndMinutes(
				firstTime, hour, 0);
		// 2014-01-24 TAN: ACA_NGHIA lacked 1 line below ====================
		newCell.calCellPreferDateTime = newCell.calCellStartDate;
		// 2014-01-24 TAN: END ==============================================
		newCell.calCellEndDate = DateTimeUtilities.dateByAddHoursAndMinutes(
				firstTime, hour, 0);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, 0);

		newCell.calCellDate = cal.getTime();
		newCell.calCellCalendarColor = Color.TRANSPARENT;
		newCell.calCellHour = hour;
		newCell.calCellMinute = 0;
		newCell.calCellGUID = this.uuid();
		newCell.calCellTitle = "";
		newCell.calCellLocation = "";
		newCell.calCellEventIdentifier = "";

		newCell.isBlank = true;
		this.addCell(newCell);
	}

	public void createNewCellWithEvent(ATLEventModel event) {
		// TODO Auto-generated method stub
		ATLCalCellData newCell = new ATLCalCellData();
		
		newCell.calCellStartDate = new Date(event.dtstart);
		Calendar cal = new GregorianCalendar(TimeZone.getDefault());
		cal.setTime(newCell.calCellStartDate);
		newCell.calCellPreferDateTime = newCell.calCellStartDate;
		newCell.calCellTitle = event.title;
		newCell.calCellDate = cal.getTime();
		newCell.calCellCalendarColor = event.event_color;
		newCell.calCellHour = cal.get(Calendar.HOUR_OF_DAY);
		newCell.calCellMinute = cal.get(Calendar.MINUTE);
		newCell.calCellEndDate = new Date(event.dtend);
		newCell.calCellDurationMinutes = (int) (((event.dtend - event.dtstart)/1000)/60);
		newCell.calCellGUID = this.uuid();
		newCell.calCellLocation = event.event_location;
		newCell.calCellEventIdentifier = "";
		newCell.isBlank = false;
		newCell.calCellEndDate = new Date(event.dtend);
		newCell.calCellNotes = event.description;
		newCell.calendarId = event.calendar_id;
		newCell.eventId = event.event_id; // Fixed move and delete events
		
		//Handle Event Group
		ATLEventGroupModel eventGroup = ATLEventGroupDatabaseAdapter.getEventGroupIdOfEventId(event.event_id);
		if(eventGroup != null){
			newCell.calCellEventGroupID = eventGroup.calCellGroupEventID;
			newCell.calCellEventIdentifier = ATLEventGroupDatabaseAdapter.getEventIDFromIDString(eventGroup.calCellEventIdentifier); //= id:startTime
			newCell.calCellPreferDateTime = ATLEventGroupDatabaseAdapter.getDateFromIDString(eventGroup.calCellEventIdentifier);
			
			newCell.calCellAlt2EventIdentifier = ATLEventGroupDatabaseAdapter.getEventIDFromIDString(eventGroup.calCellAlt2EventIdentifier);//= id1:altTime1
			newCell.calCellALT2Datetime = ATLEventGroupDatabaseAdapter.getDateFromIDString(eventGroup.calCellAlt2EventIdentifier);
			
			newCell.calCellAlt3EventIdentifier = ATLEventGroupDatabaseAdapter.getEventIDFromIDString(eventGroup.calCellAlt3EventIdentifier);//= id2:altTime2
			newCell.calCellALT3Datetime = ATLEventGroupDatabaseAdapter.getDateFromIDString(eventGroup.calCellAlt3EventIdentifier);
			newCell.eventResponseType_CellData = eventGroup.eventRespondStatus;
		}
		this.addCell(newCell);
		this.calEventsListArray.add(newCell);
	
	}

	public void sortByDateForCells(ArrayList<Object> cells) {
		// TODO Auto-generated method stub

	}


	public ArrayList<ATLEventModel> cellSortByDateForCells(
			ArrayList<ATLEventModel> cells) {
		// TODO Auto-generated method stub
		return null;
	}


	public String uuid() {
		// TODO Auto-generated method stub
		return null;
	}


	public void currentDateDidChanged(Date date) {
		// TODO Auto-generated method stub
		this.calListDate = date;
		this.load(mContext);
	}

}
