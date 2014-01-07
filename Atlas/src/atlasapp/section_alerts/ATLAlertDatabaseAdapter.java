//  ==================================================================================================================
//  ATLAlertDatabaseAdapter.java
//  AtlasConsumerAndroid
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2013-01-10 NGHIA:    Create and add insert, update, delete and load ATLAlertModel from Database
//  ==================================================================================================================

package atlasapp.section_alerts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import atlasapp.common.DB;
import atlasapp.common.DBDefines;
import atlasapp.model.ATLAlertModel;



/**
 * @author nghia
 * 
 */
public class ATLAlertDatabaseAdapter extends DB {

	/**
	 * 
	 */
	public final static String ALERT_ID = "ALERT_ID"; // Primary Key
	public final static String ALERT_UUID = "ALERT_UUID";
	public final static String ALERT_ATLAS_ID = "ALERT_ATLAS_ID";
	public final static String ALERT_SENDER_ID = "ALERT_SENDER_ID";
	public final static String ALERT_SENDER_EMAIL = "ALERT_SENDER_EMAIL";
	public final static String ALERT_SENDER_NAME = "ALERT_SENDER_NAME";
	public final static String ALERT_RECEIVER_ID = "ALERT_RECEIVER_ID";
	public final static String ALERT_RECEIVER_EMAIL = "ALERT_RECEIVER_EMAIL";
	public final static String ALERT_RECEIVER_NAME = "ALERT_RECEIVER_NAME";
	public final static String ALERT_EVENT_TITLE = "ALERT_EVENT_TITLE";
	public final static String ALERT_EVENT_DURATION = "ALERT_EVENT_DURATION";
	public final static String ALERT_EVENT_LOCATION = "ALERT_EVENT_LOCATION";
	public final static String ALERT_DESCRIPTION = "ALERT_DESCRIPTION";
	public final static String ALERT_MSG_ID = "ALERT_MSG_ID";
	public final static String ALERT_MSG_ACTION = "ALERT_MSG_ACTION";
	public final static String ALERT_MSG_CREATED_DATETIME = "ALERT_MSG_CREATED_DATETIME";
	public final static String ALERT_PREFERRED_DATETIME = "ALERT_PREFERRED_DATETIME";
	public final static String ALERT_ALT1_DATETIME = "ALERT_ALT1_DATETIME";
	public final static String ALERT_ALT2_DATETIME = "ALERT_ALT2_DATETIME";
	public final static String ALERT_EVENT_PRIORITY = "ALERT_EVENT_PRIORITY";
	public final static String ALERT_TEXT = "ALERT_TEXT";
	public final static String ALERT_ACCESSED_DATETIME = "ALERT_ACCESSED_DATETIME";
	public final static String ALERT_DISPLAYED = "ALERT_DISPLAYED";
	public final static String ALERT_READ = "ALERT_READ";
	public final static String ALERT_HANDLED = "ALERT_HANDLED";
	public final static String ALERT_ACCEPTED = "ALERT_ACCEPTED";
	public final static String ALERT_ACCEPTED_DATETIME = "ALERT_ACCEPTED_DATETIME";
	public final static String ALERT_RESPONSE_STATUS = "ALERT_RESPONSE_STATUS";
	
	public final static String ALERT_TYPE = "ALERT_TYPE";
	public final static String ALERT_TASK_PRIORITY = "ALERT_TASK_PRIORITY";
	public final static String ALERT_IS_SENT_MESSAGE_SUCCESSFULLY = "ALERT_IS_SENT_MESSAGE_SUCCESSFULLY";
	
	public final static String ALERT_CREATED_DATETIME = "ALERT_CREATED_DATETIME";	
	public final static String ALERT_MODIFIED_DATETIME = "ALERT_MODIFIED_DATETIME";
	
	public ATLAlertDatabaseAdapter() {
		// TODO Auto-generated constructor stub
	}

