package com.atlasapp.section_settings;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;


import com.atlasapp.atlas_database.AtlasServerConnect;
import com.atlasapp.atlas_database.DatabaseConstants;
import com.atlasapp.common.ATLAnimationUtils;
import com.atlasapp.common.ATLDragAndDropView;
import com.atlasapp.atlas_database.DatabaseConstants.SIGN_NEW_USERS_CALLER;
import com.atlasapp.atlas_database.UserDelagateInterface;
import com.atlasapp.common.AtlasAndroidUser;
import com.atlasapp.common.OnHeadlineSelectedListener;
import com.atlasapp.common.SlideOutActivity;
import com.atlasapp.facebook.FacebookUtilities;
import com.atlasapp.facebook.ProfilePictureView;
import com.atlasapp.section_alerts.ATLAlertFragment_2;
import com.atlasapp.section_alerts.AlertFragment;
import com.atlasapp.model.ATLContactModel;
import com.atlasapp.section_appentry.AtlasApplication;
import com.atlasapp.section_appentry.R;
import com.atlasapp.section_appentry.Register.VALID_REGISTRATION_MESSAGE;
import com.atlasapp.section_calendar.CalendarDayView;
import com.atlasapp.section_contacts.ATLContactListActivity;
import com.atlasapp.section_notes.ATLNoteListActivity;
import com.atlasapp.section_slidemenu.ATLSlideMenuFragment;
import com.atlasapp.section_tasks.Tasks;
import com.atlasapp.section_today.Today;

import com.ericharlow.DragNDrop.DragListener;
import com.ericharlow.DragNDrop.DropListener;
import com.facebook.GraphUser;
//import com.facebook.ProfilePictureView;
import com.facebook.Request;
import com.facebook.Session;
import com.facebook.Response;
// import com.facebook.android.R;
//import com.facebook.android.R;





import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class EditProfile extends  FragmentActivity implements OnHeadlineSelectedListener , UserDelagateInterface{
	public static enum VALID_REGISTRATION_MESSAGE { INVALID_PASSWORD,  INVALID_EMAIL, INVALID_EMAIL_PASSWORD, VALIDATE}

	
	private  byte[] profilePic;  
	 
	
	private final int PASSWORD_LENGTH = 2; 
//	private ProfilePictureView profilePictureView;
	public static enum SERVER_MESSAGE {
		   FAIL, LOGGED_OUT, EMAIL_OR_USER_NAME_INVALID, SUCCESS, LOGGED_IN
		 }
	private int picTypePicker = 3;// cancel default
	
	private static final int CAMERA_REQUEST = 1337; 
    private static ImageView imageView;
	private AtlasServerConnect parseConnect;


	private  String encodedImage="";
 

	private AtlasApplication applicationController;


	private static EditText emailEdit;


	private static ImageButton btnCamera;  


	private static  ImageButton btnSave;


	private static ImageButton btnSettings;


	private  Bitmap currentPhoto = null;


	private String email;


	private String passwordVerify;


	private String password;


	private String userNameDisplay;

//	private static Bitmap currentPhotoURI = null;
	private static ImageView myImage;
    static final int INTENT_REQUEST_CODE_BROWSE_PICTURE = 1;
	public static final String PREFS_NAME = "AtlasProfileLogin";
	
	LayoutInflater mInflater;
	Context mContext;
	ViewGroup mLayout;


	public ImageButton sortBtn;
	public ImageButton alertBtn;
	public ATLDragAndDropView editProfileHolderView;
	public ViewGroup alertHolderView;
	public ViewGroup selectCalendarHolderView;
	public View editProfileViewCover;
	public int topMenuBarHeight;
	public ATLSlideMenuFragment slideMenuFragment;
	public ATLAlertFragment_2 alertFragment;
	public boolean isFragmentShowing;


	private SelectProfilePicFragment selectProfilePicFragment;
	
   @Override 
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       
       mInflater = LayoutInflater.from(this);
	   mLayout = (ViewGroup) mInflater.inflate(R.layout.profile_edit, null);// 2013-03-17 TAN: change xml file
	   setContentView(mLayout);
       
//       setContentView(R.layout.edit_profile); 
       parseConnect =   AtlasServerConnect.getSingletonObject(this);

	   applicationController = (AtlasApplication)getApplicationContext();
   	//	applicationController.storePicture("atlas_profile",FacebookUtilities.getUserFacebookProfilePic());

   		ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.selection_profile_pic);
   		final ScrollView registerScroll = (ScrollView)findViewById(R.id.editProfileScroll);
		registerScroll.setScrollContainer(false);
       
       
