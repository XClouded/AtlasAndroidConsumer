package com.atlasapp.common;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.atlasapp.section_settings.Settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * Called whenever a push notification is received with an action parameter of 
 * com.atlasapp.common.PUSH_NOTIFICATION. 
 * The Intent object which is passed to the receiver contains an extras Bundle with two useful mappings. 
 * The "com.parse.Channel" key points to a string representing the channel that the message was sent on. 
 * The "com.parse.Data" key points to a string representing the JSON-encoded value of the "data" dictionary 
 * that was set in the push notification. 
 * @author sharon nachum
 *
 */
public class PushNotificationReceiver extends BroadcastReceiver {
private static final String TAG = "PushNotificationReceiver";
 
  @Override
  public void onReceive(Context context, Intent intent) {
    try {
      String action = intent.getAction();
      String channel = intent.getExtras().getString("com.parse.Channel");
      JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
 
   //   Log.d(TAG, "got action " + action + " on channel " + channel + " with:");
      Iterator itr = json.keys();
      while (itr.hasNext()) {
        String key = (String) itr.next();
        
        
        intent.putExtra(key,  json.getString(key));
   //     Log.d(TAG, "..." + key + " => " + json.getString(key));
      }
    
     

    
    } catch (JSONException e) {
   //   Log.d(TAG, "JSONException: " + e.getMessage());
    }
  }
}