package atlasapp.section_settings;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.simonvt.menudrawer.MenuDrawer;

import com.parse.ParseObject;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import atlasapp.common.ATLAnimationUtils;
import atlasapp.common.ATLDragAndDropView;
import atlasapp.common.ATLUser;
import atlasapp.common.OnHeadlineSelectedListener;
import atlasapp.common.SlideOutActivity;
import atlasapp.common.UtilitiesProject;
import atlasapp.database.DatabaseConstants.SIGN_NEW_USERS_CALLER;
import atlasapp.database.AtlasServerConnect;
import atlasapp.database.DatabaseConstants;
import atlasapp.database.EmailAddressCallBackInterface;
import atlasapp.database.EmailAddressRecord;
import atlasapp.database.ParseDBCallBackInterface;
import atlasapp.database.UserDelagateInterface;
import atlasapp.facebook.FacebookUtilities;
import atlasapp.facebook.ProfilePictureView;
import atlasapp.model.ATLContactModel;
import atlasapp.section_alerts.ATLAlertFragment_2;
import atlasapp.section_appentry.ATLWelcome;
import atlasapp.section_appentry.AtlasApplication;
import atlasapp.section_appentry.R;
import atlasapp.section_calendar.CalendarDayView;
import atlasapp.slidemenu.ATLSlideMenu;
import atlasapp.slidemenu.ATLSlideMenuFragment;

public class ATLMyProfile extends FragmentActivity implements OnHeadlineSelectedListener , UserDelagateInterface,ParseDBCallBackInterface,EmailAddressCallBackInterface {

	private  byte[] profilePic;  
	private  String encodedImage="";
	private static final int CAMERA_REQUEST = 1337; 
	private static ImageView imageView;
	private int picTypePicker = 3;// cancel default
	private static ImageButton btnCamera;
	private  Bitmap currentPhoto = null;
	private static ImageView myImage;
	static final int INTENT_REQUEST_CODE_BROWSE_PICTURE = 1;
	private static boolean profilePictureChanged = false;

	private final int PASSWORD_LENGTH = 2; 
	//	private ProfilePictureView profilePictureView;
	public static enum SERVER_MESSAGE {
		FAIL, LOGGED_OUT, EMAIL_OR_USER_NAME_INVALID, SUCCESS, LOGGED_IN
	}



	LayoutInflater mInflater;
	Context mContext;
	ViewGroup mLayout;

	public static final String PREFS_NAME = "AtlasProfileLogin";
	private AtlasServerConnect parseConnect;
	private AtlasApplication applicationController;

	private static  ImageButton btnSave;
	private static ImageButton btnSettings;
	private static EditText emailEdit;
	private static EditText userNameFirstNameEdit;
	private static EditText userNameLastNameEdit;
	private static EditText userPasswordEdit;
	private static EditText userPasswordVerifyEdit;


	public static enum VALID_REGISTRATION_MESSAGE { INVALID_PASSWORD,  INVALID_EMAIL, INVALID_EMAIL_PASSWORD, VALIDATE}

	private static ArrayList<String> emailAddressAdded = new ArrayList<String>();
	private static String passwordVerify="";
	private static String password="";
	private static String userFirstName="";
	private static String userLastName="";


	private ScrollView registerScroll;
	private RelativeLayout linearLayout2;
//	public ImageButton sortBtn;
//	public ImageButton alertBtn;
//	public ATLDragAndDropView editProfileHolderView;
//	public ViewGroup alertHolderView;
	public ViewGroup selectCalendarHolderView;
	public View editProfileViewCover;
	public int topMenuBarHeight;
//	public ATLSlideMenuFragment slideMenuFragment;
//	public ATLAlertFragment_2 alertFragment;
	public boolean isFragmentShowing;
	
	


	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		 currentContext = this;
//		 currentActivity = ATLMyProfile.this;
		mInflater = LayoutInflater.from(this);
		mLayout = (ViewGroup) mInflater.inflate(R.layout.atl_my_profile, null);
//		 getSupportActionBar().setSelectedNavigationItem(4);
//			 mMenuDrawer.setContentView(mLayout);
			 setContentView(mLayout);
		parseConnect =   AtlasServerConnect.getSingletonObject(this);

		applicationController = (AtlasApplication)getApplicationContext();

//		emailAddressAdded = new ArrayList<String>();

		registerScroll = (ScrollView)findViewById(R.id.editProfileScroll);
		registerScroll.setScrollContainer(false);

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


