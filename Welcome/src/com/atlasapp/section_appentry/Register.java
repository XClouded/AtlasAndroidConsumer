package com.atlasapp.section_appentry;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;




import com.atlasapp.facebook.ProfilePictureView;

import com.atlasapp.atlas_database.AtlasServerConnect;
import com.atlasapp.atlas_database.DatabaseConstants.SIGN_NEW_USERS_CALLER;
import com.atlasapp.atlas_database.ParseDBCallBackInterface;
import com.atlasapp.atlas_database.UserDelagateInterface;
import com.atlasapp.common.AtlasAndroidUser;
import com.atlasapp.model.ATLContactModel;
import com.atlasapp.section_calendar.CalendarDayView;
import com.atlasapp.section_contacts.ATLContactListActivity;
import com.atlasapp.section_settings.Settings;
import com.atlasapp.section_today.Today;
import com.parse.ParseObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

public class Register extends Activity implements UserDelagateInterface{

	private AtlasApplication applicationController;
	public static enum VALID_REGISTRATION_MESSAGE { INVALID_PASSWORD,  INVALID_EMAIL, INVALID_EMAIL_PASSWORD, VALIDATE, FAIL, LOGGED_IN, LOGGED_OUT}
	private ProfilePictureView profilePictureView;
	private int picTypePicker = 3;// cancel default
	private static final int CAMERA_REQUEST = 1888; 
    private ImageView imageView;
    static final int INTENT_REQUEST_CODE_BROWSE_PICTURE = 1;
	
	public static final String PREFS_NAME = "AtlasProfileLogin";
//	private EditText userName; 
//	private EditText displayname;
//	private EditText email;
//	private EditText password;
	private final int PASSWORD_LENGTH = 2; 
	private EditText passwordVerify;
	private static byte[] profileBytesPic;
	//private String previouslyEncodedImage;
	private String name;
	private String pass;
	private String passVerify;
	private String email;
	private Intent intent;
	private AtlasServerConnect parseConnect;
	private static byte[] profilePic = null;
	private static ImageButton registerBtn;

	
	public static void setProfilePic(byte[] profPic){profilePic =(profPic!=null)?profPic:profilePic; }
	
	@Override 
	public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		applicationController = (AtlasApplication)getApplicationContext();
        parseConnect =   AtlasServerConnect.getSingletonObject(this);

		 Bundle extras = getIntent().getExtras();
	        if (extras != null) {
	        	name = extras.getString("name");
	        	pass = extras.getString("pass");
	        	passVerify = extras.getString("passVerify");
	        	email = extras.getString("email");
//	        	if (extras.getByteArray("picture")!=null)
//	        		profilePic = extras.getByteArray("picture");
	        	
	        }
		if (AtlasAndroidUser.doesHaveLocalProfile())
		{
			
			/// user already had local profile , message for a possible logout....
			Intent intent = new Intent(getBaseContext(), Welcome.class);
			Toast.makeText(getApplicationContext(), "You already got an exsiting account with Atlas! try loging in ",
					Toast.LENGTH_LONG).show();
         	intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

         	startActivity(intent);
		}else
		{
			setContentView(R.layout.register);
			
			ScrollView registerScroll = (ScrollView)findViewById(R.id.registerScroll);
			registerScroll.setVisibility(View.INVISIBLE);
			
			// setting the picture picker type (library/snap)
//			ImageButton btnCamera = (ImageButton)findViewById(R.id.editProfileCameraBtn);
//		       btnCamera.setOnClickListener( new View.OnClickListener() {
//				
//		    	   
//		    	   
//		    	   
//		    	   
//				@Override
//				public void onClick(View v) {
//					
//					
//					int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
//						SlideOutActivity.prepare(Register.this, R.id.editProfileContent, width);
//						startActivity(new Intent(Register.this,
//								GetProfilePic.class));
//						overridePendingTransition(0, 0);
//
//					
//				}
//			});
//			
//		
//			 
//			 
//			 
//			 
//			 
//			 profilePictureSettings();
			
			
			
	   		ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.selection_profile_pic);
	   		if (!AtlasAndroidUser.getfbID().equals("")  )
	        {
	     	   // Find the user's profile picture custom view
	     	  
	     	   profilePictureView.setCropped(true);
	     	   profilePictureView.setUserId(AtlasAndroidUser.getfbID());
	     	   ImageButton cameraBtn = (ImageButton)findViewById(R.id.editProfileCameraBtn);
	     	   cameraBtn.setAlpha(0);
	        }
			 registerBtn = (ImageButton)findViewById(R.id.registerBtn);
			registerBtn.setOnClickListener(
					new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							setUserRegistrationInfo();
							
							
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
	 					}
	 					
	 				});
			
			
			
			// Register referenced from a click pressed on Welcome.java..
			setUserRegistrationInfo();
		}
		
		
		   
    
	}
	
	
