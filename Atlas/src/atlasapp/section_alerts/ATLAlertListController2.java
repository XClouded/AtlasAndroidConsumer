package atlasapp.section_alerts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import atlasapp.common.ATLColor;
import atlasapp.database.EventController;
import atlasapp.database.EventControllerCallBackInterface;
import atlasapp.database.EventProperties;
import atlasapp.database.ItemUserProperties;
import atlasapp.section_appentry.R;
/**
 * 
 * @author sharonnachum
 *
 */
public class ATLAlertListController2 extends FragmentActivity implements EventControllerCallBackInterface {

	
	public Activity activity;
	private Activity alertsActivity;
	private EventController eventController;
	LayoutInflater mInflater;
	View mLayout;
	
	
	TwoStateTextView headerSectionYourMoveView;
	TwoStateTextView headerSectionPendingView;
	TwoStateTextView headerSectionBookedView;
	
	
	
	private PullToRefreshListView alertYourMoveList;
	private PullToRefreshListView alertPendingList;
	private PullToRefreshListView alertBookedList;
	private ATLAlertListAdapter_2 adaper1;
	private ATLAlertListAdapter_2 adaper2;
	private ATLAlertListAdapter_2 adaper3;
	private ATLAlertCellList_2 alertCellList_2;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

//		super.onCreate(savedInstanceState);
		//         
		alertsActivity = activity;
		mInflater = LayoutInflater.from(alertsActivity);
		mLayout = (View) mInflater.inflate(R.layout.alerts_3, null);

		eventController = new EventController();
		eventController.eventControllerCallBackListener = this;

		initAttributes();

		headerSectionYourMoveView.stateChanged();
		headerSectionPendingView.resetState();
		headerSectionBookedView.resetState();

