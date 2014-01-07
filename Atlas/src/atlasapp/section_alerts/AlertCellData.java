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
//  2013-01-10 NGHIA:   Add more parameter to math with iphone database, add copy contructor and AlertCellDta(ATLAlertModel)
//  ==================================================================================================================

package atlasapp.section_alerts;

import java.util.Date;

import com.parse.ParseObject;

import android.content.Context;
import android.util.Log;
import atlasapp.common.ATLConstants.AlertType;
import atlasapp.common.ATLUser;
import atlasapp.common.CalendarUtilities;
import atlasapp.common.Utilities;
import atlasapp.database.DatabaseConstants.ITEM_USER_TASK_STATUS;
import atlasapp.database.EventProperties;
import atlasapp.database.ItemUserProperties;
import atlasapp.model.ATLAlertModel;



/**
 * @author Ryan Tan
 * 
 */
public class AlertCellData {

	public int alertCellId = 0;
	public String alertCellUuid = "";
	public String alertCellAtlasId = "";

	public String alertCellSenderId = "";
	public String alertCellSenderEmail = "";
	public String alertCellSenderName = "";

	public String alertCellRecieverId = "";
	public String alertCellReceiverEmail = "";
	public String alertCellRecieverName = "";

	public String alertCellEventTitle = "";
	public String alertCellEventLocation = "";
	public int alertCellEventDuration = 0;
	public String alertCellDescription = "";

	public Date alertCellPreferredDatetime = null;
	public Date alertCellAlt1DateTime = null;
	public Date alertCellAlt2DateTime = null;

	public String alertCellEventPriority = "";

	public Date alertCellCreatedDate = null;
	public Date alertCellModifiedDate = null;
	public String alertCellText = "";
	public Date alertCellAccessedDate = null;

	public String alertCellMsgId = "";
	public String alertCellMsgAction = "";
	public Date alertCellMsgCreatedDate = null;

	public boolean isDisplayed = false;
	public boolean isRead = false;
	public boolean isHandled = false;
	public boolean isAccepted = false;

	public Date alertCellAcceptedDate = null;
	public int alertCellResponseStatus = 0;

	public int alertTimeChosenLabel = 0;// just for choose index of event
										// request

	// ATLAlertModel alertsCellData = new ATLAlertModel();

	public String alertCellObjectID = "";
	public ParseObject parseObj = null;
	public String alertDateAccepted = null;

	public int currentType = 0;
	public boolean isSentMessageSuccessfully = false;

	// 2013-01-14 Tan Add # Start
	public boolean isTaskCompleted = false;
	public int alertCellTaskPriority = -1;
	public boolean isShowAcceptBtn = false;
	public int cellIndex = 0;

	public String alertEventPrimaryKey = "";
	public int alertItemUserStatus = 0;
	public boolean isBooked = false;
	public boolean isYourMove = false;
	public boolean isPending = false;

	// 2013-01-14 Tan Add # Start
	public AlertCellData() {
		this.deNull();
		// TODO Auto-generated constructor stub
	}

	public void copy(AlertCellData alertCellData) {
		alertCellData.deNull();
		alertCellId = alertCellData.alertCellId;
		alertCellUuid = alertCellData.alertCellUuid;
		alertCellAtlasId = alertCellData.alertCellAtlasId;

		alertCellSenderId = alertCellData.alertCellSenderId;
		alertCellSenderEmail = alertCellData.alertCellSenderEmail;
		alertCellSenderName = alertCellData.alertCellSenderName;

		alertCellRecieverId = alertCellData.alertCellRecieverId;
		alertCellReceiverEmail = alertCellData.alertCellReceiverEmail;
		alertCellRecieverName = alertCellData.alertCellRecieverName;

		alertCellEventTitle = alertCellData.alertCellEventTitle;
		alertCellEventLocation = alertCellData.alertCellEventLocation;
		alertCellEventDuration = alertCellData.alertCellEventDuration;
		alertCellDescription = alertCellData.alertCellDescription;

		alertCellPreferredDatetime = alertCellData.alertCellPreferredDatetime;
		alertCellAlt1DateTime = alertCellData.alertCellAlt1DateTime;
		alertCellAlt2DateTime = alertCellData.alertCellAlt2DateTime;

		alertCellEventPriority = alertCellData.alertCellEventPriority;
		alertCellCreatedDate = alertCellData.alertCellCreatedDate;
		alertCellModifiedDate = alertCellData.alertCellModifiedDate;
		alertCellText = alertCellData.alertCellText;
		alertCellAccessedDate = alertCellData.alertCellAccessedDate;

		alertCellMsgId = alertCellData.alertCellMsgId;
		alertCellMsgAction = alertCellData.alertCellMsgAction;
		alertCellMsgCreatedDate = alertCellData.alertCellMsgCreatedDate;

		isDisplayed = alertCellData.isDisplayed;
		isRead = alertCellData.isRead;
		isHandled = alertCellData.isHandled;
		isAccepted = alertCellData.isAccepted;

		alertCellAcceptedDate = alertCellData.alertCellAcceptedDate;
		alertCellResponseStatus = alertCellData.alertCellResponseStatus;
		alertTimeChosenLabel = alertCellData.alertTimeChosenLabel;
		alertCellObjectID = alertCellData.alertCellObjectID;
		parseObj = alertCellData.parseObj;

		currentType = alertCellData.currentType;
		isSentMessageSuccessfully = alertCellData.isSentMessageSuccessfully;

		alertCellTaskPriority = alertCellData.alertCellTaskPriority;
		cellIndex = alertCellData.cellIndex;
		isShowAcceptBtn = alertCellData.isShowAcceptBtn;

		alertEventPrimaryKey = alertCellData.alertEventPrimaryKey;
		alertItemUserStatus = alertCellData.alertItemUserStatus;
	}

