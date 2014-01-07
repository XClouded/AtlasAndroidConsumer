package atlasapp.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.util.Log;
import atlasapp.common.ATLConstants.EventResponseType;
import atlasapp.database.ItemUserProperties;
import atlasapp.database.DatabaseConstants.ITEM_TYPE;

/**
 * Defines the row in the alarm local table
 * @author sharon nachum
 *
 */
public class ATLAlarmCellModel
{
	public synchronized static int write(ATLAlarmCellModelProperties alarmProperties)
	{
		ArrayList<String> allWebEventIdFromAlarm  = getAllWebEventIdFromAlarm();
		if (allWebEventIdFromAlarm!=null && allWebEventIdFromAlarm.size()>0 && allWebEventIdFromAlarm.contains(alarmProperties.webItemId))
		{
			updateAlarmStatusByWebEventId(alarmProperties.webItemId,
					alarmProperties.respondStatus);
			return alarmProperties.alramId;
		}
		else
		{
			alarmProperties = ATLAlarmCellModelProperties.deNull(alarmProperties);

			String sql = "insert into alarm (" 
					+ "item_type,"
					+ "web_item_id,"
					+ "item_title,"
					+ "calendar_id,"
					+ "item_datetime,"
					+ "alarm_datetime,"
					+ "respond_status, "
					+ "minutes,"
					+ "sort_order,"
					+ "atlas_id,"
				+ "modified_datetime) values (" 
				+ "'" + DB.prep(alarmProperties.itemType.getItemName()) + "',"
				+ "'" + DB.prep(alarmProperties.webItemId) + "',"
				+ "'" + DB.prep(alarmProperties.title) + "'," 
				+ "'" + DB.prep(alarmProperties.calendarId) + "'," 
				+ "'" + DB.prep(alarmProperties.itemDatetime.toString()) + "',"
				+ "'" + DB.prep(alarmProperties.alarmDatetime.toString()) + "',"
				+ "'" + DB.prep(alarmProperties.respondStatus.ordinal()) + "',"
				+ "'" + DB.prep(alarmProperties.minutes) + "'," 
				+ "'" + DB.prep(alarmProperties.sortOrder) + "',"
				+ "'" + DB.prep(alarmProperties.atlasId) + "',"

				+ "'" + DB.prep(alarmProperties.modifiedDatetime.toString()) + "');";
			DB.q(sql);
			// item_user_id
			int currentAlarmId = Integer.parseInt(DB.lastInsertId());
			alarmProperties.alramId = currentAlarmId;
			
			return currentAlarmId;
		}
	}
	
	public static ATLAlarmCellModelProperties fromHashMapToATLAlarmCellModelProperties
			(HashMap<String, String> alarmMap)
	{
		ATLAlarmCellModelProperties alarmProp = new ATLAlarmCellModelProperties();
		if (alarmMap!=null && alarmMap.size()>0)
		{
			alarmProp.alarmDatetime = toDate( (String) alarmMap.get("alarm_datetime"));
			alarmProp.alramId = Integer.parseInt((String) alarmMap.get("alarm_id"));
			alarmProp.itemDatetime = toDate( (String) alarmMap.get("item_datetime"));
			alarmProp.itemType  = ((String) alarmMap.get("item_type")==null)? ITEM_TYPE.TASK:
					((String) alarmMap.get("item_type")).equals("event")? ITEM_TYPE.EVENT:
					((String) alarmMap.get("item_type")).equals("note")?ITEM_TYPE.NOTE:
					((String) alarmMap.get("item_type")).equals("task")? ITEM_TYPE.TASK:
						ITEM_TYPE.TASK;
			
			alarmProp.minutes = Integer.parseInt((String) alarmMap.get("minutes"));
			alarmProp.modifiedDatetime = toDate( (String) alarmMap.get("modified_datetime"));
			EventResponseType respondStatus = EventResponseType.getResponse(
					Integer.parseInt((String) alarmMap.get("respond_status")));
		
			alarmProp.respondStatus =respondStatus;
			alarmProp.sortOrder =  Integer.parseInt((String) alarmMap.get("sort_order"));
			alarmProp.title = (String) alarmMap.get("title");
			alarmProp.webItemId =  (String) alarmMap.get("web_item_id");
			alarmProp.calendarId =  (String) alarmMap.get("calendar_id");
			alarmProp.atlasId =  (String) alarmMap.get("atlas_id");
		}
		
		
		return alarmProp; 
	}

