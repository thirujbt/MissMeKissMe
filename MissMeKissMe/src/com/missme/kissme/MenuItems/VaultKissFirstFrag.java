package com.missme.kissme.MenuItems;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.missme.kissme.HomePageFragment;
import com.missme.kissme.MainFragmentMenu;
import com.missme.kissme.R;
import com.missme.kissme.AccountManager.LoginActivity;
import com.missme.kissme.DataBase.DBHandler;
import com.missme.kissme.InterfaceClass.AsyncResponse;
import com.missme.kissme.ServiceRequest.KissMeAsyncTask;
import com.missme.kissme.Utils.Constants;

public class VaultKissFirstFrag extends Fragment implements OnClickListener, AsyncResponse{

	ImageView backIcon,nextIcon;
	int PIC_REQUEST=1;
	StringBuilder sb;
	int currentLength;
	private int maxLength=4;
	EditText edtiTxt;
	DBHandler dbHandler;
	String vaultCode;
	SharedPreferences pref;
	Editor editor;
	int width =0;
	int height =0;
	RelativeLayout vaultCoderl;
	boolean vault=false;
	ImageView done;
	Button one_btn;
	Button two_btn;
	Button three_btn;
	Button four_btn;
	Button five_btn;
	Button six_btn;
	Button seven_btn;
	Button eight_btn;
	Button nine_btn;
	Button zero_btn;
	Button back_btn;
	private int totalCount;
	View view;
	String vaultCodeFirst;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		width = this.getResources().getDisplayMetrics().widthPixels;
		height = this.getResources().getDisplayMetrics().heightPixels;

		if(width >= 720 && height >= 1280){
			view = inflater.inflate(R.layout.fragment_vaultkissfirst_xhdpi, container,false);
		}else{
			view = inflater.inflate(R.layout.fragment_vaultkissfirst, container,false);
		}

