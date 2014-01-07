package com.atlasapp.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.atlasapp.atlas_database.DatabaseConstants.ITEM_TYPE;
import com.atlasapp.atlas_database.DatabaseConstants.ITEM_TYPE_PRIORITY_ORDER;
import com.atlasapp.atlas_database.DatabaseConstants.ITEM_USER_TASK_STATUS;
import com.atlasapp.atlas_database.EventProperties;
import com.atlasapp.atlas_database.DatabaseConstants.ACTION;
import com.atlasapp.atlas_database.DatabaseConstants.EVENT_STATUS;
import com.atlasapp.atlas_database.ItemUserProperties;

public class ATLItemUserModel implements DBCallBackInterface{
	
	private HashMap<String,String> h;

	
	public ATLItemUserModelCallBackInterface atlItemUserModelListener;
	
	
	
	protected String primaryWebEventId = "";
	///////properties
	protected int itemUserId = -1;
	/// the id of the web_item_id associated with this 
	//// item_user as set on the local db
	protected int itemId = -1;
	/// the id of the web_item_id associated with
	////this item =_user record
	///from Parse Parse
	protected  String webItemUserId = "";
	/// the id of the item_user records from Parse
	protected  String webItemId = "";
	//the invitee id
	protected  String atlasId = "";
	
	
	protected  String title="";
	protected  String location="";
	protected  String notes="";
	protected  Date startDateTime = null;
	protected  int duration =0;
	// the order we display the alternative events 
	protected  int displayOrder = 0;
	
	
	protected  ITEM_USER_TASK_STATUS status= ITEM_USER_TASK_STATUS.SENT;
	protected  ACTION action = ACTION.SAVE;
	protected  boolean wasREceived = false;
	protected Date statusDateTime= new Date();
	protected Date receivedDateTime = new Date();
	protected Date modifiedDatetime= new Date();
	protected ITEM_TYPE_PRIORITY_ORDER priority = ITEM_TYPE_PRIORITY_ORDER.IDEAL;
	
	private boolean success;


	private String itemType = "task";


	private ItemUserProperties itemUserProperties;


