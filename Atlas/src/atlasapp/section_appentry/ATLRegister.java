package atlasapp.section_appentry;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import atlasapp.section_alerts.ATLALertListController;
import atlasapp.section_appentry.R;

//import atlasapp.common.SlideOutActivity;
import atlasapp.section_settings.GetProfilePic;
import com.facebook.FacebookActivity;
import com.facebook.GraphUser;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.android.AsyncFacebookRunner;
import com.parse.ParseObject;
import com.parse.ParseUser;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import atlasapp.common.ATLUser;
import atlasapp.common.CurrentSessionFriendsList;
import atlasapp.common.DB;
import atlasapp.common.OnHeadlineSelectedListener;
import atlasapp.common.SlideOutActivity;
import atlasapp.common.UtilitiesProject;
import atlasapp.database.DatabaseConstants.SIGN_NEW_USERS_CALLER;
import atlasapp.database.ATLDBCommon;
import atlasapp.database.AtlasServerConnect;
import atlasapp.database.EmailAddressCallBackInterface;
import atlasapp.database.EmailAddressRecord;
import atlasapp.database.ParseDBCallBackInterface;
import atlasapp.database.UserDelagateInterface;
import atlasapp.facebook.FacebookUtilities;
import atlasapp.facebook.LoginButton;
import atlasapp.facebook.ProfilePictureView;
import atlasapp.model.ATLContactModel;
import atlasapp.model.ATLFriendModel;
import atlasapp.section_calendar.CalendarDayView;

public class ATLRegister extends FacebookActivity implements UserDelagateInterface,EmailAddressCallBackInterface,OnHeadlineSelectedListener,ParseDBCallBackInterface{

	private static String userParseEmail = "";
	private static String userParsePassword ="";
	private static String userParseFirstName="";
	private static String userParseLastName="";
	private HashMap<String, Object> atlasUserRegisterProperties;
	private static ArrayList<String> userEmailAddress; 
	private static String userParseId="";
	
    static final int INTENT_REQUEST_CODE_BROWSE_PICTURE = 1;
    private int picTypePicker = 3;// cancel default
	private static final int CAMERA_REQUEST = 1888; 
	private String encodedImage;

	
	private EditText emailEditText;
	private EditText passwordEditText;
	private LoginButton facebookLoginButton;
	private ImageButton loginWithFacebook;
	private ImageButton signInButton;
	private String currentAccessToken;
	private GraphUser currentFacebookGraphUser;
	private HashMap<String, Object> userFacebookLoginInfo;
	private Session session;
	private ATLDBCommon atlDBCommon;
	private CurrentSessionFriendsList currentSessionFriendsList;
	private boolean isResumed;
	
	private AtlasApplication applicationController;
	private AtlasServerConnect parseConnect;
	private EditText firstNameEditText;
	private EditText lastNameEditText;
	private TextView userTextViewTitle;
	private ImageButton registerBtn;
	private ImageButton facebookBtn;
	private ImageButton imgageBtn;
	private String message;
	private byte[] profilePic;
	private String facebookEmailAddress="";
	private static String password;
	private static String firstName;
	private static String lastName;
	private static String email;
	private static boolean newUser;
	
	@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
//	        
//	        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
//
//	        //setContentView(R.layout.main);
//
//	        /** Create an array adapter to populate dropdownlist */
//	          
//	        ActionBar actionBar = getActionBar();
//	        actionBar.hide();
	        getActionBar().hide();
	        setContentView(R.layout.atl_register);
	        atlasUserRegisterProperties = new HashMap<String,Object>();
	    	userEmailAddress = new ArrayList<String>();
	        
	        Bundle extras = getIntent().getExtras();
	        if (extras != null) {
	        	userParseId = (extras.getString("atlas_id")!=null)?extras.getString("atlas_id"):userParseId ;
	        	newUser = extras.getBoolean("new_user") ;
	        	userParseEmail = (extras.getString("email")!=null)?extras.getString("email"):userParseEmail;
	        	userParsePassword = (extras.getString("password_copy")!=null)?extras.getString("password_copy"):userParsePassword;
	        	userParseFirstName = (extras.getString("first_name")!=null)?extras.getString("first_name"):userParseFirstName;
	        	userParseLastName = (extras.getString("last_name")!=null)?extras.getString("last_name"):userParseLastName;
	        	this.picTypePicker = extras.getInt("picture_from");
		       }
		       if (picTypePicker==1)
		    	   chooseFromLibrary();
		       else
		    	   if (picTypePicker==2)
		    		   takeAPhoto();
		       