	public boolean insertATLAlertModel(ATLAlertModel model) {
		DB.openDb();
		final SQLiteDatabase db = DB.db;

		final ContentValues values = new ContentValues();

		values.put(ALERT_UUID, model.alertUuid);
		values.put(ALERT_ATLAS_ID, model.alertAtlasId);

		values.put(ALERT_SENDER_ID, model.alertSenderId);
		values.put(ALERT_SENDER_EMAIL, model.alertSenderEmail);
		values.put(ALERT_SENDER_NAME, model.alertSenderName);
		values.put(ALERT_RECEIVER_ID, model.alertReceiverId);
		values.put(ALERT_RECEIVER_EMAIL, model.alertReceiverEmail);
		values.put(ALERT_RECEIVER_NAME, model.alertReceiverName);

		values.put(ALERT_EVENT_TITLE, model.alertEventTitle);
		values.put(ALERT_EVENT_LOCATION, model.alertEventLocation);
		values.put(ALERT_EVENT_DURATION, model.alertEventDuration);
		values.put(ALERT_DESCRIPTION, model.alertDescription);

		values.put(ALERT_PREFERRED_DATETIME,
				toGmtString(model.alertPreferredDate));
		values.put(ALERT_ALT1_DATETIME, toGmtString(model.alertAlt1Date));
		values.put(ALERT_ALT2_DATETIME, toGmtString(model.alertAlt2Date));

		values.put(ALERT_EVENT_PRIORITY, model.alertEventPriority);
		values.put(ALERT_TEXT, model.alertText);
		values.put(ALERT_ACCESSED_DATETIME,
				toGmtString(model.alertAccessedDate));

		values.put(ALERT_MSG_ID, model.alertMsgId);
		values.put(ALERT_MSG_ACTION, model.alertMsgAction);
		values.put(ALERT_MSG_CREATED_DATETIME,
				toGmtString(model.alertMsgCreatedDate));

		values.put(ALERT_DISPLAYED, model.isDisplayed);
		values.put(ALERT_READ, model.isRead);
		values.put(ALERT_HANDLED, model.isHandled);
		values.put(ALERT_ACCEPTED, model.isAccepted);

		values.put(ALERT_IS_SENT_MESSAGE_SUCCESSFULLY, model.isSentMessageSuccessfully);
		values.put(ALERT_TYPE, model.currentType);

		values.put(ALERT_ACCEPTED_DATETIME,
				toGmtString(model.alertAcceptedDate));
		values.put(ALERT_RESPONSE_STATUS, model.alertResponseStatus);
		values.put(ALERT_TASK_PRIORITY, model.alertCellTaskPriority);

		values.put(ALERT_CREATED_DATETIME, toGmtString(model.alertCreatedDate));
		values.put(ALERT_MODIFIED_DATETIME, toGmtString(model.alertModifiedDate));

		try {
			db.insert(DBDefines.ATL_ALERTS, null, values);
			db.close();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			db.close();
			return false;
		}
	}

