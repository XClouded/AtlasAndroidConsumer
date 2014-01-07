/**
 * 
 */
package atlasapp.section_alerts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import atlasapp.common.DateTimeUtilities;
import atlasapp.database.DatabaseConstants.ALERT_STATUS;
import atlasapp.database.DatabaseConstants.EVENT_STATUS;
import atlasapp.database.DatabaseConstants.EVENT_TYPE;
import atlasapp.database.EventProperties;
import atlasapp.model.ATLAlertModel;
import atlasapp.model.ATLContactModel;


/**
 * @author nghia
 * 
 */
public class ATLAlertCellList_2 {

	// PROPERTIES
	public ArrayList<ATLAlert> alertList;

	public ArrayList<ATLAlert> yourmoveList;
	public ArrayList<ATLAlert> pendingList;
	public ArrayList<ATLAlert> bookingList;
	public ArrayList<ATLAlert> bookedList;
	public ArrayList<ATLAlert> declinedList;
	
	public ArrayList<ATLAlert> allYourMoveList;
	
	public ArrayList<ATLAlert> newYourMoveList;

	public int alertListCount;
	public Date alertsListDate;
	public boolean alertListSimulate;

	boolean show_Displayed = false;
	boolean show_Read = false;
	boolean show_Handled = false;
	boolean show_All = false;

	ALERT_STATUS type = ALERT_STATUS.YOURMOVE;

	// METHODS

	
	
	
	private static ATLAlertCellList_2 atlAlertCellList;
	
	
	public static ATLAlertCellList_2 getInstance()
	{
		if (atlAlertCellList==null)
		{
			atlAlertCellList = new ATLAlertCellList_2();
		}
		atlAlertCellList.alertsListDate = new Date();
		return atlAlertCellList;
	}
	
	private ATLAlertCellList_2() {
		// TODO Auto-generated constructor stub
		// SIMULATION: TURN OFF WHEN REAL DATA IS AVAILABLE
		// ==================================<<<<<<
		// WHEN ON, FAKE CELL DATA WILL BE CREATED
		this.alertListSimulate = false;
		this.alertList = new ArrayList<ATLAlert>();
		this.alertsListDate = new Date();
		
		
		yourmoveList = new ArrayList<ATLAlert>();
		pendingList = new ArrayList<ATLAlert>();
		bookingList = new ArrayList<ATLAlert>();
		bookedList = new ArrayList<ATLAlert>();
		declinedList = new ArrayList<ATLAlert>();
		newYourMoveList = new ArrayList<ATLAlert>();
		allYourMoveList= new ArrayList<ATLAlert>();
	}
	
