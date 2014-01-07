package atlasapp.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import atlasapp.model.ATLContactModel;

import com.parse.ParseObject;
import com.parse.ParseUser;



public interface  ParseDBCallBackInterface {
	
	public void getFindQueryCallBack(List<ParseObject> foundQueryList,boolean found);
	public void getSaveCallBack(boolean saved,ParseObject parseObjectSaved);
	public void getObjectIdCallBack(ParseObject retreivedObjectId, boolean success);
	public void getDataCallBack(byte[] fileRetreived, boolean success);
	public void getSuccessCallBack(boolean success);
	public void registerSuccess(boolean signUpSuccess);
	public void signInSuccess(boolean success);
	public void saveFileCallBack(boolean success, ParseObject psrseObjectSaved);
	//public void getUserEmailOnParseCallBack(List<ParseObject> list, boolean success);
	public void getUserEmailOnParseCallBack(HashMap<String, String> userParseLogin,boolean success);
	public void getFriendEmailOnParseCallBack(List<ParseObject> loginProperties,boolean success);
	public void getFacebookFriendIdOnParseCallBack(
			List<ParseObject> findResult, boolean success);
	public void getAllAtlasUsersCallBack(List<ParseObject> findResult,
			boolean success);
	public void getAllFBAtlasUsersFriendsCallBack(List<ParseObject> findResult,
			boolean success);
	public void friendSignInSuccess(boolean success);
	public void getAtlasNewFriendsByEmailCallBack(List<ParseObject> findResult,
			boolean success);
	public void getUpateCallBack(boolean success);
	public void signInNewFriendUserSuccess(boolean success, ParseUser user);
	public void refreshUserPictureOnTheBackgroundCallBack(boolean success, String imageUrl);
	
	public void isEmailVerifiedCallBack(boolean verified);
	
//	public void getAllUsersContactsEmailsOnParseCallBack(ArrayList<ATLContactModel> contactFriends);

}
