package atlasapp.section_alerts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import atlasapp.common.ATLUser;
import atlasapp.common.UtilitiesProject;
import atlasapp.database.DatabaseConstants.EVENT_STATUS;
import atlasapp.database.DatabaseConstants.ITEM_TYPE_PRIORITY_ORDER;
import atlasapp.database.DatabaseConstants.ITEM_USER_TASK_STATUS;
import atlasapp.database.EventController;
import atlasapp.database.EventControllerCallBackInterface;
import atlasapp.database.EventProperties;
import atlasapp.database.ItemUserProperties;
import atlasapp.model.ATLEventCalendarModel;
import atlasapp.section_appentry.R;

public class ATLRespondMatrixListController extends FragmentActivity implements EventControllerCallBackInterface{
	
	private ATLRespondMatrixListAdapter adapter;
	private LayoutInflater mInflater;
	private ViewGroup mLayout;
	private EventController eventController;
	
	private ArrayList<EventProperties> event;
	private ArrayList<ItemUserProperties> itemUserRecords;
	ListView inviteesRespondList ;
	private ImageButton alt1Button;
	private ImageButton alt2Button;
	private ImageButton alt3Button;
	private int timePicked = 1;// default
	private ImageButton bookEventButton;
	private ImageButton okButton;
	private ImageButton idealButton;
	private ImageButton declineButton;
	
	
	private ArrayList<ItemUserProperties> otherItemUserRecords;
	private ItemUserProperties item1,item2,item3;
	
	private boolean isInviter = false;
	private ITEM_TYPE_PRIORITY_ORDER vote = ITEM_TYPE_PRIORITY_ORDER.OK;
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
			
			
			this.event = event;
			timePicked = 1;
			this.itemUserRecords = invitees;
//			Toast.makeText(this,"SIZE "+itemUserRecords.size(), 
//	                Toast.LENGTH_SHORT).show();
//			//
			isInviter = event.get(0).atlasId.equals(ATLUser.getParseUserID());
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
			
			mInflater = LayoutInflater.from(this);
			mLayout = (ViewGroup) mInflater.inflate(R.layout.alert_respond_matrix,
					null);
			setContentView(mLayout);
			ImageView closeBtn = (ImageView)findViewById(R.id.closeBtn);
			closeBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) 
				{
					finish();
				}
			});
			
			setAltHourFields();
			
			
			
			inviteesRespondList  = (ListView)findViewById(R.id.inviteeRespondList);
			refreshVoteView();
//			ArrayList<ItemUserProperties> itemUserRespondRecords = processRecords(itemUserRecords);
			
