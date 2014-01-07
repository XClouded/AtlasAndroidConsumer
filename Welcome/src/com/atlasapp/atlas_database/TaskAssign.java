package com.atlasapp.atlas_database;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.content.Intent;
import android.text.Html;


import com.atlasapp.atlas_database.DatabaseConstants.SERVER_MESSAGE;
import com.atlasapp.atlas_database.DatabaseConstants.SIGN_NEW_USERS_CALLER;
import com.atlasapp.common.ATLConstants.TASK_CATEGORY;
import com.atlasapp.common.AtlasAndroidUser;
import com.atlasapp.model.ATLContactModel;
import com.atlasapp.section_settings.InviteContact;
import com.atlasapp.section_today.Today;
import com.parse.ParseObject;
/**
 * 
 * @author sharonnachum
 *TASK_MESSAGE = {
//	         Title : 0,
//	         Content: 1,
//	         Catagory :2,
//	         Deadline:3,
//	         Date :4,
//	         To:5
 */
public class TaskAssign extends AtlasServerTable implements UserDelagateInterface{
	
	
	private String toAtlasId, taskTitle, taskContent, toEmailAddress,toName;
	TASK_CATEGORY taskCategory;
	private Date taskDate;
	private String dateUtc;
	private final String  TASK_ASSIGN_TABLE_NAME = "TaskAssignNew";
	public TaskAssign ()
	{
		toAtlasId = "";
		taskTitle = "";
		taskContent="";
		taskCategory = TASK_CATEGORY.Low;
		toEmailAddress = "";
		taskDate = new Date();
		dateUtc = "";
		this.TABLE_NAME = TASK_ASSIGN_TABLE_NAME;

		atlasServerTable = new ParseObject(TASK_ASSIGN_TABLE_NAME);
	}
	   
	/**
	 *    
	 */
	private void taskAssign()
	{
		updateTableMessage = SERVER_MESSAGE.FAIL;
		HashMap<String, Object> taskColumns = new HashMap<String, Object>();
		
		String category = (taskCategory.equals(TASK_CATEGORY.Low))?"Low":
						  (taskCategory.equals(TASK_CATEGORY.Medium))?"Medium":"High";
		String message = taskTitle+","+taskContent+","+category+",DEADLINE,"+dateUtc+","+ AtlasAndroidUser.getUserNameDisplay()+","+toEmailAddress;
		taskColumns.put("toID", toAtlasId);
		taskColumns.put("toEmail", toEmailAddress);
		taskColumns.put("title", taskTitle);
		taskColumns.put("content", taskContent);
		taskColumns.put("category", category);  
		taskColumns.put("date", taskDate);
		taskColumns.put("toName", toName);
		taskColumns.put("message", message);
		taskColumns.put("fromID", AtlasAndroidUser.getParseUserID());
		taskColumns.put("fromEmail", AtlasAndroidUser.getEmail());
		taskColumns.put("fromName", AtlasAndroidUser.getUserNameDisplay());
		taskColumns.put("isRead", false);
		put(taskColumns,this);
//		if (toAtlasId!=null && !toAtlasId.equals(""))
//			assignAtlasUser();
//		else
//			assignNonAtlasUser();
	}
	/**
	 * 
	 * @param atlasId
	 * @param emailAddress
	 * @param title
	 * @param content
	 * @param category
	 * @param date
	 * @param firstname
	 * @param lv_dateFormateInUTC 
	 */
	public void assignFriend(String atlasId, String emailAddress, String title,
			String content, TASK_CATEGORY category, Date date, String firstname, String lv_dateFormateInUTC) {
		toAtlasId =(atlasId!=null)?atlasId:toAtlasId;
		taskTitle = (title!=null)?title:taskTitle;
		taskContent=(content!=null)?content:taskContent;
		taskCategory = (category!=null)?category:taskCategory;
		toEmailAddress = (emailAddress!=null)?emailAddress:toEmailAddress;
		taskDate = (date!=null)?date:taskDate ;
		toName =(firstname!=null)?firstname:toName ;
		dateUtc =(lv_dateFormateInUTC!=null)?lv_dateFormateInUTC:dateUtc ;
		
		taskAssign();
		
		
		
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
