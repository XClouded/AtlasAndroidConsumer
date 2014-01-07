////  ==================================================================================================================
////  ATLTaskQuickAddTaskView.java
////  ATLAS
////  Copyright (c) 2012 ATLAS Apps. All rights reserved.
////  ==================================================================================================================
////
////  ==================================================================================================================
////  HISTORY
////  YYYY-MM-DD NAME:    Description of changes
////  ==================================================================================================================
////  2012-12-19 TAN:      Create class
////  ==================================================================================================================
//
//package atlasapp.section_tasks;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//
//import kankan.wheel.widget.OnWheelChangedListener;
//import kankan.wheel.widget.OnWheelScrollListener;
//import kankan.wheel.widget.WheelView;
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.os.AsyncTask;
//import android.util.AttributeSet;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.inputmethod.EditorInfo;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import atlasapp.common.ATLActionEditText;
//import atlasapp.common.CalendarUtilities;
//import atlasapp.model.ATLCalendarModel;
//import atlasapp.section_appentry.R;
//import atlasapp.section_calendar.ATLCalendarManager;
//import atlasapp.section_calendar.ATLCalendarScrollListAdapter;
//import atlasapp.section_tasks.Tasks.GetGTaskListAsyncTask;
//
//import com.google.api.client.util.DateTime;
//import com.google.api.services.tasks.model.Task;
//
//interface ATLTaskQuickAddTaskViewDelegate {
//	void didTapDoneButton();
//
//	void didTapMoreButton(ATLTaskCellData cellData);
//}
//
//public class ATLTaskQuickAddTaskView extends RelativeLayout implements
//		View.OnClickListener, OnWheelChangedListener, OnWheelScrollListener {
//	Activity mActivity;
//	ImageView calendarImageView; // Calendar color view
//	ATLTaskCellData cellData;
//	ImageButton moreBtn;
//	ImageButton alisaBtn;
//	protected ATLTaskQuickAddTaskViewDelegate delegate;
//	public View contentHolderView;
//	private WheelView calList;
//	private WheelView priorityList;
//	private int calColor = Color.TRANSPARENT;
//	private int calId = 0;
//	private String calName = "";
//	private int taskPriority = 0;
//	private ArrayList<ATLCalendarModel> calListModel;
//	public ATLActionEditText taskTitle;
//	public CheckBox checkboxButton;
//	private ImageView priorityIconImg;
//
//	public ATLTaskQuickAddTaskView(Context context, AttributeSet attrs,
//			int defStyle) {
//		super(context, attrs, defStyle);
//		// TODO Auto-generated constructor stub
//		initView(context);
//	}
//
//	public ATLTaskQuickAddTaskView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		// TODO Auto-generated constructor stub
//		initView(context);
//	}
//
//	public ATLTaskQuickAddTaskView(Context context) {
//		super(context);
//		// TODO Auto-generated constructor stub
//		initView(context);
//	}
//
//	private void initView(Context context) {
//		// Init CallendarCell view
//		LayoutInflater.from(context).inflate(R.layout.task_quick_add_task,
//				this, true);
//		mActivity = (Activity) context;
//		contentHolderView = (View) findViewById(R.id.task_quick_add_event_content_view);
//		calendarImageView = (ImageView) findViewById(R.id.task_quick_add_task_color_img);
//		priorityIconImg = (ImageView) findViewById(R.id.task_quick_add_priority_cell_img);
//		checkboxButton = (CheckBox) findViewById(R.id.task_cell_is_complete_check_box);
//		taskTitle = (ATLActionEditText) findViewById(R.id.task_quick_add_edit_text);
//		taskTitle
//				.setOnEditorActionListener(new EditText.OnEditorActionListener() {
//
//					@Override
//					public boolean onEditorAction(TextView v, int actionId,
//							KeyEvent event) {
//						// TODO Auto-generated method stub
//						if (actionId == EditorInfo.IME_ACTION_DONE) {
//							save();
//							delegate.didTapDoneButton();
//							return true;
//						}
//						return false;
//					}
//
//				});
//
//		moreBtn = (ImageButton) findViewById(R.id.task_quick_add_bottom_bar_btn_more2x);
//		moreBtn.setOnClickListener(this);
//		alisaBtn = (ImageButton) findViewById(R.id.task_quick_add_bottom_bar_btn_allies);
//		alisaBtn.setOnClickListener(this);
//		String[] calNames = ATLCalendarManager.getCalendarName(mActivity);
//		calList = (WheelView) findViewById(R.id.task_quick_add_bottom_bar_calendars);
//		// calList.setViewAdapter(new ArrayWheelAdapter<String>(mActivity,
//		// calNames));
//		calListModel = ATLCalendarManager.getCalendars(mActivity);
//		calList.setViewAdapter(new ATLCalendarScrollListAdapter(calListModel,
//				mActivity));
//		calColor = calListModel.get(0).color;
//		calList.addChangingListener(this);
//
//		priorityList = (WheelView) findViewById(R.id.task_quick_add_priority_wheel);
//		priorityList.addChangingListener(this);
//		priorityList.addScrollingListener(this);
//		priorityListDidChange();
//		
//	}
//
//	private void priorityListDidChange() {
//		// TODO Auto-generated method stub
//		priorityList.setViewAdapter(new ATLTaskPriorityScrollListAdapter(
//				mActivity, calColor));
//	}
//
//	private void save() {
//		// TODO Auto-generated method stub
//		 this.cellData.taskCellCalendarColor = calColor;
//		 this.cellData.taskCellTitle = taskTitle.getText().toString();
//		 this.cellData.calendarId = calId;
//		 this.cellData.taskCellPriorityInt =  taskPriority;
//		 this.cellData.taskCellIsCompleted = checkboxButton.isChecked();
//		 this.cellData.taskCellCalendarName =   calName;
//		EditTaskModel.getInstance().setData(this.cellData);
//	}
//
//	private void updateDatabase() {
//		// if (this.cellData.isBlank) {
//		// CalendarUtilities.addCellData(this.cellData, mActivity);
//		// } else {
//		// CalendarUtilities.updateCellData(cellData, mActivity);
//		// }
//	}
//
//	public void setCellData(ATLTaskCellData cellData) {
//		// TODO Auto-generated method stub
//		// calendarImageView.setVisibility(View.GONE);
//		 this.cellData = cellData.copy();
//		calendarImageView.setBackgroundColor(cellData.taskCellCalendarColor);
//		taskTitle.setText(cellData.taskCellTitle);
//		checkboxButton.setChecked(cellData.taskCellIsCompleted);
//
//		int index = 0;
//		for (ATLCalendarModel calModel : calListModel) {
//			if (cellData.calendarId == -1) {// Default value
//				calList.setCurrentItem(0);
//				calendarImageView.setBackgroundColor(calModel.color);
//				break;
//			} else if (cellData.calendarId == calModel.id) {
//				calList.setCurrentItem(index);
//				calendarImageView.setBackgroundColor(calModel.color);
//				break;
//			}
//			index++;
//		}
//
//		int resourceID = R.drawable.tasks_flame_blue;
//		;
//		switch (cellData.taskCellPriorityInt) {
//		case ATLTaskPriorityScrollListAdapter.TASK_PRIORITY_INDEX_LOW:
//			resourceID = R.drawable.tasks_flame_blue;
//			break;
//		case ATLTaskPriorityScrollListAdapter.TASK_PRIORITY_INDEX_HIGH:
//			resourceID = R.drawable.tasks_very_important_blue;
//			break;
//		case ATLTaskPriorityScrollListAdapter.TASK_PRIORITY_INDEX_MEDIUM:
//			resourceID = R.drawable.tasks_important_blue;
//			break;
//
//		}
//		priorityIconImg.setImageResource(resourceID);
//		
//		priorityList.setCurrentItem(cellData.taskCellPriorityInt);
//	}
//
//	private int determineClockHour(int hour) {
//		if (hour == 0) {
//			return 12;
//		}
//		if (hour > 12) {
//			return hour - 12;
//		}
//		return hour;
//	}
//
//	private String determineAmPm(int hour) {
//		return hour < 12 ? "AM" : "PM";
//	}
//
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		if (v == moreBtn) {
//			save();
//			delegate.didTapMoreButton(this.cellData);
//		} else if (v == alisaBtn) {
//			Toast.makeText(mActivity, "Comming soon...", Toast.LENGTH_SHORT)
//					.show();
//		}
//	}
//
//	Bitmap bitmap = null;
//	Bitmap bitmap_0 = BitmapFactory.decodeResource(getResources(),
//			R.drawable.tasks_flame_blue);
//	Bitmap bitmap_1 = BitmapFactory.decodeResource(getResources(),
//			R.drawable.tasks_very_important_blue);
//	Bitmap bitmap_2 = BitmapFactory.decodeResource(getResources(),
//			R.drawable.tasks_important_blue);
//
//	@Override
//	public void onChanged(WheelView wheel, int oldValue, int newValue) {
//		// TODO Auto-generated method stub
//		if (wheel == calList) {
//			calColor = calListModel.get(newValue).color;
//			calId = calListModel.get(newValue).id;
//			calName = calListModel.get(newValue).name;
//			calendarImageView.setBackgroundColor(calColor);
//			
//
//		} else if (wheel == priorityList) {
//			switch (newValue) {
//			case ATLTaskPriorityScrollListAdapter.TASK_PRIORITY_INDEX_LOW:
//				// resourceID = R.drawable.tasks_flame_blue;
//				bitmap = bitmap_0;
//				taskPriority = ATLTaskPriorityScrollListAdapter.TASK_PRIORITY_INDEX_LOW;
//				break;
//			case ATLTaskPriorityScrollListAdapter.TASK_PRIORITY_INDEX_HIGH:
//				// resourceID = R.drawable.tasks_very_important_blue;
//				bitmap = bitmap_1;
//				taskPriority = ATLTaskPriorityScrollListAdapter.TASK_PRIORITY_INDEX_HIGH;
//				break;
//			case ATLTaskPriorityScrollListAdapter.TASK_PRIORITY_INDEX_MEDIUM:
//				// resourceID = R.drawable.tasks_important_blue;
//				bitmap = bitmap_2;
//				taskPriority = ATLTaskPriorityScrollListAdapter.TASK_PRIORITY_INDEX_MEDIUM;
//				break;
//
//			}
//
//			// priorityIconImg.setImageResource(resourceID);
//		}
//
//	}
//
//	final class ChangePriorityImageColorAsyncTask extends
//			AsyncTask<Void, Void, Void> {
//
//		private Bitmap sourceBitmap = null;
//
//		public ChangePriorityImageColorAsyncTask() {
//
//		}
//
//		@Override
//		protected Void doInBackground(final Void... params) {
//			/* Load timezone. this is very slow - may take up to 3 seconds. */
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(final Void unused) {
//			sourceBitmap = CalendarUtilities.drawableToBitmap(priorityIconImg
//					.getDrawable());
//			CalendarUtilities.setImageColor(priorityIconImg, sourceBitmap,
//					calColor);
//			priorityListDidChange();
//
//		}
//
//	}
//
//	@Override
//	public void onScrollingStarted(WheelView wheel) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onScrollingFinished(WheelView wheel) {
//		// TODO Auto-generated method stub
//		if (wheel == calList) {
//			new ChangePriorityImageColorAsyncTask().execute();
//
//		} else if (wheel == priorityList) {
//			priorityIconImg.setImageBitmap(bitmap);
//		}
//	}
//
//}