package com.atlasapp.section_appentry;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;



import com.atlasapp.atlas_database.ATLDBCommon;
import com.atlasapp.atlas_database.AtlasServerConnect;
import com.atlasapp.atlas_database.DatabaseConstants.SIGN_NEW_USERS_CALLER;
import com.atlasapp.atlas_database.UserDelagateInterface;
import com.atlasapp.common.ATLCurrentsUserAlerts;
import com.atlasapp.common.AtlasAndroidUser;
import com.atlasapp.common.DB;
import com.atlasapp.common.Utilities;
import com.atlasapp.common.UtilitiesProject;
import com.atlasapp.facebook.FacebookUtilities;
import com.atlasapp.facebook.ProfilePictureView;
import com.atlasapp.facebook.SessionEvents;
import com.atlasapp.facebook.SessionStore;
import com.atlasapp.model.ATLFriendModel;
import com.atlasapp.model.ATLContactModel;
import com.atlasapp.model.ATLContactModel;
import com.facebook.GraphUser;
import com.facebook.android.*;
import com.facebook.Session;
import com.facebook.android.AsyncFacebookRunner;


import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;

import com.parse.ParseUser;




import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

public class AtlasApplication extends Application implements UserDelagateInterface{
	
	
	private Collection<GraphUser> selectedUsers;

	
	public static enum USERS_SHARED_PREFERENCES_TYPE { STRING,  FILE, BOOLEAN, INT, BYTE}

	public static final String PREFS_NAME = "AtlasProfileLogin";
	public boolean localAtlasProf;
	private String userNameDisplay;
	private String email;
	private String password;
	private String fbID;
	private String access_token;
	private String previouslyEncodedImage;
	private String parseUserID;
	private boolean loggedIn;
	private boolean userSignonServer = false;
	private ATLCurrentsUserAlerts atlCurrentUserAlerts;
	private ATLDBCommon atlDBCommon;


	
	// Friends from FB that are already on local Friend DB
	//public static ArrayList<ATLContactModel> getAtlasFBFriendsonDB () {return UtilitiesProject.getAtlasFBFriendsonDB();} ;
//	public static ArrayList<ATLContactModel> getNonAtlasContacts () {return UtilitiesProject.getNonAtlasContacts();} ;
	
//	public  static ArrayList<ATLContactModel> getNewAtlasFriends() {return UtilitiesProject.getNewAtlasFriends();} ;
	
	
	
	
	private AtlasServerConnect parseConnect;

	public static  String destinationImagePath= "/image.png";
	
	public static File IMAGE_DIR = new File(Environment.getExternalStorageDirectory()
              + "/Android/data/com.atlastpowered/files/Pictures");
	
	
	public static File FRIENDS_IMAGE_DIR = new File(Environment.getExternalStorageDirectory()
            + "/Android/data/com.atlastpowered/files/Pictures/friendPics");
	
	
	public static File PROFILE_PIC_PATH= new File (IMAGE_DIR,destinationImagePath) ;
	
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		super.onCreate();
		
		   parseConnect = 
				   AtlasServerConnect.getSingletonObject(null);
		 
		
		 Utilities.init(getApplicationContext());
	     UtilitiesProject.init(getApplicationContext());
	     
	     if (!IMAGE_DIR.exists() ) {
	            IMAGE_DIR.mkdirs();
	            FRIENDS_IMAGE_DIR.mkdirs();
	        }
	     if (!FRIENDS_IMAGE_DIR.exists() ) {
	            FRIENDS_IMAGE_DIR.mkdirs();
	        }
	     FacebookUtilities.mFacebook = new Facebook(getResources().getString(R.string.facebook_app_id));
        
	   
		String applicationId = getResources().getText(R.string.parse_app_id).toString();
		String clientKey = getResources().getText(R.string.parse_client_key).toString();
		String facebookId = getResources().getText(R.string.facebook_app_id).toString();
		

		
		////// PARSE INITIALIZE
		Parse.initialize(this, applicationId, clientKey); 
		parseConnect.setParseInstallation();
		ParseACL defaultACL = new ParseACL();
	    
		//If you would like all objects to be private by default, remove this line.
	    defaultACL.setPublicReadAccess(true);
		//ParseACL.setDefaultACL(defaultACL, true);
		  
		////////////////////
		
		
		
		localAtlasProf = doesUerHasLocalProfile();
		
