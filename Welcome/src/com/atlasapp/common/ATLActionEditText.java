//  ==================================================================================================================
//  ATLATLActionEditText.java
//  ATLAS
//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-12-09 TAN:     Create class to handle multi-lines edit text & done button
//  ==================================================================================================================

package com.atlasapp.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;


public class ATLActionEditText extends EditText {
	public ATLActionEditText(Context context) {
		super(context);
	}

	public ATLActionEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ATLActionEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
		InputConnection conn = super.onCreateInputConnection(outAttrs);
		outAttrs.imeOptions &= ~EditorInfo.IME_FLAG_NO_ENTER_ACTION;
		return conn;
	}
}