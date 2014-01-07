package atlasapp.database;

import java.util.ArrayList;
import java.util.HashMap;


import android.app.Activity;
import android.os.AsyncTask;
import atlasapp.common.ATLFriendLocalTable;
import atlasapp.common.CurrentSessionFriendsList;
import atlasapp.model.ATLContactModel;

public class ATLDBCommon {
	
	
	private static ATLDBCommon atlDBCommon;

	private static HashMap<String, ATLContactModel> currentSessionUsersOnDB,allFBUsersModelOnTable ;

	
	
	private static HashMap<String, String> allCurrentSessionContactsEmailsOnMobile;
	/// 
	private static ArrayList<String> allCurrentFriendsIndexFoundOnParse;
	private static HashMap<String,ATLContactModel> contactModelFromMobile;
	
	private static ArrayList<String> currentSessionUsersEmails,currentSessionFacebookUsersEmails,currentSessionFacebookUsersIds;
	private static AtlasServerConnect  parseConnect;
	private static boolean friendsRetrieved = false;
	
	
	
	
	private static ArrayList<ATLContactModel> allCurrentFriendsOnLocalDB;
	private ATLDBCommon()
	{
		
	}
	
	public static ATLDBCommon getSingletonObject(Activity currentActivity)
	{
	      if (atlDBCommon == null)
	      {
	    	  atlDBCommon = new ATLDBCommon();
	    	// 
	    	  contactModelFromMobile = new HashMap<String,ATLContactModel>(); 
	    	  allCurrentFriendsIndexFoundOnParse = new ArrayList<String>();
	    	  allCurrentSessionContactsEmailsOnMobile = new HashMap<String, String>();
	    	  currentSessionUsersOnDB = new HashMap<String, ATLContactModel>();
	    	  allFBUsersModelOnTable = new HashMap<String, ATLContactModel>();
	    	  currentSessionUsersEmails = new ArrayList<String>();
	    	  allCurrentFriendsOnLocalDB = new  ArrayList<ATLContactModel>();
	 		  parseConnect =   AtlasServerConnect.getSingletonObject(null);

	      }
	      return atlDBCommon;
	}
	
	public void setAllCurrentFriendsOnLocalDB( ArrayList<ATLContactModel> list)
	{
		atlDBCommon.allCurrentFriendsOnLocalDB = list;
	}
	
	public ArrayList<ATLContactModel> getAllCurrentFriendsOnLocalDB()
	{
		return atlDBCommon.allCurrentFriendsOnLocalDB;
	}
	/**
	 * Gets the current email address list found on Parse and on the user's 
	 * contacts and set the allCurrentFriendsIndexFoundOnParse of all contacts 
	 * index found
	 * @param emails
	 */
	public void setCurrentSessionFriendsIndexFoundOnParse(
			ArrayList<String> emails) 
	{
		String id="";
		if (emails!=null && emails.size()>0)
		{
			for (String email:emails)
			{
				id = allCurrentSessionContactsEmailsOnMobile.get(email);
				if (id!=null && !id.equals(""))
					allCurrentFriendsIndexFoundOnParse.add(id);
			}
		}
		else
		{
			for (String email:allCurrentSessionContactsEmailsOnMobile.keySet())
			{
				id = allCurrentSessionContactsEmailsOnMobile.get(email);
				if (id!=null && !id.equals(""))
					allCurrentFriendsIndexFoundOnParse.add(id);
			}
		}
	}
	
