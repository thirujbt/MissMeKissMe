package com.missme.kissme.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Class contains extra functions related to this app
 * 
 * @author Sathish
 *
 */
public class Utilities {

	/**
	 * Check any network is connected
	 * 
	 * @param context
	 * @return true if network is present otherwise false
	 */
	public static boolean checkNetworkConnection(Context context) {

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null) {
			NetworkInfo[] networkInfos = connectivityManager
					.getAllNetworkInfo();
			if (networkInfos != null) {
				for (int i = 0; i < networkInfos.length; i++) {
					if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Check the WIFI connection is present
	 * 
	 * @param context
	 * @return true if WIFI is present otherwise false
	 */
	public static boolean checkWifiConnected(Context context) {
		
		 ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
         // ARE WE CONNECTED TO THE NET
         if (connectivityManager.getActiveNetworkInfo() != null
                 && connectivityManager.getActiveNetworkInfo().isAvailable()
                 && connectivityManager.getActiveNetworkInfo().isConnected()) 
            return true;
         else
        	 return false;
		
		
		
	}
	
	/*public static boolean check2Gor3Gor4GConnection(Context context) {

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);  
		
		if (info != null) {
			if (info.getState() == NetworkInfo.State.CONNECTED) {
				return true;
			}
		}
		
		return false;
	}
*/
	/**
	 * Get the date format for server when uploading the data
	 * 
	 * @param date
	 * @return yyyy-MM-dd format data from MM/dd/yyyy
	 */
	public static String getDateFormatForSetver(String date) {
		Date dateObj = null;
		String datetime = null;
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh",
				Locale.getDefault());
		try {
			dateObj = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// Change to server format
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd",
				Locale.getDefault());

		if (dateObj != null) {
			datetime = dateformat.format(dateObj);
		}

		return datetime;
	}

	/**
	 * Show the toast to the user
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showToast(Context context, String msg) {
		Toast toast=Toast.makeText(context, msg, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	/**
	 * Display the log for developer
	 * 
	 * @param t1
	 * @param t2
	 */
	public static void showLog(String t1, String t2) {
		Log.e(t1, t2);
	}
	


}
