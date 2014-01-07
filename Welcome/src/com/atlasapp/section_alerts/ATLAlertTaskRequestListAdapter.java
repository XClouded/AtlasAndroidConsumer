//  ==================================================================================================================
//  ATLAlertTaskRequestListAdapter.java
//  ATLAS
//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================

package com.atlasapp.section_alerts;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atlasapp.section_appentry.R;
import com.atlasapp.section_main.ATLMultiSectionAdapter;

public class ATLAlertTaskRequestListAdapter extends ATLMultiSectionAdapter {
	ATLAlertTaskRequestCellList mAlertCellList = null;
	Context mContext;
	int totalSize;

	public ATLAlertTaskRequestListAdapter(ATLAlertTaskRequestCellList altAlertsCellList, Context ctx) {
		// TODO Auto-generated constructor stub
		mContext = ctx;
		mAlertCellList = altAlertsCellList;
		mAlertCellList.load();
		totalSize = mAlertCellList.alertListCount;
	}

	@Override
	public int getCount() {
		return totalSize;
	}

	@Override
	public Object getItem(int position) {
		return mAlertCellList.alertListArray.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	protected void onNextPageRequested(int page) {

	}

	@Override
	protected void bindSectionHeader(View view, int position,
			boolean displaySectionHeader) {
		if (displaySectionHeader) {
			view.findViewById(R.id.header).setVisibility(View.VISIBLE);
			TextView lSectionTitle = (TextView) view.findViewById(R.id.header);
			lSectionTitle
					.setText(getSections()[getSectionForPosition(position)]);
		} else {
			view.findViewById(R.id.header).setVisibility(View.GONE);
		}
	}

	@Override
	public View getAmazingView(int position, View convertView, ViewGroup parent) {
		ATLTaskRequestCell cell;
		if (convertView == null) {
			cell = new ATLTaskRequestCell(mContext);
			convertView = (View) cell;
			convertView.setTag(cell);
		} else {
			cell = (ATLTaskRequestCell) convertView.getTag();
		}
		AlertCellData cellData = (AlertCellData) this.getItem(position);
		cellData.cellIndex = position;
		cell.setCellData(cellData);
		
		return convertView;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
		TextView lSectionHeader = (TextView) header;
		lSectionHeader.setText(getSections()[getSectionForPosition(position)]);
	}

	@Override
	public int getPositionForSection(int section) {
		
		switch(section){
		case 0:
			return 0;
		case 1:
			return mAlertCellList.taskAlertHighListArray.size();
		case 2:
			return mAlertCellList.taskAlertHighListArray.size()  
					+ mAlertCellList.taskAlertMediumListArray.size();
		default:
			return 0;
		}
	}

	@Override
	public int getSectionForPosition(int position) {
		
		if (position >= mAlertCellList.taskAlertHighListArray.size()  
				+ mAlertCellList.taskAlertMediumListArray.size()) {
			return 2;
		} else if(position >= mAlertCellList.taskAlertHighListArray.size()){
			return 1;
		}
		else{
			return 0;
		}
		
	}

	@Override
	public String[] getSections() {
			String[] res = new String[3];
			res[0] = "High";
			res[1] = "Medium";
			res[2] = "Low";
			return res;
	}

}