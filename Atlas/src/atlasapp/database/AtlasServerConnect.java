package atlasapp.database;

import java.io.FileOutputStream;
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

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
 

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import atlasapp.common.ATLConstants.ATL_EMAIL_SUBJECT_TITLE;
import atlasapp.common.ATLConstants.AlertType;
import atlasapp.common.ATLConstants.EmailTemplateType;
import atlasapp.common.ATLConstants.TASK_CATEGORY;
import atlasapp.common.ATLCurrentsUserAlerts;
import atlasapp.common.ATLEmailTemplate;
import atlasapp.common.ATLFriendLocalTable;
import atlasapp.common.ATLUser;
import atlasapp.common.CurrentSessionFriendsList;
import atlasapp.common.DateTimeUtilities;
import atlasapp.common.UtilitiesProject;
import atlasapp.database.DatabaseConstants.ITEM_TYPE;
import atlasapp.database.DatabaseConstants.ITEM_TYPE_PRIORITY_ORDER;
import atlasapp.database.DatabaseConstants.ITEM_USER_TASK_STATUS;
import atlasapp.database.DatabaseConstants.OPERATION_METHOD;
import atlasapp.database.DatabaseConstants.SERVER_MESSAGE;
import atlasapp.database.DatabaseConstants.SIGN_NEW_USERS_CALLER;
import atlasapp.database.DatabaseConstants.TABLES_NAME;
import atlasapp.database.DatabaseConstants.TABLE_ALERTS;
import atlasapp.model.ATLAlertModel;
import atlasapp.model.ATLContactModel;
import atlasapp.model.ATLEventCalendarModel;
import atlasapp.model.ATLFriendModel;
import atlasapp.model.ATLItemUserModel;
import atlasapp.section_alerts.AlertCellData;
import atlasapp.section_appentry.AtlasApplication;
import atlasapp.section_calendar.CurrentSessionEvents;


public class AtlasServerConnect implements ParseDBCallBackInterface, EmailAddressCallBackInterface, ATLOutboundEmailCallBackInterface {
	
	

	
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
  
    
    
    public static String  parseUserFirstName;
	public static void setParseUserFirstName(String currentParseUserFirstName) { parseUserFirstName = currentParseUserFirstName; }
    public static String getParseUserFirstName() { return parseUserFirstName; } 
  
    
    
    public static String  parseUserLastName;
	public static void setParseUserLastName(String currentParseUserLastName) { parseUserLastName = currentParseUserLastName; }
    public static String getParseUserLastName() { return parseUserLastName; } 
  
    
    
    
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
	protected static ParseUser currentParseUser;
	private ArrayList<String> allFacebookAtlasUsersFBID;
	private ATLCurrentsUserAlerts currentUserAlerts;
	private HashMap<String, ATLContactModel> allCurrentSessionUsersOnAtlas;
	//private InviteToAtlas inviteToAtlas;
	
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

			
			setParseUserFirstName(parseUser.getString("first_name"));

			setParseUserLastName(parseUser.getString("last_name"));

			
			