		if (localAtlasProf)
		{
			
 			upadteAtlasAndroidUserObjectFromApp();
			atlCurrentUserAlerts = ATLCurrentsUserAlerts.getSingletonObject(getApplicationContext());
			atlCurrentUserAlerts.refresAllUserAlerts();
 			if (!AtlasAndroidUser.getAccessToken().equals(""))
 			{
// 				AtlasAndroidUser.setAccessToken(Session.getActiveSession()
//                        .getAccessToken());
// 				
 				Session session  = Session.openActiveSession(this);
 				String acess = session.getAccessToken();
 				FacebookUtilities.mFacebook.setAccessToken(acess);
 				 FacebookUtilities.mFacebook.setAccessExpires(Session.getActiveSession()
                         .getExpirationDate()
                         .getTime());
 				//FacebookUtilities.mFacebook.setAccessToken(AtlasAndroidUser.getAccessToken());
 				FacebookUtilities.mAsyncRunner = new AsyncFacebookRunner( FacebookUtilities.mFacebook);
 			
 				FacebookUtilities.setFriendsRequest(getApplicationContext());
 				
 				
 				

 			}
		}else
		{
			/// create a new user
			
		}
		
		
		
	}  
	
	
	
	/**
	 * Search the user's contacts list for contacts that have atlas account
	 * and update the user's local DB
	 */
//	public  String updateAddressBookFriends()
//	{
//		String message = "";
//		message = UtilitiesProject.updateUsersContactsFromAdressBook();
//	//	UtilitiesProject.updateFriendOnDB();
//		UtilitiesProject.retreiveFriendsPicturesInBackground("address");
//		return message;
//	}
	/**
	 * Search the user's facebook friends id'd list for friends that have atlas account
	 * and update the user's local DB
	 */
