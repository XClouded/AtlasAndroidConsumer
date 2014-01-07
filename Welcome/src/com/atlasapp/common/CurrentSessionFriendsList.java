package com.atlasapp.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;

import com.atlasapp.atlas_database.AtlasServerConnect;
import com.atlasapp.model.ATLContactModel;
import com.atlasapp.model.ATLFriendModel;
/**
 * 
 * @author sharonnachum
 *
 */
public class CurrentSessionFriendsList {
	
	
	Context mContext;



	private static boolean friendsRetrieved;
	
	
	
	private  static ArrayList<ATLContactModel> curentSessionFriends = new ArrayList<ATLContactModel>();

	public static enum BACKGROUND_PROCESS {
		   READ_ALL, LOGGED_OUT, EMAIL_OR_USER_NAME_INVALID, SUCCESS, LOGGED_IN, EMAPY_VALUE
		 }

	private static CurrentSessionFriendsList currentSessionFriendsList;
	
	
	private  static ArrayList<ATLContactModel> curentATLSessionFriends = new ArrayList<ATLContactModel>();
	
	
	public static ArrayList<ATLContactModel> getCuurentSessionFriends(){return curentSessionFriends;};
	
	public static ArrayList<ATLContactModel> getCuurentATLSessionFriends(){return curentATLSessionFriends;};
	
	
	
	public boolean isFriendUpdateComplete()
	{
		return currentSessionFriendsList.friendsRetrieved;
	}
	public void setFriendsUpdateComplete(boolean updated)
	{
		currentSessionFriendsList.friendsRetrieved = updated;
	}
	private CurrentSessionFriendsList()
	{
		
	}

	public static CurrentSessionFriendsList getSingletonObject()
	{
	      if (currentSessionFriendsList == null)
	      {
	    	  currentSessionFriendsList = new CurrentSessionFriendsList();
	    	  
	    	// 
	    	
	      }
	      return currentSessionFriendsList;
	}
	public ArrayList<ATLContactModel>  getCurentSessionFriendsList()
	{
		if (currentSessionFriendsList.curentSessionFriends!=null)
		return currentSessionFriendsList.curentSessionFriends;
		else
			return new ArrayList<ATLContactModel>();
	}
	public synchronized void addToCurrentFriendsList(ArrayList<ATLContactModel> friend)
	{
		currentSessionFriendsList.curentSessionFriends.addAll(friend);
	}
	public synchronized void addToCurrentFriendsList(ATLContactModel friend)
	{
		currentSessionFriendsList.curentSessionFriends.add(friend);
	}
	public synchronized void setCurrentFriendsList(ArrayList<ATLContactModel> friends)
	{
		currentSessionFriendsList.curentSessionFriends = friends;
	}
	
	
	
