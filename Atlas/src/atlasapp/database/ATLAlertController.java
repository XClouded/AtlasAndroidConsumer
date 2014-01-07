package atlasapp.database;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import atlasapp.database.DatabaseConstants.ITEM_TYPE;
import atlasapp.model.ATLEventCalendarModel;
import atlasapp.model.ATLEventCalendarModelCallBackInterface;
import atlasapp.model.ATLItemUserModel;
import atlasapp.model.ATLItemUserModelCallBackInterface;

/**
 * 
 * @author sharonnachum
 *
 */
public class ATLAlertController implements ATLAlertControllerInterface, EventControllerCallBackInterface,ATLEventCalendarModelCallBackInterface, ATLItemUserModelCallBackInterface
{
	
	
	public static EventControllerCallBackInterface eventControllerCallBackListener;
	private ArrayList<String> pendingWebItemIdsOnLocalDB;
	private HashMap<String,String> pendingWebItemIdsOnLocalDBHashByPrimary;
	
	ATLAlertControllerCallBackInterface listener;
	/// all current web_event_id (key) and primary (value) 
	/// found on local 'event' table
	private HashMap<String,String> currentWebEventsIdsOnLocal;
	
	//// all current events on local DB found 
	/// key - primary and array list of the corresponding 
	/// web_event_ids
	private HashMap<String,ArrayList<String>> currentEvnetsIdsByPrimary;
	
	private boolean eventUpdated;
	private boolean itemUserUpdated;
	
	public ATLAlertController(ATLAlertControllerCallBackInterface listener)
	{
		this.listener = listener;
		eventControllerCallBackListener = this;
	}
	
	@Override  
	public void getNewEventReceived(
			HashMap<String, ArrayList<EventProperties>> newEventsByPrimary,
			ArrayList<String> webItemIds, 
			HashMap<String, String> webEventIdByPrimry)
	{
//		HashMap<String,ArrayList<ItemUserProperties>> itemUserRecords = 
//				ATLItemUserModel.getAllItemUserRecordsByWebItemIds(webItemIds, webEventIdByPrimry);
//		if (listener!=null)
//			listener.addToYourMoveInBackground(newEventsByPrimary, webItemIds, webEventIdByPrimry,itemUserRecords);
//		
//		
		
		
		HashMap<String,ArrayList<ItemUserProperties>> itemUserRecords = 
				ATLItemUserModel.getAllItemUserRecordsByWebItemIds(webItemIds, webEventIdByPrimry);
		
		
		if (listener!=null)
//			listener.getYourMoveAlertCallBack(newEventsByPrimary, 
//					webItemIds, webEventIdByPrimry
//									,itemUserRecords);
//		ATLEventCalendarModel.getYourMoveInBackground();
		listener.getYourMoveAlertCallBack(itemUserRecords);

		
	}
	/**  
	 * Refreshing the current event and item_user local tables from Parse
	 * call back from refreshAlertsInBackgroundCallBack
	
	/**
	 * Call back when new event updated...
	 */
	@Override
		public void refreshAllAlerts(Context context) 
		{
//			pendingWebItemIdsOnLocalDB = 
//					ATLItemUserModel.getAllCurrentWebItemIdsOnLocal(ITEM_TYPE.EVENT);
//			
//			pendingWebItemIdsOnLocalDBHashByPrimary = ATLEventCalendarModel.getPrimaryIdsByWebEventIds();
//			
//			getBookedAlert() ;
//			getPendingAlert() ;
//			getYourMove() ;
//			
			
		
		
		ATLEventCalendarModel.eventCalendarModelListener = this;
		ATLItemUserModel.atlItemUserModelListener = this;
		EventController eventController = new EventController();
		
		eventController.eventControllerCallBackListener = eventControllerCallBackListener;
		ATLEventCalendarModel.getPendingInBackground();
		ATLEventCalendarModel.getYourMoveInBackground();
		ATLEventCalendarModel.getBookedInBackground();
		eventController.RefreshAllEventRecordsFromParseInBackground(context);
//		eventController.GetNewEvents();


		}

	
	@Override
	public void eventCalendarUpdatedCallBack(boolean success) 
	{
	//	if (success)
		//{
			//EventController eventController = new EventController();
			//eventController.eventControllerCallBackListener = this;
			//eventController.refreshItemUserInBackground(this);
		
	//	}
//		else
	//		refreshAlertsInBackgroundCallBack(false);
		
		
	}
	@Override
	public void  updatedItemUserOnLocalDB (boolean success) 
	{

		 refreshAlertsInBackgroundCallBack(success);
	}
	@Override
	public synchronized void  refreshAlertsInBackgroundCallBack(boolean success)
	{
		
		if (success)
		{
			ATLEventCalendarModel.getPendingInBackground();
			ATLEventCalendarModel.getYourMoveInBackground();
			ATLEventCalendarModel.getBookedInBackground();
			// sort refreshed event from local DB to
			// Booking,Pending & Your moves lists
			
			
		}else
		
		
		if (listener!=null)
			listener.refreshAlertsInBackgroundCallBack(success);
	}
	
