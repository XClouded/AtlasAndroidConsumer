//  ==================================================================================================================
//  TaskDatabaseAdapter.java
//  ATLAS
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-11-11 TAN:    Init class
//  ==================================================================================================================

package atlasapp.section_tasks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import atlasapp.common.ATLConstants.AlertType;
import atlasapp.common.DB;
import atlasapp.common.DBDefines;
import atlasapp.model.ATLTaskModel;


/**
 * To handle ATL_TASK Table in database
 */
public class TaskDatabaseAdapter extends DB {

	// implementation TableAtlTask

	public final static String TASK_ID = "TASK_ID"; // Primary Key
	public final static String TASK_UUID = "TASK_UUID";
	public final static String TASK_ATLAS_ID = "TASK_ATLAS_ID";
	public final static String TASK_START_DATE = "TASK_START_DATE";
	public final static String TASK_END_DATE = "TASK_END_DATE";
	public final static String TASK_DUE_DATE = "TASK_DUE_DATE";
	public final static String TASK_COMPLETED_DATE = "TASK_COMPLETED_DATE";
	public final static String TASK_REMINDER = "TASK_REMINDER";
	public final static String TASK_TITLE = "TASK_TITLE";
	public final static String TASK_DESCRIPTION = "TASK_DESCRIPTION";
	public final static String TASK_NOTE = "TASK_NOTE";
	public final static String TASK_PRIORITY = "TASK_PRIORITY";
	public final static String TASK_STATUS = "TASK_STATUS";
	public final static String TASK_CALENDAR_NAME = "TASK_CALENDAR_NAME";
	public final static String TASK_CALENDAR_COLOR = "TASK_CALENDAR_COLOR";
	public final static String TASK_CALENDAR_ID = "TASK_CALENDAR_ID";
	public final static String TASK_IS_COMPLETED = "TASK_IS_COMPLETED";
	public final static String TASK_SENDER_NAME = "TASK_SENDER_NAME";
	public final static String TASK_SENDER_ID = "TASK_SENDER_ID";
	public final static String TASK_SENDER_EMAIL = "TASK_SENDER_EMAIL";
	public final static String TASK_RECEIVER_NAME = "TASK_RECEIVER_NAME";
	public final static String TASK_RECEIVER_ID = "TASK_RECEIVER_ID";
	public final static String TASK_RECEIVER_EMAIL = "TASK_RECEIVER_EMAIL";
	public final static String TASK_RESPONSE_STATUS = "TASK_RESPONSE_STATUS";
	public final static String TASK_MODIFIED_DATE = "TASK_MODIFIED_DATE";
	public final static String TASK_CREATED_DATE = "TASK_CREATED_DATE";
	public final static String TASK_IS_DELTETED = "TASK_IS_DELTETED";

	// end

	// 2012-11-10: TAN Insert, Update, Delete Table ATL_TASK

