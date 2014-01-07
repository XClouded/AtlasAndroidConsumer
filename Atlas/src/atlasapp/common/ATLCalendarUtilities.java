package atlasapp.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.content.Context;
import android.util.Log;
import atlasapp.database.EventProperties;
import atlasapp.database.DatabaseConstants.EVENT_STATUS;
import atlasapp.section_calendar.ATLCalCellData;
import atlasapp.section_calendar.ATLEventGroupDatabaseAdapter;
import atlasapp.section_calendar.ATLEventGroupModel;

public class ATLCalendarUtilities {
	
	
	
	public static String toStringFromDate(Date date) {
		// date formatter
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
		String return_date = sd.format(date);
		return return_date;
	}

	public static Date toDateFromString(String dateString) {
		// 1985-04-12T23:20:50.52Z
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
		// sd.setTimeZone(TimeZone.getDefault());
		Date date = new Date();
		try {
			date = sd.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return date;
	}

//	public static void saveEventCalendarWithCellData(ArrayList<EventProperties> cellEventData,
//			Context ctx) {
//		
//		
//		if (cellEventData!=null && cellEventData.size()>0)
//		{
//			long startMillis = 0;
//			long endMillis = 0;
//			long duration = 0;
//
//			int id1 = -1;
//			int id2 = -1;
//			int id3 = -1;
//			
//			
//			Date eventDate ;
//			Calendar beginTime ;// Calendar.getInstance();
//			ATLEventGroupModel model = new ATLEventGroupModel();
//			
//			for (EventProperties eventAlt:cellEventData)
//			{
//				if (eventAlt!=null)
//				{
//					eventDate = eventAlt.startDateTime;
//					beginTime = Calendar.getInstance();
//					beginTime.setTimeInMillis(eventDate.getTime());
//					startMillis = beginTime.getTimeInMillis();
//					duration = eventAlt.duration* 60 * 1000;
//					endMillis = startMillis + duration;
//					id1 = insertEvent(eventAlt, startMillis, endMillis, ctx);
//					
//					// Insert to event group
//					Calendar gmt0Cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
//					gmt0Cal.setTime(beginTime.getTime());
//					
//					SimpleDateFormat lv_formatter;
//					lv_formatter = new SimpleDateFormat("yyyy:DD:MM HH:mm:ss");
//					lv_formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
//					
//					EventResponseType responseType = (eventAlt.status.equals(EVENT_STATUS.PENDING))?
//					
//					model.eventRespondStatus = cellData.eventResponseType_CellData;
//
//					
//				}
//			}
//
//		
//		
//		
//		
//		
//		
//		model.eventRespondStatus = cellData.eventResponseType_CellData;
//		
//		model.calCellEventIdentifier = id1 + ":" + gmt0Cal.getTimeInMillis();
//	
//		Date atl2 = cellData.getCalCellALT3Datetime();
//		Date atl1 = cellData.getCalCellALT2Datetime();
//
//		long startMillis1 = 0;
//		long endMillis1 = 0;
//		long duration1 = 0;
//	
//		Calendar beginTime1 = Calendar.getInstance();
//		Calendar beginTime2 = Calendar.getInstance();
//		if (atl1 != null) {
//
//			beginTime1.setTimeInMillis(atl1.getTime());
//			startMillis1 = beginTime1.getTimeInMillis();
//			duration1 = cellData.getCalCellDurationMinutes() * 60 * 1000;
//			endMillis1 = startMillis1 + duration1;// To long value
//			id2 = insertEvent(cellData, startMillis1, endMillis1, ctx);
//			Log.v("CalendarUtilities", "ID  :" + id2);
//			gmt0Cal.setTime(beginTime1.getTime());
//			model.calCellAlt2EventIdentifier = id2 + ":" + gmt0Cal.getTimeInMillis();
//		}
//
//		long startMillis2 = 0;
//		long endMillis2 = 0;
//		long duration2 = 0;
//		if (atl2 != null) {
//			
//			beginTime2.setTimeInMillis(atl2.getTime());
//			startMillis2 = beginTime2.getTimeInMillis();
//			duration2 = cellData.getCalCellDurationMinutes() * 60 * 1000;
//			endMillis2 = startMillis2 + duration2;// To long value
//			id3 = insertEvent(cellData, startMillis2, endMillis2, ctx);
//			Log.v("CalendarUtilities", "ID  :" + id3);
//			gmt0Cal.setTime(beginTime2.getTime());
//			model.calCellAlt3EventIdentifier = id3 + ":" +  gmt0Cal.getTimeInMillis();
//		}
//
//		
//		// Insert Group model
//		ATLEventGroupDatabaseAdapter.insertCalendarModel(model);
//		}
//	}
}
