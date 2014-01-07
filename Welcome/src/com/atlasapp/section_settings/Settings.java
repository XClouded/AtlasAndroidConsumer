//  ==================================================================================================================
//  Tasks.java
//  ATLAS

//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2013-01-09 TAN:     Add implement the new tabbar, ATLTabbarViewDelegete and hide the old tabbar
//  ==================================================================================================================
package com.atlasapp.section_settings;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;



import com.atlasapp.model.ATLContactModel;
import com.atlasapp.section_appentry.R;
import com.atlasapp.atlas_database.AtlasServerConnect;
import com.atlasapp.common.ATLEventCalendarModel;
import com.atlasapp.common.ATLMockDB;
import com.atlasapp.common.ATLTabbarView;
import com.atlasapp.common.ATLTabbarViewDelegete;
import com.atlasapp.common.AtlasAndroidUser;
import com.atlasapp.common.ContactCard;
import com.atlasapp.common.PickFriendsActivity;
import com.atlasapp.facebook.FacebookUtilities;
import com.atlasapp.section_appentry.AtlasApplication;
import com.atlasapp.section_appentry.SignIn;
import com.atlasapp.section_appentry.Welcome;
import com.atlasapp.section_calendar.ATLCalendarIntentKeys;
import com.atlasapp.section_calendar.CalendarDayView;
import com.atlasapp.section_contacts.ATLContactListActivity;
import com.atlasapp.section_notes.ATLNoteListActivity;
import com.atlasapp.section_tasks.ATLTaskListActivity;
import com.atlasapp.section_tasks.Tasks;
import com.atlasapp.section_today.Today;
import com.facebook.GraphUser;
import com.facebook.Session;
import com.parse.ParseObject;
import com.parse.ParseUser;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.telephony.SmsManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends  FragmentActivity implements ATLTabbarViewDelegete{
	
	
	
	 private final int PICK_FRIENDS_ACTIVITY = 1;
	 private final int PICK_CONTACT = 0;
	 private final int PICK_INVITE_CONTACT = 2;
	    private Button pickFriendsButton;
	    private TextView resultsTextView;

	
	private ArrayList<ATLContactModel> facebookAtlasFriends;

    private AtlasApplication applicationController;
	private AtlasServerConnect parseConnect;
	
	// 2013-01-09 TAN: add new tabbar
	private LayoutInflater mInflater;
	private ViewGroup mLayout;
	private ATLTabbarView tabbar;
	//==============================
	@Override 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
     // =========================================================
     // 2013-01-09 TAN: START - Add new tabbar
     // =========================================================
        mInflater = LayoutInflater.from(this);
		mLayout = (ViewGroup) mInflater.inflate(R.layout.settings, null);
		setContentView(mLayout);
		
		tabbar = new ATLTabbarView(this);
		tabbar.delegate = this;
		mLayout.addView(tabbar);
	 // =========================================================
     // 2013-01-09 TAN: START - Add new tabbar
     // =========================================================
		
//        setContentView(R.layout.settings); //2013-01-09 TAN: comment out
        
        applicationController = (AtlasApplication)getApplicationContext();
        facebookAtlasFriends = new ArrayList<ATLContactModel>();
        
        parseConnect =   AtlasServerConnect.getSingletonObject(this);
        
     
        
        
        String message = "";
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) 
        {
        	 message = extras.getString("message");
        	 
        	 ATLContactModel invitee = extras.getParcelable("com.atlasapp.model.ATLContactModel");
        	 
        	 if (invitee!=null)
        		 
        		 message = "Hello "+invitee.getFirstname()+" ! ";
        
        }
        
       
        // ImageButton btn = (ImageButton)findViewById(R.id.cancelBtn);

         Button findFBfriendsBtn = (Button)findViewById(R.id.settingsFindAtlasFriendsBtn);
         findFBfriendsBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//onClickFindFriends();
				
				
				Intent intentFriend = new Intent(getBaseContext(), FriendsFinder.class);
				intentFriend.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            	//intent.putExtra("EXTRA_SESSION_ID", sessionId);
            	startActivity(intentFriend);
				
			}

			
		});
           
         
         Button findfriendsBtn = (Button)findViewById(R.id.settingsInviteFindToAtlasBtn);
         findfriendsBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			//	inviteByEmail();
				
				
				Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(intent, PICK_CONTACT);

				
			//	onClickPickFriends();
				
			}

			
		});
         
         Button signOutBtn =  (Button)findViewById(R.id.settingsSignOutBtn);
         signOutBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				applicationController.logInOrOutUser(false);
				Intent intentCalendar = new Intent(getBaseContext(), Welcome.class);
             	intentCalendar.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
             	//intent.putExtra("EXTRA_SESSION_ID", sessionId);
             	startActivity(intentCalendar); 
			}
		});
         Button testEventBtn =  (Button)findViewById(R.id.settingsForgotPasswordBtn);
         testEventBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ATLMockDB mockEvent = new ATLMockDB(getBaseContext());
			//	mockEvent.createClendarEvent();
				mockEvent.getAllEvents();    
				mockEvent.getEventByPrimaryWebEventId("LkLaDYeOTJ");
				//mockEvent.refreshUserCalendarEvents();
				mockEvent.respondToEvent("1cf9e4040b18b3f017df89e63c35afdb", "626750696261eace227e64d401084f55");
			//	mockEvent.bookAnEvent("gOSdxrtUKG", "5tIHAzaOw9");
