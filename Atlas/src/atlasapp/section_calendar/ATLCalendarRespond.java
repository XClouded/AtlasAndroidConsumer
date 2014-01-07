package atlasapp.section_calendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableLayout.LayoutParams;
import atlasapp.common.ATLUser;
import atlasapp.common.CalendarUtilities;
import atlasapp.common.UtilitiesProject;
import atlasapp.database.EventController;
import atlasapp.database.EventControllerCallBackInterface;
import atlasapp.database.EventProperties;
import atlasapp.database.ItemUserProperties;
import atlasapp.database.DatabaseConstants.ITEM_TYPE_PRIORITY_ORDER;
import atlasapp.database.DatabaseConstants.ITEM_USER_TASK_STATUS;
import atlasapp.model.ATLCalendarModel;
import atlasapp.model.ATLContactModel;
import atlasapp.section_alerts.ATLRespondMatrixCell.EVENT_DECIDE;
import atlasapp.section_alerts.ATLALertListController;
import atlasapp.section_alerts.ATLRespondMatrixListController;
import atlasapp.section_appentry.R;

public class ATLCalendarRespond extends FragmentActivity implements EventControllerCallBackInterface, CalendarCellDelegateInterface{
	
	private Drawable drawableOK;
	private Drawable drawableIdeal;
	private Drawable drawableCant;
	public enum EVENT_VOTE {IDEAL("atl_event_respond_btn_small_ideal"),
		OKAY("atl_event_respond_btn_small_ok"),
		CANT("atl_event_respond_btn_small_cant");
		private final String eventDecideName;  
	  
			private EVENT_VOTE(String eventDecideName) {    
				this.eventDecideName = eventDecideName;  
			}  
	  
			public String getEventTypeName() {  
				return eventDecideName;  
			}
	}
	public static ArrayList<String> calendarInActiveNameArray = new ArrayList<String>();
	public static ArrayList<ATLCalendarModel> calListModel = new ArrayList<ATLCalendarModel>();
	
	private LayoutInflater mInflater;
	private ViewGroup mLayout;
//	private ArrayList<ATLCalendarModel> calListModel;
//	private ArrayList<String> calendarInActiveNameArray;
	private ListView listRespondEvent;
	private ATLCalCellList aCalCellList;
	private ATLCalendarListAdapter adaper;
	private EventController eventController;
	ArrayList<EventProperties> event;
	ArrayList<ItemUserProperties> invitees;
	private Date currentDate;
	private ViewGroup quickAddViewHolder;
	private CalendarQuickAddEventView quickAddView;
	private ImageView alt1Decide;
	private ImageView alt2Decide;
	private ImageView alt3Decide;
	private ImageButton idealBtn;
	private ImageButton okayBtn;
	private ImageButton cantBtn;
	private ImageView alt1Hour;
	private ImageView alt2Hour;
	private ImageView alt3Hour;
	
	private ItemUserProperties item1,item2,item3;
	
	private Date alt1,alt2,alt3;
	private ArrayList<ItemUserProperties> otherItemUserRecords;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
		 super.onCreate(savedInstanceState);
		 
