package com.missme.kissme.MenuItems;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
//import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.missme.kissme.MainFragmentMenu;
import com.missme.kissme.R;
import com.missme.kissme.AccountManager.LoginActivity;
import com.missme.kissme.InterfaceClass.AsyncResponse;
import com.missme.kissme.ServiceRequest.KissMeAsyncTask;
import com.missme.kissme.Utils.Constants;
import com.missme.kissme.Utils.ImageLoader1;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class VaultKissThirdFrag extends Fragment implements OnClickListener, AsyncResponse{

	ImageView backIcon,nextIcon,userImage,downloadFile,delete,replayMsg;
	//Button accept;
	TextView nameTxt,dateTxt,timeTxt,msgTxt,attchTxt,milesTravelTxt,timeTravelTxt,msgTitle,viewPath;
	SharedPreferences pref;
	String toUserName,toUserProfImage,msg,attachTypeName,date,miles,time,msgId,msgFrom,latFrom,langFrom,latTo,langTo,phoneNo,blindNo;
	DisplayImageOptions options;
	String attachName=" ";
	String attachNameFormate=" ";
	String attachFile=" ";
	String path;
	String getPath;
	Editor editor;
	File file;
	LinearLayout blindLayout;
	String downloadSuccess;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.user_default_image)
		.showImageForEmptyUri(R.drawable.user_default_image)
		.showImageOnFail(R.drawable.user_default_image)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_vaultkissthird, container,false);
		pref = getActivity().getSharedPreferences(Constants.MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
		editor = pref.edit();
		backIcon=(ImageView)view.findViewById(R.id.vault_msg_back_icon);
		downloadFile=(ImageView)view.findViewById(R.id.attachment_download_icon);
		userImage=(ImageView)view.findViewById(R.id.vault_image);
		nameTxt=(TextView)view.findViewById(R.id.vault_kiss_txtthird);
		dateTxt=(TextView)view.findViewById(R.id.date);
		timeTxt=(TextView)view.findViewById(R.id.time);
		msgTxt=(TextView)view.findViewById(R.id.vault_msg);
		attchTxt=(TextView)view.findViewById(R.id.attachment_name);
		milesTravelTxt=(TextView)view.findViewById(R.id.vault_mils);
		timeTravelTxt=(TextView)view.findViewById(R.id.vault_time);
		delete=(ImageView)view.findViewById(R.id.attachment__deleteicon);
		msgTitle=(TextView)view.findViewById(R.id.vault_msg_tilte);
		replayMsg=(ImageView)view.findViewById(R.id.attachment_reply_icon);
		viewPath=(TextView)view.findViewById(R.id.view_path);
		//blindLayout=(LinearLayout)view.findViewById(R.id.ll_blind_accpet);
		//accept=(Button)view.findViewById(R.id.blind_accept);
		TextView titlee=(TextView)view.findViewById(R.id.newKiss_txtthird);
		TextView titlBar=(TextView)view.findViewById(R.id.vault_kiss_txtthird);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"SourceSansPro-Regular.otf");
		titlee.setTypeface(font);
		titlBar.setTypeface(font);
		toUserName=pref.getString(Constants.VAULT_USER_NAME_PREF, null);
		toUserProfImage=pref.getString(Constants.VAULT_USER_PROFIMAGE_PREF, null);
		attachFile=pref.getString(Constants.VAULT_ATTACHMENT_PREF, null);
		msg=pref.getString(Constants.VAULT_MSG_PREF, null);
		attachTypeName=pref.getString(Constants.VAULT_ATTACHMENT_NAME_PREF, null);
		date=pref.getString(Constants.VAULT_DATE_PREF, null);
		miles=pref.getString(Constants.VAULT_USER_MILE_TRAVEL_PREF, null);
		time=pref.getString(Constants.VAULT_USER_TIME_TRAVEL_PREF, null);
		msgId=pref.getString(Constants.VAULT_USER_MSG_ID_PREF, null);
		msgFrom=pref.getString(Constants.VAULT_USER_MSG_FROM_PREF, null);
		attachNameFormate=pref.getString(Constants.VAULT_ATTACH_NAME_PREF, null);
		phoneNo=pref.getString(Constants.VAULT_PHONE_NO_PREF, null);
		blindNo=pref.getString(Constants.VAULT_BLIND_NO_PREF, null);
		latFrom=pref.getString(Constants.LAT_FROM_PREF, null);
		langFrom=pref.getString(Constants.LANG_FROM_PREF, null);
		latTo=pref.getString(Constants.LAT_TO_PREF, null);
		langTo=pref.getString(Constants.LANG_TO_PREF, null);

		backIcon.setOnClickListener(this);
		downloadFile.setOnClickListener(this);
		delete.setOnClickListener(this);
		replayMsg.setOnClickListener(this);
		viewPath.setOnClickListener(this);
		//accept.setOnClickListener(this);
		readMessage();
		return view;
	}
	public void readMessage(){
		String url = Constants.VAULT_DETAIL_URL;
		if(isInternetOn()){
			try {
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.accumulate("msg_id", msgId);
					jsonObject.accumulate("read_status", "1");
					new KissMeAsyncTask(this.getActivity(), url, Constants.VAULT_READ_MESSAGE_RESPONSE, VaultKissThirdFrag.this).execute(jsonObject);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(getActivity(), Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		try{
			String filepath = Environment.getExternalStorageDirectory().getPath();
			file = new File(filepath,"MissMeKissMe");
			if(!file.exists()){
				file.mkdirs();
			}
			nameTxt.setText(toUserName);
			msgTitle.setText(toUserName+"'s"+" "+"Message");
			msgTxt.setText(msg);
			String[] dateTime=date.split(" ");
			String fDate=dateTime[0];
			String fTime=dateTime[1];
			dateTxt.setText(fDate);
			String[] timechange=fTime.split(":");
			String hour=timechange[0];
			String min=timechange[1];
			int hours=Integer.parseInt(hour);
			int mins=Integer.parseInt(min);
			updateTime(hours,mins);
			if(attachTypeName.equalsIgnoreCase("Photo")){
				attachName="Photo.jpg";
				downloadSuccess="Photo";
			}else if(attachTypeName.equalsIgnoreCase("Music")){
				attachName="Music.mp3";
				downloadSuccess="Audio";
			}else if(attachTypeName.equalsIgnoreCase("Video")){
				attachName="Video.mp4";
				downloadSuccess="Video";
			}else if(attachTypeName.equalsIgnoreCase("Voice")){
				attachName="Voice.mp3";
				downloadSuccess="Voice";
			}
			attchTxt.setText(attachName);
			milesTravelTxt.setText(miles);
			timeTravelTxt.setText(time);
			path=file+"/"+attachNameFormate;
			File file = new File(path);
			if(file.length()>0){
				downloadFile.setBackgroundResource(R.drawable.play);
			}else{
				downloadFile.setBackgroundResource(R.drawable.download_icon);
			}
			loadProfilImage();
		}catch(Exception e){
		}
	}
	// Used to convert 24hr format to 12hr format with AM/PM values
	private void updateTime(int hours, int mins) {
		String timeSet = "";
		if (hours > 12) {
			hours -= 12;
			timeSet = "PM";
		} else if (hours == 0) {
			hours += 12;
			timeSet = "AM";
		} else if (hours == 12)
			timeSet = "PM";
		else
			timeSet = "AM";
		String minutes = "";
		if (mins < 10)
			minutes = "0" + mins;
		else
			minutes = String.valueOf(mins);
		String hourss = "";
		if (hours < 10)
			hourss = "0" + hours;
		else
			hourss = String.valueOf(hours);
		// Append in a StringBuilder
		String aTime = new StringBuilder().append(hourss).append('.')
				.append(minutes).append("  ").append(timeSet).toString();
		timeTxt.setText(aTime);
	}

	@Override
	public void onClick(View v) {
		int id=v.getId();
		switch (id) {
		case R.id.vault_msg_back_icon:
			Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
			intent.putExtra("homepage", "vaultkisssecond");
			getActivity().startActivity(intent);
			getActivity().finish();
			break;
		case R.id.attachment_download_icon:
			if(attachFile.equalsIgnoreCase("null") && attachTypeName.equalsIgnoreCase("null")){
				Toast.makeText(getActivity(), "No file to download", Toast.LENGTH_SHORT).show();
			}else{
				if(attachTypeName.equalsIgnoreCase("Photo")){
					path=file+"/"+attachNameFormate;
					getPath="/"+attachNameFormate;
					File file = new File(path);
					if(file.length()>0){
						//file.mkdirs();
						Intent intentPhoto = new Intent();  
						intentPhoto.setAction(android.content.Intent.ACTION_VIEW);  
						intentPhoto.setDataAndType(Uri.fromFile(file), "image/jpg");  
						getActivity().startActivity(intentPhoto);			
					}else{
						new DownloadFileFromURL().execute(attachFile);
					}

				}else if(attachTypeName.equalsIgnoreCase("Music")){
					path=file+"/"+attachNameFormate;
					getPath="/"+attachNameFormate;
					new DownloadFileFromURL().execute(attachFile);
					File file = new File(path);
					if(file.length()>0){
						Intent intentMusic = new Intent();  
						intentMusic.setAction(android.content.Intent.ACTION_VIEW);  
						intentMusic.setDataAndType(Uri.fromFile(file), "audio/mp3");  
						getActivity().startActivity(intentMusic);
					}else{
						new DownloadFileFromURL().execute(attachFile);
					}
				}else if(attachTypeName.equalsIgnoreCase("Video")){
					path=file+"/"+attachNameFormate;
					getPath="/"+attachNameFormate;
					File file = new File(path);
					if(file.length()>0){
						Intent intentVideo = new Intent();  
						intentVideo.setAction(android.content.Intent.ACTION_VIEW);  
						intentVideo.setDataAndType(Uri.fromFile(file), "video/mp4");  
						getActivity().startActivity(intentVideo);
					}else{
						new DownloadFileFromURL().execute(attachFile);
					}
				}else if(attachTypeName.equalsIgnoreCase("Voice")){
					path=file+"/"+attachNameFormate;
					getPath="/"+attachNameFormate;
					File file = new File(path);
					if(file.length()>0){
						Intent intentVoice = new Intent();  
						intentVoice.setAction(android.content.Intent.ACTION_VIEW);  
						intentVoice.setDataAndType(Uri.fromFile(file), "audio/mp3");  
						getActivity().startActivity(intentVoice);
					}else{
						new DownloadFileFromURL().execute(attachFile);
					}
				}
			}
			break;
		case R.id.attachment__deleteicon:
			Delete_file(getActivity());


			break;
		case R.id.attachment_reply_icon:
			if(!blindNo.equalsIgnoreCase("null")){
				blindContactSaveDialog(getActivity());
			}else{
				showGenderSelectAlert();
			}

			break;
		case R.id.view_path:
			Intent viewIntent = new Intent(getActivity(), MainFragmentMenu.class);
			viewIntent.putExtra("homepage", "viewpath");
			getActivity().startActivity(viewIntent);
			getActivity().finish();
			break;
		
		}
	}
	public void addContact(){
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		int rawContactInsertIndex = ops.size();
		ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
				.withValue(RawContacts.ACCOUNT_TYPE, null)
				.withValue(RawContacts.ACCOUNT_NAME, null).build());
		ops.add(ContentProviderOperation
				.newInsert(Data.CONTENT_URI)
				.withValueBackReference(Data.RAW_CONTACT_ID,rawContactInsertIndex)
				.withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
				.withValue(StructuredName.DISPLAY_NAME, toUserName) // Name of the person
				.build());
		ops.add(ContentProviderOperation
				.newInsert(Data.CONTENT_URI)
				.withValueBackReference(
						ContactsContract.Data.RAW_CONTACT_ID,rawContactInsertIndex)
						.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
						.withValue(Phone.NUMBER, phoneNo) // Number of the person
						.withValue(Phone.TYPE, Phone.TYPE_MOBILE).build()); // Type of mobile number                    
		try{
			ContentProviderResult[] res = getActivity().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
			System.out.println("checking........."+res);
		}
		catch (RemoteException e){ 
		}
		catch (OperationApplicationException e){
		}       
	}

	public void sendRegSaveBlind(){
		String url = Constants.BLIND_CONTACT_SAVE_URL;
		String userid = pref.getString(Constants.USER_AUTHKEY_PREF, null);
		if(isInternetOn()){
			try {
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.accumulate("fromauthkey",userid);
					jsonObject.accumulate("toauthkey",msgFrom);
					new KissMeAsyncTask(this.getActivity(), url, Constants.BLIND_CONTACT_SAVE_RESPONSE, VaultKissThirdFrag.this).execute(jsonObject);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(getActivity(), Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
		}
	}

	private void showGenderSelectAlert() {

		final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.vaultreply);
		final RadioButton real = (RadioButton)dialog.findViewById(R.id.reply_realtime);
		final RadioButton instant = (RadioButton)dialog.findViewById(R.id.reply_instant);
		final RadioButton delayed = (RadioButton)dialog.findViewById(R.id.reply_Delayed);
		real.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences pref = getActivity().getSharedPreferences(Constants.REAL_MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
				Editor editor = pref.edit();
				editor.putString(Constants.REAL_USER_AUTHID_PREF, msgFrom);
				editor.putString(Constants.NEW_KISS_SELECTION_PREF, "realtime");
				editor.commit();
				Intent nextintent = new Intent(getActivity(), MainFragmentMenu.class);
				nextintent.putExtra("homepage", "realtimesecond");
				getActivity().startActivity(nextintent);
				getActivity().finish();
				dialog.dismiss();
			}
		});
		instant.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences pref = getActivity().getSharedPreferences(Constants.INSTANT_MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
				Editor editor = pref.edit();
				editor.putString(Constants.INSTANT_USER_AUTHID_PREF, msgFrom);
				editor.putString(Constants.NEW_KISS_SELECTION_PREF, "instant");
				editor.commit();
				Intent nextintent = new Intent(getActivity(), MainFragmentMenu.class);
				nextintent.putExtra("homepage", "instantkisssecond");
				getActivity().startActivity(nextintent);
				getActivity().finish();
				dialog.dismiss();
			}
		});
		delayed.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences pref = getActivity().getSharedPreferences(Constants.DELAY_MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
				Editor editor = pref.edit();
				editor.putString(Constants.DELAY_USER_AUTHID_PREF, msgFrom);
				editor.putString(Constants.NEW_KISS_SELECTION_PREF, "delayed");
				editor.commit();
				Intent nextintent = new Intent(getActivity(), MainFragmentMenu.class);
				nextintent.putExtra("homepage", "delayedkisssecond");
				getActivity().startActivity(nextintent);
				getActivity().finish();

				dialog.dismiss();
			}
		});
		dialog.show();
		dialog.setCancelable(true);
	}
	public void deleteMessage(){
		String url = Constants.VAULT_DETAIL_URL;
		if(isInternetOn()){
			try {
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.accumulate("msg_id",msgId);
					jsonObject.accumulate("read_status","1");
					jsonObject.accumulate("delete_status","delete");
					if(LoginActivity.getSessionname(getActivity())!=null){
						jsonObject.accumulate(Constants.SESSION_NAME,LoginActivity.getSessionname(getActivity()));
					}
					new KissMeAsyncTask(this.getActivity(), url, Constants.VAULT_DELETE_MESSAGE_RESPONSE, VaultKissThirdFrag.this).execute(jsonObject);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(getActivity(), Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
		}
	}

	public void loadProfilImage(){

		ImageLoader.getInstance().displayImage(toUserProfImage, userImage,options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
			}
			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
			}
			@Override
			public void onLoadingComplete(final String imageUri, View view, final Bitmap loadedImage) {
				final ImageView imageView = (ImageView) view;		
				ViewTreeObserver observerProfileImg = imageView.getViewTreeObserver();
				observerProfileImg.addOnPreDrawListener(new OnPreDrawListener() {

					@Override
					public boolean onPreDraw() {
						imageView.getViewTreeObserver().removeOnPreDrawListener(this);
						if(imageUri != null && ! imageUri.equals("null") && loadedImage!=null && !imageUri.equalsIgnoreCase("")){
							Bitmap bitmap = ImageLoader1.getCroppedBitmap(loadedImage, imageView.getWidth());
							imageView.setImageBitmap(bitmap);
						}
						return true;
					}
				});
			}
		});
	}
	/**
	 * Background Async Task to download file
	 * */
	class DownloadFileFromURL extends AsyncTask<String, String, String> {
		Dialog alertProgressDialog = null;
		long totalSize = 0;
		TextView progressTxt;
		/** 
		 * Before starting background thread
		 * Show Progress Bar Dialog
		 * */
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
		/**
		 * Updating progress bar
		 * */
		protected void onProgressUpdate(String... progress) {
			// setting progress percentage
			progressTxt.setText(progress[0] + "%");
		}

		/**
		 * Downloading file in background thread
		 * */
		@Override
		protected String doInBackground(String... f_url) {
			int count;
			try {
				URL url = new URL(f_url[0]);
				URLConnection conection = url.openConnection();
				conection.connect();
				// this will be useful so that you can show a tipical 0-100% progress bar
				int lenghtOfFile = conection.getContentLength();
				// download the file
				InputStream input = new BufferedInputStream(url.openStream(), 8192);
				// Output stream
				OutputStream output = new FileOutputStream(path);
				byte data[] = new byte[1024];
				long total = 0;
				while ((count = input.read(data)) != -1) {
					total += count;
					// publishing the progress....
					// After this onProgressUpdate will be called
					publishProgress(""+(int)((total*100)/lenghtOfFile));
					// writing data to file
					output.write(data, 0, count);
				}
				// flushing output
				output.flush();
				// closing streams
				output.close();
				input.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}
		/**
		 * After completing background task
		 * Dismiss the progress dialog
		 * **/
		@Override
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after the file was downloaded
			if (alertProgressDialog != null && alertProgressDialog.isShowing()) {
				alertProgressDialog.dismiss();
				alertProgressDialog = null;
			}
			// Displaying downloaded image into image view
			// Reading image path from sdcard
			String imagePath =path;
			//File check=new File(imagePath);
			Toast.makeText(getActivity(), downloadSuccess+" downloaded successfully", Toast.LENGTH_SHORT).show();
			// setting downloaded into image view
			//     my_image.setImageDrawable(Drawable.createFromPath(imagePath));

			if(attachTypeName.equalsIgnoreCase("Photo")){
				File file = new File(imagePath);
				Intent intent = new Intent();  
				intent.setAction(android.content.Intent.ACTION_VIEW);  
				intent.setDataAndType(Uri.fromFile(file), "image/jpg");  
				getActivity().startActivity(intent);
			}else if(attachTypeName.equalsIgnoreCase("Music")){
				File file = new File(imagePath);
				Intent intent = new Intent();  
				intent.setAction(android.content.Intent.ACTION_VIEW);  
				intent.setDataAndType(Uri.fromFile(file), "audio/mp3");  
				getActivity().startActivity(intent);
			}else if(attachTypeName.equalsIgnoreCase("Video")){
				File file = new File(imagePath);
				Intent intent = new Intent();  
				intent.setAction(android.content.Intent.ACTION_VIEW);  
				intent.setDataAndType(Uri.fromFile(file), "video/mp4");  
				getActivity().startActivity(intent);
			}else if(attachTypeName.equalsIgnoreCase("Voice")){
				File file = new File(imagePath);
				Intent intent = new Intent();  
				intent.setAction(android.content.Intent.ACTION_VIEW);  
				intent.setDataAndType(Uri.fromFile(file), "audio/mp3");  
				getActivity().startActivity(intent);
			}
		}
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
	@Override
	public void onProcessFinish(String serverResp, int RespValue) {
		try {
			String statusCode = "0";
			String msgResp;
			if(serverResp!=null){
				JSONObject jObjServerResp;
				if(RespValue==Constants.VAULT_DELETE_MESSAGE_RESPONSE){
					jObjServerResp = new JSONObject(serverResp);
					statusCode = jObjServerResp.getString(Constants.STATUS_CODE);
					msgResp = jObjServerResp.getString(Constants.RESPONSE_MESSAGES);
					switch (statusCode) {
					case Constants.VAULT_MSG_DELETE_SUCCESS:
						Toast.makeText(getActivity(), msgResp, Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
						intent.putExtra("homepage", "vaultkisssecond");
						getActivity().startActivity(intent);
						getActivity().finish();
						break;
					case Constants.VAULT_MSG_DELETE_FAILED:
						Toast.makeText(getActivity(), msgResp, Toast.LENGTH_SHORT).show();
						break;
					}
				}else if(RespValue==Constants.BLIND_CONTACT_SAVE_RESPONSE){
					jObjServerResp = new JSONObject(serverResp);
					statusCode = jObjServerResp.getString(Constants.STATUS_CODE);
					msgResp = jObjServerResp.getString(Constants.RESPONSE_MESSAGES);
					switch (statusCode) {
					case Constants.BLIND_CONTACT_ADD_SUCCESS:
						Toast.makeText(getActivity(), "Contact Added successfully.Check your contact list", Toast.LENGTH_SHORT).show();
						addContact();
						break;
					case Constants.BLIND_CONTACT_ADD_FAILED:
						Toast.makeText(getActivity(), "No response from server", Toast.LENGTH_SHORT).show();
						break;
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	private void Delete_file(final Activity activity){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
		builder1.setMessage("Are you Sure, Do you want delete this message?");
		builder1.setCancelable(true);
		builder1.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				if(isInternetOn()){
					try {
						deleteMessage();
					}
					catch (NumberFormatException e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
				}
				dialog.cancel();
			}
		});
		builder1.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		AlertDialog alert11 = builder1.create();
		alert11.show();
	}
	
	private void blindContactSaveDialog(final Activity activity){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
		builder1.setMessage("Are you Sure, Do you want save this contact?");
		builder1.setCancelable(true);
		builder1.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				sendRegSaveBlind();
				dialog.cancel();
			}
		});
		builder1.setNegativeButton("No",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		AlertDialog alert11 = builder1.create();
		alert11.show();
	}
}