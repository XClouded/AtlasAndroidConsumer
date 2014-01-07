package com.atlasapp.section_calendar;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlasapp.common.AtlasAndroidUser;
import com.atlasapp.model.ATLCalendarModel;
import com.atlasapp.section_appentry.AtlasApplication;
import com.atlasapp.section_appentry.R;

public class CalendarDayViewFragment extends Fragment {

	LayoutInflater mInflater;
	Context mContext;
	View mLayout;
	ListView calSelectList;
	TextView userNameTextView;
	ImageView userAvatar;
	private FragmentActivity calendarActivity;
	private CalendarSelectAdapter adapter;
	private ArrayList<ATLCalendarModel> calendarList;
	private CheckBox showAllCheckBox;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		calendarActivity = getActivity();
		mContext = (Context) getActivity();
		mInflater = LayoutInflater.from(calendarActivity);
		// calArr = ATLCalendarManager.getCalendars(calendarActivity);
		calendarList = ATLCalendarStore.loadCalendarList(mContext);
		mLayout = (View) mInflater.inflate(R.layout.calendar_select, null);
		initAttributes();
		setListener();
		bindingData();

		return mLayout;
	}

	private void initAttributes() {
		// TODO Auto-generated method stub
		calSelectList = (ListView) mLayout
				.findViewById(R.id.cal_select_list_view);
		userNameTextView = (TextView) mLayout
				.findViewById(R.id.cal_select_user_name_textview);
		userAvatar = (ImageView) mLayout
				.findViewById(R.id.cal_select_user_avatar);
		showAllCheckBox = (CheckBox) mLayout
				.findViewById(R.id.calendar_select_checkbox);

	}

	private void bindingData() {
		// TODO Auto-generated method stub
		userNameTextView.setText(AtlasAndroidUser.getUserNameDisplay());

		// 2013-01-09 TAN : add profile picture, temp solution
		Bitmap storedBitmap = null;
		File imgFile = AtlasApplication.PROFILE_PIC_PATH;
		if (imgFile.exists())
			storedBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		userAvatar.setImageBitmap(storedBitmap);
		// 2013-01-09 TAN : add profile picture, temp solution

		loadingAllCalendar();
		saveCalendarList();
		
		adapter = new CalendarSelectAdapter(calendarList, calendarActivity);
		calSelectList.setAdapter(adapter);

	}

	private void saveCalendarList() {
		ATLCalendarStore.save(calendarList, mContext);
	}

	private void loadingAllCalendar() {
		CalendarDayView.calendarInActiveNameArray.clear();
		for (ATLCalendarModel calendar : calendarList) {
			if (!calendar.isActive) {
				CalendarDayView.calendarInActiveNameArray.add(calendar.name);
			}
		}

		if (CalendarDayView.calendarInActiveNameArray.size() == 0) {
			showAllCheckBox.setChecked(true);
		} else {
			showAllCheckBox.setChecked(false);
		}
	}

	private void setListener() {
		// TODO Auto-generated method stub
		showAllCheckBox.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (ATLCalendarModel calendar : calendarList) {
					if (showAllCheckBox.isChecked()) {
						calendar.isActive = true;
					} else {
						calendar.isActive = false;
					}
				}
				bindingData();

			}

		});
	}

	// /////////////////////////////////////////////////
	// CalendarSelectAdapter START
	// /////////////////////////////////////////////////
	class CalendarSelectAdapter extends BaseAdapter {
		ArrayList<ATLCalendarModel> calModels;
		int size;
		Context mContext;

		public CalendarSelectAdapter(ArrayList<ATLCalendarModel> calArr,
				Context ctx) {

			// TODO Auto-generated constructor stub
			calModels = calArr;
			mContext = ctx;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			size = calModels.size();
			// Hard code for basic view phase
			return size;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return calModels.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			CalendarSelectCell cell;
			if (convertView == null) {
				cell = new CalendarSelectCell(mContext);
				convertView = (View) cell;
				convertView.setTag(cell);
			} else {
				cell = (CalendarSelectCell) convertView.getTag();
			}
			ATLCalendarModel cellData = (ATLCalendarModel) calModels
					.get(position);
			cell.setCellData(cellData);

			return convertView;
		}

	}

	// /////////////////////////////////////////////////
	// CalendarSelectAdapter END
	// /////////////////////////////////////////////////

	// /////////////////////////////////////////////////
	// CalendarSelectCell START
	// /////////////////////////////////////////////////

	class CalendarSelectCell extends RelativeLayout {

		private CheckBox showCalEventCheckBox;
		private TextView calNameTextView;
		private ImageView calColorView;
		ATLCalendarModel mCellData;

		public CalendarSelectCell(Context context, AttributeSet attrs,
				int defStyle) {
			super(context, attrs, defStyle);
			// TODO Auto-generated constructor stub
			initView(context);
		}

		public void setCellData(ATLCalendarModel cellData) {
			// TODO Auto-generated method stub
			this.mCellData = cellData;
			showCalEventCheckBox.setChecked(mCellData.isActive);

			String calName = cellData.name;
			if (cellData.name.length() > 25) {
				calName = calName.substring(0, 23) + "...";
			}
			calNameTextView.setText(calName);
			calColorView.setBackgroundColor(cellData.color);
		}

		public CalendarSelectCell(Context context, AttributeSet attrs) {
			super(context, attrs);
			// TODO Auto-generated constructor stub
			initView(context);
		}

		public CalendarSelectCell(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			initView(context);
		}

		private void initView(Context context) {
			// TODO Auto-generated method stub
			LayoutInflater.from(context).inflate(R.layout.calendar_select_cell,
					this, true);
			showCalEventCheckBox = (CheckBox) findViewById(R.id.calendar_select_checkbox);
			calNameTextView = (TextView) findViewById(R.id.calendar_select_calendar_name);
			calColorView = (ImageView) findViewById(R.id.calendar_select_color_img);

			showCalEventCheckBox.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mCellData.isActive = !mCellData.isActive;
					saveCalendarList();
					loadingAllCalendar();
				}
			});
		}

	}

	// /////////////////////////////////////////////////
	// CalendarSelectCell END
	// /////////////////////////////////////////////////

}