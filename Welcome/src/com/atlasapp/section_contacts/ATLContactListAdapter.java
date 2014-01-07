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

package com.atlasapp.section_contacts;


import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atlasapp.model.ATLContactModel;
import com.atlasapp.section_appentry.R;
import com.atlasapp.section_main.ATLMultiSectionAdapter;



public class ATLContactListAdapter extends ATLMultiSectionAdapter {
	ArrayList<ATLContactModel> contactA;
	Context mContext;
	int totalSize;
	LinearLayout layout;

	public ATLContactListAdapter(ArrayList<ATLContactModel> a, Context ctx) {
		mContext = ctx;
		contactA = a;
		totalSize = contactA.size();
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

	@Override
	public View getAmazingView(int position, View convertView, ViewGroup parent) {
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
		View v = vi.inflate(R.layout.contact_list_cell, null);
		layout = (LinearLayout) v.findViewById(R.id.contactCellLayout);
		
		TextView contactDisplayName = (TextView) layout.findViewById(R.id.contactDisplayName);
		//ImageView imageContact = (ImageView) layout.findViewById(R.id.imageContact);
		ImageView imageAtlasUser = (ImageView) layout.findViewById(R.id.imageAtlasUser);
		
		final ATLContactModel contact = (ATLContactModel)this.getItem(position);
		contactDisplayName.setText(contact.displayName());
		//imageContact.setImageBitmap(contact.getImage());
		if (contact.getAtlasId()!=null && !contact.getAtlasId().equals("")){
			
			
			//setProfilePic(contact.getAtlasId());
			
			imageAtlasUser.setImageResource(R.drawable.tab_bar_icon_today_selected_2x);
		}
		if (contact.getAtlasId()!=null && !contact.getAtlasId().equals("")){
			
			
			setProfilePic(contact.getAtlasId());
			
			//imageAtlasUser.setImageResource(R.drawable.avatar_group);
		}
			
		layout.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				((ATLContactListActivity)mContext).selectContact(contact);	
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
		File FRIENDS_IMAGE_DIR = new File(Environment.getExternalStorageDirectory()
	            + "/Android/data/com.atlastpowered/files/Pictures/friendPics");
		String profilePicName = (atlasId!=null && !atlasId.equals(""))?
				atlasId:"";
		String destinationImagePath= "/"+profilePicName+".png";	
		ImageView imageAtlasUser = (ImageView) layout.findViewById(R.id.imageContact);
		imageAtlasUser.setImageResource(R.drawable.avatar_group);
		Bitmap image = null;
		if (!profilePicName.equals(""))
		{
			// Bitmap storedBitmap = null;
			 File PROFILE_PIC_PATH= new File (FRIENDS_IMAGE_DIR,destinationImagePath) ;
				if(PROFILE_PIC_PATH.exists())
				//String filePath = applicationController.IMAGE_DIR+"/"+profilePicName+".png";
					
		
				 image = BitmapFactory.decodeFile(PROFILE_PIC_PATH.getAbsolutePath());
				
				if (image!=null)
					imageAtlasUser.setImageBitmap(image);
			//	contactPhoto.setImageBitmap(storedBitmap);
			//	invitee.setImage(storedBitmap);
		}
		}

}
