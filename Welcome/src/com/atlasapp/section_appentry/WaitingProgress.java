package com.atlasapp.section_appentry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.atlasapp.atlas_database.ATLDBCommon;
import com.atlasapp.atlas_database.AtlasServerConnect;
import com.atlasapp.atlas_database.ParseDBCallBackInterface;
import com.atlasapp.atlas_database.UserDelagateInterface;
import com.atlasapp.common.AtlasAndroidUser;
import com.atlasapp.common.CurrentSessionFriendsList;
import com.atlasapp.common.UtilitiesProject;
import com.atlasapp.facebook.FacebookUtilities;
import com.atlasapp.facebook.ProfilePictureView;
import com.atlasapp.model.ATLContactModel;
import com.atlasapp.section_appentry.Welcome.CONNECT_TO_ATLAS_VIA;
import com.atlasapp.section_calendar.CalendarDayView;
import com.atlasapp.section_settings.FriendsFinder;
import com.atlasapp.section_settings.LoadingFriends;
import com.facebook.FacebookActivity;
import com.facebook.GraphUser;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.android.AsyncFacebookRunner;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class WaitingProgress extends FacebookActivity implements ParseDBCallBackInterface{
	private AtlasApplication applicationController;
	private String message="";
	private AtlasServerConnect parseConnect;

	private boolean registerSuccess;  
	//indicate that the activity is visible
		private boolean isResumed = false;
	 private final int WAIT_TIME = 1000;
	private String progressRef = "facebook";
	private ATLDBCommon atlDBCommon;
	private String currentAccessToken;
	private Session session;
  
	public void onCreate(Bundle savedInstanceState) {
        
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.loading);
		 findViewById(R.id.mainSpinner1).setVisibility(View.VISIBLE);
		 
		
		 Bundle extras = getIntent().getExtras();
	        if (extras != null) {
	        	progressRef  = extras.getString("progressRef");
	      //  	currentAccessToken  = extras.getString("currentAccessToken");
	        	
	        	 /// setting all the atlas users properties in the background
	        	 // without "finding new friends"
//	        	 atlDBCommon = ATLDBCommon.getSingletonObject(null);
//	     		 atlDBCommon.setCurrentSessionUsersOnATLASInBackground(false);
	        }
	        
	      
	    	
	        
	        
	    
	        
	}
	
	
	
	
	private void waitingProgress()
	{
		
		 setContentView(R.layout.loading);
		 findViewById(R.id.mainSpinner1).setVisibility(View.VISIBLE);
		
		 applicationController = (AtlasApplication)getApplicationContext();
		  parseConnect =   AtlasServerConnect.getSingletonObject(this);
		 final int WAIT_TIME = 1000;
		 new Handler().postDelayed(new Runnable(){ 
				private CurrentSessionFriendsList currentSessionFriendsList;

				@Override 
				  public   void run() { 
			          
		
				} 
			}, WAIT_TIME);
		      
		
	}
	
	
	@SuppressWarnings("deprecation")
	private  void alertUser(String messageTitle, String message)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(
				WaitingProgress.this).create();

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
					Intent intentLoadBack = new Intent(WaitingProgress.this, FriendsFinder.class);
					intentLoadBack.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		        	startActivity(intentLoadBack);
				   
				}  
		});     
    
		// Showing Alert Message
		alertDialog.show();
	}
	
	
	
	@Override 
    public void onResume() {
        super.onResume();  
        isResumed = true;
        
        if (Session.getActiveSession() !=null && Session.getActiveSession().isOpened()){
        	FacebookUtilities.access_token = Session.getActiveSession()
                .getAccessToken();
        
        	FacebookUtilities.mFacebook.setAccessToken( FacebookUtilities.access_token);
        	FacebookUtilities.mAsyncRunner = new AsyncFacebookRunner( FacebookUtilities.mFacebook);
        }else
        {
        	/// back to welcome
        	Intent intentLoadBack = new Intent(WaitingProgress.this, Welcome.class);
			intentLoadBack.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        	startActivity(intentLoadBack);
        }
    	
        	
    }

    @Override
    public void onPause() {   
        super.onPause();
        isResumed = false;   
    }  

    @Override
    protected void onSessionStateChange(SessionState state, Exception exception) {
    	
    	// Get the active session

    	session = Session.getActiveSession();
        super.onSessionStateChange(state, exception);
      //  loginUser();
        if (session!=null){
        	
        	if (isResumed && !AtlasAndroidUser.isLoggedIn())
        	{
        		
        		//loginUser(session, state);
        	 
        	}
        }else
        {
        	/// back to welcome
        	Intent intentLoadBack = new Intent(WaitingProgress.this, Welcome.class);
			intentLoadBack.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        	startActivity(intentLoadBack);
        }
    	
       
    }
    
    
    