	public boolean insertATLTaskModel(ATLTaskModel model) {
		DB.openDb();
		final SQLiteDatabase db = DB.db;

		final ContentValues values = new ContentValues();

		/*
		 * NSString *sqlString = [NSString stringWithFormat:@
		 * "INSERT INTO ATL_TASK ( %@, %@, %@, %@, %@, %@, %@, %@, %@, %@, %@, %@, %@, %@, %@, %@, %@, %@, %@) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
		 * , TASK_UUID, TASK_ATLAS_ID, TASK_STARTDATE, TASK_ENDDATE,
		 * TASK_DUEDATE, TASK_ACTUALDATE, TASK_WORKING_TIME, TASK_REMINDER,
		 * TASK_TITLE, TASK_DESCRIPTION, TASK_NOTE, TASK_PRIORITY, TASK_STATUS,
		 * TASK_CALENDAR_NAME, TASK_CALENDAR_COLOR, TASK_ISCOMPLETED,
		 * TASK_AUTHOR_NAME, TASK_AUTHOR_EMAIL, TASK_RESPONSESTATUS ];
		 */
		values.put(TASK_UUID, model.taskUuid);
		values.put(TASK_ATLAS_ID, model.taskAtlasId);

		values.put(TASK_DUE_DATE, toGmtString(model.taskDueDate));
		values.put(TASK_COMPLETED_DATE, toGmtString(model.taskCompletedDate));

		values.put(TASK_REMINDER, model.taskReminder);

		values.put(TASK_TITLE, model.taskTitle);
		values.put(TASK_DESCRIPTION, model.taskDescription);
		values.put(TASK_NOTE, model.taskNote);
		values.put(TASK_PRIORITY, model.taskPriorityInt);// 2012-12-22 TAN: Add
															// change to int

		values.put(TASK_STATUS, model.taskStatus);
		values.put(TASK_CALENDAR_NAME, model.taskCalendarName);
		values.put(TASK_CALENDAR_COLOR, model.taskCalendarColor);
		values.put(TASK_CALENDAR_ID, model.taskCalendarId);
		values.put(TASK_IS_COMPLETED, model.taskIsCompleted);

		values.put(TASK_SENDER_ID, model.taskSenderId);
		values.put(TASK_SENDER_EMAIL, model.taskSenderEmail);
		values.put(TASK_SENDER_NAME, model.taskSenderName);
		values.put(TASK_RECEIVER_ID, model.taskReceiverId);
		values.put(TASK_RECEIVER_EMAIL, model.taskReceiverEmail);
		values.put(TASK_RECEIVER_NAME, model.taskReceiverName);

		values.put(TASK_RESPONSE_STATUS, model.taskResponseStatus);
		values.put(TASK_MODIFIED_DATE, toGmtString(model.taskModifieDate));
		values.put(TASK_CREATED_DATE, toGmtString(model.taskCreatedDate));
		values.put(TASK_IS_DELTETED, model.taskIsDeleted);

		try {
			db.insert(DBDefines.ATL_TASK, null, values);
			db.close();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			db.close();
			return false;
		}
	}

	public boolean updateATLTaskModel(ATLTaskModel model) {
		DB.openDb();
		final SQLiteDatabase db = DB.db;
		final String whereClause = TASK_ID + "=?";
		final String[] whereArgs = new String[] { String
				.valueOf(model.taskId) };

		ContentValues values = new ContentValues();

		values.put(TASK_UUID, model.taskUuid);
		values.put(TASK_ATLAS_ID, model.taskAtlasId);

		values.put(TASK_DUE_DATE, toGmtString(model.taskDueDate));
		values.put(TASK_COMPLETED_DATE, toGmtString(model.taskCompletedDate));

		values.put(TASK_REMINDER, model.taskReminder);

		values.put(TASK_TITLE, model.taskTitle);
		values.put(TASK_DESCRIPTION, model.taskDescription);
		values.put(TASK_NOTE, model.taskNote);
		values.put(TASK_PRIORITY, model.taskPriorityInt);// 2012-12-22 TAN: Add
															// change to int

		values.put(TASK_STATUS, model.taskStatus);
		values.put(TASK_CALENDAR_NAME, model.taskCalendarName);
		values.put(TASK_CALENDAR_COLOR, model.taskCalendarColor);
		values.put(TASK_CALENDAR_ID, model.taskCalendarId);
		values.put(TASK_IS_COMPLETED, model.taskIsCompleted);

		values.put(TASK_SENDER_ID, model.taskSenderId);
		values.put(TASK_SENDER_EMAIL, model.taskSenderEmail);
		values.put(TASK_SENDER_NAME, model.taskSenderName);
		values.put(TASK_RECEIVER_ID, model.taskReceiverId);
		values.put(TASK_RECEIVER_EMAIL, model.taskReceiverEmail);
		values.put(TASK_RECEIVER_NAME, model.taskReceiverName);

		values.put(TASK_RESPONSE_STATUS, model.taskResponseStatus);
		values.put(TASK_MODIFIED_DATE, toGmtString(model.taskModifieDate));
		values.put(TASK_CREATED_DATE, toGmtString(model.taskCreatedDate));
		values.put(TASK_IS_DELTETED, model.taskIsDeleted);

		db.update(DBDefines.ATL_TASK, values, whereClause, whereArgs);
		db.close();

		return true;
	}

