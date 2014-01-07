package com.atlasapp.atlas_database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.atlasapp.atlas_database.DatabaseConstants.OPERATION_METHOD;
import com.atlasapp.atlas_database.DatabaseConstants.SERVER_MESSAGE;
import com.atlasapp.atlas_database.DatabaseConstants.SIGN_NEW_USERS_CALLER;
import com.atlasapp.atlas_database.DatabaseConstants.TABLE_ALERTS;
import com.atlasapp.common.ATLConstants.AlertType;
import com.atlasapp.common.ATLConstants.TASK_CATEGORY;
import com.atlasapp.common.ATLCurrentsUserAlerts;
import com.atlasapp.common.AtlasAndroidUser;
import com.atlasapp.common.DateTimeUtilities;
import com.atlasapp.common.SendEmailActivity;
import com.atlasapp.model.ATLAlertModel;
import com.atlasapp.model.ATLContactModel;
import com.atlasapp.section_alerts.AlertCellData;
import com.atlasapp.section_appentry.AtlasApplication;
import com.atlasapp.section_calendar.ATLCalCellData;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;

public class AtlasServerConnect implements ParseDBCallBackInterface {
	
	

	
	private final String[] TABLES_ASSIGN = {"TaskAssignNew","EventInviteNew"};
	private final String[] TABLES_ACCEPT = {"TaskAcceptNew","EventAcceptNew"};
	
	private ArrayList<ATLAlertModel> userEventALerts;
	private ArrayList<ATLAlertModel> userTaskAlerts;
	
	
	public static AtlasApplication application; 
	////Server properties

	public static User parseUser;
	public static void setParseUser(User currentParseUser) { parseUser = currentParseUser; }
    public static User getParseUser() { return parseUser; }
    
    
    
    public static String  parseUserUserName;
    public static void setParseUserUserName(String currentParseUserUserName) { parseUserUserName = currentParseUserUserName; }
	public static String getParseUserUserName() { return parseUserUserName; } 
	    
	    
	public static String  parseUserUserNameDisplay;
	public static void setParseUserUserNameDisplay(String currentParseUserUserNameDisplay) { parseUserUserNameDisplay = currentParseUserUserNameDisplay; }
    public static String getParseUserUserNameDisplay() { return parseUserUserNameDisplay; } 
  
    public static String  parseUserUserObjectId;
    public static void setParseUserUserObjectId(String currentParseUserUserObjectId) { parseUserUserObjectId = currentParseUserUserObjectId; }
    public static String getParseUserUserObjectId() { return parseUserUserObjectId; }  
			  
			    
			    
    public static String  parseUserUserEmail;
    public static void setParseUserUserEmail(String currentParseUserUserEmail) { parseUserUserEmail = currentParseUserUserEmail; }
	public static String getParseUserUserEmail() { return parseUserUserEmail; } 
	    
	public static String  parseUserUserFBID;
	public static void setParseUserUserFBID(String currentParseUserUserFBID) { parseUserUserFBID = currentParseUserUserFBID; }
	public static String getParseUserUserFBID() { return parseUserUserFBID; } 
					    
	public static Bitmap  parseUserUserImage;
	public static void setParseUserUserImage(Bitmap currentParseUserUserImage) { parseUserUserImage = currentParseUserUserImage; }
     public static Bitmap getParseUserUserImage() { return parseUserUserImage; } 
      
      
     public static String  parseUserUserPassword;
     public static void setParseUserUserPassword(String currentParseUserUserPassword) { parseUserUserPassword = currentParseUserUserPassword; }
     public static String getParseUserUserPassword() { return parseUserUserPassword; }
					    
		
     public static ParseUser defaultUser;
     public static boolean userSignInSuccessfuly= false;
     public static String  parseUserUserAceessToken;
     public static void setParseUserUserAceessToken(String currentParseUserUserAceessToken) { parseUserUserAceessToken = currentParseUserUserAceessToken; }
     public static String getParseUserUserAceessToken() { return parseUserUserAceessToken; }
			
					    

     public static boolean signOnServer=false;
     public static void setSignOnServer(boolean currentSignOnServer) { signOnServer = currentSignOnServer; }
     public static boolean isSignOnServer() { return signOnServer; }
	
	private static UserDelagateInterface listener;
	/////
	private static ParseInstallation myInstallation;
	public static Activity activity;
	private static AtlasServerConnect atlasServerConnect;
	private static AtlasApplication applicationController;
	private ArrayList<String> allFacebookAtlasUsersFBID;
	private ATLCurrentsUserAlerts currentUserAlerts;
	private HashMap<String, ATLContactModel> allCurrentSessionUsersOnAtlas;
	private InviteToAtlas inviteToAtlas;
	
	private AtlasServerConnect()   
	{
		
	}
	