		 Bundle extras = getIntent().getExtras();
			String eventPrimaryId = "";
	        if (extras != null) 
	        {
	        	eventPrimaryId = (extras.getString("primaryWebEventId")!=null)?extras.getString("primaryWebEventId"):"";
	        }
	        if (eventPrimaryId!=null && !eventPrimaryId.equals(""))
	        {
	        	retrieveEventProperties(eventPrimaryId);
	        }
	        else
	        {
	        	
	        }
		 
		 
		
	}

	private void retrieveEventProperties(String eventPrimaryId)
	{
		eventController = new EventController();
		eventController.eventControllerCallBackListener = this;
		eventController.getEventByPrimaryWebEventId(eventPrimaryId);
		
	}
	@Override
	public void getEventByPrimaryWebEventIdCallBack(boolean success,
			ArrayList<EventProperties> event,
			ArrayList<ItemUserProperties> invitees) {
		if (success)
		{
			
			otherItemUserRecords = new ArrayList<ItemUserProperties>();

			for (ItemUserProperties itemUser:invitees)
				if (itemUser.atlasId.equals(ATLUser.getParseUserID()))
				{
					EventProperties eventAlt = itemUser.getEventAssociated();
					if (eventAlt!=null)
					switch (eventAlt.displayOrder)
					{
					case 0:item1 = itemUser;
					break;
					case 1:item2 = itemUser;
					break;
					case 2:item3 = itemUser;
					break;
					}
				}
				else
					otherItemUserRecords.add(itemUser);
			
			setDefaultItemUser();
			
			mInflater = LayoutInflater.from(this);
			mLayout = (ViewGroup) mInflater.inflate(R.layout.atl_calendar_repond,
					null);
			 setContentView(mLayout);

			calListModel = ATLCalendarStore.loadCalendarList(this);
			calendarInActiveNameArray = ATLCalendarStore.loadInActiveCalendarNameList(this);
			
			this.event  = event;
			this.invitees = invitees;
			initEventListView();   
		}
		else
		{

		}
	}
	private void initEventListView()
	{

		if (event !=null && event.size()>0 && invitees!=null && invitees.size()> 0)
		{
			Resources res = getResources();
			int decideOk  = res.getIdentifier(EVENT_VOTE.OKAY.getEventTypeName() , "drawable", getPackageName());
			int decideIdeal  = res.getIdentifier(EVENT_VOTE.IDEAL.getEventTypeName() , "drawable", getPackageName());
			int decideCant  = res.getIdentifier(EVENT_VOTE.CANT.getEventTypeName() , "drawable", getPackageName());
			drawableOK = res.getDrawable(decideOk );
			drawableIdeal = res.getDrawable(decideIdeal );
			drawableCant = res.getDrawable(decideCant );

			
			  
			
			
			//			quickAddViewHolder = (ViewGroup) findViewById(R.id.calendarRespondListLayout);
//			quickAddView = new CalendarQuickAddEventView(this);
//			quickAddViewHolder.addView(quickAddView, new LayoutParams(
//					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//			quickAddView.delegate = this;
//			quickAddView.setVisibility(View.GONE);
			//			calListModel = ATLCalendarStore.loadCalendarList(this);
			//			calendarInActiveNameArray = ATLCalendarStore.loadInActiveCalendarNameList(this);
			listRespondEvent = (ListView) findViewById(R.id.calendarHourRespondList);
			aCalCellList = new ATLCalCellList(calListModel,calendarInActiveNameArray);
//			Toast.makeText(this,event.get(0).startDateTime.toString(), 
//					Toast.LENGTH_SHORT).show();
			currentDate = event.get(0).startDateTime;
			alt1 = currentDate;
			if (event.size()>1)
			alt2 = event.get(1).startDateTime;
			if (event.size()>2)
			alt3 = event.get(2).startDateTime;
			aCalCellList.calListDate =event.get(0).startDateTime ;
			aCalCellList.isListEditable = false;
			adaper = new ATLCalendarListAdapter(aCalCellList, this,event);
//			Log.v("ATLCalendarRespond",
//					"DONE" );
			listRespondEvent.setAdapter(adaper);
			
			initScreenView();
			
			
//			Toast.makeText(this,event.get(0).startDateTime.toString(), 
//					Toast.LENGTH_SHORT).show();
			listRespondEvent.post(new Runnable() {

				@Override
				public void run() {
				
					// TODO Auto-generated method stub
					handleListViewPosition(currentDate);
				}
			});
		}
		else
		{
			//// failed to load event
		}
		
	}
	private void initScreenView() 
	{
		ATLContactModel inviter = ATLContactModel.getContactByAtlasId(event.get(0).atlasId);
		/// set inviter image
		ImageView inviterImage = (ImageView)findViewById(R.id.eventInviterImage);
		Bitmap pic = UtilitiesProject.getProfilePic(event.get(0).atlasId);
		if (pic!=null)
			inviterImage.setImageBitmap(pic);
		/// set event invite title
		TextView title = (TextView)findViewById(R.id.eventInviterTitleTextView);
		String text = inviter.getFirstname()+" has invited you to ";
		title.setText(text);
		TextView eventTitle = (TextView)findViewById(R.id.eventEventTitleTextView);
		text = event.get(0).title;
		eventTitle.setText(text);
		
		
		alt1Hour = (ImageView)findViewById(R.id.alt1Hour);
		TextView eventHour1 = (TextView)findViewById(R.id.alt1HourTextView);
		eventHour1.setText(getAltHourString(alt1));
		alt1Hour.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setAltHourPicked(alt1,1);
			}
		});
		
		alt2Hour = (ImageView)findViewById(R.id.alt2Hour);
		TextView eventHour2 = (TextView)findViewById(R.id.alt2HourTextView);
		if (alt2!=null)
		{
			
			eventHour2.setText(getAltHourString(alt2));
			alt2Hour.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					setAltHourPicked(alt2,2);
				}
			});
		}
		else
		{
			alt2Hour.setVisibility(View.GONE);
		}
		alt3Hour = (ImageView)findViewById(R.id.alt3Hour);
		TextView eventHour3 = (TextView)findViewById(R.id.alt3HourTextView);
		if (alt2!=null)
		{
			
			eventHour3.setText(getAltHourString(alt3));
			alt3Hour.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					setAltHourPicked(alt3,3);
				}
			});
		}
		else
		{
			alt3Hour.setVisibility(View.GONE);
		}
		
		alt1Decide = (ImageView)findViewById(R.id.alt1Decide);
		alt1Decide.setBackgroundDrawable(drawableIdeal);
		alt2Decide = (ImageView)findViewById(R.id.alt2Decide);
		alt2Decide.setBackgroundDrawable(drawableOK);
		alt3Decide = (ImageView)findViewById(R.id.alt3Decide);
		alt3Decide.setBackgroundDrawable(drawableCant);
		
		
		////
		
		
		
		
		 //////
		 idealBtn = (ImageButton)findViewById(R.id.idealBtn);
		 idealBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setVoteImages(1);
				
			}
		});
		  okayBtn = (ImageButton)findViewById(R.id.OkayBtn);
		  okayBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					setVoteImages(2);
					
				}
			});
		  cantBtn = (ImageButton)findViewById(R.id.CantBtn);
		  cantBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					setVoteImages(3);
					
				}
			});
		  
		  ImageButton attendeesBtn  = (ImageButton)findViewById(R.id.attendeesBtn);
		  attendeesBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				String primaryWebEventId = event.get(0).primaryWebEventId;
				primaryWebEventId = (primaryWebEventId!=null && !primaryWebEventId.equals(""))?
						primaryWebEventId:(event.get(0).webEventId!=null && !event.get(0).webEventId.equals("")&&primaryWebEventId.equals(""))?
								event.get(0).webEventId:"";
				Intent intent = new Intent();
				intent.putExtra("primaryWebEventId",primaryWebEventId );
				intent.setClass(ATLCalendarRespond.this,
//						ATLRespondMatrixListController.class);
						ATLRespondMatrixListController.class);
				ATLCalendarRespond.this.startActivity(intent);
				((Activity) ATLCalendarRespond.this).overridePendingTransition(
						R.anim.in_from_bottom, R.anim.stand_by);
				
			}
		});
		  
		  ImageButton decideLaterBtn  = (ImageButton)findViewById(R.id.decideLaterBtn);
		  decideLaterBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		  ImageButton pickAndVoteBtn  = (ImageButton)findViewById(R.id.pickAndVoteBtn);
		  pickAndVoteBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
