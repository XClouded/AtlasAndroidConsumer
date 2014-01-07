/**
 * 
 */
package atlasapp.section_alerts;

import java.util.ArrayList;
import java.util.HashMap;

import atlasapp.common.ATLUser;
import atlasapp.common.CalendarUtilities;
import atlasapp.common.Utilities;
import atlasapp.database.ATLAlertControllerCallBackInterface;
import atlasapp.database.DatabaseConstants.ALERT_STATUS;
import atlasapp.database.DatabaseConstants.EVENT_STATUS;
import atlasapp.database.DatabaseConstants.ITEM_USER_TASK_STATUS;
import atlasapp.database.EventProperties;
import atlasapp.database.ItemUserProperties;
import atlasapp.model.ATLContactModel;
import atlasapp.model.ATLEventCalendarModel;



/**
 * 
 * 
 */
public class ATLAlert {
	
	ArrayList<ATLContactModel> invitees;
	
	ATLContactModel contact;
	ATLContactModel inviterContact;
	ArrayList<EventProperties> eventList;
	ArrayList<ItemUserProperties> itemUserList;
	
	
	

	ALERT_STATUS type;
	String sortValue;

	/**
	 * 
	 */

	public ATLAlert() {
		invitees = new ArrayList<ATLContactModel>();
		
		contact = new ATLContactModel();
		inviterContact = new ATLContactModel();
		eventList = new ArrayList<EventProperties>();
		itemUserList = new ArrayList<ItemUserProperties>();
	}

//	public void load() {
//		String primaryWebEventId = "";
//		for (EventProperties event : eventList) {
//			if (!event.primaryWebEventId.equals("")) {
//				primaryWebEventId = event.primaryWebEventId;
//				break;
//			}
//		}
//		eventList = ATLEventCalendarModel
//				.getEventByPrimaryWebEventId(primaryWebEventId);
//
//		boolean isBooked = false;
//		for (EventProperties event : eventList) {
//			if (event.status != EVENT_STATUS.PENDING) {
//				isBooked = true;
//				break;
//			}
//		}
//
//		if (isBooked) {
//			type = ALERT_STATUS.BOOKED;
//			bookEvent();
//		} else {
//			for (EventProperties event : eventList) {
//				String ownerId = event.atlasId;
//				if (ownerId.equals(ATLUser.getParseUserID())) {
//					type = ALERT_STATUS.PENDING;
//				} else {
//					type = ALERT_STATUS.YOURMOVE;
//				}
//				break;
//			}
//		}                            
//
//		boolean isOwner = false;
//		String ownerId = "";
//		for (EventProperties event : eventList) {
//			ownerId = event.atlasId;
//			if (ownerId.equals(ATLUser.getParseUserID())) {
//				isOwner = true;
//			}
//			break;
//		}
//		if (isOwner) {
//			boolean isDecline = true;
//			for (ItemUserProperties item : itemUserList) {
//				if (ownerId.equals(item.atlasId)) {
//					inviterContact = item.contact;
//				} else {
//					contact = item.contact;
//					if (item.status != ITEM_USER_TASK_STATUS.DECLINED) {
//						isDecline = false;
//					}
//				}
//			}
//			if (isDecline) {
//				for (EventProperties event : eventList) {
//					type = ALERT_STATUS.BOOKED;
//					CalendarUtilities.deleteGroupEventByDate(
//							event.startDateTime, Utilities.ctx);
//				}
//			}
//		} else {
//			for (ItemUserProperties item : itemUserList) {
//				if (ownerId.equals(item.atlasId)) {
//					contact = item.contact;
//				} else {
//					inviterContact = item.contact;
//
//				}
//			}
//
//		}
//	}

	public void bookEvent() {
		boolean isDecline = true;
		for (EventProperties event : eventList) {
			if (event.status == EVENT_STATUS.THE_ONE) {
				CalendarUtilities.acceptEventByDate(event.startDateTime,
						Utilities.ctx);
				isDecline = false;
				return;
			} else if (event.status == EVENT_STATUS.PENDING) {
				isDecline = false;
			}
		}
		if (isDecline) {
			for (EventProperties event : eventList) {
				CalendarUtilities.deleteGroupEventByDate(event.startDateTime,
						Utilities.ctx);
			}
		}
	}

  
	
	public void bookedEvents()
	{
		type = ALERT_STATUS.BOOKED;
//		inviterContact = itemUserList.get(0).contact;
		for (EventProperties event : eventList) {
			if (event.status.equals(EVENT_STATUS.THE_ONE))
				CalendarUtilities.acceptEventByDate(event.startDateTime,
						Utilities.ctx);
			else
				if (event.status.equals(EVENT_STATUS.NOT_THE_ONE))
					CalendarUtilities.deleteGroupEventByDate(
							event.startDateTime, Utilities.ctx);
				return;
			
		}
		
	}
	public void bookAcceptedEvents()
	{
		type = ALERT_STATUS.BOOKED;
//		inviterContact = itemUserList.get(0).contact;
		for (EventProperties event : eventList) {
				CalendarUtilities.acceptEventByDate(event.startDateTime,
						Utilities.ctx);
				return;
			
		}
		
	}

	public void bookDeclinedEvents() {
		type = ALERT_STATUS.BOOKED;
//		inviterContact = itemUserList.get(0).contact;
		for (EventProperties event : eventList) {
			CalendarUtilities.deleteGroupEventByDate(
					event.startDateTime, Utilities.ctx);
				return;
			
		}
		
	}
	public void youMoveAlerts()
	{
//		inviterContact = itemUserList.get(0).contact;
		type = ALERT_STATUS.YOURMOVE;
		
	}
	public void pendingEvents()
	{
//		inviterContact = itemUserList.get(0).contact;
		type = ALERT_STATUS.PENDING;
		
	}

}