	private static Date toDate(String startTime)
	{
		Date date = null;
		//String startTime = "2011-09-05 15:00:23";
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy"); 
		if (startTime!=null && !startTime.equals(""))
			try {
				date =  dateFormat.parse(startTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return date;
		 
	}
	public static ArrayList<String> getAllIDRec(
			) {
		ArrayList<String> allIDByRespondStatus = null;
		
			String sql = "SELECT calendar_id FROM alarm ";
			allIDByRespondStatus = getAllRecordsBySQL(sql);
		
		
		
		return allIDByRespondStatus;
	}
	public static ArrayList<String> getAllIDByRespondStatus(
			EventResponseType respond) {
		ArrayList<String> allIDByRespondStatus = null;
		
		if (respond!=null )
		{
			String sql = "SELECT calendar_id FROM alarm WHERE respond_status='"+respond.ordinal() +"'";
			allIDByRespondStatus = getAllRecordsBySQL(sql);
		}
		
		
		
		return allIDByRespondStatus;
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
	
	
	public static void updateAlarmStatusByAlarmId(ArrayList<String> alarmIds, 
			EventResponseType status)
	{
		if (alarmIds!=null && alarmIds.size()>0 && status!=null)
		{
//			String sql = "SELECT alarm_id FROM alarm WHERE respond_status!='"+status.ordinal() +"'";
//			ArrayList<String> allIDByRespondStatus =  getAllRecordsBySQL(sql);
//			if (allIDByRespondStatus!=null && allIDByRespondStatus.size()>0)
//			{
				
//			}
			String sql="";
				for (String id :alarmIds)
				{
					if (id!=null && !id.equals("") && !id.equals("-1") )
					{
						sql = "update  alarm SET " 
							+ "respond_status ='"+ DB.prep(status.ordinal()) + "' "
							+ " WHERE calendar_id = '"+id+ "' ";  
						DB.q(sql);
					}
					
				}
		
//			}
		}
	}

	
	public static ATLAlarmCellModelProperties getAlarmById(long event_id) {
		
		
		ATLAlarmCellModelProperties prop=null;
		String id = event_id+"";
		String sql = "SELECT * FROM alarm WHERE calendar_id='"+id +"'";
		
		ArrayList<HashMap<String,String>> hash = DB.q(sql);
		if (hash!=null && hash.size()>0)
		{
			prop = fromHashMapToATLAlarmCellModelProperties(hash.get(0));
		}
		
		
		return prop;  
	}
	public static HashMap<String,String> getAllWebEventIdAndCalendarIDSFromAlarm() {
		ArrayList<HashMap<String,String>> allIDByRespondStatus = null;
	
		String sql = "SELECT web_item_id,calendar_id FROM alarm ";
		allIDByRespondStatus = DB.q(sql);
		
		HashMap<String,String> calendarIdByWebHash = new HashMap<String,String>();
		if (allIDByRespondStatus!=null && allIDByRespondStatus.size()>0)
		{
			for (HashMap<String,String> hash:allIDByRespondStatus)
			{
				calendarIdByWebHash.put(hash.get("web_item_id"), hash.get("calendar_id"));
			}
		}
	
	
	
		return calendarIdByWebHash;
	}
	public static ArrayList<String> getAllWebEventIdFromAlarm() {
			ArrayList<String> allIDByRespondStatus = null;
		
			String sql = "SELECT web_item_id FROM alarm ";
			allIDByRespondStatus = getAllRecordsBySQL(sql);
		
		
		
		return allIDByRespondStatus;
	}

	public static void updateAlarmStatusByWebEventId(String webEventId,
			EventResponseType respondStatus) {
		if (webEventId!=null && !webEventId.equals("") && respondStatus!=null)
		{
//			String sql = "SELECT alarm_id FROM alarm WHERE respond_status!='"+status.ordinal() +"'";
//			ArrayList<String> allIDByRespondStatus =  getAllRecordsBySQL(sql);
//			if (allIDByRespondStatus!=null && allIDByRespondStatus.size()>0)
//			{
				
//			}
			String sql="";
//				for (String id :alarmIds)
//				{
//					if (id!=null && !id.equals("") && !id.equals("-1") )
//					{
						sql = "update  alarm SET " 
							+ "respond_status ='"+ DB.prep(respondStatus.ordinal()) + "' "
							+ " WHERE web_item_id = '"+webEventId+ "' ";  
						DB.q(sql);
//					}  
					
//				}
		
//			}
		}
		
	}

	public static long getAlarmIdByWebEventId(String webItemId) {

		long id= -1;
		String sql="";
		ArrayList<String> idArr;
		if (webItemId!=null && !webItemId.equals(""))
		{
			 sql = "SELECT calendar_id FROM alarm where web_item_id ='"+ webItemId+"'";
		
			 idArr = DB.queryOneParam(sql);
			 if (idArr!=null && idArr.size()>0)
				 id = Long.valueOf(idArr.get(0));
		}
		return id;
	}

	
}
