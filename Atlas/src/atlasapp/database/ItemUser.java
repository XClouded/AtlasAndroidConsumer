package atlasapp.database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.os.AsyncTask;
import atlasapp.common.ATLUser;
import atlasapp.database.DatabaseConstants.ITEM_TYPE;
import atlasapp.database.DatabaseConstants.ITEM_TYPE_PRIORITY_ORDER;
import atlasapp.database.DatabaseConstants.ITEM_USER_TASK_STATUS;
import atlasapp.database.DatabaseConstants.OPERATION_METHOD;
import atlasapp.database.DatabaseConstants.TABLES_NAME;
import atlasapp.model.ATLContactModel;
import atlasapp.model.ATLEventCalendarModel;
import atlasapp.model.ATLItemUserModel;


public class ItemUser extends AtlasServerTable implements ParseDBCallBackInterface{
	
	private Event event;
	
	private ItemUserProperties itemUserProperties;
	
	public ItemUserDelegateInterface itemUserListener;
	private boolean setItemUser;
	
	private ArrayList<ItemUserProperties> itemUserEventsInvites;

	private int itemUserEventsInvitesCount;

	private ArrayList<ItemUserProperties> allUserItemUsersPropertiesList;

	private ArrayList<String> webItemIdsList;

	private ArrayList<String> webItemUserIdsList;
	
	
	public ItemUser()
	{
		
		itemUserProperties = new ItemUserProperties();
		// whether or not current ItemUser object been initialize
		setItemUser = false;
		TABLE_NAME = TABLES_NAME.ITEM_USER.getTableName();
		parseQuery = new ParseQuery(TABLE_NAME);
		userQuery = new ParseQuery(TABLE_NAME);
		atlasServerTable = new ParseObject(TABLE_NAME);
		parseCallBackDeleget = this;

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
	public void setItemUser( ItemUserProperties itemUserProperties
				)
	{
		
		this.itemUserProperties = itemUserProperties;
		setItemUser = true;
	}
	
	
	
	private  void saveItemUserOnParse(ItemUserProperties itemUserProperties) 
	{
		
		
		
		if (itemUserProperties!=null)
		{
			HashMap<String, Object> itemUserColumns = new HashMap<String, Object>();

		
			itemUserColumns.put("item_type", itemUserProperties.itemType.getItemName());
			itemUserColumns.put("web_item_user_id", itemUserProperties.webItemUserId);
		
			itemUserColumns.put("web_item_id", itemUserProperties.webItemId);
			itemUserColumns.put("atlas_id", itemUserProperties.atlasId);
			itemUserColumns.put("status", itemUserProperties.status.ordinal());
			itemUserColumns.put("status_datetime", itemUserProperties.statusDateTime);
			itemUserColumns.put("was_received", itemUserProperties.wasReceived);
			itemUserColumns.put("display_order", itemUserProperties.displayOrder);
//			int priority = (itemUserProperties.priorityOrder.ordinal()==1)? -1:itemUserProperties.priorityOrder.ordinal();
			itemUserColumns.put("priority_order",itemUserProperties.priorityOrder.ordinal()-1);
//			itemUserColumns.put("priority_order", itemUserProperties.priorityOrder.ordinal()-1);
			itemUserColumns.put("action", itemUserProperties.action.getActionName());
			/// wait for getSaveCallBack method...
				put(itemUserColumns);
			
		}

		
	}
	
	/**
	 * Search Parse DB for all itme_user 
	 * where atlas_id equal to the user's id
	 */
	public void getAllUserItemUserRecordsFromParse(ITEM_TYPE itemType) 
	{
		allUserItemUsersPropertiesList = new ArrayList<ItemUserProperties>();
		webItemIdsList = new ArrayList<String>();
		HashMap<String,Object> query = new HashMap<String,Object>();
		query.put("atlas_id", ATLUser.getParseUserID());
		query.put("item_type", ITEM_TYPE.EVENT.getItemName());
		// CALL BACK ON getFindQueryCallBack
		findWhereEqualTo(query, OPERATION_METHOD.FIND);
	}
	/**
	 * Search Parse DB for all new (haven't responded,status = 0 ) itme_user 
	 * where atlas_id equal to the user's id
	 */
	public void getAllNewUserItemUserRecordsFromParse(ITEM_TYPE itemType, final ArrayList<String> alreadyFetchedItemUserRecords) 
	{
		allUserItemUsersPropertiesList = new ArrayList<ItemUserProperties>();
		webItemIdsList = new ArrayList<String>();
//		HashMap<String,Object> query = new HashMap<String,Object>();
		parseQuery.whereEqualTo("atlas_id", ATLUser.getParseUserID());
		parseQuery.whereEqualTo("item_type", ITEM_TYPE.EVENT.getItemName());
//		if (alreadyFetchedItemUserRecords!=null && alreadyFetchedItemUserRecords.size()>0)
//			parseQuery.whereNotContainedIn("web_item_id", alreadyFetchedItemUserRecords);
		parseQuery.whereEqualTo("status", 0);
		parseQuery.findInBackground(new FindCallback() {
			public void done(List<ParseObject> invitesList, ParseException e)
			{
				if (e==null && invitesList!=null && invitesList.size()>0)
				{
					//// invites list contains all the item_user records
					//// on Parse that the user hasnt answered to yet and not 
					/// on local DB...
					String webItemId = "";
					ArrayList<ItemUserProperties> itemUserPropertiesList = new ArrayList<ItemUserProperties>();
					ArrayList<String> webItemIds = new ArrayList<String>();
					HashMap<String,ItemUserProperties> userMoveRecords = new HashMap<String,ItemUserProperties>();
					ItemUserProperties itemUserProp;
					for (ParseObject respondRec:invitesList)
					{
						if (respondRec!=null)
						{
							
							///// Fetch the corresponding event....
							webItemId = respondRec.getString("web_item_id");
							
							if (webItemId!=null && !webItemId.equals(""))
							{

								itemUserProp =  getItemUserPropertiesFromParse(respondRec);
								if (itemUserProp!=null)
								{
									itemUserPropertiesList.add(itemUserProp);
									webItemIds.add(webItemId);
									userMoveRecords.put(webItemId,itemUserProp);

								}
							}

							
							
						}
						
						
					}
					itemUserListener.getUserMoveFromParseCallBack(userMoveRecords,webItemIds,itemUserPropertiesList);
					
				}
				else
				{
					itemUserListener.getUserMoveFromParseCallBack(null,null,null);
				}
			}
		});
		
		
		
		
		// CALL BACK ON getFindQueryCallBack
//		findWhereEqualTo(query, OPERATION_METHOD.FIND);
	}
	/**
	 * Call back from getAllUserItems with all the user's item_user
	 * parseObjects return answer to gotItemUserCallBack method
	 */
	@Override
	public void getFindQueryCallBack(List<ParseObject> foundQueryList,
			boolean found) 
	{
		if (found && foundQueryList!=null && foundQueryList.size()>0 )
		{
			// return the user_item  records with 'was_received' and 
			///'received_datetime" updated properties for the user
		 
			allUserItemUsersPropertiesList = getItemUserPropertiesFromParseObject(foundQueryList);
			
			
				if (allUserItemUsersPropertiesList!=null && allUserItemUsersPropertiesList.size()>0 &&
						webItemIdsList!=null && webItemIdsList.size()>0)
				{
				// RETREIVE ALL THE INVITEES ITEM_USER RECORDS FROM PARSE
				ParseQuery inviteesQuery = new ParseQuery(TABLE_NAME);
				
				inviteesQuery.whereContainedIn("web_item_id", webItemIdsList);
				inviteesQuery.whereNotEqualTo("atlas_id", ATLUser.getParseUserID());
				// CALL BACK ON getFindQueryCallBack
				inviteesQuery.findInBackground(new FindCallback() {
					  public void done(List<ParseObject> inviteesList, ParseException e) {
						    if (e == null && inviteesList!=null && inviteesList.size()>0) {
						    	
						        // The query was successful.
								ArrayList<ItemUserProperties> allItemUsersInviteesPropertiesList = getItemUserPropertiesFromParseObject(inviteesList);

								itemUserListener.gotItemUserCallBack(allUserItemUsersPropertiesList,allItemUsersInviteesPropertiesList,true);

						    } else {
						        // Something went wrong.
								itemUserListener.gotItemUserCallBack(null,null,false);


						    }
						  }
						});
						
				}
				else
				{
					itemUserListener.gotItemUserCallBack(null,null,false);

				}
				

			
		}
		else
		{
			itemUserListener.gotItemUserCallBack(null,null,false);
		}
		
	}
	
	
	public void getItemUserRecordsByWebItemId(ArrayList<String> webItemIdList)
	{
		if (webItemIdList!=null && webItemIdList.size()>0)
		{
			ParseQuery query = new ParseQuery(TABLE_NAME);
			
			query.whereContainedIn("web_item_id", webItemIdList);
			query.findInBackground(new FindCallback() {
				  public void done(List<ParseObject> list, ParseException e) {
					    if (e == null && list!=null && list.size()>0) {
							webItemIdsList = new ArrayList<String>();

					        // The query was successful.
							ArrayList<ItemUserProperties> allItemUserPropertiesList = getItemUserPropertiesFromParseObject(list);

							itemUserListener.getItemUserRecordsByWebItemIdCallBack(true,allItemUserPropertiesList);


					    } else {
					        // Something went wrong.
							itemUserListener.getItemUserRecordsByWebItemIdCallBack(false,null);


					    }
					  }
					});
			
		}
		else
		{
			itemUserListener.getItemUserRecordsByWebItemIdCallBack(false,null);
		}
	}
	
	
	/**
	 * Gets an array list of item_user parse objects 
	 * and return an array list of ItemUserProperties from 
	 * each.
	 * Update the user_item records 'was_received' and 
	 * 'received_datetime"
	 * properties
	 * 
	 * also sets the webItemIdsList with all the web_item_id's 
	 * for the array list item_user , that belongs to the local user
	 * 
	 * @param list 
	 * @return list type itemUserProperties
	 */
	private ArrayList<ItemUserProperties> getItemUserPropertiesFromParseObject(List<ParseObject> list)
	{
		ArrayList<ItemUserProperties> itemUsersPropertiesList = new ArrayList<ItemUserProperties>();
		ItemUserProperties itemUserProperties;
		for (ParseObject itemUserObject:list)
		{
			itemUserProperties = new ItemUserProperties();
			String webItemUserId = (itemUserObject.getString("web_item_user_id")!=null)?
					itemUserObject.getString("web_item_user_id") : "";	
			if (itemUserObject.getString("atlas_id").equals(ATLUser.getParseUserID()))
				webItemIdsList.add(itemUserObject.getString("web_item_id"));
			ITEM_TYPE itemType = (itemUserObject.getString("item_type")!=null && itemUserObject.getString("item_type").equals("event"))?
					ITEM_TYPE.EVENT :ITEM_TYPE.TASK;
			ITEM_USER_TASK_STATUS status = (itemUserObject.getNumber("status")!=null)?
										   ((Integer)itemUserObject.getNumber("status")==0)? ITEM_USER_TASK_STATUS.SENT:
										   ((Integer)itemUserObject.getNumber("status")==1)? ITEM_USER_TASK_STATUS.ACCEPTED:
										   ((Integer)itemUserObject.getNumber("status")==2)? ITEM_USER_TASK_STATUS.DECLINED:
										   ((Integer)itemUserObject.getNumber("status")==3)? ITEM_USER_TASK_STATUS.COMPLITED:
										   ITEM_USER_TASK_STATUS.SENT:ITEM_USER_TASK_STATUS.SENT;
			ITEM_TYPE_PRIORITY_ORDER priority = (itemUserObject.getNumber("priority_order")!=null)?
												((Integer)itemUserObject.getNumber("priority_order")==0)? ITEM_TYPE_PRIORITY_ORDER.OK:
											    ((Integer)itemUserObject.getNumber("priority_order")==-1)? ITEM_TYPE_PRIORITY_ORDER.DECLINED:
											    	ITEM_TYPE_PRIORITY_ORDER.IDEAL:
											    	ITEM_TYPE_PRIORITY_ORDER.OK;	
			// update item_user date_time received date and was_received
			Date receiveDatetime = (itemUserObject.getString("atlas_id").equals(ATLUser.getParseUserID()))?
					new Date() : itemUserObject.getDate("received_datetime");
			boolean received = (itemUserObject.getString("atlas_id").equals(ATLUser.getParseUserID()))?
					true:itemUserObject.getBoolean("was_received");
			
			itemUserProperties.setItemUser(webItemUserId,"",null,
					itemType, itemUserObject.getString("web_item_id"), 
					itemUserObject.getString("atlas_id"), status,
					itemUserObject.getDate("status_datetime"), receiveDatetime, 
					priority, (Integer)itemUserObject.getNumber("display_order"), received);
			itemUserProperties.objectId = itemUserObject.getObjectId();
			

			
//			itemUserProperties.setItemUser(itemType, 
//					itemUserObject.getString("web_item_id")
//					, itemUserObject.getString("atlas_id"), status,
//					datetime, 
//					priority, 
//					(Integer)itemUserObject.getNumber("display_order"));
			itemUsersPropertiesList.add(itemUserProperties);
		}
		return itemUsersPropertiesList;
	}
	
	public static ItemUserProperties getItemUserPropertiesFromParse(ParseObject itemUserProp)
	{
		
		ItemUserProperties itemUserPropertiesRec = null;
		if (itemUserProp!=null)
		{
			itemUserPropertiesRec = new ItemUserProperties();
			String webItemUserId = (itemUserProp.getString("web_item_user_id")!=null)?
					itemUserProp.getString("web_item_user_id") : "";	
			ITEM_TYPE itemType = (itemUserProp.getString("item_type")!=null &&  itemUserProp.getString("item_type").equals("event"))?
					ITEM_TYPE.EVENT :ITEM_TYPE.TASK;
			ITEM_USER_TASK_STATUS status = (itemUserProp.getNumber("status")!=null)?
										   ((Integer)itemUserProp.getNumber("status")==0)? ITEM_USER_TASK_STATUS.SENT:
										   ((Integer)itemUserProp.getNumber("status")==1)? ITEM_USER_TASK_STATUS.ACCEPTED:
										   ((Integer)itemUserProp.getNumber("status")==2)? ITEM_USER_TASK_STATUS.DECLINED:
										   ((Integer)itemUserProp.getNumber("status")==3)? ITEM_USER_TASK_STATUS.COMPLITED:
										   ITEM_USER_TASK_STATUS.SENT:ITEM_USER_TASK_STATUS.SENT;
			ITEM_TYPE_PRIORITY_ORDER priority = (itemUserProp.getNumber("priority_order")!=null)?
												((Integer)itemUserProp.getNumber("priority_order")==0)? ITEM_TYPE_PRIORITY_ORDER.OK:
											    ((Integer)itemUserProp.getNumber("priority_order")==-1)? ITEM_TYPE_PRIORITY_ORDER.DECLINED:
			
											    		ITEM_TYPE_PRIORITY_ORDER.IDEAL:
											    			ITEM_TYPE_PRIORITY_ORDER.OK;	
			// update item_user date_time received date and was_received
			Date receiveDatetime = (itemUserProp.getString("atlas_id").equals(ATLUser.getParseUserID()))?
					new Date() : itemUserProp.getDate("received_datetime");
			boolean received = (itemUserProp.getString("atlas_id").equals(ATLUser.getParseUserID()))?
					true:itemUserProp.getBoolean("was_received");
			
			itemUserPropertiesRec.setItemUser(webItemUserId,"",null,
					itemType, itemUserProp.getString("web_item_id"), 
					itemUserProp.getString("atlas_id"), status,
					itemUserProp.getDate("status_datetime"), receiveDatetime, 
					priority, (Integer)itemUserProp.getNumber("display_order"), received);
			itemUserPropertiesRec.objectId = itemUserProp.getObjectId();
			

			
		}
			
		return itemUserPropertiesRec;
	}
	
	
	/**
	 * Gets the list of the event's members (invitess) 
	 * and the event properties (after been updated on Parse ,with their
	 * object id and primary_web_event_id set)
	 * and update Parse with the item users accordingly
	 * 
	 * when done - sends a respond to a call back method (for the itemUsers listeners)
	 * itemUsersSavedCallBack (success, ArrayList<ItemUsersProperties>)
	 * @param eventInvitess
	 * @param event
	 */
	public void setItemUsers(ArrayList<ATLContactModel> eventInvitees
			,ArrayList<EventProperties> event) {
		if (eventInvitees!=null && eventInvitees.size()>0 && event!=null && event.size()>0)
		{
			ItemUserProperties itemUserProperties;
			itemUserEventsInvites = new ArrayList<ItemUserProperties>(); 
			for (EventProperties eventALT:event)
			{
				// make an invite from each alt time of the event
				if (eventALT.objectId!=null && !eventALT.objectId.equals(""))
				{
					int inviteeNum = 1;
					for (ATLContactModel invitee:eventInvitees)
					{
						itemUserProperties = setItemUserPropertiesFromEvent(eventALT,invitee,inviteeNum);
						if (itemUserProperties!=null)
						{ /// validate event invite successfully built
							itemUserProperties.primaryWebEventId = eventALT.primaryWebEventId;
							itemUserEventsInvites.add(itemUserProperties);
						}  
						else  
						{/// error making an alt event invite to that invitee
							itemUserListener.itemUsersSavedCallBack(false, null);
							return;
						}
						if (!invitee.getAtlasId().equals(ATLUser.getParseUserID()))
							inviteeNum++;
					}
				
				}
				else
				{
					//// something wrong happened and event doesnt have objectId
					itemUserListener.itemUsersSavedCallBack(false, null);
					return;
				}
			}
			/// done making invites and filling the itemUserEventsInvites
			//// update Parse with these invites ('item_user') table
			updateItemUsersOnParse();
		}else
			
			itemUserListener.itemUsersSavedCallBack(false, null);
	}
	/**
	 * Once the itemUserEventsInvites is set with all the right invites 
	 * from the invitees list and event properties
	 * set the item user on the item_user table
	 */
	private void updateItemUsersOnParse() {
		if (itemUserEventsInvites!=null && itemUserEventsInvites.size()>0
				&& itemUserEventsInvites.get(0)!=null)
		{// update on Parse
			itemUserEventsInvitesCount = 0;
			
			saveItemUserOnParse(itemUserEventsInvites.get(itemUserEventsInvitesCount));
		}
		else
		{
			itemUserListener.itemUsersSavedCallBack(false, null);

		}
		
	}
	/**
	 * Get all the current user item_user  and update them 
	 * on Parse (was received and date time)
	 * 
	 * 
	 * call back on updateBookedItemUserCallBack and updateItemUserCallBack methos
	 * @param itemUserRecords
	 */
	public void updateItemUserOnParse(ArrayList<ItemUserProperties> itemUserRecords)
	{
		
		if (itemUserRecords!=null && itemUserRecords.size()>0)
		{
			final HashMap<String,ItemUserProperties> itemUserHashMapByWebItemId = setItemUserPropertiesHashByItemUserWebItemId(itemUserRecords);
			
			if (itemUserHashMapByWebItemId!=null && itemUserHashMapByWebItemId.size()>0)
			{
				ParseQuery inviteesQuery = new ParseQuery(TABLE_NAME);

				inviteesQuery.whereContainedIn("web_item_user_id", webItemUserIdsList);
				inviteesQuery.findInBackground(new FindCallback() {
					  public void done(List<ParseObject> updateItemUserOnParseList, ParseException e) {
						    if (e == null && updateItemUserOnParseList!=null && updateItemUserOnParseList.size()>0) {
						        
						    	updateItemUsersRecordsOnParse(updateItemUserOnParseList,itemUserHashMapByWebItemId);
						    	
						    	
						    
						    } else {
						        // Something went wrong.
					    		itemUserListener.updateItemUserCallBack(false);
//					    		itemUserListener.updateBookedItemUserCallBack(false);

						    }
						  }

					
					});
			}
			else
			{
	    		itemUserListener.updateItemUserCallBack(false);
//	    		itemUserListener.updateBookedItemUserCallBack(false);

			}
		}
		else
		{
			itemUserListener.updateItemUserCallBack(false);
//			itemUserListener.updateBookedItemUserCallBack(false);
		}
	}
	
	
	
	private void saveRespondUpdatedItemUserRecordsOnParse(
			List<ParseObject> updateItemUserOnParseList) 
{
	if (updateItemUserOnParseList!=null)
	{
		final List<ParseObject> updateItemUserOnParseListCopy = updateItemUserOnParseList;
	
		if (updateItemUserOnParseListCopy.get(0)!=null)
			updateItemUserOnParseListCopy.get(0).saveInBackground(new SaveCallback()
			{
				public void done(ParseException e) {
				    if (e == null)
				    {
				    	updateItemUserOnParseListCopy.remove(0);
				    	if (updateItemUserOnParseListCopy==null || updateItemUserOnParseListCopy.size()==0)
				    	{
//				    		itemUserListener.updateItemUserCallBack(true);
				    		itemUserListener.updateBookedItemUserCallBack(true);
				    	}
				    	else
				    		saveRespondUpdatedItemUserRecordsOnParse(updateItemUserOnParseListCopy);
				    	
				    }else 
				    {
//			    		itemUserListener.updateItemUserCallBack(false);
			    		itemUserListener.updateBookedItemUserCallBack(false);

					}
				}
			});
			
		
	}
		
	else
	{
//		itemUserListener.updateItemUserCallBack(false);
		itemUserListener.updateBookedItemUserCallBack(false);

	}
}
	
	private void updateItemUsersRespondRecordsOnParse(
			List<ParseObject> updateItemUserOnParseList,
			HashMap<String, ItemUserProperties> itemUserHashMapByWebItemId) {
    	String webItemUserId ="";
    	ItemUserProperties itemUserPropertyUpdated;
    	for (ParseObject itemUserRecord:updateItemUserOnParseList)
    	{
    		if (itemUserRecord!=null)
    		{
    			webItemUserId = itemUserRecord.getString("web_item_id");
    			if (webItemUserId!=null && !webItemUserId.equals(""))
    			{
    				itemUserPropertyUpdated = itemUserHashMapByWebItemId.get(webItemUserId);
    				if (itemUserRecord.getString("atlas_id")!=null && itemUserRecord.getString("atlas_id").equals(ATLUser.getParseUserID()))
    				{
    					itemUserRecord.put("item_type", itemUserPropertyUpdated.itemType.getItemName());
    					itemUserRecord.put("web_item_id", itemUserPropertyUpdated.webItemId);
    					itemUserRecord.put("atlas_id", itemUserPropertyUpdated.atlasId);
    					itemUserRecord.put("status", itemUserPropertyUpdated.status.ordinal());
    					itemUserRecord.put("status_datetime", itemUserPropertyUpdated.statusDateTime);
    					itemUserRecord.put("was_received", itemUserPropertyUpdated.wasReceived);
    					itemUserRecord.put("received_datetime", itemUserPropertyUpdated.receivedDateTime);
    					itemUserRecord.put("display_order", itemUserPropertyUpdated.displayOrder);
    					int priority = (itemUserPropertyUpdated.priorityOrder.equals(ITEM_TYPE_PRIORITY_ORDER.DECLINED))? -1:
    						(itemUserPropertyUpdated.priorityOrder.equals(ITEM_TYPE_PRIORITY_ORDER.IDEAL))? 1:

    						0;
    					itemUserRecord.put("priority_order",priority);
    					itemUserRecord.put("action", "upd");
    				}	    			
    			}
    		}
    	}
    	
    	
    	saveRespondUpdatedItemUserRecordsOnParse(updateItemUserOnParseList);
    	
		
	}
	public void updateRespondItemUserOnParse(ArrayList<ItemUserProperties> itemUserRecords)
	{
		
		if (itemUserRecords!=null && itemUserRecords.size()>0)
		{
			
			
			
			ArrayList<String> webItemIdsList = new ArrayList<String>();
			for (ItemUserProperties itemUserProp:itemUserRecords)
				if (itemUserProp!=null && itemUserProp.webItemId!=null && !itemUserProp.webItemId.equals("") && !webItemIdsList.contains(itemUserProp.webItemId))
					webItemIdsList.add(itemUserProp.webItemId);
				
			final HashMap<String,ItemUserProperties> itemUserHashMapByWebItemId = setItemUserPropertiesHashByItemWebItemId(itemUserRecords);
			
			if (itemUserHashMapByWebItemId!=null && itemUserHashMapByWebItemId.size()>0)
			{
				ParseQuery inviteesQuery = new ParseQuery(TABLE_NAME);

				inviteesQuery.whereContainedIn("web_item_id", webItemIdsList);
				inviteesQuery.findInBackground(new FindCallback() {
					  public void done(List<ParseObject> updateItemUserOnParseList, ParseException e) {
						    if (e == null && updateItemUserOnParseList!=null && updateItemUserOnParseList.size()>0) {
						        
						    	updateItemUsersRespondRecordsOnParse(updateItemUserOnParseList,itemUserHashMapByWebItemId);
						    	
						    	
						    
						    } else {
						        // Something went wrong.
//					    		itemUserListener.updateItemUserCallBack(false);
					    		itemUserListener.updateBookedItemUserCallBack(false);

						    }
						  }

					
					});
			}
			else
			{
//	    		itemUserListener.updateItemUserCallBack(false);
	    		itemUserListener.updateBookedItemUserCallBack(false);

			}
		}
		else
		{
//			itemUserListener.updateItemUserCallBack(false);
			itemUserListener.updateBookedItemUserCallBack(false);
		}
	}
	
	
	private HashMap<String, ItemUserProperties> setItemUserPropertiesHashByItemWebItemId(
			ArrayList<ItemUserProperties> itemUserRecords) {
		HashMap<String, ItemUserProperties> itemUserHash= null;
		ArrayList<String> webItemUserIdsList = new ArrayList<String>();
		if (itemUserRecords!=null && itemUserRecords.size()>0)
		{
			itemUserHash = new HashMap<String, ItemUserProperties>();
			for (ItemUserProperties itemUserPropert:itemUserRecords)
			{
				if (itemUserPropert!=null && itemUserPropert.atlasId.equals(ATLUser.getParseUserID()))
				{
					webItemUserIdsList.add(itemUserPropert.webItemId);
					itemUserHash.put(itemUserPropert.webItemId, itemUserPropert);
					
				}
			}
		}
		
		
		
		return itemUserHash;
	}
	
	
	
	
//			final ItemUserProperties itemUserProperties = itemUserRecords.get(0);
//			
//			if (itemUserProperties!=null)
//			{
//				parseQuery.getInBackground(itemUserProperties.objectId, new GetCallback() {
//					  public void done(ParseObject object, ParseException e) {
//					    if (e == null) {
//					    	object.put("item_type", itemUserProperties.itemType.getItemName());
//							
//					    	object.put("web_item_id", itemUserProperties.webItemId);
//					    	object.put("atlas_id", itemUserProperties.atlasId);
//					    	object.put("status", itemUserProperties.status.ordinal());
//					    	object.put("status_datetime", itemUserProperties.statusDateTime);
//					    	object.put("was_received", itemUserProperties.wasReceived);
//					    	object.put("display_order", itemUserProperties.displayOrder);
//					    	object.put("priority_order", itemUserProperties.priorityOrder.ordinal());
//					    	object.put("action", itemUserProperties.action.getActionName());
//					    	/// wait for getSaveCallBack method...
//							object.saveInBackground(new SaveCallback()
//							{
//								public void done(ParseException e) {
//								    if (e == null)
//								    {
//								    	ArrayList<ItemUserProperties> clone = (ArrayList<ItemUserProperties>)itemUserRecords.clone();
//								    	clone.remove(0);
//								    	if (clone==null || clone.size()==0)
//								    		itemUserListener.updateItemUserCallBack(true);
//								    	else
//								    		updateItemUserOnParse(clone);
//								    	
//								    }else 
//								    {
//							    		itemUserListener.updateItemUserCallBack(false);
//
//									}
//								}
//							});
//					    } else {
//					      // something went wrong
//
//					    }
//					  }
//					});
//			}
//		}
//	}
//	
//	
		private void updateItemUsersRecordsOnParse(
				List<ParseObject> updateItemUserOnParseList,
				HashMap<String, ItemUserProperties> itemUserHashMapByWebItemId) {
	    	String webItemUserId ="";
	    	ItemUserProperties itemUserPropertyUpdated;
	    	for (ParseObject itemUserRecord:updateItemUserOnParseList)
	    	{
	    		if (itemUserRecord!=null)
	    		{
	    			webItemUserId = itemUserRecord.getString("web_item_user_id");
	    			if (webItemUserId!=null && !webItemUserId.equals(""))
	    			{
	    				itemUserPropertyUpdated = itemUserHashMapByWebItemId.get(webItemUserId);
	    				if (itemUserPropertyUpdated!=null)
	    				{
	    					itemUserRecord.put("item_type", itemUserPropertyUpdated.itemType.getItemName());
	    					itemUserRecord.put("web_item_id", itemUserPropertyUpdated.webItemId);
	    					itemUserRecord.put("atlas_id", itemUserPropertyUpdated.atlasId);
	    					itemUserRecord.put("status", itemUserPropertyUpdated.status.ordinal());
	    					itemUserRecord.put("status_datetime", itemUserPropertyUpdated.statusDateTime);
	    					itemUserRecord.put("was_received", itemUserPropertyUpdated.wasReceived);
	    					itemUserRecord.put("display_order", itemUserPropertyUpdated.displayOrder);
//	    					int priority = (itemUserPropertyUpdated.priorityOrder.ordinal()==1)? -1:itemUserPropertyUpdated.priorityOrder.ordinal();
	    					itemUserRecord.put("priority_order",itemUserPropertyUpdated.priorityOrder.ordinal()-1);
	    					itemUserRecord.put("action", itemUserPropertyUpdated.action.getActionName());
	    				}	    			
	    			}
	    		}
	    	}
	    	
	    	
	    	saveUpdatedItemUserRecordsOnParse(updateItemUserOnParseList);
	    	
			
		}
		
		
		
	private void saveUpdatedItemUserRecordsOnParse(
				List<ParseObject> updateItemUserOnParseList) 
	{
		if (updateItemUserOnParseList!=null)
		{
			final List<ParseObject> updateItemUserOnParseListCopy = updateItemUserOnParseList;
		
			if (updateItemUserOnParseListCopy.get(0)!=null)
				updateItemUserOnParseListCopy.get(0).saveInBackground(new SaveCallback()
				{
					public void done(ParseException e) {
					    if (e == null)
					    {
					    	updateItemUserOnParseListCopy.remove(0);
					    	if (updateItemUserOnParseListCopy==null || updateItemUserOnParseListCopy.size()==0)
					    	{
					    		itemUserListener.updateItemUserCallBack(true);
//					    		itemUserListener.updateBookedItemUserCallBack(true);
					    	}
					    	else
					    		saveUpdatedItemUserRecordsOnParse(updateItemUserOnParseListCopy);
					    	
					    }else 
					    {
				    		itemUserListener.updateItemUserCallBack(false);
//				    		itemUserListener.updateBookedItemUserCallBack(false);

						}
					}
				});
				
			
		}
			
		else
		{
    		itemUserListener.updateItemUserCallBack(false);

		}
	}
	private HashMap<String, ItemUserProperties> setItemUserPropertiesHashByItemUserWebItemId(
			ArrayList<ItemUserProperties> itemUserRecords) {
		HashMap<String, ItemUserProperties> itemUserHash= null;
		webItemUserIdsList = new ArrayList<String>();
		if (itemUserRecords!=null && itemUserRecords.size()>0)
		{
			itemUserHash = new HashMap<String, ItemUserProperties>();
			for (ItemUserProperties itemUserPropert:itemUserRecords)
			{
				if (itemUserPropert!=null)
				{
					webItemUserIdsList.add(itemUserPropert.webItemUserId);
					itemUserHash.put(itemUserPropert.webItemUserId, itemUserPropert);
					
				}
			}
		}
		
		
		
		return itemUserHash;
	}
//	public void updateItemUserOnParse(final ArrayList<ItemUserProperties> itemUserRecords)
//	{
//		if (itemUserRecords!=null && itemUserRecords.size()>0)
//		{
//			final ItemUserProperties itemUserProperties = itemUserRecords.get(0);
//			
//			if (itemUserProperties!=null)
//			{
//				parseQuery.whereContainedIn("web_item_id", arg1)(itemUserProperties.objectId, new GetCallback() {
//					  public void done(ParseObject object, ParseException e) {
//					    if (e == null) {
//					    	object.put("item_type", itemUserProperties.itemType.getItemName());
//							
//					    	object.put("web_item_id", itemUserProperties.webItemId);
//					    	object.put("atlas_id", itemUserProperties.atlasId);
//					    	object.put("status", itemUserProperties.status.ordinal());
//					    	object.put("status_datetime", itemUserProperties.statusDateTime);
//					    	object.put("was_received", itemUserProperties.wasReceived);
//					    	object.put("display_order", itemUserProperties.displayOrder);
//					    	object.put("priority_order", itemUserProperties.priorityOrder.ordinal());
//					    	object.put("action", itemUserProperties.action.getActionName());
//					    	/// wait for getSaveCallBack method...
//							object.saveInBackground(new SaveCallback()
//							{
//								public void done(ParseException e) {
//								    if (e == null)
//								    {
//								    	ArrayList<ItemUserProperties> clone = (ArrayList<ItemUserProperties>)itemUserRecords.clone();
//								    	clone.remove(0);
//								    	if (clone==null || clone.size()==0)
//								    		itemUserListener.updateItemUserCallBack(true);
//								    	else
//								    		updateItemUserOnParse(clone);
//								    	
//								    }else 
//								    {
//							    		itemUserListener.updateItemUserCallBack(false);
//
//									}
//								}
//							});
//					    } else {
//					      // something went wrong
//
//					    }
//					  }
//					});
//			}
//		}
//	}
//	
	
	
	
	
	
	
	/**
	 * call back from parse on item user attempt to save
	 * keeps updating the parse table with the item users invites
	 * if update fails sends false message to the itemUsersSavedCallBack call back 
	 * methods
	 * otherwise , when done , sends 'true' and the array list of all the item users 
	 * properties updated.(to the listeners)
	 */
	@Override
	public void getSaveCallBack(boolean saved,ParseObject parseObjectSaved) 
	{
		if (saved && parseObjectSaved!=null)
		{
			// update the item_user saved on parse with the objectId given to it
			itemUserEventsInvites.get(itemUserEventsInvitesCount).objectId = parseObjectSaved.getObjectId();
			itemUserEventsInvitesCount++;
			if (itemUserEventsInvitesCount<itemUserEventsInvites.size())
			{ /// keep update parse with all the invites ...
				atlasServerTable = new ParseObject(TABLE_NAME);

				saveItemUserOnParse(itemUserEventsInvites.get(itemUserEventsInvitesCount));

			}
			else     
			{
				/// all item user were successfully updated  on parse DB
				itemUserListener.itemUsersSavedCallBack(true, itemUserEventsInvites);
			}
			
		}
		else
		{
			itemUserListener.itemUsersSavedCallBack(false, null);

		}
		
	}
	/**
	 * Gets an event alt time and invitee (as ATLContactModel) and
	 * return an invite type itemUserProperties object or null if
	 * not validate
	 * @param eventALT
	 * @param invitee  - Must have parse objectId (atlas id - must be an atlas user)
	 * @return
	 */
	private ItemUserProperties setItemUserPropertiesFromEvent(
			EventProperties eventALT, ATLContactModel invitee,int inviteeOrder) {

		ItemUserProperties itemUserProperties = null;
		if (eventALT!=null && eventALT.objectId!=null && !eventALT.objectId.equals("")
				&& invitee!=null && invitee.getAtlasId()!=null && !invitee.getAtlasId().equals(""))
		{
			//// event alt has the needed properties for an invite
			itemUserProperties = new ItemUserProperties();
			//ITEM_TYPE_PRIORITY_ORDER priority = (eventALT.isPrimary)? ITEM_TYPE_PRIORITY_ORDER.IDEAL:ITEM_TYPE_PRIORITY_ORDER.OKAY;
			ITEM_USER_TASK_STATUS status = (invitee.getAtlasId().equals(ATLUser.getParseUserID()))? 
					ITEM_USER_TASK_STATUS.ACCEPTED : ITEM_USER_TASK_STATUS.SENT;
			int displayOrder = (invitee.getAtlasId().equals(ATLUser.getParseUserID()))?
					0:inviteeOrder;  
			String webItemUserId = generateRandomID();
			itemUserProperties.setItemUser(
					webItemUserId,
					eventALT.title,
					invitee,
					ITEM_TYPE.EVENT, 
					eventALT.webEventId, invitee.getAtlasId(),
					status, new Date(),
					new Date(),ITEM_TYPE_PRIORITY_ORDER.OK,
					displayOrder,false);
			itemUserProperties.primaryWebEventId = eventALT.primaryWebEventId;
			
		}
		return itemUserProperties;
	}
	final int desiredRandomLength   =  8 ;

	private String  generateRandomID()
	{
	  // RANDOM NUMBER ------------------------------------------------------------------------------------------------
	  // -------------------------------------------------------------------------------------------------------------
	  int maxRandomValue ;
	  int intRandomNumber ;
	  String strRandomNumber = "" ;
	  String strRandomString = "" ;
	  maxRandomValue = (int) (Math.pow(2, 32)-1);//pow(2,32)-1 ;
	  String padString = "" ;
	  
	  
	  // Generate a 32-byte hex (128-bit binary) number 4 times, concatenating each 8-byte string to get a 32-byte string:
	  int ktr = 0 ;
	  Random random = new Random();
	  for (ktr = 1; ktr <= 4 ; ktr++) {

	    // Generate a 32-bit random number
	    intRandomNumber = random.nextInt(maxRandomValue);//arc4random_uniform(maxRandomValue) ;

	    // Format the 32-bit random number as hex:
	     strRandomNumber = Integer.toHexString(intRandomNumber);
	   // strRandomNumber = [NSString stringWithFormat: @"%x", intRandomNumber] ;
	     padString = "" ;
	    // Pad the 32-bit random number on the left with zeros to desiredRandomLength const value:
	    if (strRandomNumber.length() < desiredRandomLength) {
	    	
	    	for (int i=0; i<(desiredRandomLength - strRandomNumber.length()); i++)
	    		padString += "0";
	      //padString = [padString stringByPaddingToLength: () withString: @"0"  startingAtIndex: 0] ;
	      strRandomNumber =  padString+ strRandomNumber ;
	    }

	    // Build the random string by concatenating with last random number string
	    strRandomString =strRandomString+strRandomNumber ;
	  }
	  // -------------------------------------------------------------------------------------------------------------

	  return strRandomString ;
	  
	}
	@Override
	public void getObjectIdCallBack(ParseObject retreivedObjectId,
			boolean success) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void getDataCallBack(byte[] fileRetreived, boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getSuccessCallBack(boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void registerSuccess(boolean signUpSuccess) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void signInSuccess(boolean success) {
		// TODO Auto-generated method stub
		
	}
//	@Override
//	public void saveFileCallBack(boolean success,ParseObject parseObjectSaved) {
//		// TODO Auto-generated method stub
//		
//	}
	@Override
	public void getUserEmailOnParseCallBack(
			HashMap<String, String> loginProperties, boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getFriendEmailOnParseCallBack(
			List<ParseObject> loginProperties, boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getFacebookFriendIdOnParseCallBack(
			List<ParseObject> findResult, boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getAllAtlasUsersCallBack(List<ParseObject> findResult,
			boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getAllFBAtlasUsersFriendsCallBack(List<ParseObject> findResult,
			boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void friendSignInSuccess(boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getAtlasNewFriendsByEmailCallBack(List<ParseObject> findResult,
			boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getUpateCallBack(boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void signInNewFriendUserSuccess(boolean success, ParseUser user) {
		// TODO Auto-generated method stub
		
	}
	
	
	public static class  RefreshAllItemUserRecordsInBackground extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg) 
		{
			ArrayList<ItemUserProperties> allUserItemUsersPropertiesList = new ArrayList<ItemUserProperties>();
			ArrayList<String> webItemIdsList = new ArrayList<String>();
//			HashMap<String,Object> query = new HashMap<String,Object>();
			
//			TABLE_NAME = TABLES_NAME.ITEM_USER.getTableName();
			ParseQuery parseQuery = new ParseQuery(TABLES_NAME.ITEM_USER.getTableName());
//			userQuery = new ParseQuery(TABLE_NAME);
			ParseObject atlasServerTable = new ParseObject(TABLES_NAME.ITEM_USER.getTableName());
			
			
			parseQuery.whereEqualTo("atlas_id", ATLUser.getParseUserID());
			parseQuery.whereEqualTo("item_type", ITEM_TYPE.EVENT.getItemName());
//			if (alreadyFetchedItemUserRecords!=null && alreadyFetchedItemUserRecords.size()>0)
//				parseQuery.whereNotContainedIn("web_item_id", alreadyFetchedItemUserRecords);
//			parseQuery.whereEqualTo("status", 0);
			parseQuery.findInBackground(new FindCallback() {
				public void done(List<ParseObject> invitesList, ParseException e)
				{
					if (e==null && invitesList!=null && invitesList.size()>0)
					{
						//// invites list contains all the item_user records
						//// on Parse that the user hasnt answered to yet and not 
						/// on local DB...
						String webItemId = "";
						ArrayList<ItemUserProperties> itemUserPropertiesList = new ArrayList<ItemUserProperties>();
						ArrayList<String> webItemIds = new ArrayList<String>();
						final ArrayList<ItemUserProperties> allItemUserRecords = new ArrayList<ItemUserProperties> ();
						final HashMap<String,ItemUserProperties> userMoveRecords = new HashMap<String,ItemUserProperties>();
						ItemUserProperties itemUserProp;
						for (ParseObject respondRec:invitesList)
						{
							if (respondRec!=null)
							{
								
								///// Fetch the corresponding event....
								webItemId = respondRec.getString("web_item_id");
								
								if (webItemId!=null && !webItemId.equals(""))
								{

									itemUserProp =  getItemUserPropertiesFromParse(respondRec);
									if (itemUserProp!=null)
									{
										allItemUserRecords.add(itemUserProp);
										itemUserPropertiesList.add(itemUserProp);
										webItemIds.add(webItemId);
										userMoveRecords.put(webItemId,itemUserProp);

									}
								}

								
								
							}
							
							
						}
						final HashMap<String, ArrayList<ItemUserProperties>> userMoveRecordsByPrimaryEventId = new HashMap<String, ArrayList<ItemUserProperties>>();
						if (userMoveRecords!=null && userMoveRecords.size()>0)
						{
							
							Event event = new Event();
							event.parseQuery.whereContainedIn("web_event_id", webItemIds);
							event.parseQuery.findInBackground(new FindCallback() {
								public void done(List<ParseObject> eventList, ParseException e)
								{
									EventProperties eventProp = null;
									ArrayList<EventProperties> eventListProperties = new ArrayList<EventProperties>();
									String primaryWebEventId = "";
									String webEventId = "";
									if (e==null && eventList!=null && eventList.size()>0)
									{
										for (ParseObject eventRecord:eventList)
										{
											if (eventRecord!=null)
											{
												primaryWebEventId = eventRecord.getString("primary_web_event_id");
												webEventId = eventRecord.getString("web_event_id");
												eventProp = new EventProperties(eventRecord);
												ArrayList<ItemUserProperties> eventsItemUserRecords = null;
												ItemUserProperties itemUserProperties;
												primaryWebEventId = (primaryWebEventId!=null && primaryWebEventId.equals(""))?webEventId:primaryWebEventId;
												if (!userMoveRecordsByPrimaryEventId.containsKey(primaryWebEventId))
												{
													eventsItemUserRecords = userMoveRecordsByPrimaryEventId.get(primaryWebEventId);
												}
												if (eventsItemUserRecords==null)
													eventsItemUserRecords = new ArrayList<ItemUserProperties>();
												
												itemUserProperties = userMoveRecords.get(webEventId);
												itemUserProperties.setEventAssociated(eventProp);
												eventsItemUserRecords.add(itemUserProperties);
												userMoveRecordsByPrimaryEventId.put(primaryWebEventId, eventsItemUserRecords);
												
											}
											eventListProperties.add(eventProp);
											
										}
										/// writing event to local table 
										ATLEventCalendarModel.WriteEventProperties(eventListProperties);
										ATLItemUserModel.UpdateAllItemUserRecordsLocallyInBackground(ITEM_TYPE.EVENT,allItemUserRecords);
										
									}else
									{

									}
									

								}
							});

						}
						else
						{
						}						
					}
					else
					{
					}
				}
			});
			
			return null;
		}
	}


	@Override
	public void saveFileCallBack(boolean success, ParseObject psrseObjectSaved
			) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void refreshUserPictureOnTheBackgroundCallBack(boolean success,
			String imageUrl) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void isEmailVerifiedCallBack(boolean verified) {
		// TODO Auto-generated method stub
		
	}

	
	

}
