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

package atlasapp.section_alerts;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.google.api.client.util.DateTime;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ParseException;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import atlasapp.common.ATLColor;
import atlasapp.common.ATLHumanizedTime;
import atlasapp.common.ATLUser;
import atlasapp.common.Utilities;
import atlasapp.common.UtilitiesProject;
import atlasapp.database.DatabaseConstants.ALERT_STATUS;
import atlasapp.database.EventProperties;
import atlasapp.database.ItemUserProperties;
import atlasapp.facebook.ProfilePictureView;
import atlasapp.model.ATLContactModel;
import atlasapp.section_appentry.R;

/**
 * @author Ryan Tan & Sharon
 * 
 */
public class ATLAlertCell_2 extends RelativeLayout
{

	Context mActivity;
	ATLAlert alert;
	public TextView title;
	public TextView organizerName;
	public TextView createdDateText;

	
	
	private ImageView alertAvatarImage;
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
		alertAvatarImage = (ImageView) findViewById(R.id.alert_avatar_image);
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

	public void setAlert2(ATLAlert data) {
		alert = data;
		for (EventProperties event : alert.eventList) {
			String titleText = Utilities.deNull(event.title);
			String organizerNameText = Utilities.deNull(alert.inviterContact.displayName());
			ATLContactModel contactModel = alert.inviterContact;
			
			
			Bitmap pic =UtilitiesProject.getUserProfilePic();
			if (pic!=null)
				alertAvatarImage.setImageBitmap(pic);
			
			
//			if (contactModel!=null && !contactModel.getAtlasId().equals(ATLUser.getParseUserID()))
//			{    
//				Bitmap pic = UtilitiesProject.getProfilePic(contactModel.getAtlasId());
//				if (pic!=null)
//					alertAvatarImage.setImageBitmap(pic);
//			}
//			else
//			{
//				Bitmap pic =UtilitiesProject.getUserProfilePic();
//				if (pic!=null)
//					alertAvatarImage.setImageBitmap(pic);
//				
//			}
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
	        String key1 = "Organizer: ";//+((event.atlasId!=null && event.atlasId.equals(ATLUser.getParseUserID()))? ATLUser.getUserFirstName():"");
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
	    		
	    		Date now = new Date();
	    		Calendar calendar = UtilitiesProject.getTimeDiff(now, event.modifiedDatetime);
	    		
	    		
	    		
	    		int days = calendar.get(Calendar.DAY_OF_MONTH) ;
	    		int hours = calendar.get(Calendar.HOUR_OF_DAY) ;
	    		int minutes = calendar.get(Calendar.MINUTE);
	    		int seconds = calendar.get(Calendar.SECOND);
//	    		
	    		String elapsedTimeString = "";
	    		
	    		
	    		calendar.setTime(now);
	    		int daysElapsed = days - calendar.get(Calendar.DAY_OF_MONTH) ;
	    		daysElapsed = daysElapsed*(-1);
	    		elapsedTimeString = 
	    				(daysElapsed==1)?daysElapsed+" Day ago":
	    					(daysElapsed>1)?daysElapsed+" Days ago":
					(hours>0)? hours+" Hours ago":
					(minutes>0)? minutes+" Minutes ago":
					(seconds>0)? seconds+" Seconds ago":
					" now";
	    		
				createdDateText.setText(key3 + elapsedTimeString);
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
	
	
	
	public void setAlert(ATLAlert data) {
		alert = data;
		if (data!=null && alert.itemUserList!=null && alert.itemUserList.get(0)!=null &&
				alert.itemUserList.get(0).getEventAssociated()!=null && alert.itemUserList.get(0).getEventAssociated().eventInviterModel!=null)
		{
			int i=0;
		for (ItemUserProperties itemUser : alert.itemUserList)
		{
			
			EventProperties event =itemUser.getEventAssociated();
			
//			i++;
			ATLContactModel contactModel =event.eventInviterModel;// alert.inviterContact;
			contactModel = (contactModel!=null)? contactModel : new ATLContactModel();
			String titleText = Utilities.deNull(event.title);
			String organizerNameText = (data.type!= ALERT_STATUS.PENDING )?Utilities.deNull(contactModel.displayName()):
										(event.atlasId!=null )? 
												(event.atlasId.equals(ATLUser.getParseUserID())? ATLUser.getUserFirstName():
													""):"";
			
			
//			Bitmap pic =UtilitiesProject.getUserProfilePic();
//			if (pic!=null)
//				alertAvatarImage.setImageBitmap(pic);
//		
			
			if (contactModel.getAtlasId()!=null && contactModel.getAtlasId().equals(ATLUser.getParseUserID()))
			{
				Bitmap pic = UtilitiesProject.getUserProfilePic();
				if (pic!=null)
					alertAvatarImage.setImageBitmap(pic);
			}
			else
				
			if (contactModel!=null)
			{    
				Bitmap pic = UtilitiesProject.getProfilePic(contactModel.getAtlasId());
				if (pic!=null)
					alertAvatarImage.setImageBitmap(pic);
			}
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
	        String key1 = "Organizer: ";//+((event.atlasId!=null && event.atlasId.equals(ATLUser.getParseUserID()))? ATLUser.getUserFirstName():"");
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
	        Date modifiedDatetime = itemUser.statusDateTime;
//	        modifiedDatetime = event.modifiedDatetime;
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
	    	
	    	
	    	
	    	if (modifiedDatetime != null) {
	    		
//	    		DateTime dateTime = new DateTime(modifiedDatetime);
	    		Date now = new Date();
	    		Calendar nowCalendar = Calendar.getInstance();
	    		now = nowCalendar.getTime();
//	    		nowCalendar.setTimeZone(TimeZone.getDefault());
	    		nowCalendar.setTime(now);
	    		int todayDay = nowCalendar.get(Calendar.DAY_OF_MONTH) ;
	    		int todayHours = nowCalendar.get(Calendar.HOUR_OF_DAY) ;
	    		int todayMinutes = nowCalendar.get(Calendar.MINUTE);
	    		int todaySeconds = nowCalendar.get(Calendar.SECOND);
	    		
	    		
	    		
	    		
	    		
	    		Calendar modifiedCalendar = Calendar.getInstance();
	    		
	    		
//	    		modifiedCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
	    		modifiedCalendar.setTime(modifiedDatetime);
	    		int modifiedDay = modifiedCalendar.get(Calendar.DAY_OF_MONTH) ;
	    		int modifiedHours = modifiedCalendar.get(Calendar.HOUR_OF_DAY) ;
	    		int modifiedMinutes = modifiedCalendar.get(Calendar.MINUTE);
	    		int modifiedSeconds = modifiedCalendar.get(Calendar.SECOND);
	    		
	    		
	    		int dayElapsed = todayDay - modifiedDay ;
	    		int hoursElapsed = todayHours - modifiedHours ;
	    		int minutesElapsed = todayMinutes - modifiedMinutes;
	    		int secondsElapsed = todaySeconds - modifiedSeconds;
	    		
//	    		Calendar calendar = UtilitiesProject.getTimeDiff(now, modifiedDatetime);
	    		
	    		
	    		
	    		
//	    		
	    		String elapsedTimeString = "";
	    		
//	    		calendar.setTime(now);
//	    		int today = calendar.get(Calendar.DAY_OF_MONTH) ;
//	    		calendar.setTime(modifiedDatetime);
//	    		int statusDay = calendar.get(Calendar.DAY_OF_MONTH) ;
//	    		int daysElapsed =   modifiedDay - todayDay ;
	    		
//	    		daysElapsed = daysElapsed*(-1);
	    		elapsedTimeString = 
	    					(dayElapsed==1)?dayElapsed+" Day ago":
	    					(dayElapsed>1)?dayElapsed+" Days ago":
	    					(hoursElapsed==1)?(minutesElapsed<0)? (60-modifiedMinutes+todayMinutes)+" Minutes ago": " 1 Hour ago":
	    					(hoursElapsed>0)?(hoursElapsed+" Hours ago"):
	    					(minutesElapsed==1)? (secondsElapsed<0)? (60-modifiedSeconds+todaySeconds)+" Seconds ago":" 1 Minute ago":		
	    					(minutesElapsed>0)? minutesElapsed+" Minutes ago":
	    					(secondsElapsed>0)? secondsElapsed+" Seconds ago":
	    						" Just now";
	    		
				createdDateText.setText(key3 + elapsedTimeString);
				
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
}