		avatarPictureSettings();

		userInputFieldsSettings();

//		topMenuButtonsSettings();
  
	}
//	private void topMenuButtonsSettings() 
//	{
//		//        2013-02-23 TAN: comment out to implement swipe left/right #END
//		
//		alertBtn = (ImageButton) findViewById(R.id.alertBtn);
//		//==========================================================
//		// 2013-02-21 TAN: new slide out # START
//		//==========================================================
//		editProfileHolderView = (ATLDragAndDropView)mLayout.findViewById(R.id.editProfileContent);
//		alertHolderView = (ViewGroup)mLayout.findViewById(R.id.calendar_day_view_alert_fragment);
//		selectCalendarHolderView = (ViewGroup)mLayout.findViewById(R.id.calendar_day_view_cal_selects_fragment);
//		editProfileViewCover = (View)mLayout.findViewById(R.id.calendar_day_view_cover_view);
//		editProfileViewCover.setOnTouchListener(editProfileHolderView);
//		// top menu bar height
//		topMenuBarHeight = (int) Math.ceil(50 * this.getResources().getDisplayMetrics().density);
//
//		//	   		 if (editProfileHolderView instanceof ATLDragAndDropView) {
//		//	   			 ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
//		//	   			 editProfileHolderView.setDropListener(mDropListener);
//		//	   			 editProfileHolderView.setDragListener(mDragListener);
//		//	   	     }
//
////		slideMenuFragment = new ATLSlideMenuFragment();
////		getSupportFragmentManager().beginTransaction()
////		.add(R.id.calendar_day_view_cal_selects_fragment, slideMenuFragment).commit();
//		alertFragment = ATLAlertFragment_2.getInstance();
//		getSupportFragmentManager().beginTransaction()
//		.add(R.id.calendar_day_view_alert_fragment, alertFragment).commit();
//		//==========================================================
//		// 2013-02-21 TAN: new slide out # END
//		//==========================================================    
////		sortBtn.setOnClickListener(new View.OnClickListener() {
////			@Override
////			public void onClick(View v) {
////				if(!isFragmentShowing){
////					alertHolderView.setVisibility(View.GONE);
////					selectCalendarHolderView.setVisibility(View.VISIBLE);
////					editProfileViewCover.setVisibility(View.VISIBLE);
////					editProfileHolderView.setX(editProfileHolderView.LEFTRIGHT_WINDOW_WIDTH);
////					ATLDragAndDropView.topMenuBarHeight = editProfileHolderView.getHeight();
////					ATLDragAndDropView.isAtRight = true;
////					ATLDragAndDropView.isAtLeft = false;
////					editProfileHolderView.startAnimation(ATLAnimationUtils.mainFragmentMoveRight(editProfileHolderView.LEFTRIGHT_WINDOW_WIDTH));
////					isFragmentShowing = true;
////				}
////				else{
////					editProfileViewCover.setVisibility(View.GONE);
////					editProfileHolderView.setX(0);
////					ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
////					ATLDragAndDropView.isAtRight = false;
////					ATLDragAndDropView.isAtLeft = false;
////					editProfileHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromRight(editProfileHolderView.LEFTRIGHT_WINDOW_WIDTH));
////					isFragmentShowing = false;
////					onResume();
////				}
////				// 2013-02-03 TAN: new slide out # END
////			}
////		});
//
//		alertBtn.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if(!isFragmentShowing){
//					alertHolderView.setVisibility(View.VISIBLE);
//					selectCalendarHolderView.setVisibility(View.GONE);
//					editProfileViewCover.setVisibility(View.VISIBLE);
//					ATLDragAndDropView.topMenuBarHeight = editProfileHolderView.getHeight();
//					editProfileHolderView.setX(-editProfileHolderView.LEFTRIGHT_WINDOW_WIDTH);
//					editProfileHolderView.startAnimation(ATLAnimationUtils.mainFragmentMoveLeft(editProfileHolderView.LEFTRIGHT_WINDOW_WIDTH));
//					alertFragment.onResume();// Reload view data
//					isFragmentShowing = true;
//					ATLDragAndDropView.isAtRight = false;
//					ATLDragAndDropView.isAtLeft = true;
//				}
//				else{
//					ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
//					editProfileViewCover.setVisibility(View.GONE);
//					editProfileHolderView.setX(0);
//					editProfileHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromLeft());
//					isFragmentShowing = false;
//					ATLDragAndDropView.isAtRight = false;
//					ATLDragAndDropView.isAtLeft = false;
//				}
//
//			}
//		});
//
//	}
	
	
	
	
	private void userInputFieldsSettings() 
	{

		/// First Name field
		userNameFirstNameEdit = (EditText)findViewById(R.id.firstNameEditText);
		userNameFirstNameEdit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				registerScroll.setScrollContainer(true);
			}
		});

		String userNameFirstNameHint  =(ATLUser.getUserFirstName() == null||  ATLUser.getUserFirstName().equals(""))?  (String)userNameFirstNameEdit.getHint() : ATLUser.getUserFirstName();
		userNameFirstNameEdit.setHint(userNameFirstNameHint);

		//// Last Name field
		userNameLastNameEdit = (EditText)findViewById(R.id.lastNameEditText);
		userNameLastNameEdit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				registerScroll.setScrollContainer(true);
			}
		});

		String userNameLastNameHint  =(ATLUser.getUserLastName() == null||  ATLUser.getUserLastName().equals(""))?  (String)userNameLastNameEdit.getHint() : ATLUser.getUserLastName();
		userNameLastNameEdit.setHint(userNameLastNameHint);

		/// Password field
		userPasswordEdit = (EditText)findViewById(R.id.atlPasswordEditText);
		userPasswordEdit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				registerScroll.setScrollContainer(true);
			}
		});

		/// Password Verify field
		userPasswordVerifyEdit = (EditText)findViewById(R.id.atlPasswordVerifyEditText);
		userPasswordVerifyEdit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				registerScroll.setScrollContainer(true);
			}
		});
  
		/// Camera image picker button settings
		btnCamera = (ImageButton)findViewById(R.id.editProfileCameraBtn);
		btnCamera.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				saveUserInputFieldsFromLastInput();
				int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
				SlideOutActivity.prepare(ATLMyProfile.this, R.id.editProfileContent, width);
				Intent intent = new Intent(ATLMyProfile.this,
						GetProfilePic.class);
				intent.putExtra("from", "atl_my_profile");
				if (!encodedImage.equals(""))

					intent.putExtra("profile_path", encodedImage);
				startActivity(intent);
				overridePendingTransition(0, 0);
			}
		});

		/// Email fields init
		emailEdit = (EditText)findViewById(R.id.emailEdit);
		emailEdit.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				String emailAddress = emailEdit.getText().toString();
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) 
				{
					if (emailAddress!=null && isValidEmail(emailAddress))
					{
						if (!ATLUser.getEmail().equals(emailAddress) && !emailAddressAdded.contains(emailAddress))
						{
							emailEdit.setHint("done");
							
							((ImageView)
									findViewById(R.id.signInEditTitleCellTopImg)).setBackgroundResource(R.drawable.one_row_with_bottom_edges2x);
							emailAddressAdded.add(emailAddress);
							RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
							lp.setMargins(25, 25, 0, 0);
							emailEdit.setLayoutParams(lp);
							inflateAnotherEmail();
						}
						else
						{
							/// already added email address
							alertUser("Email Address already exsited ");
						}

					}  
					else
					{
						/// Invalid email address
						alertUser("Invalid Email Address");
					}
					
					return true;
				}
				return false;
			}});
				
			
		emailEdit.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (emailEdit.getHint().toString().equals("done"))
				{
					
					if(hasFocus)
						alertUser("Delete email address?"+emailEdit.getText().toString());
				}
			}
		});		



		// Save button 
		btnSave = (ImageButton)findViewById(R.id.editProfileSaveBtn);
		btnSave.setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						editMyProfile();

					}
				}


				);

	}
	

	private void setUserEmailAdressesFieldFromLastInput()
	{
		LinearLayout mainLinearLayout = (LinearLayout) findViewById(R.id.my_profile_email_list_view);
		if (emailAddressAdded!=null && emailAddressAdded.size()>0)
		{
			
			emailEdit.setHint("done");
			
			((ImageView)
					findViewById(R.id.signInEditTitleCellTopImg)).setBackgroundResource(R.drawable.one_row_with_bottom_edges2x);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(25, 25, 0, 0);
			emailEdit.setLayoutParams(lp);
			emailEdit.setText(emailAddressAdded.get(0));

//			final RelativeLayout linearLayout3= (RelativeLayout) View.inflate(this,
//					R.layout.custom_edit_text_cell, null);
//			final EditText editEmail = ((EditText) linearLayout3   
//					.findViewById(R.id.customTopEdgesEditText));
			RelativeLayout linearLayout3;
			EditText editEmail;
			for (final String emailAddress:emailAddressAdded)
			{
				if (emailAddress!=null && !emailAddress.equals(""))
					if (!emailAddress.equals(emailAddressAdded.get(0)))
					{
						 linearLayout3= (RelativeLayout) View.inflate(this,
						R.layout.custom_edit_text_cell, null);
						mainLinearLayout.addView(linearLayout3);
						 editEmail = ((EditText) linearLayout3   
								.findViewById(R.id.customTopEdgesEditText));


						editEmail.setHint("done");
						((ImageView)linearLayout3.
								findViewById(R.id.oneRowWithTopEdgesImg)).setBackgroundResource(R.drawable.one_row_with_edges2x);
						editEmail.setText(emailAddress);
						
						
						editEmail.setOnFocusChangeListener(new OnFocusChangeListener() {
							@Override
							public void onFocusChange(View v, boolean hasFocus) {
									if(hasFocus)
										alertUser("Delete email address?");
							} 
						});

						
						
					}
				if (emailAddress.equals(emailAddressAdded.get(emailAddressAdded.size()-1)))
					inflateAnotherEmail();
			}  
		}
	}

	private void setUserInputFieldsFromLastInput()
	{
		if(userFirstName!=null && !userFirstName.equals(""))
			userNameFirstNameEdit.setText(userFirstName);
		if(userLastName!=null && !userLastName.equals(""))
			userNameLastNameEdit.setText(userLastName);
		if(password!=null && !password.equals(""))
			userPasswordEdit.setText(password);
		if(passwordVerify!=null && !passwordVerify.equals(""))
			userPasswordVerifyEdit.setText(passwordVerify);


	}
	private void saveUserInputFieldsFromLastInput()
	{
		userFirstName = userNameFirstNameEdit.getText().toString();
		userLastName = userNameLastNameEdit.getText().toString();
		password = userPasswordEdit.getText().toString();
		passwordVerify = userPasswordVerifyEdit.getText().toString();
		
	}
	private void avatarPictureSettings()
	{
		ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.selection_profile_pic);

		if (!setProfilePic())  


			if (!ATLUser.getfbID().equals("")  )
			{
				// Find the user's profile picture custom view

				profilePictureView.setCropped(true);
				profilePictureView.setUserId(ATLUser.getfbID());
				ImageButton cameraBtn = (ImageButton)findViewById(R.id.editProfileCameraBtn);
				cameraBtn.setAlpha(0);
			}
		if (picTypePicker==1)
			chooseFromLibrary();
		else
			if (picTypePicker==2)
				takeAPhoto();

	}
	protected void editMyProfile() 
	{

		passwordVerify = userPasswordVerifyEdit.getText().toString();
		password = userPasswordEdit.getText().toString();
		userFirstName = userNameFirstNameEdit.getText().toString();
		userLastName = userNameLastNameEdit.getText().toString();

		passwordVerify = (passwordVerify!=null)? passwordVerify:"";
		password = (password!=null)? password:"";
		userFirstName = (userFirstName!=null)? userFirstName:"";
		userLastName = (userLastName!=null)? userLastName:"";



		if ((userFirstName!=null && !userFirstName.equals(""))||
				(userLastName!=null && !userLastName.equals("")) ||
				profilePic!=null ||
				(passwordVerify!=null && password!=null &&
				!passwordVerify.equals("") && !password.equals("") && 
				passwordVerify.equals(password)) ||
				profilePictureChanged ||
				(this.emailAddressAdded!=null && emailAddressAdded.size()>0 && !emailAddressAdded.contains(ATLUser.getEmailAddresses())))
		{ 
			//User can only Edit Profile while sign on...
			// call back on signInSuccess() method
			DatabaseConstants.SERVER_MESSAGE serverMessageBack;
			AtlasServerConnect.signInCurrentParseUser(ATLUser.getEmail(), ATLUser.getAtlasPassword(),this);

		}  

	}

	@Override
	public void signInSuccess(boolean success) 
	{
		if (success)
		{
		HashMap<String, Object> editProfileUpdateList = new HashMap<String, Object>();
		editProfileUpdateList = new HashMap<String, Object>();

		if (userFirstName!=null && !userFirstName.equals(""))
			editProfileUpdateList.put("first_name", userFirstName);
		if (userLastName!=null && !userLastName.equals(""))
			editProfileUpdateList.put("last_name", userLastName);
		VALID_REGISTRATION_MESSAGE userPasswordInputValid = VALID_REGISTRATION_MESSAGE.VALIDATE;
		if (passwordVerify!=null && password!=null && !passwordVerify.equals(""))
		{
			userPasswordInputValid = userPasswordInputValid(passwordVerify,password);
			switch (userPasswordInputValid) 
			{
			case VALIDATE:
				editProfileUpdateList.put("password_copy", password);
				Toast.makeText(getApplicationContext(), "updating ...", Toast.LENGTH_LONG).show();
				break;

			case INVALID_PASSWORD:
				alertUser("Passwords must be at least 8 charecter long and match ");
				break;

			}
		}
		if (editProfileUpdateList!=null && editProfileUpdateList.size()>0 &&
				userPasswordInputValid.equals(VALID_REGISTRATION_MESSAGE.VALIDATE))
			/// Call back on getUpateCallBack method
			parseConnect.updateAtlasUserInfoOnServer(editProfileUpdateList,this);  
		else
			if (this.profilePic!=null && profilePictureChanged)
			{  
				Toast.makeText(getApplicationContext(), "Saving new profile picture...", Toast.LENGTH_LONG).show();

				parseConnect.saveProfileImage(profilePic,this);

			}else
				updateUserEmailAddressesOnParse();
		}else
			alertUser("Failed to sign in ");
		

	}

	@Override
	public void getUpateCallBack(boolean success)
	{

		if (success)
		{
			if (this.profilePic!=null && profilePictureChanged )
			{  
				Toast.makeText(getApplicationContext(), "Saving new profile picture...", Toast.LENGTH_LONG).show();
				/// call back on saveCallBack
				parseConnect.saveProfileImage(profilePic,this);

			}
			else
			{
				//// update email addresses
				updateUserEmailAddressesOnParse();
			}

		}
		else
		{
			UtilitiesProject.alertUser("Uh oh!", "please check you entered valid information and that you're connected to the net",this);

		}


	}
	@Override
	public void saveCallBack(boolean saved) 
	{
		if (saved)
		{
			Toast.makeText(getApplicationContext(), "Saved profile picture...", Toast.LENGTH_LONG).show();
			AtlasServerConnect.refreshUserPictureOnTheBackground(this);
			applicationController.createLocalProfileFromServerProfile();
			//// update email addresses
			updateUserEmailAddressesOnParse();

		}
		else
			UtilitiesProject.alertUser("Error", "Youre picture has NOT been successfully updated",this);


	}
	@Override
	public void saveFileCallBack(boolean success, ParseObject psrseObjectSaved) {
		if (success)
		{
			Toast.makeText(getApplicationContext(), "Saved profile picture...", Toast.LENGTH_LONG).show();
			AtlasServerConnect.refreshUserPictureOnTheBackground(this);
			applicationController.createLocalProfileFromServerProfile();
			//// update email addresses
			updateUserEmailAddressesOnParse();

		}
		else
			UtilitiesProject.alertUser("Error", "Youre picture has NOT been successfully updated",this);


	}
	/**
	 * Set the user email addresses added on Parse email_address table
	 * gets a call back on setUserEmailAddressOnParseCallBack method
	 */
	private void updateUserEmailAddressesOnParse()
	{
		if (emailAddressAdded!=null && emailAddressAdded.size()>0)
		{
			AtlasServerConnect.addUserEmailAddress(ATLUser.getParseUserID(), emailAddressAdded, this);
		}
		else
		{
			UtilitiesProject.alertUser("", "Your Profile has been successfuly updated",this);
		}
	}
	@Override
	public void setUserEmailAddressOnParseCallBack(boolean success) 
	{
		String messageBack = (success)? "Your profile has been successfuly updated":
			"Failed to update your profile";
		UtilitiesProject.alertUser("", messageBack,this);


	}

	private VALID_REGISTRATION_MESSAGE userPasswordInputValid(
			String passwordVerify, String password) {  


		VALID_REGISTRATION_MESSAGE messageBack = VALID_REGISTRATION_MESSAGE.INVALID_PASSWORD;

		if (passwordVerify!=null && password!=null && password.equals(passwordVerify))
			messageBack = VALID_REGISTRATION_MESSAGE.VALIDATE;

		return messageBack;
	}
	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}