//			adapter = new ATLRespondMatrixListAdapter(event,itemUserRecords,this);
//			inviteesRespondList.setAdapter(adapter);
//			
			
			
			setAltButtons();
		}
		else
		{
			
		}
		
	}

	
	
	private void refreshVoteView()
	{
		ArrayList<ItemUserProperties> itemUserRespondRecords = (ArrayList<ItemUserProperties>) otherItemUserRecords.clone();
//		ArrayList<ItemUserProperties> itemUserRespondRecords = new ArrayList<ItemUserProperties>();
		if (item1!=null)
			itemUserRespondRecords.add(item1);
		if (item2!=null)
			itemUserRespondRecords.add(item2);
		if (item3!=null)
			itemUserRespondRecords.add(item3);
//		if (adapter!=null)
//			adapter.clear();
		int length = itemUserRecords.size();
		int size =(length/event.size());
//		adapter = new ATLRespondMatrixListAdapter(event,itemUserRespondRecords,this);
		if (adapter!=null)  
		adapter.update(itemUserRespondRecords);
		else
			adapter = new ATLRespondMatrixListAdapter(event,itemUserRespondRecords,this,size);
		inviteesRespondList.setAdapter(adapter);
		
//		adapter.notifyDataSetChanged(); 
		
	}
	
	
	private void setAltButtons()
	{

		alt1Button =  (ImageButton)findViewById(R.id.alt1Button);


		alt2Button =  (ImageButton)findViewById(R.id.alt2Button);


		alt3Button =  (ImageButton)findViewById(R.id.alt3Button);

		okButton =  (ImageButton)findViewById(R.id.altButtonOk);


		idealButton =  (ImageButton)findViewById(R.id.altButtonIdeal);


		declineButton =  (ImageButton)findViewById(R.id.altButtonDecline);

		bookEventButton =  (ImageButton)findViewById(R.id.bookEventButton);
		
		if (event.size()<3)
		{
			alt3Button.setVisibility(View.GONE);
			if (event.size()<2)
				alt2Button.setVisibility(View.GONE);
		}
		if (event.get(0).status.equals(EVENT_STATUS.NOT_THE_ONE) ||
				event.get(0).status.equals(EVENT_STATUS.THE_ONE) || !isInviter	)
		{
			alt1Button.setVisibility(View.GONE);
			alt2Button.setVisibility(View.GONE);
			alt3Button.setVisibility(View.GONE);
			bookEventButton.setVisibility(View.GONE);
			okButton.setVisibility(View.GONE);
			idealButton.setVisibility(View.GONE);
			declineButton.setVisibility(View.GONE);
			RelativeLayout bottomBarMenu = (RelativeLayout)findViewById(R.id.bottomBarMenu);
			bottomBarMenu.setVisibility(View.GONE);
		}
		if (isInviter)
		{
			okButton.setVisibility(View.GONE);
			idealButton.setVisibility(View.GONE);
			declineButton.setVisibility(View.GONE);
			
			RelativeLayout bottomBarMenu = (RelativeLayout)findViewById(R.id.altHoursPicksVotesLayout);
			bottomBarMenu.setVisibility(View.GONE);
			
		}
		
		
		
		alt1Button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				setOnClick(1);
				timePicked = 1;
				if (isInviter)
				{
//					ArrayList<ItemUserProperties> itemUserRecords = new ArrayList<ItemUserProperties>();
//					itemUserRecords.add(item1);
//					if (item2!=null)
//						itemUserRecords.add(item2);
//					if (item2!=null)
//						itemUserRecords.add(item3);
//					updateItemUserRecord();
//					item1.priorityOrder = ITEM_TYPE_PRIORITY_ORDER.OK;
//					if(item2!=null)
//					{
//						item2.priorityOrder = ITEM_TYPE_PRIORITY_ORDER.DECLINED;
//						item2.status = ITEM_USER_TASK_STATUS.DECLINED;
//						item2.receivedDateTime = new Date();
//						item2.modifiedDatetime = new Date();
//						item2.wasReceived = true;
//					}
				}
				
			}
		});
		if (event.size()>1)
			alt2Button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v)
				{
					setOnClick(2);
					timePicked = 2;
				}

			
			});
		if (event.size()==3)
			alt3Button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v)
				{
					setOnClick(3);
					timePicked = 3;
				}
			});
		
		
		
		okButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				switch(timePicked)
				{
				case 1: item1.priorityOrder = ITEM_TYPE_PRIORITY_ORDER.OK;
				item1.status = ITEM_USER_TASK_STATUS.ACCEPTED;
				break;
				case 2: item2.priorityOrder = ITEM_TYPE_PRIORITY_ORDER.OK;
				item2.status = ITEM_USER_TASK_STATUS.ACCEPTED;
				break;
				case 3: item3.priorityOrder = ITEM_TYPE_PRIORITY_ORDER.OK;
				item3.status = ITEM_USER_TASK_STATUS.ACCEPTED;
				break;
				}
				refreshVoteView();
			}
		});
			idealButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v)
				{
					switch(timePicked)
					{
					case 1: item1.priorityOrder = ITEM_TYPE_PRIORITY_ORDER.IDEAL;
					item1.status = ITEM_USER_TASK_STATUS.ACCEPTED;
					break;
					case 2: item2.priorityOrder = ITEM_TYPE_PRIORITY_ORDER.IDEAL;
					item2.status = ITEM_USER_TASK_STATUS.ACCEPTED;
					break;
					case 3: item3.priorityOrder = ITEM_TYPE_PRIORITY_ORDER.IDEAL;
					item3.status = ITEM_USER_TASK_STATUS.ACCEPTED;
					break;
					}
					refreshVoteView();
				}

			
			});
			declineButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v)
				{
					switch(timePicked)
					{
					case 1: item1.priorityOrder = ITEM_TYPE_PRIORITY_ORDER.DECLINED;
					item1.status = ITEM_USER_TASK_STATUS.DECLINED;
					break;
					case 2: item2.priorityOrder = ITEM_TYPE_PRIORITY_ORDER.DECLINED;
					item2.status = ITEM_USER_TASK_STATUS.DECLINED;
					break;
					case 3: item3.priorityOrder = ITEM_TYPE_PRIORITY_ORDER.DECLINED;
					item3.status = ITEM_USER_TASK_STATUS.DECLINED;
					break;
					}
					refreshVoteView();
				}
			});
		
		
		
		if (event.get(0).status.equals(EVENT_STATUS.PENDING)
				)
			
						
		bookEventButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				
				bookEvent(timePicked);
			}
		});
		else
			bookEventButton.setVisibility(View.GONE);
	}
