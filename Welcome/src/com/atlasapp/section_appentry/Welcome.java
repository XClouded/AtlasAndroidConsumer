//  ==================================================================================================================
//  Welcome.java
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.atlasapp.atlas_database.ATLDBCommon;
import com.atlasapp.atlas_database.AtlasServerConnect;
import com.atlasapp.atlas_database.DatabaseConstants.SIGN_NEW_USERS_CALLER;
import com.atlasapp.atlas_database.ParseDBCallBackInterface;
import com.atlasapp.atlas_database.UserDelagateInterface;
import com.atlasapp.common.ATLAnimationUtils;
import com.atlasapp.common.ATLColor;
import com.atlasapp.common.AtlasAndroidUser;
import com.atlasapp.common.CurrentSessionFriendsList;
import com.atlasapp.common.DB;
import com.atlasapp.common.OnHeadlineSelectedListener;
import com.atlasapp.common.PushNotificationActivityReciever;
import com.atlasapp.common.SlideOutActivity;


import com.atlasapp.facebook.ProfilePictureView;
import com.atlasapp.facebook.FacebookUtilities;
import com.atlasapp.facebook.LoginButton;
import com.atlasapp.model.ATLFriendModel;
import com.atlasapp.model.ATLContactModel;
import com.atlasapp.section_calendar.CalendarDayView;
import com.atlasapp.section_settings.GetProfilePic;
import com.atlasapp.section_settings.Settings;
import com.atlasapp.section_today.Today;
//import com.example.atlasapp.FacebookConnect.CONNECT_TYPE; 
import com.facebook.FacebookActivity;
import com.facebook.GraphObject;
import com.facebook.GraphUser;
//import com.facebook.ProfilePictureView;
import com.facebook.Request;
import com.facebook.Response;  
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
//import com.facebook.android.R;
import com.parse.LogInCallback;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFacebookUtils.Permissions;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.PushService;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class Welcome extends   FacebookActivity implements OnHeadlineSelectedListener , UserDelagateInterface{ 

	//indicate that the activity is visible
	private boolean isResumed = false;
	// Facebook facebook; 
	private LoginButton authButton;
	private ProfilePictureView profilePictureView;
	private int picTypePicker = 3;// cancel default
	private static final int CAMERA_REQUEST = 1888; 
    private ImageView imageView;
    static final int INTENT_REQUEST_CODE_BROWSE_PICTURE = 1;
	
	private AtlasApplication applicationController;
	public static enum CONNECT_TO_ATLAS_VIA { SIGNIN,  REGISTER, FACEBOOK}

	private boolean hasLocalProfile=false;
	private Session session;
	private String currentAccessToken;  
	public static final String PREFS_NAME = "AtlasProfileLogin";
	private boolean registerSuccess;  
	private CONNECT_TO_ATLAS_VIA from;    
	public static String previouslyEncodedImage="";
	private byte[] profileBytesPic;
	private ImageButton fbSignInBtn;
	private AtlasServerConnect parseConnect; 
	private static byte[] profilePic=null;
	private String encodedImage;
	private static String name="";
	private static String pass="";
	private static String emailSignIn = "";
	private static String passSignIn="";
	private static String passVerify="";
	private static String email="";
	private static byte[] profilePicInt;
	
	public static void setProfilePic(byte[] profPic){profilePicInt =(profPic!=null)?profPic:profilePicInt; }

	
	
	private ATLContactModel[] contacts;
	private CurrentSessionFriendsList currentSessionFriendsList;
	private ATLDBCommon atlDBCommon;
	private static ImageButton registerBtn;
	private static EditText passwordVerify;
	private static EditText userNameDisplay;
	private static EditText emailEdit;
	private static EditText password;
	private static ImageView profPic;
	private static EditText userNameSignInEdit,passwordSignInEdit;
	public static String message="";
	    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
		applicationController = (AtlasApplication)getApplicationContext();

        parseConnect =   AtlasServerConnect.getSingletonObject(this);
        parseConnect.application = applicationController;

//        
        //Opens the DB
         DB.openDb();
         
         // finding and updating the Friend DB in the background....
        currentSessionFriendsList = CurrentSessionFriendsList.getSingletonObject();
      //   CurrentSessionFriendsList.updateDB();
         
       //  backgroundProcesses();
        
        from = CONNECT_TO_ATLAS_VIA.FACEBOOK;
        
        String pushMessage = "";
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	String connecting = extras.getString("from");
        	if (connecting!=null)
        	 from = (connecting.equals("signIn"))? CONNECT_TO_ATLAS_VIA.SIGNIN:
        		 (connecting.equals("register"))? CONNECT_TO_ATLAS_VIA.REGISTER:CONNECT_TO_ATLAS_VIA.FACEBOOK;
        	else{
        		String jsonString = extras.getString("com.parse.Data");
        		if (jsonString!=null && !jsonString.equals("")){
        		 String data = "";
        		  try {
      		   		JSONObject json = new JSONObject(jsonString);
			
      		   		Iterator itr = json.keys();
      		   		while (itr.hasNext()) 
      		   		{
      		   			String key = (String) itr.next();
      		   			String value = json.getString(key);
      		   			data = (key.equals("inviter_message"))? value :data;
      	   
      		   		}
	   		
      		   		pushMessage = data;
      	   
      	   
      	   } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	}
        	}
        }
        if (!pushMessage.equals(""))
        {
        	backgroundProcesses();
        	Intent pushintent = new Intent(getBaseContext(), Settings.class);
		   	 pushintent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		   	 pushintent.putExtra("message", pushMessage);
		   	 startActivity(pushintent);
        }
        else
        {
        
      ///  ParseAtlasApplication applicationController = (ParseAtlasApplication)getApplicationContext();
        hasLocalProfile = applicationController.localAtlasProf;
           
        
        if (hasLocalProfile && AtlasAndroidUser.isLoggedIn())
        {
        	parseConnect.unSubscribedToChannels(getApplicationContext(), "ID"+AtlasAndroidUser.getParseUserID());
    		parseConnect.subscribedToChannels(getApplicationContext(), "ID"+AtlasAndroidUser.getParseUserID(),  Welcome.class);
    		backgroundProcesses();
        	
        	Intent intent = new Intent(getBaseContext(), CalendarDayView.class);// Tan point to CalendarView
        	intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        	//intent.putExtra("EXTRA_SESSION_ID", sessionId);
        	startActivity(intent);
        } 
        else
        {
        	
        	if (from.equals(CONNECT_TO_ATLAS_VIA.SIGNIN))
        	{
        		
        		SignIn();
        		
        	}
        	else
        		if (from.equals(CONNECT_TO_ATLAS_VIA.REGISTER))
        		{
        			
        			
        			RegisterUser();
        		}
        		else
        		{
        			setContentView(R.layout.welcome);
        	
        	
        			setFBbtns();
      	  
      	  
      	  
      	  
        			ImageButton btnSignIn = (ImageButton)findViewById(R.id.welcomeSignInBtn);
           
        			btnSignIn.setOnClickListener(
        					new View.OnClickListener() {
        						@Override
        						public void onClick(View v) {

        							SignIn();
        						}
        					});
        	 
        			ImageButton btnRegister = (ImageButton)findViewById(R.id.welcomeRegisterBtn);
             
        			btnRegister.setOnClickListener(
        					new View.OnClickListener() {
        						

								@SuppressWarnings("deprecation")
								@Override
        						public void onClick(View v) {
        							if (hasLocalProfile)
        							{
        								AlertDialog alertDialog = new AlertDialog.Builder(
        										Welcome.this).create();

        								// Setting Dialog Title
        								alertDialog.setTitle("");

        								// Setting Dialog Message
        								alertDialog.setMessage("An Atlas account was already found on this device, please try to Sign in");

        								// Setting Icon to Dialog
        							//	alertDialog.setIcon(R.drawable.tick);

        								// Setting OK Button
        								alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
        										public void onClick(DialogInterface dialog, int which) {
        										// Write your code here to execute after dialog closed
        										//Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
        										}
        								});

        								// Showing Alert Message
        								alertDialog.show();

        								
//        								Toast.makeText(getApplicationContext(), "An Atlas account was already found on this device, please try to Sign in",
//        			    	    				Toast.LENGTH_LONG).show();
        							}else
        								RegisterUser();
        						}
        					});
        	
   
        		}
        	
        	
        	}
        
        }
       
    }
    
    
    private void backgroundProcesses() {
		currentSessionFriendsList.setFriendsUpdateComplete(false);

		/////SET ALL CURRENT EMAIL'S (USER NAMES) FROM ATLAS DB (PARSE)
		atlDBCommon = ATLDBCommon.getSingletonObject(null);
		atlDBCommon.setCurrentSessionUsersOnATLASInBackground(true);
	}
    
    
    @SuppressWarnings("deprecation")
	private void alertUser(String messageTitle, String message)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(
				Welcome.this).create();

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
				
				
				
				}
		});

		// Showing Alert Message
		alertDialog.show();
	}
	private void saveLastUserRegistrationInput()
	{
		  passVerify = (passVerify!=null && passVerify.equals(""))? passwordVerify.getText().toString():passVerify;
		  name = (name!=null &&  name.equals(""))? userNameDisplay.getText().toString():name;
		  email = (email!=null &&  email.equals(""))?emailEdit.getText().toString():email;
		  pass = (pass!=null && pass.equals(""))?password.getText().toString():pass;
	
		  
	
	}
	private void saveLastUserSignInInput()
	{
		  emailSignIn = (emailSignIn!=null &&  emailSignIn.equals(""))?userNameSignInEdit.getText().toString():emailSignIn;  
	
		  
	
	}
	private void setRegistrataionInfoFromLatestInput()
	{
		
		  passwordVerify = (EditText)findViewById(R.id.registerPasswordVerifiedtxt);
		  userNameDisplay = (EditText)findViewById(R.id.registerNameDisplayEditTxt);
		  emailEdit = (EditText)findViewById(R.id.registerEmailEditTxt);
		  password = (EditText)findViewById(R.id.registerPasswordtxt);
		  profPic = (ImageView)findViewById(R.id.camerapreview);
		
		
		 passwordVerify.setText(passVerify);
		 userNameDisplay.setText(name);
		 emailEdit.setText(email);
		 password.setText(pass);
	}
	/**
	 * Sets "register" as the screen layout
	 * if the user chooses to register through Input
	 * then transfer to the Register.java Activity
	 * if through Facebook , stays on Welcome.java (FacebookActivity) 
	 */
	 private void RegisterUser()
	 {
		 setContentView(R.layout.register);
		 
		 setRegistrataionInfoFromLatestInput();
	        

			//	final byte[] profPicture = profPic.getDrawingCache();
				
//				if (profPic!=null){
//				 byte[] profPicture;
//				  ByteArrayOutputStream baos = new ByteArrayOutputStream();
//				  
//				  Bitmap b = profPic.getDrawingCache();
//				  if (b!=null){
//					  b.compress(Bitmap.CompressFormat.JPEG, 100, baos);   
//					  profPicture = baos.toByteArray();
//				  } 
//				}
		           Bundle extras = getIntent().getExtras();
			        if (extras != null) {
			        	name =(extras.getString("name")!=null)?extras.getString("name"):name;
			        	pass =( extras.getString("pass")!=null)?extras.getString("pass"):pass;
			        	passVerify =(extras.getString("passVerify")!=null)?extras.getString("passVerify"):passVerify;
			        	email = (extras.getString("email")!=null)?extras.getString("email"):email;
//			        	if (extras.getByteArray("picture")!=null)
//			        		profilePicInt = extras.getByteArray("picture");
//			        	
			        }
			  
			   
			  
			
			 if (profilePicInt!=null){
				 Bitmap bitmap = BitmapFactory.decodeByteArray(profilePicInt, 0, profilePicInt.length);
				 profPic.setImageBitmap(bitmap);
			 }

		 if (extras != null) {
	           this.picTypePicker = extras.getInt("picture_from");
	       }
	       if (picTypePicker==1)
	    	   chooseFromLibrary();
	       else
	    	   if (picTypePicker==2)
	    		   takeAPhoto();
		
		 
		    registerBtn = (ImageButton)findViewById(R.id.registerBtn);
			registerBtn.setOnClickListener(
					new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
//							 EditText passwordVerify = (EditText)findViewById(R.id.registerPasswordVerifiedtxt);
//							 EditText userNameDisplay = (EditText)findViewById(R.id.registerNameDisplayEditTxt);
//							 EditText emailText = (EditText)findViewById(R.id.registerEmailEditTxt);
//							 EditText password = (EditText)findViewById(R.id.registerPasswordtxt);
//							 ImageView profPic = (ImageView)findViewById(R.id.camerapreview);
//							 
							saveLastUserRegistrationInput(); 
				
							
							Intent intentReg = new Intent(getBaseContext(), Register.class);
							intentReg.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
							intentReg.putExtra("pass",pass);
							intentReg.putExtra("passVerify", passVerify);
							intentReg.putExtra("email",email);
							intentReg.putExtra("name", name);
							Register.setProfilePic(profilePic);
//							if (profilePic!=null)
//								intentReg.putExtra("picture", profilePic);
				         	startActivity(intentReg);
							
							  
						}

						
					});
			
			// setting the picture picker type (library/snap)
						ImageButton btnCamera = (ImageButton)findViewById(R.id.editProfileCameraBtn);
						 
						//ImageView profPic = (ImageView)findViewById(R.id.camerapreview);
						
