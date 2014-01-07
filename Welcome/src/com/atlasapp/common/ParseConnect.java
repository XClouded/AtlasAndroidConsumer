package com.atlasapp.common;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Timer;

import org.json.JSONObject;

import android.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Base64;
import android.widget.Toast;


import com.atlasapp.model.ATLContactModel;
import com.atlasapp.section_settings.FriendsFinder;
import com.atlasapp.section_settings.LoadingFriends;
import com.atlasapp.section_settings.Settings;
import com.facebook.GraphUser;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SignUpCallback;
import com.parse.facebook.Facebook;

public class ParseConnect {
	
	
	private Handler  mHandler = new Handler();;
	private static Activity activity;
	private static ParseInstallation myInstallation;
	private ParseConnect()
    {
        // no code req'd
    }

    public static ParseConnect getSingletonObject(Activity currentActivity)
    {
    	
    	
    	activity = (currentActivity!=null)? currentActivity:activity;
      if (parseConnect == null)
      {
    	  parseConnect = new ParseConnect();
    	// Obtain the installation object for the current device
    	
      }
      return parseConnect;
    }

    private static ParseConnect parseConnect;
	
	private  LongOperation longOperation;

	protected SERVER_MESSAGE updateSuccess;

	protected static ArrayList<String> currentAllUsersFBFriendsOnAtlasID;

	protected static  ArrayList<ATLContactModel> allCommonAtlasUsers;
	
	
	
	public static void setParseInstallation()
	{
		  myInstallation = ParseInstallation.getCurrentInstallation();
	}
	
	public static enum USER_INFO { USER_NAME,  PASSWORD, EMAIL, OTHER }
	public static enum SERVER_MESSAGE {
		   FAIL, LOGGED_OUT, EMAIL_OR_USER_NAME_INVALID, SUCCESS, LOGGED_IN, EMAPY_VALUE
		 }
	private static ParseUser parseUser;
	private static void setParseUser(ParseUser currentParseUser) { parseUser = currentParseUser; }
	    public static ParseUser getParseUser() { return parseUser; }
	    
	    
	    
	    private static String  parseUserUserName;
	    private static void setParseUserUserName(String currentParseUserUserName) { parseUserUserName = currentParseUserUserName; }
		public static String getParseUserUserName() { return parseUserUserName; } 
		    
		    
		private static String  parseUserUserNameDisplay;
		private static void setParseUserUserNameDisplay(String currentParseUserUserNameDisplay) { parseUserUserNameDisplay = currentParseUserUserNameDisplay; }
	    public static String getParseUserUserNameDisplay() { return parseUserUserNameDisplay; } 
	  
	    private static String  parseUserUserObjectId;
	    private static void setParseUserUserObjectId(String currentParseUserUserObjectId) { parseUserUserObjectId = currentParseUserUserObjectId; }
	    public static String getParseUserUserObjectId() { return parseUserUserObjectId; }  
				  
				    
				    
	    private static String  parseUserUserEmail;
	    private static void setParseUserUserEmail(String currentParseUserUserEmail) { parseUserUserEmail = currentParseUserUserEmail; }
		public static String getParseUserUserEmail() { return parseUserUserEmail; } 
		    
	    private static String  parseUserUserFBID;
	    private static void setParseUserUserFBID(String currentParseUserUserFBID) { parseUserUserFBID = currentParseUserUserFBID; }
		public static String getParseUserUserFBID() { return parseUserUserFBID; } 
						    
		 private static Bitmap  parseUserUserImage;
		 private static void setParseUserUserImage(Bitmap currentParseUserUserImage) { parseUserUserImage = currentParseUserUserImage; }
	     public static Bitmap getParseUserUserImage() { return parseUserUserImage; } 
	      
	     
	     private static String  parseUserUserPassword;
	     private static void setParseUserUserPassword(String currentParseUserUserPassword) { parseUserUserPassword = currentParseUserUserPassword; }
	     public static String getParseUserUserPassword() { return parseUserUserPassword; }
						    
			
	     public static ParseUser defaultUser;
	     public static boolean userSignInSuccessfuly= false;
	     private static String  parseUserUserAceessToken;
	     private static void setParseUserUserAceessToken(String currentParseUserUserAceessToken) { parseUserUserAceessToken = currentParseUserUserAceessToken; }
	     public static String getParseUserUserAceessToken() { return parseUserUserAceessToken; }
				
						    
	
	   private static boolean signOnServer=false;
	   private static void setSignOnServer(boolean currentSignOnServer) { signOnServer = currentSignOnServer; }
	   public static boolean isSignOnServer() { return signOnServer; }
	    