//	public  String  updateFacebookFriends()
//	{
//		String message = "";
//		message = 
//		UtilitiesProject.updateFacebookFriendsOnDB();
//	//	UtilitiesProject.updateFriendOnDB();
//		UtilitiesProject.retreiveFriendsPicturesInBackground("facebook");
//		return message;
//	}

	
	public String getLocalEmail()
	{
		return email;
	}
	
	
	
	/**
	 * Verify email and password against Local 
	 * Atlas profile on mobile's SharedPreferences
	 * @param email - got from user Input
	 * @param password - got from user Input
	 * @return
	 */
	public boolean userLocalAccountVerified(String email, String password)
	{
		return this.email.equals(email) && this.password.equals(password);
	}
	
	public boolean isFacebookUser()
	{
		return !fbID.equals("");
	}
	/**
	 * Update AtlasAndroidUser object class from Application properties 
	 */
	public void upadteAtlasAndroidUserObjectFromApp()
	{
		
		if (!fbID.equals("")&&!access_token.equals(""))
		{
			//AtlasFacebookConnect.setAccessToken(access_token);
		//AtlasFacebookConnect.setfbID(fbID);
		}
		
		
		
		AtlasAndroidUser.sethasLocalProfile(localAtlasProf);
		/// update user's account info
		AtlasAndroidUser.setEmail(email);
		AtlasAndroidUser.setAccessToken(access_token);
		AtlasAndroidUser.setfbID(fbID);
		AtlasAndroidUser.setUserNameDisplay(userNameDisplay);
		AtlasAndroidUser.setParseUserID(parseUserID);
		AtlasAndroidUser.setAtlasPassword(password);
		AtlasAndroidUser.setLoggedIn(loggedIn);
		AtlasAndroidUser.setUserSignonServer(userSignonServer);
	}
	/**
	 * checks on user SharedPrefernces file to see if the user already has
	 * local account and update Application user's profile info from it
	 * @return true if user got atlas local profile (on Shared Preferences)
	 */
	private boolean doesUerHasLocalProfile() {
		 
		
		boolean hasLocalProfile = false;
		
		
		upadteApplicationUsersPropertiesFromLocal();
		
		hasLocalProfile = !email.equals("")&& !userNameDisplay.equals("")  
 			   && !password.equals("") ;
		
		
		
		return hasLocalProfile;
	}
	
	/**
	 * Update Local user SharedPreferences File from AtlasAndroidUser Object Info
	 */
	public void setAtlasUserLocalInfo()
	{
		  SharedPreferences sp=getSharedPreferences(PREFS_NAME, 0);
		   SharedPreferences.Editor Ed=sp.edit();
    
		 //  Ed.putString("userName",AtlasAndroidUser.getUserName());
		   Ed.putString("facebook_id",AtlasAndroidUser.getfbID());
		   Ed.putString("access_token", AtlasAndroidUser.getAccessToken());
		   Ed.putString("parseUserID",AtlasAndroidUser.getParseUserID());
		   Ed.putString("password_copy",AtlasAndroidUser.getAtlasPassword());
		   
		   
		   
		   
		   
		   
	//	   Ed.putString("profile_pic",AtlasAndroidUser.getPicture());
		   Ed.putString("first_name",AtlasAndroidUser.getUserNameDisplay());
		   Ed.putString("email",AtlasAndroidUser.getEmail());
		   Ed.putBoolean("loggedIn",true);
		    
		   
		   if (AtlasAndroidUser.getPicture()!=null)
			   storePicture("image", AtlasAndroidUser.getPicture());
		      
		   
		   Ed.commit();
	}
	/**
	 * Log Atlas user In or Out of the app & Facebook. and
	 * update Local Profile SharedPreferences
	 * @param logIn - true for logIn
	 */
	public void logInOrOutUser(boolean logIn)
	{
		 SharedPreferences sp=getSharedPreferences(PREFS_NAME, 0);
		   SharedPreferences.Editor Ed=sp.edit();
		   Ed.putBoolean("loggedIn",logIn);
		    AtlasAndroidUser.setLoggedIn(logIn);
		      
		   
		   Ed.commit();
		   loggedIn = logIn;
		   
		   // log user out of Faecbook
		   //Closes the local in-memory Session object,
		   //but does not clear the persisted token cache.
//		   Session session  = Session.getActiveSession();
//		   if (!logIn && session!=null ){
//			  
//			   facebook.setAccessToken(null);
//		        facebook.setAccessExpires(-1);
//			   session.close();
//		   }
	}
	
	
	public  void createLocalProfileFromServerProfile() {
		
		boolean success = false;
		
		//ParseUser user = ParseUser.getCurrentUser();
		 parseConnect.setCurrentAtlasAndroidUserFromServer();
		
//		if (success)
//		{
			AtlasAndroidUser.setUserSignonServer(true);
			// ParseAtlasApplication applicationController = (ParseAtlasApplication)getApplicationContext();
			setAtlasUserLocalInfo();
//		}
//		
//		return success;
	}
	/**
	 * Gets ParseUser info from server and update local user info 
	 */
	public  void createLocalProfileFromServerProfile(String userName, String password) {
		
		boolean success = false;   
		parseConnect.logUserToServer(userName, password,this);
//		if (parseConnect.userSignInSuccessfuly)
//		{
//			success = parseConnect.setAtlasAndroidUserFromServer(userName,password);
//			if (success){
//				AtlasAndroidUser.setUserSignonServer(true);
//				// ParseAtlasApplication applicationController = (ParseAtlasApplication)getApplicationContext();
//				setAtlasUserLocalInfo();
//			}
//		}
//		return success;
	} 
	
	/**
	 * Update the the application properties from the local (Shared Preferences)
	 * profile info.
	 */
	public void upadteApplicationUsersPropertiesFromLocal()
	{
		SharedPreferences sp1= getSharedPreferences(PREFS_NAME,0);
		 /// UNCOMMENT FOR DELETING USER ATLAS SHARED PREFERNCES ON MOBILE
		 SharedPreferences.Editor Ed=sp1.edit();
//		Ed.clear();  
//		Ed.commit();      
	   	          
		 if (sp1!=null){   
	    	    email=sp1.getString("email", "");       
	    	    userNameDisplay = sp1.getString("first_name", "");
	    	   
	    	      
	    	   // userName=sp1.getString("userName", "");       
	    	    password = sp1.getString("password_copy", "");
	    	   
	    	     
	    	    fbID=sp1.getString("facebook_id", "");       
	    	//    previouslyEncodedImage = sp1.getString("profile_pic", "");
	    	    access_token = sp1.getString("access_token", ""); 
	    	    
	    	    parseUserID = sp1.getString("parseUserID", "");   
	    	    
	    	    
//	    	    if (parseUserID.equals("") && !email.equals("")){
//	    	    	parseUserID = ((ParseUser)parseConnect.getParseUserByEmail(email)).getObjectId();
//	    	    	Ed.putString("parseUserID",parseUserID);
//	    	    	Ed.commit();
//	    	    }
//	    	    	
	    	    
	    	       
	    	    loggedIn = sp1.getBoolean("loggedIn", true);
	    	    
	    	    
	    	   
	    	   
	    	  
	    	   
	    	 //  userSignonServer =  !parseUserID.equals("");
	    	  
	       }
	}
	public boolean isUserSignOnServer()
	{
		return !parseUserID.equals("");
	}
	/**
	 *   
	 * Update the atlas user's profile info list on Shared Preferences on mobile and the AtlasAndroidUser object accordingly
	 * @param userFacebookInfo list of atlas user info to be updated on shared preferences
	 */
	public  void setAtlasSharedPreferences(HashMap<String, Object> userFacebookInfo)
	{
		SharedPreferences sp=getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor Ed=sp.edit();
		USERS_SHARED_PREFERENCES_TYPE userPrefType;
		Iterator userInfoToUpdate = userFacebookInfo.keySet().iterator();
		
		while(userInfoToUpdate.hasNext())
		{
		    String key=(String)userInfoToUpdate.next();
		    String value=(String)userFacebookInfo.get(key);
		    
		    
		    userPrefType = (key.equals("loggedIn"))? USERS_SHARED_PREFERENCES_TYPE.BOOLEAN:
		    	(key.equals("picture"))? USERS_SHARED_PREFERENCES_TYPE.FILE: 
					USERS_SHARED_PREFERENCES_TYPE.STRING;
		    
		    if (!value.equals(""))
		    	switch (userPrefType)
		    	{
		    	case BOOLEAN :
		    		boolean valueType = (value.equals("TRUE"));
		    		 Ed.putBoolean(key,valueType);
		    		break;
		    		
		    	case STRING:
		    		 Ed.putString(key,value.toString());
		    		 HashMap<String, String> meMap=new HashMap<String, String>();
		    		 meMap.put(key,value);
		    		 AtlasAndroidUser.setAtlasAndroidUserProperties(meMap);
		    		break;
		    	
//		    	case FILE:
//		    		try {
//		    		// Copy image from one file path to atlas path on mobile
//		    		// where VALUE represent the source file path
//		    		    if (IMAGE_DIR.canWrite()) {
//		    		        String sourceImagePath= value.toString();//"/path/to/source/file.jpg";
//		    		       
//		    		        File source= new File(sourceImagePath);
//		    		        File destination= new File(IMAGE_DIR, destinationImagePath);
//		    		        if (source.exists()) {
//		    		            FileChannel src = new FileInputStream(source).getChannel();
//		    		            FileChannel dst = new FileOutputStream(destination).getChannel();
//		    		            dst.transferFrom(src, 0, src.size());
//		    		            src.close();
//		    		            dst.close();
//		    		        }
//		    		    }
//		    		} catch (Exception e) {}
//		    		break;
		    	
		    	}
		    	}  
		    
		    Ed.commit();
		    
		}
	
		
	 public Collection<GraphUser> getSelectedUsers() {
	        return selectedUsers;
	    }

	    public void setSelectedUsers(Collection<GraphUser> selectedUsers) {
	        this.selectedUsers = selectedUsers;
	    }

	    
		public boolean isFacebookUser(String id) {
			
			return id.equals(fbID);
		}
