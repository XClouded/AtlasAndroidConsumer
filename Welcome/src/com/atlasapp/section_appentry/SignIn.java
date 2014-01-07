//  ==================================================================================================================
//  SignIn.java
//  ATLAS
//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2013-01-26 TAN:     Fixed username and password edit text style, handle show keyboard
//  ==================================================================================================================


package com.atlasapp.section_appentry;

import java.util.ArrayList;
import java.util.HashMap;

import com.atlasapp.atlas_database.ATLDBCommon;
import com.atlasapp.atlas_database.AtlasServerConnect;
import com.atlasapp.atlas_database.DatabaseConstants.SIGN_NEW_USERS_CALLER;
import com.atlasapp.atlas_database.UserDelagateInterface;
import com.atlasapp.common.ATLAlertDialogUtils;
import com.atlasapp.common.ATLColor;
import com.atlasapp.model.ATLContactModel;
import com.atlasapp.section_settings.Settings;
import com.atlasapp.section_today.Today;
import com.facebook.GraphUser;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
//import com.facebook.android.R;
import com.atlasapp.section_appentry.R;
import com.atlasapp.section_calendar.CalendarDayView;
import com.atlasapp.section_contacts.ATLContactListActivity;
import com.parse.facebook.Facebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class SignIn  extends Activity implements UserDelagateInterface{


	private Facebook facebook; 
private AtlasApplication applicationController;
private String pass;
private String email;
private Intent intent;
private AtlasServerConnect parseConnect;
private ATLDBCommon atlDBCommon;
@Override 
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.sign_in);
    
	applicationController = (AtlasApplication)getApplicationContext();
	//parseConnect.progressDialog = new ProgressDialog(SignIn.this);
    parseConnect =   AtlasServerConnect.getSingletonObject(this);

	 Bundle extras = getIntent().getExtras();
     if (extras != null) {
     
     	pass = extras.getString("pass");
   
     	email = extras.getString("email");
     	
     }
    ImageButton backBtn = (ImageButton)findViewById(R.id.signinBackBtn);
    backBtn.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) { 
			
			Intent intent = new Intent(getBaseContext(), Welcome.class);
         	intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

         	startActivity(intent);
			
		}
	});
    
    
    ImageButton signInBtn = (ImageButton)findViewById(R.id.signinBtn);
    signInBtn.setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View v) {
			signInUser();
			
			}

			
		});
    
    // SignIn referenced from a click pressed on Welcome.java...
    signInUser();
   
	}
