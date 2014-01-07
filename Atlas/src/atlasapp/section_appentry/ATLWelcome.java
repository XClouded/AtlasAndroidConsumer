package atlasapp.section_appentry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.parse.ParseObject;
import com.parse.ParseUser;

import atlasapp.common.ATLAnimationUtils;
import atlasapp.common.ATLUser;

import atlasapp.common.UtilitiesProject;
import atlasapp.database.AtlasServerConnect;
import atlasapp.database.DatabaseConstants.SIGN_NEW_USERS_CALLER;
import atlasapp.database.EmailAddressCallBackInterface;
import atlasapp.database.EmailAddressRecord;
import atlasapp.database.ParseDBCallBackInterface;
import atlasapp.database.UserDelagateInterface;
import atlasapp.facebook.ProfilePictureView;
import atlasapp.model.ATLContactModel;
import atlasapp.section_appentry.R;
import atlasapp.section_calendar.CalendarDayView;
import atlasapp.section_settings.ATLCalendarSettingsSelectCalendarActivity;
import atlasapp.slidemenu.ATLSlideMenu;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.OnNavigationListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;
/**
 * App entry , direct atlas users (that are listed on database)
 * to signin/register otherwise puts them in the witing list
 * @author sharonnachum
 *
 */



@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class ATLWelcome extends Activity implements UserDelagateInterface,ParseDBCallBackInterface,EmailAddressCallBackInterface{

	String[] actions = new String[] {
			"Today",
			"Calendar",
			"Map View",
			"Task Lists",
			"Contacts",
			"Find My Friends",
			"My Profile",
			"Settings"
		};   
	
	private EditText userEmailEditText;
	private ImageButton enterAppBtn;
	private String userEmail;
	private HashMap<String, String> loginProperties;

	private AtlasApplication applicationController;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
//	        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
//
//	        //setContentView(R.layout.main);
//
//	        /** Create an array adapter to populate dropdownlist */
//	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, actions);
//	          
//	        ActionBar actionBar = getActionBar();
//	        actionBar.hide();
	        
//	        /** Enabling dropdown list navigation for the action bar */
//	        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
//	        
//	        /** Defining Navigation listener */
//	        ActionBar.OnNavigationListener navigationListener = new OnNavigationListener() {
//				
//				@Override
//				public boolean onNavigationItemSelected(int itemPosition, long itemId) {
//					Toast.makeText(getBaseContext(), "You selected : " + actions[itemPosition]  , Toast.LENGTH_SHORT).show();
//					return false;
//				}
//			};
//		
//			/** Setting dropdown items and item navigation listener for the actionbar */
//			getActionBar().setListNavigationCallbacks(adapter, navigationListener);        
//	        
	        ///////////////////////
	        
	        
	        applicationController = (AtlasApplication)getApplicationContext();

	        if (ATLUser.isLoggedIn() && applicationController.localAtlasProf)
	        {
	        	if (applicationController.verified)
	        	{
	        		Intent intent = new Intent(getBaseContext(), CalendarDayView.class);
	        		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	        		startActivity(intent);  
	        	}
	        	else
	        	{
	        		Intent intent = new Intent(getBaseContext(), ATLVerifyEmailAddress.class);
	        		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	        		startActivity(intent);
	        	}


	        }
	        else
	        {
	        	getActionBar().hide();
//	        	setContentView(R.layout.atl_verify_email_address);
	        	
	        	 
	        	
	        	setContentView(R.layout.atl_welcome);

	        	userEmailEditText = (EditText)findViewById(R.id.atlWelcomeEnterEmailEditText);
	        	
	        	userEmailEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() { 
	        	    @Override 
	        	    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) { 
	        	        if (actionId == EditorInfo.IME_ACTION_DONE) { 
//	                        Toast.makeText(ATLWelcome.this, "DONE ",Toast.LENGTH_LONG).show();
	        	        	userEmail = userEmailEditText.getText().toString();

		        			if (userEmail!=null && !userEmail.equals(""))
		        			{
		        				searchEmailOnParseDB();
		        				
		        			}
	        	        } 
	        	        return false; 
	        	    } 
	        	}); 
	        	enterAppBtn = (ImageButton)findViewById(R.id.atlWelcomeEnterBtn);
	        	enterAppBtn.setOnClickListener(new View.OnClickListener() {

	        		@Override
	        		public void onClick(View v) {
	        			userEmail = userEmailEditText.getText().toString();

	        			if (userEmail!=null && !userEmail.equals(""))
	        			{
	        				searchEmailOnParseDB();
	        				
	        			}
	        		}
	        		
	        		
	        		
	        	});
