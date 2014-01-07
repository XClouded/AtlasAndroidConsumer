/**
 * 
 */
package atlasapp.section_notes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import atlasapp.common.DB;
import atlasapp.common.DBDefines;
import atlasapp.model.ATLNoteModel;

/**
 * @author nghia : handle ATL_NOTE Table in database
 * 
 */
public class ATLNoteDatabaseAdapter extends DB {

	/**
	 * 
	 */

	public final static String NOTE_ID = "NOTE_ID"; // Primary Key
	public final static String NOTE_UUID = "NOTE_UUID";
	public final static String NOTE_ATLAS_ID = "NOTE_ATLAS_ID";
	public final static String NOTE_TITLE = "NOTE_TITLE";
	public final static String NOTE_BODY = "NOTE_BODY";
	public final static String NOTE_CALENDAR_NAME = "NOTE_CALENDAR_NAME";
	public final static String NOTE_CALENDAR_COLOR = "NOTE_CALENDAR_COLOR";
	public final static String NOTE_AUTHOR_ID = "NOTE_AUTHOR_ID";
	public final static String NOTE_AUTHOR_NAME = "NOTE_AUTHOR_NAME";
	public final static String NOTE_IS_SELECTED_STAR = "NOTE_IS_SELECTED_STAR";
	public final static String NOTE_DATE_CREATED = "NOTE_DATE_CREATED";
	public final static String NOTE_MODIFIED_DATE = "NOTE_MODIFIED_DATE";

	public ATLNoteDatabaseAdapter() {
		// TODO Auto-generated constructor stub
	}

	public boolean insertATLNoteModel(ATLNoteModel model) {
		DB.openDb();
		final SQLiteDatabase db = DB.db;

		final ContentValues values = new ContentValues();

		values.put(NOTE_UUID, model.noteUUId);
		values.put(NOTE_ATLAS_ID, model.noteAtlasId);

		values.put(NOTE_TITLE, model.noteTitle);
		values.put(NOTE_BODY, model.noteBody);

		values.put(NOTE_CALENDAR_NAME, model.noteCalendarName);
		values.put(NOTE_CALENDAR_COLOR, model.noteCalendarColor);

		values.put(NOTE_DATE_CREATED, toGmtString(model.noteDateCreated));
		values.put(NOTE_MODIFIED_DATE, toGmtString(model.noteModifiedDate));

		values.put(NOTE_AUTHOR_ID, model.noteAuthorId);
		values.put(NOTE_AUTHOR_NAME, model.noteAuthorName);

		values.put(NOTE_IS_SELECTED_STAR, model.noteIsStarred ? 1 : 0);

		try {
			db.insert(DBDefines.ATL_NOTE, null, values);
			db.close();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			db.close();
			return false;
		}
	}

	public boolean updateATLNoteModel() {

		return true;
	}

	public boolean deleteATLNoteModel(ATLNoteModel model) {
		DB.openDb();
		final SQLiteDatabase db = DB.db;
		final String whereClause = NOTE_ID + "=?";
		final String[] whereArgs = new String[] { String.valueOf(model.noteId) };
		db.delete(DBDefines.ATL_NOTE, whereClause, whereArgs);
		db.close();
		return true;
	}

	public boolean deleteAllATLNoteModel() {
		DB.openDb();
		final SQLiteDatabase db = DB.db;
		db.delete(DBDefines.ATL_NOTE, null, null);
		db.close();
		return true;
	}