//	protected void signInUserWithFB() 
//	{
//		Intent intent ;
//		intent = new Intent(getBaseContext(), SignIn.class);
//		if (applicationController.localAtlasProf)
//		{
//			if (applicationController.isFacebookUser())
//			{
//				applicationController.logInOrOutUser(true);
//				intent = new Intent(getBaseContext(), Today.class);
//			}else
//			{
//				 facebook = new Facebook(getResources().getString(R.string.facebook_app_id));
//				 // Set the Facebook instance session variables
//    	        facebook.setAccessToken(Session.getActiveSession()
//    	                                       .getAccessToken());
//    	        facebook.setAccessExpires(Session.getActiveSession()
//    	                                         .getExpirationDate()
//    	                                         .getTime());
//    		   
//    	        final Session  session = Session.getActiveSession();
//    		   
//    	        // If the session is open, make an API call to get user data
//    		    // and define a new callback to handle the response
//    	        if (session.isOpened()){
//    		    Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
//    		        @Override
//    		        public void onCompleted(GraphUser user, Response response) {
//    		            // If the response is successful
//    		            if (session == Session.getActiveSession()) {
//    		                if (user != null) 
//    		                {
//    		                  
//    		                }   
//    		            }
//    		        }
//					   
//    		    }); 
//    		    Request.executeBatchAsync(request);
//    	        }
//			
//		}
//		
//	
//	}else
//	{
//		
//	}
//	}
	/**
	 * SignIn user to Atlas 
	 * If user dont have local Profile account, try to retrieve users' information 
	 * from Parse (according to usersname and password input) and make a local 
	 * profile from Server. Otherwise, send the user a message of invalid username
	 * or password, or try to signIn with Facebook (if local profile found and fb_id also)
	 */
	protected void signInUser() 
	{
//		EditText userName = (EditText)findViewById(R.id.signInUserNameEditTxt);
//		EditText password = (EditText)findViewById(R.id.signInPasswordTxt);
		
		//2013-01-26 TAN: Fix style of user name & password edit text   
	    EditText emailEditText = (EditText)findViewById(R.id.signInUserNameEditTxt);
	    emailEditText.setTypeface(Typeface.DEFAULT_BOLD);
	    emailEditText.setHintTextColor(ATLColor.LTGRAY);
	    //================END========================================
	    
		//Intent intent ;
		intent = new Intent(getBaseContext(), Welcome.class);
	//	parseConnect.currentContext = getBaseContext();
		if (email!=null && pass!=null && !email.equals("")
				&& !pass.equals(""))
		{
			 emailEditText.setText(email);//2013-01-26 TAN: Fix style of user name & password edit text 
			
			if (applicationController.localAtlasProf)
			{
				if (applicationController.userLocalAccountVerified(email, 
						pass))
				{
					 setContentView(R.layout.loading);
					 findViewById(R.id.mainSpinner1).setVisibility(View.VISIBLE);
					 
					  
					backgroundProcesses();
					parseConnect.updateFacebookUseruser(this);
				
					applicationController.logInOrOutUser(true);
					intent = new Intent(getBaseContext(), CalendarDayView.class);
//					alertUser("SignIn successfully", "");
					intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); 
					startActivity(intent);

				}else
				{ 
					// if user has signed up with facebook before ,advice to sign up again....
					String signInFailedMsg = (applicationController.isFacebookUser())? "SignIn failed! Try to SignIn with Facebook": "The username or password you entered didn't work! Please try again or register a new account.";
//					Toast.makeText(getApplicationContext(), signInFailedMsg,
//							Toast.LENGTH_LONG).show();
					
					intent = new Intent(getBaseContext(), 	Welcome.class);
					intent.putExtra("from", "signIn");
					alertUser("", signInFailedMsg);

				}
			}else
			{

				//Attempt Server SignIn   
				// find if user name and password match user on Parse
				// and if do (and NO local profile found on mobile!) 
				// create a Local profile from Server....
				
				parseConnect.logUserToServer(email, pass,this);
//				if (parseConnect.isFacebookAtlasUser(email, pass))
//				{ 
//					intent = new Intent(getBaseContext(), 	Welcome.class);
//		         	intent.putExtra("email_signin",email);
//
//					intent.putExtra("from", "facebook");
//					alertUser("Please re-sign with facebook", "");  
//				}else
//				{
//				 boolean success = applicationController.createLocalProfileFromServerProfile(email, pass);
//				if (success)
//				{
//					
//					
//					 setContentView(R.layout.loading);
//					 findViewById(R.id.mainSpinner1).setVisibility(View.VISIBLE);
//					 
//					
////					if (applicationController.isFacebookUser())
////					{
////						//user must confirm login in with facebook again
////				//		applicationController.logInOrOutUser(false);
//////						String signInFailedMsg = "SignIn failed! Try to SignIn with Facebook";
////////						Toast.makeText(getApplicationContext(), signInFailedMsg,
////////								Toast.LENGTH_LONG).show();
////						AtlasAndroidUser.setfbID("");
////						applicationController.localAtlasProf = false;
////						intent = new Intent(getBaseContext(), 	Welcome.class);
////						intent.putExtra("from", "signIn");
////						//alertUser("SignIn invalid", signInFailedMsg);
////						alertUser("Please re-sign with facebook", "");  
////					
////					}
////					  
////					else
////					{
//					backgroundProcesses();
//					applicationController.logInOrOutUser(true);
//					intent = new Intent(getBaseContext(), CalendarDayView.class);
////					alertUser("", "SignIn successfully");
//					intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); 
//					startActivity(intent);
//					//}
//
//				}else
//				{
//					/// Advice the user to re-check his login info (for a re-Server login attempt)
//					//  or to register again
////					Toast.makeText(getApplicationContext(), "SignIn failed! ",
////							Toast.LENGTH_LONG).show();
//					intent = new Intent(getBaseContext(), Welcome.class);
//					intent.putExtra("from", "signIn");
//					alertUser("", "Username or password was incorrect. Please try again.");
//				}
//				
//			}
			}
		}else
		{
			intent.putExtra("from", "signIn");
			
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
		}
	
	}
	 private void backgroundProcesses() {
	    	
			/////SET ALL CURRENT EMAIL'S (USER NAMES) FROM ATLAS DB (PARSE)
			atlDBCommon = ATLDBCommon.getSingletonObject(null);
			atlDBCommon.setCurrentSessionUsersOnATLASInBackground(true);
		}
	    
//		@SuppressWarnings("deprecation")
	private void alertUser(String title, String message)
	{
			//2013-01-26 TAN: comment out and build the new alert to avoid @SuppressWarnings
			/*
			AlertDialog alertDialog = new AlertDialog.Builder(
					SignIn.this).create();

			// Setting Dialog Title
//			alertDialog.setTitle(messageTitle);
			if(title != null && title.length() > 0){
				TextView titleTextView = new TextView(this);
				titleTextView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				alertDialog.setCustomTitle(titleTextView);
				titleTextView.setTextSize(26);
				titleTextView.setTextColor(ATLColor.WHITE);
				titleTextView.setGravity(Gravity.CENTER);
				titleTextView.setText(title);
			}

			// Setting Dialog Message
			alertDialog.setMessage(message);

			// Setting Icon to Dialog
		//	alertDialog.setIcon(R.drawable.tick);

			// Setting OK Button
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
					

					public void onClick(DialogInterface dialog, int which) {
					// Write your code here to execute after dialog closed
					//Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
					
						intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); 
						startActivity(intent);
					
					
					}
			});

			// Showing Alert Message
			alertDialog.show();
			*/
			
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);

			dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					//TODO: do some thing here
					intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); 
					startActivity(intent);
				}
			});
			
			if(title != null && title.length() > 0){
				TextView titleTextView = new TextView(this);
				titleTextView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				dialog.setCustomTitle(titleTextView);
				titleTextView.setTextSize(26);
				titleTextView.setTextColor(ATLColor.WHITE);
				titleTextView.setGravity(Gravity.CENTER);
				titleTextView.setText(title);
			}
			dialog.setMessage(message);
			AlertDialog dialog1 = dialog.show();
			TextView messageView = (TextView)dialog1.findViewById(android.R.id.message);
			messageView.setGravity(Gravity.LEFT);
		}
