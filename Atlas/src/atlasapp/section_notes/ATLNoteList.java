//  ==================================================================================================================
//  ATLNotesCellList.java
//  AtlasConsumerAndroid
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-10-29 TAN:     init class to implement Notes View in simulate mode
//  ==================================================================================================================

package atlasapp.section_notes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import atlasapp.common.CalendarUtilities;
import atlasapp.model.ATLNoteModel;

import android.graphics.Color;

public class ATLNoteList {
	// PROPERTIES
	public ArrayList<ArrayList<ATLNoteCellData>> noteListArray;
	public ArrayList<String> headerList;
	public int noteListCount;
	public Date noteListDate;
	public boolean noteListSimulate;

	// METHODS

	public ATLNoteList() {
		// TODO Auto-generated constructor stub
		// SIMULATION: TURN OFF WHEN REAL DATA IS AVAILABLE
		// ==================================<<<<<<
		// WHEN ON, FAKE CELL DATA WILL BE CREATED
		this.noteListSimulate = false;
		this.noteListArray = new ArrayList<ArrayList<ATLNoteCellData>>();
		this.noteListDate = new Date();

	}

	public boolean load() {
		// load data for one day
		if (this.noteListSimulate) {
			// fake the cells
			initDataForSimulate();

		} // END IF SIMULATE

		else {

			this.noteListArray = new ArrayList<ArrayList<ATLNoteCellData>>();

			ArrayList<ATLNoteCellData> noteCellDataList = new ArrayList<ATLNoteCellData>();

			ATLNoteDatabaseAdapter dbHelper = new ATLNoteDatabaseAdapter();
			ArrayList<ATLNoteModel> list = new ArrayList<ATLNoteModel>();
			list = dbHelper.loadAllNoteInDatabase();
			for (ATLNoteModel noteModel : list) {
				ATLNoteCellData newCell = new ATLNoteCellData(noteModel);
				newCell.createSortString();
				noteCellDataList.add(newCell);
			}

			this.headerList = headerListByNoteCellDataList(noteCellDataList);
			this.noteListArray = noteSortListByNoteCellDataListAndHeaderList(
					noteCellDataList, this.headerList);

		}

		return true;
	}

