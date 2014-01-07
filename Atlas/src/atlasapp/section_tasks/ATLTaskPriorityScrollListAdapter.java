//  ==================================================================================================================
//  ATLTaskPriorityScrollListAdapter.java
//  ATLAS
//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-12-21 TAN:     Create class
//  ==================================================================================================================

package atlasapp.section_tasks;

import java.util.ArrayList;

import kankan.wheel.widget.adapters.WheelViewAdapter;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import atlasapp.common.CalendarUtilities;
import atlasapp.section_appentry.R;


public class ATLTaskPriorityScrollListAdapter implements WheelViewAdapter {

	public static final int TASK_PRIORITY_INDEX_LOW = 2;
	public static final int TASK_PRIORITY_INDEX_HIGH = 0;
	public static final int TASK_PRIORITY_INDEX_MEDIUM = 1;
	
	int size = 3; // {High, Medium, Low}
	Context mContext;
	int currentColor;
	private TaskPriorityCell cell0;
	private TaskPriorityCell cell1;
	private TaskPriorityCell cell2;
	private ArrayList<TaskPriorityCell> cellArray;
	public ATLTaskPriorityScrollListAdapter(Context ctx, int currColor) {
		// TODO Auto-generated constructor stub
		mContext = ctx;
		currentColor = currColor;
		cell0 = new TaskPriorityCell(mContext);
		cell0.setData(TASK_PRIORITY_INDEX_LOW, currColor);
		cell1 = new TaskPriorityCell(mContext);
		cell1.setData(TASK_PRIORITY_INDEX_HIGH, currColor);
		cell2 = new TaskPriorityCell(mContext);
		cell2.setData(TASK_PRIORITY_INDEX_MEDIUM, currColor);
		cellArray = new ArrayList<TaskPriorityCell>();
		cellArray.add(cell0);
		cellArray.add(cell1);
		cellArray.add(cell2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kankan.wheel.widget.adapters.WheelViewAdapter#getItemsCount()
	 */
	@Override
	public int getItemsCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kankan.wheel.widget.adapters.WheelViewAdapter#getItem(int,
	 * android.view.View, android.view.ViewGroup)
	 */


	@Override
	public View getItem(int index, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
//		TaskPriorityCell cell;
//		if (convertView == null) {
//			// cell = new CalendarCell(mContext);
//			cell = new TaskPriorityCell(mContext);
//			convertView = (View) cell;
//			convertView.setTag(cell);
//		} else {
//			cell = (TaskPriorityCell) convertView.getTag();
//		}
//		cell.setData(index, currentColor);
		
//		return convertView;
		return (View) cellArray.get(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kankan.wheel.widget.adapters.WheelViewAdapter#getEmptyItem(android.view
	 * .View, android.view.ViewGroup)
	 */
	@Override
	public View getEmptyItem(View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kankan.wheel.widget.adapters.WheelViewAdapter#registerDataSetObserver
	 * (android.database.DataSetObserver)
	 */
	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kankan.wheel.widget.adapters.WheelViewAdapter#unregisterDataSetObserver
	 * (android.database.DataSetObserver)
	 */
	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	private class TaskPriorityCell extends RelativeLayout {
		ImageView taskPriorityImg;

		public TaskPriorityCell(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			initView(context);

		}

		public TaskPriorityCell(Context context, AttributeSet attrs,
				int defStyle) {
			super(context, attrs, defStyle);
			// TODO Auto-generated constructor stub
			initView(context);
		}

		public TaskPriorityCell(Context context, AttributeSet attrs) {
			super(context, attrs);
			// TODO Auto-generated constructor stub
			initView(context);
		}

		private void initView(Context context) {
			// TODO Auto-generated method stub
			LayoutInflater.from(context).inflate(
					R.layout.task_priority_cell, this, true);
			taskPriorityImg = (ImageView) findViewById(R.id.task_priority_cell_img);
		}

		public void setData(int index, int newColor) {
			int resourceID = 0;
			switch (index) {
			case TASK_PRIORITY_INDEX_LOW:
				resourceID  = R.drawable.tasks_flame_blue;
				break;
			case TASK_PRIORITY_INDEX_HIGH:
				resourceID  = R.drawable.tasks_very_important_blue;
				break;
			case TASK_PRIORITY_INDEX_MEDIUM:
				resourceID  = R.drawable.tasks_important_blue;
				break;

			}
			Bitmap sourceBitmap = BitmapFactory.decodeResource(getResources(), resourceID);
			CalendarUtilities.setImageColor(taskPriorityImg ,sourceBitmap, newColor);
		}

	}

}
