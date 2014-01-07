package com.atlasapp.model;

import java.util.ArrayList;

public interface ATLFriendModelListenersInterface {
	
	
	
	public void readLocalFriendTableCallBack(boolean success, ArrayList<ATLContactModel> friendsFromLocal);

}