//				mockEvent.deleteAllEvents();
			
			  
			}  
		});
         
         
//         2013-01-09 TAN: comment out to hinde the old tabbar # start        
//         ((RadioButton) findViewById(R.id.btnSettings)).setVisibility(View.VISIBLE);
//         RadioButton settingsBtn = (RadioButton)findViewById(R.id.btnSettings);
//         
//         settingsBtn.setChecked(true);
//         
//         settingsBtn.setTextColor(Color.BLACK);
//         setMainMenuListener();
//         2013-01-09 TAN: comment out to hinde the old tabbar # end       
       
       
       Button editProfileBtn = (Button)findViewById(R.id.settingsEditProfileBtn);
       editProfileBtn.setOnClickListener(
 				new View.OnClickListener() {
 					@Override
 					public void onClick(View v) {
 						Intent intentCalendar = new Intent(getBaseContext(), EditProfile.class);
 	                	intentCalendar.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
 	                	//intent.putExtra("EXTRA_SESSION_ID", sessionId);
 	                	startActivity(intentCalendar);
 					}
 				});  
        if (applicationController.isFacebookUser())
        	Session.openActiveSession(this, true);
        
        
        if (!message.equals(""))
        	alertUser("", message);
        
    }  
	
	
	
	
	
//	private void onClickFindFriends() {
//		
//		
//		
//		ArrayList<String> facebookFriends = FacebookUtilities.getFacebookFriends(this);
//		if (facebookFriends.size()>0)
//		{
//			
//			//AtlasFriendInfo	facebookFriend = facebookFriends.get(0);
//			
//			
//			for (String facebookFriendsId : facebookFriends) {
//				AtlasFriendInfo atlasFacebookFriend = parseConnect.isFacebookIdOnParse(facebookFriendsId);
//				if (atlasFacebookFriend!=null)
//					facebookAtlasFriends.add(atlasFacebookFriend);
//			}
//	
////			Toast.makeText(getApplicationContext(),"Frined id "+id+" name "+name,
////					Toast.LENGTH_LONG).show();
//		}
//		
//		applicationController.setAtlasFacebookFriends(facebookAtlasFriends);
//		
//		
//	}
	
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case (PICK_CONTACT) :
            if (resultCode == Activity.RESULT_OK) {
              Uri contactData = data.getData();
              Cursor cursor =  managedQuery(contactData, null, null, null, null);
              String name="";
              String phoneNumber="";
              String emailAddress="";  
              String contactId="";
              String key = "";
              if (cursor.moveToFirst()) {
            	   contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            	    key = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                  name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)); 

                  String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                  if ( hasPhone.equalsIgnoreCase("1"))
                      hasPhone = "true";
                  else
                      hasPhone = "false" ;

                  if (Boolean.parseBoolean(hasPhone)) 
                  {
                   Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);
                   while (phones.moveToNext()) 
                   {
                     phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                   }
                   phones.close();
                  }

                  // Find Email Addresses
                  Cursor emails = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId,null, null);
                  while (emails.moveToNext()) 
                  {
                   emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                  }
                  emails.close();
              }
              name = (name!=null)? name:"";
              phoneNumber = (phoneNumber!=null)? phoneNumber:"";
              emailAddress = (emailAddress!=null)? emailAddress:"";
              
             // final Bitmap photo = retrieveContactPhoto(id);
              Bitmap contactPhoto = null;
              byte[] photoArray = null;
              try {
                  InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
                          ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactId)));

                  if (inputStream != null) {
                	  contactPhoto = BitmapFactory.decodeStream(inputStream);
                 
                	  ByteArrayOutputStream stream = new ByteArrayOutputStream();
                	  contactPhoto.compress(Bitmap.CompressFormat.PNG, 100, stream);
                	  photoArray = stream.toByteArray();
                	  
                	  //   ImageView imageView = (ImageView) findViewById(R.id.img_contact);
                    //  imageView.setImageBitmap(photo);
                  }

                  assert inputStream != null;
                 if (inputStream != null)  inputStream.close();

              } catch (IOException e) {
                  e.printStackTrace();
              }
              
              
            //  Intent intentInvite = new Intent(getBaseContext(), InviteContact.class);
              Intent intentInvite = new Intent(getBaseContext(), ContactCard.class);
              intentInvite.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
              intentInvite.putExtra("contact_name", name);
              intentInvite.putExtra("contact_email", emailAddress);
              intentInvite.putExtra("contact_mobile", phoneNumber);
              intentInvite.putExtra("contact_photo", photoArray);
              intentInvite.putExtra("invite_type","InviteToAtlas");
          	  startActivity(intentInvite); 
              
              
          //    Intent i = new Intent(Intent.ACTION_VIEW);																																
              
            //  Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId); 
            //  i.setData(contactUri);
          //    startActivityForResult(i, PICK_INVITE_CONTACT);
              
              
             
          //    i.setData(Uri.parse(ContactsContract.Contacts.CONTENT_LOOKUP_URI + "/" + key));
         //     startActivityForResult(i, PICK_INVITE_CONTACT);
              
         
