//  ==================================================================================================================
//  ATLNotesListAdapter.java
//  AtlasConsumerAndroid
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-10-29 TAN:     init class to implement Notes View in simulate mode
//  ==================================================================================================================

package atlasapp.section_contacts;


import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import atlasapp.common.CurrentSessionFriendsList;
import atlasapp.common.DB;
import atlasapp.database.AtlasServerConnect;
import atlasapp.model.ATLContactModel;
import atlasapp.section_appentry.ATLSignIn;
import atlasapp.section_appentry.R;
import atlasapp.section_main.ATLMultiSectionAdapter;


public class ATLContactListAdapter extends ATLMultiSectionAdapter {
	ArrayList<ATLContactModel> contactA;
	Context mContext;   
	int totalSize;
	RelativeLayout layout;
	ArrayList<Boolean> selected;
	ArrayList<String> conatctsIdsPicked;
	public ATLContactListAdapter(ArrayList<ATLContactModel> a, Context ctx, ArrayList<String> conatctsIdsPicked) {
		mContext = ctx;
		contactA = a;
		totalSize = contactA.size();
		 selected = new ArrayList<Boolean>();
		 for (ATLContactModel contact:contactA)
			 selected.add(false); 
		 
		 
		 this.conatctsIdsPicked = conatctsIdsPicked;
		 
	}
	
	public void setPicked(int position)
	{
		 selected.set(position, false); 
	}
	@Override
	public int getCount() {
		return totalSize;
	}

	@Override
	public Object getItem(int position) {
		
		return contactA.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	protected void onNextPageRequested(int page) {

	}

	@Override
	protected void bindSectionHeader(View view, int position,
			boolean displaySectionHeader) {
//		if (displaySectionHeader) {
//			view.findViewById(R.id.header).setVisibility(View.VISIBLE);
//			TextView lSectionTitle = (TextView) view.findViewById(R.id.header);
//			lSectionTitle
//					.setText(getSections()[getSectionForPosition(position)]);
//		} else {
//			view.findViewById(R.id.header).setVisibility(View.GONE);
//		}
	}
	
	
	
//	public void setPickedViews(ArrayList<String> contactIdPicked)
//	{
//		if (contactIdPicked!=null && contactIdPicked.size()>0)
//		{
//			if (this.totalSize>0)
//			{
//				int i=0;
//				View v;
//				ATLContactModel contact;
//				ArrayList<String> picked = ((ATLContactListActivity)mContext).getContactsPicked();
//				if (picked!=null && picked.size()>0)
//					while (i<totalSize)
//					{
//						contact = (ATLContactModel)this.getItem(i);
//						if (picked.contains(Integer.toString(contact.getFriendId())))
//						{
//							v = this.getAmazingView(i, null, null);
//							ImageView picOk = (ImageView)v.findViewById(R.id.imageSelectedContact);
//							picOk.setVisibility(View.VISIBLE);
//
//						}
//					}
//			}
//		}
//	}

	
	private static ArrayList<View> pickedViews;
	
	
	@Override
	public View getAmazingView(final int position, final View convertView, ViewGroup parent) {
		//		ContactCell cell;
		//		if (convertView == null) {
//			cell = new ContactCell(mContext);
//			convertView = (View) cell;
//			convertView.setTag(cell);
//		} else {
//			cell = (ContactCell) convertView.getTag();
//		}
		LayoutInflater vi = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View v = vi.inflate(R.layout.contact_list_cell, null);
		layout = (RelativeLayout) v.findViewById(R.id.contactCellLayout);
		
		TextView contactDisplayName = (TextView) layout.findViewById(R.id.contactDisplayName);
		//ImageView imageContact = (ImageView) layout.findViewById(R.id.imageContact);
		ImageView imageAtlasUser = (ImageView) layout.findViewById(R.id.imageAtlasUser);
		
		ImageView imageAtlasUserLeft = (ImageView) layout.findViewById(R.id.imageContact);
		imageAtlasUserLeft.setImageResource(R.drawable.avatar2x);
		
		final ATLContactModel contact = (ATLContactModel)this.getItem(position);
		contactDisplayName.setText(contact.displayName());
		
		
//		if (contact.isPicked())
//		{
//			ImageView picOk = (ImageView)v.findViewById(R.id.imageSelectedContact);
//			picOk.setVisibility(View.VISIBLE);
//		}
		ImageView picOk = (ImageView)v.findViewById(R.id.imageSelectedContact);

		RelativeLayout pickedContactLayout = (RelativeLayout)layout.findViewById(R.id.contactCellLayout);
		 
		
		
		
		if(conatctsIdsPicked!=null && conatctsIdsPicked.contains(Integer.toString(contact.getFriendId())))
	        {
//			 pickedContactLayout.setBackgroundColor(Color.parseColor("#ff0000"));
				picOk.setVisibility(View.VISIBLE);
				picOk.setBackgroundResource(R.drawable.respond_matrix_decide_ideal);

				
				if (((ATLContactListActivity)mContext).conatctsIdsPicked==null)
					((ATLContactListActivity)mContext).conatctsIdsPicked  = new ArrayList<String>();
				if (!((ATLContactListActivity)mContext).conatctsIdsPicked.contains(Integer.toString(contact.getFriendId())))
				((ATLContactListActivity)mContext).conatctsIdsPicked.add(Integer.toString(contact.getFriendId()));

			 
	        }else
	        	
	        	picOk.setVisibility(View.GONE);
		
//		if (picOk.getVisibility()==View.VISIBLE )
//				{
//			if (((ATLContactListActivity)mContext).contactIdPicked==null)
//				((ATLContactListActivity)mContext).contactIdPicked  = new ArrayList<String>();
//			if (!((ATLContactListActivity)mContext).contactIdPicked.contains(Integer.toString(contact.getFriendId())))
//			((ATLContactListActivity)mContext).contactIdPicked.add(Integer.toString(contact.getFriendId()));
//				}
//		if (ATLContactListActivity.getContactsPicked()!=null && ATLContactListActivity.getContactsPicked().contains(contact.getFriendId()))
//		{
//		}
//		
		
		//imageContact.setImageBitmap(contact.getImage());
		
		if (contact.getAtlasId()!=null && !contact.getAtlasId().equals("") && contact.isAtlasUser()){
//			new GetImageAsyncTask(imageAtlasUserLeft, contact.getAtlasId()).execute();
			setProfilePic(contact.getAtlasId());
			imageAtlasUser.setImageResource(R.drawable.tab_bar_icon_today_selected);
		}
//		ATLContactModel contactI;
//		int i=0;
//		while (i<getCount())
//		{
//			 contactI = (ATLContactModel)this.getItem(i);
//			 if (contactI!=null && )
//				 
//		}
			
		layout.setOnClickListener(new OnClickListener(){
			@SuppressLint("ResourceAsColor")
			public void onClick(View v) {
				final ATLContactModel contact = (ATLContactModel)getItem(position);
				ImageView picOk = (ImageView)v.findViewById(R.id.imageSelectedContact);
				boolean subscribe =  (!picOk.isShown());
				if (contact!=null && contact.getFriendId()!=-1)
				((ATLContactListActivity)mContext).subscribeOrUnsubscribeContact(Integer.toString(contact.getFriendId()), subscribe);
			
//				v.setBackgroundColor(R.color.orange);
				((ATLContactListActivity)mContext).selectContact(contact,v);	
			}
		});
		
		
		
		layout.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
			
			@Override
			public void onSystemUiVisibilityChange(int visibility) {
				
				if (ATLContactListActivity.getContactsPicked()!=null && ATLContactListActivity.getContactsPicked().contains(contact.getFriendId()))
				{
					ImageView picOk = (ImageView)v.findViewById(R.id.imageSelectedContact);
					picOk.setVisibility(View.VISIBLE);
				}
				
			}
		});
		
