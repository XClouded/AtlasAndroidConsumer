//  ==================================================================================================================
//  Actions.java
//  ATLAS
//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-12-02 TAN:    Create class
//  ==================================================================================================================

package atlasapp.action;

import atlasapp.section_appentry.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class Actions extends FragmentActivity implements OnClickListener {

	private ImageButton closeBtn;
	private ImageButton eventBtn;
	private ImageButton eventWithBtn;
	private ImageButton noteBtn;
	private ImageButton noteWithBtn;
	private ImageButton taskBtn;
	private ImageButton taskWithBtn;
	private ImageButton contactBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actions);
		initAttributes();
		setListener();
	}

	private void initAttributes() {
		// TODO Auto-generated method stub
		closeBtn = (ImageButton) findViewById(R.id.actions_close_btn);

		eventBtn = (ImageButton) findViewById(R.id.actions_menu_event_btn);
		eventWithBtn = (ImageButton) findViewById(R.id.actions_menu_event_with_btn);
		noteBtn = (ImageButton) findViewById(R.id.actions_menu_note_btn);
		noteWithBtn = (ImageButton) findViewById(R.id.actions_menu_note_with_btn);
		taskBtn = (ImageButton) findViewById(R.id.actions_menu_task_btn);
		taskWithBtn = (ImageButton) findViewById(R.id.actions_menu_task_delegate_btn);
		contactBtn = (ImageButton) findViewById(R.id.actions_menu_contact_btn);

	}

	private void setListener() {
		// TODO Auto-generated method stub
		closeBtn.setOnClickListener(this);
		eventBtn.setOnClickListener(this);
		eventWithBtn.setOnClickListener(this);
		noteBtn.setOnClickListener(this);
		noteWithBtn.setOnClickListener(this);
		taskBtn.setOnClickListener(this);
		taskWithBtn.setOnClickListener(this);
		contactBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String message = "Close";
		if (v == closeBtn) {
			finish();
		} else if (v == eventBtn) {
			message = "Create new calendar event";
		} else if (v == eventWithBtn) {
			message = "Book event with a contact";
		} else if (v == noteBtn) {
			message = "Write a new note";
		} else if (v == noteWithBtn) {
			message = "Share note with a contact";
		} else if (v == taskBtn) {
			message = "Create new task";
		} else if (v == taskWithBtn) {
			message = "Delegate task to a contact";
		} else if (v == contactBtn) {
			message = "Create new contact";
		}
		Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();

	}

}