	private void initDataForSimulate() {
		ATLNoteCellData newCell1 = new ATLNoteCellData();
		newCell1.noteCellDateCreated = this.noteListDate;
		newCell1.noteCellTitle = "ABC on Wayne Enterprise";
		newCell1.noteCellBody = "This is Body of Notes 1";
		newCell1.noteCellCalendarName = "Work";
		newCell1.noteCellCalendarColor = Color.RED;
		this.addCell(newCell1);

		// fake the cells
		ATLNoteCellData newCell2 = new ATLNoteCellData();
		newCell2.noteCellDateCreated = this.noteListDate;
		newCell2.noteCellTitle = "Thoughts on Wayne Enterprise";
		newCell2.noteCellBody = "This is Body of Notes 1";
		newCell2.noteCellCalendarName = "Work";
		newCell2.noteCellCalendarColor = Color.GREEN;
		this.addCell(newCell2);

		// fake the cells
		ATLNoteCellData newCell3 = new ATLNoteCellData();
		newCell3.noteCellDateCreated = this.noteListDate;
		newCell3.noteCellTitle = "Thoughts on Wayne Enterprise";
		newCell3.noteCellBody = "This is Body of Notes 1";
		newCell3.noteCellCalendarName = "Work";
		newCell3.noteCellCalendarColor = Color.BLUE;
		this.addCell(newCell3);

		// fake the cells
		ATLNoteCellData newCell4 = new ATLNoteCellData();
		newCell4.noteCellDateCreated = this.noteListDate;
		newCell4.noteCellTitle = "Thoughts on Wayne Enterprise";
		newCell4.noteCellBody = "This is Body of Notes 1";
		newCell4.noteCellCalendarName = "Work";
		newCell4.noteCellCalendarColor = Color.YELLOW;
		this.addCell(newCell4);

		// fake the cells
		ATLNoteCellData newCell5 = new ATLNoteCellData();
		newCell5.noteCellDateCreated = this.noteListDate;
		newCell5.noteCellTitle = "Thoughts on Wayne Enterprise";
		newCell5.noteCellBody = "This is Body of Notes 1";
		newCell5.noteCellCalendarName = "Work";
		newCell5.noteCellCalendarColor = Color.CYAN;
		this.addCell(newCell5);

		// fake the cells
		ATLNoteCellData newCell6 = new ATLNoteCellData();
		newCell6.noteCellDateCreated = this.noteListDate;
		newCell6.noteCellTitle = "Thoughts on Wayne Enterprise";
		newCell6.noteCellBody = "This is Body of Notes 1";
		newCell6.noteCellCalendarName = "Work";
		newCell6.noteCellCalendarColor = Color.RED;
		this.addCell(newCell6);

		// fake the cells
		ATLNoteCellData newCell7 = new ATLNoteCellData();
		newCell7.noteCellDateCreated = this.noteListDate;
		newCell7.noteCellTitle = "Thoughts on Wayne Enterprise";
		newCell7.noteCellBody = "This is Body of Notes 1";
		newCell7.noteCellCalendarName = "Work";
		newCell7.noteCellCalendarColor = Color.RED;
		this.addCell(newCell7);
		// fake the cells
		ATLNoteCellData newCell8 = new ATLNoteCellData();
		newCell8.noteCellDateCreated = this.noteListDate;
		newCell8.noteCellTitle = "Thoughts on Wayne Enterprise";
		newCell8.noteCellBody = "This is Body of Notes 1";
		newCell8.noteCellCalendarName = "Work";
		newCell8.noteCellCalendarColor = Color.RED;
		this.addCell(newCell8);
		// fake the cells
		ATLNoteCellData newCell9 = new ATLNoteCellData();
		newCell9.noteCellDateCreated = this.noteListDate;
		newCell9.noteCellTitle = "Thoughts on Wayne Enterprise";
		newCell9.noteCellBody = "This is Body of Notes 1";
		newCell9.noteCellCalendarName = "Work";
		newCell9.noteCellCalendarColor = Color.RED;
		this.addCell(newCell9);
		// fake the cells
		ATLNoteCellData newCell10 = new ATLNoteCellData();
		newCell10.noteCellDateCreated = this.noteListDate;
		newCell10.noteCellTitle = "Thoughts on Wayne Enterprise";
		newCell10.noteCellBody = "This is Body of Notes 1";
		newCell10.noteCellCalendarName = "Work";
		newCell10.noteCellCalendarColor = Color.RED;
		this.addCell(newCell10);
		// fake the cells
		ATLNoteCellData newCell11 = new ATLNoteCellData();
		newCell11.noteCellDateCreated = this.noteListDate;
		newCell11.noteCellTitle = "Thoughts on Wayne Enterprise";
		newCell11.noteCellBody = "This is Body of Notes 1";
		newCell11.noteCellCalendarName = "Work";
		newCell11.noteCellCalendarColor = Color.RED;
		this.addCell(newCell11);
	}

	private ArrayList<ArrayList<ATLNoteCellData>> noteSortListByNoteCellDataListAndHeaderList(
			ArrayList<ATLNoteCellData> noteCellDataList,
			ArrayList<String> aHeaderList) {
		// TODO Auto-generated method stub

		ArrayList<ArrayList<ATLNoteCellData>> noteSortList = new ArrayList<ArrayList<ATLNoteCellData>>();
		for (int i = 0; i < aHeaderList.size(); i++) {
			ArrayList<ATLNoteCellData> array = new ArrayList<ATLNoteCellData>();
			noteSortList.add(array);
		}

		for (ATLNoteCellData noteCellData : noteCellDataList) {
			this.addNote(noteCellData, noteSortList, aHeaderList);
		}
		// TODO Sort before return

		return noteSortList;
	}

