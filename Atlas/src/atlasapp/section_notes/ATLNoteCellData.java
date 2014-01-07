//  ==================================================================================================================
//  ATLNotesCellData.java
//  AtlasConsumerAndroid
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-12-28 NGHIA:   Add copy(), noteCellDateCreated, noteCellModifiedDate, noteCellSortString, noteCellAuthorID and noteCellAuthorName 
//  2012-10-29 TAN:     init class to implement Notes View in simulate mode
//  ==================================================================================================================

package atlasapp.section_notes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.util.Log;

import atlasapp.model.ATLAttendeeModel;
import atlasapp.model.ATLNoteModel;

public class ATLNoteCellData {
	static public final int NOTE_SORT_STARRED = 0;
	static public final int NOTE_SORT_DATECREATED = 1;
	static public final int NOTE_SORT_LASTMODIFIED = 2;
	static public final int NOTE_SORT_SHARED = 3;
	// PROPERTIES
	public int noteCellId;
	public String noteCellUUId;
	public String noteCellAtlasId;

	public String noteCellTitle;
	public String noteCellBody;

	public String noteCellCalendarName;
	public int noteCellCalendarColor;

	public Date noteCellDateCreated;
	public Date noteCellModifiedDate;

	public String noteCellSortString;
	public int sectionNumber;

	public ArrayList<ATLAttendeeModel> noteCellAttendeeList;// use for group
	public ATLAttendeeModel noteCellAttendee; // 20120920 use for one assinee,
												// will delete later
	public String noteCellAuthorId;
	public String noteCellAuthorName;

	public boolean isSelectedStar = false;

	public String noteDelegatedName = "";

	// METHODS

	public ATLNoteCellData() {
		// TODO Auto-generated constructor stub
		this.noteCellUUId = "abc";
		this.noteCellTitle = "";
		this.noteCellBody = "";
		this.noteCellDateCreated = new Date();
		this.noteCellAttendeeList = new ArrayList<ATLAttendeeModel>();
		this.isSelectedStar = false;
	}

	public ATLNoteCellData copy() {
		// TODO Auto-generated method stub
		ATLNoteCellData temp = new ATLNoteCellData();
		temp.noteCellId = this.noteCellId;
		temp.noteCellUUId = this.noteCellUUId;
		temp.noteCellAtlasId = this.noteCellAtlasId;
		temp.noteCellTitle = this.noteCellTitle;
		temp.noteCellBody = this.noteCellBody;
		temp.noteCellCalendarName = this.noteCellCalendarName;
		temp.noteCellCalendarColor = this.noteCellCalendarColor;
		temp.noteCellDateCreated = this.noteCellDateCreated;
		temp.noteCellModifiedDate = this.noteCellModifiedDate;
		temp.noteCellSortString = this.noteCellSortString;
		temp.noteCellAttendee = this.noteCellAttendee;
		temp.noteCellAttendeeList = this.noteCellAttendeeList;
		temp.noteCellAuthorId = this.noteCellAuthorId;
		temp.noteCellAuthorName = this.noteCellAuthorName;
		temp.isSelectedStar = this.isSelectedStar;
		temp.sectionNumber = this.sectionNumber;
		temp.noteDelegatedName = this.noteDelegatedName;
		return temp;
	}

	public ATLNoteCellData(ATLNoteModel noteModel) {
		// TODO Auto-generated constructor stub

		noteCellId = noteModel.noteId;
		noteCellUUId = noteModel.noteUUId;
		noteCellAtlasId = noteModel.noteAtlasId;

		noteCellSortString = "";

		noteCellTitle = noteModel.noteTitle;
		noteCellBody = noteModel.noteBody;

		noteCellCalendarName = noteModel.noteCalendarName;
		noteCellCalendarColor = noteModel.noteCalendarColor;

		noteCellDateCreated = noteModel.noteDateCreated;
		noteCellModifiedDate = noteModel.noteModifiedDate;

		noteCellAuthorId = noteModel.noteAuthorId;
		noteCellAuthorName = noteModel.noteAuthorName;
		isSelectedStar = noteModel.noteIsStarred;
		noteDelegatedName = "";
	}

	public String sortStringFromDateCreated() {
		String a = "";
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		if (noteCellDateCreated != null) {

			a = a + sf.format(noteCellDateCreated);
		}
		return a;

	}

	public String sortStringFromModifiedDate() {
		String a = "";
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		if (noteCellModifiedDate != null) {

			a = a + sf.format(noteCellModifiedDate);
		}
		return a;

	}

	public String sortStringFromShared() {
		String a = "";
		int lengh = noteDelegatedName.trim().length();
		if (lengh > 4) {
			a = noteDelegatedName.trim().substring(0, 4);
		} else {
			a += noteDelegatedName.trim();
			for (int i = 0; i < (4 - lengh); i++) {
				a += "A";
			}
		}
		return a;

	}

	public String sortStringFromStarred() {
		if (this.isSelectedStar) {
			return "1";
		}
		return "0";

	}

	public String sortStringFromIndex(int index) {
		switch (index) {
		case NOTE_SORT_STARRED:
			return sortStringFromStarred();
		case NOTE_SORT_DATECREATED:
			return sortStringFromDateCreated();
		case NOTE_SORT_LASTMODIFIED:
			return sortStringFromModifiedDate();
		case NOTE_SORT_SHARED:
			return sortStringFromShared();
		default:
			break;
		}
		return "";

	}

	public String createSortString() {

		String string0 = "";
		string0 = sortStringFromIndex(ATLNoteSortSingleTon.sortIndex[0]);

		String string1 = "";
		string1 = sortStringFromIndex(ATLNoteSortSingleTon.sortIndex[1]);

		String string2 = "";
		string2 = sortStringFromIndex(ATLNoteSortSingleTon.sortIndex[2]);

		String string3 = "";
		string3 = sortStringFromIndex(ATLNoteSortSingleTon.sortIndex[3]);

		this.noteCellSortString = string0 + string1 + string2 + string3;
		Log.v("sortstring", noteCellSortString);
		return this.noteCellSortString;

	}

}
