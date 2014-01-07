//  ==================================================================================================================
//  ATLAlertDialogUtils.java
//  ATLAS
//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================

package com.atlasapp.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

interface ATLAlertDialogUtilsDelegate {
	void didTapButtonIndex(int index);
}

public class ATLAlertDialogUtils {
	static public final int OK_BUTTON = 0;
	static public final int CANCEL_BUTTON = 1;
	public ATLAlertDialogUtilsDelegate delegate;

	public void showTwoOnptionAlert(Context ctx, String msg) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);

		dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				if (delegate != null) {
					delegate.didTapButtonIndex(OK_BUTTON);
				}
			}
		});
		dialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// User cancelled the dialog
						if (delegate != null) {
							delegate.didTapButtonIndex(CANCEL_BUTTON);
						}
					}
				});
		dialog.setMessage("");
		dialog.show();
	}
	
	static public void showAlert(Context ctx,String title, String msg) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);

		dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				//TODO: do some thing here
			}
		});
		
		if(title != null && title.length() > 0){
			TextView titleTextView = new TextView(ctx);
			titleTextView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			dialog.setCustomTitle(titleTextView);
			titleTextView.setTextSize(26);
			titleTextView.setTextColor(ATLColor.WHITE);
			titleTextView.setGravity(Gravity.CENTER);
			titleTextView.setText(title);
		}
		dialog.setMessage(msg);
		AlertDialog dialog1 = dialog.show();
		TextView messageView = (TextView)dialog1.findViewById(android.R.id.message);
		messageView.setGravity(Gravity.CENTER);
		
	
		
		
		
	}

}