//		protected void signUser() 
//		{
//			
//			EditText userName = (EditText)findViewById(R.id.signInUserNameEditTxt);
//			EditText password = (EditText)findViewById(R.id.signInPasswordTxt);
//			
//			
//			if (userName!=null && password!=null && !userName.getText().toString().equals("")
//					&& !password.getText().toString().equals(""))
//			{
//				Intent intent ;
//				intent = new Intent(getBaseContext(), SignIn.class);
//				if (AtlasAndroidUser.doesHaveLocalProfile())
//				{
//					    
//					
//					if (userName.getText().toString().equals(AtlasAndroidUser.getUserName())
//							&& password.getText().toString().equals(AtlasAndroidUser.getAtlasPassword()))
//						
//						intent = new Intent(getBaseContext(), Today.class);
//					else
//						
//						intent = new Intent(getBaseContext(), SignIn.class);
//					
//				}else
//				{
//					
//					//Attempt Server SignIn
//					 ParseAtlasApplication applicationController = (ParseAtlasApplication)getApplicationContext();
//					
//					 boolean success = applicationController.createLocalProfileFromServerProfile(userName.getText().toString(), password.getText().toString());
//					if (success)
//					{
//					//	createLocalProfileFromServerProfile(userName.getText().toString(), password.getText().toString());
//						intent = new Intent(getBaseContext(), Today.class);
//						
//					}else
//					{
//						
//						intent = new Intent(getBaseContext(), SignIn.class);
//					}
//					
//		    	
//				}
//				intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//				startActivity(intent);
//			}
//			
	//	
//		}
//		/**
//		 * Gets ParseUser info from server and update local user info 
//		 */
//		private void createLocalProfileFromServerProfile(String userName, String password) {
//			
//			ParseConnect.setAtlasAndroidUserFromServer(userName,password);
//			AtlasAndroidUser.setUserSignonServer(true);
//			 ParseAtlasApplication applicationController = (ParseAtlasApplication)getApplicationContext();
//			 applicationController.setAtlasUserLocalInfo();
//		}
	@Override
	public void getFriendEmailOnParse(
			ATLContactModel friendsPropertiesOnParseByEmail, boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void registerSuccess(boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void signInSuccess(boolean serversuccess) {
		if (serversuccess)
		{
		if ( parseConnect.isUserFacebookAtlasUser())
		{ 
			intent = new Intent(getBaseContext(), 	Welcome.class);
         	intent.putExtra("email_signin",email);

			intent.putExtra("from", "facebook");
			alertUser("Please re-sign with facebook", "");  
		}else
		{
			applicationController.createLocalProfileFromServerProfile();
	
			
			 setContentView(R.layout.loading);
			 findViewById(R.id.mainSpinner1).setVisibility(View.VISIBLE);
			 
		
			backgroundProcesses();
			applicationController.logInOrOutUser(true);
			intent = new Intent(getBaseContext(), CalendarDayView.class);
//			alertUser("", "SignIn successfully");
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); 
			startActivity(intent);
		}}
		else
		{
			/// Advice the user to re-check his login info (for a re-Server login attempt)
			//  or to register again
//			Toast.makeText(getApplicationContext(), "SignIn failed! ",
//					Toast.LENGTH_LONG).show();
			intent = new Intent(getBaseContext(), Welcome.class);
			intent.putExtra("from", "signIn");
			alertUser("", "Username or password was incorrect. Please try again.");
		}
		
	}
	@Override
	public void getUserEmailOnParseCallBack(
			HashMap<String, String> loginProperties, boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getFacebookFriendIdOnParse(ATLContactModel facebookAtlasFriend,
			boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getAllFBAtlasUsersCallBack(
			ArrayList<ATLContactModel> allFacebookAtlasUsers, boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getAllFBAtlasUsersFriendsCallBack(
			ArrayList<ATLContactModel> allFacebookAtlasUsers, boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void saveCallBack(boolean saved) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getAtlasNewFriendsByEmailCallBack(
			ArrayList<ATLContactModel> allCommonAtlasUsers) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getUpateCallBack(boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getSignNonAtlasUsersCallBack(SIGN_NEW_USERS_CALLER caller,
			boolean success,
			HashMap<String, ATLContactModel> newCurrentNonAtlasUserToSign) {
		// TODO Auto-generated method stub
		
	}
}
