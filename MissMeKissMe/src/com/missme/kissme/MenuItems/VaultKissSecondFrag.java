package com.missme.kissme.MenuItems;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.missme.kissme.MainFragmentMenu;
import com.missme.kissme.R;
import com.missme.kissme.AccountManager.LoginActivity;
import com.missme.kissme.Adapter.VaultKissAdapter;
import com.missme.kissme.Bean.VaultBean;
import com.missme.kissme.InterfaceClass.AsyncResponse;
import com.missme.kissme.ServiceRequest.KissMeAsyncTask;
import com.missme.kissme.Utils.Constants;

public class VaultKissSecondFrag extends Fragment implements OnClickListener, AsyncResponse{

	ImageView backIcon,nextIcon,titleIcon;
	private GridView menuListView;
	private VaultKissAdapter MenuAdapter;
	SharedPreferences pref;
	String userName,attachType,date,time,msg,prof_image_path,milesTravelled,attachment,id;
	List<VaultBean> userDetails = new ArrayList<VaultBean>();
	Editor editor;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_vaultkisssecond, container,false);
		NotificationManager notificationManager = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(1);
		pref = getActivity().getSharedPreferences(Constants.MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
		editor = pref.edit();
		backIcon=(ImageView)view.findViewById(R.id.vaultsecond_back_icon);
		menuListView=(GridView)view.findViewById(R.id.vault_gridview);
		TextView titlee=(TextView)view.findViewById(R.id.newKiss_txt);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"SourceSansPro-Regular.otf");
		titlee.setTypeface(font);
		backIcon.setOnClickListener(this);
		sendRequest();
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				pref = getActivity().getSharedPreferences(Constants.MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
				editor = pref.edit();
				String msg=userDetails.get(position).getMsg();
				String date=userDetails.get(position).getDate();
				String attachment=userDetails.get(position).getAttachment();
				String attachmentName=userDetails.get(position).getAttachType();
				String userName=userDetails.get(position).getUserName();
				String profImage=userDetails.get(position).getProf_image_path();
				String milesTravel=userDetails.get(position).getMilesTravelled();
				String timeTravel=userDetails.get(position).getTime();
				String msgId=userDetails.get(position).getMsgId();
				String msgFrom=userDetails.get(position).getMsgFrom();
				String attachName=userDetails.get(position).getAttachName();
				String phoneNo=userDetails.get(position).getPhoneNo();
				String blindNo=userDetails.get(position).getBlindNo();
				String latFrom=userDetails.get(position).getLatFrom();
				String langFrom=userDetails.get(position).getLangFrom();
				String latTo=userDetails.get(position).getLatTo();
				String langTo=userDetails.get(position).getLangTo();
				editor.putString(Constants.VAULT_MSG_PREF, msg);
				editor.putString(Constants.VAULT_DATE_PREF, date);
				editor.putString(Constants.VAULT_ATTACHMENT_PREF, attachment);
				editor.putString(Constants.VAULT_ATTACHMENT_NAME_PREF, attachmentName);
				editor.putString(Constants.VAULT_USER_NAME_PREF, userName);
				editor.putString(Constants.VAULT_USER_PROFIMAGE_PREF, profImage);
				editor.putString(Constants.VAULT_USER_MILE_TRAVEL_PREF, milesTravel);
				editor.putString(Constants.VAULT_USER_TIME_TRAVEL_PREF, timeTravel);
				editor.putString(Constants.VAULT_USER_MSG_ID_PREF, msgId);
				editor.putString(Constants.VAULT_USER_MSG_FROM_PREF, msgFrom);
				editor.putString(Constants.VAULT_ATTACH_NAME_PREF, attachName);
				editor.putString(Constants.VAULT_PHONE_NO_PREF, phoneNo);
				editor.putString(Constants.VAULT_BLIND_NO_PREF, blindNo);
				editor.putString(Constants.LAT_FROM_PREF, latFrom);
				editor.putString(Constants.LANG_FROM_PREF, langFrom);
				editor.putString(Constants.LAT_TO_PREF, latTo);
				editor.putString(Constants.LANG_TO_PREF, langTo);
				editor.commit();
				Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
				intent.putExtra("homepage", "vaultkissthird");
				getActivity().startActivity(intent);
				getActivity().finish();
			}
		});
	}

	public void sendRequest(){
		String url = Constants.VAULT_KISS_URL;
		String userid = pref.getString(Constants.USER_AUTHKEY_PREF, null);
		String vaultCode = pref.getString(Constants.VAULT_CODE_PREF, null);
		if(isInternetOn()){
			try {
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.accumulate("authkey", userid);
					jsonObject.accumulate("vaultcode", vaultCode);
					if(LoginActivity.getSessionname(getActivity())!=null){
						jsonObject.accumulate(Constants.SESSION_NAME,LoginActivity.getSessionname(getActivity()));
						}
					new KissMeAsyncTask(this.getActivity(), url, Constants.VAULT_KISS_RESPONSE, VaultKissSecondFrag.this).execute(jsonObject);
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
	public void onClick(View v) {
		int id=v.getId();
		switch (id) {
		case R.id.vaultsecond_back_icon:
			Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
			intent.putExtra("homepage", "vaultkissfirst");
			getActivity().startActivity(intent);
			getActivity().finish();
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
			if(serverResp!=null){
				JSONObject jObjServerResp;
				if(RespValue==Constants.VAULT_KISS_RESPONSE){
					jObjServerResp = new JSONObject(serverResp);
					statusCode = jObjServerResp.getString(Constants.STATUS_CODE);
					if(statusCode.contentEquals("600")){
						String result=jObjServerResp.getString(Constants.USER_DETAILS);
						if(!result.contentEquals("null")){
							JSONArray jObj = null;
							jObj = new JSONArray(result);
							for(int i=0;i<jObj.length();i++){
								VaultBean objDetails = new VaultBean();
								JSONObject jObjUser;
								jObjUser = jObj.getJSONObject(i);
								userName=jObjUser.getString("username");
								attachType=jObjUser.getString("attach_type");
								date=jObjUser.getString("create_date");
								msg=jObjUser.getString("messages");
								prof_image_path=jObjUser.getString("prof_image_path");
								attachment=jObjUser.getString("attachments");
								milesTravelled=jObjUser.getString("kilo");
								time=jObjUser.getString("hours");
								id=jObjUser.getString("id");
								String msgFrom=jObjUser.getString("msg_from");
								String attachName=jObjUser.getString("attach_name");
								String readStatus=jObjUser.getString("read_status");
								String phoneNo=jObjUser.getString("phone");
								String blindNo=jObjUser.getString("contact_num");
								String latFrom=jObjUser.getString("from_lat");
								String langFrom=jObjUser.getString("from_lang");
								String latTo=jObjUser.getString("to_lat");
								String langTo=jObjUser.getString("to_lang");
								objDetails.setUserName(userName);
								objDetails.setAttachType(attachType);
								objDetails.setAttachment(attachment);
								objDetails.setDate(date);
								objDetails.setMsg(msg);
								objDetails.setProf_image_path(prof_image_path);
								objDetails.setMilesTravelled(milesTravelled);
								objDetails.setTime(time);
								objDetails.setMsgId(id);
								objDetails.setMsgFrom(msgFrom);
								objDetails.setLatFrom(latFrom);
								objDetails.setLangFrom(langFrom);
								objDetails.setLatTo(latTo);
								objDetails.setLangTo(langTo);
								objDetails.setAttachName(attachName);
								objDetails.setReadStatus(readStatus);
								objDetails.setBlindNo(blindNo);
								objDetails.setPhoneNo(phoneNo);
								userDetails.add(objDetails);
							}
							MenuAdapter = new VaultKissAdapter(userDetails, getActivity());
							menuListView.setAdapter(MenuAdapter);
						}else{
							//Toast.makeText(getActivity(), "Vault is empty", Toast.LENGTH_SHORT).show();
							Showdialog(getActivity());
							
						}				
					}else{
						Toast.makeText(getActivity(), "No response from server", Toast.LENGTH_SHORT).show();
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	private void Showdialog(final Activity activity){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
		builder1.setMessage("No kisses have been received in vault");
		builder1.setCancelable(true);
		
		builder1.setNegativeButton("Ok",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		AlertDialog alert11 = builder1.create();
		alert11.show();
	}
} 