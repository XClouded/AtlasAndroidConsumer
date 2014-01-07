/**
 * 
 */
package com.atlasapp.section_alerts;

import java.util.ArrayList;

import com.atlasapp.atlas_database.DatabaseConstants.ALERT_STATUS;
import com.atlasapp.atlas_database.DatabaseConstants.ITEM_USER_TASK_STATUS;
import com.atlasapp.atlas_database.EventProperties;
import com.atlasapp.atlas_database.ItemUserProperties;
import com.atlasapp.atlas_database.DatabaseConstants.EVENT_STATUS;
import com.atlasapp.common.ATLEventCalendarModel;
import com.atlasapp.common.AtlasAndroidUser;
import com.atlasapp.common.CalendarUtilities;
import com.atlasapp.common.Utilities;
import com.atlasapp.model.ATLContactModel;

/**
 * @author nghia
 * 
 */
public class ATLAlert {
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
		// TODO Auto-generated constructor stub
		contact = new ATLContactModel();
		inviterContact = new ATLContactModel();
		eventList = new ArrayList<EventProperties>();
		itemUserList = new ArrayList<ItemUserProperties>();
	}

	public void load() {
		// TODO Auto-generated method stub
		String primaryWebEventId = "";
		for (EventProperties event : eventList) {
			if (!event.primaryWebEventId.equals("")) {
				primaryWebEventId = event.primaryWebEventId;
				break;
			}
		}
		eventList = ATLEventCalendarModel
				.getEventByPrimaryWebEventId(primaryWebEventId);

		boolean isBooked = false;
		for (EventProperties event : eventList) {
			if (event.status != EVENT_STATUS.PENDING) {
				isBooked = true;
				break;
			}
		}

		if (isBooked) {
			type = ALERT_STATUS.BOOKED;
			bookEvent();
		} else {
			for (EventProperties event : eventList) {
				String ownerId = event.atlasId;
				if (ownerId.equals(AtlasAndroidUser.getParseUserID())) {
					type = ALERT_STATUS.PENDING;
				} else {
					type = ALERT_STATUS.YOURMOVE;
				}
				break;
			}
		}

		boolean isOwner = false;
		String ownerId = "";
		for (EventProperties event : eventList) {
			ownerId = event.atlasId;
			if (ownerId.equals(AtlasAndroidUser.getParseUserID())) {
				isOwner = true;
			}
			break;
		}
		if (isOwner) {
			boolean isDecline = true;
			for (ItemUserProperties item : itemUserList) {
				if (ownerId.equals(item.atlasId)) {
					inviterContact = item.contact;
				} else {
					contact = item.contact;
					if (item.status != ITEM_USER_TASK_STATUS.DECLINED) {
						isDecline = false;
					}
				}
			}
			if (isDecline) {
				for (EventProperties event : eventList) {
					type = ALERT_STATUS.BOOKED;
					CalendarUtilities.deleteGroupEventByDate(
							event.startDateTime, Utilities.ctx);
				}
			}
		} else {
			for (ItemUserProperties item : itemUserList) {
				if (ownerId.equals(item.atlasId)) {
					inviterContact = item.contact;
				} else {
					contact = item.contact;
				}
			}
		}
		for (ItemUserProperties item : itemUserList) {
			if (ownerId.equals(item.atlasId)) {
				inviterContact = item.contact;
			} else {
				contact = item.contact;
			}
		}
	}

	private void bookEvent() {
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

}