	private ATLAlertCellList_2(ArrayList<ATLAlert> alertList) {
		// TODO Auto-generated constructor stub
		// SIMULATION: TURN OFF WHEN REAL DATA IS AVAILABLE
		// ==================================<<<<<<
		// WHEN ON, FAKE CELL DATA WILL BE CREATED
		this.alertList = alertList;

		yourmoveList = new ArrayList<ATLAlert>();
		pendingList = new ArrayList<ATLAlert>();
		bookingList = new ArrayList<ATLAlert>();
		bookedList = new ArrayList<ATLAlert>();
		declinedList = new ArrayList<ATLAlert>();
		newYourMoveList = new ArrayList<ATLAlert>();
		allYourMoveList= new ArrayList<ATLAlert>();
		loadAlertList();
		alertListCount = alertList.size();
		
		if (type == ALERT_STATUS.YOURMOVE) {
			alertListCount = allYourMoveList.size();
		} else if (type == ALERT_STATUS.PENDING) {
			alertListCount = pendingList.size();
		} else if (type == ALERT_STATUS.BOOKED) {
			alertListCount = bookedList.size();
		}

		this.alertListSimulate = false;
		this.alertsListDate = new Date();

	}
	public void setAllYouMoveList(ArrayList<ATLAlert> list) {
		if (list!=null )
			allYourMoveList = list;
		
	}
	public void setNewYouMoveList(ArrayList<ATLAlert> list) {
		if (list!=null )
			newYourMoveList = list;
		
	}
	public void setToDeclinedList(ArrayList<ATLAlert> list) {
		if (list!=null )
			declinedList = list;
		
	}
	public void setToPendingList(ArrayList<ATLAlert> list)
	{
		if (list!=null )
		{
			pendingList.clear();
			pendingList=list;
		}
	}
	public void setToBookedList(ArrayList<ATLAlert> list)
	{
		if (list!=null )
			bookedList=list;
	}
	public void setToYouMoveList(ArrayList<ATLAlert> list)
	{
		if (list!=null )
			yourmoveList=list;
	}
	public void addATLAlertCellList_2() {
		// TODO Auto-generated constructor stub
		// SIMULATION: TURN OFF WHEN REAL DATA IS AVAILABLE
		// ==================================<<<<<<
		// WHEN ON, FAKE CELL DATA WILL BE CREATED
//		this.alertList.addAll(alertListAdded);
//
//		yourmoveList = new ArrayList<ATLAlert>();
//		pendingList = new ArrayList<ATLAlert>();
//		bookingList = new ArrayList<ATLAlert>();

//		loadAlertList();
		alertListCount = alertList.size();
		
		if (type == ALERT_STATUS.YOURMOVE) {
			alertListCount = allYourMoveList.size();
		} else if (type == ALERT_STATUS.PENDING) {
			alertListCount = pendingList.size();
		} else if (type == ALERT_STATUS.BOOKED) {
			alertListCount = bookingList.size();
		}

		this.alertListSimulate = false;
		this.alertsListDate = new Date();

	}
	
	
	private void loadAlertList() {
		// TODO Auto-generated method stub
		for (ATLAlert alert : alertList) {
			if (alert.type == ALERT_STATUS.YOURMOVE) {
				allYourMoveList.add(alert);
			} else if (alert.type == ALERT_STATUS.PENDING) {
				pendingList.add(alert);
			} else if (alert.type == ALERT_STATUS.BOOKED) {
				bookedList.add(alert);
			}
		}
	}