//    private void loginUser(Session session, SessionState state)
//    {
//    	// session = Session.getActiveSession();
//    	 if (session!=null){
//	        	   if (state.isOpened()) {
//	        		    
//	        		    
//	   	            // Logged In
//	        		   currentAccessToken = Session.getActiveSession()
//                              .getAccessToken();
//	        		   
//	        		   FacebookUtilities.access_token = currentAccessToken;
//	        	        // Set the Facebook instance session variables
//	        	        FacebookUtilities.mFacebook.setAccessToken(Session.getActiveSession()
//	        	                                       .getAccessToken());
//	        	        FacebookUtilities.mFacebook.setAccessExpires(Session.getActiveSession()
//	        	                                         .getExpirationDate()
//	        	                                         .getTime());
//	        	        
//	        	        FacebookUtilities.mAsyncRunner = new AsyncFacebookRunner( FacebookUtilities.mFacebook);
////	        	        Toast.makeText(getApplicationContext(), "Connecting with Facebook...",
////        		    			Toast.LENGTH_LONG).show();
//	        		   Session currentSession = Session.getActiveSession();
//	        		   
//	        		// If the session is open, make an API call to get user data
//	        		    // and define a new callback to handle the response
//	        		    Request request = Request.newMeRequest(currentSession, new Request.GraphUserCallback() {
//	        		        @Override
//	        		        public void onCompleted(GraphUser user, Response response) { 
//	        		            
//	        		        	// If the response is successful
//	        		        //    if (session == Session.getActiveSession()) {
//	        		            		final Intent intent;  
//	        		                	registerSuccess = RegisterWithFacebook(user);
//	        		                	if (registerSuccess){
//	        		                		
//	        		                    	//backgroundProcesses();
//	        		        		    	intent = new Intent(getBaseContext(), CalendarDayView.class);// Tan point to CalendarView
//	        		        		    	intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
////	        			            		//intent.putExtra("EXTRA_SESSION_ID", sessionId);
//	        		        		    	startActivity(intent);
//	        		        		    }else
//	        		        		    {
//	        		        		    	String currentMessage = (message.equals(""))?  "Login with Facebook Failed!" :message;
////	        		        		    	Toast.makeText(getApplicationContext(),currentMessage,
////	        			    	    				Toast.LENGTH_LONG).show();
//	        		        		    	alertUser("Login with Facebook ",currentMessage);
//	        		        		    	   FacebookUtilities.mFacebook.setAccessToken(null);
//	        			        			   FacebookUtilities.mFacebook.setAccessExpires(-1);
//	        			        			   Session.getActiveSession().close();
//	        		        		    	//intent = new Intent(getBaseContext(), Welcome.class);
//	        			        			   intent = new Intent(getBaseContext(), Welcome.class);// Tan point to CalendarView
//		        		        		    	intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
////		        			            		//intent.putExtra("EXTRA_SESSION_ID", sessionId);
//		        		        		    	startActivity(intent);
//	        		        		    }
//
//	        		           // }  
//	        		        }
//							   
//	        		    }); 
//	        		    Request.executeBatchAsync(request);
//	        		    
////	        		    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);]
//////		            	//intent.putExtra("EXTRA_SESSION_ID", sessionId);
////		            	startActivity(intent);
//	        		   
//	        	   } else 
//	        		   if (state.isClosed()) 
//	        		   {  
//	        			// Clear the Facebook instance session variables
//	        			   FacebookUtilities.mFacebook.setAccessToken(null);
//	        			   FacebookUtilities.mFacebook.setAccessExpires(-1);
//	        				Intent intent = new Intent(getBaseContext(), Welcome.class);// Tan point to CalendarView
//	        		    	intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
////		            		//intent.putExtra("EXTRA_SESSION_ID", sessionId);
//	        		    	startActivity(intent);
//	   	            // Logged Out
//	        		   }
//	        	   
//	        	   /// Pass data back to the calling Activity (to the onActivityResult() method on the 
//	        	   /// reference Activity calling class....)
//	        	  // setResult(RESULT_OK,facebookCallbackIntent );
//	        	  // finish();
//	     //   }
//    	 }
//    }
    /**
	 * Sets the previouslyEncodedImage String from bytes[]
	 * once user choose a picture and using the AtlasAndroidUser picture
	 * property as a default.
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {  
	       
		

	//	if (authButton!=null && from == CONNECT_TO_ATLAS_VIA.FACEBOOK)
		{
			super.onActivityResult(requestCode, resultCode, data);
			FacebookUtilities.mFacebook.authorizeCallback(requestCode, resultCode, data);
		}
	}
	
	
	
	/**
     * Get user information from facebook 
     *  - If user has local account with email matched the one got from facebook
     * 		(but user is logged out of the app), update
     * 		user's Parse account with facebook id and acceess token
     * 		and log user in to the App.
     *  - If user has already local account ,with different email then- NOT ALLOWED 
     * 
     *  - If user doesnt have local profile(shared preferences) & not on Parse 
     * 		then make a new user profile on Server and Local from Facebook info retrieved
     * 
     *  - If user doesnt have local profile but email got from Facebook already match 
     *  	a user on Parse. Retrieve the matching user's information from the Server to create 
     *  	a local profile, with the Facebook information added...
     * 
     * 
     * @param Facebook user
     */
