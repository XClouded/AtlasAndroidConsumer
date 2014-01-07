//  ==================================================================================================================
//  ATLCalendarScrollListAdapter.java
//  ATLAS
//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================

package atlasapp.section_calendar;

import java.util.ArrayList;
import java.util.List;

import atlasapp.model.ATLCalendarModel;
import atlasapp.section_appentry.R;

import android.R.integer;
import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import kankan.wheel.widget.adapters.WheelViewAdapter;

/**
 * @author Ryan Tan
 * 
 */
public class ATLCalendarScrollListAdapter implements WheelViewAdapter {

	int size = 0;
	Context mContext;
	List<ATLCalendarModel> mCalList = new ArrayList<ATLCalendarModel>();

	public ATLCalendarScrollListAdapter(List<ATLCalendarModel> calList,
			Context ctx) {
		// TODO Auto-generated constructor stub
		size = calList.size();
		mContext = ctx;
		mCalList = calList;
	}

	@Override
	public int getItemsCount() {
		// TODO Auto-generated method stub
		return size;
	}

	@Override
	public View getItem(int index, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		CalendarScrollCell cell;
		if (convertView == null) {
			// cell = new CalendarCell(mContext);
			cell = new CalendarScrollCell(mContext);
			convertView = (View) cell;
			convertView.setTag(cell);
		} else {
			cell = (CalendarScrollCell) convertView.getTag();
		}
		cell.setData(mCalList.get(index));
		return convertView;
	}

	@Override
	public View getEmptyItem(View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	private class CalendarScrollCell extends RelativeLayout {
		ImageView calendarColorImg;
		TextView calendarName;

		public CalendarScrollCell(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			initView(context);

		}

		public CalendarScrollCell(Context context, AttributeSet attrs,
				int defStyle) {
			super(context, attrs, defStyle);
			// TODO Auto-generated constructor stub
			initView(context);
		}

		public CalendarScrollCell(Context context, AttributeSet attrs) {
			super(context, attrs);
			// TODO Auto-generated constructor stub
			initView(context);
		}

		private void initView(Context context) {
			// TODO Auto-generated method stub
			LayoutInflater.from(context).inflate(
					R.layout.calendar_scroll_list_cell, this, true);
			calendarColorImg = (ImageView) findViewById(R.id.cal_scroll_list_color_img);
			calendarName = (TextView) findViewById(R.id.cal_scroll_list_name_text);
		}

		private void setData(ATLCalendarModel cellData) {
			calendarColorImg.setBackgroundColor(cellData.color);
			String name = cellData.name.length() > 20 ? cellData.name
					.substring(0, 20) + "..." : cellData.name;
			calendarName.setText(name);
		}

	}

}