//	/**
//	 * Setting the user's contcats information on 
//	 * AtlasFriendInfo[] atlasContacts, allContacts;
//	 * (connecting to the server for finding contacts on Atlas)	
//	 */
//	public static void setUserContactsInfo()
//	{
//		atlasContacts = UtilitiesProject.getAtlasContactsFriends();
//	    allContacts = UtilitiesProject.getAllContactsFriends();
//	}
//	/**
//	 * Get all contacts info
//	 * @return Array of AtlasFriendInfo
//	 */
//	public ArrayList<AtlasFriendInfo> getAllContacts()
//	{
//		return allContacts;
//	}
//	/**
//	 * Get all atlas contacts info
//	 * @return Array of AtlasFriendInfo
//	 */
//	public ArrayList<AtlasFriendInfo> getAtlasContacts()
//	{
//		return atlasContacts;
//	}
	/**
	 * Gets contacts info (type AtlasFriendInfo) and update the user's Friend DB
	 * 
	 * @param contacts Array List of AtlasFriendInfo
	 */ 
// HARRIS NEW:
//	public void setOnFriendDB(ArrayList<ATLContactModel> contacts)
	public void setOnFriendDB(ArrayList<ATLContactModel> contacts)
	{
		ATLFriendModel contact = new ATLFriendModel();
		for (int i=0; i<contacts.size(); i++){
			contact.setEmailAddress(contacts.get(i).getEmailAddress());
			contact.setFirstname(contacts.get(i).getFirstname());
			contact.setLastname(contacts.get(i).getLastname());

			contact.setPhoneNumber(contacts.get(i).getPhoneNumber());
			//contact.setFriendId(contacts[i].getObjectId());
			
			contact.setFromFacebook(contacts.get(i).isFromFacebook());
			
			contact.setFromAddressBook(contacts.get(i).isFromAddressBook());
			
			
			contact.write();
			
// HARRIS NEW:
//			Bitmap pic = contacts.get(i).getImage();
			Bitmap pic = contacts.get(i).getImage();
			if (pic!=null){
				
				
				String picPath = String.valueOf(contact.getFriendId());
				
				
				storePicture(picPath,pic);
				
//		           ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//			 pic.compress(Bitmap.CompressFormat.PNG, 100, baos);   
//	           byte[] b = baos.toByteArray(); 
//			  String  encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
//			  
//			  
//			  
//			  
//			  
			  
			
			  contact.setPicPath(IMAGE_DIR+"/"+picPath+".png");
			  contact.write();
			  
			}
			
		}
		
		
		
	}
	
	
	
