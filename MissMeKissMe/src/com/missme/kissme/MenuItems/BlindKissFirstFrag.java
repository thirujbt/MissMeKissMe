package com.missme.kissme.MenuItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.missme.kissme.MainFragmentMenu;
import com.missme.kissme.R;
import com.missme.kissme.AccountManager.LoginActivity;
import com.missme.kissme.DataBase.DBHandler;
import com.missme.kissme.InterfaceClass.AsyncResponse;
import com.missme.kissme.ServiceRequest.KissMeAsyncTask;
import com.missme.kissme.Utils.Constants;
import com.missme.kissme.Utils.ImageLoader1;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class BlindKissFirstFrag extends Fragment implements OnClickListener, AsyncResponse{

	ImageView backIcon,userImage;
	TextView nameTxt,nameTxtTitle;
	Button nextIcon;
	DisplayImageOptions options;
	Bitmap bitmapOrg;
	public ImageLoader1 imageLoader;
	ImageLoader imageLoader1;
	String uploadFileName;
	DBHandler dbHandler;
	SharedPreferences pref;
	Editor editor;
	String name,toLat,toLang;

	String profileImage;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.default_profile_img)
		.showImageForEmptyUri(R.drawable.default_profile_img)
		.showImageOnFail(R.drawable.default_profile_img)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_blindkissfirst, container,false);
		pref = getActivity().getSharedPreferences(Constants.BLIND_MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
		editor = pref.edit();
		backIcon=(ImageView)view.findViewById(R.id.blindfirst_back_icon);
		nextIcon=(Button)view.findViewById(R.id.blindfirst_send_btn);
		userImage=(ImageView)view.findViewById(R.id.blindfirst_image_blind);
		nameTxt=(TextView)view.findViewById(R.id.blindfirst_nametxt);
		nameTxtTitle=(TextView)view.findViewById(R.id.blind_kiss_searchimage);
		TextView titlee=(TextView)view.findViewById(R.id.newKiss_txt_blind);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"SourceSansPro-Regular.otf");
		titlee.setTypeface(font);
		name=pref.getString(Constants.BLIND_TO_USER_NAME_PREF, null);
		toLat=pref.getString(Constants.BLIND_TO_USER_LAT_PREF, null);
		toLang=pref.getString(Constants.BLIND_TO_USER_LANG_PREF, null);
		nameTxt.setText(name);
		nameTxtTitle.setText(name);
		getDetails();
		backIcon.setOnClickListener(this);
		nextIcon.setOnClickListener(this);
		return view;

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public void getDetails(){
		String url = Constants.BLIND_KISS_URL;
		if(isInternetOn()){
			try {		
				JSONObject jsonObject = new JSONObject();
				jsonObject.accumulate("to_name", name);
				jsonObject.accumulate("latitude", toLat);
				jsonObject.accumulate("longitude", toLang);
				if(LoginActivity.getSessionname(getActivity())!=null){
					jsonObject.accumulate(Constants.SESSION_NAME,LoginActivity.getSessionname(getActivity()));
					}
				new KissMeAsyncTask(this.getActivity(), url, Constants.GET_BLIND_RESPONSE, BlindKissFirstFrag.this).execute(jsonObject);
			} catch (JSONException e) {
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
		case R.id.blindfirst_back_icon:
			Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
			intent.putExtra("homepage", "blindkiss");
			getActivity().startActivity(intent);
			getActivity().finish();
			break;
		case R.id.blindfirst_send_btn:
			Intent nextintent = new Intent(getActivity(), MainFragmentMenu.class);
			nextintent.putExtra("homepage", "blindkissthird");
			getActivity().startActivity(nextintent);
			getActivity().finish();
			break;
		}
	}

	public void loadProfilImage(){

		ImageLoader.getInstance().displayImage(profileImage, userImage,options, new SimpleImageLoadingListener() {
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
				if(RespValue==Constants.GET_BLIND_RESPONSE){
					jObjServerResp = new JSONObject(serverResp);
					statusCode = jObjServerResp.getString(Constants.STATUS_CODE);
					if(statusCode.contentEquals("240")){
						String result=jObjServerResp.getString(Constants.USER_DETAILS);
						JSONArray jObj = null;
						jObj = new JSONArray(result);
						for(int i=0;i<jObj.length();i++){
							JSONObject jObjUser;
							jObjUser = jObj.getJSONObject(i);
							profileImage=jObjUser.getString("prof_image_path");
							String userId=jObjUser.getString("authid");
							System.out.println(profileImage+""+userId);
							editor.putString(Constants.BLIND_USER_AUTHID_PREF, userId);
							editor.putString(Constants.BLIND_PROFILE_BASE_PREF, profileImage);
							editor.commit();
						}
						loadProfilImage();
					}else{
						Toast.makeText(getActivity(), "No response from server", Toast.LENGTH_SHORT).show();
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}