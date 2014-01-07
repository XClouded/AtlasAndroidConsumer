package com.atlasapp.section_today;






import com.atlasapp.section_appentry.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TodayFragment extends Fragment {
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
//		 TextView textView = new TextView(getActivity());
//        // textView.setGravity(Gravity.TOP);
//        // Bundle args = getArguments();
//         textView.setText("SELECT CALENDAR");
		
	//	View headerView = (View)getActivity().(R.drawable.select_cal_select_calendars_view_title_bar_title_text);
         
		
		
		
		
//		 TextView[] calendars = new TextView[4];
//		 calendars[0].setText(" All");
//		 calendars[1].setText(" Business");
//		 calendars[2].setText(" Family");
//		 calendars[3].setText(" Health");
//		 calendars[0].setBackgroundResource(R.drawable.select_cal_cell);
//		 calendars[1].setBackgroundResource(R.drawable.select_cal_cell);
//		 calendars[2].setBackgroundResource(R.drawable.select_cal_cell);
//		 calendars[3].setBackgroundResource(R.drawable.select_cal_cell);
		
		String[] calendars = {" All", " Business", " Family"," Health" };
	//	getListView().addHeaderView(headerView);
		
	
//		setListAdapter(new ArrayAdapter<String>(getActivity(),
//				R.layout.select_calendars, R.id.label, calendars));
//		getListView().setCacheColorHint(0);
		
	
//		int count = getListView().getCount();
//		for (int i=0; i<count;i++) { 
//		 //    Log.i(TAG, "Item id = "+getListView().getItemIdAtPosition(i));
//		     View item = getActivity().findViewById((int) getListView().getItemIdAtPosition(i));
//		     View r = getActivity().findViewById(R.drawable.select_cal_cell);
//		     View v1 = getListView().getChildAt(i);
//		     item.setBackgroundResource(R.drawable.select_cal_cell);
//		   //  Log.i(TAG, "Condition = "+(v == null)); //Here I get the display as true
//		    // Log.i(TAG, "Condition1 = "+(v1 == null)); //Here I get again a true, so basically both are none!
//		}
	}

//	@Override
//	public void onListItemClick(ListView l, View v, int position, long id) {
//		super.onListItemClick(l, v, position, id);
//	
//		((TodaySort)getActivity()).getSlideoutHelper().close();
//	}
//	
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflater.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
        inflater.inflate(R.layout.select_calendars, container);
        
      //  Activity a = getActivity();
        
        
        
        return super.onCreateView(inflater, container, savedInstanceState);
    }
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        String List[] = {"All", "Business", "Family", "Health"}; 
//        setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.select_calendars, R.id.label, List));
//   
//        getListView().setCacheColorHint(0);
//    
//    }
	
	
	

	
}
