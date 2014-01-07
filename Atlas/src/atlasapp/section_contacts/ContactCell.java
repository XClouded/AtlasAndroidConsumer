//  ==================================================================================================================
//  NotesCell.java
//  AtlasConsumerAndroid
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-10-28 TAN:     init class to implement Notes View in simulate mode
//  ==================================================================================================================

package atlasapp.section_contacts;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import atlasapp.section_appentry.R;


public class ContactCell extends RelativeLayout implements View.OnClickListener {

	Context mActivity;   

	public ImageView backgroundImageView;
	public ImageView imageContact;
	public TextView contactDisplayName;
	public ImageView imageAtlasUser;
	public LinearLayout layout;

	public ImageView dividerImageView;

	public ContactCell(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public ContactCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public ContactCell(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	private void initView(Context context) {
		// Init CallendarCell view
		LayoutInflater.from(context).inflate(R.layout.contact_list_cell, this, true);
		mActivity = (Activity) context;

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

//	public void setCellData(ATLNotesCellData cellData) {
//		// TODO Auto-generated method stub
//		contactDisplayName.setText(setTitle(cellData.notesCellTitle));
//		imageContact.setBackgroundColor(Color.TRANSPARENT);
//	}

//	private String setTitle(String text) {
//		if (text.length() > 20)
//			text = text.substring(0, 20) + "..";
//
//		return text;
//	}

}
