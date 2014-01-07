//  ==================================================================================================================
//  ATLTaskListActivity.java
//  ATLAS
//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2013-01-18 TAN:     Create class to replace Tasks.java for handle Task View
//  ==================================================================================================================

package com.atlasapp.section_tasks;

import java.util.ArrayList;

import java.util.Date;
import android.accounts.Account;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout.LayoutParams;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableLayout.LayoutParams;


import com.atlasapp.common.ATLAddBarView;
import com.atlasapp.common.ATLAnimationUtils;
import com.atlasapp.common.ATLDragAndDropView;
import com.atlasapp.common.ATLTabbarView;
import com.atlasapp.common.ATLTabbarViewDelegete;
import com.atlasapp.common.DateTimeUtilities;
import com.atlasapp.common.SlideOutActivity;
import com.atlasapp.common.SlideOutHelper;
import com.atlasapp.model.ATLCalendarModel;
import com.atlasapp.model.ATLTaskModel;
import com.atlasapp.section_actions.Actions;
import com.atlasapp.section_alerts.ATLAlertFragment_2;
import com.atlasapp.section_alerts.AlertFragment;
import com.atlasapp.section_alerts.Alerts;
import com.atlasapp.section_appentry.R;
import com.atlasapp.section_calendar.ATLCalendarManager;
import com.atlasapp.section_calendar.CalendarDayView;
import com.atlasapp.section_calendar.CalendarDayViewFragment;
import com.atlasapp.section_contacts.ATLContactListActivity;
import com.atlasapp.section_main.ATLMultiSectionListView;
import com.atlasapp.section_main.ATLTopbar;
import com.atlasapp.section_settings.Settings;
import com.atlasapp.section_slidemenu.ATLSlideMenuFragment;
import com.ericharlow.DragNDrop.DragListener;
import com.ericharlow.DragNDrop.DropListener;

/**
 * @author Ryan Tan
 * 
 */
