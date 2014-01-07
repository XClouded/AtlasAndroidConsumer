package atlasapp.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.parse.codec.binary.StringUtils;

import android.content.Context;
import android.util.Log;

public final class Utilities {

	public static Context ctx;

	public final static void init(Context aCtx) {
		ctx = aCtx;
	}

	public final static HashMap<String, String> eHash(
			ArrayList<HashMap<String, String>> c) {
		HashMap<String, String> h;
		if (c.isEmpty()) {
			h = new HashMap<String, String>();
		} else {
			h = (HashMap<String, String>) c.get(0);
		}
		return h;
	}

	public final static String eString(String key,
			ArrayList<HashMap<String, String>> c) {
		return (String) eHash(c).get(key);
	}

	public final static String deNull(Object o) {
		return replaceNull(o, "");
	}

	public final static String replaceNull(Object o, String replacer) {
		if (o == null || ((String) o).toUpperCase().equals("NULL")) {
			return replacer;
		}
		return (String) o;
	}

	public final static String toString(boolean b) {
		return b ? "1" : "0";
	}

	public final static boolean toBoolean(Object o) {
		return ((String) o).equals("1");
	}

	public final static String getID() {
		// RANDOM NUMBER
		// ------------------------------------------------------------------------------------------------
		// -------------------------------------------------------------------------------------------------------------
		int maxRandomValue;
		int intRandomNumber;
		String strRandomNumber = "";
		String strRandomString = "";
		maxRandomValue = (int) (Math.pow(2, 32) - 1);
		String padString = "";
		int desiredRandomLength = 8; // need constant

		// Generate a 32-byte hex (128-bit binary) number 4 times, concatenating
		// each 8-byte string to get a 32-byte string:
		int ktr = 0;
		for (ktr = 1; ktr <= 4; ktr++) {

			// Generate a 32-bit random number
			Random rn = new Random();
			intRandomNumber = rn.nextInt();
			Log.v("String getID", "String getID : " + intRandomNumber);

			// Format the 32-bit random number as hex:
			strRandomNumber = String.format("%X", intRandomNumber);
			Log.v("String strRandomNumber", "String strRandomNumber : "
					+ strRandomNumber);

			// Pad the 32-bit random number on the left with zeros to
			// desiredRandomLength const value:
			if (strRandomNumber.length() < desiredRandomLength) {
				padString = "";
				
//				padString= StringUtils.// NGHIA : need to padding
				
				// padString = [padString stringByPaddingToLength:
				// (desiredRandomLength - [strRandomNumber length]) withString:
				// @"0"
				// startingAtIndex: 0] ;
				
				strRandomNumber = padString + strRandomNumber;
			}

			// Build the random string by concatenating with last random number
			// string
			Log.v("String strRandomString", "String strRandomString : "
					+ strRandomString + strRandomNumber);
		}
		// -------------------------------------------------------------------------------------------------------------

		return strRandomString;
	}
}
