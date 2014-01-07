//  ==================================================================================================================
//  ATLAlertRespondMatrix.java
//  ATLAS
//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================

package com.atlasapp.section_alerts;

import com.atlasapp.atlas_database.EventController;
import com.atlasapp.section_appentry.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * @author Ryan Tan
 *
 */
public class ATLAlertRespondMatrix extends Activity {
	private LayoutInflater mInflater;
	private ViewGroup mLayout;
	private ImageButton closeBtn;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater = LayoutInflater.from(this);
		mLayout = (ViewGroup) mInflater.inflate(R.layout.alert_respond_matrix,
				null);
		setContentView(mLayout);
		initAttributes();
		setListener();
	}

	private void initAttributes() {
		// TODO Auto-generated method stub
		closeBtn = (ImageButton) mLayout.findViewById(R.id.closeBtn);
	}
	
	private void setListener() {
		// TODO Auto-generated method stub
		closeBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.stand_by, R.anim.out_to_bottom);
			}
		});
	}

	
//	@Override
//	public void onDestroy(){
//		
//		super.onDestroy();
//	}
	
	
}