		pref = getActivity().getSharedPreferences(Constants.MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
		editor = pref.edit();
		dbHandler = new DBHandler(getActivity());
		backIcon=(ImageView)view.findViewById(R.id.vaultKiss_back_icon);
		nextIcon=(ImageView)view.findViewById(R.id.vaultKiss_code_image);
		vaultCoderl=(RelativeLayout)view.findViewById(R.id.rr_vault_code);
		if(HomePageFragment.iSHome){
			backIcon.setVisibility(View.VISIBLE);
		}else{
			backIcon.setVisibility(View.GONE);
		}
		done = (ImageView)view.findViewById(R.id.vault_ok_btn);
		edtiTxt = (EditText)view.findViewById(R.id.sucess);
		TextView titlee=(TextView)view.findViewById(R.id.vaultKiss_txtsecond);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"SourceSansPro-Regular.otf");
		titlee.setTypeface(font);
		getDataFromDB();
		count();
		sb=new StringBuilder(edtiTxt.getText().toString());
		currentLength=sb.length();
		one_btn=(Button)view.findViewById(R.id.no_txt2);
		one_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				add("1");
			}
		});
		two_btn=(Button)view.findViewById(R.id.no_txt3);
		two_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				add("2");
			}
		});
		three_btn=(Button)view.findViewById(R.id.no_txt4);
		three_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				add("3");

			}
		});
		four_btn=(Button)view.findViewById(R.id.no_txt5);
		four_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				add("4");
			}
		});
		five_btn=(Button)view.findViewById(R.id.no_txt6);
		five_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				add("5");

			}
		});
		six_btn=(Button)view.findViewById(R.id.no_txt7);
		six_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				add("6");
			}
		});
		seven_btn=(Button)view.findViewById(R.id.no_txt8);
		seven_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				add("7");
			}
		});
		eight_btn=(Button)view.findViewById(R.id.no_txt9);
		eight_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				add("8");

			}
		});
		nine_btn=(Button)view.findViewById(R.id.no_txt10);
		nine_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				add("9");
			}
		});
		zero_btn=(Button)view.findViewById(R.id.no_txt1);
		zero_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				add("0");
			}
		});
		back_btn=(Button)view.findViewById(R.id.arow_txt);
		back_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if(sb.length()>0){
					currentLength--;
					sb.deleteCharAt((sb.length())-1);
					edtiTxt.setText(sb.toString());
				}
			}
		});
		back_btn.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				currentLength=0;
				sb=new StringBuilder();
				edtiTxt.setText(sb.toString());
				return false;
			}
		});
		done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String inputCode=edtiTxt.getText().toString();
				if(!inputCode.equalsIgnoreCase(vaultCode)){
					Toast.makeText(getActivity(), "Enter valid code in digits", Toast.LENGTH_SHORT).show();
				}else{
					Intent trackIntent = new Intent(getActivity(), MainFragmentMenu.class);
					trackIntent.putExtra("homepage", "vaultkisssecond");
					getActivity().startActivity(trackIntent);
					getActivity().finish();
				}
			}
		});
		backIcon.setOnClickListener(this);
		nextIcon.setOnClickListener(this);
		
		if(totalCount == 1 && vaultCode.equalsIgnoreCase("0")){
			showVaultCodeSetAlert();
		}
		return view;
	}
	public void count(){
		totalCount = pref.getInt("counter", 0);
		totalCount++;
		editor.putInt("counter", totalCount);
		editor.commit();	
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
					vaultCode=cursor.getString(cursor.getColumnIndex(DBHandler.PROFILE_COLUMN_VAULTCODE));
					editor.putString(Constants.VAULT_CODE_PREF, vaultCode);
					editor.commit();
					if(!cursor.isClosed()){
						cursor.close();
					}
				} while (cursor.moveToNext());
			}
			cursor.close();
			dbHandler.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	public void onClick(View v) {
		int id=v.getId();
		switch (id) {
		case R.id.vaultKiss_back_icon:
			if(vault){
				if(!HomePageFragment.iSHome){
					backIcon.setVisibility(View.GONE);
				}
				vaultCoderl.setVisibility(View.GONE);
				vault=false;
				currentLength=0;
				sb=new StringBuilder();
				edtiTxt.setText(sb.toString());
			}else{
				Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
				getActivity().startActivity(intent);
				getActivity().finish();
			}
			break;
		case R.id.vaultKiss_code_image:
			vault=true;
			vaultCoderl.setVisibility(View.VISIBLE);
			backIcon.setVisibility(View.VISIBLE);
			break;
		}
	}

	private void showVaultCodeSetAlert() {

		final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.alert_dialog_main);
		final EditText alertEditTxt = (EditText)dialog.findViewById(R.id.alert_edit_txt);
		final EditText editVaultCodeTxt = (EditText)dialog.findViewById(R.id.alert_edit_vault_code);
		final View lineView=(View)dialog.findViewById(R.id.view1);
		final View lineView1=(View)dialog.findViewById(R.id.view);
		final View lineView2=(View)dialog.findViewById(R.id.view2);
		final TextView alertTitleTxt = (TextView)dialog.findViewById(R.id.alert_title);
		Button okBtn = (Button) dialog.findViewById(R.id.alert_ok_btn);
		Button cancelBtn = (Button) dialog.findViewById(R.id.alert_cancel_btn);
		Button sendBtn = (Button) dialog.findViewById(R.id.alert_ok_btns);
		sendBtn.setVisibility(View.VISIBLE);
		sendBtn.setText("Save");
		okBtn.setVisibility(View.GONE);
		alertEditTxt.setVisibility(View.GONE);
		lineView1.setVisibility(View.GONE);
		editVaultCodeTxt.setVisibility(View.VISIBLE);
		lineView2.setVisibility(View.VISIBLE);
		editVaultCodeTxt.setHint("Enter vault code");
		lineView.setVisibility(View.GONE);
		alertTitleTxt.setText("Set Four Digit Vault Code");
		cancelBtn.setVisibility(View.GONE);
		sendBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isInternetOn()){
					vaultCodeFirst = editVaultCodeTxt.getText().toString().trim();
					String userid = pref.getString(Constants.USER_AUTHKEY_PREF, null);
					try {
						JSONObject jsonObject = new JSONObject();
						String url = Constants.SET_VAULT_CODE;
						jsonObject.accumulate("authkey", userid);
						jsonObject.accumulate("vault_code", vaultCodeFirst);
						if(LoginActivity.getSessionname(getActivity())!=null){
							jsonObject.accumulate(Constants.SESSION_NAME,LoginActivity.getSessionname(getActivity()));
							}
						new KissMeAsyncTask(getActivity(), url, Constants.SET_VAULT_CODE_RESPONSE, VaultKissFirstFrag.this).execute(jsonObject);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					dialog.dismiss();
				} else {
					Toast.makeText(getActivity(), Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
				}
			}
		});
		dialog.show();
	}
	public void add(String num)
	{
		currentLength++;
		if(currentLength<=maxLength){
			sb.append(num);
			edtiTxt.setText(sb.toString());
		}
		else
			currentLength--;
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
				if(RespValue==Constants.SET_VAULT_CODE_RESPONSE){
					jObjServerResp = new JSONObject(serverResp);
					statusCode = jObjServerResp.getString(Constants.STATUS_CODE);
					if(statusCode.contentEquals("606")){
						dbHandler.updateVaultCode(vaultCodeFirst);
						Toast.makeText(getActivity(), "Vault code set successfully", Toast.LENGTH_SHORT).show();
						Intent trackIntent = new Intent(getActivity(), MainFragmentMenu.class);
						trackIntent.putExtra("homepage", "vaultkisssecond");
						getActivity().startActivity(trackIntent);
						getActivity().finish();
					}else{
						Toast.makeText(getActivity(), "Vault code set faild", Toast.LENGTH_SHORT).show();
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}