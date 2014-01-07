//  ==================================================================================================================
//  Tasks.java
//  ATLAS

//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-11-20 TAN:     Fixing Insert, update & Delete gTask. Know issues:can not set dueTime, just set dueDate
//  2012-11-13 TAN:     Implement Google Task Sync Data
//  2012-11-12 TAN:     Add method onActivityResult & Singleton Unit test
//  2012-11-08 TAN:     Merge ACA_SHARONA to ACA_TAN
//  2012-10-21 TAN:     Init class
//  2012-10-28 TAN: 	Add listTask and binding data
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
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout.LayoutParams;

import com.atlasapp.common.ATLAnimationUtils;
import com.atlasapp.common.ATLTabbarView;
import com.atlasapp.common.ATLTabbarViewDelegete;
import com.atlasapp.common.DB;
import com.atlasapp.common.SlideOutActivity;
import com.atlasapp.common.SlideOutHelper;
import com.atlasapp.model.ATLCalendarModel;
import com.atlasapp.model.ATLTaskModel;
import com.atlasapp.section_actions.Actions;
import com.atlasapp.section_alerts.Alerts;
import com.atlasapp.section_appentry.R;
import com.atlasapp.section_calendar.ATLCalendarManager;
import com.atlasapp.section_calendar.CalendarDayView;
import com.atlasapp.section_contacts.ATLContactListActivity;
import com.atlasapp.section_contacts.Contacts;
import com.atlasapp.section_main.ATLMultiSectionListView;
import com.atlasapp.section_notes.ATLNoteListActivity;
import com.atlasapp.section_settings.Settings;
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