//						if (profilePic!=null)
//						 {
//							 Bitmap bitmap = BitmapFactory.decodeByteArray(profilePic, 0, profilePic.length);
//							 profPic.setImageBitmap(bitmap);
//							 
//						 }
					    btnCamera.setOnClickListener( new View.OnClickListener() {
							
					    	   
					    	   
					    	   
					    	     
							@Override
							public void onClick(View v) {
								
								saveLastUserRegistrationInput(); 
								int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
									SlideOutActivity.prepare(Welcome.this, R.id.registerMainLayout, width);
									Intent intent = new Intent(Welcome.this,
											GetProfilePic.class);
									intent.putExtra("from", "register");
									startActivity(intent);
									
									overridePendingTransition(0, 0);

								
							}
						});
						
					  	
			
			ImageButton backBtn = (ImageButton)findViewById(R.id.registerBackBtn);
			backBtn.setOnClickListener(
	 				new View.OnClickListener() {
	 					@Override
	 					public void onClick(View v) {
	 						Intent intent = new Intent(getBaseContext(), Welcome.class);
	 		            	intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

	 		            	startActivity(intent);
	 		            	finish();// TAN add to keep single top instance
	 					}
	 					
	 				});
			
			
			setFBbtns();
			
		 
		
	 }

	
		 private void chooseFromLibrary()
		   {
			   ///// Chose From Libraray option
			 
			 Intent intent = new Intent(Intent.ACTION_PICK,
		               (MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
			 intent.putExtra("from", "register");
		       startActivityForResult(intent,
		    		   INTENT_REQUEST_CODE_BROWSE_PICTURE);
		   
			 
			 
//		       Intent intent = new Intent();
//					intent.setType("image/*");
//					intent.setAction(Intent.ACTION_GET_CONTENT);
//					intent.addCategory(Intent.CATEGORY_OPENABLE);
//					intent.putExtra("from", "register");
//					startActivityForResult(intent,
//							INTENT_REQUEST_CODE_BROWSE_PICTURE);
		   }
		 private void takeAPhoto()
		   {
			/////// Take A Picture option....
			 Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
			 cameraIntent.putExtra("from", "register");
			 startActivityForResult(cameraIntent, CAMERA_REQUEST); 
		   }
	 private void setFBbtns()
	 {
		  fbSignInBtn = (ImageButton)findViewById(R.id.fbSignInBtn);
		 authButton = (LoginButton) findViewById(R.id.fbauthButton);
		 if (AtlasAndroidUser.getfbID().equals(""))
		 {
			 fbSignInBtn.setVisibility(View.INVISIBLE);
			 fbSignInBtn.setEnabled(false);  
			 authButton.setEnabled(true);
			 authButton.setVisibility(View.VISIBLE);
			 authButton.setApplicationId(getString(R.string.facebook_app_id));
//		    	
					 
			authButton.setReadPermissions(Arrays.asList("email"));
			 authButton.setBackgroundResource(R.drawable.signin_btn_signin_fb);
			 authButton.setText("");
		 }else
		 {
			 authButton.setText("");
			 fbSignInBtn.setVisibility(View.VISIBLE);
			 fbSignInBtn.setEnabled(true);
			 authButton.setEnabled(false);
			 authButton.setVisibility(View.INVISIBLE);
			 fbSignInBtn.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						 setContentView(R.layout.loading);
						 findViewById(R.id.mainSpinner1).setVisibility(View.VISIBLE);
						 
						
						Session session = Session.getActiveSession();
						session = (session == null)? Session.openActiveSession(getBaseContext()):session;
				    	 if (session!=null){
				    		 SessionState state = session.getState();
	
				    		 loginUser(session, state);
				    	 }
						
					}
				});
		 }
		   
	 }
		private void setSignInInfoFromLatestInput()
		{
			
			  
			 userNameSignInEdit =(EditText)findViewById(R.id.signInUserNameEditTxt);
			 passwordSignInEdit =(EditText)findViewById(R.id.signInPasswordTxt);
			  
//			 emailSignIn = (emailSignIn!=null && !emailSignIn.equals(""))? emailSignIn : userNameSignInEdit.getHint().toString();
//			 passSignIn = passwordSignInEdit.getHint().toString();
			   Bundle extras = getIntent().getExtras();
		        if (extras != null) {
		        	//passSignIn =( extras.getString("pass_signin")!=null)?extras.getString("pass_signin"):passSignIn;
		        	emailSignIn = (extras.getString("email_signin")!=null)?extras.getString("email_signin"):emailSignIn;
//		        	if (extras.getByteArray("picture")!=null)
//		        		profilePicInt = extras.getByteArray("picture");
//		        	
		        }
		        if (!emailSignIn.equals(""))  
		        	userNameSignInEdit.setText(emailSignIn);   
		
		}
	 /**
		 * Sets "signin" as the screen layout
		 * if the user chooses to signIn through Input(username & password)
		 * then transfer to the SignIn.java Activity
		 * if through Facebook , stays on Welcome.java (FacebookActivity) 
		 */
	 
	private EditText passwordEditText; 
	private EditText emailEditText;
	private HashMap<String, Object> userFacebookLoginInfo;
	private GraphUser currentFacebookGraphUser;
	private void SignIn() {
		 setContentView(R.layout.sign_in);  
		 setFBbtns();
		//2013-01-26 TAN: Fix style of user name & password edit text
	    passwordEditText = (EditText)findViewById(R.id.signInPasswordTxt);
	    passwordEditText.setTypeface(Typeface.DEFAULT_BOLD);
	    passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
	    passwordEditText.setHintTextColor(ATLColor.LTGRAY);
	    
	    emailEditText = (EditText)findViewById(R.id.signInUserNameEditTxt);
	    emailEditText.setTypeface(Typeface.DEFAULT_BOLD);
	    emailEditText.setHintTextColor(ATLColor.LTGRAY);
	    emailEditText.setText(email);
	    final Context tempContext = this;
	    passwordEditText
		.setOnEditorActionListener(new EditText.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					email = emailEditText.getText().toString();
					String pass = passwordEditText.getText().toString();

					Intent intent = new Intent(getBaseContext(), SignIn.class);
		         	intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		        	intent.putExtra("pass",pass);
			         
		         	intent.putExtra("email",email);
		         	startActivity(intent);
		         	ATLAnimationUtils.hideKeyboard(tempContext, emailEditText);
		         	finish();
					return true;
				}
				return false;
			}

		});
	    
	    if(email!= null && email.length()>0){
	    	passwordEditText.requestFocus();
	    	
//	    	ATLAnimationUtils.hideKeyboard(this, emailEditText);
//	 	    final Context tempContext = this;
//	 	    passwordEditText.postDelayed(new Runnable() {
//	 			
//	 			@Override
//	 			public void run() {
//	 				// TODO Auto-generated method stub
//	 				 ATLAnimationUtils.showKeyboard(tempContext);
//	 			}
//	 		}, 1000);
	    }
	    else{
	    	 ATLAnimationUtils.showKeyboard(this);
	    }
	   
	    //================END========================================
		
		
		 ImageButton backBtn = (ImageButton)findViewById(R.id.signinBackBtn);
		    backBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Intent intent = new Intent(getBaseContext(), Welcome.class);
		         	intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

		         	startActivity(intent);
		         	finish();
				}
			});
		    ImageButton signInBtn = (ImageButton)findViewById(R.id.signinBtn);
		    signInBtn.setOnClickListener(new View.OnClickListener() {
				
					@Override
					public void onClick(View v) {
						
						email = emailEditText.getText().toString();
						String pass = passwordEditText.getText().toString();

						Intent intent = new Intent(getBaseContext(), SignIn.class);
			         	intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			        	intent.putExtra("pass",pass);
			         	intent.putExtra("email",email);
			         	startActivity(intent);
			         	ATLAnimationUtils.hideKeyboard(tempContext, emailEditText);
			         	finish();
					
					}

					
				});
		    
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
//	        session = Session.getActiveSession();
//	        if (session!=null && authButton!=null && authButton.isEnabled()){
//	        	
//	        	if (isResumed)
//	        	{
//	        		loginUser(session, session.getState());
//	        	 
//	        	}
//	        }
	
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
	        if (session!=null && authButton!=null && authButton.isEnabled()){
	        	
	        	if (isResumed && !AtlasAndroidUser.isLoggedIn())
	        	{
	        		
	        		loginUser(session, state);
	        	 
	        	}
	        }
	    	
	       
	    }
	    
	    
	    
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
		   	            // Logged Out
		        		   }
	    	 }
	    }
	    
	    
	    /**
		 * Sets the previouslyEncodedImage String from bytes[]
		 * once user choose a picture and using the AtlasAndroidUser picture
		 * property as a default.
		 */
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {  
		       
			

			if (authButton!=null && from == CONNECT_TO_ATLAS_VIA.FACEBOOK)
			{
				super.onActivityResult(requestCode, resultCode, data);
				FacebookUtilities.mFacebook.authorizeCallback(requestCode, resultCode, data);
			}else
			if (from.equals(CONNECT_TO_ATLAS_VIA.REGISTER))
			{ 
			
			    encodedImage="";
			   switch (requestCode) {
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
						//	  profPic = (ImageView)findViewById(R.id.camerapreview);
							  Bitmap bitmap = BitmapFactory.decodeByteArray(profilePic, 0, profilePic.length);
							  profPic.setImageBitmap(bitmap);
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
						 profPic = (ImageView)findViewById(R.id.camerapreview);
						 profPic.setImageBitmap(photo);
						
//				            
				           ByteArrayOutputStream baos = new ByteArrayOutputStream();
				           photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);   
				           profilePic = baos.toByteArray(); 

				            encodedImage = Base64.encodeToString(profilePic, Base64.DEFAULT);
				       
				    
						  
					}
					break;
					
			
				}
			   
