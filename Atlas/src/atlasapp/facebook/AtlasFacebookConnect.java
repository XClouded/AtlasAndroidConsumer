package atlasapp.facebook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import atlasapp.facebook.SessionEvents.AuthListener;


import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;




public class AtlasFacebookConnect {
	
	public AtlasFacebookConnect(){};
	
	

	
	 /*
     * Callback for fetching current user's name, picture, uid.
     */
//    public class UserRequestListener extends BaseRequestListener {
//    	
//
//        @Override
//        public void onComplete(final String response, final Object state) {
//            JSONObject jsonObject;
//            try {
//                jsonObject = new JSONObject(response);
//
//                final String picURL = jsonObject.getString("picture");
//                final String name = jsonObject.getString("name");
//                FacebookUtilities.userUID = jsonObject.getString("id");
//
////                mHandler.post(new Runnable() {
////                    @Override
////                    public void run() {
////                        mText.setText("Welcome " + name + "!");
////                        mUserPic.setImageBitmap(FacebookUtilities.getBitmap(picURL));
////                    }
////                });
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
    /*
     * Request user name, and picture to show on the main screen.
     */
    public void requestUserData() {
       // mText.setText("Fetching user name, profile pic...");
        Bundle params = new Bundle();
        params.putString("fields", "name, picture");
      //  FacebookUtilities.mAsyncRunner.request("me", params, new UserRequestListener());
    }
    /*
     * The Callback for notifying the application when authorization succeeds or
     * fails.
     */

    public class FbAPIsAuthListener implements AuthListener {

        @Override
        public void onAuthSucceed() {
            requestUserData();
        }

        @Override
        public void onAuthFail(String error) {
           // mText.setText("Login Failed: " + error);
        }
    }

}
