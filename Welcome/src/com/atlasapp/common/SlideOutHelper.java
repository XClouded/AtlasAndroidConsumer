package com.atlasapp.common;





import com.atlasapp.section_appentry.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager; 
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;


public class SlideOutHelper {

	private static Bitmap sCoverBitmap = null;
	private static int sWidth = -1;
	private  int prevX;
	public static int SLIDE_OUT_X = 50;
	
	private int currentX, prevY;

	public static void prepare(Activity activity, int id, int width) {
	
		if (sCoverBitmap != null) {
			sCoverBitmap.recycle();
		}
		Rect rectgle = new Rect();
		Window window = activity.getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(rectgle);
		int statusBarHeight = rectgle.top;

		ViewGroup v1 = (ViewGroup) activity.findViewById(id).getRootView();
		v1.setDrawingCacheEnabled(true);
		Bitmap source = Bitmap.createBitmap(v1.getDrawingCache());
		v1.setDrawingCacheEnabled(false);
		if (statusBarHeight != 0) {
			sCoverBitmap = Bitmap.createBitmap(source, 0, statusBarHeight, source.getWidth(), source.getHeight() - statusBarHeight);
			source.recycle();
		} else {
			sCoverBitmap = source;
		}
		sWidth = width;
		
		
		
		
		
	} 

	private TranslateAnimation mDragAnimation;
	private TranslateAnimation mUpAnimation;
	private TranslateAnimation mDownAnimation;

	public SlideOutHelper(Activity activity) {
		this(activity, false);
	}
	
	public SlideOutHelper(Activity activity, boolean reverse) {
		mActivity = activity;
		mReverse = reverse;
		
		
		
		prevX = (mReverse)? 0 : ((WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth() ;
	}

	
	public void activateElevator()
	{
		mActivity.setContentView(R.layout.slideout);
		mCover = (ImageView) mActivity.findViewById(R.id.slidedout_cover);
		mCover.setImageBitmap(sCoverBitmap);
		
		
		mCover.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				close();
			}
		});
		

		int y = (int) (sWidth * 1.0f);
		if (mReverse) {
			@SuppressWarnings("deprecation")
			final android.widget.AbsoluteLayout.LayoutParams lp = new android.widget.AbsoluteLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 0,y);
			mActivity.findViewById(R.id.slideout_placeholder).setLayoutParams(lp);
		} else{
			@SuppressWarnings("deprecation")
			final android.widget.AbsoluteLayout.LayoutParams lp = new android.widget.AbsoluteLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 0, 0);
			mActivity.findViewById(R.id.slideout_placeholder).setLayoutParams(lp);
		}
		initElevatorAnimations();
		
	}
	
	
	private void initElevatorAnimations() {
int displayHeight = ((WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
		
		//mReverse = true;
		final int shift = (mReverse ? -1 : 1) * (sWidth - displayHeight);
		
	
		
		mUpAnimation = new TranslateAnimation(
				
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, -shift
				
				);

		mDownAnimation = new TranslateAnimation(
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, shift
				
				);
		
		
		
		mUpAnimation.setDuration(DURATION_MS);
		mUpAnimation.setFillAfter(true);
		mUpAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mCover.setAnimation(null);
				@SuppressWarnings("deprecation")
				final android.widget.AbsoluteLayout.LayoutParams lp = new android.widget.AbsoluteLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 0,-shift);
				mCover.setLayoutParams(lp);
			}
		});

		mDownAnimation.setDuration(DURATION_MS);
		mDownAnimation.setFillAfter(true);
		mDownAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mActivity.finish();
				mActivity.overridePendingTransition(0, 0);
			}
		});
	
	}

	public void activate() {
		mActivity.setContentView(R.layout.slideout);
		mCover = (ImageView) mActivity.findViewById(R.id.slidedout_cover);
		mCover.setImageBitmap(sCoverBitmap);
		
		
		mCover.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				close();
			}
		});
		

		int x = (int) (sWidth * 1.0f);
		if (mReverse) {
			@SuppressWarnings("deprecation")
			final android.widget.AbsoluteLayout.LayoutParams lp = new android.widget.AbsoluteLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, x, 0);
			mActivity.findViewById(R.id.slideout_placeholder).setLayoutParams(lp);
		} else{
			@SuppressWarnings("deprecation")
			final android.widget.AbsoluteLayout.LayoutParams lp = new android.widget.AbsoluteLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 0, 0);
			mActivity.findViewById(R.id.slideout_placeholder).setLayoutParams(lp);
		}
		initAnimations();
	}

	public void open() {
		mCover.startAnimation(mStartAnimation);
	}

	public void close() {
		mCover.startAnimation(mStopAnimation);
	}
	public void dragCover() {
		mCover.startAnimation(mDragAnimation);
		
	}
	public void up() {
		mCover.startAnimation(mUpAnimation);
	}
	public void down() {
		mCover.startAnimation(mDownAnimation);
	}
	
	
	protected void initAnimations() {
		int displayWidth = ((WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
		
		//mReverse = true;
		final int shift = (mReverse ? -1 : 1) * (sWidth - displayWidth);
		
		 
	
//		mDragAnimation = new TranslateAnimation(
//				
//				TranslateAnimation.ABSOLUTE, 0,
//				TranslateAnimation.ABSOLUTE, shift,
//				TranslateAnimation.ABSOLUTE, 0,
//				TranslateAnimation.ABSOLUTE, 0
//				
//				
//				
//				);
//		mDragAnimation.setDuration(DURATION_MS);
//		mDragAnimation.setFillAfter(true);
//		mDragAnimation.setAnimationListener(new AnimationListener() {
//
//			@Override
//			public void onAnimationStart(Animation animation) {
//			}
//
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//			}
//
//			@Override
//			public void onAnimationEnd(Animation animation) {
//				////open....
//				mCover.setAnimation(null);
//				@SuppressWarnings("deprecation")
//				final android.widget.AbsoluteLayout.LayoutParams lp =
//				new android.widget.AbsoluteLayout.LayoutParams
//				(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, -shift, 0);
//				mCover.setLayoutParams(lp);
//				
//				
//				///close...
//				
//				mActivity.finish();
//				mActivity.overridePendingTransition(0, 0);
//			}
//		});
		
		mStartAnimation = new TranslateAnimation(
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, -shift,
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, 0
				);

		mStopAnimation = new TranslateAnimation(
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, shift,
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, 0
				);
		
		
		
		mStartAnimation.setDuration(DURATION_MS);
		mStartAnimation.setFillAfter(true);
		mStartAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mCover.setAnimation(null);
				@SuppressWarnings("deprecation")
				final android.widget.AbsoluteLayout.LayoutParams lp = new android.widget.AbsoluteLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, -shift, 0);
				mCover.setLayoutParams(lp);
			}
		});

		mStopAnimation.setDuration(DURATION_MS);
		mStopAnimation.setFillAfter(true);
		mStopAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mActivity.finish();
				mActivity.overridePendingTransition(0, 0);
			}
		});
	
	}

	private static final int DURATION_MS = 250;
	private ImageView mCover;
	private Activity mActivity;
	public boolean mReverse = false;
	private Animation mStartAnimation;
	private Animation mStopAnimation;
}