package com.atlasapp.section_settings;

import java.util.ArrayList;

import com.atlasapp.atlas_database.AtlasServerConnect;
import com.atlasapp.common.ATLAnimationUtils;
import com.atlasapp.common.ATLDragAndDropView;
import com.atlasapp.common.AtlasAndroidUser;
import com.atlasapp.common.GIFView;
import com.atlasapp.common.Loading;
import com.atlasapp.model.ATLContactModel;
import com.atlasapp.section_alerts.ATLAlertFragment_2;
import com.atlasapp.section_alerts.AlertFragment;
import com.atlasapp.section_appentry.AtlasApplication;
import com.atlasapp.section_appentry.R;
import com.atlasapp.section_calendar.CalendarDayView;
import com.atlasapp.section_main.ATLMultiSectionListView;
import com.atlasapp.section_slidemenu.ATLSlideMenuFragment;
import com.ericharlow.DragNDrop.DragListener;
import com.ericharlow.DragNDrop.DropListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class FriendsFinder extends FragmentActivity{
   
	LayoutInflater mInflater;
	Context mContext;
	ViewGroup mLayout;
	
	public AtlasApplication applicationController;
	private GIFView loadingGif;
	private RelativeLayout loadingView;
	private AtlasServerConnect parseConnect;
	private Context context;
	public ATLDragAndDropView findFriendHolderView;
	public ViewGroup alertHolderView;
	public ViewGroup slideMenuHolderView;
	public View findFriendViewCover;
	public int topMenuBarHeight;
	public ATLSlideMenuFragment slideMenuFragment;
	public ATLAlertFragment_2 alertFragment;
	public ImageButton sortBtn;
	public ImageButton alertBtn;
	public boolean isFragmentShowing;
	private ProgressDialog progressBar;
	private static ProgressDialog progressDialog;
	@Override 
    public void onCreate(Bundle savedInstanceState) {
        
		super.onCreate(savedInstanceState);
//        setContentView(R.layout.friend_finder);
		mContext = this;
        mInflater = LayoutInflater.from(this);
		mLayout = (ViewGroup) mInflater.inflate(R.layout.friend_finder, null);
		setContentView(mLayout);  
        initAttributes();
        progressBar = new ProgressDialog(this);
        
//         loadingView = (RelativeLayout)findViewById(R.id.loadingLayout);
//       loadingView.setVisibility(View.INVISIBLE);
        // loadingGif = (GIFView)findViewById(R.id.loadingGif);
        
     //    loadingGif.setVisibility(View.INVISIBLE);
        
        applicationController = (AtlasApplication)getApplicationContext();
        parseConnect =   AtlasServerConnect.getSingletonObject(this);
        context = this.getBaseContext();
        
        
//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//        	String message = extras.getString("message");
//        	
//        	if (message!=null && !message.equals(""))
//        	{
//        		alertUser("",message);
//        	}
//        	
//        	
//        }
        	       
        
        Button findFacebookFriendsBtn = (Button)findViewById(R.id.findFacebookFriendsBtn);
        findFacebookFriendsBtn.setOnClickListener(new View.OnClickListener() {
			
			  

			@Override
			public void onClick(View v) {
//				 progressBar.setVisibility(View.VISIBLE);
//				 Toast.makeText(context, "Retreiving  Facebook friends info ...",
//			    			Toast.LENGTH_SHORT).show();
				 
				
				
				loadingFriends("facebook");
				
//				 setContentView(R.layout.loading);
//				 findViewById(R.id.mainSpinner1).setVisibility(View.VISIBLE);
//				 
//				 final int WAIT_TIME = 1000;
//				 new Handler().postDelayed(new Runnable(){ 
//						@Override 
//						    public void run() { 
//					              //Simulating a long running task
//							try {
//								Thread.sleep(1000);
//							} catch (InterruptedException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						  /* Create an Intent that will start the ProfileData-Activity. */
//					              Intent mainIntent = new Intent(FriendsFinder.this,LoadingFriends.class); 
//					              FriendsFinder.this.startActivity(mainIntent); 
//					              FriendsFinder.this.finish(); 
//						} 
//						}, WAIT_TIME);
					      
				 
			
			}
		});    
        
        Button findAdressBookFriendsBtn = (Button)findViewById(R.id.findContactsFriendsBtn);
        findAdressBookFriendsBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				loadingFriends("address");
//				String message = applicationController.updateAddressBookFriends();
//				if (message!=null && !message.equals(""))
//					alertUser("",message);
				
			}
		});
        
        
	}
	
	@Override
	public void onResume(){
		super.onResume();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 1001){
			ATLAnimationUtils.dismissProgressBar(progressBar);
			if(resultCode == Activity.RESULT_OK){
				applicationController = (AtlasApplication)getApplicationContext();
		        parseConnect =   AtlasServerConnect.getSingletonObject(this);
		        context = this.getBaseContext();
		        Bundle extras = data.getExtras();
		        if (extras != null) {
		        	String message = extras.getString("message");
		        	
		        	if (message!=null && !message.equals(""))
		        	{
		        		alertUser("",message);
		        	}
		        	
		        	
		        }
			}
		}
	
	}
	
	private void initAttributes() {
		// TODO Auto-generated method stub
		//==========================================================
		// 2013-02-23 TAN: new slide out # START
		//==========================================================
		findFriendHolderView = (ATLDragAndDropView)mLayout.findViewById(R.id.find_friends_content);
		alertHolderView = (ViewGroup)mLayout.findViewById(R.id.calendar_day_view_alert_fragment);
		slideMenuHolderView = (ViewGroup)mLayout.findViewById(R.id.calendar_day_view_cal_selects_fragment);
		findFriendViewCover = (View)mLayout.findViewById(R.id.calendar_day_view_cover_view);
		findFriendViewCover.setOnTouchListener(findFriendHolderView);
	    // top menu bar height
	    topMenuBarHeight = (int) Math.ceil(50 * this.getResources().getDisplayMetrics().density);
	    
		 if (findFriendHolderView instanceof ATLDragAndDropView) {
			 ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
			 findFriendHolderView.setDropListener(mDropListener);
			 findFriendHolderView.setDragListener(mDragListener);
	     }
		 
		slideMenuFragment = new ATLSlideMenuFragment();
			getSupportFragmentManager().beginTransaction()
			.add(R.id.calendar_day_view_cal_selects_fragment, slideMenuFragment).commit();
		alertFragment = ATLAlertFragment_2.getInstance();
			getSupportFragmentManager().beginTransaction()
			.add(R.id.calendar_day_view_alert_fragment, alertFragment).commit();
		//==========================================================
		// 2013-02-23 TAN: new slide out # END
		//==========================================================
		sortBtn = (ImageButton) mLayout.findViewById(R.id.sortBtn);
		alertBtn = (ImageButton) mLayout.findViewById(R.id.alertBtn);
		
		sortBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//==========================================================
				// 2013-02-23 TAN: new slide out # START
				//==========================================================
				if(!isFragmentShowing){
					alertHolderView.setVisibility(View.GONE);
					slideMenuHolderView.setVisibility(View.VISIBLE);
					findFriendViewCover.setVisibility(View.VISIBLE);
					findFriendHolderView.setX(findFriendHolderView.LEFTRIGHT_WINDOW_WIDTH);
					ATLDragAndDropView.topMenuBarHeight = findFriendHolderView.getHeight();
					ATLDragAndDropView.isAtRight = true;
					ATLDragAndDropView.isAtLeft = false;
					findFriendHolderView.startAnimation(ATLAnimationUtils.mainFragmentMoveRight(findFriendHolderView.LEFTRIGHT_WINDOW_WIDTH));
					isFragmentShowing = true;
				}
				else{
					findFriendViewCover.setVisibility(View.GONE);
					findFriendHolderView.setX(0);
					ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = false;
					findFriendHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromRight(findFriendHolderView.LEFTRIGHT_WINDOW_WIDTH));
					isFragmentShowing = false;
					onResume();
				}
				//==========================================================
				// 2013-02-23 TAN: new slide out # END
				//==========================================================
			}
		});
		
		alertBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//==========================================================
				// 2013-02-23 TAN: new slide out # START
				//==========================================================
				if(!isFragmentShowing){
					alertHolderView.setVisibility(View.VISIBLE);
					slideMenuHolderView.setVisibility(View.GONE);
					findFriendViewCover.setVisibility(View.VISIBLE);
					ATLDragAndDropView.topMenuBarHeight = findFriendHolderView.getHeight();
					findFriendHolderView.setX(-findFriendHolderView.LEFTRIGHT_WINDOW_WIDTH);
					findFriendHolderView.startAnimation(ATLAnimationUtils.mainFragmentMoveLeft(findFriendHolderView.LEFTRIGHT_WINDOW_WIDTH));
					alertFragment.onResume();// Reload view data
					isFragmentShowing = true;
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = true;
				}
				else{
					ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
					findFriendViewCover.setVisibility(View.GONE);
					findFriendHolderView.setX(0);
					findFriendHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromLeft());
					isFragmentShowing = false;
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = false;
				}
				//==========================================================
				// 2013-02-23 TAN: new slide out # END
				//==========================================================
			}
		});
	}
	
	private void loadingFriends(final String from)
	{
		
//		 setContentView(R.layout.loading);
//		 findViewById(R.id.mainSpinner1).setVisibility(View.VISIBLE);
		 ATLAnimationUtils.showProgressBar(progressBar, "Start finding...");	
		 Intent mainIntent = new Intent(mContext,LoadingFriends.class);
         mainIntent.putExtra("friendsRef", from);
         ((Activity) mContext).startActivityForResult(mainIntent, 1001); 
	/* TAN comment out	
		 final int WAIT_TIME = 1000;
		 new Handler().postDelayed(new Runnable(){ 
				@Override 
				    public void run() { 
			              //Simulating a long running task
					try {  
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				  // Create an Intent that will start the ProfileData-Activity. 
			              Intent mainIntent = new Intent(mContext,LoadingFriends.class);
			              mainIntent.putExtra("friendsRef", from);
			              ((Activity) mContext).startActivityForResult(mainIntent, 1001); 
				} 
				}, WAIT_TIME);
		   
		*/
		
	}
	
	
	protected ProgressDialog dialog;
	
	protected String message;