//	        	
	        }
//	        signInWithGMail();
  
	 }
	
	
	   private void signInWithGMail()
	    {
	    	AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
	    	Account[] list = manager.getAccounts();
	    	String gmail = null;
//	    	manager.getUserData(account, key)

	    	for(Account account: list)
	    	{
	    	    if(account.type.equalsIgnoreCase("com.google"))
	    	    {
	    	        gmail = account.name;
	    	        
	    	        UtilitiesProject.alertUser("", gmail+manager.getPassword(account), this);
	    	        break;
	    	    }
	    	}
	    }
	 @Override
     public boolean onKeyDown(int keyCode, KeyEvent event) 
     {
         if(keyCode == KeyEvent.KEYCODE_BACK)
         {
//            
        	  Intent intent = new Intent(getBaseContext(), ATLWelcome.class);
              intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(intent);
              this.overridePendingTransition(R.anim.enter,R.anim.leave);

             return true;
         }
      
        	 
         return false;
     }
	 /**
	  * Search the user email on Parse
	  * gets a call back on the getUserEmailOnParseCallBack method
	  */
	 private void searchEmailOnParseDB()
	 {
		 AtlasServerConnect.getUserEmailOnParse(userEmail,this);
		 /// background checking of email in email_address table also
		 setContentView(R.layout.atl_welcome);
		 userEmailEditText.setText(userEmail);
	 }

	 @Override
	 public void getUserEmailOnParseCallBack(
				HashMap<String, String> loginProperties, 
				boolean success)
	 {
		 if (success)
		 {
			 //// Server result back
			 if (loginProperties!=null && !loginProperties.isEmpty())
			 {
				 this.loginProperties = loginProperties;
				 ////  Atlas user already
				 /// Sign in....    
				 AtlasServerConnect.signInCurrentParseUser(loginProperties.get("email"),loginProperties.get("password_copy"),this);
				
			 }  
			 else
			 {
				 //// No such user     
				 /// waiting list
				 ////TO DO:
					 /// email not found on the Users table ....
					 /// either this isnt the User's primary invite email
					 /// or the user was never invited (send to Waiting List)
					 
					 ///try find the email on the email_address
				 AtlasServerConnect.getUserFromEmailAddressTable(userEmail, this);
			 }
		 }
		 else
		 {
			 //// Server error

		 }
			
	 }  
	 @Override
		public void getAtlasUserByEmailAddressCallBack(
				HashMap<String, Object> userLoginInfo) {
			if (userLoginInfo!=null && !userLoginInfo.isEmpty())
			{
				loginProperties = new HashMap<String, String>();
				
				loginProperties.put("email", userLoginInfo.get("email").toString());
				loginProperties.put("password_copy", userLoginInfo.get("password_copy").toString());
				loginProperties.put("is_atlas_user", userLoginInfo.get("is_atlas_user").toString());
				loginProperties.put("first_name", userLoginInfo.get("first_name").toString());
				loginProperties.put("last_name", userLoginInfo.get("last_name").toString());
				loginProperties.put("facebook_id", userLoginInfo.get("facebook_id").toString());
				loginProperties.put("atlas_id", userLoginInfo.get("atlas_id").toString());
				loginProperties.put("access_token", userLoginInfo.get("access_token").toString());
				
				
			//	 AtlasServerConnect.signInCurrentParseUser(loginProperties.get("email"),loginProperties.get("password_copy"),this);

				directAnInvitedAtlasUser();
				  
				
				
				
//				if (loginProperties==null || loginProperties.size()==0)
//				{
					//this.loginProperties = userLoginInfo;
					 ////  Atlas user already
					 /// Sign in....
				//	 AtlasServerConnect.signInCurrentParseUser(userLoginInfo.get("email").toString(),loginProperties.get("password_copy").toString(),this);
//				}
//				else
//				{
//					//// user was already found ....
//				}
				
			}
			else   
			{
		       // setContentView(R.layout.atl_waiting_list);
				// New user to Register on Parse...
				 Intent intent = new Intent(this, ATLRegister.class);
		         intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		         intent.putExtra("email", userEmail);
		         intent.putExtra("new_user", true);
		         startActivity(intent);
		         this.overridePendingTransition(R.anim.enter,R.anim.leave);
			}
			  
		}
	 @Override
		public void signInSuccess(boolean success) {
		 
		 
		 if (success)
		 {

			 directAnInvitedAtlasUser();
		 }
	 }
	   
	 
	 private void directAnInvitedAtlasUser()
	 {
		 if (loginProperties!=null && !loginProperties.isEmpty() )
		if ( loginProperties.get("is_atlas_user")!=null
				 && loginProperties.get("is_atlas_user").equals("1"))
		 {
//	        AtlasServerConnect.getAllATLUserEmailAddress(loginProperties.get("atlas_id"),ATLSignIn.listener);

			 Intent intent = new Intent(this, ATLSignIn.class);
	         intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	         intent.putExtra("email", loginProperties.get("email"));
	         intent.putExtra("password_copy", loginProperties.get("password_copy"));
	         intent.putExtra("is_atlas_user", loginProperties.get("is_atlas_user"));
				//userParseLogin.put("image", user.get)
	         intent.putExtra("first_name", loginProperties.get("first_name"));
	         intent.putExtra("last_name",loginProperties.get("last_name"));
	         intent.putExtra("facebook_id",loginProperties.get("facebook_id"));
				
	         intent.putExtra("atlas_id",loginProperties.get("atlas_id"));
	         startActivity(intent);
	         this.overridePendingTransition(R.anim.enter,R.anim.leave);
	         
	      //   ATLAnimationUtils.hideKeyboard(this, userEmailEditText);
	      //   finish();
			 }
			
			 
		 else
		 {
			 ////A non  Atlas user 
			 //// Register...
			 Intent intent = new Intent(this, ATLRegister.class);
	         intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	         intent.putExtra("email", loginProperties.get("email"));
	         intent.putExtra("new_user", false);
	         intent.putExtra("password_copy", loginProperties.get("password_copy"));
	         intent.putExtra("first_name", loginProperties.get("first_name"));
	         intent.putExtra("last_name",loginProperties.get("last_name"));
				
	         intent.putExtra("atlas_id",loginProperties.get("atlas_id"));
	         startActivity(intent);
	         this.overridePendingTransition(R.anim.enter,R.anim.leave);
	         
	      //   ATLAnimationUtils.hideKeyboard(this, userEmailEditText);
	      //   finish();
			 }
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
	@Override
	public void getAllAtlasUserEmailAddressRecordsCallBack(
			ArrayList<EmailAddressRecord> emailAddressRecords) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setUserEmailAddressOnParseCallBack(boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getAllAtlasUserEmailAddressCallBack(
			ArrayList<String> emailAddrees) {
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
	public void refreshUserPictureOnTheBackgroundCallBack(boolean success,
			String imageUrl) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void isEmailVerifiedCallBack(boolean verified) {
		// TODO Auto-generated method stub
		
	}
	
	


}