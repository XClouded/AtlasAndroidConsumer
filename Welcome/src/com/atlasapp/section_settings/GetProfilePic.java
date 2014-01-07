package com.atlasapp.section_settings;






import com.atlasapp.section_appentry.R;
import com.atlasapp.common.OnHeadlineSelectedListener;
import com.atlasapp.common.SlideOutHelper;
import com.atlasapp.section_appentry.Welcome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

public class GetProfilePic extends FragmentActivity implements OnHeadlineSelectedListener { 

	
	
	OnHeadlineSelectedListener mCallback;
	boolean fromRegister = true;
	  private int position = 2; // default cancel option 
	private String currentPhotoURI="";

    @Override
    public void onAttachFragment(Fragment activity) {
        super.onAttachFragment(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
        	
        
        	
        //    mCallback = (OnHeadlineSelectedListener) ((EditProfile)getActitvity());
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
	    super.onCreate(savedInstanceState);
	    
	    
	    
	    
	    boolean  rightSwipe = false;
	    Bundle extras = getIntent().getExtras(); 
	    if(extras !=null) {
	          rightSwipe = extras.getBoolean("rightSwipe");
	          fromRegister = extras.getString("from").equals("register");
	          currentPhotoURI = extras.getString(currentPhotoURI);
	    }
	    mSlideoutHelper = new SlideOutHelper(this, true);
	    mSlideoutHelper.activateElevator();
	    
	    
	    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
	    
	    
	    transaction.add(R.id.slideout_placeholder, new SelectProfilePicFragment(), "menu").commit();
	   
	    mSlideoutHelper.up();
	}
 
	
	 


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			
			mSlideoutHelper.down();
			
			 
			
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


	public SlideOutHelper getSlideoutHelper(){
		return mSlideoutHelper;
	}
	
	private SlideOutHelper mSlideoutHelper;

	@Override
	public void onArticleSelected(int position) {
		Intent i1;
		
		if (fromRegister)
			i1 = new Intent(GetProfilePic.this,
					 Welcome.class);
		else
			
		 i1 = new Intent(GetProfilePic.this,
				 EditProfile.class);
		 i1.putExtra("picture_from", position);
		 i1.putExtra("from", "register");
		 
		i1.putExtra("profile_path", currentPhotoURI);
        startActivity(i1);
		
		
	}

}
