package com.missme.kissme.MenuItems;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
//import java.net.MalformedURLException;
//import java.net.URL;
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.List;
import org.w3c.dom.Document;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
//import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/*import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.android.Facebook;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;*/
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.missme.kissme.MainFragmentMenu;
import com.missme.kissme.R;
import com.missme.kissme.DataBase.DBHandler;
import com.missme.kissme.MapService.GMapV2Direction;
import com.missme.kissme.Utils.Constants;
import com.missme.kissme.Utils.GPSLocation;
import com.missme.kissme.Utils.ImageLoader1;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ViewPathFrag extends Fragment implements LocationListener {

	ImageView backIcon, userImage, toUserImage, ownUserImage, share;
	LinearLayout layout1, layout2;
	TextView name;
	GPSLocation gps;
	private GoogleMap googleMap;
	MarkerOptions marker;
	Drawable ploter;
	Context mContext;
	double lat = 0.0;
	double lang = 0.0;
	double lattFrom = 0.0;
	double langiFrom = 0.0;
	LatLng start;
	Location location;
	LatLng fromPosition;
	LatLng toPosition;
	GMapV2Direction md;
	ActionBar bar;
	String toUserProfImage, latFrom, langFrom, latTo, langTo;
	Bitmap bitmap, bitmap_own;
	SharedPreferences pref;
	String profImage, toUserName, ownProfImage;
	DisplayImageOptions options;
	public ImageLoader1 imageLoader;
	RelativeLayout layout, layout_own;
	DBHandler dbHandler;
	//Session fbSession;
	//private static String APP_ID = "736038893167607";
//	Facebook facebook;
//	LoginButton authButton;
	/*String fullName, firstName, fblastName, fbdob, fbgender, email,
	fb_prof_image_path, phoneNo, fbState, fbCountry, fbPssword,
	fbuserId;*/

	//List<String> permissions/*=new ArrayList<String>()*/;


/*	public ViewPathFrag() {
		
		permissions = Arrays.asList("public_profile", "email", "user_birthday",
				"user_location", "user_about_me");
	}
*/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.user_default_image)
		.showImageForEmptyUri(R.drawable.user_default_image)
		.showImageOnFail(R.drawable.user_default_image)
		.cacheOnDisk(true).considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565).build();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_track_kiss, container,
				false);
		pref = getActivity().getSharedPreferences(
				Constants.MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
		gps = new GPSLocation(getActivity());
		dbHandler = new DBHandler(getActivity());
		backIcon = (ImageView) view.findViewById(R.id.track_back_icon);
		userImage = (ImageView) view.findViewById(R.id.track_kiss_image);
		toUserImage = (ImageView) view.findViewById(R.id.ploter_user_image);
		ownUserImage = (ImageView) view.findViewById(R.id.ploter_user_imag_own);
		name = (TextView) view.findViewById(R.id.trackKiss_txt_name);
		layout1 = (LinearLayout) view.findViewById(R.id.map_newkiss_header1);
		layout2 = (LinearLayout) view.findViewById(R.id.map_newkiss_header2);
		share = (ImageView) view.findViewById(R.id.fb_share_image);
		TextView titlee = (TextView) view.findViewById(R.id.newKiss_txt_track);
		titlee.setText("View Path");
		TextView titlBar = (TextView) view
				.findViewById(R.id.trackKiss_txt_name);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(),
				"SourceSansPro-Regular.otf");
		titlee.setTypeface(font);
		titlBar.setTypeface(font);
		layout1.setVisibility(View.GONE);
		layout2.setVisibility(View.GONE);
		getDataFromDB();
		layout = (RelativeLayout) view.findViewById(R.id.relative_ploter);
		layout_own = (RelativeLayout) view
				.findViewById(R.id.relative_ploter_own);
		profImage = pref.getString(Constants.VAULT_USER_PROFIMAGE_PREF, null);
		toUserName = pref.getString(Constants.VAULT_USER_NAME_PREF, null);
		latFrom = pref.getString(Constants.LAT_FROM_PREF, null);
		langFrom = pref.getString(Constants.LANG_FROM_PREF, null);
		latTo = pref.getString(Constants.LAT_TO_PREF, null);
		langTo = pref.getString(Constants.LANG_TO_PREF, null);
		lattFrom = Double.parseDouble(latFrom);
		langiFrom = Double.parseDouble(langFrom);
		lat = Double.parseDouble(latTo);
		lang = Double.parseDouble(langTo);
		name.setText(toUserName);
		loadProfilImage();
		loadPlatreProfilImage();
		loadPlatreOwnProfilImage();
/*
		authButton = (LoginButton)view.findViewById(R.id.login_button);
		authButton.setReadPermissions(permissions);*/

