//  ==================================================================================================================
//  ATLTaskCellList.java
//  AtlasConsumerAndroid
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-10-28 TAN:    Init class, add all atribute as iPhone
//					   implement constructor, load()-Simulate mode only, addCell(ATLTaskCellData cellData) and clear() method
//  ==================================================================================================================

package com.atlasapp.section_tasks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import android.graphics.Color;

import com.atlasapp.common.ATLConstants;
import com.atlasapp.common.ATLConstants.TaskResponseType;
import com.atlasapp.common.CalendarUtilities;
import com.atlasapp.model.ATLCalendarModel;
import com.atlasapp.model.ATLTaskModel;

public class ATLTaskCellList {

	// PROPERTIES
	public ArrayList<Object> taskListArray;
	public int taskListCount;
	public Date taskListDate;
	public boolean taskListSimulate;

	public ArrayList<Object> taskPriorityListArray;
	public ArrayList<Object> taskDueDateListArray;
	public ArrayList<Object> taskCalendarListArray;
	public ArrayList<Object> taskAssigneeListArray;
	public ArrayList<Object> taskCompletedListArray;

	public ArrayList<Object> taskListMustDoArray;
	public ArrayList<Object> taskListVeryImportantArray;
	public ArrayList<Object> taskListImportantArray;
	public ArrayList<Object> taskListCompletedArray;

	public static String[] GROUP_HEADER_PRIORITY = { "High", "Medium", "Low" };
	// public static String[] GROUP_HEADER_CALENDAR = { "High", "Medium", "Low"
	// };
	public static String[] GROUP_HEADER_DUEDATE = { "Due Today", "Due Tomorrow",
			"Due Future" };
	// public static String[] GROUP_HEADER_DELEGATED = { "High", "Medium", "Low"
	// };

	public String[] headerList;

	public ArrayList<ATLTaskCellData> taskPriorityHighArray = new ArrayList<ATLTaskCellData>();
	public ArrayList<ATLTaskCellData> taskPriorityMediumArray = new ArrayList<ATLTaskCellData>();
	public ArrayList<ATLTaskCellData> taskPriorityLowArray = new ArrayList<ATLTaskCellData>();

	public ArrayList<ATLTaskCellData> taskDueTodayArray = new ArrayList<ATLTaskCellData>();
	public ArrayList<ATLTaskCellData> taskDueTomorrowArray = new ArrayList<ATLTaskCellData>();
	public ArrayList<ATLTaskCellData> taskDueFutureArray = new ArrayList<ATLTaskCellData>();
	public ArrayList<ArrayList<ATLTaskCellData>> taskCalendarArray;
	public ArrayList<ArrayList<ATLTaskCellData>> taskDelegatedNameArray;

	// public ArrayList<Object> taskDueTodayArray;
	// public ArrayList<Object> taskDueTomorrowArray;
	// public ArrayList<Object> taskDueFutureArray;

	// METHODS
	public ATLTaskCellList() {
		// TODO Auto-generated constructor stub
		// SIMULATION: TURN OFF WHEN REAL DATA IS AVAILABLE
		// ==================================<<<<<<
		// WHEN ON, FAKE CELL DATA WILL BE CREATED
		this.taskListSimulate = false; // get data from gTask
		this.taskListArray = new ArrayList<Object>();
		this.taskPriorityListArray = new ArrayList<Object>();
		this.taskDueDateListArray = new ArrayList<Object>();
		this.taskCalendarListArray = new ArrayList<Object>();
		this.taskAssigneeListArray = new ArrayList<Object>();
		this.taskCompletedListArray = new ArrayList<Object>();

		this.taskListImportantArray = new ArrayList<Object>();
		this.taskListMustDoArray = new ArrayList<Object>();
		this.taskListVeryImportantArray = new ArrayList<Object>();
		this.taskListCompletedArray = new ArrayList<Object>();
		this.taskListDate = new Date();

	}