	/**
	 * Create a new user on Parse from user 
	 * info updated on AtlasAndroidUser object,  return parse user 
	 * ID (ObjectId) and update the userSignOnServer accordingly
	 * @return
	 */ 
	public  String createNewAtlasUser()
	{
		String userCreatedSuccess = "";
		
		
		defaultUser = new ParseUser();
		defaultUser.setUsername(AtlasAndroidUser.getEmail()); 
		defaultUser.setPassword(AtlasAndroidUser.getAtlasPassword());
		defaultUser.setEmail(AtlasAndroidUser.getEmail());
		
		defaultUser.put("displayname", AtlasAndroidUser.getUserNameDisplay());
		defaultUser.put("fbID", AtlasAndroidUser.getfbID()); 
		defaultUser.put("access_token", AtlasAndroidUser.getAccessToken());
		//////TO DO (TRANSFORM TO FILE BEFORE ....)
	//	defaultUser.put("picture", AtlasAndroidUser.getPicture());
	//	defaultUser.put("loggedIn", AtlasAndroidUser.isLoggedIn());
		defaultUser.put("passwordCopy", AtlasAndroidUser.getAtlasPassword());
		
		
		//defaultUser = user;
		
//		try {
		
//		mHandler.post(new Runnable() {
//            @Override
//            public void run() {
            	longOperation = new LongOperation();
  			  longOperation.execute("register");
//            }
//        });
//		
		
		 
			  
			  
			  
			while (connecting)
				{
				 try {
				      Thread.sleep(2000);
				      
				      Toast.makeText(activity.getApplicationContext(), "Creating A new Atlas account...",
	       	    			Toast.LENGTH_SHORT).show();
				      
				      
				   } catch (InterruptedException e) {
				      // TODO Auto-generated catch block
				      e.printStackTrace();
				   }
				   continue;
				}
			//  defaultUser.signUp();
//			setSignOnServer(true);
			
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	
		    
	//	String userId = user.getObjectId();
		if (isSignOnServer())
		{
			
			setParseUser(defaultUser);
			setParseUserInfoFromServer();
			
			userCreatedSuccess = defaultUser.getObjectId();
			AtlasAndroidUser.setParseUserID(userCreatedSuccess);
		}
		else 
			userCreatedSuccess = "";
		return userCreatedSuccess;
		
	}
	
