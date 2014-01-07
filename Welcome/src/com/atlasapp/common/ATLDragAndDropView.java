//  ==================================================================================================================
//  ATLDragAndDropView.java
//  ATLAS
//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================

package com.atlasapp.common;

import com.ericharlow.DragNDrop.DragListener;
import com.ericharlow.DragNDrop.DropListener;
import com.ericharlow.DragNDrop.RemoveListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ATLDragAndDropView extends RelativeLayout implements View.OnTouchListener{

	public ATLDragAndDropView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ATLDragAndDropView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public ATLDragAndDropView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	


	boolean mDragMode;

	int mStartPosition;
	int mEndPosition;
	int mDragPointOffset;		//Used to adjust drag view location
	
	ImageView mDragView;
	GestureDetector mGestureDetector;
	
	DropListener mDropListener;
	RemoveListener mRemoveListener;
	DragListener mDragListener;
		
	public void setDropListener(DropListener l) {
		mDropListener = l;
	}

	public void setRemoveListener(RemoveListener l) {
		mRemoveListener = l;
	}
	
	public void setDragListener(DragListener l) {
		mDragListener = l;
	}

	// title bar height
    int statusBarHeight = (int) Math.ceil(12.5 * getContext().getResources().getDisplayMetrics().density);
    // top menu bar height
    static public int topMenuBarHeight = 0;
    static public boolean isAtLeft = false;
    static public boolean isAtRight = false;
    static public boolean isTouched = false;
    long startTouchDownTime = 0;
    long endTouchDownTime = 0;
    public int LEFTRIGHT_WINDOW_WIDTH = (int) Math.ceil(310 * getContext().getResources().getDisplayMetrics().density);
    @Override
	public boolean onTouch(View v, MotionEvent ev) {
		// TODO Auto-generated method stub
    	final int action = ev.getAction();
		final int x = (int) ev.getX();
		final int y = (int) ev.getY();	
		
		if (action == MotionEvent.ACTION_DOWN && y <= topMenuBarHeight) {
			mDragMode = true;
		}

		if (!mDragMode) 
			return super.onTouchEvent(ev);

		switch (action) {
			case MotionEvent.ACTION_DOWN:{
				startTouchDownTime = System.currentTimeMillis();
				if(isAtLeft){
					mStartPosition = x + LEFTRIGHT_WINDOW_WIDTH;
				}else if(isAtRight){
					mStartPosition = x - LEFTRIGHT_WINDOW_WIDTH;
				}else{
					mStartPosition = x;
				}
				startDrag(x, y);// default is all view
				drag(x-mStartPosition,statusBarHeight);// replace 0 with x if desired
			}
				break;
			case MotionEvent.ACTION_MOVE:
				drag(x-mStartPosition,statusBarHeight);// replace 0 with x if desired
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
			default:
				endTouchDownTime = System.currentTimeMillis(); 
				
				if(isAtLeft){
					mEndPosition = x + LEFTRIGHT_WINDOW_WIDTH;
					// move back the screen
					mStartPosition -= 2*LEFTRIGHT_WINDOW_WIDTH;
					mEndPosition -= 2*LEFTRIGHT_WINDOW_WIDTH;
				}else if(isAtRight){
					mEndPosition = x - LEFTRIGHT_WINDOW_WIDTH;
					// move back the screen
					mStartPosition += 2*LEFTRIGHT_WINDOW_WIDTH;
					mEndPosition += 2*LEFTRIGHT_WINDOW_WIDTH;
					
				}else{
					mEndPosition = x;
				}

				if(endTouchDownTime - startTouchDownTime <= 150){
					mDragMode = false;
					
					isTouched = true;
					stopDrag(mStartPosition, mEndPosition);// do not destroy mDragView here
					if (mDropListener != null){
						 mDropListener.onDrop(mStartPosition, mEndPosition);
					}
				}else{
					mDragMode = false;
					stopDrag(mStartPosition, mEndPosition);// do not destroy mDragView here
					if (mDropListener != null){
						 mDropListener.onDrop(mStartPosition, mEndPosition);
					}
					
				}
				startTouchDownTime = 0;
				endTouchDownTime = 0;
				break;
		}
		return true;
	}

    
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		final int x = (int) ev.getX();
		final int y = (int) ev.getY();	
		
		if (action == MotionEvent.ACTION_DOWN && y <= topMenuBarHeight) {
			mDragMode = true;
		}

		if (!mDragMode) 
			return super.onTouchEvent(ev);

		switch (action) {
			case MotionEvent.ACTION_DOWN:
				mStartPosition = x;
				startDrag(x , y);// default is all view
				drag(x-mStartPosition,statusBarHeight);// replace 0 with x if desired
				break;
			case MotionEvent.ACTION_MOVE:
				drag(x-mStartPosition,statusBarHeight);// replace 0 with x if desired
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
			default:
				mDragMode = false;
				stopDrag(mStartPosition, mEndPosition);// do not destroy mDragView here
				if (mDropListener != null){
					 mEndPosition = x;
	        		 mDropListener.onDrop(mStartPosition, mEndPosition);
				}
				
				break;
		}
		return true;
	}	
	
	// move the drag view
	private void drag(int x, int y) {
		if (mDragView != null) {
			WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) mDragView.getLayoutParams();
			layoutParams.x = x;
			layoutParams.y = y; // TAN change
			WindowManager mWindowManager = (WindowManager) getContext()
					.getSystemService(Context.WINDOW_SERVICE);
			mWindowManager.updateViewLayout(mDragView, layoutParams);
			
			if (mDragListener != null){
				mDragListener.onDrag(x, y, null);// change null to "this" when ready to use
			}
		}
	}

	// enable the drag view for dragging
	private boolean isOnDrag = false;

	private Bitmap bitmap;
	private void startDrag(int x, int y) {
//		stopDrag();
		isOnDrag = true;
		final View item = this;
		if (isAtLeft || isAtRight){
			
		}
		else{
	//		View item = getChildAt(itemIndex);
			if (item == null) return;
			item.setDrawingCacheEnabled(true);
			
	        // Create a copy of the drawing cache so that it does not get recycled
	        // by the framework when the list tries to clean up memory
	        bitmap = Bitmap.createBitmap(item.getDrawingCache());
	        
		} 
		Context context = getContext();
		mDragView = new ImageView(context);
        mDragView.setImageBitmap(bitmap); 
        
        WindowManager.LayoutParams mWindowParams = new WindowManager.LayoutParams();
        mWindowParams.gravity = Gravity.LEFT;
        mWindowParams.x = x; // Tan Change
        mWindowParams.y = statusBarHeight;// TAN change

        mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        mWindowParams.format = PixelFormat.TRANSLUCENT;
        mWindowParams.windowAnimations = 0;
        
        WindowManager mWindowManager = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.addView(mDragView, mWindowParams);
		
        this.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (mDragListener != null && isOnDrag)
					mDragListener.onStartDrag(item);
			}
		}, 100);
		
	}

	// destroy drag view
	private void stopDrag(int from, int to) {
		if (mDragView != null && isOnDrag) {
			if (mDragListener != null)
				mDragListener.onStopDrag(mDragView);
//            mDragView.setVisibility(GONE);
            WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
            wm.removeView(mDragView);
//            mDragView.setImageDrawable(null);
//            mDragView = null;
            isOnDrag = false;
        }
		
	}

	

}