	public static AtlasServerConnect getSingletonObject(Activity currentActivity)
	{
		  activity = (currentActivity!=null)? currentActivity:activity;
	      if (atlasServerConnect == null)
	      {
	    	  atlasServerConnect = new AtlasServerConnect();
	    	// 
	    	
	      }
	      
	      return atlasServerConnect;
	}
	/**
	 * Obtain the installation object for the current device
	 */
	public static void setParseInstallation()
	{
		  myInstallation = ParseInstallation.getCurrentInstallation();
	}
	
	
	/**
	 * Create a new user on Parse from user 
	 * info updated on AtlasAndroidUser object,  return parse user 
	 * ID (ObjectId) and update the userSignOnServer accordingly
	 * @return
	 */ 
	public  void createNewAtlasUser(UserDelagateInterface listener)
	{
		parseUser = new User();
		parseUser.userDelagator = listener;
		parseUser.register();
		
		//setSignOnServer(!AtlasAndroidUser.getParseUserID().equals(""));
	
		//return AtlasAndroidUser.getParseUserID();
		
	}
	private static  void setParseUserInfoFromServer() {
		if (parseUser!=null)
		{
			setParseUserUserEmail(parseUser.getEmail());
			setParseUserUserName(parseUser.getUsername());
			setParseUserUserObjectId(parseUser.getObjectId());
			setParseUserUserAceessToken(parseUser.getString("access_token"));
			setParseUserUserFBID(parseUser.getString("facebook_id"));
			setParseUserUserNameDisplay(parseUser.getString("first_name"));
			setParseUserUserPassword(parseUser.getString("password_copy"));
			
			
			parseUser.getFile("picture");
			
			
//			byte[] b = 
//			if (b!=null)
//			{
//			 Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);      
//			// String  encodedImage = Base64.encodeToString( parseUser.getBytes("picture"), Base64.DEFAULT);
//			 setParseUserUserImage(bitmap);
//			}
		}
	}
	
	
	/**
	 * Update AtlasAndroidUser Object Info from Parse Server   
	 */
	public  void setAtlasAndroidUserFromServer()
	{
		if (parseUser!=null)
			setAtlasAndroidUserObject(parseUser);
		
//		if (parseUser==null)
//		{
//			
//			parseUser = new User();
//			parseUser.signIn(userName, password);
//			
//		}
		// checking since signin might have failed
//	
//				setSignOnServer(parseUser!=null);
//		
//		if (isSignOnServer())
//			setAtlasAndroidUserObject(parseUser);
//		
//		return isSignOnServer();
	}
	/**
	 * Set All Local AtlasAndroidUser Object properties from Server properties 
	 * @param user
	 */
	public static  void setAtlasAndroidUserObject(User user)
	{
		setParseUser(user);
		setParseUserInfoFromServer();   
		AtlasAndroidUser.setEmail(parseUserUserEmail);
		AtlasAndroidUser.setfbID(parseUserUserFBID);
		AtlasAndroidUser.setParseUserID(parseUserUserObjectId);
		AtlasAndroidUser.setAccessToken(parseUserUserAceessToken);
		AtlasAndroidUser.setPicture(parseUserUserImage);
		AtlasAndroidUser.setAtlasPassword(parseUserUserPassword);
		AtlasAndroidUser.setUserNameDisplay(parseUserUserNameDisplay);
		AtlasAndroidUser.setUserSignonServer(true);
	}
	

