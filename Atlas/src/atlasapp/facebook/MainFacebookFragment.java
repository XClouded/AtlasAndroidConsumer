package atlasapp.facebook;

import com.facebook.LoginButton;
import com.facebook.SessionState;
//import com.facebook.android.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import atlasapp.section_appentry.R;

public class MainFacebookFragment extends Fragment{
	  
	
	private LoginButton authButton;

	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, 
	        Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.facebook_login, container, false);

	    authButton = (LoginButton) view.findViewById(R.id.authButton);
	    authButton.setApplicationId(getString(R.string.facebook_app_id));
	//    authButton.setBackground(getResources().getDrawable(R.drawable.welcome_btn_connect_fb));
	    
	    authButton.setBackgroundResource(R.drawable.welcome_btn_connect_fb);
	    authButton.setText("");


	    return view;
	}

	
	public void onSessionStateChange(SessionState state, Exception exception) {
	    if (state.isOpened()) {
	            // Logged In
	    	
	    } else if (state.isClosed()) {
	            // Logged Out
	    }
	}
}