//       
//       
//       
//       else
//    	   profilePictureView.setVisibility(View.INVISIBLE);
		  
		   
       Bundle extras = getIntent().getExtras();
       if (extras != null) {
           this.picTypePicker = extras.getInt("picture_from");
           if (extras.getString("profile_path")!=null && !extras.getString("profile_path").equals(""))
        	{
        	   encodedImage = extras.getString("profile_path");
        	   
        	   profilePic = Base64.decode(encodedImage, Base64.DEFAULT);
    		   currentPhoto = BitmapFactory.decodeByteArray(profilePic, 0, profilePic.length);
      			myImage = (ImageView) findViewById(R.id.camerapreview);

    		   myImage.setImageBitmap(currentPhoto);
        	  
        	}
       }
       
       
       if (!setProfilePic())  
       
       
       if (!AtlasAndroidUser.getfbID().equals("")  )
       {
    	   // Find the user's profile picture custom view
    	  
    	   profilePictureView.setCropped(true);
    	   profilePictureView.setUserId(AtlasAndroidUser.getfbID());
    	   ImageButton cameraBtn = (ImageButton)findViewById(R.id.editProfileCameraBtn);
    	   cameraBtn.setAlpha(0);
    	//   cameraBtn.setVisibility(View.INVISIBLE);
    	  // Bitmap facebookProf = profilePictureView.getDrawingCache();
    	//	applicationController.storePicture("image",profilePictureView.getProfilePicBitmap());
    	//	myImage.setImageBitmap(profilePictureView.getProfilePicBitmap());
    	//	profilePictureView.setVisibility(View.INVISIBLE);
       }
//       
       
       if (picTypePicker==1)
    	   chooseFromLibrary();
       else
    	   if (picTypePicker==2)
    		   takeAPhoto();

    	    emailEdit = (EditText)findViewById(R.id.emailEdit);
    	   EditText userNameDisplayEdit = (EditText)findViewById(R.id.profile_edit_first_name);
    	   EditText userLastNameDisplayEdit = (EditText)findViewById(R.id.userNameDescEdit);
    	   userLastNameDisplayEdit.setHint("Last name");// Tan: hardcoded to display
    	   userNameDisplayEdit.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {

					registerScroll.setScrollContainer(true);
				}
			});
    	   //ImageView profPic = (ImageView)findViewById(R.id.camerapreview);
    	//   String previouslyEncodedImage = AtlasAndroidUser.getPicture();
    	  
    	   
    	   
//    	   if(previouslyEncodedImage!=null && !previouslyEncodedImage.equalsIgnoreCase("") )
//    	   {
//    		   profilePic = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
//    		   Bitmap bitmap = BitmapFactory.decodeByteArray(profilePic, 0, profilePic.length);
//    		   profPic.setImageBitmap(bitmap);
//    	   }
    	    
    	   String emailHint  =(AtlasAndroidUser.getEmail() == null || AtlasAndroidUser.getEmail().equals(""))?  (String)emailEdit.getHint() : AtlasAndroidUser.getEmail();
    	   String userNameDisplayHint  =(AtlasAndroidUser.getUserNameDisplay() == null||  AtlasAndroidUser.getUserNameDisplay().equals(""))?  (String)userNameDisplayEdit.getHint() : AtlasAndroidUser.getUserNameDisplay();
    	   
    	   emailEdit.setText(emailHint);
    	   userNameDisplayEdit.setText(userNameDisplayHint);
   
