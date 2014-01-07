package com.atlasapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;

import com.atlasapp.common.AtlasAndroidUser;
import com.atlasapp.common.CurrentSessionFriendsList;
import com.atlasapp.common.DB;
import com.atlasapp.common.Utilities;
import com.atlasapp.common.UtilitiesProject;

public class ATLFriendModel { 
	
	
	
	
	public static ATLFriendModelListenersInterface atlFriendModelListener;

	protected static int friendId=-1;
	protected String atlasId="";
	protected String firstname="";
	protected String lastname=""; 
	protected String company="";
	protected String phoneNumber="";  
	protected String emailAddress="";
	protected String picPath="";
	protected boolean fromFacebook=false;
	protected String facebookId="";
	protected boolean fromTwitter=false;
	protected String twitterId="";
	protected boolean fromLinkedin=false;
	protected String linkedinId="";
	protected boolean fromAddressBook=false;
	protected String addressBookId="";
	protected Date lastInteractionDatetime;
	protected Date modifiedDatetime;
	
	
	
	protected boolean isAtlasUser = false;
	private static Object mObject2;

	private HashMap<String, String> h;

	public ATLFriendModel() {
	}

	public ATLFriendModel(int friendId) {
		this.friendId = friendId;
		read();
	}

	public ATLFriendModel(HashMap<String, String> h) {
		fromHashMap(h);
	}
//	public static ArrayList<HashMap<String, String>> readAll() {
//		return DB.q("SELECT * FROM ATL_FRIEND order by FIRSTNAME");
//	}
	public static ArrayList<ATLFriendModel> readAll() {
		ArrayList<HashMap<String, String>> hA = DB.q("SELECT * FROM ATL_FRIEND order by FIRSTNAME");
		ATLFriendModel friend;
		ArrayList<ATLFriendModel> friendA = new ArrayList<ATLFriendModel>();
		if (hA!=null && hA.size()>0){
		
		friendA = new ArrayList<ATLFriendModel>(hA.size());
		for(HashMap<String, String> h : hA){
			friend = new ATLFriendModel();
			friend.fromHashMap(h);
			friendA.add(friend);
		}
		}
		return friendA;
	}
	
	/**
	 * Getting all user's friends email address that are on Atlas 
	 * @return a list of all emails
	 */
	public static ArrayList<String> readAllABEmails() {
		ArrayList<HashMap<String, String>> hA = DB.q("SELECT EMAIL_ADDRESS FROM ATL_FRIEND WHERE EMAIL_ADDRESS is not null AND FROM_ADDRESS_BOOK = '1' ORDER BY EMAIL_ADDRESS");
		ArrayList<String> emailAddressA = new ArrayList<String>();
		if (hA!=null && hA.size()>0){
		emailAddressA = new ArrayList<String>(hA.size());
		for(HashMap<String, String> h : hA){
			emailAddressA.add(h.get("EMAIL_ADDRESS"));
		}
		}
		return emailAddressA;
	}
	
	/**
	 * Getting all user's friends email address that are on Atlas 
	 * @return a list of all emails
	 */
	public static ArrayList<String> readAllEmails() {
		ArrayList<HashMap<String, String>> hA = DB.q("SELECT EMAIL_ADDRESS FROM ATL_FRIEND WHERE EMAIL_ADDRESS is not null ORDER BY EMAIL_ADDRESS");
		ArrayList<String> emailAddressA =  new ArrayList<String>();
		if (hA!=null && hA.size()>0){
		emailAddressA = new ArrayList<String>(hA.size());
		for(HashMap<String, String> h : hA){
			emailAddressA.add(h.get("EMAIL_ADDRESS"));
		}
		}
		return emailAddressA;
	}
	
	
	
	public static String getNameByAtlasId(String atlasId)
	{
		String name = "";
		if (atlasId!=null && !atlasId.equals(""))
		{
			
			ArrayList<HashMap<String, String>> hA = DB.q("SELECT FIRST_NAME FROM ATL_FRIEND WHERE ATLAS_ID='" + atlasId + "' LIMIT 1");
			ArrayList<String> names = new ArrayList<String>(hA.size());
			for(HashMap<String, String> h : hA){
				names.add(h.get("FIRST_NAME"));
			}
			name =  (names!=null && names.get(0)!=null)?names.get(0):"";
		}
		
		return name;
	}
	
