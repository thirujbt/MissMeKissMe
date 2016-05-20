package com.missme.kissme.MenuItems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.w3c.dom.Document;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.missme.kissme.MainFragmentMenu;
import com.missme.kissme.R;
import com.missme.kissme.AccountManager.LoginActivity;
import com.missme.kissme.MapService.GMapV2Direction;
import com.missme.kissme.Utils.Constants;
import com.missme.kissme.Utils.GPSLocation;
import com.missme.kissme.Utils.ImageLoader1;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class TrackKissFrag extends Fragment  implements LocationListener{

	ImageView backIcon,userImage,toUserImage,ownUserImage,fbshare;
	TextView name,milesTxt,TimeTxt,deliveryTxt;
	GPSLocation gps;
	private GoogleMap googleMap;
	MarkerOptions marker;
	Drawable ploter;
	Context mContext;
	double lat=0.0;
	double lang=0.0;
	LatLng start;
	//GoogleMap googleMap;
	Location location;
	LatLng fromPosition;
	LatLng toPosition;
	GMapV2Direction  md;
	ActionBar bar;
	Bitmap bitmap,bitmap_own;
	SharedPreferences pref,pref1;
	String profImage,toUserName,ownProfImage,vechicle,msgID,remainTime,ownName;
	DisplayImageOptions options;
	public ImageLoader1 imageLoader;
	RelativeLayout layout,layout_own;
	LinearLayout layout1,layout2;
	public static int directionSize;
	Editor editor;
	LatLng moveLatLang;
	public static int count=0;
	String gcmid;
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
		View view = inflater.inflate(R.layout.fragment_track_kiss, container,false);
		pref = getActivity().getSharedPreferences(Constants.REAL_MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
		pref1 = getActivity().getSharedPreferences(Constants.MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
		gps=new GPSLocation(getActivity());
		editor = pref.edit();
		backIcon=(ImageView)view.findViewById(R.id.track_back_icon);
		userImage=(ImageView)view.findViewById(R.id.track_kiss_image);
		toUserImage=(ImageView)view.findViewById(R.id.ploter_user_image);
		ownUserImage=(ImageView)view.findViewById(R.id.ploter_user_imag_own);
		name=(TextView)view.findViewById(R.id.trackKiss_txt_name);
		milesTxt=(TextView)view.findViewById(R.id.tracking_miles);
		TimeTxt=(TextView)view.findViewById(R.id.tracking_hours);
		deliveryTxt=(TextView)view.findViewById(R.id.realnewKiss_txt_notifyreach1);
		layout2=(LinearLayout)view.findViewById(R.id.map_newkiss_header2);
		fbshare=(ImageView)view.findViewById(R.id.fb_share_image);		
		TextView titlee=(TextView)view.findViewById(R.id.newKiss_txt_track);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"SourceSansPro-Regular.otf");
		titlee.setTypeface(font);
		name.setTypeface(font);		
		fbshare.setVisibility(View.GONE);
		layout2.setVisibility(View.GONE);
		layout = (RelativeLayout)view.findViewById(R.id.relative_ploter);
		layout_own = (RelativeLayout)view.findViewById(R.id.relative_ploter_own);
		String lati=pref.getString(Constants.REAL_TO_USER_LAT_PREF, null);
		String langi=pref.getString(Constants.REAL_TO_USER_LANG_PREF, null);
		profImage=pref.getString(Constants.REAL_PROFILE_BASE_PREF, null);
		ownProfImage=pref.getString(Constants.PROFILE_OWN_PREF, null);
		toUserName=pref.getString(Constants.REAL_TO_USER_NAME_PREF, null);
		vechicle=pref.getString(Constants.REAL_TRAVEL_TYPE_PREF, null);
		ownName=pref1.getString(Constants.OWN_NAME_PREF, null);
		msgID=pref.getString("sentmsgid", null);
		gcmid = pref1.getString("registration_id", null);
		try{
			lat=Double.parseDouble(lati);
			lang=Double.parseDouble(langi);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		name.setText(toUserName);
		loadProfilImage();
		loadPlatreProfilImage();
		loadPlatreOwnProfilImage();
		backIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stop();
				Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
				getActivity().startActivity(intent);
				getActivity().finish();

			}
		});
		try{
			initializeMap();
			new DownloadImageTask().execute();
			mTimer = new Timer();
			mTimer.schedule(timerTask, 0, 1000);
		}catch(Exception e){
			e.printStackTrace();
		}
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	private Timer mTimer;
	TimerTask timerTask = new TimerTask() {
		public void run(){
			try {
				
				/*$msg_id     =$data->{"msg_id"};

				$authkey    = $data->{"authkey"};
				$iosRegID    = $data->{"ios_regid"};
				$gcmid    = $data->{"gcmid"};
				$session_name    = $data->{"session_name"};*/
			
				JSONObject jsonObject = new JSONObject();
				String url=Constants.TRAKING_KISS_URL;
			
				jsonObject.accumulate("msg_id",msgID);
				//jsonObject.accumulate("gcmid ",gcmid);
				if(LoginActivity.getSessionname(getActivity())!=null){
					jsonObject.accumulate(Constants.SESSION_NAME,LoginActivity.getSessionname(getActivity()));
					}
				System.out.println("checking request.........."+jsonObject);
				new GetXMLTask(url).execute(jsonObject);
			} catch (Exception e) {
			}
		}
	};
	public void stop(){
		if(timerTask!=null){
			timerTask.cancel();
			mTimer.cancel();
			mTimer.purge();
		}
	}
	private class GetXMLTask extends AsyncTask<JSONObject, Void, String>{
		String url;
		public GetXMLTask(String url){
			this.url = url;
		}
		@Override
		protected String doInBackground(JSONObject... jsonObj) {
			synchronized(this){
				System.gc();
				InputStream inputStream = null;
				String result = "";
				String json = "";
				try {
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost(url);
					json = jsonObj[0].toString();
					StringEntity se = new StringEntity(json);
					httpPost.setEntity(se);
					HttpResponse httpResponse = httpclient.execute(httpPost);
					inputStream = httpResponse.getEntity().getContent();
					if(inputStream != null)
						result = convertInputStreamToString(inputStream);
					else
						result = "Did not work!";

				} catch (Exception e) {
				}
				return result;
			}
		}   
		@Override
		protected void onPostExecute(String result) {
			
			System.out.println("Trackkiss_response------ "+result);
			try {
				if(result!=null){
					String statusCode;
					JSONObject jsonObjects = new JSONObject(result);
					statusCode = jsonObjects.getString(Constants.STATUS_CODE);
					switch(statusCode){
					case Constants.TRAKING_SUCCESS:
						remainTime=jsonObjects.getString("remaining_time");
						String miles=jsonObjects.getString("kilo");
						String hours=jsonObjects.getString("hours");
						String sentStatus=jsonObjects.getString("send_status");
						String logStatus=jsonObjects.getString("log_status");
						String lat=jsonObjects.getString("lat");
						String lang=jsonObjects.getString("lng");
						if(lat.equalsIgnoreCase("null") && lang.equalsIgnoreCase("null")){
							lat=String.valueOf(gps.getLatitude());
							lang=String.valueOf(gps.getLongitude());
						}
						if(lat.equalsIgnoreCase("") && lang.equalsIgnoreCase("")){
							lat=String.valueOf(gps.getLatitude());
							lang=String.valueOf(gps.getLongitude());
						}
						layout2.setVisibility(View.VISIBLE);
						milesTxt.setText(miles);
						TimeTxt.setText(hours);
						deliveryTxt.setText(remainTime);
						if(sentStatus.equalsIgnoreCase("1")){
							stop();
							Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
							getActivity().startActivity(intent);
							getActivity().finish();
							Toast.makeText(getActivity(),"Message sent successfully", Toast.LENGTH_SHORT).show();
						}else if(sentStatus.equalsIgnoreCase("1") && logStatus.equalsIgnoreCase("0")){
							stop();
							Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
							getActivity().startActivity(intent);
							getActivity().finish();
							Toast.makeText(getActivity(),"Message sent successfully user does not login", Toast.LENGTH_SHORT).show();
						}
						double glat=Double.parseDouble(lat);
						double glang=Double.parseDouble(lang);
						moveLatLang=new LatLng(glat, glang);
						refreshMoveMarker();
						break;
					case Constants.TRAKING_FAILED:
						stop();
						Toast.makeText(getActivity(), "No response from server", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
						getActivity().startActivity(intent);
						getActivity().finish();
						break;
					}
				}else{
					stop();
					Toast.makeText(getActivity(), "No response from server", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
					getActivity().startActivity(intent);
					getActivity().finish();
				}
			} catch (Exception e) {
				e.printStackTrace();
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

	public void initializeMap(){
		// Getting Google Play availability status
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
		// Showing status
		if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, getActivity(), requestCode);
			dialog.show();
		}else {	// Google Play Services are available	
			// Getting reference to the SupportMapFragment of activity_main.xml
			SupportMapFragment fm = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_track_page);
			// Getting GoogleMap object from the fragment
			googleMap = fm.getMap();
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			md = new GMapV2Direction();
			fromPosition = new LatLng(gps.getLatitude(),gps.getLongitude());
			toPosition = new LatLng(lat,lang);
			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}
			googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(fromPosition, 15));
		}
	}
	public void loadProfilImage(){

		ImageLoader.getInstance().displayImage(profImage, userImage,options, new SimpleImageLoadingListener() {
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
		try{
		googleMap.clear();
		googleMap.addMarker(new MarkerOptions().position(fromPosition).title(ownName).icon(BitmapDescriptorFactory.fromBitmap(bitmap_own)));	
		googleMap.addMarker(new MarkerOptions().position(toPosition).title(toUserName).icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
		if(vechicle.equalsIgnoreCase("walk")){
			Document doc = md.getDocument(fromPosition, toPosition,GMapV2Direction.MODE_WALKING);
			ArrayList<LatLng> directionPoint = md.getDirection(doc);
			PolylineOptions rectLine = new PolylineOptions().width(8).color(Color.RED);
			directionSize=directionPoint.size();
			if(directionSize == 0){
				rectLine.add(fromPosition,toPosition);
			}else{
				for(int i = 0 ; i < directionPoint.size() ; i++) {			
					rectLine.add(directionPoint.get(i));
				}
			}
			googleMap.addPolyline(rectLine);
		}else{
			Document doc = md.getDocument(fromPosition, toPosition,GMapV2Direction.MODE_DRIVING);
			ArrayList<LatLng> directionPoint = md.getDirection(doc);
			PolylineOptions rectLine = new PolylineOptions().width(8).color(Color.RED);
			directionSize=directionPoint.size();
			if(directionSize == 0){
				rectLine.add(fromPosition,toPosition);
			}else{
				for(int i = 0 ; i < directionPoint.size() ; i++) {			
					rectLine.add(directionPoint.get(i));
				}
			}
			googleMap.addPolyline(rectLine);
		}
		}
		catch(Exception e)
		{
			
		}
	}

	public void refreshMoveMarker(){
		if (moveLatLang != null) {
			if (vechicle.equalsIgnoreCase("walk")) {
				googleMap.addMarker(new MarkerOptions().position(moveLatLang).icon(BitmapDescriptorFactory.fromResource(R.drawable.walk)));
				googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(moveLatLang, 13));
			} else if (vechicle.equalsIgnoreCase("cycle")) {
				googleMap.addMarker(new MarkerOptions().position(moveLatLang).icon(BitmapDescriptorFactory.fromResource(R.drawable.cycle)));
				googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(moveLatLang, 13));
			} else if (vechicle.equalsIgnoreCase("car")) {
				googleMap.addMarker(new MarkerOptions().position(moveLatLang).icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
				googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(moveLatLang, 13));
			} else if (vechicle.equalsIgnoreCase("train")) {
				googleMap.addMarker(new MarkerOptions().position(moveLatLang).icon(BitmapDescriptorFactory.fromResource(R.drawable.train)));
				googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(moveLatLang, 13));
			} else if (vechicle.equalsIgnoreCase("boat")) {
				googleMap.addMarker(new MarkerOptions().position(moveLatLang).icon(BitmapDescriptorFactory.fromResource(R.drawable.boat)));
				googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(moveLatLang, 13));
			} else if (vechicle.equalsIgnoreCase("plane")) {
				googleMap.addMarker(new MarkerOptions().position(moveLatLang).icon(BitmapDescriptorFactory.fromResource(R.drawable.plane)));
				googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(moveLatLang, 13));
			}
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
	public void onDestroy() {
		super.onDestroy();
		try {
			stop();	
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
}