	public AlertCellData(ATLAlertModel alertModel) {
		// TODO Auto-generated constructor stub
		alertCellId = alertModel.alertId;
		alertCellUuid = alertModel.alertUuid;
		alertCellAtlasId = alertModel.alertAtlasId;

		alertCellSenderId = alertModel.alertSenderId;
		alertCellSenderEmail = alertModel.alertSenderEmail;
		alertCellSenderName = alertModel.alertSenderName;

		alertCellRecieverId = alertModel.alertReceiverId;
		alertCellReceiverEmail = alertModel.alertReceiverEmail;
		alertCellRecieverName = alertModel.alertReceiverName;

		alertCellEventTitle = alertModel.alertEventTitle;
		alertCellEventLocation = alertModel.alertEventLocation;
		alertCellEventDuration = alertModel.alertEventDuration;
		alertCellDescription = alertModel.alertDescription;

		alertCellPreferredDatetime = alertModel.alertPreferredDate;
		alertCellAlt1DateTime = alertModel.alertAlt1Date;
		alertCellAlt2DateTime = alertModel.alertAlt2Date;

		alertCellEventPriority = alertModel.alertEventPriority;
		alertCellCreatedDate = alertModel.alertCreatedDate;
		alertCellModifiedDate = alertModel.alertModifiedDate;
		alertCellText = alertModel.alertText;
		alertCellAccessedDate = alertModel.alertAccessedDate;

		alertCellMsgId = alertModel.alertMsgId;
		alertCellMsgAction = alertModel.alertMsgAction;
		alertCellMsgCreatedDate = alertModel.alertMsgCreatedDate;

		isDisplayed = alertModel.isDisplayed;
		isRead = alertModel.isRead;
		isHandled = alertModel.isHandled;
		isAccepted = alertModel.isAccepted;

		alertCellAcceptedDate = alertModel.alertAcceptedDate;
		alertCellResponseStatus = alertModel.alertResponseStatus;

		isSentMessageSuccessfully = alertModel.isSentMessageSuccessfully;
		currentType = alertModel.currentType;
		alertCellTaskPriority = alertModel.alertCellTaskPriority;

		alertEventPrimaryKey = alertModel.alertEventPrimaryKey;
		alertItemUserStatus = alertModel.alertItemUserStatus;
	}
	
	public void save() {
		// TODO Auto-generated method stub
		ATLAlertModel alertModel = new ATLAlertModel(this);

		ATLAlertDatabaseAdapter dbHelper = new ATLAlertDatabaseAdapter();
		boolean isExit = dbHelper.isExistInDatabase(alertModel);
		if (isExit) {
			dbHelper.updateATLAlertModel(alertModel);
		} else {
			dbHelper.insertATLAlertModel(alertModel);
		}
	}

