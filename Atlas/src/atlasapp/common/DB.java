//  ==================================================================================================================
//  DB.java
//  ATLAS
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-12-30 NGHIA    Add Note Table
//  2012-11-09 TAN:     Change methods to public method to extends by TaskDatabaseAdapter.java 
//						Divide class to easy edit 
//  ==================================================================================================================

package atlasapp.common;

import java.sql.Date;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DB {
	
	public static DBCallBackInterface dbCallBackInterface;

	public static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, UtilitiesProject.DB_NAME, null,
					UtilitiesProject.DB_VERSION);
			
			
		}

		
		
		
		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL("DROP TABLE if exists FRIEND");
			db.execSQL("DROP TABLE if exists ATL_FRIEND");

			db.execSQL("DROP TABLE if exists item_user");
			db.execSQL("DROP TABLE if exists event");
			db.execSQL("DROP TABLE if exists alarm");
			
			
			db.execSQL("CREATE TABLE if not exists ATL_FRIEND (FRIEND_ID INTEGER PRIMARY KEY, ATLAS_ID VARCHAR(100),IS_ATLAS_USER BIT DEFAULT 0, FIRSTNAME VARCHAR(100), "
					+ "LASTNAME VARCHAR(100), COMPANY VARCHAR(100), PHONE_NUMBER VARCHAR(50), "
					+ "EMAIL_ADDRESS VARCHAR(200), PIC_PATH VARCHAR(200), FROM_FACEBOOK BIT DEFAULT 0, "
					+ "FACEBOOK_ID VARCHAR(100), FROM_TWITTER BIT DEFAULT 0, TWITTER_ID VARCHAR(100), "
					+ "FROM_LINKEDIN BIT DEFAULT 0, LINKEDIN_ID VARCHAR(100), "
					+ "FROM_ADDRESS_BOOK BIT DEFAULT 0, ADDRESS_BOOK_ID VARCHAR(100), "
					+ "LAST_INTERACTION_DATETIME VARCHAR(16), MODIFIED_DATETIME VARCHAR(16))");
			db.execSQL("CREATE TRIGGER if not exists [FriendUpdateModifiedDatetime] AFTER UPDATE ON ATL_FRIEND FOR EACH ROW BEGIN UPDATE ATL_FRIEND SET MODIFIED_DATETIME = CURRENT_TIMESTAMP WHERE FRIEND_ID = old.FRIEND_ID; END");

			// Friend friend = new Friend();
			// friend.setFirstname("testFirst");
			// friend.setLastname("testLast");
			// friend.setPhoneNumber("testPhone");
			// friend.setEmailAddress("testEmail");  
			// friend.setPicPath("testPicPath");
			// friend.setFromFacebook(true);
			// friend.write();
			//
			// ArrayList<HashMap<String, String>> friendC = Friend.readAll();
			// Log.e("DB", "friendC.size()="+friendC.size());
			//
			// 2012-11- 11 TAN: Create Table ATL_TASK
			db.execSQL("CREATE TABLE if not exists ATL_TASK ("
					+ "TASK_ID INTEGER PRIMARY KEY, "
					+ "TASK_UUID VARCHAR(100), "
					+ "TASK_ATLAS_ID VARCHAR(100), "
					+ "TASK_DUE_DATE VARCHAR(100), "
					+ "TASK_COMPLETED_DATE VARCHAR(100), "
					+ "TASK_REMINDER INTEGER, " + "TASK_TITLE VARCHAR(100),"
					+ "TASK_DESCRIPTION VARCHAR(100), "
					+ "TASK_NOTE VARCHAR(100), " + "TASK_PRIORITY INTEGER,"
					+ "TASK_STATUS VARCHAR(100), "
					+ "TASK_CALENDAR_NAME VARCHAR(100), "
					+ "TASK_CALENDAR_COLOR INTEGER,"
					+ "TASK_CALENDAR_ID INTEGER,"
					+ "TASK_IS_COMPLETED INTEGER, "
					+ "TASK_SENDER_ID VARCHAR(100), "
					+ "TASK_SENDER_EMAIL VARCHAR(100), "
					+ "TASK_SENDER_NAME VARCHAR(100), "
					+ "TASK_RECEIVER_ID VARCHAR(100), "
					+ "TASK_RECEIVER_EMAIL VARCHAR(100), "
					+ "TASK_RECEIVER_NAME VARCHAR(100), "
					+ "TASK_RESPONSE_STATUS INTEGER, "
					+ "TASK_MODIFIED_DATE VARCHAR(100), "
					+ "TASK_CREATED_DATE VARCHAR(100), "
					+ "TASK_IS_DELTETED INTEGER)");

			db.execSQL("CREATE TABLE if not exists ATL_NOTE ("
					+ "NOTE_ID INTEGER PRIMARY KEY, NOTE_UUID VARCHAR(100), NOTE_ATLAS_ID VARCHAR(100), "
					+ "NOTE_TITLE VARCHAR(100), NOTE_BODY VARCHAR(100), "
					+ "NOTE_CALENDAR_NAME VARCHAR(100),  NOTE_CALENDAR_COLOR INTEGER, "
					+ "NOTE_DATE_CREATED VARCHAR(100), NOTE_MODIFIED_DATE VARCHAR(100), "
					+ "NOTE_AUTHOR_ID VARCHAR(100), NOTE_AUTHOR_NAME VARCHAR(100), "
					+ "NOTE_IS_SELECTED_STAR INTEGER)");

			// 2012-11-14 NGHIA: Create Table ALT_APPOINTMENTS
			db.execSQL("CREATE TABLE if not exists ALT_APPOINTMENTS  (GROUP_EVENT_ID TEXT PRIMARY KEY  NOT NULL , "
					+ "PREFERRED_EVENT_ID TEXT, ALT2_EVENT_ID TEXT, ALT3_EVENT_ID TEXT, EVENT_RESPONSE_STATUS INTEGER)");

			// 2012-11-14 NGHIA: Create Table ATL_ATTENDEE
			db.execSQL("CREATE TABLE if not exists ATL_ATTENDEE (ATTENDEE_ID INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL ,"
					+ " APPOINTMENT_ID TEXT, ATLAS_ID TEXT, ATTENDEE_NAME TEXT, ATTENDEE_EMAIL TEXT, ATTENDEE_IMAGE BLOB, "
					+ "ATTENDEE_PHONENUMBER TEXT)");

			// 2012-11-14 NGHIA: Create Table ATL_ALERTS
			db.execSQL("CREATE TABLE if not exists ATL_ALERTS ("
					+ "ALERT_ID INTEGER PRIMARY KEY, " + "ALERT_UUID TEXT, "
					+ "ALERT_ATLAS_ID TEXT, " + "ALERT_SENDER_ID TEXT, "
					+ "ALERT_SENDER_EMAIL TEXT, " + "ALERT_SENDER_NAME TEXT, "
					+ "ALERT_RECEIVER_ID TEXT, "
					+ "ALERT_RECEIVER_EMAIL TEXT, "
					+ "ALERT_RECEIVER_NAME TEXT, " + "ALERT_EVENT_TITLE TEXT, "
					+ "ALERT_EVENT_DURATION INTEGER, "
					+ "ALERT_EVENT_LOCATION TEXT, "
					+ "ALERT_DESCRIPTION TEXT, " + "ALERT_MSG_ID TEXT, "
					+ "ALERT_PREFERRED_DATETIME VARCHAR(16), "
					+ "ALERT_ALT1_DATETIME VARCHAR(16), "
					+ "ALERT_ALT2_DATETIME VARCHAR(16), "
					+ "ALERT_EVENT_PRIORITY TEXT, "
					+ "ALERT_TEXT TEXT, "
					+ "ALERT_ACCESSED_DATETIME VARCHAR(16), "
					+ "ALERT_MSG_ACTION TEXT, "
					+ "ALERT_MSG_CREATED_DATETIME VARCHAR(16), "
					+ "ALERT_DISPLAYED INTEGER, " + "ALERT_READ INTEGER, "
					+ "ALERT_HANDLED INTEGER, " + "ALERT_ACCEPTED INTEGER, "
					+ "ALERT_ACCEPTED_DATETIME VARCHAR(16), "
					+ "ALERT_RESPONSE_STATUS INTEGER, "
					+ "ALERT_IS_SENT_MESSAGE_SUCCESSFULLY INTEGER, "
					+ "ALERT_TYPE INTEGER, " + "ALERT_TASK_PRIORITY,"
					+ "ALERT_MODIFIED_DATETIME VARCHAR(16), "
					+ "ALERT_CREATED_DATETIME VARCHAR(16))");
			
			
			
			
			
			
			///// Sharon :create alarm table
			
			
			db.execSQL("CREATE TABLE if not exists alarm " +
					"(alarm_id INTEGER PRIMARY KEY, " +
					"item_type VARCHAR(20), " +
					"web_item_id VARCHAR(250), " +
					"item_title VARCHAR(250), " +
					"calendar_id VARCHAR(250),"+
					"item_datetime DATETIME, " +
					"alarm_datetime DATETIME, " +
					"respond_status INT, "+
					"minutes INT, sort_order INT, " +
					"atlas_id VARCHAR(20), " +
					"modified_datetime DATETIME DEFAULT CURRENT_TIMESTAMP);");
			db.execSQL("CREATE TRIGGER if not exists " +
					"[AlarmUpdateModifiedDatetime] AFTER UPDATE ON alarm FOR EACH ROW BEGIN UPDATE" +
					" alarm SET modified_datetime = CURRENT_TIMESTAMP WHERE " +
					"alarm_id = old.alarm_id; END");
			// Sharon :create event table
			db.execSQL("CREATE TABLE IF NOT EXISTS event ("
					+"event_id INTEGER PRIMARY KEY, "
					+" web_event_id VARCHAR(25), "
					+"device_event_id VARCHAR(250), "
					+"primary_event_id INTEGER, "
					+"device_event_duration INTEGER, "
					+"primary_web_event_id VARCHAR(25), "
					+"atlas_id VARCHAR(25),  "
					+"display_order INT, " 
					+"status INTEGER, "
					+"device_event_start_datetime DATETIME, "
					+"device_event_end_datetime DATETIME, "
					+"device_event_title VARCHAR(250), "
					+"device_event_location VARCHAR(250), "
					+"device_event_notes VARCHAR(500), "
					+"action VARCHAR(10), "
					+"modified_datetime DATETIME DEFAULT CURRENT_TIMESTAMP)");
			
			
			db.execSQL("CREATE TRIGGER if not exists [EventUpdateModifiedDatetime] "
					+"AFTER UPDATE ON event FOR EACH ROW BEGIN UPDATE event "
					+"SET modified_datetime = CURRENT_TIMESTAMP WHERE event_id = old.event_id; END") ;
			 // Sharon create task table   
			db.execSQL("CREATE TABLE IF NOT EXISTS task "
					+"(task_id INTEGER PRIMARY KEY, web_task_id VARCHAR(25), "
					+"device_task_id VARCHAR(250), atlas_id VARCHAR(25), "
					+"device_task_title VARCHAR(250), "
					+"device_task_notes VARCHAR(250), "
					+"device_task_priority INTEGER, "
					+"device_task_due_datetime DATETIME, "
					+"status INTEGER, action VARCHAR(10), "
					+"modified_datetime DATETIME DEFAULT CURRENT_TIMESTAMP)");
			db.execSQL("CREATE TRIGGER if not exists [TaskUpdateModifiedDatetime] "
					+"AFTER UPDATE ON task FOR EACH ROW BEGIN UPDATE task SET modified_datetime = CURRENT_TIMESTAMP WHERE task_id = old.task_id; END");
			  // Sharon create item_user table
			db.execSQL("CREATE TABLE IF NOT EXISTS item_user "
					+"(item_user_id INTEGER PRIMARY KEY, "
					+"web_item_user_id VARCHAR(25), " 
					+"primary_web_event_id VARCHAR(25), " 
					+"item_type VARCHAR(20), "
					+"item_id INTEGER, "
					+"web_item_id VARCHAR(25), "
					+"atlas_id VARCHAR(25), "
					+"status INTEGER, "
					+"status_datetime DATETIME, "
					+"was_received BIT DEFAULT 0, "
					+"received_datetime DATETIME, "
					+"display_order INT, "
					+"priority_order INT, "
					+"action VARCHAR(10), "
					+"modified_datetime DATETIME DEFAULT CURRENT_TIMESTAMP)");
			db.execSQL("CREATE TRIGGER if not exists [ItemUserUpdateModifiedDatetime] "
					+"AFTER UPDATE ON item_user FOR EACH ROW BEGIN UPDATE item_user SET modified_datetime = CURRENT_TIMESTAMP WHERE item_user_id = old.item_user_id; END");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db2, int oldVersion, int newVersion) {
			upgrade(db2, newVersion);
			db2.execSQL("REINDEX;");
		}

		private void upgrade(SQLiteDatabase db2, int newVersion) {

		}
	}

	public static DatabaseHelper dbHelper;

	public static SQLiteDatabase db = null;

	public static final void openDb() {

		if (db == null || !db.isOpen()) {
			Context ctx = Utilities.ctx;
			if (UtilitiesProject.RESET_DB) {
				ctx.deleteDatabase(UtilitiesProject.DB_NAME);
			}

			dbHelper = new DatabaseHelper(ctx);
			db = dbHelper.getWritableDatabase();
		}
	}

	public static final void closeDb() {
		dbHelper.close();
	}

	
	
	
	public static final ArrayList<HashMap<String, String>> q(String sql) {
		return q(sql, null);
	}
	public static final ArrayList<String> queryOneParam(String sql) {
		return queryOneParam(sql, null);
	}
	/**
	 * Convenience method for updating rows in the database.
	 * Parameters table	the table to update in values
	 * a map from column names to new column values. 
	 * null is a valid value that will be translated to NULL. 
	 * 
	 * whereClause	the optional WHERE clause to apply when updating.
	 *  Passing null will update all rows.
	 * Returns the number of rows affected
	 * @param tableName
	 * @param cv
	 * @param columnName
	 * @param values
	 * @return
	 */
	public static final int update(String tableName,HashMap<String,String> updateColumns,String columnName, 
		    String []  values)
	{
		int numberOfRowAffected = 0;
		if (updateColumns!=null && updateColumns.size()>0)
		{
			  ContentValues cv=new ContentValues();
			  Iterator iterator = updateColumns.keySet().iterator();
			  String  keyColumn;
		      String valueColumn;
			  while(iterator.hasNext()) {
				  keyColumn=(String)iterator.next();
				  valueColumn=(String)updateColumns.get(keyColumn);
				  cv.put(keyColumn, valueColumn);
			  }			
			if (cv!=null && cv.size()>0)
				numberOfRowAffected = update(tableName,cv, columnName, 
				    values,null);
		}
		
		return numberOfRowAffected;
	}
	private  static final int update(String tableName,ContentValues cv,String columnName, 
		    String []  values, SQLiteDatabase db2) {
		if (db2 == null) {
			openDb();
			db2 = db;
		}
		String column = columnName+"=?";
		return db2.update(tableName, cv, column, values);
	}
	public static final int delete(String tableName,String where,
		    String []  values)
	{
		int rows = 0;
		SQLiteDatabase db2 = null;
		if (db2 == null) {
			openDb();
			db2 = db;
		}   
		if (db!=null && where!=null && !where.equals("") && values!=null && values.length>0)
		try{
			rows =  db.delete(tableName,where + " = ?", values);
		 }
        catch( Exception ex )
        {
            //Log.e( tag, ex.getMessage() );
        }
			return rows;

	}
	
	public static final void deleteAllEventsFromEventTable()
	{
		SQLiteDatabase db2 = null;
			if (db2 == null) {
				openDb();
				db2 = db;
			}
		if (db!=null)
			db.delete("event",null, null);
//		if (db!=null){
//		db.execSQL("DROP TABLE if exists event");
//		db.execSQL("CREATE TABLE IF NOT EXISTS event ("
//				+"event_id INTEGER PRIMARY KEY, "
//				+" web_event_id VARCHAR(25), "
//				+"device_event_id VARCHAR(250), "
//				+"primary_event_id INTEGER, "
//				+"device_event_duration INTEGER, "
//				+"primary_web_event_id VARCHAR(25), "
//				+"atlas_id VARCHAR(25),  "
//				+"display_order INT, " 
//				+"status INTEGER, "
//				+"device_event_start_datetime DATETIME, "
//				+"device_event_end_datetime DATETIME, "
//				+"device_event_title VARCHAR(250), "
//				+"device_event_location VARCHAR(250), "
//				+"device_event_notes VARCHAR(500), "
//				+"action VARCHAR(10), "
//				+"modified_datetime DATETIME DEFAULT CURRENT_TIMESTAMP)");
//		
//		
//		db.execSQL("CREATE TRIGGER if not exists [EventUpdateModifiedDatetime] "
//				+"AFTER UPDATE ON event FOR EACH ROW BEGIN UPDATE event "
//				+"SET modified_datetime = CURRENT_TIMESTAMP WHERE event_id = old.event_id; END") ;
//	}
	}
	private static final int delete(String tableName,String where,
		    String []  values, SQLiteDatabase db2) {
		if (db2 == null) {
			openDb();
			db2 = db;
		}
		String column = where+"=?";
		return 		db.delete(tableName,column, values);

	}
	public static final ArrayList<HashMap<String, String>> q(String sql,
			SQLiteDatabase db2) {
		if (db2 == null) {
			openDb();
			db2 = db;
		}
		
		// if (sql.toUpperCase().indexOf("UPDATE ") != -1) {
		// sql = sql.replace("activity='upd", "modified_datetime='" + U.gmt() +
		// "', activity='upd");
		// }

		if (UtilitiesProject.IS_DEBUG_MODE) {
			Log.i("Database", sql);
		}

		ArrayList<HashMap<String, String>> c = null;
		Cursor cur = null;
		try {
			cur = db2.rawQuery(sql, null);
			c = new ArrayList<HashMap<String, String>>(cur.getCount());
			HashMap<String, String> h;
			int i;
			for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
				h = new HashMap<String, String>(cur.getColumnCount());
				i = 0;
				for (String col : cur.getColumnNames()) {
					h.put(col, cur.getString(i++));
				}
				c.add(h);
			}
		} finally {
			if (cur != null) {
				cur.close();
			}
		}
		//dbCallBackInterface.writeCallBack(c!=null);
		return c;
	}
	public static final ArrayList<String> queryOneParam(String sql,
			SQLiteDatabase db2) {
		if (db2 == null) {
			openDb();
			db2 = db;
		}
		
		// if (sql.toUpperCase().indexOf("UPDATE ") != -1) {
		// sql = sql.replace("activity='upd", "modified_datetime='" + U.gmt() +
		// "', activity='upd");
		// }

		if (UtilitiesProject.IS_DEBUG_MODE) {
			Log.i("Database", sql);
		}

		ArrayList<String> c = null;
		Cursor cur = null;
		try {
			cur = db2.rawQuery(sql, null);
			c = new ArrayList<String>(cur.getCount());
			String param;
			int i;
			for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
				param = "";
				i = 0;
				for (String col : cur.getColumnNames()) {
					param =  cur.getString(i++);
				}
				c.add(param);
			}
		} finally {
			if (cur != null) {
				cur.close();
			}
		}
	//	dbCallBackInterface.writeCallBack(c!=null);
		return c;
	}
	public static final String lastInsertId() {
		return Utilities.eString("last_insert_id",
				q("select last_insert_rowid() as last_insert_id"));
	}

	public static final String param(String column, Object val) {
		if (val == null) {
			return "";
		}
		return column + "='" + prep(val) + "',";
	}

	public static final String setParam(String column, Object val,
			Boolean isValue) {
		if (val == null) {
			return "";
		} else if (isValue) {
			return "'" + DB.prep(val) + "',";
		} else {
			return column + ",";
		}
	}

	public static String prep(Object o) {
		String s;
		if (o instanceof Boolean) {
			s = ((Boolean) o).booleanValue() ? "1" : "0";
		} else if (o instanceof Integer) {
			s = Integer.toString((Integer) o);
		} else  if (o instanceof Date)
		{
			s = ((Date)o).toString();
		}else
		{
			s = (String) o;
		}
		
		s = Utilities.deNull(s);
		s = s.replaceAll("'", "''");
		return s;
	}
	private static String dateToString(Date date)
	{
		String dateString = "";
		Format formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		if (date!=null)
			dateString = formatter.format(date);
		
		return dateString;
	
	}
}