//	public boolean onOptionsItemSelected(MenuItem item) 
//	{
////		super.onOptionsItemSelected(item);
//		switch (item.getItemId()) 
//		{
//		case R.id.settings:
////			Toast.makeText(this, "Settings pressed",
////					Toast.LENGTH_SHORT).show();
//			Intent intentCalSettings = new Intent(ATLMyProfile.this,
//					ATLSettingsActivity.class);
//			intentCalSettings.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
//					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intentCalSettings);
////			CalendarDayView.this.overridePendingTransition(
////					R.anim.main_fragment_back_from_right,
////					R.anim.stand_by);
////			System.gc();
//			break;
//		case R.id.profile:
////			Intent intentTasks = new Intent(ATLMyProfile.this,
////					ATLMyProfile.class);
////			intentTasks.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
////					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
////			startActivity(intentTasks);
////			System.gc();
//			break;
//		case R.id.share:
//			Toast.makeText(this, "Coming soon",
//					Toast.LENGTH_SHORT).show();
////			Intent intentCalSettings = new Intent(CalendarDayView.this,
////					Settings.class);
////			intentCalSettings.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
////					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
////			startActivity(intentCalSettings);
//////			CalendarDayView.this.overridePendingTransition(
////					R.anim.main_fragment_back_from_right,
////					R.anim.stand_by);
////			System.gc();
//			break;
//		case R.id.feedback:
//			Toast.makeText(this, "Coming soon",
//					Toast.LENGTH_SHORT).show();
////			Intent intentCalSettings = new Intent(CalendarDayView.this,
////					Settings.class);
////			intentCalSettings.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
////					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
////			startActivity(intentCalSettings);
//////			CalendarDayView.this.overridePendingTransition(
////					R.anim.main_fragment_back_from_right,
////					R.anim.stand_by);
////			System.gc();
//			break;
//
////		default:
////			return super.onOptionsItemSelected(item);
//		}
//		return true;
//	}
	boolean validPreviousEmail=false;
	String editEmailString;
	@Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) 
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
        	finish();