//	public void storePictureFromUri(String picName, Uri sourcePicUri)
//	{
//		try{
//	    if (IMAGE_DIR.canWrite()) {
//	       
//	        File source= new File(sourcePicUri.getPath());
//	        File destination= new File(IMAGE_DIR, "/"+picName+".png");
//	        if (source.exists()) {
//	            FileChannel src = new FileInputStream(source).getChannel();
//	            FileChannel dst = new FileOutputStream(destination).getChannel();
//	            dst.transferFrom(src, 0, src.size());
//	            src.close();
//	            dst.close();
//	        }
//	    }
//		} catch (Exception e) {}
//	}
	
	
	/**
	 * Store picture on user's local atlas mobile file
	 * @param picName  picture stored name (with no extension)
	 * @param bitPic the picture to be stored in Bitmap
	 */
	public void storeFriendPicture(String picName, Bitmap bitPic)
	{
		if (bitPic!=null && picName!="")
		try {
		// Copy image from one file path to atlas path on mobile
		// where VALUE represent the source file path
		    if (FRIENDS_IMAGE_DIR.canWrite()) {
		       
		    	FileOutputStream fOut=new FileOutputStream(FRIENDS_IMAGE_DIR+"/"+picName+".png"); 
		    	// Here path is either sdcard or internal storage
		    	bitPic.compress(Bitmap.CompressFormat.PNG,100,fOut);
		    	fOut.flush();
		    	fOut.close();
		    	bitPic.recycle(); // If no longer used..
		        }
		    
		} catch (Exception e) {}
	}
	
	

	

	/**
	 * Store picture on user's local atlas mobile file
	 * @param picName  picture stored name (with no extension)
	 * @param bitPic the picture to be stored in Bitmap
	 */
	public void storePicture(String picName, Bitmap bitPic)
	{
		if (bitPic!=null && picName!="")
		try {
		// Copy image from one file path to atlas path on mobile
		// where VALUE represent the source file path
		    if (IMAGE_DIR.canWrite()) {
		       
		    	FileOutputStream fOut=new FileOutputStream(IMAGE_DIR+"/"+picName+".png"); 
		    	// Here path is either sdcard or internal storage
		    	bitPic.compress(Bitmap.CompressFormat.PNG,100,fOut);
		    	fOut.flush();
		    	fOut.close();
		    	bitPic.recycle(); // If no longer used..
		        }
		    
		} catch (Exception e) {}
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
		if (success){
			parseConnect.setAtlasAndroidUserFromServer();
		
			AtlasAndroidUser.setUserSignonServer(true);
			// ParseAtlasApplication applicationController = (ParseAtlasApplication)getApplicationContext();
			setAtlasUserLocalInfo();
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
	public void getUpateCallBack(boolean success) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void getSignNonAtlasUsersCallBack(SIGN_NEW_USERS_CALLER caller,
			boolean success,
			HashMap<String, ATLContactModel> newCurrentNonAtlasUserToSign) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