	/**
	 * Getting all user's Adress Book Contacts friends that are on Atlas 
	 * @return a list of all adress book id's 
	 */
	public static ArrayList<String> readAllAdressBookId() {
		ArrayList<HashMap<String, String>> hA = DB.q("SELECT ADDRESS_BOOK_ID FROM ATL_FRIEND WHERE ADDRESS_BOOK_ID is not null ORDER BY ADDRESS_BOOK_ID");
		ArrayList<String> addressBookIdA = new ArrayList<String>(hA.size());
		for(HashMap<String, String> h : hA){
			addressBookIdA.add(h.get("ADDRESS_BOOK_ID"));
		}
		return addressBookIdA;
	}
	
	/**
	 * update the DB with the property given ...TO DO!!!!!
	 * @param id
	 * @param property
	 */  
	public static void updateFriendByID(String id, String property, String value)
	{
		//FRIEND_PROPERTY propertyUpdate = getFriendProperty(property);
		
		
		/// TO DO.......
		
//		switch (propertyUpdate)
//		{
//		case FB_ID: 
//			break;
//		}
		
		
	}
	
	/**
	 * Getting all user's Facebook friends that are on Atlas 
	 * @return a list of all facebook id's 
	 */
	public static ArrayList<String> readAllFacebookId() {

		ArrayList<HashMap<String, String>> hA = DB.q("SELECT FACEBOOK_ID FROM ATL_FRIEND WHERE FACEBOOK_ID is not null ORDER BY FACEBOOK_ID");
		ArrayList<String> facebookIdA = new ArrayList<String>();
		if (hA!=null){
		facebookIdA = new ArrayList<String>(hA.size());
		for(HashMap<String, String> h : hA){
			facebookIdA.add(h.get("FACEBOOK_ID"));
		}
		}
		return facebookIdA;
	}
	
	public void read() {
		fromHashMap(Utilities.eHash(DB.q("SELECT * FROM ATL_FRIEND WHERE FRIEND_ID='" + friendId + "' LIMIT 1")));
	}

	public void readByFacebookId() {
		fromHashMap(Utilities.eHash(DB.q("SELECT * FROM ATL_FRIEND WHERE FACEBOOK_ID='" + facebookId + "' LIMIT 1")));
	}
	
	
	public void readByAdressBookId() {
		fromHashMap(Utilities.eHash(DB.q("SELECT * FROM ATL_FRIEND WHERE ADDRESS_BOOK_ID='" + addressBookId + "' LIMIT 1")));
	}
	
	public void readByEmailAdress() {
		fromHashMap(Utilities.eHash(DB.q("SELECT * FROM ATL_FRIEND WHERE EMAIL_ADDRESS='" + emailAddress + "' LIMIT 1")));
	}
	
	public synchronized void write() {
		String sql = "insert into ATL_FRIEND (" 
				+ "ATLAS_ID,"
				+ "IS_ATLAS_USER,"
				+ "FIRSTNAME," 
				+ "LASTNAME,"
				+ "COMPANY,"
				+ "PHONE_NUMBER," 
				+ "EMAIL_ADDRESS,"
				+ "PIC_PATH,"
				+ "FROM_FACEBOOK,"
				+ "FACEBOOK_ID,"
				+ "FROM_TWITTER,"
				+ "TWITTER_ID,"
				+ "FROM_LINKEDIN,"
				+ "LINKEDIN_ID,"
				+ "FROM_ADDRESS_BOOK,"
				+ "ADDRESS_BOOK_ID) values (" 
				+ "'" + DB.prep(atlasId) + "',"
				+ "'" + DB.prep(isAtlasUser) + "',"
				+ "'" + DB.prep(firstname) + "'," 
				+ "'" + DB.prep(lastname) + "',"   
				+ "'" + DB.prep(company) + "'," 
				+ "'" + DB.prep(phoneNumber) + "'," 
				+ "'" + DB.prep(emailAddress) + "'," 
				+ "'" + DB.prep(picPath) + "'," 
				+ "'" + DB.prep(fromFacebook) + "'," 
				+ "'" + DB.prep(facebookId) + "'," 
				+ "'" + DB.prep(fromTwitter) + "'," 
				+ "'" + DB.prep(twitterId) + "',"
				+ "'" + DB.prep(fromLinkedin) + "'," 
				+ "'" + DB.prep(linkedinId) + "',"
				+ "'" + DB.prep(fromAddressBook) + "',"
				+ "'" + DB.prep(addressBookId) + "');";
		DB.q(sql);
		friendId = Integer.parseInt(DB.lastInsertId());
	}
	
