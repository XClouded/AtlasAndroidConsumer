package com.atlasapp.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;

public class LoadingView  extends View
{
	Context context;
	public LoadingView(Context context) {
		super(context);
		this.context = context;
	}
	  protected void onDraw(Canvas canvas) {  
	        super.onDraw(canvas);
			ProgressDialog progressDialog = ProgressDialog.show(context, "", "Loading. Please wait...", true);

	  }
	
	
}
