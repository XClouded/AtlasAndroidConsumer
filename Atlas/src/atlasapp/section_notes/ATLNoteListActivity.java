//  ==================================================================================================================
//  Notes.java
//  ATLAS

//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2013-01-07 TAN:     Add new tab bar
//  2012-11-08 TAN:     Merge ACA_SHARONA to ACA_TAN
//  ==================================================================================================================

package atlasapp.section_notes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import atlasapp.common.ATLTabbarView;
import atlasapp.common.ATLTabbarViewDelegete;
import atlasapp.common.DB;
//import atlasapp.common.SlideOutActivity;
import atlasapp.model.ATLNoteModel;
import atlasapp.action.Actions;
import atlasapp.section_alerts.Alerts;
import atlasapp.section_appentry.R;
import atlasapp.section_calendar.CalendarDayView;
import atlasapp.section_contacts.Contacts;
import atlasapp.section_main.ATLMultiSectionListView;
import atlasapp.section_settings.Settings;
//import atlasapp.section_tasks.ATLTaskListActivity;

public class ATLNoteListActivity extends FragmentActivity implements
		OnClickListener, ATLNoteCellDelegete, ATLTabbarViewDelegete {

	LayoutInflater mInflater;
	Context mContext;
	ViewGroup mLayout;// 2013-01-07 TAN : add new Tabbar, change to view group
	// ATLTopbar mTopBar;
	ATLMultiSectionListView noteList;

	ImageButton sortButton;
	ImageButton addButton;
	ImageButton actionButton;
	ImageButton allyButton;

	private ATLNoteList mNoteCellList;
	private ATLTabbarView tabbar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mInflater = LayoutInflater.from(this);
		mContext = this;
		mLayout = (ViewGroup) mInflater.inflate(R.layout.note_list, null);// 2013-01-07 TAN : add new Tabbar, change to view group
		setContentView(mLayout);

		// connect between parameter and id
		initAttributes();
		// link all listener to this
		setListener();
		// binding all data for ListView
		bindingData();
		// setup main menu for tab bar
//		setMainMenuListener();

	}

	private void initAttributes() {
		// TODO Auto-generated method stub

		// mTopBar = (ATLTopbar) findViewById(R.id.topBarMenu);
		// mTopBar.setType(ATLMainTabbarActivity.TAB_NOTES);
		// 2013-01-07 TAN : add new Tabbar - start
		tabbar = new ATLTabbarView(this);
		tabbar.delegate = this;
		mLayout.addView(tabbar);
		// 2013-01-07 TAN : add new Tabbar - end

		sortButton = (ImageButton) findViewById(R.id.sortButton_test);
		addButton = (ImageButton) findViewById(R.id.addButton);
		actionButton = (ImageButton) findViewById(R.id.actionButton);
		allyButton = (ImageButton) findViewById(R.id.allyButton);

		// btnAdd = mTopBar.addButton;
		// btnAllies = mTopBar.alliesButton;
		// btnSort = mTopBar.sortButton;

		noteList = (ATLMultiSectionListView) findViewById(R.id.noteList);
		noteList.setPinnedHeaderView(LayoutInflater.from(this).inflate(
				R.layout.listview_header, noteList, false));
	}

	private void bindingData() {
		// TODO Auto-generated method stub
		mNoteCellList = new ATLNoteList();
		ATLNoteListAdapter adaper = new ATLNoteListAdapter(mNoteCellList, this);
		noteList.setAdapter(adaper);
	}

	private void setListener() {
		// TODO Auto-generated method stub
		sortButton.setOnClickListener(this);
		addButton.setOnClickListener(this);
		actionButton.setOnClickListener(this);
		allyButton.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		bindingData();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == addButton) {
			ATLNoteSingleton.getInstance().destroy(); // Reset data to add new
														// Note
			startActivityForResult(new Intent(ATLNoteListActivity.this,
					ATLNoteEditActivity.class),
					ATLNoteIntentKeyList.CALL_FROM_NOTE_LIST);
		} else if (v == sortButton) {
			int width = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 40, getResources()
							.getDisplayMetrics());
