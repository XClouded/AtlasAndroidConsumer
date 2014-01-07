package com.atlasapp.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import com.atlasapp.atlas_database.ATLDBCommon;
import com.atlasapp.atlas_database.AtlasServerConnect;
import com.atlasapp.atlas_database.DatabaseConstants.TABLE_ALERTS;
import com.atlasapp.facebook.FacebookUtilities;
import com.atlasapp.model.ATLContactModel;
import com.atlasapp.model.ATLFriendModel;
import com.atlasapp.section_appentry.AtlasApplication;

public class ATLFriendLocalTable {
	protected static CurrentSessionFriendsList currentSessionFriendsList;
	protected static ArrayList<ATLContactModel> newABAtlasFriends;
	protected static ArrayList<String> newABAtlasFriendsEmails;
	protected static Context ctx;
	protected static ArrayList<String> newFBAtlasFriendsEmails;
	protected static ArrayList<ATLContactModel> newAtlasFriends;
	protected static ArrayList<ATLContactModel> atlasFBFriendsonDB;
	protected static ArrayList<ATLContactModel> newFBAtlasFriends;
	protected static ArrayList<String> adressBookFriendsbyEmail;
	protected static AtlasServerConnect parseConnect =   AtlasServerConnect.getSingletonObject(null);
	protected static ATLDBCommon atlDBCommon = ATLDBCommon.getSingletonObject(null);
	public static void updateFBFriendsOnDBInBackground()
	{
		UpdateOnDB longOperation = new UpdateOnDB();
		longOperation.execute("write","facebook");
	}
	public static void updateABFriendsOnDBInBackground()
	{
		UpdateOnDB longOperation = new UpdateOnDB();
		longOperation.execute("write","address");
	}
	public static void updateAllFriendOnDBInBackground()
	{
		UpdateOnDB longOperation = new UpdateOnDB();
		longOperation.execute("write","all");
	}
	
