package atlasapp.common;

import java.util.ArrayList;

import atlasapp.model.ATLContactModel;

public interface CurrentSessionFriendsListCallBack {
	
	void onSetCurrentATLFriendsList(ArrayList<ATLContactModel> friends);
	
	void onSetCurrentFriendsList(ArrayList<ATLContactModel> friends);

	void onSetCurrentFacebookFriendsList(
			ArrayList<ATLContactModel> allAtlContactModelFacebookFriends);
	
	

}
