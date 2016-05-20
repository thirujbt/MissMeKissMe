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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Window;

import com.missme.kissme.R;
import com.missme.kissme.InterfaceClass.AsyncResponse;

public class KissMeAsyncTask extends AsyncTask<JSONObject, Void, String> {

	Context ctn;
	String xml,url;
	int RespInt;
	Dialog alertProgressDialog = null;
	public AsyncResponse responseInterface ;
	Activity activity;
	public KissMeAsyncTask(Context context, String url, int respInt, AsyncResponse respAsyn){
		activity = (Activity) context;
		this.ctn = context;
		this.url = url;
		this.responseInterface = respAsyn;
		this.RespInt = respInt;
	}

	protected void onPreExecute() {
		if(!activity.isFinishing()){
			if (alertProgressDialog == null)
			alertProgressDialog = new Dialog(ctn, android.R.style.Theme_Translucent);
			alertProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertProgressDialog.setContentView(R.layout.custom_progressbar);
			alertProgressDialog.setCancelable(true);
			alertProgressDialog.setCanceledOnTouchOutside(true);
			alertProgressDialog.show();
		}
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
			if (alertProgressDialog != null && alertProgressDialog.isShowing()&& !activity.isFinishing()) {
				alertProgressDialog.dismiss();
				alertProgressDialog = null;
			}	
		}
		catch(Exception ex){			
		}
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
