//  ==================================================================================================================
//  DBDefines.java
//  ATLAS
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-11-11 TAN:     Init class
//  ==================================================================================================================

package com.atlasapp.common;

public class DBDefines {

	
	//implementation DBDefines
	public final static String ALT_APPOINTMENTS = "ALT_APPOINTMENTS";
	public final static String ATL_ALERTS = "ATL_ALERTS";
	public final static String ATL_ATTENDEE = "ATL_ATTENDEE";
	public final static String ATL_TASK = "ATL_TASK";
	public final static String ATL_NOTE = "ATL_NOTE";
	public final static String ATL_FRIEND = "FRIEND";
	//end

	//implementation TableAtlFriend
	public final static String FRIEND_ID = "FRIEND_ID";	//Primary Key
	public final static String FRIEND_FIRSTNAME = "FIRSTNAME";
	public final static String FRIEND_LASTNAME = "LASTNAME";
	public final static String FRIEND_PHONE_NUMBER = "PHONE_NUMBER";
	public final static String FRIEND_EMAIL_ADDRESS = "EMAIL_ADDRESS";
	public final static String FRIEND_PIC_PATH = "PIC_PATH";
	public final static String FRIEND_FROM_FACEBOOK = "FROM_FACEBOOK";
	public final static String FRIEND_FROM_TWITTER = "FROM_TWITTER";
	public final static String FRIEND_FROM_LINKEDIN = "FROM_LINKEDIN";
	public final static String FRIEND_FROM_ADDRESS_BOOK = "FROM_ADDRESS_BOOK";
	//end


	//implementation TableAltAppointments
	public final static String GROUP_EVENT_ID = "GROUP_EVENT_ID";	//Primary Key
	public final static String PREFERRED_EVENT_ID = "PREFERRED_EVENT_ID";
	public final static String ALT2_EVENT_ID = "ALT2_EVENT_ID";
	public final static String ALT3_EVENT_ID = "ALT3_EVENT_ID";
	public final static String EVENT_RESPONSE_STATUS = "EVENT_RESPONSE_STATUS";
	//end


	//implementation TableAtlalerts
	public final static String ALERT_ID = "ALERT_ID";	//Primary Key
	public final static String ALERT_UUID = "ALERT_UUID";
	public final static String ALERT_ATLAS_ID = "ALERT_ATLAS_ID";
	public final static String ALERT_SENDERNAME = "ALERT_SENDERNAME";
	public final static String ALERT_SENDERID = "ALERT_SENDERID";
	public final static String ALERT_RECEIVERNAME = "ALERT_RECEIVERNAME";
	public final static String ALERT_RECEIVERID = "ALERT_RECEIVERID";
	public final static String ALERT_EVENTTITLE = "ALERT_EVENTTITLE";
	public final static String ALERT_EVENTLOCATION = "ALERT_EVENTLOCATION";
	public final static String ALERT_EVENTDURATION = "ALERT_EVENTDURATION";
	public final static String ALERT_DESCRIPTION = "ALERT_DESCRIPTION";
	public final static String ALERT_PREFERRED_DATETIME = "ALERT_PREFERRED_DATETIME";
	public final static String ALERT_ALT1_DATETIME = "ALERT_ALT1_DATETIME";
	public final static String ALERT_ALT2_DATETIME = "ALERT_ALT2_DATETIME";
	public final static String ALERT_EVENTPRIORITY = "ALERT_EVENTPRIORITY";
	public final static String ALERT_CREATED_DATETIME = "ALERT_CREATED_DATETIME";
	public final static String ALERT_TEXT = "ALERT_TEXT";
	public final static String ALERT_ACCESSED_DATETIME = "ALERT_ACCESSED_DATETIME";
	public final static String ALERT_MSGID = "ALERT_MSGID";
	public final static String ALERT_MSGACTION = "ALERT_MSGACTION";
	public final static String ALERT_MSGCREATED_DATETIME = "ALERT_MSGCREATED_DATETIME";
	public final static String ALERT_DISPLAYED = "ALERT_DISPLAYED";
	public final static String ALERT_READ = "ALERT_READ";
	public final static String ALERT_HANDLED = "ALERT_HANDLED";
	public final static String ALERT_ACCEPTED = "ALERT_ACCEPTED";
	public final static String ALERT_ACCEPTED_DATETIME = "ALERT_ACCEPTED_DATETIME";
	public final static String ALERT_RESPONSESTATUS = "ALERT_RESPONSESTATUS";

	//end

	//implementation TableAtlAttendee

	public final static String ATTENDEE_ID = "ATTENDEE_ID";	//Primary Key
	public final static String APPOINTMENT_ID = "APPOINTMENT_ID";
	public final static String ATLAS_ID = "ATLAS_ID";
	public final static String ATTENDEE_NAME = "ATTENDEE_NAME";
	public final static String ATTENDEE_EMAIL = "ATTENDEE_EMAIL";
	public final static String ATTENDEE_IMAGE = "ATTENDEE_IMAGE";
	public final static String ATTENDEE_PHONENUMBER = "ATTENDEE_PHONENUMBER";

	//end

	//end
	//implementation TableAtlNotes

	public final static String NOTE_ID = "NOTE_ID";	//Primary Key
	public final static String NOTE_UUID = "NOTE_UUID";
	public final static String NOTE_ATLAS_ID = "NOTE_ATLAS_ID";
	public final static String NOTE_TITLE = "NOTE_TITLE";
	public final static String NOTE_BODY = "NOTE_BODY";
	public final static String NOTE_CALENDAR_NAME = "NOTE_CALENDAR_NAME";
	public final static String NOTE_CALENDAR_COLOR = "NOTE_CALENDAR_COLOR";
	public final static String NOTE_AUTHOR_ID = "NOTE_AUTHOR_ID";
	public final static String NOTE_AUTHOR_NAME = "NOTE_AUTHOR_NAME";
	public final static String NOTE_IS_SELECTED_STAR = "NOTE_IS_SELECTED_STAR";
	public final static String NOTE_DATE_CREATED = "NOTE_DATE_CREATED";
	public final static String NOTE_MODIFIED_DATE = "NOTE_MODIFIED_DATE";

	//end
	

}
