package com.missme.kissme.MenuItems;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.missme.kissme.HomePageFragment;
import com.missme.kissme.MainFragmentMenu;
import com.missme.kissme.R;
import com.missme.kissme.AccountManager.LoginActivity;
import com.missme.kissme.Bean.BlindBean;
import com.missme.kissme.DataBase.DBHandler;
import com.missme.kissme.InterfaceClass.AsyncResponse;
import com.missme.kissme.ServiceRequest.KissMeAsyncTask;
import com.missme.kissme.Utils.Constants;
import com.missme.kissme.Utils.GPSLocation;
import com.missme.kissme.Utils.ImageLoader1;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class BlindKissMainFrag extends Fragment  implements LocationListener, AsyncResponse{

	ImageView backIcon,userImage,ownUserImage,toUserImage,searchbtn;
	GPSLocation gps;
	private GoogleMap googleMap;
	EditText address;
	Context mContext;
	LatLng ownPosition,latLng;
	Bitmap bitmap,bitmap_own;
	private List<BlindBean> latlong;
	private List<BlindBean> latlongSearch;
	Marker ownInfo;
	SharedPreferences pref,pref1;
	String profImage,toUserName,ownProfImage;
	DisplayImageOptions options;
	public ImageLoader1 imageLoader;
	RelativeLayout layout,layout_own;
	DBHandler dbHandler;
	MarkerOptions markerOptions;
	String authid=null;
	Editor editor;
	String GpsStatus;
	boolean searchResult=false;
	String deviceLogStatus;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.user_default_image)
		.showImageForEmptyUri(R.drawable.user_default_image)
		.showImageOnFail(R.drawable.user_default_image)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_blindkissmain, container,false);
		pref = getActivity().getSharedPreferences(Constants.BLIND_MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
		pref1 = getActivity().getSharedPreferences(Constants.MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
		deviceLogStatus=pref.getString(Constants.DEVICE_LOG_PREF, null);

		editor = pref.edit();
		gps=new GPSLocation(getActivity());
		latlong=new ArrayList<BlindBean>();
		latlongSearch=new ArrayList<BlindBean>();
		dbHandler = new DBHandler(getActivity());
		displayGpsStatus();
		backIcon=(ImageView)view.findViewById(R.id.blind_back_icon);
		ownUserImage=(ImageView)view.findViewById(R.id.ploter_user_imag_own_blind);
		toUserImage=(ImageView)view.findViewById(R.id.ploter_user_image_blind);
		searchbtn=(ImageView)view.findViewById(R.id.blind_kiss_searchimage);
		layout = (RelativeLayout)view.findViewById(R.id.relative_ploter_blind);
		layout_own = (RelativeLayout)view.findViewById(R.id.relative_ploter_own_blind);
		address=(EditText)view.findViewById(R.id.blind_kiss_searchbox);
		TextView titlee=(TextView)view.findViewById(R.id.newKiss_txt_blind);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"SourceSansPro-Regular.otf");
		titlee.setTypeface(font);
		if(HomePageFragment.iSHome){
			backIcon.setVisibility(View.VISIBLE);
		}else{
			backIcon.setVisibility(View.GONE);
		}
		getDataFromDB();
		getContactList();
		ownPosition = new LatLng(gps.getLatitude(),gps.getLongitude());
		backIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
				getActivity().startActivity(intent);
				getActivity().finish();
			}
		});
		loadPlatreProfilImage();
		loadPlatreOwnProfilImage();
		
		searchbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Getting user input location
				String location = address.getText().toString();
				if(location!=null && !location.equals("")){
					new GeocoderTask().execute(location);
					
				}
			}
		});

		address.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// Getting user input location
				String location = address.getText().toString();
				if(location!=null && !location.equals("")){
					new GeocoderTask().execute(location);
					
				}
				return false;
			}
		});
		
		return view;

	}

	public void getContactList(){
		String url = Constants.BLIND_KISS_URL;
		String userid = pref1.getString(Constants.USER_AUTHKEY_PREF, null);
		String lat=String.valueOf(gps.getLatitude());
		String lang=String.valueOf(gps.getLongitude());
		if(isInternetOn()){
			try {		
				JSONObject jsonObject = new JSONObject();
				jsonObject.accumulate("authkey", userid);
				jsonObject.accumulate("latitude", lat);
				jsonObject.accumulate("longitude", lang);
				new KissMeAsyncTask(this.getActivity(), url, Constants.GET_BLIND_CONTACT_LIST_RESPONSE, BlindKissMainFrag.this).execute(jsonObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(getActivity(), Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
		}
	}
	private void getDataFromDB(){
		//Get from DB
		try{
			Cursor cursor=dbHandler.getProfile();
			if (cursor.moveToFirst()) {
				do {
					ownProfImage=cursor.getString(cursor.getColumnIndex(DBHandler.PROFILE_COLUMN_PHOTO));
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
	public void onResume() {
		super.onResume();
		
		
		
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
	public void initializeMap(){

		if(!searchResult){
			// Getting Google Play availability status
			int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
			// Showing status
			if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
				int requestCode = 10;
				Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, getActivity(), requestCode);
				dialog.show();
			}else {	// Google Play Services are available	
				// Getting reference to the SupportMapFragment of activity_main.xml
				SupportMapFragment fm = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_blinkiss_page);
				// Getting GoogleMap object from the fragment
				googleMap = fm.getMap();
				googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				if (android.os.Build.VERSION.SDK_INT > 9) {
					StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
					StrictMode.setThreadPolicy(policy);
				}
				googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ownPosition, 15));
				//googleMap.setMyLocationEnabled(true);
				googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
					@Override
					public void onInfoWindowClick(Marker marker) {
						String name=marker.getTitle();
						LatLng latLang=marker.getPosition();
						if(!name.equalsIgnoreCase("You")){
							double cLat=latLang.latitude;
							double cLang=latLang.longitude;
							String lat=String.valueOf(cLat);
							String lang=String.valueOf(cLang);
							editor.putString(Constants.BLIND_TO_USER_NAME_PREF, name);
							editor.putString(Constants.BLIND_TO_USER_LAT_PREF, lat);
							editor.putString(Constants.BLIND_TO_USER_LANG_PREF, lang);
							editor.commit();
							Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
							intent.putExtra("homepage", "blindkissfirst");
							getActivity().startActivity(intent);
							getActivity().finish();
						}
					}
				});
			}
		}else{
			
			// Getting Google Play availability status
			int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
			// Showing status
			if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available[d
				int requestCode = 10;
				Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, getActivity(), requestCode);
				dialog.show();
			}else {	// Google Play Services are available	
				// Getting reference to the SupportMapFragment of activity_main.xml
				SupportMapFragment fm = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_blinkiss_page);
				// Getting GoogleMap object from the fragment
				googleMap = fm.getMap();
				googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				if (android.os.Build.VERSION.SDK_INT > 9) {
					StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
					StrictMode.setThreadPolicy(policy);
				}
				googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
				googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
					@Override
					public void onInfoWindowClick(Marker marker) {
						String name=marker.getTitle();
						LatLng latLang=marker.getPosition();
						if(!name.equalsIgnoreCase("You")){
							double cLat=latLang.latitude;
							double cLang=latLang.longitude;
							String lat=String.valueOf(cLat);
							String lang=String.valueOf(cLang);
							editor.putString(Constants.BLIND_TO_USER_NAME_PREF, name);
							editor.putString(Constants.BLIND_TO_USER_LAT_PREF, lat);
							editor.putString(Constants.BLIND_TO_USER_LANG_PREF, lang);
							editor.commit();
							Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
							intent.putExtra("homepage", "blindkissfirst");
							getActivity().startActivity(intent);
							getActivity().finish();
						}
					}
				});
			}
		}
	}
	public void loadPlatreProfilImage(){

		ImageLoader.getInstance().displayImage(profImage, toUserImage,options, new SimpleImageLoadingListener() {
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

	public void loadPlatreOwnProfilImage(){

		ImageLoader.getInstance().displayImage(ownProfImage, ownUserImage,options, new SimpleImageLoadingListener() {
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

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		public DownloadImageTask() {
		}
		@Override
		protected Bitmap doInBackground(String... urls) {
			//convertBitmap image
			try {
				Thread.sleep(2000);
				layout.setDrawingCacheEnabled(true);
				layout.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
				layout.layout(0, 0, layout.getMeasuredWidth(), layout.getMeasuredHeight());
				layout.buildDrawingCache(true);
				bitmap = Bitmap.createBitmap(layout.getDrawingCache());
				layout.setDrawingCacheEnabled(false);
				layout_own.setDrawingCacheEnabled(true);
				layout_own.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
				layout_own.layout(0, 0, layout_own.getMeasuredWidth(), layout_own.getMeasuredHeight());
				layout_own.buildDrawingCache(true);
				bitmap_own = Bitmap.createBitmap(layout_own.getDrawingCache());
				layout_own.setDrawingCacheEnabled(false);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return bitmap;
		}
		@Override
		protected void onPostExecute(Bitmap result) {
			refreshMarker();
		}
	}

	public void refreshMarker() {
		if(!searchResult){
			googleMap.clear();
			googleMap.addMarker(new MarkerOptions().position(ownPosition).title("You").icon(BitmapDescriptorFactory.fromBitmap(bitmap_own)));
			for (int i = 0; i < latlong.size(); i++) {
				BlindBean item = latlong.get(i);
				try{
					double lati=Double.parseDouble(item.getLatTo());
					double longLat=Double.parseDouble(item.getLangTo());
					String name=item.getName();
					googleMap.addMarker(new MarkerOptions().position(new LatLng(lati,longLat)).title(name).icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			Marker ownInfo=googleMap.addMarker(new MarkerOptions().position(ownPosition).title("You").icon(BitmapDescriptorFactory.fromBitmap(bitmap_own)));
			ownInfo.showInfoWindow();
		}else{
			googleMap.clear();
			latlong.clear();
			googleMap.addMarker(new MarkerOptions().position(ownPosition).title("You").icon(BitmapDescriptorFactory.fromBitmap(bitmap_own)));
			for (int i = 0; i < latlongSearch.size(); i++) {
				BlindBean item = latlongSearch.get(i);
				double lati=Double.parseDouble(item.getLatTo());
				double longLat=Double.parseDouble(item.getLangTo());
				String name=item.getName();
				googleMap.addMarker(new MarkerOptions().position(new LatLng(lati,longLat)).title(name).icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
			}
			Marker ownInfo=googleMap.addMarker(new MarkerOptions().position(ownPosition).title("You").icon(BitmapDescriptorFactory.fromBitmap(bitmap_own)));
			ownInfo.showInfoWindow();
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// Getting latitude of the current location
		double latitude = location.getLatitude();
		// Getting longitude of the current location
		double longitude = location.getLongitude();		
		// Creating a LatLng object for the current location
		LatLng latLng = new LatLng(latitude, longitude);
		// Showing the current location in Google Map
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		// Zoom in the Google Map
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));		
		// Setting latitude and longitude in the TextView tv_location
	}

	// An AsyncTask class for accessing the GeoCoding Web Service
	private class GeocoderTask extends AsyncTask<String, Void, List<Address>>{

		@Override
		protected List<Address> doInBackground(String... locationName) {
			// Creating an instance of Geocoder class
			Geocoder geocoder = new Geocoder(getActivity());
			List<Address> addresses = null;
			try {
				// Getting a maximum of 3 Address that matches the input text
				addresses = geocoder.getFromLocationName(locationName[0], 3);
			} catch (IOException e) {
				e.printStackTrace(); 
			}
			return addresses;   
		}

		@Override
		protected void onPostExecute(List<Address> addresses) {
			if(addresses==null || addresses.size()==0){
				Toast.makeText(getActivity(), "No Location found", Toast.LENGTH_SHORT).show();
			}
			// Clears all the existing markers on the map
			googleMap.clear();
			// Adding Markers on Google Map for each matching address
			for(int i=0;i<addresses.size();i++){
				Address address = (Address) addresses.get(i);
				// Creating an instance of GeoPoint, to display in Google Map
				latLng = new LatLng(address.getLatitude(), address.getLongitude());
				searchAddress(latLng);
				/*String addressText = String.format("%s, %s",
						address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
								address.getCountryName());*/
			}
		}
	}

	public void searchAddress(LatLng latLang){
		double cLat=latLang.latitude;
		double cLang=latLang.longitude;
		String lat=String.valueOf(cLat);
		String lang=String.valueOf(cLang);
		String url = Constants.BLIND_KISS_URL;
		String userid = pref1.getString(Constants.USER_AUTHKEY_PREF, null);
		if(isInternetOn()){
			try {		
				JSONObject jsonObject = new JSONObject();
				jsonObject.accumulate("authkey", userid);
				jsonObject.accumulate("latitude", lat);
				jsonObject.accumulate("longitude", lang);
				if(LoginActivity.getSessionname(getActivity())!=null){
					jsonObject.accumulate(Constants.SESSION_NAME,LoginActivity.getSessionname(getActivity()));
				}

				new KissMeAsyncTask(this.getActivity(), url, Constants.SEARCH_BLIND_KISS_RESPONSE, BlindKissMainFrag.this).execute(jsonObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(getActivity(), Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
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
				if(RespValue==Constants.GET_BLIND_CONTACT_LIST_RESPONSE){
					jObjServerResp = new JSONObject(serverResp);
					statusCode = jObjServerResp.getString(Constants.STATUS_CODE);
					if(statusCode.contentEquals("236")){
						String result=jObjServerResp.getString(Constants.USER_DETAILS);
						JSONArray jObj = null;
						jObj = new JSONArray(result);
						for(int i=0;i<jObj.length();i++){
							BlindBean objContact = new BlindBean();
							JSONObject jObjUser;
							jObjUser = jObj.getJSONObject(i);
							String name=jObjUser.getString("username");
							String profileImage=jObjUser.getString("prof_image_path");
							String userId=jObjUser.getString("authid");
							String lat=jObjUser.getString("latitude");
							String lang=jObjUser.getString("langitude");
							objContact.setName(name);
							objContact.setProfile_Image(profileImage);
							objContact.setToAuthId(userId);
							objContact.setLatTo(lat);
							objContact.setLangTo(lang);
							latlong.add(objContact);
						}
						if(GpsStatus.equalsIgnoreCase("on")){
							initializeMap();
							new DownloadImageTask().execute();
						}else{
							Toast.makeText(getActivity(), "Please Switch ON GPS Satellites", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
							getActivity().startActivity(intent);
							getActivity().finish();
						}
					}else {		
						if(GpsStatus.equalsIgnoreCase("on")){
							Toast.makeText(getActivity(), "No users available", Toast.LENGTH_SHORT).show();
							initializeMap();
							new DownloadImageTask().execute();
						}else{
							Toast.makeText(getActivity(), "Please Switch ON GPS Satellites", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
							getActivity().startActivity(intent);
							getActivity().finish();
						}
					}
				}else if(RespValue==Constants.SEARCH_BLIND_KISS_RESPONSE){
					jObjServerResp = new JSONObject(serverResp);
					statusCode = jObjServerResp.getString(Constants.STATUS_CODE);
					if(statusCode.contentEquals("236")){
						String result=jObjServerResp.getString(Constants.USER_DETAILS);
						JSONArray jObj = null;
						jObj = new JSONArray(result);
						for(int i=0;i<jObj.length();i++){
							BlindBean objContact = new BlindBean();
							JSONObject jObjUser;
							jObjUser = jObj.getJSONObject(i);
							String name=jObjUser.getString("username");
							String profileImage=jObjUser.getString("prof_image_path");
							String userId=jObjUser.getString("authid");
							String lat=jObjUser.getString("latitude");
							String lang=jObjUser.getString("langitude");
							objContact.setName(name);
							objContact.setProfile_Image(profileImage);
							objContact.setToAuthId(userId);
							objContact.setLatTo(lat);
							objContact.setLangTo(lang);
							latlongSearch.add(objContact);
						}
						searchResult=true;
						if(GpsStatus.equalsIgnoreCase("on")){
							googleMap.clear();
							initializeMap();
							new DownloadImageTask().execute();
						}else{
							Toast.makeText(getActivity(), "Please Switch ON GPS Satellites", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
							getActivity().startActivity(intent);
							getActivity().finish();
						}
					}else{
						address.setText(" ");
						Toast.makeText(getActivity(), "No users available", Toast.LENGTH_SHORT).show();
						initializeMap();
						new DownloadImageTask().execute();
						
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}