	private void addNote(ATLNoteCellData noteCellData,
			ArrayList<ArrayList<ATLNoteCellData>> noteSortList,
			ArrayList<String> aHeaderList) {

		noteCellData.noteCellSortString = this
				.sortStringFromNoteCell(noteCellData);

		switch (ATLNoteSortSingleTon.getGroupKind()) {
		case ATLNoteCellData.NOTE_SORT_STARRED: {
			this.addNoteWhenStarredSort(noteCellData, noteSortList, aHeaderList);
			break;
		}
		case ATLNoteCellData.NOTE_SORT_DATECREATED: {
			this.addNoteWhenDateCreatedSort(noteCellData, noteSortList,
					aHeaderList);
			break;
		}
		case ATLNoteCellData.NOTE_SORT_LASTMODIFIED: {
			this.addNoteWhenLastModifiedSort(noteCellData, noteSortList,
					aHeaderList);
			break;
		}
		case ATLNoteCellData.NOTE_SORT_SHARED: {
			this.addNoteWhenSharedSort(noteCellData, noteSortList, aHeaderList);
			break;
		}
		default:
			break;
		}

	}

	private void addNoteWhenSharedSort(ATLNoteCellData noteCellData,
			ArrayList<ArrayList<ATLNoteCellData>> noteSortList,
			ArrayList<String> aHeaderList) {
		// TODO Need to implement when has contact
		boolean isAdded = false;
		for (int i = 0; i< headerList.size();i++) {
			String delegateName = headerList.get(i);
			if(noteCellData.noteDelegatedName.equals(delegateName)){
				ArrayList<ATLNoteCellData> array = noteSortList.get(i);
				array.add(noteCellData);
				isAdded = true;
				break;
			}
		}
		if(!isAdded){
			ArrayList<ATLNoteCellData> array = noteSortList.get(noteSortList.size()-1);
			array.add(noteCellData);
		}

	}

	private void addNoteWhenDateCreatedSort(ATLNoteCellData noteCellData,
			ArrayList<ArrayList<ATLNoteCellData>> noteSortList,
			ArrayList<String> aHeaderList) {
		// TODO Filter noteCellDateCreated base on { TODAY, PAST_WEEK,
		// PAST_MONTH, OLDER }

		ArrayList<ATLNoteCellData> noteTodayList = noteSortList.get(0);
		ArrayList<ATLNoteCellData> notePastWeekList = noteSortList.get(1);
		ArrayList<ATLNoteCellData> notePastMonthList = noteSortList.get(2);
		ArrayList<ATLNoteCellData> noteOrderList = noteSortList.get(3);
		if (CalendarUtilities.isPast(noteCellData.noteCellDateCreated)) {
			if (CalendarUtilities.isPastWeek(noteCellData.noteCellDateCreated)) {
				notePastWeekList.add(noteCellData);
			} else if (CalendarUtilities
					.isPastMonth(noteCellData.noteCellDateCreated)) {
				notePastMonthList.add(noteCellData);
			} else {
				noteOrderList.add(noteCellData);
			}

		} else {
			noteTodayList.add(noteCellData);
		}

	}

	private void addNoteWhenLastModifiedSort(ATLNoteCellData noteCellData,
			ArrayList<ArrayList<ATLNoteCellData>> noteSortList,
			ArrayList<String> aHeaderList) {
		// TODO Auto-generated method stub
		ArrayList<ATLNoteCellData> noteTodayList = noteSortList.get(0);
		ArrayList<ATLNoteCellData> notePastWeekList = noteSortList.get(1);
		ArrayList<ATLNoteCellData> notePastMonthList = noteSortList.get(2);
		ArrayList<ATLNoteCellData> noteOrderList = noteSortList.get(3);
		if (CalendarUtilities.isPast(noteCellData.noteCellModifiedDate)) {
			if (CalendarUtilities.isPastWeek(noteCellData.noteCellModifiedDate)) {
				notePastWeekList.add(noteCellData);
			} else if (CalendarUtilities
					.isPastMonth(noteCellData.noteCellModifiedDate)) {
				notePastMonthList.add(noteCellData);
			} else {
				noteOrderList.add(noteCellData);
			}

		} else {
			noteTodayList.add(noteCellData);
		}
	}

