package com.atlasapp.atlas_database;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.atlasapp.atlas_database.DatabaseConstants.ITEM_TYPE;
import com.atlasapp.atlas_database.DatabaseConstants.OPERATION_METHOD;
import com.atlasapp.atlas_database.DatabaseConstants.SERVER_MESSAGE;
import com.atlasapp.atlas_database.DatabaseConstants.TABLE_ALERTS;
import com.atlasapp.common.ATLCurrentsUserAlerts;
import com.atlasapp.common.AtlasAndroidUser;
import com.atlasapp.model.ATLAlertModel;
import com.atlasapp.model.ATLContactModel;
import com.atlasapp.section_appentry.AtlasApplication;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
 
public class AtlasServerTable implements IAtlasServerTable{
	
	protected String TABLE_NAME;
	protected ParseObject atlasServerTable;
//	protected ParseUser atlasServerUsersTable;
	protected boolean connecting;
	protected ParseQuery userQuery,parseQuery;
	protected String parseQueryObjectId;
	protected List<ParseObject> findResult;
	protected ParseObject retreivedObjectId;
//	protected LongOperation longOperation;
	protected byte[] image;
	protected ParseFile parseFile;
	public ParseDBCallBackInterface parseCallBackDeleget;
	
	public UserDelagateInterface userListener;
	
	protected ArrayList<String> allUserNamesOnTable,allFBUserNamesOnTable,allFBUserFacebookIdOnTable;
	protected List<ParseObject> allUsersOnTable;

	public static AtlasApplication application; 

	
	protected HashMap<String, ATLContactModel> allContactsmodel,allFBUsersModelOnTable;
	protected SERVER_MESSAGE updateTableMessage = SERVER_MESSAGE.FAIL;
	private AtlasServerConnect parseConnect;
	@Override
	public String getAtlasID() {
		return atlasServerTable.getObjectId();
	}
	public void saveUser()
	{
		//performLongOperation("save_user");
		performParseCallOperation(new String[]{"save_user"});
		
	}
	
	@Override
	public void save() {
		//performLongOperation("save");
		performParseCallOperation(new String[]{"save"});
		
	} 
	@Override
	public void saveItemUser() {
		//performLongOperation("save");
		performParseCallOperation(new String[]{"save_item_user"});
		
	} 
	@Override
	public void update() {
		//performLongOperation("save");
		performParseCallOperation(new String[]{"update"});
		
	} 
	@Override
	public String getString(String column) {
		return atlasServerTable.getString(column);
	}


	@Override
	public Object get(String column) {
		
		return atlasServerTable.get(column);
	}
	@Override
	public byte[] getBytes(String column) {
		return atlasServerTable.getBytes(column);
	}
	
	
	
	@Override
	public String getColumnNameByFile(String fileName) {
		String columnName = "";
		
		columnName = (fileName.equals("image.jpg"))? "picture" : "";
		
		
		return columnName;
	}
	@Override
	public void saveImageFile(String fileName, byte[] image) {
		
		parseFile = new ParseFile(fileName, image);
		connecting = true;
	//	longOperation = new LongOperation();
		String columnName = getColumnNameByFile(fileName);
		//longOperation.execute("save_file",columnName);
		performParseCallOperation(new String[]{"save_file",columnName});

//		while (connecting)
//		{
//		 try {
//		      Thread.sleep(1000);
//		 } catch (InterruptedException e) {
//		      e.printStackTrace();
//		   }
//		   continue;
//		}
	}
	@Override
	public void getFile(String fileColumnName) {
		
		
		parseFile = (ParseFile)atlasServerTable.get(fileColumnName);
		if (parseFile!=null)
		{
			//performLongOperation("get_data");
			performParseCallOperation(new String[]{"get_data"});
		}	
		//return image;
	}

	

	@Override
	public void setTableName(String tableName) {
		TABLE_NAME = tableName;
		this.atlasServerTable = new ParseObject(tableName);
		
	}
	@Override
	public void putUpdated(HashMap<String, Object> fieldsValues) {
		
		Iterator iter = fieldsValues.keySet().iterator();
	
		while(iter.hasNext()) {

			String key = (String)iter.next();
			Object val = (Object)fieldsValues.get(key);
			
			atlasServerTable.put(key, val);
		}
		update();
		
	}
	@Override
	public SERVER_MESSAGE put(HashMap<String, Object> fieldsValues) {
		
		Iterator iter = fieldsValues.keySet().iterator();
	
		while(iter.hasNext()) {

			String key = (String)iter.next();
			Object val = (Object)fieldsValues.get(key);
			
			atlasServerTable.put(key, val);
		}
		save();
		return updateTableMessage;
		
	}
	
	/**
	 * Generates a new ParseObject to be saved on 
	 * the current parse table object 
	 * without saving it
	 * @param fieldsValues
	 */
	public void generateNewRecord(HashMap<String, Object> fieldsValues) {
		
		Iterator iter = fieldsValues.keySet().iterator();
	
		while(iter.hasNext()) {

			String key = (String)iter.next();
			Object val = (Object)fieldsValues.get(key);
			
			atlasServerTable.put(key, val);
		}
		
	}
	
	
	
