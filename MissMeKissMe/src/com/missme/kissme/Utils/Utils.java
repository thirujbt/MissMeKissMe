package com.missme.kissme.Utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.missme.kissme.R;

import android.provider.Settings;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Utils {
	
	
	public static final boolean validateEmailId(String email) {

		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public static boolean validateNameField(String name){
		 return name.matches("[a-zA-Z ,.'-]+");
		
	}
	public static final boolean validateGender(String gender){
		if(gender.equalsIgnoreCase("Male")||gender.equalsIgnoreCase("Female"))
			return true;
		else
			return false;
		
		
	}
	public static final boolean isInternetOn(Context context) {
		ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
				|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
			return true;
		} else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
				|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
			return false;
		}
		return    false;
	}
	
	public static void showToast(String Msg,Context context){
		Toast toast=Toast.makeText(context, Msg, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();

	
	}
	
	public static void copyStream(final InputStream is, final OutputStream os) {
		final int buffer_size = 1024;
		try {
			final byte[] bytes = new byte[buffer_size];
			for (;;) {
				final int count = is.read(bytes, 0, buffer_size);
				if (count == -1) {
					break;
				}
				os.write(bytes, 0, count);
			}
		} catch (final Exception ex) {
		}
	}

	
	
	//Show Alert When GPS not Enabled
		public static void showSettingsAlert(final Context cont) {

			final Dialog dialog = new Dialog(cont, android.R.style.Theme_Translucent);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setCancelable(false);
			dialog.setContentView(R.layout.signup_exiting_dialog);

			final Button ok = (Button)dialog.findViewById(R.id.alert_dialog_ok_btn);
			final TextView alertText = (TextView)dialog.findViewById(R.id.alert_dialog_txt);
			final EditText editText = (EditText)dialog.findViewById(R.id.OTP_edit);

			alertText.setText(Constants.ALERT_ENABLE_GPS);
			editText.setVisibility(View.GONE);
			ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					cont.startActivity(settingsIntent);
					dialog.dismiss();
				}
			});

			dialog.show();
			dialog.setCancelable(true);
		}
		private static final String APP_TAG = "AudioRecorder";

		public static int logString(String message){
		     return Log.i(APP_TAG,message);
		}
		
}
