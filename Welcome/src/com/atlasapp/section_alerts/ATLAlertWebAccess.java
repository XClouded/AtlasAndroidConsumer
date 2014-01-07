//  ==================================================================================================================
//  ATLAlertWebAccess.java
//  ATLAS
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================

package com.atlasapp.section_alerts;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;


import android.content.Intent;

import com.atlasapp.atlas_database.AtlasServerConnect;
import com.atlasapp.common.ATLConstants;
import com.atlasapp.common.ATLConstants.AlertType;
import com.atlasapp.common.AtlasAndroidUser;
import com.atlasapp.common.CalendarUtilities;
import com.atlasapp.common.DateTimeUtilities;
import com.atlasapp.common.SendEmailActivity;
import com.atlasapp.section_calendar.ATLCalCellData;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class ATLAlertWebAccess {

	public ATLAlertWebAccessCallBackInterface delegate;
	public String invitee = "";
	public String inviter = "";
	public String atlasId = "";
	public String inviteId = "";
	public String inviteeName = "";
	public String inviteeEmail = "";
	public String inviterName = "";
	public String inviterEmail = "";

	public String fromDisplayname = "";
	public int eventDuration = 0;
	public String eventLocation = "";
	public String eventTitle = "";
	public String timeChosen = "";
	public int timeLabel = 0;
	public String objectId = "";

	public String userId = "";
	public String userDisplayname = "";
	public String username = "";
	public String userEmail = "";

	public Object currentData;

	public ATLAlertWebAccess() {

		// TODO Auto-generated constructor stub
		userId = AtlasAndroidUser.getParseUserID();
		userEmail = AtlasAndroidUser.getEmail();
		userDisplayname = AtlasAndroidUser.getUserNameDisplay();
		username = AtlasAndroidUser.getEmail();
	}

	// ==================================================================================================================
	// GETPAGE ASYNC
	// ==================================================================================================================
	public void getPage_AsyncWithType(int alertType, Object item) {
		switch (alertType) {
		case AlertType.eventInvited_Sent: {
			asyncEventInvitedSendWithData(item);
			break;
		}
		case AlertType.eventInvited_Received: {
			asyncEventInvitedReceived();
			break;
		}
		case AlertType.eventAccepted_Sent: {
			asyncEventAcceptSendWithData(item);
			break;
		}
		case AlertType.eventAccepted_Received: {
			asyncEventAcceptReceived();
			break;
		}
		case AlertType.taskAssigned_Sent: {
			asyncTaskAssignSendWithData(item);
			break;
		}
		case AlertType.taskAssigned_Received: {
			asyncTaskAssignReceived();
			break;
		}
		case AlertType.taskAccepted_Sent: {
			asyncTaskAcceptSendWithData(item);
			break;
		}
		case AlertType.taskAccepted_Received: {
			asyncTaskAcceptReceived();
			break;
		}
		case AlertType.taskComplete_Sent: {
			asyncTaskCompleteSendWithData(item);
			break;
		}
		case AlertType.taskComplete_Received: {
			asyncTaskCompleteReceived();
			break;
		}
		case AlertType.notesShare_Sent: {
			asyncNoteShareSendWithData(item);
			break;
		}
		case AlertType.notesShare_Received: {
			asyncNoteShareReceived();
			break;
		}
		default:
			break;
		}
	}

	private void asyncNoteShareReceived() {
		// TODO Auto-generated method stub

	}

	private void asyncNoteShareSendWithData(Object item) {
		// TODO Auto-generated method stub

	}

	private void asyncTaskCompleteReceived() {
		// TODO Auto-generated method stub

	}

	private void asyncTaskCompleteSendWithData(Object item) {
		// TODO Auto-generated method stub

	}

	private void asyncTaskAcceptReceived() {
		// TODO Auto-generated method stub
		invitee = userId;

		ParseQuery query = new ParseQuery(
				ATLConstants.kAtlasEZTaskAcceptNewClassKey);
		query.whereEqualTo(ATLConstants.kAtlasEZTaskAcceptInviteeKey, invitee);
		query.whereEqualTo(ATLConstants.kAtlasEZTaskAcceptIsReadKey, false);
		query.findInBackground(new FindCallback() {
			@Override
			public void done(List<ParseObject> events, ParseException arg1) {
				if (arg1 == null) {
					if (events.size() > 0) {
						delegate.didGetDataFinish("",
								AlertType.taskAccepted_Received, "");
						delegate.didGetEventList(events,
								AlertType.taskAccepted_Received, "");
					}
				} else {

				}
			}
		});

	}

	private void asyncTaskAcceptSendWithData(Object item) {
		// TODO Auto-generated method stub
		AlertCellData data = (AlertCellData) item;

		final ParseObject parseObject = new ParseObject(
				ATLConstants.kAtlasEZTaskAcceptNewClassKey);
		currentData = item;
		parseObject.put("inviteID", data.alertCellAtlasId);
		parseObject.put("taskID", data.alertCellAtlasId);
		parseObject.put("invitee", data.alertCellRecieverId);
		parseObject.put("inviter", data.alertCellSenderId);
		parseObject.put("isRead", false);
		parseObject.put("task_title", data.alertCellEventTitle);
		parseObject.put("accept", data.isAccepted);
		parseObject.put("date", data.alertCellAcceptedDate);
		parseObject.put("fromID", data.alertCellSenderId);
		parseObject.put("inviteeName", data.alertCellSenderName);
		String message = "";
		if (data.isAccepted) {
			message += "ACCEPT" + ",";
		} else {
			message += "DECLINE" + ",";
		}
		message += DateTimeUtilities.toString(data.alertCellPreferredDatetime)
				+ "," + data.alertCellSenderName + "," + data.alertCellSenderId;
		parseObject.put("message", message);
		parseObject.saveInBackground(new SaveCallback() {
			public void done(ParseException e) {
				AlertCellData data = (AlertCellData) currentData;
				AlertCellData alert = new AlertCellData();
				alert.copy(data);
				alert.alertCellId = -1;// set to save new Alert
				alert.isHandled = true;
				alert.alertCellAtlasId = parseObject.getObjectId();
				alert.isRead = true;
				alert.isDisplayed = false;
				alert.alertCellMsgCreatedDate = new Date();
				if (data.isAccepted) {
					alert.currentType = AlertType.taskAccepted_Sent;
				} else {
					alert.currentType = AlertType.taskReject_Sent;
				}
				alert.alertCellAcceptedDate = DateTimeUtilities
						.toGMTDate(new Date());
				if (e == null) {
					alert.alertCellAtlasId = parseObject.getObjectId();
					alert.isSentMessageSuccessfully = true;
					// TODO push notification
					// TODO Sound
					// TODO lauch message

				} else {
					alert.isSentMessageSuccessfully = false;
				}
				alert.save();

				data.isHandled = true;
				data.save();
			}
		});
	}

	private void asyncTaskAssignReceived() {
		// TODO Auto-generated method stub
		invitee = userId;
		inviteeEmail = userEmail;

		ParseQuery query = new ParseQuery(
				ATLConstants.kAtlasEZTaskAssignNewClassKey);
		query.whereEqualTo(ATLConstants.kAtlasEZTaskAssignToIDKey, invitee);
		query.whereEqualTo(ATLConstants.kAtlasEZTaskAssignIsReadKey, false);
		query.findInBackground(new FindCallback() {
			@Override
			public void done(List<ParseObject> events, ParseException arg1) {
				if (arg1 == null) {
					if (events.size() > 0) {
						delegate.didGetDataFinish("",
								AlertType.taskAssigned_Received, "");
						delegate.didGetEventList(events,
								AlertType.taskAssigned_Received, "");
					} else {
						ParseQuery query = new ParseQuery(
								ATLConstants.kAtlasEZTaskAssignNewClassKey);
						query.whereEqualTo(
								ATLConstants.kAtlasEZTaskAssignToEmailKey,
								inviteeEmail);
						query.whereEqualTo(
								ATLConstants.kAtlasEZTaskAssignIsReadKey, false);
						query.findInBackground(new FindCallback() {
							@Override
							public void done(List<ParseObject> events,
									ParseException arg1) {
								if (arg1 == null) {
									if (events.size() > 0) {
										delegate.didGetDataFinish(
												"",
												AlertType.taskAssigned_Received,
												"");
										delegate.didGetEventList(
												events,
												AlertType.taskAssigned_Received,
												"");
									} else {

									}
								} else {

								}
							}
						});
					}
				} else {

				}
			}
		});

	}

	private void asyncTaskAssignSendWithData(Object item) {
		// TODO Auto-generated method stub

	}

	private void asyncEventAcceptReceived() {
		// TODO Auto-generated method stub
		inviter = userId;
		ParseQuery query = new ParseQuery(
				ATLConstants.kAtlasEZEventAcceptNewClassKey);
		query.whereEqualTo(ATLConstants.kAtlasEZEventAcceptInviterKey, inviter);
		query.whereEqualTo(ATLConstants.kAtlasEZEventAcceptIsReadKey, false);
		query.findInBackground(new FindCallback() {

			@Override
			public void done(List<ParseObject> events, ParseException arg1) {
				if (arg1 == null) {
					if (events.size() > 0) {
						delegate.didGetDataFinish("",
								AlertType.eventAccepted_Received, "");
						delegate.didGetEventList(events,
								AlertType.eventAccepted_Received, "");
					}
				} else {

				}
			}
		});

	}

	private void asyncEventAcceptSendWithData(Object item) {
		AlertCellData data = (AlertCellData) item;
		currentData = item;
		Date dateAccepted;
		String sendString;
		if (data.alertCellAcceptedDate != null) {
			dateAccepted = data.alertCellAcceptedDate;
			data.isAccepted = true;
			data.alertCellMsgAction = "EventAcceptSend";
			sendString = "1,";
		} else {
			dateAccepted = data.alertCellPreferredDatetime;
			data.isAccepted = false;
			data.alertCellMsgAction = "EventRejectSent";
			sendString = "0,";
		}
		timeChosen = AlertParseObjectParser
				.localDateTimeToServerTimeString(dateAccepted);
		sendString += sendString + timeChosen + "," + eventTitle;

		final ParseObject newAcceptSend = new ParseObject(
				ATLConstants.kAtlasEZEventAcceptNewClassKey);
		if (inviteId.length() > 0) {
			newAcceptSend.put("inviteID", inviteId);
		}
		if (invitee.length() > 0) {
			newAcceptSend.put("invitee", invitee);
			newAcceptSend.put("toID", invitee);
		}
		if (inviter.length() > 0) {
			newAcceptSend.put("inviter", inviter);
			newAcceptSend.put("fromID", inviter);
		}
		if (fromDisplayname.length() > 0) {
			newAcceptSend.put("inviteeName", fromDisplayname);
		}
		if (eventLocation.length() > 0) {
			newAcceptSend.put("event_location", eventLocation);
			newAcceptSend.put("location", eventLocation);
		}
		if (eventTitle.length() > 0) {
			newAcceptSend.put("event_title", eventTitle);
			newAcceptSend.put("title", eventTitle);
		}
		if (timeChosen.length() > 0) {
			newAcceptSend.put("time_choosen", timeChosen);
		}
		if (dateAccepted != null) {
			newAcceptSend.put("date_choosen", dateAccepted);
		}
		if (sendString.length() > 0) {
			newAcceptSend.put("message", sendString);
		}
		newAcceptSend.put("isAccepted", data.isAccepted);
		newAcceptSend.put("isRead", false);
		newAcceptSend.put("time_label", timeLabel);// 1 2 3
		newAcceptSend.put("event_duration", eventDuration);
		newAcceptSend.put("duration", eventDuration);
		newAcceptSend.saveInBackground(new SaveCallback() {
			public void done(ParseException e) {
				// Now let's update it with some new data. In this case, only
				// cheatMode and score
				// will get sent to the Parse Cloud. playerName hasn't changed.
				AlertCellData data = (AlertCellData) currentData;
				data.isHandled = true;
				data.save();

				AlertCellData newData = new AlertCellData();
				newData.copy(data);
				newData.alertCellAtlasId = "Se"+newAcceptSend.getObjectId();
				newData.isHandled = true;
				newData.isRead = true;
				newData.alertCellMsgCreatedDate = new Date();
				newData.alertCellCreatedDate = newAcceptSend.getCreatedAt();
				newData.alertCellModifiedDate = newAcceptSend.getUpdatedAt();
				if (newData.isAccepted) {
					newData.currentType = AlertType.eventAccepted_Sent;
				} else {
					newData.currentType = AlertType.eventRejected_Sent;
				}
				if (e == null) {
					newData.isSentMessageSuccessfully = true;
					// TODO push notification
					// TODO Sound
					// TODO lauch message
					// TODO handle is sent or not be sent
					// delegate.didPostDataFinish(currentData,
					// AlertType.eventInvited_Sent, "");
					LinkedList<String> channels = new LinkedList<String>();
					String message =  AtlasAndroidUser.getUserNameDisplay()+" has confirmed "+eventTitle+" at "+timeChosen;
					channels.add("ID"+newData.alertCellSenderId);
					JSONObject jsonObject = new JSONObject();
					try {  
						jsonObject = new JSONObject("{\"alert\": \""+message+"\",\"badge\": \"Increment\",\"sound\": \"Incoming_Atlas_Push.mp3\"}");
					} catch (JSONException ef) {
						// TODO Auto-generated catch block     
						ef.printStackTrace();
					}
					if (jsonObject!=null && channels.size()>0 && message!=null && !message.equals(""))
						AtlasServerConnect.pushNotification(channels,message,jsonObject);

				} else {
					newData.isSentMessageSuccessfully = false;
				}

				
				newData.save();
				
				// TODO push notification
				// TODO Sound
				// TODO lauch message
				delegate.didPostDataFinish(newData,
						AlertType.eventAccepted_Sent, String.valueOf(timeLabel));
			}
		});
	}

	private void asyncEventInvitedSendWithData(Object item) {
		// TODO Auto-generated method stub
		ATLCalCellData data = (ATLCalCellData) item;

		final ParseObject newAcceptSend = new ParseObject(
				ATLConstants.kAtlasEZEventInviteNewClassKey);
		currentData = item;
		String pref;
		String alt1;
		String alt2;
		String sendString;

		if (data.calCellPreferDateTime != null) {
			pref = AlertParseObjectParser
					.stringFromDate(data.calCellPreferDateTime);
			newAcceptSend.put("pref_date", data.calCellPreferDateTime);
		} else {
			pref = "";
		}

		if (data.calCellALT2Datetime != null) {
			alt1 = AlertParseObjectParser
					.stringFromDate(data.calCellALT2Datetime);
			newAcceptSend.put("alt_one_date", data.calCellALT2Datetime);
		} else {
			alt1 = "";
		}

		if (data.calCellALT3Datetime != null) {
			alt2 = AlertParseObjectParser
					.stringFromDate(data.calCellALT3Datetime);
			newAcceptSend.put("alt_two_date", data.calCellALT3Datetime);
		} else {
			alt2 = "";
		}

		sendString = data.calCellTitle + "," + data.calCellLocation + ","
				+ data.calCellDurationMinutes + "," + "PREF" + "," + pref + ","
				+ "ALT1" + "," + alt1 + "," + "ALT2" + "," + alt2 + ","
				+ userDisplayname + "," + userId + ","
				+ data.attendee.displayName();
		
		if (inviteId.length() > 0) {
			newAcceptSend.put("inviteID", inviteId);
		}
		
		if (invitee.length() > 0) {
			newAcceptSend.put("invitee", invitee);
			newAcceptSend.put("toID", invitee);
		}
		if (inviteeEmail.length() > 0) {
			newAcceptSend.put("toEmail", inviteeEmail);
			newAcceptSend.put("inviteeEmail", inviteeEmail);
		}
		if (inviteeName.length() > 0) {
			newAcceptSend.put("toName", inviteeName);
			newAcceptSend.put("inviteeName", inviteeName);
		}

		if (inviter.length() > 0) {
			newAcceptSend.put("inviter", inviter);
			newAcceptSend.put("fromID", inviter);
		}
		if (inviterName.length() > 0) {
			newAcceptSend.put("inviterName", inviterName);
			newAcceptSend.put("fromName", inviterName);
		}
		if (inviterEmail.length() > 0) {
			newAcceptSend.put("inviterEmail", inviterEmail);
			newAcceptSend.put("fromEmail", inviterEmail);
		}
		
		if (eventLocation.length() > 0) {
			newAcceptSend.put("event_location", eventLocation);
			newAcceptSend.put("location", eventLocation);
		}
		if (eventTitle.length() > 0) {
			newAcceptSend.put("event_title", eventTitle);
			newAcceptSend.put("title", eventTitle);
		}
		if (sendString.length() > 0) {
			newAcceptSend.put("message", sendString);
		}
		newAcceptSend.put("event_duration", eventDuration);
		newAcceptSend.put("duration", eventDuration);

		if(data.calCellPreferDateTime != null){
			newAcceptSend.put("pref_day", data.calCellPreferDateTime);
		}
		if(data.calCellALT2Datetime != null){
			newAcceptSend.put("alt_one_day", data.calCellALT2Datetime);
		}
		if(data.calCellALT3Datetime != null){
			newAcceptSend.put("alt_two_day", data.calCellALT3Datetime);
		}
		
		newAcceptSend.put("respond", false);
		newAcceptSend.put("isRead", false);
		newAcceptSend.saveInBackground(new SaveCallback() {
			public void done(ParseException e) {
				ATLCalCellData data = (ATLCalCellData) currentData;
				AlertCellData alert = new AlertCellData();
				alert.isHandled = true;
				alert.isRead = true;
				alert.alertCellAtlasId = "RC"+newAcceptSend.getObjectId();
				alert.isDisplayed = false;
				alert.alertCellMsgCreatedDate = DateTimeUtilities
						.toGMTDate(new Date());
				alert.alertCellModifiedDate = DateTimeUtilities
						.toGMTDate(new Date());
				alert.alertCellCreatedDate = DateTimeUtilities
						.toGMTDate(new Date());
				alert.currentType = AlertType.eventInvited_Sent;
				if (e == null) {
					alert.alertCellEventTitle = data.calCellTitle;
					alert.alertCellEventLocation = data.calCellLocation;
					alert.alertCellEventDuration = data.calCellDurationMinutes;
					alert.alertCellPreferredDatetime = data.calCellPreferDateTime;
					alert.alertCellAlt1DateTime = data.calCellALT2Datetime;
					alert.alertCellAlt2DateTime = data.calCellALT3Datetime;
					alert.alertCellRecieverId = invitee;
					alert.alertCellRecieverName = inviteeName;
					alert.alertCellSenderId = inviter;
					alert.alertCellSenderName = inviterName;
					alert.isSentMessageSuccessfully = true;
					// TODO push notification
					// TODO Sound
					// TODO lauch message
					// TODO handle is sent or not be sent
					// delegate.didPostDataFinish(currentData,
					// AlertType.eventInvited_Sent, "");LinkedList<String> channels = new LinkedList<String>();
					if(alert.alertCellRecieverId != null){
						LinkedList<String> channels = new LinkedList<String>();
						String message =  alert.alertCellSenderName+" has invited you to "+alert.alertCellEventTitle+". Confirm event?";
						channels.add("ID"+alert.alertCellRecieverId);
						JSONObject jsonObject = new JSONObject();
						try {  
							jsonObject = new JSONObject("{\"alert\": \""+message+"\",\"badge\": \"Increment\",\"sound\": \"Incoming_Atlas_Push.mp3\"}");
						} catch (JSONException ef) {
							// TODO Auto-generated catch block     
							ef.printStackTrace();
						}
						if (jsonObject!=null && channels.size()>0 && message!=null && !message.equals(""))
							AtlasServerConnect.pushNotification(channels,message,jsonObject);
					}else{
						Intent intent = new Intent(AtlasServerConnect.activity.getBaseContext(),SendEmailActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
						intent.putExtra("fromName", inviterName);
						intent.putExtra("name", inviteeName);
						intent.putExtra("atlasId", newAcceptSend.getObjectId());
						intent.putExtra("emailAddress",inviteeEmail);
						intent.putExtra("item", "eventm");
						intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						AtlasServerConnect.activity.getBaseContext().startActivity(intent);   
					}

				} else {
					alert.isSentMessageSuccessfully = false;
				}

				alert.save();
			}
		});
	}

	private void asyncEventInvitedReceived() {
		invitee = userId;
		inviteeEmail = userEmail;
		ParseQuery query = new ParseQuery(
				ATLConstants.kAtlasEZEventInviteNewClassKey);
		query.whereEqualTo(ATLConstants.kAtlasEZEventInviteToIDKey, invitee);
		query.whereEqualTo(ATLConstants.kAtlasEZEventInviteIsReadKey, false);
		query.findInBackground(new FindCallback() {

			@Override
			public void done(List<ParseObject> events, ParseException arg1) {
				if (arg1 == null) {
					if (events.size() > 0) {
						delegate.didGetDataFinish("",
								AlertType.eventInvited_Received, "");
						delegate.didGetEventList(events,
								AlertType.eventInvited_Received, "");
					} else {
						/*
						 * Nghia Cover the case dont have atlas ID
						 */
						ParseQuery query = new ParseQuery(
								ATLConstants.kAtlasEZEventInviteNewClassKey);
						query.whereEqualTo(
								ATLConstants.kAtlasEZEventInviteToEmailKey,
								inviteeEmail);
						query.whereEqualTo(
								ATLConstants.kAtlasEZEventInviteIsReadKey,
								false);
						query.findInBackground(new FindCallback() {

							@Override
							public void done(List<ParseObject> events,
									ParseException arg1) {
								if (arg1 == null) {
									if (events.size() > 0) {
										delegate.didGetDataFinish(
												"",
												AlertType.eventInvited_Received,
												"");
										delegate.didGetEventList(
												events,
												AlertType.eventInvited_Received,
												"");
									} else {

									}
								} else {

								}
							}
						});
					}
				} else {

				}
			}
		});
	}

	// private String toGmtString(Date date) {
	// // date formatter
	// SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
	// // sd.setTimeZone(TimeZone.getTimeZone("UTC"));
	// String return_date = sd.format(date);
	// return return_date;
	// }
}