	public void setContactModelFromMobile(HashMap<String,ATLContactModel> contactModelFromMobile)
	{
		atlDBCommon.contactModelFromMobile = contactModelFromMobile;
	}
	public HashMap<String,ATLContactModel> getContactModelFromMobile()
	{
		return atlDBCommon.contactModelFromMobile ;
	}
	public HashMap<String,ATLContactModel> getNonAtlasContact()
	{
		if (atlDBCommon.contactModelFromMobile!=null && atlDBCommon.contactModelFromMobile.size()>0
					&& atlDBCommon.allCurrentSessionContactsEmailsOnMobile!=null && 
						atlDBCommon.allCurrentSessionContactsEmailsOnMobile.size()>0)
		{
			if (atlDBCommon.allCurrentFriendsIndexFoundOnParse!=null 
					&& atlDBCommon.allCurrentFriendsIndexFoundOnParse.size()>0)
				if (atlDBCommon.allCurrentFriendsIndexFoundOnParse!=null
						&& atlDBCommon.allCurrentFriendsIndexFoundOnParse.size()>0)
				{
					String id = "";
					for (String email: atlDBCommon.allCurrentSessionContactsEmailsOnMobile.keySet())
					{
						id = atlDBCommon.allCurrentSessionContactsEmailsOnMobile.get(email);
						if (atlDBCommon.allCurrentFriendsIndexFoundOnParse.contains(id))
						{
							/// found on Parse (belongs to Atlas Friends category)
							atlDBCommon.contactModelFromMobile.remove(id);
						}
					}
				}
					return atlDBCommon.contactModelFromMobile;
		}
		else
		{
			return new HashMap<String,ATLContactModel>();
		}
	}

	public void setAllCurrentFriendsIndexFoundOnParse(ArrayList<String> currentFriendsIndexFoundOnParse)
	{
		atlDBCommon.allCurrentFriendsIndexFoundOnParse = currentFriendsIndexFoundOnParse;
	}
	public ArrayList<String> getAllCurrentFriendsIndexFoundOnParse()
	{
		return atlDBCommon.allCurrentFriendsIndexFoundOnParse ;
	}
	
	public void setAllCurrentSessionContactsEmailsOnMobile(HashMap<String, String> currentSessionContactsEmailsOnMobile)
	{
		atlDBCommon.allCurrentSessionContactsEmailsOnMobile = currentSessionContactsEmailsOnMobile;
	}
	public HashMap<String, String> getAllCurrentSessionContactsEmailsOnMobile()
	{
		return atlDBCommon.allCurrentSessionContactsEmailsOnMobile ;
	}
	
	public  synchronized void setCurrentSessionEmailsOnATLAS(HashMap<String, ATLContactModel> currentSessionUsersOnDB)
	{
		atlDBCommon.currentSessionUsersOnDB = currentSessionUsersOnDB;
	}
	public  void setCurrentSessionUsersEmails(ArrayList<String> usersEmails)
	{
		atlDBCommon.currentSessionUsersEmails = usersEmails;
	}
	public  ArrayList<String> getCurrentSessionUsersEmails()
	{
		return atlDBCommon.currentSessionUsersEmails;
	}
	private static boolean firstRegister =false;
	public synchronized void setCurrentSessionUsersOnATLASInBackground(boolean retrieveFriends, boolean firstRegister)
	{
		this.firstRegister = firstRegister;
		CurrentSessionFriendsList contactsUpdated = CurrentSessionFriendsList.getSingletonObject();
		contactsUpdated.setFacebookFriendsUpdateComplete(false,null);
		contactsUpdated.setFriendsUpdateComplete(false);
		CurrentSessionUsersOnATLASInBackground longOperation = new CurrentSessionUsersOnATLASInBackground();
	  	String updateMessage = (retrieveFriends)?"find_friends":"";
		longOperation.execute(updateMessage);
	}
	private static ArrayList<String> contactsEmailOnMobile;
	public synchronized void setCurrentSessionUsersOnATLASInBackground(ArrayList<String> contactsEmails,boolean retrieveFriends)
	{
		CurrentSessionFriendsList contactsUpdated = CurrentSessionFriendsList.getSingletonObject();
		contactsUpdated.setFriendsUpdateComplete(false);
		CurrentSessionUsersOnATLASInBackground longOperation = new CurrentSessionUsersOnATLASInBackground();
	  	String updateMessage = (retrieveFriends)?"find_friends":"";
	  	contactsEmailOnMobile = contactsEmails;
		longOperation.execute(updateMessage);
	}
	public  HashMap<String, ATLContactModel> getCurrentSessionUserssOnDB()
	{
		return currentSessionUsersOnDB;
	}
	public void setCurrentSessionUsersFacebookId(
			HashMap<String, ATLContactModel> allFBUsersModelOnTable) {
		atlDBCommon.allFBUsersModelOnTable = allFBUsersModelOnTable;
		
	}
	
