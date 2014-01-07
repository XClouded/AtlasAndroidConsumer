package atlasapp.common;

import java.util.Date;

import atlasapp.common.ATLConstants.EventResponseType;
import atlasapp.database.DatabaseConstants.ITEM_TYPE;

public class ATLAlarmCellModelProperties 
{
	// primary key
	public int alramId = -1;
	public ITEM_TYPE itemType=ITEM_TYPE.TASK;
	// id of the item (task/event) as on the 
	// local & Parse DB on the item_user table
	public String webItemId="";
	public String title="";
	/// 
	public Date itemDatetime = new Date();
	public Date alarmDatetime = new Date();
	public int minutes= 0;
	public int sortOrder=0;
	public Date modifiedDatetime = new Date();
	public EventResponseType respondStatus= EventResponseType.None;
	public String calendarId="-1";
	public String atlasId="";
	
	

	public ATLAlarmCellModelProperties()
	{}
	public static ATLAlarmCellModelProperties deNull(
			ATLAlarmCellModelProperties alarmProperties) {
		
		ATLAlarmCellModelProperties deNulledAlarmProp = new ATLAlarmCellModelProperties();
		if (alarmProperties==null)
			alarmProperties = deNulledAlarmProp;
		deNulledAlarmProp.itemType = (alarmProperties.itemType==null)? ITEM_TYPE.TASK:alarmProperties.itemType;
		deNulledAlarmProp.webItemId = (alarmProperties.webItemId==null)? "" :alarmProperties.webItemId;
		deNulledAlarmProp.title = (alarmProperties.title==null)? "" :alarmProperties.title;
		deNulledAlarmProp.itemDatetime =  (alarmProperties.itemDatetime==null)? new Date() :alarmProperties.itemDatetime;
		deNulledAlarmProp.alarmDatetime = (alarmProperties.alarmDatetime==null)? new Date() :alarmProperties.alarmDatetime;
		deNulledAlarmProp.minutes = (alarmProperties.minutes<0)? 0 :alarmProperties.minutes;
		deNulledAlarmProp.sortOrder = (alarmProperties.sortOrder<0)? 0 :alarmProperties.sortOrder;
		deNulledAlarmProp.modifiedDatetime = (alarmProperties.modifiedDatetime==null)? new Date() :alarmProperties.modifiedDatetime;
		deNulledAlarmProp.respondStatus = (alarmProperties.respondStatus==null)? EventResponseType.None :alarmProperties.respondStatus;
		deNulledAlarmProp.calendarId = (alarmProperties.calendarId==null)? "-1" :alarmProperties.calendarId;
		
		deNulledAlarmProp.atlasId = (alarmProperties.atlasId==null)? "" :alarmProperties.atlasId;
		
		
		
		return deNulledAlarmProp;
	}
}
