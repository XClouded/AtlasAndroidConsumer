package com.atlasapp.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.atlasapp.common.ATLItemUserModel;
import com.atlasapp.common.CurrentSessionFriendsList;
import com.atlasapp.common.DB;
import com.atlasapp.common.Utilities;
import com.atlasapp.section_appentry.AtlasApplication;
import com.atlasapp.section_appentry.R;

public class ATLContactModel extends ATLFriendModel implements Parcelable {

	/**
	 * 
	 */
	// private static final long serialVersionUID = -7136974343734351719L;
	private String group;
	private String phoneNumberWork;
	private String emailAddressWork;
	private String url;
	private String addressStreet;
	private String addressCity;
	private String addressState;
	private String addressZip;
	private String notes;
	private String sortValue;
	private Bitmap image;
	private int sectionNumber;

	
	
	
	
	// private static Object mObject;
	private String strValue;
	private Integer intValue;
	private AtlasApplication applicationController;

	// private HashMap<String, String> h;

	public ATLContactModel() {
		friendId = -1;
	}

	public ATLContactModel(ATLFriendModel friend) {
		friendId = friend.friendId;
		isAtlasUser = friend.isAtlasUser;
		atlasId = friend.atlasId;
		firstname = friend.firstname;
		lastname = friend.lastname;
		company = friend.company;
		phoneNumber = friend.phoneNumber;
		emailAddress = friend.emailAddress;
		picPath = friend.picPath;
		fromFacebook = friend.fromFacebook;
		facebookId = friend.facebookId;
		fromTwitter = friend.fromTwitter;
		twitterId = friend.twitterId;
		fromLinkedin = friend.fromLinkedin;
		linkedinId = friend.linkedinId;
		fromAddressBook = friend.fromAddressBook;
		addressBookId = friend.addressBookId;
	}

	/**
	 * Gets the array list of friends atlas id's 
	 * and return hash map:
	 * key - atlas id
	 * value - ATLContactModel
	 * 
	 * ATLContactModel if on the local DB
	 * or NULL otherwise
	 * @param 
	 * @return
	 */
	public static HashMap<String,ATLContactModel> getContactByAtlasId(ArrayList<String> atlasIds)
	{
		HashMap<String,ATLContactModel> contact= null;
		
		if (atlasIds!=null && atlasIds.size()>0)
		{
			contact = new HashMap<String,ATLContactModel> ();
			String sql = "select * from ATL_FRIEND where ATLAS_ID = '" + atlasIds.get(0) + "' ";
			for  (String id:atlasIds)
			{
			    if (id!=null && !id.equals(""))
			    	sql +="OR atlas_id = '" + id + "' ";
			    
			}
			ArrayList<HashMap<String,String>> itemsH = DB.q(sql);
			
			
			
			ATLContactModel friend;
			if (itemsH!=null && itemsH.size()>0)
			{
		//	ArrayList<ATLContactModel> friends = new ArrayList<ATLContactModel>();
			
			
			for(HashMap<String, String> h : itemsH){
				friend = new ATLContactModel();
				friend.fromHashMap(h);
				contact.put(friend.atlasId, friend);
				
			}
			
			}
		
		
		}
		return contact;
	}
	
	
	public String displayName() {
		String displayName = "";
		if (firstname != null) {
			displayName = firstname;
			if (lastname != null) {
				displayName += " " + lastname;
			}
		} else if (lastname != null) {
			displayName = lastname;
		} else {
			displayName = Utilities.deNull(company);
		}
		return displayName;
	}

	public boolean hasPhoneNumber() {
		return Utilities.deNull(phoneNumber).length() > 0;
	}

	public boolean hasPhoneNumberWork() {
		return Utilities.deNull(phoneNumberWork).length() > 0;
	}

	public boolean hasEmailAddress() {
		return Utilities.deNull(emailAddress).length() > 0;
	}

	public boolean hasEmailAddressWork() {
		return Utilities.deNull(emailAddressWork).length() > 0;
	}

	public boolean hasUrl() {
		return Utilities.deNull(url).length() > 0;
	}

	public boolean hasAddress() {
		return Utilities.deNull(addressStreet).length() > 0
				|| Utilities.deNull(addressCity).length() > 0
				|| Utilities.deNull(addressState).length() > 0
				|| Utilities.deNull(addressZip).length() > 0;
	}

	public boolean hasNotes() {
		return Utilities.deNull(notes).length() > 0;
	}

	// public String toString(){
	// returnrmat:@"\n CONTACT \n group=%@ \n phoneNumberWork=%@ \n emailAddressWork=%@ \n url=%@ \n addressStreet=%@ \n addressCity=%@ \n addressState=%@ \n addressZip=%@ \n notes=%@ ",group,phoneNumberWork,emailAddressWork,url,addressStreet,addressCity,addressState,addressZip,notes];
	// return s;
	// }

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getPhoneNumberWork() {
		return phoneNumberWork;
	}

	public void setPhoneNumberWork(String phoneNumberWork) {
		this.phoneNumberWork = phoneNumberWork;
	}

	public String getEmailAddressWork() {
		return emailAddressWork;
	}

