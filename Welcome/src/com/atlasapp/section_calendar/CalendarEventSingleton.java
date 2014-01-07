//  ==================================================================================================================
//  CalendarEventSingleton.java
//  AtlasConsumerAndroid
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-11-13 NGHIA:    Create
//  ==================================================================================================================

package com.atlasapp.section_calendar;

/**
 * @author nghia
 *
 */
public class CalendarEventSingleton {

	/**
	 * 
	 */
	private static CalendarEventSingleton instance;
	private ATLCalCellData calCellData = new ATLCalCellData();
	public static CalendarEventSingleton getInstance(){
		if(instance == null){
			instance = new CalendarEventSingleton();
		}
		return instance;
	}
	
	public void setCalCellData(ATLCalCellData calCellData){
		this.calCellData = calCellData;
	}
	
	public ATLCalCellData getCalCellData(){
		return calCellData;
	}

}