		       atlasUserRegisterProperties.put("atlas_id", userParseId);
		       
		       if (userParseEmail!=null && !userParseEmail.equals(""))
		       {
		    	   email = (email==null || email.equals(""))?userParseEmail:email;
		    	   if (!userEmailAddress.contains(userParseEmail))

		    		   userEmailAddress.add(userParseEmail);
		       }
	        setRegistrataionInfoFromLatestInput();
	        
	        applicationController = (AtlasApplication)getApplicationContext();

	        parseConnect =   AtlasServerConnect.getSingletonObject(this);
	        parseConnect.application = applicationController;

	        DB.openDb();
	         
	         // finding and updating the Friend DB in the background....
	        currentSessionFriendsList = CurrentSessionFriendsList.getSingletonObject();

	        setFBButton() ;
	
	}
	private void saveLastUserRegistrationInput()
	{
		password = (passwordEditText!=null && !passwordEditText.getText().toString().equals(""))? passwordEditText.getText().toString():password;
		firstName = (firstNameEditText!=null && !firstNameEditText.getText().toString().equals(""))? firstNameEditText.getText().toString():firstName;

		lastName = (lastNameEditText!=null && !lastNameEditText.getText().toString().equals(""))? lastNameEditText.getText().toString():lastName;


		email = (emailEditText!=null &&  !emailEditText.getText().toString().equals(""))?emailEditText.getText().toString():email;
	
		  
	
	}
	private void setRegistrataionInfoFromLatestInput()
	{
		
	    emailEditText =  (EditText)findViewById(R.id.atlRegisterEmailEditTxt);
        firstNameEditText =  (EditText)findViewById(R.id.atlRegisterFirstNameEditTxt);
        lastNameEditText =  (EditText)findViewById(R.id.atlRegisterLastNameEditTxt);
        passwordEditText =  (EditText)findViewById(R.id.atlRegisterPasswordEditTxt);

        userTextViewTitle =  (TextView)findViewById(R.id.atlRegisterNameTxtView);
        userTextViewTitle.setText(userParseEmail);
        
        passwordEditText.setText(password);
        firstNameEditText.setText(firstName);
        lastNameEditText.setText(lastName);
		 emailEditText.setText(email);
        
        
        
        registerBtn =  (ImageButton)findViewById(R.id.atlRegisterBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				register();
			}

			
		});
        imgageBtn =  (ImageButton)findViewById(R.id.atlRegisterUserImageBtn);
        imgageBtn.setOnClickListener( new View.OnClickListener() {
			
	    	   
	    	   
	    	   
    	     
			@Override
			public void onClick(View v) {
				
				saveLastUserRegistrationInput(); 
				int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
					SlideOutActivity.prepare(ATLRegister.this, R.id.atlRegisterLayout, width);
					Intent intent = new Intent(ATLRegister.this,
							GetProfilePic.class);
					startActivity(intent);
					
					overridePendingTransition(0, 0);

				
			}
		});
		
		
	}
	 private void chooseFromLibrary()
	   {
		   ///// Chose From Libraray option
		 
		 Intent intent = new Intent(Intent.ACTION_PICK,
	               (MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
	       startActivityForResult(intent,
	    		   INTENT_REQUEST_CODE_BROWSE_PICTURE);
	   
		 
	   }
	 private void takeAPhoto()
	   {
		/////// Take A Picture option....
		 Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
		 startActivityForResult(cameraIntent, CAMERA_REQUEST); 
	   }
	 @Override
     public boolean onKeyDown(int keyCode, KeyEvent event) 
     {
         if(keyCode == KeyEvent.KEYCODE_BACK)
         {
         Intent intent = new Intent(getBaseContext(), ATLWelcome.class);
         intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        		//intent.putExtra("EXTRA_SESSION_ID", sessionId);
         startActivity(intent);
         this.overridePendingTransition(R.anim.enter,R.anim.leave);

         
         
             return true;
         }
         return false;
     }  
	 /**
	  * Register user from edit text fields
	  */
	 private void register()
	 {
	
		  firstName = (firstNameEditText.getText()!=null)?firstNameEditText.getText().toString():"" ;
		  lastName = (lastNameEditText.getText()!=null)? lastNameEditText.getText().toString():"";
		  email = (emailEditText.getText()!=null)?emailEditText.getText().toString():"";
		  password = (passwordEditText.getText()!=null)?passwordEditText.getText().toString():"";
		 
		 
		 
		 
		 if (validateUserNameAndPassword(firstName,lastName,email,password))
		 {
			 atlasUserRegisterProperties = new HashMap<String, Object>();

			 atlasUserRegisterProperties.put("first_name", firstName);
			 atlasUserRegisterProperties.put("last_name", lastName);
			 atlasUserRegisterProperties.put("password_copy", password);
			 atlasUserRegisterProperties.put("email", email);
			 
			 
			 
			  
			

			 if (userParseId!=null && !userParseId.equals("") && userEmailAddress!=null && !userEmailAddress.contains(email))
				{
				 	userEmailAddress.add(email);
					/// update user email from facebook
					/// success on setUserEmailAddressOnParseCallBack 
					AtlasServerConnect.addUserEmailAddress(userParseId,userEmailAddress,this);
				}
			 if (userParseId!=null && !userParseId.equals(""))
				 ATLUser.setEmailAddresses(userEmailAddress);
			 
     		applicationController.setAtlasSharedPreferences(atlasUserRegisterProperties);

     		applicationController.upadteApplicationUsersPropertiesFromLocal();
			applicationController.upadteAtlasAndroidUserObjectFromApp();
			///until email verfied the user is_atlas_user set to false
			 atlasUserRegisterProperties.put("is_atlas_user", false);
			applicationController.logInOrOutUser(true);

			 Toast.makeText(getApplicationContext(), "Register...",
		    			Toast.LENGTH_LONG).show();
			 parseConnect.setUserListener(this);
				/// getUpateCallBack call back...
			 if (userParseId!=null && !userParseId.equals(""))
				 parseConnect.updateAtlasUserInfoOnServer(atlasUserRegisterProperties,this);
			 else
				 /// call back on registerSuccess method
				 parseConnect.createNewAtlasUser(this);
				 
		 }
		 else
		 {
			 
		 }
		 
		 
		 
	 } 
	 @Override
	 public void registerSuccess(boolean success) 
	 {
		 if (success)
		 {   
			 if (currentFacebookGraphUser!=null)
			 {
				 if ( ATLUser.getParseUserID()!=null &&
						 !ATLUser.getParseUserID().equals("") &&      
						 userEmailAddress!=null )
				 {
					 if (!userEmailAddress.contains(facebookEmailAddress))
						 userEmailAddress.add(facebookEmailAddress);
					 /// update user email from facebook
					 /// success on setUserEmailAddressOnParseCallBack 
					 AtlasServerConnect.addUserEmailAddress(ATLUser.getParseUserID(),userEmailAddress,this);
				 }   
 
				 ATLUser.setEmailAddresses(userEmailAddress);
				 signInSuccess(true);
			 }
			 else

				 if(ATLUser.getParseUserID()!=null && !ATLUser.getParseUserID().equals(""))
				 {

					 //				 userParseEmail = ATLUser.getEmail();
					 //				 userParsePassword = ATLUser.getAtlasPassword();
					 if (userEmailAddress!=null )
					 {
						 if (!userEmailAddress.contains(email))
							 userEmailAddress.add(email);
						 /// update user email from facebook
						 /// success on setUserEmailAddressOnParseCallBack 
						 AtlasServerConnect.addUserEmailAddress(ATLUser.getParseUserID(),userEmailAddress,this);
						 ATLUser.setEmailAddresses(userEmailAddress);
					 }

					
					 // parseConnect.updateAtlasUserInfoOnServer(atlasUserRegisterProperties,this);
				     HashMap<String,Object> atlasId = new HashMap<String,Object>();
				     
				     atlasId.put("atlas_id", ATLUser.getParseUserID());
				       applicationController.setAtlasSharedPreferences(atlasId);
		        		///get profile picture in the background
		        	//	AtlasServerConnect.refreshUserPictureOnTheBackground();
		    		
//		    			applicationController.upadteApplicationUsersPropertiesFromLocal();
//		    			applicationController.upadteAtlasAndroidUserObjectFromApp();
					 getUpateCallBack(true);

				 }       
				 else
   
					 getUpateCallBack(false);

		 }
		 else
		 {
//			 if (newUser)
//				 UtilitiesProject.alertUser("",
//						 "Failed to Rregister new User  please check your connected to the internet",this);
		 }

	 }

/**
 * Validating user email and password fields
 * and add the new added email to the current list
 * email of the user to be updated on the email-address table 
 * on parse 
 * @return
 */
 private boolean validateUserNameAndPassword(String firstName, String lastName,
		 				String email, String password) {
			
		 
		 boolean validate = false;
//		 String password = (passwordEditText.getText()!=null)? passwordEditText.getText().toString():"";
//		 String email =  (emailEditText.getText()!=null)? emailEditText.getText().toString():"";
		 validate = (password!=null && !password.equals("")&& email!=null && !email.equals("")
				 && firstName!=null && !firstName.equals("") && lastName!=null && !lastName.equals(""));
		
		
		 if (validate && !userEmailAddress.contains(email))
			 userEmailAddress.add(email);
		 message  = "";
		 message += (password == null || password.equals(""))? "Must add password field":"";
		 message += (email == null || email.equals(""))? "\n Must add email field":"";
		 message += (firstName == null || firstName.equals(""))? "\n Must add First Name field":"";
		 message += (lastName == null || lastName.equals(""))? "\n Must add Last Name field":"";
		 
		 message.trim();
		 
//		 if (password == null || password.equals(""))
//		 {
		 if (message!=null && !message.equals(""))
			 UtilitiesProject.alertUser("", message,this);
		 //}
		 
		return validate;
		}
	private void setFBButton() {
		facebookLoginButton = (LoginButton)findViewById(R.id.fbauthButton);
        	
		facebookLoginButton.setEnabled(true);
        facebookLoginButton.setVisibility(View.VISIBLE);
        facebookLoginButton.setApplicationId(getString(R.string.facebook_app_id));
				 
        facebookLoginButton.setReadPermissions(Arrays.asList("email"));
        facebookLoginButton.setBackgroundResource(R.drawable.atl_signin_fb_signin);
        facebookLoginButton.setText("");
	
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
        if (session!=null && facebookLoginButton!=null && facebookLoginButton.isEnabled()){
        	
        	if (isResumed )
        	{
        		
        		loginUser(session, state);
        	 
        	}
        }
    	
       
    }
    
	/**
	 * Login with facebook
	 * @param session
	 * @param state
	 */
	 private void loginUser(Session session, SessionState state)
	    {
	    	// session = Session.getActiveSession();
	    	 if (session!=null){
	    		 setContentView(R.layout.loading);
     		 findViewById(R.id.mainSpinner1).setVisibility(View.VISIBLE);
     		 
		        	   if (state.isOpened()) {
		   	            // Logged In
		        		   currentAccessToken = Session.getActiveSession()
	                              .getAccessToken();
		        		   
		        		   FacebookUtilities.access_token = currentAccessToken;
		        	        // Set the Facebook instance session variables
		        	        FacebookUtilities.mFacebook.setAccessToken(Session.getActiveSession()
		        	                                       .getAccessToken());
		        	        FacebookUtilities.mFacebook.setAccessExpires(Session.getActiveSession()
		        	                                         .getExpirationDate()
		        	                                         .getTime());
		        	        
		        	        FacebookUtilities.mAsyncRunner = new AsyncFacebookRunner( FacebookUtilities.mFacebook);
//		        	        Toast.makeText(getApplicationContext(), "Connecting with Facebook...",
//	        		    			Toast.LENGTH_LONG).show();
		        		   Session currentSession = Session.getActiveSession();
		        		   
		        		// If the session is open, make an API call to get user data
		        		    // and define a new callback to handle the response
		        		    Request request = Request.newMeRequest(currentSession, new Request.GraphUserCallback() {
		        		        @Override
		        		        public void onCompleted(GraphUser user, Response response) { 
		        		            
		        		        	// If the response is successful
		        		            		final Intent intent;  
		        		            		// call back from registerSuccess
		        		                	RegisterWithFacebook(user);
		        		        }
								   
		        		    }); 
		        		    Request.executeBatchAsync(request);
		        		   
		        	   } else 
		        		   if (state.isClosed()) 
		        		   {  
		        			// Clear the Facebook instance session variables
		        			   FacebookUtilities.mFacebook.setAccessToken(null);
		        			   FacebookUtilities.mFacebook.setAccessExpires(-1);
		        			   setContentView(R.layout.atl_signin);
		   	            // Logged Out
		        		   }
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
	    private void  RegisterWithFacebook(GraphUser user)
	    {
	    	if (user==null)
	    		return ;
	    	currentFacebookGraphUser = user;
			 facebookEmailAddress = user.getProperty("email").toString();
	    	boolean registerSuccess = false;
	    	
	    	atlasUserRegisterProperties.put("facebook_id", user.getId());
	    	atlasUserRegisterProperties.put("access_token", currentAccessToken);
	    	
	    	
	    	
	    	if (userParseId!=null && !userParseId.equals("") && userEmailAddress!=null && userEmailAddress.size()>0 && !userEmailAddress.contains(facebookEmailAddress))
			{
	    		userEmailAddress.add(facebookEmailAddress);
				/// update user email from facebook
				/// success on setUserEmailAddressOnParseCallBack 
				AtlasServerConnect.addUserEmailAddress(userParseId,userEmailAddress,this);
			}
	    	if (userParseId!=null && !userParseId.equals(""))
	    	
	    		ATLUser.setEmailAddresses(userEmailAddress);
	    		
	    	atlasUserRegisterProperties.put("first_name", currentFacebookGraphUser.getFirstName());
	    	atlasUserRegisterProperties.put("last_name", currentFacebookGraphUser.getLastName());

	    	// generate a password for parse using username+id (from facebook)
	    	atlasUserRegisterProperties.put("password_copy", currentFacebookGraphUser.getUsername()+currentFacebookGraphUser.getId());
	    	atlasUserRegisterProperties.put("email", facebookEmailAddress);
	    	atlasUserRegisterProperties.put("verified_email", "TRUE");
 			atlasUserRegisterProperties.put("was_non_user", "FALSE");
 			
 			
 			applicationController.setAtlasSharedPreferences(atlasUserRegisterProperties);
 			applicationController.upadteApplicationUsersPropertiesFromLocal();
 			applicationController.upadteAtlasAndroidUserObjectFromApp();
 			applicationController.logInOrOutUser(true);
 			
 			
 			atlasUserRegisterProperties.put("verified_email", true);
 			atlasUserRegisterProperties.put("was_non_user", false);
 			
 			AtlasServerConnect.signOnServer = true;
 			
 			
 			
 			if (userParseId!=null && !userParseId.equals(""))
 				signInCurrentParseUser();
 			else
 				parseConnect.createNewAtlasUser(this);
 			
 			/// call back from getUpateCallBack method
// 			parseConnect.updateAtlasUserInfoOnServer(atlasUserRegisterProperties, this);
	    
	    }
	    /*
	     * Sign the user on parse - call back from signInSuccess method
	     */
	    private void signInCurrentParseUser() {
			AtlasServerConnect.signInCurrentParseUser(userParseEmail,userParsePassword,this);

			
		}
	    @Override
		public void signInSuccess(boolean success) {
	    	if (success && parseConnect.isSignOnServer())
			{
				  Log.v("ATLRegister", "success ");
				
				if (this.currentFacebookGraphUser!=null)
				{
				
					 userFacebookLoginInfo = new HashMap<String, Object>();
					 userFacebookLoginInfo.put("facebook_id", currentFacebookGraphUser.getId());
					 userFacebookLoginInfo.put("access_token", currentAccessToken);
					 atlasUserRegisterProperties.put("facebook_id", currentFacebookGraphUser.getId());
					 atlasUserRegisterProperties.put("access_token", currentAccessToken);

//					 {
						 applicationController.setAtlasSharedPreferences(userFacebookLoginInfo);
//						 ///get profile picture in the background
//						 AtlasServerConnect.refreshUserPictureOnTheBackground();
//					 }
						/// for setting the Atlas Id retrieved from Register...
					 applicationController.setAtlasUserLocalInfo();
					 applicationController.upadteApplicationUsersPropertiesFromLocal();
					 applicationController.upadteAtlasAndroidUserObjectFromApp();

					 applicationController.logInOrOutUser(true);
					 Toast.makeText(getApplicationContext(), "Register with Facebook...",
							 Toast.LENGTH_LONG).show();
//			
				
				if (userParseId!=null && userParseId.equals(""))
				
				/// uodate user's Facebook info on Server...
					parseConnect.updateAtlasUserInfoOnServer(atlasUserRegisterProperties,this);
//			else
				else
					getUpateCallBack(true);
				
			}
				else
				{
					///
					  
				}
//			onFacebookRegisterProcesscomplete(registerSuccess);
			}
			
		}
	    @Override
		public void getUpateCallBack(boolean success) 
	    {
			if (success)
			{
				
				Log.v("ATLRegister", "UPDATE SUCCESS");
				if (atlasUserRegisterProperties.get("facebook_id")!=null &&
						!atlasUserRegisterProperties.get("facebook_id").toString().equals(""))
				{
					Log.v("ATLRegister", "FACEBOOK USER");
//					if (userEmailAddress==null || !userEmailAddress.contains(facebookEmailAddress))
//		 			{
//						userEmailAddress.add(facebookEmailAddress);
//		 				/// update user email from facebook
//		 				/// success on setUserEmailAddressOnParseCallBack 
					//		 				AtlasServerConnect.addUserEmailAddress(userParseId,userEmailAddress,this);
					//		 			}  
					//		 	    	
					//		 	    	
					//		 	    	
					//		 	    	ATLUser.setEmailAddresses(userEmailAddress);
					/// retrieving and storing facebook profile picture in the background
					/// on local and Parse
					/// call back on the saveCallBack method....
					ProfilePictureView profilePictureView = new ProfilePictureView(getBaseContext());
					profilePictureView.setCropped(true);
					profilePictureView.setUserId(ATLUser.getfbID());
					profilePictureView.queryHeight = 100;
					profilePictureView.queryWidth = 100;
					profilePictureView.sendProfileImageRequest(true,this);

					FacebookUtilities.access_token = (String) userFacebookLoginInfo.get("access_token");
					FacebookUtilities.mFacebook.setAccessToken(FacebookUtilities.access_token);
					FacebookUtilities.mFacebook.setAccessExpires(Session.getActiveSession()
							.getExpirationDate()
							.getTime());
					FacebookUtilities.mAsyncRunner = new AsyncFacebookRunner( FacebookUtilities.mFacebook);

					FacebookUtilities.setFriendsRequest(getApplicationContext());
					Log.v("ATLRegister", "FACEBOOK COMPLETE");
					//AtlasServerConnect.refreshUserPictureOnTheBackground();
					onFacebookRegisterProcesscomplete(success);

				}
				else
				{
					Log.v("ATLRegister", "NON FACEBOOK USER");
					if (profilePic!=null)
					{
						Log.v("ATLRegister", "NON FACEBOOK USER SAE IMAGE");
						/// call back on the saveCallBack method....
						AtlasServerConnect parseConnect =   AtlasServerConnect.getSingletonObject(null);
						parseConnect.saveProfileImage(profilePic,this);
					}
					else
						onFacebookRegisterProcesscomplete(success);	

				}
					
				
			}
			else
			onFacebookRegisterProcesscomplete(success);
			
		}
	    @Override
		public void setUserEmailAddressOnParseCallBack(boolean success) {
	    	
			
		}
	    @Override
		public void saveFileCallBack(boolean success, ParseObject psrseObjectSaved) {
	    	if (atlasUserRegisterProperties.get("facebook_id")!=null &&
	    			!atlasUserRegisterProperties.get("facebook_id").toString().equals(""))
	    	{

	    	}
	    	else
	    		if (success)
	    		{

	    			AtlasServerConnect.refreshUserPictureOnTheBackground(this);
	    		}else
	    			onFacebookRegisterProcesscomplete(success);
			
		}
	    /**
	     * Saved profile picture on Parse call back method
	     */
	    @Override
		public void saveCallBack(boolean saved) 
	    {
	    	if (atlasUserRegisterProperties.get("facebook_id")!=null &&
	    			!atlasUserRegisterProperties.get("facebook_id").toString().equals(""))
	    	{

	    	}
	    	else
	    		if (saved)
	    			AtlasServerConnect.refreshUserPictureOnTheBackground(this);
	    		else
	    			onFacebookRegisterProcesscomplete(saved);
			
		}
	    @Override
		public void refreshUserPictureOnTheBackgroundCallBack(boolean success,
				String imageUrl) {
	    	onFacebookRegisterProcesscomplete(true);
		}
		
	    private void onFacebookRegisterProcesscomplete(boolean success)
		{
	    	if (success)
	    	{

	    		if (ATLUser.isAtlasUser())
	    		{

	    			backgroundProcesses(true);
	    			Intent intent = new Intent(getBaseContext(), CalendarDayView.class);
	    			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	    			//	            		//intent.putExtra("EXTRA_SESSION_ID", sessionId);
	    			startActivity(intent);
	    		}
	    		else
	    		{
//	    			backgroundProcesses(true);
	    			
	    			Intent intent = new Intent(getBaseContext(), ATLVerifyEmailAddress.class);
	    			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	    			//	            		//intent.putExtra("EXTRA_SESSION_ID", sessionId);
	    			startActivity(intent);  
	    			
	    			
	    			
//	    			UtilitiesProject.alertUser("Atlas ","Please verify your email",this);
	    		}
	    	}else
	    	{
	    		String currentMessage = (message==null || message.equals(""))?  "Login with Facebook Failed!" :message;
	    		//        		    	Toast.makeText(getApplicationContext(),currentMessage,
	    		//	    	    				Toast.LENGTH_LONG).show();
	    		UtilitiesProject.alertUser("Login with Facebook ",currentMessage,this);
	    		FacebookUtilities.mFacebook.setAccessToken(null);
	    		FacebookUtilities.mFacebook.setAccessExpires(-1);
	    		Session.getActiveSession().close();
	    		//intent = new Intent(getBaseContext(), Welcome.class);
	    	}
		}
	    //	    private void backgroundProcesses() {
	    //			currentSessionFriendsList.setFriendsUpdateComplete(false);
	    //
	    //			/////SET ALL CURRENT EMAIL'S (USER NAMES) FROM ATLAS DB (PARSE)
	    //			atlDBCommon = ATLDBCommon.getSingletonObject(null);
//			atlDBCommon.setCurrentSessionUsersOnATLASInBackground(true);
//			
//			ATLFriendModel.readAllLocalFriendsInBackground();
//		}
	    public void backgroundProcesses(boolean firstRegister) {
			
	    	
			AtlasServerConnect.subscribedToChannels(getApplicationContext(), "ID"+ATLUser.getParseUserID(), ATLALertListController.class);

	    	CurrentSessionFriendsList currentSessionFriendsList = CurrentSessionFriendsList.getSingletonObject();
			currentSessionFriendsList.setFriendsUpdateComplete(false);

			/////SET ALL CURRENT EMAIL'S (USER NAMES) FROM ATLAS DB (PARSE)
			atlDBCommon = ATLDBCommon.getSingletonObject(null);
			
//			ATLContactModel.readAllLocalFriendsInBackground();
			
			
			
			atlDBCommon.setCurrentSessionUsersOnATLASInBackground(true,firstRegister);

			ATLContactModel.readAllLocalFriendsInBackground();

		}
	    @Override
		public void onArticleSelected(int position) {
			switch (position)
			{
			case 0:
				chooseFromLibrary();
				break;
			case 1:
				takeAPhoto();
				break;
			default:
				break;
			
			}
	    }
	    @Override
		 public void onActivityResult(int requestCode, int resultCode, Intent data) {  
		       
				   encodedImage="";
				   switch (requestCode) 
				   {
					case INTENT_REQUEST_CODE_BROWSE_PICTURE:
						if( resultCode == Activity.RESULT_OK ){

							Uri photoUri = data.getData();
							if (photoUri != null) { 
								
								
								
								setPictureFromFile(photoUri);
							try {
								 Bundle params = new Bundle();
								  params.putByteArray("photo",
										  FacebookUtilities.scaleImage(getApplicationContext(), photoUri));
								
								  profilePic = params.getByteArray("photo");
								  ImageView profPicture = (ImageView)findViewById(R.id.camerapreview);
								  Bitmap bitmap = BitmapFactory.decodeByteArray(profilePic, 0, profilePic.length);
								  profPicture.setImageBitmap(bitmap);
								  
								  encodedImage = Base64.encodeToString(profilePic, Base64.DEFAULT);
								  
								
						  		
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							}
						}  
						break;
						
					case CAMERA_REQUEST: 
						if (resultCode == RESULT_OK)
						{
							 Bitmap photo = (Bitmap) data.getExtras().get("data"); 
					         //  imageView.setImageBitmap(photo);
							// profPic = (ImageView)findViewById(R.id.camerapreview);
							 ImageView profPicture = (ImageView)findViewById(R.id.camerapreview);
							//  Bitmap bitmap = BitmapFactory.decodeByteArray(profilePic, 0, profilePic.length);
							  profPicture.setImageBitmap(photo);
							
//					            
					           ByteArrayOutputStream baos = new ByteArrayOutputStream();
					           photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);   
					           profilePic = baos.toByteArray(); 
					      //     cameraBtn.setVisibility(View.INVISIBLE);
					            encodedImage = Base64.encodeToString(profilePic, Base64.DEFAULT);
					       
					    
							  
						}
						break;
						default:
						if (facebookLoginButton!=null )
						{
							super.onActivityResult(requestCode, resultCode, data);
							FacebookUtilities.mFacebook.authorizeCallback(requestCode, resultCode, data);
						}
						break;
				
					}
		 }
	 
	 
	 
	 
	    private void setPictureFromFile(Uri photoUri)
	    {
	    	File imgFile = new  File(photoUri.getPath());
	    	if(imgFile.exists()){

	    	    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

	    	  //  ImageView myImage = (ImageView) findViewById(R.id.camerapreview);
	    	    imgageBtn.setImageBitmap(myBitmap);
	    	    
	    	    

	    	}
			
		}

	 
	 
	@Override
	public void getAllAtlasUserEmailAddressRecordsCallBack(
			ArrayList<EmailAddressRecord> emailAddressRecords) {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void getAllAtlasUserEmailAddressCallBack(
			ArrayList<String> emailAddrees) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getSignNonAtlasUsersCallBack(SIGN_NEW_USERS_CALLER caller,
			boolean success,
			HashMap<String, ATLContactModel> newCurrentNonAtlasUserToSign) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getFriendEmailOnParse(
			ATLContactModel friendsPropertiesOnParseByEmail, boolean success) {
		// TODO Auto-generated method stub
		
	}

	

	

	@Override
	public void getUserEmailOnParseCallBack(
			HashMap<String, String> userParseLogin, boolean success) {
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
	public void getAtlasNewFriendsByEmailCallBack(
			ArrayList<ATLContactModel> allCommonAtlasUsers) {
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
	@Override
	public void getAtlasUserByEmailAddressCallBack(
			HashMap<String, Object> userLoginInfo) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void resetPasswordCallBack(boolean emailSuccessfullySent,
			String parseMessage) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getEmailAddressRecordCallBack(
			EmailAddressRecord emailAddressRecord) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void isEmailVerifiedCallBack(boolean verified) {
		// TODO Auto-generated method stub
		
	}
	
		
	

}
