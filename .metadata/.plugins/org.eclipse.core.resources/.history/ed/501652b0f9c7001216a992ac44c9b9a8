package atlasapp.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.parse.ParseObject;

import atlasapp.database.DatabaseConstants.SIGN_NEW_USERS_CALLER;
import atlasapp.model.ATLContactModel;


public interface UserDelagateInterface {

	public void getSignNonAtlasUsersCallBack(SIGN_NEW_USERS_CALLER caller,boolean success,HashMap<String,ATLContactModel> newCurrentNonAtlasUserToSign);

	public void getFriendEmailOnParse(ATLContactModel friendsPropertiesOnParseByEmail, boolean success);
	public void registerSuccess(boolean success);
	public void signInSuccess(boolean success);
	public void getUserEmailOnParseCallBack(HashMap<String, String> userParseLogin, boolean success);
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

	public void resetPasswordCallBack(boolean emailSuccessfullySent, String parseMessage);

	
}
