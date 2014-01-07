package atlasapp.section_notes;




import atlasapp.section_appentry.R;
//import atlasapp.common.SlideOutHelper;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

public class ATLNoteSort extends FragmentActivity implements ATLNoteFragmentDelegate{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		boolean rightSwipe = false;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			rightSwipe = extras.getBoolean("rightSwipe");
		}
//		mSlideoutHelper = new SlideOutHelper(this, rightSwipe);
//		mSlideoutHelper.activate();
//		ATLNoteSortFragment fragment = new ATLNoteSortFragment();
//		fragment.delegate = this;
//		getSupportFragmentManager().beginTransaction()
//				.add(R.id.slideout_placeholder, fragment, "menu").commit();
//		mSlideoutHelper.open();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			mSlideoutHelper.close();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

//	public SlideOutHelper getSlideoutHelper() {
//		return mSlideoutHelper;
//	}
//
//	private SlideOutHelper mSlideoutHelper;

	@Override
	public void didChangeSortOrder(int[] p) {
		// TODO Auto-generated method stub

	}

}