	/**
	 * Log the user to the server according to 
	 * sign-in user name and password
	 * update the ParseConncet parse user 
	 * properties from the server and set the 
	 * userSignOnServer flag accordingly 
	 * @param userName
	 * @param password
	 * 
	 */
	public  void logUserToServer(final String userName, final String password,UserDelagateInterface listener )
	{
		
		parseUser = new User();
		parseUser.userDelagator = listener;
		parseUser.signIn(userName, password);
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
	 * 
	 */
	public static void pushNotification(LinkedList<String> channels, String alertMessage, JSONObject data)
	{
		
//		JSONObject data = new JSONObject("{\"action\": \"com.atlasapp.common.PUSH_NOTIFICATION\","
//                \"name\": \"Vaughn\",
//                \"newsItem\": \"Man bites dog\""}));
//		
		  
		
		ParsePush push = new ParsePush();
		push.setChannels(channels); 
		push.setMessage(alertMessage);
		push.setPushToIOS(true);  

		push.setData(data);
		push.sendInBackground();  
//		try {
//			push.send();
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
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
	public  SERVER_MESSAGE  updateAtlasUserInfoOnServer(HashMap<String, Object> userAtlasInfoUpdtes,UserDelagateInterface listener)
	{
		SERVER_MESSAGE updateSuccess = SERVER_MESSAGE.FAIL;
		
		updateSuccess = (userAtlasInfoUpdtes.isEmpty())?  SERVER_MESSAGE.EMAPY_VALUE :updateSuccess;
		
		
		if (!updateSuccess.equals(SERVER_MESSAGE.EMAPY_VALUE))
		{
			parseUser.userDelagator = listener;
			if (parseUser==null)
				logUserToServer(AtlasAndroidUser.getEmail(), AtlasAndroidUser.getAtlasPassword(),listener);
				
			
			
			updateSuccess = (parseUser!=null)? SERVER_MESSAGE.LOGGED_IN:SERVER_MESSAGE.LOGGED_OUT; 
			
			parseUser.put(userAtlasInfoUpdtes,listener);
			updateSuccess  = parseUser.updateTableMessage ;
		}
		return updateSuccess;
	}
	
	/**
	 * Gets the current mobile ParseUser from Parse
	 *  sets the AtlasAndroidUser object from DB and sets the SignOnServer flag to true
	 * @return
	 */
	public  void setCurrentAtlasAndroidUserFromServer() {
		boolean suceess =false;
		suceess = (parseUser!=null);
		if (suceess)
		{
			setAtlasAndroidUserObject(parseUser);
		}
	//	return suceess;
			
	}
	
	/**
	 * Searching whether A  user is found on 
	 * Parse by email address 
	 * @param email
	 * @return username and atlas password for that Parse user
	 */ 
	public void getUserEmailOnParse( String email, UserDelagateInterface listener) {
		
		HashMap<String, Object> userParseLogin = new HashMap<String, Object>();
		userParseLogin.put("email", email);
		ParseUser user=null;
		parseUser = new User();
		parseUser.parseQuery = ParseUser.getQuery();
		parseUser.userDelagator = listener;
		parseUser.findWhereEqualTo(userParseLogin,OPERATION_METHOD.FIND_USER_EMAIL);
//		if (list!=null && list.size()==1)
//			user = (ParseUser)list.get(0); 
//		 
//		if (user!=null) 
//		{
//			userParseLogin.put("email", user.getUsername());
//			userParseLogin.put("passwordCopy", user.get("passwordCopy").toString());
//			userParseLogin.put("object_id", user.getObjectId());
//			signOnServer = true;
//		}
		
		
	//	return userParseLogin;
	}
	
	/**
	 * Updating users fbID and access_token saved on local profile 
	 * on the server side and setting the SignOnServer flag accordingly
	 */
	public  void updateFacebookUseruser(HashMap<String, Object> facebookInfo,UserDelagateInterface listener) 
	{
		if (!AtlasAndroidUser.getfbID().equals("")&& !AtlasAndroidUser.getAccessToken().equals(""))
		{
		
			//HashMap<String, Object > facebookInfo = new HashMap<String,Object>();
			facebookInfo.put("facebook_id", facebookInfo.get("facebook_id"));
			facebookInfo.put("access_token",facebookInfo.get("access_token"));
		
			updateAtlasUserInfoOnServer(facebookInfo,  listener);
		}
		
	}
	/**
	 * Updating users fbID and access_token saved on local profile 
	 * on the server side according to AtlasAndroidUser and setting the SignOnServer flag accordingly
	 */
	public  void updateFacebookUseruser(UserDelagateInterface listener) 
	{
		if (!AtlasAndroidUser.getfbID().equals("")&& !AtlasAndroidUser.getAccessToken().equals(""))
		{
		
			HashMap<String, Object> facebookInfo = new HashMap<String,Object>();
			facebookInfo.put("facebook_id", AtlasAndroidUser.getfbID());
			facebookInfo.put("access_token", AtlasAndroidUser.getAccessToken());
		
		
			updateAtlasUserInfoOnServer(facebookInfo,listener);
		}
		
	}
	public boolean isFacebookAtlasUser(String email, String pass) {
		User user = new User();
		user.signIn(email, pass);
		String fbId = user.getString("facebook_id");
		boolean fbUser =(user!=null && fbId!=null && !fbId.equals(""));
		return fbUser;
	}
	private HashMap<String, String> nonAtlasUsersByEmail;
	HashMap<String,ATLContactModel> newCurrentNonAtlasUserToSign = new HashMap<String,ATLContactModel>();
	ArrayList<ATLContactModel> currentNonAtlasUserToSign;
	SIGN_NEW_USERS_CALLER currentSignNewcaller;
	int index=0;
	UserDelagateInterface signNewlistener;
	public void signNonAtlasUsers(SIGN_NEW_USERS_CALLER caller, ArrayList<ATLContactModel> newNonAtlasUserToSign, UserDelagateInterface listener)
	{
		currentNonAtlasUserToSign = newNonAtlasUserToSign;
		signNewlistener = listener;
		currentSignNewcaller = caller;
		if (newNonAtlasUserToSign!=null )
		{
			User user  = new User();
			
//			for (ATLContactModel invitee:newNonAtlasUserToSign)
//			{
				if (newNonAtlasUserToSign.size()>0 && index<newNonAtlasUserToSign.size() && newNonAtlasUserToSign.get(index)!=null && 
						newNonAtlasUserToSign.get(index).getEmailAddress()!=null && !newNonAtlasUserToSign.get(index).getEmailAddress().equals(""))
				{
					user.parseCallBackDeleget = this;
					user.signInNewUser(newNonAtlasUserToSign.get(index).getEmailAddress()
							,newNonAtlasUserToSign.get(index).getFirstname());
					
				}
				else
				{
					index = 0;
					//newCurrentNonAtlasUserToSign = new HashMap<String,ATLContactModel>();
					listener.getSignNonAtlasUsersCallBack(caller, true, newCurrentNonAtlasUserToSign);

				}
		//	}
		}
		else
		{
			
			listener.getSignNonAtlasUsersCallBack(caller, true, newCurrentNonAtlasUserToSign);

		}

	
	}
	@Override
	public void signInNewFriendUserSuccess(boolean success, ParseUser user) {
		if (success)
		{
//		nonAtlasUsersByEmail.put(user.getString("email"), );
		currentNonAtlasUserToSign.get(index).setAtlasId(user.getObjectId());
		newCurrentNonAtlasUserToSign.put(user.getString("email"), currentNonAtlasUserToSign.get(index));
		
		index++;
		signNonAtlasUsers(currentSignNewcaller,currentNonAtlasUserToSign,signNewlistener);
	
		}
		else
		{
			signNewlistener.getSignNonAtlasUsersCallBack(currentSignNewcaller, false, newCurrentNonAtlasUserToSign);
		}
	}
	public void saveProfileImage(byte[] image, UserDelagateInterface listener)
	{
		if (parseUser!=null)
		{
			parseUser.userDelagator = listener;
			parseUser.saveImageFile("image.jpg", image);
		}
	}
//	public byte[] getImage()
//	{
//		
//		return parseUser.getFile("picture");
//		
//	}
	/**
	 * Finding whether friend's facebook id on parse and 
	 * if does return the friends info from server
	 * and stores the friend's picture in a background process on local 
	 * mobile atals pictures folder named after the friend's objectId
	 * @param fbID friend's facebook id
	 * @return facebook friend's info from atlas sever
	 */
	public void getFacebookIdOnParse(String fbID)
	{
		ATLContactModel facebookAtlasFriend = null;
		ParseUser facebookUser = null;
		parseUser = new User();
		HashMap<String,Object> facebookHash = new HashMap<String,Object>();
		facebookHash.put("facebook_id", fbID);
		
		parseUser.findWhereEqualTo(facebookHash, OPERATION_METHOD.FIND_FACEBOOK_ID);
//		if (list!=null && list.size()==1)
//			facebookUser = (ParseUser)list.get(0); 
//		if (facebookUser!=null && facebookUser.getEmail()!=null && !facebookUser.getString("email").equals("")
//				&&facebookUser.getString("passwordCopy")!=null && !facebookUser.getString("passwordCopy").equals("")) 
//		{
//			 
//		//	User user = new User(facebookUser);
//			//facebookAtlasFriend.setDisplayName(facebookUser.getString("userNameDisplay"));
//			facebookAtlasFriend.setFirstname(facebookUser.getString("displayname"));
//			facebookAtlasFriend.setLastname("");
//			facebookAtlasFriend.setEmailAddress(facebookUser.getEmail());
//			facebookAtlasFriend.setFacebookId(fbID);
//			facebookAtlasFriend.setAtlasId(facebookUser.getObjectId());
//			facebookAtlasFriend.setFromFacebook(true);
//			
//			
//			String friendsFilePictureName = facebookUser.getObjectId();
//			parseUser.saveFriendPictureFileInBackground(facebookUser, friendsFilePictureName, application);
//			
////			byte[] profilePic = user.getFile("picture");
////			if (profilePic!=null)
////			{
////				Bitmap bitmap = BitmapFactory.decodeByteArray(profilePic, 0, profilePic.length);
////				
////				facebookAtlasFriend.setImage(bitmap);
////			}	
//		
//			
//		}
//		return facebookAtlasFriend;
	}
	
	    
	
	
	
	
	/**
	 * Return all the current Facebook users on Atlas Server DB (Parse)
	 * @return ArrayList of all Facebook Atlas users each in a ATLContactModel type
	 * without pictures...
	 */
	public  void getAllFBAtlasUsers()
	{
		ArrayList<ATLContactModel> allFacebookAtlasUsers = new ArrayList<ATLContactModel>();
		allFacebookAtlasUsersFBID = new ArrayList<String>();
		
		parseUser = new User();
		//parseUser.findWhereExists("fbID",OPERATION_METHOD.FIND);
		String[] empty = {""};
		
	
		 parseUser.findWhereNotContainedIn("facebook_id", Arrays.asList(empty),OPERATION_METHOD.FIND_ALL_USERS);
//		if (allFacebookAtlasUsersObjects!=null && allFacebookAtlasUsersObjects.size()>0)
//		{
//			allFacebookAtlasUsers = new ArrayList<ATLContactModel>();
//			allFacebookAtlasUsersFBID = new ArrayList<String>();
//			for (ParseObject fbAtlasUser :allFacebookAtlasUsersObjects)
//			{
//				
//				if (fbAtlasUser.getString("email")!=null && !fbAtlasUser.getString("email").equals("") &&
//						fbAtlasUser.getString("passwordCopy")!=null && !fbAtlasUser.getString("passwordCopy").equals(""))
//				{
//				//User user = new User(fbAtlasUser);
//				
//				ATLContactModel atlContactModel = new ATLContactModel();
//				atlContactModel.setAddressBookId("");
//				atlContactModel.setAtlasId(fbAtlasUser.getObjectId());
//				atlContactModel.setEmailAddress(fbAtlasUser.getString("email"));
//				atlContactModel.setFacebookId(fbAtlasUser.getString("fbID"));
//				atlContactModel.setFirstname(fbAtlasUser.getString("displayname"));
//				atlContactModel.setFromFacebook(true);
//				atlContactModel.setLastname(fbAtlasUser.getString("displayname"));
//				 
//				
//				
////				ParseFile applicantResume = (ParseFile)fbAtlasUser.get("picture");
////				applicantResume.getDataInBackground(new GetDataCallback() {
////				  public void done(byte[] data, ParseException e) {
////				    if (e == null) {
////				      // data has the bytes for the resume
////				    	Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
////						atlContactModel.setImage(bitmap);
////				    } else {
////				      // something went wrong
////				    }
////				  }
////				});
////				
////				ParseFile applicantResume = (ParseFile)user.get("picture");
////				byte[] profilePic;
////				try {
////					profilePic = applicantResume.getData();
//				
//				
////				String friendsFilePictureName = fbAtlasUser.getObjectId();
////				parseUser.saveFriendPictureFileInBackground(fbAtlasUser, friendsFilePictureName, application);
////				
//					 
////					byte[] profilePic = user.getFile("picture");
////					if (profilePic!=null)
////					{
////						Bitmap bitmap = BitmapFactory.decodeByteArray(profilePic, 0, profilePic.length);
////						atlContactModel.setImage(bitmap);
////					}	
//////				} catch (ParseException e) {
//////					// TODO Auto-generated catch block
//////					e.printStackTrace();
//////				}
//
//			
//				
//				allFacebookAtlasUsers.add(atlContactModel);
//				allFacebookAtlasUsersFBID.add(fbAtlasUser.getString("fbID"));
//			}
//			}
//			
//		}
//		return allFacebookAtlasUsers;
	}
	
	
	/**
	 * Cheacking the list of emails given on Parse and
	 * returning a list of ATLContactModel objects for 
	 * user's email's that are on Parse (Atlas Users)
	 * and srores their picture in a background process on mobile
	 * local atlas picture folder
	 * @param newABContactsEmailAdd
	 * @return
	 */
	public void getAtlasNewFriendsByEmail(
			ArrayList<String> newABContactsEmailAdd) {
		parseUser = new User();
		ArrayList<ATLContactModel> allCommonAtlasUsers = new ArrayList<ATLContactModel>();
		String[] emailsArray = newABContactsEmailAdd.toArray(new String[newABContactsEmailAdd.size()]);
		parseUser.findWhereContainedIn("email",Arrays.asList(emailsArray),OPERATION_METHOD.FIND_COMMON);
	}
	
	/**
	 * Gets all the atlas users on Parse by FB_ID,
	 * in a ATLContactModel type , without pictures 
	 * @param allUsersFBFriendsOnAtlasID
	 * @return
	 */
	public void getAllUsersByFBID(
			ArrayList<String> allUsersFBFriendsOnAtlasID) {
		
		parseUser = new User();
		ArrayList<ATLContactModel> allCommonAtlasUsers = new ArrayList<ATLContactModel>();
		String[] array = allUsersFBFriendsOnAtlasID.toArray(new String[allUsersFBFriendsOnAtlasID.size()]);
		 parseUser.findWhereContainedIn("facebook_id",Arrays.asList(array),OPERATION_METHOD.FIND_ALL_FACEBOOK_USERS_FRIENDS);
		
		
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
	 * and stores the friend's picture in a background process
	 * on local mobile atlas folder
	 * @param friend's email address
	 * @return  friend's info from atlas sever
	 */
	public  void getFriendEmailOnParse(String email)
	{
		ATLContactModel friendAtlasFriend = null;
		ParseUser friendUser=null;
		parseUser = new User();
		HashMap<String,Object> loginHash = new HashMap<String,Object>();
		loginHash.put("email", email);
		parseUser.findWhereEqualTo(loginHash, OPERATION_METHOD.FIND_FRIEND_EMAIL);
		 
//		if (list!=null && list.size()==1)
//		{
//			
//			
//			friendUser = (ParseUser)list.get(0); 
//			if (friendUser.getString("email")!=null && !friendUser.getString("email").equals("") &&
//					friendUser.getString("passwordCopy")!=null && !friendUser.getString("passwordCopy").equals(""))
//			{
//			//User user = new User(friendUser);
//			friendAtlasFriend = new ATLContactModel();
//			friendAtlasFriend.setFirstname(friendUser.getString("displayname"));
//			friendAtlasFriend.setLastname("");
//			friendAtlasFriend.setEmailAddress(friendUser.getEmail());
//			friendAtlasFriend.setFacebookId(friendUser.getString("fbID"));
//			friendAtlasFriend.setAtlasId(friendUser.getObjectId());
//			friendAtlasFriend.setFromFacebook(!friendUser.getString("fbID").equals(""));
//			
//			
//			
//			String friendsFilePictureName = friendUser.getObjectId();
//			parseUser.saveFriendPictureFileInBackground(friendUser, friendsFilePictureName, application);
//			
////			byte[] profilePic = user.getFile("picture");
////			if (profilePic!=null)
////			{
////				Bitmap bitmap = BitmapFactory.decodeByteArray(profilePic, 0, profilePic.length);
////				friendAtlasFriend.setImage(bitmap);
////			}
//			}
//		}
//		
//		return friendAtlasFriend;
	}
	
	public void  inviteToAtlas(String inviterId, String inviteeEmail, 
			String inviteeName, String phone) {
		
		 inviteToAtlas = new InviteToAtlas();
		
		HashMap<String, Object> fieldsValues = new HashMap<String, Object>();
		
		
		fieldsValues.put("inviterID", inviterId);
		fieldsValues.put("email",  inviteeEmail);
		fieldsValues.put("name",  inviteeName);
		fieldsValues.put("phone",  phone);
		
		inviteToAtlas.put(fieldsValues,inviteToAtlas.inviteATLDelagator);
		
	//	return inviteToAtlas.getAtlasID(); 
	}
	
	
	
	
	/**
	 * LogIn the given user by userName and Password  
	 * Retrieve his picture from parse
	 * and store it on local atlas folder on a background process 
	 * @param userName
	 * @param password
	 * @param fileName
	 */
	public void retrieveUserFile(String userName, String fileName)
	{
		ParseQuery query = ParseUser.getQuery();
		query.whereEqualTo("email", userName);
		query.findInBackground(new FindCallback() {
		  public void done(List<ParseObject> list, ParseException e) {
		    if (e == null) {
		        // The query was successful.
		    	if (list!=null && list.size()==1)
				{
					ParseUser user = (ParseUser)list.get(0); 
					if (user!=null)
					{
						String friendsFilePictureName = user.getObjectId();
						
						storeUserPictureInBackground(user,friendsFilePictureName);
						
					}
				
				}
		    } else {
		        // Something went wrong.
		    }
		  }
		});
//		User user = new User(userName,password);
//		if (user!=null)
//		{
//			String friendsFilePictureName = user.getObjectId();
//			storeUserPictureInBackground(user,friendsFilePictureName);
//			
//		}
	}
	/**
	 * Retrieve the current user object picture from parse
	 * and store it on local atlas folder on a background process 
	 * @param friendsFilePictureName
	 * @param application
	 */
	public void storeUserPictureInBackground(ParseUser user,final String friendsFilePictureName
			) {
		if (user!=null){
		      ParseFile applicantResume = (ParseFile)user.get("picture");
		      if(applicantResume != null){
		    	  applicantResume.getDataInBackground(new GetDataCallback() {
					  public void done(byte[] data, ParseException e) {
					    if (e == null) {
					      // data has the bytes for the resume
					    	Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
							
					    	application.storeFriendPicture(friendsFilePictureName, bitmap);
					    } else {
					      // something went wrong
					    }
					  }
					});
		      }
				
		}
	}
	public void getParseUserByEmail(String email) {
		ParseUser byEmailUser = null;
		
		parseUser = new User();
		HashMap<String,Object> userEmail = new HashMap<String,Object>();
		userEmail.put("email", email);
		parseUser.findWhereEqualTo(userEmail,OPERATION_METHOD.FIND_USER_EMAIL );
		
//		if (list!=null && list.size()==1)
//		{
//			byEmailUser = (ParseUser)list.get(0); 
//		}
//		
//		
//		return byEmailUser;
	}
//	  TASK_MESSAGE = {
//	         Title : 0,
//	         Content: 1,
//	         Catagory :2,
//	         Deadline:3,
//	         Date :4,
//	         To:5
//	         
//	           
	
	public void taskAssign(ATLContactModel to, String title, String content,
			TASK_CATEGORY category, Date date, String lv_dateFormateInUTC)
	{
		TaskAssign taskAssignTable = new TaskAssign();
		if (to!=null && 
			((title!=null && !title.equals("")||(content!=null && !content.equals(""))))
			&& category!=null && date!=null && lv_dateFormateInUTC!=null && !lv_dateFormateInUTC.equals("")	)
		{
			
					taskAssignTable.assignFriend(to.getAtlasId(),to.getEmailAddress(), title, content, category, date,to.getFirstname(),lv_dateFormateInUTC);
					if (taskAssignTable.updateTableMessage.equals(SERVER_MESSAGE.SUCCESS))
					{
						if (to.getAtlasId()!=null && !to.getAtlasId().equals(""))
						{// assign atlas user (push notification)
							/// Task Request: Organizer's Name has assigned "Task Title" to you. Accept task?
						
							LinkedList<String> channels = new LinkedList<String>();
							String message =  AtlasAndroidUser.getUserNameDisplay()+" has assigned "+title+" to you. Accept task?";
							channels.add("ID"+to.getAtlasId());
							JSONObject data = new JSONObject();
							try {  
								data = new JSONObject("{\"alert\": \""+message+"\",\"badge\": \"Increment\",\"sound\": \"Incoming_Atlas_Push.mp3\"}");
							} catch (JSONException e) {
								// TODO Auto-generated catch block     
								e.printStackTrace();
							}
							if (data!=null && channels.size()>0 && message!=null && !message.equals(""))
								pushNotification(channels,message,data);
						  
						}
						else
						{// assign Non atlas user
							Intent intent = new Intent(activity.getBaseContext(),SendEmailActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
							intent.putExtra("name", to.getFirstname());
							intent.putExtra("atlasId", taskAssignTable.getAtlasID());
							intent.putExtra("item", "taskm");
//							intent.putExtra("emailAddress","sharonanachum@gmail.com");//to.getEmailAddress()
							intent.putExtra("emailAddress",to.getEmailAddress());//to.getEmailAddress()
							intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							activity.getBaseContext().startActivity(intent);   
						}
						
						
						//NGHIA save alert case we send task assign successfully
						AlertCellData alert = new AlertCellData();
						alert.isHandled = true;
						alert.isRead = true;
						alert.alertCellAtlasId = "RC"+taskAssignTable.getAtlasID();
						alert.isDisplayed = false;
						alert.alertCellMsgCreatedDate = DateTimeUtilities
								.toGMTDate(new Date());
						alert.alertCellModifiedDate = DateTimeUtilities
								.toGMTDate(new Date());
						alert.alertCellCreatedDate = DateTimeUtilities
								.toGMTDate(new Date());
						alert.currentType = AlertType.taskAssigned_Sent;
						alert.isSentMessageSuccessfully = true;
						alert.alertCellEventTitle = title;
						alert.alertCellEventLocation = content;
						alert.alertCellEventDuration = 0;
						alert.alertCellPreferredDatetime = date;
						
						alert.alertCellRecieverId = to.getAtlasId();
						alert.alertCellRecieverName = to.getFirstname()+" "+to.getLastname();
						alert.alertCellSenderId = AtlasAndroidUser.getParseUserID();
						alert.alertCellSenderName = AtlasAndroidUser.getUserNameDisplay();
						alert.isSentMessageSuccessfully = true;

						alert.save();
					}else{// NGHIA save alert case we can not send task
						AlertCellData alert = new AlertCellData();
						alert.isHandled = true;
						alert.isRead = true;
						alert.alertCellAtlasId = "RC"+taskAssignTable.getAtlasID();
						alert.isDisplayed = false;
						alert.alertCellMsgCreatedDate = DateTimeUtilities
								.toGMTDate(new Date());
						alert.alertCellModifiedDate = DateTimeUtilities
								.toGMTDate(new Date());
						alert.alertCellCreatedDate = DateTimeUtilities
								.toGMTDate(new Date());
						alert.currentType = AlertType.taskAssigned_Sent;
						alert.isSentMessageSuccessfully = true;
						alert.alertCellEventTitle = title;
						alert.alertCellEventLocation = content;
						alert.alertCellEventDuration = 0;
						alert.alertCellPreferredDatetime = date;
						
						alert.alertCellRecieverId = to.getAtlasId();
						alert.alertCellRecieverName = to.getFirstname()+" "+to.getLastname();
						alert.alertCellSenderId = AtlasAndroidUser.getParseUserID();
						alert.alertCellSenderName = AtlasAndroidUser.getUserNameDisplay();
						alert.isSentMessageSuccessfully = false;

						alert.save();
						
					}
		}
		  
		
	}   
	
	
	
	public SERVER_MESSAGE calendarInvite(String fromName, ATLContactModel to, String title, String location,
			  Date datePref,Date dateAlt1,Date dateAlt2, int duration,
			  String prefdateFormateInUTC,String altOnefdateFormateInUTC,String altTwofdateFormateInUTC)
	{
		SERVER_MESSAGE serverMessage = SERVER_MESSAGE.FAIL;
		CalendarInvite calendarInviteTable = new CalendarInvite();
		if (to!=null && 
			((title!=null && !title.equals("")&&(location!=null)))
			&& datePref!=null && dateAlt1!=null && 
			dateAlt2!=null &&
			prefdateFormateInUTC!=null && !prefdateFormateInUTC.equals("")	)
		{
			calendarInviteTable.inviteFriend(fromName,to.getAtlasId(), to.getEmailAddress(), title, 
					location, datePref, dateAlt1, dateAlt2, to.displayName(), duration, prefdateFormateInUTC, 
					altOnefdateFormateInUTC, altTwofdateFormateInUTC);
			
					if (calendarInviteTable.updateTableMessage.equals(SERVER_MESSAGE.SUCCESS))
					{
						if (to.getAtlasId()!=null && !to.getAtlasId().equals(""))
						{// event invite  atlas user (push notification)
							/// Organizer's Name has invited you to "Event Title". Confirm event?
						
							LinkedList<String> channels = new LinkedList<String>();
							String message =  fromName+" has invited you to "+title+". Confirm event?";
							channels.add("ID"+to.getAtlasId());
							JSONObject data = new JSONObject();
							try {  
								data = new JSONObject("{\"alert\": \""+message+"\",\"badge\": \"Increment\",\"sound\": \"Incoming_Atlas_Push.mp3\"}");
							} catch (JSONException e) {
								// TODO Auto-generated catch block     
								e.printStackTrace();
							}
							if (data!=null && channels.size()>0 && message!=null && !message.equals(""))
								pushNotification(channels,message,data);
						  
						}    
						else
						{// assign Non atlas user
							Intent intent = new Intent(activity.getBaseContext(),SendEmailActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
							intent.putExtra("fromName", fromName);
							intent.putExtra("name", to.getFirstname());
							intent.putExtra("atlasId", calendarInviteTable.getAtlasID());
							intent.putExtra("emailAddress",to.getEmailAddress());
							intent.putExtra("item", "eventm");
							intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							activity.getBaseContext().startActivity(intent);   
						}
						
						serverMessage = SERVER_MESSAGE.SUCCESS;
					}
		}
		return serverMessage;
		  
		
	}
	public SERVER_MESSAGE taskAssign(String toAtlasId, String toEmailAddress, String title,
			String content, TASK_CATEGORY category, Date date, String toFirstname, String lv_dateFormateInUTC)
	{
		SERVER_MESSAGE serverMessage = SERVER_MESSAGE.FAIL;
		TaskAssign taskAssignTable = new TaskAssign();
		if (toEmailAddress!=null && !toEmailAddress.equals("") &&
			((title!=null && !title.equals("")&&(content!=null)))
			&& date!=null &&
					lv_dateFormateInUTC!=null && !lv_dateFormateInUTC.equals("")	)
		{
			taskAssignTable.assignFriend(toAtlasId, toEmailAddress, title, content, category, date, toFirstname, lv_dateFormateInUTC);
			
					if (taskAssignTable.updateTableMessage.equals(SERVER_MESSAGE.SUCCESS))
					{
						if (toAtlasId!=null && !toAtlasId.equals(""))
						{// event invite  atlas user (push notification)
							/// Organizer's Name has invited you to "Event Title". Confirm event?
						
							LinkedList<String> channels = new LinkedList<String>();
							String message =  AtlasAndroidUser.getUserNameDisplay()+" has assigned you to "+title+". Confirm assignment?";
							channels.add("ID"+toAtlasId);
							JSONObject data = new JSONObject();
							try {  
								data = new JSONObject("{\"alert\": \""+message+"\",\"badge\": \"Increment\",\"sound\": \"Incoming_Atlas_Push.mp3\"}");
							} catch (JSONException e) {
								// TODO Auto-generated catch block     
								e.printStackTrace();
							}
							if (data!=null && channels.size()>0 && message!=null && !message.equals(""))
								pushNotification(channels,message,data);
						  
						}    
						else
						{// assign Non atlas user
							Intent intent = new Intent(activity.getBaseContext(),SendEmailActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
							intent.putExtra("fromName", AtlasAndroidUser.getUserNameDisplay());
							intent.putExtra("name", toFirstname);
							intent.putExtra("atlasId", taskAssignTable.getAtlasID());
							intent.putExtra("emailAddress",toEmailAddress);
							intent.putExtra("item", "taskm");
							intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							activity.getBaseContext().startActivity(intent);   
						}
						
						serverMessage = SERVER_MESSAGE.SUCCESS;
					}
		}
		return serverMessage;
		  
		
	}
	public void setAllUserAlerts()
	{
		ArrayList<ParseObject> newUserAlerts = new ArrayList<ParseObject>();
		
		for (String table:TABLES_ASSIGN)
		{
			AtlasServerTable alertTable = new AtlasServerTable();
			alertTable.setTableName(table);
			HashMap<String,Object> alertUnRead = new HashMap<String,Object>();
			alertUnRead.put("isRead", false);
			alertUnRead.put("fromID", AtlasAndroidUser.getParseUserID());
			
			
			alertTable.findWhereEqualTo(alertUnRead,OPERATION_METHOD.FIND);
//			if (alerts!=null && alerts.size()>0)
//				newUserAlerts.addAll(alerts);
		}
//		
//		return newUserAlerts;
		
	}
	
	/**
	 * Sets the ArrayList of ATLAlertModel of all the current un read 
	 * alerts from the corresponding talbe (alert : event/task)
	 * given and updates ATLCurrentsUserAlerts
	 * @param alert
	 */
	public void setAlerts(TABLE_ALERTS alert)
	{
		currentUserAlerts = ATLCurrentsUserAlerts.getSingletonObject(null);
		ArrayList<ATLAlertModel> newUserAlerts = new ArrayList<ATLAlertModel>();
		
			AtlasServerTable alertTable = new AtlasServerTable();
			alertTable.setTableName(alert.getTableName());
			HashMap<String,Object> alertUnRead = new HashMap<String,Object>();
			alertUnRead.put("isRead", false);
			
			
			if (alert.equals(TABLE_ALERTS.EVENT))
			alertUnRead.put("invitee", AtlasAndroidUser.getParseUserID());//"d1EJbNSFvv");//
		
			else
				alertUnRead.put("toID", AtlasAndroidUser.getParseUserID());//"d1EJbNSFvv");//

			//alertUnRead.put("invitee", "8TYJRhpfth");////checking Lasha's alert
			alertTable.findAlertsInBackground(alertUnRead,alert);
			

	}
	
	/**
	 * Gets the alerts as a list of ParseObjects and return an
	 * array of ATLAlertsModel the app can use, with all the prorperties 
	 * initiated
	 * @param alerts
	 */
	public ArrayList<ATLAlertModel> toATLAlertModel(ArrayList<ParseObject> alerts)
	{
		ArrayList<ATLAlertModel> alertsModel = new ArrayList<ATLAlertModel>();
		
		for (ParseObject alert:alerts)
		{
			ATLAlertModel newAlertModel = new ATLAlertModel();
			
			/*NGHIA Comment out
			//itemType = TABLE_ALERTS.TASK;
			newAlertModel.alertEventLocation=alert.getString("location");
			newAlertModel.alertEventtitle=alert.getString("title");
			newAlertModel.alertSenderAtlasId = alert.getString("fromID");
			newAlertModel.alertSendername =alert.getString("fromName");
			   
			newAlertModel.alertSenderemail=alert.getString("fromEmail");
			newAlertModel.isRead = alert.getBoolean("isRead");
			
			///for Event Invite
			newAlertModel.alertAlt1Datetime = alert.getDate("alt_one_date");
			newAlertModel.alertAlt2Datetime = alert.getDate("alt_two_date");
			newAlertModel.alertPreferredDatetime = alert.getDate("pref_date");
			
			newAlertModel.duration = (Integer) alert.getNumber("duration");
			
			////for Task Assign
			newAlertModel.priorityCategory = alert.getString("catagory");
			newAlertModel.alertDescription = alert.getString("content");
			alertsModel.add(newAlertModel);
			*/
		}
		
		
	 	
		
		return alertsModel;
	}
	/**
	 * Sets the ATLDBCommon properties in the background
	 * @param retrieveFriends whether or not updating the local friends DB as well
	 */
	public void setAllAtlasUsers(boolean retrieveFriends) {
		User users = new User();
		
		 users.setAllUsers(retrieveFriends);
		
	}
	public SERVER_MESSAGE calendarInviteRespond(String inviteId,
			String optionPicked, String toAtlasId,
			String toEmail, String toName,
			String title, String location,
			  Date datePref, int duration,
			  String prefdateFormateInUTC)
	{
		SERVER_MESSAGE serverMessage = SERVER_MESSAGE.FAIL;
		if (inviteId!=null && !inviteId.equals(""))
		{
			
		
		
		
		CalendarRespond calendarInviteRespondTable = new CalendarRespond();
		if (toAtlasId!=null && 
			((title!=null && !title.equals("")&&(location!=null)))
			&&
			prefdateFormateInUTC!=null && !prefdateFormateInUTC.equals("")	)
		{
			calendarInviteRespondTable.respondFriend(inviteId,optionPicked, 
					toAtlasId, title, location, toEmail, datePref,toName , 
					duration, prefdateFormateInUTC);
			
					if (calendarInviteRespondTable.updateTableMessage.equals(SERVER_MESSAGE.SUCCESS))
					{
						if (toAtlasId!=null && !toAtlasId.equals(""))
						{// event invite  atlas user (push notification)
							///Responder's Name has confirmed "Event Title" at time 
							//on day. (for example: "at 4pm on Saturday").
						
							LinkedList<String> channels = new LinkedList<String>();
							String message =  AtlasAndroidUser.getUserNameDisplay()+" has confirmed "+title+" at "+datePref;
							channels.add("ID"+toAtlasId);
							JSONObject data = new JSONObject();
							try {  
								data = new JSONObject("{\"alert\": \""+message+"\",\"badge\": \"Increment\",\"sound\": \"Incoming_Atlas_Push.mp3\"}");
							} catch (JSONException e) {
								// TODO Auto-generated catch block     
								e.printStackTrace();
							}
							if (data!=null && channels.size()>0 && message!=null && !message.equals(""))
								pushNotification(channels,message,data);
						  
						}    
//						else
//						{// assign Non atlas user
//							Intent intent = new Intent(activity.getBaseContext(),SendEmailActivity.class);
//							intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//							intent.putExtra("fromName", AtlasAndroidUser.getUserNameDisplay());
//							intent.putExtra("name", toName);
//							intent.putExtra("atlasId", calendarInviteRespondTable.getAtlasID());
//							intent.putExtra("emailAddress",toEmail);//"sharonanachum@gmail.com");//
//							intent.putExtra("item", "eventm");
//							intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//							activity.getBaseContext().startActivity(intent);   
//						}
//					
//						HashMap<String,Object> respond = new HashMap<String,Object>();
//						respond.put("inviteID", inviteId);
//						calendarInviteRespondTable.put(respond);
//						
						
						
						serverMessage = SERVER_MESSAGE.SUCCESS;
					}
		}
		//// updating respond 
		CalendarInvite updateCalendarInvite = new CalendarInvite();
		HashMap<String, Object> updates = new HashMap<String,Object>();
		updates.put("isRead", true);
		updates.put("respond", true);
		updateCalendarInvite.updateRowInTableInBackground(inviteId, updates);

		}
		else
			serverMessage = SERVER_MESSAGE.FAIL;
		return serverMessage;
		  
		
	}
	public SERVER_MESSAGE taskAssignRespond(String taskId,boolean acceptTask,String toId,String taskTitle,String taskContent,
			String toEmail,Date datePref, String tofirstname,
			  String prefdateFormateInUTC) 
	{
		SERVER_MESSAGE serverMessage = SERVER_MESSAGE.FAIL;
		if (taskId!=null && !taskId.equals(""))
		{
			
		
		
		
		TaskAssignRespond taskAssignRespondTable = new TaskAssignRespond();
		if (taskId!=null && 
			((taskTitle!=null && !taskTitle.equals("")&&(taskContent!=null)))
			&&
			prefdateFormateInUTC!=null && !prefdateFormateInUTC.equals("")	)
		{
			taskAssignRespondTable.respondFriend(taskId, acceptTask,
					toId, taskTitle, taskContent, toEmail, datePref, 
					tofirstname, prefdateFormateInUTC);
			
					if (taskAssignRespondTable.updateTableMessage.equals(SERVER_MESSAGE.SUCCESS))
					{
						if (toId!=null && !toId.equals(""))
						{// event invite  atlas user (push notification)
							///Responder's Name has confirmed "Event Title" at time 
							//on day. (for example: "at 4pm on Saturday").
						
							LinkedList<String> channels = new LinkedList<String>();
							String message =  AtlasAndroidUser.getUserNameDisplay()+" has confirmed "+taskTitle+" at "+datePref;
							channels.add("ID"+toId);
							JSONObject data = new JSONObject();
							try {  
								data = new JSONObject("{\"alert\": \""+message+"\",\"badge\": \"Increment\",\"sound\": \"Incoming_Atlas_Push.mp3\"}");
							} catch (JSONException e) {
								// TODO Auto-generated catch block     
								e.printStackTrace();
							}
							if (data!=null && channels.size()>0 && message!=null && !message.equals(""))
								pushNotification(channels,message,data);
						  
						}    
//						else
//						{// assign Non atlas user
//							Intent intent = new Intent(activity.getBaseContext(),SendEmailActivity.class);
//							intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//							intent.putExtra("fromName", AtlasAndroidUser.getUserNameDisplay());
//							intent.putExtra("name", toName);
//							intent.putExtra("atlasId", calendarInviteRespondTable.getAtlasID());
//							intent.putExtra("emailAddress",toEmail);//"sharonanachum@gmail.com");//
//							intent.putExtra("item", "eventm");
//							intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//							activity.getBaseContext().startActivity(intent);   
//						}
//					
//						HashMap<String,Object> respond = new HashMap<String,Object>();
//						respond.put("inviteID", inviteId);
//						calendarInviteRespondTable.put(respond);
//						
						
						
						serverMessage = SERVER_MESSAGE.SUCCESS;
					}
		}
		//// updating respond 
		TaskAssign updateTaskAssign = new TaskAssign();
		HashMap<String, Object> updates = new HashMap<String,Object>();
		updates.put("isRead", true);
		updateTaskAssign.updateRowInTableInBackground(taskId, updates);

		}
		else
			serverMessage = SERVER_MESSAGE.FAIL;
		return serverMessage;
		  
		
	}
	public boolean isUserFacebookAtlasUser() {
		boolean facebookUser = false;
		
		if (parseUser!=null)
			if(parseUser.getString("facebook_id")!=null)
				facebookUser = !parseUser.getString("facebook_id").equals("");
				
		
		return  facebookUser;
	}
	public void setUserListener(UserDelagateInterface currentlistener) {
		listener = currentlistener;
		
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
			HashMap<String, String> loginProperties, boolean success) {
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
	
	
}