	private boolean wasReceived;

	
	public ATLItemUserModel()
	{
		//for call back from DBCallBackInterface (writeCallBack  to db)

		DB.dbCallBackInterface = this;
	}
	
public synchronized static int write(ItemUserProperties itemUserProperties) {
		
		
		
		String sql = "insert into item_user (" 
				+ "web_item_user_id,"   
				+ "primary_web_event_id,"
				+ "item_type,"
				+ "item_id,"
				+ "web_item_id," 
				+ "atlas_id,"
				+ "status,"
				+ "display_order,"
				
				+ "status_datetime,"
				+ "was_received,"
				+ "received_datetime,"
				+ "priority_order,"
				+ "action) values (" 
				+ "'" + DB.prep(itemUserProperties.webItemUserId) + "',"
			    + "'" + DB.prep(itemUserProperties.primaryWebEventId) + "',"
	
				+ "'" + DB.prep(itemUserProperties.itemType.getItemName()) + "'," 
				+ "'" + DB.prep(itemUserProperties.itemId) + "',"   
				+ "'" + DB.prep(itemUserProperties.webItemId) + "'," 
				+ "'" + DB.prep(itemUserProperties.atlasId) + "'," 
				+ "'" + DB.prep(itemUserProperties.status.ordinal()) + "'," 
				+ "'" + DB.prep(itemUserProperties.displayOrder) + "'," 
				+ "'" + DB.prep(itemUserProperties.statusDateTime.toString()) + "'," 
				+ "'" + DB.prep(itemUserProperties.wasReceived) + "'," 
				+ "'" + DB.prep(itemUserProperties.receivedDateTime.toString()) + "'," 
				+ "'" + DB.prep(itemUserProperties.priorityOrder.ordinal()) + "',"
				+ "'" + DB.prep(itemUserProperties.action.getActionName()) + "');";
		DB.q(sql);
		// item_user_id
		int currentItemUserId = Integer.parseInt(DB.lastInsertId());
		itemUserProperties.itemUserId = currentItemUserId;
		
		return currentItemUserId;
			
	}
/**
 * Gets the item_id (the id given from the local DB of thr task 
 * or event associated with this item_user record) and delete
 * the all the item user records from local DB
 * @param eventId
 * @return the number of rows deleted from the DB
 */
private synchronized  int deleteItemUserByItemId(int itemId)
{
	int rowsDeleted = 0;
	
	if (itemId!=-1)
	{
		String id = DB.prep(itemId);
		if (id!=null && !id.equals(""))
			rowsDeleted = DB.delete("item_user", "item_id", new String[]{id}); 
	}
	
	return rowsDeleted;
}


public static synchronized  int deleteItemUserByWebItemId(ArrayList<String> webItemId)
{
	int rowsDeleted = 0;
	
	if (webItemId!=null && webItemId.size()>0)
	{
		//for (String webItem:webItemId)
		//{
		String[] webItem  = new String[webItemId.size()];
		int index = 0;
		for (String item:webItemId)
			if (item!=null && !item.equals(""))
			{
				webItem[index] = item;
				index++;
			}
			if (webItem!=null && webItem.length>0)
				rowsDeleted += DB.delete("item_user", "web_item_id",webItem); 

		//}
	}
	
	return rowsDeleted;
}

/**
 * Update the local item_user table by item_user_id, according to the hash map 
 * key - column
 * value - updated
 * @param itemUserUpdatedColumns
 * @param itemUserId - primary local key given to this item_user record 
 * @return number of rows updated on the DB
 */
public synchronized int upadteItemUserByItemUserId(HashMap<String,Object> itemUserUpdatedColumns, int itemUserId)
{
	if (itemUserUpdatedColumns!=null && itemUserId!=-1)
	{
		HashMap<String,String> updateItemUserHash = new HashMap<String,String>();
		String id = DB.prep(itemUserId);
		  Iterator iterator = itemUserUpdatedColumns.keySet().iterator();
		  String  keyColumn;
	      String valueColumn;
		  while(iterator.hasNext()) 
		  {
			  keyColumn=(String)iterator.next();
			  valueColumn = DB.prep(itemUserUpdatedColumns.get(keyColumn));
			  
			  updateItemUserHash.put(keyColumn, valueColumn);
		  }
		
		
		 
		   return DB.update("item_user", updateItemUserHash, "item_user_id", 
		    new String []{id}); 
	}else
		return 0;
	}
	public int deleteAllItemUserRecords(ArrayList<ItemUserProperties> records)
	{
		int deleted = 0;
		
		if (records!=null && records.size()>0)
		{
			for (ItemUserProperties record:records)
			{
				if (record!=null)
				{
					deleted += deleteItemUserByItemId(record.itemId);
				}
					
			}
		}
		
		return deleted;
	}
	private ItemUserProperties fromHashMapToEventProperties(HashMap<String, String> itemUser) {
		
		primaryWebEventId = (String) itemUser.get("primary_web_event_id");
		itemId = Integer.parseInt((String) itemUser.get("item_id"));
		webItemId = (String) itemUser.get("web_item_id");
		webItemUserId = (String) itemUser.get("web_item_user_id");
		itemUserId =Integer.parseInt((String) itemUser.get("item_user_id"));
		itemType  = (String) itemUser.get("item_type");
		atlasId = (String) itemUser.get("atlas_id");
		displayOrder =Integer.parseInt((String) itemUser.get("display_order"));
		ITEM_USER_TASK_STATUS eventStatus = ((String) itemUser.get("status"))!=null?
				Integer.parseInt((String) itemUser.get("status"))==0? ITEM_USER_TASK_STATUS.SENT:
				Integer.parseInt((String) itemUser.get("status"))==1? ITEM_USER_TASK_STATUS.ACCEPTED:
				Integer.parseInt((String) itemUser.get("status"))==2? ITEM_USER_TASK_STATUS.DECLINED:
				Integer.parseInt((String) itemUser.get("status"))==3? ITEM_USER_TASK_STATUS.COMPLITED:
					ITEM_USER_TASK_STATUS.SENT: ITEM_USER_TASK_STATUS.SENT;
		status = eventStatus;
		wasReceived = ((String) itemUser.get("was_received")!=null)?
					(Integer.parseInt((String) itemUser.get("was_received"))==0)? false:true:false;
		statusDateTime = toDate( (String) itemUser.get("status_datetime"));
		receivedDateTime = toDate( (String) itemUser.get("received_datetime"));
		ACTION eventAction =( (String) itemUser.get("action")!=null)?
				(((String) itemUser.get("action")).equals("del"))? ACTION.DELETE:
					ACTION.SAVE:ACTION.SAVE;

		action = eventAction;
		modifiedDatetime = toDate( (String) itemUser.get("dmodified_datetime"));
		ITEM_TYPE_PRIORITY_ORDER priorityOrder =( (String) itemUser.get("priority_order")!=null)?
				(Integer.parseInt((String) itemUser.get("priority_order"))==0) ? ITEM_TYPE_PRIORITY_ORDER.IDEAL:
					(Integer.parseInt((String) itemUser.get("priority_order"))==1) ? ITEM_TYPE_PRIORITY_ORDER.OKAY:
						ITEM_TYPE_PRIORITY_ORDER.IDEAL:ITEM_TYPE_PRIORITY_ORDER.IDEAL;
		priority = priorityOrder;
		itemUserProperties = toItemUserPropertiesFromLatestProperties();
		return itemUserProperties;

	}
	private ItemUserProperties toItemUserPropertiesFromLatestProperties() {
		itemUserProperties = new ItemUserProperties();
		
		itemUserProperties.itemId = itemId ;
		itemUserProperties.webItemId = webItemId;
		itemUserProperties.objectId = webItemUserId;
		itemUserProperties.itemUserId =itemUserId;
		itemUserProperties.itemType  = (itemType!=null && !itemType.equals(""))?
										(itemType.equals("event"))? ITEM_TYPE.EVENT:
											(itemType.equals("task"))? ITEM_TYPE.TASK:
												ITEM_TYPE.TASK:ITEM_TYPE.TASK	;
		itemUserProperties.atlasId = atlasId;
		itemUserProperties.displayOrder =displayOrder;
		itemUserProperties.status = status;
		
		itemUserProperties.statusDateTime = statusDateTime;
		itemUserProperties.receivedDateTime = receivedDateTime;
		itemUserProperties.action = action;
		itemUserProperties.modifiedDatetime = modifiedDatetime;
		itemUserProperties.priorityOrder = priority;
		itemUserProperties.wasReceived = wasReceived;
		itemUserProperties.primaryWebEventId = primaryWebEventId;
		
		itemUserProperties.setItemUser(itemUserProperties.webItemUserId,"", null,
				itemUserProperties.itemType, itemUserProperties.webItemId, 
				itemUserProperties.atlasId, itemUserProperties.status,
				itemUserProperties.statusDateTime, 
				itemUserProperties.receivedDateTime, 
				itemUserProperties.priorityOrder, 
				itemUserProperties.displayOrder, 
				itemUserProperties.wasReceived);
		
		
		
		return itemUserProperties;
	}

