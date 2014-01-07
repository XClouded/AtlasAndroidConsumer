package com.example.android.apis.view;

import android.content.Context;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class FlingGallery  extends FrameLayout
{
	// Constants

	private final int swipe_min_distance = 120;
    private final int swipe_max_off_path = 250;
    private final int swipe_threshold_veloicty = 400;

    // Properties

	private int mViewPaddingWidth = 0;
    private int mAnimationDuration = 400;
    private float mSnapBorderRatio = 0.4f;
    private boolean mIsGalleryCircular = true;

    // Members

    private int mGalleryWidth = 0;
    private boolean mIsTouched = false;
    private boolean mIsDragging = false;
    private float mCurrentOffset = 0.0f;
    private int mFlingDirection = 0;
    private int mCurrentPosition = 0;
    private int mCurrentViewNumber = 0;

    private Context mContext;
    private Adapter mAdapter;
    private FlingGalleryView[] mViews;
    private FlingGalleryAnimation mAnimation;
    private GestureDetector mGestureDetector;
    private Interpolator mDecelerateInterpolater;

    public FlingGallery(Context context)
	{
		super(context);

		mContext = context;
		mAdapter = null;

        mViews = new FlingGalleryView[3];
        mViews[0] = new FlingGalleryView(0, this);
        mViews[1] = new FlingGalleryView(1, this);
        mViews[2] = new FlingGalleryView(2, this);

		mAnimation = new FlingGalleryAnimation();
		mGestureDetector = new GestureDetector(new FlingGestureDetector());
		mDecelerateInterpolater = AnimationUtils.loadInterpolator(mContext,
android.R.anim.decelerate_interpolator);
	    AnimationUtils.loadInterpolator(mContext,
android.R.anim.linear_interpolator);
	}

	public void setPaddingWidth(int viewPaddingWidth)
	{
		mViewPaddingWidth = viewPaddingWidth;
	}

	public void setAnimationDuration(int animationDuration)
	{
		mAnimationDuration = animationDuration;
	}

	public void setSnapBorderRatio(float snapBorderRatio)
	{
		mSnapBorderRatio = snapBorderRatio;
	}

	public void setIsGalleryCircular(boolean isGalleryCircular)
	{
		if (mIsGalleryCircular != isGalleryCircular)
		{
			mIsGalleryCircular = isGalleryCircular;

			if (mCurrentPosition == getFirstPosition())
			{
				// We need to reload the view immediately to the left to change it to circular view or blank
	
mViews[getPrevViewNumber(mCurrentViewNumber)].recycleView(getPrevPosition(mCurrentPosition));
			}

			if (mCurrentPosition == getLastPosition())
			{
				// We need to reload the view immediately to the right to change it to circular view or blank
	
mViews[getNextViewNumber(mCurrentViewNumber)].recycleView(getNextPosition(mCurrentPosition));
			}
		}
	}

	public int getGalleryCount()
	{
		return (mAdapter == null) ? 0 : mAdapter.getCount();
	}

	public int getFirstPosition()
	{
		return 0;
	}

	public int getLastPosition()
	{
		return (getGalleryCount() == 0) ? 0 : getGalleryCount() - 1;
	}

	private int getPrevPosition(int relativePosition)
	{
		int prevPosition = relativePosition - 1;

		if (prevPosition < getFirstPosition())
		{
			prevPosition = getFirstPosition() - 1;

			if (mIsGalleryCircular == true)
			{
				prevPosition = getLastPosition();
			}
		}

		return prevPosition;
	}

	private int getNextPosition(int relativePosition)
	{
		int nextPosition = relativePosition + 1;

		if (nextPosition > getLastPosition())
		{
			nextPosition = getLastPosition() + 1;

			if (mIsGalleryCircular == true)
			{
				nextPosition = getFirstPosition();
			}
		}

		return nextPosition;
	}

	private int getPrevViewNumber(int relativeViewNumber)
	{
		return (relativeViewNumber == 0) ? 2 : relativeViewNumber - 1;
	}

	private int getNextViewNumber(int relativeViewNumber)
	{
		return (relativeViewNumber == 2) ? 0 : relativeViewNumber + 1;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int
right, int bottom)
	{
		super.onLayout(changed, left, top, right, bottom);

		// Calculate our view width
		mGalleryWidth = right - left;

		if (changed == true)
		{
	    	// Position views at correct starting offsets
	    	mViews[0].setOffset(0, 0, mCurrentViewNumber);
	    	mViews[1].setOffset(0, 0, mCurrentViewNumber);
	    	mViews[2].setOffset(0, 0, mCurrentViewNumber);
	    }
	}

	public void setAdapter(Adapter adapter)
    {
    	mAdapter = adapter;
    	mCurrentPosition = 0;
        mCurrentViewNumber = 0;

        // Load the initial views from adapter
        mViews[0].recycleView(mCurrentPosition);
    	mViews[1].recycleView(getNextPosition(mCurrentPosition));
    	mViews[2].recycleView(getPrevPosition(mCurrentPosition));

    	// Position views at correct starting offsets
    	mViews[0].setOffset(0, 0, mCurrentViewNumber);
    	mViews[1].setOffset(0, 0, mCurrentViewNumber);
    	mViews[2].setOffset(0, 0, mCurrentViewNumber);
    }

	private int getViewOffset(int viewNumber, int relativeViewNumber)
	{
		// Determine width including configured padding width
		int offsetWidth = mGalleryWidth + mViewPaddingWidth;

		// Position the previous view one measured width to left
		if (viewNumber == getPrevViewNumber(relativeViewNumber))
		{
			return offsetWidth;
		}

		// Position the next view one measured width to the right
		if (viewNumber == getNextViewNumber(relativeViewNumber))
		{
			return offsetWidth * -1;
		}

		return 0;
	}

	void movePrevious()
	{
		// Note: We prevent movement if an animation is running so that we don't
		// get too ahead of ourselves. We could allow it but given that a second
		// movement while an existing animation is in progress would involve the
		// off-screen view we would need to update the animation code as well

		if (false == mAnimation.mIsRunningAnimation)
		{
			// Slide to previous view
			mFlingDirection = 1;
			processGesture();
		}
	}

	void moveNext()
	{
		if (false == mAnimation.mIsRunningAnimation)
		{
			// Slide to next view
			mFlingDirection = -1;
			processGesture();
		}
	}

	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event)
	 {
	    switch (keyCode)
	    {
	    case KeyEvent.KEYCODE_DPAD_LEFT:
	        movePrevious();
	        return true;

	    case KeyEvent.KEYCODE_DPAD_RIGHT:
	        moveNext();
	        return true;

	    case KeyEvent.KEYCODE_DPAD_CENTER:
	    case KeyEvent.KEYCODE_ENTER:
	    }

	    return super.onKeyDown(keyCode, event);
	}

	public boolean onGalleryTouchEvent(MotionEvent event)
	{
		boolean consumed = mGestureDetector.onTouchEvent(event);

		if (event.getAction() == MotionEvent.ACTION_UP)
		{
			if (mIsTouched || mIsDragging)
			{
				processScrollSnap();
				processGesture();
			}
		}

        return consumed;
    }

	void processGesture()
	{
		int newViewNumber = mCurrentViewNumber;
		int reloadViewNumber = 0;
		int reloadPosition = 0;

		mIsTouched = false;
		mIsDragging = false;

		if (mFlingDirection > 0)
		{
			if (mCurrentPosition > getFirstPosition() || mIsGalleryCircular ==
true)
			{
				// Determine previous view and outgoing view to recycle
				newViewNumber = getPrevViewNumber(mCurrentViewNumber);
				mCurrentPosition = getPrevPosition(mCurrentPosition);
				reloadViewNumber = getNextViewNumber(mCurrentViewNumber);
				reloadPosition = getPrevPosition(mCurrentPosition);
			}
		}

		if (mFlingDirection < 0)
		{
			if (mCurrentPosition < getLastPosition() || mIsGalleryCircular ==
true)
			{
				// Determine the next view and outgoing view to recycle
				newViewNumber = getNextViewNumber(mCurrentViewNumber);
				mCurrentPosition = getNextPosition(mCurrentPosition);
				reloadViewNumber = getPrevViewNumber(mCurrentViewNumber);
				reloadPosition = getNextPosition(mCurrentPosition);
			}
		}

		if (newViewNumber != mCurrentViewNumber)
		{
			mCurrentViewNumber = newViewNumber;

			// Reload outgoing view from adapter in new position
			mViews[reloadViewNumber].recycleView(reloadPosition);
		}

		// Ensure input focus on the current view
		mViews[mCurrentViewNumber].requestFocus();

		// Run the slide animations for view transitions
		mAnimation.prepareAnimation(mCurrentViewNumber);
		this.startAnimation(mAnimation);

		// Reset fling state
		mFlingDirection = 0;
	}

	void processScrollSnap()
	{
		// Snap to next view if scrolled passed snap position
		float rollEdgeWidth = mGalleryWidth * mSnapBorderRatio;
		int rollOffset = mGalleryWidth - (int) rollEdgeWidth;
		int currentOffset = mViews[mCurrentViewNumber].getCurrentOffset();

		if (currentOffset <= rollOffset * -1)
		{
			// Snap to previous view
			mFlingDirection = 1;
		}

		if (currentOffset >= rollOffset)
		{
			// Snap to next view
			mFlingDirection = -1;
		}
	}

	private class FlingGalleryView
	{
		private int mViewNumber;
		private FrameLayout mParentLayout;

		private FrameLayout mInvalidLayout = null;
		private LinearLayout mInternalLayout = null;
		private View mExternalView = null;

		public FlingGalleryView(int viewNumber, FrameLayout parentLayout)
		{
			mViewNumber = viewNumber;
			mParentLayout = parentLayout;

			// Invalid layout is used when outside gallery
			mInvalidLayout = new FrameLayout(mContext);
			mInvalidLayout.setLayoutParams(new LinearLayout.LayoutParams(
	                LayoutParams.MATCH_PARENT,
LayoutParams.MATCH_PARENT));

			// Internal layout is permanent for duration
			mInternalLayout = new LinearLayout(mContext);
			mInternalLayout.setLayoutParams(new LinearLayout.LayoutParams(
	                LayoutParams.MATCH_PARENT,
LayoutParams.MATCH_PARENT));

			mParentLayout.addView(mInternalLayout);
		}

		public void recycleView(int newPosition)
		{
			if (mExternalView != null)
			{
				mInternalLayout.removeView(mExternalView);
			}

			if (mAdapter != null)
			{
				mExternalView = mInvalidLayout;

				if (newPosition >= getFirstPosition() && newPosition <=
getLastPosition())
				{
					mExternalView = mAdapter.getView(newPosition, mExternalView,
mInternalLayout);
				}
			}

			if (mExternalView != null)
			{
				mInternalLayout.addView(mExternalView, new
LinearLayout.LayoutParams(
	                LayoutParams.MATCH_PARENT,
LayoutParams.MATCH_PARENT));
			}
		}

		public void setOffset(int xOffset, int yOffset, int
relativeViewNumber)
		{
			// Scroll the target view relative to its own position relative to currently displayed view
			mInternalLayout.scrollTo(getViewOffset(mViewNumber,
relativeViewNumber) + xOffset, yOffset);
		}

		public int getCurrentOffset()
		{
			// Return the current scroll position
			return mInternalLayout.getScrollX();
		}

		public void requestFocus()
		{
			mInternalLayout.requestFocus();
		}
	}

    private class FlingGalleryAnimation extends Animation
    {
    	private boolean mIsRunningAnimation;
    	private int mRelativeViewNumber;
    	private int mInitialOffset;
    	private int mTargetOffset;
    	private int mTargetDistance;

    	public FlingGalleryAnimation()
    	{
    		mIsRunningAnimation = false;
    		mRelativeViewNumber = 0;
        	mInitialOffset = 0;
        	mTargetOffset = 0;
        	mTargetDistance = 0;
    	}

    	void prepareAnimation(int relativeViewNumber)
    	{
    		mRelativeViewNumber = relativeViewNumber;

			// Note: In this implementation the targetOffset will always be zero
    		// as we are centering the view; we only include the calculations of
			// targetOffset and targetDistance for use in future implementations

			mInitialOffset = mViews[mRelativeViewNumber].getCurrentOffset();
			mTargetOffset = getViewOffset(mRelativeViewNumber,
mRelativeViewNumber);
			mTargetDistance = mTargetOffset - mInitialOffset;

			// Configure base animation properties
			this.setDuration(mAnimationDuration);
			this.setInterpolator(mDecelerateInterpolater);
		}

        @Override
        protected void applyTransformation(float interpolatedTime,
Transformation transformation)
        {
        	// Ensure interpolatedTime does not over-shoot then calculate new offset
        	interpolatedTime = (interpolatedTime > 1.0f) ? 1.0f :
interpolatedTime;
			int offset = mInitialOffset + (int) (mTargetDistance *
interpolatedTime);

			for (int viewNumber = 0; viewNumber < 3; viewNumber++)
			{
				// We only need to animate two views as the other view will always be off-screen
				// as long as we don't allow additional movements while an animation is in progress
				if ((mTargetDistance > 0 && viewNumber !=
getNextViewNumber(mRelativeViewNumber)) ||
					(mTargetDistance < 0 && viewNumber !=
getPrevViewNumber(mRelativeViewNumber)))
				{
					mViews[viewNumber].setOffset(offset, 0, mRelativeViewNumber);
				}
			}
        }

        @Override
        public boolean getTransformation(long currentTime,
Transformation outTransformation)
        {
        	mIsRunningAnimation = true;

        	if (super.getTransformation(currentTime, outTransformation)
== false)
        	{
        		// Perform final adjustment to offsets to cleanup animation
        		mViews[0].setOffset(mTargetOffset, 0, mRelativeViewNumber);
				mViews[1].setOffset(mTargetOffset, 0, mRelativeViewNumber);
				mViews[2].setOffset(mTargetOffset, 0, mRelativeViewNumber);

				mIsRunningAnimation = false;
        	}

        	// Cancel if the screen touched
        	if (mIsTouched || mIsDragging)
        	{
        		mIsRunningAnimation = false;
        	}

        	return mIsRunningAnimation;
        }
    }

	private class FlingGestureDetector extends
