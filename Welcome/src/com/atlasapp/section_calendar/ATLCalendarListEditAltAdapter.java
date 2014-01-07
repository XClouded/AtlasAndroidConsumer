//  ==================================================================================================================
//  ATLCalendarListAdapter.java
//  AtlasConsumerAndroid
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-10-23 TAN:    Create class
//  ==================================================================================================================

package com.atlasapp.section_calendar;



import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

public class ATLCalendarListEditAltAdapter extends BaseAdapter{
	ATLCalCellList mCalCellList;
	int size;
	Context mContext;
	OnClickListener onClickListener;
	
//	public OnClickListener getOnClickListener() {
//		return onClickListener;
//	}
//
//	public void setOnClickListener(OnClickListener onClickListener) {
//		this.onClickListener = onClickListener;
//	}

	public ATLCalendarListEditAltAdapter(ATLCalCellList calCellList, Context ctx) {

		// TODO Auto-generated constructor stub
		mCalCellList = calCellList;
		mContext = ctx;
		mCalCellList.load(ctx);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		size = mCalCellList.getCalListArray().size();
		// Hard code for basic view phase
		return size > 24 ? size : 24;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mCalCellList.getCalListArray().get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		CalendarEditAltCell cell;
		if (convertView == null) {
			cell = new CalendarEditAltCell(mContext);
			convertView = (View)cell;
			convertView.setTag(cell);
		}
		else{
			cell = (CalendarEditAltCell)convertView.getTag();
		}
		ATLCalCellData cellData = (ATLCalCellData) mCalCellList.getCalListArray().get(position);
		cell.index = position;
		cell.setCellData(cellData);
		
		return convertView;
	}

	
	

}
