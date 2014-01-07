package atlasapp.section_settings;

import java.util.ArrayList;
import java.util.HashMap;

import atlasapp.database.AtlasServerConnect;
import atlasapp.database.DatabaseConstants.SIGN_NEW_USERS_CALLER;
import atlasapp.database.InviteToAtlasDelagateInterface;
import atlasapp.database.UserDelagateInterface;
import atlasapp.common.ATLUser;
import atlasapp.model.ATLContactModel;
import atlasapp.section_appentry.R;
import com.parse.ParseObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class InviteContact extends Activity implements UserDelagateInterface,InviteToAtlasDelagateInterface{ 
	
	private String name,email,mobile;
	private byte[] photo;
	private AtlasServerConnect parseConnect;
	private RelativeLayout atlasLayout;
	private RelativeLayout mobileLayout;
	private RelativeLayout emailLayout;
	private Button emailBtn;
	@Override 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_contact_info_disabled);
        parseConnect =   AtlasServerConnect.getSingletonObject(this);
        ImageButton btnCancel = (ImageButton)findViewById(R.id.cancelBtn);
        btnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(), Settings.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
				
			}
		});
        
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) 
        {
        	name = extras.getString("contact_name");
        	email = extras.getString("contact_email");
        	mobile = extras.getString("contact_mobile");
        	photo = extras.getByteArray("contact_photo");
        	
        }
        name = (name!=null && !name.equals(""))? name :"";
        if (!name.equals(""))
        {
        	TextView contactName = (TextView)findViewById(R.id.contactName);
        	contactName.setText(name);
        }
        
         atlasLayout = (RelativeLayout)findViewById(R.id.atlasLayout);
        atlasLayout.setVisibility(View.INVISIBLE);
         mobileLayout = (RelativeLayout)findViewById(R.id.mobileLayout);
         emailLayout = (RelativeLayout)findViewById(R.id.emailLayout);
        
        if (mobile!=null && !mobile.equals(""))  
        {
        	Button mobileBtn = (Button)findViewById(R.id.mobileBtn);
        	mobileBtn.setText(("mobile : "+mobile));
        	mobileBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					sendSMSAlert(name,mobile);
					
				}
			});
        	
        }else
        	mobileLayout.setVisibility(View.INVISIBLE);
        
        if (email!=null && !email.equals(""))
        {
        	 emailBtn = (Button)findViewById(R.id.emailBtn);
        	emailBtn.setText(("email : "+email));
        	
        	/// wait for getFriendEmailOnParse() call back
        	parseConnect.getFriendEmailOnParse(email);
        	
//        	if (atlasContact!=null && !atlasContact.getAtlasId().equals(""))
//        	{
//        		atlasLayout.setVisibility(View.VISIBLE);
//        	}
//        	
//        	
//        	emailBtn.setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					
//					inviteByEmail(email,name);
//					 
//				}
//			});
        	
        }else
        	emailLayout.setVisibility(View.INVISIBLE);
        
        
        if (photo!=null)
        {
        	ImageView contactPhoto = (ImageView)findViewById(R.id.contactImage);
        	Bitmap bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
        	contactPhoto.setImageBitmap(bitmap);
        }
        
        
	}
	
	
	protected void sendSMSAlert(String name, final String mobile) {
		final String messageInvite = "Hi "+name+"! I just discovered Atlas - a slick new mobile planner that makes it really easy to manage your time and collaborate with anyone. If you use it too, it'll make our lives a lot easier. Check it out, it's free - [For iPhone] [For Android]";
//		the new mobile planner, "
//				+"so you too will be able to collaborate with anyone on any platform with calendars, tasks, notes and contacts. ";
//				
				
		 final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		    dialog.setMessage("Enter Holla message");
		    dialog.setTitle("Send SMS");
		    dialog.setMessage("Invite "+name+" to Atlas ");
		    dialog.setCancelable(false);
		    dialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int id) {
		        	sendSMS(mobile,messageInvite);
		        }
		    });
		    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) {
		        }
		    });
		    AlertDialog alert = dialog.create();
		    alert.show();
		
	}
	private void sendSMS(String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(
        		InviteContact.this, 0, new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(
        		InviteContact.this, 0, new Intent(DELIVERED), 0);
        final String messageRecieved;
        // ---when the SMS has been sent---final String string =
        // "deprecation";
        registerReceiver(new BroadcastReceiver() {
        	
            @Override
            public void onReceive(Context arg0, Intent arg1) {
            	String message = "";
                switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(InviteContact.this, "SMS sent",  
                            Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Toast.makeText(InviteContact.this,
                            "Generic failure", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    Toast.makeText(InviteContact.this, "No service",
                            Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    Toast.makeText(InviteContact.this, "Null PDU",
                            Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    Toast.makeText(InviteContact.this, "Radio off",
                            Toast.LENGTH_SHORT).show();
                    break;

                }
            }
        }, new IntentFilter(SENT));

        // ---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(InviteContact.this,
                            "SMS delivered", Toast.LENGTH_SHORT).show();
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(InviteContact.this,
                            "SMS not delivered", Toast.LENGTH_SHORT)
                            .show();
                    break;
                }
                
                
                
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, "Invitation to Atlas", sentPI,
                deliveredPI);

    }

//	private void inviteByEmail(String email, String name) {
//		
//		// wait for getInviteSuccessCallBack() 
//		 generateInviteID(email, name);
//		
//		
//		
//		           
//	}
	
//	private void generateInviteID(String email, String name) {
//		String inviteID = "";
//		String phoneNum = (mobile!=null && mobile!="")? mobile :"";
//		
//		parseConnect.inviteToAtlas(AtlasAndroidUser.getParseUserID(),email,name, phoneNum);
//		
//		
//		//return inviteID;
//	}


	@Override
	public void getFriendEmailOnParse(
			ATLContactModel friendsPropertiesOnParseByEmail, boolean success) {
		if (friendsPropertiesOnParseByEmail!=null && !friendsPropertiesOnParseByEmail.getAtlasId().equals(""))
    	{
    		atlasLayout.setVisibility(View.VISIBLE);
    	}
    	
    	
    	emailBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			//	inviteByEmail(email,name);
				 
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
	public void getInviteSuccessCallBack(String inviteObjectId, boolean success) {
		
		if (!success || inviteObjectId==null || (inviteObjectId!=null && inviteObjectId.equals("")))
			return;
		String url = "getatlas.com/reply/invite_to_atlas_mobile.html?id="+inviteObjectId;
		Html.fromHtml("<a href='http://"+url+"'>http://"+url+"</a>");
	      
		String messageInvite = "Hey "+ name+" , I just discovered Atlas and thought you'd like to check it out. \n" +
				"It's a really slick new mobile planner that makes it really easy to manage your time and collaborate " +
				"with friends and colleagues. \n It makes stressful things like scheduling an event with someone really simple " +
				"and easy. \n No more endless back-and-forth emails to find the best time to meet up. And it's a calendar, task list, " +
				"and notebook -- all in one. "

				+"[Oh… and it syncs with iCal, Google Cal, Yahoo, Hotmail, and Exchange]."

				+"\n Just check it out when you get a sec -- it's free and definitely worth a look."
				+"iPhone app or Android app. ";
				
				  
				
		String    subjectRext = "Invite to Atlas";
		    
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
 
        emailIntent.setType("plain/text");
   
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ email});

        
    //    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,email);
 
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subjectRext);
 
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, messageInvite+Html.fromHtml("<a href='http://"+url+"'>http://"+url+"</a>"));
        
        

        InviteContact.this.startActivity(Intent.createChooser(emailIntent, "Send mail..."));

		
		
		
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
