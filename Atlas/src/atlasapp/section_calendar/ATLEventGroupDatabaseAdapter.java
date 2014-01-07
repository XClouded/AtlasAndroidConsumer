//  ==================================================================================================================
//  ATLEventGroupDatabaseAdapter.java
//  ATLAS
//  Copyright (c) 2012 Atlas Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2013-01-11 TAN:    Clear Code
//  2012-11-14 NGHIA:  Created
//  ==================================================================================================================

package atlasapp.section_calendar;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import atlasapp.common.DB;
import atlasapp.common.DBDefines;
import atlasapp.common.ATLConstants.EventResponseType;

public class ATLEventGroupDatabaseAdapter extends DB {


	static public boolean insertCalendarModel(ATLEventGroupModel model) {
		DB.openDb();
		final SQLiteDatabase db = DB.db;

		final ContentValues values = new ContentValues();
		Date insertTime = new Date();
		model.calCellGroupEventID = insertTime.getTime() +"|"+model.calCellEventIdentifier
				+"|"+model.calCellAlt2EventIdentifier
				+"|"+model.calCellAlt3EventIdentifier;
		values.put(DBDefines.GROUP_EVENT_ID, model.calCellGroupEventID);
		values.put(DBDefines.PREFERRED_EVENT_ID, model.calCellEventIdentifier);
		values.put(DBDefines.ALT2_EVENT_ID, model.calCellAlt2EventIdentifier);
		values.put(DBDefines.ALT3_EVENT_ID, model.calCellAlt3EventIdentifier);
		values.put(DBDefines.EVENT_RESPONSE_STATUS, model.eventRespondStatus.ordinal());

		try {
			db.insert(DBDefines.ALT_APPOINTMENTS, null, values);
			db.close();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			db.close();
			return false;
		}
	}

	static public boolean updateATLTaskModel() {

		return true;
	}

	static public boolean deleteCalendarModel(ATLEventGroupModel model) {
		DB.openDb();
		final SQLiteDatabase db = DB.db;
		final String whereClause = DBDefines.GROUP_EVENT_ID + "=?";
		final String[] whereArgs = new String[] { String
				.valueOf(model.calCellGroupEventID) };
		db.delete(DBDefines.ALT_APPOINTMENTS, whereClause, whereArgs);
		db.close();
		return true;
	}

	static public boolean deleteAllCalendarModel() {
		DB.openDb();
		final SQLiteDatabase db = DB.db;
		db.delete(DBDefines.ALT_APPOINTMENTS, null, null);
		db.close();
		return true;
	}

