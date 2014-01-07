package atlasapp.facebook;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.provider.MediaStore;
import android.util.Log;


import com.facebook.FacebookActivity;
import com.facebook.FacebookException;

import com.facebook.LoggingBehaviors;
import com.facebook.Session;
import com.facebook.ProfilePictureView.OnErrorListener;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.facebook.android.AsyncFacebookRunner.RequestListener;


public class FacebookUtilities {
	
    private static final String HASH_ALGORITHM_MD5 = "MD5";
    static final int DEFAULT_STREAM_BUFFER_SIZE = 8192;

		public static Facebook mFacebook;
	    public static AsyncFacebookRunner mAsyncRunner;
	    public static JSONObject mFriendsList;
	    public static String userUID = null;
	    public static String objectID = null;
	    public static FriendsGetProfilePics model;
	    public static AndroidHttpClient httpclient = null;
	    public static Hashtable<String, String> currentPermissions = new Hashtable<String, String>();

	    private static int MAX_IMAGE_DIMENSION = 720;
	    public static final String HACK_ICON_URL = "http://www.facebookmobileweb.com/hackbook/img/facebook_icon_large.png";

	    
	    
	    private static String usersFacebookUserName="";
	    private static String usersFacebookID="";
	    private static Bitmap usersFacebookProfilePicBitmap=null;
	  
	    
	    
	    public static String access_token;
	    
	    static <T> boolean isNullOrEmpty(Collection<T> c) {
	        return (c == null) || (c.size() == 0);
	    }
	 // Returns true iff all items in subset are in superset, treating null and
	    // empty collections as
	    // the same.
	    static <T> boolean isSubset(Collection<T> subset, Collection<T> superset) {
	        if ((superset == null) || (superset.size() == 0)) {
	            return ((subset == null) || (subset.size() == 0));
	        }

	        HashSet<T> hash = new HashSet<T>(superset);
	        for (T t : subset) {
	            if (!hash.contains(t)) {
	                return false;
	            }
	        }
	        return true;
	    }

	    static boolean isNullOrEmpty(String s) {
	        return (s == null) || (s.length() == 0);
	    }
	    static void disconnectQuietly(URLConnection connection) {
	        if (connection instanceof HttpURLConnection) {
	            ((HttpURLConnection)connection).disconnect();
	        }
	    }

	    private static ArrayList<String> facebookFriends = new ArrayList<String>();
	    static String md5hash(String key) {
	        MessageDigest hash = null;
	        try {
	            hash = MessageDigest.getInstance(HASH_ALGORITHM_MD5);
	        } catch (NoSuchAlgorithmException e) {
	            return null;
	        }

	        hash.update(key.getBytes());
	        byte[] digest = hash.digest();
	        StringBuilder builder = new StringBuilder();
	        for (int b : digest) {
	            builder.append(Integer.toHexString((b >> 4) & 0xf));
	            builder.append(Integer.toHexString((b >> 0) & 0xf));
	        }
	        return builder.toString();
	    }

	    
	    /**
	     * Get all Facebook friends id's
	     * @param context
	     * @return Array list of all facebook friends id's
	     */
	    public static ArrayList<String> getFacebookFriends(Context context){if (facebookFriends.isEmpty()) setFriendsRequest(context);  return facebookFriends;};
	    
	    
	    
