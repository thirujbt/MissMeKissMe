package com.missme.kissme.MenuItems;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.missme.kissme.MainFragmentMenu;
import com.missme.kissme.R;
import com.missme.kissme.AccountManager.LoginActivity;
import com.missme.kissme.MapService.GPSTracker;
import com.missme.kissme.Utils.AndroidMultiPartEntity;
import com.missme.kissme.Utils.Constants;
import com.missme.kissme.Utils.AndroidMultiPartEntity.ProgressListener;

public class DelayedKissFifthFrag extends Fragment implements OnClickListener{

	ImageView backIcon,titleIcon,process;
	TextView title,timeTitle,timeValue;
	Button send;
	ArrayList<String> videoDetails;
	SharedPreferences pref,pref1;
	TextView nameTxt,vechicleTxt,attachTxt,msgTxt;
	String fromUSerId,toUserName,toUserAuthId,vechicle,attachType,attachFile,msg,attachTypeName,toUserLat,toUserLang,fromLat,fromLong,delayTime,delayDate,delayDateTime;
	GPSTracker gps;
	String currentDateandTime,selectedTime,selectedDate;
	private String userid;
	private String gcmid;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_realtimekissfifth, container,false);
		pref = getActivity().getSharedPreferences(Constants.DELAY_MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
		pref1 = getActivity().getSharedPreferences(Constants.MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
		videoDetails=new ArrayList<String>();
		gps=new GPSTracker(getActivity());
		backIcon=(ImageView)view.findViewById(R.id.realtimefifth_back_icon);
		send=(Button)view.findViewById(R.id.kiss_send);
		titleIcon=(ImageView)view.findViewById(R.id.realtime_icon5);
		titleIcon.setBackgroundResource(R.drawable.delayed_kiss_icon);
		title=(TextView)view.findViewById(R.id.realnewKiss_txtfifth);
		timeTitle=(TextView)view.findViewById(R.id.realnewKiss_txt3);
		timeValue=(TextView)view.findViewById(R.id.realnewKiss_txt_vechicle);
		process=(ImageView)view.findViewById(R.id.realtime_process_iconfifth);
		process.setBackgroundResource(R.drawable.process3);
		process.setVisibility(View.GONE);
		title.setText("Delayed Kiss");
		TextView titlee=(TextView)view.findViewById(R.id.newKiss_txtfifth);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"SourceSansPro-Regular.otf");
		titlee.setTypeface(font);
		title.setTypeface(font);		
		nameTxt=(TextView)view.findViewById(R.id.realnewKiss_txt_name);
		attachTxt=(TextView)view.findViewById(R.id.realnewKiss_txt_attach);
		msgTxt=(TextView)view.findViewById(R.id.realnewKiss_txt_msg);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		currentDateandTime = sdf.format(new Date());
		fromUSerId=pref1.getString(Constants.USER_AUTHKEY_PREF, null);
		toUserName=pref.getString(Constants.DELAY_TO_USER_NAME_PREF, null);
		toUserAuthId=pref.getString(Constants.DELAY_USER_AUTHID_PREF, null);
		vechicle=pref.getString(Constants.DELAY_TRAVEL_TYPE_PREF, null);
		attachType=pref.getString(Constants.DELAY_ATTACHMENT_TYPE_PREF, null);
		attachTypeName=pref.getString(Constants.DELAY_ATTACHMENT_TYPE_NAME_PREF, null);
		attachFile=pref.getString(Constants.DELAY_ATTACHMENT_PREF, null);
		msg=pref.getString(Constants.DELAY_MESSAGE_PREF, null);
		delayTime=pref.getString(Constants.DELAY_TIME_PREF, null);
		delayDate=pref.getString(Constants.DELAY_DATE_PREF, null);
		selectedTime=pref.getString(Constants.SELECTED_TIME_PREF, null);
		selectedDate=pref.getString(Constants.SELECTED_DATE_PREF, null);
		delayDateTime=delayDate+"  "+delayTime;
		toUserLat=pref.getString(Constants.DELAY_TO_USER_LAT_PREF, null);
		toUserLang=pref.getString(Constants.DELAY_TO_USER_LANG_PREF, null);
		// 
		userid = pref1.getString(Constants.USER_AUTHKEY_PREF, null);
		gcmid = pref1.getString("registration_id", null);
		fromLat=String.valueOf(gps.getLatitude());
		fromLong=String.valueOf(gps.getLongitude());
		nameTxt.setText(toUserName);
		attachTxt.setText(attachType);
		msgTxt.setText(msg);
		timeTitle.setText("Time to delivery kiss");
		timeValue.setText(selectedTime+"  "+selectedDate);
		backIcon.setOnClickListener(this);
		send.setOnClickListener(this);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	@Override
	public void onClick(View v) {
		int id=v.getId();
		switch (id) {
		case R.id.realtimefifth_back_icon:
			Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
			intent.putExtra("homepage", "delayedkissfourth");
			getActivity().startActivity(intent);
			getActivity().finish();
			break;
		case R.id.kiss_send:
			sendKiss();
			break;
		}
	}

	public void sendKiss(){
		if(isInternetOn()){
			try {
				String allDatas=attachFile+"@"+fromUSerId+"@"+toUserAuthId+"@"+"delayed"+"@"+delayDateTime+"@"+msg+"@"+attachTypeName+"@"+toUserLat+"@"+toUserLang+"@"+fromLat+"@"+fromLong+"@"+currentDateandTime+"@"+gcmid;
				videoDetails.add(allDatas);
				for(int i=0;i<videoDetails.size();i++){
					String data=videoDetails.get(i);
					uploadDetails(data);
				}
				videoDetails.clear();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(getActivity(), Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
		}
	}
	private void uploadDetails(String datas){
		new UploadFileToServer(datas).execute();
	}

	//Check Internet connection
	public final boolean isInternetOn() {
		ConnectivityManager connec = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
				|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
			return true;
		} else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
				|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
			return false;
		}
		return false;
	}
	/**
	 * Uploading the file to server
	 * */
	private class UploadFileToServer extends AsyncTask<Void, Integer, String> {

		Dialog alertProgressDialog = null;
		long totalSize = 0;
		TextView progressTxt;
		String[] data;
		public UploadFileToServer(String datas) {
			data=datas.split("@");
		}

		@Override
		protected void onPreExecute() {
			if (alertProgressDialog == null) {
				alertProgressDialog = new Dialog(getActivity(),android.R.style.Theme_Translucent);
				alertProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				alertProgressDialog.setContentView(R.layout.progress_video_upload);
				alertProgressDialog.setCancelable(true);
				alertProgressDialog.setCanceledOnTouchOutside(true);
				progressTxt = (TextView) alertProgressDialog.findViewById(R.id.progress_txt);
				alertProgressDialog.show();
			}
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			progressTxt.setText(progress[0] + "%");
		}

		@Override
		protected String doInBackground(Void ...param) {
			return uploadFile();
		}
		private String uploadFile() {
			String responseString = null;
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Constants.REALTIME_SEND_KISS_URL);
			try {
				AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
						new ProgressListener() {
							@Override
							public void transferred(long num) {
								publishProgress((int) ((num / (float) totalSize) * 100));
							}
						});
				// Adding file data to http body
				// Extra parameters if you want to pass to server
				String fromUserId = data[1];
				String toUserId =data[2];
				String kissType = data[3];
				String delayedDateTime = data[4];
				String msg = data[5];
				String attachTypeName = data[6];
				String toUserLat = data[7];
				String toUSerLang = data[8];
				String fromLat = data[9];
				String toLat = data[10];
				String currentDateTime = data[11];
				String gcmid=data[12];
				final File sourceFile = new File(data[0]);
			if(attachFile == null){
					entity.addPart("fromauthkey", new StringBody(fromUserId));
					entity.addPart("toauthkey", new StringBody(toUserId));
					entity.addPart("type", new StringBody(kissType));
					entity.addPart("delay_datetime", new StringBody(delayedDateTime));
					entity.addPart("message", new StringBody(msg));
					entity.addPart("attach_type", new StringBody(attachTypeName));
					entity.addPart("to_lat", new StringBody(toUserLat));
					entity.addPart("to_lang", new StringBody(toUSerLang));
					entity.addPart("from_lat", new StringBody(fromLat));
					entity.addPart("from_lang", new StringBody(toLat));
					entity.addPart("user_time", new StringBody(currentDateTime));
					entity.addPart("gcmid", new StringBody(gcmid));
					entity.addPart("authkey", new StringBody(userid));
					System.out.println("gcmid..."+gcmid);
					System.out.println("authkey..."+userid);
					if(LoginActivity.getSessionname(getActivity())!=null){
						entity.addPart(Constants.SESSION_NAME, new StringBody(LoginActivity.getSessionname(getActivity())));
						}
				}else{
					entity.addPart(Constants.ATR_VIDEO_FILE, new FileBody(sourceFile));
					entity.addPart("fromauthkey", new StringBody(fromUserId));
					entity.addPart("toauthkey", new StringBody(toUserId));
					entity.addPart("type", new StringBody(kissType));
					entity.addPart("delay_datetime", new StringBody(delayedDateTime));
					entity.addPart("message", new StringBody(msg));
					entity.addPart("attach_type", new StringBody(attachTypeName));
					entity.addPart("to_lat", new StringBody(toUserLat));
					entity.addPart("to_lang", new StringBody(toUSerLang));
					entity.addPart("from_lat", new StringBody(fromLat));
					entity.addPart("from_lang", new StringBody(toLat));
					entity.addPart("user_time", new StringBody(currentDateTime));
					entity.addPart("gcmid", new StringBody(gcmid));
					entity.addPart("authkey", new StringBody(userid));
					System.out.println("gcmid..."+gcmid);
					System.out.println("authkey..."+userid);
					if(LoginActivity.getSessionname(getActivity())!=null){
						entity.addPart(Constants.SESSION_NAME, new StringBody(LoginActivity.getSessionname(getActivity())));
						}
				}				
				totalSize = entity.getContentLength();
				httppost.setEntity(entity);
				// Making server call
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity r_entity = response.getEntity();
				responseString = EntityUtils.toString(r_entity);
			} catch (ClientProtocolException e) {
				responseString = e.toString();
			} catch (Exception e) {
				responseString = e.toString();
			}
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
				try {
					if (alertProgressDialog != null && alertProgressDialog.isShowing()) {
						alertProgressDialog.dismiss();
						alertProgressDialog = null;
					}
					String statusCode,msgResp;
					JSONObject jsonObjects = new JSONObject(result);
					statusCode = jsonObjects.getString(Constants.STATUS_CODE);
					msgResp = jsonObjects.getString(Constants.RESPONSE_MESSAGES);
					switch(statusCode){
					case Constants.KISS_SENT_SUCCESS:
						showKissSentAlert();
						break;
					case Constants.KISS_SENT_FAILED:
						showFaildAlert(msgResp);
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			super.onPostExecute(result);
		}
	}
	private void showKissSentAlert() {

		final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.kiss_send_alert);
		final TextView goHomeTxt = (TextView)dialog.findViewById(R.id.go_home_txt);
		final TextView timeTxt = (TextView)dialog.findViewById(R.id.sucess);
		final TextView yourTxt = (TextView)dialog.findViewById(R.id.your);
		final Button okbtn = (Button)dialog.findViewById(R.id.Track_ok_btn);
		final ImageView profImage = (ImageView)dialog.findViewById(R.id.realtime_final);
		profImage.setBackgroundResource(R.drawable.popup_kiss_logo);
		okbtn.setText("OK");
		yourTxt.setText("Your kiss will be delivered at");
		timeTxt.setText(selectedTime+"\n"+selectedDate);
		goHomeTxt.setText(Html.fromHtml(getString(R.string.go_home)));
		okbtn.setVisibility(View.GONE);
		goHomeTxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
				getActivity().startActivity(intent);
				getActivity().finish();
				dialog.dismiss();
				clearSherPref();
			}
		});
		dialog.show();
	}
	private void showFaildAlert(String msg) {

		final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.signup_exiting_dialog);
		final Button ok = (Button)dialog.findViewById(R.id.alert_dialog_ok_btn);
		final TextView alertText = (TextView)dialog.findViewById(R.id.alert_dialog_txt);
		final EditText editText = (EditText)dialog.findViewById(R.id.OTP_edit);
		alertText.setText(msg);
		editText.setVisibility(View.GONE);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
				getActivity().startActivity(intent);
				getActivity().finish();
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	public void onDestroy() {
		super.onDestroy();
		
	}
	public void clearSherPref(){
		SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.DELAY_MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.clear();
		editor.commit();
	}
}