	private void addNoteWhenStarredSort(ATLNoteCellData noteCellData,
			ArrayList<ArrayList<ATLNoteCellData>> noteSortList,
			ArrayList<String> aHeaderList) {
		ArrayList<ATLNoteCellData> noteStarredList = noteSortList.get(0);
		ArrayList<ATLNoteCellData> noteNotStarredList = noteSortList.get(1);
		if (noteCellData.isSelectedStar) {
			noteCellData.sectionNumber = 0;
			noteStarredList.add(noteCellData);
		} else {
			noteCellData.sectionNumber = 1;
			noteNotStarredList.add(noteCellData);
		}
	}

	private String sortStringFromNoteCell(ATLNoteCellData noteCellData) {
		// TODO Auto-generated method stub
		return null;
	}

	public final String STARRED = "Starred Notes";
	public final String NOT_STARRED = "Notes";
	public final String TODAY = "TODAY";
	public final String PAST_WEEK = "PAST WEEK";
	public final String PAST_MONTH = "PAST MONTH";
	public final String OLDER = "OLDER";

	private ArrayList<String> headerListByNoteCellDataList(
			ArrayList<ATLNoteCellData> noteCellDataList) {
		// TODO Auto-generated method stub
		switch (ATLNoteSortSingleTon.getGroupKind()) {
		case ATLNoteCellData.NOTE_SORT_STARRED: {
			ArrayList<String> aHeaderList = new ArrayList<String>();
			aHeaderList.add(STARRED);
			aHeaderList.add(NOT_STARRED);
			return aHeaderList;
		}
		case ATLNoteCellData.NOTE_SORT_DATECREATED: {
			ArrayList<String> aHeaderList = new ArrayList<String>();
			aHeaderList.add(TODAY);
			aHeaderList.add(PAST_WEEK);
			aHeaderList.add(PAST_MONTH);
			aHeaderList.add(OLDER);
			return aHeaderList;
		}
		case ATLNoteCellData.NOTE_SORT_LASTMODIFIED: {
			ArrayList<String> aHeaderList = new ArrayList<String>();
			aHeaderList.add(TODAY);
			aHeaderList.add(PAST_WEEK);
			aHeaderList.add(PAST_MONTH);
			aHeaderList.add(OLDER);
			return aHeaderList;
		}
		case ATLNoteCellData.NOTE_SORT_SHARED: {
			// String[] aHeaderList = { STARRED, NOT_STARRED };
			// return aHeaderList;
			// }
			ArrayList<String> headerArrList = new ArrayList<String>();
			for (Object atlCal : noteCellDataList) {
				String delegatedName = ((ATLNoteCellData) atlCal).noteDelegatedName;
				if (delegatedName.equals("")) {
					delegatedName = "Not Delegated";
				}
				boolean isExist = false;
				for (String name : headerArrList) {

					if (delegatedName.equals(name)) {
						isExist = true;
						break;
					}

				}
				if (!isExist) {
					headerArrList.add(delegatedName);
				}
			}
			Collections.sort(headerArrList, new IgnoreStringCaseComparator());
			headerArrList.remove("Not Delegated");
			headerArrList.add("Not Delegated");// Add to the end of list
			return headerArrList;
		}
		}
		return null;
	}

	public boolean save() {
		return true;
	}

	public void clear() {
		this.noteListArray.clear();
	}

	public void addCell(ATLNoteCellData notesCellData) {
		// TODO need check index to make sure how to save
		// this.noteListArray.add(notesCellData);
	}

	public void removeCell(ATLNoteCellData notesCellData) {
		this.noteListArray.remove(noteListCount);
	}

	public void currentDateDidChanged(Date currentDate) {
		// TODO Auto-generated method stub
		this.noteListDate = currentDate;
		this.load();
	}

	public class ATLNoteCellDataComparator implements
			Comparator<ATLNoteCellData> {
		@Override
		public int compare(ATLNoteCellData o1, ATLNoteCellData o2) {
			return o1.noteCellSortString.compareTo(o2.noteCellSortString);
		}
	}

	public class IgnoreStringCaseComparator implements Comparator<String> {
		@Override
		public int compare(String o1, String o2) {
			return o1.trim().toUpperCase().compareTo(o2.trim().toUpperCase());
		}
	}
}
