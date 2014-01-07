package atlasapp.common;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import atlasapp.database.AtlasServerConnect;
import atlasapp.database.DatabaseConstants.SIGN_NEW_USERS_CALLER;
import atlasapp.database.UserDelagateInterface;
import atlasapp.model.ATLContactModel;
import atlasapp.section_appentry.AtlasApplication;
import atlasapp.section_appentry.R;
import atlasapp.section_settings.Settings;
import atlasapp.section_tasks.EditTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Gets contact ,picked by the user through address book, name,email,phone
 * number and photo (if exsits) check whether contact already on Atlas and shows
 * the available option to contact this contact (email/phone/atlas)
 * 
 * return to the latest activity (called) with the ATLContactModel invitee made
 * for farther processing
 * 
 * @author sharonnachum
 * 
 */
public class ContactCard extends Activity implements UserDelagateInterface{ 

	private String name, email, mobile, inviteType;
	private byte[] photo;
	private AtlasServerConnect parseConnect;
	private static ATLContactModel invitee;
	private Intent inviteIntent;
//	private SectionActivity act;
	private AtlasApplication applicationController;
	private Button emailBtn;
	private Button mobileBtn;
	private RelativeLayout atlasLayout;
	private RelativeLayout mobileLayout;
	private RelativeLayout emailLayout;

//	public enum SectionActivity {
//
//		InviteToAtlas(com.atlasapp.section_settings.Settings.class), TaskAssign(
//				EditTask.class);
//
//		public Class<? extends Activity> activityClass;
//
//		private SectionActivity(Class<? extends Activity> clazz) {
//			this.activityClass = clazz;
//		}
//
//		public Class<? extends Activity> getActivity() {
//			return activityClass;
//		}
//	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		applicationController = (AtlasApplication)getApplicationContext();

		setContentView(R.layout.pick_contact_info_disabled);
		parseConnect = AtlasServerConnect.getSingletonObject(this);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			name = extras.getString("contact_name");
			email = extras.getString("contact_email");
			mobile = extras.getString("contact_mobile");
			photo = extras.getByteArray("contact_photo");
			inviteType = extras.getString("invite_type");
			invitee = new ATLContactModel();
		}

	//	act = SectionActivity.valueOf(inviteType);
	//	inviteIntent = new Intent(this, act.getActivity());
		// inviteIntent.putExtra("ContactInfo", invitee);
		// startActivity(inviteIntent);
		// to retrieve object in second Activity
		// ATLContactModel invitee = (ATLContactModel)
		// getIntent().getSerializableExtra("ContactInfo");

		name = (name != null && !name.equals("")) ? name : "";
		if (!name.equals("")) {
			invitee.setFirstname(name);
			invitee.setLastname("");
			TextView contactName = (TextView) findViewById(R.id.contactName);
			contactName.setText(name);
		}

		 atlasLayout = (RelativeLayout) findViewById(R.id.atlasLayout);
		atlasLayout.setVisibility(View.INVISIBLE);
		 mobileLayout = (RelativeLayout) findViewById(R.id.mobileLayout);
		 emailLayout = (RelativeLayout) findViewById(R.id.emailLayout);

		
		
		
		setProfilePic();
		
		if (mobile != null && !mobile.equals("")) {
			invitee.setPhoneNumber(mobile);
			 mobileBtn = (Button) findViewById(R.id.mobileBtn);
			mobileBtn.setText(("mobile : " + mobile));
			mobileBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// sendSMSAlert(name,mobile);

				}
			});

		} else
			mobileLayout.setVisibility(View.INVISIBLE);

		if (email != null && !email.equals("")) {
			invitee.setEmailAddress(email);
			 emailBtn = (Button) findViewById(R.id.emailBtn);
			emailBtn.setText(("email : " + email));

			
			/// wait for getFriendEmailOnParse() call back
			 parseConnect.getFriendEmailOnParse(email);
//
//			if (atlasContact != null && !atlasContact.getAtlasId().equals("")) {
//				atlasLayout.setVisibility(View.VISIBLE);
//				invitee = atlasContact;
//
//				Button atlasBtn = (Button) findViewById(R.id.atlasBtn);
//				atlasBtn.setOnClickListener(new View.OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						inviteIntent.putExtra(
//								"com.atlasapp.model.ATLContactModel", invitee);
//						startActivity(inviteIntent);
//
//					}
//				});
//			}
//
//			emailBtn.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//
//				}
//			});

		} else
			emailLayout.setVisibility(View.INVISIBLE);

	}

	private void setProfilePic() {	
		
		
		
		ImageView contactPhoto = (ImageView) findViewById(R.id.contactImage);
	
	String profilePicName = (invitee.getAtlasId()!=null && !invitee.getAtlasId().equals(""))?
			invitee.getAtlasId():"";
	String destinationImagePath= "/"+profilePicName+".png";	
			
	if (!profilePicName.equals(""))
	{
		 Bitmap storedBitmap = null;
		 File PROFILE_PIC_PATH= new File (applicationController.IMAGE_DIR+"/friendPics/",destinationImagePath) ;
			if(PROFILE_PIC_PATH.exists())
			//String filePath = applicationController.IMAGE_DIR+"/"+profilePicName+".png";
	
	
			 storedBitmap = BitmapFactory.decodeFile(PROFILE_PIC_PATH.getAbsolutePath());

			contactPhoto.setImageBitmap(storedBitmap);
			invitee.setImage(storedBitmap);
	}
	}// TODO Auto-generated method stub

	@Override
	public void getFriendEmailOnParse(
			ATLContactModel friendsPropertiesOnParseByEmail, boolean success) {
		if (friendsPropertiesOnParseByEmail != null && !friendsPropertiesOnParseByEmail.getAtlasId().equals("")) {
			atlasLayout.setVisibility(View.VISIBLE);
			invitee = friendsPropertiesOnParseByEmail;

			Button atlasBtn = (Button) findViewById(R.id.atlasBtn);
			atlasBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					inviteIntent.putExtra(
							"com.atlasapp.model.ATLContactModel", invitee);
					startActivity(inviteIntent);

				}
			});
		}

		emailBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		
	}

	@Override
	public void registerSuccess(boolean success) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void signInSuccess(boolean success) {
		// TODO Auto-generated method stub
		
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

	@Override
	public void resetPasswordCallBack(boolean emailSuccessfullySent,
			String parseMessage) {
		// TODO Auto-generated method stub
		
	}

	

}
