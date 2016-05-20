package com.missme.kissme.AccountManager;

import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.missme.kissme.MainFragmentMenu;
import com.missme.kissme.R;
import com.missme.kissme.DataBase.DBHandler;
import com.missme.kissme.GCM.Config;
import com.missme.kissme.GCM.ServerUtilities;
import com.missme.kissme.InterfaceClass.AsyncResponse;
import com.missme.kissme.ServiceRequest.KissMeAsyncTask;
import com.missme.kissme.Utils.Constants;
import com.missme.kissme.Utils.SessionManager;
import com.missme.kissme.Utils.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface; 
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class LoginActivity extends Activity implements AsyncResponse {

	TextView title;
	CheckBox remember;
	EditText emailID,pwd;
	String mEmail,mPassword;
	Context mContext;
	String statusCode,msgResp,userID;
	int width =0;
	int height =0;
	Context context;
	public static String regid;
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_ON_SERVER_EXPIRATION_TIME ="onServerExpirationTimeMs";
	public static final long REGISTRATION_EXPIRY_TIME_MS = 1000 * 3600 * 24 * 7;
	GoogleCloudMessaging gcm;
	String SENDER_ID = Config.GOOGLE_SENDER_ID;
	static boolean registered = false;
	double lat;
	DBHandler dbHandler;
	SessionManager session;
	SharedPreferences pref,pref1;
	public boolean RemebermeClicked=false;
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		width = this.getResources().getDisplayMetrics().densityDpi;
		height = this.getResources().getDisplayMetrics().heightPixels;
		setContentView(R.layout.activity_login);
		new SimpleEula(this).show();
		pref = LoginActivity.this.getSharedPreferences(Constants.MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
		prefs = getPreferences(Context.MODE_PRIVATE);
		editor = prefs.edit();
		session = new SessionManager(this);
		title=(TextView)findViewById(R.id.title_txt);
		emailID=(EditText)findViewById(R.id.email_id);
		pwd=(EditText)findViewById(R.id.password);
		remember=(CheckBox)findViewById(R.id.remeber_checkbox);
		Typeface font = Typeface.createFromAsset(getAssets(),"Streamster.ttf");
		title.setTypeface(font);
		remember.setButtonDrawable(R.drawable.checkbox_selector);	
	}

	public void onResume(){
		super.onResume();	
		if (session.isLoggedIn()) {
			finish();
		}
		LocationManager locationManager =(LocationManager) getSystemService(Context.LOCATION_SERVICE);
		final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (!gpsEnabled) {
			Utils.showSettingsAlert(LoginActivity.this);
		}
		remember.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					RemebermeClicked=true;
					editor.putBoolean("rClicked", RemebermeClicked);
					editor.commit();
				}else{
					RemebermeClicked=false;
					editor.putBoolean("rClicked", RemebermeClicked);
					editor.commit();
				}
			}
		});	
		boolean rClicked=prefs.getBoolean("rClicked", false);
		if(rClicked){
			remember.setChecked(true);
			String rEmail=prefs.getString("remail", null);
			String rpwd=prefs.getString("rpwd", null);
			emailID.setText(rEmail);
			pwd.setText(rpwd);
		}
	}	
	public void openForgetPage(View view){
		showForgetPassAlert();
	}
	public void checkLoginCredentials(View view){
		if(isInternetOn()){
			try {
				mEmail=emailID.getText().toString().trim();
				mPassword=pwd.getText().toString().trim();
				editor.putString("remail",mEmail);
				editor.putString("rpwd",mPassword);
				editor.commit();
				String url=Constants.LOGIN_URL;
				if(valideFields()){
					registerBackground();
					JSONObject jsonObject = new JSONObject();
					jsonObject.accumulate("email_id", mEmail);
					jsonObject.accumulate("password", mPassword);
					new KissMeAsyncTask(LoginActivity.this, url, Constants.LOGIN_RESPONSE, LoginActivity.this).execute(jsonObject);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(getApplicationContext(), Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
		}
	}

	@SuppressLint("NewApi")
	private  boolean valideFields(){
		if(!Utils.validateEmailId(mEmail)){
			Utils.showToast(Constants.INVALID_EMAIL, this);
			return false;
		}  
		else if(mPassword.isEmpty()){
			Utils.showToast(Constants.INVALID_PASSWORD, this);
			return false;
		}
		else if(mPassword.length()<5){
			Utils.showToast(Constants.INVALID_PASSWORD_LENGTH, this);
			return false;
		}
		return true;
	}
	public void openSignUpPage(View view){
		startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
		emailID.setText(null);
		pwd.setText(null);
		LoginActivity.this.finish();
	}
	// Forgot Password Alert																																																																																																																	                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
	private void showForgetPassAlert() {

		final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.alert_dialog_main);
		final EditText alertEditTxt = (EditText)dialog.findViewById(R.id.alert_edit_txt);
		Button okBtn = (Button) dialog.findViewById(R.id.alert_ok_btn);
		Button cancelBtn = (Button) dialog.findViewById(R.id.alert_cancel_btn);
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		okBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(isInternetOn()){
					String email = alertEditTxt.getText().toString().trim();
					if(email!=null && !email.equalsIgnoreCase("") && email.length()>0){
						if(Utils.validateEmailId(email)){
							try {JSONObject jsonObject = new JSONObject();
							String url = Constants.FORGOT_PASSWORD_URL;
							jsonObject.accumulate("email_id", email);
							new KissMeAsyncTask(LoginActivity.this, url, Constants.FORGOT_PASSWORD_RESPONSE, LoginActivity.this).execute(jsonObject);
							} catch (JSONException e) {
								e.printStackTrace();
							}
							dialog.dismiss();
						} else {
							Utils.showToast("Enter Valid E-mail Id",LoginActivity.this);
						}
					} else {
						Utils.showToast("Enter E-mail Id",LoginActivity.this);
					}
				} else {
					Toast.makeText(getApplicationContext(), Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
				}
			}
		});
		alertEditTxt.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE){
					if(isInternetOn()){
						String email = alertEditTxt.getText().toString().trim();
						if(email!=null && !email.equalsIgnoreCase("") && email.length()>0){
							if(Utils.validateEmailId(email)){
								try {
									JSONObject jsonObject = new JSONObject();
									String url = Constants.FORGOT_PASSWORD_URL;
									jsonObject.accumulate("email_id", email);
									new KissMeAsyncTask(LoginActivity.this, url, Constants.FORGOT_PASSWORD_RESPONSE, LoginActivity.this).execute(jsonObject);
								} catch (JSONException e) {
									e.printStackTrace();
								}
								dialog.dismiss();
							} else {
								Utils.showToast("Enter Valid E-mail Id",LoginActivity.this);
							}
						} else {
							Utils.showToast("Enter E-mail Id",LoginActivity.this);
						}
					} else {
						Toast.makeText(getApplicationContext(), Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
					}
				}
				return false;
			}
		});
		dialog.show();
		dialog.setCancelable(true);
	}

	@Override
	public void onProcessFinish(String serverResp, int RespValue) {

		System.out.println("djfgbksjdfhsdfkjsd..........."+serverResp);
	//{"Message":"Login successfully","deviceid":null,"session_name":"WWJ3nWC830UmK9ElPZyF1GVq6nU9GZIDWsGkpjtsjoP4xtpnsX3uEZULmOU3ODHL6o5wHyZ0WO5uivSLsVg7VaShZNlNr2yxqE47",
    //"results":{"username":"vinothsai","lastname":"","phoneno":"9875463120","emailid":"vinothsai.u@pickzy.com","gender":"Male","dob":"","prof_image_path":"","pro_cover_path":"","authid":"mQuuuUxw","blindkiss":"1","vault_code":"0"},"status_code":300}
    //{"Message":"Please verify your mobile number","authid":"YPiVbEXV","status_code":404}
   
		try {
			
			if(serverResp != null){
				if(RespValue == Constants.LOGIN_RESPONSE){
					JSONObject jObj = null;
					jObj = new JSONObject(serverResp);
					msgResp = jObj.getString(Constants.RESPONSE_MESSAGES);
					statusCode = jObj.getString(Constants.STATUS_CODE);
				
					//Inserting Session name
				
					
					/*if(getSessionname(LoginActivity.this)!=null&&!getSessionname(LoginActivity.this).equals(session_name))
					{
						logoutUser();
					}*/
					
					
					switch (statusCode) {
					case Constants.LOGIN_SUCCESS:
						String session_name=jObj.getString("session_name");
						System.out.println("session name..................."+session_name);
						setSessionname(LoginActivity.this, session_name);
						onSuccessLogin(jObj);
						break;
					case Constants.MOBILE_VERIFIY:
						userID=jObj.getString(Constants.AUTH_KEY);
						
						showMobileVerifyAlert();
						break;
					case Constants.INVALID_USER_ID:
						showInvalidEmailAlert(msgResp);
						break;
					case Constants.INVALID_PWD:
						showInvalidPWDAlert(msgResp);
						break;
						/*case Constants.LOGED_IN_OTHER_DEVICE:
						showInvalidPWDAlert(msgResp);
						break;*/
					}
				}else if(RespValue == Constants.MOBILE_VERIFY_RESPONSE){
					JSONObject jObj = null;
					jObj = new JSONObject(serverResp);
					statusCode = jObj.getString(Constants.STATUS_CODE);
					switch (statusCode) {
					case Constants.MOBILE_VERIFIY_SUCCESS:
						String session_name=jObj.getString("session_name");
						System.out.println("session name..................."+session_name);
						setSessionname(LoginActivity.this, session_name);
						onSuccessLogin(jObj);
						
						break;
					case Constants.MOBILE_VERIFIY_FAILD:
						showMobileVerifyFaildAlert();
						break;
					}
				}else if(RespValue == Constants.FORGOT_PASSWORD_RESPONSE){
					JSONObject jObj = null;
					jObj = new JSONObject(serverResp);
					statusCode = jObj.getString(Constants.STATUS_CODE);
					msgResp = jObj.getString(Constants.RESPONSE_MESSAGE);
					switch (statusCode) {
					case Constants.FORGOT_PASSWORD_SUCCESS:
						showForgetPassworAlert("Password has been sent to your mail id");
						break;
					case Constants.FORGOT_PASSWORD_FAILD:
						showForgetPassworAlert(msgResp);
						break;
					}
				}else if(RespValue == Constants.VERIFY_CODE_RESEND_RESPONSE){
					JSONObject jObj = null;
					jObj = new JSONObject(serverResp);
					statusCode = jObj.getString(Constants.STATUS_CODE);
					msgResp = jObj.getString(Constants.RESPONSE_MESSAGE);
					switch (statusCode) {
					case Constants.VRIFY_RESEND_SUCCESS:
						showForgetPassworAlert(msgResp);
						break;
					case Constants.VRIFY_RESEND_FAILD:
						showForgetPassworAlert(msgResp);
						break;
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	/*private void logoutUser(){
		if(isInternetOn()){
			String userid = pref.getString(Constants.USER_AUTHKEY_PREF, null);
			String url = Constants.LOGOUT_URL;
			if (userid != null) {
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.accumulate("authkey", userid);
					if(getSessionname(LoginActivity.this)!=null){
						
					}
					new KissMeAsyncTask(LoginActivity.this, url, Constants.LOGOUT_RESPONSE, LoginActivity.this).execute(jsonObject);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		} else {
			Toast.makeText(getApplicationContext(), Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
		}
	}*/
	public void onSuccessLogin(JSONObject mJsonObject){
		try {
			String result=mJsonObject.getString(Constants.USER_DETAILS);

			//{"username":"vinothsai","lastname":"","phoneno":"9875463120","emailid":"vinothsai.u@pickzy.com","gender":"Male"
			//,"dob":"","prof_image_path":"","pro_cover_path":"","authid":"mQuuuUxw","blindkiss":"1","vault_code":"0"}
			JSONObject jObj = null;
			jObj = new JSONObject(result);
			String userName=jObj.getString("username");
			String phoneNo=jObj.getString("phoneno");
			String emailId=jObj.getString("emailid");
			String gender=jObj.getString("gender");
			String dob=jObj.getString("dob");
			String prof_image_path=jObj.getString("prof_image_path");
			String userId=jObj.getString("authid");
			String CoverPhoto=jObj.getString("pro_cover_path");
			String optBlind=jObj.getString("blindkiss");
			String vaultCode=jObj.getString("vault_code");
			
			
			Editor editor = pref.edit();
			editor.putString(Constants.USER_AUTHKEY_PREF, userId);
			editor.commit();
			dbHandler = new DBHandler(LoginActivity.this);
			dbHandler.insertProfile(userName, null, phoneNo, emailId, gender, dob, null, vaultCode, prof_image_path,CoverPhoto,optBlind);
			
			session.createLoginSession(mEmail, mPassword, userId,RemebermeClicked);
			
			startActivity(new Intent(LoginActivity.this,MainFragmentMenu.class));
			LoginActivity.this.finish();
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static void setSessionname(Context ctx, String value) {
		SharedPreferences sp = ctx.getSharedPreferences(Constants.MY_PREFS_NAME, ctx.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(Constants.SESSION_NAME, value);
		editor.commit();
	}

	public static String getSessionname(Context ctx) {
		String prefValue;
		SharedPreferences sp = ctx.getSharedPreferences(Constants.MY_PREFS_NAME, ctx.MODE_PRIVATE);
		prefValue = sp.getString(Constants.SESSION_NAME, null);
		return prefValue;
	}
	
	public static void clearSherePref(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(Constants.MY_PREFS_NAME, ctx.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.clear();
		editor.commit();
	}
	
	private void setRegistrationId(Context context, String regId) {
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		long expirationTime = System.currentTimeMillis() + REGISTRATION_EXPIRY_TIME_MS;
		editor.putLong(PROPERTY_ON_SERVER_EXPIRATION_TIME, expirationTime);
		editor.commit();
		String emailId = emailID.getText().toString().trim();
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
					
					System.out.println("checking gcm id........................"+regid);
					
				} catch (IOException ex) {
				}
				return msg;
			}
			@Override
			protected void onPostExecute(String msg) {
			}
		}.execute(null, null, null);
	}
	private void showMobileVerifyAlert() {

		final Dialog dialog = new Dialog(LoginActivity.this, android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.signup_exiting_dialog);
		final Button ok = (Button)dialog.findViewById(R.id.alert_dialog_ok_btn);
		final TextView alertText = (TextView)dialog.findViewById(R.id.alert_dialog_txt);
		final EditText editText = (EditText)dialog.findViewById(R.id.OTP_edit);
		final TextView resend = (TextView)dialog.findViewById(R.id.resend_otp);
		alertText.setText("Enter verify code");
	
		editText.setVisibility(View.VISIBLE);
		resend.setVisibility(View.VISIBLE);
	
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isInternetOn()){
					String otp=editText.getText().toString();
					String url=Constants.MOBILE_VERIFY_URL;
					try {
						JSONObject jsonObject = new JSONObject();
						jsonObject.accumulate("authkey", userID);
						jsonObject.accumulate("verifycode", otp);
						
						new KissMeAsyncTask(LoginActivity.this, url, Constants.MOBILE_VERIFY_RESPONSE, LoginActivity.this).execute(jsonObject);	
					} catch (JSONException e) {
						e.printStackTrace();
					}
					dialog.dismiss();
				} else {
					Toast.makeText(getApplicationContext(), Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
				}
			}
		});
		resend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(isInternetOn()){
					String url=Constants.MOBILE_VERIFY_RESEND_URL;
					try {
						JSONObject jsonObject = new JSONObject();
						jsonObject.accumulate("authkey", userID);
						new KissMeAsyncTask(LoginActivity.this, url, Constants.VERIFY_CODE_RESEND_RESPONSE, LoginActivity.this).execute(jsonObject);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					dialog.dismiss();
				} else {
					Toast.makeText(getApplicationContext(), Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
				}
			}
		});
		dialog.show();
		dialog.setCancelable(true);
	}
	private void showMobileVerifyFaildAlert() {

		final Dialog dialog = new Dialog(LoginActivity.this, android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.signup_exiting_dialog);
		final Button ok = (Button)dialog.findViewById(R.id.alert_dialog_ok_btn);
		final TextView alertText = (TextView)dialog.findViewById(R.id.alert_dialog_txt);
		final EditText editText = (EditText)dialog.findViewById(R.id.OTP_edit);
		final TextView resend = (TextView)dialog.findViewById(R.id.resend_otp);
		alertText.setText("Entered code is wrong...!");
		editText.setVisibility(View.VISIBLE);
		resend.setVisibility(View.VISIBLE);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(isInternetOn()){
					String otp=editText.getText().toString();
					String url=Constants.MOBILE_VERIFY_URL;
					try {
						JSONObject jsonObject = new JSONObject();
						jsonObject.accumulate("authkey", userID);
						jsonObject.accumulate("verifycode", otp);
						new KissMeAsyncTask(LoginActivity.this, url, Constants.MOBILE_VERIFY_RESPONSE, LoginActivity.this).execute(jsonObject);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					dialog.dismiss();
				} else {
					Toast.makeText(getApplicationContext(), Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
				}
			}
		});
		resend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(isInternetOn()){
					String url=Constants.MOBILE_VERIFY_RESEND_URL;
					try {
						JSONObject jsonObject = new JSONObject();
						jsonObject.accumulate("authkey", userID);
						new KissMeAsyncTask(LoginActivity.this, url, Constants.VERIFY_CODE_RESEND_RESPONSE, LoginActivity.this).execute(jsonObject);

					} catch (JSONException e) {
						e.printStackTrace();
					}
					dialog.dismiss();
				} else {
					Toast.makeText(getApplicationContext(), Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
				}
			}
		});
		dialog.show();
		dialog.setCancelable(true);
	}
	private void showInvalidEmailAlert(String msg) {

		final Dialog dialog = new Dialog(LoginActivity.this, android.R.style.Theme_Translucent);
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
				emailID.setText(null);
				pwd.setText(null);
				dialog.dismiss();
			}
		});
		dialog.show();
		dialog.setCancelable(true);
	}
	private void showInvalidPWDAlert(String msg) {

		final Dialog dialog = new Dialog(LoginActivity.this, android.R.style.Theme_Translucent);
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
				emailID.setText(null);
				pwd.setText(null);
				dialog.dismiss();
			}
		});
		dialog.show();
		dialog.setCancelable(true);
	}
	private void showForgetPassworAlert(String msg) {

		final Dialog dialog = new Dialog(LoginActivity.this, android.R.style.Theme_Translucent);
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
				emailID.setText(null);
				pwd.setText(null);
				dialog.dismiss();
			}
		});
		dialog.show();
		dialog.setCancelable(true);
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