	public static class  UpdateOnDB extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			ctx = UtilitiesProject.ctx;
			String[] operation = params;
			String backgroundOperation = operation[0];
			String friendsToUpdate = operation[1];
			friendsToUpdate = (friendsToUpdate!=null)? friendsToUpdate : "";
			if (backgroundOperation.equals("write"))
			{
				if (friendsToUpdate.equals("address") || friendsToUpdate.equals("all"))
				{
					///////////////
					  currentSessionFriendsList = CurrentSessionFriendsList.getSingletonObject();  
					   ArrayList<String> currentABContactsEmailAdd = new  ArrayList<String>();

						String message = "";  
				    	
						ContentResolver cr = ctx.getContentResolver();//.getContentResolver();
					    Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
					            null, null, null);
					  
					    newABAtlasFriends =  new ArrayList<ATLContactModel>(); // need to be added to the local DB friend
					    newABAtlasFriendsEmails = new ArrayList<String>();
					        
					    
					    if (cur!=null && cur.getCount() > 0) {  
					    	ATLContactModel conatctModel = new ATLContactModel();
					    	int index ;
					    	String id;
					    	HashMap<String,ATLContactModel> contactLocalProperties = new HashMap<String,ATLContactModel>();
					        while (cur.moveToNext()) 
					        {
					        	conatctModel = new ATLContactModel();
					        	index = cur
					                    .getColumnIndex(ContactsContract.Contacts._ID);
					           id = cur.getString(cur.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
					           id = (id==null)? "" :id;
					           String[] name =new String[3]; 
			    	            name[0] = "";
			    	            name[1] = "";
			    	            name[2] = "";
			    	            String[] phoneNum={""};  
					           /// Getting Contact Email add...
					           String email = "";
					            	Cursor emailCur = cr.query( 
					            		ContactsContract.CommonDataKinds.Email.CONTENT_URI, 
					            		null,
					            		ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", 
					            		new String[]{id}, null); 
					            	while (emailCur!=null && emailCur.moveToNext() )
					            	{ 
					            	    // This would allow you get several email addresses
					                        // if the email addresses were stored in an array
					            	     email = emailCur.getString(
					                                  emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
					            	     if(!currentABContactsEmailAdd.contains(email)){
					            	    	 currentABContactsEmailAdd.add(email);
					            	    	 conatctModel.setEmailAddress(email);
					            	    	 conatctModel.setAddressBookId(id);
					            	    	 conatctModel.setFromAddressBook(true);
					            	    //	 contactLocalProperties.put("email", email);
					            	     }
					             	    
					             	} 
					             	emailCur.close();
					             	
					             	//////////////////
					             	if (email!=null && !email.equals("")){
					             	 String whereName = ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = ?";
					             	whereName = (whereName==null)? "":whereName;  
					             	 String[] whereNameParams = new String[] { ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE, id };
					    	            Cursor nameCur = cr.query(ContactsContract.Data.CONTENT_URI, null, whereName, whereNameParams, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
					    	           
					    	         
					    	            while (nameCur !=null && nameCur.moveToNext()) {
					    	            	
					    	            	
					    	            	
					    	            	 try {
					    	            		 name[0] =nameCur.getString(nameCur.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
					    	            		 }catch (Exception e){name[0] ="";};
					    	            	 try {
					    	            	 name[1] = nameCur.getString(nameCur.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
					    	            	 }catch (Exception e){name[1] ="";};
					    	            	 try {
					    	            	 name[2] =nameCur.getString(nameCur.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME));
					    	            	 }catch (Exception e){name[2] ="";};
					    	              
					    	            }
					    	            nameCur.close();
					    	            String firstName = (name[0]!=null)? name[0]:"";
					    	            String lastName = (name[1]!=null)? name[1]:"";
					    	            conatctModel.setFirstname(firstName);
					    	            conatctModel.setLastname(lastName);
					             	}
					             	
					  	            if (("1").equals(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))) 
					  	            {
					  	                Cursor pCur = cr.query(
					  	                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					  	                        null,
					  	                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
					  	                                + " = ?", new String[] { id }, null);
					  	                
					  	                if (pCur!=null){
					  	                int i = 0;
					  	                int pCount = pCur.getCount();
					  	                phoneNum = new String[pCount];
					  	                String[] phoneType = new String[pCount];
					  	                while (pCur.moveToNext()) {
					  	                    phoneNum[i] = pCur
					  	                            .getString(pCur
					  	                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					  	                    phoneType[i] = pCur
					  	                            .getString(pCur
					  	                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
					  
					  	                    i++;
					  	                }
					  	              pCur.close();// 2013-01-29 TAN: close cursor
					  	            }
					  	            }
					  	            if (phoneNum.length > 0 && phoneNum[0]!=null && !phoneNum[0].equals(""))// TAN FIX CRASH
					  	            	conatctModel.setPhoneNumber(phoneNum[0]); 
					  	            
					  	          final Bitmap photo = retrieveContactPhoto(id);
					  	          if (photo!=null)
					  	        	  conatctModel.setImage(photo);  
					  	            
					  	          if (conatctModel.getEmailAddress()!=null && !conatctModel.getEmailAddress().equals("") 
					  	        		&&conatctModel.getFirstname()!=null  && !conatctModel.getFirstname().equals(""))
					  	        	  contactLocalProperties.put(conatctModel.getEmailAddress(), conatctModel);
					             	
					             //////////////////
					        }
					        // Getting list of the contacts that are not already on local DB.
					        //and remove the contacts  found that already in it
					        ArrayList<String> newABContactsEmailAdd = getNewContactsList(currentABContactsEmailAdd);
					        if (newFBAtlasFriendsEmails!=null)
					        	newABContactsEmailAdd.removeAll(newFBAtlasFriendsEmails);
					        if (newABAtlasFriendsEmails!=null)
					        	newABContactsEmailAdd.removeAll(newABAtlasFriendsEmails);
					        newABAtlasFriendsEmails = newABContactsEmailAdd;
					        if (!newABContactsEmailAdd.isEmpty() )
					        {
					        	//   
					        	ArrayList<ATLContactModel> atlContacts = new ArrayList<ATLContactModel>();
					        	 ArrayList<ArrayList<ATLContactModel>> all = null;
					        	 all = atlDBCommon.getUsersByEmail(newABContactsEmailAdd,contactLocalProperties);
					        	newABAtlasFriends = (all!=null && all.size()>1)? all.get(1) : newABAtlasFriends;
					        	atlContacts = (all!=null && all.size()>0)? all.get(0) :atlContacts;   
					        	if (atlContacts!=null && atlContacts.size()>0)
					        		currentSessionFriendsList.setCurrentATLFriendsList(atlContacts);
					        }
					       
					   }  
				        currentSessionFriendsList.addToCurrentFriendsList(newABAtlasFriends);
					    
					//////////////
					if (newABAtlasFriends!=null)
						
						for (ATLContactModel newfriend:newABAtlasFriends)
						{
							newfriend.setFromAddressBook(true);
							newfriend.write();
							
						}
						
					newAtlasFriends = newABAtlasFriends;
					cur.close(); // 2013-01-29 TAN: close cursor
				}
				if (AtlasAndroidUser.getfbID() != null && !AtlasAndroidUser.getfbID().equals("")
						&& friendsToUpdate != null 
						&&((friendsToUpdate.equals("facebook")||friendsToUpdate.equals("all"))))
				{
						atlasFBFriendsonDB =  new ArrayList<ATLContactModel>();
						String message = "";
						 // All Users' Facebook Friends
						ArrayList<String> facebookFriends = FacebookUtilities.getFacebookFriends(ctx);
					   
					   /// facebook friends on loca DB
						ArrayList<String> localFriendsFBID = ATLFriendModel.readAllFacebookId();
						
						// remove the facebook friends that are already recognized
						/// as atlas friends from all facebook list 
						facebookFriends.removeAll(localFriendsFBID);
						// retain only  facebook friends  that are on Atlas
						ArrayList<String> allFBATLUSERS = atlDBCommon.getCurrentSessionFacebookUsersFacebookId();
						allFBATLUSERS.retainAll(facebookFriends);
						newFBAtlasFriends = atlDBCommon.getUsersByFacebookId(allFBATLUSERS);
						
						if (newFBAtlasFriends!=null)
						{	
							for (ATLContactModel newfriend:newFBAtlasFriends)
							{
								newfriend.setFromFacebook(true);
								newfriend.write();
							}
						
						
							if (newAtlasFriends!=null)
								newAtlasFriends.addAll(newFBAtlasFriends);
							else
								newAtlasFriends = newFBAtlasFriends;
						}
					       currentSessionFriendsList.addToCurrentFriendsList(newFBAtlasFriends);
						    
					       currentSessionFriendsList.addToCurrentATLFriendsList(newFBAtlasFriends);  
						
						
					}
					retrieveAllFriendsPicturesInBacground();
				}
			currentSessionFriendsList.setFriendsUpdateComplete(true);
			return null;
		}
		
	}
	
	
	
	
	
	/**
	 * Checking the current list of contacts and returning a list 
	 * of the new contacts that arnt already on the local DB
	 * @param currentABContactsEmailAdd
	 * @return
	 */
	private static ArrayList<String> getNewContactsList(
			ArrayList<String> currentABContactsEmailAdd) {
		
		adressBookFriendsbyEmail = ATLFriendModel.readAllEmails();
        if (adressBookFriendsbyEmail!=null)
        	
        	currentABContactsEmailAdd.removeAll(adressBookFriendsbyEmail);
		
		
		return currentABContactsEmailAdd;
	}


	/**
	 * update on Friend local DB the new found friends
	 */
	public static void updateFriendOnDB(ArrayList<ATLContactModel> newAdded) {
	
		if (newAdded!=null)
		
		for (ATLContactModel newfriend:newAdded)
		{
			newfriend.write();
		}
		
	}
	/**
	 * Checks to see if contact email on Friend DB
	 * @param email contact email
	 * @return
	 */
	private static boolean contactEMAILOnLocalDB(String email) {

		boolean onDB = false;
    	onDB = adressBookFriendsbyEmail.contains(email);
	
		return onDB;
	}
	
	private static Bitmap retrieveContactPhoto(String contactID) {

        Bitmap photo = null;  

        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(ctx.getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
             //   ImageView imageView = (ImageView) findViewById(R.id.img_contact);
              //  imageView.setImageBitmap(photo);
            }

            assert inputStream != null;
           if (inputStream != null)  inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        return photo;

    }
	
	
//	public  static String updateFacebookFriendsOnDB()
//	{
//		String message = "";
//		 // All Users' Facebook Friends
//		ArrayList<String> facebookFriendsIds = FacebookUtilities.getFacebookFriends(ctx);
//		adressBookFriendsbyEmail = ATLFriendModel.readAllEmails();
//	     // need to be added to the local DB friend
//	    atlasFBFriendsonDB =  new ArrayList<ATLContactModel>();
//	   
//	    // All Friends on local DB
//// HARRIS NEW:
////	    ArrayList<ATLFriendModel> allFriendsOnLocalDB = ATLFriendModel.readAll();
//	    // HARRIS NEW:
//	    // ArrayList<HashMap<String, String>> allFriendsOnLocalDB = ATLFriendModel.readAll();
//	//    ArrayList<HashMap<String, String>> allFriendsOnLocalDB = ATLContactModel.readAll();
//	    
////		  Toast.makeText(ctx,"Loading friends from Facebook...",
////					Toast.LENGTH_LONG).show();
//		// All Facebook Atlas Users On local Mobile DB
//	//	ArrayList<String> facebookUserDBFriends = ATLFriendModel.readAllFacebookId() ;
//
//		
//		//All Atlas Facebook users
//		ArrayList<ATLContactModel> atlasFBUsers = getAllAtlasFBUsers();
//		
//		
//		
//		ArrayList<ATLContactModel> userFBFriendsOnAtlas = getAllUsersFBFriendsOnAtlas(atlasFBUsers,facebookFriendsIds);
//		
//		ArrayList<String> localFriendsEmail = ATLFriendModel.readAllEmails();
//		ArrayList<String> localFriendsFBID = ATLFriendModel.readAllFacebookId();
//		
//		
//		newFBAtlasFriends = new ArrayList<ATLContactModel>();
//		
//		for (ATLContactModel facebookAtlasFriend: userFBFriendsOnAtlas)
//		{
//			String fbId = facebookAtlasFriend.getFacebookId();
//			String email = facebookAtlasFriend.getEmailAddress();
//			  
//			if (!localFriendsFBID.contains(fbId) && !localFriendsEmail.contains(email)
//					&& (newABAtlasFriendsEmails==null || (newABAtlasFriendsEmails!=null && !newABAtlasFriendsEmails.contains(email))))
//			{
//				if (newFBAtlasFriendsEmails==null)
//					newFBAtlasFriendsEmails = new ArrayList<String>();
//				facebookAtlasFriend.setFromFacebook(true);
//				newFBAtlasFriends.add(facebookAtlasFriend);
//				newFBAtlasFriendsEmails.add(email);
//				
//			}
//			else
//			{
//				/// UPDATE DATABASE......
//			}
//		}
//		  
//		message =  "Facebook Friends found:  "+facebookFriendsIds.size() +
//				"Facebook Friends on Atlas: "+userFBFriendsOnAtlas.size()+
//				"Facebook Friends in your Atlas list: "+localFriendsFBID.size()+
//				"Atlas Users added to your Atlas list: "+newFBAtlasFriends.size();
//		
//	//	retreiveFriendsPicturesInBackground();
//		updateFriendOnDB(newFBAtlasFriends);
//	return message;
//
//}
//
//			
			
			
		
	
	
	/**
	 * Gets the list of the current User's Facebook friends and Atlas Facebook Users by Facebook ID'S
	 * and return a list of all the facebook friends that are on atlas 
	 * @param atlasFBUsers
	 * @param facebookFriends
	 * @return
	 */
//	private static ArrayList<ATLContactModel> getAllUsersFBFriendsOnAtlas(
//			ArrayList<ATLContactModel> atlasFBUsers,
//			ArrayList<String> facebookFriends
//		) {
//		
//		
//		ArrayList<ATLContactModel> allUsersFBFriendsOnAtlas = new ArrayList<ATLContactModel>();
//		ArrayList<String> allUsersFBFriendsOnAtlasID = new ArrayList<String>();
//
//		
//		if (atlasFBUsers!=null && facebookFriends !=null && 
//				atlasFBUsers.size()>0 && facebookFriends.size()>0)
//		{
//			
//			
//			ArrayList<String> list1,list2;
//			
//			ArrayList<String> facebookAtlasUsersID = parseConnect.getAllFBAtlasUsersID();
//			
//			list1 = (facebookAtlasUsersID.size() > facebookFriends.size())? facebookFriends:facebookAtlasUsersID;
//			list2 = (facebookAtlasUsersID.size() > facebookFriends.size())? facebookAtlasUsersID: facebookFriends;
//			ATLContactModel commonFBfriend = new ATLContactModel();
//			for (String fbId: list1)
//			{
//				if (list2.contains(fbId))
//				{
//					allUsersFBFriendsOnAtlasID.add(fbId);
//					
//				}
//			}
//			
//			allUsersFBFriendsOnAtlas = parseConnect.getAllUsersByFBID(allUsersFBFriendsOnAtlasID);
//			
//			
//		}
//	
//		return allUsersFBFriendsOnAtlas;
//	}

//	private static ArrayList<ATLContactModel> getAllAtlasFBUsers() 
//	{
//		ArrayList<ATLContactModel> allAtlasFBUsers = new ArrayList<ATLContactModel>();
//
//		allAtlasFBUsers = parseConnect.getAllFBAtlasUsers();
//		
//		return allAtlasFBUsers;
//	}

	@SuppressWarnings("deprecation")
	private static void alertUser(String messageTitle, String message)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(
				ctx).create();

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
	
	
	
	private static ATLContactModel getFacebookFriendFromLocal(String facebookFriendsId) {
		
		ATLContactModel atlasFacebookFriend = null;
		
		
		ATLFriendModel facebookFriend = new ATLFriendModel();
		facebookFriend.setFacebookId(facebookFriendsId);
		facebookFriend.readByFacebookId();
		

		//atlasFacebookFriend.setDisplayName(facebookFriend.getFirstname());
		atlasFacebookFriend.setEmailAddress(facebookFriend.getEmailAddress());
		atlasFacebookFriend.setFacebookId(facebookFriendsId);
		atlasFacebookFriend.setFromFacebook(true);
		atlasFacebookFriend.setFromAddressBook(facebookFriend.isFromAddressBook());
		atlasFacebookFriend.setLastname(facebookFriend.getLastname());
		atlasFacebookFriend.setFirstname(facebookFriend.getFirstname());
		atlasFacebookFriend.setAtlasId(facebookFriend.getAtlasId());
		
		
		// TO DO: CALLING HARRIS METHOD FOR GETTING THE Friend object from facebook id
	
		return atlasFacebookFriend;
	}
	
	
	  
	
	public static void retreiveFriendsPicturesInBackground(String newAtlasFriendsAddType)
	{
		LongOperation longOperation = new LongOperation();
	  	longOperation.execute(newAtlasFriendsAddType);
		
	}
	public static void retrieveAllFriendsPicturesInBacground()
	{
		LongOperation longOperation = new LongOperation();
	  	longOperation.execute("all");
	}
	public static class  LongOperation extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			String[] operation = params;
			String newAtlasFriendsAddType = operation[0];
			ArrayList<ATLContactModel> newAtlasFriendsAdd = (newAtlasFriendsAddType.equals("facebook"))?newFBAtlasFriends :
															(newAtlasFriendsAddType.equals("address"))? newABAtlasFriends:
															(newAtlasFriendsAddType.equals("all"))?	newAtlasFriends:
															null;
				
					
			
			if (newAtlasFriendsAdd!=null && newAtlasFriendsAdd.size()>0)
			for (ATLContactModel friend:newAtlasFriendsAdd)
			{
				parseConnect.retrieveUserFile(friend.getEmailAddress(),  friend.getAtlasId());
			}
			
		
			return null;
		}
		
	}
	public static void setAlertsInBackground()
	{
		SetAlerts longOperation = new SetAlerts();
		longOperation.execute("alert_search");
	}
	
