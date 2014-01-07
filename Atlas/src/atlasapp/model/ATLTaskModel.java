//  ==================================================================================================================
//  ATLTaskModel.java
//  AtlasConsumerAndroid
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-11-20 TAN: 	Fix stringToDate(String) method, save UTC and load to Local Time Zone 
//	2012-11-11 TAN:	    Create class
//  ==================================================================================================================

package atlasapp.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.google.api.client.util.DateTime;
import com.google.api.services.tasks.model.Task;

import android.test.mock.MockApplication;
import android.util.Log;
import atlasapp.common.ATLConstants.TASK_CATEGORY;
import atlasapp.common.ATLConstants.TaskResponseType;
import atlasapp.common.DateTimeUtilities;
import atlasapp.section_tasks.ATLTaskCellData;
//import atlasapp.section_tasks.ATLTaskListActivity;
import atlasapp.section_tasks.TaskDatabaseAdapter;


public class ATLTaskModel {

	// //SHARTON ADDINGS////
	public TASK_CATEGORY taskCategory = TASK_CATEGORY.High;
	// /////
	public int taskId = -1;
	public String taskUuid = "";
	public String taskAtlasId = "";
	public Date taskDueDate = new Date();
	public Date taskCompletedDate = new Date();
	public int taskReminder = 0;
	public String taskTitle = "";
	public String taskDescription = "";
	public String taskNote = "";
	public String taskPriority = "";
	public int taskPriorityInt = 0;
	public String taskStatus = "";
	public String taskCalendarName = "";
	public int taskCalendarColor = 0;
	public int taskCalendarId = 0;
	public boolean taskIsCompleted = false;
	public String taskSenderId = "";
	public String taskSenderEmail = "";
	public String taskSenderName = "";
	public String taskReceiverId = "";
	public String taskReceiverEmail = "";
	public String taskReceiverName = "";
	public int taskResponseStatus = TaskResponseType.taskResponseType_None;
	public Date taskModifieDate = new Date();
	public Date taskCreatedDate = new Date();
	public boolean taskIsDeleted = false;

