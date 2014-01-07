/**
 * 
 */
package com.atlasapp.section_alerts;

import java.util.List;

import com.atlasapp.common.ATLColor;
import com.atlasapp.common.ATLConstants.AlertType;
import com.atlasapp.section_appentry.R;
import com.atlasapp.section_main.ATLMultiSectionListView;
import com.atlasapp.section_tasks.ATLTaskCellData;
import com.parse.ParseObject;
import com.atlasapp.section_alerts.ATLTaskRequestCellDelegete;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.BufferType;


public class AlertTaskRequest extends Activity implements OnClickListener, ATLTaskRequestCellDelegete, ATLAlertWebAccessCallBackInterface{
	
	private ATLAlertTaskRequestCellList mTaskCellList;
	private AlertCellData alertCellData;
	private ATLAlertTaskRequestListAdapter adapter;
	private ATLMultiSectionListView listTask;
	private ImageButton acceptBtn;
	private ImageButton declineBtn;
	private ImageButton decideLaterBtn;
	private TextView descriptionTextView;
	private String alertCellSenderName = "No Name";
	private String senderId = "";
	private String cellId = "";
	private int lastShowAcceptButton = -1;
	private int currentIndex = 0;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_task_request);
		senderId = getIntent().getExtras().getString("senderId");
		alertCellSenderName = getIntent().getExtras().getString("senderName");
		cellId = getIntent().getExtras().getString("alertCellAtlasId");
		initAttributes();
		setListener();
		bindingData();
	}

	

	private void initAttributes() {
		// TODO Auto-generated method stub
		acceptBtn = (ImageButton) findViewById(R.id.acceptBtn);
		declineBtn = (ImageButton) findViewById(R.id.declineBtn);
		decideLaterBtn = (ImageButton) findViewById(R.id.cancelBtn);
		
		descriptionTextView = (TextView) findViewById(R.id.alerts_task_request_description_textview);
		
		listTask = (ATLMultiSectionListView) findViewById(R.id.listTask);
		listTask.setPinnedHeaderView(LayoutInflater.from(this).inflate(
				R.layout.listview_header, listTask, false));
	}
	
	private void setListener() {
		// TODO Auto-generated method stub
		acceptBtn.setOnClickListener(this);
		declineBtn.setOnClickListener(this);
		decideLaterBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == acceptBtn){
			sendTaskAcceptToServerWithReply(true);
		}else if(v == decideLaterBtn){
			finish();
			overridePendingTransition(R.anim.stand_by, R.anim.out_to_bottom);
		}else if(v== declineBtn){
			sendTaskAcceptToServerWithReply(false);
		}
	}
	
	public void bindingData() {
		//TODO Load All alerts from database to alerts view
		// this is the text we'll be operating on  
        String key = "The above Tasks was assigned to you by ";
        SpannableString title = new SpannableString(key + alertCellSenderName + ".");
        // make "key" (characters 0 to key.length()) White  
        title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, key.length(), 0);
        // make "alertCellSenderName" (characters 0 to key.length()) White Bold
        title.setSpan(new ForegroundColorSpan(ATLColor.blue_event_title), key.length(),key.length()+ alertCellSenderName.length(), 0);
        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),key.length(),key.length()+ alertCellSenderName.length(), 0);  
		descriptionTextView.setText(title, BufferType.SPANNABLE);
		
		
		//Handle show the first Accept button View
		mTaskCellList = new ATLAlertTaskRequestCellList(cellId);
		adapter = new ATLAlertTaskRequestListAdapter(mTaskCellList, this);
		int size = mTaskCellList.alertListCount; 
		for(int i = 0; i <size; i++){
			AlertCellData currentCell = mTaskCellList.alertListArray.get(i);
			if(currentCell.alertCellAtlasId.equals(cellId)){
				currentIndex = i;
				currentCell.isShowAcceptBtn = true;
				break;
			}
		}
		lastShowAcceptButton = currentIndex;
		
		listTask.setAdapter(adapter);
		
		listTask.smoothScrollToPositionFromTop(currentIndex, 0);
		
		for(AlertCellData cellData:mTaskCellList.alertListArray){
			alertCellData = cellData;
			break;
		}
	}



	@Override
	public void didChangeIsComplete(AlertCellData cellData) {
		// TODO Auto-generated method stub
		//Handle Accept task here
		Log.v("AlertTaskRequest", "Handle didChangeIsComplete task here");
	}

	@Override
	public void didShowAcceptTaskAtIndex(int index) {
		// TODO Auto-generated method stub
		Log.v("Task", "index: " + index);

		if (lastShowAcceptButton != -1) {
			// Edit old data
			AlertCellData cellOldData = (AlertCellData) listTask
					.getAdapter().getItem(lastShowAcceptButton);
			// cellOldData.isShowOffHour = false;
			// Edit old view
			int firstVisible = listTask.getFirstVisiblePosition();
			int lastVisible = listTask.getLastVisiblePosition();
			Log.v("Tasks", "lastShowAcceptButton: " + lastShowAcceptButton);
			Log.v("Tasks", "firstVisible: " + firstVisible);
			if (firstVisible > lastShowAcceptButton
					|| lastShowAcceptButton > lastVisible) {
				cellOldData.isShowAcceptBtn = false;
			} else if (firstVisible <= lastShowAcceptButton
					&& lastShowAcceptButton <= lastVisible) {
				if (lastShowAcceptButton - firstVisible != index
						&& lastShowAcceptButton != index
						&& cellOldData.isShowAcceptBtn) {
					ATLTaskRequestCell cellOld = (ATLTaskRequestCell) listTask
							.getChildAt(lastShowAcceptButton - firstVisible);
					cellOld.dismissAcceptTaskButton();
					// Toast.makeText(this, "dismissDeleteTaskButton ",
					// Toast.LENGTH_SHORT).show();
				}
			}
			lastShowAcceptButton = index;
		} else {
			// Do nothing
			lastShowAcceptButton = index;
		}
	}



	@Override
	public void didAcceptTaskAtIndex(AlertCellData cellData) {
		// TODO Auto-generated method stub
		//Handle Accept task here
		Log.v("AlertTaskRequest", "Handle Accept task here");
		sendTaskAcceptToServerWithReply(true);
	}
	
	public void sendTaskAcceptToServerWithReply(boolean reply){
//		for(AlertCellData cellData: mTaskCellList.alertListArray){
//			
//			
//			break;
//		}
		/* TODO send task accept to server with
		 * reply = true: Accepted
		 * reply = false: Decline
		 */
		if(reply){
			// Accepted
			alertCellData.isAccepted = true;
		}else{
			// Decline
			alertCellData.isAccepted = false;
		}
		
		// Parse server
		ATLAlertWebAccess taskAcceptWebAccess = new ATLAlertWebAccess();
		taskAcceptWebAccess.delegate = this;
		taskAcceptWebAccess.getPage_AsyncWithType(
				AlertType.taskAccepted_Sent, alertCellData);
		// End Parse server
		finish();
		overridePendingTransition(R.anim.stand_by, R.anim.out_to_bottom);
	}

	@Override
	public void didGetDataFinish(Object data, int alertType, String result) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void didPostDataFinish(Object data, int alertType, String result) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void didGetEventList(List<ParseObject> events, int alertType,
			String result) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void didGetSenderName() {
		// TODO Auto-generated method stub
		
	}


}