	public void setCurrentSessionFacebookUsersUserNames(
			ArrayList<String> allFBUserNamesOnTable) {
		atlDBCommon.currentSessionFacebookUsersEmails = allFBUserNamesOnTable;
		
	}
	public ArrayList<String> getCurrentSessionFacebookUsersUserNames()
	{
		return atlDBCommon.currentSessionFacebookUsersEmails;
	}
	
	public HashMap<String, ATLContactModel> CurrentSessionUsersFacebookId()
	{
		return atlDBCommon.allFBUsersModelOnTable;
	}
	/**
	 * Gets an array list of the new emails found on the local 
	 * user mobile (via address book or email) and return array list 
	 * of ATLContactModel of all the users that also have Atlas account
	 * @param newABContactsEmailAdd
	 * @param contactLocalProperties 
	 * @return
	 */
	public  ArrayList<ArrayList<ATLContactModel>> getUsersByEmail(
			ArrayList<String> newABContactsEmailAdd, HashMap<String, ATLContactModel> contactLocalProperties
			) {
		
	
		ArrayList<ArrayList<ATLContactModel>> all = new ArrayList<ArrayList<ATLContactModel>>();
	//	atlContacts = new ArrayList<ATLContactModel>();
		
		
		if (contactLocalProperties!=null && contactLocalProperties.size()>0){
		ArrayList<String> abContactsEmailAdd = (ArrayList<String>) newABContactsEmailAdd.clone();
		ArrayList<ATLContactModel> newUsers = new ArrayList<ATLContactModel>();
		ArrayList<ATLContactModel> newATLUsers = new ArrayList<ATLContactModel>();
		ArrayList<String> newEmails = new ArrayList<String>(); 
		all.add(newUsers);
		all.add(newATLUsers);
		
		if (currentSessionUsersEmails!=null && currentSessionUsersEmails.size()>0
				&& currentSessionUsersOnDB !=null && currentSessionUsersOnDB.size()>0)
		{
			abContactsEmailAdd.retainAll(currentSessionUsersEmails);
			if (abContactsEmailAdd.size()>0)
			{  
				
				for (String email:abContactsEmailAdd)
				{
					if (currentSessionUsersOnDB.get(email)!=null && contactLocalProperties.get(email)!=null)
					{
						ATLContactModel contact = currentSessionUsersOnDB.get(email);
						contact.setFromAddressBook(true);
						contact.setPhoneNumber(contactLocalProperties.get(email).getPhoneNumber());      
						newUsers.add(currentSessionUsersOnDB.get(email));
					}
				}
				newATLUsers = (ArrayList<ATLContactModel>) newUsers.clone();
				
				all.set(0, newATLUsers);    
			}
		}
		
		ArrayList<String> abNonATLContactsEmailAdd =  (ArrayList<String>) newABContactsEmailAdd.clone();
		abNonATLContactsEmailAdd.removeAll(currentSessionUsersEmails);
		if (currentSessionUsersEmails!=null && currentSessionUsersEmails.size()>0
				&& currentSessionUsersOnDB !=null && currentSessionUsersOnDB.size()>0)
		{
			if (abNonATLContactsEmailAdd.size()>0)
			{
				ATLContactModel nonATLcontact ; 
				for (String nonATLemail:abNonATLContactsEmailAdd)
				{
					nonATLcontact = contactLocalProperties.get(nonATLemail);
				//	nonATLcontact.setEmailAddress(nonATLemail);
				//	nonATLcontact.setAtlasId("");
//					nonATLcontact.setFromAddressBook(true);
//					nonATLcontact.setFirstname((String)contactLocalProperties.get("first_name"));
//					nonATLcontact.setPhoneNumber((String)contactLocalProperties.get("phone_number"));
					if (nonATLcontact!=null )
					newUsers.add(nonATLcontact);
				}
			
			}
		}
		all.set(1, newUsers);      
		}
		return all;
	}
	/**
	 * Gets an array list of the new facebook id's found on the local 
	 * user mobile (via facebook) and return array list 
	 * of ATLContactModel of all the users that also have Atlas account
	 * @param newABContactsEmailAdd
	 * @return
	 */
	public  ArrayList<ATLContactModel> getUsersByFacebookId(
			ArrayList<String> newFBContactsFacebookIdAdd) {
		ArrayList<ATLContactModel> newUsers = new ArrayList<ATLContactModel>();
//		ArrayList<String> newEmails = new ArrayList<String>(); 
//		if (currentSessionUsersEmails!=null && currentSessionUsersEmails.size()>0
//				&& currentSessionUsersOnDB !=null && currentSessionUsersOnDB.size()>0)
//		{
//			newFBContactsFacebookIdAdd.retainAll(currentSessionUsersEmails);
			if (newFBContactsFacebookIdAdd!=null && newFBContactsFacebookIdAdd.size()>0)
//			{
				
				for (String id:newFBContactsFacebookIdAdd)
				{
					if (allFBUsersModelOnTable.get(id)!=null)
						newUsers.add(allFBUsersModelOnTable.get(id));
				}
		//	}
	//	}
		
		
		return newUsers;
	}
	public  ArrayList<ATLContactModel> getFacebookUsersByEmail(
			ArrayList<String> newFBContactsFacebookIdAdd) {
		ArrayList<ATLContactModel> newUsers = new ArrayList<ATLContactModel>();
		ArrayList<String> newEmails = new ArrayList<String>(); 
		if (currentSessionUsersEmails!=null && currentSessionUsersEmails.size()>0
				&& currentSessionUsersOnDB !=null && currentSessionUsersOnDB.size()>0)
		{
			newFBContactsFacebookIdAdd.retainAll(currentSessionUsersEmails);
			if (newFBContactsFacebookIdAdd.size()>0)
			{
				
				for (String id:newFBContactsFacebookIdAdd)
				{
					if (allFBUsersModelOnTable.get(id)!=null)
						newUsers.add(allFBUsersModelOnTable.get(id));
				}
			}
		}
		
		
		return newUsers;
	}
	/**
	 * Search on Parse DB on the users table and sets all the 
	 * ATLDBCommon :
	 * currentSessionUsersEmails,currentSessionFacebookUsersEmails,
	 * currentSessionFacebookUsersIds
	 * @author sharonnachum
	 *
	 */
	public static class  CurrentSessionUsersOnATLASInBackground extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			
			String[] operation = params;
			String backgroundOperation = operation[0];
			
			boolean retrieveFriends = backgroundOperation.equals("find_friends");
			
			ATLFriendLocalTable.updateAllFriendOnDBInBackground(firstRegister);
			firstRegister = false;
//			parseConnect.setAllAtlasUsers(retrieveFriends);
			
		//	parseConnect.setAllUsersContactsEmailsOnParse(contactsEmailOnMobile);
    		
			return null;
		}
		
	}
	public void setCurrentSessionFacebookUsersFacebookId(
			ArrayList<String> allFBUserIdOnTable) {
		atlDBCommon.currentSessionFacebookUsersIds = allFBUserIdOnTable;
		
	}
	public ArrayList<String> getCurrentSessionFacebookUsersFacebookId(
			) {
		return atlDBCommon.currentSessionFacebookUsersIds ;
		
	}


	

}