//	protected void updateItemUserRecord(
//			) {
//		
//		switch (timePicked)
//		{
//		case 1:item1.priorityOrder = ITEM_TYPE_PRIORITY_ORDER.OK;
//			   item1.status = ITEM_USER_TASK_STATUS.ACCEPTED;
//			   item1.wasReceived = true;
//			break;
//		}
//		
//		item1.priorityOrder = ITEM_TYPE_PRIORITY_ORDER.OK;
//		if(item2!=null)
//		{
//			item2.priorityOrder = ITEM_TYPE_PRIORITY_ORDER.DECLINED;
//			item2.status = ITEM_USER_TASK_STATUS.DECLINED;
//			item2.receivedDateTime = new Date();
//			item2.modifiedDatetime = new Date();
//			item2.wasReceived = true;
//		}
//		
//	}

	// call back on bookEventCallBack method
	private void bookEvent(int timePicked)
	{
		ArrayList<ItemUserProperties> allItems = otherItemUserRecords;
		allItems.add(item1);
		if (item2!=null)
			allItems.add(item2);
		if (item3!=null)
			allItems.add(item3);
		/// updating inviter events records
//		if (event.get(0).atlasId.equals(ATLUser.getParseUserID())
//				|| allItems.size()/event.size()==2)
//		{
//			for (EventProperties eventAlt:event)
//			{
//				switch (eventAlt.displayOrder)
//				{
//				case 0:eventAlt.status = (item1.priorityOrder.equals(ITEM_TYPE_PRIORITY_ORDER.OK))? EVENT_STATUS.THE_ONE:
//					EVENT_STATUS.NOT_THE_ONE;
//				break;
//				case 1:eventAlt.status = (item2.priorityOrder.equals(ITEM_TYPE_PRIORITY_ORDER.OK))? EVENT_STATUS.THE_ONE:
//					EVENT_STATUS.NOT_THE_ONE;
//				break;
//				case 2:eventAlt.status = (item3.priorityOrder.equals(ITEM_TYPE_PRIORITY_ORDER.OK))? EVENT_STATUS.THE_ONE:
//					EVENT_STATUS.NOT_THE_ONE;
//				break;
//				
//				}
//				
//						
//			}
//		}
		
		eventController = new EventController();
		eventController.eventControllerCallBackListener = this;
		if (!isInviter)
		eventController.bookEvent(timePicked, event, allItems ,this );
		else
			eventController.bookEvent( event, allItems ,timePicked,this );
	}
	@Override
	public void bookEventCallBack(boolean success)
	{
		String message = "";
		if (success)
		{
			
			if (this.event.get(0).atlasId.equals(ATLUser.getParseUserID()))
			{
				message = event.get(0).title +" has been booked ";
				
			}
			else
			{
				message = "Your respond to "+event.get(0).title +" was sent ";
			}
		}
		else
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

	private void setOnClick(int altPicked)
	{
		switch (altPicked)
		{
		case 1:
			alt1Button.setImageResource(R.drawable.calendar_editevent_alt_btn_alttime_select);
			if (alt2Button!=null)
			alt2Button.setImageResource(R.drawable.calendar_editevent_alttimes_unselect_btn);
			if (alt3Button!=null)
			alt3Button.setImageResource(R.drawable.calendar_editevent_alttimes_unselect_btn);

			break;
		case 2:
			alt1Button.setImageResource(R.drawable.calendar_editevent_alttimes_unselect_btn);
			if (alt2Button!=null)
			alt2Button.setImageResource(R.drawable.calendar_editevent_alt_btn_alttime_select);
			if (alt3Button!=null)
			alt3Button.setImageResource(R.drawable.calendar_editevent_alttimes_unselect_btn);

			break;
		case 3:
			alt1Button.setImageResource(R.drawable.calendar_editevent_alttimes_unselect_btn);
			if (alt2Button!=null)
			alt2Button.setImageResource(R.drawable.calendar_editevent_alttimes_unselect_btn);
			if (alt3Button!=null)
			alt3Button.setImageResource(R.drawable.calendar_editevent_alt_btn_alttime_select);

			break;

		}
	}
	private void setAltHourFields() 
	{
		Date date;
		int i=0;
		int resId;
		TextView textView;  
		ArrayList<String> dateStringPref;
		String hourSetString = "";
		String daySetString="";
		for (EventProperties eventProp:event)
		{
			
			if (eventProp!=null)
			{
				date = eventProp.startDateTime;  
				
				if (date!=null)
				{
					dateStringPref = getDateStrings(date);
					i = eventProp.displayOrder;  
					
					if (dateStringPref!=null && dateStringPref.size()==2)
					{
						hourSetString = (dateStringPref.get(0)!=null)?dateStringPref.get(0):"";
						daySetString=(dateStringPref.get(1)!=null)?dateStringPref.get(1):"";
						resId = getResources().getIdentifier("alt" + (i+1)+"hourTop", "id", getPackageName());
						textView = (TextView) findViewById(resId);
						textView.setText(hourSetString);
						resId = getResources().getIdentifier("alt" + (i+1)+"hourBottom", "id", getPackageName());
						textView = (TextView) findViewById(resId);
						textView.setText(hourSetString);
						resId = getResources().getIdentifier("alt" + (i+1)+"dayTop", "id", getPackageName());
						textView = (TextView) findViewById(resId);
						textView.setText(daySetString);
						resId = getResources().getIdentifier("alt" + (i+1)+"dayBottom", "id", getPackageName());
						textView = (TextView) findViewById(resId);
						textView.setText(daySetString);
					}
				}
				
			}
			//i++;   
		}
//		
	}

	private ArrayList<String> getDateStrings(Date startDateTime)
	{
		
		ArrayList<String> hourAndDayString = new ArrayList<String>();
		
		if (startDateTime!=null)
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy/MMM/dd HH:mm:ss");
			Calendar modifiedCalendar = Calendar.getInstance();
			modifiedCalendar.setTime(startDateTime);

			String time_str = dateFormat.format(modifiedCalendar.getTime());
			String[] s = time_str.split(" ");
			String year_sys = (s[0].split("/")[0]);
			String month_sys = (s[0].split("/")[1]);
			String day_sys =(s[0].split("/")[2]);

			String hour_sys = (s[1].split(":")[0]);
			String min_sys = (s[1].split(":")[1]);
			
			
			hourAndDayString.add(hour_sys+":"+min_sys);
			hourAndDayString.add(month_sys+" "+day_sys);
			
		}
        
        return hourAndDayString;
		
	}
	private ArrayList<ItemUserProperties> processRecords(
			ArrayList<ItemUserProperties> itemUserRecords) {
		
		ArrayList<ItemUserProperties> respondRecords = new ArrayList<ItemUserProperties>();
		HashMap<String,ItemUserProperties> inviteeRespond = new HashMap<String,ItemUserProperties>();
		if (itemUserRecords!=null && itemUserRecords.size()>0)
		{
			String atlasId = "";         
			ITEM_USER_TASK_STATUS respond;
			EventProperties eventAssociated = event.get(0);
			String inviter = eventAssociated.atlasId;
			
			for (ItemUserProperties itemUserRecord:itemUserRecords)
			{
				if (itemUserRecord!=null)  
				{
					atlasId = itemUserRecord.atlasId;
					respond = itemUserRecord.status; 
					if (!inviteeRespond.containsKey(atlasId))
					{
						inviteeRespond.put(atlasId, itemUserRecord);
					}else
						if (eventAssociated.status.equals(EVENT_STATUS.PENDING) && atlasId.equals(inviter)) 
						{
							itemUserRecord.status = ITEM_USER_TASK_STATUS.SENT;
							inviteeRespond.put(atlasId, itemUserRecord);
						}
					if (!respond.equals(ITEM_USER_TASK_STATUS.DECLINED) && !inviteeRespond.get(atlasId).status.equals(ITEM_USER_TASK_STATUS.SENT))
					{
						inviteeRespond.put(atlasId, itemUserRecord);
						
					}
				}
			}
			
			if (inviteeRespond!=null && inviteeRespond.size()>0)
				for (String inviteeRecord:inviteeRespond.keySet())
					respondRecords.add(inviteeRespond.get(inviteeRecord));
		}

		return respondRecords;
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

}