//       2013-02-23 TAN: comment out to implement swipe left/right #START   	   
//       btnSettings = (ImageButton)findViewById(R.id.backToSettingBtn);
//      
//       btnSettings.setOnClickListener(
//				new View.OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						Intent intentSetting = new Intent(getBaseContext(), Settings.class);
//	                	intentSetting.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
////                	//intent.putExtra("EXTRA_SESSION_ID", sessionId);
//                	startActivity(intentSetting);
//					}
//				}); 
//        2013-02-23 TAN: comment out to implement swipe left/right #END
    	sortBtn = (ImageButton) findViewById(R.id.sortBtn);
   		alertBtn = (ImageButton) findViewById(R.id.alertBtn);
   		//==========================================================
   		// 2013-02-21 TAN: new slide out # START
   		//==========================================================
   		editProfileHolderView = (ATLDragAndDropView)mLayout.findViewById(R.id.editProfileContent);
   		alertHolderView = (ViewGroup)mLayout.findViewById(R.id.calendar_day_view_alert_fragment);
   		selectCalendarHolderView = (ViewGroup)mLayout.findViewById(R.id.calendar_day_view_cal_selects_fragment);
   		editProfileViewCover = (View)mLayout.findViewById(R.id.calendar_day_view_cover_view);
   		editProfileViewCover.setOnTouchListener(editProfileHolderView);
   	    // top menu bar height
   	    topMenuBarHeight = (int) Math.ceil(50 * this.getResources().getDisplayMetrics().density);
   	    
   		 if (editProfileHolderView instanceof ATLDragAndDropView) {
   			 ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
   			 editProfileHolderView.setDropListener(mDropListener);
   			 editProfileHolderView.setDragListener(mDragListener);
   	     }
   		 
   		slideMenuFragment = new ATLSlideMenuFragment();
   			getSupportFragmentManager().beginTransaction()
   			.add(R.id.calendar_day_view_cal_selects_fragment, slideMenuFragment).commit();
   		alertFragment = ATLAlertFragment_2.getInstance();
   			getSupportFragmentManager().beginTransaction()
   			.add(R.id.calendar_day_view_alert_fragment, alertFragment).commit();
   		//==========================================================
   		// 2013-02-21 TAN: new slide out # END
   		//==========================================================    
		sortBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!isFragmentShowing){
					alertHolderView.setVisibility(View.GONE);
					selectCalendarHolderView.setVisibility(View.VISIBLE);
					editProfileViewCover.setVisibility(View.VISIBLE);
					editProfileHolderView.setX(editProfileHolderView.LEFTRIGHT_WINDOW_WIDTH);
					ATLDragAndDropView.topMenuBarHeight = editProfileHolderView.getHeight();
					ATLDragAndDropView.isAtRight = true;
					ATLDragAndDropView.isAtLeft = false;
					editProfileHolderView.startAnimation(ATLAnimationUtils.mainFragmentMoveRight(editProfileHolderView.LEFTRIGHT_WINDOW_WIDTH));
					isFragmentShowing = true;
				}
				else{
					editProfileViewCover.setVisibility(View.GONE);
					editProfileHolderView.setX(0);
					ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = false;
					editProfileHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromRight(editProfileHolderView.LEFTRIGHT_WINDOW_WIDTH));
					isFragmentShowing = false;
					onResume();
				}
				// 2013-02-03 TAN: new slide out # END
			}
		});
		
		alertBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!isFragmentShowing){
					alertHolderView.setVisibility(View.VISIBLE);
					selectCalendarHolderView.setVisibility(View.GONE);
					editProfileViewCover.setVisibility(View.VISIBLE);
					ATLDragAndDropView.topMenuBarHeight = editProfileHolderView.getHeight();
					editProfileHolderView.setX(-editProfileHolderView.LEFTRIGHT_WINDOW_WIDTH);
					editProfileHolderView.startAnimation(ATLAnimationUtils.mainFragmentMoveLeft(editProfileHolderView.LEFTRIGHT_WINDOW_WIDTH));
					alertFragment.onResume();// Reload view data
					isFragmentShowing = true;
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = true;
				}
				else{
					ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
					editProfileViewCover.setVisibility(View.GONE);
					editProfileHolderView.setX(0);
					editProfileHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromLeft());
					isFragmentShowing = false;
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = false;
				}
				
			}
		});   			
       
       btnSave = (ImageButton)findViewById(R.id.editProfileSaveBtn);
       btnSave.setOnClickListener(
    		   new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					editProfile();
					
					
					
				
		
				}
			}
    		   
    		   
    		   );
 
       btnCamera = (ImageButton)findViewById(R.id.editProfileCameraBtn);
       
     
       btnCamera.setOnClickListener( new View.OnClickListener() {
		  
		@Override
		public void onClick(View v) {
			
			
			int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
				SlideOutActivity.prepare(EditProfile.this, R.id.editProfileContent, width);
				
				Intent intent = new Intent(EditProfile.this,
						GetProfilePic.class);
				intent.putExtra("from", "edit_profile");
				if (!encodedImage.equals(""))
					
					intent.putExtra("profile_path", encodedImage);
				startActivity(intent);
				
				overridePendingTransition(0, 0);

			
		}
	});
       
       selectProfilePicFragment = new SelectProfilePicFragment();
       
       
       
