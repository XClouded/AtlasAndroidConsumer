//  ==================================================================================================================
//  AlertCellData.java
//  ATLAS
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2013-01-10 NGHIA:   Change alertSenderAtlasId to alertSenderId
//  ==================================================================================================================

package atlasapp.section_alerts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import android.util.Log;
import atlasapp.common.ATLConstants;
import atlasapp.common.ATLConstants.AlertType;
import atlasapp.common.ATLUser;
import atlasapp.common.DateTimeUtilities;
import atlasapp.section_tasks.ATLTaskPriorityScrollListAdapter;
import atlasapp.section_tasks.TaskDatabaseAdapter;


import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class AlertParseObjectParser {
	static public ATLAlertWebAccessCallBackInterface delegate;

	public AlertParseObjectParser() {
		// TODO Auto-generated constructor stub
	}

	public static Date dateFromString(String serverTimeString) {
		if (serverTimeString.equals("") || serverTimeString.equals(null)) {
			return null;
		}
		SimpleDateFormat lv_formatter;
		lv_formatter = new SimpleDateFormat("yyyy:dd:MM HH:mm:ss");
		lv_formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date = new Date();
		try {
			date = lv_formatter.parse(serverTimeString);
			return date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static String stringFromDate(Date date) {
		if (date == null) {
			return null;
		}
		String serverTimeFormat = "yyyy:dd:MM HH:mm:ss";// 2012:29:11 03:00:00
		// UTC
		SimpleDateFormat sd = new SimpleDateFormat(serverTimeFormat);
		sd.setTimeZone(TimeZone.getTimeZone("UTC"));
		String serverDateString = sd.format(date);
		return serverDateString;
	}

	public static Date serverTimeStringToLocalDateTime(String serverTimeString) {
		if (serverTimeString.equals("") || serverTimeString.equals(null)) {
			return null;
		}
		Calendar localCal = Calendar.getInstance();

		String serverTimeFormat = "yyyy:dd:MM HH:mm:ss";// 2012:29:11 03:00:00
														// UTC
		SimpleDateFormat sd = new SimpleDateFormat(serverTimeFormat);
		sd.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		Date date = new Date();
		try {
			date = sd.parse(serverTimeString);
			localCal.setTime(date);
			return localCal.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static String localDateTimeToServerTimeString(Date localDateTime) {
		if (localDateTime == null) {
			return null;
		}
		Calendar localCal = Calendar.getInstance(TimeZone
				.getTimeZone("GMT+00:00"));
		localCal.setTime(localDateTime);
		String serverTimeFormat = "yyyy:dd:MM HH:mm:ss";// 2012:29:11 03:00:00
		// UTC
		SimpleDateFormat sd = new SimpleDateFormat(serverTimeFormat);
		sd.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		String serverDateString = sd.format(localCal.getTime());
		return serverDateString;
	}

	public static ArrayList<AlertCellData> alertCellDataListFromParseObjectList(
			List<ParseObject> parseObjectList, int alertType) {
		// TODO Auto-generated method stub
		switch (alertType) {
		case AlertType.eventInvited_Received: {
			break;
		}
		case AlertType.eventAccepted_Received: {
			ArrayList<AlertCellData> alertCellDataList = new ArrayList<AlertCellData>();
			// Log.v("AlertsParseObjectParser", events.toString());
			for (final ParseObject parseObject : parseObjectList) {

				final AlertCellData alertCellData = new AlertCellData();

//				String message = parseObject.getString("message");
//				Log.v("AlertsParseObjectParser message", message);
//				final String[] itemStrings = message.split(",");
//
//				alertCellData.isAccepted = Boolean.valueOf(itemStrings[0]);
				alertCellData.isAccepted = parseObject.getBoolean("isAccepted");
				
				alertCellData.alertCellAtlasId = parseObject.getObjectId();
				alertCellData.alertCellEventDuration = parseObject
						.getInt("duration");
				alertCellData.alertCellEventLocation = parseObject
						.getString("location");
				alertCellData.alertCellEventTitle = parseObject
						.getString("title");
				// TODO inviteID
				alertCellData.alertCellRecieverId = parseObject
						.getString("invitee");
				alertCellData.alertCellRecieverName = parseObject
						.getString("inviteeName");
				alertCellData.alertCellSenderId = parseObject
						.getString("inviter");
				alertCellData.alertCellAcceptedDate = dateFromString(parseObject
						.getString("time_choosen"));
//				Date dateAccept = parseObject.getDate("date_choosen");
//				if (dateAccept != null) {
//					alertCellData.alertCellAcceptedDate = dateAccept;
//				} else {
//					alertCellData.alertCellAcceptedDate = dateFromString(parseObject
//							.getString("time_choosen"));
//				}
				alertCellData.alertTimeChosenLabel = parseObject
						.getInt("time_label");
				alertCellData.alertCellCreatedDate = parseObject.getCreatedAt();
				alertCellData.alertCellModifiedDate = parseObject
						.getUpdatedAt();
				alertCellData.alertCellAccessedDate = DateTimeUtilities
						.toGMTDate(new Date());

				alertCellData.alertCellMsgId = "";
				alertCellData.alertCellMsgAction = "Event Accept Received";
				alertCellData.alertCellMsgCreatedDate = parseObject
						.getCreatedAt();

				alertCellData.isDisplayed = true;
				alertCellData.isHandled = true;
				alertCellData.isRead = parseObject.getBoolean("isRead");
				if (alertCellData.isAccepted) {
					alertCellData.currentType = AlertType.eventAccepted_Received;
				} else {
					alertCellData.currentType = AlertType.eventRejected_Received;
				}

				// SAVE OBJECT AS HAS READ
				parseObject
						.put(ATLConstants.kAtlasEZEventAcceptIsReadKey, true);
				parseObject.saveInBackground();
				// TODO Push Notification
				// JSONObject data;
				if (alertCellData.isAccepted) {
				} else {
				}
				// final String[] itemStrings = message.split(",");

				alertCellDataList.add(alertCellData);
			}
			return alertCellDataList;

		}
		case AlertType.taskAssigned_Received: {
			ArrayList<AlertCellData> alertCellDataList = new ArrayList<AlertCellData>();
			// Log.v("AlertsParseObjectParser", events.toString());
			for (final ParseObject parseObject : parseObjectList) {
				final AlertCellData alertCellData = new AlertCellData();
				alertCellData.alertCellRecieverId = ATLUser
						.getParseUserID();
				alertCellData.alertCellReceiverEmail = ATLUser
						.getEmail();
				alertCellData.alertCellRecieverName = ATLUser
						.getUserNameDisplay();

				alertCellData.alertCellSenderId = parseObject
						.getString("fromID");
				alertCellData.alertCellSenderEmail = parseObject
						.getString("fromEmail");
				alertCellData.alertCellSenderName = parseObject
						.getString("fromName");

				alertCellData.alertCellEventTitle = parseObject
						.getString("title");
				alertCellData.alertCellDescription = parseObject
						.getString("content");
				String priority = "";
				try {
					priority = parseObject.getString("category").toLowerCase();
				} catch (Exception e) {
					// TODO: handle exception
					priority = "";
				}
				if (priority.equals("low")) {
					alertCellData.alertCellTaskPriority = ATLTaskPriorityScrollListAdapter.TASK_PRIORITY_INDEX_LOW;
				} else if (priority.equals("medium")) {
					alertCellData.alertCellTaskPriority = ATLTaskPriorityScrollListAdapter.TASK_PRIORITY_INDEX_MEDIUM;
				} else {
					alertCellData.alertCellTaskPriority = ATLTaskPriorityScrollListAdapter.TASK_PRIORITY_INDEX_HIGH;
				}
				alertCellData.alertCellPreferredDatetime = parseObject
						.getDate("date");
				alertCellData.alertCellText = parseObject.getString("message");
				alertCellData.isSentMessageSuccessfully = true;
				alertCellData.currentType = AlertType.taskAssigned_Received;
				alertCellData.alertCellAcceptedDate = new Date();
				alertCellData.isHandled = false;
				alertCellData.alertCellUuid = parseObject.getObjectId();
				alertCellData.alertCellAtlasId = parseObject.getObjectId();
				alertCellData.alertCellCreatedDate = parseObject.getCreatedAt();
				Date date = new Date();
				alertCellData.alertCellAccessedDate = date;

				alertCellData.alertCellMsgId = "";
				alertCellData.alertCellModifiedDate = parseObject.getUpdatedAt();
				alertCellData.alertCellMsgAction = "Task Assign Received";
				alertCellData.alertCellMsgCreatedDate = parseObject
						.getCreatedAt();

				alertCellData.alertCellEventPriority = parseObject
						.getString("category");
				alertCellData.isDisplayed = false;
				alertCellData.isRead = parseObject.getBoolean("isRead");

				// SAVE OBJECT AS HAS READ
				parseObject
						.put(ATLConstants.kAtlasEZEventAcceptIsReadKey, true);
				parseObject.saveInBackground();

				alertCellDataList.add(alertCellData);
			}
			return alertCellDataList;
		}
		case AlertType.taskAccepted_Received: {
			ArrayList<AlertCellData> alertCellDataList = new ArrayList<AlertCellData>();
			// Log.v("AlertsParseObjectParser", events.toString());
			for (final ParseObject parseObject : parseObjectList) {
				final AlertCellData alertCellData = new AlertCellData();

				alertCellData.alertCellSenderId = ATLUser
						.getParseUserID();
				alertCellData.alertCellSenderEmail = ATLUser
						.getEmail();
				alertCellData.alertCellSenderName = ATLUser
						.getUserNameDisplay();

				alertCellData.alertCellEventTitle = parseObject
						.getString("task_title");
				alertCellData.alertCellDescription = "";

				String message = parseObject.getString("message");
				final String[] itemStrings = message.split(",");
				if (itemStrings[0].equals("ACCEPT")) {
					alertCellData.currentType = AlertType.taskAccepted_Received;
//					alertCellData.isAccepted = true;
				} else {
					alertCellData.currentType = AlertType.taskReject_Receive;
//					alertCellData.isAccepted = false;
				}
				alertCellData.isAccepted = parseObject.getBoolean("accept");
				
				
				alertCellData.alertCellPreferredDatetime = DateTimeUtilities
						.toDate((itemStrings[1]!=null)?itemStrings[1]:"");
				

				alertCellData.alertCellRecieverId = parseObject
						.getString(ATLConstants.kAtlasEZTaskAcceptInviterKey);
			////SHARON EDITING TO PREVENT NULL CRASH
				alertCellData.alertCellRecieverName = parseObject.getString("toName");

				alertCellData.alertCellText = parseObject.getString("message");
				alertCellData.isSentMessageSuccessfully = true;

				alertCellData.alertCellAcceptedDate = parseObject
						.getUpdatedAt();
				alertCellData.alertCellCreatedDate = parseObject.getCreatedAt();
				alertCellData.alertCellModifiedDate = parseObject
						.getUpdatedAt();

				alertCellData.isHandled = true;

				alertCellData.alertCellUuid = parseObject.getObjectId();
				alertCellData.alertCellAtlasId = parseObject.getObjectId();

				alertCellData.alertCellAccessedDate = DateTimeUtilities
						.toGMTDate(new Date());

				alertCellData.alertCellMsgId = "";
				alertCellData.alertCellMsgAction = "Task Assign Received";
				alertCellData.alertCellMsgCreatedDate = parseObject
						.getCreatedAt();

				alertCellData.isDisplayed = false;
				alertCellData.isRead = false;

				// SAVE OBJECT AS HAS READ
				parseObject.put(ATLConstants.kAtlasEZTaskAcceptIsReadKey, true);
				parseObject.saveInBackground();

				alertCellDataList.add(alertCellData);

				// Update Task based on accepted // NGHIA comment out to avoid
				// crash
				TaskDatabaseAdapter.updateTaskWithDue(
						alertCellData.alertCellPreferredDatetime,
						alertCellData.isAccepted, false);

			}
			return alertCellDataList;
		}
		default:
			break;
		}
		return null;

	}

	public static ArrayList<AlertCellData> toListAlertCellData(
			List<ParseObject> events) {
		// TODO Auto-generated method stub
		ArrayList<AlertCellData> list = new ArrayList<AlertCellData>();
		// Log.v("AlertsParseObjectParser", events.toString());
		for (final ParseObject parseObject : events) {
			final AlertCellData alertCellData = new AlertCellData();
			// Log.v("AlertsParseObjectParser", parseObject.toString());

			String message = parseObject.getString("message");
			Log.v("AlertsParseObjectParser message", message);
			final String[] itemStrings = message.split(",");
			alertCellData.parseObj = parseObject;

			alertCellData.alertCellUuid = "";
			alertCellData.alertCellAtlasId = parseObject.getObjectId();

			// alertCellData.alertCellSenderId =
			// parseObject.getString("inviter");
			alertCellData.alertCellSenderId = parseObject.getString("fromID");
			// alertCellData.alertCellSenderName =
			// itemStrings[event_respondData.event_respondData_senderName];
			alertCellData.alertCellSenderName = parseObject
					.getString("fromName");

			// alertCellData.alertCellRecieverId = parseObject
			// .getString("invitee");
			alertCellData.alertCellRecieverId = parseObject.getString("toID");
			// alertCellData.alertCellRecieverName =
			// itemStrings[event_respondData.event_respondData_recievername];
			alertCellData.alertCellRecieverName = parseObject
					.getString("toName");

			// alertCellData.alertCellEventTitle =
			// itemStrings[event_respondData.event_respondData_title];
			// alertCellData.alertCellEventLocation =
			// itemStrings[event_respondData.event_respondData_location];
			// alertCellData.alertCellEventDuration = Integer
			// .valueOf(itemStrings[event_respondData.event_respondData_duration]);
			alertCellData.alertCellEventTitle = parseObject.getString("title");
			alertCellData.alertCellEventLocation = parseObject
					.getString("location");
			alertCellData.alertCellEventDuration = parseObject
					.getInt("duration");

			alertCellData.alertCellDescription = message;

			// alertCellData.alertCellPreferredDatetime =
			// dateFromString(itemStrings[event_respondData.event_respondData_preferredDatetime]);
			// alertCellData.alertCellAlt1DateTime =
			// dateFromString(itemStrings[event_respondData.event_respondData_Alt1Datetime]);
			// alertCellData.alertCellAlt2DateTime =
			// dateFromString(itemStrings[event_respondData.event_respondData_Alt2Datetime]);

			alertCellData.alertCellPreferredDatetime = parseObject
					.getDate("pref_day");
			alertCellData.alertCellAlt1DateTime = parseObject
					.getDate("alt_one_day");
			alertCellData.alertCellAlt2DateTime = parseObject
					.getDate("alt_two_day");

			alertCellData.alertCellEventPriority = "High";
			Date date = new Date();
			alertCellData.alertCellCreatedDate = parseObject.getCreatedAt();
			alertCellData.alertCellModifiedDate = parseObject.getUpdatedAt();
			alertCellData.alertCellText = message;

			alertCellData.alertCellAccessedDate = date;

			alertCellData.alertCellMsgId = "";
			alertCellData.alertCellMsgAction = "Event Invite Received";
			alertCellData.alertCellMsgCreatedDate = parseObject.getCreatedAt();

			alertCellData.isDisplayed = false;
			alertCellData.isHandled = false;
			alertCellData.isRead = false;
			alertCellData.currentType = AlertType.eventInvited_Received;

			alertCellData.alertCellAcceptedDate = parseObject.getUpdatedAt();

			alertCellData.alertCellObjectID = parseObject.getObjectId();
			;

			// ///////////////////////////////////////////////////////////
			// GET SENDER INFORMATION
			// ///////////////////////////////////////////////////////////
			if (alertCellData.alertCellSenderId != null) {
				ParseQuery query = new ParseQuery(
						ATLConstants.kAtlasEZUserClassKey);
				query.whereEqualTo(ATLConstants.kAtlasEZUserObjectIdKey,
						alertCellData.alertCellSenderId);
				query.findInBackground(new FindCallback() {
					@Override
					public void done(List<ParseObject> users,
							com.parse.ParseException arg1) {
						// TODO Auto-generated method stub
						if (arg1 == null && users.size() > 0) {
							ParseObject user = users.get(0);
							alertCellData.alertCellSenderName = user
									.getString("displayname");
							delegate.didGetSenderName();
							// SAVE OBJECT AS HAS READ
							parseObject.put(
									ATLConstants.kAtlasEZEventInviteIsReadKey,
									true);
							parseObject.saveInBackground();
						} else {

						}
					}
				});
				list.add(alertCellData);
			}

		}
		return list;
	}

}
