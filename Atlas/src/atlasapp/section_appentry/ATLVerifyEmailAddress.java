package atlasapp.section_appentry;

import java.util.HashMap;
import java.util.List;

import com.parse.ParseObject;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;
import atlasapp.common.ATLUser;
import atlasapp.common.CurrentSessionFriendsList;
import atlasapp.database.ATLDBCommon;
import atlasapp.database.AtlasServerConnect;
import atlasapp.database.EmailAddressCallBackInterface;
import atlasapp.database.ParseDBCallBackInterface;
import atlasapp.database.UserDelagateInterface;
import atlasapp.model.ATLContactModel;
import atlasapp.section_alerts.ATLALertListController;
import atlasapp.section_calendar.CalendarDayView;
import atlasapp.slidemenu.ATLSlideMenu;

public class ATLVerifyEmailAddress extends ATLSlideMenu implements ViewFactory, ParseDBCallBackInterface{
	
	private AtlasApplication applicationController;

	private AtlasServerConnect parseConnect;

	 @Override
	    public void onCreate(Bundle savedInstanceState) {
		  currentActivity = ATLVerifyEmailAddress.this;
	        super.onCreate(savedInstanceState);
	        currentContext = this;
	        setContentView(R.layout.atl_verify_email_address);
	        
	        disableDrawer();
			setRefreshButtonEnable();
			refreshActioBarBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) 
				{
					checkEmailVerified();
				}
			});
       	 	switchImages();
       	 	ImageButton resendEmail = (ImageButton)findViewById(R.id.atlVerifyEmailResendEmailBtn);
       	 	resendEmail.setOnClickListener(new View.OnClickListener() {

       	 		@Override
       	 		public void onClick(View v) 
       	 		{
       	 			Toast.makeText(ATLVerifyEmailAddress.this,"Resending verification email...",
       	 					Toast.LENGTH_SHORT).show();
       	 			parseConnect.verifyPrimaryEmailAddress();
       	 		}
       	 	});
       	 	parseConnect =   AtlasServerConnect.getSingletonObject(this);
       	 parseConnect.verifyPrimaryEmailAddress();
	 }
	 /**
	  * Check on Parse database whether the user email is_verified column
	  * set to true callback on the isEmailVerifiedCallBack method
	  */
	 protected void checkEmailVerified() 
	 {
		 parseConnect.isEmailVerified(this);
	 }
	 @Override
	 public void isEmailVerifiedCallBack(boolean verified)
	 {
		 if (verified)
		 {
			 // update shared preferences on the user mobile
		        applicationController = (AtlasApplication)getApplicationContext();
		        HashMap<String, Object> atlasUserVerified = new HashMap<String, Object>();
		        atlasUserVerified.put("verified_email", "TRUE");
		        ATLUser.setIsAtlasUser(true);
		        applicationController.setAtlasSharedPreferences(atlasUserVerified);
		        backgroundProcesses(true);
		        Intent intent = new Intent(getBaseContext(), CalendarDayView.class);
        		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        		startActivity(intent); 
		 
		 }
		 else
		 {
//			 Toast.makeText(ATLVerifyEmailAddress.this,"Resending verification email...",
//	 					Toast.LENGTH_SHORT).show();
	 			parseConnect.verifyPrimaryEmailAddress();
		 }
	 }
	 public void backgroundProcesses(boolean firstRegister) {
			
	    	
			AtlasServerConnect.subscribedToChannels(getApplicationContext(), "ID"+ATLUser.getParseUserID(), ATLALertListController.class);

	    	CurrentSessionFriendsList currentSessionFriendsList = CurrentSessionFriendsList.getSingletonObject();
			currentSessionFriendsList.setFriendsUpdateComplete(false);

			/////SET ALL CURRENT EMAIL'S (USER NAMES) FROM ATLAS DB (PARSE)
			ATLDBCommon atlDBCommon = ATLDBCommon.getSingletonObject(null);
			
//			ATLContactModel.readAllLocalFriendsInBackground();
			
			
			
			atlDBCommon.setCurrentSessionUsersOnATLASInBackground(true,firstRegister);

			ATLContactModel.readAllLocalFriendsInBackground();

		}
	 private void switchImages()
	 {
		 final Integer[] imageIDs = { 
				 R.drawable.atl_verify_email_address_calendar,
				 R.drawable.atl_verify_email_address_contacts,
				 R.drawable.atl_verify_email_address_tasks ,
				 R.drawable.atl_verify_email_address_push};
     	final Animation out = AnimationUtils.loadAnimation(this,
                  android.R.anim.slide_out_right);
     	 final Animation in = AnimationUtils.loadAnimation(this,
                  android.R.anim.slide_in_left);
     	 in.setStartOffset(1000);
     	 out.setStartOffset(1000);
          final ImageSwitcher imageSwitcher = (ImageSwitcher) findViewById(R.id.switcher1);
          imageSwitcher.setFactory(this);
          imageSwitcher.setInAnimation(in);
          imageSwitcher.setOutAnimation(out);
          imageSwitcher.postDelayed(new Runnable() {
              int i = 0;
              public void run() {
            	  i++;  
            	  int res=-1;   
            	  res =   (i% 4 == 0 )?
        			  R.drawable.atl_verify_email_address_calendar :
                    	  (i%4==1)?
                      R.drawable.atl_verify_email_address_contacts:
                    	  (i%4==2)?
                    	  R.drawable.atl_verify_email_address_tasks :
                    		  R.drawable.atl_verify_email_address_push;
                  imageSwitcher.setImageResource(res );
                  imageSwitcher.setInAnimation(in);
                  imageSwitcher.setOutAnimation(out);
                  imageSwitcher.postDelayed(this, 3000);
              }
          }, 1000);
//		
	 }
	 
	 
	 
	 @Override
		public View makeView() {
			 ImageView imageView = new ImageView(ATLVerifyEmailAddress.this);
//	        TouchImageView imageView = new TouchImageView(ATLWelcome.this);
//	        imageView.setBackgroundColor(0xFF000000);
	        imageView.setScaleType(ImageView.ScaleType.CENTER);
	        imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
	                ImageSwitcher.LayoutParams.FILL_PARENT, ImageSwitcher.LayoutParams.FILL_PARENT));
	        return imageView;
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
	public void registerSuccess(boolean signUpSuccess) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void signInSuccess(boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void saveFileCallBack(boolean success, ParseObject psrseObjectSaved) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getUserEmailOnParseCallBack(
			HashMap<String, String> userParseLogin, boolean success) {
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
	public void getUpateCallBack(boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void signInNewFriendUserSuccess(boolean success, ParseUser user) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void refreshUserPictureOnTheBackgroundCallBack(boolean success,
			String imageUrl) {
		// TODO Auto-generated method stub
		
	}
	
}
