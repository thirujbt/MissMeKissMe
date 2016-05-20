package com.missme.kissme.ServiceRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import com.missme.kissme.MapService.GPSTracker;
import com.missme.kissme.Utils.Constants;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class GPSStatusUpdateService extends Service{
	GetXMLTask jsonOperations;
	Context context=this;
	String GpsStatus;
	JSONObject jsonObject;
	GPSTracker gps;
	SharedPreferences pref;
	@Override
	public IBinder onBind(Intent intent){
		return null;
	}
	public GPSStatusUpdateService() {
		
	}
	public void onCreate(){
		super.onCreate();
		gps=new GPSTracker(GPSStatusUpdateService.this);
		pref = GPSStatusUpdateService.this.getSharedPreferences(Constants.MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
		mTimer = new Timer();
		mTimer.schedule(timerTask, 0, 6000 * 10 * 3);
	}

	private Timer mTimer;
	TimerTask timerTask = new TimerTask() {
		public void run(){
			try {
				displayGpsStatus();
				String lat=String.valueOf(gps.getLatitude());
				String lang=String.valueOf(gps.getLongitude());
				String url=Constants.BACKGROUND_GPS_STATUS_SERVICE_URL;
				String uSerId=pref.getString(Constants.USER_AUTHKEY_PREF, null);
				JSONObject jsonObject = new JSONObject();
				jsonObject.accumulate("authkey",uSerId);
				jsonObject.accumulate("gps_status",GpsStatus);
				jsonObject.accumulate("latitude",lat);
				jsonObject.accumulate("longitude",lang);
				
				 new GetXMLTask(url).execute(jsonObject);			 
			} catch (Exception e) {
				System.out.println("DashBoard 8");
			}
		}
	};
	private void displayGpsStatus() {
		ContentResolver contentResolver = getBaseContext().getContentResolver();
		boolean gpsStatus = Settings.Secure.isLocationProviderEnabled(contentResolver, LocationManager.GPS_PROVIDER);
		if (gpsStatus) {			
			GpsStatus="on";
		} else {
			GpsStatus="off";
		}
	}		
	private class GetXMLTask extends AsyncTask<JSONObject, Void, String>{

		String url;
		public GetXMLTask(String url){
			this.url = url;
		}	
		@Override
		protected String doInBackground(JSONObject... jsonObj) {
			synchronized(this){
				System.gc();
				InputStream inputStream = null;
				String result = "";
				String json = "";
				try {
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost(url);
					json = jsonObj[0].toString();
					StringEntity se = new StringEntity(json);
					httpPost.setEntity(se);
					httpPost.setHeader("Accept", "application/json");
					httpPost.setHeader("Content-type", "application/json");
					HttpResponse httpResponse = httpclient.execute(httpPost);
					inputStream = httpResponse.getEntity().getContent();
					if(inputStream != null)
						result = convertInputStreamToString(inputStream);
					else
						result = "Did not work!";

				} catch (Exception e) {
					Log.d("InputStream", e.getLocalizedMessage());
				}
				return result;
			}
			
		}   	
		@Override
		protected void onPostExecute(String result) {
			
		}
	}
	
	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while((line = bufferedReader.readLine()) != null)
			result += line;
		inputStream.close();
		return result;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// Let it continue running until it is stopped.
		return 0;
	}
	public void onDestroy() {
		super.onDestroy();
		try {
			mTimer.cancel();
			timerTask.cancel();	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}