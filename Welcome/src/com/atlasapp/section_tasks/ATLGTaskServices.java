//  ==================================================================================================================
//  ATLGTaskServices.java
//  ATLAS
//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2013-01-18 TAN:     Create class to handle Insert, update, delete and sync Atlas Task with gtasks
//  ==================================================================================================================

package com.atlasapp.section_tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.atlasapp.common.DateTimeUtilities;
import com.atlasapp.model.ATLTaskModel;
import com.google.api.client.extensions.android2.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessProtectedResource;
import com.google.api.client.googleapis.extensions.android2.auth.GoogleAccountManager;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.json.JsonHttpRequest;
import com.google.api.client.http.json.JsonHttpRequestInitializer;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.tasks.TasksRequest;
import com.google.api.services.tasks.model.Task;

interface ATLGTaskSyncServiceDelegate {
	void didFinishSyncService(boolean isSuccess);

	void requestShowDialog();
}

public class ATLGTaskServices {
	/** Logging level for HTTP requests/responses. */
	static public interface GTaskSyncType {
		static final int SYNC_ALL = 0;
		static final int INSERT = 1;
		static final int UPDATE = 2;
		static final int DELETE = 3;
		static final int IS_COMPLETE = 4;
	}

	private static final Level LOGGING_LEVEL = Level.OFF;

	private static final String TAG = "TasksSample";

	// This must be the exact string, and is a special for alias OAuth 2 scope
	// "https://www.googleapis.com/auth/tasks"
	private static final String AUTH_TOKEN_TYPE = "Manage your tasks";

	private static final String PREF = "MyPrefs";
	public static final int REQUEST_AUTHENTICATE = 0;

	private final HttpTransport transport = AndroidHttp
			.newCompatibleTransport();

	com.google.api.services.tasks.Tasks service;
	GoogleAccessProtectedResource accessProtectedResource = new GoogleAccessProtectedResource(
			null);

	// TODO(yanivi): save auth token in preferences?
	GoogleAccountManager accountManager;

	ATLGTaskSyncServiceDelegate delegate;

	Context mContext;

	public ATLGTaskServices(Context ctx) {
		mContext = ctx;
	}

	// public boolean isUpdate = false;
	// public boolean isInsert = false;
	// public boolean isDelete = false;

	public final class GetGTaskListAsyncTask extends
			AsyncTask<Void, Void, Void> {

		public GetGTaskListAsyncTask() {
			// pR.setVisibility(View.VISIBLE);

		}

		@Override
		protected Void doInBackground(final Void... params) {
			/* Load timezone. this is very slow - may take up to 3 seconds. */
			service = com.google.api.services.tasks.Tasks
					.builder(transport, new JacksonFactory())
					.setApplicationName("Google-TasksSample/1.0")
					.setHttpRequestInitializer(accessProtectedResource)
					.setJsonHttpRequestInitializer(
							new JsonHttpRequestInitializer() {

								public void initialize(JsonHttpRequest request)
										throws IOException {
									TasksRequest tasksRequest = (TasksRequest) request;
									tasksRequest.setKey(ClientCredentials.KEY);
								}
							}).build();
			accountManager = new GoogleAccountManager(mContext);
			Logger.getLogger("com.google.api.client").setLevel(LOGGING_LEVEL);
			gotAccount(false);
			return null;
		}
	}

	void gotAccount(boolean tokenExpired) {
		new GetAccountAsyncTask(tokenExpired).execute();
	}

	final class GetAccountAsyncTask extends AsyncTask<Void, Void, Void> {

		boolean tokenExpired = false;
		boolean isGotAccount = false;

		public GetAccountAsyncTask(boolean tokenExpired) {
			// pR.setVisibility(View.VISIBLE);
			this.tokenExpired = tokenExpired;

		}

		@Override
		protected Void doInBackground(final Void... params) {
			/* Load timezone. this is very slow - may take up to 3 seconds. */
			SharedPreferences settings = mContext.getSharedPreferences(PREF, 0);
			String accountName = settings.getString("accountName", null);
			Account account = accountManager.getAccountByName(accountName);
			if (account != null) {
				if (tokenExpired) {
					accountManager.invalidateAuthToken(accessProtectedResource
							.getAccessToken());
					accessProtectedResource.setAccessToken(null);
				}
				isGotAccount = true;
				gotAccount(account);
				return null;
			}

			return null;
		}

