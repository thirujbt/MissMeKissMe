package com.missme.kissme.MenuItems;

import java.io.File;
import java.util.ArrayList;

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
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
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
import com.missme.kissme.Utils.ImageLoader1;
import com.missme.kissme.Utils.AndroidMultiPartEntity.ProgressListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class BlindKissFifthFrag extends Fragment implements OnClickListener {

	ImageView backIcon, titleIcon;
	TextView titleName, title1, title2, titleName1, kisstype1, kisstype2;
	Button send;
	ImageView process;

	ArrayList<String> videoDetails;
	SharedPreferences pref, pref1;
	String userid, gcmid;
	TextView nameTxt, vechicleTxt, attachTxt, msgTxt;
	String fromUSerId, toUserName, toUserAuthId, vechicle, attachType,
			attachFile, msg, attachTypeName, toUserLat, toUserLang, fromLat,
			fromLong, profileImg;
	GPSTracker gps;
	String name;
	DisplayImageOptions options;
	Bitmap bitmapOrg;
	public ImageLoader1 imageLoader;
	ImageLoader imageLoader1;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.default_image)
				.showImageForEmptyUri(R.drawable.default_image)
				.showImageOnFail(R.drawable.default_image).cacheOnDisk(true)
				.considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_realtimekissfifth,
				container, false);
		pref = getActivity().getSharedPreferences(
				Constants.BLIND_MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
		pref1 = getActivity().getSharedPreferences(
				Constants.MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
		videoDetails = new ArrayList<String>();
		gps = new GPSTracker(getActivity());
		backIcon = (ImageView) view.findViewById(R.id.realtimefifth_back_icon);
		send = (Button) view.findViewById(R.id.kiss_send);
		titleName = (TextView) view.findViewById(R.id.realnewKiss_txtfifth);
		process = (ImageView) view
				.findViewById(R.id.realtime_process_iconfifth);
		process.setBackgroundResource(R.drawable.process_completed);
		process.setVisibility(View.GONE);
		titleName1 = (TextView) view.findViewById(R.id.newKiss_txtfifth);
		titleName1.setText("Blind Kiss");
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(),
				"SourceSansPro-Regular.otf");
		titleName.setTypeface(font);
		titleName1.setTypeface(font);
		title1 = (TextView) view.findViewById(R.id.realnewKiss_txt1);
		title2 = (TextView) view.findViewById(R.id.realnewKiss_txt_name);
		kisstype1 = (TextView) view.findViewById(R.id.realnewKiss_txt3);
		kisstype2 = (TextView) view.findViewById(R.id.realnewKiss_txt_vechicle);
		title1.setVisibility(View.GONE);
		title2.setVisibility(View.GONE);
		kisstype1.setVisibility(View.GONE);
		kisstype2.setVisibility(View.GONE);
		nameTxt = (TextView) view.findViewById(R.id.realnewKiss_txt_name);
		attachTxt = (TextView) view.findViewById(R.id.realnewKiss_txt_attach);
		msgTxt = (TextView) view.findViewById(R.id.realnewKiss_txt_msg);
		// new changes
		userid = pref1.getString(Constants.USER_AUTHKEY_PREF, null);
		gcmid = pref1.getString("registration_id", null);

		fromUSerId = pref1.getString(Constants.USER_AUTHKEY_PREF, null);
		toUserName = pref.getString(Constants.BLIND_TO_USER_NAME_PREF, null);
		toUserAuthId = pref.getString(Constants.BLIND_USER_AUTHID_PREF, null);
		vechicle = pref.getString(Constants.BLIND_TRAVEL_TYPE_PREF, null);
		attachType = pref.getString(Constants.BLIND_ATTACHMENT_TYPE_PREF, null);
		attachTypeName = pref.getString(
				Constants.BLIND_ATTACHMENT_TYPE_NAME_PREF, null);
		attachFile = pref.getString(Constants.BLIND_ATTACHMENT_PREF, null);
		msg = pref.getString(Constants.BLIND_MESSAGE_PREF, null);
		profileImg = pref.getString(Constants.BLIND_PROFILE_BASE_PREF, null);
		toUserLat = pref.getString(Constants.BLIND_TO_USER_LAT_PREF, null);
		toUserLang = pref.getString(Constants.BLIND_TO_USER_LANG_PREF, null);
		fromLat = String.valueOf(gps.getLatitude());
		fromLong = String.valueOf(gps.getLongitude());
		titleName.setText(toUserName);
		nameTxt.setText(toUserName);
		attachTxt.setText(attachType);
		msgTxt.setText(msg);
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
		int id = v.getId();
		switch (id) {
		case R.id.realtimefifth_back_icon:
			Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
			intent.putExtra("homepage", "blindkissfourth");
			getActivity().startActivity(intent);
			getActivity().finish();
			break;
		case R.id.kiss_send:
			sendKiss();
			break;
		}
	}

	public void sendKiss() {
		if (isInternetOn()) {
			try {
				String allDatas = attachFile + "@" + fromUSerId + "@"
						+ toUserAuthId + "@" + "blind" + "@" + msg + "@"
						+ attachTypeName + "@" + toUserLat + "@" + toUserLang
						+ "@" + fromLat + "@" + fromLong+"@"+gcmid;
				videoDetails.add(allDatas);
				for (int i = 0; i < videoDetails.size(); i++) {
					String data = videoDetails.get(i);
					Log.e("", data);
					uploadDetails(data);
				}
				videoDetails.clear();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(getActivity(), Constants.NO_INTERNET,
					Toast.LENGTH_SHORT).show();
		}
	}

	private void uploadDetails(String datas) {
		new UploadFileToServer(datas).execute();
	}

	private void showKissSentAlert() {

		final Dialog dialog = new Dialog(getActivity(),
				android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.kiss_send_alert);
		final TextView goHomeTxt = (TextView) dialog
				.findViewById(R.id.go_home_txt);
		final TextView yourKiss = (TextView) dialog.findViewById(R.id.your);
		final ImageView profImage = (ImageView) dialog
				.findViewById(R.id.realtime_final);
		final Button track = (Button) dialog.findViewById(R.id.Track_ok_btn);
		goHomeTxt.setText(Html.fromHtml(getString(R.string.go_home)));
		yourKiss.setText("Your Kiss Sent to " + toUserName);

		track.setVisibility(View.GONE);
		profImage.setMaxWidth(80);
		profImage.setMaxHeight(80);
		goHomeTxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						MainFragmentMenu.class);
				getActivity().startActivity(intent);
				getActivity().finish();
				dialog.dismiss();
				clearSherePref();
			}
		});
		ImageLoader.getInstance().displayImage(profileImg, profImage, options,
				new SimpleImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
					}

					@Override
					public void onLoadingComplete(final String imageUri,
							View view, final Bitmap loadedImage) {
						final ImageView imageView = (ImageView) view;
						ViewTreeObserver observerProfileImg = imageView
								.getViewTreeObserver();
						observerProfileImg
								.addOnPreDrawListener(new OnPreDrawListener() {

									@Override
									public boolean onPreDraw() {
										imageView.getViewTreeObserver()
												.removeOnPreDrawListener(this);
										if (imageUri != null
												&& !imageUri.equals("null")
												&& loadedImage != null
												&& !imageUri
														.equalsIgnoreCase("")) {
											Bitmap bitmap = ImageLoader1
													.getCroppedBitmap(
															loadedImage,
															imageView
																	.getWidth());
											imageView.setImageBitmap(bitmap);
										}
										return true;
									}
								});
					}
				});
		dialog.show();
	}

	// Check Internet connection
	public final boolean isInternetOn() {
		ConnectivityManager connec = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
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

		final Dialog dialog = new Dialog(getActivity(),
				android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.signup_exiting_dialog);
		final Button ok = (Button) dialog
				.findViewById(R.id.alert_dialog_ok_btn);
		final TextView alertText = (TextView) dialog
				.findViewById(R.id.alert_dialog_txt);
		final EditText editText = (EditText) dialog.findViewById(R.id.OTP_edit);
		alertText.setText(msg);
		editText.setVisibility(View.GONE);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						MainFragmentMenu.class);
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
			data = datas.split("@");
		}

		@Override
		protected void onPreExecute() {
			if (alertProgressDialog == null) {
				alertProgressDialog = new Dialog(getActivity(),
						android.R.style.Theme_Translucent);
				alertProgressDialog
						.setContentView(R.layout.progress_video_upload);
				alertProgressDialog.setCancelable(true);
				alertProgressDialog.setCanceledOnTouchOutside(true);
				progressTxt = (TextView) alertProgressDialog
						.findViewById(R.id.progress_txt);
				alertProgressDialog.show();
			}
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			progressTxt.setText(progress[0] + "%");
		}

		@Override
		protected String doInBackground(Void... param) {
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
				String toUserId = data[2];
				String kissType = data[3];
				String msg = data[4];
				String attachTypeName = data[5];
				String toUserLat = data[6];
				String toUSerLang = data[7];
				String fromLat = data[8];
				String toLat = data[9];
				String gcmid=data[10];

				final File sourceFile = new File(data[0]);

				if (attachFile == null) {
					entity.addPart("fromauthkey", new StringBody(fromUserId));
					entity.addPart("toauthkey", new StringBody(toUserId));
					entity.addPart("type", new StringBody(kissType));
					entity.addPart("message", new StringBody(msg));
					entity.addPart("attach_type",
							new StringBody(attachTypeName));
					entity.addPart("to_lat", new StringBody(toUserLat));
					entity.addPart("to_lang", new StringBody(toUSerLang));
					entity.addPart("from_lat", new StringBody(fromLat));
					entity.addPart("from_lang", new StringBody(toLat));
					entity.addPart("gcmid", new StringBody(gcmid));
					entity.addPart("authkey", new StringBody(userid));
					if(LoginActivity.getSessionname(getActivity())!=null){
						entity.addPart(Constants.SESSION_NAME, new StringBody(LoginActivity.getSessionname(getActivity())));
						}
					System.out.println("gcmid..."+gcmid);
					System.out.println("authkey..."+userid);
				} else {
					entity.addPart(Constants.ATR_VIDEO_FILE, new FileBody(
							sourceFile));
					entity.addPart("fromauthkey", new StringBody(fromUserId));
					entity.addPart("toauthkey", new StringBody(toUserId));
					entity.addPart("type", new StringBody(kissType));
					entity.addPart("message", new StringBody(msg));
					entity.addPart("attach_type",
							new StringBody(attachTypeName));
					entity.addPart("to_lat", new StringBody(toUserLat));
					entity.addPart("to_lang", new StringBody(toUSerLang));
					entity.addPart("from_lat", new StringBody(fromLat));
					entity.addPart("from_lang", new StringBody(toLat));
					entity.addPart("gcmid", new StringBody(gcmid));
					entity.addPart("authkey", new StringBody(userid));
					if(LoginActivity.getSessionname(getActivity())!=null){
						entity.addPart(Constants.SESSION_NAME, new StringBody(LoginActivity.getSessionname(getActivity())));
						}
					System.out.println("gcmid..."+gcmid);
					System.out.println("authkey..."+userid);
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

			System.out.println("sdkfhkjsdfds..." + result);
			try {
				if (alertProgressDialog != null
						&& alertProgressDialog.isShowing()) {
					alertProgressDialog.dismiss();
					alertProgressDialog = null;
				}
				String statusCode, msgResp;
				JSONObject jsonObject = new JSONObject(result);
				statusCode = jsonObject.getString(Constants.STATUS_CODE);
				msgResp = jsonObject.getString(Constants.RESPONSE_MESSAGES);

				switch (statusCode) {
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

	public void onDestroy() {
		super.onDestroy();

	}

	public void clearSherePref() {
		SharedPreferences sharedPreferences = getActivity()
				.getSharedPreferences(Constants.BLIND_MISS_ME_KISS_ME_PREF,
						Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.clear();
		editor.commit();
	}
}