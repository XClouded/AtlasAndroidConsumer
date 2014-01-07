//  ==================================================================================================================
//  ATLNoteModel.java
//  AtlasConsumerAndroid
//  Copyright (c) 2012 Atlas Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//	2012-12-30 NGHIA:	    Create class
//  ==================================================================================================================

package com.atlasapp.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

import com.atlasapp.common.DateTimeUtilities;
import com.atlasapp.section_notes.ATLNoteCellData;
import com.google.api.client.util.DateTime;
import com.google.api.services.tasks.model.Task;

public class ATLNoteModel {
	public int noteId = -1;
	public String noteUUId = "";
	public String noteAtlasId = "";
	public String noteTitle = "";
	public String noteBody = "";
	public String noteCalendarName = "";
	public int noteCalendarColor = 0;
	public String noteAuthorName = "";
	public String noteAuthorId = "";
	public boolean noteIsStarred = false;
	public Date noteDateCreated = new Date();
	public Date noteModifiedDate = new Date();

	public ATLNoteModel() {
		// TODO Auto-generated constructor stub
	}

	public ATLNoteModel(ATLNoteCellData noteCellData) {
		
		noteId = noteCellData.noteCellId;
		noteUUId = noteCellData.noteCellUUId;
		noteAtlasId = noteCellData.noteCellAtlasId;

		noteTitle = noteCellData.noteCellTitle;
		noteBody = noteCellData.noteCellBody;

		noteCalendarName = noteCellData.noteCellCalendarName;
		noteCalendarColor = noteCellData.noteCellCalendarColor;

		noteDateCreated = noteCellData.noteCellDateCreated;
		noteModifiedDate = noteCellData.noteCellModifiedDate;

		noteAuthorId = noteCellData.noteCellAuthorId;
		noteAuthorName = noteCellData.noteCellAuthorName;
		noteIsStarred = noteCellData.isSelectedStar;
	}

	private Date stringToDate(String dateString) {
		// 1985-04-12T23:20:50.52Z
		SimpleDateFormat sd = new SimpleDateFormat(
				"yyyy-MM-dd'T'H:mm:ss.SSS'Z'");
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
