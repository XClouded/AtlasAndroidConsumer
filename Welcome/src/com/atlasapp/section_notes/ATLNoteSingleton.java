//==================================================================================================================
//ATLNoteSingleton.java
//AtlasConsumerAndroid
//Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//==================================================================================================================
//
//==================================================================================================================
//HISTORY
//YYYY-MM-DD NAME:    Description of changes
//==================================================================================================================
//2012-12-28 NGHIA:     Init class : For contain data of edit note view
//==================================================================================================================

package com.atlasapp.section_notes;

public class ATLNoteSingleton {

	public static boolean isNewNote = false;
	private static ATLNoteSingleton instance;
	private ATLNoteCellData noteCellData = new ATLNoteCellData();

	public static ATLNoteSingleton getInstance() {
		if (instance == null) {
			instance = new ATLNoteSingleton();
		}
		return instance;

	}

	public void destroy() {
		if (instance != null) {
			instance = null;
			noteCellData = null;
			isNewNote = true;
		}
	}

	public void setData(ATLNoteCellData aNoteCellData) {
		isNewNote = false;
		ATLNoteCellData temp = new ATLNoteCellData();
		temp = aNoteCellData.copy();
		this.noteCellData = temp;
	}

	public ATLNoteCellData getNoteCellData() {
		return noteCellData;
	}

}
