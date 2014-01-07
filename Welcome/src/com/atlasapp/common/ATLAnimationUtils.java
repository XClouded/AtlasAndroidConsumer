//  ==================================================================================================================
//  ATLAnimationUtils.java
//  ATLAS
//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================

package com.atlasapp.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class ATLAnimationUtils {

	private final static long QUICK_ADD_ANIMATION_DURATION = 500;
	
	static public Animation inAlpha(){
		AlphaAnimation alpha = new AlphaAnimation(0, 1);
		alpha.setDuration(100);
		alpha.setInterpolator(new AccelerateInterpolator());
		return alpha;
	}
	
	static public Animation outAlpha(){
		AlphaAnimation alpha = new AlphaAnimation(1, 0);
		alpha.setDuration(100);
		alpha.setInterpolator(new AccelerateInterpolator());
		return alpha;
	}

	static public Animation rotate_90() {
		RotateAnimation rotate_90 = new RotateAnimation(180, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotate_90.setDuration(500);
		rotate_90.setInterpolator(new LinearInterpolator());
		return rotate_90;

	}
	
	static public Animation rotate_45() {
		RotateAnimation rotate_45 = new RotateAnimation(-225, 0,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotate_45.setDuration(400);
		rotate_45.setInterpolator(new LinearInterpolator());
		return rotate_45;

	}

	static public Animation inFromRightAnimation() {

		Animation inFromRight = new TranslateAnimation(

		Animation.RELATIVE_TO_PARENT, +1.0f, Animation.RELATIVE_TO_PARENT,
				0.0f,

				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f

		);

		inFromRight.setDuration(200);

		inFromRight.setInterpolator(new AccelerateInterpolator());

		return inFromRight;

	}

	static public Animation inFromRightAnimation(int duration) {

		Animation inFromRight = new TranslateAnimation(

		Animation.RELATIVE_TO_PARENT, +1.0f, Animation.RELATIVE_TO_PARENT,
				0.0f,

				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f

		);

		inFromRight.setDuration(duration);

		inFromRight.setInterpolator(new AccelerateInterpolator());

		return inFromRight;

	}

	static public Animation inFromLeftAnimation() {

		Animation inFromLeft = new TranslateAnimation(

		Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT,
				0.0f,

				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f

		);

		inFromLeft.setDuration(200);

		inFromLeft.setInterpolator(new AccelerateInterpolator());

		return inFromLeft;

	}

	static public Animation outToLeftAnimation() {

		Animation outtoLeft = new TranslateAnimation(

		Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
				-1.0f,

				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f

		);

		outtoLeft.setDuration(200);

		outtoLeft.setInterpolator(new AccelerateInterpolator());

		return outtoLeft;

	}

	static public Animation outToRightAnimation() {

		Animation outtoRight = new TranslateAnimation(

		Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
				+1.0f,

				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f

		);

		outtoRight.setDuration(200);

		outtoRight.setInterpolator(new AccelerateInterpolator());

		return outtoRight;

	}

	static public Animation outToRightAnimation(int duration) {

		Animation outtoRight = new TranslateAnimation(

		Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
				+1.0f,

				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f

		);

		outtoRight.setDuration(duration);

		outtoRight.setInterpolator(new AccelerateInterpolator());

		return outtoRight;

	}

	static public Animation outToBottomAnimation() {

		Animation outtoRight = new TranslateAnimation(

		Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,

		Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, +1.0f

		);

		outtoRight.setDuration(900);

		outtoRight.setInterpolator(new LinearInterpolator());

		return outtoRight;

	}

	static public Animation outToBottomAnimation(long duration) {

		Animation outtoRight = new TranslateAnimation(

		Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,

		Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, +1.0f

		);

		outtoRight.setDuration(duration);

		outtoRight.setInterpolator(new LinearInterpolator());

		return outtoRight;

	}

	static public Animation inFromBottomLeftAnimation() {

		Animation inFromLeft = new TranslateAnimation(

		Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT,
				0.0f,

				Animation.RELATIVE_TO_PARENT, 1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f

		);

		inFromLeft.setDuration(200);

		inFromLeft.setInterpolator(new LinearInterpolator());

		return inFromLeft;

	}

	static public Animation settingsInPathAnimation() {

		AnimationSet animationSet = new AnimationSet(true);
		RotateAnimation rotate_360 = new RotateAnimation(0, 720,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotate_360.setDuration(900);
		rotate_360.setInterpolator(new LinearInterpolator());

		Animation step1 = new TranslateAnimation(Animation.ABSOLUTE, -720,
				Animation.ABSOLUTE, -660, Animation.ABSOLUTE, 280,
				Animation.ABSOLUTE, 0);
		step1.setDuration(300);
		step1.setInterpolator(new LinearInterpolator());

		Animation inFromLeft = new TranslateAnimation(Animation.ABSOLUTE, 0,
				Animation.ABSOLUTE, 660,

				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromLeft.setDuration(500);
		inFromLeft.setInterpolator(new LinearInterpolator());
		animationSet.addAnimation(rotate_360);
		animationSet.addAnimation(step1);
		animationSet.addAnimation(inFromLeft);

		return animationSet;

	}

	static public Animation tasksInPathAnimation() {

		AnimationSet animationSet = new AnimationSet(true);
		RotateAnimation rotate_360 = new RotateAnimation(0, 720,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotate_360.setDuration(900);
		rotate_360.setInterpolator(new LinearInterpolator());

		Animation step1 = new TranslateAnimation(Animation.ABSOLUTE, -640,
				Animation.ABSOLUTE, -580, Animation.ABSOLUTE, 540,
				Animation.ABSOLUTE, 82);
		step1.setDuration(300);
		step1.setInterpolator(new LinearInterpolator());

		Animation inFromLeft = new TranslateAnimation(Animation.ABSOLUTE, 0,
				Animation.ABSOLUTE, 580, Animation.ABSOLUTE, 0,
				Animation.ABSOLUTE, -82);
		inFromLeft.setDuration(500);
		inFromLeft.setInterpolator(new LinearInterpolator());
		animationSet.addAnimation(rotate_360);
		animationSet.addAnimation(step1);
		animationSet.addAnimation(inFromLeft);

		return animationSet;

	}

	static public Animation notesInPathAnimation() {

		AnimationSet animationSet = new AnimationSet(true);
		RotateAnimation rotate_360 = new RotateAnimation(0, 720,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotate_360.setDuration(900);
		rotate_360.setInterpolator(new LinearInterpolator());

		Animation step1 = new TranslateAnimation(Animation.ABSOLUTE, -580,
				Animation.ABSOLUTE, -520, Animation.ABSOLUTE, 720,
				Animation.ABSOLUTE, 200);
		step1.setDuration(300);
		step1.setInterpolator(new LinearInterpolator());

		Animation inFromLeft = new TranslateAnimation(Animation.ABSOLUTE, 0,
				Animation.ABSOLUTE, 520, Animation.ABSOLUTE, 0,
				Animation.ABSOLUTE, -200);
		inFromLeft.setDuration(500);
		inFromLeft.setInterpolator(new LinearInterpolator());
		animationSet.addAnimation(rotate_360);
		animationSet.addAnimation(step1);
		animationSet.addAnimation(inFromLeft);

		return animationSet;

	}

	static public Animation calendarInPathAnimation() {

		AnimationSet animationSet = new AnimationSet(true);
		RotateAnimation rotate_360 = new RotateAnimation(0, 720,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotate_360.setDuration(900);
		rotate_360.setInterpolator(new LinearInterpolator());

		Animation step1 = new TranslateAnimation(Animation.ABSOLUTE, -500,
				Animation.ABSOLUTE, -440, Animation.ABSOLUTE, 720,
				Animation.ABSOLUTE, 200);
		step1.setDuration(300);
		step1.setInterpolator(new LinearInterpolator());

		Animation inFromLeft = new TranslateAnimation(Animation.ABSOLUTE, 0,
				Animation.ABSOLUTE, 440, Animation.ABSOLUTE, 0,
				Animation.ABSOLUTE, -200);
		inFromLeft.setDuration(500);
		inFromLeft.setInterpolator(new LinearInterpolator());
		animationSet.addAnimation(rotate_360);
		animationSet.addAnimation(step1);
		animationSet.addAnimation(inFromLeft);

		return animationSet;

	}

	static public Animation contactsInPathAnimation() {

		AnimationSet animationSet = new AnimationSet(true);
		RotateAnimation rotate_360 = new RotateAnimation(0, 720,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotate_360.setDuration(900);
		rotate_360.setInterpolator(new LinearInterpolator());

		Animation step1 = new TranslateAnimation(Animation.ABSOLUTE, -320,
				Animation.ABSOLUTE, -280, Animation.ABSOLUTE, 280,
				Animation.ABSOLUTE, 0);
		step1.setDuration(300);
		step1.setInterpolator(new LinearInterpolator());

		Animation inFromLeft = new TranslateAnimation(Animation.ABSOLUTE, 0,
				Animation.ABSOLUTE, 280, Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromLeft.setDuration(500);
		inFromLeft.setInterpolator(new LinearInterpolator());
		animationSet.addAnimation(rotate_360);
		animationSet.addAnimation(step1);
		animationSet.addAnimation(inFromLeft);

		return animationSet;

	}

	static public Animation dropAndRotateAnimation() {

		AnimationSet animationSet = new AnimationSet(true);
		RotateAnimation rotate_720 = new RotateAnimation(0, 720,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotate_720.setDuration(500);
		rotate_720.setInterpolator(new LinearInterpolator());

		animationSet.addAnimation(rotate_720);
		animationSet.addAnimation(ATLAnimationUtils.outToBottomAnimation());

		return animationSet;

	}

	static public Animation alphaScaleDropRotateAnimation() {

		AnimationSet newAnimationSet = new AnimationSet(true);
		AlphaAnimation alpha = new AlphaAnimation(0, 1);
		alpha.setDuration(50);
		alpha.setInterpolator(new AccelerateInterpolator());

		ScaleAnimation scale = new ScaleAnimation(3.0f, 1.0f, 3.0f, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f); // HERE
		scale.setDuration(50);
		scale.setInterpolator(new LinearInterpolator());
		newAnimationSet.addAnimation(alpha);
		newAnimationSet.addAnimation(scale);

		RotateAnimation rotate_720 = new RotateAnimation(0, 720,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotate_720.setDuration(500);
		rotate_720.setInterpolator(new LinearInterpolator());

		newAnimationSet.addAnimation(rotate_720);
		newAnimationSet.addAnimation(ATLAnimationUtils
				.outToBottomAnimation(700));

		return newAnimationSet;

	}

	static public Animation quickAddEventShow(float yValue) {

		AnimationSet animationSet = new AnimationSet(true);

		ScaleAnimation scale = new ScaleAnimation(1.0f, 1.0f, 0.125f, 1.0f); // HERE
		scale.setDuration(QUICK_ADD_ANIMATION_DURATION);
		scale.setInterpolator(new AccelerateInterpolator());
		animationSet.addAnimation(scale);

		TranslateAnimation a = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,

				Animation.ABSOLUTE, yValue, Animation.RELATIVE_TO_PARENT, 0.0f);
		a.setDuration(QUICK_ADD_ANIMATION_DURATION);
		a.setInterpolator(new AccelerateInterpolator());
		animationSet.addAnimation(a);

		return animationSet;

	}

	// 2012-12-09 TAN: START - add to handle soft keyboard

	static public void showKeyboard(Context ctx) {

		InputMethodManager imm = (InputMethodManager) ctx
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

	}

	static public void hideKeyboard(Context ctx, EditText editText) {

		InputMethodManager imm = (InputMethodManager) ctx
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

	}
	// 2012-12-09 TAN: END - add to handle soft keyboard

	public static Animation moveUp(int height) {
		// TODO Auto-generated method stub
		Animation moveUp = new TranslateAnimation(

			Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
	
			Animation.ABSOLUTE, height, Animation.ABSOLUTE, 0

		);

		moveUp.setDuration(200);

		moveUp.setInterpolator(new LinearInterpolator());

		return moveUp;
	}

	public static Animation moveDown(int height) {
		// TODO Auto-generated method stub
		Animation moveDown = new TranslateAnimation(

			Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
	
			Animation.ABSOLUTE, -height, Animation.ABSOLUTE, 0

		);

		moveDown.setDuration(200);

		moveDown.setInterpolator(new LinearInterpolator());

		return moveDown;
	}
	
	static public Animation mainFragmentMoveRight(int distance) {

		AnimationSet animationSet = new AnimationSet(true);
		Animation step1 = new TranslateAnimation(Animation.ABSOLUTE, - distance,
				Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0,
				Animation.ABSOLUTE, 0);
		step1.setDuration(400);
		step1.setInterpolator(new LinearInterpolator());
		animationSet.addAnimation(step1);
		animationSet.setInterpolator(new BounceInterpolator());
		return animationSet;

	}
	
	static public Animation mainFragmentMoveLeft(int distance) {

		AnimationSet animationSet = new AnimationSet(true);
		Animation step1 = new TranslateAnimation(Animation.ABSOLUTE, distance,
				Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0,
				Animation.ABSOLUTE, 0);
		step1.setDuration(400);
		step1.setInterpolator(new LinearInterpolator());
		animationSet.addAnimation(step1);
		animationSet.setInterpolator(new BounceInterpolator());
		return animationSet;

	}
	
	static public Animation mainFragmentBackFromRight(int distance) {

		AnimationSet animationSet = new AnimationSet(true);
		
		Animation step1 = new TranslateAnimation(Animation.ABSOLUTE, distance,
				Animation.ABSOLUTE, 720 , Animation.ABSOLUTE, 0,
				Animation.ABSOLUTE, 0);
		step1.setDuration(200);

		Animation step2 = new TranslateAnimation(Animation.ABSOLUTE, 100,
				Animation.ABSOLUTE, -720, Animation.ABSOLUTE, 0,
				Animation.ABSOLUTE, 0);
		step2.setDuration(400);
		
		animationSet.addAnimation(step1);
		animationSet.addAnimation(step2);
		animationSet.setInterpolator(new BounceInterpolator());
		return animationSet;

	}
	
	static public Animation mainFragmentBackFromLeft() {

		AnimationSet animationSet = new AnimationSet(true);
		

		Animation step2 = new TranslateAnimation(Animation.ABSOLUTE, -700,
				Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0,
				Animation.ABSOLUTE, 0);
		step2.setDuration(400);
		
		animationSet.addAnimation(step2);
		animationSet.setInterpolator(new BounceInterpolator());
		return animationSet;

	}

	public static Animation mainFragmentBackFromLeftAt(int currentX, int distance) {
		// TODO Auto-generated method stub
		AnimationSet animationSet = new AnimationSet(true);
		

		Animation step2 = new TranslateAnimation(Animation.ABSOLUTE,currentX-distance,
				Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0,
				Animation.ABSOLUTE, 0);
		step2.setDuration(100);
		
		animationSet.addAnimation(step2);
//		animationSet.setInterpolator(new BounceInterpolator());
		return animationSet;
	}

	public static Animation mainFragmentBackFromRightAt(int from) {
		// TODO Auto-generated method stub
		AnimationSet animationSet = new AnimationSet(true);

		Animation step2 = new TranslateAnimation(Animation.ABSOLUTE, from,
				Animation.ABSOLUTE, 0 , Animation.ABSOLUTE, 0,
				Animation.ABSOLUTE, 0);
		step2.setDuration(100);
		
		animationSet.addAnimation(step2);
//		animationSet.setInterpolator(new BounceInterpolator());
		return animationSet;
	}

	public static Animation mainFragmentMoveLeftAt(int from, int distance) {
		// TODO Auto-generated method stub
		AnimationSet animationSet = new AnimationSet(true);
		Animation step1 = new TranslateAnimation(Animation.ABSOLUTE, distance - from,
				Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0,
				Animation.ABSOLUTE, 0);
		step1.setDuration(100);
		animationSet.addAnimation(step1);
//		animationSet.setInterpolator(new BounceInterpolator());
		return animationSet;
	}

	public static Animation mainFragmentMoveRightAt(int from,  int distance) {
		// TODO Auto-generated method stub
		AnimationSet animationSet = new AnimationSet(true);
		Animation step1 = new TranslateAnimation(Animation.ABSOLUTE, from - distance,
				Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0,
				Animation.ABSOLUTE, 0);
		step1.setDuration(100);
		animationSet.addAnimation(step1);
//		animationSet.setInterpolator(new BounceInterpolator());
		return animationSet;
	}
	
	public static void showProgressBar(ProgressDialog progressBar, String message){
		progressBar.setCancelable(false);
		progressBar.setMessage(message);
		progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressBar.show();
		
	}
	
	public static void dismissProgressBar(ProgressDialog progressBar){
		progressBar.dismiss();
	}
}
