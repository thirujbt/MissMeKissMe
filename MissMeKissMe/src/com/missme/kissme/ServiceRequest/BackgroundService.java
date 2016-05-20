package com.missme.kissme.ServiceRequest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.missme.kissme.Utils.Constants;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

public class BackgroundService extends Service{
	GetXMLTask jsonOperations;
	Context context=this;
	String GpsStatus;
	@Override
	public IBinder onBind(Intent intent){
		return null;
	}
	public BackgroundService() {
	}
	public void onCreate(){
		super.onCreate();
		jsonOperations = new GetXMLTask();
		mTimer = new Timer();
		//mTimer.schedule(timerTask, 0, 6000 * 10*1);
		mTimer.schedule(timerTask, 0, 2000);
	}

	private Timer mTimer;
	TimerTask timerTask = new TimerTask() {
		public void run(){
			try {
				jsonOperations = new GetXMLTask();
				String path = Constants.BACKGROUND_SERVICE_URL;
				jsonOperations.execute(new String[] { path });
			} catch (Exception e) {
			}
		}
	};
	private class GetXMLTask extends AsyncTask<String, Void, String>{
		@Override
		protected String doInBackground(String... urls) {
			synchronized(this){
				String output = null;
				for (String url : urls) {
					output = getOutputFromUrl(url);
				}
				return output;
			}
			
		}    
		private String getOutputFromUrl(String url) {
			String output = null;
			try {
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpGet = new HttpPost(url);
				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				output = EntityUtils.toString(httpEntity);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
 			return output;
		}

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