	@Override
	public SERVER_MESSAGE put(HashMap<String, Object> fieldsValues,EventControllerCallBackInterface listener) {
		
		Iterator iter = fieldsValues.keySet().iterator();
	
		while(iter.hasNext()) {

			String key = (String)iter.next();
			Object val = (Object)fieldsValues.get(key);
			
			atlasServerTable.put(key, val);
		}
		save();
		return updateTableMessage;
		
	}
	@Override
	public SERVER_MESSAGE put(HashMap<String, Object> fieldsValues,ItemUserDelegateInterface listener) {
		
		Iterator iter = fieldsValues.keySet().iterator();
	
		while(iter.hasNext()) {

			String key = (String)iter.next();
			Object val = (Object)fieldsValues.get(key);
			
			atlasServerTable.put(key, val);
		}
		saveItemUser();
		return updateTableMessage;
		
	}
	private HashMap<String, Object> updatedColumns;
	@Override
	public void updateObjectId(String objectId,HashMap<String, Object> fieldsValues) {
		
		updatedColumns = fieldsValues;
		if (updatedColumns!= null && updatedColumns.size()>0)
		{
			performParseCallOperation(new String[]{"update_object_id",objectId});

		}
		
	}
	
	@Override
	public SERVER_MESSAGE put(HashMap<String, Object> fieldsValues,InviteToAtlasDelagateInterface listener) {
		
		Iterator iter = fieldsValues.keySet().iterator();
	
		while(iter.hasNext()) {

			String key = (String)iter.next();
			Object val = (Object)fieldsValues.get(key);
			
			atlasServerTable.put(key, val);
		}
		save();
		return updateTableMessage;
		
	}
//	public void putInBackground(HashMap<String, Object> fieldsValues) {
//		Iterator iter = fieldsValues.keySet().iterator();
//
//		while(iter.hasNext()) {
//
//			String key = (String)iter.next();
//			Object val = (Object)fieldsValues.get(key);
//			
//			atlasServerTable.put(key, val);
//		}
//		atlasServerTable.saveInBackground();
//	}
//	@Override
//	public void findWhereEqualTo(String column, Object value ) {
//		
//		connecting = true;
//		findResult = null;
//		
//		userQuery  = ParseUser.getQuery();
//		userQuery.whereEqualTo(column, value);
//	//	performLongOperation("find");
//			//String operation =(column!=null && column.equals("email"))? "find_user_email":"find";
//		performParseCallOperation(new String[]{"find"});
//		//return findResult;
//	}
	
//	@Override
//	public void findWhereUserEqualToEmail(String column, Object value ) {
//		
//		connecting = true;
//		findResult = null;
//		
//		userQuery  = ParseUser.getQuery();
//		userQuery.whereEqualTo(column, value);
//	//	performLongOperation("find");
//		//	String operation =(column!=null && column.equals("email"))? "find_user_email":"find";
//		performParseCallOperation(new String[]{"find_user_email"});
//		//return findResult;
//	}
//	@Override
//	public void findWhereFriendUserEqualToEmail(String column, Object value ) {
//		
//		connecting = true;
//		findResult = null;
//		
//		userQuery  = ParseUser.getQuery();
//		userQuery.whereEqualTo(column, value);
//	//	performLongOperation("find");
//		//	String operation =(column!=null && column.equals("email"))? "find_user_email":"find";
//		performParseCallOperation(new String[]{"find_friend_email"});
//		//return findResult;
//	}
//	@Override
//	public void findWhereFacebookFriendUserEqualToID(String column, Object value ) {
//		
//		connecting = true;
//		findResult = null;
//		
//		userQuery  = ParseUser.getQuery();
//		userQuery.whereEqualTo(column, value);
//	//	performLongOperation("find");
//		//	String operation =(column!=null && column.equals("email"))? "find_user_email":"find";
//		performParseCallOperation(new String[]{"find_facebook_id"});
//		//return findResult;
//	}
	
	/**
	 * call back from getObjectIdCallBack for listeners
	 */
	@Override
	public void retrieveRowByObjectId(String objectId) {
		retreivedObjectId =null;
		parseQueryObjectId = "";
		
		if (TABLE_NAME!=null && !TABLE_NAME.equals("") && objectId!=null && !objectId.equals("") )
		{
			parseQuery = new ParseQuery(TABLE_NAME);
			parseQueryObjectId = objectId;
			performParseCallOperation(new String[]{"get"});
		}
	}
	
	
//	@Override
//	public ParseObject retrieveRowByObjectId(String objectId) {
//		connecting = true;
//		retreivedObjectId =null;
//		parseQueryObjectId = "";
//		
//		if (TABLE_NAME!=null && !TABLE_NAME.equals("") && objectId!=null && !objectId.equals("") )
//		{
//			parseQuery = new ParseQuery(TABLE_NAME);
//			parseQueryObjectId = objectId;
//		//	performLongOperation("get");
//			performParseCallOperation(new String[]{"get"});
//		}
//	//	return retreivedObjectId;
//	}
//	
	
	@Override
	public void findWhereEqualTo(HashMap<String, Object> conditions,OPERATION_METHOD method) 
	{
		connecting = true;
		findResult = null;
		//parseCallBackDeleget = listener;
		//userQuery  = ParseUser.getQuery();
		if (conditions!=null && parseQuery!=null){
		Iterator userCondition = conditions.keySet().iterator();
		
		while(userCondition.hasNext())
		{
		    String key=(String)userCondition.next();
		    Object value=conditions.get(key);
		    parseQuery.whereEqualTo(key, value);
		}
		
		
		//performLongOperation("find");
		performParseCallOperation(new String[]{method.getMethodName()});
		}
		//return findResult;
	}
	