public class ATLTaskListActivity extends FragmentActivity implements
		View.OnClickListener, ATLTaskCellDelegete, ATLGTaskSyncServiceDelegate,
		ATLTaskQuickAddTaskViewDelegate{
	LayoutInflater mInflater;
	Context mContext;
	ViewGroup mLayout;
	ATLTopbar mTopBar;
	ATLMultiSectionListView listTask;
	static public ArrayList<ATLCalendarModel> calListModel;

	// 2012-11-08 RYAN TAN: end add #1
	ImageButton btnSort;
	ImageButton btnAllies;
	ImageButton btnAdd;
	private ImageButton btnLightning;

	private ATLTaskCellList mTaskCellList;
	private ATLTaskListAdapter adapter;


//	private ATLTabbarView tabbar;
	public ATLDragAndDropView taskListHolderView;
	private ViewGroup alertHolderView;
	private ViewGroup taskSortHolderView; 
	public View taskListViewCover;
	private int topMenuBarHeight;
//	private TasksFragment slideMenuFragment;//TAN is going on menu 3.0
	private ATLSlideMenuFragment slideMenuFragment;
	
	private ATLAlertFragment_2 alertFragment;
	
	private ATLAddBarView addbar;
	
	static ATLGTaskServices gtaskServices;

	private static final int DIALOG_ACCOUNTS = 0;
	public static final int REQUEST_AUTHENTICATE = 0;
	public static final int RESULT_FROM_EDIT_TASK = 101; // 2012-11-12 TAN add
	public static final int RESULT_FROM_GTASK_SYNC = 102;
	public static final String GOOGLE_TASKS_DID_UPDATE = "Google Tasks has updated";
	public static boolean isTaskDataChanged = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater = LayoutInflater.from(this);
		mContext = this;
		mLayout = (ViewGroup) mInflater.inflate(R.layout.task_list_3, null);
		calListModel = ATLCalendarManager.getCalendars(this);
		setContentView(mLayout);
		initAttributes();
		setListener();
		bindingData();
		gtaskServices = new ATLGTaskServices(this);
		gtaskServices.delegate = this;
	}

	@Override
	public void onResume() {
		super.onResume();
		
		taskListViewCover.setVisibility(View.GONE);
		taskListHolderView.setX(0);
		
		//2013-01-27 TAN fix double tap to show side menu # Start
//		btnSort.setClickable(true);
//		btnAllies.setClickable(true);
		//2013-01-27 TAN fix double tap to show side menu # End
		bindingData();
		TaskDatabaseAdapter dbHepler = new TaskDatabaseAdapter();
		ArrayList<ATLTaskModel> taskArr = dbHepler.loadAllTasksInDatabase();
		for(ATLTaskModel task :taskArr){
			if(task.taskIsDeleted){
				isTaskDataChanged = true;
				break;
			}
		}
		
		if(isTaskDataChanged){
			Log.v("ATLGTaskServices", "isTaskDataChanged is True");
			try{
				gtaskServices.sync();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
//		bindingData();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// =============== START Hide quick add view
		ATLAnimationUtils.hideKeyboard(this, quickAddTaskView.taskTitle);
		// bottomMenu.setVisibility(View.VISIBLE); // 2013-01-04 TAN: fix for
		// new tabbar
//		tabbar.setVisibility(View.VISIBLE);
		addbar.setVisibility(View.VISIBLE);
		quickAddTaskView.setVisibility(View.GONE);
		// ================ END
		isFragmentShowing = false;
		ATLDragAndDropView.isAtRight = false;
		ATLDragAndDropView.isAtLeft = false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_AUTHENTICATE:
			if (resultCode == RESULT_OK) {
				gtaskServices.gotAccount(false);
			} else {
				showDialog(DIALOG_ACCOUNTS);
			}
			break;
		case ATLTaskIntentKeys.CALL_FROM_TASK_CELL:
			if (resultCode == ATLTaskIntentKeys.RESULT_FROM_TASK_EDIT) { // Have
//				gtaskServices.updateGoogleTask();
//				Toast.makeText(mContext, "ATLTaskIntentKeys.CALL_FROM_TASK_CELL", Toast.LENGTH_SHORT).show();
			}
			break;
		case ATLTaskIntentKeys.CALL_FROM_TASK_LIST:
//				gtaskServices.insertGoogleTask();
//				Toast.makeText(mContext, "ATLTaskIntentKeys.CALL_FROM_TASK_LIST", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_ACCOUNTS:
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setTitle("Select a Google account");
			final Account[] accounts = gtaskServices.accountManager.getAccounts();
			final int size = accounts.length;
			String[] names = new String[size];
			for (int i = 0; i < size; i++) {
				names[i] = accounts[i].name;
			}
			builder.setItems(names, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					gtaskServices.gotAccount(accounts[which]);
				}
			});
			return builder.create();
		}
		return null;
	}
	

	private void initAttributes() {
		// TODO Auto-generated method stub
		// mTopBar.setType(ATLMainTabbarActivity.TAB_TASKS);
		//==========================================================
		// 2013-02-03 TAN: new slide out # START
		//==========================================================
		
		taskListHolderView = (ATLDragAndDropView)mLayout.findViewById(R.id.tasks_content);
		alertHolderView = (ViewGroup)mLayout.findViewById(R.id.task_alert_fragment);
//		alertHolderView.setX(100);
		taskSortHolderView = (ViewGroup)mLayout.findViewById(R.id.task_sort_fragment);
		taskListViewCover = (View)mLayout.findViewById(R.id.task_list_cover_view);
		taskListViewCover.setOnTouchListener(taskListHolderView);
	    // top menu bar height
	    topMenuBarHeight = (int) Math.ceil(50 * this.getResources().getDisplayMetrics().density);
	    
		 if (taskListHolderView instanceof ATLDragAndDropView) {
			 ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
			 taskListHolderView.setDropListener(mDropListener);
			 taskListHolderView.setDragListener(mDragListener);
	     }
		slideMenuFragment = new ATLSlideMenuFragment();
		getSupportFragmentManager().beginTransaction()
		.add(R.id.task_sort_fragment, slideMenuFragment).commit();
		alertFragment =  ATLAlertFragment_2.getInstance();
		getSupportFragmentManager().beginTransaction()
		.add(R.id.task_alert_fragment, alertFragment).commit();
		
		//==========================================================
		// 2013-02-03 TAN: new slide out # END
		//==========================================================
//		tabbar = new ATLTabbarView(this);
//		tabbar.delegate = this;
		addbar = new ATLAddBarView(this);
		taskListHolderView.addView(addbar);

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
		
	}

	private void bindingData() {
		// TODO Auto-generated method stub
		mTaskCellList = new ATLTaskCellList();
		adapter = new ATLTaskListAdapter(mTaskCellList, this);
		listTask.setAdapter(adapter);
	}

	private void setListener() {
		// TODO Auto-generated method stub
		btnAdd.setOnClickListener(this);
		btnAllies.setOnClickListener(this);
		btnSort.setOnClickListener(this);
		btnLightning.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnAdd) {
			EditTaskModel.getInstance().destroy(); // Reset data to new a task
			// EditTaskModel.isNewTask = true;
//			gtaskServices.isInsert = true; // Active Insert Mode
			startActivityForResult(new Intent(ATLTaskListActivity.this,
					EditTask.class), ATLTaskIntentKeys.CALL_FROM_TASK_LIST);

		} else if (v == btnSort) {
//			int width = (int) TypedValue.applyDimension(
//					TypedValue.COMPLEX_UNIT_DIP, SlideOutHelper.SLIDE_OUT_X,
//					getResources().getDisplayMetrics());
//			SlideOutActivity.prepare(ATLTaskListActivity.this,
//					R.id.tasks_content, width);
//			startActivity(new Intent(ATLTaskListActivity.this, TasksSort.class));
//			overridePendingTransition(0, 0);
			// 2013-02-03 TAN: new slide out # START
			
			if(!isFragmentShowing){
				alertHolderView.setVisibility(View.GONE);
				taskSortHolderView.setVisibility(View.VISIBLE);
				taskListViewCover.setVisibility(View.VISIBLE);
				taskListHolderView.setX(taskListHolderView.LEFTRIGHT_WINDOW_WIDTH);
				ATLDragAndDropView.topMenuBarHeight = taskListHolderView.getHeight();
				ATLDragAndDropView.isAtRight = true;
				ATLDragAndDropView.isAtLeft = false;
				taskListHolderView.startAnimation(ATLAnimationUtils.mainFragmentMoveRight(taskListHolderView.LEFTRIGHT_WINDOW_WIDTH));
				isFragmentShowing = true;
			}
			else{
				taskListViewCover.setVisibility(View.GONE);
				taskListHolderView.setX(0);
				ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
				ATLDragAndDropView.isAtRight = false;
				ATLDragAndDropView.isAtLeft = false;
				taskListHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromRight(taskListHolderView.LEFTRIGHT_WINDOW_WIDTH));
				isFragmentShowing = false;
				onResume();
			}
			// 2013-02-03 TAN: new slide out # END

		} else if (v == btnAllies) {
//			int width = (int) TypedValue.applyDimension(
//					TypedValue.COMPLEX_UNIT_DIP, SlideOutHelper.SLIDE_OUT_X,
//					getResources().getDisplayMetrics());
//			SlideOutActivity.prepare(ATLTaskListActivity.this,
//					R.id.tasks_content, width);
//			Intent intent = new Intent(getBaseContext(), Alerts.class);
//			intent.putExtra("rightSwipe", true);
//			startActivity(intent);
//			overridePendingTransition(0, 0);
			if(!isFragmentShowing){
				alertHolderView.setVisibility(View.VISIBLE);
				taskSortHolderView.setVisibility(View.GONE);
				taskListViewCover.setVisibility(View.VISIBLE);
				ATLDragAndDropView.topMenuBarHeight = taskListHolderView.getHeight();
				taskListHolderView.setX(-taskListHolderView.LEFTRIGHT_WINDOW_WIDTH);
				taskListHolderView.startAnimation(ATLAnimationUtils.mainFragmentMoveLeft(taskListHolderView.LEFTRIGHT_WINDOW_WIDTH));
				alertFragment.onResume();// Reload view data
				isFragmentShowing = true;
				ATLDragAndDropView.isAtRight = false;
				ATLDragAndDropView.isAtLeft = true;
			}
			else{
				ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
				taskListViewCover.setVisibility(View.GONE);
				taskListHolderView.setX(0);
				taskListHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromLeft());
				isFragmentShowing = false;
				ATLDragAndDropView.isAtRight = false;
				ATLDragAndDropView.isAtLeft = false;
				onResume();
			}

		} else if (v == btnLightning) {
			Intent actionMenu = new Intent(mContext, Actions.class);
			startActivity(actionMenu);
		}

	}

	// ===============================================================================
	// 2012-12-19 TAN: ATLTaskCellDelegete START Implement
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

		cellData.taskCellModifiedDate = new Date();
		cellData.taskCellIsDeleted = true;
		cellData.save();
		// reload data
		bindingData();

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
//		tabbar.setVisibility(View.GONE);
		addbar.setVisibility(View.GONE);
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
	public View bottomMenu;
	public boolean isFragmentShowing;

	@Override
	public void didTapDoneButton() {
		// TODO Auto-generated method stub
		// Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
		ATLAnimationUtils.hideKeyboard(this, quickAddTaskView.taskTitle);
		// bottomMenu.setVisibility(View.VISIBLE);
//		tabbar.setVisibility(View.VISIBLE);
		addbar.setVisibility(View.VISIBLE);
		quickAddTaskView.setVisibility(View.GONE);

		// Reload data for list
		//gtaskServices.isUpdate = true;
		
//		gtaskServices.updateGoogleTask();
		EditTaskModel.getInstance().getTaskCellData().taskCellModifiedDate = new Date();
		EditTaskModel.getInstance().getTaskCellData().save();
		EditTaskModel.getInstance().destroy();
		
//		mTaskCellList.load();
//		adapter.notifyDataSetChanged();
		bindingData();
//		gtaskServices.sync();
		// new GetGTaskListAsyncTask().execute();
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

/* 2013-02-16 TAN: remove tabbar
	 
	// ===============================================================================
	// 2013-01-04 TAN: ATLTabBarViewDelegete Start Implement
	// ===============================================================================

	@Override
	public void didTabToTabIndex(int index) {
		// TODO Auto-generated method stub
		switch (index) {
		case ATLTabbarViewDelegete.TabbarIndex.tabbar_icon_contacts:
			Intent intentContact = new Intent(this,
					ATLContactListActivity.class);
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
			EditTaskModel.getInstance().destroy(); // Reset data to new a task
			// EditTaskModel.isNewTask = true;
//			gtaskServices.isInsert = true; // Active Insert Mode
			startActivityForResult(new Intent(ATLTaskListActivity.this,
					EditTask.class), ATLTaskIntentKeys.CALL_FROM_TASK_LIST);
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
*/
	@Override
	public void didChangeIsComplete(ATLTaskCellData cellData) {
		// TODO Auto-generated method stub
//		gtaskServices.updateIsCompleteTask(cellData);
		cellData.taskCellModifiedDate = new Date();
		cellData.save();
		
//		mTaskCellList.load();
//		adapter.notifyDataSetChanged();
		bindingData();
//		gtaskServices.sync();
	}

	@Override
	public void didFinishSyncService(boolean isSuccess) {
		// TODO Auto-generated method stub
		if(isSuccess){
//			mTaskCellList.load();
//			adapter.notifyDataSetChanged();
			isTaskDataChanged = false;
			bindingData();
		}
		else{
//			gtaskServices.sync();// retry
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void requestShowDialog() {
		// TODO Auto-generated method stub
		try{
			showDialog(DIALOG_ACCOUNTS);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}


	// ===============================================================================
	// 2013-01-04 TAN: ATLTabBarViewDelegete End Implement
	// ===============================================================================
	// ===============================================================================
	// 2013-02-03 TAN: Implement Swipe left and right # START
	// ===============================================================================
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			taskListViewCover.setVisibility(View.GONE);
			taskListHolderView.setX(0);
			taskListHolderView.setVisibility(View.VISIBLE);
			ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
			
			if(ATLDragAndDropView.isAtRight){
				taskListHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromRight(taskListHolderView.LEFTRIGHT_WINDOW_WIDTH));
				ATLDragAndDropView.isAtRight = false;
				ATLDragAndDropView.isAtLeft = false;
				isFragmentShowing = false;
				onResume();
				return true;
			}
			else if(ATLDragAndDropView.isAtLeft){
				taskListHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromLeft());
				ATLDragAndDropView.isAtRight = false;
				ATLDragAndDropView.isAtLeft = false;
				isFragmentShowing = false;
				onResume();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
		
	}

	private DragListener mDragListener =
		new DragListener() {
			public void onDrag(int x, int y, ListView listView) {
				// TODO Auto-generated method stub
				
				if(x <= 0){
					//isSwipeLeft = true;
					alertHolderView.setVisibility(View.VISIBLE);
					taskSortHolderView.setVisibility(View.GONE);
				}
				else{
					//isSwipeLeft = false;
					alertHolderView.setVisibility(View.GONE);
					taskSortHolderView.setVisibility(View.VISIBLE);
				}
				
			}
	
			public void onStartDrag(View itemView) {
				taskListHolderView.setVisibility(View.GONE);
				Log.v("CalendarDayView", "onStartDrag");
			}
	
			public void onStopDrag(View itemView) {
				taskListHolderView.setVisibility(View.VISIBLE);
				Log.v("CalendarDayView", "onStopDrag");
			}
		
	};

	private DropListener mDropListener = 
			new DropListener() {
	        public void onDrop(int from, int to) {
	        	taskListHolderView.setVisibility(View.VISIBLE);
	        	if(ATLDragAndDropView.isTouched){
	        		ATLDragAndDropView.isTouched = false;
	        		taskListViewCover.setVisibility(View.GONE);
	    			taskListHolderView.setX(0);
	    			taskListHolderView.setVisibility(View.VISIBLE);
	    			ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
	    			
	    			if(ATLDragAndDropView.isAtRight){
	    				taskListHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromRight(taskListHolderView.LEFTRIGHT_WINDOW_WIDTH));
	    				ATLDragAndDropView.isAtRight = false;
	    				ATLDragAndDropView.isAtLeft = false;
	    				isFragmentShowing = false;
	    				onResume();
	    			}
	    			else if(ATLDragAndDropView.isAtLeft){
	    				taskListHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromLeft());
	    				ATLDragAndDropView.isAtRight = false;
	    				ATLDragAndDropView.isAtLeft = false;
	    				isFragmentShowing = false;
						onResume();
	    			}
	        	}
	        	else if(ATLDragAndDropView.isAtLeft){
	        		if(from > to){
	        		
	        		}
	        		else if(to > from && to > 300){
	        			ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
						taskListViewCover.setVisibility(View.GONE);
						taskListHolderView.setX(0);
						taskListHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromLeftAt(to, taskListHolderView.LEFTRIGHT_WINDOW_WIDTH));
						isFragmentShowing = false;
						ATLDragAndDropView.isAtRight = false;
						ATLDragAndDropView.isAtLeft = false;
						onResume();
	    	        }
				}else if(ATLDragAndDropView.isAtRight){
					if(from < to){
		        		
	        		}
	        		else if(from > to && to < 450){
	        			taskListViewCover.setVisibility(View.GONE);
						taskListHolderView.setX(0);
						ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
						ATLDragAndDropView.isAtRight = false;
						ATLDragAndDropView.isAtLeft = false;
						taskListHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromRightAt(to 
								- (from - taskListHolderView.LEFTRIGHT_WINDOW_WIDTH)));
						isFragmentShowing = false;
						onResume();
	    	        }
				}else{
					if(from > to && to < 360){
		        		
		        		alertHolderView.setVisibility(View.VISIBLE);
						taskSortHolderView.setVisibility(View.GONE);
						taskListViewCover.setVisibility(View.VISIBLE);
						taskListHolderView.setX(-taskListHolderView.LEFTRIGHT_WINDOW_WIDTH);
						taskListHolderView.startAnimation(ATLAnimationUtils.mainFragmentMoveLeftAt(from - to, taskListHolderView.LEFTRIGHT_WINDOW_WIDTH));
						ATLDragAndDropView.topMenuBarHeight = taskListHolderView.getHeight();
						alertFragment.onResume();// Reload view data
						isFragmentShowing = true;
						ATLDragAndDropView.isAtRight = false;
						ATLDragAndDropView.isAtLeft = true;
		        	}
		        	else if(to > from && to > 360){
		        		
		        		alertHolderView.setVisibility(View.GONE);
						taskSortHolderView.setVisibility(View.VISIBLE);
						taskListViewCover.setVisibility(View.VISIBLE);
						taskListHolderView.setX(taskListHolderView.LEFTRIGHT_WINDOW_WIDTH);
						taskListHolderView.startAnimation(ATLAnimationUtils.mainFragmentMoveRightAt(to - from, taskListHolderView.LEFTRIGHT_WINDOW_WIDTH));
						ATLDragAndDropView.topMenuBarHeight = taskListHolderView.getHeight();
						isFragmentShowing = true;
						ATLDragAndDropView.isAtRight = true;
						ATLDragAndDropView.isAtLeft = false;
		        	}
				}
	        	
	        }
		};
	// ===============================================================================
	// 2013-02-03 TAN: Implement Swipe left and right # END
	// ===============================================================================		
}