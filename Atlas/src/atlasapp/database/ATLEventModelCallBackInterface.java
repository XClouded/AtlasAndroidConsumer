package atlasapp.database;

import java.util.ArrayList;

import atlasapp.database.EventProperties;

public interface ATLEventModelCallBackInterface {
	
	/**
	 * Call back from setEventOnLocalDBEventTable on ATLEventModel
	 * 
	 * @param success if saved on local DB
	 * @param primaryEventLocalId if success return the primary event id on local DB
	 */
//	void savedEventOnLocalDB(boolean success, int primaryEventLocalId);

	void savedEventOnLocalDB(boolean success, ArrayList<EventProperties> event,
			EventProperties primaryEvent);
	
}
