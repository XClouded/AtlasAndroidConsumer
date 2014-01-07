package atlasapp.database;

import java.util.ArrayList;
import java.util.HashMap;


public interface ItemUserDelegateInterface {
	
	public void savedNewItemUserCallBack(boolean success);
	public void updateItemUserCallBack(boolean success);
	public void gotItemUserCallBack(ArrayList<ItemUserProperties> allItemUsersPropertiesList, ArrayList<ItemUserProperties> allItemUsersInviteesPropertiesList, boolean success);

	public void itemUsersSavedCallBack(boolean success, ArrayList<ItemUserProperties> inviteesEventInvites);
	public void getItemUserRecordsByWebItemIdCallBack(boolean success, ArrayList<ItemUserProperties> itemUserRecordsByWebItemId);
	public void getUserMoveFromParseCallBack(
			HashMap<String, ItemUserProperties> userMoveRecords, ArrayList<String> webItemIds, ArrayList<ItemUserProperties> itemUserPropertiesList);
	public void updateBookedItemUserCallBack(boolean success);




}
