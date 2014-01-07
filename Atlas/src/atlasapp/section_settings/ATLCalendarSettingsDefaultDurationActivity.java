/**
 * 
 */
package atlasapp.section_settings;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import atlasapp.common.ATLTabbarViewDelegete;
import atlasapp.section_appentry.R;

/**
 * @author nghia
 *
 */
public class ATLCalendarSettingsDefaultDurationActivity extends FragmentActivity implements
ATLTabbarViewDelegete, View.OnClickListener {

	/**
	 * 
	 */
	public ATLCalendarSettingsDefaultDurationActivity() {
		// TODO Auto-generated constructor stub
	}
	private LayoutInflater mInflater;
	private ViewGroup mLayout;
	ImageButton btnSort;
	public boolean isFragmentShowing;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater = LayoutInflater.from(this);
		mLayout = (ViewGroup) mInflater.inflate(R.layout.calendar_settings_default_duration, null);
		setContentView(mLayout);
		
//		initAttributes();
//		setListener();
//		bindingData();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didTabToTabIndex(int index) {
		// TODO Auto-generated method stub
		
	}
}