	static public ArrayList<ATLEventGroupModel> loadAllEventGroupInDatabase() {
		DB.openDb();
		final SQLiteDatabase db = DB.db;
		final String whereClause = null;
		final String[] columns = null;

		final Cursor cursor = db.query(DBDefines.ALT_APPOINTMENTS, columns,
				whereClause, null, "", "", "", "");
		final ArrayList<ATLEventGroupModel> list = new ArrayList<ATLEventGroupModel>();
		if (cursor.moveToFirst()) {
			do {
				ATLEventGroupModel calendarModel = new ATLEventGroupModel();
				try {

					calendarModel.calCellGroupEventID = cursor.getString(cursor
							.getColumnIndex(DBDefines.GROUP_EVENT_ID));
					calendarModel.calCellEventIdentifier = cursor
							.getString(cursor
									.getColumnIndex(DBDefines.PREFERRED_EVENT_ID));
					calendarModel.calCellAlt2EventIdentifier = cursor
							.getString(cursor
									.getColumnIndex(DBDefines.ALT2_EVENT_ID));
					calendarModel.calCellAlt3EventIdentifier = cursor
							.getString(cursor
									.getColumnIndex(DBDefines.ALT3_EVENT_ID));
					calendarModel.eventRespondStatus =EventResponseType.getResponse( cursor
							.getInt(cursor
									.getColumnIndex(DBDefines.EVENT_RESPONSE_STATUS)));

					list.add(calendarModel);
				} catch (Exception e) {
					// ignore
				}
			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();
		return list;
	}

	static public ATLEventGroupModel loadCalendarModelByPrimaryKey(String groupID) {

		DB.openDb();
		final SQLiteDatabase db = DB.db;
		final String whereClause = DBDefines.GROUP_EVENT_ID + "=?";
		final String[] whereArgs = new String[] { groupID };
		final String[] columns = null;
		final Cursor cursor = db.query(DBDefines.ALT_APPOINTMENTS, columns,
				whereClause, whereArgs, "", "", "", "");

		ATLEventGroupModel eventGroup = null;
		if (cursor.moveToFirst()) {
			do {
				eventGroup = new ATLEventGroupModel();
				try {

					eventGroup.calCellGroupEventID = cursor.getString(cursor
							.getColumnIndex(DBDefines.GROUP_EVENT_ID));
					eventGroup.calCellEventIdentifier = cursor
							.getString(cursor
									.getColumnIndex(DBDefines.PREFERRED_EVENT_ID));
					eventGroup.calCellAlt2EventIdentifier = cursor
							.getString(cursor
									.getColumnIndex(DBDefines.ALT2_EVENT_ID));
					eventGroup.calCellAlt3EventIdentifier = cursor
							.getString(cursor
									.getColumnIndex(DBDefines.ALT3_EVENT_ID));
					eventGroup.eventRespondStatus =EventResponseType.getResponse( cursor
							.getInt(cursor
									.getColumnIndex(DBDefines.EVENT_RESPONSE_STATUS)));

				} catch (Exception e) {
					// ignore
				}
			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();

		return eventGroup;
	}

	static public boolean isExistInDatabase(ATLEventGroupModel model) {
		DB.openDb();
		final SQLiteDatabase db = DB.db;
		final String whereClause = DBDefines.GROUP_EVENT_ID + "=?";
		final String[] whereArgs = new String[] { model.calCellGroupEventID };
		final String[] columns = null;
		final Cursor cursor = db.query(DBDefines.ALT_APPOINTMENTS, columns,
				whereClause, whereArgs, "", "", "", "");

		return cursor.moveToFirst();
	}

	static public boolean updateCalendarModel(ATLEventGroupModel model) {
		DB.openDb();
		final SQLiteDatabase db = DB.db;
		final String whereClause = DBDefines.GROUP_EVENT_ID + "=?";
		final String[] whereArgs = new String[] { model.calCellGroupEventID };

		ContentValues values = new ContentValues();

		values.put(DBDefines.GROUP_EVENT_ID, model.calCellGroupEventID);
		values.put(DBDefines.PREFERRED_EVENT_ID, model.calCellEventIdentifier);
		values.put(DBDefines.ALT2_EVENT_ID, model.calCellAlt2EventIdentifier);
		values.put(DBDefines.ALT3_EVENT_ID, model.calCellAlt3EventIdentifier);
		values.put(DBDefines.EVENT_RESPONSE_STATUS, model.eventRespondStatus.ordinal());
		db.update(DBDefines.ALT_APPOINTMENTS, values, whereClause, whereArgs);
		db.close();
		return false;
	}

	static public ATLEventGroupModel getEventGroupIdOfEventId(long event_id) {
		// TODO Auto-generated method stub
		String eventID = event_id +"";
		ArrayList<ATLEventGroupModel> allGroup = loadAllEventGroupInDatabase();
		for(ATLEventGroupModel group : allGroup){
			Log.v("ATLEventGroupDatabaseAdapter", "GROUP ID: "+ group.calCellGroupEventID);
			String[] IDs = group.calCellGroupEventID.split("\\|");
			int length = IDs.length;
			for(int i = 1; i < length; i++){
				String actualEventID = IDs[i].split(":")[0];
				if(eventID.equals(actualEventID)){
					return group;
				}
			}
			
		}
		return null;
	}
	
	static public ATLEventGroupModel getEventGroupOfEventDate(long dateLong) {
		// TODO Auto-generated method stub
		String eventID = dateLong +"";
		ArrayList<ATLEventGroupModel> allGroup = loadAllEventGroupInDatabase();
		for(ATLEventGroupModel group : allGroup){
			Log.v("ATLEventGroupDatabaseAdapter", "GROUP ID: "+ group.calCellGroupEventID);
			String[] IDs = group.calCellGroupEventID.split("\\|");
			int length = IDs.length;
			for(int i = 1; i < length; i++){
				if(IDs[i].length()>1){
					String actualEventID = IDs[i].split(":")[1];
					if(eventID.equals(actualEventID)){
						
						return group;
					}
				}
			}
			
		}
		return null;
	}

	public static boolean deleteEventGroupID(String calCellEventGroupID) {
		// TODO Auto-generated method stub
		DB.openDb();
		final SQLiteDatabase db = DB.db;
		final String whereClause = DBDefines.GROUP_EVENT_ID + "=?";
		final String[] whereArgs = new String[] { calCellEventGroupID};
		db.delete(DBDefines.ALT_APPOINTMENTS, whereClause, whereArgs);
		db.close();
		return true;
	}
	
	static public String getEventIDFromIDString(String eventGroupStringId) {
		// TODO Auto-generated method stub
		if(eventGroupStringId != null){
			if(eventGroupStringId.length()>0){
				String eventID = eventGroupStringId.split(":")[0]; 
				return eventID;
			}
		}
		return null;
	}

	static public Date getDateFromIDString(String eventGroupStringId) {
		if(eventGroupStringId != null){
			if(eventGroupStringId.length()>0){
				String timeMiliSeconds = eventGroupStringId.split(":")[1]; 
				long dateLong = Long.valueOf(timeMiliSeconds);
				return new Date(dateLong);
			}
		}
		return null;
	}
	
	static public String getDateStringFromIDString(String eventGroupStringId) {
		if(eventGroupStringId != null){
			if(eventGroupStringId.length()>0){
				String timeMiliSeconds = eventGroupStringId.split(":")[1]; 
				return timeMiliSeconds;
			}
		}
		return null;
	}

}
