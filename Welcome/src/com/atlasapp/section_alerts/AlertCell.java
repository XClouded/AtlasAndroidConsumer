//  ==================================================================================================================
//  ATLAlertCell.java
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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.atlasapp.common.ATLColor;
import com.atlasapp.common.ATLDragAndDropView;
import com.atlasapp.common.ATLEventCalendarModel;
import com.atlasapp.common.ATLHumanizedTime;
import com.atlasapp.common.ATLConstants.AlertType;
import com.atlasapp.section_appentry.R;

/**
 * @author Ryan Tan
 *
 */
public class AlertCell extends RelativeLayout implements View.OnClickListener {

	Context mActivity;

	public ImageView alertTypeImageView;
	public ImageView backgroundImageView;
	public int calendarColor;
	public ImageView avatarImageView;
	public TextView alertInviterName;
	public TextView alertEventTitle;
	public TextView alertEventDateTime;

	public ImageView dividerImageView;

	ATLEventCalendarModel mCellData;
	boolean isSimulateMode = false;

	public AlertCell(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public AlertCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

		initView(context);
	}

	public AlertCell(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	private void initView(Context context) {
		// Init CallendarCell view
		LayoutInflater.from(context).inflate(R.layout.alert_cell, this, true);
		mActivity = (Activity) context;
		mCellData = new ATLEventCalendarModel();
		alertEventTitle = (TextView) findViewById(R.id.alerts_events_title);
		alertInviterName = (TextView) findViewById(R.id.alerts_inviter_name);
		alertEventDateTime = (TextView) findViewById(R.id.alerts_event_prefered_date);
		avatarImageView = (ImageView) findViewById(R.id.alert_avatar_image);
		alertTypeImageView = (ImageView) findViewById(R.id.alerts_tag_event);
		this.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		if (ATLDragAndDropView.isAtLeft) {
			
	    	Intent i = new Intent();
	    	AlertRequestEventSingleton.getInstance().loadEventProperties(mCellData);
			 i.setClass(mActivity, ATLAlertEventRequest.class);
			mActivity.startActivity(i);
			((Activity)mActivity).overridePendingTransition(R.anim.in_from_bottom, R.anim.stand_by);
			 
		}
		else{
			// Do nothing here 
//			Toast.makeText(getContext(), "Notice \nAlerts is not visible",
//					Toast.LENGTH_SHORT).show();
		}
		
		/*
		if(!mCellData.isHandled){
			switch (mCellData.currentType) {
			    case AlertType.eventInvited_Sent:{
			    	
			    }
			    case AlertType.eventInvited_Received:
			    case AlertType.eventAccepted_Sent:
			    case AlertType.eventAccepted_Received:
			    case AlertType.eventRejected_Sent:
			    case AlertType.eventRejected_Received:{
			    	if (isSimulateMode){
			    	
			    	}
			    	else{
				    	Intent i = new Intent();
						AlertRequestEventSingleton.getInstance().setData(mCellData);
						i.setClass(mActivity, AlertEventRequest.class);
						mActivity.startActivity(i);
						((Activity)mActivity).overridePendingTransition(R.anim.in_from_bottom, R.anim.stand_by);
			    	}
			    }
			      break;
			    case AlertType.taskAssigned_Sent:
			    case AlertType.taskAssigned_Received:
			    case AlertType.taskAccepted_Sent:
			    case AlertType.taskAccepted_Received:
			    case AlertType.taskReject_Sent:
			    case AlertType.taskReject_Receive:
			    case AlertType.taskComplete_Sent:
			    case AlertType.taskComplete_Received:{
			    	if (isSimulateMode){
			    		Intent i = new Intent();
						i.putExtra("senderId", mCellData.alertCellSenderId);
						i.putExtra("senderName", mCellData.alertCellSenderName);
						i.putExtra("cellId", mCellData.alertCellUuid);
						i.setClass(mActivity, AlertTaskRequest.class);
						mActivity.startActivity(i);
			    	}
			    	else{
			    		Intent i = new Intent();
						i.putExtra("senderId", mCellData.alertCellSenderId);
						i.putExtra("senderName", mCellData.alertCellSenderName);
						i.putExtra("alertCellAtlasId", mCellData.alertCellAtlasId);
						i.setClass(mActivity, AlertTaskRequest.class);
						mActivity.startActivity(i);
						((Activity)mActivity).overridePendingTransition(R.anim.in_from_bottom, R.anim.stand_by);
			    	}
			    }
			      break;
			    case AlertType.notesShare_Sent:
			    case AlertType.notesShare_Received:
			    	alertTypeImageView.setImageResource(R.drawable.alerts_tag_notification_notes2x);
			      break;
			    default:
			      break;
			}
		}else{
			ATLAlertDialogUtils.showAlert(mActivity, "Alert", "No action is required for this alert.");
		}
		*/
}

	public void setCellData(ATLEventCalendarModel cellData) {
		// TODO Set title base on alertCellData
		
		Log.v("cellData title", cellData.title);
		mCellData = cellData;
		
		//===========================================
		// Set cell background color #START
		//===========================================
		avatarImageView.setBackgroundColor(Color.TRANSPARENT);
		//===========================================
		// Set cell background color #END
		//===========================================
		//===========================================
		// Set time display #START
		//===========================================
		if(cellData.modifiedDatetime == null){
			cellData.modifiedDatetime = new Date();// TAN hard data for testing
		}
		
		if (cellData.modifiedDatetime != null) {
			Log.v("ATLAlertCell", cellData.modifiedDatetime.toString());
			String humanizedTime = ATLHumanizedTime.toStringWithHumanizedTimeDiffrence(cellData.modifiedDatetime);
			alertEventDateTime.setText(humanizedTime);
		}
		else {
			alertEventDateTime.setText("");
		}
		
		//===========================================
		// Set time display # END
		//===========================================
		//===========================================
		// Set image display #START
		//===========================================
//		switch (cellData.currentType) {
		switch (AlertType.eventRejected_Received) {
		    case AlertType.eventInvited_Sent:{
		    	
		    }
		    case AlertType.eventInvited_Received:
		    case AlertType.eventAccepted_Sent:
		    case AlertType.eventAccepted_Received:
		    case AlertType.eventRejected_Sent:
		    case AlertType.eventRejected_Received:
		    	alertTypeImageView.setImageResource(R.drawable.alerts_tag_notification_calendar2x);
		      break;
		    case AlertType.taskAssigned_Sent:
		    case AlertType.taskAssigned_Received:
		    case AlertType.taskAccepted_Sent:
		    case AlertType.taskAccepted_Received:
		    case AlertType.taskReject_Sent:
		    case AlertType.taskReject_Receive:
		    case AlertType.taskComplete_Sent:
		    case AlertType.taskComplete_Received:
		    	alertTypeImageView.setImageResource(R.drawable.alerts_tag_notification_todo2x);
		      break;
		    case AlertType.notesShare_Sent:
		    case AlertType.notesShare_Received:
		    	alertTypeImageView.setImageResource(R.drawable.alerts_tag_notification_notes2x);
		      break;
		    default:
		      break;
	  }
		//===========================================
		// Set image display #END
		//===========================================
		
		//===========================================
		// Set primary title display #START
		//===========================================
		
		alertEventTitle.setText(buildTitle(cellData), BufferType.SPANNABLE);
		//===========================================
		// Set primary title display # END
		//===========================================
		
		
	}

	private CharSequence buildTitle(ATLEventCalendarModel cellData) {
		// TODO Auto-generated method stub
		{/*
	    	String key = " has invited you to \"";
	        // this is the text we'll be operating on  
	        SpannableString title = new SpannableString("Sender" + key + cellData.title + "\".");
	        // make "SenderName" (characters 0 to key.length()) White  
	        title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, "Sender".length(), 0);
	        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),0, "Sender".length(), 0);
	        
	        // make "key" (characters 0 to key.length()) White Bold
	        title.setSpan(new ForegroundColorSpan(Color.WHITE), "Sender".length() ,
	        		"Sender".length() + key.length(), 0);
	        
	      // make "Description" (characters 0 to key.length()) White  
	        title.setSpan(new ForegroundColorSpan(ATLColor.blue_event_title), "Sender".length() 
	        		+ key.length(), "Sender".length() 
	        		+ key.length()+cellData.title.length(), 0);
	        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),"Sender".length() 
	        		+ key.length(), "Sender".length() 
	        		+ key.length()+cellData.title.length(), 0);
	         
	        return title;    
	        */
			return "";
	    }
//		return cellData.title + " "+ cellData.location;
		
		/*
		switch (cellData.currentType) {
	    case AlertType.eventInvited_Sent:{
	    	 if (cellData.isSentMessageSuccessfully) {
		    	// this is the text we'll be operating on  
		        String key = "Event invite sent to ";
		        SpannableString title = new SpannableString(key + cellData.alertCellRecieverName + ".");
		        // make "key" (characters 0 to key.length()) White  
		        title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, key.length(), 0);
		        
		        // make "key" (characters 0 to key.length()) White Bold
		        title.setSpan(new ForegroundColorSpan(Color.WHITE), key.length(),key.length()+ cellData.alertCellRecieverName.length(), 0);
		        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),key.length(),key.length()+ cellData.alertCellRecieverName.length(), 0);  
		         
		        return title;
	    	 }
	    	 else{ 
	    	    // this is the text we'll be operating on  
		        String key = "Event invite could not be sent to ";
		        SpannableString title = new SpannableString(key + cellData.alertCellRecieverName + ". Please try again.");
		        // make "key" (characters 0 to key.length()) White  
		        title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, key.length(), 0);
		        
		        // make "key" (characters 0 to key.length()) White Bold
		        title.setSpan(new ForegroundColorSpan(Color.WHITE), key.length(),key.length() + cellData.alertCellRecieverName.length(), 0);
		        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),key.length(),key.length() + cellData.alertCellRecieverName.length(), 0);  
		         
		        return title;    
	    	 }
	    }
	    case AlertType.eventInvited_Received:{
	    	String key = " has invited you to \"";
	        // this is the text we'll be operating on  
	        SpannableString title = new SpannableString(cellData.alertCellSenderName + key + cellData.alertCellEventTitle + "\".");
	        // make "SenderName" (characters 0 to key.length()) White  
	        title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, cellData.alertCellSenderName.length(), 0);
	        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),0, cellData.alertCellSenderName.length(), 0);
	        
	        // make "key" (characters 0 to key.length()) White Bold
	        title.setSpan(new ForegroundColorSpan(Color.WHITE), cellData.alertCellSenderName.length() ,
	        		cellData.alertCellSenderName.length() + key.length(), 0);
	        
	      // make "Description" (characters 0 to key.length()) White  
	        title.setSpan(new ForegroundColorSpan(ATLColor.blue_event_title), cellData.alertCellSenderName.length() 
	        		+ key.length(), cellData.alertCellSenderName.length() 
	        		+ key.length()+cellData.alertCellEventTitle.length(), 0);
	        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),cellData.alertCellSenderName.length() 
	        		+ key.length(), cellData.alertCellSenderName.length() 
	        		+ key.length()+cellData.alertCellEventTitle.length(), 0);
	         
	        return title;    
	    }
	    case AlertType.eventAccepted_Sent:
	    {
	    	if (cellData.isSentMessageSuccessfully) {
	    		String key1 = " have confirmed \"";
    	        String key2 = "\" at ";
    	        String key3 = " on ";
    	        
    	        Date timeSelected = cellData.alertCellAcceptedDate;
    	        String dayString = (String)DateFormat.format("EEEE", timeSelected);
    	        String timeString = (String)DateFormat.format("h:mm a", timeSelected);
    	        
    	        String aReceiverName = "You" ;
    	        // this is the text we'll be operating on  
    	        SpannableString title = new SpannableString(aReceiverName + key1 + cellData.alertCellEventTitle 
    	        		+ key2 + timeString + key3 + dayString );
    	        // make "aReceiverName" (characters 0 to key.length()) White  Bold
    	        int length0 = aReceiverName.length();
    	        title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, length0, 0);
    	        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),0, length0, 0);
    	        // make "key1" (characters 0 to key.length()) White 
    	        int length1 = length0 + key1.length();
    	        title.setSpan(new ForegroundColorSpan(Color.WHITE), length0 ,length1, 0);
    	        // make "cellData.alertCellEventTitle" (characters 0 to key.length()) White  Bold
    	        int length2 = length1 + cellData.alertCellEventTitle.length();
    	        title.setSpan(new ForegroundColorSpan(ATLColor.blue_event_title), length1, length2 , 0);
    	        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),length1, length2 , 0);
    	        // make "key2" (characters 0 to key.length()) White 
    	        int length3 = length2 + key2.length();
    	        title.setSpan(new ForegroundColorSpan(Color.WHITE), length2, length3, 0);
    	        // make "timeString" (characters 0 to key.length()) White Bold
    	        int length4 = length3 + timeString.length();
    	        title.setSpan(new ForegroundColorSpan(Color.WHITE), length3, length4, 0);
    	        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),length3, length4, 0);
    	        // make "key3" (characters 0 to key.length()) White 
    	        int length5 = length4 + key3.length();
    	        title.setSpan(new ForegroundColorSpan(Color.WHITE), length4, length5, 0);
    	        // make "timeString" (characters 0 to key.length()) White Bold
    	        int length6 = length5 + dayString.length();
    	        title.setSpan(new ForegroundColorSpan(Color.WHITE), length5, length6, 0);
    	        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),length5, length6, 0);
    	        return title;
	    	}
	    	else{
	    		String key = "Event confirmation could not be sent to ";
	    		SpannableString title = new SpannableString(key + cellData.alertCellSenderName + ". Please try again.");
		        // make "key" (characters 0 to key.length()) White  
		        title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, key.length(), 0);
		        
		        // make "key" (characters 0 to key.length()) White Bold
		        title.setSpan(new ForegroundColorSpan(Color.WHITE), key.length(),key.length() + cellData.alertCellRecieverName.length(), 0);
		        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),key.length(),key.length() + cellData.alertCellRecieverName.length(), 0);  
		         
		        return title;    
	    	}
	    }
	    case AlertType.eventAccepted_Received:{
	        //NSLog(@"eventAccepted_Received");
	        String key1 = " has confirmed \"";
	        String key2 = "\" at ";
	        String key3 = " on ";
	        Date timeSelected = cellData.alertCellAcceptedDate;
	        String dayString = (String)DateFormat.format("EEEE", timeSelected);
	        String timeString = (String)DateFormat.format("h:mm a", timeSelected);
	        
	        String aReceiverName = cellData.alertCellRecieverName ;
	        // this is the text we'll be operating on  
	        SpannableString title = new SpannableString(aReceiverName + key1 + cellData.alertCellEventTitle 
	        		+ key2 + timeString + key3 + dayString );
	        // make "aReceiverName" (characters 0 to key.length()) White  Bold
	        int length0 = aReceiverName.length();
	        title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, length0, 0);
	        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),0, length0, 0);
	        // make "key1" (characters 0 to key.length()) White 
	        int length1 = length0 + key1.length();
	        title.setSpan(new ForegroundColorSpan(Color.WHITE), length0 ,length1, 0);
	        // make "cellData.alertCellEventTitle" (characters 0 to key.length()) White  Bold
	        int length2 = length1 + cellData.alertCellEventTitle.length();
	        title.setSpan(new ForegroundColorSpan(ATLColor.blue_event_title), length1, length2 , 0);
	        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),length1, length2 , 0);
	        // make "key2" (characters 0 to key.length()) White 
	        int length3 = length2 + key2.length();
	        title.setSpan(new ForegroundColorSpan(Color.WHITE), length2, length3, 0);
	        // make "timeString" (characters 0 to key.length()) White Bold
	        int length4 = length3 + timeString.length();
	        title.setSpan(new ForegroundColorSpan(Color.WHITE), length3, length4, 0);
	        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),length3, length4, 0);
	        // make "key3" (characters 0 to key.length()) White 
	        int length5 = length4 + key3.length();
	        title.setSpan(new ForegroundColorSpan(Color.WHITE), length4, length5, 0);
	        // make "timeString" (characters 0 to key.length()) White Bold
	        int length6 = length5 + dayString.length();
	        title.setSpan(new ForegroundColorSpan(Color.WHITE), length5, length6, 0);
	        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),length5, length6, 0);
	        return title;
	    }
	    case AlertType.eventRejected_Sent:{
	    	if (cellData.isSentMessageSuccessfully) {
		        String key = " has declined \"";
		        // this is the text we'll be operating on  
		        String receiverName = "You";
		        SpannableString title = new SpannableString(receiverName + key + cellData.alertCellEventTitle + "\".");
		        // make "SenderName" (characters 0 to key.length()) White  
		        title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, receiverName.length(), 0);
		        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),0, receiverName.length(), 0);
		        
		        // make "key" (characters 0 to key.length()) White Bold
		        title.setSpan(new ForegroundColorSpan(Color.WHITE), receiverName.length() ,
		        		receiverName.length() + key.length(), 0);
		        
		      // make "Description" (characters 0 to key.length()) White  
		        title.setSpan(new ForegroundColorSpan(ATLColor.blue_event_title), receiverName.length() 
		        		+ key.length(), receiverName.length() 
		        		+ key.length()+cellData.alertCellEventTitle.length(), 0);
		        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),receiverName.length() 
		        		+ key.length(), receiverName.length() 
		        		+ key.length()+cellData.alertCellEventTitle.length(), 0);
		         
		        return title;    
	    	}
	    	else{
	    		String key = "Event rejection could not be sent to ";
	            SpannableString title = new SpannableString(key + cellData.alertCellSenderName + ". Please try again.");
		        // make "key" (characters 0 to key.length()) White  
		        title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, key.length(), 0);
		        
		        // make "key" (characters 0 to key.length()) White Bold
		        title.setSpan(new ForegroundColorSpan(Color.WHITE), key.length(),key.length() + cellData.alertCellSenderName.length(), 0);
		        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),key.length(),key.length() + cellData.alertCellSenderName.length(), 0);  
		         
		        return title;    
	    	}
	    }
	    case AlertType.eventRejected_Received:{
	    	 String key = " has declined \"";
	    	// this is the text we'll be operating on  
	    	 SpannableString title = new SpannableString(cellData.alertCellRecieverName + key + cellData.alertCellEventTitle + "\".");
	        // make "SenderName" (characters 0 to key.length()) White  
	        title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, cellData.alertCellRecieverName.length(), 0);
	        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),0, cellData.alertCellRecieverName.length(), 0);
	        
	        // make "key" (characters 0 to key.length()) White Bold
	        title.setSpan(new ForegroundColorSpan(Color.WHITE), cellData.alertCellRecieverName.length() ,
	        		cellData.alertCellRecieverName.length() + key.length(), 0);
	        
	      // make "Description" (characters 0 to key.length()) White  
	        title.setSpan(new ForegroundColorSpan(ATLColor.blue_event_title), cellData.alertCellRecieverName.length() 
	        		+ key.length(), cellData.alertCellRecieverName.length() 
	        		+ key.length()+cellData.alertCellEventTitle.length(), 0);
	        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),cellData.alertCellRecieverName.length() 
	        		+ key.length(), cellData.alertCellRecieverName.length() 
	        		+ key.length()+cellData.alertCellEventTitle.length(), 0);
	         
	        return title;    
	    }
	    case AlertType.taskAssigned_Sent:{
    	 if (cellData.isSentMessageSuccessfully) {
		    	// this is the text we'll be operating on  
		        String key = "Task assigned to ";
		        SpannableString title = new SpannableString(key + cellData.alertCellRecieverName + ".");
		        // make "key" (characters 0 to key.length()) White  
		        title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, key.length(), 0);
		        
		        // make "key" (characters 0 to key.length()) White Bold
		        title.setSpan(new ForegroundColorSpan(Color.WHITE), key.length(),title.length(), 0);
		        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),key.length(),title.length(), 0);  
		         
		        return title;
	    	 }
    	 else{ 
    	    // this is the text we'll be operating on  
	        String key = "Task could not be assigned to ";
	        SpannableString title = new SpannableString(key + cellData.alertCellRecieverName + ". Please try again.");
	        // make "key" (characters 0 to key.length()) White  
	        title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, key.length(), 0);
	        
	        // make "key" (characters 0 to key.length()) White Bold
	        title.setSpan(new ForegroundColorSpan(Color.WHITE), key.length(),key.length() + cellData.alertCellRecieverName.length(), 0);
	        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),key.length(),key.length() + cellData.alertCellRecieverName.length(), 0);  
	         
	        return title;    
    	 }
		}
	    case AlertType.taskAssigned_Received:{
	        String key = " has assigned \"";
//	        NSString *txt = [NSString stringWithFormat:@"%@%@%@\" to you.", aSenderName, key, aContent];
	        // this is the text we'll be operating on  
	        SpannableString title = new SpannableString(cellData.alertCellSenderName + key + cellData.alertCellEventTitle
	        		+"\" to you.");
	        // make "SenderName" (characters 0 to key.length()) White  
	        title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, cellData.alertCellSenderName.length(), 0);
	        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),0, cellData.alertCellSenderName.length(), 0);
	        
	        // make "key" (characters 0 to key.length()) White Bold
	        title.setSpan(new ForegroundColorSpan(Color.WHITE), cellData.alertCellSenderName.length() ,
	        		cellData.alertCellSenderName.length() + key.length(), 0);
	        
	       // make "Description" (characters 0 to key.length()) White  
	        title.setSpan(new ForegroundColorSpan(ATLColor.blue_event_title), cellData.alertCellSenderName.length() 
	        		+ key.length(), cellData.alertCellSenderName.length() 
	        		+ key.length() + cellData.alertCellEventTitle.length(), 0);
	        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),cellData.alertCellSenderName.length() 
	        		+ key.length(), cellData.alertCellSenderName.length() 
	        		+ key.length() + cellData.alertCellEventTitle.length(), 0);
	        return title;       
	    }
	    case AlertType.taskAccepted_Sent:{
	        if (cellData.isSentMessageSuccessfully) {
	            String key = "Task confirmation sent to ";
	            SpannableString title = new SpannableString(key + cellData.alertCellSenderName + ".");
		        // make "key" (characters 0 to key.length()) White  
		        title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, key.length(), 0);
		        
		        // make "key" (characters 0 to key.length()) White Bold
		        title.setSpan(new ForegroundColorSpan(Color.WHITE), key.length(),
		        		key.length() + cellData.alertCellSenderName.length(), 0);
		        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),key.length(),
		        		key.length() + cellData.alertCellSenderName.length(), 0);  
		         
		        return title;
	        }
	        else{
	        	String key = "Task confirmation could not be sent to ";
	            //NSString* txt = [NSString stringWithFormat:@"%@%@. Please try again.", key, aSenderName];
	        	SpannableString title = new SpannableString(key + cellData.alertCellSenderName + ". Please try again.");
	        	  // make "key" (characters 0 to key.length()) White  
		        title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, key.length(), 0);
		        
		        // make "key" (characters 0 to key.length()) White Bold
		        title.setSpan(new ForegroundColorSpan(Color.WHITE), key.length(),key.length() + cellData.alertCellSenderName.length(), 0);
		        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),key.length(),key.length() + cellData.alertCellSenderName.length(), 0);  
		         
		        return title;    
		         
	        }
	    }
	    case AlertType.taskAccepted_Received:{ 
	    	String key = " has accepted \"";
	        //NSString *txt = [NSString stringWithFormat:@"%@%@%@\" to you.", aSenderName, key, aContent];
	    	// this is the text we'll be operating on  
	        SpannableString title = new SpannableString(cellData.alertCellRecieverName + key + cellData.alertCellEventTitle
	        		+"\".");
	        // make "SenderName" (characters 0 to key.length()) White  
	        title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, cellData.alertCellRecieverName.length(), 0);
	        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),0, cellData.alertCellRecieverName.length(), 0);
	        
	        // make "key" (characters 0 to key.length()) White Bold
	        title.setSpan(new ForegroundColorSpan(Color.WHITE), cellData.alertCellRecieverName.length() ,
	        		cellData.alertCellRecieverName.length() + key.length(), 0);
	        
	       // make "Description" (characters 0 to key.length()) White  
	        title.setSpan(new ForegroundColorSpan(ATLColor.blue_event_title), cellData.alertCellRecieverName.length() 
	        		+ key.length(), cellData.alertCellRecieverName.length() 
	        		+ key.length() + cellData.alertCellEventTitle.length(), 0);
	        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),cellData.alertCellRecieverName.length() 
	        		+ key.length(), cellData.alertCellRecieverName.length() 
	        		+ key.length() + cellData.alertCellEventTitle.length(), 0);
	        return title;   
	    }
	    case AlertType.taskReject_Sent:{
	        if (cellData.isSentMessageSuccessfully) {
	            String key = "Your decline task assignment sent to ";
//	            NSString* txt = [NSString stringWithFormat:@"%@%@.", key, aSenderName];
	            SpannableString title = new SpannableString(key + cellData.alertCellSenderName + ".");
		        // make "key" (characters 0 to key.length()) White  
		        title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, key.length(), 0);
		        
		        // make "key" (characters 0 to key.length()) White Bold
		        title.setSpan(new ForegroundColorSpan(Color.WHITE), key.length(),title.length(), 0);
		        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),key.length(),title.length(), 0);  
		         
		        return title;
	        }else {
	            String key = "Your decline task assignment could not send to ";
//	            NSString* txt = [NSString stringWithFormat:@"%@%@. Please try again.", key, aSenderName];
	            SpannableString title = new SpannableString(key + cellData.alertCellSenderName + ". Please try again.");
	        	  // make "key" (characters 0 to key.length()) White  
		        title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, key.length(), 0);
		        
		        // make "key" (characters 0 to key.length()) White Bold
		        title.setSpan(new ForegroundColorSpan(Color.WHITE), key.length(),key.length() + cellData.alertCellSenderName.length(), 0);
		        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),key.length(),key.length() + cellData.alertCellSenderName.length(), 0);  
		         
		        return title;    
	        }
	    }
	    case AlertType.taskReject_Receive:{
	        String key = " has declined \"";
	        //NSString* txt = [NSString stringWithFormat:@"%@%@%@\".", aSenderName, key, aContent];
	        SpannableString title = new SpannableString(cellData.alertCellRecieverName + key + cellData.alertCellEventTitle
	        		+"\".");
	        // make "SenderName" (characters 0 to key.length()) White  
	        title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, cellData.alertCellRecieverName.length(), 0);
	        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),0, cellData.alertCellRecieverName.length(), 0);
	        
	        // make "key" (characters 0 to key.length()) White Bold
	        title.setSpan(new ForegroundColorSpan(Color.WHITE), cellData.alertCellRecieverName.length() ,
	        		cellData.alertCellRecieverName.length() + key.length(), 0);
	        
	       // make "Description" (characters 0 to key.length()) White  
	        title.setSpan(new ForegroundColorSpan(ATLColor.blue_event_title), cellData.alertCellRecieverName.length() 
	        		+ key.length(), cellData.alertCellRecieverName.length() 
	        		+ key.length() + cellData.alertCellEventTitle.length(), 0);
	        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),cellData.alertCellRecieverName.length() 
	        		+ key.length(), cellData.alertCellRecieverName.length() 
	        		+ key.length() + cellData.alertCellEventTitle.length(), 0);
	        return title;   
	    }
	    case AlertType.taskComplete_Sent:{
	    	if (cellData.isSentMessageSuccessfully) {
	    
		        String key1 = "We have informed ";
		        String key2 = " that \"";
	//	        NSString* txt = [NSString stringWithFormat:@"%@%@%@%@\" is done.", key1, aSenderName, key2, aContent];
		        // this is the text we'll be operating on  
		        SpannableString title = new SpannableString(key1 + cellData.alertCellSenderName 
		        		+ key2 + cellData.alertCellEventTitle +"\" is done.");
		        // make "key1" (characters 0 to key.length()) White
		        int length0 = key1.length();
		        title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, length0, 0);
		        // make "alertCellSenderName" (characters 0 to key.length()) White Bold
		        int length1 = length0 + cellData.alertCellSenderName.length();
		        title.setSpan(new ForegroundColorSpan(Color.WHITE), length0 ,length1, 0);
		        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),length0, length1 , 0);
		        // make "cellData.alertCellEventTitle" (characters 0 to key.length()) White  
		        int length2 = length1 + key2.length();
		        title.setSpan(new ForegroundColorSpan(Color.WHITE), length1, length2 , 0);
		        // make "key2" (characters 0 to key.length()) White Bold
		        int length3 = length2 + cellData.alertCellEventTitle.length();
		        title.setSpan(new ForegroundColorSpan(ATLColor.blue_event_title), length2, length3, 0);
		        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),length2, length3, 0);	
		        return title;
	    	}
	    	else{
	    		String key1 = "We were unable to inform ";
	            String key2 = " that \"";
//	            NSString* txt = [NSString stringWithFormat:@"%@%@%@%@\" is done.", key1, aSenderName, key2, aContent]; // concat the 5 (#define) constant parts in a single NSString
	         // this is the text we'll be operating on  
		        SpannableString title = new SpannableString(key1 + cellData.alertCellSenderName + key2 
		        		+ cellData.alertCellEventTitle +"\" is done.");
		        // make "key1" (characters 0 to key.length()) White
		        int length0 = key1.length();
		        title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, length0, 0);
		        // make "alertCellSenderName" (characters 0 to key.length()) White Bold
		        int length1 = length0 + cellData.alertCellSenderName.length();
		        title.setSpan(new ForegroundColorSpan(Color.WHITE), length0 ,length1, 0);
		        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),length0, length1 , 0);
		        // make "cellData.alertCellEventTitle" (characters 0 to key.length()) White  
		        int length2 = length1 + key2.length();
		        title.setSpan(new ForegroundColorSpan(Color.WHITE), length1, length2 , 0);
		        // make "key2" (characters 0 to key.length()) White Bold
		        int length3 = length2 + cellData.alertCellEventTitle.length();
		        title.setSpan(new ForegroundColorSpan(ATLColor.blue_event_title), length2, length3, 0);
		        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),length2, length3, 0);	
		        return title;
	    	}
	    }
	    case AlertType.taskComplete_Received:{
	    	 String key = " has completed \"";
//	         NSString* txt = [NSString stringWithFormat:@"%@%@%@\". Whoohoo!", aSenderName, key, aContent]; 
	    	SpannableString title = new SpannableString(cellData.alertCellRecieverName + key + cellData.alertCellEventTitle
		        		+"\". Whoohoo!");
	        // make "SenderName" (characters 0 to key.length()) White  
	        title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, cellData.alertCellRecieverName.length(), 0);
	        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),0, cellData.alertCellRecieverName.length(), 0);
	        
	        // make "key" (characters 0 to key.length()) White Bold
	        title.setSpan(new ForegroundColorSpan(Color.WHITE), cellData.alertCellRecieverName.length() ,
	        		cellData.alertCellRecieverName.length() + key.length(), 0);
	        
	       // make "Description" (characters 0 to key.length()) White  
	        title.setSpan(new ForegroundColorSpan(ATLColor.blue_event_title), cellData.alertCellRecieverName.length() 
	        		+ key.length(), cellData.alertCellRecieverName.length() 
	        		+ key.length() + cellData.alertCellEventTitle.length(), 0);
	        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),cellData.alertCellRecieverName.length() 
	        		+ key.length(), cellData.alertCellRecieverName.length() 
	        		+ key.length() + cellData.alertCellEventTitle.length(), 0);
	        return title;   
	    }
	    case AlertType.notesShare_Sent:{
	        if (cellData.isSentMessageSuccessfully) {
	            
	            // aSenderName needs to be changed eventually to aReceiverName
	            String key1 = "Note \"";
	            String key2 = "\" shared with ";
//	            NSString *txt = [NSString stringWithFormat:@"%@%@%@%@.", key1, aContent, key2, aSenderName];
	        	// this is the text we'll be operating on  
		        SpannableString title = new SpannableString(key1 +  cellData.alertCellEventTitle
		        		+ key2 + cellData.alertCellSenderName +".");
		        // make "key1" (characters 0 to key.length()) White
		        int length0 = key1.length();
		        title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, length0, 0);
		        // make "alertCellSenderName" (characters 0 to key.length()) White Bold
		        int length1 = length0 + cellData.alertCellEventTitle.length();
		        title.setSpan(new ForegroundColorSpan(ATLColor.blue_event_title), length0 ,length1, 0);
		        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),length0, length1 , 0);
		        // make "cellData.alertCellEventTitle" (characters 0 to key.length()) White  
		        int length2 = length1 + key2.length();
		        title.setSpan(new ForegroundColorSpan(Color.WHITE), length1, length2 , 0);
		        // make "key2" (characters 0 to key.length()) White Bold
		        int length3 = length2 + cellData.alertCellSenderName.length();
		        title.setSpan(new ForegroundColorSpan(Color.WHITE), length2, length3, 0);
		        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),length2, length3, 0);
		        return title;
	        }
	        else{
	        	 String key1 = "Note \"";
	             String key2 = "\" was not shared with ";
//	             NSString *txt = [NSString stringWithFormat:@"%@%@%@%@. Please try again.", key1, aContent, key2, aSenderName]; // concat the 5 (#define) constant parts in a single NSString
	        	// this is the text we'll be operating on  
		        SpannableString title = new SpannableString(key1 +  cellData.alertCellEventTitle
		        		+ key2 + cellData.alertCellSenderName +". Please try again.");
		        // make "key1" (characters 0 to key.length()) White
		        int length0 = key1.length();
		        title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, length0, 0);
		        // make "alertCellSenderName" (characters 0 to key.length()) White Bold
		        int length1 = length0 + cellData.alertCellEventTitle.length();
		        title.setSpan(new ForegroundColorSpan(ATLColor.blue_event_title), length0 ,length1, 0);
		        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),length0, length1 , 0);
		        // make "cellData.alertCellEventTitle" (characters 0 to key.length()) White  
		        int length2 = length1 + key2.length();
		        title.setSpan(new ForegroundColorSpan(Color.WHITE), length1, length2 , 0);
		        // make "key2" (characters 0 to key.length()) White Bold
		        int length3 = length2 + cellData.alertCellSenderName.length();
		        title.setSpan(new ForegroundColorSpan(Color.WHITE), length2, length3, 0);
		        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),length2, length3, 0);
		        return title;
	        }
	    }
	    case AlertType.notesShare_Received:{
	    	      
		     String key = " has shared \"";
//		      NSString *txt = [NSString stringWithFormat:@"%@%@%@\" to you.", aSenderName, key, aContent]; 
	    	SpannableString title = new SpannableString(cellData.alertCellSenderName + key + cellData.alertCellEventTitle
	        		+"\" to you.");
	        // make "SenderName" (characters 0 to key.length()) White  
	        title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, cellData.alertCellSenderName.length(), 0);
	        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),0, cellData.alertCellSenderName.length(), 0);
	        
	        // make "key" (characters 0 to key.length()) White Bold
	        title.setSpan(new ForegroundColorSpan(Color.WHITE), cellData.alertCellSenderName.length() ,
	        		cellData.alertCellSenderName.length() + key.length(), 0);
	        
	       // make "Description" (characters 0 to key.length()) White  
	        title.setSpan(new ForegroundColorSpan(ATLColor.blue_event_title), cellData.alertCellSenderName.length() 
	        		+ key.length(), cellData.alertCellSenderName.length() 
	        		+ key.length() + cellData.alertCellEventTitle.length(), 0);
	        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),cellData.alertCellSenderName.length() 
	        		+ key.length(), cellData.alertCellSenderName.length() 
	        		+ key.length() + cellData.alertCellEventTitle.length(), 0);
	        return title;   
	    }
	    default:
	      break;
  }
		return null;
		
	*/
	}
}
