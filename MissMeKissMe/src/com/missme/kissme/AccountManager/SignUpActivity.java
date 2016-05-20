package com.missme.kissme.AccountManager;


import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.missme.kissme.R;
import com.missme.kissme.DataBase.DBHandler;
import com.missme.kissme.GCM.Config;
import com.missme.kissme.GCM.ServerUtilities;
import com.missme.kissme.InterfaceClass.AsyncResponse;
import com.missme.kissme.MapService.GPSTracker;
import com.missme.kissme.ServiceRequest.KissMeAsyncTask;
import com.missme.kissme.Utils.Constants;
import com.missme.kissme.Utils.SessionManager;
import com.missme.kissme.Utils.Utils;

public class SignUpActivity extends Activity implements AsyncResponse {

	TextView title;
	RadioButton male,female,selectedGenderBtn;
	RadioGroup gender;
	EditText userName,password,rePassword,email,contact;
	static String mUserName;
	String mRePassword;
	static String mContact;
	static String mEmail;
	static String mPassword;
	String mGender;
	static int selectedId;
	GPSTracker gps;
	DBHandler dbHandler;
	static Context mContext;
	Context context;
	static String regid;
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_ON_SERVER_EXPIRATION_TIME =
			"onServerExpirationTimeMs";
	public static final long REGISTRATION_EXPIRY_TIME_MS = 1000 * 3600 * 24 * 7;
	GoogleCloudMessaging gcm;
	String SENDER_ID = Config.GOOGLE_SENDER_ID;
	static boolean registered = false;
	double lat;
	double longi;
	static String lati;
	static String longui;
	SharedPreferences prefs;
	SessionManager session;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		gps=new GPSTracker(SignUpActivity.this);
		session = new SessionManager(this);
		prefs= SignUpActivity.this.getSharedPreferences("MissMeKissMe", 0);
		lat=gps.getLatitude();
		longi=gps.getLongitude();
		lati=String.valueOf(lat);
		longui=String.valueOf(longi);
		title=(TextView)findViewById(R.id.title_txt_signup);
		male=(RadioButton)findViewById(R.id.male);
		female=(RadioButton)findViewById(R.id.female);
		userName=(EditText)findViewById(R.id.username_txt);
		password=(EditText)findViewById(R.id.password);
		rePassword=(EditText)findViewById(R.id.pwd_retype_txt);
		email=(EditText)findViewById(R.id.email_txt_signup);
		contact=(EditText)findViewById(R.id.contact_txt);
		gender=(RadioGroup)findViewById(R.id.gender_radiogroup);
		selectedId = gender.getCheckedRadioButtonId();
		selectedGenderBtn = (RadioButton)findViewById(selectedId);
		Typeface font = Typeface.createFromAsset(getAssets(),"Streamster.ttf");
		title.setTypeface(font);
		male.setButtonDrawable(R.drawable.checkbox_selector);
		female.setButtonDrawable(R.drawable.checkbox_selector);
	}

	public void onResume(){
		super.onResume();
		if(!isInternetOn()){
			Utils.showToast("No Internet connection",mContext); 
		}
		if(session.isLoggedIn()){
			finish();
		}
		if(!gps.canGetLocation()){
			LocationManager locationManager =(LocationManager) getSystemService(Context.LOCATION_SERVICE);
			final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			if (!gpsEnabled) {
				Utils.showSettingsAlert(SignUpActivity.this);
			}
		}
	}
	public void onClickBack(View view){
		startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
		SignUpActivity.this.finish();
	}

	public void onClickSignup(View view){
		mUserName=userName.getText().toString().trim();
		mPassword=password.getText().toString().trim();
		mRePassword=rePassword.getText().toString().trim();
		mEmail=email.getText().toString().trim();
		mContact=contact.getText().toString().trim();
		selectedId = gender.getCheckedRadioButtonId();
		if(isInternetOn()){
			if(valideFields()){
				int selectedId = gender.getCheckedRadioButtonId();
				selectedGenderBtn = (RadioButton)findViewById(selectedId);
				mGender=selectedGenderBtn.getText().toString().trim();
				String url=Constants.SIGN_UP_URL;
				regid = prefs.getString(PROPERTY_REG_ID, "");
				if(regid.length() == 0) {
					registerBackground();
				}
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.accumulate("username", mUserName);
					jsonObject.accumulate("password", mPassword);
					jsonObject.accumulate("email_id", mEmail);
					jsonObject.accumulate("phone", mContact);
					jsonObject.accumulate("gender", mGender);
					jsonObject.accumulate("latitude", lati);
					jsonObject.accumulate("langitude", longui);
					jsonObject.accumulate("gcmid", regid);
					new KissMeAsyncTask(SignUpActivity.this, url, Constants.SIGN_UP_RESP, SignUpActivity.this).execute(jsonObject);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		} else {
			Utils.showToast("No Internet Connection",mContext);
		}
	}

	@SuppressLint("NewApi")
	private  boolean valideFields(){
		if(mUserName.isEmpty()){
			Utils.showToast(Constants.INVALID_NAME, this);
			return false;
		}else if(!Utils.validateNameField(mUserName)){
			Utils.showToast(Constants.INVALID_NAME_FORMAT, this);
			return false;
		}else if(!Utils.validateEmailId(mEmail)){
			Utils.showToast(Constants.INVALID_EMAIL, this);
			return false;
		}else if(mPassword.isEmpty()){
			Utils.showToast(Constants.INVALID_PASSWORD, this);
			return false;
		}else if(mPassword.length()<6){
			Utils.showToast(Constants.INVALID_PASSWORD_LENGTH, this);
			return false;
		}else if(mRePassword.isEmpty()){
			Utils.showToast(Constants.INVALID_PASSWORD_RE_PWD, this);
			return false;
		}else if(mRePassword.length()<6){
			Utils.showToast(Constants.INVALID_PASSWORD_LENGTH_RE_PWD, this);
			return false;
		}else if(!mPassword.equalsIgnoreCase(mRePassword)){
			Utils.showToast(Constants.INVALID_PASSWORD_RE, this);
			return false;
		}else if(mContact.isEmpty()){
			Utils.showToast(Constants.INVALID_CONTACT, this);
			return false;
		}else if (selectedId == -1) {
			Utils.showToast(Constants.INVALID_GENDER_SELECTION, this);
			return false;
		}
		return true;
	}

	private void setRegistrationId(Context context, String regId) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		long expirationTime = System.currentTimeMillis() + REGISTRATION_EXPIRY_TIME_MS;
		editor.putLong(PROPERTY_ON_SERVER_EXPIRATION_TIME, expirationTime);
		editor.commit();
		String emailId = email.getText().toString().trim();
		registered = ServerUtilities.register(getApplicationContext(), emailId, regid);
	}

	private void registerBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
					}
					regid = gcm.register(SENDER_ID);
					msg = "Device registered, registration id=" + regid;
					setRegistrationId(getApplicationContext(), regid);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
				}
				return msg;
			}
			@Override
			protected void onPostExecute(String msg) {
			}
		}.execute(null, null, null);
	}

	@Override
	public void onProcessFinish(String serverResp, int RespValue) {

		if(serverResp != null){
			if(RespValue == Constants.SIGN_UP_RESP){
				JSONObject jObj = null;
				try {
					jObj = new JSONObject(serverResp);
					String msgResp = jObj.getString(Constants.RESPONSE_MESSAGES);
					if(msgResp.equalsIgnoreCase("Email already exist")){
						showKissSignupExistAlert();
					}else {
						String userNameDB =userName.getText().toString();
						String phoneNoDB =contact.getText().toString();
						String emailIdDB =  email.getText().toString();
						dbHandler = new DBHandler(SignUpActivity.this);
						dbHandler.insertProfile(userNameDB, null,phoneNoDB ,emailIdDB, null, null, null, null, null,null,null);
						showKissSignupSuccessAlert();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void showKissSignupExistAlert() {

		final Dialog dialog = new Dialog(SignUpActivity.this, android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.signup_exiting_dialog);
		final Button ok = (Button)dialog.findViewById(R.id.alert_dialog_ok_btn);
		final TextView alertText = (TextView)dialog.findViewById(R.id.alert_dialog_txt);
		alertText.setText(Constants.ALERT_TITLE_ALREADY_EXIST);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				male.setChecked(false);
				male.setClickable(true);
				female.setChecked(false);
				userName.setText(null);
				password.setText(null);
				rePassword.setText(null);
				email.setText(null);
				contact.setText(null);
				dialog.cancel();
			}
		});
		dialog.show();
	}

	private void showKissSignupSuccessAlert() {

		final Dialog dialog = new Dialog(SignUpActivity.this, android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.signup_exiting_dialog);
		final Button ok = (Button)dialog.findViewById(R.id.alert_dialog_ok_btn);
		final TextView alertText = (TextView)dialog.findViewById(R.id.alert_dialog_txt);
		alertText.setText(Constants.ALERT_CONFIRM_MESSAGE);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
				dialog.cancel();
			}
		});
		dialog.show();
	}

	//Check Internet connection
	public final boolean isInternetOn() {
		ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
				|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
			return true;
		} else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
				|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
			return false;
		}
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setCancelable(false);
			dialog.setContentView(R.layout.alert_dialog_main);
			final EditText alertEditTxt = (EditText)dialog.findViewById(R.id.alert_edit_txt);
			final TextView alertTitleTxt = (TextView)dialog.findViewById(R.id.alert_title);
			final TextView alertTxt = (TextView)dialog.findViewById(R.id.alert_txt);
			final View view=(View)dialog.findViewById(R.id.view);
			Button okBtn = (Button) dialog.findViewById(R.id.alert_ok_btn);
			Button cancelBtn = (Button) dialog.findViewById(R.id.alert_cancel_btn);
			alertEditTxt.setVisibility(View.INVISIBLE);
			alertTxt.setVisibility(View.VISIBLE);
			view.setVisibility(View.INVISIBLE);
			alertTitleTxt.setText(Constants.ALERT_TITLE);
			alertTxt.setText(Constants.ALERT_MSG_EXIT_APP);
			okBtn.setText("Yes");
			cancelBtn.setText("No");
			cancelBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			okBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					finish();
				}
			});
			dialog.show();
		}
		return super.onKeyDown(keyCode, event);
	}
}
