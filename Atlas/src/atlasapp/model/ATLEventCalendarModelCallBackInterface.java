package atlasapp.model;

import java.util.ArrayList;
import java.util.HashMap;

import atlasapp.database.EventProperties;

public interface ATLEventCalendarModelCallBackInterface {
	
	
	void eventCalendarUpdatedCallBack(boolean success);

	void getYourMoveInBackground(
			HashMap<String, ArrayList<EventProperties>> yourMoveHash, ArrayList<String> webEventIdsFound, HashMap<String, String> webEventIdByPrimry);

	void getBookedInBackground(
			HashMap<String, ArrayList<EventProperties>> yourCompletedHash, ArrayList<String> webEventIdsFound, HashMap<String, String> webEventIdByPrimry);

	void getPendingInBackground(
			HashMap<String, ArrayList<EventProperties>> pendingHash, ArrayList<String> webEventIdsFound, HashMap<String, String> webEventIdByPrimry);

}
