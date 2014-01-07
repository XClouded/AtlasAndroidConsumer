package atlasapp.section_calendar;




import atlasapp.section_appentry.R;
//import atlasapp.common.SlideOutHelper;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

public class CalendarDayViewSort extends FragmentActivity{
	public static CalendarDayViewFragment fragment = new CalendarDayViewFragment();
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    boolean  rightSwipe = false;
	    Bundle extras = getIntent().getExtras(); 
	    if(extras !=null) {
	          rightSwipe = extras.getBoolean("rightSwipe");
	    }
//	    mSlideoutHelper = new SlideOutHelper(this, rightSwipe);
//	    mSlideoutHelper.activate();
//	    getSupportFragmentManager().beginTransaction().add(R.id.slideout_placeholder, fragment, "menu").commit();
//	    mSlideoutHelper.open();
	}
 
	
	 


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
//			mSlideoutHelper.close();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


//	public SlideOutHelper getSlideoutHelper(){
//		return mSlideoutHelper;
//	}
//	
//	private SlideOutHelper mSlideoutHelper;

}