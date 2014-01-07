package atlasapp.section_settings;

import atlasapp.database.ATLDBCommon;
import atlasapp.database.AtlasServerConnect;
import atlasapp.common.ATLFriendLocalTable;
import atlasapp.common.ATLUser;
import atlasapp.common.CurrentSessionFriendsList;
import atlasapp.common.UtilitiesProject;
import atlasapp.section_appentry.AtlasApplication;
import atlasapp.section_appentry.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class LoadingFriends extends Activity{
	
	
	private AtlasApplication applicationController;
	private String message="";
	private AtlasServerConnect parseConnect;

	
	 private final int WAIT_TIME = 1000;
	private String friendsRef = "facebook";
	private ATLDBCommon atlDBCommon;
  
	public void onCreate(Bundle savedInstanceState) {
        
		super.onCreate(savedInstanceState);
	//	 setContentView(R.layout.friend_finder);
//		 findViewById(R.id.mainSpinner1).setVisibility(View.VISIBLE);
//		 
		
		 Bundle extras = getIntent().getExtras();
	        if (extras != null) {
	        	 friendsRef  = extras.getString("friendsRef");
	        	 /// setting all the atlas users properties in the background
	        	 // without "finding new friends"
	        	 atlDBCommon = ATLDBCommon.getSingletonObject(null);
	     		 atlDBCommon.setCurrentSessionUsersOnATLASInBackground(false,false);
	        }
		  
		
		 setContentView(R.layout.loading);
		 findViewById(R.id.mainSpinner1).setVisibility(View.VISIBLE);
		
		 applicationController = (AtlasApplication)getApplicationContext();
		  parseConnect =   AtlasServerConnect.getSingletonObject(this);
		 final int WAIT_TIME = 1000;
		 new Handler().postDelayed(new Runnable(){ 
				private CurrentSessionFriendsList currentSessionFriendsList;

				@Override 
				    public void run() { 
			          
		 
		if (ATLUser.getfbID().equals("") && friendsRef.equals("facebook"))
		{
			alertUser("","Must sign in with Facebook first");
		}
		else
		{
			if ((friendsRef.equals("facebook")))
				ATLFriendLocalTable.updateFBFriendsOnDBInBackground();
			else
				if ((friendsRef.equals("address")))
					ATLFriendLocalTable.updateABFriendsOnDBInBackground();
		
			
//		message = (friendsRef.equals("facebook"))? applicationController.updateFacebookFriends()
//												: applicationController.updateAddressBookFriends();
		
			
			message = "Updating your Atlas friends list...";
			if (message!=null && !message.equals(""))
			 {
			 // finding and updating the Friend DB in the background....
	         currentSessionFriendsList = CurrentSessionFriendsList.getSingletonObject();
	        // currentSessionFriendsList.updateDB();
	         
				 //	progressDialog.dismiss();
		    		// dialog = ProgressDialog.show(FriendsFinder.this, "", message, true);
					//	alertUser("",message);
			Intent intentLoadBack = new Intent(LoadingFriends.this, FriendsFinder.class);
			intentLoadBack.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			intentLoadBack.putExtra("message", message);
			
        	startActivity(intentLoadBack);
        	
			 }
		}
				} 
			}, WAIT_TIME);
		      
		
	}
	
	
	@SuppressWarnings("deprecation")
	private  void alertUser(String messageTitle, String message)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(
				LoadingFriends.this).create();

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
				//	progressDialog.dismiss();
					Intent intentLoadBack = new Intent(LoadingFriends.this, FriendsFinder.class);
					intentLoadBack.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		        	startActivity(intentLoadBack);
				   
				}  
		});     
    
		// Showing Alert Message
		alertDialog.show();
	}

}
