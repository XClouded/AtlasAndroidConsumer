package com.atlasapp.atlas_database;

public class DatabaseConstants {
	
	
	public static enum USER_INFO { USER_NAME,  PASSWORD, EMAIL, OTHER }
	
	
	public static enum ITEM_TYPE { 
		USER("user"), TASK("task"), EVENT("event"), NOTE("note");
		private final String itemName;  
		  
		private ITEM_TYPE(String itemName) {  
			this.itemName = itemName;  
		}  
  
		public String getItemName() {  
			return itemName;  
		}  
	
	}
	public static enum SERVER_MESSAGE {
		   FAIL, LOGGED_OUT, EMAIL_OR_USER_NAME_INVALID, SUCCESS, LOGGED_IN, EMAPY_VALUE
		 }
	
	public static enum ITEM_USER_TASK_STATUS {SENT, ACCEPTED,DECLINED,COMPLITED}
	public static enum EVENT_STATUS {PENDING, THE_ONE, NOT_THE_ONE}
	public static enum ALERT_STATUS {YOURMOVE, PENDING, BOOKED}
	public static enum EVENT_TYPE {
		CALL("call"),MEETING("meeting"),LUNCH("lunch"),DINNER("dinner"),COFFEE("coffee"),DRINKS("drinks");
		
		private final String eventTypeName;  
  
		private EVENT_TYPE(String eventTypeName) {  
			this.eventTypeName = eventTypeName;  
		}  
  
		public String getEventTypeName() {  
			return eventTypeName;  
		}  
	}
	public static enum ITEM_TYPE_PRIORITY_ORDER {IDEAL, OKAY}
	public static enum SIGN_NEW_USERS_CALLER { EVENT_INVITE , TASK_ASSIGN}
	public static enum ACTION {
		DELETE("del"),SAVE("");
		
		private final String actionName;  
  
		private ACTION(String tableName) {  
			this.actionName = tableName;  
		}  
  
		public String getActionName() {  
			return actionName;  
		}  
	}
	public static enum OPERATION_METHOD { 
		FIND("find"), 
		SAVE("save"),
		GET_DATA("get_data"),
		SAVE_FILE("save_file"),
		REGISTER("register"),
		SIGN_IN("sign_in"),
		SAVE_USER("save_user"),
		OTHER("oteher"), 
		SIGN_IN_FRIEND("sign_in_friend"),
		GET("get"),
		FIND_USER_EMAIL("find_user_email"),
		FIND_FRIEND_EMAIL("find_friend_email"),
		FIND_FACEBOOK_ID("find_facebook_id"),
		FIND_ALL_FACEBOOK_USERS_FRIENDS("find_all_facebook_users_friends"), FIND_ALL_USERS("find_all_users"), FIND_COMMON("find_common"), UPDATE("update"), SAVE_ITEM_USER("save_item_user"), UPDATE_OBJECT_ID("update_object_id"), SIGN_IN_NEW("sign_in_new");
		
		private final String methodName;  
		  
		private OPERATION_METHOD(String methodName) {  
			this.methodName = methodName;  
		}  
  
		public String getMethodName() {  
			return methodName;  
		}  
	
	}

	public enum TABLES_NAME {
		EVENT("event"), TASK("task"), ITEM_USER("item_user"),
		EMAIL_ADDRESS("email_address") ;
		
		private final String tableName;  
  
		private TABLES_NAME(String tableName) {  
			this.tableName = tableName;  
		}  
  
		public String getTableName() {  
			return tableName;  
		}  
	};
	
	
	
	
	
	
	
	public enum TABLE_ALERTS {
		EVENT("EventInviteNew"), TASK("TaskAssignNew") ;
		
		private final String tableName;  
  
		private TABLE_ALERTS(String tableName) {  
			this.tableName = tableName;  
		}  
  
		public String getTableName() {  
			return tableName;  
		}  
	}; 



}
