package atlasapp.slidemenu;

import java.io.File;

import com.actionbarsherlock.*;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.MenuDrawer.OnDrawerStateChangeListener;
import net.simonvt.menudrawer.Position;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.OnNavigationListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import atlasapp.common.ATLAnimationUtils;
import atlasapp.common.ATLDragAndDropView;
import atlasapp.common.ATLUser;
import atlasapp.database.AtlasServerConnect;
import atlasapp.section_alerts.ATLALertListController;
import atlasapp.section_alerts.ATLAlertFragment_2;
import atlasapp.section_appentry.AtlasApplication;
import atlasapp.section_appentry.R;
import atlasapp.section_calendar.CalendarDayView;
import atlasapp.section_contacts.ATLContactListActivity;
import atlasapp.section_contacts.ATLContactSettingsActivity;
import atlasapp.section_settings.ATLCalendarSettingsSelectCalendarActivity;
import atlasapp.section_settings.ATLMyProfile;
import atlasapp.section_settings.FriendsFinder;
import atlasapp.section_settings.Settings;
//import atlasapp.section_tasks.ATLTaskListActivity;

public abstract class ATLSlideMenu extends ATLActionBar implements View.OnClickListener,OnDrawerStateChangeListener, com.actionbarsherlock.app.ActionBar.OnNavigationListener {
	/** An array of strings to populate dropdown list */
	String[] actions = new String[] {
		"Today",
		"Calendar",
		"Map View",
		"Task Lists",
		"Contacts",
		"Find My Friends",
		"My Profile",
		"Settings"
	};   
	
	
	
	
	protected String todayDateTime = "Calendar";
	
	 private static final String STATE_MENUDRAWER2 = "atlasapp.slidemenu.ATLNotificationSlider.menuDrawer";
	    private static final String STATE_ACTIVE_VIEW_ID2 = "atlasapp.slidemenu.ATLNotificationSlider.activeViewId";

	
	
    private static final String STATE_MENUDRAWER = "atlasapp.slidemenu.ATLSlideMenu.menuDrawer";
    private static final String STATE_ACTIVE_VIEW_ID = "atlasapp.slidemenu.ATLSlideMenu.activeViewId";

    protected MenuDrawer mMenuDrawer;
//    private TextView mContentTextView;

    protected static Context currentContext;
    protected static Activity currentActivity;
    
    
    private int mActiveViewId;
    
    
    private TextView userNameTextView;
    private ImageView userAvatar;

    private View todayCell;
    private View calendarCell;
    private View mapViewCell;
    private View taskListCell;
    private View contactCell;

    private View findMyFriendCell;  
    private View myProfileCell;
    private View settingsCell;

    private ImageButton calSettings;
    private ImageButton taskSettings;
    private ImageButton contactSettings;
	private AtlasApplication applicationController;
	private Context context;
	private ImageButton sortBtn;
	
	 private static String[] menuItems;
	
	private View customView;

	protected ImageButton rightActioBarBtn;
	
	protected ArrayAdapter<CharSequence> listAdapter;



	protected ImageButton doneActioBarBtn;
	protected ATLALertListController atlAlertController;




	protected ImageButton refreshActioBarBtn;
	
	protected static String fromNavigationListItem = "";
//	 private MenuDrawer mNotificationDrawer;
//	private int mActiveViewId2;
	
	
    @Override
    public void onCreate(Bundle inState) {
    	
    	///// Setting Action Bar
    	setTheme(R.style.AppTheme); //Used for theme switching in samples
//        requestWindowFeature(Window.FEATURE_PROGRESS);
//        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
//    	
//    	
    	
    	///////
    	
    	
    	
        super.onCreate(inState);
       
		AtlasServerConnect.subscribedToChannels(getApplicationContext(), "ID"+ATLUser.getParseUserID(), ATLALertListController.class);

        
        ///////
        setSupportProgressBarIndeterminateVisibility(false);
        setSupportProgressBarVisibility(false);
  	  	getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        
        LayoutInflater li = LayoutInflater.from(this);
	    customView = li.inflate(R.layout.custom_action_bar, null);
        getSupportActionBar().setCustomView(customView);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
  
        Context context = getSupportActionBar().getThemedContext();
        menuItems = getResources().getStringArray(R.array.menu_items);

        	
        listAdapter = ArrayAdapter.createFromResource(context, R.array.menu_items, R.layout.atl_spinner_item);
        listAdapter.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
        
        
        
  
        getSupportActionBar().setListNavigationCallbacks(listAdapter, this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false); 
        
        
        
        ////////
        setMenuOnCreate(inState);
        
        
//        TextView spinner = (TextView)findViewById(R.id.text1);
//        spinner.setText("Sharon");
        
    }
    /**
     * Called back from OnDrawerStateChangeListener
     */
    @Override
	public void onDrawerStateChange(int oldState, int newState)
    {
    	if (newState==mActiveViewId)
		  rightActioBarBtn.setVisibility(View.VISIBLE);
    	else
    		rightActioBarBtn.setVisibility(View.INVISIBLE);

    	
    }
    /**
	 * Options Menu<br>
	 * example toggle
	 */
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
//	
    protected void setMenuOnCreate(Bundle inState)
    {
    	if (inState != null) {
            mActiveViewId = inState.getInt(STATE_ACTIVE_VIEW_ID);
        }
    	
    	

        context = getApplicationContext();
        mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_CONTENT, Position.RIGHT);
      //  mMenuDrawer.setContentView(R.layout.calendar_day_view_2);
       
