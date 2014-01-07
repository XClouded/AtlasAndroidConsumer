package atlasapp.database;

import java.util.ArrayList;
import java.util.HashMap;

public interface ATLAlertControllerCallBackInterface 
{
	/**
	 * Return an hash map where key = primary_web_event_id, and value= item user property record
	 * of all accepted records
	 * @param acceptedRecords
	 */
	void getBookedAcceptedAlertCallBack(HashMap<String, ItemUserProperties> acceptedRecords);
	/**
	 * Return an hash map where key = primary_web_event_id, and value= item user property record
	 * of all declined records
	 * @param declinedRecords
	 */
	void getBookedDeclinedAlertCallBack(HashMap<String, ItemUserProperties> declinedBookedItemUserRecords);

	
	void getPendingAlertCallBack(HashMap<String, ItemUserProperties> pendingItemUserRecords);
	
	
	
	
	void getYourMoveInBackground(
			HashMap<String, ArrayList<EventProperties>> yourMoveHash, ArrayList<String> webEventIdsFound, HashMap<String, String> webEventIdByPrimry, HashMap<String, ArrayList<ItemUserProperties>> itemUserRecords);

	void getBookedInBackground(
			HashMap<String, ArrayList<EventProperties>> yourCompletedHash, ArrayList<String> webEventIdsFound, HashMap<String, String> webEventIdByPrimry, HashMap<String, ArrayList<ItemUserProperties>> itemUserRecords);

	void getPendingInBackground(
			HashMap<String, ArrayList<EventProperties>> pendingHash, ArrayList<String> webEventIdsFound, HashMap<String, String> webEventIdByPrimry, HashMap<String, ArrayList<ItemUserProperties>> itemUserRecords);

	
	
	void getYourMoveAlertCallBack(HashMap<String, ArrayList<ItemUserProperties>> userMoveRecordsByPrimaryEventId);
	void refreshAlertsInBackgroundCallBack(boolean success);
//	void addToYourMoveInBackground(
//			HashMap<String, ArrayList<EventProperties>> newEventsByPrimary,
//			ArrayList<String> webItemIds,
//			HashMap<String, String> webEventIdByPrimry,
//			HashMap<String, ArrayList<ItemUserProperties>> itemUserRecords);
//	
	void addToYourMoveInBackground(
			HashMap<String, ArrayList<EventProperties>> newEventsByPrimary,
			ArrayList<String> webItemIds,
			HashMap<String, String> webEventIdByPrimry,
			HashMap<String, ArrayList<ItemUserProperties>> itemUserRecords);
}
