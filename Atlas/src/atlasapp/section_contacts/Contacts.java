//  ==================================================================================================================
//  Contacts.java
//  ATLAS
//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================

package atlasapp.section_contacts;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

//import atlasapp.common.SlideOutActivity;
import atlasapp.action.Actions;
import atlasapp.section_appentry.R;
import atlasapp.section_calendar.CalendarDayView;
import atlasapp.section_notes.ATLNoteListActivity;
import atlasapp.section_settings.Settings;
//import atlasapp.section_tasks.Tasks;
//import atlasapp.section_today.TodaySort;

/**
 * @author Ryan Tan
 * 
 */
public class Contacts extends FragmentActivity {
	private RadioButton todayBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts_disabled);

		ImageButton btnSort = (ImageButton) findViewById(R.id.sortButton);
		btnSort.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int width = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 40, getResources()
								.getDisplayMetrics());
//				SlideOutActivity.prepare(Contacts.this, R.id.today_content,
//						width);
//				startActivity(new Intent(Contacts.this, TodaySort.class));
//				overridePendingTransition(0, 0);
			}
		});

		ImageButton btnAllies = (ImageButton) findViewById(R.id.alliesbtn);
		btnAllies.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int width = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 40, getResources()
								.getDisplayMetrics());
//				SlideOutActivity.prepare(Contacts.this, R.id.today_content,
//						width);
//				Intent intent = new Intent(getBaseContext(), TodaySort.class);
//				intent.putExtra("rightSwipe", true);
//				startActivity(intent);
//				// startActivity(new Intent(Today.this,
//				// TodaySort.class));
//				overridePendingTransition(0, 0);
			}
		});

		ImageButton btnActions = (ImageButton) findViewById(R.id.actionsBtn);
		btnActions.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getBaseContext(), Actions.class);
				startActivity(intent);
			}
		});

		((RadioButton) findViewById(R.id.btnToday)).setVisibility(View.VISIBLE);
		todayBtn = (RadioButton) findViewById(R.id.btnToday);

		todayBtn.setChecked(true);
		todayBtn.setTextColor(Color.BLACK);

//		setMainMenuListener();

	}

	@Override
	protected void onResume() {
		super.onResume();
		todayBtn.setChecked(true);
		todayBtn.setTextColor(Color.BLACK);
	}

//	private void setMainMenuListener() {
//
//		((RadioGroup) findViewById(R.id.main_btns_group))
//				.setVisibility(View.VISIBLE);
//
//		RadioGroup mainBtns = (RadioGroup) findViewById(R.id.main_btns_group);
//		RadioGroup.OnCheckedChangeListener rdGrpCheckedListener = new RadioGroup.OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//				// TODO Auto-generated method stub
//				switch (checkedId) {
//				case R.id.btnCal:
//					Intent intentCalendar = new Intent(getBaseContext(),
//							CalendarDayView.class);
//					intentCalendar.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//					intentCalendar.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
//							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					// intent.putExtra("EXTRA_SESSION_ID", sessionId);
//					startActivity(intentCalendar);
//
//					break;
//				case R.id.btnNotes:
//					Intent intentNotes = new Intent(getBaseContext(),
//							ATLNoteListActivity.class);
//					intentNotes.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//					intentNotes.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
//							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					// //intent.putExtra("EXTRA_SESSION_ID", sessionId);
//					startActivity(intentNotes);
//
//					break;
//				case R.id.btnSettings:
//					Intent intent = new Intent(getBaseContext(), Settings.class);
//					intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//					intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
//							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					// //intent.putExtra("EXTRA_SESSION_ID", sessionId);
//					startActivity(intent);
//
//					break;
//				case R.id.btnToday:
//
//					break;
//				case R.id.btnToDo:
//					Intent intentTasks = new Intent(getBaseContext(),
//							Tasks.class);
//					intentTasks.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//					intentTasks.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
//							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					// //intent.putExtra("EXTRA_SESSION_ID", sessionId);
//					startActivity(intentTasks);
//
//					break;
//				default:
//
//					break;
//				}
//			}
//		};
//
//		mainBtns.setOnCheckedChangeListener(rdGrpCheckedListener);
//
//	}

}