	private  void setParseUserInfoFromServer() {
		if (parseUser!=null)
		{
			setParseUserUserEmail(parseUser.getEmail());
			setParseUserUserName(parseUser.getUsername());
			setParseUserUserObjectId(parseUser.getObjectId());
			setParseUserUserAceessToken(parseUser.getString("access_token"));
			setParseUserUserFBID(parseUser.getString("fbID"));
			setParseUserUserNameDisplay(parseUser.getString("displayname"));
			setParseUserUserPassword(parseUser.getString("passwordCopy"));
			
			byte[] b = parseUser.getBytes("picture");
			if (b!=null)
			{
			 Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, parseUser.getBytes("picture").length);
			 
			 
			
			 
			 
			
			 String  encodedImage = Base64.encodeToString( parseUser.getBytes("picture"), Base64.DEFAULT);
			
			
			
			
			setParseUserUserImage(bitmap);
			}
		}
	}
	/**
	 * Update AtlasAndroidUser Object Info from Parse Server   
	 */
	public  boolean setAtlasAndroidUserFromServer(final String userName, final String password)
	{
		
		if (parseUser==null)
		{
			
			
//			 mHandler.post(new Runnable() {
//                 @Override
//                 public void run() {
                	 longOperation = new LongOperation();
       			  longOperation.execute("signin",userName,password);
//                 }
//             });
			
			
			 
//			  if (userSignInSuccessfuly)
//				  this.setParseUser(currentTestedUser);
			  
			  
       			while (connecting)
				{
				 try {
				      Thread.sleep(2000);
				      Toast.makeText(activity.getApplicationContext(), "Signing In...",
		       	    			Toast.LENGTH_SHORT).show();
				   } catch (InterruptedException e) {
				      // TODO Auto-generated catch block
				      e.printStackTrace();
				   }
				   continue;
				}
//				
			
		}
		// checking since signin might have failed
	
			// parseUser = ParseUser.getCurrentUser();
				setSignOnServer(parseUser!=null);
		
//		//ParseUser user = new ParseUser();
//		try {
//			parseUser = (parseUser==null)
//			 ParseUser.logIn(userName, password);
//			 parseUser = ParseUser.getCurrentUser();
//			setSignOnServer(true);
//		
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		
		if (isSignOnServer())
			setAtlasAndroidUserObject(parseUser);
			
		
		
		
		return isSignOnServer();
	}
	/**
	 * Log the user to the server according to 
	 * sign-in user name and password
	 * update the ParseConncet parse user 
	 * properties from the server and set the 
	 * userSignOnServer flag accordingly 
	 * @param userName
	 * @param password
	 * @return true if on server
	 */
	public  void logUserToServer(final String userName, final String password)
	{
		boolean onServer = false;
		
		
//		 mHandler.post(new Runnable() {
//             @Override
//             public void run() {
            	 longOperation = new LongOperation();
     			userSignInSuccessfuly = false;
//     			connecting = true;
     			longOperation.execute("signin",userName, password );
//             }
//         });
//		try {
			
			// while (connecting);
     			while (connecting)
				{
				 try {
				      Thread.sleep(2000);
				      Toast.makeText(activity.getApplicationContext(), "Signing In...",
		       	    			Toast.LENGTH_SHORT).show();
				   } catch (InterruptedException e) {
				      // TODO Auto-generated catch block
				      e.printStackTrace();
				   }
				   continue;
				}
			 
			  
			
		//	parseUser = ParseUser.logIn(userName, password);
//			onServer = (ParseUser.getCurrentUser()!=null);
//			setParseUserInfoFromServer();
////		} catch (ParseException e1) {
////			// TODO Auto-generated catch block
////			e1.printStackTrace();
////		}
//		
//		setSignOnServer(onServer);
//		AtlasAndroidUser.setUserSignonServer(onServer);
//		setSignOnServer(onServer);
		//return onServer;
	}
	
	/**
	 * Subscribe to a channel ,to enable push notification
	 * @param context
	 * @param channel
	 * @param activity the Activity to be run when a user responds to notifications on this channel.
	 */
	public void subscribedToChannels(Context context,String channel,Class<? extends Activity> activity)
	{
		PushService.subscribe(context, channel, activity);
	}
	/**
	 * UN Subscribe to a channel ,to disable push notification
	 * @param context
	 * @param channel
	 */
	public void unSubscribedToChannels(Context context,String channel)
	{
		PushService.unsubscribe(context, channel);
	}
	/**
	 * Get the set of channels that the current device is subscribed to
	 * @param context
	 * @return
	 */
	public Set<String> getSubscriptions(Context context)
	{
		return PushService.getSubscriptions(context);
	}
	/**
	 * Push Notification made in background to the list of channels
	 * @param channel 
	 * @param alertMessage
	 */
	public void pushNotification(LinkedList<String> channels, String alertMessage)
	{
		
//		JSONObject data = new JSONObject("{\"action\": \"com.atlasapp.common.PUSH_NOTIFICATION\","
//                \"name\": \"Vaughn\",
//                \"newsItem\": \"Man bites dog\""}));
//		
		  
		
		ParsePush push = new ParsePush();
		push.setChannels(channels); 
		push.setMessage(alertMessage);
		push.sendInBackground();
	}
	/**
	 * Try to update user info on the server according to key value (case its not empty)
	 * given 
	 * connecting with login information on the current PraseUser
	 * @param key
	 * @param value   
	 * @return 
	 * @return SERVER_MESSAGE type  
	 */
	public  SERVER_MESSAGE  updateAtlasUserInfoOnServer(HashMap<String, String> userAtlasInfoUpdtes)
	{
		 updateSuccess = SERVER_MESSAGE.FAIL;
		
		updateSuccess = (userAtlasInfoUpdtes.isEmpty())?  SERVER_MESSAGE.EMAPY_VALUE :updateSuccess;
		
		
		if (!updateSuccess.equals(SERVER_MESSAGE.EMAPY_VALUE))
		{
		  
//			ParseUser user;
//			try {
//				ParseUser.logIn(AtlasAndroidUser.getUserName(),AtlasAndroidUser.getAtlasPassword());
//				updateSuccess = SERVER_MESSAGE.LOGGED_IN;
//			} catch (ParseException e) {
//				updateSuccess = SERVER_MESSAGE.LOGGED_OUT;
//				e.printStackTrace();
//			}
			
			if (parseUser==null)
				logUserToServer(AtlasAndroidUser.getEmail(), AtlasAndroidUser.getAtlasPassword());
				
			
			
			updateSuccess = (parseUser!=null)? SERVER_MESSAGE.LOGGED_IN:SERVER_MESSAGE.LOGGED_OUT; 
			
			if (updateSuccess.equals(SERVER_MESSAGE.LOGGED_IN))
			{
			//	user = ParseUser.getCurrentUser();
				USER_INFO userInfo;
				
				Iterator userInfoToUpdate = userAtlasInfoUpdtes.keySet().iterator();
				
				/// Going through the list of user info updates updates 
				while(userInfoToUpdate.hasNext())
				{
				    String key=(String)userInfoToUpdate.next();
				    String value=(String)userAtlasInfoUpdtes.get(key);
				    
				    
				    userInfo = (key.equals("email"))? USER_INFO.EMAIL:
						(key.equals("password"))? USER_INFO.PASSWORD :
						 USER_INFO.OTHER;
				    
				    if (!value.equals(""))
				    	switch (userInfo)
				    	{
							case PASSWORD:
								parseUser.setPassword(value);
								parseUser.put("passwordCopy", value);
								break;
						
							case EMAIL:
								parseUser.setEmail(value);
								parseUser.setUsername(value);// setting the user name as email
								break;
							default:
								parseUser.put(key, value);
					
								break;
				    	}
				
				}
				
				
				
				// 

//				 mHandler.post(new Runnable() {
//		             @Override
//		             public void run() {
		            	 longOperation = new LongOperation();
		 				userSignInSuccessfuly = false;
//		 				connecting = true;
		 				longOperation.execute("save");
//		             }
//		         });
//				
		 				while (connecting)
						{
						 try {
						      Thread.sleep(2000);
						      Toast.makeText(activity.getApplicationContext(), "Storing User information on database...",
				       	    			Toast.LENGTH_SHORT).show();
						   } catch (InterruptedException e) {
						      // TODO Auto-generated catch block
						      e.printStackTrace();
						   }
						   continue;
						}
			}
		}
		return updateSuccess;
	}
	
	
	/**
	 * Gets the current mobile ParseUser from Parse
	 *  sets the AtlasAndroidUser object from DB and sets the SignOnServer flag to true
	 * @return
	 */
	public  boolean setCurrentAtlasAndroidUserFromServer() {
		boolean suceess =false;
		//ParseUser user = ParseUser.getCurrentUser();
		suceess = (parseUser!=null);
		if (suceess)
		{
			setAtlasAndroidUserObject(parseUser);
		}
		return suceess;
			
	}
	private  void setAtlasAndroidUserObject(ParseUser user)
	{
		setParseUser(user);
		setParseUserInfoFromServer();   
		AtlasAndroidUser.setEmail(parseUserUserEmail);
		AtlasAndroidUser.setfbID(parseUserUserFBID);
		AtlasAndroidUser.setParseUserID(parseUserUserObjectId);
		//AtlasAndroidUser.setUserName(parseUserUserName);
		AtlasAndroidUser.setAccessToken(parseUserUserAceessToken);
		AtlasAndroidUser.setPicture(parseUserUserImage);
		AtlasAndroidUser.setAtlasPassword(parseUserUserPassword);
		AtlasAndroidUser.setUserNameDisplay(parseUserUserNameDisplay);
		//AtlasAndroidUser.setLoggedIn(true);
		AtlasAndroidUser.setUserSignonServer(true);
	}

	/**
	 * Searching whether A  user is found on 
	 * Parse by email address 
	 * @param email
	 * @return username and atlas password for that Parse user
	 */ 
	public  HashMap<String, String> getUserEmailOnParse( String email) {
		boolean onParse = false;
		
		userParseLogin = new HashMap<String, String>();
		
		
//		mHandler.post(new Runnable() {
//            @Override
//            public void run() {
            	longOperation = new LongOperation();
        		userSignInSuccessfuly = false;
//        		connecting = true;
        		longOperation.execute("find_login",email );
//            }
//        });
//		
//		
//	
        		while (connecting)
				{
				 try {
				      Thread.sleep(2000);
				      Toast.makeText(activity.getApplicationContext(), "Confirm log in information ...",
		       	    			Toast.LENGTH_SHORT).show();
				   } catch (InterruptedException e) {
				      // TODO Auto-generated catch block
				      e.printStackTrace();
				   }
				   continue;
				}
		
		return userParseLogin;
	}
	
	/**
	 * Updating users fbID and access_token saved on local profile 
	 * on the server side and setting the SignOnServer flag accordingly
	 */
	public  void updateFacebookUseruser(HashMap<String, String > facebookInfo) 
	{
		if (!AtlasAndroidUser.getfbID().equals("")&& !AtlasAndroidUser.getAccessToken().equals(""))
		{
		
			//HashMap<String, String > facebookInfo = new HashMap<String,String>();
			facebookInfo.put("fbID", facebookInfo.get("fbID"));
			facebookInfo.put("access_token",facebookInfo.get("access_token"));
		
			updateAtlasUserInfoOnServer(facebookInfo);
		}
		
	}
	/**
	 * Updating users fbID and access_token saved on local profile 
	 * on the server side according to AtlasAndroidUser and setting the SignOnServer flag accordingly
	 */
	public  void updateFacebookUseruser() 
	{
		if (!AtlasAndroidUser.getfbID().equals("")&& !AtlasAndroidUser.getAccessToken().equals(""))
		{
		
			HashMap<String, String > facebookInfo = new HashMap<String,String>();
			facebookInfo.put("fbID", AtlasAndroidUser.getfbID());
			facebookInfo.put("access_token", AtlasAndroidUser.getAccessToken());
		
		
			updateAtlasUserInfoOnServer(facebookInfo);
		}
		
	}
	public void saveProfileImage(byte[] image)
	{
		currentFileSaved = new ParseFile("image.jpg", image);
		longOperation = new LongOperation();
		userSignInSuccessfuly = false;  
//		connecting = true;
//		
//		mHandler.post(new Runnable() {
//            @Override
//            public void run() {
            	longOperation.execute("save_file");
//            }
//        });
//		
//		
		
            	while (connecting)
				{
				 try {
				      Thread.sleep(2000);
				      Toast.makeText(activity.getApplicationContext(), "Storing information ...",
		       	    			Toast.LENGTH_SHORT).show();
				   } catch (InterruptedException e) {
				      // TODO Auto-generated catch block
				      e.printStackTrace();
				   }
				   continue;
				}
		
	}
	public byte[] getImage()
	{
		
//		mHandler.post(new Runnable() {
//            @Override
//            public void run() {
            	longOperation = new LongOperation();
        		userSignInSuccessfuly = false;
//        		connecting = true;
        		longOperation.execute("get_picture");
//            }
//        });
//		
//		
		
        		while (connecting)
				{
				 try {
				      Thread.sleep(2000);
				      Toast.makeText(activity.getApplicationContext(), "Retrieving picture...",
		       	    			Toast.LENGTH_SHORT).show();
				   } catch (InterruptedException e) {
				      // TODO Auto-generated catch block
				      e.printStackTrace();
				   }
				   continue;
				}
		
		
		return profilePic;
		
	}
	/**
	 * Finding whether friend's facebook id on parse and 
	 * if does return the friends info from server
	 * @param fbID friend's facebook id
	 * @return facebook friend's info from atlas sever
	 */
	public  ATLContactModel getFacebookIdOnParse(String fbID)
	{
		facebookAtlasFriend = null;
		
	
		longOperation = new LongOperation();


		longOperation.execute("find_fb",fbID);


		while (connecting)
		{
		 try {
		      Thread.sleep(2000);
		      Toast.makeText(activity.getApplicationContext(), "Searching facebook atlas friend...",
     	    			Toast.LENGTH_SHORT).show();
		   } catch (InterruptedException e) {
		      // TODO Auto-generated catch block
		      e.printStackTrace();
		   }
		   continue;
		}
		
		
		
		
		return facebookAtlasFriend;
	}
	
	
	/**
	 * Return all the current Facebook users on Atlas Server DB (Parse)
	 * @return ArrayList of all Facebook Atlas users each in a ATLContactModel type
	 */
	public  ArrayList<ATLContactModel> getAllFBAtlasUsers()
	{
		allFacebookAtlasUsers = new ArrayList<ATLContactModel>();
		allFacebookAtlasUsersFBID = new ArrayList<String>();
		
	//	progressDialog = ProgressDialog.show(activity, "", "Loading. Please wait...", true);
//		 progressDialog = new ProgressDialog(activity);
//		    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//		    progressDialog.setMessage("Loading. Please wait...");
//		//    progressDialog.setMax(100);
//		    progressDialog.setProgress(0);
//		    progressDialog.show();
//		   
		
		longOperation = new LongOperation();


		longOperation.execute("find_all_fb");

	//	int i=0;
		
		while (connecting)
		{
		 try {
			 
//			 if (i % 5 == 0) {
//                 progressDialog.setProgress(i);
//             }
		      Thread.sleep(2000);
		      Toast.makeText(activity.getApplicationContext(), "Searching for Atlas Facebook friends...",
     	    			Toast.LENGTH_SHORT).show();
		//      i++;
		   } catch (InterruptedException e) {
		      // TODO Auto-generated catch block
		      e.printStackTrace();
		   }
		   continue;
		}
		  
	//	progressDialog.dismiss();
		return allFacebookAtlasUsers;
	}  
	/**
	 * Checcking the list of emails given on Parse and
	 * retuning a list of ATLContactModel objects for 
	 * user's email's that are on Parse (Atlas Users)
	 * @param newABContactsEmailAdd
	 * @return
	 */
	public ArrayList<ATLContactModel> getAtlasNewFriendsByEmail(
			ArrayList<String> newABContactsEmailAdd) {
		
		allCommonAtlasUsers = new ArrayList<ATLContactModel>();
		
		longOperation = new LongOperation();

		emailsToCheck = newABContactsEmailAdd;
		
		longOperation.execute("find_all_email");


		while (connecting)
		{
		 try {
		      Thread.sleep(2000);
		      Toast.makeText(activity.getApplicationContext(), "Searching friends by usernames ...",
     	    			Toast.LENGTH_SHORT).show();
		   } catch (InterruptedException e) {
		      // TODO Auto-generated catch block
		      e.printStackTrace();
		   }
		   continue;
		}
		
		
		return allCommonAtlasUsers;
	}
	
	public void showToast(final String toast)
	{
	    activity.runOnUiThread(new Runnable() {
	        public void run()
	        {
	            Toast.makeText(activity, toast, Toast.LENGTH_SHORT).show();
	        }
	    });
	}
	
	
	public ArrayList<ATLContactModel> getAllUsersByFBID(
			ArrayList<String> allUsersFBFriendsOnAtlasID) {
		
		
//		activity.runOnUiThread(new Runnable() {
//		    public void run() {
//		    	 Toast.makeText(activity.getApplicationContext(), "Search for Facebook friends ...",
//			    			Toast.LENGTH_SHORT).show();
//		    }
//		});
//		
		 
		allCommonAtlasUsers = new ArrayList<ATLContactModel>();
		
		longOperation = new LongOperation();

		currentAllUsersFBFriendsOnAtlasID = allUsersFBFriendsOnAtlasID;
		
		longOperation.execute("find_all_fb_by_id");


		while (connecting)
		{
		 try {
		      Thread.sleep(100);
//		      Toast.makeText(activity.getApplicationContext(), "Search for Facebook friends ...",
//     	    			Toast.LENGTH_SHORT).show();
		   } catch (InterruptedException e) {
		      // TODO Auto-generated catch block
		      e.printStackTrace();
		   }
		   continue;
		}
		
		
		return allCommonAtlasUsers;
	}
	
	/**
	 * Must be called after getAllFBAtlasUsers() been called at least once
	 * and populate the allFacebookAtlasUsersFBID with all the current facebook atlas
	 * users' facebook id's
	 * NO actual call to the Parse DB been done
	 * @return all current facebook atlas users' facebook id's
	 */
	public  ArrayList<String> getAllFBAtlasUsersID()
	{
		if (allFacebookAtlasUsersFBID==null)
			allFacebookAtlasUsersFBID = new ArrayList<String>();
		
		return allFacebookAtlasUsersFBID;
	} 
	
	/**
	 * Finding whether friend's email on parse and 
	 * if does return the friends info from server
	 * @param friend's email address
	 * @return  friend's info from atlas sever
	 */
	public  ATLContactModel getFriendEmailOnParse(String email)
	{
		friendAtlasFriend = null;
		
	
		longOperation = new LongOperation();


		longOperation.execute("find_friend_email",email);


		while (connecting)
		{
		 try {
		      Thread.sleep(2000);
		      Toast.makeText(activity.getApplicationContext(), "Finding friends on Database...",
     	    			Toast.LENGTH_SHORT).show();
		   } catch (InterruptedException e) {
		      // TODO Auto-generated catch block
		      e.printStackTrace();
		   }
		   continue;
		}
		
		
		
		
		return friendAtlasFriend;
	}
	public ParseObject inviteToAtlas(String inviterId, String inviteeEmail, String inviteeName, String phone) {
		inviteToAtlas = null;
		
		
		longOperation = new LongOperation();


		longOperation.execute("invite_to_atlas",inviterId,  inviteeEmail,  inviteeName, phone);


		while (connecting)
		{
		 try {
		      Thread.sleep(2000);
		      Toast.makeText(activity.getApplicationContext(), "Invite a friend to Atlas...",
     	    			Toast.LENGTH_SHORT).show();
		   } catch (InterruptedException e) {
		      // TODO Auto-generated catch block
		      e.printStackTrace();
		   }
		   continue;
		}
		
		
		
		
		return inviteToAtlas;
	}
	public ParseUser getParseUserByEmail(String email) {
		byEmailUser = null;
		
		
		longOperation = new LongOperation();


		longOperation.execute("find_by_email",email);


		while (connecting)
		{
		 try {
		      Thread.sleep(2000);
		      Toast.makeText(activity.getApplicationContext(), "Searching friends on atlas...",
     	    			Toast.LENGTH_SHORT).show();
		   } catch (InterruptedException e) {
		      // TODO Auto-generated catch block
		      e.printStackTrace();
		   }
		   continue;
		}
		
		
		
		
		return byEmailUser;
	}
	protected ProgressDialog progressDialog;
	protected Timer timer;
	protected int seconds;
	protected ParseObject inviteToAtlas;
	protected static ArrayList<String> emailsToCheck;
	protected ParseUser byEmailUser;
	protected static ArrayList<String> allFacebookAtlasUsersFBID;
	protected static ATLContactModel facebookAtlasFriend,friendAtlasFriend;
	protected static ArrayList<ATLContactModel> allFacebookAtlasUsers;
	protected static byte[] profilePic;
	protected static ParseFile currentFileSaved;
	protected static HashMap<String, String> userParseLogin;
	public static HashMap<String, String> getLastSearchedUserParseLoginByEmail(){return userParseLogin;};
	public static Context currentContext;	
	public static boolean connecting  = false;
	public static enum OPERATION_METHOD { INVITE_TO_ATLAS,SIGNIN,FIND_FB,FIND_ALL_FB_BY_ID,FIND_ALL_EMAIL,FIND_FRIEND_EMAIL,FIND_ALL_FB,  REGISTER, FIND,FIND_LOGIN, SAVE, SAVE_FILE, GET_picture, SIGN_IN_NEW, FIND_BY_EMAIL}
	public static ParseUser currentTestedUser;
	private  class LongOperation extends AsyncTask<String, Integer, String> {

		
		
		
		@Override
		protected String doInBackground(String... params) {
			 connecting = true;
			String[] operation = params;
			userSignInSuccessfuly = false;
			OPERATION_METHOD method = (operation[0].equals("signin"))? OPERATION_METHOD.SIGNIN:
									(operation[0].equals("invite_to_atlas"))? OPERATION_METHOD.INVITE_TO_ATLAS:
									  (operation[0].equals("register"))?OPERATION_METHOD.REGISTER:
								      (operation[0].equals("find_login"))?OPERATION_METHOD.FIND_LOGIN:
								      (operation[0].equals("save"))?OPERATION_METHOD.SAVE:
								    	  (operation[0].equals("find_fb"))?OPERATION_METHOD.FIND_FB:
								    		  (operation[0].equals("find_all_fb"))?OPERATION_METHOD.FIND_ALL_FB:
									    		  (operation[0].equals("find_by_email"))?OPERATION_METHOD.FIND_BY_EMAIL:
									    			  (operation[0].equals("find_all_email"))?OPERATION_METHOD.FIND_ALL_EMAIL:
								    			  (operation[0].equals("find_all_fb_by_id"))?OPERATION_METHOD.FIND_ALL_FB_BY_ID:
								    		  (operation[0].equals("find_friend_email"))?OPERATION_METHOD.FIND_FRIEND_EMAIL:
								    	  (operation[0].equals("save_file"))?OPERATION_METHOD.SAVE_FILE:
								    		  (operation[0].equals("get_picture"))?	 OPERATION_METHOD.GET_picture:
								    			  OPERATION_METHOD.SIGN_IN_NEW	  ;
			switch (method)
			{
			case SIGN_IN_NEW:
				try {
					parseUser.signUp();
					setSignOnServer(true);
					userSignInSuccessfuly = true;
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			case SIGNIN:
				  
				try {
					
					currentTestedUser = ParseUser.logIn(operation[1], operation[2]);
					setSignOnServer(true);
					userSignInSuccessfuly = true;
					parseUser = currentTestedUser;
					setSignOnServer(true);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				break;
			case REGISTER:
				try {
					defaultUser.signUp();
					setSignOnServer(true);
					userSignInSuccessfuly = true;
					parseUser = defaultUser;
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;	
				
				
			case INVITE_TO_ATLAS:
				inviteToAtlas = new ParseObject("inviteToAtlas");
				ParseACL defaultACL = new ParseACL();
				
				defaultACL.setPublicReadAccess(true);
				defaultACL.setPublicWriteAccess(true);
				
				inviteToAtlas.put("inviterID", operation[1]);
				inviteToAtlas.put("email",  operation[2]);
				inviteToAtlas.put("name",  operation[3]);
				inviteToAtlas.put("phone",  operation[4]);
				
				inviteToAtlas.setACL(defaultACL);

				try {
					inviteToAtlas.save();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				break;
				
				
				
			case FIND_LOGIN:
	
			ParseQuery query = ParseUser.getQuery();
			query.whereEqualTo("email", operation[1]);
			ParseUser user = null;
//			ParseQuery query = new ParseQuery("Users");
//			query.whereEqualTo("email", email);
			try {
				List<ParseObject> list = query.find();
				if (list!=null && list.size()==1)
					user = (ParseUser)list.get(0); 
				 
				if (user!=null) 
				{
					userParseLogin.put("email", user.getUsername());
					userParseLogin.put("passwordCopy", user.get("passwordCopy").toString());
					userParseLogin.put("object_id", user.getObjectId());
					signOnServer = true;
				}
				
				
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				break;
			case FIND_FB:
				ParseQuery fb_query = ParseUser.getQuery();
				fb_query.whereEqualTo("fbID", operation[1]);
				ParseUser facebookUser = null;
//				ParseQuery query = new ParseQuery("Users");
//				query.whereEqualTo("email", email);
				try {
					List<ParseObject> list = fb_query.find();
					if (list!=null && list.size()==1)
						facebookUser = (ParseUser)list.get(0); 
					if (facebookUser!=null) 
					{
						//facebookAtlasFriend.setDisplayName(facebookUser.getString("userNameDisplay"));
						facebookAtlasFriend.setFirstname(facebookUser.getString("displayname"));
						facebookAtlasFriend.setLastname("");
						facebookAtlasFriend.setEmailAddress(facebookUser.getEmail());
						facebookAtlasFriend.setFacebookId(operation[1]);
						facebookAtlasFriend.setAtlasId(facebookUser.getObjectId());
						facebookAtlasFriend.setFromFacebook(true);
						
						ParseFile image = (ParseFile)parseUser.get("picture");
						if (image!=null)
						{
							try {
								byte[] profilePic = image.getData();
								if (profilePic!=null)
								{
									Bitmap bitmap = BitmapFactory.decodeByteArray(profilePic, 0, profilePic.length);
									facebookAtlasFriend.setImage(bitmap);
								}
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
						
						
				//		facebookAtlasFriend.setPicture(currentpicture)(facebookUser.getString("userNameDisplay"));

						
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			
				
				
				
			case FIND_ALL_FB:
				ParseQuery all_fb_query =ParseUser.getQuery();
				all_fb_query.whereExists("fbID");
				String[] empty = {""};
				all_fb_query.whereNotContainedIn("fbID",Arrays.asList(empty));
//				ParseQuery query = new ParseQuery("Users");
//				query.whereEqualTo("email", email);
				try {
					List<ParseObject> allFacebookAtlasUsersObjects = all_fb_query.find();
					if (allFacebookAtlasUsersObjects!=null && allFacebookAtlasUsersObjects.size()>0)
					{
						allFacebookAtlasUsers = new ArrayList<ATLContactModel>();
						allFacebookAtlasUsersFBID = new ArrayList<String>();
						for (ParseObject fbAtlasUser :allFacebookAtlasUsersObjects)
						{
							ATLContactModel ATLContactModel = new ATLContactModel();
							ATLContactModel.setAddressBookId("");
							ATLContactModel.setAtlasId(fbAtlasUser.getObjectId());
							ATLContactModel.setEmailAddress(fbAtlasUser.getString("email"));
							ATLContactModel.setFacebookId(fbAtlasUser.getString("fbID"));
							ATLContactModel.setFirstname(fbAtlasUser.getString("displayname"));
							ATLContactModel.setFromFacebook(true);
							ATLContactModel.setLastname(fbAtlasUser.getString("displayname"));
							
							ParseFile image = (ParseFile)fbAtlasUser.get("picture");
							if (image!=null)
							{
								try {
									byte[] profilePic = image.getData();
									if (profilePic!=null)
									{
										Bitmap bitmap = BitmapFactory.decodeByteArray(profilePic, 0, profilePic.length);
							
										ATLContactModel.setImage(bitmap);
										
									}
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
							allFacebookAtlasUsers.add(ATLContactModel);
							allFacebookAtlasUsersFBID.add(fbAtlasUser.getString("fbID"));
						}
						
					}
					
						
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case FIND_ALL_FB_BY_ID:
				ParseQuery common_fb_query =ParseUser.getQuery();
				String[] array = currentAllUsersFBFriendsOnAtlasID.toArray(new String[currentAllUsersFBFriendsOnAtlasID.size()]);
				common_fb_query.whereContainedIn("fbID",Arrays.asList(array));
//				ParseQuery query = new ParseQuery("Users");
//				query.whereEqualTo("email", email);
				try {
					
					List<ParseObject> allCommonFacebookAtlasUsersObjects = common_fb_query.find();
					if (allCommonFacebookAtlasUsersObjects!=null && allCommonFacebookAtlasUsersObjects.size()>0)
					{
						
						allCommonAtlasUsers = new ArrayList<ATLContactModel>();
						for (ParseObject fbAtlasUser :allCommonFacebookAtlasUsersObjects)
						{
							ATLContactModel ATLContactModel = new ATLContactModel();
							ATLContactModel.setAddressBookId("");
							ATLContactModel.setAtlasId(fbAtlasUser.getObjectId());
							ATLContactModel.setEmailAddress(fbAtlasUser.getString("email"));
							ATLContactModel.setFacebookId(fbAtlasUser.getString("fbID"));
							ATLContactModel.setFirstname(fbAtlasUser.getString("displayname"));
							ATLContactModel.setFromFacebook(true);
							ATLContactModel.setLastname("");
						
							ParseFile image = (ParseFile)fbAtlasUser.get("picture");
							if (image!=null)
							{
								try {
									byte[] profilePic = image.getData();
									if (profilePic!=null)
									{
										Bitmap bitmap = BitmapFactory.decodeByteArray(profilePic, 0, profilePic.length);
							
										ATLContactModel.setImage(bitmap);
										
									}
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
							allCommonAtlasUsers.add(ATLContactModel);
						}
						
					}
					
						
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;	
			case FIND_BY_EMAIL:
				ParseQuery email_query = ParseUser.getQuery();
				email_query.whereEqualTo("email", operation[1]);
				byEmailUser = null;
				try {
					List<ParseObject> list = email_query.find();
					if (list!=null && list.size()==1)
					{
						byEmailUser = (ParseUser)list.get(0); 
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				break;

			case FIND_FRIEND_EMAIL:
				ParseQuery friend_query = ParseUser.getQuery();
				friend_query.whereEqualTo("email", operation[1]);
				ParseUser friendUser = null;
//				ParseQuery query = new ParseQuery("Users");
//				query.whereEqualTo("email", email);
				
				friendAtlasFriend = new ATLContactModel();
				friendAtlasFriend.setAtlasId("");
				
				
				try {
					List<ParseObject> list = friend_query.find();
					if (list!=null && list.size()==1)
					{
						friendUser = (ParseUser)list.get(0); 
						friendAtlasFriend = new ATLContactModel();
						//friendAtlasFriend.setDisplayName(friendUser.getString("userNameDisplay"));
						friendAtlasFriend.setFirstname(friendUser.getString("displayname"));
						friendAtlasFriend.setLastname("");
						friendAtlasFriend.setEmailAddress(friendUser.getEmail());
						friendAtlasFriend.setFacebookId(friendUser.getString("fbID"));
						friendAtlasFriend.setAtlasId(friendUser.getObjectId());
						friendAtlasFriend.setFromFacebook(!friendUser.getString("fbID").equals(""));
						
						ParseFile image = (ParseFile)friendUser.get("picture");
						if (image!=null)
						{
							try {
								byte[] profilePic = image.getData();
								if (profilePic!=null)
								{
									Bitmap bitmap = BitmapFactory.decodeByteArray(profilePic, 0, profilePic.length);
									friendAtlasFriend.setImage(bitmap);
								}
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
						
						
				//		facebookAtlasFriend.setPicture(currentpicture)(facebookUser.getString("userNameDisplay"));

						
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				break;
				
				
			case FIND_ALL_EMAIL:
				ParseQuery emails_fb_query =ParseUser.getQuery();
				String[] emailsArray = emailsToCheck.toArray(new String[emailsToCheck.size()]);
				emails_fb_query.whereContainedIn("email",Arrays.asList(emailsArray));
//				ParseQuery query = new ParseQuery("Users");
//				query.whereEqualTo("email", email);
				try {
					List<ParseObject> allCommonEmails = emails_fb_query.find();
					if (allCommonEmails!=null && allCommonEmails.size()>0)
					{
						allCommonAtlasUsers = new ArrayList<ATLContactModel>();
						for (ParseObject emailAtlasUser :allCommonEmails)
						{
							ATLContactModel ATLContactModel = new ATLContactModel();
							ATLContactModel.setAddressBookId("");
							ATLContactModel.setAtlasId(emailAtlasUser.getObjectId());
							ATLContactModel.setEmailAddress(emailAtlasUser.getString("email"));
							ATLContactModel.setFacebookId(emailAtlasUser.getString("fbID"));
							ATLContactModel.setFirstname(emailAtlasUser.getString("displayname"));
							ATLContactModel.setFromFacebook(true);
							ATLContactModel.setLastname("");
							
							ParseFile image = (ParseFile)emailAtlasUser.get("picture");
							if (image!=null)
							{
								try {
									byte[] profilePic = image.getData();
									if (profilePic!=null)
									{
										Bitmap bitmap = BitmapFactory.decodeByteArray(profilePic, 0, profilePic.length);
							
										ATLContactModel.setImage(bitmap);
										
									}
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
							allCommonAtlasUsers.add(ATLContactModel);
						}
						
					}
					
						
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
				
			case SAVE: 
				try {
					parseUser.save();
					updateSuccess = SERVER_MESSAGE.SUCCESS;
					setParseUserInfoFromServer();
					signOnServer = true;
				} catch (ParseException e) {
					updateSuccess = SERVER_MESSAGE.FAIL;
					
					e.printStackTrace();
				}
				break;
			case SAVE_FILE:
				try {
					currentFileSaved.save();
					parseUser.put("picture", currentFileSaved);
					parseUser.save();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case GET_picture:
				ParseFile image = (ParseFile)parseUser.get("picture");
				if (image!=null)
				{
					try {
						profilePic = image.getData();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
				
			}
		//	progressDialog.dismiss();
			connecting = false; 
			
			
			return null;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(String result) {
			// execution of result of Long time consuming operation
			//connecting = false;
			
		
		//	progressDialog.dismiss();
			connecting = false;
				
		}
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			connecting  = true;
//			 Toast.makeText(activity.getBaseContext(), "Search for Facebook friends ...",
//		    			Toast.LENGTH_SHORT).show();  
		//	progressDialog = ProgressDialog.show(activity, "", "Loading. Please wait...", true);
			seconds = 0;
//			   seconds = Math.max(1, SyncProcessor.secondsForBackup());
//	            timer = new Timer();
//	            progressDialog = new ProgressDialog(currentContext);
//	            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//	            progressDialog.setMessage("Backing Up...");
//	            progressDialog.setMax(10 * seconds);
//	            progressDialog.show();
			
			
		
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
		 */
		@Override
		protected void onProgressUpdate(Integer... progress) {
			
			
			
	      // Things to be done while execution of long running operation is in progress. For example updating ProgessDialog
		//	progressDialog.setProgress(progress[0]);
		
		}
	}
	public boolean isAtlasUserByEmail(String email) {
		// TODO Auto-generated method stub
		return false;
	}

	

	

	
	
	
	

}
