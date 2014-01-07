package com.atlasapp.atlas_database;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.os.AsyncTask;

import com.atlasapp.atlas_database.DatabaseConstants.ACTION;
import com.atlasapp.atlas_database.DatabaseConstants.ITEM_TYPE;
import com.atlasapp.atlas_database.DatabaseConstants.ITEM_TYPE_PRIORITY_ORDER;
import com.atlasapp.atlas_database.DatabaseConstants.ITEM_USER_TASK_STATUS;
import com.atlasapp.atlas_database.DatabaseConstants.TABLES_NAME;
import com.atlasapp.common.AtlasAndroidUser;
import com.atlasapp.common.CurrentSessionFriendsList;
import com.atlasapp.common.DB;
import com.atlasapp.model.ATLContactModel;
import com.parse.ParseObject;

public class ItemUserProperties {
	
	public  ITEM_TYPE itemType = ITEM_TYPE.TASK ;
	/// the Id of the event/task associated 
	/// with this item_user 
	/// as in the local DB 
	public int itemId = -1;
	// task.objectId or event.objectId 
	public String webItemId="";
	//the user associated with the event/task (invitee)
	public  String atlasId = "" ;
	public Date statusDateTime= new Date();
	public boolean wasReceived;
	public Date receivedDateTime = new Date();
	public ITEM_USER_TASK_STATUS status;
	public ACTION action = 	ACTION.SAVE;
	
	public ITEM_TYPE_PRIORITY_ORDER priorityOrder;
	// order to display invitees
	public int displayOrder = -1;
	
	
	public String itemUserAlertTitle = "";
	public  ATLContactModel contact;
	
	// the event row associated with this item_user
	/// (the one web_item_id points on the evnets 
	/// item list (objectId))
	private Event event;
	// id given by the Parse DB = web_item_user_id as 
	/// in 'item_user' table
	public String objectId="";
	/// the primary int key given by the local DB 'item_user'
		/// table to recognize this record
	public int itemUserId = -1;
	public Date modifiedDatetime = new Date();
	
	public  String primaryWebEventId = "";
	public String webItemUserId="";
	