		@Override
		protected void onPostExecute(final Void unused) {
			if (!isGotAccount)
				delegate.requestShowDialog();
			// ((Activity) mContext).showDialog(DIALOG_ACCOUNTS);

		}
	}

	void gotAccount(final Account account) {
		new GetAccount2AsyncTask(account).execute();
	}

	final class GetAccount2AsyncTask extends AsyncTask<Void, Void, Void> {

		Account account;

		public GetAccount2AsyncTask(final Account account) {
			// pR.setVisibility(View.VISIBLE);
			this.account = account;
		}

		@SuppressWarnings("deprecation")
		@Override
		protected Void doInBackground(final Void... params) {
			/* Load timezone. this is very slow - may take up to 3 seconds. */
			SharedPreferences settings = mContext.getSharedPreferences(PREF, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("accountName", account.name);
			editor.commit();
			accountManager.manager.getAuthToken(account, AUTH_TOKEN_TYPE, true,
					new AccountManagerCallback<Bundle>() {

						public void run(AccountManagerFuture<Bundle> future) {
							try {
								Bundle bundle = future.getResult();
								if (bundle
										.containsKey(AccountManager.KEY_INTENT)) {
									Intent intent = bundle
											.getParcelable(AccountManager.KEY_INTENT);
									intent.setFlags(intent.getFlags()
											& ~Intent.FLAG_ACTIVITY_NEW_TASK);
									((Activity) mContext)
											.startActivityForResult(intent,
													REQUEST_AUTHENTICATE);
								} else if (bundle
										.containsKey(AccountManager.KEY_AUTHTOKEN)) {
									accessProtectedResource.setAccessToken(bundle
											.getString(AccountManager.KEY_AUTHTOKEN));
									// if (isUpdate) {
									// updateGoogleTask();
									// } else if (isInsert) {
									// insertGoogleTask();
									// } else if (isDelete) {
									// deleteGoogleTask();
									// } else {
									onAuthToken();
									// }
								}
							} catch (Exception e) {
								handleException(e);
							}
						}

					}, null);
			return null;
		}
	}