//    private boolean  RegisterWithFacebook(GraphUser user)
//    {
//    	if (user==null)
//    		return false;
//    	String email = user.getProperty("email").toString();
//    	boolean registerSuccess = false;
//    	
//    	HashMap<String, Object> userFacebookInfo = new HashMap<String, Object>();
//    	userFacebookInfo.put("fbID", user.getId());
//    	userFacebookInfo.put("access_token", currentAccessToken);
//    	
//    	/// user got a local atlas profile
//    	if (this.applicationController.localAtlasProf)
//    	{
//    		
//    		boolean firstConnectionWithFB = AtlasAndroidUser.getfbID().equals("");
//    		
//    		
//    		
//    		
//    		if (email.equals(AtlasAndroidUser.getEmail() )||
//    				!firstConnectionWithFB)
//    		{
//    			
//    			
//    			// user has local profile and email from facebook matched the profile info
//    			// or fb_id matched the one found on shared preferences
//    			// USER HASNT BEEN VERFIED ON SERVER (FB_ID & ACCESS TOKEN ARNT UPDATED ON SERVER 
//    			// SIDE TILL HE SIGN ON SERVER)
//    			
//    			applicationController.setAtlasSharedPreferences(userFacebookInfo);
//    			applicationController.upadteApplicationUsersPropertiesFromLocal();
//    			applicationController.upadteAtlasAndroidUserObjectFromApp();
//    			
//    			applicationController.logInOrOutUser(true);
//    			 Toast.makeText(getApplicationContext(), "Register with Facebook...",
// 		    			Toast.LENGTH_LONG).show();
//    			if  (firstConnectionWithFB)
//    				parseConnect.updateAtlasUserInfoOnServer(userFacebookInfo);
//    			FacebookUtilities.access_token = (String) userFacebookInfo.get("access_token");
//    			FacebookUtilities.mFacebook.setAccessToken(FacebookUtilities.access_token);
// 				 FacebookUtilities.mFacebook.setAccessExpires(Session.getActiveSession()
//                         .getExpirationDate()
//                         .getTime());
//    			FacebookUtilities.mAsyncRunner = new AsyncFacebookRunner( FacebookUtilities.mFacebook);
//     			
// 				FacebookUtilities.setFriendsRequest(getApplicationContext());
//    			registerSuccess = true;
//    			   
//    		}
//    		else
//    		{
//    			/// User got local profile email that doesnt match
//    			/// the one got from Facebook...
//    		
//    			message = "This Atlas already in use by another Facebook user";
//    			
//    			
//    			FacebookUtilities.mFacebook.setAccessToken(null);
//    			 FacebookUtilities.mFacebook.setAccessExpires(-1);
// 			    session.close();
//    			
//    			
//    			registerSuccess = false;
//    		}
//    	}
//    	/// User doesnt have local profile 
//    	
//    	else
//    	{
//    		
//    		HashMap <String, String> parseAtlasUser = parseConnect.getUserEmailOnParse(email);
//    		if (!parseAtlasUser.isEmpty())
//    		{
//    			/// email got from facebook already in Parse and user doesnt have 
//    			/// local profile on his mobile
//    			/// create a local profile from server with facebook info
//    			 Toast.makeText(getApplicationContext(), "Creating new Atlas profile ...",
// 		    			Toast.LENGTH_LONG).show();
//    			parseConnect.logUserToServer(parseAtlasUser.get("email"), parseAtlasUser.get("passwordCopy"));
//    			if (parseConnect.isSignOnServer())
//    			{
//    				/// uodate user's Facebook info on Server...
//    				parseConnect.updateAtlasUserInfoOnServer(userFacebookInfo);
//    				
//    				
//    				if (parseConnect.setCurrentAtlasAndroidUserFromServer())
//    				{  
//    					applicationController.setAtlasUserLocalInfo();
//    					applicationController.logInOrOutUser(true);
//    					FacebookUtilities.access_token = (String) userFacebookInfo.get("access_token");
//    					
//    					FacebookUtilities.mFacebook.setAccessToken(FacebookUtilities.access_token);
//    	 				 FacebookUtilities.mFacebook.setAccessExpires(Session.getActiveSession()
//    	                         .getExpirationDate()
//    	                         .getTime());
//    	 				//FacebookUtilities.mFacebook.setAccessToken(AtlasAndroidUser.getAccessToken());
//    	 				FacebookUtilities.mAsyncRunner = new AsyncFacebookRunner( FacebookUtilities.mFacebook);
//    	 				FacebookUtilities.setFriendsRequest(getApplicationContext());
//
//    					
//		    			registerSuccess = true;
//    				}
//    				
//    			}
//    			
//    		}
//    		else
//    		{
//    		 
//    		
//    			///User dont have a local profile and email got from
//    			////Facebook doesnt exsits on parse	....create a new account 
//    			//// from Facebook Info.
//    			 Toast.makeText(getApplicationContext(), "Creating new Atlas profile ...",
//     		    			Toast.LENGTH_LONG).show();
//    		//	userFacebookInfo.put("userName", email);// setting the user name (for Parse) as the email got from Facebook
//    			userFacebookInfo.put("displayname", user.getName());
//    			// generate a password for parse using username+id (from facebook)
//    			userFacebookInfo.put("passwordCopy", user.getUsername()+user.getId());
//    			userFacebookInfo.put("email", user.getProperty("email").toString());
//			
//    			//// TO DO: SET PICTURE
//    			applicationController.setAtlasSharedPreferences(userFacebookInfo);
//    			applicationController.upadteApplicationUsersPropertiesFromLocal();
//    			applicationController.upadteAtlasAndroidUserObjectFromApp();
//			
//    			parseConnect.createNewAtlasUser();
//    			
//    			applicationController.setAtlasUserLocalInfo();
//    			
//			
//    			if (AtlasServerConnect.isSignOnServer())
//    			{
////    				userFacebookInfo = new HashMap<String, Object>();
////    				userFacebookInfo.put("parseUserID", parseId);
////    			
////    				applicationController.setAtlasSharedPreferences(userFacebookInfo);
////    				applicationController.upadteAtlasAndroidUserObjectFromApp();
//    				applicationController.logInOrOutUser(true);
//    				FacebookUtilities.access_token = (String) userFacebookInfo.get("access_token");
//    				
//    				
//    				FacebookUtilities.mFacebook.setAccessToken(FacebookUtilities.access_token);
//	 				 FacebookUtilities.mFacebook.setAccessExpires(Session.getActiveSession()
//	                         .getExpirationDate()
//	                         .getTime());
//	 				//FacebookUtilities.mFacebook.setAccessToken(AtlasAndroidUser.getAccessToken());
//	 				FacebookUtilities.mAsyncRunner = new AsyncFacebookRunner( FacebookUtilities.mFacebook);
//	 				FacebookUtilities.setFriendsRequest(getApplicationContext());
//    				registerSuccess = true;
//    				
//    			//// Retreive and Store User Facebook profile picture....
//            		if (!AtlasAndroidUser.getfbID().equals(""))
//                	{
//                		ProfilePictureView profilePictureView = new ProfilePictureView(getBaseContext());
//         				profilePictureView.setCropped(true);
//         	    	    profilePictureView.setUserId(AtlasAndroidUser.getfbID());
//         	    	    profilePictureView.queryHeight = 100;
//         	    	    profilePictureView.queryWidth = 100;
//         	    	    profilePictureView.sendProfileImageRequest(true);
//         	    	  
//                	}
//    			}
//			
//			
//    		}
//    		
//    	}
//    	return registerSuccess;
//   }
//
//
//






	@Override
	public void registerSuccess(boolean signUpSuccess) {
		// TODO Auto-generated method stub
		
	}






	@Override
	public void getUserEmailOnParseCallBack(
			HashMap<String, String> loginProperties, boolean success) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void signInSuccess(boolean success) {
		// TODO Auto-generated method stub
		
	}