	public void saveWithEventPropertyAndItemUserProperty(
			ItemUserProperties itemUser, EventProperties event,
			String senderName, String receiverName) {
		this.deNull();
		// alertCellSenderId = sender.getAtlasId();
		// alertCellSenderEmail = sender.getEmailAddress();
		alertCellSenderName = senderName;

		// alertCellRecieverId = receiver.getAtlasId();
		// alertCellReceiverEmail = receiver.getEmailAddress();
		alertCellRecieverName = receiverName;

		alertCellEventTitle = event.title;
		alertCellEventLocation = event.location;
		alertCellEventDuration = event.duration;

		alertCellPreferredDatetime = event.startDateTime;
		alertCellAlt1DateTime = new Date();
		alertCellAlt2DateTime = new Date();

		alertCellEventPriority = "High";
		alertCellCreatedDate = new Date();
		alertCellModifiedDate = event.modifiedDatetime;
		alertCellText = "";
		alertCellAccessedDate = new Date();

		alertCellMsgId = "";
		alertCellMsgAction = "";
		alertCellMsgCreatedDate = new Date();

		isDisplayed = false;
		// isRead = alertModel.isRead;
		// isHandled = alertModel.isHandled;
		// isAccepted = alertModel.isAccepted;
		//
		alertCellAcceptedDate = new Date();
		// alertCellResponseStatus = alertModel.alertResponseStatus;

		if (itemUser.status == ITEM_USER_TASK_STATUS.SENT) {
			if (event.atlasId == ATLUser.getParseUserID()) {
				alertCellResponseStatus = AlertType.eventInvited_Sent;
				isHandled = true;
			} else {
				alertCellResponseStatus = AlertType.eventInvited_Received;
				isHandled = false;
			}
		} else if (itemUser.status == ITEM_USER_TASK_STATUS.COMPLITED
				|| itemUser.status == ITEM_USER_TASK_STATUS.ACCEPTED) {
			if (event.atlasId == ATLUser.getParseUserID()) {
				alertCellResponseStatus = AlertType.eventAccepted_Received;
				isHandled = true;
			} else {
				alertCellResponseStatus = AlertType.eventAccepted_Sent;
				isHandled = true;
			}
		} else if (itemUser.status == ITEM_USER_TASK_STATUS.DECLINED) {
			if (event.atlasId == ATLUser.getParseUserID()) {
				alertCellResponseStatus = AlertType.eventRejected_Received;
				isHandled = true;
			} else {
				alertCellResponseStatus = AlertType.eventRejected_Sent;
				isHandled = true;
			}
		}
		isSentMessageSuccessfully = true;
		currentType = alertCellResponseStatus;
		alertCellTaskPriority = 0;
		alertCellAtlasId = event.primaryWebEventId + "|" + itemUser.status;
		// alertEventPrimaryKey = alertModel.alertEventPrimaryKey;
		// alertItemUserStatus = alertModel.alertItemUserStatus;
		// TODO Auto-generated method stub
		ATLAlertModel alertModel = new ATLAlertModel(this);

		ATLAlertDatabaseAdapter dbHelper = new ATLAlertDatabaseAdapter();
		boolean isExit = dbHelper.isExistInDatabase(alertModel);
		if (isExit) {
			dbHelper.updateATLAlertModel(alertModel);
		} else {
			dbHelper.insertATLAlertModel(alertModel);
		}
	}

