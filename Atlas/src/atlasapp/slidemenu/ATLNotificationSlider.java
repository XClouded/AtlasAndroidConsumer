package atlasapp.slidemenu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import atlasapp.common.ATLUser;
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

public class ATLNotificationSlider extends FragmentActivity implements View.OnClickListener {

	
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
	
	
	  private static final String STATE_MENUDRAWER = "atlasapp.slidemenu.ATLNotificationSlider.menuDrawer";
	    private static final String STATE_ACTIVE_VIEW_ID = "atlasapp.slidemenu.ATLNotificationSlider.activeViewId";

    private static final int MENU_OVERFLOW = 1;

    private MenuDrawer mNotificationDrawer;

//    private MenuAdapter mAdapter;
    private ListView mList;
    protected static Context currentContext;
    protected static Activity currentActivity;
    private int mActivePosition = -1;
    private String mContentText;
    private TextView mContentTextView;

    @Override
    public void onCreate(Bundle inState) {
        super.onCreate(inState);
        setMenuOnCreate(inState);
        
        
    }
    
    protected void setMenuOnCreate(Bundle inState)
    {
    	if (inState != null) {
            mActiveViewId = inState.getInt(STATE_ACTIVE_VIEW_ID);
        }

        context = getApplicationContext();
        mNotificationDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_CONTENT, Position.RIGHT);
      //  mNotificationDrawer.setContentView(R.layout.calendar_day_view_2);
        mNotificationDrawer.setMenuView(R.layout.menu_scrollview);

        MenuScrollView msv = (MenuScrollView) mNotificationDrawer.getMenuView();
        msv.setOnScrollChangedListener(new MenuScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                mNotificationDrawer.invalidate();
            }
        });    

        TextView activeView = (TextView) findViewById(mActiveViewId);
        if (activeView != null) {
            mNotificationDrawer.setActiveView(activeView);
//            mContentTextView.setText("Active item: " + activeView.getText());
        }

        // This will animate the drawer open and closed until the user manually drags it. 
        //Usually this would only be
        // called on first launch.
        mNotificationDrawer.peekDrawer();
        
        
    	initAttributes();
		setListener();
		bindingData();
    }
    private void initAttributes() {
		userNameTextView = (TextView) 
				findViewById(R.id.cal_select_user_name_textview);
		userAvatar = (ImageView) 
				findViewById(R.id.cal_select_user_avatar);

		todayCell = (View) 
				findViewById(R.id.slide_menu_table_row_today);
		calendarCell = (View) 
				findViewById(R.id.slide_menu_table_row_calendars);
		mapViewCell = (View) 
				findViewById(R.id.slide_menu_table_row_map_view);
		taskListCell = (View) 
				findViewById(R.id.slide_menu_table_row_tasks);
		contactCell = (View) 
				findViewById(R.id.slide_menu_table_row_contacts);

		findMyFriendCell = (View) 
				findViewById(R.id.slide_menu_table_row_find_friends);
		myProfileCell = (View) 
				findViewById(R.id.slide_menu_table_row_my_profile);
		settingsCell = (View) 
				findViewById(R.id.slide_menu_table_row_settings);

		calSettings = (ImageButton) 
				findViewById(R.id.slide_menu_calendars_settings);
		taskSettings = (ImageButton) 
				findViewById(R.id.slide_menu_tasks_settings);
		contactSettings = (ImageButton) 
				findViewById(R.id.slide_menu_contacts_settings);
		
		
	
	}

	private void setListener() {
		todayCell.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "Today \nComming soon",
						Toast.LENGTH_SHORT).show();
				
			}
		});
		calendarCell.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				toCalendar();
			}
		});
		mapViewCell.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "Map View \nComming soon",
						Toast.LENGTH_SHORT).show();
				
			}
		});
		taskListCell.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toTaskView();
				
			}
		});
		contactCell.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if ( !(currentActivity instanceof ATLContactListActivity) ) 
				{
					Intent intentTasks = new Intent(currentContext,
							ATLContactListActivity.class);
					intentTasks.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intentTasks);
					
					
					mNotificationDrawer.closeMenu();
					