	@Override
	public void findWhereExists(String column, OPERATION_METHOD method) {
		connecting = true;
		findResult = null;
		
		userQuery  = ParseUser.getQuery();
		userQuery.whereExists(column);
		
		//performLongOperation("find");
		performParseCallOperation(new String[]{method.getMethodName()});
		//return findResult;
	}

	@Override
	public void findWhereContainedIn(String column, List<String> valueList, OPERATION_METHOD method) {
		connecting = true;
		findResult = null;
		
		userQuery  = ParseUser.getQuery();
		userQuery.whereContainedIn(column, valueList);
		
		//performLongOperation("find");
		performParseCallOperation(new String[]{method.getMethodName()});
		//return findResult;
	}
	
	
	
	
	@Override
	public void findWhereNotContainedInBackground(String column,
			List<String> valueList) {
		
		
		
	
	}
	
	
	@Override
	public void findWhereNotContainedIn(String column,
			List<String> valueList,OPERATION_METHOD method) {
		connecting = true;
		findResult = null;
		
		userQuery  = ParseUser.getQuery();
		userQuery.whereNotContainedIn(column, valueList);
		//performLongOperation("find");
		performParseCallOperation(new String[]{method.getMethodName()});
		//return findResult;
	}
	@Override
	public void findAlertsInBackground(HashMap<String, Object> query,final TABLE_ALERTS alert) {
		if (query!=null && query.size()>0)
			{
			
				userQuery  = new ParseQuery(this.TABLE_NAME);
				Iterator userQueryToFind = query.keySet().iterator();
				
				while(userQueryToFind.hasNext())
				{
				    String column=(String)userQueryToFind.next();
				    Object value=(Object)query.get(column);  
				    userQuery.whereEqualTo(column, value);
				
				}
				userQuery.findInBackground(new FindCallback() {
				    public void done(List<ParseObject> alerts, ParseException e) {
				        if (e == null) {
				        	if (alerts!=null && alerts.size()>0){
				        	ATLCurrentsUserAlerts currentUserAlerts = ATLCurrentsUserAlerts.getSingletonObject(null);

							ArrayList<ATLAlertModel> newUserAlerts = new ArrayList<ATLAlertModel>();
				        	List<ATLAlertModel> alertsModel = new ArrayList<ATLAlertModel>();
  
				        	for (ParseObject newAlert:alerts)
							{	/*NGHIA Comment out
								ATLAlertModel alertModel = new ATLAlertModel(newAlert);
								alertModel.itemType = alert;  
								alertsModel.add(alertModel);
								*/
							}
							
							
								newUserAlerts.addAll(alertsModel);
						
							ArrayList<ATLAlertModel> userEventALerts = (alert.equals(TABLE_ALERTS.EVENT))?newUserAlerts:currentUserAlerts.currentUserEventAlerts;
							ArrayList<ATLAlertModel> userTaskAlerts = (alert.equals(TABLE_ALERTS.TASK))?newUserAlerts:currentUserAlerts.currentUserTaskAlerts ;
							
							currentUserAlerts.setCurrentUserEventAlerts(userEventALerts);
							currentUserAlerts.setCurrentUserTaskAlerts(userTaskAlerts);
						
							currentUserAlerts.setCurrentAllUserAlerts(userEventALerts);
							currentUserAlerts.setCurrentAllUserAlerts(userTaskAlerts);
							}
						
				        } else {
				        //    Log.d("score", "Error: " + e.getMessage());
				        }
				    }
				});
				
			}
	}
	
//	@Override
//	public void findWhereEqualToInBackground(HashMap<String, Object> query) {
//
//		
//		if (query!=null && query.size()>0)
//		{
//			userQuery  = ParseUser.getQuery();
//			Iterator userQueryToFind = query.keySet().iterator();
//			
//			while(userQueryToFind.hasNext())
//			{
//			    String column=(String)userQueryToFind.next();
//			    String value=(String)query.get(column);
//			    userQuery.whereEqualTo(column, value);
//			
//			}
//			userQuery.findInBackground(new FindCallback() {
//			    public void done(List<ParseObject> list, ParseException e) {
//			        if (e == null) {
//			      //      Log.d("score", "Retrieved " + scoreList.size() + " scores");
//			        } else {
//			        //    Log.d("score", "Error: " + e.getMessage());
//			        }
//			    }
//			});
//			
//		}
//	}
//	
	
//	@Override
//	public HashMap<String, ATLContactModel> getAllUsersOnTable(ITEM_TYPE table) {
//		
//
//		switch (table)
//		{
//		case USER:
//				User user = new User();
//				user.getAllUsersUserNamesOnTable();
//			break;
//		case TASK:
//			break;
//		case EVENT:
//			break;
//		case NOTE:
//			break;
//		}
//		
//		
//		return allContactsmodel;
//	}


	
//	public void performLongOperation(String operation)
//	{
//		connecting = true;
//		longOperation = new LongOperation();
//		longOperation.execute(operation);
//		
//		while (connecting)
//		{
//		 try {
//		      Thread.sleep(1000);
//		 } catch (InterruptedException e) {
//		      e.printStackTrace();
//		   }
//		   continue;
//		}
//	}
//	
	
