package com.atlasapp.atlas_database;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.atlasapp.atlas_database.DatabaseConstants.SERVER_MESSAGE;
import com.atlasapp.atlas_database.DatabaseConstants.SIGN_NEW_USERS_CALLER;
import com.atlasapp.common.AtlasAndroidUser;
import com.atlasapp.model.ATLContactModel;
import com.parse.ParseObject;

/**
 * 
 * @author sharonnachum
 *INVITE_MESSAGE FORMAT= {
	         Title : 0,
	         Location: 1,
	         Duration :2,
	         PREF :3,
	         PreferredDate :4,
	         ALT1:5 ,
	         AlternativeDate1 :6,
	         ALT2: 7,
	         AlternativeDate2:8,
	         fromDisplayName:9,
	         fromAtlasID:10,
	         toDisplayName:11
	         
	}
	message from Parse: Hello evrnt,
						Optionl text,
						255,
						PREF,
						2013:04:01 23:00:00,
						ALT1,
						2013:05:01 05:00:00,
						ALT2,
						2013:05:01 06:00:00,
						Hunter Gray,
						VonTlk5hUp,
						HUNTER GRAY
 */
public class CalendarInvite extends AtlasServerTable implements UserDelagateInterface{
	
	
	private String eventTitle,eventLocation,toAtlasId,toEmailAdress,toName;
	private int eventDuration;
	private Date prefDate,altOneDate,altTwoDate;
	private String prefDateUTC, altOneDateUTC, altTwoDateUTC;
	private String calendarRespondId = "";
	private final String CALENDAR_INVITE_TABLE_NAME = "EventInviteNew";
	public CalendarInvite()
	{
		eventTitle = "";
		eventLocation = "";
		eventDuration = 0;
		toAtlasId = "";
		toEmailAdress = "";
		toName = "";
		prefDate = new Date();
		altOneDate = new Date();
		altTwoDate = new Date();
		
		prefDateUTC = "";
		altOneDateUTC = "";
		altTwoDateUTC = "";
		this.TABLE_NAME = CALENDAR_INVITE_TABLE_NAME;

		atlasServerTable = new ParseObject(CALENDAR_INVITE_TABLE_NAME);
	}
	public void inviteFriend(String fromName, String atlasId, String emailAddress, String title, String location,
			  Date datePref,Date dateAlt1,Date dateAlt2, String firstname,int duration,
			  String prefdateFormateInUTC,String altOnefdateFormateInUTC,String altTwofdateFormateInUTC) {
		toAtlasId = (atlasId!=null)?atlasId:toAtlasId;
		eventTitle = (title!=null)?title:eventTitle;
		eventLocation = (location!=null)?location:eventLocation;
		toEmailAdress = (emailAddress!=null)?emailAddress:toEmailAdress ;
		eventDuration = ( duration>=0)?duration:eventDuration ;
		prefDate = (datePref!=null)?datePref:prefDate;
		altOneDate = (dateAlt1!=null)?dateAlt1:altOneDate;
		altTwoDate = (dateAlt2!=null)?dateAlt2:altTwoDate;
		
		toName = (firstname!=null)?firstname: toName;
		
		prefDateUTC = (prefdateFormateInUTC!=null)?prefdateFormateInUTC: prefDateUTC;
		altOneDateUTC = (altOnefdateFormateInUTC!=null)?altOnefdateFormateInUTC: altOneDateUTC;
		altTwoDateUTC = (altTwofdateFormateInUTC!=null)?altTwofdateFormateInUTC: altTwoDateUTC;
		
		calendarInvite();
		
		
		 
	}
	private void calendarInvite() {
		updateTableMessage = SERVER_MESSAGE.FAIL;
		HashMap<String, Object> calendarInviteColumns = new HashMap<String, Object>();
		// Mesaage format example:
		//Hello evrnt,Optionl text,255,PREF,2013:04:01 23:00:00,ALT1,2013:05:01 05:00:00,ALT2,2013:05:01 06:00:00,Hunter Gray,VonTlk5hUp,HUNTER GRAY
		String message = eventTitle+","+eventLocation+","+eventDuration+",PREF,"+prefDateUTC+","+",ALT1,"+altOneDateUTC+","+",ALT2,"+altTwoDateUTC+","+ AtlasAndroidUser.getUserNameDisplay()+","+AtlasAndroidUser.getParseUserID()+","+toName;
		
		
		calendarInviteColumns.put("invitee", toAtlasId);
		calendarInviteColumns.put("fromID", AtlasAndroidUser.getParseUserID());
		calendarInviteColumns.put("respond", false);
		calendarInviteColumns.put("toEmail", toEmailAdress);
		calendarInviteColumns.put("title", eventTitle);
		calendarInviteColumns.put("event_title", eventTitle);
		calendarInviteColumns.put("location", eventLocation);
		calendarInviteColumns.put("event_location", eventLocation);
		calendarInviteColumns.put("duration", eventDuration);  
		calendarInviteColumns.put("event_duration", eventDuration);  
		calendarInviteColumns.put("pref_date", prefDate);
		calendarInviteColumns.put("alt_one_date", altOneDate);
		calendarInviteColumns.put("alt_two_date", altTwoDate);
		calendarInviteColumns.put("fromName", AtlasAndroidUser.getUserNameDisplay());
		calendarInviteColumns.put("toName", toName);
		calendarInviteColumns.put("message", message);
		calendarInviteColumns.put("inviter", AtlasAndroidUser.getParseUserID());
		calendarInviteColumns.put("isRead", false);
		put(calendarInviteColumns,this);
		
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
