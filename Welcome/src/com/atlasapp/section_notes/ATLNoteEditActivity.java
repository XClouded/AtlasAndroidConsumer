//==================================================================================================================
//EditTask.java
//ATLAS
//Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//==================================================================================================================
//
//==================================================================================================================
//HISTORY
//YYYY-MM-DD NAME:    Description of changes
//==================================================================================================================
//2012-12-26 NGHIA:     init class
//==================================================================================================================

package com.atlasapp.section_notes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;

import com.atlasapp.model.ATLCalendarModel;
import com.atlasapp.section_appentry.R;
import com.atlasapp.section_calendar.ATLCalendarManager;
import com.atlasapp.section_calendar.ATLCalendarScrollListAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

/**
 * @author nghia
 * 
 */

public class ATLNoteEditActivity extends Activity implements OnClickListener,
		OnWheelChangedListener {
	// private ATLTaskCellData taskTemp;
	LayoutInflater mInflater;
	View mLayout;
	ImageButton saveButton;
	ImageButton cancelButton;

	EditText titleEditText;
	EditText bodyEditText;

	WheelView calendarListView;
	ArrayList<ATLCalendarModel> calendarListModel;
	ATLNoteCellData noteTemp;
	TwoStateButton starButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater = LayoutInflater.from(this);
		mLayout = (View) mInflater.inflate(R.layout.note_edit, null);
		setContentView(mLayout);
		noteTemp = ATLNoteSingleton.getInstance().getNoteCellData();
		initAttributes();
		bindingData();
		setListener();
	}

	private void initAttributes() {
		// TODO Auto-generated method stub
		cancelButton = (ImageButton) findViewById(R.id.cancelButton);
		saveButton = (ImageButton) findViewById(R.id.saveButton);

		titleEditText = (EditText) findViewById(R.id.note_edit_title);
		bodyEditText = (EditText) findViewById(R.id.note_edit_body);

		// Init star buttons
		starButton = new TwoStateButton(this,
				R.drawable.note_edit_bottombar_star_unselected,
				R.drawable.note_edit_bottombar_star_selected,
				(ImageButton) findViewById(R.id.note_edit_star));

		calendarListView = (WheelView) findViewById(R.id.note_bottom_bar_calendarlist);
		calendarListModel = ATLCalendarManager.getCalendarList(this);
		calendarListView.setViewAdapter(new ATLCalendarScrollListAdapter(
				calendarListModel, this));
		calendarListView.addChangingListener(this);

		int index = 0;
		for (ATLCalendarModel calendarModel : calendarListModel) {
			if (noteTemp.noteCellCalendarName == null
					|| noteTemp.noteCellCalendarName.equals("")) {// Default
				// value
				calendarListView.setCurrentItem(0);
				// set Calendar Value when Add new Note
				noteTemp.noteCellCalendarColor = calendarListModel.get(0).color;
				noteTemp.noteCellCalendarName = calendarListModel.get(0).name;
				
				break;
			} else if (noteTemp.noteCellCalendarName.equals(calendarModel.name)) {
				calendarListView.setCurrentItem(index);
				break;
			}
			index++;
		}
	}

	private void setListener() {
		// TODO Auto-generated method stub
		saveButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);

		starButton.imgButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Handle All action when touch on any buttons
		if (v == cancelButton) {
			// Call back
			setResult(ATLNoteIntentKeyList.RESULT_FROM_NOTE_EDIT_CANCEL);
			finish();
		} else if (v == saveButton) {
			save();
			if(ATLNoteSingleton.isNewNote){
				setResult(ATLNoteIntentKeyList.RESULT_FROM_NOTE_EDIT_INSERT);
			}else{
				setResult(ATLNoteIntentKeyList.RESULT_FROM_NOTE_EDIT_UPDATE);
			}
			finish();
		} else if (v == starButton.imgButton) {
			starButton.stateChanged();
		}
	}

	private void save() {
		// TODO Copy all data to noteTemp
		noteTemp.noteCellTitle = titleEditText.getText().toString();
		noteTemp.noteCellBody = bodyEditText.getText().toString();
		noteTemp.isSelectedStar =  starButton.state;
		if(noteTemp.isSelectedStar){
			Log.v("isSelectedStar", "isSelectedStar");
		}else{
			Log.v(" not isSelectedStar", "not isSelectedStar");
		}
		Calendar c = new GregorianCalendar(TimeZone.getDefault());
		c.setTime(new Date());
		int amPmInt = c.get(Calendar.AM_PM);
		int curMinutes = c.get(Calendar.MINUTE);
		int today = c.get(Calendar.DAY_OF_MONTH);
		int month = c.get(Calendar.MONTH);
		int year = c.get(Calendar.YEAR);

		int curHours = c.get(Calendar.HOUR);
		if (amPmInt == 0) {
			if (curHours == 12) {
				curHours = 0;
			}
		} else {
			if (curHours != 12) {
				curHours += 12;
			}
		}

		Calendar c1 = Calendar.getInstance(TimeZone.getDefault());
		c1.set(year, month, today, curHours, curMinutes, 0);
		
		if(noteTemp.noteCellDateCreated == null){
			noteTemp.noteCellDateCreated = c1.getTime();
		}
		noteTemp.noteCellModifiedDate = c1.getTime();
	}

	private void bindingData() {
		// TODO Auto-generated method stub

		if (ATLNoteSingleton.isNewNote) { // New Task

		} else { // Edit Note

			titleEditText.setText(noteTemp.noteCellTitle);
			bodyEditText.setText(noteTemp.noteCellBody);
			
			if (noteTemp.isSelectedStar){
				starButton.stateChanged();
			}

		}
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		if (wheel == calendarListView) {
			noteTemp.noteCellCalendarColor = calendarListModel.get(newValue).color;
			// noteTemp.calendarId = calListModel.get(newValue).id;
			noteTemp.noteCellCalendarName = calendarListModel.get(newValue).name;
		}
	}

	private class TwoStateButton extends RelativeLayout {
		public static final boolean ACTIVE_STATE = true;
		public static final boolean IN_ACTIVE_STATE = false;
		public ImageButton imgButton;
		int imgID0;
		int imgID1;
		public boolean state = IN_ACTIVE_STATE;

		public TwoStateButton(Context context, int imgID0, int imgID1,
				ImageButton imgButon) {
			super(context);
			// TODO Auto-generated constructor stub
			imgButton = new ImageButton(context);
			this.imgID0 = imgID0;
			this.imgID1 = imgID1;
			this.imgButton = imgButon;
			imgButton.setImageResource(imgID0);
			imgButton.setBackgroundColor(Color.TRANSPARENT);
		}

		// public void resetState() {
		// state = IN_ACTIVE_STATE;
		// imgButton.setImageResource(imgID0);
		// imgButton.setBackgroundColor(Color.TRANSPARENT);
		// }

		public void stateChanged() {
			if (state == IN_ACTIVE_STATE) {
				state = ACTIVE_STATE;
				imgButton.setImageResource(imgID1);
				imgButton.setBackgroundColor(Color.TRANSPARENT);
			} else {
				state = IN_ACTIVE_STATE;
				imgButton.setImageResource(imgID0);
				imgButton.setBackgroundColor(Color.TRANSPARENT);
			}
		}

	}
}
