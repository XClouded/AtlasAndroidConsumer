//  ==================================================================================================================
//  ATLAlertModel.java
//  ATLAS
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2013-01-10 NGHIA:   Add more parameter to math with iphone database
//  ==================================================================================================================
package atlasapp.model;

import java.util.Date;

import atlasapp.section_alerts.ATLAlertDatabaseAdapter;
import atlasapp.section_alerts.AlertCellData;


/**
 * @author NGHIA
 * 
 */
public class ATLAlertModel {

	// INTERNAL PROPERTIES
	public int alertId = 0;
	public String alertUuid = "";
	public String alertAtlasId = "";

	public String alertSenderId = "";
	public String alertSenderEmail = "";
	public String alertSenderName = "";

	public String alertReceiverId = "";
	public String alertReceiverEmail = "";
	public String alertReceiverName = "";

	public String alertEventTitle = "";
	public String alertEventLocation = "";
	public int alertEventDuration = 0;
	public String alertDescription = "";

	public Date alertPreferredDate = null;
	public Date alertAlt1Date = null;
	public Date alertAlt2Date = null;

	public String alertEventPriority = "";
	public String alertText = "";
	public Date alertAccessedDate = null;

	public Date alertCreatedDate = null;
	public Date alertModifiedDate = null;
	
	public String alertMsgId = "";
	public String alertMsgAction = "";
	public Date alertMsgCreatedDate = null;

	public boolean isDisplayed = false;
	public boolean isRead = false;
	public boolean isHandled = false;
	public boolean isAccepted = false;

	public Date alertAcceptedDate = null;
	public int alertResponseStatus = 0;
	public int currentType = 0;
	public boolean isSentMessageSuccessfully = false;
	public int alertCellTaskPriority = 1;

	public String alertEventPrimaryKey = "";
	public int alertItemUserStatus = 0;
	
	public ATLAlertModel() {
		// TODO Auto-generated constructor stub
	}

	public ATLAlertModel(AlertCellData alertCellData) {
		alertCellData.deNull();
		alertId = alertCellData.alertCellId;
		alertUuid = alertCellData.alertCellUuid;
		alertAtlasId = alertCellData.alertCellAtlasId;

		alertSenderId = alertCellData.alertCellSenderId;
		alertSenderEmail = alertCellData.alertCellSenderEmail;
		alertSenderName = alertCellData.alertCellSenderName;

		alertReceiverId = alertCellData.alertCellRecieverId;
		alertReceiverEmail = alertCellData.alertCellReceiverEmail;
		alertReceiverName = alertCellData.alertCellRecieverName;

		alertEventTitle = alertCellData.alertCellEventTitle;
		alertEventLocation = alertCellData.alertCellEventLocation;
		alertEventDuration = alertCellData.alertCellEventDuration;
		alertDescription = alertCellData.alertCellDescription;

		alertPreferredDate = alertCellData.alertCellPreferredDatetime;
		alertAlt1Date = alertCellData.alertCellAlt1DateTime;
		alertAlt2Date = alertCellData.alertCellAlt2DateTime;

		alertEventPriority = alertCellData.alertCellEventPriority;
		alertCreatedDate = alertCellData.alertCellCreatedDate;
		alertModifiedDate = alertCellData.alertCellModifiedDate;
		alertText = alertCellData.alertCellText;
		alertAccessedDate = alertCellData.alertCellAccessedDate;

		alertMsgId = alertCellData.alertCellMsgId;
		alertMsgAction = alertCellData.alertCellMsgAction;
		alertMsgCreatedDate = alertCellData.alertCellMsgCreatedDate;

		isDisplayed = alertCellData.isDisplayed;
		isRead = alertCellData.isRead;
		isHandled = alertCellData.isHandled;
		isAccepted = alertCellData.isAccepted;

		alertAcceptedDate = alertCellData.alertCellAcceptedDate;
		alertResponseStatus = alertCellData.alertCellResponseStatus;
		
		currentType = alertCellData.currentType;
		isSentMessageSuccessfully = alertCellData.isSentMessageSuccessfully;
		alertCellTaskPriority = alertCellData.alertCellTaskPriority;

		alertEventPrimaryKey = alertCellData.alertEventPrimaryKey;
		alertItemUserStatus = alertCellData.alertItemUserStatus;
	}
	
	public void save() {
		// TODO Auto-generated method stub

		ATLAlertDatabaseAdapter dbHelper = new ATLAlertDatabaseAdapter();
		boolean isExit = dbHelper.isExistInDatabase(this);
		if (isExit) {
			dbHelper.updateATLAlertModel(this);
		} else {
			dbHelper.insertATLAlertModel(this);
		}
	}
}