//			   if ( !encodedImage.equals("")){
//			   
//				   SharedPreferences sp=getSharedPreferences(PREFS_NAME, 0);
//				   SharedPreferences.Editor Ed=sp.edit();
//		     
//				   Ed.putString("profile_pic",encodedImage);
//				   Ed.commit();
//			   }
			   
			   
//			   previouslyEncodedImage =(encodedImage.equals(""))?
//						 AtlasAndroidUser.getPicture():encodedImage;
//						 
//						 
//						 
//						 
//						 ImageView profPic = (ImageView)findViewById(R.id.camerapreview);				    	   
//		      if(previouslyEncodedImage!=null && !previouslyEncodedImage.equalsIgnoreCase("") )
//			  {
//				 profileBytesPic = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
//				             
//				 Bitmap bitmap = BitmapFactory.decodeByteArray(profileBytesPic, 0,
//						 profileBytesPic.length);
//				 profPic.setImageBitmap(bitmap);
//			 }
			}
			   
		}
	    
	    
	    
	    
	    
	    private void setPictureFromFile(Uri photoUri)
	    {
	    	File imgFile = new  File(photoUri.getPath());
	    	if(imgFile.exists()){

	    	    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

	    	    ImageView myImage = (ImageView) findViewById(R.id.camerapreview);
	    	    myImage.setImageBitmap(myBitmap);
	    	    
	    	    

	    	}
			
		}

		/**
	 
	   
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
	    	String email = user.getProperty("email").toString();
	    	boolean registerSuccess = false;
	    	
	    	 userFacebookLoginInfo = new HashMap<String, Object>();
	    	userFacebookLoginInfo.put("facebook_id", user.getId());
	    	userFacebookLoginInfo.put("access_token", currentAccessToken);
	    	
	    	/// user got a local atlas profile
	    	if (this.applicationController.localAtlasProf)
	    	{
	    		
	    		boolean firstConnectionWithFB = AtlasAndroidUser.getfbID().equals("");
	    		if (email.equals(AtlasAndroidUser.getEmail() )||
	    				!firstConnectionWithFB)
	    		{
	    			// user has local profile and email from facebook matched the profile info
	    			// or fb_id matched the one found on shared preferences
	    			// USER HASNT BEEN VERFIED ON SERVER (FB_ID & ACCESS TOKEN ARNT UPDATED ON SERVER 
	    			// SIDE TILL HE SIGN ON SERVER)
	    			
	    			applicationController.setAtlasSharedPreferences(userFacebookLoginInfo);
	    			applicationController.upadteApplicationUsersPropertiesFromLocal();
	    			applicationController.upadteAtlasAndroidUserObjectFromApp();
	    			
	    			applicationController.logInOrOutUser(true);
	    			 Toast.makeText(getApplicationContext(), "Register with Facebook...",
     		    			Toast.LENGTH_LONG).show();
	    			if  (firstConnectionWithFB)
	    			{
	    				parseConnect.setUserListener(this);
	    				parseConnect.updateAtlasUserInfoOnServer(userFacebookLoginInfo,this);
	    			}else
	    				{
	    					FacebookUtilities.access_token = (String) userFacebookLoginInfo.get("access_token");
	    					FacebookUtilities.mFacebook.setAccessToken(FacebookUtilities.access_token);
	    					FacebookUtilities.mFacebook.setAccessExpires(Session.getActiveSession()
	    							.getExpirationDate()
	    							.getTime());
	    					FacebookUtilities.mAsyncRunner = new AsyncFacebookRunner( FacebookUtilities.mFacebook);
	     			
	    					FacebookUtilities.setFriendsRequest(getApplicationContext());
	    					registerSuccess = true;
	    					onFacebookRegisterProcesscomplete(true);
	    				}
	    		}
	    		else
	    		{
	    			/// User got local profile email that doesnt match
	    			/// the one got from Facebook...
	    		
	    			message = "This Atlas already in use by another Facebook user";
	    			  
	    			
	    			FacebookUtilities.mFacebook.setAccessToken(null);
	    			 FacebookUtilities.mFacebook.setAccessExpires(-1);
	 			    session.close();
	    			
	    			
	    			registerSuccess = false;
	    			onFacebookRegisterProcesscomplete(false);

	    		}
	    	}
	    	/// User doesnt have local profile 
	    	
	    	else
	    	{
	    		parseConnect.getUserEmailOnParse(email,this);
	    	}
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

		
		
		
		private void onFacebookRegisterProcesscomplete(boolean success)
		{
			if (success){
        		
                    	backgroundProcesses();
        		    	Intent intent = new Intent(getBaseContext(), CalendarDayView.class);// Tan point to CalendarView
        		    	intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//	            		//intent.putExtra("EXTRA_SESSION_ID", sessionId);
        		    	startActivity(intent);
        		    }else
        		    {
        		    	String currentMessage = (message.equals(""))?  "Login with Facebook Failed!" :message;
//        		    	Toast.makeText(getApplicationContext(),currentMessage,
//	    	    				Toast.LENGTH_LONG).show();
        		    	alertUser("Login with Facebook ",currentMessage);
        		    	   FacebookUtilities.mFacebook.setAccessToken(null);
	        			   FacebookUtilities.mFacebook.setAccessExpires(-1);
	        			   Session.getActiveSession().close();
        		    	//intent = new Intent(getBaseContext(), Welcome.class);
        		    }
		}

	



		@Override
		public void registerSuccess(boolean signUpSuccess) {
			applicationController.setAtlasUserLocalInfo();
			if (!signUpSuccess)
			{
				onFacebookRegisterProcesscomplete(false);
				return;
			}
			else
				if (AtlasServerConnect.isSignOnServer())
			{
				applicationController.logInOrOutUser(true);
				FacebookUtilities.access_token = (String) userFacebookLoginInfo.get("access_token");
				
				
				FacebookUtilities.mFacebook.setAccessToken(FacebookUtilities.access_token);
 				 FacebookUtilities.mFacebook.setAccessExpires(Session.getActiveSession()
                         .getExpirationDate()
                         .getTime());
 				FacebookUtilities.mAsyncRunner = new AsyncFacebookRunner( FacebookUtilities.mFacebook);
 				FacebookUtilities.setFriendsRequest(getApplicationContext());
				registerSuccess = true;
				
			//// Retreive and Store User Facebook profile picture....
        		if (AtlasAndroidUser.getfbID()!=null && !AtlasAndroidUser.getfbID().equals(""))
            	{
            		ProfilePictureView profilePictureView = new ProfilePictureView(getBaseContext());
     				profilePictureView.setCropped(true);
     	    	    profilePictureView.setUserId(AtlasAndroidUser.getfbID());
     	    	    profilePictureView.queryHeight = 100;
     	    	    profilePictureView.queryWidth = 100;
     	    	    profilePictureView.sendProfileImageRequest(true);
     	    	  
            	}
			}
			onFacebookRegisterProcesscomplete(registerSuccess);

		}


		@Override
		public void signInSuccess( boolean success) {
			if (success && parseConnect.isSignOnServer())
			{
				/// uodate user's Facebook info on Server...
				parseConnect.updateAtlasUserInfoOnServer(userFacebookLoginInfo,this);
				parseConnect.setCurrentAtlasAndroidUserFromServer();
				
				if (parseConnect.parseUser!=null)
				{  
					applicationController.setAtlasUserLocalInfo();
					applicationController.logInOrOutUser(true);
					FacebookUtilities.access_token = (String) userFacebookLoginInfo.get("access_token");
					
					FacebookUtilities.mFacebook.setAccessToken(FacebookUtilities.access_token);
	 				 FacebookUtilities.mFacebook.setAccessExpires(Session.getActiveSession()
	                         .getExpirationDate()
	                         .getTime());
	 				//FacebookUtilities.mFacebook.setAccessToken(AtlasAndroidUser.getAccessToken());
	 				FacebookUtilities.mAsyncRunner = new AsyncFacebookRunner( FacebookUtilities.mFacebook);
	 				FacebookUtilities.setFriendsRequest(getApplicationContext());

					
	    			registerSuccess = true;
	    			
				}
				
			}
			onFacebookRegisterProcesscomplete(registerSuccess);
			
		}



		@Override
		public void getUserEmailOnParseCallBack(
				HashMap<String, String> loginProperties, boolean success) {
			if (!loginProperties.isEmpty())
    		{
    			/// email got from facebook already in Parse and user doesnt have 
    			/// local profile on his mobile
    			/// create a local profile from server with facebook info
    			 Toast.makeText(getApplicationContext(), "Creating new Atlas profile ...",
 		    			Toast.LENGTH_LONG).show();
    			 /// get facebook call back from signInSuccess method
    			parseConnect.logUserToServer(loginProperties.get("email"), loginProperties.get("password_copy"),this);
    		
    			
    		}
    		else
    		{
    		 if (currentFacebookGraphUser==null)
    		 {
    				onFacebookRegisterProcesscomplete(false);

    		 }else
    			 
    		 {
    			///User dont have a local profile and email got from
    			////Facebook doesnt exsits on parse	....create a new account 
    			//// from Facebook Info.
    			 Toast.makeText(getApplicationContext(), "Creating new Atlas profile ...",
     		    			Toast.LENGTH_LONG).show();
    			 userFacebookLoginInfo.put("first_name", currentFacebookGraphUser.getName());
    			// generate a password for parse using username+id (from facebook)
    			 userFacebookLoginInfo.put("password_copy", currentFacebookGraphUser.getUsername()+currentFacebookGraphUser.getId());
    			 userFacebookLoginInfo.put("email", currentFacebookGraphUser.getProperty("email").toString());
			
    			//// TO DO: SET PICTURE
    			applicationController.setAtlasSharedPreferences(userFacebookLoginInfo);
    			applicationController.upadteApplicationUsersPropertiesFromLocal();
    			applicationController.upadteAtlasAndroidUserObjectFromApp();
			
    			
    			/// call back from registerSuccess method
    			parseConnect.createNewAtlasUser(this);
    		 }
			
    		}
			
		}


		@Override
		public void getFriendEmailOnParse(
				ATLContactModel friendsPropertiesOnParseByEmail, boolean success) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void getFacebookFriendIdOnParse(
				ATLContactModel facebookAtlasFriend, boolean success) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void getAllFBAtlasUsersCallBack(
				ArrayList<ATLContactModel> allFacebookAtlasUsers,
				boolean success) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void getAllFBAtlasUsersFriendsCallBack(
				ArrayList<ATLContactModel> allFacebookAtlasUsers,
				boolean success) {
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
			if (success){
			FacebookUtilities.access_token = (String) userFacebookLoginInfo.get("access_token");
			FacebookUtilities.mFacebook.setAccessToken(FacebookUtilities.access_token);
				 FacebookUtilities.mFacebook.setAccessExpires(Session.getActiveSession()
                     .getExpirationDate()
                     .getTime());
			FacebookUtilities.mAsyncRunner = new AsyncFacebookRunner( FacebookUtilities.mFacebook);
 			
				FacebookUtilities.setFriendsRequest(getApplicationContext());
			registerSuccess = true;
			}
			onFacebookRegisterProcesscomplete(success);

			
		}


		@Override
		public void getSignNonAtlasUsersCallBack(SIGN_NEW_USERS_CALLER caller,
				boolean success,
				HashMap<String, ATLContactModel> newCurrentNonAtlasUserToSign) {
			// TODO Auto-generated method stub
			
		}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
//    @Override
//    public void onResume() {
//        super.onResume();
//        isResumed = true;
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        isResumed = false;
//    }
//    
//    /**
//     * If the activity is visible show 
//     */
//    @Override
//    protected void onSessionStateChange(SessionState state, Exception exception) {
//    	// Get the active session
//
//    	session = Session.getActiveSession();
//        super.onSessionStateChange(state, exception);
//        if (isResumed)
//        {
//        	   if (state.isOpened()) {
//   	            // Logged In
//        		   Toast.makeText(getApplicationContext(), "Logged In",
//       	    			Toast.LENGTH_LONG).show();
//        		   
//        		   
//        		   
//        		//   sendRequestButton.setVisibility(View.VISIBLE);
//
//        	        // Set the Facebook instance session variables
//        	        facebook.setAccessToken(Session.getActiveSession()
//        	                                       .getAccessToken());
//        	        facebook.setAccessExpires(Session.getActiveSession()
//        	                                         .getExpirationDate()
//        	                                         .getTime());
//        		   
//        		   
//        		   
//        		// If the session is open, make an API call to get user data
//        		    // and define a new callback to handle the response
//        		    Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
//        		        @Override
//        		        public void onCompleted(GraphUser user, Response response) {
//        		            // If the response is successful
//        		            if (session == Session.getActiveSession()) {
//        		                if (user != null) {
////        		                	AtlasAndroidUser.setFb_id(user.getId());
////        		                	AtlasAndroidUser.setEmail(user.getProperty("email").toString());
////        		                	AtlasAndroidUser.setUserName(user.getUsername());
////        		                	AtlasAndroidUser.setUserNameDisplay(user.getName());
////        		                
//        		                	Toast.makeText(getApplicationContext(), 
//        		                			" email "+user.getProperty("email")+" id "+user.getId()+" access "+Session.getActiveSession()
// 	                                       .getAccessToken(),
//        	        		    			Toast.LENGTH_LONG).show();
//        		                	
//        		                	RegisterWithFacebook(user);
//        		                    // Set the id for the ProfilePictureView
//        		                    // view that in turn displays the profile picture
//        		             //       profilePictureView.setUserId(user.getId());
//        		                    // Set the Textview's text to the user's name
//        		             //       userNameView.setText(user.getName());
//        		                }   
//        		            }   
//        		        }
//
//						   
//        		    }); 
//        		    Request.executeBatchAsync(request);
//        		   
//        		   
//        		   
//        	   } else 
//        		   if (state.isClosed()) 
//        		   {  
//        			   Toast.makeText(getApplicationContext(), "Logged Out",
//        		    			Toast.LENGTH_LONG).show();
//        			   
//        			   
//        			// Clear the Facebook instance session variables
//        		        facebook.setAccessToken(null);
//        		        facebook.setAccessExpires(-1);
//   	            // Logged Out
//        		   }
//        }
//    }
//    /**
//     * Get user information from facebook and update AtlasAndroidUser Object
//     * @param user
//     */
//    private void RegisterWithFacebook(GraphUser user)
//    {
//    	
//    	String email = user.getProperty("email").toString();
//    	 
//    	// checks wheather email given from facebook already on Parses
//    	HashMap<String, String> userParseLogginInfo = ParseConnect.isUserEmailOnParse(email);
//    	if (!userParseLogginInfo.isEmpty())
//    	{
//    	
//    	///// TO DO : Update Parse user with Facebook info (fb_id,access_token...)
//    		///// and update AtlasAndroidUser object from Parse server (connect with Parse account)
//    		//// DONT CHANGE USER NAME ON PARSE ACCORDING TO Facebook !!!
//    		AtlasAndroidUser.setAccessToken( Session.getActiveSession().getAccessToken());
//    		AtlasAndroidUser.setFb_id(user.getId());
//    		AtlasAndroidUser.setEmail(email);
//    		AtlasAndroidUser.setUserName(userParseLogginInfo.get("userName").toString());
//    		AtlasAndroidUser.setAtlasPassword(userParseLogginInfo.get("passwordCopy").toString());
//    	
//    		
//    		HashMap<String, String> facebookInfo = new HashMap<String, String>();
//    		facebookInfo.put("fb_id", user.getId());
//    		facebookInfo.put("access_token", Session.getActiveSession().getAccessToken());
//    		// UPDATING USERS' PARSE INFO WITH FACEBOOK ID ACCESS TOKEN
//    		ParseConnect.updateAtlasUserInfoOnServer(facebookInfo);
//    		ParseConnect.setCurrentAtlasAndroidUserFromServer();
//    	
//    	}
//    	else 
//    	{
//    	/// User connected to Atlas with  Facebook only....
//    		AtlasAndroidUser.setAccessToken( Session.getActiveSession().getAccessToken());
//    	
//    		/// generate a password for Parse account
//    		AtlasAndroidUser.setAtlasPassword(user.getUsername()+user.getId());
//    		AtlasAndroidUser.setEmail(user.getProperty("email").toString());
//    		AtlasAndroidUser.setFb_id(user.getId());
//    		/// username used on facebook will be used in Parse also
//    		AtlasAndroidUser.setUserName(user.getUsername());
//    		AtlasAndroidUser.setUserNameDisplay(user.getName());
//    		AtlasAndroidUser.setLoggedIn(true);
//    		
//    		// 	CREATE A NEW PARSE USER FROM AtlasAndroidUser Object (updated by Faecbook info)
//    		/// and get the user ObjectId if register succeed ...
//    		String parseUserID =ParseConnect.createNewAtlasUser();
//			
//    		AtlasAndroidUser.setUserSignonServer(!parseUserID.equals(""));
//  
//    		AtlasAndroidUser.setParseUserID(parseUserID);
//    		
//    		
//    	}
//    	
//    	// update users' local  SharedPreferences profile info
//		applicationController.setAtlasUserLocalInfo();
//	}
    
//    
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode,
//            Intent data) {
//    	super.onActivityResult(requestCode, resultCode, data);
//        if (data != null) {
//        	Bundle extras = data.getExtras();
//		       if (extras != null) {
//		    	    boolean facebookConnected = extras.getBoolean("fb_connect_success");
//		    	    String facebookMessage = extras.getString("fb_connect_message");
//		          
//		    	      
//		    	    if (facebookConnected)
//		    	    {
//		    	    	Intent intent = new Intent(getBaseContext(), Today.class);
//		            	intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
////		            	//intent.putExtra("EXTRA_SESSION_ID", sessionId);
//		            	startActivity(intent);
//		    	    }else
//		    	    {
//		    	    	
//		    	    	if (facebookMessage!=null)
//		    	    		Toast.makeText(getApplicationContext(), facebookMessage,
//		    	    				Toast.LENGTH_LONG).show();
//		    	    	
//		    	    	Intent intent = new Intent(getBaseContext(), Welcome.class);
//		            	intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
////		            	//intent.putExtra("EXTRA_SESSION_ID", sessionId);
//		            	startActivity(intent);
//		    	    }
//		       }
//        }
//    }
    
    
}