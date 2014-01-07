//  ==================================================================================================================
//  ATLMyProfileEmailsAdapter.java
//  ATLAS
//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================

package atlasapp.section_settings;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import atlasapp.common.ATLAlertDialogUtils;
import atlasapp.common.ATLUser;
import atlasapp.section_appentry.R;

interface ATLMyProfileEmailsAdapterDelegate{
	void didDatasetChanged();
	void onFocusToAdditionalEditTextView(EditText etxt);
}

public class ATLMyProfileEmailsAdapter extends BaseAdapter {
	String [] emailArr;
	ArrayList <String> emails = new ArrayList<String>();
	String emailArrString ="";
	int size;
	Context mContext;
	public ATLMyProfileEmailsAdapterDelegate delegate;

	public ATLMyProfileEmailsAdapter(Context ctx) {

		// TODO Auto-generated constructor stub
		mContext = ctx;
		SharedPreferences prefs = ctx.getSharedPreferences(
				 "com.atlasapp.UserSharePreference", ctx.MODE_PRIVATE);
		emailArrString = prefs.getString("emails", "");
		emails.clear();
		if(ATLUser.getEmail() != null){
			emails.add(ATLUser.getEmail());
		}
		
		if(emailArrString.length() > 3){
			emailArr = emailArrString.split("\\|");
			if(emailArr.length > 0){
				for(int i = 0; i< emailArr.length; i++){
					
					emails.add(emailArr[i]);
					
				}
			}
		}
		emails.add("Add additional email");
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		size = emails.size();// Add all options
		// Hard code for basic view phase
		return size;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return emails.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		AdditionalEmailCell cell;
		if (convertView == null) {
			cell = new AdditionalEmailCell(mContext);
			convertView = (View) cell;
			convertView.setTag(cell);
		} else {
			cell = (AdditionalEmailCell) convertView.getTag();
		}
		String cellData = emails.get(position);
		cell.setCellData(cellData,position);
		if(size == 1){
			cell.backgroundImg.setBackgroundResource(R.drawable.one_row_with_edges2x);
			cell.additionalEmailView.setVisibility(View.INVISIBLE);
		}
		else if(position == 0){
			cell.backgroundImg.setBackgroundResource(R.drawable.one_row_with_bottom_edges2x);
			cell.additionalEmailView.setVisibility(View.INVISIBLE);
			cell.emailTextView.setVisibility(View.VISIBLE);
		}else if(position == size-1){
			cell.backgroundImg.setBackgroundResource(R.drawable.one_row_with_top_edges2x);
			cell.additionalEmailView.setVisibility(View.VISIBLE);
			cell.emailTextView.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}
	
	class AdditionalEmailCell extends RelativeLayout {

		public TextView emailTextView;
		public EditText additionalEmailView;
		public View backgroundImg;

		public AdditionalEmailCell(Context context, AttributeSet attrs,
				int defStyle) {
			super(context, attrs, defStyle);
			// TODO Auto-generated constructor stub
			initView(context);
		}

		public void setCellData(final String cellData, final int position) {
			// TODO Auto-generated method stub
			emailTextView.setText(cellData);
			additionalEmailView.setHint(cellData);
			if(position != 0 && position != size -1){
				backgroundImg.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);

						dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int which) {
								emails.remove(position);
								notifyDataSetChanged();
								emailArrString = "";
								for (int idx = 1; idx < emails.size()-1; idx++){
									if(idx != emails.size()-2){
										emailArrString += emails.get(idx)+ "|";
									}
									else{
										emailArrString += emails.get(idx);
									}
								}
								SharedPreferences prefs = mContext.getSharedPreferences(
										 "com.atlasapp.UserSharePreference", mContext.MODE_PRIVATE);
								Editor editor = prefs.edit();
								editor.putString("emails", emailArrString);
								editor.commit();
								if(delegate != null){
									delegate.didDatasetChanged();
								}
							}
						});
						dialog.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										// User cancelled the dialog
										dialog.dismiss();
									}
								});
						dialog.setMessage("Do you want to remove \""+ cellData + "\"?");
						dialog.show();
					}
				});
			}
		}

		public AdditionalEmailCell(Context context, AttributeSet attrs) {
			super(context, attrs);
			// TODO Auto-generated constructor stub
			initView(context);
		}

		public AdditionalEmailCell(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			initView(context);
		}

		private void initView(Context context) {
			// TODO Auto-generated method stub
			LayoutInflater.from(context).inflate(R.layout.my_profile_email_list_cell,
					this, true);
			emailTextView = (TextView) findViewById(R.id.my_profile_email_text);
			additionalEmailView = (EditText) findViewById(R.id.my_profile_email_edittext);
			backgroundImg = (View) findViewById(R.id.my_profile_email_row_view);
			
			additionalEmailView
			.setOnEditorActionListener(new EditText.OnEditorActionListener() {

				@Override
				public boolean onEditorAction(TextView v, int actionId,
						KeyEvent event) {
					// TODO Auto-generated method stub
					if (actionId == EditorInfo.IME_ACTION_DONE) {
						String email = additionalEmailView.getText().toString();
						if(isEmailValid(email)){
							SharedPreferences prefs = mContext.getSharedPreferences(
									 "com.atlasapp.UserSharePreference", mContext.MODE_PRIVATE);
							Editor editor = prefs.edit();
							if(emailArrString != null && emailArrString.length() >3){
								emailArrString += "|" +  email;
							}
							else{
								emailArrString = email;
							}
							editor.putString("emails", emailArrString);
							editor.commit();
							notifyDataSetChanged();
							if(delegate != null){
								delegate.didDatasetChanged();
							}
						}
						else{
							ATLAlertDialogUtils.showAlert(mContext, "", "Invalid email!");
						}
						
						return true;
					}
					return false;
				}

			});
			additionalEmailView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(delegate != null){
						delegate.onFocusToAdditionalEditTextView((EditText)v);
					}
				}
			});
//			additionalEmailView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//				@Override
//				public void onFocusChange(View v, boolean hasFocus) {
//					// TODO Auto-generated method stub
//					if(hasFocus && delegate != null){
//						delegate.onFocusToAdditionalEditTextView();
//					}
//				}
//			});
		}
		boolean isEmailValid(CharSequence email) {
			   return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
		}
	}

}