	    /**
	     * Get the current app owner, Facebook profile picture as Bitmap
	     * @return
	     * @throws JSONException 
	     */
	    public static Bitmap getUserFacebookProfilePic() 
	    {
	    	if (usersFacebookProfilePicBitmap==null)
	    	{
	    		try {
//	                JSONObject me = new JSONObject("me");
//	           //     String id=me.getString("id");
//	                String userName=me.getString("username");
//	                ImageView picture;
//	                TextView usr = (TextView)findViewById(R.id.userName);
//	                picture = (ImageView) findViewById(R.id.profilePicture);
	    			
	    		//	String str = mFacebook.request("me/picture");


	    	//		InputStream is = new ByteArrayInputStream(str.getBytes());

	    	//		usersFacebookProfilePicBitmap = BitmapFactory.decodeStream(is);

	    			
	    			
	    			
	    			JSONObject me = new JSONObject(mFacebook.request("me"));
	                String userName=me.getString("username");
	                URL image_value= new URL("http://graph.facebook.com/" + userName + "/picture" );
	                usersFacebookProfilePicBitmap =BitmapFactory.decodeStream(image_value.openConnection().getInputStream());
	    			
	    			
	    			
	    		//	URL MyProfilePicURL = new URL("/graph.facebook.com/me/picture?type=normal" );
	    			
	    			
	    		//	 img_value = new URL("http://graph.facebook.com/"+id+"/picture?type=large");

	    			
	    		//	 Bitmap mIcon1 = BitmapFactory.decodeStream(MyProfilePicURL.openConnection().getInputStream());

	    			
	    		//	usersFacebookProfilePicBitmap = getBitmap("https://graph.facebook.com/me/picture?type=normal&method=GET&access_token="+ mFacebook.getAccessToken());
	    			
	             //   URL image_value= new URL("http://graph.facebook.com/" + userName + "/picture" );
	             //   usersFacebookProfilePicBitmap =BitmapFactory.decodeStream(image_value.openConnection().getInputStream());
	           //     picture.setImageBitmap(profPict);
	            //    usr.setText(userName);
	            } catch (MalformedURLException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            } catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            } catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	    	return usersFacebookProfilePicBitmap;
	    }
	    
	    
	    public static Bitmap getBitmap(String url) {
	        Bitmap bm = null;
	        try {
	            URL aURL = new URL(url);
	            URLConnection conn = aURL.openConnection();
	            conn.connect();
	            InputStream is = conn.getInputStream();
	            BufferedInputStream bis = new BufferedInputStream(is);
	            bm = BitmapFactory.decodeStream(new FlushedInputStream(is));
	            bis.close();
	            is.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            if (httpclient != null) {
	                httpclient.close();
	            }
	        }
	        return bm;
	    }

	    static class FlushedInputStream extends FilterInputStream {
	        public FlushedInputStream(InputStream inputStream) {
	            super(inputStream);
	        }

	        @Override
	        public long skip(long n) throws IOException {
	            long totalBytesSkipped = 0L;
	            while (totalBytesSkipped < n) {
	                long bytesSkipped = in.skip(n - totalBytesSkipped);
	                if (bytesSkipped == 0L) {
	                    int b = read();
	                    if (b < 0) {
	                        break; // we reached EOF
	                    } else {
	                        bytesSkipped = 1; // we read one byte
	                    }
	                }
	                totalBytesSkipped += bytesSkipped;
	            }
	            return totalBytesSkipped;
	        }
	    }

	    public static byte[] scaleImage(Context context, Uri photoUri) throws IOException {
	        InputStream is = context.getContentResolver().openInputStream(photoUri);
	        BitmapFactory.Options dbo = new BitmapFactory.Options();
	        dbo.inJustDecodeBounds = true;
	        BitmapFactory.decodeStream(is, null, dbo);
	        is.close();

	        int rotatedWidth, rotatedHeight;
	        int orientation = getOrientation(context, photoUri);

	        if (orientation == 90 || orientation == 270) {
	            rotatedWidth = dbo.outHeight;
	            rotatedHeight = dbo.outWidth;
	        } else {
	            rotatedWidth = dbo.outWidth;
	            rotatedHeight = dbo.outHeight;
	        }

	        Bitmap srcBitmap;
	        is = context.getContentResolver().openInputStream(photoUri);
	        if (rotatedWidth > MAX_IMAGE_DIMENSION || rotatedHeight > MAX_IMAGE_DIMENSION) {
	            float widthRatio = ((float) rotatedWidth) / ((float) MAX_IMAGE_DIMENSION);
	            float heightRatio = ((float) rotatedHeight) / ((float) MAX_IMAGE_DIMENSION);
	            float maxRatio = Math.max(widthRatio, heightRatio);

	            // Create the bitmap from file
	            BitmapFactory.Options options = new BitmapFactory.Options();
	            options.inSampleSize = (int) maxRatio;
	            srcBitmap = BitmapFactory.decodeStream(is, null, options);
	        } else {
	            srcBitmap = BitmapFactory.decodeStream(is);
	        }
	        is.close();

	        /*
	         * if the orientation is not 0 (or -1, which means we don't know), we
	         * have to do a rotation.
	         */
	        if (orientation > 0) {
	            Matrix matrix = new Matrix();
	            matrix.postRotate(orientation);

	            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
	                    srcBitmap.getHeight(), matrix, true);
	        }

