package com.missme.kissme.MenuItems;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.missme.kissme.MainFragmentMenu;
import com.missme.kissme.R;
import com.missme.kissme.Utils.Constants;
import com.missme.kissme.Utils.Utils;

public class InstantKissSecondFrag extends Fragment implements OnClickListener{

	ImageView backIcon,nextIcon,voice,photo,music,videos;
	boolean videoClick=false;
	public static String uploadFileName,fileName,uploadRecordFileName;
	public static Uri uploadFileNameUri;
	public static String uploadFileName1;
	public static Uri uploadFileNameUri1;
	private static int GALLERY_RES_CODE=5;
	private static int CAMERA_RES_CODE=6;
	private static int AUDIO_RES_CODE=2;
	File camProfileImage;
	File camProfileImage1;
	Bitmap bitmapOrg;
	Context mContext;
	SharedPreferences pref;
	Editor editor;
	Uri fileUri;
	private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".amr";
	private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
	private MediaRecorder recorder = null;
	private int currentFormat = 0;
	private int output_formats[] = { MediaRecorder.OutputFormat.AMR_NB };
	private String file_exts[] = { AUDIO_RECORDER_FILE_EXT_3GP }; 
	LinearLayout llNext;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_instantkisssecond, container,false);
		pref = getActivity().getSharedPreferences(Constants.INSTANT_MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
		editor = pref.edit();
		backIcon=(ImageView)view.findViewById(R.id.instantsecond_back_icon);
		nextIcon=(ImageView)view.findViewById(R.id.instantsecond_next_icon);
		TextView titlee=(TextView)view.findViewById(R.id.newKiss_txt);
		TextView titlBar=(TextView)view.findViewById(R.id.realnewKiss_txtssx);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"SourceSansPro-Regular.otf");
		titlee.setTypeface(font);
		titlBar.setTypeface(font);		
		voice=(ImageView)view.findViewById(R.id.Insatnt_voice_icon);
		photo=(ImageView)view.findViewById(R.id.Insatnt_photos_icon);
		music=(ImageView)view.findViewById(R.id.Insatnt_music_icon);
		videos=(ImageView)view.findViewById(R.id.Insatnt_videos_icon);
		llNext=(LinearLayout)view.findViewById(R.id.llNextInstantSecond);
		llNext.setOnClickListener(this);				
		backIcon.setOnClickListener(this);
		nextIcon.setOnClickListener(this);
		voice.setOnClickListener(this);
		photo.setOnClickListener(this);
		music.setOnClickListener(this);
		videos.setOnClickListener(this);
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
		case R.id.instantsecond_back_icon:
			Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
			intent.putExtra("homepage", "instantkiss");
			getActivity().startActivity(intent);
			getActivity().finish();
			break;
		case R.id.llNextInstantSecond:
			Intent nextintent = new Intent(getActivity(), MainFragmentMenu.class);
			nextintent.putExtra("homepage", "instantkissthird");
			getActivity().startActivity(nextintent);
			getActivity().finish();
			break;
		case R.id.Insatnt_voice_icon:
			showAudoiRecorderDialog();
			break;
		case R.id.Insatnt_photos_icon:
			showProfileImageOptionDialog();
			break;
		case R.id.Insatnt_music_icon:
			showMusicfile();
			break;
		case R.id.Insatnt_videos_icon:
			videoClick=true;
			showProfileVideoOptionDialog();
			break;
		}
	}
	private void showAudoiRecorderDialog(){
		final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);		
		dialog.setContentView(R.layout.profile_image_option_alert);
		Button start = (Button) dialog.findViewById(R.id.profile_img_opt_cam_btn);
		Button stop = (Button) dialog.findViewById(R.id.profile_img_opt_gallery_btn);
		Button cancelBtn  = (Button)dialog.findViewById(R.id.profile_img_opt_cancel_btn);
		TextView title=(TextView)dialog.findViewById(R.id.profile_img_opt_title);
		final Chronometer myChronometer = (Chronometer)dialog.findViewById(R.id.chronometer);
		title.setVisibility(View.INVISIBLE);
		myChronometer.setVisibility(View.VISIBLE);
		start.setText("Start Recording");
		stop.setText("stop Recording");
		start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utils.logString("Start Recording");
				startRecording();
				myChronometer.start();
			}
		});
		stop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utils.logString("stop Recording");
				stopRecording();
				myChronometer.stop();
				dialog.dismiss();
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				stopRecording();
				myChronometer.stop();
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	//Code start image choosing

	private void showProfileImageOptionDialog(){
		final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);		
		dialog.setContentView(R.layout.profile_image_option_alert);
		Button camBtn = (Button) dialog.findViewById(R.id.profile_img_opt_cam_btn);
		Button galleryBtn = (Button) dialog.findViewById(R.id.profile_img_opt_gallery_btn);
		Button cancelBtn  = (Button)dialog.findViewById(R.id.profile_img_opt_cancel_btn);
		camBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.ENGLISH);
				Date date = new Date();
				fileName = dateFormat.format(date);
				camProfileImage = new File(android.os.Environment.getExternalStorageDirectory(), fileName+".jpg");
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(camProfileImage)); 
				startActivityForResult(intent, CAMERA_RES_CODE);
				dialog.dismiss();
			}
		});
		galleryBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_PICK);
				i.setType("image/*");
				startActivityForResult(i, GALLERY_RES_CODE);
				dialog.dismiss();
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	public void showMusicfile(){
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(Intent.createChooser(intent, "Gallery"), AUDIO_RES_CODE);
	}
	private void showProfileVideoOptionDialog(){
		final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);		
		dialog.setContentView(R.layout.profile_image_option_alert);
		Button camBtn = (Button) dialog.findViewById(R.id.profile_img_opt_cam_btn);
		Button galleryBtn = (Button) dialog.findViewById(R.id.profile_img_opt_gallery_btn);
		Button cancelBtn  = (Button)dialog.findViewById(R.id.profile_img_opt_cancel_btn);
		camBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.ENGLISH);
				Date date = new Date();
				fileName = dateFormat.format(date);
				camProfileImage = new File(android.os.Environment.getExternalStorageDirectory(), fileName+".mp4");
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(camProfileImage)); 
				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
				startActivityForResult(intent, CAMERA_RES_CODE);
				dialog.dismiss();
			}
		});
		galleryBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_PICK);
				i.setType("video/*");
				startActivityForResult(i, GALLERY_RES_CODE);
				dialog.dismiss();
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private static final int VIDEOFILESIZE = 50; 

	private static boolean isFileSizeAcceptable(long fileSize) {
		long var1 = fileSize/1024;
		long var2 = var1/1024;
		if(var2 > VIDEOFILESIZE) {
			return false;
		}
		return true;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == GALLERY_RES_CODE) {
				if(videoClick){
					if (data != null) {
						Uri uploadFileNameUri = data.getData();
						if (uploadFileNameUri != null) {
							try{
								Cursor cur = getActivity().getContentResolver().query(uploadFileNameUri,new String[]{ MediaColumns.DATA }, null, null,null);
								if(cur!=null){
									cur.moveToFirst();
									uploadFileName = cur.getString(0);
									File length=new File(uploadFileName);
									System.out.println("Checking file path gallary......"+uploadFileName);
									if(isFileSizeAcceptable(length.length())) {
										editor.putString(Constants.INSTANT_ATTACHMENT_PREF, uploadFileName);
										editor.putString(Constants.INSTANT_ATTACHMENT_TYPE_PREF, "Video"+".mp4");
										editor.putString(Constants.INSTANT_ATTACHMENT_TYPE_NAME_PREF, "video");
										editor.commit();
									} else {
										Toast.makeText(getActivity(), "File is greater than 50MB.", Toast.LENGTH_SHORT).show();
									}
								}
								else{
									Toast.makeText(getActivity(), "File is corrupted.", Toast.LENGTH_SHORT).show();
								}
							}
							catch(Exception ex){ex.printStackTrace();}
						}
					}
				}else{
					if (data != null) {
						Uri uploadFileNameUri = data.getData();
						if (uploadFileNameUri != null) {
							try{
								Cursor cur = getActivity().getContentResolver().query(uploadFileNameUri,new String[]{ MediaColumns.DATA }, null, null,null);
								if(cur!=null){
									cur.moveToFirst();
									uploadFileName = cur.getString(0);
									editor.putString(Constants.INSTANT_ATTACHMENT_PREF, uploadFileName);
									editor.putString(Constants.INSTANT_ATTACHMENT_TYPE_PREF, "Photo"+".jpg");
									editor.putString(Constants.INSTANT_ATTACHMENT_TYPE_NAME_PREF, "photo");
									editor.commit();
								}
								else{
									Toast.makeText(getActivity(), "File is corrupted.", Toast.LENGTH_SHORT).show();
								}
							}
							catch(Exception ex){
								ex.printStackTrace();
							}
						}
					}
				}
			} else if (requestCode == CAMERA_RES_CODE) {
				if(videoClick){
					if (camProfileImage != null && camProfileImage.exists()) {
						uploadFileName = camProfileImage.getPath();
						File length=new File(uploadFileName);
						System.out.println("Checking file path camera......"+uploadFileName);
						if(isFileSizeAcceptable(length.length())) {
							editor.putString(Constants.INSTANT_ATTACHMENT_PREF, uploadFileName);
							editor.putString(Constants.INSTANT_ATTACHMENT_TYPE_PREF, "Video"+".mp4");
							editor.putString(Constants.INSTANT_ATTACHMENT_TYPE_NAME_PREF, "video");
							editor.commit();
						}  else {
							Toast.makeText(getActivity(), "File is greater than 50MB.", Toast.LENGTH_SHORT).show();
						}
					}
				}else{
					if (camProfileImage != null && camProfileImage.exists()) {
						uploadFileName = camProfileImage.getPath();
						editor.putString(Constants.INSTANT_ATTACHMENT_PREF, uploadFileName);
						editor.putString(Constants.INSTANT_ATTACHMENT_TYPE_PREF, fileName+".jpg");
						editor.putString(Constants.INSTANT_ATTACHMENT_TYPE_NAME_PREF, "photo");
						editor.commit();
					}
				}

			}else if (requestCode == AUDIO_RES_CODE){
				Uri uploadFileNameUri = data.getData();
				if (uploadFileNameUri != null) {
					try{
						Cursor cur = getActivity().getContentResolver().query(uploadFileNameUri,new String[]{ MediaColumns.DATA }, null, null,null);
						if(cur!=null){
							cur.moveToFirst();
							uploadFileName = cur.getString(0);
							editor.putString(Constants.INSTANT_ATTACHMENT_PREF, uploadFileName);
							editor.putString(Constants.INSTANT_ATTACHMENT_TYPE_PREF, "Music"+".mp3");
							editor.putString(Constants.INSTANT_ATTACHMENT_TYPE_NAME_PREF, "music");
							editor.commit();
						}
						else{
							Toast.makeText(getActivity(), "File is corrupted.", Toast.LENGTH_SHORT).show();
						}
					}
					catch(Exception ex){ex.printStackTrace();}
				}
			}
		}
	}
	private String getFilename(){
		String filepath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(filepath,AUDIO_RECORDER_FOLDER);
		if(!file.exists()){
			file.mkdirs();
		}
		uploadFileName=file.getAbsolutePath() + "/" + System.currentTimeMillis() + file_exts[currentFormat];
		long name=System.currentTimeMillis();
		fileName=String.valueOf(name);
		return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + file_exts[currentFormat]);
	}
	private void startRecording(){
		try {
		recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(output_formats[currentFormat]);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setOutputFile(getFilename());
		recorder.setOnErrorListener(errorListener);
		recorder.setOnInfoListener(infoListener);
		
			recorder.prepare();
			recorder.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
		@Override
		public void onError(MediaRecorder mr, int what, int extra) {
			Utils.logString("Error: " + what + ", " + extra);
		}
	};

	private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
		@Override
		public void onInfo(MediaRecorder mr, int what, int extra) {
			Utils.logString("Warning: " + what + ", " + extra);
		}
	};
	private void stopRecording(){
		try{
			if(null != recorder){
				editor.putString(Constants.INSTANT_ATTACHMENT_PREF, uploadFileName);
				editor.putString(Constants.INSTANT_ATTACHMENT_TYPE_PREF, "Voice"+".mp3");
				editor.putString(Constants.INSTANT_ATTACHMENT_TYPE_NAME_PREF, "voice");
				editor.commit();
				recorder.stop();
				recorder.reset();
				recorder.release();
				recorder = null;
			}
		}catch(Exception e){
			
		}
		
	}
}