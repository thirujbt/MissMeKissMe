package com.missme.kissme.MenuItems;

import java.util.Collections;
import java.util.Comparator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.missme.kissme.HomePageFragment;
import com.missme.kissme.MainFragmentMenu;
import com.missme.kissme.R;
import com.missme.kissme.Adapter.NewKissContactAdapter;
import com.missme.kissme.Bean.ContactBean;
import com.missme.kissme.InterfaceClass.AsyncResponse;
import com.missme.kissme.ServiceRequest.KissMeAsyncTask;
import com.missme.kissme.Utils.Constants;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RealTimeKissFirstFrag extends Fragment implements OnClickListener, AsyncResponse{

	ImageView backIcon,nextIcon,titleIcon,processIcon;
	Button send;
	ListView menuListView;
	NewKissContactAdapter Adapter;
	SharedPreferences pref,pref1;
	String authid=null;
	String gpsStatus=null;
	String phone,sendStatus,nonUserName;
	Editor editor;
	int width =0;
	int height =0;
	LinearLayout llNext,llsendbtn;
	RelativeLayout nextProcess;
	String ownUserId;
	LinearLayout selectOption;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		width = this.getResources().getDisplayMetrics().widthPixels;
		height = this.getResources().getDisplayMetrics().heightPixels;
		View view = inflater.inflate(R.layout.fragment_realtimekissfirst, container,false);
		pref = getActivity().getSharedPreferences(Constants.REAL_MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
		pref1 = getActivity().getSharedPreferences(Constants.MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
		editor = pref.edit();
		backIcon=(ImageView)view.findViewById(R.id.realtime_back_icon);
		nextIcon=(ImageView)view.findViewById(R.id.realtime_next_icon);
		menuListView=(ListView)view.findViewById(R.id.newkiss_contact_gridview);
		titleIcon=(ImageView)view.findViewById(R.id.realtime_icon1);
		titleIcon.setBackgroundResource(R.drawable.realtime_kiss_icon);
		llNext=(LinearLayout)view.findViewById(R.id.llNextFirst);
		llsendbtn=(LinearLayout)view.findViewById(R.id.llsendbtn);
		send=(Button)view.findViewById(R.id.send_btn);
		processIcon=(ImageView)view.findViewById(R.id.realtime_process_icon);
		nextProcess=(RelativeLayout)view.findViewById(R.id.real_newkiss_header4);
		TextView titlee=(TextView)view.findViewById(R.id.newKiss_txt);
		TextView titlBar=(TextView)view.findViewById(R.id.realnewKiss_txt);
		selectOption=(LinearLayout)view.findViewById(R.id.real_newkiss_header2);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"SourceSansPro-Regular.otf");
		titlee.setTypeface(font);
		titlBar.setTypeface(font);
		ownUserId=pref1.getString(Constants.USER_AUTHKEY_PREF, null);
		llNext.setOnClickListener(this);
		backIcon.setOnClickListener(this);
		nextIcon.setOnClickListener(this);
		send.setOnClickListener(this);	
		clearSharePref();
		return view;
	}

	public void clearSharePref(){
		SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.REAL_MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.clear();
		editor.commit();
	}	 
	
	@Override
	public void onResume() {
		super.onResume();
		if(HomePageFragment.contactBeanList1.isEmpty()){
			selectOption.setVisibility(View.GONE);
			Toast.makeText(getActivity(), "Contacts are not available.", Toast.LENGTH_SHORT).show();
		}else{
			selectOption.setVisibility(View.VISIBLE);
			/*Collections.sort(HomePageFragment.contactBeanList1, new Comparator<ContactBean>() {
			    public int compare(ContactBean one, ContactBean other) {
			        return one.getSrname().compareTo(other.getSrname());
			    }
			});*/
			try{
				Collections.sort(HomePageFragment.contactBeanList1, new Comparator<ContactBean>() {
				    public int compare(ContactBean one, ContactBean other) {
				        return one.getName().compareTo(other.getName());
				    }
				});
			}catch(Exception e)
			{
				Collections.sort(HomePageFragment.contactBeanList1, new Comparator<ContactBean>() {
				    public int compare(ContactBean one, ContactBean other) {
				        return one.getSrname().compareTo(other.getSrname());
				    }
				});
			}
			Adapter = new NewKissContactAdapter(getActivity(),HomePageFragment.contactBeanList1);
			menuListView.setAdapter(Adapter);
		}
		menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				gpsStatus=HomePageFragment.contactBeanList1.get(position).getSrGpsStatus();
				authid=HomePageFragment.contactBeanList1.get(position).getSrUserid();

				if(!ownUserId.equalsIgnoreCase(authid)){

					if(authid.contains("null")){
						phone=HomePageFragment.contactBeanList1.get(position).getSrphoneNo();
						sendStatus=HomePageFragment.contactBeanList1.get(position).getSrSentStatus();
						nonUserName=HomePageFragment.contactBeanList1.get(position).getSrname();
						if(sendStatus.contains("1")){
							llNext.setVisibility(View.GONE);
							llsendbtn.setVisibility(View.GONE);
							processIcon.setVisibility(View.GONE);
							nextProcess.setVisibility(View.GONE);
							Toast.makeText(getActivity(), "Kiss Already Sent", Toast.LENGTH_SHORT).show();
						}else{
							llNext.setVisibility(View.GONE);
							llsendbtn.setVisibility(View.VISIBLE);
							processIcon.setVisibility(View.GONE);
							nextProcess.setVisibility(View.VISIBLE);
						}				
					}else{
						if(gpsStatus.equalsIgnoreCase("ON")){
							processIcon.setVisibility(View.VISIBLE);
							llNext.setVisibility(View.VISIBLE);
							llsendbtn.setVisibility(View.GONE);
							nextProcess.setVisibility(View.VISIBLE);
							authid=HomePageFragment.contactBeanList1.get(position).getSrUserid();
							String toUser=HomePageFragment.contactBeanList1.get(position).getSrname();
							String toUserLat=HomePageFragment.contactBeanList1.get(position).getSrlat();
							String toUserLang=HomePageFragment.contactBeanList1.get(position).getSrlangi();
							String profImage=HomePageFragment.contactBeanList1.get(position).getSrprofileImg();
							editor.putString(Constants.REAL_USER_AUTHID_PREF, authid);
							editor.putString(Constants.REAL_TO_USER_NAME_PREF, toUser);
							editor.putString(Constants.REAL_TO_USER_LAT_PREF, toUserLat);
							editor.putString(Constants.REAL_TO_USER_LANG_PREF, toUserLang);
							editor.putString(Constants.REAL_PROFILE_BASE_PREF, profImage);
							editor.commit();
						}else{
							llNext.setVisibility(View.VISIBLE);
							processIcon.setVisibility(View.VISIBLE);
							llsendbtn.setVisibility(View.GONE);
							nextProcess.setVisibility(View.VISIBLE);
							authid=HomePageFragment.contactBeanList1.get(position).getSrUserid();
							String toUser=HomePageFragment.contactBeanList1.get(position).getSrname();
							String profImage=HomePageFragment.contactBeanList1.get(position).getSrprofileImg();
							editor.putString(Constants.REAL_USER_AUTHID_PREF, authid);
							editor.putString(Constants.REAL_TO_USER_NAME_PREF, toUser);
							editor.putString(Constants.REAL_PROFILE_BASE_PREF, profImage);
							editor.commit();
						}
					}
				}else{
					llNext.setVisibility(View.GONE);
					llsendbtn.setVisibility(View.GONE);
					processIcon.setVisibility(View.GONE);
					nextProcess.setVisibility(View.GONE);
				}
			}
		});
	}
	@Override
	public void onClick(View v) {
		int id=v.getId();
		switch (id) {
		case R.id.realtime_back_icon:
			Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
			intent.putExtra("homepage", "newkiss");
			getActivity().startActivity(intent);
			getActivity().finish();
			break;
		case R.id.llNextFirst:
			if(authid==null){
				Toast.makeText(getActivity(), Constants.CONTACT_EMPTY, Toast.LENGTH_SHORT).show();
			}else if(gpsStatus.equalsIgnoreCase("OFF")){
				showAddressAlert();
			}else{
				editor.putString(Constants.NEW_KISS_SELECTION_PREF, "realtime");
				editor.commit();
				Intent nextintent = new Intent(getActivity(), MainFragmentMenu.class);
				nextintent.putExtra("homepage", "realtimesecond");
				getActivity().startActivity(nextintent);
				getActivity().finish();	
			}
			break;
		case R.id.send_btn:
			String url = Constants.KISS_SEND_CONTACT;
			String userid = pref1.getString(Constants.USER_AUTHKEY_PREF, null);
			if(isInternetOn()){
				try {
					try {
						JSONObject jsonObject = new JSONObject();
						jsonObject.accumulate("fromauthkey",userid);
						jsonObject.accumulate("phone_num",phone);
						new KissMeAsyncTask(getActivity(), url, Constants.VAULT_DELETE_MESSAGE_RESPONSE, RealTimeKissFirstFrag.this).execute(jsonObject);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(getActivity(), Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
			}		
			break;
		}
	}

	private void showAddressAlert() {

		final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.alert_dialog_main);
		final EditText alertEditTxt = (EditText)dialog.findViewById(R.id.alert_edit_txt);
		final EditText alertEditAddressTxt = (EditText)dialog.findViewById(R.id.alert_edit_addres_txt);
		final View lineView=(View)dialog.findViewById(R.id.view1);
		final View lineView1=(View)dialog.findViewById(R.id.view);
		final TextView alertTitleTxt = (TextView)dialog.findViewById(R.id.alert_title);
		Button okBtn = (Button) dialog.findViewById(R.id.alert_ok_btn);
		Button cancelBtn = (Button) dialog.findViewById(R.id.alert_cancel_btn);
		okBtn.setText("OK");
		alertEditTxt.setVisibility(View.GONE);
		lineView1.setVisibility(View.GONE);
		alertEditAddressTxt.setVisibility(View.VISIBLE);
		lineView.setVisibility(View.VISIBLE);
		alertTitleTxt.setText("Enter Address");
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
					String address = alertEditAddressTxt.getText().toString().trim();
					if(address!=null && !address.equalsIgnoreCase("") && address.length()>0){
						String url="https://maps.googleapis.com/maps/api/geocode/json?address="+address;
						new GetLatLang(url).execute();
						dialog.dismiss();
					} else {
						Toast.makeText(getActivity(), "Enter address", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getActivity(), Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
				}
			}
		});
		dialog.show();
	}
	public class GetLatLang extends AsyncTask<Void, Void, String> {

		String xml,url;
		public GetLatLang(String url){
			this.url=url;
		}
		protected void onPreExecute() {
		}
		@Override
		protected String doInBackground(Void... params) {
			try{
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity htpEntity = httpResponse.getEntity();
				xml = EntityUtils.toString(htpEntity);
			}
			catch(Exception e){
			}
			return xml;
		}
		protected void onPostExecute(String response) {
			if(response!=null){
				JSONObject jObjServerResp;
				try {
					jObjServerResp = new JSONObject(response);
					String result=jObjServerResp.getString(Constants.USER_DETAILS);
					String status=jObjServerResp.getString("status");

					if(status.contentEquals("OK")){
						JSONArray jObj = null;
						jObj = new JSONArray(result);
						for(int i=0;i<jObj.length();i++){
							JSONObject jObjUser;
							jObjUser = jObj.getJSONObject(i);          
							//	String formatted_address=jObjUser.getString("formatted_address");
							JSONObject geometry=jObjUser.getJSONObject("geometry");
							JSONObject location=geometry.getJSONObject("location");
							String lat=location.getString("lat");
							String lang=location.getString("lng");
							editor.putString(Constants.REAL_TO_USER_LAT_PREF, lat);
							editor.putString(Constants.REAL_TO_USER_LANG_PREF, lang);
							editor.commit();
							if(lat != null && lang != null){
								Intent nextintent = new Intent(getActivity(), MainFragmentMenu.class);
								nextintent.putExtra("homepage", "realtimesecond");
								getActivity().startActivity(nextintent);
								getActivity().finish();
							}else{
								Toast.makeText(getActivity(), "You are enterd wrong address", Toast.LENGTH_SHORT).show();
							}
						}
					}else{
						Toast.makeText(getActivity(), "You are enterd wrong address", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}else{
				Toast.makeText(getActivity(), "You are enterd wrong address", Toast.LENGTH_SHORT).show();
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
			if(serverResp!=null){
				JSONObject jObjServerResp;
				if(RespValue==Constants.VAULT_DELETE_MESSAGE_RESPONSE){
					jObjServerResp = new JSONObject(serverResp);
					statusCode = jObjServerResp.getString(Constants.STATUS_CODE);
					if(statusCode.contentEquals("608")){
						showKissSentAlert();						
						if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT_WATCH){
							SmsManager smsManager = SmsManager.getDefault();
							smsManager.sendTextMessage(phone, null,Constants.INVITE__KISS_MESSAGE, null, null);
							Toast.makeText(getActivity(), "Invite sent.", Toast.LENGTH_SHORT).show();
						}else{
							Intent smsIntent = new Intent(Intent.ACTION_VIEW);
							smsIntent.setData(Uri.parse("smsto:"));
							smsIntent.setType("vnd.android-dir/mms-sms");
							smsIntent.putExtra("address"  ,phone);
							smsIntent.putExtra("sms_body"  , Constants.INVITE__KISS_MESSAGE);
							getActivity().startActivity(smsIntent);
						}						
					}else{
						Toast.makeText(getActivity(), "Kiss sent faild", Toast.LENGTH_SHORT).show();
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	private void showKissSentAlert() {

		final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.kiss_send_alert);
		final TextView goHomeTxt = (TextView)dialog.findViewById(R.id.go_home_txt);
		final TextView yourKiss = (TextView)dialog.findViewById(R.id.your);
		final ImageView profImage = (ImageView)dialog.findViewById(R.id.realtime_final);
		final Button track = (Button)dialog.findViewById(R.id.Track_ok_btn);
		goHomeTxt.setText(Html.fromHtml(getString(R.string.go_home)));
		yourKiss.setText("Your Kiss Sent to "+"\n\t\t\t"+nonUserName);
		track.setVisibility(View.GONE);
		profImage.setMaxWidth(80);
		profImage.setMaxHeight(80);		
		goHomeTxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
				getActivity().startActivity(intent);
				getActivity().finish();
				dialog.dismiss();
			}
		});		
		dialog.show();
	}
}