	public boolean updateATLAlertModel(ATLAlertModel model) {
		DB.openDb();
		final SQLiteDatabase db = DB.db;
		final String whereClause = ALERT_ATLAS_ID + "=?";
		final String[] whereArgs = new String[] { String.valueOf(model.alertAtlasId) };

		ContentValues values = new ContentValues();

		values.put(ALERT_UUID, model.alertUuid);
		values.put(ALERT_ATLAS_ID, model.alertAtlasId);

		values.put(ALERT_SENDER_ID, model.alertSenderId);
		values.put(ALERT_SENDER_EMAIL, model.alertSenderEmail);
		values.put(ALERT_SENDER_NAME, model.alertSenderName);
		values.put(ALERT_RECEIVER_ID, model.alertReceiverId);
		values.put(ALERT_RECEIVER_EMAIL, model.alertReceiverEmail);
		values.put(ALERT_RECEIVER_NAME, model.alertReceiverName);

		values.put(ALERT_EVENT_TITLE, model.alertEventTitle);
		values.put(ALERT_EVENT_LOCATION, model.alertEventLocation);
		values.put(ALERT_EVENT_DURATION, model.alertEventDuration);
		values.put(ALERT_DESCRIPTION, model.alertDescription);

		values.put(ALERT_PREFERRED_DATETIME,
				toGmtString(model.alertPreferredDate));
		values.put(ALERT_ALT1_DATETIME, toGmtString(model.alertAlt1Date));
		values.put(ALERT_ALT2_DATETIME, toGmtString(model.alertAlt2Date));

		values.put(ALERT_EVENT_PRIORITY, model.alertEventPriority);
		values.put(ALERT_TEXT, model.alertText);
		values.put(ALERT_ACCESSED_DATETIME,
				toGmtString(model.alertAccessedDate));

		values.put(ALERT_MSG_ID, model.alertMsgId);
		values.put(ALERT_MSG_ACTION, model.alertMsgAction);
		values.put(ALERT_MSG_CREATED_DATETIME,
				toGmtString(model.alertMsgCreatedDate));

		values.put(ALERT_DISPLAYED, model.isDisplayed);
		values.put(ALERT_READ, model.isRead);
		values.put(ALERT_HANDLED, model.isHandled);
		values.put(ALERT_ACCEPTED, model.isAccepted);

		values.put(ALERT_IS_SENT_MESSAGE_SUCCESSFULLY, model.isSentMessageSuccessfully);
		values.put(ALERT_TYPE, model.currentType);

		values.put(ALERT_ACCEPTED_DATETIME,
				toGmtString(model.alertAcceptedDate));
		values.put(ALERT_RESPONSE_STATUS, model.alertResponseStatus);
		values.put(ALERT_TASK_PRIORITY, model.alertCellTaskPriority);
		
		values.put(ALERT_CREATED_DATETIME, toGmtString(model.alertCreatedDate));
		values.put(ALERT_MODIFIED_DATETIME, toGmtString(model.alertModifiedDate));

		db.update(DBDefines.ATL_ALERTS, values, whereClause, whereArgs);
		db.close();

		return true;
	}

	public boolean deleteATLAlertModel(ATLAlertModel model) {
		DB.openDb();
		final SQLiteDatabase db = DB.db;
		final String whereClause = ALERT_ATLAS_ID + "=?";
		final String[] whereArgs = new String[] { String.valueOf(model.alertAtlasId) };
		db.delete(DBDefines.ATL_ALERTS, whereClause, whereArgs);
		db.close();
		return true;
	}

	public boolean deleteAllATLAlertModel() {
		DB.openDb();
		final SQLiteDatabase db = DB.db;
		db.delete(DBDefines.ATL_ALERTS, null, null);
		db.close();
		return true;
	}

