package com.atlasapp.common;

import com.atlasapp.section_appentry.AtlasApplication;
import com.atlasapp.section_appentry.R;
import com.atlasapp.section_settings.FriendsFinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class Loading extends Activity{
	
	
	public static enum FRIENDS_FINDER { FACEBOOK, TWITTER, LINKEDIN, ADDRESS_BOOK, OTHER }

	private AtlasApplication applicationController;
	private String finder;
	private FRIENDS_FINDER friendsFinder;
	
	@Override 
    public void onCreate(Bundle savedInstanceState) {
        
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.loading);
        applicationController = (AtlasApplication)getApplicationContext();

       
         finder="";
         friendsFinder = FRIENDS_FINDER.OTHER;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	
        	finder = extras.getString("finder");
        	friendsFinder = (finder.equals("facebook"))? FRIENDS_FINDER.FACEBOOK:
        					(finder.equals("twitter"))? FRIENDS_FINDER.TWITTER:
        						(finder.equals("linkedin"))? FRIENDS_FINDER.LINKEDIN:
        							(finder.equals("address_book"))? FRIENDS_FINDER.ADDRESS_BOOK:FRIENDS_FINDER.OTHER;
        						
        	
        }
     
        
        
        
        
	}
	@SuppressWarnings("deprecation")
	private  void alertUser(String messageTitle, String message)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(
				Loading.this).create();

		// Setting Dialog Title
		alertDialog.setTitle(messageTitle);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		// Setting Icon to Dialog
	//	alertDialog.setIcon(R.drawable.tick);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog closed
				//Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
				
					Intent intentCalendar = new Intent(getBaseContext(), FriendsFinder.class);
	            	intentCalendar.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	            	//intent.putExtra("EXTRA_SESSION_ID", sessionId);
	            	startActivity(intentCalendar);
				   
				}  
		});     
    
		// Showing Alert Message
		alertDialog.show();
	}
	
	@Override
	public void onResume(){
	    super.onResume();
	    switch (friendsFinder)
        {
        case FACEBOOK:
        //	String message = applicationController.updateFacebookFriends();
//			if (message!=null && !message.equals(""))
//				alertUser("",message);
//			
        	break;
        case ADDRESS_BOOK:
        	break;
        	
        }
	}
	
	
	
	
}
