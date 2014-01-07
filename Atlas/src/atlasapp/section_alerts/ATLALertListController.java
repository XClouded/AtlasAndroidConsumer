package atlasapp.section_alerts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import atlasapp.common.ATLColor;
import atlasapp.database.ATLAlertController;
import atlasapp.database.ATLAlertControllerCallBackInterface;
import atlasapp.database.DatabaseConstants.EVENT_STATUS;
import atlasapp.database.EventController;
import atlasapp.database.EventControllerCallBackInterface;
import atlasapp.database.EventProperties;
import atlasapp.database.ItemUserProperties;
import atlasapp.section_appentry.R;
import atlasapp.section_calendar.ATLCalendarRespond;
import atlasapp.slidemenu.ATLSlideMenu;
import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class ATLALertListController extends FragmentActivity implements
 ATLAlertControllerCallBackInterface {

//	private EventController eventController;
	
	public static ATLAlertController alertController;
	
	private enum ALERT_REFRES {YOUR_MOVE, PENDING, BOOKED};
	LayoutInflater mInflater;
	// public Context mContext;
	View mLayout;
	// PullToRefreshListView alertsList;
	View findFriendList;
	private Activity alertsActivity;
  
	public TwoStateTextView headerSectionYourMoveView;
	public TwoStateTextView headerSectionPendingView;
	public TwoStateTextView headerSectionBookedView;
	private PullToRefreshListView alertYourMoveList;
	private PullToRefreshListView alertPendingList;
	private PullToRefreshListView alertBookedList;
	private ATLAlertListAdapter_2 adaper1;
	private ATLAlertListAdapter_2 adaper2;
	private ATLAlertListAdapter_2 adaper3;
	private ATLAlertCellList_2 alertCellList_2;
	
	
	
	 ArrayList<ATLAlert> pendingAlerts =new ArrayList<ATLAlert>();

	 ArrayList<ATLAlert> bookedAlerts=new ArrayList<ATLAlert>();

	 ArrayList<ATLAlert> yourMoveAlerts=new ArrayList<ATLAlert>();

	private static ATLALertListController instance;

	public static ATLALertListController getInstance() {
		if (instance == null) {
			instance = new ATLALertListController();
//			pendingAlerts = new ArrayList<ATLAlert>();
//			bookedAlerts = new ArrayList<ATLAlert>();
//			yourMoveAlerts = new ArrayList<ATLAlert>();
			alertController = new ATLAlertController(instance);
		}
		return instance;
	}
	private Context mContext;
	public Activity activity;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		mContext = this;
//		super.onCreate(savedInstanceState);   
		//         
		alertsActivity = activity;
		mInflater = LayoutInflater.from(alertsActivity);
		mLayout = (View) mInflater.inflate(R.layout.alerts_3, null);

//		eventController = new EventController();
//		eventController.eventControllerCallBackListener = this;

		
		
		
		initAttributes();

		headerSectionYourMoveView.stateChanged();
		headerSectionPendingView.resetState();
		headerSectionBookedView.resetState();

		setListener();
		bindingData(); // will be called in onResume();

		
		refresh();

	  
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
//		refresh();
		// getNewAlertFromServer();
	}

	public void initAttributes() {
		// TODO Auto-generated method stub
		findFriendList = (View) mLayout
				.findViewById(R.id.alert_find_friend_view);
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

	public void bindingData() {
		// TODO Auto-generated method stub
		alertCellList_2 = ATLAlertCellList_2.getInstance();  

		adaper1 = new ATLAlertListAdapter_2(alertCellList_2.yourmoveList,
				alertsActivity);
		alertYourMoveList.setAdapter(adaper1);

		adaper2 = new ATLAlertListAdapter_2(alertCellList_2.pendingList,
				alertsActivity);
		alertPendingList.setAdapter(adaper2);

		
		alertCellList_2.bookedList = alertCellList_2.bookingList;
		alertCellList_2.bookedList.addAll(alertCellList_2.declinedList);
		
		alertCellList_2.bookedList = alertCellList_2.bookingList;
		alertCellList_2.bookedList.addAll(alertCellList_2.declinedList);
		
		adaper3 = new ATLAlertListAdapter_2(alertCellList_2.bookedList,
				alertsActivity);
		alertBookedList.setAdapter(adaper3);

		/*
		 * findFriendList .setAdapter(new ArrayAdapter<String>(getActivity(),
		 * android.R.layout.simple_list_item_1, new String[] {
		 * " Find Friends on Atlas", " Invite Friends on Atlas" }));
		 */
	}

	private synchronized void bindingBookedAlertList() {
		alertCellList_2.addATLAlertCellList_2();

//		adaper1 = new ATLAlertListAdapter_2(alertCellList_2.yourmoveList,
//				alertsActivity);
//		alertYourMoveList.setAdapter(adaper1);
//
//		adaper2 = new ATLAlertListAdapter_2(alertCellList_2.pendingList,
//				alertsActivity);
//		alertPendingList.setAdapter(adaper2);

//		alertCellList_2.bookedList = alertCellList_2.bookingList;
//		alertCellList_2.bookedList.addAll(alertCellList_2.declinedList);
		
		adaper3 = new ATLAlertListAdapter_2(alertCellList_2.bookedList,
				alertsActivity);
		alertBookedList.setAdapter(adaper3);

	}
	private synchronized void bindingYourMoveAlertList() {
		// TODO Auto-generated method stub
		alertCellList_2.addATLAlertCellList_2();
		
		alertCellList_2.allYourMoveList = alertCellList_2.yourmoveList;
		if (alertCellList_2.allYourMoveList!=null )
		{
			if ( alertCellList_2.newYourMoveList!=null && alertCellList_2.newYourMoveList.size()>0)
				alertCellList_2.allYourMoveList.addAll(alertCellList_2.newYourMoveList);
		}
		else
			alertCellList_2.allYourMoveList  = alertCellList_2.newYourMoveList;
		adaper1 = new ATLAlertListAdapter_2(alertCellList_2.allYourMoveList,
				alertsActivity);
		alertYourMoveList.setAdapter(adaper1);

//		adaper2 = new ATLAlertListAdapter_2(alertCellList_2.pendingList,
//				alertsActivity);
//		alertPendingList.setAdapter(adaper2);
//
//		adaper3 = new ATLAlertListAdapter_2(alertCellList_2.bookingList,
//				alertsActivity);
//		alertBookedList.setAdapter(adaper3);

	}
	private synchronized void bindingPendingAlertList() {
		// TODO Auto-generated method stub
		alertCellList_2.addATLAlertCellList_2();

//		adaper1 = new ATLAlertListAdapter_2(alertCellList_2.yourmoveList,
//				alertsActivity);
//		alertYourMoveList.setAdapter(adaper1);

		adaper2 = new ATLAlertListAdapter_2(alertCellList_2.pendingList,
				alertsActivity);
		alertPendingList.setAdapter(adaper2);

//		adaper3 = new ATLAlertListAdapter_2(alertCellList_2.bookingList,
//				alertsActivity);
//		alertBookedList.setAdapter(adaper3);

	}
//	private void bindDataToPendingAlertList(ArrayList<ATLAlert> alertList) 
//	{
//		alertCellList_2 = new ATLAlertCellList_2(alertList);
//		adaper2 = new ATLAlertListAdapter_2(alertCellList_2.pendingList,
//				alertsActivity);
//		alertPendingList.setAdapter(adaper2);
//		
//	}
//	private void bindDataToBookedAlertList(ArrayList<ATLAlert> alertList) 
//	{
//		alertCellList_2 = new ATLAlertCellList_2(alertList);
//		adaper3 = new ATLAlertListAdapter_2(alertCellList_2.bookingList,
//				alertsActivity);
//		alertBookedList.setAdapter(adaper3);
//	}
	public void setListener() {
		// TODO Auto-generated method stub
		alertYourMoveList
		.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView,
					View view, int i, long l) {
				ATLAlert mCellData = alertCellList_2.yourmoveList
						.get(i);
				if (mCellData!=null)
				{
					ArrayList<EventProperties> eventList = mCellData.eventList;
					if (eventList!=null && eventList.size()>0)
					{
						String primaryWebEventId = eventList.get(0).primaryWebEventId;
						primaryWebEventId = (primaryWebEventId!=null && !primaryWebEventId.equals(""))?
								primaryWebEventId:(eventList.get(0).webEventId!=null && !eventList.get(0).webEventId.equals("")&&primaryWebEventId.equals(""))?
										eventList.get(0).webEventId:"";
						if (primaryWebEventId!=null && !primaryWebEventId.equals(""))
						{
							Intent intent = new Intent();
							intent.putExtra("primaryWebEventId",primaryWebEventId );
							intent.setClass(alertsActivity,
//									ATLRespondMatrixListController.class);
									ATLCalendarRespond.class);
									alertsActivity.startActivity(intent);
							((Activity) alertsActivity).overridePendingTransition(
									R.anim.in_from_bottom, R.anim.stand_by);
						}
					}
				}
				
				
				
				
				
//				Intent intent = new Intent();
//				ATLAlert mCellData = alertCellList_2.yourmoveList
//						.get(i);
//				AlertRequestEventSingleton.getInstance().setAlert(
//						mCellData);
//				intent.setClass(alertsActivity,
//						ATLAlertEventRequest_2.class);
//				alertsActivity.startActivity(intent);
//				((Activity) alertsActivity).overridePendingTransition(
//						R.anim.in_from_bottom, R.anim.stand_by);
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

				ATLAlert mCellData = alertCellList_2.pendingList
						.get(i);
				if (mCellData!=null)
				{
					ArrayList<EventProperties> eventList = mCellData.eventList;
					if (eventList!=null && eventList.size()>0)
					{
						String primaryWebEventId = eventList.get(0).primaryWebEventId;
						primaryWebEventId = (primaryWebEventId!=null && !primaryWebEventId.equals(""))?
								primaryWebEventId:(eventList.get(0).webEventId!=null && !eventList.get(0).webEventId.equals("")&&primaryWebEventId.equals(""))?
										eventList.get(0).webEventId:"";
						if (primaryWebEventId!=null && !primaryWebEventId.equals(""))
						{
							Intent intent = new Intent();
							intent.putExtra("primaryWebEventId",primaryWebEventId );
							intent.setClass(alertsActivity,
									ATLRespondMatrixListController.class);
							alertsActivity.startActivity(intent);
							((Activity) alertsActivity).overridePendingTransition(
									R.anim.in_from_bottom, R.anim.stand_by);
						}
					}
				}
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
				ATLAlert mCellData = alertCellList_2.bookedList
						.get(i);
				if (mCellData!=null)
				{
					ArrayList<EventProperties> eventList = mCellData.eventList;
					if (eventList!=null && eventList.size()>0)
					{
						String primaryWebEventId = eventList.get(0).primaryWebEventId;
						primaryWebEventId = (primaryWebEventId!=null && !primaryWebEventId.equals(""))?
								primaryWebEventId:(eventList.get(0).webEventId!=null && !eventList.get(0).webEventId.equals("")&&primaryWebEventId.equals(""))?
										eventList.get(0).webEventId:"";
										
						if (primaryWebEventId!=null && !primaryWebEventId.equals(""))
						{
							
							Intent intent = new Intent();
							intent.putExtra("primaryWebEventId",primaryWebEventId );
							intent.setClass(alertsActivity,
									ATLRespondMatrixListController.class);
							alertsActivity.startActivity(intent);
							((Activity) alertsActivity).overridePendingTransition(
									R.anim.in_from_bottom, R.anim.stand_by);
						}
					}
				}
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

	
	public boolean isRefreshing = false;

	private boolean isBookedRefreshing;

	private boolean isPendingRefreshing;

	private boolean isYourMoveRefreshing;

	public void refresh() {
//		isRefreshing = isBookedRefreshing && isPendingRefreshing && isYourMoveRefreshing;
		if (!isRefreshing) {
			isRefreshing = true;
			/// call back on getAllUserEventsCallBack
//			eventController.refreshUserCalendarEvents();
			
//			isBookedRefreshing = true;
//			isPendingRefreshing = true;
//			isYourMoveRefreshing = true;
			alertController.refreshAllAlerts(mContext);
//			alertController.getBookedAlert();
//			alertController.getPendingAlert();
		}
	}
	
	@Override
	public void getBookedAcceptedAlertCallBack(
			HashMap<String, ItemUserProperties> acceptedRecords) {
		
		
		
		isBookedRefreshing = false;
		alertYourMoveList.onRefreshComplete();
		alertPendingList.onRefreshComplete();
		alertBookedList.onRefreshComplete();
////		
		if (acceptedRecords!=null && acceptedRecords.size()>0)
		{
			
			for (String prop:acceptedRecords.keySet())
			{
				ItemUserProperties propItem = acceptedRecords.get(prop);
				Toast.makeText(alertsActivity.getApplicationContext(),
						"Item User invitee "+propItem.getItemUserContact().getFirstname(), Toast.LENGTH_LONG).show();
				EventProperties event = propItem.getEventAssociated();
				if (event!=null)
				{
				Toast.makeText(alertsActivity.getApplicationContext(),
						"Event title "+event.title, Toast.LENGTH_LONG).show();
				Toast.makeText(alertsActivity.getApplicationContext(),
						"Event time "+event.startDateTime.toString(), Toast.LENGTH_LONG).show();
				Toast.makeText(alertsActivity.getApplicationContext(),
						"Event modified time "+event.modifiedDatetime.toString(), Toast.LENGTH_LONG).show();
				}
				
			}
				updateBookedAcceptAlert(acceptedRecords);
			
			Toast.makeText(alertsActivity.getApplicationContext(),
					"Update Alert Successfully", Toast.LENGTH_LONG).show();
			
		}
		else
		{
			Toast.makeText(alertsActivity.getApplicationContext(),
					"No new booked notifications found", Toast.LENGTH_LONG).show();
		}
		
	}
	
	@Override
	public void getYourMoveAlertCallBack(
			HashMap<String, ArrayList<ItemUserProperties>> pendingItemUserRecords) {
		isRefreshing = false;
		alertYourMoveList.onRefreshComplete();
		alertPendingList.onRefreshComplete();
		alertBookedList.onRefreshComplete();
		if (pendingItemUserRecords!=null && pendingItemUserRecords.size()>0)
		{
//			updateYourMoveAlert(pendingItemUserRecords);
		}
		else
		{
			Toast.makeText(alertsActivity.getApplicationContext(),
					"No new your move notifications found", Toast.LENGTH_LONG).show();
		}
	}
	@Override
	public void getYourMoveInBackground(
			HashMap<String, ArrayList<EventProperties>> yourMoveHash,
			ArrayList<String> webEventIdsFound,
			HashMap<String, String> webEventIdByPrimry,
			HashMap<String, ArrayList<ItemUserProperties>> itemUserRecords) {
		
		isRefreshing = false;
		alertYourMoveList.onRefreshComplete();
		alertPendingList.onRefreshComplete();
		alertBookedList.onRefreshComplete();
		if (yourMoveHash!=null && yourMoveHash.size()>0)
		{
			updateYourMoveAlert(yourMoveHash,
					webEventIdsFound,
					webEventIdByPrimry,
					itemUserRecords);
		}
		else
		{
			Toast.makeText(alertsActivity.getApplicationContext(),
					"No new  your move notifications found", Toast.LENGTH_LONG).show();
		}
	}
	@Override
	public void addToYourMoveInBackground(
			HashMap<String, ArrayList<EventProperties>> newEventsByPrimary,
			ArrayList<String> webItemIds,
			HashMap<String, String> webEventIdByPrimry,
			HashMap<String, ArrayList<ItemUserProperties>> itemUserRecords) {
		isRefreshing = false;
		alertYourMoveList.onRefreshComplete();
		alertPendingList.onRefreshComplete();
		alertBookedList.onRefreshComplete();
		if (newEventsByPrimary!=null && newEventsByPrimary.size()>0)
		{
			ArrayList<ATLAlert> alertList = new ArrayList<ATLAlert>();
			if (newEventsByPrimary!=null && newEventsByPrimary.size()>0)
			{
			
//			ArrayList<ItemUserProperties> itemUserRecords;
			
			EventProperties event;
			ATLAlert alert ;  
			String primary = "";
			for (String alertAcceptedKey:newEventsByPrimary.keySet())
			{
				alert = new ATLAlert();
//				itemUserRecords = yourMoveHash.get(alertAcceptedKey);
				
				if (itemUserRecords!=null && itemUserRecords.size()>0)
				{
					
					alert.eventList= newEventsByPrimary.get(alertAcceptedKey);
					primary = alert.eventList.get(0).primaryWebEventId;
					primary = (primary==null || primary.equals(""))? alert.eventList.get(0).webEventId:
						primary;
					alert.itemUserList= (itemUserRecords.get(primary));
					alert.youMoveAlerts();
					alertList.add(alert);
				}
				
			}
			alertCellList_2.setNewYouMoveList(alertList);
			bindingYourMoveAlertList();
			}
		}
		else
		{
			Toast.makeText(alertsActivity.getApplicationContext(),
					"No new your move notifications found", Toast.LENGTH_LONG).show();
		}
		
	}
	
	private void updateYourMoveAlert(
			HashMap<String, ArrayList<EventProperties>> yourMoveHash,
			ArrayList<String> webEventIdsFound,
			HashMap<String, String> webEventIdByPrimryYourMove,
			HashMap<String, ArrayList<ItemUserProperties>> itemUserRecordsYourMove)
	{
		ArrayList<ATLAlert> alertList = new ArrayList<ATLAlert>();
		if (yourMoveHash!=null && yourMoveHash.size()>0)
		{
		
//		ArrayList<ItemUserProperties> itemUserRecords;
		
		EventProperties event;
		ATLAlert alert ;  
		String primary = "";
		boolean notBooked=true;
		for (String alertAcceptedKey:yourMoveHash.keySet())
		{
			alert = new ATLAlert();
//			itemUserRecords = yourMoveHash.get(alertAcceptedKey);
			
			if (itemUserRecordsYourMove!=null && itemUserRecordsYourMove.size()>0)
			{
				
				alert.eventList= yourMoveHash.get(alertAcceptedKey);
				primary = alert.eventList.get(0).primaryWebEventId;
				notBooked = alert.eventList.get(0).status.equals(EVENT_STATUS.PENDING);
				primary = (primary==null || primary.equals(""))? alert.eventList.get(0).webEventId:
					primary;
				alert.itemUserList= (itemUserRecordsYourMove.get(primary));
				alert.youMoveAlerts();
				if (notBooked)
					alertList.add(alert);
			}
			
		}
		alertCellList_2.setToYouMoveList(alertList);
		bindingYourMoveAlertList();
		}
		
	}
	
	
	
	
	@Override
	public void getPendingAlertCallBack(
			HashMap<String, ItemUserProperties> pendingItemUserRecords)
	{
//		isRefreshing = false;
//		isPendingRefreshing = false;
//		alertYourMoveList.onRefreshComplete();
//		alertPendingList.onRefreshComplete();
//		alertBookedList.onRefreshComplete();
//		if (pendingItemUserRecords!=null && pendingItemUserRecords.size()>0)
//		{
////			updatePendingAlert(pendingItemUserRecords);
//		}
//		else
//		{
//			Toast.makeText(alertsActivity.getApplicationContext(),
//					"No new pending notifications found", Toast.LENGTH_LONG).show();
//		}
		
	}
	@Override
	public void getPendingInBackground(
			HashMap<String, ArrayList<EventProperties>> pendingHash,
			ArrayList<String> webEventIdsFound,
			HashMap<String, String> webEventIdByPrimry,
			HashMap<String, ArrayList<ItemUserProperties>> itemUserRecords) {
		isRefreshing = false;
		isPendingRefreshing = false;
		alertYourMoveList.onRefreshComplete();
		alertPendingList.onRefreshComplete();
		alertBookedList.onRefreshComplete();
		if (pendingHash!=null && pendingHash.size()>0)
		{
			updatePendingAlert(
					pendingHash,
					 webEventIdsFound,
					webEventIdByPrimry,
					itemUserRecords);
		}
		else
		{
			Toast.makeText(alertsActivity.getApplicationContext(),
					"No new pending notifications found", Toast.LENGTH_LONG).show();
		}
		
	}
	
	private synchronized void updatePendingAlert(
			HashMap<String, ArrayList<EventProperties>> yourCompletedHash,
			ArrayList<String> webEventIdsFound,
			HashMap<String, String> webEventIdByPrimry,
			HashMap<String, ArrayList<ItemUserProperties>> itemUserRecords)
	{
		ArrayList<ATLAlert> alertList = new ArrayList<ATLAlert>();
		if (yourCompletedHash!=null && yourCompletedHash.size()>0)
		{
		
//		ArrayList<ItemUserProperties> itemUserRecords;
		
		EventProperties event;
		ATLAlert alert ;  
		String primary = "";
//		boolean notBooked=true;
		for (String alertAcceptedKey:yourCompletedHash.keySet())
		{
			alert = new ATLAlert();
//			itemUserRecords = yourMoveHash.get(alertAcceptedKey);
			
			if (itemUserRecords!=null && itemUserRecords.size()>0)
			{
				
				alert.eventList= yourCompletedHash.get(alertAcceptedKey);
				primary = alert.eventList.get(0).primaryWebEventId;
				primary = (primary==null || primary.equals(""))? alert.eventList.get(0).webEventId:
					primary;
				alert.itemUserList= (itemUserRecords.get(primary));
//				notBooked = alert.eventList.get(0).status.equals(EVENT_STATUS.PENDING);
				alert.pendingEvents();
//				if (notBooked)
					alertList.add(alert);
			}
			
		
		}
		alertCellList_2.setToPendingList(alertList);
		bindingPendingAlertList();
		}
//		bindDataToPendingAlertList(alertList);
		
		
		
	}  
	
	@Override
	public void getBookedDeclinedAlertCallBack(
			HashMap<String, ItemUserProperties> declinedBookedItemUserRecords) {
		isRefreshing = false;
		isBookedRefreshing = false;
		alertYourMoveList.onRefreshComplete();
		alertPendingList.onRefreshComplete();
		alertBookedList.onRefreshComplete();
		if (declinedBookedItemUserRecords!=null && declinedBookedItemUserRecords.size()>0)
		{
//			updateBookedDeclinedAlert(declinedBookedItemUserRecords);
		}
		else
		{
			Toast.makeText(alertsActivity.getApplicationContext(),
					"No new booked notifications found", Toast.LENGTH_LONG).show();
		}
		
	}
	
	@Override
	public void refreshAlertsInBackgroundCallBack(boolean success) {
		if (!success)
		{
			isRefreshing = false;
			isBookedRefreshing = false;
			alertYourMoveList.onRefreshComplete();
			alertPendingList.onRefreshComplete();
			alertBookedList.onRefreshComplete();
		}
		
	}
	
	@Override
	public void getBookedInBackground(
			HashMap<String, ArrayList<EventProperties>> yourCompletedHash,
			ArrayList<String> webEventIdsFound,
			HashMap<String, String> webEventIdByPrimry,
			HashMap<String, ArrayList<ItemUserProperties>> itemUserRecords) {

		
		isRefreshing = false;
		isBookedRefreshing = false;
		alertYourMoveList.onRefreshComplete();
		alertPendingList.onRefreshComplete();
		alertBookedList.onRefreshComplete();
		if (yourCompletedHash!=null && yourCompletedHash.size()>0)
		{
			updateBookedAlert(
			 yourCompletedHash,
			 webEventIdsFound,
			 webEventIdByPrimry,
			itemUserRecords);
		}
		else
		{
			Toast.makeText(alertsActivity.getApplicationContext(),
					"No new booked notifications found", Toast.LENGTH_LONG).show();
		}
		
		
	}
	private  void updateBookedAlert(
			HashMap<String, ArrayList<EventProperties>> yourCompletedHash,
			ArrayList<String> webEventIdsFound,
			HashMap<String, String> webEventIdByPrimry,
			HashMap<String, ArrayList<ItemUserProperties>> itemUserRecords)
	{
		ArrayList<ATLAlert> alertList = new ArrayList<ATLAlert>();
		if (yourCompletedHash!=null && yourCompletedHash.size()>0)
		{
		
//		ArrayList<ItemUserProperties> itemUserRecords;
		
		EventProperties event;
		ATLAlert alert ;  
		String primary = "";
		for (String alertAcceptedKey:yourCompletedHash.keySet())
		{
			alert = new ATLAlert();
//			itemUserRecords = yourMoveHash.get(alertAcceptedKey);
			
			if (itemUserRecords!=null && itemUserRecords.size()>0)
			{
				
				alert.eventList= yourCompletedHash.get(alertAcceptedKey);
				primary = alert.eventList.get(0).primaryWebEventId;
				primary = (primary==null || primary.equals(""))? alert.eventList.get(0).webEventId:
					primary;
				alert.itemUserList= (itemUserRecords.get(primary));
				
				alert.bookedEvents();
				alertList.add(alert);
			}
			
		
		}
			alertCellList_2.setToBookedList(alertList);
			bindingBookedAlertList();
		}
		//		bindDataToBookedAlertList();



	} 
	
	private synchronized void updateBookedAcceptAlert(HashMap<String, ItemUserProperties> acceptedRecords)
	{
		ArrayList<ATLAlert> alertList = new ArrayList<ATLAlert>();
		if (acceptedRecords!=null && acceptedRecords.size()>0)
		{

			ItemUserProperties itemUser;
			EventProperties event;
			ATLAlert alert ;
			for (String alertAcceptedKey:acceptedRecords.keySet())
			{
				alert = new ATLAlert();
				itemUser = acceptedRecords.get(alertAcceptedKey);
				event = itemUser.getEventAssociated();
				alert.eventList.add(event);
				alert.itemUserList.add(itemUser);
				alert.bookAcceptedEvents();
				alertList.add(alert);
			}
			alertCellList_2.setToBookedList(alertList);
			bindingBookedAlertList();
		}
		//		bindDataToBookedAlertList();



	}  
	
	
	
	private  void updateBookedDeclinedAlert(HashMap<String, ItemUserProperties> acceptedRecords)
	{
		ArrayList<ATLAlert> alertList = new ArrayList<ATLAlert>();
		ItemUserProperties itemUser;
		EventProperties event;
		ATLAlert alert ;
		if (acceptedRecords!=null && acceptedRecords.size()>0){
			for (String alertAcceptedKey:acceptedRecords.keySet())
			{
				alert = new ATLAlert();
				itemUser = acceptedRecords.get(alertAcceptedKey);
				event = itemUser.getEventAssociated();
				alert.eventList.add(event);
				alert.itemUserList.add(itemUser);
				alert.bookDeclinedEvents();
				alertList.add(alert);
			}
			alertCellList_2.setToDeclinedList(alertList);
			bindingBookedAlertList();
			//		bindDataToBookedAlertList();
		}
	}
	
	
	
	
//	@Override
//	public void getAllUserEventsCallBack(
//			HashMap<String, ArrayList<EventProperties>> allEventsRetrievedFromItemUser,
//			ArrayList<ItemUserProperties> allUserItemUsersPropertiesRefreshedList,
//			boolean success) {
//		// TODO Auto-generated method stu
////		Log.v("getAllUserEventsCallBack", "getAllUserEventsCallBack");
//		if (success) {
//			if (allEventsRetrievedFromItemUser != null
//					&& allEventsRetrievedFromItemUser.size() > 0) {
////				populateAlert(allEventsRetrievedFromItemUser,
////						allUserItemUsersPropertiesRefreshedList);
//				isRefreshing = false;
//				alertYourMoveList.onRefreshComplete();
//				alertPendingList.onRefreshComplete();
//				alertBookedList.onRefreshComplete();
//				// alertsList.onRefreshComplete();
//
//				Toast.makeText(alertsActivity.getApplicationContext(),
//						"Update Alert Successfully", Toast.LENGTH_LONG).show();
//
//			} else {
//				Toast.makeText(alertsActivity.getApplicationContext(),
//						"Update Alert Successfully", Toast.LENGTH_LONG).show();
//
//			}
//		} else {
//			Toast.makeText(alertsActivity.getApplicationContext(),
//					"Update Alert Unsuccessfully", Toast.LENGTH_LONG).show();
//
//		}
//
//	}
	
//	@Override
//	public void createCalendarEventCallBack(boolean success) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void getAllUserAnEventCallBack(
//			HashMap<String, ArrayList<EventProperties>> userEvents,
//			HashMap<String, ArrayList<String>> eventsMembers, boolean success) {
//		// TODO Auto-generated method stub
//
//	}
//
//
//	
//	private void populateAlert(
//			HashMap<String, ArrayList<EventProperties>> allEventsRetrievedFromItemUser,
//			ArrayList<ItemUserProperties> allUserItemUsersPropertiesRefreshedList) {
//		// TODO Auto-generated method stub
//
//		Iterator iterator = allEventsRetrievedFromItemUser.keySet().iterator();
//		String keyColumn;
//		ArrayList<EventProperties> valueColumn = null;
//		// ArrayList<String> webEventIds = new ArrayList<String>();
//		ArrayList<ATLAlert> alertList = new ArrayList<ATLAlert>();
//		while (iterator.hasNext()) {
//			keyColumn = (String) iterator.next();
//			valueColumn = allEventsRetrievedFromItemUser.get(keyColumn);
//			ATLAlert alert = new ATLAlert();
//			String web_event_id1 = "";
//			String web_event_id2 = "";
//			String web_event_id3 = "";
//
//			for (EventProperties event : valueColumn) {
//				switch (event.displayOrder) {
//				case 0:
//					web_event_id1 = event.webEventId;
//					break;
//				case 1:
//					web_event_id2 = event.webEventId;
//					break;
//				case 2:
//					web_event_id3 = event.webEventId;
//					break;
//				default:
//					break;
//				}
//				alert.eventList.add(event);
//			}
//
//			for (ItemUserProperties item : allUserItemUsersPropertiesRefreshedList) {
//				if (item.webItemId.equals(web_event_id1)
//						|| item.webItemId.equals(web_event_id2)
//						|| item.webItemId.equals(web_event_id3)) {
//					alert.itemUserList.add(item);
//				}
//			}
//			alert.load();
//			alertList.add(alert);
//		}
////		bindingDataByAlertList(alertList);
//
//	}
//
//	@Override
//	public void getEventByPrimaryWebEventIdCallBack(boolean success,
//			ArrayList<EventProperties> events,
//			ArrayList<ItemUserProperties> itemUsers) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void respondToEventInviteCallBack(boolean success) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void bookEventCallBack(boolean success) {
//		// TODO Auto-generated method stub
//
//	}



	public class TwoStateTextView extends RelativeLayout implements
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
//			if (state == IN_ACTIVE_STATE) {
				state = ACTIVE_STATE;
				imgTextView.setBackgroundResource(imgID1);
				imgTextView.setTextColor(ATLColor.BLACK);
//			}
		}

	}




	
	
	
	
	
	
	
	



	
	
	
	
	

}