/*
		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		}
*/
		backIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						MainFragmentMenu.class);
				intent.putExtra("homepage", "vaultkissthird");
				getActivity().startActivity(intent);
				getActivity().finish();
			}
		});
		share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// captureScreen();
				dialog();
			}
		});
		return view;
	}

	/*private void onSessionStateChange(final Session session,
			SessionState state, Exception exception) {
		if (state.isOpened()) {
			String token = session.getAccessToken();
			Request request = Request.newMeRequest(session,
					new Request.GraphUserCallback() {

				@Override
				public void onCompleted(GraphUser user,
						Response response) {
					if (session == Session.getActiveSession()) {
						if (user != null) {
							fbuserId = user.getId();
							Log.i("FBuserID", fbuserId);
							if (user.getName() != null) {
								fullName = user.getName();
							}
							if (user.getFirstName() != null) {
								firstName = user.getFirstName();
							}
							if (user.getLastName() != null) {
								fblastName = user.getLastName();
							}
							if (user.getBirthday() != null) {
								fbdob = user.getBirthday();
							}
							if (user.asMap().get("gender").toString() != null) {
								fbgender = user.asMap().get("gender")
										.toString();

							}
							if ((String) user.getProperty("email") != null) {
								email = (String) user
										.getProperty("email");
							}

							Log.i("FBuserEmail", email);
							Log.i("FBuserFirstname", firstName);

							try {
								URL imgUrl = new URL(
										"http://graph.facebook.com/"
												+ user.getId()
												+ "/picture?type=large");
								fb_prof_image_path = imgUrl.toString();
							} catch (MalformedURLException e) {
								e.printStackTrace();
							}
							// FBLoginAPI();
						}
					}
				}
			});
			Request.executeBatchAsync(request);
			Log.i("Facebook Login", "Logged in...");
		} else if (state.isClosed()) {
			Log.i("Facebook Logout", "Logged out...");
			email = "";
			String token = "";

		}
	}*/

	/*@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Session.getActiveSession().onActivityResult(getActivity(), requestCode,
				resultCode, data);

	}*/

	private void getDataFromDB() {
		// Get from DB
		try {
			Cursor cursor = dbHandler.getProfile();
			if (cursor.moveToFirst()) {
				do {
					ownProfImage = cursor.getString(cursor
							.getColumnIndex(DBHandler.PROFILE_COLUMN_PHOTO));
					if (!cursor.isClosed()) {
						cursor.close();
					}
				} while (cursor.moveToNext());
			}
			cursor.close();
			dbHandler.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		try {
			initializeMap();
			new DownloadImageTask().execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void share(String nameApp, String imagePath, String message) {
		try {
			List<Intent> targetedShareIntents = new ArrayList<Intent>();
			Intent share = new Intent(android.content.Intent.ACTION_SEND);
			share.setType("image/jpeg");
			List<ResolveInfo> resInfo = getActivity().getPackageManager()
					.queryIntentActivities(share, 0);
			if (!resInfo.isEmpty()) {
				for (ResolveInfo info : resInfo) {
					Intent targetedShare = new Intent(
							android.content.Intent.ACTION_SEND);
					targetedShare.setType("image/jpeg"); // put here your mime
					// type
					if (info.activityInfo.packageName.toLowerCase().contains(
							nameApp)
							|| info.activityInfo.name.toLowerCase().contains(
									nameApp)) {
						targetedShare.putExtra(Intent.EXTRA_SUBJECT,
								"Miss Me Kiss Me");
						targetedShare.putExtra(Intent.EXTRA_TEXT, message);
						targetedShare.putExtra(Intent.EXTRA_STREAM,
								Uri.fromFile(new File(imagePath)));
						targetedShare.setPackage(info.activityInfo.packageName);
						targetedShareIntents.add(targetedShare);
					}
				}
				Intent chooserIntent = Intent.createChooser(
						targetedShareIntents.remove(0), "Select app to share");
				chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
						targetedShareIntents.toArray(new Parcelable[] {}));
				startActivity(chooserIntent);
			}
		} catch (Exception e) {
		}
	}

	public void initializeMap() {
		// Getting Google Play availability status
		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getActivity());
		// Showing status
		if (status != ConnectionResult.SUCCESS) { // Google Play Services are
			// not available
			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status,
					getActivity(), requestCode);
			dialog.show();
		} else { // Google Play Services are available
			// Getting reference to the SupportMapFragment of activity_main.xml
			SupportMapFragment fm = (SupportMapFragment) getChildFragmentManager()
					.findFragmentById(R.id.map_track_page);
			// Getting GoogleMap object from the fragment
			googleMap = fm.getMap();
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			md = new GMapV2Direction();
			fromPosition = new LatLng(lattFrom, langiFrom);
			toPosition = new LatLng(lat, lang);
			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}
			googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
					fromPosition, 15));
		}
	}

	public void captureScreen() {
		SnapshotReadyCallback callback = new SnapshotReadyCallback() {
			@Override
			public void onSnapshotReady(Bitmap snapshot) {
				bitmap = snapshot;
				String mPath = Environment.getExternalStorageDirectory()
						.toString() + "/" + "viewpath" + ".jpg";
				try {
					FileOutputStream outputStream = new FileOutputStream(mPath);
					int quality = 100;
					bitmap.compress(Bitmap.CompressFormat.JPEG, quality,
							outputStream);
					outputStream.flush();
					outputStream.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
				}
				String msg = "Sends real time kisses by boat, plane, train, car, bike, or on foot! Miss Me Kiss Me brings people closer together. So, we ask you,"
						+ "Who do you miss? We encourage you to take advantage of this new app and send your first kiss today! \n http://missmekissme.com/";
				share("facebook", mPath, msg);
			}
		};
		googleMap.snapshot(callback);
	}

	public void loadProfilImage() {

		ImageLoader.getInstance().displayImage(profImage, userImage, options,
				new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
			}

			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
			}

			@Override
			public void onLoadingComplete(final String imageUri,
					View view, final Bitmap loadedImage) {
				final ImageView imageView = (ImageView) view;
				ViewTreeObserver observerProfileImg = imageView
						.getViewTreeObserver();
				observerProfileImg
				.addOnPreDrawListener(new OnPreDrawListener() {
					@Override
					public boolean onPreDraw() {
						imageView.getViewTreeObserver()
						.removeOnPreDrawListener(this);
						if (imageUri != null
								&& !imageUri.equals("null")
								&& loadedImage != null
								&& !imageUri
								.equalsIgnoreCase("")) {
							Bitmap bitmap = ImageLoader1
									.getCroppedBitmap(
											loadedImage,
											imageView
											.getWidth());
							imageView.setImageBitmap(bitmap);
						}
						return true;
					}
				});
			}
		});
	}

	public void loadPlatreProfilImage() {

		ImageLoader.getInstance().displayImage(profImage, toUserImage, options,
				new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
			}

			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
			}

			@Override
			public void onLoadingComplete(final String imageUri,
					View view, final Bitmap loadedImage) {
				final ImageView imageView = (ImageView) view;
				ViewTreeObserver observerProfileImg = imageView
						.getViewTreeObserver();
				observerProfileImg
				.addOnPreDrawListener(new OnPreDrawListener() {

					@Override
					public boolean onPreDraw() {
						imageView.getViewTreeObserver()
						.removeOnPreDrawListener(this);
						if (imageUri != null
								&& !imageUri.equals("null")
								&& loadedImage != null
								&& !imageUri
								.equalsIgnoreCase("")) {
							Bitmap bitmap = ImageLoader1
									.getCroppedBitmap(
											loadedImage,
											imageView
											.getWidth());
							imageView.setImageBitmap(bitmap);
						}
						return true;
					}
				});
			}
		});
	}

	public void loadPlatreOwnProfilImage() {

		ImageLoader.getInstance().displayImage(ownProfImage, ownUserImage,
				options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
			}

			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
			}

			@Override
			public void onLoadingComplete(final String imageUri,
					View view, final Bitmap loadedImage) {
				final ImageView imageView = (ImageView) view;
				ViewTreeObserver observerProfileImg = imageView
						.getViewTreeObserver();
				observerProfileImg
				.addOnPreDrawListener(new OnPreDrawListener() {

					@Override
					public boolean onPreDraw() {
						imageView.getViewTreeObserver()
						.removeOnPreDrawListener(this);
						if (imageUri != null
								&& !imageUri.equals("null")
								&& loadedImage != null
								&& !imageUri
								.equalsIgnoreCase("")) {
							Bitmap bitmap = ImageLoader1
									.getCroppedBitmap(
											loadedImage,
											imageView
											.getWidth());
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
			// convertBitmap image
			try {
				Thread.sleep(2000);
				layout.setDrawingCacheEnabled(true);
				layout.measure(
						MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
						MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
				layout.layout(0, 0, layout.getMeasuredWidth(),
						layout.getMeasuredHeight());
				layout.buildDrawingCache(true);
				bitmap = Bitmap.createBitmap(layout.getDrawingCache());
				layout.setDrawingCacheEnabled(false);
				layout_own.setDrawingCacheEnabled(true);
				layout_own
				.measure(MeasureSpec.makeMeasureSpec(0,
						MeasureSpec.UNSPECIFIED), MeasureSpec
						.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
				layout_own.layout(0, 0, layout_own.getMeasuredWidth(),
						layout_own.getMeasuredHeight());
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
		googleMap.clear();
		googleMap.addMarker(new MarkerOptions().position(fromPosition)
				.title("Current Location")
				.icon(BitmapDescriptorFactory.fromBitmap(bitmap_own)));
		googleMap.addMarker(new MarkerOptions().position(toPosition)
				.title("Desitnation")
				.icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
		Document doc = md.getDocument(fromPosition, toPosition,
				GMapV2Direction.MODE_DRIVING);
		ArrayList<LatLng> directionPoint = md.getDirection(doc);
		PolylineOptions rectLine = new PolylineOptions().width(8).color(
				Color.RED);
		for (int i = 0; i < directionPoint.size(); i++) {
			rectLine.add(directionPoint.get(i));
		}
		googleMap.addPolyline(rectLine);
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

	void dialog() {
		Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.fb_login);
		dialog.setTitle("Title...");

		// set the custom dialog components - text, image and button


		dialog.show();
	}
}