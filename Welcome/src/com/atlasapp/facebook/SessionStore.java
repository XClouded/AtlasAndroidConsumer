package com.atlasapp.facebook;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.facebook.android.Facebook;

public class SessionStore {
	
	 private static final String TOKEN = "access_token";
	    private static final String EXPIRES = "expires_in";
	    private static final String LAST_UPDATE = "last_update";
	    private static final String KEY = "facebook-session";

	    /*
	     * Save the access token and expiry date so you don't have to fetch it each
	     * time
	     */
	    public static boolean save(Facebook session, Context context) {
	        Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
	        editor.putString(TOKEN, session.getAccessToken());
	        editor.putLong(EXPIRES, session.getAccessExpires());
	        editor.putLong(LAST_UPDATE, session.getLastAccessUpdate());
	        return editor.commit();
	    }

	    /*
	     * Restore the access token and the expiry date from the shared preferences.
	     */
	    public static boolean restore(Facebook session, Context context) {
	        SharedPreferences savedSession = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
	        session.setTokenFromCache(
	                savedSession.getString(TOKEN, null),
	                savedSession.getLong(EXPIRES, 0),
	                savedSession.getLong(LAST_UPDATE, 0));
	        return session.isSessionValid();
	    }

	    public static void clear(Context context) {
	        Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
	        editor.clear();
	        editor.commit();
	    }

}