	public ItemUserProperties()
	{
		primaryWebEventId = "";
		itemType = ITEM_TYPE.TASK;//default
		webItemId = "";
		atlasId = "";
		statusDateTime  = new Date();
		wasReceived = false;
		priorityOrder = ITEM_TYPE_PRIORITY_ORDER.IDEAL;
		status = ITEM_USER_TASK_STATUS.SENT;
		action = ACTION.SAVE;
		displayOrder = 0;
		receivedDateTime = new Date();
		objectId = "";

	} 
	public ATLContactModel getItemUserContact()
	{
		if (contact==null)
		{
			ArrayList<HashMap<String, String>> hA = DB.q("SELECT * FROM ATL_FRIEND where atlas_id='"+atlasId+"'");
			ATLContactModel friend;
			if (hA!=null && hA.size()>0)
			{
		//	cuurentSessionFriends.setCurrentFriendsList(new ArrayList<ATLContactModel>(hA.size()));
			ArrayList<ATLContactModel> friends = new ArrayList<ATLContactModel>();
			for(HashMap<String, String> h : hA){
				friend = new ATLContactModel();
				friend.fromHashMap(h);
				friends.add(friend);
//				if (friend.getAtlasId()!=null && !friend.getAtlasId().equals(""))
//					cuurentSessionFriends.addToCurrentATLFriendsList(friend);
//				cuurentSessionFriends.addToCurrentFriendsList(friend);

			}
			if (friends!=null && friends.size()>0)
				contact = friends.get(0);
	//		cuurentSessionFriends.addToCurrentFriendsList(friends);
			//atlFriendModelListener.currentSessionFriendsRetreived(true);
			}
			
		}
		return contact;
	}
	/**
	 * Set the item_user table with the event properties
	 * @param setItemType - type ITEM_TYPE ,event or task
	 * @param setWebItemId - ITEM_TYPE.objectId
	 * @param setInviteeAtlasId 
	 * @param setStatus - type ITEM_USER_TASK_STATUS
	 * @param setStatusDateTime
	 * @param setPriorityOrder - type ITEM_TYPE_PRIORITY_ORDER
	 * @param setDisplayOrder - order to display invitees
	 * @return
	 */
	public void setItemUser(
			String setWebItemUserId,
			String setItemUserAlertTitle,
			ATLContactModel invitee,
			ITEM_TYPE setItemType, String setWebItemId, String setInviteeAtlasId,
			ITEM_USER_TASK_STATUS setStatus, Date setStatusDateTime,Date setReceivedDateTime,
			ITEM_TYPE_PRIORITY_ORDER setPriorityOrder, int setDisplayOrder, boolean setWasReceived	)
	{
		webItemUserId = (setWebItemUserId!=null)?setWebItemUserId:"";
		contact = invitee;
		itemUserAlertTitle = (setItemUserAlertTitle!= null)?setItemUserAlertTitle:"";
		itemType = (setItemType!=null)? setItemType :itemType;//default
		webItemId = (setWebItemId!=null)? setWebItemId :webItemId;
		atlasId = (setInviteeAtlasId!=null)? setInviteeAtlasId:"";
		statusDateTime  =(setStatusDateTime!=null)?setStatusDateTime: new Date();
		wasReceived = setWasReceived;
		priorityOrder =(setPriorityOrder!=null)?setPriorityOrder: ITEM_TYPE_PRIORITY_ORDER.IDEAL;
		status = (setStatus!=null)?setStatus:ITEM_USER_TASK_STATUS.SENT;
		action = ACTION.SAVE;
		displayOrder = (atlasId.equals(AtlasAndroidUser.getParseUserID()))?
				0:setDisplayOrder;
		priorityOrder = (setPriorityOrder!=null)? setPriorityOrder: priorityOrder;
		statusDateTime = (setStatusDateTime!=null)?setStatusDateTime:statusDateTime;
		receivedDateTime = (setReceivedDateTime!=null)? setReceivedDateTime: receivedDateTime;
		
		
		if (contact==null || contact.getFirstname()==null || contact.getFirstname().equals(""))
		{
			ArrayList<HashMap<String, String>> hA = DB.q("SELECT * FROM ATL_FRIEND where atlas_id='"+atlasId+"'");
			ATLContactModel friend;
			if (hA!=null && hA.size()>0)
			{
		//	cuurentSessionFriends.setCurrentFriendsList(new ArrayList<ATLContactModel>(hA.size()));
			ArrayList<ATLContactModel> friends = new ArrayList<ATLContactModel>();
			for(HashMap<String, String> h : hA){
				friend = new ATLContactModel();
				friend.fromHashMap(h);
				friends.add(friend);
//				if (friend.getAtlasId()!=null && !friend.getAtlasId().equals(""))
//					cuurentSessionFriends.addToCurrentATLFriendsList(friend);
//				cuurentSessionFriends.addToCurrentFriendsList(friend);

			}
			if (friends!=null && friends.size()>0)
				contact = friends.get(0);
	//		cuurentSessionFriends.addToCurrentFriendsList(friends);
			//atlFriendModelListener.currentSessionFriendsRetreived(true);
			}
		}
	}
	
	
	
//	public static class  LongOperation extends AsyncTask<String, Integer, String> {
//
//		@Override
//		protected String doInBackground(String... params) {
//			
//			
//			String[] operation = params;
////			
////			//CurrentSessionFriendsList cuurentSessionFriends = CurrentSessionFriendsList.getSingletonObject();
////			
////			
////			ArrayList<HashMap<String, String>> hA = DB.q("SELECT * FROM ATL_FRIEND where atlas_id='"+atlasId+"'");
////			ATLContactModel friend;
////			if (hA!=null && hA.size()>0)
////			{
////		//	cuurentSessionFriends.setCurrentFriendsList(new ArrayList<ATLContactModel>(hA.size()));
////			ArrayList<ATLContactModel> friends = new ArrayList<ATLContactModel>();
////			for(HashMap<String, String> h : hA){
////				friend = new ATLContactModel();
////				friend.fromHashMap(h);
////				friends.add(friend);
//////				if (friend.getAtlasId()!=null && !friend.getAtlasId().equals(""))
//////					cuurentSessionFriends.addToCurrentATLFriendsList(friend);
//////				cuurentSessionFriends.addToCurrentFriendsList(friend);
////
////			}
////			if (friends!=null && friends.size()>0)
////				contact = friends.get(0);
////	//		cuurentSessionFriends.addToCurrentFriendsList(friends);
////			//atlFriendModelListener.currentSessionFriendsRetreived(true);
////			}
//			return null;
//		}
		
//	}
	
	
	
	
	
	
	

}
