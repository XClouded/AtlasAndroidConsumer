package com.atlasapp.common;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.atlasapp.section_settings.Settings;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class PushNotificationActivityReciever extends Activity{
	
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) 
	 {
	        super.onCreate(savedInstanceState);
	        
	        String data = "";
	        
	        Bundle extras = getIntent().getExtras();
	        if (extras != null) {
	        	
	        	   try {
	        		   		JSONObject json = new JSONObject(extras.getString("com.parse.Data"));
				
	        		   		Iterator itr = json.keys();
	        		   		while (itr.hasNext()) 
	        		   		{
	        		   			String key = (String) itr.next();
	        		   			String value = json.getString(key);
	        		   			data = (key.equals("inviter_message"))? value :data;
	        	   
	        		   		}
	        	   
	        	   
	        	   
	        	   } catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
	        	
	        	
	        }
	        
	        Intent intent = new Intent(getBaseContext(), Settings.class);
          	intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
          	intent.putExtra("message", data);
          	startActivity(intent);
  	        
	      
	 }
	
	

}