//					currentActivity.overridePendingTransition(
//							R.anim.main_fragment_back_from_right,
//							R.anim.stand_by);
					currentActivity.finish();
					System.gc();

				}
				else
				{
					mNotificationDrawer.closeMenu();
				}

			}
		});
		findMyFriendCell.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intentTasks = new Intent(ATLNotificationSlider.this,
						FriendsFinder.class);
				intentTasks.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intentTasks);
				ATLNotificationSlider.this.overridePendingTransition(
						R.anim.main_fragment_back_from_right,
						R.anim.stand_by);
				ATLNotificationSlider.this.finish();
				System.gc();
				
			}
		});
		myProfileCell.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ( !(currentActivity instanceof ATLMyProfile) ) 
				{
				Intent intentTasks = new Intent(currentContext,
						ATLMyProfile.class);
				intentTasks.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intentTasks);
				mNotificationDrawer.closeMenu();
				currentActivity.finish();
				System.gc();
				}
				else
				{
					mNotificationDrawer.closeMenu();
				}
				
			}
		});
		settingsCell.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intentCalSettings = new Intent(ATLNotificationSlider.this,
						Settings.class);
				intentCalSettings.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intentCalSettings);
				ATLNotificationSlider.this.overridePendingTransition(
						R.anim.main_fragment_back_from_right,
						R.anim.stand_by);
				System.gc();
				
			}
		});

		calSettings.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intentCalSettings = new Intent(ATLNotificationSlider.this,
						ATLCalendarSettingsSelectCalendarActivity.class);
				intentCalSettings.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intentCalSettings);
				ATLNotificationSlider.this.overridePendingTransition(
						R.anim.main_fragment_back_from_right,
						R.anim.stand_by);
				ATLNotificationSlider.this.finish();
				System.gc();
				
			}
		});
		taskSettings.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "Tasks Settings \nComming soon",
						Toast.LENGTH_SHORT).show();
				
			}
		});
		contactSettings.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intentTasks = new Intent(ATLNotificationSlider.this,
						ATLContactSettingsActivity.class);
				intentTasks.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intentTasks);
				ATLNotificationSlider.this.overridePendingTransition(
						R.anim.main_fragment_back_from_right,
						R.anim.stand_by);
				ATLNotificationSlider.this.finish();
				System.gc();
				
			}
		});
		
		
		
	}

	protected void toTaskView() {

		
//			Intent intentTasks = new Intent(ATLNotificationSlider.this,
//					ATLTaskListActivity.class);
//			intentTasks.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
//					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intentTasks);
//			ATLNotificationSlider.this.overridePendingTransition(
//					R.anim.main_fragment_back_from_right,
//					R.anim.stand_by);
//			ATLNotificationSlider.this.finish();
//			System.gc();
		
	}
	protected void toCalendar() {
		
		if ( !(currentActivity instanceof CalendarDayView) ) 
		{
			Intent intentTasks = new Intent(currentContext,
					CalendarDayView.class);
			intentTasks.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intentTasks);
//			currentActivity.overridePendingTransition(R.anim.leave,R.anim.enter);
			mNotificationDrawer.closeMenu();
			 currentActivity.finish();
			System.gc();
		}
		else
		{
			mNotificationDrawer.closeMenu();
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
        mNotificationDrawer.restoreState(inState.getParcelable(STATE_MENUDRAWER));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_MENUDRAWER, mNotificationDrawer.saveState());
        outState.putInt(STATE_ACTIVE_VIEW_ID, mActiveViewId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mNotificationDrawer.toggleMenu();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        final int drawerState = mNotificationDrawer.getDrawerState();
        if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
            mNotificationDrawer.closeMenu();
            return;
        }

        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        mNotificationDrawer.setActiveView(v);
//        mContentTextView.setText("Active item: " + ((TextView) v).getText());
        mNotificationDrawer.closeMenu();
        mActiveViewId = v.getId();
    }
}

