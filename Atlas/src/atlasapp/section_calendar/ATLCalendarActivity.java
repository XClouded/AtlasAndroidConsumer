//  ==================================================================================================================
//  ATLCalendarActivity.java
//  AtlasConsumerAndroid
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-10-21 TAN:    Init class
//  2012-10-25 TAN:    Add private attributes ATLCalendarListAdapter adapter;ATLCalCellList aCalCellList; to handle when data of the View
//					   did changed
//  ==================================================================================================================

package atlasapp.section_calendar;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


import atlasapp.section_appentry.R;
import atlasapp.section_main.ATLMainTabbarActivity;
import atlasapp.section_main.ATLTopbar;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


public class ATLCalendarActivity extends Activity implements View.OnClickListener{
	LayoutInflater mInflater;
	Context mContext;
	View mLayout;
	ATLTopbar mTopBar;
	ListView listEvent;
	ImageButton backArrowBtn;
	ImageButton forwardArrowBtn;
	TextView dateTime;
	Date currentDate = new Date();
	ATLCalendarListAdapter adaper;
	ATLCalCellList aCalCellList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater = LayoutInflater.from(this);
		mLayout = (View)mInflater.inflate(R.layout.calendar, null);
		setContentView(mLayout);
		initAttributes();
		setListener();
		bindingData();
		
	}


	// Init View attributes
	private void initAttributes() {
		// TODO Auto-generated method stub
		mTopBar = (ATLTopbar)findViewById(R.id.topBarMenu);
		mTopBar.setType(ATLMainTabbarActivity.TAB_CALENDAR);
		
//		listEvent = (ListView) findViewById(R.id.listEvents);
//		aCalCellList = new ATLCalCellList();
//		adaper = new ATLCalendarListAdapter(aCalCellList, this);
//		listEvent.setAdapter(adaper);
//		
//		backArrowBtn = (ImageButton)findViewById(R.id.backArrowBtn);
//		forwardArrowBtn = (ImageButton)findViewById(R.id.forwardArrowBtn);
//		dateTime = (TextView) findViewById(R.id.dataTimeText);
	}
	
	// To set data for UI
	private void bindingData() {
		// TODO Auto-generated method stub
		dateTime.setText(DateFormat.format("EEEE, MMM d, yyyy", currentDate));
	}
	
	// Register UI touch listener 
	private void setListener() {
		// TODO Auto-generated method stub
		backArrowBtn.setOnClickListener(this);
		forwardArrowBtn.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		cal.setTime(currentDate);
		if(v == backArrowBtn){
			// TAN - still have bug at the first time call
			Calendar calTemp = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
			calTemp.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
					cal.get(Calendar.DAY_OF_MONTH) - 1, 0, 10, 0);
			currentDate = calTemp.getTime();
		}
		else if(v == forwardArrowBtn){
			Calendar calTemp = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
			calTemp.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
					cal.get(Calendar.DAY_OF_MONTH) + 1, 0, 10, 0);
			currentDate = calTemp.getTime();
		}
		aCalCellList.currentDateDidChanged(currentDate);
		bindingData();
		adaper.notifyDataSetChanged();
		
	}

}