	@Override
	public void getYourMoveInBackground(
			HashMap<String, ArrayList<EventProperties>> yourMoveHash,
			ArrayList<String> webEventIdsFound,
			HashMap<String, String> webEventIdByPrimry) 
	{

		HashMap<String,ArrayList<ItemUserProperties>> itemUserRecords = 
				ATLItemUserModel.getAllItemUserRecordsByWebItemIds(webEventIdsFound, webEventIdByPrimry);
		if (listener!=null)
			listener.getYourMoveInBackground(
					yourMoveHash, webEventIdsFound, 
					webEventIdByPrimry,itemUserRecords);
	}
	@Override
	public void getBookedInBackground(
			HashMap<String, ArrayList<EventProperties>> yourCompletedHash,
			ArrayList<String> webEventIdsFound,
			HashMap<String, String> webEventIdByPrimry)
	{
		HashMap<String,ArrayList<ItemUserProperties>> itemUserRecords = 
				ATLItemUserModel.getAllItemUserRecordsByWebItemIds(webEventIdsFound, webEventIdByPrimry);
		
		
		if (listener!=null)
			listener.getBookedInBackground(yourCompletedHash, 
					webEventIdsFound, webEventIdByPrimry
									,itemUserRecords);
//		EventController.GetNewEvents();
	}
	@Override
	public void getPendingInBackground(
			HashMap<String, ArrayList<EventProperties>> pendingHash,
			ArrayList<String> webEventIdsFound,
			HashMap<String, String> webEventIdByPrimry) 
	{
		HashMap<String,ArrayList<ItemUserProperties>> itemUserRecords = 
				ATLItemUserModel.getAllItemUserRecordsByWebItemIds(webEventIdsFound, webEventIdByPrimry);
		if (listener!=null)
			listener.getPendingInBackground(pendingHash, webEventIdsFound, webEventIdByPrimry,itemUserRecords);
		
	}
	
	@Override
	public void getBookedAlert() {
		ArrayList<String> pendingWebItemIdsOnLocalDB = 
				ATLItemUserModel.getAllCurrentWebItemIdsOnLocal(ITEM_TYPE.EVENT);
		
		HashMap<String,String> pendingWebItemIdsOnLocalDBHashByPrimary = ATLEventCalendarModel.getPrimaryIdsByWebEventIds();
		if (pendingWebItemIdsOnLocalDB!=null && pendingWebItemIdsOnLocalDBHashByPrimary!=null )
		{
			AtlasServerConnect.searchForPendingItemUserRecords(pendingWebItemIdsOnLocalDBHashByPrimary,pendingWebItemIdsOnLocalDB,listener);
		}
		else
		{
			listener.getBookedAcceptedAlertCallBack(null);
			listener.getBookedDeclinedAlertCallBack(null);
		}
	}

	@Override
	public void getPendingAlert() 
	{
		
		
	}
	@Override
	public void getYourMove() 
	{
		EventController eventController = new EventController();
		eventController.eventControllerCallBackListener = this;
		eventController.getUserMoveFromParse();
//		
	}
	
	
	@Override
	public void getUserMoveFromParseCallBack(
			boolean success,
			HashMap<String, ArrayList<ItemUserProperties>> userMoveRecordsByPrimaryEventId) {
		listener.getYourMoveAlertCallBack(userMoveRecordsByPrimaryEventId);
		
	}
	@Override
	public void createCalendarEventCallBack(boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getAllUserAnEventCallBack(
			HashMap<String, ArrayList<EventProperties>> userEvents,
			HashMap<String, ArrayList<String>> eventsMembers, boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getAllUserEventsCallBack(
			HashMap<String, ArrayList<EventProperties>> allEventsRetrievedFromItemUser,
			ArrayList<ItemUserProperties> allUserItemUsersPropertiesRefreshedList,
			boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getEventByPrimaryWebEventIdCallBack(boolean success,
			ArrayList<EventProperties> event,
			ArrayList<ItemUserProperties> invitees) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void respondToEventInviteCallBack(boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void bookEventCallBack(boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void savedItemUserOnLocalDB(boolean success,
			ArrayList<ItemUserProperties> itemUser) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void writeItemUserOnLocalDB(boolean success)
	{
		if (success)
		{
			
		}
	}
	
	
	
	
	
	
	









	

}
