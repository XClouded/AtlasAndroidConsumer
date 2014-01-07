
package com.atlasapp.section_calendar;

import com.atlasapp.common.ATLConstants.EventResponseType;


public class ATLEventGroupModel {

	public ATLEventGroupModel() {
		// TODO Auto-generated constructor stub
		calCellGroupEventID = "";
		calCellEventIdentifier = "";
		calCellAlt2EventIdentifier = "";
		calCellAlt3EventIdentifier = "";
		eventRespondStatus = EventResponseType.eventResponseType_None;
	}
	
	public String calCellGroupEventID; 
	public String calCellEventIdentifier; 
	public String calCellAlt2EventIdentifier; 
	public String calCellAlt3EventIdentifier; 
	public int eventRespondStatus;

	public void loadCalendarModel(ATLCalCellData cellData) {
		calCellGroupEventID = new String(cellData.getCalCellEventIdentifier());
		calCellEventIdentifier = new String(cellData.getCalCellEventIdentifier());
		calCellAlt2EventIdentifier = new String(cellData.getCalCellAlt2EventIdentifier());
		calCellAlt3EventIdentifier = new String(cellData.getCalCellAlt3EventIdentifier());
	}
}
