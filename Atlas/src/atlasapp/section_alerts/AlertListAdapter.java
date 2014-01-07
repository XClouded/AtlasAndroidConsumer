//  ==================================================================================================================
//  AlertListAdapter.java
//  AtlasConsumerAndroid
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================

package atlasapp.section_alerts;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import atlasapp.model.ATLEventCalendarModel;
import atlasapp.section_appentry.R;
import atlasapp.section_main.ATLMultiSectionAdapter;



public class AlertListAdapter extends ATLMultiSectionAdapter {
	AlertCellList mAlertCellList = new AlertCellList();
	Context mContext;
	int totalSize;

	public AlertListAdapter(AlertCellList altAlertsCellList, Context ctx) {
		// TODO Auto-generated constructor stub
		mContext = ctx;
		mAlertCellList = altAlertsCellList;
		mAlertCellList.load();
		totalSize = mAlertCellList.alertListCount;
	}
	public AlertListAdapter(Context ctx) {
		// TODO Auto-generated constructor stub
		mContext = ctx;
		mAlertCellList = new AlertCellList();
		mAlertCellList.load();
		totalSize = mAlertCellList.alertListCount;
	}

	@Override
	public int getCount() {
		return totalSize;
	}

	@Override
	public Object getItem(int position) {
		/*
		if (position < mAlertCellList.newAlertListArray.size()) {
			return mAlertCellList.newAlertListArray.get(position);
		} else {
			return mAlertCellList.pastAlertListArray.get(position
					- mAlertCellList.newAlertListArray.size());
		}
		*/
return mAlertCellList.alertList.get(position);
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
		AlertCell cell;
		if (convertView == null) {
			cell = new AlertCell(mContext);
			convertView = (View) cell;
			convertView.setTag(cell);
		} else {
			cell = (AlertCell) convertView.getTag();
		}
		ATLEventCalendarModel cellData = (ATLEventCalendarModel) this.getItem(position);
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

		return 0;
/*
		if (section == 0) {
			return 0;
		} else {
			int postion = mAlertCellList.newAlertListArray.size();
			// for (int i = 0; i < section; i++) {
			// postion += mAlertCellList.alertListArray
			// .size();
			// }
			return postion;
		}
	*/	
	}

	@Override
	public int getSectionForPosition(int position) {

		return 0;
		/*
		if (position >= this.mAlertCellList.newAlertListArray.size()) {
			return 1;
		} else {
			return 0;
		}
		*/
	}

	@Override
	public String[] getSections() {
		String[] res = new String[1];
		res[0] = "Your Move";
		return res;
		/*
		String[] res = new String[2];
		res[0] = "New";
		res[1] = "Past";
		return res;
		*/
	}

}