	protected void performParseCallOperation(String[] operation)
	{
        parseConnect =   AtlasServerConnect.getSingletonObject(null);

		updateTableMessage = SERVER_MESSAGE.FAIL;
		OPERATION_METHOD method = 
				(operation[0].equals("register"))? OPERATION_METHOD.REGISTER:
				(operation[0].equals("sign_in"))? OPERATION_METHOD.SIGN_IN:
				(operation[0].equals("sign_in_new"))? OPERATION_METHOD.SIGN_IN_NEW:

				(operation[0].equals("sign_in_friend"))? OPERATION_METHOD.SIGN_IN_FRIEND:
				(operation[0].equals("save"))? OPERATION_METHOD.SAVE:
				(operation[0].equals("save_user"))? OPERATION_METHOD.SAVE_USER:
				(operation[0].equals("find"))? OPERATION_METHOD.FIND:
				(operation[0].equals("find_user_email"))? OPERATION_METHOD.FIND_USER_EMAIL:
				(operation[0].equals("find_friend_email"))? OPERATION_METHOD.FIND_FRIEND_EMAIL:
				(operation[0].equals("find_all_users"))? OPERATION_METHOD.FIND_ALL_USERS:
				(operation[0].equals("find_all_facebook_users_friends"))? OPERATION_METHOD.FIND_ALL_FACEBOOK_USERS_FRIENDS:
				(operation[0].equals("update"))? OPERATION_METHOD.UPDATE:
				(operation[0].equals("save_item_user"))? OPERATION_METHOD.SAVE_ITEM_USER:
				(operation[0].equals("update_object_id"))? OPERATION_METHOD.UPDATE_OBJECT_ID:

					
				(operation[0].equals("find_facebook_id"))? OPERATION_METHOD.FIND_FACEBOOK_ID:
					
				(operation[0].equals("get"))? OPERATION_METHOD.GET:
				(operation[0].equals("get_data"))? OPERATION_METHOD.GET_DATA:
				(operation[0].equals("save_file"))? OPERATION_METHOD.SAVE_FILE:
				OPERATION_METHOD.OTHER;
		
		
		switch(method)
		{
		case REGISTER:
			
				((ParseUser)atlasServerTable).signUpInBackground(new SignUpCallback() {
					  public void done(ParseException e) {
						    if (e == null) {
						      // Hooray! Let them use the app now.
						    	AtlasAndroidUser.setParseUserID(atlasServerTable.getObjectId());
								((ParseUser)atlasServerTable).put("first_name", AtlasAndroidUser.getUserNameDisplay());
								((ParseUser)atlasServerTable).put("is_atlas_user", true); 
								
								((ParseUser)atlasServerTable).put("facebook_id", AtlasAndroidUser.getfbID()); 
								((ParseUser)atlasServerTable).put("access_token", AtlasAndroidUser.getAccessToken());
								((ParseUser)atlasServerTable).put("password_copy", AtlasAndroidUser.getAtlasPassword());
								((ParseUser)atlasServerTable).saveInBackground(new SaveCallback()
								{
									public void done(ParseException e) {
									    if (e == null) {
									    	boolean  signin = (AtlasAndroidUser.getParseUserID()!=null &&
									    			!AtlasAndroidUser.getParseUserID().equals(""));
									    					
											parseConnect.setSignOnServer(signin);

									    	parseCallBackDeleget.registerSuccess(true);
											updateTableMessage = SERVER_MESSAGE.SUCCESS;
									    }else {
									    	parseCallBackDeleget.registerSuccess(false);
											updateTableMessage = SERVER_MESSAGE.FAIL;
										      // save didn't succeed. Look at the ParseException
										      // to figure out what went wrong
										    }
									}
								});
						    } else {
						    	parseCallBackDeleget.registerSuccess(false);
								updateTableMessage = SERVER_MESSAGE.FAIL;
						      // Sign up didn't succeed. Look at the ParseException
						      // to figure out what went wrong
						    }
						  }
						});
				
				
		
			break;
		case SIGN_IN:
				ParseUser.logInInBackground(AtlasAndroidUser.getEmail(), AtlasAndroidUser.getAtlasPassword(), new LogInCallback() {
					  public void done(ParseUser user, ParseException e) {
					    if (user != null) {
					      // Hooray! The user is logged in.
					    	atlasServerTable = user;
					    	AtlasServerConnect.userSignInSuccessfuly = true;
							updateTableMessage = SERVER_MESSAGE.SUCCESS;
  
					    	parseCallBackDeleget.signInSuccess( true);
					    } else {
					      // Signup failed. Look at the ParseException to see what happened.
					    	AtlasServerConnect.userSignInSuccessfuly = false;
							updateTableMessage = SERVER_MESSAGE.FAIL;
					    	parseCallBackDeleget.signInSuccess(false);

					    }
					  }
					});
			break;
		case SIGN_IN_NEW:
			final String userName = operation[1];
			final String password = operation[2];
			final String name = operation[3];
			((ParseUser)atlasServerTable).setUsername(userName); 
			((ParseUser)atlasServerTable).setPassword(password);

			((ParseUser)atlasServerTable).setEmail(userName);
			((ParseUser)atlasServerTable).signUpInBackground(new SignUpCallback() {
				  public void done(ParseException e) {
					    if (e == null) {
					      // Hooray! Let them use the app now.
					    	AtlasAndroidUser.setParseUserID(atlasServerTable.getObjectId());
							((ParseUser)atlasServerTable).put("first_name", name);
							((ParseUser)atlasServerTable).put("password_copy", password);
							((ParseUser)atlasServerTable).put("is_atlas_user", false);

							((ParseUser)atlasServerTable).saveInBackground(new SaveCallback()
							{
								public void done(ParseException e) {
								    if (e == null) {

								    	parseCallBackDeleget.signInNewFriendUserSuccess(true,(ParseUser)atlasServerTable);

								    }else {

								    	parseCallBackDeleget.signInNewFriendUserSuccess(false,null);

								    	// save didn't succeed. Look at the ParseException
									      // to figure out what went wrong
									    }
								}
							});
					    } else {

					    	parseCallBackDeleget.signInNewFriendUserSuccess(false,null);

					    	// Sign up didn't succeed. Look at the ParseException
					      // to figure out what went wrong
					    }
					  }
					});
			
			
//			
//			ParseUser.logInInBackground(operation[0], operation[1], new LogInCallback() {
//				  public void done(ParseUser user, ParseException e) {
//				    if (user != null) {
//				      // Hooray! The user is logged in.
//				    
//				    	parseCallBackDeleget.signInNewFriendUserSuccess(true,user);
//				    } else {
//				      // Signup failed. Look at the ParseException to see what happened.
//				    	parseCallBackDeleget.signInNewFriendUserSuccess(false,null);
//
//				    }
//				  }
//				});
		break;
		case SIGN_IN_FRIEND:
			ParseUser.logInInBackground(operation[1], operation[2], new LogInCallback() {
				  public void done(ParseUser user, ParseException e) {
				    if (user != null) {
				      // Hooray! The user is logged in.
				    	atlasServerTable = user;
				    	AtlasServerConnect.userSignInSuccessfuly = true;
						updateTableMessage = SERVER_MESSAGE.SUCCESS;

				    	parseCallBackDeleget.friendSignInSuccess(true);
				    } else {
				      // Signup failed. Look at the ParseException to see what happened.
				    	AtlasServerConnect.userSignInSuccessfuly = false;
						updateTableMessage = SERVER_MESSAGE.FAIL;
				    	parseCallBackDeleget.friendSignInSuccess(false);

				    }
				  }
				});
			break;
		case SAVE:
			atlasServerTable.saveInBackground(new SaveCallback()
			{
				public void done(ParseException e) {
				    if (e == null) {
				    	parseCallBackDeleget.getSaveCallBack(true,atlasServerTable);
						updateTableMessage = SERVER_MESSAGE.SUCCESS;
				    }else {
				    	parseCallBackDeleget.getSaveCallBack(false,null);
						updateTableMessage = SERVER_MESSAGE.FAIL;
					      // save didn't succeed. Look at the ParseException
					      // to figure out what went wrong
					    }
				}
			});
			break;
			
		case UPDATE:
			atlasServerTable.saveInBackground(new SaveCallback()
			{
				public void done(ParseException e) {
				    if (e == null) {
				    	parseCallBackDeleget.getUpateCallBack(true);
						updateTableMessage = SERVER_MESSAGE.SUCCESS;
				    }else {
				    	parseCallBackDeleget.getUpateCallBack(false);
						updateTableMessage = SERVER_MESSAGE.FAIL;
					      // save didn't succeed. Look at the ParseException
					      // to figure out what went wrong
					    }
				}
			});
			break;
		case SAVE_USER:
			atlasServerTable.saveInBackground(new SaveCallback()
			{
				public void done(ParseException e) {
				    if (e == null) {
				    	if (parseCallBackDeleget!=null)
				    	parseCallBackDeleget.getSaveCallBack(true,atlasServerTable);
						updateTableMessage = SERVER_MESSAGE.SUCCESS;
				    }else {
				    	if (parseCallBackDeleget!=null)
				    	parseCallBackDeleget.getSaveCallBack(false,null);
						updateTableMessage = SERVER_MESSAGE.FAIL;
					      // save didn't succeed. Look at the ParseException
					      // to figure out what went wrong
					    }
				}
			});
			break;
		case UPDATE_OBJECT_ID:
			ParseQuery query = new ParseQuery(this.TABLE_NAME);
			query.getInBackground(operation[1], new GetCallback() {
			  public void done(ParseObject parseObjectRetreived, ParseException e) {
			    if (e == null) {
			    	if (updatedColumns!=null && updatedColumns.size()>0)
			    	{
			    		atlasServerTable = parseObjectRetreived;
			    		putUpdated(updatedColumns);
			    	}else
			    		if (parseCallBackDeleget!=null)
			    		parseCallBackDeleget.getUpateCallBack(false);
			    } else {
			      // something went wrong
			    	if (parseCallBackDeleget!=null)
		    		parseCallBackDeleget.getUpateCallBack(false);

			    }
			  }
			});
			break;
			
		case FIND_USER_EMAIL:
				parseQuery.findInBackground(new FindCallback() {
				  public void done(List<ParseObject> list, ParseException e) {
				    if (e == null) {
				        // The query was successful.
				    	updateTableMessage = SERVER_MESSAGE.SUCCESS;
				    	findResult = list;
				    	HashMap<String, String> userParseLogin = new HashMap<String, String>();
						
						ParseUser user=null;
							if (list!=null && list.size()==1)
								user = (ParseUser)list.get(0); 
							 
							if (user!=null) 
							{
								userParseLogin.put("email", user.getUsername());
								userParseLogin.put("password_copy", user.get("password_copy").toString());
								userParseLogin.put("object_id", user.getObjectId());
								parseConnect.signOnServer = true;
						}
							if (parseCallBackDeleget!=null)
				    	parseCallBackDeleget.getUserEmailOnParseCallBack(userParseLogin, true);
							if (userListener!=null)
								userListener.getUserEmailOnParseCallBack(userParseLogin, true);
				    } else {
				        // Something went wrong.
						parseConnect.signOnServer = false;
						if (parseCallBackDeleget!=null)
				    	parseCallBackDeleget.getUserEmailOnParseCallBack(null, false);
						if (userListener!=null)
							userListener.getUserEmailOnParseCallBack(null, false);

				    }
				  }
				});
			break;
		case FIND:
			if (parseQuery != null)
			parseQuery.findInBackground(new FindCallback() {
			  public void done(List<ParseObject> list, ParseException e) {
			    if (e == null) {
			        // The query was successful.
			    	updateTableMessage = SERVER_MESSAGE.SUCCESS;
			    	findResult = list;
			    	if (parseCallBackDeleget!=null)		    
			    	parseCallBackDeleget.getFindQueryCallBack(findResult, true);
			    } else {
			        // Something went wrong.
			    	if (parseCallBackDeleget!=null)
			    	parseCallBackDeleget.getFindQueryCallBack(findResult, false);

			    }
			  }
			});
			
		break;
		case FIND_COMMON:
			userQuery.findInBackground(new FindCallback() {
			  public void done(List<ParseObject> list, ParseException e) {
			    if (e == null) {
			        // The query was successful.
			    	updateTableMessage = SERVER_MESSAGE.SUCCESS;
			    	findResult = list;
			    	if (parseCallBackDeleget!=null)		    
			    	parseCallBackDeleget.getAtlasNewFriendsByEmailCallBack(findResult, true);
			    } else {
			        // Something went wrong.
			    	if (parseCallBackDeleget!=null)
			    	parseCallBackDeleget.getAtlasNewFriendsByEmailCallBack(findResult, false);

			    }
			  }
			});
			
		break;
		case FIND_ALL_USERS:
			userQuery.findInBackground(new FindCallback() {
			  public void done(List<ParseObject> list, ParseException e) {
			    if (e == null) {
			        // The query was successful.
			    	updateTableMessage = SERVER_MESSAGE.SUCCESS;
			    	findResult = list;
			    	if (parseCallBackDeleget!=null)		    
			    	parseCallBackDeleget.getAllAtlasUsersCallBack(findResult, true);
			    } else {
			        // Something went wrong.
			    	if (parseCallBackDeleget!=null)
			    	parseCallBackDeleget.getAllAtlasUsersCallBack(findResult, false);

			    }
			  }
			});
			
		break;
		case FIND_ALL_FACEBOOK_USERS_FRIENDS:
			userQuery.findInBackground(new FindCallback() {
			  public void done(List<ParseObject> list, ParseException e) {
			    if (e == null) {
			        // The query was successful.
			    	updateTableMessage = SERVER_MESSAGE.SUCCESS;
			    	findResult = list;
			    	if (parseCallBackDeleget!=null) 
			    	parseCallBackDeleget.getAllFBAtlasUsersFriendsCallBack(findResult, true);
			    } else {
			        // Something went wrong.
			    	if (parseCallBackDeleget!=null)
			    	parseCallBackDeleget.getAllFBAtlasUsersFriendsCallBack(findResult, false);

			    }
			  }
			});
			
		break;
		case FIND_FRIEND_EMAIL:
			userQuery.findInBackground(new FindCallback() {
			  public void done(List<ParseObject> list, ParseException e) {
			    if (e == null) {
			        // The query was successful.
			    	updateTableMessage = SERVER_MESSAGE.SUCCESS;
			    	findResult = list;
			    	if (parseCallBackDeleget!=null)
			    	parseCallBackDeleget.getFriendEmailOnParseCallBack(findResult, true);
			    } else {
			        // Something went wrong.
			    	if (parseCallBackDeleget!=null)
			    	parseCallBackDeleget.getFriendEmailOnParseCallBack(findResult, false);

			    }
			  }
			});
			
		break;
		case FIND_FACEBOOK_ID:
			userQuery.findInBackground(new FindCallback() {
			  public void done(List<ParseObject> list, ParseException e) {
			    if (e == null) {
			        // The query was successful.
			    	updateTableMessage = SERVER_MESSAGE.SUCCESS;
			    	findResult = list;
			    	if (parseCallBackDeleget!=null)
			    	parseCallBackDeleget.getFacebookFriendIdOnParseCallBack(findResult, true);
			    } else {
			        // Something went wrong.
			    	if (parseCallBackDeleget!=null)
			    	parseCallBackDeleget.getFacebookFriendIdOnParseCallBack(findResult, false);

			    }
			  }
			});
			
		break;
		case GET_DATA:
				parseFile.getDataInBackground(new GetDataCallback() {
				  public void done(byte[] data, ParseException e) {
				    if (e == null) {
				      // data has the bytes for the resume
				    	image = data;
						updateTableMessage = SERVER_MESSAGE.SUCCESS;
						if (parseCallBackDeleget!=null)
						parseCallBackDeleget.getDataCallBack(image, true);
				    } else {
				    	if (parseCallBackDeleget!=null)
				      // something went wrong
						parseCallBackDeleget.getDataCallBack(null, false);

				    }
				  }
				});
			break;
		case GET:
				parseQuery.getInBackground(parseQueryObjectId, new GetCallback() {
					  public void done(ParseObject object, ParseException e) {
					    if (e == null) {
					    	retreivedObjectId = object;
							updateTableMessage = SERVER_MESSAGE.SUCCESS;
							if (parseCallBackDeleget!=null)
					    	parseCallBackDeleget.getObjectIdCallBack(retreivedObjectId, true);
					    	
					    } else {
					      // something went wrong
							updateTableMessage = SERVER_MESSAGE.FAIL;
							if (parseCallBackDeleget!=null)
					    	parseCallBackDeleget.getObjectIdCallBack(null, false);

					    }
					  }
					});
			break;
		case SAVE_FILE:
			final String columnFileName = operation[1];
			parseFile.saveInBackground(new SaveCallback()
			{
				public void done(ParseException e) {
				    if (e == null) {
				    	
						atlasServerTable.put(columnFileName, parseFile);
						atlasServerTable.saveInBackground(new SaveCallback()
						{
							public void done(ParseException e) {
							    if (e == null) {
							    	if (parseCallBackDeleget!=null)
							    	parseCallBackDeleget.saveFileCallBack(true,atlasServerTable);
							    	if (userListener!=null)
							    		userListener.saveCallBack(true);	
									updateTableMessage = SERVER_MESSAGE.SUCCESS;
							    }else {
							    	if (parseCallBackDeleget!=null)
							    	parseCallBackDeleget.saveFileCallBack(false,null);
							    	if (userListener!=null)
							    		userListener.saveCallBack(false);	
									updateTableMessage = SERVER_MESSAGE.FAIL;
								      // save didn't succeed. Look at the ParseException
								      // to figure out what went wrong
								    }
							}
						});
				    }else {
				    	if (parseCallBackDeleget!=null)
				    	parseCallBackDeleget.getSaveCallBack(false,null);
				    	if (userListener!=null)
				    	userListener.saveCallBack(false);
						updateTableMessage = SERVER_MESSAGE.FAIL;
				    	parseCallBackDeleget.saveFileCallBack(false,null);

					      // save didn't succeed. Look at the ParseException
					      // to figure out what went wrong
					    }
				}
			});
				
				
			break;
	
		case OTHER:
			break;
		}
	}
	
	
	
//	class LongOperation extends AsyncTask<String, Integer, SERVER_MESSAGE> {
//
//		
//		protected void onPostExecute(SERVER_MESSAGE result) {
//           
//            return;
//
//        }
//		@Override
//		protected SERVER_MESSAGE doInBackground(String... params) {
//				updateTableMessage = SERVER_MESSAGE.FAIL;
//				String[] operation = params;
//				OPERATION_METHOD method = 
//						(operation[0].equals("register"))? OPERATION_METHOD.REGISTER:
//						(operation[0].equals("sign_in"))? OPERATION_METHOD.SIGN_IN:
//						(operation[0].equals("sign_in_friend"))? OPERATION_METHOD.SIGN_IN_FRIEND:
//						(operation[0].equals("save"))? OPERATION_METHOD.SAVE:
//						(operation[0].equals("save_user"))? OPERATION_METHOD.SAVE_USER:
//						(operation[0].equals("find"))? OPERATION_METHOD.FIND:
//						(operation[0].equals("get"))? OPERATION_METHOD.GET:
//						(operation[0].equals("get_data"))? OPERATION_METHOD.GET_DATA:
//						(operation[0].equals("save_file"))? OPERATION_METHOD.SAVE_FILE:
//						OPERATION_METHOD.OTHER;
//				
//				
//				switch(method)
//				{
//				case REGISTER:
//					try {
//						((ParseUser)atlasServerTable).signUp();
//						AtlasAndroidUser.setParseUserID(atlasServerTable.getObjectId());
//						((ParseUser)atlasServerTable).put("displayname", AtlasAndroidUser.getUserNameDisplay());
//						((ParseUser)atlasServerTable).put("fbID", AtlasAndroidUser.getfbID()); 
//						((ParseUser)atlasServerTable).put("access_token", AtlasAndroidUser.getAccessToken());
//						((ParseUser)atlasServerTable).put("passwordCopy", AtlasAndroidUser.getAtlasPassword());
//						((ParseUser)atlasServerTable).save();
//						updateTableMessage = SERVER_MESSAGE.SUCCESS;
//					} catch (ParseException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//					break;
//				case SIGN_IN:
//					try {
//						atlasServerTable = ParseUser.logIn(AtlasAndroidUser.getEmail(), 
//								AtlasAndroidUser.getAtlasPassword());
//						AtlasServerConnect.userSignInSuccessfuly = atlasServerTable!=null;
//						updateTableMessage = SERVER_MESSAGE.SUCCESS;
//					} catch (ParseException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//					break;
//				case SIGN_IN_FRIEND:
//					try {
//						
//						atlasServerTable = ParseUser.logIn(operation[1], 
//								operation[2]);
//					//	AtlasServerConnect.userSignInSuccessfuly = atlasServerTable!=null;
//					//	updateTableMessage = SERVER_MESSAGE.SUCCESS;
//					} catch (ParseException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//					break;
//				case SAVE:
//					try {
//						atlasServerTable.save();
//						updateTableMessage = SERVER_MESSAGE.SUCCESS;
//					} catch (ParseException e) {
//						e.printStackTrace();
//						
//					}
//					break;
//				case SAVE_USER:
//					try {
//						atlasServerTable.save();
//						updateTableMessage = SERVER_MESSAGE.SUCCESS;
//					} catch (ParseException e) {
//						e.printStackTrace();
//					}
//					break;
//				case FIND:
//					try {
//						findResult = userQuery.find();
//						updateTableMessage = SERVER_MESSAGE.SUCCESS;
//					} catch (ParseException e) {
//						e.printStackTrace();
//					}
//					
//					break;
//				case GET_DATA:
//					try {
//						image = parseFile.getData();
//						updateTableMessage = SERVER_MESSAGE.SUCCESS;
//					} catch (ParseException e) {
//						e.printStackTrace();
//					}
//					break;
//				case GET:
//					try {
//						retreivedObjectId = parseQuery.get(parseQueryObjectId);
//						updateTableMessage = SERVER_MESSAGE.SUCCESS;
//
//					} catch (ParseException e1) {
//						e1.printStackTrace();
//					}
//					break;
//				case SAVE_FILE:
//					try {
//						parseFile.save();
//						String columnFileName = operation[1];
//						atlasServerTable.put(columnFileName, parseFile);
//						atlasServerTable.save();
//						updateTableMessage = SERVER_MESSAGE.SUCCESS;
//					} catch (ParseException e) {
//						e.printStackTrace();
//					}
//					break;
//			
//				case OTHER:
//					break;
//				}
//				
//				
//				
//				connecting = false;
//				return updateTableMessage;
//					
//		}
//		
//	}
//	
//	
	