	public ArrayList<ATLNoteModel> loadAllNoteInDatabase() {
		DB.openDb();
		final SQLiteDatabase db = DB.db;
		final String whereClause = null;
		final String[] columns = null;

		final Cursor cursor = db.query(DBDefines.ATL_NOTE, columns,
				whereClause, null, "", "", "", "");
		final ArrayList<ATLNoteModel> list = new ArrayList<ATLNoteModel>();
		if (cursor.moveToFirst()) {
			do {
				ATLNoteModel note = new ATLNoteModel();
				try {

					note.noteId = cursor.getInt(cursor.getColumnIndex(NOTE_ID));
					note.noteUUId = cursor.getString(cursor
							.getColumnIndex(NOTE_UUID));
					note.noteAtlasId = cursor.getString(cursor
							.getColumnIndex(NOTE_ATLAS_ID));

					note.noteTitle = cursor.getString(cursor
							.getColumnIndex(NOTE_TITLE));
					note.noteBody = cursor.getString(cursor
							.getColumnIndex(NOTE_BODY));

					note.noteCalendarName = cursor.getString(cursor
							.getColumnIndex(NOTE_CALENDAR_NAME));
					note.noteCalendarColor = cursor.getInt(cursor
							.getColumnIndex(NOTE_CALENDAR_COLOR));

					note.noteDateCreated = stringToDate(cursor.getString(cursor
							.getColumnIndex(NOTE_DATE_CREATED)));
					note.noteModifiedDate = stringToDate(cursor
							.getString(cursor
									.getColumnIndex(NOTE_MODIFIED_DATE)));

					note.noteAuthorId = cursor.getString(cursor
							.getColumnIndex(NOTE_AUTHOR_ID));
					note.noteAuthorName = cursor.getString(cursor
							.getColumnIndex(NOTE_AUTHOR_NAME));

					note.noteIsStarred = cursor.getInt(cursor
							.getColumnIndex(NOTE_IS_SELECTED_STAR)) != 0 ? true
							: false;

					list.add(note);
				} catch (Exception e) {
					// ignore
				}
			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();
		return list;
	}

	public ATLNoteModel loadATLNoteModelByPrimaryKey(int noteID) {

		DB.openDb();
		final SQLiteDatabase db = DB.db;
		final String whereClause = NOTE_ID + "=?";
		final String[] whereArgs = new String[] { String.valueOf(noteID) };
		final String[] columns = null;
		final Cursor cursor = db.query(DBDefines.ATL_NOTE, columns,
				whereClause, whereArgs, "", "", "", "");

		ATLNoteModel note = null;
		if (cursor.moveToFirst()) {
			do {
				note = new ATLNoteModel();
				try {

					note.noteId = cursor.getInt(cursor.getColumnIndex(NOTE_ID));
					note.noteUUId = cursor.getString(cursor
							.getColumnIndex(NOTE_UUID));
					note.noteAtlasId = cursor.getString(cursor
							.getColumnIndex(NOTE_ATLAS_ID));

					note.noteTitle = cursor.getString(cursor
							.getColumnIndex(NOTE_TITLE));
					note.noteBody = cursor.getString(cursor
							.getColumnIndex(NOTE_BODY));

					note.noteCalendarName = cursor.getString(cursor
							.getColumnIndex(NOTE_CALENDAR_NAME));
					note.noteCalendarColor = cursor.getInt(cursor
							.getColumnIndex(NOTE_CALENDAR_COLOR));

					note.noteDateCreated = stringToDate(cursor.getString(cursor
							.getColumnIndex(NOTE_DATE_CREATED)));
					note.noteModifiedDate = stringToDate(cursor
							.getString(cursor
									.getColumnIndex(NOTE_MODIFIED_DATE)));

					note.noteAuthorId = cursor.getString(cursor
							.getColumnIndex(NOTE_AUTHOR_ID));
					note.noteAuthorName = cursor.getString(cursor
							.getColumnIndex(NOTE_AUTHOR_NAME));

					note.noteIsStarred = cursor.getInt(cursor
							.getColumnIndex(NOTE_IS_SELECTED_STAR)) != 0 ? true
							: false;

				} catch (Exception e) {
					// ignore
				}
			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();

		return note;
	}

	public boolean isExistInDatabase(ATLNoteModel model) {
		DB.openDb();
		final SQLiteDatabase db = DB.db;
		final String whereClause = NOTE_ID + "=?";
		final String[] whereArgs = new String[] { String.valueOf(model.noteId) };
		final String[] columns = null;
		final Cursor cursor = db.query(DBDefines.ATL_NOTE, columns,
				whereClause, whereArgs, "", "", "", "");

		boolean result = cursor.moveToFirst();
		cursor.close();
		return result;
	}

	public boolean updateATLNoteModel(ATLNoteModel model) {
		DB.openDb();
		final SQLiteDatabase db = DB.db;
		final String whereClause = NOTE_ID + "=?";
		final String[] whereArgs = new String[] { String.valueOf(model.noteId) };

		ContentValues values = new ContentValues();

		values.put(NOTE_UUID, model.noteUUId);
		values.put(NOTE_ATLAS_ID, model.noteAtlasId);

		values.put(NOTE_TITLE, model.noteTitle);
		values.put(NOTE_BODY, model.noteBody);

		values.put(NOTE_CALENDAR_NAME, model.noteCalendarName);
		values.put(NOTE_CALENDAR_COLOR, model.noteCalendarColor);

		values.put(NOTE_DATE_CREATED, toGmtString(model.noteDateCreated));
		values.put(NOTE_MODIFIED_DATE, toGmtString(model.noteModifiedDate));

		values.put(NOTE_AUTHOR_ID, model.noteAuthorId);
		values.put(NOTE_AUTHOR_NAME, model.noteAuthorName);

		values.put(NOTE_IS_SELECTED_STAR, model.noteIsStarred ? 1 : 0);

		db.update(DBDefines.ATL_NOTE, values, whereClause, whereArgs);
		db.close();

		return true;
	}

	private String toGmtString(Date date) {
		// date formatter
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
		// sd.setTimeZone(TimeZone.getTimeZone("UTC"));
		String return_date = sd.format(date);
		return return_date;
	}

	private Date stringToDate(String dateString) {
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

}