			setParseUserUserPassword(parseUser.getString("password_copy"));
			
			
			parseUser.getFile("picture");
			
		
		}
	}
	
	
	/**
	 * Update AtlasAndroidUser Object Info from Parse Server   
	 */
	public  void setAtlasAndroidUserFromServer()
	{
		if (parseUser!=null)
			setAtlasAndroidUserObject(parseUser);
		
	}
	/**
	 * Set All Local AtlasAndroidUser Object properties from Server properties 
	 * @param user
	 */
	public static  void setAtlasAndroidUserObject(User user)
	{
		setParseUser(user);
		setParseUserInfoFromServer();   
		ATLUser.setEmail(parseUserUserEmail);
		ATLUser.setfbID(parseUserUserFBID);
		ATLUser.setParseUserID(parseUserUserObjectId);
		ATLUser.setAccessToken(parseUserUserAceessToken);
		ATLUser.setPicture(parseUserUserImage);
		ATLUser.setAtlasPassword(parseUserUserPassword);
		ATLUser.setUserNameDisplay(parseUserUserNameDisplay);
	
		
		
		ATLUser.setFirstName(parseUserFirstName);

		ATLUser.setLastName(parseUserLastName);

		
		
		ATLUser.setUserSignonServer(true);
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
	public static void subscribedToChannels(Context context,String channel,Class<? extends Activity> activity)
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
	
		
		ParsePush push = new ParsePush();
		push.setChannels(channels); 
		push.setMessage(alertMessage);
		push.setPushToIOS(true);
		push.setData(data);
		push.sendInBackground();  
	}
	public static void pushNotification(String channels, String alertMessage, JSONObject data)
	{
	
//		Log.v("EVENT CONTROLLER ", "PUSHed");
		ParsePush push = new ParsePush();
		push.setChannel(channels); 
		push.setMessage(alertMessage);
		push.setPushToIOS(true);
		push.setData(data);
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
//	public  SERVER_MESSAGE  updateAtlasUserInfoOnServer(HashMap<String, Object> userAtlasInfoUpdtes,UserDelagateInterface listener)
//	{
//		SERVER_MESSAGE updateSuccess = SERVER_MESSAGE.FAIL;
//		
//		updateSuccess = (userAtlasInfoUpdtes.isEmpty())?  SERVER_MESSAGE.EMAPY_VALUE :updateSuccess;
//		
//		
//		if (!updateSuccess.equals(SERVER_MESSAGE.EMAPY_VALUE))
//		{
//			parseUser.userDelagator = listener;
//			if (parseUser==null)
//				logUserToServer(ATLUser.getEmail(), ATLUser.getAtlasPassword(),listener);
//				
//			
//			
//			updateSuccess = (parseUser!=null)? SERVER_MESSAGE.LOGGED_IN:SERVER_MESSAGE.LOGGED_OUT; 
//			
//			parseUser.put(userAtlasInfoUpdtes,listener);
//			updateSuccess  = parseUser.updateTableMessage ;
//		}
//		return updateSuccess;
//	}
	     
	public void updateAtlasUserInfoOnServer(HashMap<String, Object> userAtlasInfoUpdtes,UserDelagateInterface listener)
	{
		Log.v("SERVER CONNECT", "success ");
		if (parseUser!=null)
		{
//			Log.v("SERVER CONNECT", "NOT NULL ");
			parseUser.parseObjectUser = ParseUser.getCurrentUser();
			parseUser.put(userAtlasInfoUpdtes,listener);
		}
		else
		{
//			Log.v("SERVER CONNECT", "NULL ");
			 parseUser = new User();
			parseUser.parseObjectUser = ParseUser.getCurrentUser();
			parseUser.put(userAtlasInfoUpdtes,listener);
		}
//		if (parseUser==null)
//			listener.getUpateCallBack(false);
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
			
	}
	
	/**
	 * Searching user  on 
	 * Parse User table by email address 
	 * 
	 * 
	 * 
	 * @param email
	 * @return username and atlas password for that Parse user
	 */ 
	public static void getUserEmailOnParse( String email, UserDelagateInterface listener) {
		
		HashMap<String, Object> userParseLogin = new HashMap<String, Object>();
		userParseLogin.put("email", email);
		parseUser = new User();
		parseUser.parseQuery = ParseUser.getQuery();
		parseUser.userDelagator = listener;
		parseUser.findWhereEqualTo(userParseLogin,OPERATION_METHOD.FIND_USER_EMAIL);
	}

	/**
	 * 
	 */
	public static void getUserFromEmailAddressTable(String email, EmailAddressCallBackInterface emailAddressListener)
	{
		EmailAddress emailUser = new EmailAddress();
		emailUser.emailAddressListener = emailAddressListener;
		emailUser.getAtlasUserByEmailAddress(email);
	}
	/**
	 * Updating users fbID and access_token saved on local profile 
	 * on the server side and setting the SignOnServer flag accordingly
	 */
	public  void updateFacebookUseruser(HashMap<String, Object> facebookInfo,UserDelagateInterface listener) 
	{
		if (!ATLUser.getfbID().equals("")&& !ATLUser.getAccessToken().equals(""))
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
		if (!ATLUser.getfbID().equals("")&& !ATLUser.getAccessToken().equals(""))
		{
		
			HashMap<String, Object> facebookInfo = new HashMap<String,Object>();
			facebookInfo.put("facebook_id", ATLUser.getfbID());
			facebookInfo.put("access_token", ATLUser.getAccessToken());
		
		
			updateAtlasUserInfoOnServer(facebookInfo,listener);
		}
		
	}
	public static void signInCurrentParseUser(final String email,
			final String password,final ParseDBCallBackInterface parseCallBackDeleget)
	{
		
		
		ParseUser.logInInBackground(email, password, new LogInCallback() {
			  public void done(ParseUser user, ParseException e) {
			    if (user != null) {
			    	parseUser.setUser(user);
			      // Hooray! The user is logged in.
			    	AtlasServerConnect.userSignInSuccessfuly = true;
			    	parseCallBackDeleget.signInSuccess( true);
//			    	user.setUsername(email);
//			    	user.setPassword(password);
//			    	user.signUpInBackground(new SignUpCallback() {
//						  public void done(ParseException e) {
//							    if (e == null) {
//							    	parseCallBackDeleget.signInSuccess( true);
//							    }
//							    else
//							    {
//							    	parseCallBackDeleget.signInSuccess(false);
//							    }
//						  }});
			    	
			    } else {
			      // Signup failed. Look at the ParseException to see what happened.
			    	AtlasServerConnect.userSignInSuccessfuly = false;
			    	parseCallBackDeleget.signInSuccess(false);

			    }
			  }
			});
//		currentParseUser.signUpInBackground(new SignUpCallback() {
//			  public void done(ParseException e) {
//				    if (e == null) {
//				    	
//				    }
//				    else
//				    {
//				    	
//				    }
//			  }});
		
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
//	int index=0;
	UserDelagateInterface signNewlistener;
	private void signNonAtlasUsers(SIGN_NEW_USERS_CALLER caller, ArrayList<ATLContactModel> newNonAtlasUserToSign, UserDelagateInterface listener)
	{
		currentNonAtlasUserToSign = newNonAtlasUserToSign;
		signNewlistener = listener;
		currentSignNewcaller = caller;
		if (newNonAtlasUserToSign!=null )
		{
			User user  = new User();
			
//			for (ATLContactModel invitee:newNonAtlasUserToSign)
//			{
				if (newNonAtlasUserToSign!=null && newNonAtlasUserToSign.size()>0 &&  newNonAtlasUserToSign.get(0)!=null && 
						newNonAtlasUserToSign.get(0).getEmailAddress()!=null && !newNonAtlasUserToSign.get(0).getEmailAddress().equals(""))
				{
					user.parseCallBackDeleget = this;
					//// TO DO:CHECK IF THE INVITEE EMAIL ALREADY 
					/// ON PARSE email_address table
					
					
//					checkIfNonFrinedEmailOnParse(newNonAtlasUserToSign)
					
					user.signInNewUser(
							newNonAtlasUserToSign.get(0).getEmailAddress()
							
							,newNonAtlasUserToSign.get(0).getFirstname()
							,newNonAtlasUserToSign.get(0).getLastname());
					
				}
				else
				{
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
	
	
	
	
	public void checkIfNonFrinedEmailOnParse(final SIGN_NEW_USERS_CALLER caller,
			ArrayList<ATLContactModel> newNonAtlasUserToSign, 
			final UserDelagateInterface listener) {

		if (newNonAtlasUserToSign==null || newNonAtlasUserToSign.size()==0)
			listener.getSignNonAtlasUsersCallBack(caller, true, newCurrentNonAtlasUserToSign);
		//		Log.v("ATLAS SERVER CONNECT", "SIGN NON USERS "+newNonAtlasUserToSign.size());
		else
		{
			final HashMap<String,ATLContactModel> nonUsersHash = new HashMap<String,ATLContactModel>();


			ArrayList<String> nonAtlasUsersEmails = new ArrayList<String>();
			for (ATLContactModel nonAtlasUser:newNonAtlasUserToSign)
				if (nonAtlasUser!=null && nonAtlasUser.getEmailAddress()!=null && !nonAtlasUser.getEmailAddress().equals(""))
				{
					nonAtlasUsersEmails.add(nonAtlasUser.getEmailAddress());
					nonUsersHash.put(nonAtlasUser.getEmailAddress(), nonAtlasUser);
				}
			Log.v("ATLAS SERVER CONNECT", "nonAtlasUsersEmails "+nonAtlasUsersEmails.size());

			Log.v("ATLAS SERVER CONNECT", "nonAtlasUsersEmails "+nonAtlasUsersEmails);

			//		ParseObject emailAddress = new  ParseObject("email_addess");
			ParseQuery  emailQuery = new ParseQuery("email_address");
			emailQuery.whereContainedIn("email_address", nonAtlasUsersEmails);
			emailQuery.findInBackground(new FindCallback() {
				public void done(List<ParseObject> list, ParseException e) {
					if (e == null) {
						Log.v("ATLAS SERVER CONNECT", "SIGN NON USERS LIST "+list.size());

						// The query was successful.
						// non atlas user was already invited before..

						if (list!=null && list.size()>0)
						{
							newCurrentNonAtlasUserToSign = new HashMap<String,ATLContactModel>();
							ATLContactModel contact;
							String atlasId = "";//list.get(0).getString("atlas_id");
							for (ParseObject user:list)
							{
								atlasId = user.getString("atlas_id");
								contact = nonUsersHash.get(user.getString("email"));
								contact.setAtlasId(atlasId);
								nonUsersHash.remove(user.getString("email_address"));
								newCurrentNonAtlasUserToSign.put(user.getString("email"), 
										contact);


							}
						}
						else
						{
							/// 

						}
					} else {
						// Something went wrong.

					}
					currentNonAtlasUserToSign = new ArrayList<ATLContactModel>();
					if (nonUsersHash!=null && nonUsersHash.size()>0)
						for (String email:nonUsersHash.keySet())
							currentNonAtlasUserToSign.add( 
									nonUsersHash.get(email));
					Log.v("ATLAS SERVER CONNECT", "SIGN NON USERS "+currentNonAtlasUserToSign.size());
					Log.v("ATLAS SERVER CONNECT", "SIGN NON USERS "+currentNonAtlasUserToSign);

					signNonAtlasUsers(caller,
							currentNonAtlasUserToSign, 
							listener);
				}
			});


		}


	}
	@Override
	public void getAtlasUserByEmailAddressCallBack(
			HashMap<String, Object> userLoginInfo) {



	}
	@Override
	public void signInNewFriendUserSuccess(boolean success, ParseUser user) {
		if (success)
		{
			//// TO DO: UPDATE EMAIL_ADDRESS TABLE ON PARSE WITH THE USER EMAIL ADDRESS
			saveEmailAddrss(user);
			
				//		nonAtlasUsersByEmail.put(user.getString("email"), );
				currentNonAtlasUserToSign.get(0).setAtlasId(user.getObjectId());
				Log.v("ATLAS SERVER CONNECT", "SIGN UP NEW USER OBJECT ID"+user.getObjectId());
				newCurrentNonAtlasUserToSign.put(user.getString("email"), currentNonAtlasUserToSign.get(0));
				if (currentNonAtlasUserToSign!=null && currentNonAtlasUserToSign.size()>1)
				{
					currentNonAtlasUserToSign.remove(0);
					//		index++;
					signNonAtlasUsers(currentSignNewcaller,currentNonAtlasUserToSign,signNewlistener);
				}else
					signNewlistener.getSignNonAtlasUsersCallBack(currentSignNewcaller, true, newCurrentNonAtlasUserToSign);

		}
		else  
		{
//	    	Log.v("ATLAS SERVER CONNECT", "FAILED NEW NON USER");

			signNewlistener.getSignNonAtlasUsersCallBack(currentSignNewcaller, false, newCurrentNonAtlasUserToSign);
		}
	}
	private void saveEmailAddrss(ParseUser user) 
	{

		EmailAddress email = new EmailAddress();
		ArrayList<String> atlasUserEmails = new ArrayList<String> ();
		atlasUserEmails.add(user.getString("email"));
		email.setUserEmailAddressOnParse(atlasUserEmails, user.getObjectId());
		
	}
	public void saveProfileImage(byte[] image, ParseDBCallBackInterface listener)
	{
		if (parseUser!=null)
		{
			parseUser.parseCallBackDeleget = listener;
			parseUser.saveProfileImageFile("image.png", image);
		}
	}
//	
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
//		
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
		String[] empty = {""};
		
	
		 parseUser.findWhereNotContainedIn("facebook_id", Arrays.asList(empty),OPERATION_METHOD.FIND_ALL_USERS);
//		
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
		
	}
	
//	
	
	
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
//		

	}
	/**
	 * Retrieve the current user object picture from parse
	 * and store it on local atlas folder on a background process 
	 * @param friendsFilePictureName
	 * @param application
	 */
	public static void storeUserPictureInBackground(ParseUser user,final String friendsFilePictureName
			) {
		if (user!=null){
		      ParseFile applicantResume = (ParseFile)user.get("picture");
		      if(applicantResume != null){
		    	  applicantResume.getDataInBackground(new GetDataCallback() {
					  public void done(byte[] data, ParseException e) {
					    if (e == null && data!=null) {
					      // data has the bytes for the resume
					    	Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
							if (bitmap!=null)
								AtlasApplication.storeFriendsPictureInBacground(friendsFilePictureName, bitmap);
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
		
//		
	}
	public static void getParseUserByEmail(ArrayList<String> email,UserDelagateInterface listener) {
		
		parseUser = new User();
		parseUser.userDelagator = listener;
//		HashMap<String,ArrayList<String>> userEmail = new HashMap<String,ArrayList<String>>();
//		userEmail.put("email", email);
		parseUser.findWhereContainedIn("email", email, OPERATION_METHOD.FIND_USER_EMAIL);//(userEmail, );
		
//		
	}
//	  
	public void setAllUserAlerts()
	{
		ArrayList<ParseObject> newUserAlerts = new ArrayList<ParseObject>();
		
		for (String table:TABLES_ASSIGN)
		{
			AtlasServerTable alertTable = new AtlasServerTable();
			alertTable.setTableName(table);
			HashMap<String,Object> alertUnRead = new HashMap<String,Object>();
			alertUnRead.put("isRead", false);
			alertUnRead.put("fromID", ATLUser.getParseUserID());
			
			
			alertTable.findWhereEqualTo(alertUnRead,OPERATION_METHOD.FIND);
		}
//		
		
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
			alertUnRead.put("invitee", ATLUser.getParseUserID());//"d1EJbNSFvv");//
		
			else
				alertUnRead.put("toID", ATLUser.getParseUserID());//"d1EJbNSFvv");//

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
//	
	
	
	
	public static void setNewAtlasFriendsFromAtlasIdsListInBackground(ArrayList<String> atlasIds)
	{
		final ATLDBCommon atlDBCommon = ATLDBCommon.getSingletonObject(null);
		final CurrentSessionFriendsList currentSessionFriendsList = CurrentSessionFriendsList.getSingletonObject();
		if (atlasIds!=null && atlasIds.size()>0)
		{
			ParseQuery userQuery  = ParseUser.getQuery();
			userQuery.whereContainedIn("objectId", atlasIds);
			userQuery.findInBackground(new FindCallback() {
			    public void done(List<ParseObject> friendsList, ParseException e) 
			    {
			        if (e == null) 
			        {
			        	if (friendsList!=null && friendsList.size()>0)
			        	{
			        		/// FriendsList now contain all the friends found on the user table 
			        		/// by email ....
			        		ATLContactModel atlContactModel;
			        		ArrayList<ATLContactModel> allAtlContactModelFriends = new ArrayList<ATLContactModel>();
//			        		User usersFriends = new User();
			        		for (ParseObject atlasUser :friendsList)
			        		{
			        			if (atlasUser.getString("email")!=null && !atlasUser.getString("email").equals("")
			        					&& !atlasUser.getObjectId().equals(ATLUser.getParseUserID()))
								{
			        				 storeUserPictureInBackground((ParseUser)atlasUser,atlasUser.getObjectId());
			        				 atlContactModel = User.getATLContactModelFromParseUser(atlasUser);
			        				 allAtlContactModelFriends.add(atlContactModel);
			        				 
			        				
								}
			        			
			        		}
			        		
			        		if (allAtlContactModelFriends!=null && allAtlContactModelFriends.size()>0)
			        		{
			        			CurrentSessionFriendsList contactsUpdated = CurrentSessionFriendsList.getSingletonObject();
			        			contactsUpdated.setFacebookFriendsUpdateComplete(true,null);
			        			contactsUpdated.setFriendsUpdateComplete(false);
//			        			currentSessionFriendsList.addToCurrentATLFriendsList(allAtlContactModelFriends);
			        			ATLFriendModel.addNewFriendsToDBInBackground(allAtlContactModelFriends);
			        		}
			        		
			        			
//			        			ATLFriendLocalTable.retrieveAllFriendsPicturesInBacground();
			        		}
			        		
			        	}
			        	
			        }
			    }
			);
		}
	}
	
	/**
	 * Gets a list of all the user's contact's email address found
	 * on the mobile ,search them on the email_address table
	 * and return a list of all the emails found , then search that
	 * list on the User table by email for all the Atlas Users that are 
	 * also the user's contacts
	 * 
	 * all done in the background
	 * 
	 * 
	 * @param emailAddresses
	 * @param newRegister 
	 */
	public void setAllUsersContactsEmailsOnParse(final ArrayList<String> emailAddresses, final boolean newRegister)
	{
		
		final ATLDBCommon atlDBCommon = ATLDBCommon.getSingletonObject(null);
		final CurrentSessionFriendsList currentSessionFriendsList = CurrentSessionFriendsList.getSingletonObject();
//		if (emailAddresses!=null && emailAddresses.length>0)
//		{
			EmailAddress emailAddressesTable = new EmailAddress();
			
			// call back on getAllAtlasUserEmailAddressCallBack method
			emailAddressesTable.parseQuery.whereContainedIn("email_address", emailAddresses);
			
			emailAddressesTable.parseQuery.findInBackground(new FindCallback() {
			    public void done(List<ParseObject> emailList, ParseException e) {
			        if (e == null) 
			        {
			        	//emailList contains all email address records found both 
			        	// on the User mobile and on Parse 
						if (emailList!=null && emailList.size()>0)
						{
							ArrayList<String> emails = new ArrayList<String>();
							for (ParseObject email:emailList)
							{
								if (email!=null && email.getString("atlas_id")!=null && 
										!email.getString("atlas_id").equals("") && 
										!ATLUser.getParseUserID().equals(email))
									emails.add(email.getString("atlas_id"));
							}

							if (emails!=null && emails.size()>0)
							{
					        	atlDBCommon.setCurrentSessionFriendsIndexFoundOnParse(emails);
//								String[] emailArray = emails.toArray(new String[emails.size()]);
								ParseQuery userQuery  = ParseUser.getQuery();
								userQuery.whereContainedIn("objectId", emails);
								userQuery.findInBackground(new FindCallback() {
								    public void done(List<ParseObject> friendsList, ParseException e) 
								    {
								        if (e == null) 
								        {
								        	if (friendsList!=null && friendsList.size()>0)
								        	{
								        		/// FriendsList now contain all the friends found on the user table 
								        		/// by email ....
								        		ATLContactModel atlContactModel;
								        		HashMap<String,String> atlAtlasIdsFound = new HashMap<String,String>();
								        		ArrayList<ATLContactModel> allAtlContactModelFriends = new ArrayList<ATLContactModel>();
//								        		User usersFriends = new User();
								        		for (ParseObject atlasUser :friendsList)
								        		{
								        			if (atlasUser.getString("email")!=null && !atlasUser.getString("email").equals("")
								        					&& !atlasUser.getObjectId().equals(ATLUser.getParseUserID()))
													{
								        				 storeUserPictureInBackground((ParseUser)atlasUser,atlasUser.getObjectId());
								        				 atlContactModel = User.getATLContactModelFromParseUser(atlasUser);
								        				 allAtlContactModelFriends.add(atlContactModel);
								        				 atlAtlasIdsFound.put(atlContactModel.getAtlasId(),atlContactModel.getFirstname()+" "+atlContactModel.getLastname());
								        				 
								        				
													}
								        			
								        		}
								        		
								        		
								        		HashMap<String,ATLContactModel> nonATLContactsModel = atlDBCommon.getNonAtlasContact();
								        		if (nonATLContactsModel!=null && nonATLContactsModel.size()>0)
								        		{
								        			ArrayList<ATLContactModel> nonATLContactModelList = new ArrayList<ATLContactModel>();
								        			for (String id:nonATLContactsModel.keySet())
								        			{
								        				nonATLContactModelList.add(nonATLContactsModel.get(id));
								        				
								        			}
								        			ArrayList<ATLContactModel> all = (ArrayList<ATLContactModel>)allAtlContactModelFriends.clone();
								        				
								        			if (all!=null && all.size()>0)
								        			{
								        				if (nonATLContactModelList!=null && nonATLContactModelList.size()>0)
								        					nonATLContactModelList.addAll(all);
//								        					all.addAll(nonATLContactModelList);
								        				currentSessionFriendsList.setCurrentFriendsList(nonATLContactModelList);
								        				
								        			}
								        			
								        			ATLContactModel.updateAll(false,nonATLContactModelList);
								        			ATLFriendLocalTable.retrieveAllFriendsPicturesInBacground();
								        		}
								        		if (allAtlContactModelFriends!=null && allAtlContactModelFriends.size()>0)
								        		{
								        			
								        			currentSessionFriendsList.setCurrentATLFriendsList(allAtlContactModelFriends);
								        			if (newRegister)
								        			{
								      				  Log.v("PUSH NEW ", "NEW PUSH NOTIFICATION ");

								        				AtlasServerConnect.newFriendPushNotification(atlAtlasIdsFound);
								        			}
								        			
								        		}
								        		
								        	}
								        	
								        }
								    }
								});
							}
							
							
						}
			        }
			    }
			});
	}
	

	protected static void newFriendPushNotification(
			HashMap<String,String> atlAtlasIdsFound) {
		if (atlAtlasIdsFound!=null && atlAtlasIdsFound.size()>0)
		{
			JSONObject data = new JSONObject();
			String message = "";
			String friendName = ATLUser.getUserFirstName()+" "+ATLUser.getUserLastName();
			String channel="";
			for (String atlasId:atlAtlasIdsFound.keySet())
			{
				  Log.v("NOTIFICATION ", atlasId);

				if (atlasId!=null && !atlasId.equals("") && 
						friendName!=null && !friendName.equals(""))
				{
					channel = "ID"+atlasId;
					message  = "Your Friend "+friendName+" has just joined Atlas ";
					try {  
						data = new JSONObject("{\"alert\": \""+message+"\"," +
								"\"badge\": \"Increment\"," +
								"\"action\": \"atlasapp.section_alerts.PUSH_NOTIFICATION\"," +
								"\"name\": \"Vaughn\"," +
								"\"atlasId\": \""+ATLUser.getParseUserID()+"\"," +
								"\"sound\": \"Incoming_Atlas_Push.mp3\"}");
					} catch (JSONException e) {
						// TODO Auto-generated catch block     
						e.printStackTrace();
					}
				}
				AtlasServerConnect.pushNotification(channel,message,data);
				
			}
		}
		
	}
	/**
	 * Gets array list of all the current new facebook_id's 
	 * of the user's facebook friends (got from facebook)
	 * and search them on Parse
	 * @param facebookFriends
	 */
	public void scanCurrentNewFacebookFriendsOnParse(  
			ArrayList<String> facebookFriends) 
	{
		final CurrentSessionFriendsList currentSessionFriendsList = CurrentSessionFriendsList.getSingletonObject();

		if (facebookFriends!=null && facebookFriends.size()>0)  
		{
			
			ParseQuery parseQuery = ParseUser.getQuery();
			parseQuery.whereContainedIn("facebook_id", facebookFriends);
			parseQuery.findInBackground(new FindCallback() {
				public void done(List<ParseObject> facebookATLFriends, ParseException e) {
					if (e == null) 
					{
						if (facebookATLFriends!=null && facebookATLFriends.size()>0)
						{
							ATLContactModel atlContactModel;
							ArrayList<ATLContactModel> allAtlContactModelFacebookFriends = new ArrayList<ATLContactModel>();
							for (ParseObject atlasFacebookUser :facebookATLFriends)
							{
								if (!ATLUser.getEmailAddresses().contains(atlasFacebookUser.getString("email")))
								{
									storeUserPictureInBackground((ParseUser)atlasFacebookUser,atlasFacebookUser.getObjectId());
									atlContactModel = User.getATLContactModelFromParseUser(atlasFacebookUser);
									allAtlContactModelFacebookFriends.add(atlContactModel);
								}
							}

		        			ATLContactModel.updateAll(true,allAtlContactModelFacebookFriends);

							
						}
						else
						{
							/// user doesnt have new ATLAS Facebook friends....
						}

					}
					else
					{
						/// Parse error...
					}
				}});
		}
		else
		{
			// no new facebook friends found
			/// no need to do any kind of update!
		}
		
		
	}
	
	@Override
	public void getFindQueryCallBack(List<ParseObject> foundQueryList,
			boolean found) {
		if (found)
		{
			
		}
		else
		{
			     
		}
		
		
	}
	
	
	
//						atlDBCommon.setCurrentSessionEmailsOnATLAS(allContactsmodel);
//						atlDBCommon.setCurrentSessionUsersEmails(allUserNamesOnTable);
//						atlDBCommon.setCurrentSessionUsersFacebookId(allFBUsersModelOnTable);
//						atlDBCommon.setCurrentSessionFacebookUsersUserNames(allFBUserNamesOnTable);
//						atlDBCommon.setCurrentSessionFacebookUsersFacebookId(allFBUserFacebookIdOnTable);
//					//	if (retrieveFriends)
//					//		.updateAllFriendOnDBInBackground();
//						ATLFriendLocalTable.updateAllFriendOnDBInBackground();
//			        } else {
//			        }
//			    }
//			    
//			});
			
			
			
			
//			emailAddressesTable.parseQuery.whereContainedIn("email_address",Arrays.asList(emailAddresses));
//			emailAddressesTable.parseQuery.
			
//		}
//		else
//		{
////			parseCallBackListener.getAllUsersContactsEmailsOnParseCallBack(null);
//		}
		
		
//	@Override
//	public void getAllAtlasUserEmailAddressCallBack(
//			ArrayList<String> emailAddrees) 
//	{
//		if (emailAddrees!=null && emailAddrees.size()>0)
//		{
//			
//		}
//		else
//		{
//			
//		}
//	}
	
	
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
	
	
	/**
	 * Search user parse id on Parse email_address table
	 * and return ArrayList of the user email address on the
	 *   getAllAtlasUserEmailAddressCallBack method
	 * @param userParseId
	 * @param listener
	 */
	public static void getAllATLUserEmailAddress(String userParseId, EmailAddressCallBackInterface listener) {
		EmailAddress userEmailAddress = new EmailAddress();
		userEmailAddress.emailAddressListener = listener;
		
		userEmailAddress.getAllAtlasUserEmailAddress(userParseId);
		
	}
	public static void addUserEmailAddress(String parseUserID, String email, EmailAddressCallBackInterface listener) {
		EmailAddress userEmailAddress = new EmailAddress();
		userEmailAddress.emailAddressListener = listener;
		ArrayList<String> atlasUserEmails = new ArrayList<String>();
		atlasUserEmails.add(email);
		userEmailAddress.setUserEmailAddressOnParse(atlasUserEmails, parseUserID);
		
	}
	
	public static void resetUserPassword(String email, UserDelagateInterface userDelagator)
	{
		if (userDelagator!=null)  
		{
			if(email!=null && !email.equals(""))
			{
				User user = new User();
				user.resetPassword(email,userDelagator);
			}
			else  
			{
				userDelagator.resetPasswordCallBack(false, "Wrong Email address");
			}
			
		}
	}
	
	
	public static void addUserEmailAddress(String parseUserID, ArrayList<String> emails, EmailAddressCallBackInterface listener) {
		EmailAddress userEmailAddress = new EmailAddress();
		userEmailAddress.emailAddressListener = listener;
//		ArrayList<String> atlasUserEmails = new ArrayList<String>();
//		atlasUserEmails.add(email);
		userEmailAddress.setUserEmailAddressOnParse(emails, parseUserID);
		
	}
	/**
	 * Retrieving and storing the user profile picture 
	 * currently updated on Parse to local mobile
	 * folder
	 */
	public static void refreshUserPictureOnTheBackground(final ParseDBCallBackInterface listener) {
		ParseUser user = new ParseUser();
		ParseUser.logInInBackground(ATLUser.getEmail(), ATLUser.getAtlasPassword(), new LogInCallback() {
			  public void done(ParseUser user, ParseException e) {
			    if (user != null) {
			      // Hooray! The user is logged in.
			    	final ParseFile parseFile = (ParseFile)user.get("picture");
			    	if (parseFile!=null)
			    		parseFile.getDataInBackground(new GetDataCallback() {
			    			public void done(byte[] data, ParseException e) {
			    				if (e == null) {
			    					Bitmap bitPic;
			    					if (data!=null)
			    					{
			    						bitPic = BitmapFactory.decodeByteArray(data, 0, data.length);
			    						if (bitPic!=null )
			    							try {
			    								// Copy image from one file path to atlas path on mobile
			    								// where VALUE represent the source file path
			    								if (UtilitiesProject.IMAGE_DIR.canWrite()) {

			    									FileOutputStream fOut=new FileOutputStream(UtilitiesProject.IMAGE_DIR+"/"+"image"+".png"); 
			    									// Here path is either sdcard or internal storage
			    									bitPic.compress(Bitmap.CompressFormat.PNG,100,fOut);
			    									fOut.flush();
			    									fOut.close();
			    									bitPic.recycle(); // If no longer used..
			    								}

			    							} catch (Exception e1) {}
			    						if (parseFile.getUrl()!=null && !parseFile.getUrl().equals(""))
			    							ATLUser.setImageUrl(parseFile.getUrl());
			    						if (listener!=null)
			    							listener.refreshUserPictureOnTheBackgroundCallBack(true,parseFile.getUrl());
			    					}
			    				} else {
			    					if (listener!=null)
		    							listener.refreshUserPictureOnTheBackgroundCallBack(false,null);

			    				}
			    			}
			    		});
			    } else {
			    	// Signup failed. Look at the ParseException to see what happened.
			    	if (listener!=null)
						listener.refreshUserPictureOnTheBackgroundCallBack(false,null);

			    }
			  }
		});



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
	/**
	 * Search pn Parse for the current local item_user records pending
	 * if there's a respond (accept/declined) then return the record from Parse
	 * 
	 * 
	 * call back on getPendingAcceptedAlertCallBack & getPendingDeclinedAlertCallBack methods
	 * for ATLAlertControllerCallBackInterface listener sent.
	 * 
	 * @param pendingWebItemIdsOnLocalDBHashByPrimary
	 * @param pendingWebItemIdsOnLocalDB
	 * @param listener
	 */
	public static void searchForPendingItemUserRecords(
			final HashMap<String,String> pendingWebItemIdsOnLocalDBHashByPrimary,
			ArrayList<String> pendingWebItemIdsOnLocalDB,
			final ATLAlertControllerCallBackInterface listener) {
		
		if (pendingWebItemIdsOnLocalDB!=null && pendingWebItemIdsOnLocalDB.size()>0 )
		{
			ItemUser itemUser = new ItemUser();
			itemUser.parseQuery.whereContainedIn("web_item_id", pendingWebItemIdsOnLocalDB);
			/// Retrieve only accepted/declined records
//			itemUser.parseQuery.whereNotEqualTo("status", 0);
//			itemUser.parseQuery.whereNotEqualTo("status", 3);
			/// NOT BELONG TO THE USER (INVITEE ONLY)
			itemUser.parseQuery.whereNotEqualTo("atlas_id", ATLUser.getParseUserID());
			itemUser.parseQuery.findInBackground(new FindCallback() {
				public void done(List<ParseObject> respondList, ParseException e)
				{
					if (e==null && respondList!=null && respondList.size()>0)
					{
						HashMap<String,ParseObject> declinedList = new HashMap<String,ParseObject>();
						HashMap<String,ItemUserProperties> acceptedRecords = new HashMap<String,ItemUserProperties>();
						HashMap<String,ItemUserProperties> pendingRecords = new HashMap<String,ItemUserProperties>();

						
						ArrayList<String> acceptedPrimaryWebEventIds = new ArrayList<String>();
						for (ParseObject respondRec:respondList)
						{
							if (respondRec!=null)
							{
								int status = (Integer) respondRec.getNumber("status");
//								String atlasId = respondRec.getString("atlas_id");
								
//								boolean  userRecord = (atlasId!=null)? atlasId.equals(ATLUser.getParseUserID()):false;
								String primaryWebEventIdKey = "";
								
								switch (status)
								{
								case 1:
									{/// accepted...  
										
										primaryWebEventIdKey = "";
										ItemUserProperties itemUserProp = ItemUser.getItemUserPropertiesFromParse(respondRec);
										/// Ignoring the inviter's accept records...
										if (itemUserProp!=null && itemUserProp.getEventAssociated()!=null && 
												(itemUserProp.getEventAssociated().atlasId!=null &&
												!itemUserProp.getEventAssociated().atlasId.equals(itemUserProp.atlasId)))
										{
											primaryWebEventIdKey = pendingWebItemIdsOnLocalDBHashByPrimary.get(respondRec.getString("web_item_id"));
											if (primaryWebEventIdKey!=null && !primaryWebEventIdKey.equals(""))
											{
												acceptedRecords.put(primaryWebEventIdKey, itemUserProp);
												acceptedPrimaryWebEventIds.add(primaryWebEventIdKey);
											}
										}	
									}
									break;
								case 2:
										{/// declined...
											primaryWebEventIdKey = "";
											ItemUserProperties itemUserProp = ItemUser.getItemUserPropertiesFromParse(respondRec);
											if (itemUserProp!=null && itemUserProp.getEventAssociated()!=null && 
													(itemUserProp.getEventAssociated().atlasId!=null &&
													!itemUserProp.getEventAssociated().atlasId.equals(itemUserProp.atlasId)))
											{
											if (!declinedList.containsKey(respondRec.getString("web_item_id")))
												declinedList.put(respondRec.getString("web_item_id"),respondRec);	
											}
										}
										break;
								case 0:
								{//// Friends' Pendings from user invites ....
//									if (userRecord)
									{
										primaryWebEventIdKey = "";
										ItemUserProperties itemUserProp = ItemUser.getItemUserPropertiesFromParse(respondRec);
										if (itemUserProp!=null)
										{
											primaryWebEventIdKey = pendingWebItemIdsOnLocalDBHashByPrimary.get(respondRec.getString("web_item_id"));
											if (primaryWebEventIdKey!=null && !primaryWebEventIdKey.equals(""))
											{
												pendingRecords.put(primaryWebEventIdKey, itemUserProp);
											}
  

										}
									}

								}
									break;
								case 3:
								{ ///Complited...
									
								}
									break;
								case 4:
								{ ///Canceled...
									
								}
									break;
									
								
									}
							}
						}
						
						/// call back from pending ready...
						listener.getPendingAlertCallBack(pendingRecords);
						/// call back from accepted ready...
						listener.getBookedAcceptedAlertCallBack(acceptedRecords);
						HashMap<String,ItemUserProperties> declinedRecords = null;

						if (declinedList!=null && declinedList.size()>0)
						{
							declinedRecords = new HashMap<String,ItemUserProperties>();
							/// remove declined alt hours from accepted list...
							String primaryWebEventIdKey = "";
							ItemUserProperties itemUserProp = null;
							for (String webItemIdDeclined:declinedList.keySet())
							{
								primaryWebEventIdKey = pendingWebItemIdsOnLocalDBHashByPrimary.get(webItemIdDeclined);
								if (webItemIdDeclined!=null && !webItemIdDeclined.equals("") && !acceptedPrimaryWebEventIds.contains(primaryWebEventIdKey))
								{
									
									itemUserProp = ItemUser.getItemUserPropertiesFromParse(declinedList.get(webItemIdDeclined));
									if (itemUserProp!=null)
									{
										declinedRecords.put(primaryWebEventIdKey,itemUserProp);
									}
								}
							}
							
						}
						///// update local DB
						
						
						
						/// call back from declined ready...
						listener.getBookedDeclinedAlertCallBack(declinedRecords);
						
					}
					else
					{
						listener.getBookedAcceptedAlertCallBack(null);
						listener.getBookedDeclinedAlertCallBack(null);
					}
					
				}
			});
		}
		else
		{
			listener.getBookedAcceptedAlertCallBack(null);
			listener.getBookedDeclinedAlertCallBack(null);
		}

	}

//	public static void refreshAllEventsFromParseInBackground()
//	{
//		EventController eventController = new EventController();
//		//		eventController.eventControllerCallBackListener = this;
//		eventController.getUserMoveFromParse();
//
//	}

	

	
	
	
		private static  ItemUserProperties getItemUserPropertiesFromParse(ParseObject itemUserProp)
		{
			ItemUserProperties itemUserPropertiesRec = null;
			if (itemUserProp!=null)
			{
				itemUserPropertiesRec = new ItemUserProperties();
				String webItemUserId = (itemUserProp.getString("web_item_user_id")!=null)?
						itemUserProp.getString("web_item_user_id") : "";	
						ITEM_TYPE itemType = (itemUserProp.getString("item_type")!=null &&  itemUserProp.getString("item_type").equals("event"))?
								ITEM_TYPE.EVENT :ITEM_TYPE.TASK;
						ITEM_USER_TASK_STATUS status = (itemUserProp.getNumber("status")!=null)?
								((Integer)itemUserProp.getNumber("status")==0)? ITEM_USER_TASK_STATUS.SENT:
									((Integer)itemUserProp.getNumber("status")==1)? ITEM_USER_TASK_STATUS.ACCEPTED:
										((Integer)itemUserProp.getNumber("status")==2)? ITEM_USER_TASK_STATUS.DECLINED:
											((Integer)itemUserProp.getNumber("status")==3)? ITEM_USER_TASK_STATUS.COMPLITED:
												ITEM_USER_TASK_STATUS.SENT:ITEM_USER_TASK_STATUS.SENT;
						ITEM_TYPE_PRIORITY_ORDER priority = (itemUserProp.getNumber("priority_order")!=null)?
								((Integer)itemUserProp.getNumber("priority_order")==0)? ITEM_TYPE_PRIORITY_ORDER.IDEAL:
									((Integer)itemUserProp.getNumber("priority_order")==-1)? ITEM_TYPE_PRIORITY_ORDER.DECLINED:
										
											ITEM_TYPE_PRIORITY_ORDER.IDEAL:ITEM_TYPE_PRIORITY_ORDER.IDEAL;	
						// update item_user date_time received date and was_received
						Date receiveDatetime = (itemUserProp.getString("atlas_id").equals(ATLUser.getParseUserID()))?
								new Date() : itemUserProp.getDate("received_datetime");
								boolean received = (itemUserProp.getString("atlas_id").equals(ATLUser.getParseUserID()))?
										true:itemUserProp.getBoolean("was_received");

								itemUserPropertiesRec.setItemUser(webItemUserId,"",null,
										itemType, itemUserProp.getString("web_item_id"), 
										itemUserProp.getString("atlas_id"), status,
										itemUserProp.getDate("status_datetime"), receiveDatetime, 
										priority, (Integer)itemUserProp.getNumber("display_order"), received);
								itemUserPropertiesRec.objectId = itemUserProp.getObjectId();



			}

			return itemUserPropertiesRec;
		}
		
		public static  void refreshAllEventsFromParseInBackground(){
//			@Override
//			protected Void doInBackground(Void... params)  
			{
				ArrayList<ItemUserProperties> allUserItemUsersPropertiesList = new ArrayList<ItemUserProperties>();
				ArrayList<String> webItemIdsList = new ArrayList<String>();
				//			HashMap<String,Object> query = new HashMap<String,Object>();

				//			TABLE_NAME = TABLES_NAME.ITEM_USER.getTableName();
				ParseQuery parseQuery = new ParseQuery(TABLES_NAME.ITEM_USER.getTableName());
				//			userQuery = new ParseQuery(TABLE_NAME);
//				ParseObject atlasServerTable = new ParseObject(TABLES_NAME.ITEM_USER.getTableName());


				parseQuery.whereEqualTo("atlas_id", ATLUser.getParseUserID());
				parseQuery.whereEqualTo("item_type", ITEM_TYPE.EVENT.getItemName());
				//			if (alreadyFetchedItemUserRecords!=null && alreadyFetchedItemUserRecords.size()>0)
				//				parseQuery.whereNotContainedIn("web_item_id", alreadyFetchedItemUserRecords);
				//			parseQuery.whereEqualTo("status", 0);
				parseQuery.findInBackground(new FindCallback() {   
					public void done(List<ParseObject> invitesList, ParseException e)
					{
						if (e==null && invitesList!=null && invitesList.size()>0)
						{
							//// invites list contains all the item_user records
							//// on Parse that the user hasnt answered to yet and not 
							/// on local DB...
							String webItemId = "";  
							ArrayList<ItemUserProperties> itemUserPropertiesList = new ArrayList<ItemUserProperties>();
							final ArrayList<String> webItemIds = new ArrayList<String>();
							final ArrayList<ItemUserProperties> allItemUserRecords = new ArrayList<ItemUserProperties> ();
							final HashMap<String,ItemUserProperties> userMoveRecords = new HashMap<String,ItemUserProperties>();
							ItemUserProperties itemUserProp;
							for (ParseObject respondRec:invitesList)
							{
								if (respondRec!=null)
								{

									///// Fetch the corresponding event....
									webItemId = respondRec.getString("web_item_id");

									if (webItemId!=null && !webItemId.equals(""))
									{

										itemUserProp =  getItemUserPropertiesFromParse(respondRec);
										if (itemUserProp!=null)
										{
											allItemUserRecords.add(itemUserProp);
											itemUserPropertiesList.add(itemUserProp);
											webItemIds.add(webItemId);
											userMoveRecords.put(webItemId,itemUserProp);

										}
									}



								}


							}
							////////////////////////////
							ArrayList<String> webItemIdsList = ATLEventCalendarModel.getAllEventWebEventsIds();
							if (webItemIds!=null && webItemIds.size()>0)
								webItemIdsList.removeAll(webItemIds);
								if (webItemIdsList!=null &&  webItemIdsList.size()>0)
								{
									ATLItemUserModel.deleteItemUserByWebItemId(webItemIdsList);
									ATLEventCalendarModel.deleteEventsByEventWebId(webItemIdsList);
								}
						
							
							
							//////////////
							/////////////
							final HashMap<String, ArrayList<ItemUserProperties>> userMoveRecordsByPrimaryEventId = new HashMap<String, ArrayList<ItemUserProperties>>();
							if (userMoveRecords!=null && userMoveRecords.size()>0)
							{

								Event event = new Event();
								event.parseQuery.whereContainedIn("web_event_id", webItemIds);
								event.parseQuery.findInBackground(new FindCallback() {
									public void done(List<ParseObject> eventList, ParseException e)
									{
										EventProperties eventProp = null;
										final ArrayList<EventProperties> eventListProperties = new ArrayList<EventProperties>();
										String primaryWebEventId = "";
										String webEventId = "";
										if (e==null && eventList!=null && eventList.size()>0)
										{
											for (ParseObject eventRecord:eventList)
											{
												if (eventRecord!=null)
												{
													primaryWebEventId = eventRecord.getString("primary_web_event_id");
													webEventId = eventRecord.getString("web_event_id");
													eventProp = new EventProperties(eventRecord);
													ArrayList<ItemUserProperties> eventsItemUserRecords = null;
													ItemUserProperties itemUserProperties;
													primaryWebEventId = (primaryWebEventId!=null && primaryWebEventId.equals(""))?webEventId:primaryWebEventId;
													if (!userMoveRecordsByPrimaryEventId.containsKey(primaryWebEventId))
													{
														eventsItemUserRecords = userMoveRecordsByPrimaryEventId.get(primaryWebEventId);
													}
													if (eventsItemUserRecords==null)
														eventsItemUserRecords = new ArrayList<ItemUserProperties>();

													itemUserProperties = userMoveRecords.get(webEventId);
													itemUserProperties.setEventAssociated(eventProp);
													eventsItemUserRecords.add(itemUserProperties);
													userMoveRecordsByPrimaryEventId.put(primaryWebEventId, eventsItemUserRecords);

												}
												eventListProperties.add(eventProp);

											}

	    

											/////////
											//// retrieve invitees item user's records also...
											ParseQuery parseQuery2 =  new ParseQuery(TABLES_NAME.ITEM_USER.getTableName());
											parseQuery2.whereNotEqualTo("atlas_id", ATLUser.getParseUserID());
											parseQuery2.whereEqualTo("item_type", ITEM_TYPE.EVENT.getItemName());
											//						if (alreadyFetchedItemUserRecords!=null && alreadyFetchedItemUserRecords.size()>0)
											parseQuery2.whereContainedIn("web_item_id", webItemIds);  
											//						parseQuery.whereEqualTo("status", 0);
											parseQuery2.findInBackground(new FindCallback() {
												public void done(List<ParseObject> invitesList, ParseException e)
												{
													if (e==null && invitesList!=null && invitesList.size()>0)
													{
														//// invites list contains all the item_user records
														//// on Parse that the user hasnt answered to yet and not 
														/// on local DB...
														String webItemId = "";
														ArrayList<ItemUserProperties> itemUserPropertiesList2 = new ArrayList<ItemUserProperties>();
														ArrayList<String> webItemIds2 = new ArrayList<String>();
														final ArrayList<ItemUserProperties> allItemUserRecords2 = new ArrayList<ItemUserProperties> ();
														final HashMap<String,ItemUserProperties> userMoveRecords2 = new HashMap<String,ItemUserProperties>();
														ItemUserProperties itemUserProp;
														for (ParseObject respondRec:invitesList)
														{
															if (respondRec!=null)
															{

																///// Fetch the corresponding event....
																webItemId = respondRec.getString("web_item_id");

																if (webItemId!=null && !webItemId.equals(""))
																{

																	itemUserProp =  getItemUserPropertiesFromParse(respondRec);
																	if (itemUserProp!=null)
																	{
																		allItemUserRecords.add(itemUserProp);
																		itemUserPropertiesList2.add(itemUserProp);
																		webItemIds2.add(webItemId);
																		userMoveRecords2.put(webItemId,itemUserProp);

																	}
																}



															}


														}

														/////
														allItemUserRecords.addAll(allItemUserRecords2);
														/// writing event to local table 
														ATLEventCalendarModel.WriteEventProperties(eventListProperties);
														ATLItemUserModel.UpdateAllItemUserRecordsLocallyInBackground(ITEM_TYPE.EVENT,allItemUserRecords);


														////////
													}



													////////


													else
													{    

													}


												}
											});

										}
										else
										{
										}						

									}});
							}
						}
					}});
//				return null;
				
//				return null;
				//	
			}}
		
		
		
		private ParseDBCallBackInterface parseListener;
		private boolean verifyEmail = false;
		/**
		 * Checks on Parse email_address table whether the is_verified column set to true
		 * and if so update the User table is_atlas_user to true and send true respond
		 * on the isEmailVerifiedCallBack method for listeners
		 */
		public void isEmailVerified(ParseDBCallBackInterface listener)
		{
			parseListener = listener;
			if (ATLUser.getParseUserID()!=null && !ATLUser.getParseUserID().equals("")
					&& ATLUser.getEmail()!=null && !ATLUser.getEmail().equals(""))
			{
				verifyEmail = true;
				EmailAddress emailAddress = new EmailAddress();
				emailAddress.emailAddressListener = this;
				// call back on getEmailAddressRecordCallBack method
				emailAddress.getEmailAddressRecord(ATLUser.getEmail());
			}
			else
			{
				parseListener.isEmailVerifiedCallBack(false);
			}
			
		}
		public  void verifyPrimaryEmailAddress() 
		{
			if (ATLUser.getParseUserID()!=null && !ATLUser.getParseUserID().equals("")
					&& ATLUser.getEmail()!=null && !ATLUser.getEmail().equals(""))
			{
				EmailAddress emailAddress = new EmailAddress();
				emailAddress.emailAddressListener = this;
				/// call back on getEmailAddressRecordCallBack method
				emailAddress.getEmailAddressRecord(ATLUser.getEmail());
				
//				OutboundEmailProperties outboundEmailProperties = new OutboundEmailProperties();
//				outboundEmailProperties.from = 
						
				
			}
			else
			{
				/// user is not validated
			}
			
		}
		@Override
		public void getEmailAddressRecordCallBack(
				EmailAddressRecord emailAddressRecord
				) 
		{

			
			if (emailAddressRecord!=null)
			{
				if (!verifyEmail)
				{
					OutboundEmailProperties outboundEmail = new OutboundEmailProperties();

					outboundEmail.body = ATLEmailTemplate.emailTemplateToVerifyEmailAddress
							(emailAddressRecord.emailAddress, 
									emailAddressRecord.emailAddressId,
									emailAddressRecord.emailAddressId);
					outboundEmail.from = "team@getatlas.com";
					outboundEmail.fromName = "Team Atlas";
					outboundEmail.hasError = false;
					outboundEmail.isPending = false;
					outboundEmail.isSent  = false;
					outboundEmail.subject = ATL_EMAIL_SUBJECT_TITLE.EMAIL_VERIFY.getSubjectName();
					outboundEmail.to = emailAddressRecord.emailAddress;

					ATLOutboundEmail atlOutboundEmail = new ATLOutboundEmail(this);

					atlOutboundEmail.saveOutboundEmailRecordOnParse(outboundEmail,null);
				}
				else
				{
					
					
					if  (emailAddressRecord.isVerified)
					{
						// call back on signInSuccess() method
						ParseUser.logInInBackground(ATLUser.getEmail(), ATLUser.getAtlasPassword(), new LogInCallback() {
							  public void done(ParseUser user, ParseException e) {
							    if (user != null) {
//							    	parseUser.setUser(user);
//									HashMap<String, Object> verifyEmail = new HashMap<String, Object>();
									user.put("is_atlas_user", true);
//									user.put(verifyEmail);
									//  call back on getUpateCallBack
									user.saveInBackground(new SaveCallback()
									{
										public void done(ParseException e) {
										    if (e == null) {
										    	parseListener.isEmailVerifiedCallBack(true);
														verifyEmail = false;
													
										    }else 
										    {
										    	parseListener.isEmailVerifiedCallBack(false);
											      // save didn't succeed. Look at the ParseException
											      // to figure out what went wrong
											    }
										}
									});
							   
							    	
							    } else {
							      // Signup failed. Look at the ParseException to see what happened.
//							    	AtlasServerConnect.userSignInSuccessfuly = false;
							    	parseListener.isEmailVerifiedCallBack(false);

							    }
							  }
							});
						
					}
					else
					{
						parseListener.isEmailVerifiedCallBack(false);
					}

				}

			}
			else
				if (!verifyEmail)
					savedNewOutboundEmailCallBack(false,null); 
				else
					parseListener.isEmailVerifiedCallBack(false);
			

		}
		private boolean eventInviteEmailSent = false;
		private ArrayList<ATLContactModel> nonUsers;
		private ArrayList<EventProperties> eventNonUsers;
		private ArrayList<ItemUserProperties> itemUserNonUsers;
		/**
		 * Sends an email version of the event invite
		 * to a non atlas users
		 * 
		 * @param nonUsers 
		 * @param itemUser
		 */
		public  void sendNonUserEventInvite(
				ArrayList<ATLContactModel> nonUsers,
				ArrayList<EventProperties> event,
				ArrayList<ItemUserProperties> itemUser,
				EmailTemplateType emailTemplateType)
		{
//			Log.v("ATLAS SERVER TABLE", "IN OUTBOUND");
			if (nonUsers!=null && nonUsers.size()>0
					&& event!=null && event.size()>0
					&& itemUser!=null && itemUser.size()>0
					&& emailTemplateType!=null)
			{
				this.nonUsers = nonUsers;

				this.eventNonUsers = event;
				this.itemUserNonUsers = itemUser;
				OutboundEmailProperties outboundEmail = new OutboundEmailProperties();
				ArrayList<String> atlasIdsSent = new ArrayList<String>();
				
				ATLContactModel invitee = nonUsers.get(0);
		  		
//				for (ATLContactModel invitee:nonUsers)
//				{
				ArrayList<ItemUserProperties> itemUserList = new ArrayList<ItemUserProperties>();
					if (invitee!=null && invitee.getAtlasId()!=null
							&&!invitee.getAtlasId().equals("") && 
							!atlasIdsSent.contains(invitee.getAtlasId()))
					{
						atlasIdsSent.add(invitee.getAtlasId());
						outboundEmail = new OutboundEmailProperties();
						for (ItemUserProperties item:itemUser)
							if(item.atlasId.equals(invitee.getAtlasId()))
								itemUserList.add(item);
						
						  Log.v("itemUserList", itemUserList.size() +itemUserList.get(0).atlasId);

						outboundEmail.body = ATLEmailTemplate.emailTemplateForEvent(invitee,event, itemUserList, emailTemplateType);

						
						outboundEmail.from = ATLUser.getEmail();
						outboundEmail.fromName = ATLUser.getUserFirstName()+" "+ATLUser.getUserLastName();
						outboundEmail.hasError = false;
						outboundEmail.isPending = false;
						outboundEmail.isSent  = false;
						outboundEmail.subject = ATL_EMAIL_SUBJECT_TITLE.EVENT_INVITE.getSubjectName();
						outboundEmail.to = invitee.getEmailAddress();

						ATLOutboundEmail atlOutboundEmail = new ATLOutboundEmail(this);
						
						eventInviteEmailSent = true;
						atlOutboundEmail.saveOutboundEmailRecordOnParse(outboundEmail,emailTemplateType);
						
					}
				}

				
//			}
			
		}
		
		@Override
		public void savedNewOutboundEmailCallBack(boolean success,EmailTemplateType emailTemplateType)
		{
			if (success)
			if (eventInviteEmailSent
					&& eventNonUsers!=null && eventNonUsers.size()>0 
					&& itemUserNonUsers!=null && itemUserNonUsers.size()>0)
			{
				
				if (nonUsers!=null && nonUsers.size()>0)
				{
					
					nonUsers.remove(0);
					this.sendNonUserEventInvite(nonUsers, eventNonUsers, itemUserNonUsers,emailTemplateType);
					
				}
				else
				{//// no non users to save on Parse...
//					Log.v("ATLAS SERVER TABLE", "IN OUTBOUND SENT");
				}
			}
			else
			{
				
			}
			

			
		}
		
		
		@Override
		public void getUpateCallBack(boolean success) 
		{
			
				
			
		}

		@Override
		public void signInSuccess(boolean success) 
		{
			
			
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
		


	


//public static void RefreshAllItemUserRecordsInBackground() {
//	RefreshAllItemUserRecordsInBackground refresh = new RefreshAllItemUserRecordsInBackground();
//
//	refresh.execute();
//}





	}
