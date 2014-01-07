//  ==================================================================================================================
//  ATLAlertListAdapter_2.java
//  ATLAS
//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================

package com.atlasapp.section_alerts;

import java.util.ArrayList;

import com.atlasapp.common.ATLEventCalendarModel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author Ryan Tan
 *
 */
public class ATLAlertListAdapter_2 extends BaseAdapter{
	
//	ATLAlertCellList_2 mAlertCellList = new ATLAlertCellList_2();
	ArrayList<ATLAlert> mAlertCellList = new ArrayList<ATLAlert>();
	Context mContext;
	int totalSize;

	public ATLAlertListAdapter_2(ArrayList<ATLAlert> altAlertsCellList, Context ctx){
		mContext = ctx;
		mAlertCellList = altAlertsCellList;
		totalSize = mAlertCellList.size();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return totalSize;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mAlertCellList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ATLAlertCell_2 cell;
		if (convertView == null) {
			cell = new ATLAlertCell_2(mContext);
			convertView = (View) cell;
			convertView.setTag(cell);
		} else {
			cell = (ATLAlertCell_2) convertView.getTag();
		}
		ATLAlert cellData = (ATLAlert) this.getItem(position);
		cell.setAlert(cellData);
		return convertView;
	}
	

}
