//  ==================================================================================================================
//  ATLTodayCellData.java
//  AtlasConsumerAndroid
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-10-29 TAN:     Inti class, implement contructor ATLTodayCellData() method
//  ==================================================================================================================
package com.atlasapp.section_today;

import java.util.ArrayList;
import java.util.Date;

import com.atlasapp.common.ATLConstants.TodayCellType;
import com.atlasapp.model.ATLAttendeeModel;

import android.graphics.Color;



public class ATLTodayCellData {

	// PROPERTIES

	// Cell identifier
	public String todayCellGUID;
	public TodayCellType todayCellType ;

	// Cell date
	public Date todayCellDate;

	// Cell labels
	String todayCellTitle; // Event (appointment) title
	String todayCellLocation; // Event (appointment) location
	String todayCellCalendar; // Calendar, ie work, home, etc.
	public int todayCellCalendarColor; // Calendar color

	// Cell allies
	public ArrayList<Object> todayCellAlliesAtlasIDList;
	public ArrayList<Object> todayCellAlliesEmailList;
	public ArrayList<Object> todayCellAlliesNameList;
	public ArrayList<Object> todayCellAlliesImageList;
	public ArrayList<Object> todayCellAlliesStatusList;

	// Cell Attendee ADDED 2012-05-25 NGHIA
	public ATLAttendeeModel attendee;

	// METHODS

	public ATLTodayCellData() {
		// TODO Auto-generated constructor stub
		  this.todayCellCalendarColor         =       Color.TRANSPARENT ;
		  this.todayCellAlliesAtlasIDList     =      new ArrayList<Object>(); 
		  this.todayCellAlliesEmailList       =      new ArrayList<Object>();
		  this.todayCellAlliesNameList        =      new ArrayList<Object>();
		  this.todayCellAlliesImageList       =      new ArrayList<Object>();
		  this.todayCellAlliesStatusList      =      new ArrayList<Object>();  
		  this.attendee                       =      new ATLAttendeeModel();
	}

}
