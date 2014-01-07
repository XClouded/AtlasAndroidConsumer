//  ==================================================================================================================
//  ATLTaskCellData.java
//  AtlasConsumerAndroid
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-10-27 TAN:    Init class, add all atribute as iPhone
//					   implement constructor
//  ==================================================================================================================

package atlasapp.section_tasks;

import java.text.SimpleDateFormat;
import java.util.Date;


import android.graphics.Color;
import atlasapp.common.ATLConstants.TASK_CATEGORY;
import atlasapp.common.ATLConstants.TaskResponseType;
import atlasapp.common.Utilities;
import atlasapp.model.ATLContactModel;
import atlasapp.model.ATLTaskModel;

public class ATLTaskCellData {
	static public final int TASK_SORT_PRIORITY = 0;
	static public final int TASK_SORT_CALENDAR = 1;
	static public final int TASK_SORT_DUEDATE = 2;
	static public final int TASK_SORT_DELEGATED = 3;

	// PROPERTIES
	public int taskResponseStatus = TaskResponseType.taskResponseType_None;
	public int taskCellId;
	public String taskCellUuid; // Task identifier from database
	public String taskCellAtlasID;
	public Date taskCellDueDate;
	public Date taskCellCompletedDate;
	public Date taskCellModifiedDate;
	public Date taskCellCreatedDate;
	public int taskCellRemider;
	public String taskCellTitle;
	public String taskCellDescription;
	public String taskCellNote;
	public String taskCellPriority;
	public int taskCellPriorityInt;
	public String taskCellStatus;

	public String taskCellCalendarName;
	public int taskCellCalendarColor = Color.TRANSPARENT;
	public int calendarId = -1;

	public boolean taskCellIsCompleted;

	public ATLContactModel taskCellAttendee;
	// /SHARON ADDING...
	public TASK_CATEGORY taskCategory;
	// ////
	public String taskCellSortString;
	public boolean isShowDelete = false;

	public String taskCellSenderId = "";
	public String taskCellSenderEmail = "";
	public String taskCellSenderName = "";
	public String taskCellReceiverId = "";
	public String taskCellReceiverEmail = "";
	public String taskCellReceiverName = "ZZZZNotDelegated";
	public boolean taskCellIsDeleted = false;

	// METHODS

	public ATLTaskCellData() {
		// TODO Auto-generated constructor stub
		this.taskCellUuid = "";
		this.taskCellIsCompleted = false;
		this.taskCellSortString = "";
	}