//	  public class BackgroundAsyncTask extends
//	    AsyncTask<String, Integer, String> {
//	      
//	     int myProgress;
//	     @Override
//			protected void onPostExecute(String message) {
//	    	 
//	    	 
//	    	
//				// execution of result of Long time consuming operation
//				//connecting = false;
//	    	  dialog.dismiss();
//	    	  
//	    	  
//	    	  
//	    	 // finish();	
//			}
////	     @Override
////	     protected void onPostExecute(Void result) {
////	         // TODO Auto-generated method stub
////	        // displayRss();
////	        
////
////
////	     }
//
//	     @Override
//	     protected void onPreExecute() {
//	         // TODO Auto-generated method stub
//	           dialog = ProgressDialog.show(FriendsFinder.this, "", "Loading. Please wait...", true);
//	           myProgress = 0;
//	     }
//	     @Override
//	     protected void onProgressUpdate(Integer... progress) {
//	         dialog.setProgress(progress[0]);
//	     }
////	     @Override
////	      protected void onProgressUpdate(Void... values) {
////	       // TODO Auto-generated method stub
////	       super.onProgressUpdate(values);
////	      }
//
////	     @Override
////	     protected Void doInBackground(Void... arg0) {
////	         // TODO Auto-generated method stub
////	         loadFeed();
////	         return null;
////	     }
//
//		@Override
//		protected String  doInBackground(String... params) {
//			
//			String[] operation = params;
//			message = applicationController.updateFacebookFriends();
//			 if (message!=null && !message.equals(""))
//		    		// dialog = ProgressDialog.show(FriendsFinder.this, "", message, true);
//						alertUser("",message);
//			// dialog.dismiss();
////			 message = applicationController.updateFacebookFriends();
////			 if (message!=null && !message.equals(""))
////						alertUser("",message);
////			dialog = ProgressDialog.show(FriendsFinder.this, "", "Loading. Please wait...", true);
//			
//			// TODO Auto-generated method stub
//			return "";
//		}
//
//
//
//	 }
	@SuppressWarnings("deprecation")
	private  void alertUser(String messageTitle, String message)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(
				FriendsFinder.this).create();

		// Setting Dialog Title
		alertDialog.setTitle(messageTitle);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		// Setting Icon to Dialog
	//	alertDialog.setIcon(R.drawable.tick);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog closed
				//Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
				//	progressDialog.dismiss();
