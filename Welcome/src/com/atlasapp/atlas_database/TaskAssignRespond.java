package com.atlasapp.atlas_database;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.atlasapp.atlas_database.DatabaseConstants.SERVER_MESSAGE;
import com.atlasapp.atlas_database.DatabaseConstants.SIGN_NEW_USERS_CALLER;
import com.atlasapp.common.AtlasAndroidUser;
import com.atlasapp.model.ATLContactModel;
import com.parse.ParseObject;

public class TaskAssignRespond extends AtlasServerTable implements UserDelagateInterface{
	private String eventTitle,content,toAtlasId,toEmailAdress,toName;
	private Date date;
	private String prefDateUTC;
	
	private String taskAssignId;
	
	private boolean accept ;
	private final String TASK_ASSIGN_RESPOND_TABLE = "TaskAcceptNew";
	public TaskAssignRespond()
	{
		eventTitle = "";
		content = "";
		toAtlasId = "";
		toEmailAdress = "";
		toName = "";
		date = new Date();
		prefDateUTC = "";
		taskAssignId="";
		accept = false;
		this.TABLE_NAME = TASK_ASSIGN_RESPOND_TABLE;
		atlasServerTable = new ParseObject(TASK_ASSIGN_RESPOND_TABLE);
	}
	public void respondFriend(String taskId,boolean acceptTask,String toId,String taskTitle,String taskContent,
			String toEmail,Date datePref, String tofirstname,
			  String prefdateFormateInUTC) {
		accept = acceptTask;
		toAtlasId = (toId!=null)?toId:toAtlasId;
		eventTitle = (taskTitle!=null)?taskTitle:eventTitle;
		content = (taskContent!=null)?taskContent:content;
		toEmailAdress = (toEmail!=null)?toEmail:toEmailAdress ;
		date = (datePref!=null)?datePref:date;
		toName = (tofirstname!=null)?tofirstname: toName;
		prefDateUTC = (prefdateFormateInUTC!=null)?prefdateFormateInUTC: prefDateUTC;
		taskAssignId = (taskId!=null)? taskId :taskAssignId;
		taskAssignRespond();
		
		
		 
	}
	private void taskAssignRespond() {
		updateTableMessage = SERVER_MESSAGE.FAIL;
		HashMap<String, Object> calendarInviteColumns = new HashMap<String, Object>();
		// Mesaage format example:
		//1,1,2013:26:01 02:00:50,test o tests
//		 [prefOptionPicked]+","+
//		   [prefDatePicked]+","+
//		  [INVITE_MESSAGE.Title]+","+
//		  [INVITE_MESSAGE.Location];
		String message = prefDateUTC+","+eventTitle+","+content;
		
		calendarInviteColumns.put("fromID", AtlasAndroidUser.getParseUserID());
		calendarInviteColumns.put("inviteeName", AtlasAndroidUser.getUserNameDisplay());
		calendarInviteColumns.put("invitee", AtlasAndroidUser.getParseUserID());

		calendarInviteColumns.put("task_title", eventTitle);
		calendarInviteColumns.put("content", content);
		calendarInviteColumns.put("inviter", toAtlasId);
		calendarInviteColumns.put("inviterEmail",toEmailAdress);
		
		calendarInviteColumns.put("isRead", false);
		calendarInviteColumns.put("date", date);
		calendarInviteColumns.put("accept", accept);
		calendarInviteColumns.put("taskID", taskAssignId);

		calendarInviteColumns.put("message", message);
		calendarInviteColumns.put("isRead", false);
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
