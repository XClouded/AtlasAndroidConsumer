/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.apis.view;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.example.android.apis.R;

public class Gallery1 extends Activity {
	CoverFlow channels, epg;
	 static int mPrevPos = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LinearLayout ll =new LinearLayout(getBaseContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        
        LinearLayout headerSV =new LinearLayout(getBaseContext());
        ImageView blank = new ImageView(getBaseContext());
        blank.setBackgroundResource(R.drawable.tab_normal);
        
        headerSV.addView(blank, 50, 30);
        
        channels = new CoverFlow(getBaseContext());
        channels.setAdapter(new ImageAdapter(this,false));
        channels.setSpacing(10);
       // LayoutParams lp = new LayoutParams(450, LayoutParams.WRAP_CONTENT);
        channels.setUnselectedAlpha(100);
        //channels.setPadding(-300, 0, 0, 0);
        channels.setSelection(1);
       // channels.setAnimationDuration(400);
        headerSV.addView(channels, 450, LayoutParams.WRAP_CONTENT);
 /*       
        ScrollView headerSV = new ScrollView(getBaseContext());
        LinearLayout headerLL = new LinearLayout(getBaseContext());
        for(int i=0; i<15;i++)
        {
        	LinearLayout item = new LinearLayout(getBaseContext());
        	 ImageView ch = new ImageView(getBaseContext());
    		 ch.setImageResource(R.drawable.channel);
             //ch.setScaleType(ImageView.ScaleType.FIT_XY);
    		 //LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    		 //ch.setLayoutParams(lp);
    		 item.addView(ch);
    		 item.setMinimumWidth(135);
    		 item.setGravity(Gravity.CENTER_HORIZONTAL);
    		 headerLL.addView(item);
        }
        LayoutParams chlp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        headerSV.addView(headerLL);
        LayoutParams lp2 = new LayoutParams(450, LayoutParams.WRAP_CONTENT);

        ll.addView(headerSV,lp2);
      */  
        
        ScrollView sv = new ScrollView(getBaseContext());
        
        LinearLayout llmain = new LinearLayout(getBaseContext());
        
        ImageView iv = new ImageView(getBaseContext());
        iv.setBackgroundResource(R.drawable.tab_normal);
        
        llmain.addView(iv, new LayoutParams(50, 1688));
        
        
        epg = new CoverFlow(getBaseContext());
        epg.setAdapter(new ImageAdapter(this,true));
        epg.setSpacing(10);
       // LayoutParams lp = new LayoutParams(450, LayoutParams.WRAP_CONTENT);
        epg.setUnselectedAlpha(100);
       // epg.setPadding(-300, 0, 0, 0);
        epg.setSelection(1);
       // channels.setHeader(epg);
		//Set the onClick behavior for the coverflow
       
		epg.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long arg3) {
			      if(epg.cascadeScroll)
			      {
			    	  channels.cascadeScroll=false;
//			    	  if(pos==0)
//			    		  channels.setSelection(1);
			    	  channels.setSelection(pos);
			    	  channels.cascadeScroll=true;
			      }
				
			}
			public void onNothingSelected(AdapterView<?> arg0) {
				//Do nothing
			}
		});
		channels.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long arg3) {
			      if(channels.cascadeScroll)
			      {
			    	  epg.cascadeScroll=false;
//			    	  if(pos==0)
//			    		  epg.setSelection(1);
			    	  epg.setSelection(pos);
			    	  epg.cascadeScroll=true;
			      }
			}
			public void onNothingSelected(AdapterView<?> arg0) {
				//Do nothing
			}
		});
        llmain.addView(epg, 450, LayoutParams.WRAP_CONTENT);
        sv.addView(llmain,500, LayoutParams.WRAP_CONTENT);
        
//        g.invalidate();
        channels.setOther(epg);
        epg.setOther(channels);
        ll.addView(headerSV, 500, LayoutParams.WRAP_CONTENT);
        ll.addView(sv, 500, LayoutParams.WRAP_CONTENT);
        
        setContentView(ll);
        // We also want to show context menu for longpressed items in the gallery
       // registerForContextMenu(g);
    }

    public class ImageAdapter extends BaseAdapter {
    	boolean epg;
        public ImageAdapter(Context c, boolean _epg) {
            mContext = c;
            epg = _epg;
        }

        public int getCount() {
            return mImageIds.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
        	LinearLayout holder = new LinearLayout(mContext);
        	if(epg)
        	{
        	
        	holder.setOrientation(LinearLayout.VERTICAL);
            ImageView i = new ImageView(mContext);
            Button b1 = new Button(mContext);
            b1.setText("Button 1");
            Button b2 = new Button(mContext);
            b2.setText("Button 2");
            Button b3 = new Button(mContext);
            b3.setText("Button 3");
            Button b4 = new Button(mContext);
            b4.setText("Button 4");
            b1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Button b = (Button)v;
					Toast.makeText(Gallery1.this, "" + b.getText(), Toast.LENGTH_SHORT).show();
					
				}
			});
            b2.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Button b = (Button)v;
					Toast.makeText(Gallery1.this, "" + b.getText(), Toast.LENGTH_SHORT).show();
					
				}
			});
            i.setImageResource(mImageIds[position]);
            i.setScaleType(ImageView.ScaleType.FIT_XY);
            i.setLayoutParams(new Gallery.LayoutParams(136, 1688));
	   		 ImageView ch = new ImageView(mContext);
			 ch.setImageResource(mChannelIds[position]);
			 holder.addView(ch);
            holder.addView(i);
            holder.addView(b1);
            holder.addView(b2);
            holder.addView(b3);
            holder.addView(b4);
            
            // The preferred Gallery item background
           // i.setBackgroundResource(mGalleryItemBackground);
            
            
        	}
        	else 
        	{
        		
        		 ImageView ch = new ImageView(mContext);
        		 ch.setImageResource(mChannelIds[position]);
                 //ch.setScaleType(ImageView.ScaleType.FIT_XY);
        		 LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        		 ch.setLayoutParams(lp);
        		 holder.addView(ch, lp);
        		 holder.setMinimumWidth(135);
        		 holder.setGravity(Gravity.CENTER_HORIZONTAL);
        		// ch
        		 
        		//return ch;
        	}
        	return holder;
        }

        private Context mContext;

        private Integer[] mImageIds = {
        		R.drawable.tab_normal,
        		R.drawable.tab_normal,
        		R.drawable.tab_normal,
        		R.drawable.tab_normal,
        		R.drawable.tab_normal,
        		R.drawable.tab_normal,
        		R.drawable.tab_normal,
        		R.drawable.tab_normal,
        		R.drawable.tab_normal,
        		R.drawable.tab_normal,
        		R.drawable.tab_normal,
        		R.drawable.tab_normal,
        		R.drawable.tab_normal,
        		R.drawable.tab_normal,
                R.drawable.tab_normal
        };
        private Integer[] mChannelIds = {
        		R.drawable.channel1,
        		R.drawable.channel2,
        		R.drawable.channel3,
        		R.drawable.channel4,
        		R.drawable.channel5,
        		R.drawable.channel1,
        		R.drawable.channel2,
        		R.drawable.channel3,
        		R.drawable.channel4,
        		R.drawable.channel5,
        		R.drawable.channel1,
        		R.drawable.channel2,
        		R.drawable.channel3,
        		R.drawable.channel4,
        		R.drawable.channel5
        };
    }
}