	public boolean load() {
		// load data for one day
		if (this.taskListSimulate) {
			createFakeDataForSimulateMode();

		} // END IF SIMULATE
		else {
			// load the cells from the actual database:
			// #OPENTASK
			// TODO: load all task from database and addCell
			TaskDatabaseAdapter dbHelper = new TaskDatabaseAdapter();
			ArrayList<ATLTaskModel> list = new ArrayList<ATLTaskModel>();
			list = dbHelper.loadAllTasksInDatabase();

			for (ATLTaskModel atlTask : list) {
				ATLTaskCellData newCell = new ATLTaskCellData(atlTask);
				newCell.createSortString(ATLTaskSortSingleton.sortIndex);
				//Log.v("ATLTaskCellList","SORT STRING: " + newCell.taskCellSortString);
				this.taskListArray.add(newCell);
			}

			buildHeaderList(this.taskListArray);
			taskListArray.clear();
			for (ATLTaskModel atlTask : list) {

				ATLTaskCellData newCell = new ATLTaskCellData(atlTask);
				newCell.createSortString(ATLTaskSortSingleton.sortIndex);
				switch (newCell.taskCellPriorityInt) {
				case 0:
					newCell.taskCellPriority = ATLConstants.kAtlasTaskPriorityHigh;
					break;
				case 1:
					newCell.taskCellPriority = ATLConstants.kAtlasTaskPriorityMedium;
					break;
				case 2:
					newCell.taskCellPriority = ATLConstants.kAtlasTaskPriorityLow;
					break;
				}
				if(!newCell.taskCellIsDeleted){
					if (!ATLTaskSortSingleton.isShowCompletedTasks) {
						if (!newCell.taskCellIsCompleted) {
							this.addCell(newCell);
						}
					} else {
						this.addCell(newCell);
					}
				}
			
			}
			sort();

		}
		
		

		this.taskListCount = this.taskListArray.size();
		
		if (this.taskListSimulate){
			for (int i = 0; i < taskListCount; i++){
				ATLTaskCellData cellData = (ATLTaskCellData)taskListArray.get(i);
				int mode = i % 6;
				switch(mode){
				case TaskResponseType.taskResponseType_None:
					cellData.taskResponseStatus = TaskResponseType.taskResponseType_None;
					 break;
				case TaskResponseType.taskResponseType_NoAssignees:
					cellData.taskResponseStatus = TaskResponseType.taskResponseType_NoAssignees;
					break;
				case TaskResponseType.taskResponseType_AssignmentsPending:
					cellData.taskResponseStatus = TaskResponseType.taskResponseType_AssignmentsPending;
					break;
				case TaskResponseType.taskResponseType_AssigneeDeclined:
					cellData.taskResponseStatus = TaskResponseType.taskResponseType_AssigneeDeclined;
					break;
				case TaskResponseType.taskResponseType_AssigneeAccepted:
					cellData.taskResponseStatus = TaskResponseType.taskResponseType_AssigneeAccepted;
					break;
				case TaskResponseType.taskResponseType_AssigneeCompleted:
					cellData.taskResponseStatus = TaskResponseType.taskResponseType_AssigneeCompleted;
					break;	
				}
				
			}
		}

		return true;
	}