	public void saveWithEventPropertyAndItemUserProperty(
			ITEM_USER_TASK_STATUS status, int chosenOrder, Date chosenDate,
			Date prefDate, Date alt1Date, Date alt2Date, EventProperties event,
			String senderName, String receiverName, boolean isHandle) {
		this.deNull();
		// alertCellSenderId = sender.getAtlasId();
		// alertCellSenderEmail = sender.getEmailAddress();
		alertCellSenderName = senderName;

		// alertCellRecieverId = receiver.getAtlasId();
		// alertCellReceiverEmail = receiver.getEmailAddress();
		alertCellRecieverName = receiverName;

		alertCellEventTitle = event.title;
		alertCellEventLocation = event.location;
		alertCellEventDuration = event.duration;

		alertCellPreferredDatetime = prefDate;
		if (alertCellPreferredDatetime == null) {
			alertCellPreferredDatetime = new Date();
		}
		alertCellAlt1DateTime = alt1Date;
		if (alertCellAlt1DateTime == null) {
			alertCellAlt1DateTime = new Date();
		}
		alertCellAlt2DateTime = alt2Date;
		if (alertCellAlt2DateTime == null) {
			alertCellAlt2DateTime = new Date();
		}

		alertCellEventPriority = "High";
		alertCellCreatedDate = new Date();
		alertCellModifiedDate = event.modifiedDatetime;
		alertCellText = "";
		alertCellAccessedDate = new Date();

		alertCellMsgId = "";
		alertCellMsgAction = "";
		alertCellMsgCreatedDate = new Date();

		isDisplayed = false;
		// isRead = alertModel.isRead;
		// isHandled = alertModel.isHandled;
		// isAccepted = alertModel.isAccepted;
		//
		alertCellAcceptedDate = new Date();
		// alertCellResponseStatus = alertModel.alertResponseStatus;

		if (status == ITEM_USER_TASK_STATUS.SENT) {
			if (event.atlasId.equals(ATLUser.getParseUserID())) {
				alertCellResponseStatus = AlertType.eventInvited_Sent;
				isHandled = isHandle;
			} else {
				alertCellResponseStatus = AlertType.eventInvited_Received;
				isHandled = isHandle;
			}
		} else if (status == ITEM_USER_TASK_STATUS.COMPLITED
				|| status == ITEM_USER_TASK_STATUS.ACCEPTED) {
			if (event.atlasId.equals(ATLUser.getParseUserID())) {
				alertCellResponseStatus = AlertType.eventAccepted_Received;
				switch (chosenOrder) {
				case 0:
					if (prefDate != null) {
						CalendarUtilities.acceptEventByDate(prefDate,
								Utilities.ctx);
						alertCellAcceptedDate = prefDate;
					}
					break;
				case 1:

					if (alt1Date != null) {
						CalendarUtilities.acceptEventByDate(alt1Date,
								Utilities.ctx);
						alertCellAcceptedDate = alt1Date;
					}
					break;
				case 2:
					if (alt2Date != null) {
						CalendarUtilities.acceptEventByDate(alt2Date,
								Utilities.ctx);
						alertCellAcceptedDate = alt2Date;
					}
					break;
				default:
					break;
				}
				isHandled = isHandle;
			} else {
				alertCellResponseStatus = AlertType.eventAccepted_Sent;
				isHandled = isHandle;
			}
		} else if (status == ITEM_USER_TASK_STATUS.DECLINED) {
			if (event.atlasId.equals(ATLUser.getParseUserID())) {
				alertCellResponseStatus = AlertType.eventRejected_Received;
				isHandled = isHandle;
			} else {
				alertCellResponseStatus = AlertType.eventRejected_Sent;
				isHandled = isHandle;
			}
		}
		isSentMessageSuccessfully = true;
		currentType = alertCellResponseStatus;
		alertCellTaskPriority = 0;
		alertCellAtlasId = event.primaryWebEventId + "|" + status;
		// alertEventPrimaryKey = alertModel.alertEventPrimaryKey;
		// alertItemUserStatus = alertModel.alertItemUserStatus;
		// TODO Auto-generated method stub
		ATLAlertModel alertModel = new ATLAlertModel(this);

		ATLAlertDatabaseAdapter dbHelper = new ATLAlertDatabaseAdapter();
		boolean isExit = dbHelper.isExistInDatabase(alertModel);
		if (isExit) {
			dbHelper.updateATLAlertModel(alertModel);
		} else {
			dbHelper.insertATLAlertModel(alertModel);
		}

	}

	public void deNull() {
		alertCellUuid = Utilities.deNull(alertCellUuid);
		alertCellAtlasId = Utilities.deNull(alertCellAtlasId);

		alertCellSenderId = Utilities.deNull(alertCellSenderId);
		alertCellSenderEmail = Utilities.deNull(alertCellSenderEmail);
		alertCellSenderName = Utilities.deNull(alertCellSenderName);

		alertCellRecieverId = Utilities.deNull(alertCellRecieverId);
		alertCellReceiverEmail = Utilities.deNull(alertCellReceiverEmail);
		alertCellRecieverName = Utilities.deNull(alertCellRecieverName);

		alertCellEventTitle = Utilities.deNull(alertCellEventTitle);
		alertCellEventLocation = Utilities.deNull(alertCellEventLocation);
		alertCellDescription = Utilities.deNull(alertCellDescription);
		alertCellEventPriority = Utilities.deNull(alertCellEventPriority);
		alertCellText = Utilities.deNull(alertCellText);

		alertCellMsgId = Utilities.deNull(alertCellMsgId);
		alertCellMsgAction = Utilities.deNull(alertCellMsgId);
		alertCellObjectID = Utilities.deNull(alertCellObjectID);

		alertEventPrimaryKey = Utilities.deNull(alertEventPrimaryKey);

	}

	public String getEventPrimaryKey(){
		String key = "";
		if(this.alertCellAtlasId != null 
				&& !this.alertCellAtlasId.equals("")){
			String []temp = this.alertCellAtlasId.split("\\|");
			if(temp.length > 0){
				key = this.alertCellAtlasId.split("\\|")[0];
			}
		}
		return key;
	}

}