	public ATLAlertModel loadATLAlertModelByAlertId(int alertId) {

		DB.openDb();
		final SQLiteDatabase db = DB.db;
		final String whereClause = ALERT_ID + "=?";
		final String[] whereArgs = new String[] { String.valueOf(alertId) };
		final String[] columns = null;
		final Cursor cursor = db.query(DBDefines.ATL_ALERTS, columns,
				whereClause, whereArgs, "", "", "", "");

		ATLAlertModel alertModel = null;
		if (cursor.moveToFirst()) {
			do {
				alertModel = new ATLAlertModel();
				try {

					alertModel.alertId = cursor.getInt(cursor
							.getColumnIndex(ALERT_ID));
					alertModel.alertUuid = cursor.getString(cursor
							.getColumnIndex(ALERT_UUID));
					alertModel.alertAtlasId = cursor.getString(cursor
							.getColumnIndex(ALERT_ATLAS_ID));

					alertModel.alertSenderId = cursor.getString(cursor
							.getColumnIndex(ALERT_SENDER_ID));
					alertModel.alertSenderEmail = cursor.getString(cursor
							.getColumnIndex(ALERT_SENDER_EMAIL));
					alertModel.alertSenderName = cursor.getString(cursor
							.getColumnIndex(ALERT_SENDER_NAME));
					
					alertModel.alertReceiverId = cursor.getString(cursor
							.getColumnIndex(ALERT_RECEIVER_ID));
					alertModel.alertReceiverEmail = cursor.getString(cursor
							.getColumnIndex(ALERT_RECEIVER_EMAIL));
					alertModel.alertReceiverName = cursor.getString(cursor
							.getColumnIndex(ALERT_RECEIVER_NAME));

					alertModel.alertEventTitle = cursor.getString(cursor
							.getColumnIndex(ALERT_EVENT_TITLE));
					alertModel.alertEventLocation = cursor.getString(cursor
							.getColumnIndex(ALERT_EVENT_LOCATION));
					alertModel.alertEventDuration = cursor.getInt(cursor
							.getColumnIndex(ALERT_EVENT_DURATION));
					alertModel.alertDescription = cursor.getString(cursor
							.getColumnIndex(ALERT_DESCRIPTION));

					alertModel.alertPreferredDate = stringToDate(cursor
							.getString(cursor
									.getColumnIndex(ALERT_PREFERRED_DATETIME)));
					alertModel.alertAlt1Date = stringToDate(cursor
							.getString(cursor
									.getColumnIndex(ALERT_ALT1_DATETIME)));
					alertModel.alertAlt2Date = stringToDate(cursor
							.getString(cursor
									.getColumnIndex(ALERT_ALT2_DATETIME)));

					alertModel.alertEventPriority = cursor.getString(cursor
							.getColumnIndex(ALERT_EVENT_PRIORITY));
					alertModel.alertText = cursor.getString(cursor
							.getColumnIndex(ALERT_TEXT));
					alertModel.alertAccessedDate = stringToDate(cursor
							.getString(cursor
									.getColumnIndex(ALERT_ACCESSED_DATETIME)));

					alertModel.alertMsgId = cursor.getString(cursor
							.getColumnIndex(ALERT_MSG_ID));
					alertModel.alertMsgAction = cursor.getString(cursor
							.getColumnIndex(ALERT_MSG_ACTION));
					alertModel.alertMsgCreatedDate = stringToDate(cursor
							.getString(cursor
									.getColumnIndex(ALERT_MSG_CREATED_DATETIME)));

					if (cursor.getInt(cursor.getColumnIndex(ALERT_DISPLAYED)) == 0) {
						alertModel.isDisplayed = false;
					} else {
						alertModel.isDisplayed = true;
					}

					if (cursor.getInt(cursor.getColumnIndex(ALERT_READ)) == 0) {
						alertModel.isRead = false;
					} else {
						alertModel.isRead = true;
					}

					if (cursor.getInt(cursor.getColumnIndex(ALERT_HANDLED)) == 0) {
						alertModel.isHandled = false;
					} else {
						alertModel.isHandled = true;
					}
					
					if (cursor.getInt(cursor.getColumnIndex(ALERT_IS_SENT_MESSAGE_SUCCESSFULLY)) == 0) {
						alertModel.isSentMessageSuccessfully = false;
					} else {
						alertModel.isSentMessageSuccessfully = true;
					}
					
					alertModel.currentType = cursor.getInt(cursor.getColumnIndex(ALERT_TYPE));

					if (cursor.getInt(cursor.getColumnIndex(ALERT_ACCEPTED)) == 0) {
						alertModel.isAccepted = false;
					} else {
						alertModel.isAccepted = true;
					}
					alertModel.alertAcceptedDate = stringToDate(cursor
							.getString(cursor
									.getColumnIndex(ALERT_ACCEPTED_DATETIME)));
					alertModel.alertResponseStatus = cursor.getInt(cursor
							.getColumnIndex(ALERT_RESPONSE_STATUS));
					alertModel.alertCellTaskPriority = cursor.getInt(cursor
							.getColumnIndex(ALERT_TASK_PRIORITY));

					alertModel.alertCreatedDate = stringToDate(cursor
							.getString(cursor
									.getColumnIndex(ALERT_CREATED_DATETIME)));
					alertModel.alertModifiedDate = stringToDate(cursor
							.getString(cursor
									.getColumnIndex(ALERT_MODIFIED_DATETIME)));

				} catch (Exception e) {
					// ignore
				}
			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();

		return alertModel;
	}
	
	public ATLAlertModel loadATLAlertModelByAlertAtlasId(String alertAtlasId) {

		DB.openDb();
		final SQLiteDatabase db = DB.db;
		final String whereClause = ALERT_ATLAS_ID + "=?";
		final String[] whereArgs = new String[] { alertAtlasId };
		final String[] columns = null;
		final Cursor cursor = db.query(DBDefines.ATL_ALERTS, columns,
				whereClause, whereArgs, "", "", "", "");

		ATLAlertModel alertModel = null;
		if (cursor.moveToFirst()) {
			do {
				alertModel = new ATLAlertModel();
				try {

					alertModel.alertId = cursor.getInt(cursor
							.getColumnIndex(ALERT_ID));
					alertModel.alertUuid = cursor.getString(cursor
							.getColumnIndex(ALERT_UUID));
					alertModel.alertAtlasId = cursor.getString(cursor
							.getColumnIndex(ALERT_ATLAS_ID));

					alertModel.alertSenderId = cursor.getString(cursor
							.getColumnIndex(ALERT_SENDER_ID));
					alertModel.alertSenderEmail = cursor.getString(cursor
							.getColumnIndex(ALERT_SENDER_EMAIL));
					alertModel.alertSenderName = cursor.getString(cursor
							.getColumnIndex(ALERT_SENDER_NAME));
					
					alertModel.alertReceiverId = cursor.getString(cursor
							.getColumnIndex(ALERT_RECEIVER_ID));
					alertModel.alertReceiverEmail = cursor.getString(cursor
							.getColumnIndex(ALERT_RECEIVER_EMAIL));
					alertModel.alertReceiverName = cursor.getString(cursor
							.getColumnIndex(ALERT_RECEIVER_NAME));

					alertModel.alertEventTitle = cursor.getString(cursor
							.getColumnIndex(ALERT_EVENT_TITLE));
					alertModel.alertEventLocation = cursor.getString(cursor
							.getColumnIndex(ALERT_EVENT_LOCATION));
					alertModel.alertEventDuration = cursor.getInt(cursor
							.getColumnIndex(ALERT_EVENT_DURATION));
					alertModel.alertDescription = cursor.getString(cursor
							.getColumnIndex(ALERT_DESCRIPTION));

					alertModel.alertPreferredDate = stringToDate(cursor
							.getString(cursor
									.getColumnIndex(ALERT_PREFERRED_DATETIME)));
					alertModel.alertAlt1Date = stringToDate(cursor
							.getString(cursor
									.getColumnIndex(ALERT_ALT1_DATETIME)));
					alertModel.alertAlt2Date = stringToDate(cursor
							.getString(cursor
									.getColumnIndex(ALERT_ALT2_DATETIME)));

					alertModel.alertEventPriority = cursor.getString(cursor
							.getColumnIndex(ALERT_EVENT_PRIORITY));
					alertModel.alertText = cursor.getString(cursor
							.getColumnIndex(ALERT_TEXT));
					alertModel.alertAccessedDate = stringToDate(cursor
							.getString(cursor
									.getColumnIndex(ALERT_ACCESSED_DATETIME)));

					alertModel.alertMsgId = cursor.getString(cursor
							.getColumnIndex(ALERT_MSG_ID));
					alertModel.alertMsgAction = cursor.getString(cursor
							.getColumnIndex(ALERT_MSG_ACTION));
					alertModel.alertMsgCreatedDate = stringToDate(cursor
							.getString(cursor
									.getColumnIndex(ALERT_MSG_CREATED_DATETIME)));

					if (cursor.getInt(cursor.getColumnIndex(ALERT_DISPLAYED)) == 0) {
						alertModel.isDisplayed = false;
					} else {
						alertModel.isDisplayed = true;
					}

					if (cursor.getInt(cursor.getColumnIndex(ALERT_READ)) == 0) {
						alertModel.isRead = false;
					} else {
						alertModel.isRead = true;
					}

					if (cursor.getInt(cursor.getColumnIndex(ALERT_HANDLED)) == 0) {
						alertModel.isHandled = false;
					} else {
						alertModel.isHandled = true;
					}
					
					if (cursor.getInt(cursor.getColumnIndex(ALERT_IS_SENT_MESSAGE_SUCCESSFULLY)) == 0) {
						alertModel.isSentMessageSuccessfully = false;
					} else {
						alertModel.isSentMessageSuccessfully = true;
					}
					
					alertModel.currentType = cursor.getInt(cursor.getColumnIndex(ALERT_TYPE));

					if (cursor.getInt(cursor.getColumnIndex(ALERT_ACCEPTED)) == 0) {
						alertModel.isAccepted = false;
					} else {
						alertModel.isAccepted = true;
					}
					alertModel.alertAcceptedDate = stringToDate(cursor
							.getString(cursor
									.getColumnIndex(ALERT_ACCEPTED_DATETIME)));
					alertModel.alertResponseStatus = cursor.getInt(cursor
							.getColumnIndex(ALERT_RESPONSE_STATUS));
					alertModel.alertCellTaskPriority = cursor.getInt(cursor
							.getColumnIndex(ALERT_TASK_PRIORITY));
					

					alertModel.alertCreatedDate = stringToDate(cursor
							.getString(cursor
									.getColumnIndex(ALERT_CREATED_DATETIME)));
					alertModel.alertModifiedDate = stringToDate(cursor
							.getString(cursor
									.getColumnIndex(ALERT_MODIFIED_DATETIME)));

				} catch (Exception e) {
					// ignore
				}
			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();

		return alertModel;
	}

	public ArrayList<ATLAlertModel> loadAllAlertInDatabase() {
		DB.openDb();
		final SQLiteDatabase db = DB.db;
		final String whereClause = null;
		final String[] columns = null;

		final Cursor cursor = db.query(DBDefines.ATL_ALERTS, columns,
				whereClause, null, "", "", "", "");
		final ArrayList<ATLAlertModel> list = new ArrayList<ATLAlertModel>();
		if (cursor.moveToFirst()) {
			do {
				ATLAlertModel alertModel = new ATLAlertModel();
				try {


					alertModel.alertId = cursor.getInt(cursor
							.getColumnIndex(ALERT_ID));
					alertModel.alertUuid = cursor.getString(cursor
							.getColumnIndex(ALERT_UUID));
					alertModel.alertAtlasId = cursor.getString(cursor
							.getColumnIndex(ALERT_ATLAS_ID));

					alertModel.alertSenderId = cursor.getString(cursor
							.getColumnIndex(ALERT_SENDER_ID));
					alertModel.alertSenderEmail = cursor.getString(cursor
							.getColumnIndex(ALERT_SENDER_EMAIL));
					alertModel.alertSenderName = cursor.getString(cursor
							.getColumnIndex(ALERT_SENDER_NAME));
					
					alertModel.alertReceiverId = cursor.getString(cursor
							.getColumnIndex(ALERT_RECEIVER_ID));
					alertModel.alertReceiverEmail = cursor.getString(cursor
							.getColumnIndex(ALERT_RECEIVER_EMAIL));
					alertModel.alertReceiverName = cursor.getString(cursor
							.getColumnIndex(ALERT_RECEIVER_NAME));

					alertModel.alertEventTitle = cursor.getString(cursor
							.getColumnIndex(ALERT_EVENT_TITLE));
					alertModel.alertEventLocation = cursor.getString(cursor
							.getColumnIndex(ALERT_EVENT_LOCATION));
					alertModel.alertEventDuration = cursor.getInt(cursor
							.getColumnIndex(ALERT_EVENT_DURATION));
					alertModel.alertDescription = cursor.getString(cursor
							.getColumnIndex(ALERT_DESCRIPTION));

					alertModel.alertPreferredDate = stringToDate(cursor
							.getString(cursor
									.getColumnIndex(ALERT_PREFERRED_DATETIME)));
					alertModel.alertAlt1Date = stringToDate(cursor
							.getString(cursor
									.getColumnIndex(ALERT_ALT1_DATETIME)));
					alertModel.alertAlt2Date = stringToDate(cursor
							.getString(cursor
									.getColumnIndex(ALERT_ALT2_DATETIME)));

					alertModel.alertEventPriority = cursor.getString(cursor
							.getColumnIndex(ALERT_EVENT_PRIORITY));
					alertModel.alertText = cursor.getString(cursor
							.getColumnIndex(ALERT_TEXT));
					alertModel.alertAccessedDate = stringToDate(cursor
							.getString(cursor
									.getColumnIndex(ALERT_ACCESSED_DATETIME)));

					alertModel.alertMsgId = cursor.getString(cursor
							.getColumnIndex(ALERT_MSG_ID));
					alertModel.alertMsgAction = cursor.getString(cursor
							.getColumnIndex(ALERT_MSG_ACTION));
					alertModel.alertMsgCreatedDate = stringToDate(cursor
							.getString(cursor
									.getColumnIndex(ALERT_MSG_CREATED_DATETIME)));

					if (cursor.getInt(cursor.getColumnIndex(ALERT_DISPLAYED)) == 0) {
						alertModel.isDisplayed = false;
					} else {
						alertModel.isDisplayed = true;
					}

					if (cursor.getInt(cursor.getColumnIndex(ALERT_READ)) == 0) {
						alertModel.isRead = false;
					} else {
						alertModel.isRead = true;
					}

					if (cursor.getInt(cursor.getColumnIndex(ALERT_HANDLED)) == 0) {
						alertModel.isHandled = false;
					} else {
						alertModel.isHandled = true;
					}
					
					if (cursor.getInt(cursor.getColumnIndex(ALERT_IS_SENT_MESSAGE_SUCCESSFULLY)) == 0) {
						alertModel.isSentMessageSuccessfully = false;
					} else {
						alertModel.isSentMessageSuccessfully = true;
					}
					
					alertModel.currentType = cursor.getInt(cursor.getColumnIndex(ALERT_TYPE));

					if (cursor.getInt(cursor.getColumnIndex(ALERT_ACCEPTED)) == 0) {
						alertModel.isAccepted = false;
					} else {
						alertModel.isAccepted = true;
					}
					alertModel.alertAcceptedDate = stringToDate(cursor
							.getString(cursor
									.getColumnIndex(ALERT_ACCEPTED_DATETIME)));
					alertModel.alertResponseStatus = cursor.getInt(cursor
							.getColumnIndex(ALERT_RESPONSE_STATUS));
					alertModel.alertCellTaskPriority = cursor.getInt(cursor
							.getColumnIndex(ALERT_TASK_PRIORITY));
					

					alertModel.alertCreatedDate = stringToDate(cursor
							.getString(cursor
									.getColumnIndex(ALERT_CREATED_DATETIME)));
					alertModel.alertModifiedDate = stringToDate(cursor
							.getString(cursor
									.getColumnIndex(ALERT_MODIFIED_DATETIME)));

					list.add(alertModel);
				} catch (Exception e) {
					// ignore
				}
			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();
		return list;
	}

	public boolean isExistInDatabase(ATLAlertModel model) {
		DB.openDb();
		final SQLiteDatabase db = DB.db;
		final String whereClause = ALERT_ATLAS_ID + "=?";
		final String[] whereArgs = new String[] { String.valueOf(model.alertAtlasId) };
		final String[] columns = null;
		final Cursor cursor = db.query(DBDefines.ATL_ALERTS, columns,
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
			return null;
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

}
