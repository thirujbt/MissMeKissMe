package com.missme.kissme.ServiceRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import com.missme.kissme.InterfaceClass.HomeAsyncResponse;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class MissMeKissMeAsyncTask extends AsyncTask<JSONObject, Void, String> {

	Context ctn;
	String xml,url;
	int RespInt;
	Dialog alertProgressDialog = null;
	public HomeAsyncResponse responseInterface ;
	Activity activity;
	public MissMeKissMeAsyncTask(Context context, String url, int respInt, HomeAsyncResponse respAsyn){
		activity = (Activity) context;
		this.ctn = context;
		this.url = url;
		this.responseInterface = respAsyn;
		this.RespInt = respInt;
	}

	protected void onPreExecute() {

	}

	@Override
	protected String doInBackground(JSONObject... jsonObj) {
		synchronized(this){
			InputStream inputStream = null;
			String result = "";
			String json = "";
			try {				
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				json = jsonObj[0].toString();
				StringEntity se = new StringEntity(json);
				httpPost.setEntity(se);
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
	@SuppressLint("NewApi")
	@Override
	protected void onPostExecute(String result) {
		try{	
			if (android.os.Build.VERSION.SDK_INT >= 17) {
				if(activity!=null && !activity.isDestroyed())
					responseInterface.onProcessFinish(result , RespInt);
			}else{
				if(activity!=null)
					responseInterface.onProcessFinish(result , RespInt);
			}
		}
		catch(Exception ex){}
	}

	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while((line = bufferedReader.readLine()) != null)
			result += line;
		inputStream.close();
		System.out.println("Response...................."+result);
		return result;
	}
}