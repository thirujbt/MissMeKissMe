package com.missme.kissme.MenuItems;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.missme.kissme.MainFragmentMenu;
import com.missme.kissme.R;
import com.missme.kissme.AccountManager.LoginActivity;
import com.missme.kissme.CustomizedView.MySwitch;
import com.missme.kissme.DataBase.DBHandler;
import com.missme.kissme.InterfaceClass.AsyncResponse;
import com.missme.kissme.ServiceRequest.KissMeAsyncTask;
import com.missme.kissme.Utils.Base64;
import com.missme.kissme.Utils.Constants;
import com.missme.kissme.Utils.ImageLoader1;
//import com.missme.kissme.Utils.RoundedImageView;
import com.missme.kissme.Utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ProfileEditPage extends Fragment implements OnClickListener, AsyncResponse{

	Context mContext;
	ImageView backIcon,editIcon,profileImage;
	MySwitch optBlind;
	EditText name,phoNo,email,vaultCode;
	TextView dob,gender,changePwd;
	Button save,clearAll,profileImageUpload;
	String mUserName,mContact,mEmail,mGender,mDOB,mVaultCode,mOptBlind;
	LinearLayout coverPhotoUpload;
	public static final int REQUEST_CODE_CROP_IMAGE   = 0x3;
	DisplayImageOptions options;
	Bitmap bitmapOrg;
	public ImageLoader1 imageLoader;
	public static String uploadFileName;
	public static Uri uploadFileNameUri;
	public static String uploadFileName1;
	public static Uri uploadFileNameUri1;
	private static int GALLERY_RES_CODE=1;
	private static int CAMERA_RES_CODE=2;
	File camProfileImage;
	File camProfileImage1;
	DBHandler dbHandler;
	Calendar calendarDatePicker;
	String photoPath,dbphotoPath,dbphotoCoverPath;
	Bitmap bitmap;
	SharedPreferences pref;
	LinearLayout profileCover;
	String oldPwd,newPwd,rePwd;
	// variables to store the selected date and time
	private int mSelectedYear;
	private int mSelectedMonth;
	private int mSelectedDay;
	// CallBacks for date and time pickers
	private OnDateSetListener mOnDateSetListener = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			// update the current variables ( year, month and day)
			mSelectedDay = dayOfMonth;
			mSelectedMonth = monthOfYear;
			mSelectedYear = year;
			if (view.isShown()) {
				Calendar calendarTemp = Calendar.getInstance();
				calendarTemp.set(year, monthOfYear, dayOfMonth);
				Date todaysDate = new Date(System.currentTimeMillis());
				if (calendarTemp.getTime().compareTo(todaysDate) <= 0) {
					updateDateUI();
				} else {
					Toast.makeText(getActivity(), "Date should not be greater than today's date.", Toast.LENGTH_SHORT).show();
				}
			}

		}
	};
	String byteStr = null;
	String coverByteStr = null;
	boolean coverClick=false;
	Editor editor;
	String userid;
	ImageLoader imageLoader1;
	String deviceLogStatus;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.default_imagess)
		.showImageForEmptyUri(R.drawable.default_imagess)
		.showImageOnFail(R.drawable.default_imagess)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		super.onCreate(savedInstanceState);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_profileeditpage, container,false);
		pref = getActivity().getSharedPreferences(Constants.MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
		deviceLogStatus=pref.getString(Constants.DEVICE_LOG_PREF, null);
		editor = pref.edit();
		dbHandler = new DBHandler(getActivity());
		userid = pref.getString(Constants.USER_AUTHKEY_PREF, null);
		backIcon=(ImageView)view.findViewById(R.id.profileEditPage_back_icon);
		optBlind=(MySwitch)view.findViewById(R.id.profile_edit_opt_blind_img);
		name=(EditText)view.findViewById(R.id.profileEdit_txt_name);
		phoNo=(EditText)view.findViewById(R.id.profileEdit_txt_phoneno);
		email=(EditText)view.findViewById(R.id.profileEdit_txt_emailid);
		dob=(TextView)view.findViewById(R.id.profileEdit_txt_dob);
		gender=(TextView)view.findViewById(R.id.profileEdit_txt_gender);
		vaultCode=(EditText)view.findViewById(R.id.profileEdit_txt_vaultcode);
		save=(Button)view.findViewById(R.id.btn_save);
		clearAll=(Button)view.findViewById(R.id.btn_clear_all);
		profileImageUpload=(Button)view.findViewById(R.id.profileEdit_uploaded_image);
		coverPhotoUpload=(LinearLayout)view.findViewById(R.id.profileEditPage_header1);
		profileImage=(ImageView)view.findViewById(R.id.profileEdit_image);
		changePwd=(TextView)view.findViewById(R.id.txtPWDChange);
		TextView titlee=(TextView)view.findViewById(R.id.profile_title_txt);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"SourceSansPro-Regular.otf");
		titlee.setTypeface(font);	
		backIcon.setVisibility(View.GONE);
		clearAll.setVisibility(View.GONE);
		backIcon.setOnClickListener(this);
		save.setOnClickListener(this);
		clearAll.setOnClickListener(this);
		dob.setOnClickListener(this);
		gender.setOnClickListener(this);
		profileImageUpload.setOnClickListener(this);
		profileImage.setOnClickListener(this);
		coverPhotoUpload.setOnClickListener(this);
		changePwd.setOnClickListener(this);
		imageLoader = new ImageLoader1(getActivity());
		imageLoader1 = ImageLoader.getInstance();
		imageLoader1.init(ImageLoaderConfiguration.createDefault(getActivity()));

		optBlind.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					//optBlind.setChecked(true);
					mOptBlind="1";
				}else{
					mOptBlind="0";
				}
			}
		});
		// initialize the current date
		Calendar calendar = Calendar.getInstance();
		this.mSelectedYear = calendar.get(Calendar.YEAR);
		this.mSelectedMonth = calendar.get(Calendar.MONTH);
		this.mSelectedDay = calendar.get(Calendar.DAY_OF_MONTH);
		try{
			getDataFromDB();

			if(mOptBlind.equalsIgnoreCase("1")){
				optBlind.setChecked(true);
				mOptBlind="1";
			}else{
				optBlind.setChecked(false);
				mOptBlind="0";
			}
		}catch(Exception e){
		}
		loadProfilImage();
		if(dbphotoCoverPath.isEmpty()){
			coverPhotoUpload.setBackgroundResource(R.drawable.profile_bg);
		}else{
			loadProfilCoverImage();
		}
		return view;
	}
	@Override
	public void onResume() {
		super.onResume();
	
	}
	private void getDataFromDB(){
		//Get from DB
		try{
			Cursor cursor=dbHandler.getProfile();
			if (cursor.moveToFirst()) {
				do {
					name.setText(cursor.getString(cursor.getColumnIndex(DBHandler.PROFILE_COLUMN_FIRST_NAME)));
					phoNo.setText(cursor.getString(cursor.getColumnIndex(DBHandler.PROFILE_COLUMN_PHONE_NO)));
					email.setText(cursor.getString(cursor.getColumnIndex(DBHandler.PROFILE_COLUMN_EMAIL_ID)));
					gender.setText(cursor.getString(cursor.getColumnIndex(DBHandler.PROFILE_COLUMN_GENDER))); 
					String date=cursor.getString(cursor.getColumnIndex(DBHandler.PROFILE_COLUMN_DOB));
					vaultCode.setText(cursor.getString(cursor.getColumnIndex(DBHandler.PROFILE_COLUMN_VAULTCODE)));
					dbphotoPath = cursor.getString(cursor.getColumnIndex(DBHandler.PROFILE_COLUMN_PHOTO));
					dbphotoCoverPath = cursor.getString(cursor.getColumnIndex(DBHandler.PROFILE_COLUMN_COVER_PHOTO));
					mOptBlind=cursor.getString(cursor.getColumnIndex(DBHandler.OPT_BLIND));				
					if(date.isEmpty()){
						dob.setText("DD.MM.YYYY");
					}else{
						dob.setText(date);
					}
					if(!cursor.isClosed()){
						cursor.close();
					}
				} while (cursor.moveToNext());
			}
			dbHandler.close();
		}catch(Exception e){
			e.printStackTrace();
		}		
	}

	public void loadProfilImage(){

		ImageLoader.getInstance().displayImage(dbphotoPath, profileImage,options, new SimpleImageLoadingListener() {
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
							Bitmap bitmap = ImageLoader1.getRoundCroppedBitmapimg(loadedImage, imageView.getWidth());
							imageView.setImageBitmap(bitmap);
						}
						return true;
					}
				});
			}
		});
	}
	public void loadProfilCoverImage(){

		imageLoader1.loadImage(dbphotoCoverPath, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				super.onLoadingComplete(imageUri, view, loadedImage);
				coverPhotoUpload.setBackgroundDrawable(new BitmapDrawable(loadedImage));
			}
		});

	}
	private void updateDateUI() {
		String month = ((mSelectedMonth+1) > 9) ? ""+(mSelectedMonth+1): "0"+(mSelectedMonth+1) ;
		String day = ((mSelectedDay) < 10) ? "0"+mSelectedDay: ""+mSelectedDay ;

		dob.setText(day+"."+month+"."+mSelectedYear);
	}
	// initialize the DatePickerDialog
	private DatePickerDialog showDatePickerDialog(int initialYear, int initialMonth, int initialDay, OnDateSetListener listener) {
		DatePickerDialog dialog = new DatePickerDialog(getActivity(), listener, initialYear, initialMonth, initialDay);
		dialog.show();
		return dialog;
	}
	@Override
	public void onClick(View v) {
		int id=v.getId();
		switch (id) {
		case R.id.profileEditPage_back_icon:
			Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
			getActivity().startActivity(intent);
			getActivity().finish();
			break;
		case R.id.btn_save:
			saveAllDetails();
			break;
		case R.id.btn_clear_all:
			name.setText(" ");
			phoNo.setText(" ");
			email.setText(" ");
			dob.setText(" ");
			gender.setText(" ");
			vaultCode.setText(" ");
			break;
		case R.id.profileEdit_txt_dob:
			showDatePickerDialog(mSelectedYear, mSelectedMonth, mSelectedDay, mOnDateSetListener);
			break;
		case R.id.profileEdit_txt_gender:
			showGenderSelectAlert();
			break;
		case R.id.profileEdit_uploaded_image:
			showProfileImageOptionDialog();
			break;
		case R.id.profileEdit_image:
			showProfileImageOptionDialog();
			coverClick=false;
			break;
		case R.id.profileEditPage_header1:
			showProfileCoverImageOptionDialog();
			coverClick=true;
			break;
		case R.id.txtPWDChange:
			showPasswordChangeAlert();
			break;
		}
	}
	public void saveAllDetails(){
		mUserName=name.getText().toString().trim();
		mContact=phoNo.getText().toString().trim();
		mEmail=email.getText().toString().trim();
		mGender=gender.getText().toString().trim();
		mDOB=dob.getText().toString().trim();
		mVaultCode=vaultCode.getText().toString().trim();
		if(valideFields()){
			try {
				JSONObject jsonObject = new JSONObject();
				String url = Constants.PROFILE_EDIT_URL;
				jsonObject.accumulate("authkey", userid);
				jsonObject.accumulate("username", mUserName);
				jsonObject.accumulate("email_id", mEmail);
				jsonObject.accumulate("phone", mContact);
				jsonObject.accumulate("gender", mGender);
				jsonObject.accumulate("dob", mDOB);
				jsonObject.accumulate("vault_code", mVaultCode);
				jsonObject.accumulate("blindkiss", mOptBlind);
				if(LoginActivity.getSessionname(getActivity())!=null){
					jsonObject.accumulate(Constants.SESSION_NAME,LoginActivity.getSessionname(getActivity()));
					}
				new KissMeAsyncTask(this.getActivity(), url, Constants.PROFILEEDITPAGE_RESPONSE, ProfileEditPage.this).execute(jsonObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	private  boolean valideFields(){
		if(mUserName.isEmpty()){
			Utils.showToast(Constants.INVALID_NAME, getActivity());
			return false;
		}else if(mContact.isEmpty()){
			Utils.showToast(Constants.INVALID_CONTACT, getActivity());
			return false;
		}else if(!Utils.validateEmailId(mEmail)){
			Utils.showToast(Constants.INVALID_EMAIL, getActivity());
			return false;
		}else if(mGender.isEmpty()){
			Utils.showToast(Constants.INVALID_GENDER_SELECTION, getActivity());
			return false;
		}else if (mVaultCode.isEmpty()) {
			Utils.showToast(Constants.INVALID_VAULT_CODE, getActivity());
			return false;
		}else if (mVaultCode.length()<4) {
			Utils.showToast(Constants.INVALID_VAULT_LENGTH_CODE, getActivity());
			return false;
		}
		return true;
	}

	private void showGenderSelectAlert() {

		final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.gender_select_dialog);
		final RadioButton male = (RadioButton)dialog.findViewById(R.id.gender_male);
		final RadioButton female = (RadioButton)dialog.findViewById(R.id.gender_female);
		male.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				gender.setText("Male");
				dialog.dismiss();
			}
		});
		female.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				gender.setText("Female");
				dialog.dismiss();
			}
		});
		dialog.show();
		dialog.setCancelable(true);
	}
	/**
	 * Show dialog box for profile image option
	 */
	private void showProfileImageOptionDialog(){
		final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);		
		dialog.setContentView(R.layout.profile_image_option_alert);
		Button camBtn = (Button) dialog.findViewById(R.id.profile_img_opt_cam_btn);
		Button galleryBtn = (Button) dialog.findViewById(R.id.profile_img_opt_gallery_btn);
		Button removeBtn = (Button) dialog.findViewById(R.id.profile_img_opt_remove_btn);
		Button cancelBtn  = (Button)dialog.findViewById(R.id.profile_img_opt_cancel_btn);
		removeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();	
			}
		});
		camBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.ENGLISH);
				Date date = new Date();
				String fileName = dateFormat.format(date);
				System.out.println(dateFormat.format(date)); 
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

	private void showProfileCoverImageOptionDialog(){
		final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);		
		dialog.setContentView(R.layout.profile_image_option_alert);
		Button camBtn = (Button) dialog.findViewById(R.id.profile_img_opt_cam_btn);
		Button galleryBtn = (Button) dialog.findViewById(R.id.profile_img_opt_gallery_btn);
		Button removeBtn = (Button) dialog.findViewById(R.id.profile_img_opt_remove_btn);
		Button cancelBtn  = (Button)dialog.findViewById(R.id.profile_img_opt_cancel_btn);
		removeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		camBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.ENGLISH);
				Date date = new Date();
				String fileName = dateFormat.format(date);
				String camFileName="cover"+fileName;
				System.out.println(dateFormat.format(date)); 
				camProfileImage1 = new File(android.os.Environment
						.getExternalStorageDirectory(), camFileName+".jpg");
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(camProfileImage1)); 
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

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == GALLERY_RES_CODE) {
				if (data != null) {
					if(coverClick){
						Uri uploadFileNameUri = data.getData();
						if (uploadFileNameUri != null) {
							try{
								Cursor cur = getActivity().getContentResolver().query(uploadFileNameUri,new String[]{ MediaColumns.DATA }, null, null,null);
								if(cur!=null){
									cur.moveToFirst();
									uploadFileName1 = cur.getString(0);
								}
								else{
									Toast.makeText(getActivity(), "File is corrupted.", Toast.LENGTH_SHORT).show();	
								}
								if(uploadFileName1!=null && ! uploadFileName1.isEmpty()&& !uploadFileName1.equalsIgnoreCase("null")){
									camProfileImage1 = new File(uploadFileName1);
									if(camProfileImage1!=null && camProfileImage1.exists()){
										String uri = Uri.fromFile(camProfileImage1).toString();	
										//decode for file name space problem
										String decoded = Uri.decode(uri);
										// Upload image to server
										ExifInterface ei;
										float angleValue = 0 ;
										try {
											ei = new ExifInterface(uploadFileName1);
											int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
													ExifInterface.ORIENTATION_NORMAL);
											switch (orientation) {
											case ExifInterface.ORIENTATION_ROTATE_90:
												angleValue = 90;
												break;
											case ExifInterface.ORIENTATION_ROTATE_180:
												angleValue =  180;
												break;
											case ExifInterface.ORIENTATION_ROTATE_270:
												angleValue = 270;
												break;
											}
										} catch (IOException e) {
											e.printStackTrace();
										}
										loadCoverImage(decoded , angleValue);
									}
								}
								else{
									Toast.makeText(getActivity(), "File is corrupted.", Toast.LENGTH_SHORT).show();	
								}
							}
							catch(Exception ex){ex.printStackTrace();}
						}
					}else{
						Uri uploadFileNameUri = data.getData();
						if (uploadFileNameUri != null) {
							try{
								Cursor cur = getActivity().getContentResolver().query(uploadFileNameUri,new String[]{ MediaColumns.DATA }, null, null,null);
								if(cur!=null){
									cur.moveToFirst();
									uploadFileName = cur.getString(0);
								}
								else{
									Toast.makeText(getActivity(), "File is corrupted.", Toast.LENGTH_SHORT).show();
								}
								if(uploadFileName!=null && ! uploadFileName.isEmpty()&& !uploadFileName.equalsIgnoreCase("null")){
									camProfileImage = new File(uploadFileName);
									if(camProfileImage!=null && camProfileImage.exists()){
										String uri = Uri.fromFile(camProfileImage).toString();	
										//decode for file name space problem
										String decoded = Uri.decode(uri);
										// Upload image to server
										ExifInterface ei;
										float angleValue = 0 ;
										try {
											ei = new ExifInterface(uploadFileName);
											int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
													ExifInterface.ORIENTATION_NORMAL);
											switch (orientation) {
											case ExifInterface.ORIENTATION_ROTATE_90:
												angleValue = 90;
												break;
											case ExifInterface.ORIENTATION_ROTATE_180:
												angleValue =  180;
												break;
											case ExifInterface.ORIENTATION_ROTATE_270:
												angleValue = 270;
												break;
											}
										} catch (IOException e) {
											e.printStackTrace();
										}
										loadImage(decoded,angleValue);
									}
								}
								else{
									Toast.makeText(getActivity(), "File is corrupted.", Toast.LENGTH_SHORT).show();
								}
							}
							catch(Exception ex){ex.printStackTrace();}
						}
					}
				}
			} else if (requestCode == CAMERA_RES_CODE) {
				if(coverClick){
					if (camProfileImage1 != null && camProfileImage1.exists()) {
						uploadFileName1 = camProfileImage1.getPath();
						String uri = Uri.fromFile(camProfileImage1).toString();
						//decode for file name space problem
						String decoded = Uri.decode(uri);
						ExifInterface ei;
						float angleValue = 0 ;
						try {
							ei = new ExifInterface(uploadFileName1);
							int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
									ExifInterface.ORIENTATION_NORMAL);
							switch (orientation) {
							case ExifInterface.ORIENTATION_ROTATE_90:
								angleValue = 90;
								break;
							case ExifInterface.ORIENTATION_ROTATE_180:
								angleValue =  180;
								break;
							case ExifInterface.ORIENTATION_ROTATE_270:
								angleValue = 270;
								break;
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						catch (Exception e) {
							e.printStackTrace();
						}
						loadCoverImage(decoded , angleValue);
					}
				}else{
					if (camProfileImage != null && camProfileImage.exists()) {
						uploadFileName = camProfileImage.getPath();
						String uri = Uri.fromFile(camProfileImage).toString();
						//decode for file name space problem
						String decoded = Uri.decode(uri);
						ExifInterface ei;
						float angleValue = 0 ;
						try {
							ei = new ExifInterface(uploadFileName);
							int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
									ExifInterface.ORIENTATION_NORMAL);
							switch (orientation) {
							case ExifInterface.ORIENTATION_ROTATE_90:
								angleValue = 90;
								break;
							case ExifInterface.ORIENTATION_ROTATE_180:
								angleValue =  180;
								break;
							case ExifInterface.ORIENTATION_ROTATE_270:
								angleValue = 270;
								break;
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						catch (Exception e) {
							e.printStackTrace();
						}
						loadImage(decoded,angleValue);
					}
				}
			}
		}
	}
	public Bitmap callPhotoUploadMethod(String imagePath1 , float angle) {
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imagePath1, o);
		// The new size we want to scale to
		final int REQUIRED_SIZE = 1024;
		// Find the correct scale value. It should be the power of 2.
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
				break;
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}
		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		bitmapOrg = BitmapFactory.decodeFile(imagePath1, o2);
		if(angle != 0){
			bitmapOrg = rotateImage(bitmapOrg, angle);
		}
		return bitmapOrg;
	}		
	private Bitmap rotateImage(Bitmap source, float angle) {
		Bitmap bitmap = null;
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		try {
			bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
					matrix, true);
		} catch (OutOfMemoryError err) {
			err.printStackTrace();
		}
		return bitmap;
	}

	private void loadImage(String uri , float angle) {
		if (uploadFileName != null && !uploadFileName.equals("")) {
			try {
				Bitmap bm = callPhotoUploadMethod(uploadFileName,angle);
				//	profileImage.setImageBitmap(bm);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bm.compress(CompressFormat.JPEG, 75, bos);
				byte[] byteData = bos.toByteArray();
				byteStr = Base64.encodeBytes(byteData);
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.accumulate("authkey", userid);
					jsonObject.accumulate("pro_img_key", "profileImage");
					jsonObject.accumulate("prof_image_path", byteStr);
					System.out.println(jsonObject);
					new ImgUploadAsync().execute(jsonObject);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (OutOfMemoryError e) {
				Toast.makeText(getActivity(), "Image size is too large", Toast.LENGTH_SHORT).show();
			}
		}

	}

	private void loadCoverImage(String uri , float angle) {
		if (uploadFileName1 != null && !uploadFileName1.equals("")) {
			try {
				Bitmap bm = callPhotoUploadMethod(uploadFileName1,angle);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bm.compress(CompressFormat.JPEG, 75, bos);
				byte[] byteData = bos.toByteArray();
				coverByteStr = Base64.encodeBytes(byteData);
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.accumulate("authkey", userid);
					jsonObject.accumulate("pro_cvr_key", "coverImage");
					jsonObject.accumulate("pro_cover_path", coverByteStr);
					new ImgUploadAsync().execute(jsonObject);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (OutOfMemoryError e) {
				Toast.makeText(getActivity(), "Image size is too large", Toast.LENGTH_SHORT).show();
			}
		}
	}
	@Override
	public void onProcessFinish(String serverResp, int RespValue) {
		try {
			String statusCode = "0";
			String msgResp;
			if(serverResp!=null){
				JSONObject jObjServerResp;
				if(RespValue==Constants.PROFILEEDITPAGE_RESPONSE){
					jObjServerResp = new JSONObject(serverResp);
					statusCode = jObjServerResp.getString(Constants.STATUS_CODE);
					msgResp = jObjServerResp.getString(Constants.RESPONSE_MESSAGE);
					switch (statusCode) {
					case Constants.PROFILE_EDIT_SUCCESS:
						mUserName=name.getText().toString().trim();
						mContact=phoNo.getText().toString().trim();
						mEmail=email.getText().toString().trim();
						mGender=gender.getText().toString().trim();
						mDOB=dob.getText().toString().trim();
						mVaultCode=vaultCode.getText().toString().trim();
						dbHandler.updateProfileEdit(mUserName, mContact, mEmail, mGender, mDOB, mVaultCode,mOptBlind);
						showSuccessAlert("Updated Successfully");
					//	MainFragmentMenu.homePageService=false;
						break;
					case Constants.PROFILE_EDIT_FAILD:
						showSuccessAlert(msgResp);
						break;
					}
				}else if(RespValue==Constants.PASSWORD_CHANGE_RESPONSE){
					jObjServerResp = new JSONObject(serverResp);
					statusCode = jObjServerResp.getString(Constants.STATUS_CODE);
					if(statusCode.equalsIgnoreCase("308")){
					//	MainFragmentMenu.homePageService=false;
						showSuccessAlert("Password changed successfully");
						editor.putString("password", newPwd);
						editor.commit();
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	private void showSuccessAlert(String msg) {

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
				Intent intentNext = new Intent(getActivity(), MainFragmentMenu.class);
				//intentNext.putExtra("homepage", "editprofilepage");
				getActivity().startActivity(intentNext);
				getActivity().finish();
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	class ImgUploadAsync extends AsyncTask<JSONObject, Void, String>{
		Dialog alertProgressDialog = null;
		@Override
		protected void onPreExecute() {
			if (alertProgressDialog == null){
				alertProgressDialog = new Dialog(getActivity(), android.R.style.Theme_Translucent);
			}
			alertProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertProgressDialog.setContentView(R.layout.custom_progressbar);
			alertProgressDialog.setCancelable(true);
			alertProgressDialog.setCanceledOnTouchOutside(true);
			alertProgressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(JSONObject... jsonObj) {
			InputStream inputStream = null;
			String result = "";
			String json = "";
			try{
				String imgUploadURL = Constants.PROFILE_IMAGE_URL;
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(imgUploadURL);
				json = jsonObj[0].toString();
				StringEntity se = new StringEntity(json);
				httpPost.setEntity(se);
				HttpResponse httpResponse = httpclient.execute(httpPost);
				inputStream = httpResponse.getEntity().getContent();
				if(inputStream != null)
					result = convertInputStreamToString(inputStream);
				else
					result = "Did not work!";
			}
			catch(Exception e){
			}
			return result;
		}
		@SuppressWarnings("unused")
		protected void onPostExecute(String serverResp) {
			super.onPostExecute(serverResp);
			try{
				if(serverResp != null){
					JSONObject jObj = new JSONObject(serverResp);
					String statusMsg = jObj.getString(Constants.STATUS);
					String statusCodeImg = jObj.getString(Constants.STATUS_CODE);
					//System.out.println(statusCodeImg);
					switch (statusCodeImg) {
					case Constants.PROFILE_IMAGE_SUCCESS:
						dbphotoPath=jObj.getString(Constants.USER_DETAILS);
						dbHandler.updateProfileImage(dbphotoPath);
						loadProfilImage();
						if (alertProgressDialog != null && alertProgressDialog.isShowing()) {
							alertProgressDialog.dismiss();
							alertProgressDialog = null;
						}
						Toast.makeText(getActivity(), "Profile Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
						break;

					case Constants.PROFILE_COVER_IMAGE_SUCCESS:
						dbphotoCoverPath=jObj.getString(Constants.USER_DETAILS);
						loadProfilCoverImage();
						dbHandler.updateProfileCoverImage(dbphotoCoverPath);
						if (alertProgressDialog != null && alertProgressDialog.isShowing()) {
							alertProgressDialog.dismiss();
							alertProgressDialog = null;
						}
						Toast.makeText(getActivity(), "Cover Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
						coverClick=false;
						break;
					default:
						if (alertProgressDialog != null && alertProgressDialog.isShowing()) {
							alertProgressDialog.dismiss();
							alertProgressDialog = null;
						}
						Toast.makeText(getActivity(), "Image Uploaded failed", Toast.LENGTH_SHORT).show();
						break;
					}
				} else {
					Toast.makeText(getActivity(), "No Response From Server", Toast.LENGTH_SHORT).show();
				}
			}
			catch(Exception e){
			}
		}
	}
	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while((line = bufferedReader.readLine()) != null)
			result += line;
		inputStream.close();
		return result;
	}

	private void showPasswordChangeAlert() {

		final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.password_change_dialog);
		final Button save = (Button)dialog.findViewById(R.id.alert_ok_btn_pc);
		final Button cancel = (Button)dialog.findViewById(R.id.alert_cancel_btn_pc);
		final EditText editTextold = (EditText)dialog.findViewById(R.id.alert_edit_txt_old);
		final EditText editTextnew = (EditText)dialog.findViewById(R.id.alert_edit_txt_new);
		final EditText editTextre = (EditText)dialog.findViewById(R.id.alert_edit_txt_re);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				oldPwd=editTextold.getText().toString();
				newPwd=editTextnew.getText().toString();
				rePwd=editTextre.getText().toString();
				if(valideChangePwdFields()){
					if(isInternetOn()){
						try {
							JSONObject jsonObject = new JSONObject();
							String url = Constants.CHANGE_PASSWORD_URL;
							jsonObject.accumulate("authkey", userid);
							jsonObject.accumulate("password", newPwd);
							new KissMeAsyncTask(getActivity(), url, Constants.PASSWORD_CHANGE_RESPONSE, ProfileEditPage.this).execute(jsonObject);
							dialog.dismiss();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} else {
						Toast.makeText(getActivity(), Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		dialog.show();
	}
	private  boolean valideChangePwdFields(){
		String oldPWD = pref.getString("password", null);
		if(oldPwd.isEmpty()){
			Toast.makeText(getActivity(), Constants.INVALID_PASSWORD, Toast.LENGTH_SHORT).show();
			return false;
		}else if(!oldPwd.equalsIgnoreCase(oldPWD)){
			Toast.makeText(getActivity(), Constants.INVALID_OLD_PASSWORD, Toast.LENGTH_SHORT).show();
			return false;
		}else if(newPwd.isEmpty()){
			Toast.makeText(getActivity(), Constants.INVALID_PASSWORD, Toast.LENGTH_SHORT).show();
			return false;
		}else if(newPwd.length()<6){
			Toast.makeText(getActivity(), Constants.INVALID_PASSWORD_LENGTH, Toast.LENGTH_SHORT).show();
			return false;
		}else if(!newPwd.equalsIgnoreCase(rePwd)){
			Toast.makeText(getActivity(), Constants.INVALID_PASSWORD_RE, Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
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
}