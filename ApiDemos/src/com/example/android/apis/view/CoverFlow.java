package com.example.android.apis.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Gallery;
import android.widget.HorizontalScrollView;
import android.widget.AbsListView.OnScrollListener;

public class CoverFlow extends Gallery {
	CoverFlow other = null;
	int nTotalScrollX = 0;
	HorizontalScrollView chScroll;
	public boolean cascadeScroll = true;
	static int x = 0;
	public CoverFlow(Context context) {
		super(context);
		setAnimationDuration(400);
		this.setGravity(Gravity.LEFT);
		this.setStaticTransformationsEnabled(true);
	}

	public CoverFlow(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setGravity(Gravity.LEFT);
		this.setStaticTransformationsEnabled(true);
	}

	public CoverFlow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setGravity(Gravity.LEFT);
		this.setStaticTransformationsEnabled(true);   
	}
	public void setOther(HorizontalScrollView _other)
	{
		chScroll = _other;
	}
	public void setOther(CoverFlow _other)
	{
		other = _other;
	}
	public boolean isScrollingLeft(MotionEvent e1, MotionEvent e2){
		  return e2.getX() > e1.getX();
		}
	private int   getCenterOfGallery() 
	{
			return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2 + getPaddingLeft() + getPaddingLeft();
	}
  /*  @Override
    public boolean  onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY){
//    	setAnimationDuration(400);
//    	//int nDelta = (int)  ((isScrollingLeft(e1,e2)) ? distanceX* -1 : distanceX);
//    	nTotalScrollX  += distanceX;
//    	nTotalScrollX = nTotalScrollX < 0 ? 0 : nTotalScrollX;
//    	
//    	Log.i("", "[S]The delta X is : " + distanceX);
//    	Log.i(""," [S]The total X movement : " + nTotalScrollX);
//    	Log.i("","computeHorizontalScrollOffset : " + computeHorizontalScrollExtent ());
//    	
//    	chScroll.scrollBy((int)distanceX, 0);
      if(cascadeScroll)
      {
    	  other.cascadeScroll=false;
    	  other.onScroll(e1, e2, distanceX, distanceY);
    	  other.cascadeScroll=true;
      }
      super.onScroll(e1, e2, distanceX, distanceY);
	return true;
    }*/
//    
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//    	super.onTouchEvent(event);
//    	 if(cascadeScroll)
//	      {
//	    	  other.cascadeScroll=false;
//	    	  other.onTouchEvent(event);
//	    	  other.cascadeScroll=true;
//	      }
//    	return true;
//    }
//   
//    
    /*
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		setAnimationDuration(400);
		float distanceX = (float) (velocityX * 0.4);
		int nDirection  = (int)  ((isScrollingLeft(e1,e2)) ? 1 : 1);
		int nMaxWidth = super.getCount()*146;
		nTotalScrollX  -= (nDirection * distanceX);
    	nTotalScrollX = nTotalScrollX < 0 ? 0 : nTotalScrollX;
    	nTotalScrollX = nTotalScrollX > nMaxWidth ? nMaxWidth : nTotalScrollX;
    	Log.i("", "[V]The delta X is : " + distanceX);
    	Log.i("", "[V]The total X movement : " + nTotalScrollX);
    	Log.i("","computeHorizontalScrollOffset : " + computeHorizontalScrollExtent ());
    	chScroll.scrollTo(nTotalScrollX, 0);
    	if(cascadeScroll)
		
	      {
	    	  other.cascadeScroll=false;
	    	  other.onFling(e1, e2, velocityX, velocityY);
	    	  other.cascadeScroll=true;
	      }
		 super.onFling(e1, e2, velocityX, velocityY);
		 return false;
		 
	}*/
	@Override
	 public boolean   onSingleTapUp(MotionEvent e) {
		return false;
		 
	 }

}