//					Intent intentCalendar = new Intent(getBaseContext(), FriendsFinder.class);
//	            	intentCalendar.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//	            	//intent.putExtra("EXTRA_SESSION_ID", sessionId);
//	            	startActivity(intentCalendar);
				}  
		});     
    
		// Showing Alert Message
		alertDialog.show();
	}
	
	// ===============================================================================
	// 2013-02-03 TAN: Implement Swipe left and right # START
	// ===============================================================================
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			findFriendViewCover.setVisibility(View.GONE);
			findFriendHolderView.setX(0);
			findFriendHolderView.setVisibility(View.VISIBLE);
			ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
			
			if(ATLDragAndDropView.isAtRight){
				findFriendHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromRight(findFriendHolderView.LEFTRIGHT_WINDOW_WIDTH));
				ATLDragAndDropView.isAtRight = false;
				ATLDragAndDropView.isAtLeft = false;
				isFragmentShowing = false;
				onResume();
				return true;
			}
			else if(ATLDragAndDropView.isAtLeft){
				findFriendHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromLeft());
				ATLDragAndDropView.isAtRight = false;
				ATLDragAndDropView.isAtLeft = false;
				isFragmentShowing = false;
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
		
	}

	private DragListener mDragListener =
		new DragListener() {
			public void onDrag(int x, int y, ListView listView) {
				// TODO Auto-generated method stub
				
				if(x <= 0){
					//isSwipeLeft = true;
					alertHolderView.setVisibility(View.VISIBLE);
					slideMenuHolderView.setVisibility(View.GONE);
				}
				else{
					//isSwipeLeft = false;
					alertHolderView.setVisibility(View.GONE);
					slideMenuHolderView.setVisibility(View.VISIBLE);
				}
				
			}
	
			public void onStartDrag(View itemView) {
				findFriendHolderView.setVisibility(View.GONE);
			}
	
			public void onStopDrag(View itemView) {
				findFriendHolderView.setVisibility(View.VISIBLE);
				float x = itemView.getX();
				float y = itemView.getY();
				Log.v("CalendarDayView", "onStopDrag  =====   "+ x +" ===  "+y);
			}
			
	};	

	private DropListener mDropListener = 
		new DropListener() {
        

		public void onDrop(int from, int to) {
        	if(findFriendHolderView.getVisibility() != View.VISIBLE){
        		findFriendHolderView.setVisibility(View.VISIBLE);
        	}
        	
        	if(ATLDragAndDropView.isTouched){
        		ATLDragAndDropView.isTouched = false;
        		findFriendViewCover.setVisibility(View.GONE);
    			findFriendHolderView.setX(0);
    			findFriendHolderView.setVisibility(View.VISIBLE);
    			ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
    			
    			if(ATLDragAndDropView.isAtRight){
    				findFriendHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromRight(findFriendHolderView.LEFTRIGHT_WINDOW_WIDTH));
    				ATLDragAndDropView.isAtRight = false;
    				ATLDragAndDropView.isAtLeft = false;
    				isFragmentShowing = false;
    				onResume();
    			}
    			else if(ATLDragAndDropView.isAtLeft){
    				findFriendHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromLeft());
    				ATLDragAndDropView.isAtRight = false;
    				ATLDragAndDropView.isAtLeft = false;
    				isFragmentShowing = false;
					onResume();
    			}
        	}
        	else if(ATLDragAndDropView.isAtLeft){
        		if(from > to){
        		
        		}
        		else if(to > from && to > 300){
        			ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
					findFriendViewCover.setVisibility(View.GONE);
					findFriendHolderView.setX(0);
					findFriendHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromLeftAt(to, findFriendHolderView.LEFTRIGHT_WINDOW_WIDTH));
					isFragmentShowing = false;
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = false;
					onResume();
    	        }
			}else if(ATLDragAndDropView.isAtRight){
				if(from < to){
	        		
        		}
        		else if(from > to && to < 450){
        			findFriendViewCover.setVisibility(View.GONE);
					findFriendHolderView.setX(0);
					ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = false;
					findFriendHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromRightAt(to 
							- (from - findFriendHolderView.LEFTRIGHT_WINDOW_WIDTH)));
					isFragmentShowing = false;
					onResume();
    	        }
			}else{
				if(from > to && to < 360){
	        		
	        		alertHolderView.setVisibility(View.VISIBLE);
					slideMenuHolderView.setVisibility(View.GONE);
					findFriendViewCover.setVisibility(View.VISIBLE);
					findFriendHolderView.setX(-findFriendHolderView.LEFTRIGHT_WINDOW_WIDTH);
					findFriendHolderView.startAnimation(ATLAnimationUtils.mainFragmentMoveLeftAt(from - to, findFriendHolderView.LEFTRIGHT_WINDOW_WIDTH));
					ATLDragAndDropView.topMenuBarHeight = findFriendHolderView.getHeight();
					alertFragment.onResume();// Reload view data
					isFragmentShowing = true;
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = true;
					//onResume();
					
	        	}
	        	else if(to > from && to > 360){
	        		
	        		alertHolderView.setVisibility(View.GONE);
					slideMenuHolderView.setVisibility(View.VISIBLE);
					findFriendViewCover.setVisibility(View.VISIBLE);
					findFriendHolderView.setX(findFriendHolderView.LEFTRIGHT_WINDOW_WIDTH);
					findFriendHolderView.startAnimation(ATLAnimationUtils.mainFragmentMoveRightAt(to - from, findFriendHolderView.LEFTRIGHT_WINDOW_WIDTH));
					ATLDragAndDropView.topMenuBarHeight = findFriendHolderView.getHeight();
					isFragmentShowing = true;
					ATLDragAndDropView.isAtRight = true;
					ATLDragAndDropView.isAtLeft = false;
					//onResume();
	        	}
			}
        	
        }
	};
	// ===============================================================================
	// 2013-02-03 TAN: Implement Swipe left and right # END
	// ===============================================================================	
	
	
}
