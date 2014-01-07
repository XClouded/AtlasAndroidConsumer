package com.atlasapp.atlas_database;

import java.util.ArrayList;

import com.atlasapp.model.ATLContactModel;

public interface ItemUserDelegateInterface {
	
	public void savedNewItemUserCallBack(boolean success);
	public void updateItemUserCallBack(boolean success);
	public void gotItemUserCallBack(ArrayList<ItemUserProperties> allItemUsersPropertiesList, ArrayList<ItemUserProperties> allItemUsersInviteesPropertiesList, boolean success);

	public void itemUsersSavedCallBack(boolean success, ArrayList<ItemUserProperties> inviteesEventInvites);
	public void getItemUserRecordsByWebItemIdCallBack(boolean success, ArrayList<ItemUserProperties> itemUserRecordsByWebItemId);




}