//           
//       	 if (rightActioBarBtn.getVisibility()==View.VISIBLE)
//       	 {
//       		  Intent intent = new Intent(getBaseContext(), CalendarDayView.class);
//                 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                		//intent.putExtra("EXTRA_SESSION_ID", sessionId);
//                 startActivity(intent);
//                 this.overridePendingTransition(R.anim.enter,R.anim.leave);
//
//                return true;
//       	 }else
//       		 
//       	 
//       		 mMenuDrawer.closeMenu();
       	
        }
       
        
        
        return false;
    }
	
	  @Override
	  public boolean onOptionsItemSelected(MenuItem item) 
		{
//			super.onOptionsItemSelected(item);
			switch (item.getItemId()) 
			{
			case R.id.settings:
//				Toast.makeText(this, "Settings pressed",
//						Toast.LENGTH_SHORT).show();
				Intent intentCalSettings = new Intent(ATLMyProfile.this,
						ATLSettingsActivity.class);
				intentCalSettings.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intentCalSettings);
//				CalendarDayView.this.overridePendingTransition(
//						R.anim.main_fragment_back_from_right,
//						R.anim.stand_by);
//				System.gc();
				break;
			case R.id.profile:
//				Intent intentTasks = new Intent(ATLMyProfile.this,
//						ATLMyProfile.class);
//				intentTasks.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
//						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intentTasks);
//				System.gc();
				break;
			case R.id.share:
				Toast.makeText(this, "Coming soon",
						Toast.LENGTH_SHORT).show();
//				Intent intentCalSettings = new Intent(CalendarDayView.this,
//						Settings.class);
//				intentCalSettings.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
//						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intentCalSettings);
////				CalendarDayView.this.overridePendingTransition(
//						R.anim.main_fragment_back_from_right,
//						R.anim.stand_by);
//				System.gc();
				break;
			case R.id.feedback:
				Toast.makeText(this, "Coming soon",
						Toast.LENGTH_SHORT).show();
//				Intent intentCalSettings = new Intent(CalendarDayView.this,
//						Settings.class);
//				intentCalSettings.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
//						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intentCalSettings);
////				CalendarDayView.this.overridePendingTransition(
//						R.anim.main_fragment_back_from_right,
//						R.anim.stand_by);
//				System.gc();
				break;

//			default:
//				return super.onOptionsItemSelected(item);
			}
			return true;
		}
	private void inflateAnotherEmail()
	{
		final LinearLayout mainLinearLayout = (LinearLayout) findViewById(R.id.my_profile_email_list_view);


		final RelativeLayout linearLayout3 = (RelativeLayout) View.inflate(this,
				R.layout.custom_edit_text_cell, null);


		mainLinearLayout.addView(linearLayout3);



		final EditText editEmail = ((EditText) linearLayout3
				.findViewById(R.id.customTopEdgesEditText));

		editEmail.requestFocus();
		editEmail.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				String emailAddress = editEmail.getText().toString();
				  

					if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) 
					{

						if (emailAddress!=null && isValidEmail(emailAddress))
						{

							if (!editEmail.getHint().toString().equals("done"))
							{
								if (!emailAddressAdded.contains(emailAddress) && !ATLUser.getEmail().equals(emailAddress))
								{
									editEmail.setHint("done");
									((ImageView)linearLayout3.
											findViewById(R.id.oneRowWithTopEdgesImg)).setBackgroundResource(R.drawable.one_row_with_edges2x);

									emailAddressAdded.add(emailAddress);
									inflateAnotherEmail();
								}else
								{
									alertUser("Email Address already exsited ");
								}
							}
   
						}
						else
						{
							alertUser("Invalid Email Address");

						}

						// Toast.makeText(HelloFormStuff.this, edittext.getText(), Toast.LENGTH_SHORT).show();
						return true;
					}
				return false;
			}
		}
				);
		editEmail.addTextChangedListener(new TextWatcher(){


			public void afterTextChanged(Editable s) {



			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
				if (editEmail.getHint().toString().equals("done"))
				{
					alertUser("Delete email address?");
				}
				
			}
			public void onTextChanged(CharSequence s, int start, int before, int count){

				editEmailString = s.toString();
			}
		});
		
		
		editEmail.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (editEmail.getHint().toString().equals("done"))
				{

					if(hasFocus)
						alertUser("Delete email address?");
					//				        Toast.makeText(getApplicationContext(), "got the focus", Toast.LENGTH_LONG).show();
					//				    }else
					//				        Toast.makeText(getApplicationContext(), "lost the focus", Toast.LENGTH_LONG).show();
					//				    }
				}
			} 
		});

	}
	@Override
	public void onResume(){
		super.onResume();
	}

	private void alertUser(String message)
	{
		UtilitiesProject.alertUser("", message, this);

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
//		if(imgFile.exists())

			storedBitmap = UtilitiesProject.getUserProfilePic();//BitmapFactory.decodeFile(imgFile.getAbsolutePath());

		currentBitmap  = (currentPhoto!=null)? currentPhoto : storedBitmap;

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
		setUserInputFieldsFromLastInput();
		setUserEmailAdressesFieldFromLastInput();
		switch (requestCode) {
		case INTENT_REQUEST_CODE_BROWSE_PICTURE:
			if( resultCode == Activity.RESULT_OK ){

				Uri currentPhotoURI = data.getData();
				if (currentPhotoURI != null) {
					try {
						Bundle params = new Bundle();
						params.putByteArray("photo",
								FacebookUtilities.scaleImage(getApplicationContext(), currentPhotoURI));
						profilePictureChanged = true;
						profilePic = params.getByteArray("photo");
						currentPhoto = BitmapFactory.decodeByteArray(profilePic, 0, profilePic.length);
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
				profilePic = baos.toByteArray(); 
				profilePictureChanged = true;
				encodedImage = Base64.encodeToString(profilePic, Base64.DEFAULT);

			}
			break;


		}


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
	public void registerSuccess(boolean success) {
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
	public void resetPasswordCallBack(boolean emailSuccessfullySent,
			String parseMessage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onArticleSelected(int position) {
		// TODO Auto-generated method stub

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
	public void getAtlasUserByEmailAddressCallBack(
			HashMap<String, Object> userLoginInfo) {
		// TODO Auto-generated method stub

	}




	@Override
	public void getEmailAddressRecordCallBack(
			EmailAddressRecord emailAddressRecord) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void refreshUserPictureOnTheBackgroundCallBack(boolean success,
			String imageUrl) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void isEmailVerifiedCallBack(boolean verified) {
		// TODO Auto-generated method stub
		
	}

}