	public static class  SetAlerts extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			String[] operation = params;
			String backgroundOperation = operation[0];
			
			if (backgroundOperation.equals("alert_search"))
			{
				parseConnect.setAlerts(TABLE_ALERTS.EVENT);
				parseConnect.setAlerts(TABLE_ALERTS.TASK);
			}
			
			
			return null;
		}
		
	}
	 public static Bitmap getProfilePic(String imageObjectId)
	   {
		 AtlasApplication applicationController = (AtlasApplication)ctx;

		   Bitmap currentBitmap  = null;
		   
		   String imageDir ="";
//				   ()?
//				   applicationController.IMAGE_DIR
//				   
			
		   
		   File imgFile;
		   if (!imageObjectId.equals(AtlasAndroidUser.getParseUserID()))
		   {  imageDir =
				applicationController.FRIENDS_IMAGE_DIR+"/"+imageObjectId+".png";
			   imgFile = new File (imageDir) ;
		   }else
			   imgFile = applicationController.PROFILE_PIC_PATH;
		   

			if(imgFile.exists())
	   		
				currentBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
	   		
			  
		
////		   File imgFile = (currentPhotoURI!=null)? new File(currentPhotoURI.getPath()):this.applicationController.PROFILE_PIC_PATH;
//		 
	//   	
//	   		if(!imgFile.exists())
//	   		imgFile = applicationController.PROFILE_PIC_PATH;
	//   	
	//  
//	   		if(imgFile.exists())
//	   		{
//	   			Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			
		//	currentPhoto = currentBitmap;
			
			
			

	   		return currentBitmap;
	   		
		}
}
