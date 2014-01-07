//  ==================================================================================================================
//  ATLTodayListAdapter.java
//  AtlasConsumerAndroid
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-10-29 TAN:     Inti class, implement @override methods of ATLMultiSectionAdapter
//  ==================================================================================================================

package atlasapp.section_today;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import atlasapp.section_calendar.ATLCalCellData;
import atlasapp.section_calendar.CalendarCell;
import atlasapp.section_main.ATLMultiSectionAdapter;
import atlasapp.section_notes.ATLNoteCell;
import atlasapp.section_notes.ATLNoteCellData;
import atlasapp.section_tasks.ATLTaskCellData;
import atlasapp.section_tasks.TaskCell;
import atlasapp.section_appentry.R;




public class ATLTodayListAdapter extends ATLMultiSectionAdapter {
	ATLTodayCellList mTodayCellList;
	Context mContext;
	int totalSize;
	ATLNoteCell notesCell;
	CalendarCell calCell;
	TaskCell taskCell;

	public ATLTodayListAdapter(ATLTodayCellList aTodayCellList, Context ctx) {
		// TODO Auto-generated constructor stub
		mContext = ctx;

		notesCell = new ATLNoteCell(mContext);
		calCell = new CalendarCell(mContext);
		taskCell = new TaskCell(mContext);

		mTodayCellList = aTodayCellList;
		mTodayCellList.load();

		totalSize = mTodayCellList.todayListCount;
	}

	@Override
	public int getCount() {
		return totalSize;
	}

	@Override
	public Object getItem(int position) {
		return mTodayCellList.todayListArray.get(position);
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

		View cell = new View(mContext);

		Object cellData = this.getItem(position);

		if (convertView == null) {
			if (cellData instanceof ATLNoteCellData) {
				cell = new ATLNoteCell(mContext);
			} else if (cellData instanceof ATLCalCellData) {
				cell = new CalendarCell(mContext);
			} else if (cellData instanceof ATLTaskCellData) {
				cell = new TaskCell(mContext);
			}
			convertView = (View) cell;
			convertView.setTag(cell);
		} else {
			if (cellData instanceof ATLNoteCellData) {
				if (convertView instanceof ATLNoteCell) {
					cell = (ATLNoteCell) convertView.getTag();
				} else {
					cell = new ATLNoteCell(mContext);
					convertView = (View) cell;
					convertView.setTag(cell);
				}
			} else if (cellData instanceof ATLCalCellData) {
				if (convertView instanceof CalendarCell) {
					cell = (CalendarCell) convertView.getTag();
				} else {
					cell = new CalendarCell(mContext);
					convertView = (View) cell;
					convertView.setTag(cell);
				}

			} else if (cellData instanceof ATLTaskCellData) {
				if (convertView instanceof TaskCell) {
					cell = (TaskCell) convertView.getTag();
				} else {
					cell = new TaskCell(mContext);
					convertView = (View) cell;
					convertView.setTag(cell);
				}

			}
		}

		if (cellData instanceof ATLNoteCellData) {
			((ATLNoteCell) cell).setCellData((ATLNoteCellData) cellData);
		} else if (cellData instanceof ATLCalCellData) {
			((CalendarCell) cell).setCellData((ATLCalCellData) cellData);
		} else if (cellData instanceof ATLTaskCellData) {
			((TaskCell) cell).setCellData((ATLTaskCellData) cellData);
		}

		return convertView;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
		TextView lSectionHeader = (TextView) header;
		lSectionHeader.setText(getSections()[getSectionForPosition(position)]);
	}

	@Override
	public int getPositionForSection(int section) {
		switch (section) {
		case 0:

			return 0;

		case 1: {
			return mTodayCellList.mCalCellList.calEventsListArray.size();
		}
		case 2: {
			return mTodayCellList.mCalCellList.calEventsListArray.size()
					+ mTodayCellList.mTaskCellList.taskListCount;
		}

		}
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		if (position >= (mTodayCellList.mCalCellList.calEventsListArray.size() + mTodayCellList.mTaskCellList.taskListCount)) {
			return 2;
		} else if (position >= mTodayCellList.mCalCellList.calEventsListArray
				.size()) {
			return 1;
		} else if (position >= 0) {
			return 0;
		}
		return 0;
	}

	@Override
	public String[] getSections() {
		if (mTodayCellList.todayListSimulate) {
			String[] res = new String[3];
			res[0] = "Events";
			res[1] = "Tasks";
			res[2] = "Notes";
			return res;
		}
		return null;
	}

}