//       ((RadioButton) findViewById(R.id.btnSettings)).setVisibility(View.VISIBLE);
//       RadioButton settingsBtn = (RadioButton)findViewById(R.id.btnSettings);
//       
//       settingsBtn.setChecked(true);
//       
//       settingsBtn.setTextColor(Color.BLACK);
//       setMainMenuListener();
   }
   
   @Override
   public void onResume(){
	   super.onResume();
   }
   
   /**
    * Setting the profile picture on the camera view image 
    * from the current set photo URI and use the stored profile picture
    * from /Android/data/com.atlastpowered/files/Pictures/profile_pic.png
    * as a default (if exists)
    * 
    * 
    * return true if an image was set
    *
    */
   private boolean setProfilePic()
   {
	   Bitmap currentBitmap ;
	   Bitmap storedBitmap = null;
	   File imgFile = applicationController.PROFILE_PIC_PATH;
		if(imgFile.exists())
   		
			storedBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
   		
		currentBitmap  = (currentPhoto!=null)? currentPhoto : storedBitmap;
		  
	
////	   File imgFile = (currentPhotoURI!=null)? new File(currentPhotoURI.getPath()):this.applicationController.PROFILE_PIC_PATH;
//	 
//   	
//   		if(!imgFile.exists())
//   		imgFile = applicationController.PROFILE_PIC_PATH;
//   	
//  
//   		if(imgFile.exists())
//   		{
//   			Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		
	//	currentPhoto = currentBitmap;
		
		
		
		if (currentBitmap!=null){

   			myImage = (ImageView) findViewById(R.id.camerapreview);
   			myImage.setImageBitmap(currentBitmap);
		}
   	    

   		return currentBitmap!=null;
   		
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
   
   
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
       
	 //  String  encodedImage="";
	   switch (requestCode) {
		case INTENT_REQUEST_CODE_BROWSE_PICTURE:
			if( resultCode == Activity.RESULT_OK ){

				Uri currentPhotoURI = data.getData();
				if (currentPhotoURI != null) {
				try {
					 Bundle params = new Bundle();
					  params.putByteArray("photo",
							  FacebookUtilities.scaleImage(getApplicationContext(), currentPhotoURI));
					  
					  profilePic = params.getByteArray("photo");
//					  ImageView profPic = (ImageView)findViewById(R.id.camerapreview);
					 currentPhoto = BitmapFactory.decodeByteArray(profilePic, 0, profilePic.length);
//					  profPic.setImageBitmap(bitmap);
//					   encodedImage = Base64.encodeToString(profilePic, Base64.DEFAULT);
//					  
						//applicationController.storePicture("image", currentPhoto);  
					 setProfilePic();
					
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
				
				//finish();
				 Bitmap photo = (Bitmap) data.getExtras().get("data"); 
		         //  imageView.setImageBitmap(photo);
				 
				 ImageView profPic = (ImageView)findViewById(R.id.camerapreview);
				 profPic.setImageBitmap(photo);
				 profPic.setScaleType(ScaleType.FIT_XY);
//		            
		           ByteArrayOutputStream baos = new ByteArrayOutputStream();
		           photo.compress(Bitmap.CompressFormat.PNG, 100, baos);   
		           byte[] b = baos.toByteArray(); 

		            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
		            
			}
			break;
			
	
		}
	   
//	   if ( !encodedImage.equals("")){
//	   
//		   SharedPreferences sp=getSharedPreferences(PREFS_NAME, 0);
//		   SharedPreferences.Editor Ed=sp.edit();
//     
//		   Ed.putString("profile_pic",encodedImage);
//		   Ed.commit();
//	   }
//	   
	   

   } 
   
   
   protected void editProfile() {
	
	   
	   boolean success = false;
	   EditText emailEdit = (EditText)findViewById(R.id.emailEdit);
	   EditText userNameDisplayEdit = (EditText)findViewById(R.id.userNameDescEdit);
	   
	   
	   EditText passwordVerifyEdit = (EditText)findViewById(R.id.passwordVerify);
	   EditText passwordEdit = (EditText)findViewById(R.id.editProfilePassword);
	   
	   
	    email = emailEdit.getText().toString();
	    passwordVerify = passwordVerifyEdit.getText().toString();
	    password = passwordEdit.getText().toString();
	    userNameDisplay = userNameDisplayEdit.getText().toString();
	   
	   if (!email.equals("") || !userNameDisplay.equals("")|| profilePic!=null ||
			   !passwordVerify.equals("") || !password.equals(""))
	   { 
		   //User can only Edit Profile while sign on...
		   
		   DatabaseConstants.SERVER_MESSAGE serverMessageBack;
		
		  parseConnect.logUserToServer(AtlasAndroidUser.getEmail(), AtlasAndroidUser.getAtlasPassword(),this);
	//	  parseConnect.updateFacebookUseruser();
		
		
			
		  }
	
   }
   @SuppressWarnings("deprecation")
	private void alertUser(String messageTitle, String message)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(
				EditProfile.this).create();

		// Setting Dialog Title
		alertDialog.setTitle(messageTitle);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		// Setting Icon to Dialog
	//	alertDialog.setIcon(R.drawable.tick);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				
//                   
					
				}
		});

		// Showing Alert Message
		alertDialog.show();
	}
   private VALID_REGISTRATION_MESSAGE userInputValid(String passVerify, String pass, String email) {
		
	
		boolean passwordValid = (pass.equals(passVerify) && pass.length()>=PASSWORD_LENGTH)||(pass.equals("")&&passVerify.equals("")) ;
		boolean emailVaid = isValidEmail(email) || email.equals("");
		
		
		
		return (!passwordValid && !emailVaid)? VALID_REGISTRATION_MESSAGE.INVALID_EMAIL_PASSWORD: 
			   (!passwordValid)? VALID_REGISTRATION_MESSAGE.INVALID_PASSWORD:
			   (!emailVaid)? VALID_REGISTRATION_MESSAGE.INVALID_EMAIL:
			   VALID_REGISTRATION_MESSAGE.VALIDATE ;
	}
	
	
	
	
	public final static boolean isValidEmail(CharSequence target) {
	    if (target == null) {
	        return false;
	    } else {
	        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	    }
	}




