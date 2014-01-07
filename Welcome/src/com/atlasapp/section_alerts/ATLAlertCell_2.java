//  ==================================================================================================================
//  ATLAlertCell_2.java
//  ATLAS
//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================

package com.atlasapp.section_alerts;

import java.util.Date;

import com.atlasapp.atlas_database.DatabaseConstants.ALERT_STATUS;
import com.atlasapp.atlas_database.EventProperties;
import com.atlasapp.common.ATLColor;
import com.atlasapp.common.ATLHumanizedTime;
import com.atlasapp.common.Utilities;
import com.atlasapp.section_appentry.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

/**
 * @author Ryan Tan
 * 
 */
public class ATLAlertCell_2 extends RelativeLayout
{

	Context mActivity;
	ATLAlert alert;
	public TextView title;
	public TextView organizerName;
	public TextView createdDateText;

	public ATLAlertCell_2(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public ATLAlertCell_2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public ATLAlertCell_2(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

		initView(context);
	}

	private void initView(Context context) {
		// TODO Auto-generated method stub
		LayoutInflater.from(context).inflate(R.layout.alert_cell_2, this, true);
		mActivity = (Activity) context;
		title = (TextView) findViewById(R.id.alerts_event_title);
		organizerName = (TextView) findViewById(R.id.alerts_organizer_name);
		createdDateText = (TextView) findViewById(R.id.alerts_event_created_date);
		alert = new ATLAlert();
	}

//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		if (alert.type == ALERT_STATUS.YOURMOVE) {
//			Intent i = new Intent();
//			AlertRequestEventSingleton.getInstance().setAlert(alert);
//			i.setClass(mActivity, ATLAlertEventRequest_2.class);
//			mActivity.startActivity(i);
//			((Activity) mActivity).overridePendingTransition(
//					R.anim.in_from_bottom, R.anim.stand_by);
//		}
//
//	}

	public void setAlert(ATLAlert data) {
		// TODO Auto-generated method stub
		alert = data;
		for (EventProperties event : alert.eventList) {
			String titleText = Utilities.deNull(event.title);
			String organizerNameText = Utilities.deNull(alert.inviterContact.displayName());
			
			//===========================================
			// Set cell event title #START
			//===========================================
			// this is the text we'll be operating on  
	        String key = "Event: ";
	        SpannableString titleSStr = new SpannableString(key + titleText);
	        // make "key" (characters 0 to key.length()) White  bold
	        titleSStr.setSpan(new ForegroundColorSpan(Color.WHITE), 0, key.length(), 0);
	        titleSStr.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, key.length(), 0);
	        
	        // make "key" (characters 0 to key.length()) White Bold
	        titleSStr.setSpan(new ForegroundColorSpan(ATLColor.blue_event_title), key.length(),key.length()+ titleText.length(), 0);
	        titleSStr.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),key.length(),key.length()+ titleText.length(), 0);  
	        
	        title.setText(titleSStr, BufferType.SPANNABLE);
	        //===========================================
	        // Set cell event title #END
	        //=========================================== 
	       
	        //===========================================
	        // Set cell event organizer #START
	        //=========================================== 
	        String key1 = "Organizer: ";
	        SpannableString organizerText = new SpannableString(key1 + organizerNameText);
	        // make "key" (characters 0 to key.length()) White  bold
	        organizerText.setSpan(new ForegroundColorSpan(Color.WHITE), 0, key1.length(), 0);
	        organizerText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, key1.length(), 0);
	        
	        // make "key" (characters 0 to key.length()) White Bold
	        organizerText.setSpan(new ForegroundColorSpan(Color.WHITE), key1.length(),key1.length()+ organizerNameText.length(), 0);
	//		        organizerText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),key1.length(),key1.length()+ organizerNameText.length(), 0);  
	        
	        organizerName.setText(organizerText, BufferType.SPANNABLE);        
	        //===========================================
	        // Set cell event organizer #END
	        //=========================================== 
		    //===========================================
		    // Set cell event humanized Time #START
		    //=========================================== 
	        String key3 = "";
	    	if (alert.type == ALERT_STATUS.YOURMOVE) {
	    		key3 = "You have been invited ";
			} 
			else if(alert.type == ALERT_STATUS.PENDING) {
				key3 = "Event was created ";
			}
			else{
				key3 = "Event was booked ";
			}
	    	
	    	if (event.modifiedDatetime == null){
	    		event.modifiedDatetime = new Date();
	    	}
	    	
	    	if (event.modifiedDatetime != null) {
				String humanizedTime = ATLHumanizedTime.toStringWithHumanizedTimeDiffrence(event.modifiedDatetime);
				createdDateText.setText(key3 + humanizedTime);
			}
			else {
				createdDateText.setText(key3);
			}
		    //===========================================
		    // Set cell event organizer #END
		    //=========================================== 		

    		break;
		}
	}

}
