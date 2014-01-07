package atlasapp.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.parse.ParseObject;

import atlasapp.database.DatabaseConstants.OPERATION_METHOD;
import atlasapp.database.DatabaseConstants.SERVER_MESSAGE;
import atlasapp.database.DatabaseConstants.TABLE_ALERTS;
import atlasapp.section_appentry.AtlasApplication;


public interface IAtlasServerTable {
	
	
	public  void  setTableName(String tableName);
	public void save();
//	public void findWhereUserEqualToEmail(String column, Object value);
	public void findWhereEqualTo(HashMap<String,Object> conditions,OPERATION_METHOD method);

	public void findWhereExists(String column,OPERATION_METHOD method);
	public void findWhereContainedIn(String column, List<String> valueList,OPERATION_METHOD method);
	public void findWhereNotContainedInBackground(String column, List<String> valueList);
	public void findAlertsInBackground(HashMap<String,Object> query,TABLE_ALERTS alert);
	public void findWhereNotContainedIn(String column, List<String> valueList,OPERATION_METHOD method);
	public Object get(String column);
	public String getString(String column);
	public byte[] getBytes(String column);
	public void getFile(String fileColumnName);
	public void saveFriendPictureFileInBackground(ParseObject parseObject, String fileName, AtlasApplication application);
	public void saveImageFile(String fileName, byte[] image);
	public String getColumnNameByFile(String fileName);
	public SERVER_MESSAGE put(HashMap<String, Object> fieldsValues,UserDelagateInterface listener);
	public String getAtlasID();
	public void retrieveRowByObjectId(String objectId);
	//public HashMap<String, ATLContactModel> getAllUsersOnTable(ITEM_TYPE table);
	public void updateRowInTableInBackground(String objectId,HashMap<String,Object> updates);
//	public void findWhereFriendUserEqualToEmail(String column, Object value);
//	public void findWhereFacebookFriendUserEqualToID(String column, Object value);
	public void update();
	SERVER_MESSAGE put(HashMap<String, Object> fieldsValues,
			InviteToAtlasDelagateInterface listener);
	SERVER_MESSAGE put(HashMap<String, Object> fieldsValues,
			ItemUserDelegateInterface listener);
	void saveItemUser();
	SERVER_MESSAGE put(HashMap<String, Object> fieldsValues);
	SERVER_MESSAGE put(HashMap<String, Object> fieldsValues,
			EventControllerCallBackInterface listener);
	void updateObjectId(String objectId, HashMap<String, Object> fieldsValues);
	void putUpdated(HashMap<String, Object> fieldsValues);
	void updateUser();

}  
