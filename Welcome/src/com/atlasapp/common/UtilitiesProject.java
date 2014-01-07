package com.atlasapp.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.atlasapp.atlas_database.ATLDBCommon;
import com.atlasapp.atlas_database.AtlasServerConnect;
import com.atlasapp.atlas_database.DatabaseConstants.SIGN_NEW_USERS_CALLER;
import com.atlasapp.atlas_database.DatabaseConstants.TABLE_ALERTS;
import com.atlasapp.atlas_database.UserDelagateInterface;
import com.atlasapp.facebook.FacebookUtilities;
import com.atlasapp.model.ATLFriendModel;
import com.atlasapp.model.ATLContactModel;
import com.atlasapp.model.ATLFriendModel.LongOperation;
import com.atlasapp.section_appentry.AtlasApplication;
import com.atlasapp.section_appentry.R;
import com.atlasapp.section_appentry.Register;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.ImageView;
import android.widget.Toast;
public final class UtilitiesProject implements UserDelagateInterface{
	static Context ctx;

	// ---------------------------------------------------
	public final static boolean IS_DEBUG_MODE = false ;   
	public final static String ENVIRON = "dev"; //dev | qa | prod

	public final static boolean RESET_DB = false ;                                                                          
	// --------------------------------------------------- 

	public final static String DB_NAME = "atlasDB";
	public final static int DB_VERSION = 1;
	
	public static Activity currentActivity;

	private static AtlasServerConnect parseConnect;
	  
	
	
	// Facebook Friends that are already on local Friend DB
// HARRIS NEW:
//		private static ArrayList<ATLContactModel> atlasFBFriendsonDB = null ;
		private static ArrayList<ATLContactModel> atlasFBFriendsonDB = null ;
	// Friends from address book that are already on local Friend DB
// HARRIS NEW:
//  private static ArrayList<ATLContactModel> atlasABFriendsonDB = null ;
	private static ArrayList<ATLContactModel> atlasABFriendsonDB = null ;
	//Friends of the user (contacts/facebook) that dont have the Atlas app
// HARRIS NEW:
// private static ArrayList<ATLContactModel> nonAtlasContacts = null;
	private static ArrayList<ATLContactModel> nonAtlasContacts = null;
	// Friends of the user (address book/friend) that have the Atlas app 
	// but needed to be uddated on the user's local DB
// HARRIS NEW:
// private static ArrayList<ATLContactModel> newAtlasFriends = null;
	private static ArrayList<ATLContactModel> newAtlasFriends = null;
	
	private static ArrayList<ATLContactModel> newFBAtlasFriends = null;
	private static ArrayList<String> newFBAtlasFriendsEmails = null;
	private static ArrayList<ATLContactModel> newABAtlasFriends = null;
	private static ArrayList<String> newABAtlasFriendsEmails = null;

	private static ArrayList<String> adressBookFriends;

	private static ArrayList<String> adressBookFriendsbyEmail;

	private static CurrentSessionFriendsList currentSessionFriendsList;

	private static ATLDBCommon atlDBCommon;

	public static String deviceId;
	
	
	/**
	 * Get all users Friends from Facebook that are already on local Friend DB
	 * @return
	 */
// HARRIS NEW:
// public static ArrayList<ATLContactModel> getAtlasFBFriendsonDB () {
//	public static ArrayList<ATLContactModel> getAtlasFBFriendsonDB () {
//		
//	
////		if (atlasFriendsonDB==null)
////		{
////			updateUsersContactsFromAdressBook();
////			
////		}
//		return atlasFBFriendsonDB;
//	}
	
	/**
	 * Get all users Friends from address book that are already on local Friend DB
	 * @return
	 */
// HARRIS NEW:
// public static ArrayList<ATLContactModel> getAtlasABFriendsonDB () {
//	public static ArrayList<ATLContactModel> getAtlasABFriendsonDB () {
//		
//	
////		if (atlasFriendsonDB==null)
////		{
////			updateUsersContactsFromAdressBook();
////			
////		}
//		return atlasABFriendsonDB;
//	}
	/**
	 * Friends of the user (address book/friend) that have the Atlas app 
	 * but needed to be updated on the user's local DB
	 * @return
	 */
// HARRIS NEW:
//	public static ArrayList<ATLContactModel> getNewAtlasFriends()
	
//	public static ArrayList<ATLContactModel> getNewAtlasFriends()
//	{
//		if (newAtlasFriends == null)
//		{
//			//updateUsersContactsFromAdressBook();
//			if (newFBAtlasFriends!=null)
//			{
//				newAtlasFriends = newFBAtlasFriends;
//				if (newABAtlasFriends!=null)
//				{
//					newAtlasFriends.addAll(newABAtlasFriends);
//				}
//			}else
//				if (newABAtlasFriends!=null)
//					newAtlasFriends = newABAtlasFriends;
//			
//		}
//		return newAtlasFriends;
//	}
	
	/**
	 * Friends of the user (contacts/facebook) that dont have the Atlas app
	 * @return
	 */
// HARRIS NEW:
//public static ArrayList<ATLContactModel> getNonAtlasContacts()
//	public static ArrayList<ATLContactModel> getNonAtlasContacts()
//	{
////		if (nonAtlasContacts == null)
////		{
////			updateUsersContactsFromAdressBook();
////		}
//		return nonAtlasContacts;
//	}
	
	public final static void init(Context aCtx) {
		ctx = aCtx;
		final TelephonyManager tm =(TelephonyManager)ctx.getSystemService(Context.TELEPHONY_SERVICE);

		deviceId = tm.getDeviceId();
		 parseConnect =   AtlasServerConnect.getSingletonObject(null);
		 atlDBCommon = ATLDBCommon.getSingletonObject(null);
	}
	
//
	

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
		// TODO Auto-generated method stub
		
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
