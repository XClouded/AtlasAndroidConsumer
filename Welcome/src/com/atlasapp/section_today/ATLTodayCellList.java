//  ==================================================================================================================
//  ATLTodayCellList.java
//  AtlasConsumerAndroid
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-10-29 TAN:     Inti class, implement load(), clear(), currentDateDidChanged() method
//  ==================================================================================================================

package com.atlasapp.section_today;

import java.util.ArrayList;
import java.util.Date;

import com.atlasapp.section_calendar.ATLCalCellList;
import com.atlasapp.section_notes.ATLNoteList;
import com.atlasapp.section_tasks.ATLTaskCellList;


import android.content.Context;

public class ATLTodayCellList {

	public ArrayList<Object> todayListArray;
	public int todayListCount = 0;;
	public Date todayListDate;
	public boolean todayListSimulate;
	Context mContext;

	public ATLCalCellList mCalCellList;
	public ATLNoteList mNotesCellList;
	public ATLTaskCellList mTaskCellList;

	public ATLTodayCellList(Context ctx) {
		// TODO Auto-generated constructor stub
		// SIMULATION: TURN OFF WHEN REAL DATA IS AVAILABLE
		// ==================================<<<<<<
		// WHEN ON, FAKE CELL DATA WILL BE CREATED
		this.todayListSimulate = true;
		mContext = ctx;
		this.todayListArray = new ArrayList<Object>();
		this.todayListDate = new Date();
		
		mCalCellList = new ATLCalCellList();
		mTaskCellList = new ATLTaskCellList();
		mNotesCellList = new ATLNoteList();
	}

	public void load() {
		if (this.todayListSimulate) {
			this.clear();
			mCalCellList.setCalListDate(this.todayListDate);
			mCalCellList.load(mContext);

			mTaskCellList.taskListDate = this.todayListDate;
			mTaskCellList.load();

			mNotesCellList.noteListDate = this.todayListDate;
			mNotesCellList.load();
			
			todayListArray.addAll(mCalCellList.calEventsListArray);
			todayListArray.addAll(mTaskCellList.taskListArray);
			todayListArray.addAll(mNotesCellList.noteListArray);

		} // END IF SIMULATE
		else {
			// load the cells from the actual database:
			// #OPENTASK
		}

		// [this sortByDateForCells:this.todayListArray];
		this.todayListCount = mNotesCellList.noteListArray.size()
				+ mCalCellList.calEventsListArray.size()
				+ mTaskCellList.taskListArray.size();
	}

	public void clear() {
		this.todayListArray.clear();
		this.mCalCellList.clear();
		this.mNotesCellList.clear();
		this.mTaskCellList.clear();
	}
	
	public void currentDateDidChanged(Date currentDate) {
		// TODO Auto-generated method stub
		this.todayListDate = currentDate;
//		this.clear();
//		this.load();
	}
}