	        String type = context.getContentResolver().getType(photoUri);
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        if (type.equals("image/png")) {
	            srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
	        } else if (type.equals("image/jpg") || type.equals("image/jpeg")) {
	            srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
	        }
	        byte[] bMapArray = baos.toByteArray();
	        baos.close();
	        return bMapArray;
	    }

	    public static int getOrientation(Context context, Uri photoUri) {
	        /* it's on the external media. */
	        Cursor cursor = context.getContentResolver().query(photoUri,
	                new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

	        if (cursor.getCount() != 1) {
	            return -1;
	        }

	        cursor.moveToFirst();
	        return cursor.getInt(0);
	    }
	    
	    
	    @SuppressWarnings("deprecation")
		public static void setFriendsRequest(Context context)
	    {
	    	
//	    	Session session  = Session.openActiveSession(context);
//				String acess = session.getAccessToken();
//				FacebookUtilities.mFacebook.setAccessToken(acess);
//				//FacebookUtilities.mFacebook.setAccessToken(AtlasAndroidUser.getAccessToken());
//				FacebookUtilities.mAsyncRunner = new AsyncFacebookRunner( FacebookUtilities.mFacebook);
//	    	
////	    	String access_token = Session.getActiveSession()
////                    .getAccessToken();
////	    	mFacebook.setAccessToken(access_token);
//	    	mAsyncRunner = new AsyncFacebookRunner(mFacebook);
	    	if(mAsyncRunner != null){ //2013-03-20 TAN: avoid null pointer 
	    		mAsyncRunner.request("me/friends", new FriendsRequestListener());
	    	}
	    }
	    private static JSONArray friends;
	    
	    private static class FriendsRequestListener implements RequestListener 
		{           
		          
					@Override
		            public void onComplete(String response, Object state) 
		            {
		                    try 
		                    {
		                    	
		                            JSONObject json = Util.parseJson(response);
		                             friends = json.getJSONArray("data");        
		                             
		                             JSONObject friendRequest;
	                               //     String id,name,email,photo ;
	                                    
	                                    
	                                    for(int i =0 ; i<friends.length(); i++){
	                                    	
	                                    	String friend = "";
	                                        friendRequest= friends.getJSONObject(i);
	                                        friend = friendRequest.getString("id");
	                                    //    friend.setFirstName(friendRequest.getString("name"));
	                                    //    friend.setEmail(friendRequest.getString("email"));
	                                          
//	                                        myRequestFriendsMap.put(fRId, fRNm);
//	                                        intent.putExtra("map2",myRequestFriendsMap);
//	                                        startActivity(intent);
	                                        
	                                        
	                                        facebookFriends.add(friend);
	                                    }
	                                    
		                             
		                             
		                             
		                    } 
		                    catch (JSONException e) 
		                    {
		                            Log.e("OnComplete","JSONException");
		                    }
		                    catch (FacebookError e) 
		                    {
		                            Log.e("OnComplete","FacebookError");
		                    }
		            }
		            @Override
		            public void onIOException(IOException e, Object state)
		            {
		                    Log.e("FriendsRequest","onIOException");
		            }
		            @Override
		            public void onFileNotFoundException(FileNotFoundException e,Object state)
		            {
		                    Log.e("FriendsRequest","onFileNotFoundException");
		            }
		            @Override
		            public void onMalformedURLException(MalformedURLException e,Object state)
		            {
		                    Log.e("FriendsRequest","onMalformedURLException");
		            }
		            @Override
		            public void onFacebookError(FacebookError e, Object state)
		            {
		                    Log.e("FriendsRequest","onFacebookError");
		            }
		    }
	    
	    
	
	    
	    
	    static void closeQuietly(Closeable closeable) {
	        try {
	            if (closeable != null) {
	                closeable.close();
	            }
	        } catch (IOException ioe) {
	            // ignore
	        }
	    }


}
