package com.atlasapp.atlas_database;

import java.util.HashMap;
import java.util.List;

import com.atlasapp.atlas_database.DatabaseConstants.OPERATION_METHOD;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class InviteToAtlas extends AtlasServerTable implements ParseDBCallBackInterface{
	public InviteToAtlasDelagateInterface inviteATLDelagator;
	
	public InviteToAtlas()
	{
		setTableName("inviteToAtlas");
		atlasServerTable = new ParseObject(TABLE_NAME);
		ParseACL defaultACL = new ParseACL();
		defaultACL.setPublicReadAccess(true);
		defaultACL.setPublicWriteAccess(true);
		atlasServerTable.setACL(defaultACL);
	}

	

	@Override
	public void getFindQueryCallBack(List<ParseObject> foundQueryList,
			boolean found) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getSaveCallBack(boolean saved,ParseObject parseObjectSaved) {
		inviteATLDelagator.getInviteSuccessCallBack(atlasServerTable.getObjectId(), saved);
		
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
	public void getFriendEmailOnParseCallBack(
			List<ParseObject> loginProperties, boolean success) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getFacebookFriendIdOnParseCallBack(
			List<ParseObject> findResult, boolean success) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void getAllFBAtlasUsersFriendsCallBack(List<ParseObject> findResult,
			boolean success) {
		// TODO Auto-generated method stub
		
	}



	


	@Override
	public void getAllAtlasUsersCallBack(List<ParseObject> findResult,
			boolean success) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void friendSignInSuccess(boolean success) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void getAtlasNewFriendsByEmailCallBack(List<ParseObject> findResult,
			boolean success) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void getUpateCallBack(boolean success) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void saveFileCallBack(boolean success, ParseObject psrseObjectSaved) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void signInNewFriendUserSuccess(boolean success, ParseUser user) {
		// TODO Auto-generated method stub
		
	}

}
