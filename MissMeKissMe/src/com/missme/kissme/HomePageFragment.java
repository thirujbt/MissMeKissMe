package com.missme.kissme;

//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.content.pm.PackageManager.NameNotFoundException;
//import android.content.pm.Signature;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.Fragment;
//import android.util.Base64;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.missme.kissme.AccountManager.LoginActivity;
import com.missme.kissme.Adapter.ContactPageAdap;
import com.missme.kissme.Bean.ContactBean;
import com.missme.kissme.DataBase.DBHandler;
import com.missme.kissme.InterfaceClass.AsyncResponse;
import com.missme.kissme.InterfaceClass.HomeAsyncResponse;
import com.missme.kissme.ServiceRequest.GPSStatusUpdateService;
import com.missme.kissme.ServiceRequest.KissMeAsyncTask;
import com.missme.kissme.ServiceRequest.MissMeKissMeAsyncTask;
import com.missme.kissme.Utils.Constants;
import com.missme.kissme.Utils.SessionManager;
import com.missme.kissme.Utils.Utils;

public class HomePageFragment extends Fragment implements OnClickListener, HomeAsyncResponse, AsyncResponse{

	private ImageView mNewKiss,mBlindKiss,mVaultKiss,mTravelingKiss,mContacts,premium;
	LinearLayout leftKiss;
	TextView valutKissCountTxt,travlingKissCountTxt,kissLeftCountTxt;
	String valutCount;
	String travlingCount;
	Context context;
	SessionManager session;
	SharedPreferences pref;
	DBHandler dbHandler;
	String userName,phoneNo,emailID,gender,dob,prof_image_path,CoverPhoto,optBlind,vaultCode,loginStatus,purchaseStatus,kissLeft;
	Editor editor;
	String GpsStatus;
	String opt_Blind;
	List<ContactBean> contactBeanList = new ArrayList<ContactBean>();
	public static ArrayList<ContactBean> contactBeanList1 = new ArrayList<ContactBean>();
	public static ArrayList<String> searchList = new ArrayList<String>();
	ContactPageAdap contPageAdap;
	LinearLayout llKissCounter;
	boolean gpsEnabled=false;
	String deviceLog;
	String session_name;
	public static boolean iSHome=false;
	public String getOnClick=null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_homepage, container,false);
		NotificationManager notificationManager = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(2);
		pref = getActivity().getSharedPreferences(Constants.MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
		session = new SessionManager(getActivity());
		editor = pref.edit();
		initializeFields(view);

		if(!MainFragmentMenu.homePageService){
			getcontact();
			sendContactList();
			sendHomeRequest();
		}else{
			System.out.println("checking.jgjkgkkgkgkkk.................");
		}
		//sendHomeRequest();
		getOwnUserData();
		displayGpsStatus();
		//calculateHashKey("com.missme.kissme");
	
		return view;
	}


	@Override
	public void onResume() {
		super.onResume();

		LocationManager locationManager =(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

	}

	public void getOwnUserData(){
		try{
			dbHandler = new DBHandler(getActivity());
			Cursor cursor = null;
			if(dbHandler!=null)
				cursor = dbHandler.getProfile();
			if(cursor != null && cursor.getCount() > 0){
				cursor.moveToFirst();
				do {
					opt_Blind = cursor.getString(cursor.getColumnIndex(DBHandler.OPT_BLIND));
				} while (cursor.moveToNext());
			}
			cursor.close();
			dbHandler.close(); 
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	private void sendHomeRequest(){
		String url = Constants.HOME_URL;
		String userid = pref.getString(Constants.USER_AUTHKEY_PREF, null);
		if(isInternetOn()){
			try {
				if(session.isLoggedIn()) {
					try {
						JSONObject jsonObject = new JSONObject();
						jsonObject.accumulate("authkey", userid);	
						//--------------------------------------//
						jsonObject.accumulate("gcmid",LoginActivity.regid);
						if(LoginActivity.getSessionname(getActivity())!=null){
							jsonObject.accumulate(Constants.SESSION_NAME,LoginActivity.getSessionname(getActivity()));
						}
						System.out.println("Home page....."+jsonObject);
						new KissMeAsyncTask(this.getActivity(), url, Constants.HOMEPAGE_RESPONSE, HomePageFragment.this).execute(jsonObject);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(getActivity(), Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
		}
	}

	public void getcontact(){
		Cursor cur = getActivity().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		if(cur.getCount() > 0){
			while(cur.moveToNext()){
				ContactBean objContact = new ContactBean();
				String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
				String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				String photo = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
				objContact.setName(name);
				objContact.setProfileImg(photo);
				Cursor pCur = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID+ " = ?", new String[] { id }, null);
				if(pCur.getCount()>0) {
					while (pCur.moveToNext()) {
						// Do something with phones
						String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						objContact.setPhoneNo(phoneNo); // Here you can list of contact.
						contactBeanList.add(objContact);
					}    
				} 
				pCur.close();
			}
		}
	}
	public void sendContactList(){
		if(isInternetOn()){
			String url = Constants.CONTACT_SEND_URL;
			String userid = pref.getString(Constants.USER_AUTHKEY_PREF, null);
			ArrayList<String> contNameList = new ArrayList<String>();
			ArrayList<String> contPhonelist = new ArrayList<String>();
			for (ContactBean ctn : contactBeanList) {
				contNameList.add(ctn.getName());
				contPhonelist.add(ctn.getPhoneNo());
			}
			JSONArray contListJson = new JSONArray(contNameList);
			JSONArray phoneListJson = new JSONArray(contPhonelist);
			try {    
				JSONObject sendInviteJSON = new JSONObject();
				sendInviteJSON.put("authkey", userid);
				sendInviteJSON.put("name", contListJson);
				sendInviteJSON.put("contactlist",phoneListJson);
				if(LoginActivity.getSessionname(getActivity())!=null){
					sendInviteJSON.put(Constants.SESSION_NAME,LoginActivity.getSessionname(getActivity()));
				}

				new MissMeKissMeAsyncTask(this.getActivity(), url, Constants.CONTACTPAGE_RESPONSE, HomePageFragment.this).execute(sendInviteJSON);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(getActivity(), Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
		}
	}
	private void displayGpsStatus() {
		ContentResolver contentResolver = getActivity().getBaseContext().getContentResolver();
		boolean gpsStatus = Settings.Secure.isLocationProviderEnabled(contentResolver, LocationManager.GPS_PROVIDER);
		if (gpsStatus) {			
			GpsStatus="on";
		} else {
			GpsStatus="off";
		}
	}	

	private void initializeFields(View view){

		mNewKiss=(ImageView)view. findViewById(R.id.new_kiss);
		mBlindKiss=(ImageView) view. findViewById(R.id.Blind_kiss);
		mVaultKiss=(ImageView) view.findViewById(R.id.valut_kiss);
		mTravelingKiss=(ImageView)view. findViewById(R.id.travling_kiss);
		mContacts=(ImageView)view. findViewById(R.id.contacts_kiss);
		premium=(ImageView)view.findViewById(R.id.premium_kiss);
		valutKissCountTxt=(TextView)view. findViewById(R.id.valut_kiss_count_txt);
		travlingKissCountTxt=(TextView)view. findViewById(R.id.travling_kiss_count_txt);
		leftKiss=(LinearLayout)view.findViewById(R.id.ll_kisses_left);
		kissLeftCountTxt=(TextView)view.findViewById(R.id.kisses_left_count);
		mNewKiss.setOnClickListener(this);
		mBlindKiss.setOnClickListener(this);
		mVaultKiss.setOnClickListener(this);
		mTravelingKiss.setOnClickListener(this);
		mContacts.setOnClickListener(this);
		premium.setOnClickListener(this);
		leftKiss.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		int id=v.getId();
		switch (id) {
		case R.id.new_kiss:
			getOnClick="newkiss";
			sendHomeRequest();
			break;
		case R.id.Blind_kiss:
			getOnClick="blindkiss";
			sendHomeRequest();
			break;
		case R.id.valut_kiss:
			getOnClick="vaultkiss";
			sendHomeRequest();
			break;
		case R.id.travling_kiss:
			getOnClick="travelkiss";
			sendHomeRequest();
			break;
		case R.id.contacts_kiss:
			getOnClick="contacts";
			sendHomeRequest();
			break;
		case R.id.premium_kiss:
			Toast.makeText(getActivity(), "You have already purchased unlimited kisses", Toast.LENGTH_SHORT).show();
			break;
		case R.id.ll_kisses_left:
			getOnClick="kisses";
			sendHomeRequest();
			break;
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
			String menuClick=getOnClick;
			System.out.println("cheking..........................."+menuClick);
			if(serverResp!=null){
				JSONObject jObjServerResp;

				if(RespValue==Constants.HOMEPAGE_RESPONSE){
					jObjServerResp = new JSONObject(serverResp);
					statusCode = jObjServerResp.getString(Constants.STATUS_CODE);
					session_name=jObjServerResp.getString("session_name");
					///----------------------------///
					if(LoginActivity.getSessionname(getActivity())!=null&&!LoginActivity.getSessionname(getActivity()).equals(session_name)){
						Toast.makeText(getActivity(), "You have been logged in Other Device", Toast.LENGTH_SHORT).show();
						logoutUser();
					}else if(getOnClick==null){

					}else if(getOnClick.contentEquals("newkiss")){
						try{
							if(purchaseStatus==null && kissLeft==null){
								Toast.makeText(getActivity(), "No response from server", Toast.LENGTH_SHORT).show();
							}else if(purchaseStatus.equalsIgnoreCase("2") && kissLeft.equalsIgnoreCase("0")){
								Toast.makeText(getActivity(), "You don't have kisses.Please purchase kisses", Toast.LENGTH_SHORT).show();
							}else if(purchaseStatus.equalsIgnoreCase("0") && kissLeft.equalsIgnoreCase("0")){
								Toast.makeText(getActivity(), "You don't have kisses.Please purchase kisses", Toast.LENGTH_SHORT).show();
							}else if (!gpsEnabled) {
								Utils.showSettingsAlert(getActivity());
							}else{
								Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
								intent.putExtra("homepage", "newkiss");
								getActivity().startActivity(intent);
								getActivity().finish();
								iSHome=true;
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}else if(getOnClick.contentEquals("blindkiss")){
						try{
							if(purchaseStatus==null && kissLeft==null){
								Toast.makeText(getActivity(), "No response from server", Toast.LENGTH_SHORT).show();
							}else if(purchaseStatus.equalsIgnoreCase("2") && kissLeft.equalsIgnoreCase("0")){
								Toast.makeText(getActivity(), "You don't have kisses.Please purchase kisses", Toast.LENGTH_SHORT).show();
							}else if(purchaseStatus.equalsIgnoreCase("0") && kissLeft.equalsIgnoreCase("0")){
								Toast.makeText(getActivity(), "You don't have kisses.Please purchase kisses", Toast.LENGTH_SHORT).show();
							}else if (!gpsEnabled) {
								Utils.showSettingsAlert(getActivity());
							}else if(opt_Blind.equalsIgnoreCase("1")){
								Intent intentBlindKiss = new Intent(getActivity(), MainFragmentMenu.class);
								intentBlindKiss.putExtra("homepage", "blindkiss");
								getActivity().startActivity(intentBlindKiss);
								getActivity().finish();
								iSHome=true;
							}else{
								Toast.makeText(getActivity(), "Switch on blind kiss option", Toast.LENGTH_SHORT).show();
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}else if(getOnClick.contentEquals("vaultkiss")){
						Intent intentVault = new Intent(getActivity(), MainFragmentMenu.class);
						intentVault.putExtra("homepage", "vaultkissfirst");
						getActivity().startActivity(intentVault);
						getActivity().finish();
						iSHome=true;
					}else if(getOnClick.contentEquals("travelkiss")){
						Intent intentTraveling = new Intent(getActivity(), MainFragmentMenu.class);
						intentTraveling.putExtra("homepage", "floatingkissfirst");
						getActivity().startActivity(intentTraveling);
						getActivity().finish();
						iSHome=true;
					}else if(getOnClick.contentEquals("contacts")){
						Intent intentContact = new Intent(getActivity(), MainFragmentMenu.class);
						intentContact.putExtra("homepage", "contactpage");
						getActivity().startActivity(intentContact);
						getActivity().finish();
						iSHome=true;
					}else if(getOnClick.contentEquals("kisses")){
						Intent intentPurchaseLeft = new Intent(getActivity(), MainFragmentMenu.class);
						intentPurchaseLeft.putExtra("homepage", "purchasepage");
						getActivity().startActivity(intentPurchaseLeft);
						getActivity().finish();
						iSHome=true;
					}
					if(statusCode.contentEquals("206")){
						String result=jObjServerResp.getString(Constants.USER_DETAILS);
						valutCount=jObjServerResp.getString("vault_count");
						travlingCount=jObjServerResp.getString("delevery_count");
						deviceLog=jObjServerResp.getString("device_log");

						if(!valutCount.equalsIgnoreCase("0")){
							valutKissCountTxt.setVisibility(View.VISIBLE);
						}
						if(!travlingCount.equalsIgnoreCase("0")){
							travlingKissCountTxt.setVisibility(View.VISIBLE);
						}
						valutKissCountTxt.setText(valutCount);
						travlingKissCountTxt.setText(travlingCount);
						JSONArray jObj = null;
						jObj = new JSONArray(result);
						for(int i=0;i<jObj.length();i++){
							JSONObject jObjUser;
							jObjUser = jObj.getJSONObject(i);
							userName=jObjUser.getString("username");
							phoneNo=jObjUser.getString("phone");
							emailID=jObjUser.getString("email_id");
							gender=jObjUser.getString("gender");
							dob=jObjUser.getString("dob");
							prof_image_path=jObjUser.getString("prof_image_path");
							CoverPhoto=jObjUser.getString("pro_cover_path");
							optBlind=jObjUser.getString("blindkiss");
							vaultCode=jObjUser.getString("vault_code");
							purchaseStatus=jObjUser.getString("kiss_purchased");
							kissLeft=jObjUser.getString("kiss_left");
							editor.putString(Constants.DEVICE_LOG_PREF, deviceLog);
							editor.putString(Constants.PROFILE_OWN_PREF, prof_image_path);
							editor.putString(Constants.KISS_PURCHASE_STATUS_PREF, purchaseStatus);
							editor.putString(Constants.KISS_LEFT_PREF, kissLeft);
							editor.putString(Constants.OWN_NAME_PREF, userName);
							editor.commit();
						}if(purchaseStatus.equalsIgnoreCase("0") && kissLeft.equalsIgnoreCase("5")){
							leftKiss.setVisibility(View.VISIBLE);
							premium.setVisibility(View.GONE);
							kissLeftCountTxt.setText("\nCongrats!"+"\nYou have "+kissLeft+"\nkiss to send!");
						}else if(purchaseStatus.equalsIgnoreCase("0") && kissLeft.equalsIgnoreCase("4")){
							leftKiss.setVisibility(View.VISIBLE);
							premium.setVisibility(View.GONE);
							kissLeftCountTxt.setText("\n\tYou have"+"\n\t\t"+kissLeft+"\n\tkisses left!");
						}else if(purchaseStatus.equalsIgnoreCase("0") && kissLeft.equalsIgnoreCase("3")){
							leftKiss.setVisibility(View.VISIBLE);
							premium.setVisibility(View.GONE);
							kissLeftCountTxt.setText("\n\tYou have"+"\n\t\t"+kissLeft+"\n\tkisses left!");
						}else if(purchaseStatus.equalsIgnoreCase("0") && kissLeft.equalsIgnoreCase("2")){
							leftKiss.setVisibility(View.VISIBLE);
							premium.setVisibility(View.GONE);
							kissLeftCountTxt.setText("\n\tYou have"+"\n\t\t"+kissLeft+"\n\tkisses left!");
						}else if(purchaseStatus.equalsIgnoreCase("0") && kissLeft.equalsIgnoreCase("1")){
							leftKiss.setVisibility(View.VISIBLE);
							premium.setVisibility(View.GONE);
							kissLeftCountTxt.setText("\n\tYou have"+"\n\t\t"+kissLeft+"\n\tkisses left!");
						}else if(purchaseStatus.equalsIgnoreCase("0") && kissLeft.equalsIgnoreCase("0")){
							leftKiss.setVisibility(View.VISIBLE);
							premium.setVisibility(View.GONE);
							kissLeftCountTxt.setText("\n\tYou have"+"\n\t\t"+kissLeft+"\n\tkisses left!");
						}else if(purchaseStatus.equalsIgnoreCase("2")){
							leftKiss.setVisibility(View.VISIBLE);
							premium.setVisibility(View.GONE);
							kissLeftCountTxt.setText("\n\tYou have"+"\n\t\t"+kissLeft+"\n\tkisses left!");
						}else if(purchaseStatus.equalsIgnoreCase("1")){
							leftKiss.setVisibility(View.GONE);
							premium.setVisibility(View.VISIBLE);
						}else{
							leftKiss.setVisibility(View.VISIBLE);
							premium.setVisibility(View.GONE);
							kissLeftCountTxt.setText(kissLeft);
						}
						dbHandler = new DBHandler(getActivity());
						dbHandler.updateProfile(userName, null, phoneNo, emailID, gender, dob, null, vaultCode, prof_image_path,CoverPhoto,optBlind);
						MainFragmentMenu.homepage=false;

					}else{
						Toast.makeText(getActivity(), "No response from server", Toast.LENGTH_SHORT).show();
					}
				}else if(RespValue==Constants.CONTACTPAGE_RESPONSE){
					contactBeanList1.clear();
					searchList.clear();

					jObjServerResp = new JSONObject(serverResp);
					statusCode = jObjServerResp.getString(Constants.STATUS_CODE);
					if(statusCode.contentEquals("500")){
						String result=jObjServerResp.getString(Constants.USER_DETAILS);
						JSONArray jObj = null;
						jObj = new JSONArray(result);
						for(int i=0;i<jObj.length();i++){						
							ContactBean objContact = new ContactBean();
							JSONObject jObjUser;
							jObjUser = jObj.getJSONObject(i);
							String name=jObjUser.getString("name");
							String phoneNo=jObjUser.getString("contactlist");
							String profileImage=jObjUser.getString("prof_image_path");
							String userId=jObjUser.getString("authid");
							String invite=jObjUser.getString("Invite");
							String lat=jObjUser.getString("latitude");
							String lang=jObjUser.getString("langitude");
							String gpsStatus=jObjUser.getString("gps_status");
							String sentStatus=jObjUser.getString("send_id");
							String send=jObjUser.getString("send");						
							objContact.setSrname(name);
							objContact.setSrphoneNo(phoneNo);
							objContact.setSrprofileImg(profileImage);
							objContact.setSrUserid(userId);
							objContact.setSrInvite(invite);
							objContact.setSrlat(lat);
							objContact.setSrlangi(lang);
							objContact.setSrGpsStatus(gpsStatus);
							objContact.setSrSentStatus(sentStatus);
							objContact.setSrSend(send); 						
							contactBeanList1.add(objContact);
							searchList.add(name);
						}
						Collections.sort(contactBeanList1, new ContactBean.CompareTitle());
					}
				}else if(RespValue==Constants.LOGOUT_RESPONSE){
					//	JSONObject jObjServerResp;
					try {
						jObjServerResp = new JSONObject(serverResp);
						statusCode = jObjServerResp.getString(Constants.STATUS_CODE);
						switch (statusCode) {
						case Constants.SUCCESS:
							if (session == null) {
								session = new SessionManager(getActivity());
							}
							session.logoutUser(getActivity());
							LoginActivity.clearSherePref(getActivity());
							stopBackgroundService();
							Intent i = new Intent(getActivity(),LoginActivity.class);
							startActivity(i);
							getActivity().finish();
						case Constants.FAILD:
							break;
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}				
			}else{
				Toast.makeText(getActivity(), "No response from server", Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void stopBackgroundService(){
		getActivity().stopService(new Intent(getActivity(), GPSStatusUpdateService.class));
	}

/*	private void calculateHashKey(String yourPackageName) {
		try {
			PackageInfo info = getActivity().getPackageManager().getPackageInfo(yourPackageName,PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.e("KeyHash:",Base64.encodeToString(md.digest(), Base64.DEFAULT));

			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}*/
	private void logoutUser(){
		if(isInternetOn()){
			String userid = pref.getString(Constants.USER_AUTHKEY_PREF, null);
			String url = Constants.LOGOUT_URL;
			if (userid != null) {
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.accumulate("authkey", userid);
					new KissMeAsyncTask(getActivity(), url, Constants.LOGOUT_RESPONSE, HomePageFragment.this).execute(jsonObject);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		} else {
			Toast.makeText(getActivity(), Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
		}
	}
}