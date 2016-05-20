package com.missme.kissme.MenuItems;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.missme.kissme.HomePageFragment;
import com.missme.kissme.MainFragmentMenu;
import com.missme.kissme.R;
import com.missme.kissme.AccountManager.LoginActivity;
import com.missme.kissme.Adapter.FloatingKissAdapter;
import com.missme.kissme.Bean.FloatingBean;
import com.missme.kissme.InterfaceClass.AsyncResponse;
import com.missme.kissme.ServiceRequest.KissMeAsyncTask;
import com.missme.kissme.Utils.Constants;

public class FloatingKissFirstFrag extends Fragment implements OnClickListener, AsyncResponse{

	ImageView backIcon,nextIcon,titleIcon;
	private ListView menuListView;
	private FloatingKissAdapter MenuAdapter;
	SharedPreferences pref;
	String userName,attachType,date,time,msg,prof_image_path,milesTravelled,attachment,id;
	List<FloatingBean> userDetails = new ArrayList<FloatingBean>();
	Editor editor;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_floatingkissfirst, container,false);
		pref = getActivity().getSharedPreferences(Constants.MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
		editor = pref.edit();
		backIcon=(ImageView)view.findViewById(R.id.floating_back_icon);
		menuListView=(ListView)view.findViewById(R.id.floating_listview);
		TextView titlee=(TextView)view.findViewById(R.id.newKiss_txt);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"SourceSansPro-Regular.otf");
		titlee.setTypeface(font);
		if(HomePageFragment.iSHome){
			backIcon.setVisibility(View.VISIBLE);
		}else{
			backIcon.setVisibility(View.GONE);
		}
		backIcon.setOnClickListener(this);
		getDetails();
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
				String kissType=userDetails.get(position).getKissType();		
				if(kissType.equalsIgnoreCase("realtime")){
					String userName=userDetails.get(position).getUserName();
					String profImage=userDetails.get(position).getProf_image_path();
					String milesTravel=userDetails.get(position).getMilesTravelled();
					String timeTravel=userDetails.get(position).getTime();
					String latFrom=userDetails.get(position).getLatFrom();
					String langFrom=userDetails.get(position).getLangFrom();
					String latTo=userDetails.get(position).getLatTo();
					String langTo=userDetails.get(position).getLangTo();
					String sendType=userDetails.get(position).getSendType();
					String msgID=userDetails.get(position).getMsgId();
					editor.putString(Constants.FLOATING_USER_NAME_PREF, userName);
					editor.putString(Constants.FLOATING_USER_PROFIMAGE_PREF, profImage);
					editor.putString(Constants.FLOATING_USER_MILE_TRAVEL_PREF, milesTravel);
					editor.putString(Constants.FLOATING_USER_TIME_TRAVEL_PREF, timeTravel);
					editor.putString(Constants.FLOATING_LAT_FROM_PREF, latFrom);
					editor.putString(Constants.FLOATING_LANG_FROM_PREF, langFrom);
					editor.putString(Constants.FLOATING_LAT_TO_PREF, latTo);
					editor.putString(Constants.FLOATING_LANG_TO_PREF, langTo);
					editor.putString(Constants.FLOATING_SEND_TYPE_PREF, sendType);
					editor.putString(Constants.FLOATING_MSG_ID_PREF, msgID);
					editor.commit();
					Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
					intent.putExtra("homepage", "floatingkisssecond");
					getActivity().startActivity(intent);
					getActivity().finish();
				}else{
					Toast.makeText(getActivity(),"You can able to track real time kisses only", Toast.LENGTH_SHORT).show();
				}			
			}
		});
	}
	public void getDetails(){
		String url = Constants.FLOATING_KISS_URL;
		String userid = pref.getString(Constants.USER_AUTHKEY_PREF, null);
		if(isInternetOn()){
			try {
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.accumulate("authkey", userid);
					if(LoginActivity.getSessionname(getActivity())!=null){
						jsonObject.accumulate(Constants.SESSION_NAME,LoginActivity.getSessionname(getActivity()));
					}
					new KissMeAsyncTask(this.getActivity(), url, Constants.FLOATING_KISS_RESPONSE, FloatingKissFirstFrag.this).execute(jsonObject);
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
		case R.id.floating_back_icon:
			Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
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
		System.out.println("Traveling kiss Response------------"+serverResp);

		//{"Message":"Authentication Success","device_log":"Yes","results":null,"status_code":105}

		try {
			String statusCode = "0";
			if(serverResp!=null){
				JSONObject jObjServerResp;
				if(RespValue==Constants.FLOATING_KISS_RESPONSE){
					jObjServerResp = new JSONObject(serverResp);
					statusCode = jObjServerResp.getString(Constants.STATUS_CODE);
					String result=jObjServerResp.getString(Constants.USER_DETAILS);
					if(statusCode.equalsIgnoreCase("105")){
						JSONArray jObj = null;
						if(!result.equals("null"))
						{
							jObj = new JSONArray(result);
							for(int i=0;i<jObj.length();i++){
								FloatingBean objDetails = new FloatingBean();
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
								String latFrom=jObjUser.getString("from_lat");
								String langFrom=jObjUser.getString("from_lang");
								String latTo=jObjUser.getString("to_lat");
								String langTo=jObjUser.getString("to_lang");
								String sendType=jObjUser.getString("send_type");
								String KissType=jObjUser.getString("kiss_type");
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
								objDetails.setSendType(sendType);
								objDetails.setKissType(KissType);						
								userDetails.add(objDetails);
							}
							MenuAdapter = new FloatingKissAdapter(userDetails, getActivity());
							menuListView.setAdapter(MenuAdapter);
						}
						else
						{
							blindContactSaveDialog(getActivity());
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
	private void blindContactSaveDialog(final Activity activity){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
		builder1.setMessage("No traveling kisses are available");
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