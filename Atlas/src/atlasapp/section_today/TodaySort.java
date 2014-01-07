//package atlasapp.section_today;
//
//
//
//
//
//
//import atlasapp.common.SlideOutHelper;
//import atlasapp.section_appentry.R;
//
//import android.os.Bundle;
//import android.app.Activity;
//import android.support.v4.app.FragmentActivity;
//import android.view.KeyEvent;
//import android.view.Menu;
//
//public class TodaySort extends FragmentActivity{
//    
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//	    super.onCreate(savedInstanceState);
//	    boolean  rightSwipe = false;
//	    Bundle extras = getIntent().getExtras(); 
//	    if(extras !=null) {
//	          rightSwipe = extras.getBoolean("rightSwipe");
//	    }
//	    mSlideoutHelper = new SlideOutHelper(this, rightSwipe);
//	    mSlideoutHelper.activate();
//	    getSupportFragmentManager().beginTransaction().add(R.id.slideout_placeholder, new TodayFragment(), "menu").commit();
//	    
//	    mSlideoutHelper.open();
//	}
// 
//	   
//	   
//
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if(keyCode == KeyEvent.KEYCODE_BACK){
//			mSlideoutHelper.close();
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
//
//
//	public SlideOutHelper getSlideoutHelper(){
//		return mSlideoutHelper;
//	}
//	
//	private SlideOutHelper mSlideoutHelper;
//
//}