	private Date toDate(String startTime)
	{
		Date date = null;
		//String startTime = "2011-09-05 15:00:23";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		if (startTime!=null && !startTime.equals(""))
			try {
				date =  dateFormat.parse(startTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return date;
		 
	}
	/**
	 * Gets the itemId and retrieve all the item_user records associated
	 * @param itemId - id given from local DB to the event or task associated
	 * @return
	 */
	public static ArrayList<ItemUserProperties> getAllItemUserRecordsByItemId(String itemId)
	{
		ArrayList<ItemUserProperties> allRecords = new ArrayList<ItemUserProperties>();
		ArrayList<HashMap<String,String>> itemsH = DB.q("SELECT * FROM item_user WHERE item_id='" + itemId);
	
		if (itemId!=null && !itemId.equals("") &&
				itemsH!=null && itemsH.size()>0	)
		{
			ATLItemUserModel itemUser ;
			ItemUserProperties itemUserProperties;
			for (HashMap<String,String> rec:itemsH)
			{
				itemUser = new ATLItemUserModel();
				if (rec!=null)
				{
					itemUserProperties = itemUser.fromHashMapToEventProperties(rec);
					allRecords.add(itemUserProperties);
				}
			}
		}
		
		
		return allRecords;
		
		
	}
	public static ArrayList<ItemUserProperties> getAllItemUserRecordsByPrimaryWebEventId(String primaryWebEventId)
	{
		ArrayList<ItemUserProperties> allRecords = new ArrayList<ItemUserProperties>();
		if (primaryWebEventId!=null && !primaryWebEventId.equals(""))
		{
		ArrayList<HashMap<String,String>> itemsH = DB.q("SELECT * FROM item_user WHERE primary_web_event_id='" + primaryWebEventId);
	
		if (
				itemsH!=null && itemsH.size()>0	)
		{
			ATLItemUserModel itemUser ;
			ItemUserProperties itemUserProperties;
			for (HashMap<String,String> rec:itemsH)
			{
				itemUser = new ATLItemUserModel();
				if (rec!=null)
				{
					itemUserProperties = itemUser.fromHashMapToEventProperties(rec);
					allRecords.add(itemUserProperties);
				}
			}
		}
		}
		
		return allRecords;
		
		
	}
	public static ArrayList<ItemUserProperties> getAllItemUserRecordsByWebItemId(String webItemId)
	{
		ArrayList<ItemUserProperties> allRecords = new ArrayList<ItemUserProperties>();
	
		if (webItemId!=null && !webItemId.equals(""))
		{
			ArrayList<HashMap<String,String>> itemsH = DB.q("SELECT * FROM item_user WHERE web_item_id='" + webItemId);
			if(	itemsH!=null && itemsH.size()>0	)
			{
				ATLItemUserModel itemUser ;
				ItemUserProperties itemUserProperties;
				for (HashMap<String,String> rec:itemsH)
				{
					itemUser = new ATLItemUserModel();
					if (rec!=null)
					{
						itemUserProperties = itemUser.fromHashMapToEventProperties(rec);
						allRecords.add(itemUserProperties);
					}
				}
			}
		}
		
		return allRecords;
	}
	/**
	 * Gets an ArrayList of all the web_item_id's(either event or task object id's)
	 * and return all the item_user associated with these items id's
	 * from the local DB
	 * @param webEventIds - the web event/task id (as in Parse) 
	 * @return ArrayList of all the item_user associated with these items id's 
	 */
	public static ArrayList<ItemUserProperties> getAllItemUserRecordsByWebItemIds(
			ArrayList<String> webEventIds) {
		ArrayList<ItemUserProperties> allEventRecords = new ArrayList<ItemUserProperties>();

		if (webEventIds!=null && webEventIds.size()>0)
		{
			String sql = "select * from item_user where web_item_id = '" + webEventIds.get(0) + "' ";
			for  (String id:webEventIds)
			{
			    if (id!=null && !id.equals(""))
			    	sql +="OR web_item_id = '" + id + "' ";
			    
			}
			ATLItemUserModel itemUser;
			ArrayList<HashMap<String,String>> itemsH = DB.q(sql);
			for (HashMap<String,String> rec:itemsH)
			{
				 itemUser = new ATLItemUserModel();
				if (rec!=null)
				{
					itemUser.itemUserProperties = itemUser.fromHashMapToEventProperties(rec);
					allEventRecords.add(itemUser.itemUserProperties);
				}
			}
		}
		
		return allEventRecords;
	}
	
	
	
	
	
	/**
	 * Gets the current web_item__user_id's ,of the items type 
	 * event as on Parse database 
	 * @param allItemUserRec - web_item_id's where item_type=='event'
	 * @return - the list of item_user objectId's,type 'event'
	 * 			that are on local Database but not
	 * 			on Parse 
	 */
	public static ArrayList<String> getEventsWebIdsRecordsThatAreNotOnParse(
			ArrayList<ItemUserProperties> allItemUserRec) {
		ArrayList<String> eventsIdsRecordsThatAreNotOnParse = null;
		
		if (allItemUserRec!=null && allItemUserRec.size()>0)
		{
			ArrayList<String> allWebItemUserRec = getListOfWebIds(allItemUserRec);
			String sql = "SELECT web_item_user_id FROM item_user WHERE item_type='event'";
			eventsIdsRecordsThatAreNotOnParse = getAllRecordsBySQL(sql);
			if (eventsIdsRecordsThatAreNotOnParse!=null && eventsIdsRecordsThatAreNotOnParse.size()>0)
				eventsIdsRecordsThatAreNotOnParse.removeAll(allWebItemUserRec);
		}
		
		
		
		return eventsIdsRecordsThatAreNotOnParse;
	}
	/**
	 * 
	 * @param allItemUserRec
	 * @return A list containing all the item_user
	 * 			object Id's as on Parse
	 */
	private static ArrayList<String> getListOfWebIds(
			ArrayList<ItemUserProperties> allItemUserRec) {
		ArrayList<String> list = new ArrayList<String>();
		
		if (allItemUserRec!=null && allItemUserRec.size()>0)
		{
			for (ItemUserProperties itemUser:allItemUserRec)
			{
				if (itemUser!=null)
				{
					if (itemUser.objectId!=null && !itemUser.objectId.equals(""))
						list.add(itemUser.objectId);
				}
			}
		}
		
		
		return list;
	}

	private static ArrayList<String> getAllRecordsBySQL(String sql)
	{
		
		ArrayList<String> allEventRecords =null;
		if (sql!=null && !sql.equals(""))
		{
			allEventRecords = DB.queryOneParam(sql);
			
		
		}
		return allEventRecords;
	}
	
	
	public static ArrayList<ATLItemUserModel> getAllEventItemRecords()
	{
		ArrayList<ATLItemUserModel> allEventRecords = new ArrayList<ATLItemUserModel>();
		ATLItemUserModel itemUser;
		ArrayList<HashMap<String,String>> itemsH = DB.q("SELECT * FROM item_user WHERE item_type='event'");
		for (HashMap<String,String> rec:itemsH)
		{
			 itemUser = new ATLItemUserModel();
			if (rec!=null)
			{
				itemUser.itemUserProperties = itemUser.fromHashMapToEventProperties(rec);
				allEventRecords.add(itemUser);
			}
		}
		return allEventRecords;
	}
	/**
	 * Updating all the item_user records on local table
	 * and writes the new ones
	 * 
	 * @param allItemUserRec  - item_user records to update/write 
	 * 							to local table ,type ItemUserProperties
	 */
	public static void updateAllItemUserRecordsLocally(ITEM_TYPE itemType,
			ArrayList<ItemUserProperties> allItemUserRec) {

		if (allItemUserRec!=null && allItemUserRec.size()>0 && itemType!=null)
		{
			ArrayList<String> allWebItemUserIds = getAllCurrentWebItemUserIdsOnLocal(itemType);
			allWebItemUserIds = (allWebItemUserIds!=null)? allWebItemUserIds: new ArrayList<String>();
			for (ItemUserProperties itemUserProperties:allItemUserRec)
			{
				if (allWebItemUserIds.contains(itemUserProperties.webItemUserId))
				{
					String sql = "update  item_user SET " 
							+ "status ='"+ DB.prep(itemUserProperties.status.ordinal()) + "', "
							+ "display_order ='"+DB.prep(itemUserProperties.displayOrder) + "'," 
					
							+ "status_datetime ='"+ DB.prep(itemUserProperties.statusDateTime.toString()) + "'," 
							+ "was_received ='"+ DB.prep(itemUserProperties.wasReceived) + "'," 
							+ "received_datetime ='"+ DB.prep(itemUserProperties.receivedDateTime.toString()) + "'," 
							+ "priority_order ='"+ DB.prep(itemUserProperties.priorityOrder.ordinal()) + "',"
							+ "action ='" + DB.prep(itemUserProperties.action.getActionName()) + "' "
							+ " WHERE web_item_user_id = '"+itemUserProperties.webItemUserId+ "' ";  
					DB.q(sql);
				}
				else
				{
					// new item_user to write
					write(itemUserProperties);
				}
			}
			
		}
	}

	
	/**
	 * 
	 * @param itemType - 'event' or 'task'
	 * @return the current list of web_item_user_id (objectId on Parse)
	 * 			of the item_user
	 */
	private static ArrayList<String> getAllCurrentWebItemUserIdsOnLocal(ITEM_TYPE itemType) {
			ArrayList<String> eventsIdsRecordsOnLocal = null;
			if (itemType!=null)
			{
				String sql = "SELECT web_item_user_id FROM item_user WHERE item_type='"+itemType.getItemName() +"'";
				eventsIdsRecordsOnLocal = getAllRecordsBySQL(sql);
			}
		return eventsIdsRecordsOnLocal;
	}

	@Override
	public void writeCallBack(boolean success) {
		this.success = success;
	}

	


}