	public void setEmailAddressWork(String emailAddressWork) {
		this.emailAddressWork = emailAddressWork;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAddressStreet() {
		return addressStreet;
	}

	public void setAddressStreet(String addressStreet) {
		this.addressStreet = addressStreet;
	}

	public String getAddressCity() {
		return addressCity;
	}

	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}

	public String getAddressState() {
		return addressState;
	}

	public void setAddressState(String addressState) {
		this.addressState = addressState;
	}

	public String getAddressZip() {
		return addressZip;
	}

	public void setAddressZip(String addressZip) {
		this.addressZip = addressZip;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getSortValue() {
		return sortValue;
	}

	public void setSortValue(String sortValue) {
		this.sortValue = sortValue;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public int getSectionNumber() {
		return sectionNumber;
	}

	public void setSectionNumber(int sectionNumber) {
		this.sectionNumber = sectionNumber;
	}

	//***Parcelable
	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {
		
		out.writeInt(friendId);
		out.writeBooleanArray(new boolean[] {isAtlasUser});
		out.writeString(atlasId);
		out.writeString(firstname);
		out.writeString(lastname);
		out.writeString(company);
		out.writeString(phoneNumber);
		out.writeString(emailAddress);
		out.writeString(picPath);
		out.writeBooleanArray(new boolean[] {fromFacebook});
		
		out.writeString(facebookId);
		out.writeBooleanArray(new boolean[] {fromTwitter});
		out.writeString(twitterId);
		out.writeBooleanArray(new boolean[] {fromLinkedin});
		out.writeString(linkedinId);
		out.writeBooleanArray(new boolean[] {fromAddressBook});
		out.writeString(addressBookId);
		out.writeSerializable(lastInteractionDatetime);
		out.writeSerializable(modifiedDatetime);

		out.writeString(group);
		out.writeString(phoneNumberWork);
		out.writeString(emailAddressWork);
		out.writeString(url);
		out.writeString(addressStreet);
		out.writeString(addressCity);
		out.writeString(addressState);
		out.writeString(addressZip);
		out.writeString(notes);
		out.writeString(sortValue);
		//setProfilePic();
		//out.writeParcelable(image,0);
		out.writeInt(sectionNumber);
	}

	public static final Parcelable.Creator<ATLContactModel> CREATOR = new Parcelable.Creator<ATLContactModel>() {
		public ATLContactModel createFromParcel(Parcel in) {
			return new ATLContactModel(in);
		}

		public ATLContactModel[] newArray(int size) {
			return new ATLContactModel[size];
		}
	};

	private ATLContactModel(Parcel in) {
		boolean[] booleanA = new boolean[1];

		friendId = in.readInt();
		in.readBooleanArray(booleanA);
		isAtlasUser =  booleanA[0];
		atlasId = in.readString();
		firstname = in.readString();
		lastname = in.readString();
		company = in.readString();
		phoneNumber = in.readString();
		emailAddress = in.readString();
		picPath = in.readString();
		in.readBooleanArray(booleanA);
		fromFacebook = booleanA[0];
		facebookId = in.readString();
		in.readBooleanArray(booleanA);
		fromTwitter = booleanA[0];
		twitterId = in.readString();
		in.readBooleanArray(booleanA);
		fromLinkedin = booleanA[0];
		linkedinId = in.readString();
		in.readBooleanArray(booleanA);
		fromAddressBook = booleanA[0];
		addressBookId = in.readString();
		lastInteractionDatetime = (Date)in.readSerializable();
		modifiedDatetime = (Date)in.readSerializable();

		group = in.readString();
		phoneNumberWork = in.readString();
		emailAddressWork = in.readString();
		url = in.readString();
		addressStreet = in.readString();
		addressCity = in.readString();
		addressState = in.readString();
		addressZip = in.readString();
		notes = in.readString();
		sortValue = in.readString();
	//	setProfilePic();
	//	image = (Bitmap)in.readParcelable(getClass().getClassLoader());
		sectionNumber = in.readInt();
	}
	
	  
	
private void setProfilePic() {	
		
//	ImageView contactPhoto = (ImageView) findViewById(R.id.contactImage);
	File FRIENDS_IMAGE_DIR = new File(Environment.getExternalStorageDirectory()
            + "/Android/data/com.atlastpowered/files/Pictures/friendPics");
	String profilePicName = (atlasId!=null && !atlasId.equals(""))?
			atlasId:"";
	String destinationImagePath= "/"+profilePicName+".png";	
			
	if (!profilePicName.equals(""))
	{
		// Bitmap storedBitmap = null;
		 File PROFILE_PIC_PATH= new File (FRIENDS_IMAGE_DIR,destinationImagePath) ;
			if(PROFILE_PIC_PATH.exists())
			//String filePath = applicationController.IMAGE_DIR+"/"+profilePicName+".png";
	
	
			 image = BitmapFactory.decodeFile(PROFILE_PIC_PATH.getAbsolutePath());

		//	contactPhoto.setImageBitmap(storedBitmap);
		//	invitee.setImage(storedBitmap);
	}
	}// TO
	
	
	
}