		return layout;
		//return cell;
		//return convertView;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
		TextView lSectionHeader = (TextView) header;
		lSectionHeader.setText(getSections()[getSectionForPosition(position)]);
	}

	@Override
	public int getPositionForSection(int section) {
		return 0;
//		if (true) {
//			switch (section) {
//			case 0:
//
//				return 0;
//
//			case 1: {
//				return 2;
//			}
//			case 2: {
//				return 4;
//			}
//			case 3: {
//				return 5;
//			}
//
//			}
//		}
//		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		return 0;
//		if (true) {
//			if (position >= 5) {
//				return 3;
//			} else if (position >= 4) {
//				return 2;
//			} else if (position >= 2) {
//				return 1;
//			} else if (position >= 0) {
//				return 0;
//			}
//		}
//		return 0;
	}

	@Override
	public String[] getSections() {
//			String[] res = new String[4];
//			res[0] = null;
//			res[1] = "Yesterday";
//			res[2] = "Monday, October 22, 2012";
//			res[3] = "Tuesday, October 23, 2012";
//
//			return res;
		String[] res = new String[1];
		res[0] = null;
		return res;
	}
	
	
	private void setProfilePic(String atlasId) {	
		
//		ImageView contactPhoto = (ImageView) findViewById(R.id.contactImage);
		
		Log.v("ATLContactListAdapter","______0");
		File FRIENDS_IMAGE_DIR = new File(Environment.getExternalStorageDirectory()
	            + "/Android/data/com.atlastpowered/files/Pictures/friendPics");
		String profilePicName = (atlasId!=null && !atlasId.equals(""))?
				atlasId:"";
		Log.v("ATLContactListAdapter","______1");
		String destinationImagePath= "/"+profilePicName+".png";	
		ImageView imageAtlasUser = (ImageView) layout.findViewById(R.id.imageContact);
		imageAtlasUser.setImageResource(R.drawable.avatar_group);
		Bitmap image = null;
		Log.v("ATLContactListAdapter","______2");
		if (!profilePicName.equals(""))
		{
			// Bitmap storedBitmap = null;
			 File PROFILE_PIC_PATH= new File (FRIENDS_IMAGE_DIR,destinationImagePath) ;
			
			 Log.v("ATLContactListAdapter","______3");
			 if(PROFILE_PIC_PATH.exists()){
				//String filePath = applicationController.IMAGE_DIR+"/"+profilePicName+".png";
					
				 Log.v("ATLContactListAdapter","______4");
				 image = BitmapFactory.decodeFile(PROFILE_PIC_PATH.getAbsolutePath());
				 
				}
			 Log.v("ATLContactListAdapter","______5");
				if (image!=null){
					imageAtlasUser.setImageBitmap(image);
					Log.v("ATLContactListAdapter","______6");
				}
			//	contactPhoto.setImageBitmap(storedBitmap);
			//	invitee.setImage(storedBitmap);
		}
	}
	
	private void setProfilePic(String atlasId, ImageView imageAtlasUser) {	
		
//		ImageView contactPhoto = (ImageView) findViewById(R.id.contactImage);
		Log.v("ATLContactListAdapter","______0");
		File FRIENDS_IMAGE_DIR = new File(Environment.getExternalStorageDirectory()
	            + "/Android/data/com.atlastpowered/files/Pictures/friendPics");
		String profilePicName = (atlasId!=null && !atlasId.equals(""))?
				atlasId:"";
		Log.v("ATLContactListAdapter","______1");
		String destinationImagePath= "/"+profilePicName+".png";	
		imageAtlasUser.setImageResource(R.drawable.avatar_group);
		Bitmap image = null;
		Log.v("ATLContactListAdapter","______2");
		if (!profilePicName.equals(""))
		{
			// Bitmap storedBitmap = null;
			 File PROFILE_PIC_PATH= new File (FRIENDS_IMAGE_DIR,destinationImagePath) ;
			
			 Log.v("ATLContactListAdapter","______3");
			 if(PROFILE_PIC_PATH.exists()){
				//String filePath = applicationController.IMAGE_DIR+"/"+profilePicName+".png";
					
				 Log.v("ATLContactListAdapter","______4");
				 image = BitmapFactory.decodeFile(PROFILE_PIC_PATH.getAbsolutePath());
				 
				}
			 Log.v("ATLContactListAdapter","______5");
				if (image!=null){
					imageAtlasUser.setImageBitmap(image);
					Log.v("ATLContactListAdapter","______6");
				}
			//	contactPhoto.setImageBitmap(storedBitmap);
			//	invitee.setImage(storedBitmap);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	final class GetImageAsyncTask extends AsyncTask<Void, Void, Void> {
		private String atlasId = "";
		private ImageView mImgView;
		Bitmap image = null;
		
		public GetImageAsyncTask( ImageView imgView,  String atlId){
			mImgView = imgView;
			atlasId = atlId;
		}
		@Override
		protected Void doInBackground(final Void... params) {
			/* Load timezone. this is very slow - may take up to 3 seconds. */
//			ImageView contactPhoto = (ImageView) findViewById(R.id.contactImage);
			Log.v("ATLContactListAdapter","______0");
			File FRIENDS_IMAGE_DIR = new File(Environment.getExternalStorageDirectory()
		            + "/Android/data/com.atlastpowered/files/Pictures/friendPics");
			String profilePicName = (atlasId!=null && !atlasId.equals(""))?
					atlasId:"";
			Log.v("ATLContactListAdapter","______1");
			String destinationImagePath= "/"+profilePicName+".png";	
			
			Log.v("ATLContactListAdapter","______2");
			if (!profilePicName.equals(""))
			{
				// Bitmap storedBitmap = null;
				 File PROFILE_PIC_PATH= new File (FRIENDS_IMAGE_DIR,destinationImagePath) ;
				
				 Log.v("ATLContactListAdapter","______3");
				 if(PROFILE_PIC_PATH.exists()){
					//String filePath = applicationController.IMAGE_DIR+"/"+profilePicName+".png";
						
					 Log.v("ATLContactListAdapter","______4");
					 image = BitmapFactory.decodeFile(PROFILE_PIC_PATH.getAbsolutePath());
					 
					}
				 Log.v("ATLContactListAdapter","______5");
					
				//	contactPhoto.setImageBitmap(storedBitmap);
				//	invitee.setImage(storedBitmap);
				 
				 
				 
				 
				 
				 
				 
				 
				 
				 
				 
				 
				 
				 
				 
				 
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			mImgView.setImageResource(R.drawable.avatar_group);
			if (image!=null){
				mImgView.setImageBitmap(image);
				Log.v("ATLContactListAdapter","______6");
			}
		}

	}	 
	
	

}
