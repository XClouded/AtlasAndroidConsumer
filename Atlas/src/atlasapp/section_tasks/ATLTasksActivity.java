//  ==================================================================================================================
//  ATLTasksActivity.java
//  AtlasConsumerAndroid
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-10-21 TAN:     Init class
//  2012-10-28 TAN: 	Add listTask and binding data
//  ==================================================================================================================
package atlasapp.section_tasks;



import atlasapp.section_appentry.R;
import atlasapp.section_main.ATLMainTabbarActivity;
import atlasapp.section_main.ATLMultiSectionListView;
import atlasapp.section_main.ATLTopbar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class ATLTasksActivity extends Activity {
	LayoutInflater mInflater;
	Context mContext;
	View mLayout;
	ATLTopbar mTopBar;
	ATLMultiSectionListView listTask;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater = LayoutInflater.from(this);
		mLayout = (View)mInflater.inflate(R.layout.tasks2, null);
		setContentView(mLayout);
		initAttributes();
		setListener();
		bindingData();
	}

	

	private void initAttributes() {
		// TODO Auto-generated method stub
		mTopBar = (ATLTopbar)findViewById(R.id.topBarMenu);
		mTopBar.setType(ATLMainTabbarActivity.TAB_TASKS);
		listTask = (ATLMultiSectionListView) findViewById(R.id.listTask);
		listTask.setPinnedHeaderView(LayoutInflater.from(this).inflate(R.layout.listview_header, listTask, false));
	}
	
	private void bindingData() {
		// TODO Auto-generated method stub
		ATLTaskListAdapter adaper = new ATLTaskListAdapter(new ATLTaskCellList(), this);
		listTask.setAdapter(adaper);
	}

	private void setListener() {
		// TODO Auto-generated method stub
		
	}
}