GestureDetector.SimpleOnGestureListener
    {
    	@Override
    	public boolean onDown(MotionEvent e)
    	{
    		// Stop animation
    		mIsTouched = true;

    		// Reset fling state
    		mFlingDirection = 0;
            return true;
    	}

    	@Override
    	public boolean onScroll(MotionEvent e1, MotionEvent e2, float
distanceX, float distanceY)
    	{
    		if (mIsDragging == false)
    		{
        		// Stop animation
    			mIsTouched = true;

    			// Reconfigure scroll
    			mIsDragging = true;
    			mFlingDirection = 0;
    			mCurrentOffset = mViews[mCurrentViewNumber].getCurrentOffset();
    		}

    		if (e2.getAction() == MotionEvent.ACTION_MOVE)
        	{
        		int scrollOffset = Math.round(mCurrentOffset + e1.getX() -
e2.getX());

        		// We can't scroll more than the width of our own frame layout
        		if (scrollOffset >= mGalleryWidth) scrollOffset =
mGalleryWidth;
        		if (scrollOffset <= mGalleryWidth * -1) scrollOffset =
mGalleryWidth * -1;

        		mViews[0].setOffset(scrollOffset, 0, mCurrentViewNumber);
    			mViews[1].setOffset(scrollOffset, 0, mCurrentViewNumber);
    			mViews[2].setOffset(scrollOffset, 0, mCurrentViewNumber);
        	}

            return false;
    	}

    	@Override
    	public boolean onFling(MotionEvent e1, MotionEvent e2, float
velocityX, float velocityY)
    	{
            if (Math.abs(e1.getY() - e2.getY()) <= swipe_max_off_path)
            {
                if (e2.getX() - e1.getX() > swipe_min_distance &&
Math.abs(velocityX) > swipe_threshold_veloicty)
                {
                	// Fling to previous view
                    mFlingDirection = 1;
                    Math.abs(velocityX);
                }

                if(e1.getX() - e2.getX() > swipe_min_distance &&
Math.abs(velocityX) > swipe_threshold_veloicty)
                {
                	// Fling to next view
                    mFlingDirection = -1;
                    Math.abs(velocityX);
                }

                if (mFlingDirection != 0)
                {
                	// Process immediately
                	processGesture();
                }
            }

            return false;
    	}

    	@Override
    	public void onLongPress(MotionEvent e)
    	{
    		// Finalise scrolling
    		mFlingDirection = 0;
            processGesture();
    	}

    	@Override
    	public void onShowPress(MotionEvent e)
    	{
    	}

    	@Override
    	public boolean onSingleTapUp(MotionEvent e)
    	{
    		// Reset fling state
    		mFlingDirection = 0;
            return false;
    	}
    }
}