private void setMainMenuListener() {
		
		
		  ((RadioGroup) findViewById(R.id.main_btns_group)).setVisibility(View.VISIBLE);
		
			RadioGroup mainBtns = (RadioGroup)findViewById(R.id.main_btns_group);
			 
			
		//	holder.rdgCategory = (RadioGroup)row.findViewById(R.id.radiogroup);

	        RadioGroup.OnCheckedChangeListener rdGrpCheckedListener = new RadioGroup.OnCheckedChangeListener(){

	         @Override
	         public void onCheckedChanged(RadioGroup group, int checkedId) {
	        	 
	        	 
	        	 //ViewFlipper vf = (ViewFlipper)findViewById(R.id.content_vf);
	        	// ImageView mainTitle = (ImageView)findViewById(R.id.mainTitleLabel);
	        	// 	mainTitle.setVisibility(0);
	        	 	 
	             // TODO Auto-generated method stub
//	               setCategoryinList(position, checkedId);
	                switch (checkedId){ 
	                case R.id.btnCal:
	                	Intent intentCalendar = new Intent(getBaseContext(), CalendarDayView.class);
	                	intentCalendar.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	                	//intent.putExtra("EXTRA_SESSION_ID", sessionId);
	                	startActivity(intentCalendar);
	                   
	                    break;
	                case R.id.btnNotes:
	                	Intent intentNotes = new Intent(getBaseContext(), ATLNoteListActivity.class);
	                	intentNotes.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//	                	//intent.putExtra("EXTRA_SESSION_ID", sessionId);
	                	startActivity(intentNotes);
	                	
	                    break;
	                case R.id.btnSettings:	
	                	Intent intentSetting = new Intent(getBaseContext(), Settings.class);
	                	intentSetting.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                	//intent.putExtra("EXTRA_SESSION_ID", sessionId);
                	startActivity(intentSetting);
	                  
	                break;
	            case R.id.btnToday:
	            	Intent intent = new Intent(getBaseContext(), ATLContactListActivity.class);
	            	intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
////	        	//intent.putExtra("EXTRA_SESSION_ID", sessionId);
	        	startActivity(intent);
	            		
	            	
	                break;
	            case R.id.btnToDo:
	            	Intent intentTasks = new Intent(getBaseContext(), Tasks.class);
	            	intentTasks.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//	            	//intent.putExtra("EXTRA_SESSION_ID", sessionId);
	            	startActivity(intentTasks);
	            
	                break;
	                default:
	                
	                	
	                    break;
	                }
	            }
	        };

	        mainBtns.setOnCheckedChangeListener(rdGrpCheckedListener);

			
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
public void getFriendEmailOnParse(
		ATLContactModel friendsPropertiesOnParseByEmail, boolean success) {
	// TODO Auto-generated method stub
	
}
@Override
public void registerSuccess(boolean success) {
	// TODO Auto-generated method stub
	
}
@Override
public void signInSuccess(boolean success) {
	  switch (userInputValid(passwordVerify,password,email)) 
		{
			
			case VALIDATE:
				 HashMap<String, Object> editProfileUpdateList = new HashMap<String, Object>();
				 editProfileUpdateList.put("email", email);
				 editProfileUpdateList.put("password_copy", password);
				 editProfileUpdateList.put("first_name", userNameDisplay);
				parseConnect.updateAtlasUserInfoOnServer(editProfileUpdateList,this);  
				 // serverMessageBack = parseConnect.updateSuccess;
					
				
				break;
			case INVALID_EMAIL:
				alertUser("Invalid input", "Please enter a valid email address");
				break;
			case INVALID_EMAIL_PASSWORD:
				alertUser("Invalid input", "Passwords dont match and invalid email address");
				break;
			case INVALID_PASSWORD:
				alertUser("Invalid input", "Passwords must be at least 8 charecter long and match ");
				break;
	  
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
public void getUpateCallBack(boolean serverSuccess) {
	  String message = (serverSuccess)? "Your profile been successfuly updated!": "Failed to update, please check you entered valid information and that you're connected to the net";

	if (serverSuccess)
	{
		   
		//	  ParseConnect.setCurrentAtlasAndroidUserFromServer();
			// Update AtlasAndroidUser Object and Local mobile Shared Preferences file.....
		//	ParseConnect.setAtlasAndroidUserFromServer(userNameDesc, password);
				  if (this.currentPhoto!=null )
					  {  
//					  HashMap<String, String> newprofPic = new HashMap<String, String>();
//					  encodedImage = (encodedImage==null)?  Base64.encodeToString(profilePic, Base64.DEFAULT):encodedImage;
//					  newprofPic.put("profile_pic", encodedImage);
//					  applicationController.setAtlasSharedPreferences(newprofPic);
					  	parseConnect.saveProfileImage(profilePic,this);
					  	applicationController.storePicture("image", currentPhoto);
					  
					  }
				  applicationController.createLocalProfileFromServerProfile();
				//  success = (parseConnect.parseUser!=null);
			

				  
				  
				  
				  alertUser("", message);

			 
			  }else
				  alertUser("Uh oh!", "please check you entered valid information and that you're connected to the net");
				  
	
}
@Override
public void getSignNonAtlasUsersCallBack(SIGN_NEW_USERS_CALLER caller,
		boolean success,
		HashMap<String, ATLContactModel> newCurrentNonAtlasUserToSign) {
	// TODO Auto-generated method stub
	
}
   
   
	//===============================================================================
	// 2013-02-03 TAN: Implement Swipe left and right # START
	// ===============================================================================
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			editProfileViewCover.setVisibility(View.GONE);
			editProfileHolderView.setX(0);
			editProfileHolderView.setVisibility(View.VISIBLE);
			ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
			
			if(ATLDragAndDropView.isAtRight){
				editProfileHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromRight(editProfileHolderView.LEFTRIGHT_WINDOW_WIDTH));
				ATLDragAndDropView.isAtRight = false;
				ATLDragAndDropView.isAtLeft = false;
				isFragmentShowing = false;
				onResume();
				return true;
			}
			else if(ATLDragAndDropView.isAtLeft){
				editProfileHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromLeft());
				ATLDragAndDropView.isAtRight = false;
				ATLDragAndDropView.isAtLeft = false;
				isFragmentShowing = false;
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
		
	}

	private DragListener mDragListener =
		new DragListener() {
			public void onDrag(int x, int y, ListView listView) {
				// TODO Auto-generated method stub
				
				if(x <= 0){
					//isSwipeLeft = true;
					alertHolderView.setVisibility(View.VISIBLE);
					selectCalendarHolderView.setVisibility(View.GONE);
				}
				else{
					//isSwipeLeft = false;
					alertHolderView.setVisibility(View.GONE);
					selectCalendarHolderView.setVisibility(View.VISIBLE);
				}
				
			}
	
			public void onStartDrag(View itemView) {
				editProfileHolderView.setVisibility(View.GONE);
			}
	
			public void onStopDrag(View itemView) {
				editProfileHolderView.setVisibility(View.VISIBLE);
				float x = itemView.getX();
				float y = itemView.getY();
				Log.v("CalendarDayView", "onStopDrag  =====   "+ x +" ===  "+y);
			}
			
	};	

	private DropListener mDropListener = 
		new DropListener() {
     public void onDrop(int from, int to) {
     	if(editProfileHolderView.getVisibility() != View.VISIBLE){
     		editProfileHolderView.setVisibility(View.VISIBLE);
     	}
     	
     	if(ATLDragAndDropView.isTouched){
     		ATLDragAndDropView.isTouched = false;
     		editProfileViewCover.setVisibility(View.GONE);
 			editProfileHolderView.setX(0);
 			editProfileHolderView.setVisibility(View.VISIBLE);
 			ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
 			
 			if(ATLDragAndDropView.isAtRight){
 				editProfileHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromRight(editProfileHolderView.LEFTRIGHT_WINDOW_WIDTH));
 				ATLDragAndDropView.isAtRight = false;
 				ATLDragAndDropView.isAtLeft = false;
 				isFragmentShowing = false;
 				onResume();
 			}
 			else if(ATLDragAndDropView.isAtLeft){
 				editProfileHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromLeft());
 				ATLDragAndDropView.isAtRight = false;
 				ATLDragAndDropView.isAtLeft = false;
 				isFragmentShowing = false;
					onResume();
 			}
     	}
     	else if(ATLDragAndDropView.isAtLeft){
     		if(from > to){
     		
     		}
     		else if(to > from && to > 300){
     			ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
					editProfileViewCover.setVisibility(View.GONE);
					editProfileHolderView.setX(0);
					editProfileHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromLeftAt(to, editProfileHolderView.LEFTRIGHT_WINDOW_WIDTH));
					isFragmentShowing = false;
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = false;
					onResume();
 	        }
			}else if(ATLDragAndDropView.isAtRight){
				if(from < to){
	        		
     		}
     		else if(from > to && to < 450){
     			editProfileViewCover.setVisibility(View.GONE);
					editProfileHolderView.setX(0);
					ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = false;
					editProfileHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromRightAt(to 
							- (from - editProfileHolderView.LEFTRIGHT_WINDOW_WIDTH)));
					isFragmentShowing = false;
					onResume();
 	        }
			}else{
				if(from > to && to < 360){
	        		
	        		alertHolderView.setVisibility(View.VISIBLE);
					selectCalendarHolderView.setVisibility(View.GONE);
					editProfileViewCover.setVisibility(View.VISIBLE);
					editProfileHolderView.setX(-editProfileHolderView.LEFTRIGHT_WINDOW_WIDTH);
					editProfileHolderView.startAnimation(ATLAnimationUtils.mainFragmentMoveLeftAt(from - to, editProfileHolderView.LEFTRIGHT_WINDOW_WIDTH));
					ATLDragAndDropView.topMenuBarHeight = editProfileHolderView.getHeight();
					alertFragment.onResume();// Reload view data
					isFragmentShowing = true;
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = true;
					//onResume();
					
	        	}
	        	else if(to > from && to > 360){
	        		
	        		alertHolderView.setVisibility(View.GONE);
					selectCalendarHolderView.setVisibility(View.VISIBLE);
					editProfileViewCover.setVisibility(View.VISIBLE);
					editProfileHolderView.setX(editProfileHolderView.LEFTRIGHT_WINDOW_WIDTH);
					editProfileHolderView.startAnimation(ATLAnimationUtils.mainFragmentMoveRightAt(to - from, editProfileHolderView.LEFTRIGHT_WINDOW_WIDTH));
					ATLDragAndDropView.topMenuBarHeight = editProfileHolderView.getHeight();
					isFragmentShowing = true;
					ATLDragAndDropView.isAtRight = true;
					ATLDragAndDropView.isAtLeft = false;
					//onResume();
	        	}
			}
     	
     }
	};
	// ===============================================================================
	// 2013-02-03 TAN: Implement Swipe left and right # END
	// ===============================================================================	
   
   
}