//	/**
//	 * "Catches" the picture picker type (from library, take a photo)
//	 * and reference to the correct method accordingly
//	 */
//	private void profilePictureSettings() {
//		profilePictureView.setVisibility(View.INVISIBLE);
//	   	
//
//	       Bundle extras = getIntent().getExtras();
//	       if (extras != null) {
//	           this.picTypePicker = extras.getInt("picture_from");
//	       }
//	       if (picTypePicker==1)
//	    	   chooseFromLibrary();
//	       else
//	    	   if (picTypePicker==2)
//	    		   takeAPhoto();
//
//	    	  
//	    	     
//	    	  
//		
//	}
//	 private void chooseFromLibrary()
//	   {
//		   ///// Chose From Libraray option
//	       Intent intent = new Intent();
//				intent.setType("image/*");
//				intent.setAction(Intent.ACTION_GET_CONTENT);
//				intent.addCategory(Intent.CATEGORY_OPENABLE);
//				startActivityForResult(intent,
//						INTENT_REQUEST_CODE_BROWSE_PICTURE);
//	   }
//	   private void takeAPhoto()
//	   {
//		/////// Take A Picture option....
//					 Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
//		             startActivityForResult(cameraIntent, CAMERA_REQUEST); 
//	   }
	
	
	@SuppressWarnings("deprecation")
	private void alertUser(String messageTitle, String message)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(
				Register.this).create();

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
				
					intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); 
					startActivity(intent);
				
				
				}
		});

		// Showing Alert Message
		alertDialog.show();
	} 
	
	
	private void setUserRegistrationInfo() {
		 
		 
//		 passwordVerify = (EditText)findViewById(R.id.registerPasswordVerifiedtxt);
//		 displayname = (EditText)findViewById(R.id.registerNameDisplayEditTxt);
//		 email = (EditText)findViewById(R.id.registerEmailEditTxt);
//		 password = (EditText)findViewById(R.id.registerPasswordtxt);
//		 
//		 
//		 String passVerify = passwordVerify.getText().toString();
//		 String name = displayname.getText().toString();
//		 String emailText = email.getText().toString();
//		 String pass = password.getText().toString();
		 
		 
		 
		  intent = new Intent(getBaseContext(), Welcome.class);
		if (!passVerify.equals("") && !name.equals("") 
				&& !email.equals("") && !pass.equals(""))
		{
			 setContentView(R.layout.loading);
			 findViewById(R.id.mainSpinner1).setVisibility(View.VISIBLE);
			 
			
			 Toast.makeText(getApplicationContext(), "Creating new Atlas profile ...",
		    			Toast.LENGTH_LONG).show();
			//HashMap<String, String> userOnParse = usersEmailNotOnParseALready();
			
			
			parseConnect.getUserEmailOnParse(email,this);
			
//			
//			if (userOnParse==null || userOnParse.isEmpty())
//			{
//			
//			switch (userInputValid()) 
//			{
//				
//				case VALIDATE:
//				
//					String parseUserID ="";
//					updateLocalUserInfo(); 
//					parseUserID = updateOnParse();
//					
//					if (!parseUserID.equals(""))
//					{
//						 if (profilePic!=null )
//						  {
//						//  HashMap<String, String> newprofPic = new HashMap<String, String>();
//						 // newprofPic.put("profile_pic", this.encodedImage);
//						 // applicationController.setAtlasSharedPreferences(newprofPic);
//							 Toast.makeText(getApplicationContext(), "Storing user info on Atlas ...",
//						    			Toast.LENGTH_SHORT).show();
//							 
//							 
//							 
//							 parseConnect.saveProfileImage(profilePic);
//						  	 Bitmap bitmap = BitmapFactory.decodeByteArray(profilePic, 0, profilePic.length);
//						  	applicationController.storePicture("image", bitmap);
//						  
//						  }
//						HashMap<String, Object> registerLocalInfo = new HashMap<String, Object>();
//				
//						registerLocalInfo.put("parseUserID",parseUserID);
//					
//						applicationController.setAtlasSharedPreferences(registerLocalInfo);
//						applicationController.logInOrOutUser(true);
//				//		AtlasAndroidUser.setParseUserID(parseUserID);
//						
//				
//						intent = new Intent(getBaseContext(), CalendarDayView.class);//Tan change to Calendar View
//						intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//						alertUser("Registration complited successfuly", "Welcome");
//
//	         	
//					}
//					else
//					{
//						Toast.makeText(getApplicationContext(), "Registration failed! Either check you have internet connection, or username has already been taken",
//								Toast.LENGTH_LONG).show();
//				
//						intent = new Intent(getBaseContext(), 	Welcome.class);
//						intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//						intent.putExtra("from", "register");
//						/// loggin failed....
//					}
//					break;
//				case INVALID_EMAIL:
//					alertUser("Uh Oh!", "Please enter a valid email address");
//					break; 
//				case INVALID_EMAIL_PASSWORD:
//					alertUser("Uh Oh!", "Passwords dont match and invalid email address");
//					break;
//				case INVALID_PASSWORD:
//					alertUser("Uh Oh!", "Passwords must be at least 8 charecter long and match ");
//					break;
//				default:
//					break;
//			}
//			}else
//			{
//				//// user's email already on Parse
//				intent = new Intent(getBaseContext(), 	Welcome.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//				intent.putExtra("from", "");  
//				intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); 
//				String message =  "User already got an account! Please try to SignIn"
//						;
//				alertUser("Uh Oh!", message);
//
//				
//			//	startActivity(intent);
//			}
			
			
		}else
		{
			intent.putExtra("from", "register");
		
			
			intent.putExtra("pass",pass);
         	intent.putExtra("passVerify", passVerify);
         	intent.putExtra("email",email);
         	intent.putExtra("name", name);
         	if (profilePic!=null)
//         	intent.putExtra("picture", profilePic);
			
			Welcome.setProfilePic(profilePic);

		
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); 
		startActivity(intent);
		}
		
	}
		
	
	private VALID_REGISTRATION_MESSAGE userInputValid() {
		

		boolean passwordValid = pass.equals(passVerify) && pass.length()>=PASSWORD_LENGTH;
		boolean emailVaid = isValidEmail(email);
		
		
		
		return (!passwordValid && !emailVaid)? VALID_REGISTRATION_MESSAGE.INVALID_EMAIL_PASSWORD: (!passwordValid)? VALID_REGISTRATION_MESSAGE.INVALID_PASSWORD: (!emailVaid)? VALID_REGISTRATION_MESSAGE.INVALID_EMAIL:VALID_REGISTRATION_MESSAGE.VALIDATE ;
	}
	
	
	
	
	public final static boolean isValidEmail(CharSequence target) {
	    if (target == null) {
	        return false;
	    } else {
	        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	    }
	}
	
	
	
	/**
	 * Checks whether email got from users' input already on
	 * Parse...
	 * @return true if email found on Parse
	 */
