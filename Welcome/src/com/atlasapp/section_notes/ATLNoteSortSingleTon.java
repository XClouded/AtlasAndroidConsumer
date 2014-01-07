//==================================================================================================================
//ATLNoteSortSingleTon.java
//ATLAS
//Copyright (c) 2012 ATLAS Apps. All rights reserved.
//==================================================================================================================
//
//==================================================================================================================
//HISTORY
//YYYY-MM-DD NAME:    Description of changes
//==================================================================================================================
//2013-01-01 NGHIA:    Create class
//==================================================================================================================

package com.atlasapp.section_notes;

public class ATLNoteSortSingleTon {

	public static int[] sortIndex = { ATLNoteCellData.NOTE_SORT_STARRED,
			ATLNoteCellData.NOTE_SORT_DATECREATED,
			ATLNoteCellData.NOTE_SORT_LASTMODIFIED,
			ATLNoteCellData.NOTE_SORT_SHARED };

	public static int getGroupKind() {
		return sortIndex[0];
	}
}
