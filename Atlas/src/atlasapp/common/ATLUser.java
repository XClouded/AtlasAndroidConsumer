package atlasapp.common;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.graphics.Bitmap;



public  class ATLUser {
	
	
	public static enum USER_INFO { USER_NAME, USER_NAME_DISPLAY, PASSWORD, EMAIL, PARSE_ID, FB_ID, IMAGE };
	
//	private static String userName="";
//    public static void setUserName(String currentUserName) { userName = currentUserName; }
//    public static String getUserName() { return userName; }
//    
    private static boolean wasNonUser = false;

    private static boolean isAtlasUser = false;
    private static String displayname="";
    public static void setUserNameDisplay(String currentUserNameDisplay) { displayname = currentUserNameDisplay; }
    public static String getUserNameDisplay() { return displayname; }

    private static String imageUrl="";
    
    private static ArrayList<String> emailAddresses=new ArrayList<String>();
    public static void setEmailAddresses(ArrayList<String> currentUserFirstName) { emailAddresses = currentUserFirstName; }
    public static ArrayList<String> getEmailAddresses() { return emailAddresses; }
    public static void addEmailAddress(String emailAddress){
    	
    	if (emailAddresses==null)
    		emailAddresses = new ArrayList<String>();
    	if (emailAddress!=null && !emailAddress.equals("") && !emailAddresses.contains(emailAddress))
    	{
    		emailAddresses.add(emailAddress);
    		
    	}
    	if (email==null || email.equals(""))
    		email = emailAddress;
    }

    public static void setIsAtlasUser(boolean setIsAtlasUser)
    {
    	isAtlasUser = setIsAtlasUser;
    }
    public static boolean isAtlasUser()
    {
    	return isAtlasUser;
    }
    
    
    
    
    public static void setWasNonUser(boolean setWasNonUser)
    {
    	wasNonUser = setWasNonUser;
    }
    public static boolean wasNonUser()
    {
    	return wasNonUser;
    }
    
    public static void setImageUrl(String setImageUrl)
    {
    	imageUrl = setImageUrl;
    }
    public static String getImageUrl()
    {
    	return imageUrl;
    }
    private static String firstName="";
    public static void setFirstName(String currentUserFirstName) { firstName = currentUserFirstName; }
    public static String getUserFirstName() { return firstName; }

    private static String lastName="";
    public static void setLastName(String currentUserLastName) { lastName = currentUserLastName; }
    public static String getUserLastName() { return lastName; }
    
    
    
    private static String email="";
    public static void setEmail(String currentemail) { email = currentemail; addEmailAddress(email); }
    public static String getEmail() { return email; }
    
    
    private static String fbID="";
    public static void setfbID(String currentfbID) { fbID = currentfbID; }
    public static String getfbID() { return fbID; }
 
    
    
    private static Bitmap picture;
    public static void setPicture(Bitmap currentpicture) { picture = currentpicture; }
    public static Bitmap getPicture() { return picture; }
    
    
    
    private static String password="";
    public static void setAtlasPassword(String currentPassword) { password = currentPassword; }
    public static String getAtlasPassword() { return password; }
    
    
    private static boolean loggedIn=true;
    public static void setLoggedIn(boolean currentLoggedIn) { loggedIn = currentLoggedIn; }
    public static boolean isLoggedIn() { return loggedIn; }
    
    
    private static String parseUserID="";
    public static void setParseUserID(String currentParseUserID) { parseUserID = currentParseUserID; }
    public static String getParseUserID() { return parseUserID; }
        
      
    
    private static boolean hasLocalProfile=false;
    public static void sethasLocalProfile(boolean currentHasLocalProfile) { hasLocalProfile = currentHasLocalProfile; }
    public static boolean doesHaveLocalProfile() { return hasLocalProfile; }
    
    
    
    private static boolean userSignonServer=false;
    public static void setUserSignonServer(boolean currentUserSignonServer) { userSignonServer = currentUserSignonServer; }
    public static boolean isUserSignonServer() { return userSignonServer; }
	
    
    private static String accessToken="";
    public static void setAccessToken(String currentAccessToken) { accessToken = currentAccessToken; }
    public static String getAccessToken() { return accessToken; }
    
   
    public static enum USERS_SHARED_PREFERENCES_PROPERTY { FIRST_NAME,LAST_NAME,  EMAIL, PASSWORD, IMAGE, fbID, ACCESS_TOKEN, OTHER}
   public static void setAtlasAndroidUserProperties(HashMap<String, String> userProperties)
   {
	   Iterator userInfoToUpdate = userProperties.keySet().iterator();
		
		while(userInfoToUpdate.hasNext())
		{
		    String key=(String)userInfoToUpdate.next();
		    String value=(String)userProperties.get(key);
		    
		    USERS_SHARED_PREFERENCES_PROPERTY userInfo = 
		    		(key.equals("email"))? USERS_SHARED_PREFERENCES_PROPERTY.EMAIL:
		    		(key.equals("password"))? USERS_SHARED_PREFERENCES_PROPERTY.PASSWORD :
		    		(key.equals("password_copy"))? USERS_SHARED_PREFERENCES_PROPERTY.PASSWORD :
		    	    (key.equals("facebook_id"))? USERS_SHARED_PREFERENCES_PROPERTY.fbID:
		    	    (key.equals("fbID"))? USERS_SHARED_PREFERENCES_PROPERTY.fbID:
			    	(key.equals("last_name"))? USERS_SHARED_PREFERENCES_PROPERTY.LAST_NAME:

		    	    (key.equals("first_name"))? USERS_SHARED_PREFERENCES_PROPERTY.FIRST_NAME:
					(key.equals("access_token"))? USERS_SHARED_PREFERENCES_PROPERTY.ACCESS_TOKEN:
						USERS_SHARED_PREFERENCES_PROPERTY.OTHER;
		    
		   switch (userInfo)
		   {  
		   case FIRST_NAME: setUserNameDisplay(value);
		   					setFirstName(value);
		   		break;
		   case LAST_NAME: 
					setLastName(value);
				break;
		   case EMAIL: addEmailAddress(value);
		   		break;
		   case PASSWORD: setAtlasPassword(value);
		   		break;
		   case ACCESS_TOKEN: setAccessToken(value);
		   		break;
		   case fbID: setfbID(value);
		   		break;
//		   case IMAGE: setPicture(value);
//		   		break;
		   }
   }
   }
    
    
}