//	private HashMap<String, String> usersEmailNotOnParseALready() {
//		
//		
//		return parseConnect.getUserEmailOnParse(email);
//	}
//	
//	
	
	
	/**
	 * Update user info on SharedPreferences local file and on AtlasAndroidUser class
	 * Object
	 */
	private void updateOnParse() 
	{
		// wait for registerSuccess() call back method
		parseConnect.createNewAtlasUser(this);
		
		
		
	}
	/**
	 * Set users : user name display, email and password in SharedPreferences local
	 * profile and the AtlasAndroidUser object accordingly
	 */
	private void updateLocalUserInfo() {
		
		
		HashMap<String, Object> registerLocalInfo = new HashMap<String, Object>();
		registerLocalInfo.put("first_name",name);
		registerLocalInfo.put("email",email);
		
		registerLocalInfo.put("password_copy",pass);
		registerLocalInfo.put("password",pass);
	
		applicationController.setAtlasSharedPreferences(registerLocalInfo);
		
	
		
	}


	

	@Override
	public void registerSuccess(boolean signUpSuccess) {
		if (signUpSuccess && AtlasAndroidUser.getParseUserID()!=null && !AtlasAndroidUser.getParseUserID().equals(""))
		{
			 if (profilePic!=null )
			  {
			//  HashMap<String, String> newprofPic = new HashMap<String, String>();
			 // newprofPic.put("profile_pic", this.encodedImage);
			 // applicationController.setAtlasSharedPreferences(newprofPic);
				 Toast.makeText(getApplicationContext(), "Storing user info on Atlas ...",
			    			Toast.LENGTH_SHORT).show();
				 
				 
				 
				 parseConnect.saveProfileImage(profilePic,this);
//			  	 Bitmap bitmap = BitmapFactory.decodeByteArray(profilePic, 0, profilePic.length);
//			  	applicationController.storePicture("image", bitmap);
			  
			  }
			HashMap<String, Object> registerLocalInfo = new HashMap<String, Object>();
	
			registerLocalInfo.put("parseUserID",AtlasAndroidUser.getParseUserID());
		
			applicationController.setAtlasSharedPreferences(registerLocalInfo);
			applicationController.logInOrOutUser(true);
	//		AtlasAndroidUser.setParseUserID(parseUserID);
			
	
			intent = new Intent(getBaseContext(), CalendarDayView.class);//Tan change to Calendar View
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			//alertUser("Registration complited successfuly", "Welcome");

			startActivity(intent);
		}
		else
		{
			Toast.makeText(getApplicationContext(), "Registration failed! Either check you have internet connection, or username has already been taken",
					Toast.LENGTH_LONG).show();
	
			intent = new Intent(getBaseContext(), 	Welcome.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			intent.putExtra("from", "register");
			/// loggin failed....
			startActivity(intent);
		}
		
	}

	@Override
	public void getUserEmailOnParseCallBack(
			HashMap<String, String> loginProperties, boolean success) {
		if (loginProperties==null || loginProperties.isEmpty())
		{
		
		switch (userInputValid()) 
		{
			
			case VALIDATE:
			
				updateLocalUserInfo(); 
				
				// call back from registerSuccess() method
				updateOnParse();
				
//				if (!parseUserID.equals(""))
//				{
//					 if (profilePic!=null )
//					  {
//					//  HashMap<String, String> newprofPic = new HashMap<String, String>();
//					 // newprofPic.put("profile_pic", this.encodedImage);
//					 // applicationController.setAtlasSharedPreferences(newprofPic);
//						 Toast.makeText(getApplicationContext(), "Storing user info on Atlas ...",
//					    			Toast.LENGTH_SHORT).show();
//						 
//						 
//						 
//						 parseConnect.saveProfileImage(profilePic);
//					  	 Bitmap bitmap = BitmapFactory.decodeByteArray(profilePic, 0, profilePic.length);
//					  	applicationController.storePicture("image", bitmap);
//					  
//					  }
//					HashMap<String, Object> registerLocalInfo = new HashMap<String, Object>();
//			
//					registerLocalInfo.put("parseUserID",parseUserID);
//				
//					applicationController.setAtlasSharedPreferences(registerLocalInfo);
//					applicationController.logInOrOutUser(true);
//			//		AtlasAndroidUser.setParseUserID(parseUserID);
//					
//			
//					intent = new Intent(getBaseContext(), CalendarDayView.class);//Tan change to Calendar View
//					intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//					alertUser("Registration complited successfuly", "Welcome");
//
//         	
//				}
//				else
//				{
//					Toast.makeText(getApplicationContext(), "Registration failed! Either check you have internet connection, or username has already been taken",
//							Toast.LENGTH_LONG).show();
//			
//					intent = new Intent(getBaseContext(), 	Welcome.class);
//					intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//					intent.putExtra("from", "register");
//					/// loggin failed....
//				}
				break;
			case INVALID_EMAIL:
				alertUser("Uh Oh!", "Please enter a valid email address");
				break; 
			case INVALID_EMAIL_PASSWORD:
				alertUser("Uh Oh!", "Passwords dont match and invalid email address");
				break;
			case INVALID_PASSWORD:
				alertUser("Uh Oh!", "Passwords must be at least 8 charecter long and match ");
				break;
			default:
				break;
		}
		}else
		{
			//// user's email already on Parse
			intent = new Intent(getBaseContext(), 	Welcome.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			intent.putExtra("from", "");  
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); 
			String message =  "User already got an account! Please try to SignIn"
					;
			alertUser("Uh Oh!", message);

			
		}
		startActivity(intent);
		
	}

	@Override
	public void signInSuccess(boolean success) {
		if (success)
		{
			alertUser("Uh Oh!", "Register complete");

		}
		else
		{
			alertUser("Uh Oh!", "Register Failed");

		}
		
	}

	@Override
	public void getFriendEmailOnParse(
			ATLContactModel friendsPropertiesOnParseByEmail, boolean success) {
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
		if (saved && profilePic!=null)
		{
			 Bitmap bitmap = BitmapFactory.decodeByteArray(profilePic, 0, profilePic.length);
			 applicationController.storePicture("image", bitmap);
			  
		}
		
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