	private void createFakeDataForSimulateMode() {
		// fake the cells
		ATLTaskCellData newCell1 = new ATLTaskCellData();

//			newCell1.taskCellPriority = TaskCellPriority.priority_MustDo;

		newCell1.taskCellDueDate = this.taskListDate;
		// newCell1.taskCellHour = 5;
		// newCell1.taskCellMinute = 30 ;
		newCell1.taskCellCalendarColor = Color.RED;
		newCell1.taskCellTitle = "Save World";
		newCell1.taskCellDescription = "The world needs saving so get busy!";
		// newCell1.taskCellAlliesEmailList addObject:
		// "joe.chicagoatlaspowered.com" ;
		// newCell1.taskCellAlliesNameList addObject: "Joe Chicago" ;
		// newCell1.taskCellAlliesImageList addObject:AVATAR_MAN;// ADDED
		// 2012-03-06 HR
		// newCell1.taskCellAlliesStatusList addObject:"OK";// ADDED
		// 2012-03-06 HR
		this.addCell(newCell1);

		ATLTaskCellData newCell2;
		newCell2 = new ATLTaskCellData();
//			newCell2.taskCellPriority = TaskCellPriority.priority_MustDo;
		newCell2.taskCellCalendarColor = Color.RED;

		newCell2.taskCellDueDate = this.taskListDate;
		// newCell2.taskCellHour = 5;
		// newCell2.taskCellMinute = 30 ;
		newCell2.taskCellTitle = "Hello World";
		newCell2.taskCellDescription = "The world is awesome!";
		// newCell2.taskCellAlliesEmailList addObject:
		// "joe.chicagoatlaspowered.com" ;
		// newCell2.taskCellAlliesNameList addObject: "Joe Chicago" ;
		// newCell2.taskCellAlliesImageList addObject:AVATAR_WOMAN;// ADDED
		// 2012-03-06 HR
		// newCell2.taskCellAlliesStatusList addObject:"abc";// ADDED
		// 2012-03-06 HR
		this.addCell(newCell2);

		// fake the cells
		ATLTaskCellData newCell3;
		newCell3 = new ATLTaskCellData();

		newCell3.taskCellDueDate = this.taskListDate;
		// newCell3.taskCellHour = 5;
		// newCell3.taskCellMinute = 30 ;
//			newCell3.taskCellPriority = TaskCellPriority.priority_VeryImportant;
		newCell3.taskCellCalendarColor = Color.RED;
		newCell3.taskCellTitle = "save to calendar title";
		newCell3.taskCellDescription = "use sqlite";
		// newCell3.taskCellAlliesEmailList addObject:
		// "richardatlaspowered.com" ;
		// newCell3.taskCellAlliesNameList addObject: "Richard Grossman" ;
		// newCell3.taskCellAlliesImageList addObject:AVATAR_MAN;// ADDED
		// 2012-03-06 HR
		// newCell3.taskCellAlliesStatusList addObject:"OK";// ADDED
		// 2012-03-06 HR
		this.addCell(newCell3);

		ATLTaskCellData newCell4;
		newCell4 = new ATLTaskCellData();
		newCell4.taskCellDueDate = this.taskListDate;
		// newCell4.taskCellHour = 5;
		// newCell4.taskCellMinute = 30 ;
//			newCell4.taskCellPriority = TaskCellPriority.priority_VeryImportant;
		newCell4.taskCellCalendarColor = Color.GREEN;
		newCell4.taskCellTitle = "save to calendar";
		newCell4.taskCellDescription = "use coredata";
		// newCell4.taskCellAlliesEmailList addObject:
		// "nghiaatlaspowered.com" ;
		// newCell4.taskCellAlliesNameList addObject: "Nghia Truong" ;
		// newCell4.taskCellAlliesImageList addObject:AVATAR_MAN;// ADDED
		// 2012-03-06 HR
		// newCell4.taskCellAlliesStatusList addObject:"OK";// ADDED
		// 2012-03-06 HR
		this.addCell(newCell4);

		// fake the cells
		ATLTaskCellData newCell5;
		newCell5 = new ATLTaskCellData();
		newCell5.taskCellDueDate = this.taskListDate;
		// newCell5.taskCellHour = 5;
		// newCell5.taskCellMinute = 30 ;
		newCell5.taskCellTitle = "Urgent";
		newCell5.taskCellDescription = "Task View";
//			newCell5.taskCellPriority = TaskCellPriority.priority_VeryImportant;
		newCell5.taskCellCalendarColor = Color.CYAN;
		// newCell5.taskCellAlliesEmailList addObject:
		// "joe.chicagoatlaspowered.com" ;
		// newCell5.taskCellAlliesNameList addObject: "Joe Chicago" ;
		// newCell5.taskCellAlliesImageList addObject:AVATAR_MAN;// ADDED
		// 2012-03-06 HR
		// newCell5.taskCellAlliesStatusList addObject:"OK";// ADDED
		// 2012-03-06 HR
		this.addCell(newCell5);

		ATLTaskCellData newCell6;
		newCell6 = new ATLTaskCellData();
		newCell6.taskCellDueDate = this.taskListDate;
		// newCell6.taskCellHour = 5;
		// newCell6.taskCellMinute = 30 ;
		newCell6.taskCellTitle = "very Urgent";
		newCell6.taskCellDescription = "Task edit";
//			newCell6.taskCellPriority = TaskCellPriority.priority_Complete;
		newCell6.taskCellCalendarColor = Color.RED;
		// newCell6.taskCellAlliesEmailList addObject:
		// "joe.chicagoatlaspowered.com" ;
		// newCell6.taskCellAlliesNameList addObject: "Joe Chicago" ;
		// newCell6.taskCellAlliesImageList addObject:AVATAR_WOMAN;// ADDED
		// 2012-03-06 HR
		// newCell6.taskCellAlliesStatusList addObject:"abc";// ADDED
		// 2012-03-06 HR
		this.addCell(newCell6);

		// fake the cells
		ATLTaskCellData newCell7;
		newCell7 = new ATLTaskCellData();
//			newCell7.taskCellPriority = TaskCellPriority.priority_MustDo;
		newCell7.taskCellIsCompleted = true;
		newCell7.taskCellDueDate = this.taskListDate;
		newCell7.taskCellCalendarColor = Color.RED;
		// newCell7.taskCellHour = 5;
		// newCell7.taskCellMinute = 30 ;
		newCell7.taskCellTitle = "Urgent";
		newCell7.taskCellDescription = "Task View";
		// newCell7.taskCellAlliesEmailList addObject:
		// "joe.chicagoatlaspowered.com" ;
		// newCell7.taskCellAlliesNameList addObject: "Joe Chicago" ;
		// newCell7.taskCellAlliesImageList addObject:AVATAR_MAN;// ADDED
		// 2012-03-06 HR
		// newCell7.taskCellAlliesStatusList addObject:"OK";// ADDED
		// 2012-03-06 HR
		this.addCell(newCell7);

		ATLTaskCellData newCell8;
		newCell8 = new ATLTaskCellData();
//			newCell8.taskCellPriority = TaskCellPriority.priority_Important;
		newCell8.taskCellIsCompleted = true;
		newCell8.taskCellDueDate = this.taskListDate;
		newCell8.taskCellCalendarColor = Color.GREEN;
		// newCell8.taskCellHour = 5;
		// newCell8.taskCellMinute = 30 ;
		newCell8.taskCellTitle = "very Urgent";
		newCell8.taskCellDescription = "Task edit";
		// newCell8.taskCellAlliesEmailList addObject:
		// "joe.chicagoatlaspowered.com" ;
		// newCell8.taskCellAlliesNameList addObject: "Joe Chicago" ;
		// newCell8.taskCellAlliesImageList addObject:AVATAR_WOMAN;// ADDED
		// 2012-03-06 HR
		// newCell8.taskCellAlliesStatusList addObject:"abc";// ADDED
		// 2012-03-06 HR
		this.addCell(newCell8);
	}