//			SlideOutActivity.prepare(ATLNoteListActivity.this,
//					R.id.noteList_content, width);
//			startActivity(new Intent(ATLNoteListActivity.this,
//					ATLNoteSort.class));
//			overridePendingTransition(0, 0);
		} else if (v == actionButton) {
			Intent actionMenu = new Intent(getBaseContext(), Actions.class);
			startActivity(actionMenu);
		} else if (v == allyButton) {

			int width = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 40, getResources()
							.getDisplayMetrics());
//			SlideOutActivity.prepare(ATLNoteListActivity.this,
//					R.id.noteList_content, width);
//
//			Intent intent = new Intent(getBaseContext(), Alerts.class);
//
//			intent.putExtra("rightSwipe", true);
//			startActivity(intent);
//			overridePendingTransition(0, 0);

		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Log.v("onActivityResult", "onActivityResult");
		switch (requestCode) {
		case ATLNoteIntentKeyList.CALL_FROM_NOTE_CELL:
			Log.v("CALL_FROM_NOTE_CELL", "CALL_FROM_NOTE_CELL");
			if (resultCode == ATLNoteIntentKeyList.RESULT_FROM_NOTE_EDIT_CANCEL) {

			} else if (resultCode == ATLNoteIntentKeyList.RESULT_FROM_NOTE_EDIT_UPDATE) {
				Log.v("RESULT_FROM_NOTE_EDIT_UPDATE",
						"RESULT_FROM_NOTE_EDIT_UPDATE");
				ATLNoteDatabaseAdapter dbHepler = new ATLNoteDatabaseAdapter();
				DB.openDb();
				ATLNoteModel noteModel = new ATLNoteModel(ATLNoteSingleton
						.getInstance().getNoteCellData());
				dbHepler.updateATLNoteModel(noteModel);
				DB.closeDb();

				bindingData();

			} else if (resultCode == ATLNoteIntentKeyList.RESULT_FROM_NOTE_EDIT_INSERT) {

			}
			break;
		case ATLNoteIntentKeyList.CALL_FROM_NOTE_LIST:
			Log.v("CALL_FROM_NOTE_LIST", "CALL_FROM_NOTE_LIST");
			if (resultCode == ATLNoteIntentKeyList.RESULT_FROM_NOTE_EDIT_CANCEL) {

			} else if (resultCode == ATLNoteIntentKeyList.RESULT_FROM_NOTE_EDIT_UPDATE) {

			} else if (resultCode == ATLNoteIntentKeyList.RESULT_FROM_NOTE_EDIT_INSERT) {

				Log.v("RESULT_FROM_NOTE_EDIT_INSERT",
						"RESULT_FROM_NOTE_EDIT_INSERT");
				ATLNoteDatabaseAdapter dbHepler = new ATLNoteDatabaseAdapter();
				DB.openDb();
				ATLNoteModel noteModel = new ATLNoteModel(ATLNoteSingleton
						.getInstance().getNoteCellData());
				dbHepler.insertATLNoteModel(noteModel);
				DB.closeDb();

				bindingData();
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void didShowDeleteNoteAtIndex(int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public void didDeleteNoteAtIndex(int index) {
		// TODO Auto-generated method stub
		ATLNoteCellData cellData = (ATLNoteCellData) noteList.getAdapter()
				.getItem(index);

		ATLNoteDatabaseAdapter dbHepler = new ATLNoteDatabaseAdapter();
		DB.openDb();
		ATLNoteModel noteModel = new ATLNoteModel(cellData);
		dbHepler.deleteATLNoteModel(noteModel);
		DB.closeDb();

		bindingData();
	}

//	private void setMainMenuListener() {
//
//		((RadioGroup) findViewById(R.id.main_btns_group))
//				.setVisibility(View.VISIBLE);
//
//		RadioGroup mainBtns = (RadioGroup) findViewById(R.id.main_btns_group);
//
//		// holder.rdgCategory = (RadioGroup)row.findViewById(R.id.radiogroup);
//
//		RadioGroup.OnCheckedChangeListener rdGrpCheckedListener = new RadioGroup.OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//				// ViewFlipper vf = (ViewFlipper)findViewById(R.id.content_vf);
//				// ImageView mainTitle =
//				// (ImageView)findViewById(R.id.mainTitleLabel);
//				// mainTitle.setVisibility(0);
//
//				// TODO Auto-generated method stub
//				// setCategoryinList(position, checkedId);
//				switch (checkedId) {
//				case R.id.btnCal:
//
//					Intent intentCalendar = new Intent(getBaseContext(),
//							CalendarDayView.class);
//					// intent.putExtra("EXTRA_SESSION_ID", sessionId);
//					// 2012-11-08 RYAN TAN: add to clear to top
//					intentCalendar.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//					intentCalendar.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
//							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					startActivity(intentCalendar);
//
//					break;
//				case R.id.btnNotes:
//
//					// Intent intentNotes = new Intent(getBaseContext(),
//					// Notes.class);
//					// intentNotes.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//					// // 2012-11-08 RYAN TAN: add to clear to top
//					// intentNotes.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					// // //intent.putExtra("EXTRA_SESSION_ID", sessionId);
//					// startActivity(intentNotes);
//
//					break;
//				case R.id.btnSettings:
//					Intent intentSettings = new Intent(getBaseContext(),
//							Settings.class);
//					intentSettings.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//					intentSettings.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
//							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					// //intent.putExtra("EXTRA_SESSION_ID", sessionId);
//					startActivity(intentSettings);
//
//					break;
//				case R.id.btnToday:
//
//					Intent intentContacts = new Intent(getBaseContext(),
//							Contacts.class);
//					intentContacts.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//					intentContacts.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
//							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					// //intent.putExtra("EXTRA_SESSION_ID", sessionId);
//					startActivity(intentContacts);
//
//					break;
//				case R.id.btnToDo:
//					Intent intentTasks = new Intent(getBaseContext(),
//							ATLTaskListActivity.class);
//					intentTasks.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//
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

	// ===============================================================================
	// 2013-01-07 TAN: ATLTabBarViewDelegete Start Implement
	// ===============================================================================

	@Override
	public void didTabToTabIndex(int index) {
		// TODO Auto-generated method stub
		switch (index) {
		case ATLTabbarViewDelegete.TabbarIndex.tabbar_icon_contacts:
			Intent intentToday = new Intent(getBaseContext(),

			Contacts.class);
			intentToday.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			intentToday.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// //intent.putExtra("EXTRA_SESSION_ID", sessionId);
			startActivity(intentToday);
			break;
		case ATLTabbarViewDelegete.TabbarIndex.tabbar_icon_calendar:
			Intent intentCalendar = new Intent(getBaseContext(),
					CalendarDayView.class);
			// intent.putExtra("EXTRA_SESSION_ID", sessionId);
			// 2012-11-08 RYAN TAN: add to clear to top
			intentCalendar.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

			intentCalendar.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intentCalendar);
			break;
		case ATLTabbarViewDelegete.TabbarIndex.tabbar_icon_home:
			break;
		case ATLTabbarViewDelegete.TabbarIndex.tabbar_icon_tasks:
//			Intent intentTasks = new Intent(getBaseContext(), ATLTaskListActivity.class);
//			intentTasks.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//
//			intentTasks.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
//					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			// //intent.putExtra("EXTRA_SESSION_ID", sessionId);
//			startActivity(intentTasks);
			break;
		case ATLTabbarViewDelegete.TabbarIndex.tabbar_icon_settings:
			Intent intentSettings = new Intent(getBaseContext(), Settings.class);
			intentSettings.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

			intentSettings.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// //intent.putExtra("EXTRA_SESSION_ID", sessionId);
			startActivity(intentSettings);
			break;
		}
	}

	// ===============================================================================
	// 2013-01-07 TAN: ATLTabBarViewDelegete End Implement
	// ===============================================================================

}