	public ArrayList<ATLContactModel>  getCurentATLSessionFriendsList()
	{
		return currentSessionFriendsList.curentATLSessionFriends;
	}
	public synchronized void addToCurrentATLFriendsList(ArrayList<ATLContactModel> friend)
	{
		currentSessionFriendsList.curentATLSessionFriends.addAll(friend);
	}
	public synchronized void addToCurrentATLFriendsList(ATLContactModel friend)
	{
		currentSessionFriendsList.curentATLSessionFriends.add(friend);
	}
	public synchronized void setCurrentATLFriendsList(ArrayList<ATLContactModel> friends)
	{
		currentSessionFriendsList.curentATLSessionFriends = friends;
	}
	
	
	
	
	
	
	
	
private ArrayList<ATLContactModel> contactListFromAddressBook() {
		
		ArrayList<ATLContactModel> contactA = new ArrayList<ATLContactModel>();
		
		ContentResolver cr = mContext.getApplicationContext().getContentResolver();
		Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);
		int testCtr = 0;
		String id = "";
		if (cursor.getCount() > 0) {

			while (cursor.moveToNext()) {
				id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

				ATLContactModel contact = new ATLContactModel();
				
				/////// Retreive Phone Numbers
				ArrayList<String> phones = new ArrayList<String>();
				
				
    //            Cursor phoneCur = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);

//				Cursor phoneCur = cr.query( 
//		          		ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
//		          		null,
//		          		ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", 
//		          		new String[]{id}, null); 
                        int index = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER); 
                        int size = cursor.getColumnCount();
//                        if (index!=-1 && cursor.getColumnCount()>=index){
//                        	
						String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

						if ( hasPhone.equalsIgnoreCase("1"))
								hasPhone = "true";
						else
								hasPhone = "false" ;

						if (Boolean.parseBoolean(hasPhone)) 
						{
							Cursor phonesCur = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
							while (phonesCur.moveToNext()) 
							{
								phones.add(phonesCur.getString(phonesCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
							}
							phonesCur.close();
						}
				
                     //   }
				////////////
				
				
				
				

				//TODO - get phone types, email types, group names
				// - also, address is not broken into city, state etc. how handle that?
				// - also, this is too slow right now.  was much quicker when I used contactListFromAddressBookOLD
				
				contact.setAddressBookId(id);
				
				contact.setFirstname(retrieveContactProperty(id,StructuredName.GIVEN_NAME,StructuredName.CONTENT_ITEM_TYPE));
				
				contact.setLastname(retrieveContactProperty(id,StructuredName.FAMILY_NAME,StructuredName.CONTENT_ITEM_TYPE));
					
				contact.setFromAddressBook(true);
				
				
			//	ArrayList<String> phones = getContactPhoneLNumberListFromAddressBook(id);
				String phoneNumber = (phones.size()!=0 && phones.get(0).length()>=10)? phones.get(0):"";  
				phoneNumber = (phoneNumber!=null)?phoneNumber:"";
				contact.setPhoneNumber(phoneNumber);
				
				
//				ArrayList<String> emailAddresses = getContactEmailsFromAddressBook(id);
//				String emailAddress = (emailAddresses.size()!=0)?emailAddresses.get(0):"";
//				emailAddress = (emailAddress!=null)?emailAddress:"";
//				contact.setEmailAddress(emailAddress);
//				
				
				
				contact.setCompany(retrieveContactProperty(id,Organization.COMPANY,Organization.CONTENT_ITEM_TYPE));

				contact.setGroup(retrieveContactProperty(id,GroupMembership.GROUP_SOURCE_ID,GroupMembership.CONTENT_ITEM_TYPE));
							
 				contact.setImage(retrieveContactPhoto(id));
 				
 				
 				if (contact.getEmailAddress()!=null && contact.getFirstname()!=null && !contact.getFirstname().equals("") && 
 						(!contact.getEmailAddress().equals("") || !contact.getPhoneNumber().equals(""))
 						)
 				  
 				
 					contactA.add(contact);
			}
		}
		cursor.close();

		return contactA;

	}
private String retrieveContactProperty(String contactId, String columnName,
		String mimetype) {
	String property = "";
	Cursor cursor = mContext.getContentResolver().query(
			ContactsContract.Data.CONTENT_URI,
			new String[] { columnName },
			ContactsContract.Data.CONTACT_ID + " = " + contactId
					+ " AND ContactsContract.Data.MIMETYPE = '"
					+ mimetype + "'", null, null);
	try {
		cursor.moveToNext();
		property = cursor.getString(cursor.getColumnIndexOrThrow(columnName));
	} catch (Exception e) {
		//Log.i("harris", "****property error=" + e.getLocalizedMessage());
	} 
		cursor.close();
	
	return property;
}

private Bitmap retrieveContactPhoto(String contactID) {

	Bitmap photo = null;

	try {
		InputStream inputStream = ContactsContract.Contacts
				.openContactPhotoInputStream(mContext.getContentResolver(),
						ContentUris.withAppendedId(
								ContactsContract.Contacts.CONTENT_URI,
								Long.valueOf(contactID)));

		if (inputStream != null) {
			photo = BitmapFactory.decodeStream(inputStream);
		}

		assert inputStream != null;
		if (inputStream != null)
			inputStream.close();

	} catch (IOException e) {
		e.printStackTrace();
	}

	return photo;

}







}
