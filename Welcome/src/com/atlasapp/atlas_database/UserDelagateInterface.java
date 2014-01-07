package com.atlasapp.atlas_database;

import java.util.ArrayList;
import java.util.HashMap;

import com.atlasapp.atlas_database.DatabaseConstants.SIGN_NEW_USERS_CALLER;
import com.atlasapp.model.ATLContactModel;

public interface UserDelagateInterface {

	public void getSignNonAtlasUsersCallBack(SIGN_NEW_USERS_CALLER caller,boolean success,HashMap<String,ATLContactModel> newCurrentNonAtlasUserToSign);

	public void getFriendEmailOnParse(ATLContactModel friendsPropertiesOnParseByEmail, boolean success);
	public void registerSuccess(boolean success);
	public void signInSuccess(boolean success);
	public void getUserEmailOnParseCallBack(HashMap<String, String> loginProperties, boolean success);
	public void getFacebookFriendIdOnParse(ATLContactModel facebookAtlasFriend,
			boolean success);
	public void getAllFBAtlasUsersCallBack(
			ArrayList<ATLContactModel> allFacebookAtlasUsers, boolean success);
	public void getAllFBAtlasUsersFriendsCallBack(
			ArrayList<ATLContactModel> allFacebookAtlasUsers, boolean success);
	public void saveCallBack(boolean saved);
	public void getAtlasNewFriendsByEmailCallBack(
			ArrayList<ATLContactModel> allCommonAtlasUsers);
	public void getUpateCallBack(boolean success); 
}