//				Log.v("PICK AND VOTE",
//						"BOOK EVENT " );
				pickAndVote();
				
			}
		});
		  
//		 
		  //////
	}
	
	protected void pickAndVote()
	{
		
//		Toast.makeText(this,"PICK AND VOTE ", 
//				Toast.LENGTH_SHORT).show();
//		Log.v("PICK AND VOTE",
//				"BOOK EVENT " );
		ArrayList<ItemUserProperties> allItems = otherItemUserRecords;
		allItems.add(item1);
		if (item2!=null)
			allItems.add(item2);
		if (item3!=null)
			allItems.add(item3);
		eventController = new EventController();
		eventController.eventControllerCallBackListener = this;
		eventController.bookEvent(altPicked, event, allItems ,this );
	}

	private void setDefaultItemUser()
	{
		item1.priorityOrder = ITEM_TYPE_PRIORITY_ORDER.IDEAL;
		item1.status = ITEM_USER_TASK_STATUS.ACCEPTED;
		
		item2.priorityOrder = ITEM_TYPE_PRIORITY_ORDER.OK;
		item2.status = ITEM_USER_TASK_STATUS.ACCEPTED;
		
		item3.priorityOrder = ITEM_TYPE_PRIORITY_ORDER.DECLINED;
		item3.status = ITEM_USER_TASK_STATUS.DECLINED;
		
	}
	protected void setVoteImages(int decide)
	{
		Drawable vote = (decide==3)?drawableCant:
						(decide==2)?drawableOK:
						(decide==1)?drawableIdeal:drawableOK;
		ITEM_TYPE_PRIORITY_ORDER priority = (decide==3)?ITEM_TYPE_PRIORITY_ORDER.DECLINED:
			(decide==2)?ITEM_TYPE_PRIORITY_ORDER.OK:
			(decide==1)?ITEM_TYPE_PRIORITY_ORDER.IDEAL:ITEM_TYPE_PRIORITY_ORDER.OK;
		
		
		ITEM_USER_TASK_STATUS status = (decide==3)? ITEM_USER_TASK_STATUS.DECLINED:
			
			ITEM_USER_TASK_STATUS.ACCEPTED;
		switch (altPicked)
		{
		case 1:alt1Decide.setBackgroundDrawable(vote);
		item1.priorityOrder = priority;
		item1.status = status;
			break;
		case 2:alt2Decide.setBackgroundDrawable(vote);
		item2.priorityOrder = priority;
		item2.status = status;
			break;
		case 3:alt3Decide.setBackgroundDrawable(vote);
		item3.priorityOrder = priority;
		item3.status = status;
			break;
		}
		
	}

	int altPicked = 1;
	protected void setAltHourPicked(final Date datePicked,int altPicked) 
	{
		if (datePicked!=null)
		{
			this.altPicked = altPicked;
			aCalCellList.calListDate =datePicked ;
			adaper = new ATLCalendarListAdapter(aCalCellList, this,event);
//			Log.v("ATLCalendarRespond",
//					"DONE" );
			listRespondEvent.setAdapter(adaper);
			listRespondEvent.post(new Runnable() {

				@Override
				public void run() {
					handleListViewPosition(datePicked);
				}
			});
			
		}
		
		
	}
	@Override
	public void conformCellData(ATLCalCellData cellData) {
//		Toast.makeText(this,"Confirm "+cellData.calCellDate.toString(), 
//				Toast.LENGTH_SHORT).show();
	}
	private void setEventCalendarCells(Date alt)
	{
		if (alt!=null)
		{
			
		}
	}
	
	
	private String getAltHourString(Date date)
	{
		String dateString= "";
		boolean am = true;
		int currentHourIndex,currentMinutesIndex;
		String month;
		if (date!=null)
		{
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			currentHourIndex = cal.get(Calendar.HOUR_OF_DAY);// 0 - 23
			currentMinutesIndex= cal.get(Calendar.MINUTE);
			am = (currentHourIndex<13);
			currentHourIndex = currentHourIndex%12;
			month = new SimpleDateFormat("MMM dd").format(cal.getTime());
			if (currentHourIndex!=-1 && month!=null)
			{
				dateString = (currentHourIndex<10)?"0"+currentHourIndex:currentHourIndex+"";
				dateString +=":";
				dateString += ((currentMinutesIndex>9)?currentMinutesIndex+"":"0"+currentMinutesIndex);
				dateString += am? "AM":"PM";
				dateString += "\n";
				dateString += month;
			}
			
		}
		return dateString;
	}
	private void handleListViewPosition(Date currentDate) {
		boolean isToday = CalendarUtilities.isToday(currentDate);
//		Date currentTime = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		if (isToday) {
			if (aCalCellList.calEventsListArray.size() > 0) {

				int currentHourIndex = cal.get(Calendar.HOUR_OF_DAY);// 0 - 23
				for (Object temp : aCalCellList.calEventsListArray) {
					ATLCalCellData cellData = (ATLCalCellData) temp;
					if (cellData.getCalCellHour() > 0) {
						if (cellData.getCalCellHour() > currentHourIndex) {
							currentHourIndex = cellData.getCalCellHour();
						}
					}
				}

				if (currentHourIndex > 0) {
					listRespondEvent.smoothScrollToPositionFromTop(currentHourIndex - 1, 0);
				}

			} else {
				int currentHourIndex = cal.get(Calendar.HOUR_OF_DAY);// 0 - 23
				if (currentHourIndex > 0) {
					listRespondEvent.smoothScrollToPositionFromTop(currentHourIndex - 1, 0);
				}
			}
		} else {
			if (aCalCellList.calEventsListArray.size() > 0) {
				int currentHourIndex = cal.get(Calendar.HOUR_OF_DAY);// 0 - 23
				for (Object temp : aCalCellList.calEventsListArray) {
					ATLCalCellData cellData = (ATLCalCellData) temp;
					if (cellData.getCalCellHour() > 0) {
						currentHourIndex = cellData.getCalCellHour();
					}
				}

				if (currentHourIndex > 0) {
					listRespondEvent.smoothScrollToPositionFromTop(currentHourIndex - 1, 0);
				}

			}

		}
	}
	@Override
	public void bookEventCallBack(boolean success) {
		String message = "";
		if (success)
		{
			message = "Your respond to "+event.get(0).title +" was sent ";

		}else
		{
			message = "Your respond to "+event.get(0).title +" failed  ";
		}
		alertUser("", message, this);
	}
	private  void alertUser(String messageTitle, String message, Context context)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(
				context).create();

		// Setting Dialog Title
		alertDialog.setTitle(messageTitle);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		// Setting Icon to Dialog
	//	alertDialog.setIcon(R.drawable.tick);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				
					closeAndRefresh();
				
				}
		});

		// Showing Alert Message
		alertDialog.show();
	}
	private void closeAndRefresh() 
	{
		ATLALertListController.getInstance().refresh();
		finish();
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
	public void getAllUserEventsCallBack(
			HashMap<String, ArrayList<EventProperties>> allEventsRetrievedFromItemUser,
			ArrayList<ItemUserProperties> allUserItemUsersPropertiesRefreshedList,
			boolean success) {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void respondToEventInviteCallBack(boolean success) {
		// TODO Auto-generated method stub
		
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

	@Override
	public void didTapToShowOffHours(int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestAddNewView(int hour, int minute) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didTouchToMoveEvent(int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didDeleteEventAtIndex(int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didDoubleTapToQuickAddEventAtIndex(int index) {
		// TODO Auto-generated method stub
		
	}

	

	

}