//	@Override
//	public void getFriendEmailOnParse(
//			ATLContactModel friendsPropertiesOnParseByEmail, boolean success) {
//		// TODO Auto-generated method stub
//		
//	}
//



//	@Override
//	public void getFacebookFriendIdOnParse(ATLContactModel facebookAtlasFriend,
//			boolean success) {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//
//
//	@Override
//	public void getAllFBAtlasUsersCallBack(
//			ArrayList<ATLContactModel> allFacebookAtlasUsers, boolean success) {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//
//
//	@Override
//	public void getAllFBAtlasUsersFriendsCallBack(
//			ArrayList<ATLContactModel> allFacebookAtlasUsers, boolean success) {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//
//
//	@Override
//	public void saveCallBack(boolean saved) {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//
//
//	@Override
//	public void getAtlasNewFriendsByEmailCallBack(
//			ArrayList<ATLContactModel> allCommonAtlasUsers) {
//		// TODO Auto-generated method stub
//		
//	}




	@Override
	public void getUpateCallBack(boolean success) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void getFindQueryCallBack(List<ParseObject> foundQueryList,
			boolean found) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void getSaveCallBack(boolean saved, ParseObject parseObjectSaved) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void getObjectIdCallBack(ParseObject retreivedObjectId,
			boolean success) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void getDataCallBack(byte[] fileRetreived, boolean success) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void getSuccessCallBack(boolean success) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void saveFileCallBack(boolean success, ParseObject psrseObjectSaved) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void getFriendEmailOnParseCallBack(
			List<ParseObject> loginProperties, boolean success) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void getFacebookFriendIdOnParseCallBack(
			List<ParseObject> findResult, boolean success) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void getAllAtlasUsersCallBack(List<ParseObject> findResult,
			boolean success) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void getAllFBAtlasUsersFriendsCallBack(List<ParseObject> findResult,
			boolean success) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void friendSignInSuccess(boolean success) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void getAtlasNewFriendsByEmailCallBack(List<ParseObject> findResult,
			boolean success) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void signInNewFriendUserSuccess(boolean success, ParseUser user) {
		// TODO Auto-generated method stub
		
	}

	
	
	
	
	
	
	
	
	
	

}
