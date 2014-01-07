//  ==================================================================================================================
//  ATLTopbar.java
//  AtlasConsumerAndroid
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-10-24 TAN:    Init Class 
//  ==================================================================================================================

package atlasapp.section_main;



import atlasapp.section_appentry.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ATLTopbar extends RelativeLayout implements View.OnClickListener {
	public Activity mActivity;
	public int topbarType;
	public ImageButton sortButton;
	public ImageButton calButton;
	public ImageButton addButton;
	public ImageButton alliesButton;
//	public ImageView titleImage;
	public TextView titleTextView;

	/**
	 * @param context
	 */
	public ATLTopbar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.initView(context);
	}


	public ATLTopbar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.initView(context);
	}


	public ATLTopbar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.initView(context);
	}
	 
	private void initView(Context context){
		LayoutInflater.from(context).inflate(R.layout.top_menu_bar1, this, true);
		mActivity = (Activity) context;
		sortButton = (ImageButton) findViewById(R.id.sortButton);
		calButton = (ImageButton) findViewById(R.id.btnMonthImage);
		addButton = (ImageButton) findViewById(R.id.addbtn);
		alliesButton = (ImageButton) findViewById(R.id.alliesbtn);
//		titleImage = (ImageView) findViewById(R.id.titleImage);
		titleTextView = (TextView) findViewById(R.id.titleTextView);
	}
	
	public void setType(int type){
		topbarType = type;
		switch (topbarType) {

		case ATLMainTabbarActivity.TAB_CONTACTS:
//			addButton.setVisibility(View.GONE);
			titleTextView.setText("Contacts");
//			titleImage.setImageResource(R.drawable.title_contacts);
			break;
		case ATLMainTabbarActivity.TAB_CALENDAR:
//			titleImage.setImageResource(R.drawable.title_cal);
			break;
		case ATLMainTabbarActivity.TAB_TASKS:
//			calButton.setVisibility(View.GONE);
//			titleImage.setImageResource(R.drawable.title_tasks);
			break;
		case ATLMainTabbarActivity.TAB_NOTES:
			calButton.setVisibility(View.GONE);
//			titleImage.setImageResource(R.drawable.title_notes);
			break;
		case ATLMainTabbarActivity.TAB_SETTINGS:
			sortButton.setVisibility(View.GONE);
			calButton.setVisibility(View.GONE);
			addButton.setVisibility(View.GONE);
			alliesButton.setVisibility(View.GONE);
//			titleImage.setImageResource(R.drawable.settings_title_bar_title);
			break;
		default:
			break;
		}
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
