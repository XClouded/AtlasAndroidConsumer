package atlasapp.section_alerts;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import atlasapp.database.EventProperties;
import atlasapp.database.ItemUserProperties;

public class ATLRespondMatrixListAdapter extends BaseAdapter{

	
	ArrayList<EventProperties> event;
	/// for each invitee ,only the record decided...
	ArrayList<ItemUserProperties> itemUserRespondRecords;
	Context ctx;
	int totalSize;
	
	public ATLRespondMatrixListAdapter(ArrayList<EventProperties> event,
			ArrayList<ItemUserProperties> itemUserRecords,
			Context ctx, int size) 
	{
		
		this.event = event;
		this.itemUserRespondRecords = itemUserRecords;
		this.ctx = ctx;
//		this.totalSize = itemUserRespondRecords.size();
		
//		int size  =  (event!=null && event.size()>0 )? 
//				itemUserRecords.size()/event.size():0;
		this.totalSize =size;// (event!=null && event.size()>0 )?
			//	itemUserRespondRecords.size()/event.size():0;
				
				
//		setCellsPositions();
		
		
	}

	

	@Override
	public int getCount() {
		return totalSize;
	}

	public void update(ArrayList<ItemUserProperties> itemUserRespondRecordsUpdated)
	{
//		if (itemUserRespondRecords!=null && itemUserRespondRecords.size()>0)
		itemUserRespondRecords = itemUserRespondRecordsUpdated;
		
		notifyDataSetChanged(); 
	}
	@Override
	public Object getItem(int position) {
		
		ArrayList<ItemUserProperties> userItemUser = new ArrayList<ItemUserProperties>();
		for (ItemUserProperties itemUser:itemUserRespondRecords)
		{
			if (itemUser.displayOrder==position)
			{
				userItemUser.add(itemUser);
			}
				
		}
		return userItemUser;  
//		return itemUserRespondRecords.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ATLRespondMatrixCell cell;
		if (convertView == null) {
			cell = new ATLRespondMatrixCell(ctx);
			convertView = (View) cell;
			convertView.setTag(cell);
		} else {
			cell = (ATLRespondMatrixCell) convertView.getTag();
		}
		
		ArrayList<ItemUserProperties> cellData = (ArrayList<ItemUserProperties>) this.getItem(position);
//		ArrayList<ItemUserProperties> userItemUser = new ArrayList<ItemUserProperties>();
//		for (ItemUserProperties itemUser:cellData)
//		{
//			if (itemUser.displayOrder==position)
//				userItemUser.add(itemUser);
//				
//		}
		
		cell.setRespondMatrixCell(cellData,event);
		return convertView;
	}

}