public class Tasks extends FragmentActivity implements View.OnClickListener,
		ATLTaskCellDelegete, ATLTaskQuickAddTaskViewDelegate,
		ATLTabbarViewDelegete {

	/** Logging level for HTTP requests/responses. */
	private static final Level LOGGING_LEVEL = Level.OFF;

	private static final String TAG = "TasksSample";

	// This must be the exact string, and is a special for alias OAuth 2 scope
	// "https://www.googleapis.com/auth/tasks"
	private static final String AUTH_TOKEN_TYPE = "Manage your tasks";

	private static final String PREF = "MyPrefs";
	private static final int DIALOG_ACCOUNTS = 0;
	private static final int MENU_ACCOUNTS = 0;
	public static final int REQUEST_AUTHENTICATE = 0;

	private final HttpTransport transport = AndroidHttp
			.newCompatibleTransport();

	com.google.api.services.tasks.Tasks service;
	GoogleAccessProtectedResource accessProtectedResource = new GoogleAccessProtectedResource(
			null);

	// TODO(yanivi): save auth token in preferences?
	GoogleAccountManager accountManager;

	public static final int RESULT_FROM_EDIT_TASK = 101; // 2012-11-12 TAN add
	public static final int RESULT_FROM_GTASK_SYNC = 102;
	public static final String GOOGLE_TASKS_DID_UPDATE = "Google Tasks has updated";

	// 2012-11-08 RYAN TAN: add #1
	LayoutInflater mInflater;
	Context mContext;
	ViewGroup mLayout;

	// ATLTopbar mTopBar;
	ATLMultiSectionListView listTask;
	// 2012-11-08 RYAN TAN: end add #1
	ImageButton btnSort;
	ImageButton btnAllies;
	ImageButton btnAdd;

	private RadioButton todoBtn;

	private ImageButton btnLightning;

	private ATLTaskCellList mTaskCellList;

	private ATLTaskListAdapter adaper;

	private ATLTabbarView tabbar;

	public static ArrayList<ATLCalendarModel> calListModel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 2012-11-08 RYAN TAN: add #2
		mInflater = LayoutInflater.from(this);
		mContext = this;
		mLayout = (ViewGroup) mInflater.inflate(R.layout.tasks2, null);
		calListModel = ATLCalendarManager.getCalendars(this);
		setContentView(mLayout);
		initAttributes();
		setListener();
		bindingData();

		// 2012-11-08 RYAN TAN: end add #2

		((RadioButton) findViewById(R.id.btnToDo)).setVisibility(View.VISIBLE);

		todoBtn = (RadioButton) findViewById(R.id.btnToDo);

		todoBtn.setChecked(true);
		todoBtn.setTextColor(Color.BLACK);

		setMainMenuListener();
		new GetGTaskListAsyncTask().execute();
		// startActivityForResult(new Intent(this, GTaskSync.class),
		// RESULT_FROM_GTASK_SYNC);

	}

	@Override
	protected void onResume() {
		super.onResume();
		todoBtn.setChecked(true);
		todoBtn.setTextColor(Color.BLACK);
		bindingData();

	}

	@Override
	protected void onPause() {
		super.onPause();
		// =============== START Hide quick add view
		ATLAnimationUtils.hideKeyboard(this, quickAddTaskView.taskTitle);
		// bottomMenu.setVisibility(View.VISIBLE); // 2013-01-04 TAN: fix for
		// new tabbar
		tabbar.setVisibility(View.VISIBLE);
		quickAddTaskView.setVisibility(View.GONE);
		// ================ END
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_AUTHENTICATE:
			if (resultCode == RESULT_OK) {
				gotAccount(false);
			} else {
				showDialog(DIALOG_ACCOUNTS);
			}
			break;
		case ATLTaskIntentKeys.CALL_FROM_TASK_CELL:
			if (resultCode == ATLTaskIntentKeys.RESULT_FROM_TASK_EDIT) { // Have
																			// edited
				isUpdate = true;
				new GetGTaskListAsyncTask().execute();

			}
			break;
		case ATLTaskIntentKeys.CALL_FROM_TASK_LIST:
			if (resultCode == ATLTaskIntentKeys.RESULT_FROM_TASK_EDIT) { // Have
																			// edited
				isInsert = true;
				new GetGTaskListAsyncTask().execute();
			}

			break;
		case RESULT_FROM_GTASK_SYNC:
			bindingData();
			break;
		}
	}

	// 2012-11-08 RYAN TAN: add #3
	private void initAttributes() {
		// TODO Auto-generated method stub

		// mTopBar = (ATLTopbar) findViewById(R.id.topBarMenu);
		// mTopBar.setType(ATLMainTabbarActivity.TAB_TASKS);
		tabbar = new ATLTabbarView(this);
		tabbar.delegate = this;
		mLayout.addView(tabbar);

		// =========================================================
		// 2012-12-19 TAN: START - Add quick add event view
		// =========================================================
		bottomMenu = (View) findViewById(R.id.bottomBarTasks);
		quickAddViewHolder = (ViewGroup) findViewById(R.id.task_view_content_layout);
		quickAddTaskView = new ATLTaskQuickAddTaskView(this);
		quickAddViewHolder.addView(quickAddTaskView, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		quickAddTaskView.delegate = this;
		quickAddTaskView.setVisibility(View.GONE);
		// =========================================================
		// 2012-12-19 TAN: END - Add quick add event view
		// =========================================================

		btnSort = (ImageButton) findViewById(R.id.sortButton);
		btnLightning = (ImageButton) findViewById(R.id.actionBtn);
		btnAdd = (ImageButton) findViewById(R.id.addbtn);
		btnAllies = (ImageButton) findViewById(R.id.alliesbtn);

		// (ProgressBar) findViewById(R.id.progressBar);

		listTask = (ATLMultiSectionListView) findViewById(R.id.listTask);
		listTask.setPinnedHeaderView(LayoutInflater.from(this).inflate(
				R.layout.listview_header, listTask, false));

		// mTaskCellList = new ATLTaskCellList();
		// adaper = new ATLTaskListAdapter(mTaskCellList, this);
		// listTask.setAdapter(adaper);

	}

	private void bindingData() {
		// TODO Auto-generated method stub
		// progressBar.setVisibility(View.GONE);

		mTaskCellList = new ATLTaskCellList();
		adaper = new ATLTaskListAdapter(mTaskCellList, this);
		listTask.setAdapter(adaper);

	}

	private void setListener() {
		// TODO Auto-generated method stub
		btnAdd.setOnClickListener(this);
		btnAllies.setOnClickListener(this);
		btnSort.setOnClickListener(this);

		btnLightning.setOnClickListener(this);
	}

	// 2012-11-08 RYAN TAN: end add #3
	// 2012-11-08 RYAN TAN: Implement on click listener in one place
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnAdd) {
			EditTaskModel.getInstance().destroy(); // Reset data to new a task
			// EditTaskModel.isNewTask = true;
			isInsert = true; // Active Insert Mode
			startActivityForResult(new Intent(Tasks.this, EditTask.class),
					ATLTaskIntentKeys.CALL_FROM_TASK_LIST);

		} else if (v == btnSort) {
			int width = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, SlideOutHelper.SLIDE_OUT_X, getResources()
							.getDisplayMetrics());
			SlideOutActivity.prepare(Tasks.this, R.id.tasks_content, width);
			startActivity(new Intent(Tasks.this, TasksSort.class));
			overridePendingTransition(0, 0);

		} else if (v == btnAllies) {
			int width = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, SlideOutHelper.SLIDE_OUT_X, getResources()
							.getDisplayMetrics());
			SlideOutActivity.prepare(Tasks.this, R.id.tasks_content, width);
			Intent intent = new Intent(getBaseContext(), Alerts.class);
			intent.putExtra("rightSwipe", true);
			startActivity(intent);
			overridePendingTransition(0, 0);

		} else if (v == btnLightning) {
			Intent actionMenu = new Intent(mContext, Actions.class);
			startActivity(actionMenu);
		}

	}

	private void setMainMenuListener() {

		((RadioGroup) findViewById(R.id.main_btns_group))
				.setVisibility(View.VISIBLE);

		RadioGroup mainBtns = (RadioGroup) findViewById(R.id.main_btns_group);

		RadioGroup.OnCheckedChangeListener rdGrpCheckedListener = new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.btnCal:
					Intent intentCalendar = new Intent(getBaseContext(),
							CalendarDayView.class);
					// intent.putExtra("EXTRA_SESSION_ID", sessionId);
					// 2012-11-08 RYAN TAN: add to clear to top
					intentCalendar.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

					intentCalendar.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intentCalendar);

					break;
				case R.id.btnNotes:
					Intent intentNotes = new Intent(getBaseContext(),
							ATLNoteListActivity.class);
					intentNotes.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

					intentNotes.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					// //intent.putExtra("EXTRA_SESSION_ID", sessionId);
					startActivity(intentNotes);

					break;
				case R.id.btnSettings:
					Intent intentSettings = new Intent(getBaseContext(),
							Settings.class);
					intentSettings.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

					intentSettings.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					// //intent.putExtra("EXTRA_SESSION_ID", sessionId);
					startActivity(intentSettings);

					break;
				case R.id.btnToday:
					Intent intentToday = new Intent(getBaseContext(),

					Contacts.class);
					intentToday.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					intentToday.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					// //intent.putExtra("EXTRA_SESSION_ID", sessionId);
					startActivity(intentToday);

					break;
				case R.id.btnToDo:
					// Intent intentTasks = new Intent(getBaseContext(),
					// Tasks.class);
					// intentTasks.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					// // //intent.putExtra("EXTRA_SESSION_ID", sessionId);
					// startActivity(intentTasks);

					break;
				default:

					break;
				}
			}
		};

		mainBtns.setOnCheckedChangeListener(rdGrpCheckedListener);

	}

	boolean isUpdate = false;
	boolean isInsert = false;
	boolean isDelete = false;

	final class GetGTaskListAsyncTask extends AsyncTask<Void, Void, Void> {

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

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_ACCOUNTS:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Select a Google account");
			final Account[] accounts = accountManager.getAccounts();
			final int size = accounts.length;
			String[] names = new String[size];
			for (int i = 0; i < size; i++) {
				names[i] = accounts[i].name;
			}
			builder.setItems(names, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					gotAccount(accounts[which]);
				}
			});
			return builder.create();
		}
		return null;
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
			SharedPreferences settings = getSharedPreferences(PREF, 0);
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
				showDialog(DIALOG_ACCOUNTS);

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

		@Override
		protected Void doInBackground(final Void... params) {
			/* Load timezone. this is very slow - may take up to 3 seconds. */
			SharedPreferences settings = getSharedPreferences(PREF, 0);
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
									startActivityForResult(intent,
											REQUEST_AUTHENTICATE);
								} else if (bundle
										.containsKey(AccountManager.KEY_AUTHTOKEN)) {
									accessProtectedResource.setAccessToken(bundle
											.getString(AccountManager.KEY_AUTHTOKEN));
									if (isUpdate) {
										updateGoogleTask();
									} else if (isInsert) {
										insertGoogleTask();
									} else if (isDelete) {
										deleteGoogleTask();
									} else {
										onAuthToken();
									}
								}
							} catch (Exception e) {
								handleException(e);
							}
						}

					}, null);
			return null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_ACCOUNTS, 0, "Switch Account");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_ACCOUNTS:
			showDialog(DIALOG_ACCOUNTS);
			return true;
		}
		return false;
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
		new OnAuthTokenAsyncTask().execute();
	}

	final class OnAuthTokenAsyncTask extends AsyncTask<Void, Void, Void> {
		List<String> taskTitles = new ArrayList<String>();
		List<Task> taskList = null;

		public OnAuthTokenAsyncTask() {
			// pR.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(final Void... params) {
			/* Load timezone. this is very slow - may take up to 3 seconds. */
			try {

				taskList = service.tasks().list("@default").execute()
						.getItems();

			} catch (IOException e) {
				handleException(e);
			}

			return null;
		}

		@Override
		protected void onPostExecute(final Void unused) {
			if (taskList != null) {
				// Insert Task into DB
				TaskDatabaseAdapter dbHepler = new TaskDatabaseAdapter();
				DB.openDb();
				dbHepler.deleteAllATLTaskModel();
				for (Task task : taskList) {
					ATLTaskModel atlTask = new ATLTaskModel(task);
					dbHepler.insertATLTaskModel(atlTask);
				}
				DB.closeDb();
				bindingData();// Notify change data in database
				// Toast.makeText(mContext, GOOGLE_TASKS_DID_UPDATE,
				// Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void insertGoogleTask() {
		// TODO Auto-generated method stub
		new InsertTaskAsyncTask().execute();
	}

	private void updateGoogleTask() {
		// TODO Auto-generated method stub
		new UpdateTaskAsyncTask(EditTaskModel.getInstance().getTaskCellData())
				.execute();
	}

	private void deleteGoogleTask() {
		// TODO Auto-generated method stub

		new DeleteTaskAsyncTask(
				EditTaskModel.getInstance().getTaskCellData().taskCellUuid)
				.execute();
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
			isInsert = false;
			if (result != null) {
				// Toast.makeText(mContext, "Task Inserted", Toast.LENGTH_SHORT)
				// .show();

				new GetGTaskListAsyncTask().execute();
			}
		}
	}

	final class UpdateTaskAsyncTask extends AsyncTask<Void, Void, Void> {
		Task result = null;
		Task task = null;
		ATLTaskCellData mCellData;

		public UpdateTaskAsyncTask(ATLTaskCellData cellData) {
			mCellData = cellData.copy();
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
			isUpdate = false;
			if (result != null) {
				// Toast.makeText(mContext, "Task Updated", Toast.LENGTH_SHORT)
				// .show();
				new GetGTaskListAsyncTask().execute();
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

			} catch (IOException e) {
				result = false;
				handleException(e);

			}

			return null;
		}

		@Override
		protected void onPostExecute(final Void unused) {
			isDelete = false;
			if (result) {
				// Toast.makeText(mContext, "Task Deteted", Toast.LENGTH_SHORT)
				// .show();
				EditTaskModel.getInstance().destroy();// Clear data
				new GetGTaskListAsyncTask().execute();
			}
		}
	}

	@Override
	public void didChangeIsComplete(ATLTaskCellData cellData) {
		// TODO Auto-generated method stub
		new UpdateIsCompletedTaskAsyncTask(cellData).execute();
	}

	final class UpdateIsCompletedTaskAsyncTask extends
			AsyncTask<Void, Void, Void> {

		Task result = null;
		Task task = null;
		ATLTaskCellData mCellData;

		public UpdateIsCompletedTaskAsyncTask(ATLTaskCellData cellData) {
			mCellData = cellData.copy();
		}

		@Override
		protected Void doInBackground(final Void... params) {
			/* Load timezone. this is very slow - may take up to 3 seconds. */
			try {
				// pR.setVisibility(View.VISIBLE);
				Date cellDate = new Date();
				Calendar calLocal = Calendar.getInstance();
				calLocal.setTime(cellDate);

				String taskID = mCellData.taskCellUuid;
				// First retrieve the task to update.
				task = service.tasks().get("@default", taskID).execute();

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
			if (result != null) {
				new GetGTaskListAsyncTask().execute();
			}
		}

	}

	// ===============================================================================
	// 2012-12-19 TAN: ATLTaskCellDelegete START Implement
	// ==========================
	// ===============================================================================
	int lastShowDeleteButton = -1;

	@Override
	public void didShowDeleteTaskAtIndex(int index) {
		// TODO Auto-generated method stub
		Log.v("Task", "index: " + index);

		if (lastShowDeleteButton != -1) {
			// Edit old data
			ATLTaskCellData cellOldData = (ATLTaskCellData) listTask
					.getAdapter().getItem(lastShowDeleteButton);
			// cellOldData.isShowOffHour = false;
			// Edit old view
			int firstVisible = listTask.getFirstVisiblePosition();
			int lastVisible = listTask.getLastVisiblePosition();
			Log.v("Tasks", "lastShowDeleteButton: " + lastShowDeleteButton);
			Log.v("Tasks", "firstVisible: " + firstVisible);
			if (firstVisible > lastShowDeleteButton
					|| lastShowDeleteButton > lastVisible) {
				// Toast.makeText(this, "Do nothing",
				// Toast.LENGTH_SHORT).show();
				// Do nothing
			} else if (firstVisible <= lastShowDeleteButton
					&& lastShowDeleteButton <= lastVisible) {
				if (lastShowDeleteButton - firstVisible != index
						&& lastShowDeleteButton != index
						&& cellOldData.isShowDelete) {
					TaskCell cellOld = (TaskCell) listTask
							.getChildAt(lastShowDeleteButton - firstVisible);
					cellOld.dismissDeleteTaskButton();
					// Toast.makeText(this, "dismissDeleteTaskButton ",
					// Toast.LENGTH_SHORT).show();
				}
			}
			lastShowDeleteButton = index;
		} else {
			// Do nothing
			lastShowDeleteButton = index;
		}
	}

	@Override
	public void didDeleteTaskAtIndex(int index) {
		// TODO Auto-generated method stub
		ATLTaskCellData cellData = (ATLTaskCellData) listTask.getAdapter()
				.getItem(index);
		// Reload data for list
		new DeleteTaskAsyncTask(cellData.taskCellUuid).execute();
	}

	// ===============================================================================
	// 2012-12-19 TAN: ATLTaskCellDelegete END Implement
	// ===============================================================================

	// ===============================================================================
	// 2012-12-19 TAN: ATLQuickAddTaskViewDelegete Start Implement
	// ===============================================================================
	@Override
	public void didDoubleTapToQuickAddEventAtIndex(ATLTaskCellData cellData,
			int index) {
		// TODO Auto-generated method stub
		EditTaskModel.getInstance().setData(cellData);
		// bottomMenu.setVisibility(View.GONE);// Because it's always above
		// keyboard
		tabbar.setVisibility(View.GONE);
		ATLAnimationUtils.showKeyboard(this);
		View eventView = getCurrentEventView(index);

		quickAddTaskView.setAnimation(ATLAnimationUtils
				.quickAddEventShow(eventView.getY()));
		quickAddTaskView.setVisibility(View.VISIBLE);
		// ATLTaskCellData cellData = (ATLTaskCellData) listTask.getAdapter()
		// .getItem(index);
		quickAddTaskView.setCellData(cellData);

		quickAddTaskView.taskTitle.requestFocus();
	}

	private View getCurrentEventView(int index) {
		// TODO Auto-generated method stub
		int firstVisible = listTask.getFirstVisiblePosition();
		TaskCell cellOld = (TaskCell) listTask.getChildAt(index - firstVisible);
		return cellOld;
	}

	private ViewGroup quickAddViewHolder;
	private ATLTaskQuickAddTaskView quickAddTaskView;
	private View bottomMenu;

	@Override
	public void didTapDoneButton() {
		// TODO Auto-generated method stub
		// Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
		ATLAnimationUtils.hideKeyboard(this, quickAddTaskView.taskTitle);
		// bottomMenu.setVisibility(View.VISIBLE);
		tabbar.setVisibility(View.VISIBLE);
		quickAddTaskView.setVisibility(View.GONE);

		// Reload data for list
		isUpdate = true;
		new GetGTaskListAsyncTask().execute();
	}

	@Override
	public void didTapMoreButton(ATLTaskCellData cellData) {
		EditTaskModel.getInstance().setData(cellData);
		Intent i = new Intent(this, EditTask.class);
		startActivityForResult(i, ATLTaskIntentKeys.CALL_FROM_TASK_CELL);
	}

	// ===============================================================================
	// 2012-12-19 TAN: ATLQuickAddTaskViewDelegete End Implement
	// ===============================================================================

	// ===============================================================================
	// 2013-01-04 TAN: ATLTabBarViewDelegete Start Implement
	// ===============================================================================

	@Override
	public void didTabToTabIndex(int index) {
		// TODO Auto-generated method stub
		switch (index) {
		case ATLTabbarViewDelegete.TabbarIndex.tabbar_icon_contacts:
			Intent intentContact = new Intent(this,ATLContactListActivity.class);
			intentContact.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			intentContact.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// //intent.putExtra("EXTRA_SESSION_ID", sessionId);
			startActivity(intentContact);
			break;
		case ATLTabbarViewDelegete.TabbarIndex.tabbar_icon_calendar:
			Intent intentCalendar = new Intent(getBaseContext(),
					CalendarDayView.class);
			// intent.putExtra("EXTRA_SESSION_ID", sessionId);
			// 2012-11-08 RYAN TAN: add to clear to top
			intentCalendar.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

			intentCalendar.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intentCalendar);
			break;
		case ATLTabbarViewDelegete.TabbarIndex.tabbar_icon_home:
//			2013-01-09 TAN: Remove Notes tab			
//			Intent intentNotes = new Intent(getBaseContext(), ATLNoteListActivity.class);
//			intentNotes.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//
//			intentNotes.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
//					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			// //intent.putExtra("EXTRA_SESSION_ID", sessionId);
//			startActivity(intentNotes);
			EditTaskModel.getInstance().destroy(); // Reset data to new a task
			// EditTaskModel.isNewTask = true;
			isInsert = true; // Active Insert Mode
			startActivityForResult(new Intent(Tasks.this, EditTask.class),
					ATLTaskIntentKeys.CALL_FROM_TASK_LIST);
			overridePendingTransition(R.anim.in_from_bottom, R.anim.stand_by);
			
			break;
		case ATLTabbarViewDelegete.TabbarIndex.tabbar_icon_tasks:
			break;
		case ATLTabbarViewDelegete.TabbarIndex.tabbar_icon_settings:
			Intent intentSettings = new Intent(getBaseContext(), Settings.class);
			intentSettings.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

			intentSettings.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// //intent.putExtra("EXTRA_SESSION_ID", sessionId);
			startActivity(intentSettings);
			break;
		}
	}

	// ===============================================================================
	// 2013-01-04 TAN: ATLTabBarViewDelegete End Implement
	// ===============================================================================
}
