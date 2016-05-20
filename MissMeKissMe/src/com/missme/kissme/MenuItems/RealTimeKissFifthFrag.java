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
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
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
import com.missme.kissme.Animation.AnimatorPath;
import com.missme.kissme.Animation.PathEvaluator;
import com.missme.kissme.Animation.PathPoint;
import com.missme.kissme.Animation.SoundMeter;
import com.missme.kissme.MapService.GPSTracker;
import com.missme.kissme.Utils.AndroidMultiPartEntity;
import com.missme.kissme.Utils.AndroidMultiPartEntity.ProgressListener;
import com.missme.kissme.Utils.Constants;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.animation.AnimatorProxy;

public class RealTimeKissFifthFrag extends Fragment implements OnClickListener{

	ImageView backIcon,titleIcon,process;
	ArrayList<String> videoDetails;
	Button send;
	SharedPreferences pref,pref1;
	TextView nameTxt,vechicleTxt,attachTxt,msgTxt;
	String fromUSerId,toUserName,toUserAuthId,vechicle,attachType,attachFile,msg,attachTypeName,toUserLat,toUserLang,fromLat,fromLong;
	GPSTracker gps;
	String currentDateandTime;
	Editor editor;
	ImageView mButton;
	AnimatorProxy mButtonProxy;
	PathEvaluator mEvaluator = new PathEvaluator();
	String gcmid;
	/* constants */
	private static final int POLL_INTERVAL = 300;
	/** running state **/
	private boolean mRunning = false;
	/** config state **/
	private int mThreshold;
	private PowerManager.WakeLock mWakeLock;
	private Handler mHandler = new Handler();
	/* References to view elements */
	/* data source */
	private SoundMeter mSensor;
	/****************** Define runnable thread again and again detect noise *********/
	private Runnable mSleepTask = new Runnable() {
		public void run() {
			start();
		}
	};
	// Create runnable thread to Monitor Voice
	private Runnable mPollTask = new Runnable() {
		public void run() {
			double amp = mSensor.getAmplitude();
			updateDisplay("Monitoring Voice...", amp);
			if ((amp > mThreshold)) {
				callForHelp();
			}
			// Runnable(mPollTask) will again execute after POLL_INTERVAL
			mHandler.postDelayed(mPollTask, POLL_INTERVAL);
		}
	};
	/*********************************************************/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_realtimekissfifth, container,false);
		pref = getActivity().getSharedPreferences(Constants.REAL_MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
		pref1 = getActivity().getSharedPreferences(Constants.MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
		editor = pref.edit();
		videoDetails=new ArrayList<String>();
		gps=new GPSTracker(getActivity());
		mSensor = new SoundMeter();
		backIcon=(ImageView)view.findViewById(R.id.realtimefifth_back_icon);
		mButton=(ImageView)view.findViewById(R.id.sent_kiss_image);
		mButtonProxy = AnimatorProxy.wrap(mButton);
		send=(Button)view.findViewById(R.id.kiss_send);
		titleIcon=(ImageView)view.findViewById(R.id.realtime_icon5);
		titleIcon.setBackgroundResource(R.drawable.realtime_kiss_icon);
		process=(ImageView)view.findViewById(R.id.realtime_process_iconfifth);
		process.setBackgroundResource(R.drawable.process3);
		process.setVisibility(View.GONE);
		nameTxt=(TextView)view.findViewById(R.id.realnewKiss_txt_name);
		vechicleTxt=(TextView)view.findViewById(R.id.realnewKiss_txt_vechicle);
		attachTxt=(TextView)view.findViewById(R.id.realnewKiss_txt_attach);
		msgTxt=(TextView)view.findViewById(R.id.realnewKiss_txt_msg);
		TextView titlee=(TextView)view.findViewById(R.id.newKiss_txtfifth);
		TextView titlBar=(TextView)view.findViewById(R.id.realnewKiss_txtfifth);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"SourceSansPro-Regular.otf");
		titlee.setTypeface(font);
		titlBar.setTypeface(font);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		currentDateandTime = sdf.format(new Date());
		fromUSerId=pref1.getString(Constants.USER_AUTHKEY_PREF, null);
		toUserName=pref.getString(Constants.REAL_TO_USER_NAME_PREF, null);
		toUserAuthId=pref.getString(Constants.REAL_USER_AUTHID_PREF, null);
		vechicle=pref.getString(Constants.REAL_TRAVEL_TYPE_PREF, null);
		attachType=pref.getString(Constants.REAL_ATTACHMENT_TYPE_PREF, null);
		attachTypeName=pref.getString(Constants.REAL_ATTACHMENT_TYPE_NAME_PREF, null);
		attachFile=pref.getString(Constants.REAL_ATTACHMENT_PREF, null);
		msg=pref.getString(Constants.REAL_MESSAGE_PREF, null);
		toUserLat=pref.getString(Constants.REAL_TO_USER_LAT_PREF, null);
		toUserLang=pref.getString(Constants.REAL_TO_USER_LANG_PREF, null);
		gcmid=pref1.getString("registration_id", null);
		
		System.out.println("checking gcm id................."+gcmid);
		fromLat=String.valueOf(gps.getLatitude());
		fromLong=String.valueOf(gps.getLongitude());
		nameTxt.setText(toUserName);
		vechicleTxt.setText("Real Time Kiss by "+vechicle);
		if(vechicle.equalsIgnoreCase("walk")){
			mButton.setBackgroundResource(R.drawable.running_anim);
		}else if(vechicle.equalsIgnoreCase("cycle")){
			mButton.setBackgroundResource(R.drawable.cycle_anim);
		}else if(vechicle.equalsIgnoreCase("car")){
			mButton.setBackgroundResource(R.drawable.car_anim);
		}else if(vechicle.equalsIgnoreCase("train")){
			mButton.setBackgroundResource(R.drawable.train_anim);
		}else if(vechicle.equalsIgnoreCase("boat")){
			mButton.setBackgroundResource(R.drawable.boat_anim);
		}else if(vechicle.equalsIgnoreCase("plane")){
			mButton.setBackgroundResource(R.drawable.aeroplane_anim);
		}
		attachTxt.setText(attachType);
		msgTxt.setText(msg);
		backIcon.setOnClickListener(this);
		PowerManager pm = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "NoiseAlert");
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		if(vechicle.equalsIgnoreCase("plane")){
			// Set up the path we're animating along
			AnimatorPath path = new AnimatorPath();
			path.moveTo(10, 10);
			path.lineTo(-900, -600);
			// Set up the animation
			final ObjectAnimator anim = ObjectAnimator.ofObject(this, "buttonLoc",
					new PathEvaluator(), path.getPoints().toArray());
			anim.setDuration(3000);
			send.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mButton.setVisibility(View.VISIBLE);
					anim.start();
					sendKiss();
				}
			});
		}else{
			// Set up the path we're animating along
			AnimatorPath path = new AnimatorPath();
			path.moveTo(10, 10);
			path.lineTo(-900, 20);
			// Set up the animation
			final ObjectAnimator anim = ObjectAnimator.ofObject(this, "buttonLoc",
					new PathEvaluator(), path.getPoints().toArray());
			anim.setDuration(3000);
			send.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mButton.setVisibility(View.VISIBLE);
					anim.start();
					sendKiss();
				}
			});
		}
		initializeApplicationConstants();
		if (!mRunning) {
			mRunning = true;
			start();
		}
	}
	@Override
	public void onStop() {
		super.onStop();
		stop();
	}
	private void start() {
		mSensor.start();
		if (!mWakeLock.isHeld()) {
			mWakeLock.acquire();
		}
		//Noise monitoring start
		mHandler.postDelayed(mPollTask, POLL_INTERVAL);
	}
	private void stop() {
		if (mWakeLock.isHeld()) {
			mWakeLock.release();
		}
		mHandler.removeCallbacks(mSleepTask);
		mHandler.removeCallbacks(mPollTask);
		mSensor.stop();
		updateDisplay("stopped...", 0.0);
		mRunning = false;
	}
	private void initializeApplicationConstants() {
		// Set Noise Threshold
		mThreshold = 8;
	}
	private void updateDisplay(String status, double signalEMA) {
		if(signalEMA == 12.135925925925926){
			mButton.setVisibility(View.VISIBLE);
			if(vechicle.equalsIgnoreCase("plane")){
				// Set up the path we're animating along
				AnimatorPath path = new AnimatorPath();
				path.moveTo(10, 10);
				path.lineTo(-900, -600);
				// Set up the animation
				final ObjectAnimator anim = ObjectAnimator.ofObject(this, "buttonLoc",
						new PathEvaluator(), path.getPoints().toArray());
				anim.setDuration(2000);
				anim.start();
				sendKiss();
			}else{
				// Set up the path we're animating along
				AnimatorPath path = new AnimatorPath();
				path.moveTo(10, 10);
				path.lineTo(-900, 20);
				// Set up the animation
				final ObjectAnimator anim = ObjectAnimator.ofObject(this, "buttonLoc",
						new PathEvaluator(), path.getPoints().toArray());
				anim.setDuration(2000);
				anim.start();
				sendKiss();
			}
		}
	}
	private void callForHelp() {
		// Show alert when noise thersold crossed
		// Toast.makeText(getActivity(), "Noise Thersold Crossed, do here your stuff.", Toast.LENGTH_SHORT).show();
	}
	/**
	 * We need this setter to translate between the information the animator
	 * produces (a new "PathPoint" describing the current animated location)
	 * and the information that the button requires (an xy location). The
	 * setter will be called by the ObjectAnimator given the 'buttonLoc'
	 * property string.
	 */
	public void setButtonLoc(PathPoint newLoc) {
		mButtonProxy.setTranslationX(newLoc.mX);
		mButtonProxy.setTranslationY(newLoc.mY);
	}
	@Override
	public void onClick(View v) {
		int id=v.getId();
		switch (id) {
		case R.id.realtimefifth_back_icon:
			Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
			intent.putExtra("homepage", "realtimefourth");
			getActivity().startActivity(intent);
			getActivity().finish();
			break;
		}
	}
	public void sendKiss(){
		if(isInternetOn()){
			try {
				String allDatas=attachFile+"@"+fromUSerId+"@"+toUserAuthId+"@"+"realtime"+"@"+vechicle+"@"+msg+"@"+attachTypeName+"@"+toUserLat+"@"+toUserLang+"@"+fromLat+"@"+fromLong+"@"+currentDateandTime+"@"+gcmid;
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
	private void showKissSentAlert() {

		final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.kiss_send_alert);
		final TextView goHomeTxt = (TextView)dialog.findViewById(R.id.go_home_txt);
		final Button track = (Button)dialog.findViewById(R.id.Track_ok_btn);
		final ImageView profImage = (ImageView)dialog.findViewById(R.id.realtime_final);
		goHomeTxt.setText(Html.fromHtml(getString(R.string.go_home)));
		profImage.setBackgroundResource(R.drawable.popup_kiss_logo);
		track.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent trackIntent = new Intent(getActivity(), MainFragmentMenu.class);
				trackIntent.putExtra("homepage", "trackkiss");
				getActivity().startActivity(trackIntent);
				getActivity().finish();
				dialog.dismiss();
				//clearSharePref();
			}
		});
		goHomeTxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
				getActivity().startActivity(intent);
				getActivity().finish();
				dialog.dismiss();
				//clearSharePref();
			}
		});
		dialog.show();
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
			stop();
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
			String userid = pref1.getString(Constants.USER_AUTHKEY_PREF, null);
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
				String vechicle = data[4];
				String msg = data[5];
				String attachTypeName = data[6];
				String toUserLat = data[7];
				String toUSerLang = data[8];
				String fromLat = data[9];
				String toLat = data[10];
				String currentDateTime = data[11];
				String gcmId=data[12];
				final File sourceFile = new File(data[0]);
				if(attachFile == null){
					entity.addPart("fromauthkey", new StringBody(fromUserId));
					entity.addPart("toauthkey", new StringBody(toUserId));
					entity.addPart("type", new StringBody(kissType));
					entity.addPart("vehicle", new StringBody(vechicle));
					entity.addPart("message", new StringBody(msg));
					entity.addPart("attach_type", new StringBody(attachTypeName));
					entity.addPart("to_lat", new StringBody(toUserLat));
					entity.addPart("to_lang", new StringBody(toUSerLang));
					entity.addPart("from_lat", new StringBody(fromLat));
					entity.addPart("from_lang", new StringBody(toLat));
					entity.addPart("user_time", new StringBody(currentDateTime));
					entity.addPart("gcmid", new StringBody(gcmId));
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
					entity.addPart("vehicle", new StringBody(vechicle));
					entity.addPart("message", new StringBody(msg));
					entity.addPart("attach_type", new StringBody(attachTypeName));
					entity.addPart("to_lat", new StringBody(toUserLat));
					entity.addPart("to_lang", new StringBody(toUSerLang));
					entity.addPart("from_lat", new StringBody(fromLat));
					entity.addPart("from_lang", new StringBody(toLat));
					entity.addPart("user_time", new StringBody(currentDateTime));
					entity.addPart("gcmid", new StringBody(gcmId));
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
			System.out.println("----------------REAL TIME KISS-------------------"+result);
			try {
				if (alertProgressDialog != null && alertProgressDialog.isShowing()) {
					alertProgressDialog.dismiss();
					alertProgressDialog = null;
				}
				String statusCode,msgResp,msgID;
				JSONObject jsonObjects = new JSONObject(result);
				statusCode = jsonObjects.getString(Constants.STATUS_CODE);
				msgResp = jsonObjects.getString(Constants.RESPONSE_MESSAGES);
				msgID = jsonObjects.getString("msg_id");
				switch(statusCode){
				case Constants.KISS_SENT_SUCCESS:
					
					editor.putString("sentmsgid", msgID);
					editor.commit();
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
	public void clearSharePref(){
		SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.REAL_MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.clear();
		editor.commit();
	}	 
	public void onDestroy() {
		super.onDestroy();
		
	}
}