	public HashMap<String, String> toHashMap() {
		h = new HashMap<String, String>();
		h.put("FRIEND_ID", Integer.toString(friendId));
		h.put("IS_ATLAS_USER", Utilities.toString(isAtlasUser));
		h.put("ATLAS_ID", atlasId);
		h.put("FIRSTNAME", firstname);
		h.put("LASTNAME", lastname);
		h.put("COMPANY", company);
		h.put("PHONE_NUMBER", phoneNumber);
		h.put("EMAIL_ADDRESS", emailAddress);
		h.put("PIC_PATH", picPath);
		h.put("FROM_FACEBOOK", Utilities.toString(fromFacebook));
		h.put("FACEBOOK_ID", facebookId);
		h.put("FROM_TWITTER", Utilities.toString(fromTwitter));
		h.put("FACEBOOK_ID", facebookId);
		h.put("FROM_LINKEDIN", Utilities.toString(fromLinkedin));
		h.put("LINKEDIN_ID", linkedinId);
		h.put("FROM_ADDRESS_BOOK", Utilities.toString(fromAddressBook));
		h.put("ADDRESS_BOOK_ID", addressBookId);
		return h;
	}

	public void fromHashMap(HashMap<String, String> h) {
		this.h = h;
		friendId = Integer.parseInt((String) h.get("FRIEND_ID"));
		isAtlasUser = Utilities.toBoolean(h.get("IS_ATLAS_USER"));
		atlasId = (String) h.get("ATLAS_ID");
		firstname = (String) h.get("FIRSTNAME");
		lastname = (String) h.get("LASTNAME");
		company = (String) h.get("COMPANY");
		phoneNumber = (String) h.get("PHONE_NUMBER");
		emailAddress = (String) h.get("EMAIL_ADDRESS");
		picPath = (String) h.get("PIC_PATH");
		fromFacebook = Utilities.toBoolean(h.get("FROM_FACEBOOK"));
		facebookId = (String) h.get("FACEBOOK_ID");
		fromTwitter = Utilities.toBoolean(h.get("FROM_TWITTER"));
		twitterId = (String) h.get("TWITTER_ID");
		fromLinkedin = Utilities.toBoolean(h.get("FROM_LINKEDIN"));
		linkedinId = (String) h.get("LINKEDIN_ID");
		fromAddressBook = Utilities.toBoolean(h.get("FROM_ADDRESS_BOOK"));
		addressBookId = (String) h.get("ADDRESS_BOOK_ID");
	}

	public HashMap<String, String> getH() {
		return h;
	}

	public String toString() {
		toHashMap();
		String s = "";
		for (String key : h.keySet()) {
			s += key + " = " + h.get(key) + "\n";
		}
		return s;
	}
	
	/** Accessors **/
	
	public int getFriendId() {
		return friendId;
	}

	public void setFriendId(int friendId) {
		this.friendId = friendId;
	}
		
	public String getAtlasId() {
		return atlasId;
	}
	public void setAtlasId(String atlasId) {
		this.atlasId = atlasId;
	}
	
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public boolean isFromFacebook() {
		return fromFacebook;
	}

	public void setFromFacebook(boolean fromFacebook) {
		this.fromFacebook = fromFacebook;
	}
	public boolean isAtlasUser() {
		return isAtlasUser;
	}

	public void setIsAtlasUser(boolean atlasUser) {
		this.isAtlasUser = atlasUser;
	}
	
	public String getFacebookId() {
		return facebookId;
	}
	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public boolean isFromTwitter() {
		return fromTwitter;
	}