        atlAlertController = ATLALertListController.getInstance();
        atlAlertController.activity = this.currentActivity;
        atlAlertController.onCreate(inState);
//        mMenuDrawer.setMenuView(R.layout.menu_scrollview);
        
    //    mMenuView = LayoutInflater.from(context).inflate(layoutResId, mMenuContainer, false);
        mMenuDrawer.setMenuView(atlAlertController.getView());
        mMenuDrawer.setOnDrawerStateChangeListener(this);
        
        
//        MenuScrollView msv = (MenuScrollView) mMenuDrawer.getMenuView();
//        msv.setOnScrollChangedListener(new MenuScrollView.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                mMenuDrawer.invalidate();
//            }
//        });       

//        TextView activeView = (TextView) findViewById(mActiveViewId);
//        if (activeView != null) {
//            mMenuDrawer.setActiveView(activeView);
////            mContentTextView.setText("Active item: " + activeView.getText());
//        }

        // This will animate the drawer open and closed until the user manually drags it. 
        //Usually this would only be
        // called on first launch.
//        mMenuDrawer.peekDrawer();
        
        ////////////////
     

        /////////////
      
        
        /////////////
//    	initAttributes();
		setListener();
//		bindingData();
    }
   
    public void disableDrawer()
    {
        mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_NONE);
        rightActioBarBtn.setVisibility(View.INVISIBLE);
        
        
        
        listAdapter = ArrayAdapter.createFromResource(context, R.array.menu_items_empty, R.layout.atl_spinner_item);
        listAdapter.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
        
        
        getSupportActionBar().setListNavigationCallbacks(listAdapter, null);
        
    }
    
    public void setRefreshButtonEnable()
    {
    	 refreshActioBarBtn = (ImageButton) customView.findViewById(R.id.refreshActionBarBtn);
    	 refreshActioBarBtn.setVisibility(View.VISIBLE);
    }
    public void setDoneButtonEnable()
    {
    	 doneActioBarBtn = (ImageButton) customView.findViewById(R.id.doneActionBarBtn);
    	 doneActioBarBtn.setVisibility(View.VISIBLE);
    }
    
    
