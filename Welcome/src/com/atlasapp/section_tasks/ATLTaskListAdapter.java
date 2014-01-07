//  ==================================================================================================================
//  ATLTaskListAdapter.java
//  AtlasConsumerAndroid
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-10-28 TAN:    Init class
//					   implement ATLMultiSectionAdapter methods
//  ==================================================================================================================

package com.atlasapp.section_tasks;

import java.util.ArrayList;

import com.atlasapp.section_appentry.R;
import com.atlasapp.section_main.ATLMultiSectionAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ATLTaskListAdapter extends ATLMultiSectionAdapter {
	ATLTaskCellList mTaskCellList;
	Context mContext;
	int totalSize;

	public ATLTaskListAdapter(ATLTaskCellList atlTaskCellList, Context ctx) {
		// TODO Auto-generated constructor stub
		mContext = ctx;
		mTaskCellList = atlTaskCellList;
		mTaskCellList.load();
		totalSize = mTaskCellList.taskListArray.size();
	}

	@Override
	public int getCount() {
		return totalSize;
	}

	@Override
	public Object getItem(int position) {
		// return mTaskCellList.taskListArray.get(position);
		int section = this.getSectionForPosition(position);

		switch (ATLTaskSortSingleton.getGroupKind()) {
		case ATLTaskCellData.TASK_SORT_PRIORITY: {
			switch (section) {
			case 0:
				return mTaskCellList.taskPriorityHighArray.get(position);
			case 1:
				return mTaskCellList.taskPriorityMediumArray.get(position
						- mTaskCellList.taskPriorityHighArray.size());
			case 2:
				return mTaskCellList.taskPriorityLowArray
						.get(position
								- (mTaskCellList.taskPriorityHighArray.size() + mTaskCellList.taskPriorityMediumArray
										.size()));
			}
		}
			break;
		case ATLTaskCellData.TASK_SORT_CALENDAR:
			// int size = mTaskCellList.taskCalendarArray.size();
			// int []childSize = new int[size];
			// int index = 0;
			// for(ArrayList<ATLTaskCellData> cell :
			// mTaskCellList.taskCalendarArray){
			// childSize[index] = cell.size();
			// index++;
			// }
			// int numberOfSection = mTaskCellList.headerList.length;
			return mTaskCellList.taskListArray.get(position);

		case ATLTaskCellData.TASK_SORT_DUEDATE:
			switch (section) {
			case 0:
				return mTaskCellList.taskDueTodayArray.get(position);
			case 1:
				return mTaskCellList.taskDueTomorrowArray.get(position
						- mTaskCellList.taskDueTodayArray.size());
			case 2:
				return mTaskCellList.taskDueFutureArray
						.get(position
								- (mTaskCellList.taskDueTodayArray.size() + mTaskCellList.taskDueTomorrowArray
										.size()));
			}
			break;
		case ATLTaskCellData.TASK_SORT_DELEGATED:
			return mTaskCellList.taskListArray.get(position);
		}

		return null;
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
		TaskCell cell;
		if (convertView == null) {
			cell = new TaskCell(mContext);
			convertView = (View) cell;
			convertView.setTag(cell);
		} else {
			cell = (TaskCell) convertView.getTag();
		}
		ATLTaskCellData cellData = (ATLTaskCellData) this.getItem(position);
		cell.cellIndex = position;
		cell.setCellData(cellData);

		// cell.setOnClickListener(cell);// Set listener for it-self to handle
		// tap events

		return convertView;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
		TextView lSectionHeader = (TextView) header;
		lSectionHeader.setText(getSections()[getSectionForPosition(position)]);
	}

	@Override
	public int getPositionForSection(int section) {
		switch (ATLTaskSortSingleton.getGroupKind()) {
		case ATLTaskCellData.TASK_SORT_PRIORITY: {
			switch (section) {
			case 0:
				return 0;
			case 1: {
				return mTaskCellList.taskPriorityHighArray.size();
			}
			case 2:
				return mTaskCellList.taskPriorityHighArray.size()
						+ mTaskCellList.taskPriorityMediumArray.size();
			}
		}
			break;
		case ATLTaskCellData.TASK_SORT_CALENDAR:

			if (section == 0) {
				return 0;
			} else {
				int postion = 0;
				for (int j = 0; j < section; j++) {
					postion += mTaskCellList.taskCalendarArray.get(j).size();
				}
				return postion;
			}

		case ATLTaskCellData.TASK_SORT_DUEDATE:
			switch (section) {
			case 0:
				return 0;
			case 1: {
				return mTaskCellList.taskDueTodayArray.size();
			}
			case 2:
				return mTaskCellList.taskDueTodayArray.size()
						+ mTaskCellList.taskDueTomorrowArray.size();
			}
			break;
		case ATLTaskCellData.TASK_SORT_DELEGATED:
			if (section == 0) {
				return 0;
			} else {
				int postion = 0;
				for (int j = 0; j < section; j++) {
					postion += mTaskCellList.taskDelegatedNameArray.get(j)
							.size();
				}
				return postion;
			}
		}

		// switch (section) {
		// case 0:
		//
		// return 0;
		//
		// case 1: {
		// return mTaskCellList.taskListMustDoArray.size();
		// }
		// case 2: {
		// return mTaskCellList.taskListMustDoArray.size()
		// + mTaskCellList.taskListVeryImportantArray.size();
		// }
		// case 3: {
		// return mTaskCellList.taskListMustDoArray.size()
		// + mTaskCellList.taskListVeryImportantArray.size()
		// + mTaskCellList.taskListImportantArray.size();
		// }

		// }
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		switch (ATLTaskSortSingleton.getGroupKind()) {
		case ATLTaskCellData.TASK_SORT_PRIORITY: {
			if (position >= mTaskCellList.taskPriorityHighArray.size()
					+ mTaskCellList.taskPriorityMediumArray.size()) {
				return 2;
			} else if (position >= mTaskCellList.taskPriorityHighArray.size()) {
				return 1;
			} else {
				return 0;
			}
		}

		case ATLTaskCellData.TASK_SORT_CALENDAR: {
			int numberOfSection = mTaskCellList.headerList.length;
			int[] childSize = new int[numberOfSection];
			int index = 0;
			for (ArrayList<ATLTaskCellData> cell : mTaskCellList.taskCalendarArray) {
				childSize[index] = cell.size();
				index++;
			}

			int[] sumToIndex = new int[numberOfSection];
			sumToIndex[0] = 0;
			for (int i = numberOfSection - 1; i > 0; i--) {
				for (int j = 0; j < i; j++) {
					sumToIndex[i] += childSize[j];
				}
			}

			for (int idx = numberOfSection - 1; idx >= 0; idx--) {
				if (position >= sumToIndex[idx]) {
					return idx;
				}
			}
		}

		case ATLTaskCellData.TASK_SORT_DUEDATE:
			if (position >= mTaskCellList.taskDueTodayArray.size()
					+ mTaskCellList.taskDueTomorrowArray.size()) {
				return 2;
			} else if (position >= mTaskCellList.taskDueTodayArray.size()) {
				return 1;
			} else {
				return 0;
			}
		case ATLTaskCellData.TASK_SORT_DELEGATED: {
			int numberOfSection = mTaskCellList.headerList.length;
			int[] childSize = new int[numberOfSection];
			int index = 0;
			for (ArrayList<ATLTaskCellData> cell : mTaskCellList.taskDelegatedNameArray) {
				childSize[index] = cell.size();
				index++;
			}

			int[] sumToIndex = new int[numberOfSection];
			sumToIndex[0] = 0;
			for (int i = numberOfSection - 1; i > 0; i--) {
				for (int j = 0; j < i; j++) {
					sumToIndex[i] += childSize[j];
				}
			}

			for (int idx = numberOfSection - 1; idx >= 0; idx--) {
				if (position >= sumToIndex[idx]) {
					return idx;
				}
			}
		}
		}
		// if (mTaskCellList.taskListSimulate) { // SIMULATE MODE - face 3 4
		// sections
		// if (position >= mTaskCellList.taskListMustDoArray.size()
		// + mTaskCellList.taskListVeryImportantArray.size()
		// + mTaskCellList.taskListImportantArray.size()) {
		// return 3;
		// } else if (position >= mTaskCellList.taskListMustDoArray.size()
		// + mTaskCellList.taskListVeryImportantArray.size()) {
		// return 2;
		// } else if (position >= mTaskCellList.taskListMustDoArray.size()) {
		// return 1;
		// } else if (position >= 0) {
		// return 0;
		// }
		// }

		return 0;
	}

	public final String HIGH = "High";
	public final String MEDIUM = "Medium";
	public final String LOW = "Low";
	public final String COMPLETED = "Completed";

	@Override
	public String[] getSections() {
		// if (mTaskCellList.taskListSimulate) { // SIMULATE MODE - face 3 4
		// sections
		// String[] res = new String[4];
		// res[0] = HIGH;
		// res[1] = MEDIUM;
		// res[2] = LOW;
		// res[3] = COMPLETED;

		return mTaskCellList.headerList;
		// }
		// return null;
	}

}