		setListener();
		bindingData(); // will be called in onResume();

		
	        
	  
	}
	public View getView()
	{
//		 
		return mLayout;
	}
	@Override
	public void onResume() {  
		super.onResume();
		// TODO
		// bindingData();
		refresh();
		// getNewAlertFromServer();
	}
	private static ATLAlertListController2 instance;

	public static ATLAlertListController2 getInstance() {
		if (instance == null) {
			instance = new ATLAlertListController2();
		}
		return instance;
	}
	private void initAttributes() {
		// TODO Auto-generated method stub
//		findFriendList = (View) mLayout
//				.findViewById(R.id.alert_find_friend_view);
		// alertsList = (PullToRefreshListView) mLayout
		// .findViewById(R.id.pull_to_refresh_listview);

		alertYourMoveList = (PullToRefreshListView) mLayout
				.findViewById(R.id.pull_to_refresh_yourmove_listview);
		alertPendingList = (PullToRefreshListView) mLayout
				.findViewById(R.id.pull_to_refresh_pending_listview);
		alertPendingList.setVisibility(View.GONE);
		alertBookedList = (PullToRefreshListView) mLayout
				.findViewById(R.id.pull_to_refresh_booked_listview);
		alertBookedList.setVisibility(View.GONE);

		headerSectionYourMoveView = new TwoStateTextView(alertsActivity,
				R.drawable.alerts_segmented_controls_red,
				(TextView) mLayout
				.findViewById(R.id.alert_header_sections_yourmove_view));
		headerSectionPendingView = new TwoStateTextView(alertsActivity,
				R.drawable.alerts_segmented_controls_yellow,
				(TextView) mLayout
				.findViewById(R.id.alert_header_sections_pending_view));
		headerSectionBookedView = new TwoStateTextView(alertsActivity,
				R.drawable.alerts_segmented_controls_green,
				(TextView) mLayout
				.findViewById(R.id.alert_header_sections_booked_view));

	}

	private void bindingData() {
		// TODO Auto-generated method stub
		alertCellList_2 = ATLAlertCellList_2.getInstance();

		adaper1 = new ATLAlertListAdapter_2(alertCellList_2.yourmoveList,
				alertsActivity);
		alertYourMoveList.setAdapter(adaper1);

		adaper2 = new ATLAlertListAdapter_2(alertCellList_2.pendingList,
				alertsActivity);
		alertPendingList.setAdapter(adaper2);

		adaper3 = new ATLAlertListAdapter_2(alertCellList_2.bookingList,
				alertsActivity);
		alertBookedList.setAdapter(adaper3);

		/*
		 * findFriendList .setAdapter(new ArrayAdapter<String>(getActivity(),
		 * android.R.layout.simple_list_item_1, new String[] {
		 * " Find Friends on Atlas", " Invite Friends on Atlas" }));
		 */
	}

	private void bindingDataByAlertList(ArrayList<ATLAlert> alertList) {
		// TODO Auto-generated method stub
		alertCellList_2 = ATLAlertCellList_2.getInstance();

		adaper1 = new ATLAlertListAdapter_2(alertCellList_2.yourmoveList,
				alertsActivity);
		alertYourMoveList.setAdapter(adaper1);

		adaper2 = new ATLAlertListAdapter_2(alertCellList_2.pendingList,
				alertsActivity);
		alertPendingList.setAdapter(adaper2);

		adaper3 = new ATLAlertListAdapter_2(alertCellList_2.bookingList,
				alertsActivity);
		alertBookedList.setAdapter(adaper3);

	}

	private void setListener() {
		// TODO Auto-generated method stub
		alertYourMoveList
		.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView,
					View view, int i, long l) {
				Intent intent = new Intent();
				ATLAlert mCellData = alertCellList_2.yourmoveList
						.get(i);
				AlertRequestEventSingleton.getInstance().setAlert(
						mCellData);
				intent.setClass(alertsActivity,
						ATLAlertEventRequest_2.class);
				alertsActivity.startActivity(intent);
				((Activity) alertsActivity).overridePendingTransition(
						R.anim.in_from_bottom, R.anim.stand_by);
			}
		});
		alertYourMoveList.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				refresh();
				// Your code to refresh the list contents goes here

				// Make sure you call listView.onRefreshComplete()
				// when the loading is done. This can be done from here or any
				// other place, like on a broadcast receive from your loading
				// service or the onPostExecute of your AsyncTask.

				// For the sake of this sample, the code will pause here to
				// force a delay when invoking the refresh
			}
		});

		alertPendingList.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				refresh();
			}
		});

		alertPendingList
		.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView,
					View view, int i, long l) {

				Intent intent = new Intent();
				intent.setClass(alertsActivity,
						ATLAlertRespondMatrix.class);
				alertsActivity.startActivity(intent);
				((Activity) alertsActivity).overridePendingTransition(
						R.anim.in_from_bottom, R.anim.stand_by);
			}
		});

		alertBookedList.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				refresh();
			}
		});
		alertBookedList
		.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView,
					View view, int i, long l) {
				Intent intent = new Intent();
				intent.setClass(alertsActivity,
						ATLAlertRespondMatrix.class);
				alertsActivity.startActivity(intent);
				((Activity) alertsActivity).overridePendingTransition(
						R.anim.in_from_bottom, R.anim.stand_by);
			}
		});

		headerSectionYourMoveView.imgTextView
		.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				headerSectionYourMoveView.stateChanged();
				headerSectionPendingView.resetState();
				headerSectionBookedView.resetState();
				alertYourMoveList.setVisibility(View.VISIBLE);
				alertPendingList.setVisibility(View.GONE);
				alertBookedList.setVisibility(View.GONE);
			}
		});

		headerSectionPendingView.imgTextView
		.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				headerSectionPendingView.stateChanged();
				headerSectionYourMoveView.resetState();
				headerSectionBookedView.resetState();

				alertYourMoveList.setVisibility(View.GONE);
				alertPendingList.setVisibility(View.VISIBLE);
				alertBookedList.setVisibility(View.GONE);
			}
		});

		headerSectionBookedView.imgTextView
		.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				headerSectionBookedView.stateChanged();
				headerSectionYourMoveView.resetState();
				headerSectionPendingView.resetState();

				alertYourMoveList.setVisibility(View.GONE);
				alertPendingList.setVisibility(View.GONE);
				alertBookedList.setVisibility(View.VISIBLE);
			}
		});
	}

	
	
	boolean isRefreshing = false;

	public void refresh() {
		if (!isRefreshing) {
			isRefreshing = true;
			// call back from getAllUserEventsCallBack
			eventController.refreshUserCalendarEvents();
		}
	}
	
	@Override
	public void getAllUserEventsCallBack(
			HashMap<String, ArrayList<EventProperties>> allEventsRetrievedFromItemUser,
			ArrayList<ItemUserProperties> allUserItemUsersPropertiesRefreshedList,
			boolean success) {
		if (success) {
			if (allEventsRetrievedFromItemUser != null
					&& allEventsRetrievedFromItemUser.size() > 0) {
//				populateAlert(allEventsRetrievedFromItemUser,
//						allUserItemUsersPropertiesRefreshedList);
				isRefreshing = false;
				alertYourMoveList.onRefreshComplete();
				alertPendingList.onRefreshComplete();
				alertBookedList.onRefreshComplete();
				// alertsList.onRefreshComplete();

				Toast.makeText(alertsActivity.getApplicationContext(),
						"Update Alert Successfully", Toast.LENGTH_LONG).show();

			} else {
				Toast.makeText(alertsActivity.getApplicationContext(),
						"Update Alert Successfully", Toast.LENGTH_LONG).show();

			}
		} else {
			Toast.makeText(alertsActivity.getApplicationContext(),
					"No new notifications found", Toast.LENGTH_LONG).show();

		}
		
	}
	private void populateAlert(
			HashMap<String, ArrayList<EventProperties>> allEventsRetrievedFromItemUser,
			ArrayList<ItemUserProperties> allUserItemUsersPropertiesRefreshedList) {
		// TODO Auto-generated method stub

		Iterator iterator = allEventsRetrievedFromItemUser.keySet().iterator();
		String keyColumn;
		ArrayList<EventProperties> valueColumn = null;
		// ArrayList<String> webEventIds = new ArrayList<String>();
		ArrayList<ATLAlert> alertList = new ArrayList<ATLAlert>();
		while (iterator.hasNext()) {
			keyColumn = (String) iterator.next();
			valueColumn = allEventsRetrievedFromItemUser.get(keyColumn);
			ATLAlert alert = new ATLAlert();
			String web_event_id1 = "";
			String web_event_id2 = "";
			String web_event_id3 = "";

			for (EventProperties event : valueColumn) {
				switch (event.displayOrder) {
				case 0:
					web_event_id1 = event.webEventId;
					break;
				case 1:
					web_event_id2 = event.webEventId;
					break;
				case 2:
					web_event_id3 = event.webEventId;
					break;
				default:
					break;
				}
				alert.eventList.add(event);
			}

			for (ItemUserProperties item : allUserItemUsersPropertiesRefreshedList) {
				if (item.webItemId.equals(web_event_id1)
						|| item.webItemId.equals(web_event_id2)
						|| item.webItemId.equals(web_event_id3)) {
					alert.itemUserList.add(item);
				}
			}
//			alert.load();
			alertList.add(alert);
		}
		bindingDataByAlertList(alertList);

	}

	@Override
	public void createCalendarEventCallBack(boolean success) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getAllUserAnEventCallBack(
			HashMap<String, ArrayList<EventProperties>> userEvents,
			HashMap<String, ArrayList<String>> eventsMembers, boolean success) {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void getEventByPrimaryWebEventIdCallBack(boolean success,
			ArrayList<EventProperties> event,
			ArrayList<ItemUserProperties> invitees) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void respondToEventInviteCallBack(boolean success) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bookEventCallBack(boolean success) {
		// TODO Auto-generated method stub
		
	}
	
	
	private class TwoStateTextView extends RelativeLayout implements
	OnClickListener {
		public static final int ACTIVE_STATE = 1;
		public static final int IN_ACTIVE_STATE = 0;
		public TextView imgTextView;
		int imgID0;
		int imgID1;
		public int state = IN_ACTIVE_STATE;

		public TwoStateTextView(Context context, int imgID1, TextView imgButon) {
			super(context);
			// TODO Auto-generated constructor stub
			imgTextView = new TextView(context);
			this.imgID1 = imgID1;
			this.imgTextView = imgButon;
			imgTextView.setBackgroundResource(Color.TRANSPARENT);
			imgTextView.setTextColor(ATLColor.LTGRAY);
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (state == IN_ACTIVE_STATE) {
				state = ACTIVE_STATE;
				imgTextView.setBackgroundResource(imgID1);
			}

		}

		public void resetState() {
			state = IN_ACTIVE_STATE;
			imgTextView.setBackgroundResource(Color.TRANSPARENT);
			imgTextView.setTextColor(ATLColor.LTGRAY);
		}

		public void stateChanged() {
			if (state == IN_ACTIVE_STATE) {
				state = ACTIVE_STATE;
				imgTextView.setBackgroundResource(imgID1);
				imgTextView.setTextColor(ATLColor.BLACK);
			}
		}

	}


	@Override
	public void getUserMoveFromParseCallBack(
			boolean success,
			HashMap<String, ArrayList<ItemUserProperties>> userMoveRecordsByPrimaryEventId) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void refreshAlertsInBackgroundCallBack(boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getNewEventReceived(
			HashMap<String, ArrayList<EventProperties>> newEventsByPrimary,
			ArrayList<String> webItemIds,
			HashMap<String, String> webEventIdByPrimry) {
		// TODO Auto-generated method stub
		
	}

}