	public void setFromTwitter(boolean fromTwitter) {
		this.fromTwitter = fromTwitter;
	}
	
	public String getTwitterId() {
		return twitterId;
	}

	public void setTwitterId(String twitterId) {
		this.twitterId = twitterId;
	}
	
	public boolean isFromLinkedin() {
		return fromLinkedin;
	}

	public void setFromLinkedin(boolean fromLinkedin) {
		this.fromLinkedin = fromLinkedin;
	}
	
	public String getLinkedinId() {
		return linkedinId;
	}

	public void getLinkedinId(String linkedinId) {
		this.linkedinId = linkedinId;
	}
	
	public boolean isFromAddressBook() {
		return fromAddressBook;
	}

	public void setFromAddressBook(boolean fromAddressBook) {
		this.fromAddressBook = fromAddressBook;
	}
	
	public String getAddressBookId() {
		return addressBookId;
	}

	public void setAddressBookId(String addressBookId) {
		this.addressBookId = addressBookId;
	}

	
	/**
	 * Calls an AsynTask class for reading all the 
	 * friend records from the local Friend table 
	 * in a background process.
	 * and update the CurrentSessionFriendsList singleton 
	 * class and properties of the ATLContactModel list retrieved
	 */
	public synchronized static void readAllLocalFriendsInBackground() {
		ReadAllFriendTableInBackground longOperation = new ReadAllFriendTableInBackground();
	  	longOperation.execute();
	}
	public static class  LongOperation extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			
			
			String[] operation = params;
			
			CurrentSessionFriendsList cuurentSessionFriends = CurrentSessionFriendsList.getSingletonObject();
			
			
			ArrayList<HashMap<String, String>> hA = DB.q("SELECT * FROM ATL_FRIEND order by FIRSTNAME");
			ATLContactModel friend;
			if (hA!=null && hA.size()>0)
			{
			cuurentSessionFriends.setCurrentFriendsList(new ArrayList<ATLContactModel>(hA.size()));
			ArrayList<ATLContactModel> friends = new ArrayList<ATLContactModel>();
			for(HashMap<String, String> h : hA){
				friend = new ATLContactModel();
				friend.fromHashMap(h);
				friends.add(friend);
				if (friend.getAtlasId()!=null && !friend.getAtlasId().equals("")
						&& friend.isAtlasUser)
					cuurentSessionFriends.addToCurrentATLFriendsList(friend);
				cuurentSessionFriends.addToCurrentFriendsList(friend);

			}
			
	//		cuurentSessionFriends.addToCurrentFriendsList(friends);
			//atlFriendModelListener.currentSessionFriendsRetreived(true);
			}
			return null;
		}
		
	}
	
	/**
	 * Read all the friends found on local Friend table
	 */
	public static class  ReadAllFriendTableInBackground extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... arg0) {
			
			// ATLFriendModel.readAllInBackground();
			
			CurrentSessionFriendsList cuurentSessionFriends = CurrentSessionFriendsList.getSingletonObject();
			
			
			ArrayList<HashMap<String, String>> hA = DB.q("SELECT * FROM ATL_FRIEND order by FIRSTNAME");
			ATLContactModel friend;
			if (hA!=null && hA.size()>0)
			{
			cuurentSessionFriends.setCurrentFriendsList(new ArrayList<ATLContactModel>(hA.size()));
			ArrayList<ATLContactModel> friends = new ArrayList<ATLContactModel>();
			for(HashMap<String, String> h : hA){
				friend = new ATLContactModel();
				friend.fromHashMap(h);
				friends.add(friend);
				if (friend.getAtlasId()!=null && !friend.getAtlasId().equals(""))
					cuurentSessionFriends.addToCurrentATLFriendsList(friend);
				cuurentSessionFriends.addToCurrentFriendsList(friend);

			}
			
//			cuurentSessionFriends.addToCurrentFriendsList(friends);
			//atlFriendModelListener.currentSessionFriendsRetreived(true);
			}
			
	   		return null;
		}
		 @Override
	      protected void onPostExecute(String result) {               
	      }

	      @Override
	      protected void onPreExecute() {
	      }

	     
		
	}


	
	
	
	

}