//    @Override
//    public void closeOptionsMenu() {
//    	 rightActioBarBtn.setVisibility(View.VISIBLE);
//        if (!getSherlock().dispatchCloseOptionsMenu()) {
//            super.closeOptionsMenu();
//           
//        }
//    }
//    @Override
//    public void onPanelClosed(int featureId, android.view.Menu menu) {
////        getSherlock().dispatchPanelClosed(featureId, menu);
//        super.onPanelClosed(featureId, menu);
//        rightActioBarBtn.setVisibility(View.VISIBLE);
//    }
//    private void initAttributes() {
//		userNameTextView = (TextView) 
//				findViewById(R.id.cal_select_user_name_textview);
//		userAvatar = (ImageView) 
//				findViewById(R.id.cal_select_user_avatar);
//
//		todayCell = (View) 
//				findViewById(R.id.slide_menu_table_row_today);
//		calendarCell = (View) 
//				findViewById(R.id.slide_menu_table_row_calendars);
//		mapViewCell = (View) 
//				findViewById(R.id.slide_menu_table_row_map_view);
//		taskListCell = (View) 
//				findViewById(R.id.slide_menu_table_row_tasks);
//		contactCell = (View) 
//				findViewById(R.id.slide_menu_table_row_contacts);
//
//		findMyFriendCell = (View) 
//				findViewById(R.id.slide_menu_table_row_find_friends);
//		myProfileCell = (View) 
//				findViewById(R.id.slide_menu_table_row_my_profile);
//		settingsCell = (View) 
//				findViewById(R.id.slide_menu_table_row_settings);
//
//		calSettings = (ImageButton) 
//				findViewById(R.id.slide_menu_calendars_settings);
//		taskSettings = (ImageButton) 
//				findViewById(R.id.slide_menu_tasks_settings);
//		contactSettings = (ImageButton) 
//				findViewById(R.id.slide_menu_contacts_settings);
//		
//		  
//	
//	}

	private void setListener() {
//		todayCell.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Toast.makeText(context, "Today \nComming soon",
//						Toast.LENGTH_SHORT).show();
//				
//			}
//		});
//		calendarCell.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				
//				toCalendar();
//			}
//		});
//		mapViewCell.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Toast.makeText(context, "Map View \nComming soon",
//						Toast.LENGTH_SHORT).show();
//				
//			}
//		});
//		taskListCell.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				toTaskView();
//				
//			}
//		});
//		contactCell.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				if ( !(currentActivity instanceof ATLContactListActivity) ) 
//				{
//					Intent intentTasks = new Intent(currentContext,
//							ATLContactListActivity.class);
//					intentTasks.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
//							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					startActivity(intentTasks);
//					
//					
//					mMenuDrawer.closeMenu();
//					
//					
////					currentActivity.overridePendingTransition(
////							R.anim.main_fragment_back_from_right,
////							R.anim.stand_by);
//					currentActivity.finish();
//					System.gc();
//
//				}
//				else
//				{
//					mMenuDrawer.closeMenu();
//					
//				}
//
//			}
//		});
//		findMyFriendCell.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intentTasks = new Intent(ATLSlideMenu.this,
//						FriendsFinder.class);
//				intentTasks.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
//						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intentTasks);
//				ATLSlideMenu.this.overridePendingTransition(
//						R.anim.main_fragment_back_from_right,
//						R.anim.stand_by);
//				ATLSlideMenu.this.finish();
//				System.gc();
//				
//			}
//		});
//		myProfileCell.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if ( !(currentActivity instanceof ATLMyProfile) ) 
//				{
//				Intent intentTasks = new Intent(currentContext,
//						ATLMyProfile.class);
//				intentTasks.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
//						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intentTasks);
//				
//				mMenuDrawer.closeMenu();
//				 
//				currentActivity.finish();
//				System.gc();
//				}
//				else
//				{
//					mMenuDrawer.closeMenu();
//					 
//				}
//				
//			}
//		});
//		settingsCell.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intentCalSettings = new Intent(ATLSlideMenu.this,
//						Settings.class);
//				intentCalSettings.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
//						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intentCalSettings);
//				ATLSlideMenu.this.overridePendingTransition(
//						R.anim.main_fragment_back_from_right,
//						R.anim.stand_by);
//				System.gc();
//				
//			}
//		});
//
//		calSettings.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intentCalSettings = new Intent(ATLSlideMenu.this,
//						ATLCalendarSettingsSelectCalendarActivity.class);
//				intentCalSettings.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
//						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intentCalSettings);
//				ATLSlideMenu.this.overridePendingTransition(
//						R.anim.main_fragment_back_from_right,
//						R.anim.stand_by);
//				ATLSlideMenu.this.finish();
//				System.gc();
//				
//			}
//		});
//		taskSettings.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Toast.makeText(context, "Tasks Settings \nComming soon",
//						Toast.LENGTH_SHORT).show();
//				
//			}
//		});
//		contactSettings.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intentTasks = new Intent(ATLSlideMenu.this,
//						ATLContactSettingsActivity.class);
//				intentTasks.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
//						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intentTasks);
//				ATLSlideMenu.this.overridePendingTransition(
//						R.anim.main_fragment_back_from_right,
//						R.anim.stand_by);
//				ATLSlideMenu.this.finish();
//				System.gc();
//				
//			}
//		});
		
		 rightActioBarBtn = (ImageButton) customView.findViewById(R.id.rightActionItem);
		 rightActioBarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	 final int drawerState = mMenuDrawer.getDrawerState();
                 if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
                     mMenuDrawer.closeMenu();
                     
                 }
                 else
                 {
                	 mMenuDrawer.openMenu();
                	 
                 }
                
            }
        });
		
		
		
	}
	public static enum MENU_ITEM { 
		CALENDAR("Calendar"), TIMELINE("timeline"),TASKS("tasks"), CONTACTS("Contacts");
		private final String itemMenu;  

		private MENU_ITEM(String itemMenu) {  
			this.itemMenu = itemMenu;  
		}  
		
		public String getItemMenu() {  
			return itemMenu;  
		}  

	}
	private static MENU_ITEM lastPosition = MENU_ITEM.CALENDAR;
	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		//		Toast.makeText(context, menuItems[itemPosition],
		//					Toast.LENGTH_SHORT).show();




//		if(itemPosition == MENU_ITEM.TODAY.ordinal() )
//			toTodayView();
//		else  
			if(itemPosition== MENU_ITEM.CALENDAR.ordinal())
			{
//				if (todayDateTime!=null && !todayDateTime.equals(""))
//				{
//					TextView spinner = (TextView)findViewById(R.id.text1);
//					spinner.setText(todayDateTime); 
//				}
				toCalendar();
			}
			else
				if(itemPosition== MENU_ITEM.CONTACTS.ordinal())
					toContactsView();
				else
					if(itemPosition== MENU_ITEM.TASKS.ordinal())
						toTaskView();
					else
						if(itemPosition== MENU_ITEM.TIMELINE.ordinal())
							toTimeline();


		//		 Toast.makeText(context,"Selected: "+itemPosition, 
		//	                Toast.LENGTH_SHORT).show();

		//		 mSelected.setText("Selected: " + mLocations[itemPosition]);
		return true;
	}