//              if (!phoneNumber.equals(""))
//            	  alertUser(name,phoneNumber);
//              
//              if (!emailAddress.equals(""))
//            	  inviteByEmail(emailAddress, name);
//            
            }
        break;
        case PICK_INVITE_CONTACT:
        	 if (resultCode == Activity.RESULT_OK) {
                 Uri contactData = data.getData();
                 Cursor cursor =  managedQuery(contactData, null, null, null, null);
        	 }
            break;
            case PICK_FRIENDS_ACTIVITY:
                String results = "";
                if (resultCode == RESULT_OK) {
                    AtlasApplication application = (AtlasApplication)getApplication();
                    Collection<GraphUser> selection = application.getSelectedUsers();
                    if (selection != null && selection.size() > 0) {
                        ArrayList<String> names = new ArrayList<String>();
                        for (GraphUser user : selection) {
                            names.add(user.getName());
                        }
                        results = TextUtils.join(", ", names);
                    } else {
                        results = "<No friends selected>";
                    }
                } else {
                    results = "<Cancelled>";
                }
            //    resultsTextView.setText(results);    
                break;
            default:
                Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
                break;
        }
    }
	
	@SuppressWarnings("deprecation")
	private void alertUser(String messageTitle, String message)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(
				Settings.this).create();

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
	
    private void onClickPickFriends() {
        Intent intent = new Intent(this, PickFriendsActivity.class);
        // Note: The following line is optional, as multi-select behavior is the default for
        // FriendPickerFragment. It is here to demonstrate how parameters could be passed to the
        // friend picker if single-select functionality was desired, or if a different user ID was
        // desired (for instance, to see friends of a friend).
        PickFriendsActivity.populateParameters(intent, null, true, true);
        startActivityForResult(intent, PICK_FRIENDS_ACTIVITY);
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
//               setCategoryinList(position, checkedId);
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
//                	//intent.putExtra("EXTRA_SESSION_ID", sessionId);
                	startActivity(intentNotes);
                	
                    break;
                case R.id.btnSettings:
//                	Intent intent = new Intent(getBaseContext(), Settings.class);
////                	//intent.putExtra("EXTRA_SESSION_ID", sessionId);
//                	startActivity(intent);
//               
                
                break;
                case R.id.btnToday:
					Intent intentToday = new Intent(getBaseContext(),

							ATLContactListActivity.class);
					intentToday.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					intentToday.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					// //intent.putExtra("EXTRA_SESSION_ID", sessionId);
					startActivity(intentToday);

            		
            	
                break;
            case R.id.btnToDo:
            	Intent intentTasks = new Intent(getBaseContext(), Tasks.class);
            	intentTasks.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            	//intent.putExtra("EXTRA_SESSION_ID", sessionId);
            	startActivity(intentTasks);
            
                break;
                default:
                
                	
                    break;
                }
            }
        };

        mainBtns.setOnCheckedChangeListener(rdGrpCheckedListener);

		
	}





	//===============================================================================
	// 2013-01-09 TAN: ATLTabBarViewDelegete Start Implement
	// ===============================================================================

	@Override
	public void didTabToTabIndex(int index) {
		// TODO Auto-generated method stub
		switch (index) {
		case ATLTabbarViewDelegete.TabbarIndex.tabbar_icon_contacts:
			Intent intentContact = new Intent(this,ATLContactListActivity.class);
			intentContact.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			intentContact.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// //intent.putExtra("EXTRA_SESSION_ID", sessionId);
			startActivity(intentContact);
			break;
		case ATLTabbarViewDelegete.TabbarIndex.tabbar_icon_calendar:
			Intent intentCalendar = new Intent(getBaseContext(),
					CalendarDayView.class);
			// intent.putExtra("EXTRA_SESSION_ID", sessionId);
			// 2012-11-08 RYAN TAN: add to clear to top
			intentCalendar.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

			intentCalendar.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intentCalendar);
			break;
		case ATLTabbarViewDelegete.TabbarIndex.tabbar_icon_home:
			// Will implement add new Contact here
			Intent intentCalendarAddEvent = new Intent(getBaseContext(),
					CalendarDayView.class);
			intentCalendarAddEvent.putExtra(ATLCalendarIntentKeys.CALL_CALLENDARDAYVIEW_MODE, 
					ATLCalendarIntentKeys.CALL_CALLENDARDAYVIEW_MODE_ADD_EVENT);
			
			intentCalendarAddEvent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

			intentCalendarAddEvent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intentCalendarAddEvent);
			break;
		case ATLTabbarViewDelegete.TabbarIndex.tabbar_icon_tasks:
			Intent intentTasks = new Intent(getBaseContext(), ATLTaskListActivity.class);
			intentTasks.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

			intentTasks.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// //intent.putExtra("EXTRA_SESSION_ID", sessionId);
			startActivity(intentTasks);
			break;
		case ATLTabbarViewDelegete.TabbarIndex.tabbar_icon_settings:
			
			break;
		}
	}

	// ===============================================================================
	// 2013-01-09 TAN: ATLTabBarViewDelegete End Implement
	// ===============================================================================
  
}