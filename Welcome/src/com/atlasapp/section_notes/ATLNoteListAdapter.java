//  ==================================================================================================================
//  ATLNotesListAdapter.java
//  AtlasConsumerAndroid
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-10-29 TAN:     init class to implement Notes View in simulate mode
//  ==================================================================================================================

package com.atlasapp.section_notes;

import java.util.ArrayList;

import com.atlasapp.section_appentry.R;
import com.atlasapp.section_main.ATLMultiSectionAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ATLNoteListAdapter extends ATLMultiSectionAdapter {
	ATLNoteList mNoteCellList;
	Context mContext;
	int totalSize;
	String[] headerList;
	public ATLNoteListAdapter(ATLNoteList noteCellList, Context ctx) {
		// TODO Auto-generated constructor stub
		mContext = ctx;
		mNoteCellList = noteCellList;
		mNoteCellList.load();
		calculateTotalSize();

		headerList = new String[mNoteCellList.headerList.size()];
		headerList = mNoteCellList.headerList.toArray(headerList);
	}

	private void calculateTotalSize() {
		totalSize = 0;
		for(ArrayList<ATLNoteCellData> noteList : mNoteCellList.noteListArray){
			totalSize += noteList.size();
		}
	}

	@Override
	public int getCount() {
		return totalSize;
	}

	@Override
	public Object getItem(int position) {
		return mNoteCellList.noteListArray.get(this
				.getSectionForPosition(position)).get(this.getPositionInSectionForPosition(position));
	}

	@Override
	public long getItemId(int position) {
		return position;
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
		ATLNoteCell cell;
		if (convertView == null) {
			cell = new ATLNoteCell(mContext);
			convertView = (View) cell;
			convertView.setTag(cell);
		} else {
			cell = (ATLNoteCell) convertView.getTag();
		}
		ATLNoteCellData cellData = (ATLNoteCellData) this.getItem(position);
		cell.setCellData(cellData);
		cell.cellIndex = position;
		return convertView;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
		TextView lSectionHeader = (TextView) header;
		lSectionHeader.setVisibility(View.GONE);
		 lSectionHeader.setText(getSections()[getSectionForPosition(position)]);
	}

	@Override
	public int getPositionForSection(int section) {
		if (section == 0) {
			return 0;
		} else {
			int postion = 0;
			for (int i = 0; i < section; i++) {
				postion += mNoteCellList.noteListArray.get(i)
						.size();
			}
			return postion;
		}
	}

	public int getPositionInSectionForPosition(int position) {
		int numberOfSection = mNoteCellList.headerList.size();
		int[] childSize = new int[numberOfSection];
		int index = 0;
		for (ArrayList<ATLNoteCellData> cell : mNoteCellList.noteListArray) {
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
				return position-sumToIndex[idx];
			}
		}
		return 0;

	}

	@Override
	public int getSectionForPosition(int position) {
		// Return Section number when we have position

			int numberOfSection = mNoteCellList.headerList.size();
			int[] childSize = new int[numberOfSection];
			int index = 0;
			for (ArrayList<ATLNoteCellData> cell : mNoteCellList.noteListArray) {
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

		return 0;
	}

	@Override
	public String[] getSections() {
		return this.headerList;
	}

	@Override
	protected void onNextPageRequested(int page) {
		// TODO Auto-generated method stub
		
	}
}