//	protected void toTodayView()
//	{
//		Toast.makeText(context, "Today \nComming soon",
//				Toast.LENGTH_SHORT).show();
//	}

	protected void toContactsView()
	{
		if ( !(currentActivity instanceof ATLContactListActivity) ) 
		{
			lastPosition = MENU_ITEM.CONTACTS;
			Intent intentTasks = new Intent(currentContext,
					ATLContactListActivity.class);
			intentTasks.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intentTasks);

			currentActivity.finish();
			mMenuDrawer.closeMenu();


			//			currentActivity.overridePendingTransition(
			//					R.anim.main_fragment_back_from_right,
			//					R.anim.stand_by);

			System.gc();

		}
		else
		{
			mMenuDrawer.closeMenu();

		}

	}
	protected void toTimeline()
	{
		
		Toast.makeText(context, "Timeline \nComming soon",
				Toast.LENGTH_SHORT).show();
		TextView spinner = (TextView)findViewById(R.id.text1);
		spinner.setText(lastPosition.getItemMenu());
//		onNavigationItemSelected(lastPosition,this.listAdapter.getItemId(lastPosition));
//		listAdapter.setNotifyOnChange(true);
		//		if ( !(currentActivity instanceof ATLMyProfile) ) 
//		{
//			Intent intentTasks = new Intent(currentContext,
//					ATLMyProfile.class);
//			intentTasks.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
//					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intentTasks);
//
//			currentActivity.finish();
//			mMenuDrawer.closeMenu();
//
//
//			//			currentActivity.overridePendingTransition(
//			//					R.anim.main_fragment_back_from_right,
//			//					R.anim.stand_by);
//
//			System.gc();
//
//		}
//		else
//		{
//			mMenuDrawer.closeMenu();
//
//		}    

	}
	protected void toTaskView() {
		
		Toast.makeText(context, "Tasks \nComming soon",
				Toast.LENGTH_SHORT).show();
		TextView spinner = (TextView)findViewById(R.id.text1);
		spinner.setText(lastPosition.getItemMenu());
		//		if ( !(currentActivity instanceof ATLTaskListActivity) ) 
		//		{
//			Intent intentTasks = new Intent(ATLSlideMenu.this,
//					ATLTaskListActivity.class);
//			intentTasks.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
//					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intentTasks);
//
//			mMenuDrawer.closeMenu();
//			 
//			 currentActivity.finish();
//			System.gc();
//		}
//		else
//		{
//			mMenuDrawer.closeMenu();
//			 
//		}
		
	}
	protected void toCalendar() {
		
		if ( !(currentActivity instanceof CalendarDayView) ) 
		{
			lastPosition = MENU_ITEM.CALENDAR;
			Intent intentTasks = new Intent(currentContext,
					CalendarDayView.class);
			intentTasks.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intentTasks);
//			currentActivity.overridePendingTransition(R.anim.leave,R.anim.enter);
			
			mMenuDrawer.closeMenu();
			 
			 currentActivity.finish();
			System.gc();
		}
		else
		{
			mMenuDrawer.closeMenu();
			 
		}
		
	}
	private void bindingData() {
		userNameTextView.setText(ATLUser.getUserNameDisplay());
		   Bitmap storedBitmap = null;
		   File imgFile = applicationController.PROFILE_PIC_PATH;
			if(imgFile.exists())
	   		
				storedBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			if (storedBitmap!=null)
				userAvatar.setImageBitmap(storedBitmap);
	}
  
    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        mMenuDrawer.restoreState(inState.getParcelable(STATE_MENUDRAWER));
       
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_MENUDRAWER, mMenuDrawer.saveState());
        outState.putInt(STATE_ACTIVE_VIEW_ID, mActiveViewId);
        
       
        
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                mMenuDrawer.toggleMenu();
//                return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onBackPressed() {
        final int drawerState = mMenuDrawer.getDrawerState();
        rightActioBarBtn.setVisibility(View.VISIBLE);
        if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
        	
            mMenuDrawer.closeMenu();
          
            return;
        }
//     

        super.onBackPressed();
        
        
    }

    @Override
    public void onClick(View v) {
        mMenuDrawer.setActiveView(v);
//        mContentTextView.setText("Active item: " + ((TextView) v).getText());
      
        mMenuDrawer.closeMenu();
       
        mActiveViewId = v.getId();
    }
    
    
}