	private void sort() {
		// TODO Auto-generated method stub
		switch (ATLTaskSortSingleton.getGroupKind()) {
		case ATLTaskCellData.TASK_SORT_PRIORITY: {

			Collections.sort(taskPriorityHighArray,
					new ATLTaskCellDataComparator());
			Collections.sort(taskPriorityMediumArray,
					new ATLTaskCellDataComparator());
			Collections.sort(taskPriorityLowArray,
					new ATLTaskCellDataComparator());

		}
			break;
		case ATLTaskCellData.TASK_SORT_CALENDAR: {
			taskListArray.clear();
			for (ArrayList<ATLTaskCellData> taskArr : taskCalendarArray) {
				Collections.sort(taskArr, new ATLTaskCellDataComparator());
				taskListArray.addAll(taskArr);
			}

		}
			break;
		case ATLTaskCellData.TASK_SORT_DUEDATE: {

			Collections
					.sort(taskDueTodayArray, new ATLTaskCellDataComparator());
			Collections.sort(taskDueTomorrowArray,
					new ATLTaskCellDataComparator());
			Collections.sort(taskDueFutureArray,
					new ATLTaskCellDataComparator());
		}
			break;
		case ATLTaskCellData.TASK_SORT_DELEGATED: {
			taskListArray.clear();
			for (ArrayList<ATLTaskCellData> taskArr : taskDelegatedNameArray) {
				Collections.sort(taskArr, new ATLTaskCellDataComparator());
				taskListArray.addAll(taskArr);
			}
		}
			break;
		}

	}

	private String[] buildHeaderList(ArrayList<Object> taskArr) {
		switch (ATLTaskSortSingleton.getGroupKind()) {
		case ATLTaskCellData.TASK_SORT_PRIORITY:
			headerList = GROUP_HEADER_PRIORITY;
			break;
		case ATLTaskCellData.TASK_SORT_CALENDAR: {
			int index = 0;
			Collections.sort(ATLTaskListActivity.calListModel,
					new ATLCalendarModelComparator());// Sort calendar Name
			headerList = new String[ATLTaskListActivity.calListModel.size()];
			for (ATLCalendarModel atlCal : ATLTaskListActivity.calListModel) {
				headerList[index] = atlCal.name;
				index++;
			}
			// Init array
			int numOfCals = ATLTaskListActivity.calListModel.size();
			taskCalendarArray = new ArrayList<ArrayList<ATLTaskCellData>>();
			for (int i = 0; i < numOfCals; i++) {
				ArrayList<ATLTaskCellData> calArr = new ArrayList<ATLTaskCellData>();
				taskCalendarArray.add(calArr);
			}

		}
			break;
		case ATLTaskCellData.TASK_SORT_DUEDATE:
			headerList = GROUP_HEADER_DUEDATE;
			break;
		case ATLTaskCellData.TASK_SORT_DELEGATED: {
			// Collections.sort(Tasks.calListModel,
			// new ATLTaskCellDataComparator());// Sort calendar Name

			ArrayList<String> headerArrList = new ArrayList<String>();
			for (Object atlCal : taskArr) {
				String delegatedName = ((ATLTaskCellData) atlCal).taskCellReceiverName;
				if (delegatedName.length() == 0) {
					delegatedName = "Not Delegated";
				}
				int count = 0;
				for (String name : headerArrList) {

					if (delegatedName.equals(name)) {
						count++;
					}

				}
				if (count == 0) {

					headerArrList.add(delegatedName);
				}

			}
			Collections.sort(headerArrList, new IgnoreStringCaseComparator());
			headerArrList.remove("Not Delegated");
			headerArrList.add("Not Delegated");// Add to the end of list
			int numOfName = headerArrList.size();
			headerList = new String[numOfName];
			for (int index = 0; index < numOfName; index++) {
				headerList[index] = headerArrList.get(index);
			}

			taskDelegatedNameArray = new ArrayList<ArrayList<ATLTaskCellData>>();
			for (int i = 0; i < numOfName; i++) {
				ArrayList<ATLTaskCellData> calArr = new ArrayList<ATLTaskCellData>();
				taskDelegatedNameArray.add(calArr);
			}
		}
			break;
		}
		return headerList;
	}