	void handleException(Exception e) {
		e.printStackTrace();
		if (e instanceof HttpResponseException) {
			HttpResponse response = ((HttpResponseException) e).getResponse();
			int statusCode = response.getStatusCode();
			try {
				response.ignore();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			// TODO(yanivi): should only try this once to avoid infinite loop
			if (statusCode == 401) {
				gotAccount(true);
				return;
			}
		}
		Log.e(TAG, e.getMessage(), e);
	}

	void onAuthToken() {
		new GTaskSyncUpAsyncTask().execute();
	}

	public boolean isSyncing = false;

	public void sync() {
		if (!isSyncing) {
			Log.v("ATLGTaskServices", "Start sync task.............1");
			isSyncing = true;
			new GetGTaskListAsyncTask().execute();
		}

	}

	final class GTaskSyncUpAsyncTask extends AsyncTask<Void, Void, Void> {
		List<String> taskTitles = new ArrayList<String>();
		List<Task> taskList = new ArrayList<Task>();
		ArrayList<ATLTaskModel> taskArr = new ArrayList<ATLTaskModel>();
		boolean result = false;

		public GTaskSyncUpAsyncTask() {
			// pR.setVisibility(View.VISIBLE);

		}

		@Override
		protected Void doInBackground(final Void... params) {
			/* Load timezone. this is very slow - may take up to 3 seconds. */
			try {

				taskList = service.tasks().list("@default").execute()
						.getItems();
				// Insert Task into DB
				TaskDatabaseAdapter dbHepler = new TaskDatabaseAdapter();
				taskArr = dbHepler.loadAllTasksInDatabase();
				if (taskList != null) {

					if (taskArr.size() == 0) { // No tasks, it will insert all
						Log.v("ATLGTaskServices", "No tasks, insert all");
						for (Task task : taskList) {
							ATLTaskModel atlTask = new ATLTaskModel(task);
							dbHepler.insertATLTaskModel(atlTask);
						}
					} else {
						// Step 1: Delete all is deleted in Atlas database
						taskArr = dbHepler.loadAllTasksInDatabase();
						boolean isHasDeleted = false;
						for (ATLTaskModel atlTask : taskArr) {
							if (atlTask.taskIsDeleted) {
								isHasDeleted = true;
								try {
									// Just can delete 1 task per time
									if(atlTask.taskUuid != null && atlTask.taskUuid.length() > 0){
										deleteGoogleTask(atlTask.taskUuid);
									}
									else{
										Log.v("ATLGTaskServices", "isHasDeleted local");
										TaskDatabaseAdapter helper = new TaskDatabaseAdapter();
										helper.deleteATLTaskModel(atlTask);
									}
								} catch (Exception ex) {
									ex.printStackTrace();
								}
								break;
							} 
						}
						
						// Step 2: Sync down from server to local
						// Reload list task after delete 
						if(isHasDeleted){
							Log.v("ATLGTaskServices", "isHasDeleted");
							taskArr = dbHepler.loadAllTasksInDatabase();
							taskList = service.tasks().list("@default").execute()
									.getItems();
						}
						
						boolean isHasUpdateTask = false;
						ArrayList<Task> newTaskListInServer = new ArrayList<Task>();
						if(taskList != null){
							for (Task gTask : taskList) {
								boolean isHandled = true;
								for (ATLTaskModel atlTask : taskArr) {
									isHandled = false;
									String updateDateString = gTask.getUpdated()
											.toStringRfc3339();
									Log.v("ATLTaskModel", updateDateString);
	
									Date gTaskUpdateDate = DateTimeUtilities
											.serverTimeStringToLocalDateTime(
													updateDateString,
													"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
									if (gTask.getId().equals(
											atlTask.taskUuid)) {// Already existed
										isHandled = true;
										if (gTaskUpdateDate
												.after(atlTask.taskModifieDate)) {
											// sync down
											atlTask.updateInfoGTask(gTask);
											atlTask.save();
											isHasUpdateTask = true;
											Log.v("ATLGTaskServices", "isHasUpdateTask down");
											break;
										} else if (gTaskUpdateDate
												.before(atlTask.taskModifieDate)) {
											ATLTaskCellData mCellData = new ATLTaskCellData(
													atlTask);
											try {
												updateGoogleTask(mCellData);
												Log.v("ATLGTaskServices", "isHasUpdateTask up");
											} catch (Exception ex) {
												ex.printStackTrace();
											}
											isHasUpdateTask = true;
											break;
										}
										break;
									} 
								}
								
								if (!isHandled) {
									newTaskListInServer.add(gTask);
	//								ATLTaskModel aNewAtlTask = new ATLTaskModel(
	//										gTask);
	//								aNewAtlTask.save();
								}
							}
						}
						boolean isHasNewTasks = false;
						for (Task gTask : newTaskListInServer){
							isHasNewTasks = true;
							ATLTaskModel aNewAtlTask = new ATLTaskModel(
									gTask);
							aNewAtlTask.save();
						}
						
						// Step 3: Sync up local to server
						// Reload list task after update
						if(isHasNewTasks){
							Log.v("ATLGTaskServices", "isHasNewTasks");
							taskArr = dbHepler.loadAllTasksInDatabase();
							taskList = service.tasks().list("@default").execute()
									.getItems();
						}
						
						// Insert new task from local to server
						boolean isHasInsertNew = false;
						for (ATLTaskModel atlTask : taskArr) {

							if (atlTask.taskUuid.equals("") || atlTask.taskUuid == null 
									||atlTask.taskUuid.toLowerCase().equals("NULL")
									|| atlTask.taskUuid.length() < 5) 
							{
								try {
									isHasInsertNew = true;
									insertGoogleTask(new ATLTaskCellData(
											atlTask));
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							} 
						}
						
						if(isHasInsertNew){
							Log.v("ATLGTaskServices", "isHasInsertNew");
							taskArr = dbHepler.loadAllTasksInDatabase();
							taskList = service.tasks().list("@default").execute()
									.getItems();
//							Log.v("ATLGTaskServices", "taskArr.count = " + taskArr.size());
//							Log.v("ATLGTaskServices", "taskList.count = " + taskList.size());
						}
						
						// Delete event if server had deleted them
						for(ATLTaskModel atlTask: taskArr){
							boolean isHandledATLTask = true;
							if(taskList != null){
								for (Task gTask : taskList) {
									isHandledATLTask = false;
									if (gTask.getId().equals(atlTask.taskUuid)) {
										isHandledATLTask = true;
										break;
									}
								}
							}
							if (!isHandledATLTask) {
								TaskDatabaseAdapter helper = new TaskDatabaseAdapter();
								helper.deleteATLTaskModelByUuid(atlTask.taskUuid);
								Log.v("ATLGTaskServices", "isDeleteLocalTask "+ atlTask.taskTitle);
							}
							
						}

					}

				}
				result = true;
			} catch (IOException e) {
				handleException(e);
				result = false;
			}

			return null;
		}

		@Override
		protected void onPostExecute(final Void unused) {
			isSyncing = false;
			Log.v("ATLGTaskServices", "End sync task.............1");
			delegate.didFinishSyncService(result);
		}
	}

	public boolean insertGoogleTask(ATLTaskCellData mCellData) {
		// TODO Auto-generated method stub
		Task task = new Task();
		task.setTitle(mCellData.taskCellTitle);
		if (mCellData.taskCellDueDate != null) {
			// 2012-12-17 TAN: Still got issues with Google Task
			// They do not support set due time, just can set due day
			// Need save the due time by Atlas server
			// 2012-12-18 TAN: Store due time in Task Notes
			Date cellDate = mCellData.taskCellDueDate;
			Calendar calLocal = Calendar.getInstance();
			calLocal.setTime(cellDate);
			Calendar cal = new GregorianCalendar(
					TimeZone.getTimeZone("GMT+00:00"));
			cal.set(calLocal.get(Calendar.YEAR), calLocal.get(Calendar.MONTH),
					calLocal.get(Calendar.DAY_OF_MONTH),
					calLocal.get(Calendar.HOUR_OF_DAY),
					calLocal.get(Calendar.MINUTE), 0);
			DateTime dueDate = new DateTime(cal.getTime(),
					TimeZone.getTimeZone("GMT+00:00"));
			DateTime dueTime = new DateTime(calLocal.getTime(),
					TimeZone.getTimeZone("GMT+00:00"));
			task.setDue(dueDate);
			task.setNotes(mCellData.taskCellDescription + "\nDueTime:"
					+ dueTime.toStringRfc3339() + "\nPriority:"
					+ mCellData.taskCellPriorityInt + "\nReminder:"
					+ mCellData.taskCellRemider + "\nCalendarName:"
					+ mCellData.taskCellCalendarName + "\nDelegated:"
					+ mCellData.taskCellReceiverName);
			Log.v("EditTask", "Inseart setDue: " + dueDate.toString());
			try {

				task = service.tasks().insert("@default", task).execute();
				if (task != null) {
					
					mCellData.taskCellUuid = task.getId();
//					Log.v("ATLGTaskServices", "Inseart success ID: " + mCellData.taskCellUuid);
					String updateDateString = task.getUpdated()
							.toStringRfc3339();
					Date gTaskUpdateDate = DateTimeUtilities
							.serverTimeStringToLocalDateTime(updateDateString,
									"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
					mCellData.taskCellModifiedDate = gTaskUpdateDate;
					mCellData.save();
				}
			} catch (IOException e) {
				handleException(e);
				return false;
			}
		}
		return true;
	}

	public boolean updateGoogleTask(ATLTaskCellData mCellData) {
		// TODO Auto-generated method stub
		// new
		// UpdateTaskAsyncTask(EditTaskModel.getInstance().getTaskCellData())
		// .execute();
		// ATLTaskCellData mCellData =
		// EditTaskModel.getInstance().getTaskCellData().copy();
		// pR.setVisibility(View.VISIBLE);
		Date cellDate = mCellData.taskCellDueDate;
		Calendar calLocal = Calendar.getInstance();
		calLocal.setTime(cellDate);

		String taskID = mCellData.taskCellUuid;
		// First retrieve the task to update.
		Task task = new Task();
		try {
			task = service.tasks().get("@default", taskID).execute();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}

		task.setTitle(mCellData.taskCellTitle);

		if (cellDate != null) {
			// 2012-12-17 TAN: Still got issues with Google Task
			// They do not support set due time, just can set due day
			// Need save the due time by Atlas server

			Calendar cal = new GregorianCalendar(
					TimeZone.getTimeZone("GMT+00:00"));
			cal.set(calLocal.get(Calendar.YEAR), calLocal.get(Calendar.MONTH),
					calLocal.get(Calendar.DAY_OF_MONTH),
					calLocal.get(Calendar.HOUR_OF_DAY),
					calLocal.get(Calendar.MINUTE), 0);
			DateTime dueTime = new DateTime(calLocal.getTime(),
					TimeZone.getTimeZone("GMT+00:00"));
			DateTime dueDate = new DateTime(cal.getTime(),
					TimeZone.getTimeZone("GMT+00:00"));
			Log.v("EditTask", "setDue save:" + dueDate.toString());
			task.setNotes(mCellData.taskCellDescription + "\nDueTime:"
					+ dueTime.toStringRfc3339() + "\nPriority:"
					+ mCellData.taskCellPriorityInt + "\nReminder:"
					+ mCellData.taskCellRemider + "\nCalendarName:"
					+ mCellData.taskCellCalendarName + "\nDelegated:"
					+ mCellData.taskCellReceiverName);
			task.setDue(dueDate);
		}

		DateTime oldComplete = task.getCompleted();
		if (mCellData.taskCellIsCompleted) {
			if (oldComplete == null) { // active completed
				task.setStatus("completed");
			}
		} else {
			if (oldComplete != null) {
				DateTime completeDate = null;
				task.setCompleted(completeDate);
				task.setStatus("needsAction");
			}
		}

		try {
			task = service.tasks().update("@default", task.getId(), task)
					.execute();
			if (task != null) {
				mCellData.taskCellUuid = task.getId();
				String updateDateString = task.getUpdated().toStringRfc3339();
				Date gTaskUpdateDate = DateTimeUtilities
						.serverTimeStringToLocalDateTime(updateDateString,
								"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
				mCellData.taskCellModifiedDate = gTaskUpdateDate;
				if (mCellData.taskCellIsCompleted) {
					String completeDateString = task.getCompleted()
							.toStringRfc3339();
					mCellData.taskCellCompletedDate = DateTimeUtilities
							.serverTimeStringToLocalDateTime(
									completeDateString,
									"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
				}

				mCellData.save();
			}
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteGoogleTask(String taskUuid) {
		// TODO Auto-generated method stub
		try {
			service.tasks().delete("@default", taskUuid).execute();
			TaskDatabaseAdapter helper = new TaskDatabaseAdapter();
			helper.deleteATLTaskModelByUuid(taskUuid);
			return true;
		} catch (IOException e) {

			handleException(e);
			return false;
		}

	}

	final class InsertTaskAsyncTask extends AsyncTask<Void, Void, Void> {
		Task result = null;
		Task task = null;
		DateTime dueDate = new DateTime(new Date());
		private ATLTaskCellData mCellData;

		public InsertTaskAsyncTask() {
			// pR.setVisibility(View.VISIBLE);
			task = new Task();
			mCellData = EditTaskModel.getInstance().getTaskCellData().copy();
			task.setTitle(mCellData.taskCellTitle);
			if (mCellData.taskCellDueDate != null) {
				// 2012-12-17 TAN: Still got issues with Google Task
				// They do not support set due time, just can set due day
				// Need save the due time by Atlas server
				// 2012-12-18 TAN: Store due time in Task Notes
				Date cellDate = mCellData.taskCellDueDate;
				Calendar calLocal = Calendar.getInstance();
				calLocal.setTime(cellDate);
				Calendar cal = new GregorianCalendar(
						TimeZone.getTimeZone("GMT+00:00"));
				cal.set(calLocal.get(Calendar.YEAR),
						calLocal.get(Calendar.MONTH),
						calLocal.get(Calendar.DAY_OF_MONTH),
						calLocal.get(Calendar.HOUR_OF_DAY),
						calLocal.get(Calendar.MINUTE), 0);
				dueDate = new DateTime(cal.getTime(),
						TimeZone.getTimeZone("GMT+00:00"));
				DateTime dueTime = new DateTime(calLocal.getTime(),
						TimeZone.getTimeZone("GMT+00:00"));
				task.setDue(dueDate);
				task.setNotes(mCellData.taskCellDescription + "\nDueTime:"
						+ dueTime.toStringRfc3339() + "\nPriority:"
						+ mCellData.taskCellPriorityInt + "\nReminder:"
						+ mCellData.taskCellRemider + "\nCalendarName:"
						+ mCellData.taskCellCalendarName + "\nDelegated:"
						+ mCellData.taskCellReceiverName);
				Log.v("EditTask", "Inseart setDue: " + dueDate.toString());
				try {

					result = service.tasks().insert("@default", task).execute();

				} catch (IOException e) {
					handleException(e);
				}

			}

		}

		@Override
		protected Void doInBackground(final Void... params) {
			/* Load timezone. this is very slow - may take up to 3 seconds. */
			try {

				result = service.tasks().insert("@default", task).execute();

			} catch (IOException e) {
				handleException(e);
			}

			return null;
		}

		@Override
		protected void onPostExecute(final Void unused) {
			// isInsert = false;
			EditTaskModel.getInstance().destroy();
			if (result != null) {
				mCellData.taskCellUuid = result.getId();
				String updateDateString = result.getUpdated().toStringRfc3339();
				Date gTaskUpdateDate = DateTimeUtilities
						.serverTimeStringToLocalDateTime(updateDateString,
								"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
				mCellData.taskCellModifiedDate = gTaskUpdateDate;
				mCellData.save();
			}
		}
	}

	final class DeleteTaskAsyncTask extends AsyncTask<Void, Void, Void> {
		boolean result = true;
		Task task = null;
		String taskID;

		public DeleteTaskAsyncTask(String taskId) {
			// pR.setVisibility(View.VISIBLE);
			taskID = taskId;
		}

		@Override
		protected Void doInBackground(final Void... params) {
			/* Load timezone. this is very slow - may take up to 3 seconds. */
			try {
				service.tasks().delete("@default", taskID).execute();
				result = true;
			} catch (IOException e) {
				result = false;
				handleException(e);

			}

			return null;
		}

		@Override
		protected void onPostExecute(final Void unused) {
			// isDelete = false;
			if (result) {
				TaskDatabaseAdapter helper = new TaskDatabaseAdapter();
				helper.deleteATLTaskModelByUuid(taskID);
			}
			EditTaskModel.getInstance().destroy();// Clear data
		}
	}

	final class UpdateTaskAsyncTask extends AsyncTask<Void, Void, Void> {
		Task result = null;
		Task task = null;
		ATLTaskCellData mCellData;
		boolean isSuccess = false;

		public UpdateTaskAsyncTask(ATLTaskCellData cellData) {
			mCellData = cellData;
		}

		@Override
		protected Void doInBackground(final Void... params) {
			/* Load timezone. this is very slow - may take up to 3 seconds. */
			try {
				// pR.setVisibility(View.VISIBLE);
				Date cellDate = mCellData.taskCellDueDate;
				Calendar calLocal = Calendar.getInstance();
				calLocal.setTime(cellDate);

				String taskID = mCellData.taskCellUuid;
				// First retrieve the task to update.
				task = service.tasks().get("@default", taskID).execute();
				task.setTitle(mCellData.taskCellTitle);

				if (cellDate != null) {
					// 2012-12-17 TAN: Still got issues with Google Task
					// They do not support set due time, just can set due day
					// Need save the due time by Atlas server

					Calendar cal = new GregorianCalendar(
							TimeZone.getTimeZone("GMT+00:00"));
					cal.set(calLocal.get(Calendar.YEAR),
							calLocal.get(Calendar.MONTH),
							calLocal.get(Calendar.DAY_OF_MONTH),
							calLocal.get(Calendar.HOUR_OF_DAY),
							calLocal.get(Calendar.MINUTE), 0);
					DateTime dueTime = new DateTime(calLocal.getTime(),
							TimeZone.getTimeZone("GMT+00:00"));
					DateTime dueDate = new DateTime(cal.getTime(),
							TimeZone.getTimeZone("GMT+00:00"));
					Log.v("EditTask", "setDue save:" + dueDate.toString());
					task.setNotes(mCellData.taskCellDescription + "\nDueTime:"
							+ dueTime.toStringRfc3339() + "\nPriority:"
							+ mCellData.taskCellPriorityInt + "\nReminder:"
							+ mCellData.taskCellRemider + "\nCalendarName:"
							+ mCellData.taskCellCalendarName + "\nDelegated:"
							+ mCellData.taskCellReceiverName);
					task.setDue(dueDate);
				}

				DateTime oldComplete = task.getCompleted();
				if (mCellData.taskCellIsCompleted) {
					if (oldComplete == null) { // active completed
						task.setStatus("completed");
					}
				} else {
					if (oldComplete != null) {
						DateTime completeDate = null;
						task.setCompleted(completeDate);
						task.setStatus("needsAction");
					}
				}

				result = service.tasks().update("@default", task.getId(), task)
						.execute();

			} catch (IOException e) {
				handleException(e);
			}

			return null;
		}

		@Override
		protected void onPostExecute(final Void unused) {
			// isUpdate = false;
			EditTaskModel.getInstance().destroy();
			if (result != null) {

			} else {

			}
		}
	}

}