	public boolean deleteATLTaskModel(ATLTaskModel model) {
		DB.openDb();
		final SQLiteDatabase db = DB.db;
		final String whereClause = TASK_ID + "=?";
		final String[] whereArgs = new String[] { String.valueOf(model.taskId) };
		db.delete(DBDefines.ATL_TASK, whereClause, whereArgs);
		db.close();
		return true;
	}

	public boolean deleteAllATLTaskModel() {
		DB.openDb();
		final SQLiteDatabase db = DB.db;
		db.delete(DBDefines.ATL_TASK, null, null);
		db.close();
		return true;
	}

	public ArrayList<ATLTaskModel> loadAllTasksInDatabase() {
		DB.openDb();
		final SQLiteDatabase db = DB.db;
		final String whereClause = null;
		final String[] columns = null;
		// new String[] { TASK_ID,TASK_UUID, TASK_ATLAS_ID,
		// TASK_STARTDATE, TASK_ENDDATE, TASK_DUEDATE, TASK_ACTUALDATE,
		// TASK_WORKING_TIME, TASK_REMINDER, TASK_TITLE, TASK_DESCRIPTION,
		// TASK_NOTE, TASK_PRIORITY, TASK_STATUS, TASK_CALENDAR_NAME,
		// TASK_CALENDAR_COLOR, TASK_ISCOMPLETED, TASK_AUTHOR_NAME,
		// TASK_AUTHOR_EMAIL, TASK_RESPONSESTATUS };
		final Cursor cursor = db.query(DBDefines.ATL_TASK, columns,
				whereClause, null, "", "", "", "");
		final ArrayList<ATLTaskModel> list = new ArrayList<ATLTaskModel>();
		if (cursor.moveToFirst()) {
			do {
				ATLTaskModel task = new ATLTaskModel();
				try {

					task.taskId = cursor.getInt(cursor.getColumnIndex(TASK_ID));
					task.taskUuid = cursor.getString(cursor
							.getColumnIndex(TASK_UUID));
					task.taskAtlasId = cursor.getString(cursor
							.getColumnIndex(TASK_ATLAS_ID));

					task.taskDueDate = stringToDate(cursor.getString(cursor
							.getColumnIndex(TASK_DUE_DATE)));
					task.taskCompletedDate = stringToDate(cursor
							.getString(cursor
									.getColumnIndex(TASK_COMPLETED_DATE)));

					task.taskReminder = cursor.getInt(cursor
							.getColumnIndex(TASK_REMINDER));
					task.taskTitle = cursor.getString(cursor
							.getColumnIndex(TASK_TITLE));
					task.taskDescription = cursor.getString(cursor
							.getColumnIndex(TASK_DESCRIPTION));
					task.taskNote = cursor.getString(cursor
							.getColumnIndex(TASK_NOTE));
					task.taskPriorityInt = cursor.getInt(cursor
							.getColumnIndex(TASK_PRIORITY));// 2012-12-22 TAN:
															// change to int

					task.taskStatus = cursor.getString(cursor
							.getColumnIndex(TASK_STATUS));
					task.taskCalendarName = cursor.getString(cursor
							.getColumnIndex(TASK_CALENDAR_NAME));
					task.taskCalendarColor = cursor.getInt(cursor
							.getColumnIndex(TASK_CALENDAR_COLOR));
					task.taskCalendarId = cursor.getInt(cursor
							.getColumnIndex(TASK_CALENDAR_ID));
					if (cursor.getInt(cursor.getColumnIndex(TASK_IS_COMPLETED)) == 1) {
						task.taskIsCompleted = true;
					} else {
						task.taskIsCompleted = false;
					}

					task.taskSenderId = cursor.getString(cursor
							.getColumnIndex(TASK_SENDER_ID));
					task.taskSenderEmail = cursor.getString(cursor
							.getColumnIndex(TASK_SENDER_EMAIL));
					task.taskSenderName = cursor.getString(cursor
							.getColumnIndex(TASK_SENDER_NAME));
					task.taskReceiverId = cursor.getString(cursor
							.getColumnIndex(TASK_RECEIVER_ID));
					task.taskReceiverEmail = cursor.getString(cursor
							.getColumnIndex(TASK_RECEIVER_EMAIL));
					task.taskReceiverName = cursor.getString(cursor
							.getColumnIndex(TASK_RECEIVER_NAME));

					task.taskResponseStatus = cursor.getInt(cursor
							.getColumnIndex(TASK_RESPONSE_STATUS));
					task.taskModifieDate = stringToDate(cursor
							.getString(cursor
									.getColumnIndex(TASK_MODIFIED_DATE)));
					task.taskCreatedDate = stringToDate(cursor
							.getString(cursor.getColumnIndex(TASK_CREATED_DATE)));
					
					if (cursor.getInt(cursor.getColumnIndex(TASK_IS_DELTETED)) == 1) {
						task.taskIsDeleted = true;
					} else {
						task.taskIsDeleted = false;
					}

					list.add(task);
				} catch (Exception e) {
					// ignore
				}
			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();
		return list;
	}

	public ATLTaskModel loadATLTaskModelByPrimaryKey(int taskID) {

		DB.openDb();
		final SQLiteDatabase db = DB.db;
		final String whereClause = TASK_ID + "=?";
		final String[] whereArgs = new String[] { String.valueOf(taskID) };
		final String[] columns = null;
		final Cursor cursor = db.query(DBDefines.ATL_TASK, columns,
				whereClause, whereArgs, "", "", "", "");

		ATLTaskModel task = null;
		if (cursor.moveToFirst()) {
			do {
				task = new ATLTaskModel();
				try {

					task.taskId = cursor.getInt(cursor.getColumnIndex(TASK_ID));
					task.taskUuid = cursor.getString(cursor
							.getColumnIndex(TASK_UUID));
					task.taskAtlasId = cursor.getString(cursor
							.getColumnIndex(TASK_ATLAS_ID));

					task.taskDueDate = stringToDate(cursor.getString(cursor
							.getColumnIndex(TASK_DUE_DATE)));
					task.taskCompletedDate = stringToDate(cursor
							.getString(cursor
									.getColumnIndex(TASK_COMPLETED_DATE)));

					task.taskReminder = cursor.getInt(cursor
							.getColumnIndex(TASK_REMINDER));
					task.taskTitle = cursor.getString(cursor
							.getColumnIndex(TASK_TITLE));
					task.taskDescription = cursor.getString(cursor
							.getColumnIndex(TASK_DESCRIPTION));
					task.taskNote = cursor.getString(cursor
							.getColumnIndex(TASK_NOTE));
					task.taskPriorityInt = cursor.getInt(cursor
							.getColumnIndex(TASK_PRIORITY));// 2012-12-22 TAN:
															// change to int

					task.taskStatus = cursor.getString(cursor
							.getColumnIndex(TASK_STATUS));
					task.taskCalendarName = cursor.getString(cursor
							.getColumnIndex(TASK_CALENDAR_NAME));
					task.taskCalendarColor = cursor.getInt(cursor
							.getColumnIndex(TASK_CALENDAR_COLOR));
					task.taskCalendarId = cursor.getInt(cursor
							.getColumnIndex(TASK_CALENDAR_ID));
					if (cursor.getInt(cursor.getColumnIndex(TASK_IS_COMPLETED)) == 1) {
						task.taskIsCompleted = true;
					} else {
						task.taskIsCompleted = false;
					}

					task.taskSenderId = cursor.getString(cursor
							.getColumnIndex(TASK_SENDER_ID));
					task.taskSenderEmail = cursor.getString(cursor
							.getColumnIndex(TASK_SENDER_EMAIL));
					task.taskSenderName = cursor.getString(cursor
							.getColumnIndex(TASK_SENDER_NAME));
					task.taskReceiverId = cursor.getString(cursor
							.getColumnIndex(TASK_RECEIVER_ID));
					task.taskReceiverEmail = cursor.getString(cursor
							.getColumnIndex(TASK_RECEIVER_EMAIL));
					task.taskReceiverName = cursor.getString(cursor
							.getColumnIndex(TASK_RECEIVER_NAME));


					task.taskResponseStatus = cursor.getInt(cursor
							.getColumnIndex(TASK_RESPONSE_STATUS));
					task.taskModifieDate = stringToDate(cursor
							.getString(cursor
									.getColumnIndex(TASK_MODIFIED_DATE)));
					task.taskCreatedDate = stringToDate(cursor
							.getString(cursor.getColumnIndex(TASK_CREATED_DATE)));
					
					if (cursor.getInt(cursor.getColumnIndex(TASK_IS_DELTETED)) == 1) {
						task.taskIsDeleted = true;
					} else {
						task.taskIsDeleted = false;
					}

				} catch (Exception e) {
					// ignore
				}
			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();

		return task;
	}

	public ArrayList<ATLTaskModel> loadATLTaskModelByDeadline(Date deadline) {
		DB.openDb();
		final SQLiteDatabase db = DB.db;
		final String whereClause = TASK_DUE_DATE + "=?";
		final String[] whereArgs = new String[] { toGmtString(deadline) };
		final String[] columns = null;
		// new String[] { TASK_ID,TASK_UUID, TASK_ATLAS_ID,
		// TASK_STARTDATE, TASK_ENDDATE, TASK_DUEDATE, TASK_ACTUALDATE,
		// TASK_WORKING_TIME, TASK_REMINDER, TASK_TITLE, TASK_DESCRIPTION,
		// TASK_NOTE, TASK_PRIORITY, TASK_STATUS, TASK_CALENDAR_NAME,
		// TASK_CALENDAR_COLOR, TASK_ISCOMPLETED, TASK_AUTHOR_NAME,
		// TASK_AUTHOR_EMAIL, TASK_RESPONSESTATUS };
		final Cursor cursor = db.query(DBDefines.ATL_TASK, columns,
				whereClause, whereArgs, "", "", "", "");
		final ArrayList<ATLTaskModel> list = new ArrayList<ATLTaskModel>();
		if (cursor.moveToFirst()) {
			do {
				ATLTaskModel task = new ATLTaskModel();
				try {

					task.taskId = cursor.getInt(cursor.getColumnIndex(TASK_ID));
					task.taskUuid = cursor.getString(cursor
							.getColumnIndex(TASK_UUID));
					task.taskAtlasId = cursor.getString(cursor
							.getColumnIndex(TASK_ATLAS_ID));

					task.taskDueDate = stringToDate(cursor.getString(cursor
							.getColumnIndex(TASK_DUE_DATE)));
					task.taskCompletedDate = stringToDate(cursor
							.getString(cursor
									.getColumnIndex(TASK_COMPLETED_DATE)));

					task.taskReminder = cursor.getInt(cursor
							.getColumnIndex(TASK_REMINDER));
					task.taskTitle = cursor.getString(cursor
							.getColumnIndex(TASK_TITLE));
					task.taskDescription = cursor.getString(cursor
							.getColumnIndex(TASK_DESCRIPTION));
					task.taskNote = cursor.getString(cursor
							.getColumnIndex(TASK_NOTE));
					task.taskPriorityInt = cursor.getInt(cursor
							.getColumnIndex(TASK_PRIORITY));// 2012-12-22 TAN:
															// change to int

					task.taskStatus = cursor.getString(cursor
							.getColumnIndex(TASK_STATUS));
					task.taskCalendarName = cursor.getString(cursor
							.getColumnIndex(TASK_CALENDAR_NAME));
					task.taskCalendarColor = cursor.getInt(cursor
							.getColumnIndex(TASK_CALENDAR_COLOR));
					task.taskCalendarId = cursor.getInt(cursor
							.getColumnIndex(TASK_CALENDAR_ID));
					if (cursor.getInt(cursor.getColumnIndex(TASK_IS_COMPLETED)) == 1) {
						task.taskIsCompleted = true;
					} else {
						task.taskIsCompleted = false;
					}

					task.taskSenderId = cursor.getString(cursor
							.getColumnIndex(TASK_SENDER_ID));
					task.taskSenderEmail = cursor.getString(cursor
							.getColumnIndex(TASK_SENDER_EMAIL));
					task.taskSenderName = cursor.getString(cursor
							.getColumnIndex(TASK_SENDER_NAME));
					task.taskReceiverId = cursor.getString(cursor
							.getColumnIndex(TASK_RECEIVER_ID));
					task.taskReceiverEmail = cursor.getString(cursor
							.getColumnIndex(TASK_RECEIVER_EMAIL));
					task.taskReceiverName = cursor.getString(cursor
							.getColumnIndex(TASK_RECEIVER_NAME));

					task.taskResponseStatus = cursor.getInt(cursor
							.getColumnIndex(TASK_RESPONSE_STATUS));
					task.taskModifieDate = stringToDate(cursor
							.getString(cursor
									.getColumnIndex(TASK_MODIFIED_DATE)));
					task.taskCreatedDate = stringToDate(cursor
							.getString(cursor.getColumnIndex(TASK_CREATED_DATE)));
					
					if (cursor.getInt(cursor.getColumnIndex(TASK_IS_DELTETED)) == 1) {
						task.taskIsDeleted = true;
					} else {
						task.taskIsDeleted = false;
					}
					
					list.add(task);
				} catch (Exception e) {
					// ignore
				}
			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();
		return list;
	}

	public boolean isExistInDatabase(ATLTaskModel model) {
		DB.openDb();
		final SQLiteDatabase db = DB.db;
		final String whereClause = TASK_ID + "=?";
		final String[] whereArgs = new String[] { String.valueOf(model.taskId) };
		final String[] columns = null;
		final Cursor cursor = db.query(DBDefines.ATL_TASK, columns,
				whereClause, whereArgs, "", "", "", "");

		boolean result = cursor.moveToFirst();
		cursor.close();
		return result;
	}

	private static String toGmtString(Date date) {
		if(date == null)
			return "";
		// date formatter
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		sd.setTimeZone(TimeZone.getTimeZone("UTC"));
		String return_date = sd.format(date);
		return return_date;
	}

	private Date stringToDate(String dateString) {
		if(dateString == null || dateString.equals(""))
			return new Date();
		// 1985-04-12T23:20:50.52Z
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		sd.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		Date date  = new Date(); 
		try {
			date = sd.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return date;
	}

	// ===============================================================================
	// 2013-01-18 TAN: START
	// ===============================================================================

	static public boolean deleteTaskByDueDate(Date date) {
		DB.openDb();
		final SQLiteDatabase db = DB.db;
		final String whereClause = TASK_DUE_DATE + "=?";
		final String[] whereArgs = new String[] { toGmtString(date) };

		try {
			db.delete(DBDefines.ATL_TASK, whereClause, whereArgs);
			db.close();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			db.close();
			return false;
		}
	}

	static public boolean updateTaskWithDue(Date dueDate, Boolean isAccepted,
			Boolean isCompleted) {
		DB.openDb();
		final SQLiteDatabase db = DB.db;
		final String whereClause = TASK_DUE_DATE + "=?";
		final String[] whereArgs = new String[] { toGmtString(dueDate) };

		ContentValues values = new ContentValues();

		values.put(TASK_IS_COMPLETED, isCompleted);
		if (isAccepted) {
			values.put(TASK_RESPONSE_STATUS, AlertType.taskAccepted_Received);
		} else {
			values.put(TASK_RESPONSE_STATUS, AlertType.taskReject_Receive);
		}

		try {
			db.update(DBDefines.ATL_TASK, values, whereClause, whereArgs);
			db.close();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			db.close();
			return false;
		}

	}
	// ===============================================================================
	// 2013-01-18 TAN: END
	// ===============================================================================

	public boolean deleteATLTaskModelByUuid(String taskUuid) {
		// TODO Auto-generated method stub
		boolean isSuccess = false;
		DB.openDb();
		final SQLiteDatabase db = DB.db;
		final String whereClause = TASK_UUID + "=?";
		final String[] whereArgs = new String[] { taskUuid };
		try{
			db.delete(DBDefines.ATL_TASK, whereClause, whereArgs);
			db.close();
			isSuccess = true;
		}
		catch(Exception ex){
			ex.printStackTrace();
			db.close();
			isSuccess = false;
		}
		return isSuccess;
	}
}
