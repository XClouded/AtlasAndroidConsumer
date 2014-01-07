package com.atlasapp.section_settings;




import com.atlasapp.section_appentry.R;
import com.atlasapp.common.OnHeadlineSelectedListener;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SelectProfilePicFragment extends ListFragment {

	
	OnHeadlineSelectedListener mCallback;
	Context context;
  
	View mHeader;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      //  inflater.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
     //   inflater.inflate(R.layout.main, container);
    	
    	//context = inflater.getContext();
    	
		 
    	//View list_root = inflater.inflate(R.layout.fragment_list, null);
		
		
		mHeader = inflater.inflate(R.layout.list_header, null);
		
    	
        return super.onCreateView(inflater, container, savedInstanceState);
    }
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		
		if (mHeader!=null)
		{
			getListView().addHeaderView(mHeader);;
		}
		getListView().setBackgroundColor(Color.GRAY);
		
		setListAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, new String[] { "Choose from library", "Take Photo", "Cancel"}));
		getListView().setCacheColorHint(0);
		
		
		
		
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		((GetProfilePic)getActivity()).getSlideoutHelper().down();
		 mCallback.onArticleSelected(position); 
//		if (position==0) 
//			this.get
//			chooseFromLibrary() ;
//			else
//				takeAPhoto();
		
		
		
	}

	
}
