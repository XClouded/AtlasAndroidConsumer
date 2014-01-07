//  ==================================================================================================================
//  ATLTabbarView.java
//  ATLAS
//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2013-01-03 TAN:    Create class
//  ==================================================================================================================

package atlasapp.common;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import atlasapp.section_appentry.R;

public class ATLTabbarView extends RelativeLayout implements
		View.OnClickListener {

	Activity mActivity;
	int currentIdx = -1;
	public ImageButton contactImgBtn;
	public ImageButton calendarImgBtn;
	public ImageButton atlasImgBtn;
	public ImageButton homeImgBtn;
	public ImageButton tasksImgBtn;
	public ImageButton settingsImgBtn;
	public TextView primaryLabel;
	public TextView secondaryLabel;

	private ArrayList<View> arrTabIcon = new ArrayList<View>();

	public ATLTabbarViewDelegete delegate;

	public ATLTabbarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public ATLTabbarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public ATLTabbarView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	private void initView(Context context) {
		// Init CallendarCell view
		LayoutInflater.from(context).inflate(R.layout.bottom_tabbar_new, this,
				true);
		mActivity = (Activity) context;
		atlasImgBtn = (ImageButton) findViewById(R.id.imageButton6);
		contactImgBtn = (ImageButton) findViewById(R.id.imageButton4);
		calendarImgBtn = (ImageButton) findViewById(R.id.imageButton2);
		homeImgBtn = (ImageButton) findViewById(R.id.imageButton1);
		tasksImgBtn = (ImageButton) findViewById(R.id.imageButton3);
		settingsImgBtn = (ImageButton) findViewById(R.id.imageButton5);

		arrTabIcon.add(contactImgBtn);
		arrTabIcon.add(calendarImgBtn);
		arrTabIcon.add(homeImgBtn);
		arrTabIcon.add(tasksImgBtn);
		arrTabIcon.add(settingsImgBtn);

		settingsImgBtn.setVisibility(View.GONE);
		contactImgBtn.setVisibility(View.GONE);
		calendarImgBtn.setVisibility(View.GONE);
		homeImgBtn.setVisibility(View.GONE);
		tasksImgBtn.setVisibility(View.GONE);

		atlasImgBtn.setOnClickListener(this);
		for (View v : arrTabIcon) {
			v.setOnClickListener(this);
		}

	}

	private void showAllTabbarIcons() {
		calendarImgBtn.startAnimation(ATLAnimationUtils
				.calendarInPathAnimation());
		contactImgBtn.startAnimation(ATLAnimationUtils
				.contactsInPathAnimation());
		homeImgBtn.startAnimation(ATLAnimationUtils.notesInPathAnimation());
		tasksImgBtn.startAnimation(ATLAnimationUtils.tasksInPathAnimation());
		settingsImgBtn.startAnimation(ATLAnimationUtils
				.settingsInPathAnimation());

		calendarImgBtn.setVisibility(View.VISIBLE);
		contactImgBtn.setVisibility(View.VISIBLE);
		homeImgBtn.setVisibility(View.VISIBLE);
		tasksImgBtn.setVisibility(View.VISIBLE);
		settingsImgBtn.setVisibility(View.VISIBLE);
	}

	private void hideTabbarIcons(int index) {

		calendarImgBtn.startAnimation(ATLAnimationUtils
				.dropAndRotateAnimation());
		contactImgBtn
				.startAnimation(ATLAnimationUtils.dropAndRotateAnimation());
		homeImgBtn.startAnimation(ATLAnimationUtils.dropAndRotateAnimation());
		tasksImgBtn.startAnimation(ATLAnimationUtils.dropAndRotateAnimation());
		settingsImgBtn.startAnimation(ATLAnimationUtils
				.dropAndRotateAnimation());

		switch (index) {
		case 0:
			contactImgBtn.startAnimation(ATLAnimationUtils
					.alphaScaleDropRotateAnimation());
			break;
		case 1:
			calendarImgBtn.startAnimation(ATLAnimationUtils
					.alphaScaleDropRotateAnimation());

			break;
		case 2:
			homeImgBtn.startAnimation(ATLAnimationUtils
					.alphaScaleDropRotateAnimation());
			break;
		case 3:
			tasksImgBtn.startAnimation(ATLAnimationUtils
					.alphaScaleDropRotateAnimation());
			break;
		case 4:
			settingsImgBtn.startAnimation(ATLAnimationUtils
					.alphaScaleDropRotateAnimation());
			break;
		default:
			break;
		}

		calendarImgBtn.setVisibility(View.GONE);
		contactImgBtn.setVisibility(View.GONE);
		homeImgBtn.setVisibility(View.GONE);
		tasksImgBtn.setVisibility(View.GONE);
		settingsImgBtn.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == atlasImgBtn) {
			atlasImgBtn.startAnimation(ATLAnimationUtils.rotate_90());
			if (calendarImgBtn.getVisibility() == View.GONE) {
				atlasImgBtn
						.setBackgroundResource(R.drawable.tabbar_icon_atlas_rotate_90_selector);

				showAllTabbarIcons();

			} else {

				atlasImgBtn
						.setBackgroundResource(R.drawable.tabbar_icon_atlas_selector);

				hideTabbarIcons(-1);
			}
		} else {
			atlasImgBtn.startAnimation(ATLAnimationUtils.rotate_90());
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
							delegate.didTabToTabIndex(currentIdx);
						}
					}, 600);
					hideTabbarIcons(idx);
				}
			}
		}
	}

}