	public boolean load() {
		// load data for one day
		if (this.alertListSimulate) {

//			initFakeDataWhenSimulate();

		} // END IF SIMULATE

		else {

			// ATLAlertDatabaseAdapter dbHelper = new ATLAlertDatabaseAdapter();
			// ArrayList<ATLAlertModel> list =
			// dbHelper.loadAllAlertInDatabase();
			//
			// for (ATLAlertModel alertModel : list) {
			// AlertCellData cellData = new AlertCellData(alertModel);
			// addCell(cellData);
			// }
		}
		// Collections.sort(alertListArray, new ATLAlertCellDataComparator());
		// Collections.sort(newAlertListArray, new
		// ATLAlertCellDataComparator());
		// Collections.sort(pastAlertListArray, new
		// ATLAlertCellDataComparator());
		return true;
	}

//	private void initFakeDataWhenSimulate() {
//		// TODO Auto-generated method stub
//		ATLAlert newCell1 = new ATLAlert();
//		this.alertList.add(newCell1);
//
//		newCell1.inviterContact = new ATLContactModel();
//		newCell1.inviterContact.setFirstname("Tan");
//		newCell1.inviterContact.setLastname("Nguyen");
//		newCell1.inviterContact.setAtlasId("izmlNOuqFn");
//
//		newCell1.contact = new ATLContactModel();
//		newCell1.contact.setFirstname("Nghia");
//		newCell1.contact.setLastname("Truong");
//		newCell1.contact.setAtlasId("I9aNtJzIqP");
//
//		ArrayList<EventProperties> eventList = new ArrayList<EventProperties>();
//		newCell1.eventList = eventList;
//
//		EventProperties eventPri = new EventProperties();
//		eventPri.objectId = "YzEBouhD5f";
//		eventPri.title = "morning iphone";
//		eventPri.displayOrder = 0;
//		eventPri.atlasId = "izmlNOuqFn";
//		eventPri.location = "nghia home";
//		eventPri.primaryWebEventId = "";
//		eventPri.eventType = EVENT_TYPE.LUNCH;
//		eventPri.startDateTime = new Date();
//		eventPri.modifiedDatetime = new Date();
//		eventPri.status = EVENT_STATUS.PENDING;
//		eventPri.webEventId = "069be48311f71e4d481fe9f622a241bb";
//		eventList.add(eventPri);
//
//		EventProperties event1 = new EventProperties();
//		event1.objectId = "imxia99ewT";
//		event1.title = "morning iphone";
//		event1.displayOrder = 1;
//		event1.atlasId = "izmlNOuqFn";
//		event1.location = "nghia home";
//		event1.primaryWebEventId = "069be48311f71e4d481fe9f622a241bb";
//		event1.eventType = EVENT_TYPE.LUNCH;
//		event1.startDateTime = new Date();
//		event1.modifiedDatetime = new Date();
//		event1.status = EVENT_STATUS.PENDING;
//		event1.webEventId = "5e217f1d5895a2b01f1abe6c6c2c5b00";
//		eventList.add(event1);
//
//		EventProperties event2 = new EventProperties();
//		event2.objectId = "upQ4ggZP8v";
//		event2.title = "morning iphone";
//		event2.displayOrder = 2;
//		event2.atlasId = "izmlNOuqFn";
//		event2.location = "nghia home";
//		event2.primaryWebEventId = "069be48311f71e4d481fe9f622a241bb";
//		event2.eventType = EVENT_TYPE.LUNCH;
//		event2.startDateTime = new Date();
//		event2.modifiedDatetime = new Date();
//		event2.status = EVENT_STATUS.PENDING;
//		event2.webEventId = "3e06a42f14e251f86bbcdd3768a7e879";
//		eventList.add(event2);
//
//		// ATLContactModel contact;
//		// ATLContactModel inviterContact;
//		// ArrayList<EventProperties> eventList;
//		// ArrayList<ItemUserProperties> itemUserList;
//		//
//		// String type;
//		// String sortValue;
//	}

	public boolean save() {
		return true;
	}

	public void clear() {
		this.alertList.clear();
	}

	// public void addCell(AlertCellData noteCellData) {
	// if (noteCellData.isHandled) {
	// this.pastAlertListArray.add(noteCellData);
	// } else {
	// this.newAlertListArray.add(noteCellData);
	// }
	// // this.eventList.add(noteCellData);
	// }
	//
	// public void removeCell(AlertCellData noteCellData) {
	// if (noteCellData.isHandled) {
	// this.pastAlertListArray.remove(noteCellData);
	// } else {
	// this.newAlertListArray.remove(noteCellData);
	// }
	// this.alertListArray.remove(alertListCount);
	// }

	public void currentDateDidChanged(Date currentDate) {
		// TODO Auto-generated method stub
		this.alertsListDate = currentDate;
		this.load();
	}

	public void addAlertCellDataToDatabase(AlertCellData cellData) {
		ATLAlertModel alertModel = new ATLAlertModel(cellData);

		ATLAlertDatabaseAdapter dbHelper = new ATLAlertDatabaseAdapter();
		boolean isExit = dbHelper.isExistInDatabase(alertModel);
		if (isExit) {
			dbHelper.updateATLAlertModel(alertModel);
		} else {
			dbHelper.insertATLAlertModel(alertModel);
		}

	}

	public class ATLAlertCellDataComparator implements
			Comparator<AlertCellData> {
		// NGHIA sort for newest to top.
		@Override
		public int compare(AlertCellData o1, AlertCellData o2) {
			return DateTimeUtilities.toString(o2.alertCellModifiedDate)
					.compareTo(
							DateTimeUtilities
									.toString(o1.alertCellModifiedDate));
		}
	}

	
}
