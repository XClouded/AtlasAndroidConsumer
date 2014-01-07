package com.atlasapp.atlas_database;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.atlasapp.atlas_database.DatabaseConstants.SERVER_MESSAGE;
import com.atlasapp.atlas_database.DatabaseConstants.SIGN_NEW_USERS_CALLER;
import com.atlasapp.common.AtlasAndroidUser;
import com.atlasapp.model.ATLContactModel;
import com.parse.ParseObject;

public class CalendarRespond extends AtlasServerTable implements UserDelagateInterface{
	
	
	private String eventTitle,eventLocation,toAtlasId,toEmailAdress,toName;
	private int timeLabel;
	private int eventDuration;
	private Date prefDate;
	private String prefDateUTC;
	
	private final String CALENDAR_INVITE_TABLE_NAME = "EventAcceptNew";
	private String inviteId;
	public CalendarRespond()
	{
		eventTitle = "";
		eventLocation = "";
		eventDuration = 0;
		toAtlasId = "";
		toEmailAdress = "";
		toName = "";
		prefDate = new Date();
		prefDateUTC = "";
		timeLabel = 1;
		inviteId = "";
		atlasServerTable = new ParseObject(CALENDAR_INVITE_TABLE_NAME);
		this.TABLE_NAME = CALENDAR_INVITE_TABLE_NAME;
	}
	public void respondFriend(String inviteEventId,String optionPicked,String toId,String title,String location,
			String toEmail, Date datePref, String tofirstname,int duration,
			  String prefdateFormateInUTC) {
		toAtlasId = (toId!=null)?toId:toAtlasId;
		eventTitle = (title!=null)?title:eventTitle;
		eventLocation = (location!=null)?location:eventLocation;
		toEmailAdress = (toEmail!=null)?toEmail:toEmailAdress ;
		eventDuration = ( duration>=0)?duration:eventDuration ;
		prefDate = (datePref!=null)?datePref:prefDate;
		toName = (tofirstname!=null)?tofirstname: toName;
		timeLabel = (optionPicked!=null && !optionPicked.equals(""))? 
				(optionPicked.equals("1"))? 1:
					(optionPicked.equals("2"))? 2:	
						(optionPicked.equals("3"))? 3:1:1;
		prefDateUTC = (prefdateFormateInUTC!=null)?prefdateFormateInUTC: prefDateUTC;
		inviteId = (inviteEventId!=null)? inviteEventId:inviteId;
		calendarInvite();
		
		
		 
	}
	private void calendarInvite() {
		updateTableMessage = SERVER_MESSAGE.FAIL;
		HashMap<String, Object> calendarInviteColumns = new HashMap<String, Object>();
		// Mesaage format example:
		//1,1,2013:26:01 02:00:50,test o tests
//		 [prefOptionPicked]+","+
//		   [prefDatePicked]+","+
//		  [INVITE_MESSAGE.Title]+","+
//		  [INVITE_MESSAGE.Location];
		String message = timeLabel+","+prefDateUTC+","+eventTitle+","+eventLocation;
		
		calendarInviteColumns.put("invitee", AtlasAndroidUser.getParseUserID());
		calendarInviteColumns.put("inviteeName", AtlasAndroidUser.getUserNameDisplay());
		calendarInviteColumns.put("title", eventTitle);
		calendarInviteColumns.put("event_title", eventTitle);
		calendarInviteColumns.put("location", eventLocation);
		calendarInviteColumns.put("event_location", eventLocation);
		calendarInviteColumns.put("duration", eventDuration);  
		calendarInviteColumns.put("event_duration", eventDuration);  
		calendarInviteColumns.put("inviter", toAtlasId);
		calendarInviteColumns.put("inviterName",toName);
		calendarInviteColumns.put("isRead", false);
		calendarInviteColumns.put("time_choosen", prefDateUTC);
		calendarInviteColumns.put("respond", false);
		
		calendarInviteColumns.put("time_label", timeLabel);
		calendarInviteColumns.put("message", message);
		calendarInviteColumns.put("inviteID", inviteId);
	//	calendarInviteColumns.put("inviteID", inviteID);
//		calendarInviteColumns.put("fromID", AtlasAndroidUser.getParseUserID());
//		
//		calendarInviteColumns.put("toEmail", toEmailAdress);
//		
//		  
//		
//		
//		
//		
//		calendarInviteColumns.put("toName", toName);
	//	
		
		put(calendarInviteColumns,this);
	//	putInBackground(calendarInviteColumns);
	}
	@Override
	public void getFriendEmailOnParse(
			ATLContactModel friendsPropertiesOnParseByEmail, boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void registerSuccess(boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void signInSuccess(boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getUserEmailOnParseCallBack(
			HashMap<String, String> loginProperties, boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getFacebookFriendIdOnParse(ATLContactModel facebookAtlasFriend,
			boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getAllFBAtlasUsersCallBack(
			ArrayList<ATLContactModel> allFacebookAtlasUsers, boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getAllFBAtlasUsersFriendsCallBack(
			ArrayList<ATLContactModel> allFacebookAtlasUsers, boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void saveCallBack(boolean saved) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getAtlasNewFriendsByEmailCallBack(
			ArrayList<ATLContactModel> allCommonAtlasUsers) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getUpateCallBack(boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getSignNonAtlasUsersCallBack(SIGN_NEW_USERS_CALLER caller,
			boolean success,
			HashMap<String, ATLContactModel> newCurrentNonAtlasUserToSign) {
		// TODO Auto-generated method stub
		
	}
	
	

}