	@Override
	public  synchronized void saveFriendPictureFileInBackground(final ParseObject parseObject,
			final String fileName, final AtlasApplication application) {
		
		ParseUser.logInInBackground(parseObject.getString("email"), parseObject.getString("password_copy"), new LogInCallback() {
			  public void done(ParseUser user, ParseException e) {
			    if (user != null) {
			      ParseFile applicantResume = (ParseFile)parseObject.get("picture");
					applicantResume.getDataInBackground(new GetDataCallback() {
						  public void done(byte[] data, ParseException e) {
						    if (e == null) {
						      // data has the bytes for the resume
						    	Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
								
								//application.storeFriendPicture(fileName, bitmap);
								if (bitmap!=null && fileName!="")
									try {
									// Copy image from one file path to atlas path on mobile
									// where VALUE represent the source file path
									    if (application.IMAGE_DIR.canWrite()) {
									    	parseCallBackDeleget.getSuccessCallBack(true);
									    	FileOutputStream fOut=new FileOutputStream(application.IMAGE_DIR+"/friendPics/"+fileName+".png"); 
									    	// Here path is either sdcard or internal storage
									    	bitmap.compress(Bitmap.CompressFormat.PNG,100,fOut);
									    	fOut.flush();
									    	fOut.close();
									    	bitmap.recycle(); // If no longer used..
									        }
									    
									} catch (Exception e1) {
								    	parseCallBackDeleget.getSuccessCallBack(false);

									}
						    } else {
						      // something went wrong
						    	parseCallBackDeleget.getSuccessCallBack(false);

						    }
						  }
						});
			    } else {
			      // Signup failed. Look at the ParseException to see what happened.
			    	parseCallBackDeleget.getSuccessCallBack(false);

			    }
			  }
			});	

		
		
	}
	@Override
	public void updateRowInTableInBackground(String objectId,
			final HashMap<String, Object> updates) {

		
		if (objectId != null && !objectId.equals("")
				&& updates!=null && updates.size()>0)
		{
			ParseQuery query = new ParseQuery(this.TABLE_NAME);
			query.getInBackground(objectId, new GetCallback() {
				  public void done(ParseObject inviteOrigin, ParseException e) {
					    if (e == null) {
					    	if (inviteOrigin!=null )
					    	{
					    		Iterator iter = updates.keySet().iterator();

					    		while(iter.hasNext()) {

					    			String key = (String)iter.next();
					    			Object val = (Object)updates.get(key);
					    			
					    			inviteOrigin.put(key, val);
					    		}
//					    		inviteOrigin.put("isRead", true);
//					    		inviteOrigin.put("respond", true);
					    		inviteOrigin.saveInBackground();
					    	}
					    	
					      	
					    	
					    	
					    } else {
					      // something went wrong
					    }
					  }
					});
		}
			
		
		
	}

	@Override
	public SERVER_MESSAGE put(HashMap<String, Object> fieldsValues,
			UserDelagateInterface listener) {
		// TODO Auto-generated method stub
		return null;
	}
	
	



	



	


	


}