	public ATLTaskCellData(ATLTaskModel atlTask) {
		// TODO Auto-generated constructor stub
		taskResponseStatus = atlTask.taskResponseStatus;
		taskCellId = atlTask.taskId;
		taskCellUuid = atlTask.taskUuid;
		taskCellAtlasID = atlTask.taskAtlasId;
		taskCellSortString = "";

		taskCellDueDate = atlTask.taskDueDate;
		taskCellCompletedDate = atlTask.taskCompletedDate;
		taskCellModifiedDate = atlTask.taskModifieDate;
		taskCellCreatedDate = atlTask.taskCreatedDate;
		taskCellRemider = atlTask.taskReminder;
		taskCellTitle = atlTask.taskTitle;
		taskCellDescription = atlTask.taskDescription;
		taskCellNote = atlTask.taskNote;
		taskCellPriority = atlTask.taskPriority;
		taskCellPriorityInt = atlTask.taskPriorityInt;
		;
		taskCellStatus = atlTask.taskStatus;
		taskCellCalendarColor = atlTask.taskCalendarColor;
		taskCellCalendarName = atlTask.taskCalendarName;

		taskCategory = atlTask.taskCategory;
		taskCellIsCompleted = atlTask.taskIsCompleted;

		taskCellSenderId = atlTask.taskSenderId;
		taskCellSenderEmail = atlTask.taskSenderEmail;
		taskCellSenderName = atlTask.taskSenderName;
		taskCellReceiverId = atlTask.taskReceiverId;
		taskCellReceiverEmail = atlTask.taskReceiverEmail;
		taskCellReceiverName = atlTask.taskReceiverName;
		taskCellIsDeleted = atlTask.taskIsDeleted;

		// 2012-12-28 TAN - START : face data delegate name ============
		String taskDelegatedName = null;
		taskCellDescription = atlTask.taskDescription;
		if (atlTask.taskDescription != null) {
			int subStringIndex = atlTask.taskDescription.indexOf("DueTime:");

			if (subStringIndex != -1) {
				// dueTimeString =
				// atlTask.taskDescription.substring(subStringIndex)
				// .trim();
				taskCellDescription = atlTask.taskDescription.substring(0,
						subStringIndex);
				String[] atlasData = atlTask.taskDescription.substring(
						subStringIndex).split("\n");
				try {
					if (atlasData.length > 0) {

						taskDelegatedName = atlasData[4];
						taskDelegatedName = taskDelegatedName.replace(
								"Delegated:", "");
						this.taskCellReceiverName = taskDelegatedName;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else {
				taskCellDescription = atlTask.taskDescription;
			}
		}
		// 2012-12-28 TAN - END : face data delegate name ============

		// taskCategory = ATLTaskModel.taskCategory;
	}

	public ATLTaskCellData copy() {
		// TODO Auto-generated method stub
		ATLTaskCellData temp = new ATLTaskCellData();

		temp.taskResponseStatus = this.taskResponseStatus;
		temp.taskCellId = this.taskCellId;
		temp.taskCellUuid = this.taskCellUuid;
		temp.taskCellAtlasID = this.taskCellAtlasID;
		temp.taskCellSortString = "";

		temp.taskCellDueDate = this.taskCellDueDate;
		temp.taskCellCompletedDate = this.taskCellCompletedDate;
		temp.taskCellModifiedDate = this.taskCellModifiedDate;
		temp.taskCellCreatedDate = this.taskCellCreatedDate;
		temp.taskCellRemider = this.taskCellRemider;
		temp.taskCellTitle = this.taskCellTitle;
		temp.taskCellDescription = this.taskCellDescription;
		temp.taskCellNote = this.taskCellNote;
		temp.taskCellPriority = this.taskCellPriority;
		temp.taskCellPriorityInt = this.taskCellPriorityInt;
		temp.taskCellStatus = this.taskCellStatus;
		temp.taskCellCalendarName = this.taskCellCalendarName;
		temp.taskCellCalendarColor = this.taskCellCalendarColor;
		temp.taskCellIsCompleted = this.taskCellIsCompleted;
		temp.taskCellAttendee = this.taskCellAttendee;
		temp.taskCategory = this.taskCategory;
		temp.isShowDelete = this.isShowDelete;

		temp.taskCellSenderId = this.taskCellSenderId;
		temp.taskCellSenderEmail = this.taskCellSenderEmail;
		temp.taskCellSenderName = this.taskCellSenderName;
		temp.taskCellReceiverId = this.taskCellReceiverId;
		temp.taskCellReceiverEmail = this.taskCellReceiverEmail;
		temp.taskCellReceiverName = this.taskCellReceiverName;
		temp.taskCellIsDeleted = this.taskCellIsDeleted;
		return temp;
	}

	private String checkSortBy(int p, String a) {
		switch (p) {
		case TASK_SORT_PRIORITY:
			a = a + taskCellPriorityInt;
			break;
		case TASK_SORT_CALENDAR: {
			// a = a + taskCellCalendarName.substring(0,4).trim();
			int lengh = taskCellCalendarName.trim().length();
			if (lengh > 4) {
				a = a + taskCellCalendarName.trim().substring(0, 4);
			} else {
				a = a + taskCellCalendarName.trim();
				for (int i = 0; i < (4 - lengh); i++) {
					a = a + "A";
				}
			}
		}
			break;
		case TASK_SORT_DUEDATE:
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
			if (taskCellDueDate != null) {

				a = a + sf.format(taskCellDueDate);
			}

			break;
		case TASK_SORT_DELEGATED: {
			// a = a + taskDelegatedName.substring(0,4).trim();
			// a = a + taskDelegatedName.trim();
			int lengh = taskCellReceiverName.trim().length();
			if (lengh >= 4) {
				a = a + taskCellReceiverName.trim().substring(0, 4);
			} else {
				a = a + taskCellReceiverName.trim();
				for (int i = 0; i < (4 - lengh); i++) {
					a = a + "Z";
				}
			}
		}
			break;

		}
		return a;

	}

	public String createSortString(int[] a) {
		taskCellSortString = "";
		for (int i : a) {
			taskCellSortString = checkSortBy(i, taskCellSortString);
		}
		taskCellSortString = taskCellSortString.trim().toUpperCase();

		return taskCellSortString;

	}

	public void save() {
		// TODO Auto-generated method stub
//		ATLTaskListActivity.isTaskDataChanged = true;
//		ATLTaskModel alertModel = new ATLTaskModel(this);
//		TaskDatabaseAdapter dbHelper = new TaskDatabaseAdapter();
//		boolean isExit = dbHelper.isExistInDatabase(alertModel);
//		if (isExit) {
//			dbHelper.updateATLTaskModel(alertModel);
//		} else {
//			dbHelper.insertATLTaskModel(alertModel);
//		}
	}

	public void deNull() {
		taskCellUuid = Utilities.deNull(taskCellUuid);
		taskCellAtlasID = Utilities.deNull(taskCellAtlasID);
		taskCellTitle = Utilities.deNull(taskCellTitle);
		taskCellDescription = Utilities.deNull(taskCellDescription);
		taskCellNote = Utilities.deNull(taskCellNote);
		taskCellPriority = Utilities.deNull(taskCellPriority);
		taskCellStatus = Utilities.deNull(taskCellStatus);
		taskCellCalendarName = Utilities.deNull(taskCellCalendarName);
		taskCellSortString = Utilities.deNull(taskCellSortString);
		taskCellSenderId = Utilities.deNull(taskCellSenderId);
		taskCellSenderEmail = Utilities.deNull(taskCellSenderEmail);
		taskCellSenderName = Utilities.deNull(taskCellSenderName);
		taskCellReceiverId = Utilities.deNull(taskCellReceiverId);
		taskCellReceiverEmail = Utilities.deNull(taskCellReceiverEmail);
		taskCellReceiverName = Utilities.deNull(taskCellReceiverName);

	}

}
