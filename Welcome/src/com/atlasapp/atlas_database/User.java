package com.atlasapp.atlas_database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

//import com.atlasapp.atlas_database.AtlasServerTable.LongOperation;
import com.atlasapp.atlas_database.DatabaseConstants.OPERATION_METHOD;
import com.atlasapp.atlas_database.DatabaseConstants.SERVER_MESSAGE;
import com.atlasapp.atlas_database.DatabaseConstants.USER_INFO;
import com.atlasapp.common.ATLFriendLocalTable;
import com.atlasapp.common.AtlasAndroidUser;
import com.atlasapp.common.UtilitiesProject;
import com.atlasapp.model.ATLContactModel;
import com.atlasapp.section_appentry.AtlasApplication;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class User extends AtlasServerTable implements ParseDBCallBackInterface{
	
	
	public UserDelagateInterface userDelagator;
	private ArrayList<String> allFacebookAtlasUsersFBID;
	private AtlasServerConnect parseConnect =  AtlasServerConnect.getSingletonObject(null);
	public User(ParseObject parseUser)
	{
		parseCallBackDeleget = this;
		if (parseUser!=null)
		{
			atlasServerTable = new ParseUser();
			connecting = true;
	//		longOperation = new LongOperation();
	//		longOperation.execute("sign_in_friend",parseUser.getString("email"), parseUser.getString("passwordCopy"));
			performParseCallOperation(new String[]{"sign_in_friend",parseUser.getString("email"), parseUser.getString("password_copy")});

//			while (connecting)
//			{
//			 try {
//			      Thread.sleep(1000);
//			 } catch (InterruptedException e) {
//			      e.printStackTrace();
//			   }
//			   continue;
//			}
			
		}
		
	}
	public User(String userName, String password)
	{
		parseCallBackDeleget = this;
		atlasServerTable = new ParseUser();
		connecting = true;
		//longOperation = new LongOperation();
		//longOperation.execute("sign_in_friend",userName, password);
		performParseCallOperation(new String[]{"sign_in_friend",userName, password});

//		while (connecting)
//		{
//		 try {
//		      Thread.sleep(1000);
//		 } catch (InterruptedException e) {
//		      e.printStackTrace();
//		   }
//		   continue;
//		}
	}
	public User()
	{
		
		atlasServerTable =  new ParseUser();
		
		parseCallBackDeleget = this;
	}
	public void setUser(ParseUser parseUser)
	{
		atlasServerTable = parseUser;
		parseCallBackDeleget = this;
	}

	
	
	public void register() {
		parseCallBackDeleget = this;
		((ParseUser)atlasServerTable).setUsername(AtlasAndroidUser.getEmail()); 
		((ParseUser)atlasServerTable).setPassword(AtlasAndroidUser.getAtlasPassword());
		((ParseUser)atlasServerTable).setEmail(AtlasAndroidUser.getEmail());
		performParseCallOperation(new String[]{"register" });

	
//		connecting = true;
//		longOperation = new LongOperation();
//		longOperation.execute("register");
		
//		while (connecting)
//		{
//		 try {
//		      Thread.sleep(1000);
//		 } catch (InterruptedException e) {
//		      e.printStackTrace();
//		   }
//		   continue;
//		}
//		
//		
		
	}
//	
	public void signInNewUser(String email,String name)
	{
		/// call back from signInNewFriendUserSuccess
		performParseCallOperation(new String[]{"sign_in_new",email,email,name});

	}
	@Override
	public void signInNewFriendUserSuccess(boolean success, ParseUser user) {
		if (success)
		{
			
		}
		else
		{
			
		}
	}
	
	public void signIn(String username, String password)
	{
		
		parseCallBackDeleget = this;
		AtlasAndroidUser.setEmail(username);
		AtlasAndroidUser.setAtlasPassword(password);
		
//		connecting = true;
//		longOperation = new LongOperation();
//		longOperation.execute("sign_in");
		performParseCallOperation(new String[]{"sign_in" });
//
//		while (connecting)
//		{
//		 try {
//		      Thread.sleep(1000);
//		 } catch (InterruptedException e) {
//		      e.printStackTrace();
//		   }
//		   continue;
//		}
	}
	public String getUsername()
	{
		parseCallBackDeleget = this;
		return ((ParseUser)atlasServerTable).getUsername();
	}
	public String getEmail()
	{
		parseCallBackDeleget = this;
		return ((ParseUser)atlasServerTable).getEmail();
	}
	public String getObjectId()
	{
		parseCallBackDeleget = this;
		return ((ParseUser)atlasServerTable).getObjectId();
	}
	@Override
	public SERVER_MESSAGE put(HashMap<String,Object> columnValues, UserDelagateInterface listener)
	{
		parseCallBackDeleget = this;
		USER_INFO userInfo;
		
		Iterator userInfoToUpdate = columnValues.keySet().iterator();
		this.userDelagator = listener;
		/// Going through the list of user info updates updates 
		while(userInfoToUpdate.hasNext())
		{
		    String key=(String)userInfoToUpdate.next();
		    String value=(String)columnValues.get(key);
		    
			updateTableMessage = SERVER_MESSAGE.FAIL;

		    userInfo = (key.equals("email"))? USER_INFO.EMAIL:
				(key.equals("password"))? USER_INFO.PASSWORD :
				 USER_INFO.OTHER;
		    
		    if (!value.equals(""))
		    	switch (userInfo)
		    	{
					case PASSWORD:
						((ParseUser)atlasServerTable).setPassword(value);
						((ParseUser)atlasServerTable).put("password_copy", value);
						updateTableMessage = SERVER_MESSAGE.SUCCESS;
						break;
					
					case EMAIL:
						((ParseUser)atlasServerTable).setEmail(value);
						((ParseUser)atlasServerTable).setUsername(value);// setting the user name as email
						updateTableMessage = SERVER_MESSAGE.SUCCESS;
						break;
					default:
						((ParseUser)atlasServerTable).put(key, value);
						updateTableMessage = SERVER_MESSAGE.SUCCESS;
						break;
					
		    	}
		
		}
		update();
		return updateTableMessage;
		
	}
//	public void getAllUsersUserNamesOnTable() {
//		parseCallBackDeleget = this;
//		allUserNamesOnTable = new ArrayList<String>() ;
//		allUsersOnTable = null ;
//		
//		
//		
//		
//		userQuery  = ParseUser.getQuery();
//		userQuery.whereExists("email");
//		
//		
//		
//		connecting = true;
//		longOperation = new LongOperation();
//		longOperation.execute("all_user_names");
//		
//		while (connecting)
//		{
//		 try {
//		      Thread.sleep(1000);
//		 } catch (InterruptedException e) {
//		      e.printStackTrace();
//		   }
//		   continue;
//		}
//		
//	}
	
	
	/**
	 * Return all the current  users on Atlas Server DB (Parse)
	 * @return ArrayList of all  Atlas users each in a ATLContactModel type
	 * without pictures...
	 */
	public  void  setAllUsers(final boolean retrieveFriends)
	{
		parseCallBackDeleget = this;
		
		final ATLDBCommon atlDBCommon = ATLDBCommon.getSingletonObject(null);
		
		allContactsmodel = new HashMap<String, ATLContactModel>();
		allFBUsersModelOnTable =  new HashMap<String, ATLContactModel>();
		allFBUserNamesOnTable =  new ArrayList<String>();
		allUserNamesOnTable = (allUserNamesOnTable == null ||  allUserNamesOnTable.size()==0)?new ArrayList<String>():allUserNamesOnTable ;
		allUsersOnTable = (allUsersOnTable==null)? null:allUsersOnTable ;
		allFBUserFacebookIdOnTable = new ArrayList<String>();
		
		userQuery  = ParseUser.getQuery();
		userQuery.whereExists("email");
		
		userQuery.findInBackground(new FindCallback() {
		    public void done(List<ParseObject> scoreList, ParseException e) {
		        if (e == null) {
		        	String email = "";
		        	allUsersOnTable = scoreList;
					if (allUsersOnTable!=null && allUsersOnTable.size()>0)
					{
						for (ParseObject user:allUsersOnTable)
						{
							email = user.getString("email");
							if (email!=null && !email.equals(""))
								allUserNamesOnTable.add(email);
						}
						
					}
					updateTableMessage = SERVER_MESSAGE.SUCCESS;
					
					if (allUsersOnTable!=null && allUsersOnTable.size()>0)
					{
						for (ParseObject atlasUser :allUsersOnTable)
						{
							
							if (atlasUser.getString("email")!=null && !atlasUser.getString("email").equals(""))
							{
							
							ATLContactModel atlContactModel = getATLContactModelFromParseUser(atlasUser);
//							atlContactModel.setAddressBookId("");
//							atlContactModel.setAtlasId(atlasUser.getObjectId());
//							atlContactModel.setEmailAddress(atlasUser.getString("email"));
//							atlContactModel.setFacebookId(atlasUser.getString("fbID"));
//							atlContactModel.setFirstname(atlasUser.getString("displayname"));
//							atlContactModel.setLastname("");
//							 
							

							allContactsmodel.put(atlContactModel.getEmailAddress(),atlContactModel);
							if (atlContactModel != null && atlContactModel.getFacebookId()!=null && !atlContactModel.getFacebookId().equals(""))
							{
								allFBUsersModelOnTable.put(atlContactModel.getFacebookId(), atlContactModel);
								allFBUserNamesOnTable.add(atlContactModel.getEmailAddress());
								allFBUserFacebookIdOnTable.add(atlContactModel.getFacebookId());
							}
							allUserNamesOnTable.add(atlContactModel.getEmailAddress());
						}
						}
						
					}  
					
					atlDBCommon.setCurrentSessionEmailsOnATLAS(allContactsmodel);
					atlDBCommon.setCurrentSessionUsersEmails(allUserNamesOnTable);
					atlDBCommon.setCurrentSessionUsersFacebookId(allFBUsersModelOnTable);
					atlDBCommon.setCurrentSessionFacebookUsersUserNames(allFBUserNamesOnTable);
					atlDBCommon.setCurrentSessionFacebookUsersFacebookId(allFBUserFacebookIdOnTable);
				//	if (retrieveFriends)
				//		.updateAllFriendOnDBInBackground();
					ATLFriendLocalTable.updateAllFriendOnDBInBackground();
		        } else {
		        }
		    }
		    
		});
	
		
	
		
		
	
	}
	
	private ATLContactModel getATLContactModelFromParseUser(ParseObject atlasUser)
	{
		parseCallBackDeleget = this;
		ATLContactModel atlContactModel = new ATLContactModel();
		if (atlasUser!=null)
		{
			atlContactModel.setAddressBookId("");
			atlContactModel.setAtlasId(atlasUser.getObjectId());
			atlContactModel.setEmailAddress(atlasUser.getString("email"));
			atlContactModel.setFacebookId(atlasUser.getString("facebook_id"));
			atlContactModel.setFirstname(atlasUser.getString("first_name"));
			atlContactModel.setIsAtlasUser(atlasUser.getBoolean("is_atlas_user"));
			atlContactModel.setLastname("");
		}
		return atlContactModel;
	}
	@Override
	public void getFindQueryCallBack(List<ParseObject> foundQueryList,
			boolean found) {
		
		
	}
	@Override
	public void getSaveCallBack(boolean saved,ParseObject parseObjectSaved) {
		userDelagator.saveCallBack(saved);
		
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
		userDelagator.registerSuccess(signUpSuccess);
		
	}
	@Override
	public void signInSuccess(boolean success) {
		parseConnect.setSignOnServer(success);
		
		if (success) 
			parseConnect.setAtlasAndroidUserObject(parseConnect.parseUser);
		userDelagator.signInSuccess(success);
		
	}
	@Override
	public void saveFileCallBack(boolean success,ParseObject parseObjectSaved) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getUserEmailOnParseCallBack(
			HashMap<String, String> loginProperties, boolean success) {
		userDelagator.getUserEmailOnParseCallBack(loginProperties, success);
		
	}
	@Override
	public void getFriendEmailOnParseCallBack(
			List<ParseObject> list, boolean success) {
		ATLContactModel friendAtlasFriend = null;
		ParseUser friendUser=null;
		if (list!=null && list.size()==1)
		{
			
			
			friendUser = (ParseUser)list.get(0); 
			if (friendUser.getString("email")!=null && !friendUser.getString("email").equals("") &&
					friendUser.getString("password_copy")!=null && !friendUser.getString("password_copy").equals(""))
			{
			//User user = new User(friendUser);
			friendAtlasFriend = new ATLContactModel();
			friendAtlasFriend.setFirstname(friendUser.getString("first_name"));
			friendAtlasFriend.setLastname("");
			friendAtlasFriend.setEmailAddress(friendUser.getEmail());
			friendAtlasFriend.setFacebookId(friendUser.getString("facebook_id"));
			friendAtlasFriend.setAtlasId(friendUser.getObjectId());
			friendAtlasFriend.setFromFacebook(!friendUser.getString("facebook_id").equals(""));
			friendAtlasFriend.setIsAtlasUser(friendUser.getBoolean("is_atlas_user"));
			
			
			String friendsFilePictureName = friendUser.getObjectId();
			saveFriendPictureFileInBackground(friendUser, friendsFilePictureName, application);
			
//			byte[] profilePic = user.getFile("picture");
//			if (profilePic!=null)
//			{
//				Bitmap bitmap = BitmapFactory.decodeByteArray(profilePic, 0, profilePic.length);
//				friendAtlasFriend.setImage(bitmap);
//			}
			}
		}
		userDelagator.getFriendEmailOnParse(friendAtlasFriend, friendAtlasFriend!=null);
		
		
	}
	@Override
	public void getFacebookFriendIdOnParseCallBack(
			List<ParseObject> list, boolean success) {
		
		
		ATLContactModel facebookAtlasFriend = null;
		ParseUser facebookUser = null;
		if (success && list!=null && list.size()==1)
			facebookUser = (ParseUser)list.get(0); 
		if (facebookUser!=null && facebookUser.getEmail()!=null && !facebookUser.getString("email").equals("")
				&&facebookUser.getString("password_copy")!=null && !facebookUser.getString("password_copy").equals("")) 
		{
			 
		//	User user = new User(facebookUser);
			//facebookAtlasFriend.setDisplayName(facebookUser.getString("userNameDisplay"));
			facebookAtlasFriend.setFirstname(facebookUser.getString("first_name"));
			facebookAtlasFriend.setLastname("");
			facebookAtlasFriend.setEmailAddress(facebookUser.getEmail());
			facebookAtlasFriend.setFacebookId(facebookUser.getString("fb_id"));
			facebookAtlasFriend.setAtlasId(facebookUser.getObjectId());
			facebookAtlasFriend.setFromFacebook(true);
			
			
			String friendsFilePictureName = facebookUser.getObjectId();
			saveFriendPictureFileInBackground(facebookUser, friendsFilePictureName, application);
			
//			byte[] profilePic = user.getFile("picture");
//			if (profilePic!=null)
//			{
//				Bitmap bitmap = BitmapFactory.decodeByteArray(profilePic, 0, profilePic.length);
//				
//				facebookAtlasFriend.setImage(bitmap);
//			}	
		
			
		}
//		return facebookAtlasFriend;
		userDelagator.getFacebookFriendIdOnParse(facebookAtlasFriend, facebookAtlasFriend!=null);

	}
	
	@Override
	public void getAllAtlasUsersCallBack(List<ParseObject> allFacebookAtlasUsersObjects,
			boolean success) {
		ArrayList<ATLContactModel> allFacebookAtlasUsers=null;
		if (success && allFacebookAtlasUsersObjects!=null && allFacebookAtlasUsersObjects.size()>0)
		{
			allFacebookAtlasUsers = new ArrayList<ATLContactModel>();
			allFacebookAtlasUsersFBID = new ArrayList<String>();
			for (ParseObject fbAtlasUser :allFacebookAtlasUsersObjects)
			{
				
				if (fbAtlasUser.getString("email")!=null && !fbAtlasUser.getString("email").equals("") &&
						fbAtlasUser.getString("password_copy")!=null && !fbAtlasUser.getString("password_copy").equals(""))
				{
				//User user = new User(fbAtlasUser);
				
				ATLContactModel atlContactModel = new ATLContactModel();
				atlContactModel.setAddressBookId("");
				atlContactModel.setAtlasId(fbAtlasUser.getObjectId());
				atlContactModel.setEmailAddress(fbAtlasUser.getString("email"));
				atlContactModel.setFacebookId(fbAtlasUser.getString("facebook_id"));
				atlContactModel.setFirstname(fbAtlasUser.getString("first_name"));
				atlContactModel.setFromFacebook(true);
				atlContactModel.setLastname(fbAtlasUser.getString("first_name"));
				 
		
				atlContactModel.setIsAtlasUser(fbAtlasUser.getBoolean("is_atlas_user"));
				allFacebookAtlasUsers.add(atlContactModel);
				allFacebookAtlasUsersFBID.add(fbAtlasUser.getString("facebook_id"));
			}
			}
			
		}
		//return allFacebookAtlasUsers;
		userDelagator.getAllFBAtlasUsersCallBack(allFacebookAtlasUsers,success);
	}
	@Override
	public void getAllFBAtlasUsersFriendsCallBack(List<ParseObject> allFacebookAtlasUsersObjects,
			boolean success) {
		ArrayList<ATLContactModel> allFacebookAtlasUsers=null;

		if (success && allFacebookAtlasUsersObjects!=null && allFacebookAtlasUsersObjects.size()>0)
		{
			allFacebookAtlasUsers = new ArrayList<ATLContactModel>();
			allFacebookAtlasUsersFBID = new ArrayList<String>();
			for (ParseObject fbAtlasUser :allFacebookAtlasUsersObjects)
			{
				
				if (fbAtlasUser.getString("email")!=null && !fbAtlasUser.getString("email").equals("") &&
						fbAtlasUser.getString("password_copy")!=null && !fbAtlasUser.getString("password_copy").equals(""))
				{
				//User user = new User(fbAtlasUser);
				
				ATLContactModel atlContactModel = new ATLContactModel();
				atlContactModel.setAddressBookId("");
				atlContactModel.setAtlasId(fbAtlasUser.getObjectId());
				atlContactModel.setEmailAddress(fbAtlasUser.getString("email"));
				atlContactModel.setFacebookId(fbAtlasUser.getString("facebook_id"));
				atlContactModel.setFirstname(fbAtlasUser.getString("first_name"));
				atlContactModel.setFromFacebook(true);
				atlContactModel.setLastname(fbAtlasUser.getString("first_name"));
				 
				atlContactModel.setIsAtlasUser(fbAtlasUser.getBoolean("is_atlas_user"));
				allFacebookAtlasUsers.add(atlContactModel);
				allFacebookAtlasUsersFBID.add(fbAtlasUser.getString("facebook_id"));
			}
			}
			
		}
		//return allFacebookAtlasUsers;
		userDelagator.getAllFBAtlasUsersFriendsCallBack(allFacebookAtlasUsers,success);
		
	}
	@Override
	public void friendSignInSuccess(boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getAtlasNewFriendsByEmailCallBack(List<ParseObject> allCommonEmails,
			boolean success) {
		ArrayList<ATLContactModel> allCommonAtlasUsers = new ArrayList<ATLContactModel>();

		if (allCommonEmails!=null && allCommonEmails.size()>0)
		{
			allCommonAtlasUsers = new ArrayList<ATLContactModel>();
			for (ParseObject emailAtlasUser :allCommonEmails)
			{
				
				if (emailAtlasUser.getString("email")!=null && !emailAtlasUser.getString("email").equals("") &&
						emailAtlasUser.getString("password_copy")!=null && !emailAtlasUser.getString("password_copy").equals(""))
				{
				//User user = new User(emailAtlasUser);
				ATLContactModel atlContactModel = new ATLContactModel();
				atlContactModel.setAddressBookId("");
				atlContactModel.setAtlasId(emailAtlasUser.getObjectId());
				atlContactModel.setEmailAddress(emailAtlasUser.getString("email"));
				atlContactModel.setFacebookId(emailAtlasUser.getString("facebook_id"));
				atlContactModel.setFirstname(emailAtlasUser.getString("first_name"));
				atlContactModel.setFromFacebook(true);
				atlContactModel.setLastname("");
				
				atlContactModel.setIsAtlasUser(emailAtlasUser.getBoolean("is_atlas_user"));
				
				String friendsFilePictureName = emailAtlasUser.getObjectId();
				saveFriendPictureFileInBackground(emailAtlasUser, friendsFilePictureName, application);
				
//				byte[] profilePic = user.getFile("picture");
//				if (profilePic!=null)
//				{
//					Bitmap bitmap = BitmapFactory.decodeByteArray(profilePic, 0, profilePic.length);
//					atlContactModel.setImage(bitmap);
//				}	
				
				allCommonAtlasUsers.add(atlContactModel);
			}
			}
			
		}
		//return allCommonAtlasUsers;
		userDelagator.getAtlasNewFriendsByEmailCallBack(allCommonAtlasUsers);
	}
	@Override
	public void getUpateCallBack(boolean success) {
		userDelagator.getUpateCallBack(success);

		
	}
	
	

}