	public ATLTaskModel(Task gTask) {

		taskCompletedDate = new Date();
		Log.v("ATLTaskModel", "Completed " + gTask.getCompleted());
		DateTime gTaskDue = gTask.getDue();
		taskDueDate = new Date();

		String dueTimeString = null;
		String priorityString = null;
		String reminderString = null;
		String calendarIdString = null;
		String taskDelegatedName = null;

		if (gTask.getNotes() != null) {
			int subStringIndex = gTask.getNotes().indexOf("DueTime:");

			if (subStringIndex != -1) {
				taskNote = gTask.getNotes();// .substring(0, subStringIndex);
				String[] atlasData = gTask.getNotes().substring(subStringIndex)
						.split("\n");
				try {
					if (atlasData.length > 0) {
						dueTimeString = atlasData[0].trim();
						if (dueTimeString != null) {
							dueTimeString = dueTimeString.replace("DueTime:",
									"");
							Log.v("ATLTaskModel", dueTimeString);
							taskDueDate = DateTimeUtilities
									.serverTimeStringToLocalDateTime(
											dueTimeString,
											"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
						} else if (gTaskDue != null) {
							// Log.v("ATLTaskModel", "DueDay " +
							// gTask.getDue());
							String dueString = gTaskDue.toStringRfc3339();
							Log.v("ATLTaskModel", dueString);
							taskDueDate = stringToDate(dueString);
						}

						priorityString = atlasData[1];
						priorityString = priorityString
								.replace("Priority:", "");
						taskPriorityInt = Integer.valueOf(priorityString);

						reminderString = atlasData[2];
						reminderString = reminderString
								.replace("Reminder:", "");
						taskReminder = Integer.valueOf(reminderString);

						calendarIdString = atlasData[3];
						calendarIdString = calendarIdString.replace(
								"CalendarName:", "");
						taskCalendarName = calendarIdString;

						taskDelegatedName = atlasData[4];
						taskDelegatedName = taskDelegatedName.replace(
								"Delegated:", "");
						this.taskReceiverName = taskDelegatedName;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else {
				taskNote = gTask.getNotes();
			}
		}

		taskDescription = taskNote;
		taskUuid = gTask.getId();
		taskStatus = gTask.getStatus();
		taskTitle = gTask.getTitle();
		
		String updateString = gTask.getUpdated().toStringRfc3339();
		Log.v("ATLTaskModel", updateString);
		taskModifieDate = stringToDate(updateString);
		
		if (gTask.getCompleted() != null) {
			taskIsCompleted = true;
			String completeString = gTask.getCompleted().toStringRfc3339();
			Log.v("ATLTaskModel", completeString);
			taskCompletedDate = stringToDate(completeString);
		} else {
			taskIsCompleted = false;
		}
		
	}

	public static Date stringToDate(String dateString) {
		// 1985-04-12T23:20:50.52Z
		SimpleDateFormat sd = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		sd.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		Date date = new Date();
		try {
			date = sd.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return date;
	}

	public ATLTaskModel(ATLTaskCellData taskCellData) {
		// TODO Auto-generated constructor stub
		taskCellData.deNull();

		taskCategory = taskCellData.taskCategory;
		taskId = taskCellData.taskCellId;
		taskUuid = taskCellData.taskCellUuid;
		taskAtlasId = taskCellData.taskCellAtlasID;
		taskDueDate = taskCellData.taskCellDueDate;
		taskCompletedDate = taskCellData.taskCellCompletedDate;
		taskReminder = taskCellData.taskCellRemider;
		taskTitle = taskCellData.taskCellTitle;
		taskDescription = taskCellData.taskCellDescription;
		taskNote = taskCellData.taskCellNote;
		taskPriority = taskCellData.taskCellPriority;
		taskPriorityInt = taskCellData.taskCellPriorityInt;
		taskStatus = taskCellData.taskCellStatus;
		taskCalendarName = taskCellData.taskCellCalendarName;
		taskCalendarColor = taskCellData.taskCellCalendarColor;
		taskCalendarId = taskCellData.calendarId;
		taskIsCompleted = taskCellData.taskCellIsCompleted;
		taskSenderId = taskCellData.taskCellSenderId;
		taskSenderEmail = taskCellData.taskCellSenderEmail;
		taskSenderName = taskCellData.taskCellSenderName;
		taskReceiverId = taskCellData.taskCellReceiverId;
		taskReceiverEmail = taskCellData.taskCellReceiverEmail;
		taskReceiverName = taskCellData.taskCellReceiverName;
		taskResponseStatus = taskCellData.taskResponseStatus;
		taskModifieDate = taskCellData.taskCellModifiedDate;
		taskCreatedDate = taskCellData.taskCellCreatedDate;
		taskIsDeleted = taskCellData.taskCellIsDeleted;
	}
	public ATLTaskModel() {
		// TODO Auto-generated constructor stub
		
	}

	public void updateInfoGTask(Task gTask) {
		// TODO Auto-generated method stub
		ATLTaskModel model = new ATLTaskModel(gTask);
		this.taskCompletedDate = model.taskCompletedDate;
		this.taskDescription = model.taskDescription;
		this.taskDueDate = model.taskDueDate;
		this.taskIsCompleted = model.taskIsCompleted;
		
		String updateString = gTask.getUpdated().toStringRfc3339();
		Log.v("ATLTaskModel", updateString);
		this.taskModifieDate = stringToDate(updateString);// Change to local date
		
//		this.taskModifieDate = model.taskModifieDate;// TAN: will change to Local Date
		this.taskNote = model.taskNote;
		this.taskPriority = model.taskPriority;
		this.taskPriorityInt = model.taskPriorityInt;
		this.taskTitle = model.taskTitle;
		this.taskStatus = model.taskStatus;
		this.taskReceiverName = model.taskReceiverName;
	}
	
	public void save(){
//		ATLTaskListActivity.isTaskDataChanged = true;
//		TaskDatabaseAdapter dbHelper = new TaskDatabaseAdapter();
//		boolean isExit = dbHelper.isExistInDatabase(this);
//		if (isExit) {
//			dbHelper.updateATLTaskModel(this);
//		} else {
//			dbHelper.insertATLTaskModel(this);
//		}
	}

}
