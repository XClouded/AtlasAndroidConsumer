//  ==================================================================================================================
//  ATLAddBarView.java
//  ATLAS
//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================

package atlasapp.common;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import atlasapp.section_calendar.ATLCalCellData;
import atlasapp.section_calendar.ATLCalendarIntentKeys;
import atlasapp.section_calendar.CalendarCell;
import atlasapp.section_calendar.CalendarEditView;
import atlasapp.section_calendar.CalendarEventSingleton;
import atlasapp.section_tasks.ATLTaskIntentKeys;
//import atlasapp.section_tasks.ATLTaskListActivity;
import atlasapp.section_tasks.EditTask;
import atlasapp.section_tasks.EditTaskModel;
import atlasapp.section_appentry.R;

/**
 * @author Ryan Tan
 * 
 */
public class ATLAddBarView extends RelativeLayout implements
		View.OnClickListener {

	Activity mActivity;
	int currentIdx = -1;
	public ImageButton contactImgBtn;
	public ImageButton calendarImgBtn;
	public ImageButton atlasImgBtn;
	public ImageButton tasksImgBtn;
	public TextView primaryLabel;
	public TextView secondaryLabel;

	private ArrayList<View> arrTabIcon = new ArrayList<View>();
	private float startX;
	private float startY;
	private float calX;
	private float calY;
	private float contX;
	private float contY;
	private float taskX;
	private float taskY;

//	public ATLTabbarViewDelegete delegate;

	public ATLAddBarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public ATLAddBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public ATLAddBarView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	private void initView(Context context) {
		// Init CallendarCell view
		LayoutInflater.from(context).inflate(R.layout.addbar, this,
				true);
		mActivity = (Activity) context;
		atlasImgBtn = (ImageButton) findViewById(R.id.imageButton6);
		contactImgBtn = (ImageButton) findViewById(R.id.imageButton2);
		calendarImgBtn = (ImageButton) findViewById(R.id.imageButton1);
		tasksImgBtn = (ImageButton) findViewById(R.id.imageButton3);
		
		arrTabIcon.add(contactImgBtn);
		arrTabIcon.add(calendarImgBtn);
		arrTabIcon.add(tasksImgBtn);

		contactImgBtn.setVisibility(View.INVISIBLE);
		calendarImgBtn.setVisibility(View.INVISIBLE);
		tasksImgBtn.setVisibility(View.INVISIBLE);

		atlasImgBtn.setOnClickListener(this);
		for (View v : arrTabIcon) {
			v.setOnClickListener(this);
		}
	}
	
	Animation contactsInPathAnimation(float sX, float sY){
		AnimationSet animationSet = new AnimationSet(true);
		RotateAnimation rotate_360 = new RotateAnimation(0, 720,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotate_360.setDuration(200);
		rotate_360.setInterpolator(new LinearInterpolator());

		Animation step1 = new TranslateAnimation(Animation.ABSOLUTE, sX,
				Animation.ABSOLUTE, 0, Animation.ABSOLUTE, sY,
				Animation.ABSOLUTE, 0);
		step1.setDuration(200);
		step1.setInterpolator(new LinearInterpolator());

		animationSet.addAnimation(rotate_360);
		animationSet.addAnimation(step1);

		return animationSet;

	}
	
	
	Animation calendarInPathAnimation(float sX, float sY){
		AnimationSet animationSet = new AnimationSet(true);
		RotateAnimation rotate_360 = new RotateAnimation(0, 720,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotate_360.setDuration(400);
		rotate_360.setInterpolator(new LinearInterpolator());

		Animation step1 = new TranslateAnimation(Animation.ABSOLUTE, 0,
				Animation.ABSOLUTE, 0, Animation.ABSOLUTE, sY,
				Animation.ABSOLUTE, 0);
		step1.setDuration(400);
		step1.setInterpolator(new LinearInterpolator());

		animationSet.addAnimation(rotate_360);
		animationSet.addAnimation(step1);

		return animationSet;

	}
	

	Animation tasksInPathAnimation(float sX, float sY){
		AnimationSet animationSet = new AnimationSet(true);
		RotateAnimation rotate_360 = new RotateAnimation(0, 720,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotate_360.setDuration(600);
		rotate_360.setInterpolator(new LinearInterpolator());

		Animation step1 = new TranslateAnimation(Animation.ABSOLUTE, sX,
				Animation.ABSOLUTE, 0, Animation.ABSOLUTE, sY,
				Animation.ABSOLUTE, 0);
		step1.setDuration(600);
		step1.setInterpolator(new LinearInterpolator());

		animationSet.addAnimation(rotate_360);
		animationSet.addAnimation(step1);

		return animationSet;

	}
	
	Animation contactsOutPathAnimation(float sX, float sY){
		AnimationSet animationSet = new AnimationSet(true);
		RotateAnimation rotate_360 = new RotateAnimation(0, 720,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotate_360.setDuration(200);
		rotate_360.setInterpolator(new LinearInterpolator());

		Animation step1 = new TranslateAnimation(Animation.ABSOLUTE, 0,
				Animation.ABSOLUTE, sX, Animation.ABSOLUTE, 0,
				Animation.ABSOLUTE, sY);
		step1.setDuration(200);
		step1.setInterpolator(new LinearInterpolator());

		animationSet.addAnimation(rotate_360);
		animationSet.addAnimation(step1);

		return animationSet;

	}
	
	
	Animation calendarOutPathAnimation(float sX, float sY){
		AnimationSet animationSet = new AnimationSet(true);
		RotateAnimation rotate_360 = new RotateAnimation(0, 720,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotate_360.setDuration(400);
		rotate_360.setInterpolator(new LinearInterpolator());

		Animation step1 = new TranslateAnimation(Animation.ABSOLUTE, 0,
				Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0,
				Animation.ABSOLUTE, sY);
		step1.setDuration(400);
		step1.setInterpolator(new LinearInterpolator());

		animationSet.addAnimation(rotate_360);
		animationSet.addAnimation(step1);

		return animationSet;

	}
	

	Animation tasksOutPathAnimation(float sX, float sY){
		AnimationSet animationSet = new AnimationSet(true);
		RotateAnimation rotate_360 = new RotateAnimation(0, 720,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotate_360.setDuration(600);
		rotate_360.setInterpolator(new LinearInterpolator());

		Animation step1 = new TranslateAnimation(Animation.ABSOLUTE, 0,
				Animation.ABSOLUTE, sX, Animation.ABSOLUTE, 0,
				Animation.ABSOLUTE, sY);
		step1.setDuration(600);
		step1.setInterpolator(new LinearInterpolator());

		animationSet.addAnimation(rotate_360);
		animationSet.addAnimation(step1);

		return animationSet;

	}

	private void showAllTabbarIcons() {
		
		startX = atlasImgBtn.getX();
		startY = atlasImgBtn.getY();
		calX   = calendarImgBtn.getX();
		calY   = calendarImgBtn.getY();
		contX  = contactImgBtn.getX();
		contY  = contactImgBtn.getY();
		taskX  = tasksImgBtn.getX();
		taskY  = tasksImgBtn.getY();
		
		calendarImgBtn.startAnimation(calendarInPathAnimation(startX, startY - calY));
		contactImgBtn.startAnimation(contactsInPathAnimation(startX - contX, startY - contY));
		tasksImgBtn.startAnimation(tasksInPathAnimation(startX - taskX, startY- taskY));

		calendarImgBtn.setVisibility(View.VISIBLE);
		contactImgBtn.setVisibility(View.VISIBLE);
		tasksImgBtn.setVisibility(View.VISIBLE);
	}

	private void hideTabbarIcons(int index) {

		calendarImgBtn.startAnimation(calendarOutPathAnimation(startX, startY - calY));
		contactImgBtn.startAnimation(contactsOutPathAnimation(startX - contX, startY - contY));
		tasksImgBtn.startAnimation(tasksOutPathAnimation(startX - taskX, startY- taskY));
		
		calendarImgBtn.setVisibility(View.INVISIBLE);
		contactImgBtn.setVisibility(View.INVISIBLE);
		tasksImgBtn.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == atlasImgBtn) {
			atlasImgBtn.startAnimation(ATLAnimationUtils.rotate_45());
			if (calendarImgBtn.getVisibility() == View.INVISIBLE) {
				atlasImgBtn
						.setBackgroundResource(R.drawable.tabbar_icon_atlas_rotate_90_selector);

				showAllTabbarIcons();

			} else {

				atlasImgBtn
						.setBackgroundResource(R.drawable.tabbar_icon_atlas_selector);

				hideTabbarIcons(-1);
			}
		} else {
			atlasImgBtn.startAnimation(ATLAnimationUtils.rotate_45());
			atlasImgBtn
					.setBackgroundResource(R.drawable.tabbar_icon_atlas_selector);
			int size = arrTabIcon.size();
			for (int idx = 0; idx < size; idx++) {
				if (v == arrTabIcon.get(idx)) {
					currentIdx = idx;
					// 2012-01-05 TAN: To keep the animation run well
					atlasImgBtn.postDelayed(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
//							delegate.didTabToTabIndex(currentIdx);
							addNew(currentIdx);
						}
					}, 450);
					hideTabbarIcons(idx);
				}
			}
		}
	}
	
	private void addNew(int index){
		switch (index) {
		case 0:
			Toast.makeText(mActivity, "Add new contact", Toast.LENGTH_SHORT).show();
			break;
		case 1:{
//			calendarImgBtn.startAnimation(ATLAnimationUtils
//					.alphaScaleDropRotateAnimation());
			ATLCalCellData cellData = new ATLCalCellData();
			CalendarEventSingleton.getInstance().setCalCellData(cellData);
			Intent i = new Intent(mActivity, CalendarEditView.class);
			i.putExtra(CalendarCell.CELL_IS_BLANK, cellData.isBlank);
			// i.putExtra("celldata", this.cellData);
			mActivity.startActivityForResult(i, ATLCalendarIntentKeys.CALL_FROM_CALENDAR_CELL);
			mActivity.overridePendingTransition(R.anim.in_from_bottom, R.anim.stand_by);
		}
			break;
		case 2:{
			EditTaskModel.getInstance().destroy(); // Reset data to new a task
			// EditTaskModel.isNewTask = true;
//			gtaskServices.isInsert = true; // Active Insert Mode
			mActivity.startActivityForResult(new Intent(mActivity,
					EditTask.class), ATLTaskIntentKeys.CALL_FROM_TASK_LIST);
			mActivity.overridePendingTransition(R.anim.in_from_bottom, R.anim.stand_by);
		}
			break;
		case 3:
			break;
		default:
			break;
		}
	}

}