	private void addCell(ATLTaskCellData taskCell) {
		// TODO Auto-generated method stub
		this.taskListArray.add(taskCell);

		switch (ATLTaskSortSingleton.getGroupKind()) {
		case ATLTaskCellData.TASK_SORT_PRIORITY: {
			switch (taskCell.taskCellPriorityInt) {
			case ATLTaskPriorityScrollListAdapter.TASK_PRIORITY_INDEX_HIGH:
				taskPriorityHighArray.add(taskCell);
				break;
			case ATLTaskPriorityScrollListAdapter.TASK_PRIORITY_INDEX_MEDIUM:
				taskPriorityMediumArray.add(taskCell);
				break;
			case ATLTaskPriorityScrollListAdapter.TASK_PRIORITY_INDEX_LOW:
				taskPriorityLowArray.add(taskCell);
				break;
			}

		}
			break;
		case ATLTaskCellData.TASK_SORT_CALENDAR: {

			int idx = 0;
			for (String calName : headerList) {
				if (taskCell.taskCellCalendarName.trim().toUpperCase()
						.equals(calName.trim().toUpperCase())) {
					taskCalendarArray.get(idx).add(taskCell);
				}
				idx++;
			}
		}
			break;
		case ATLTaskCellData.TASK_SORT_DUEDATE: {
			if (CalendarUtilities.isToday(taskCell.taskCellDueDate)
					|| CalendarUtilities.isPast(taskCell.taskCellDueDate)) {
				taskDueTodayArray.add(taskCell);
			} else if (CalendarUtilities.isTomorrow(taskCell.taskCellDueDate)) {
				taskDueTomorrowArray.add(taskCell);
			} else {
				taskDueFutureArray.add(taskCell);
			}
		}
			break;
		case ATLTaskCellData.TASK_SORT_DELEGATED: {
			int idx = 0;
			for (String calName : headerList) {
				if (taskCell.taskCellReceiverName.trim().toUpperCase()
						.equals(calName.trim().toUpperCase())) {
					taskDelegatedNameArray.get(idx).add(taskCell);
				} else if (calName.equals("Not Delegated")
						&& taskCell.taskCellReceiverName.trim().equals("")) {
					taskDelegatedNameArray.get(idx).add(taskCell);
				}
				idx++;
			}

		}
			break;
		}

	}

	public class ATLTaskCellDataComparator implements
			Comparator<ATLTaskCellData> {
		@Override
		public int compare(ATLTaskCellData o1, ATLTaskCellData o2) {
			return o1.taskCellSortString.compareTo(o2.taskCellSortString);
		}
	}

	public class ATLCalendarModelComparator implements
			Comparator<ATLCalendarModel> {
		@Override
		public int compare(ATLCalendarModel o1, ATLCalendarModel o2) {
			return o1.name.trim().toUpperCase()
					.compareTo(o2.name.trim().toUpperCase());
		}
	}

	public class IgnoreStringCaseComparator implements Comparator<String> {
		@Override
		public int compare(String o1, String o2) {
			return o1.trim().toUpperCase().compareTo(o2.trim().toUpperCase());
		}
	}

	public void clear() {
		// TODO Auto-generated method stub
		this.taskListArray.clear();
		this.taskListMustDoArray.clear();
		this.taskListVeryImportantArray.clear();
		this.taskListImportantArray.clear();
		this.taskListCompletedArray.clear();
	}

	public void currentDateDidChanged(Date currentDate) {
		// TODO Auto-generated method stub
		this.taskListDate = currentDate;
		this.load();
	}
}
