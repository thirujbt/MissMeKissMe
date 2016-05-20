package com.missme.kissme;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.missme.kissme.AccountManager.LoginActivity;
import com.missme.kissme.Adapter.HomeMenuAdapter;
import com.missme.kissme.Bean.MenuItem;
import com.missme.kissme.Bean.UserDetail;
import com.missme.kissme.DataBase.DBHandler;
import com.missme.kissme.InterfaceClass.AsyncResponse;
import com.missme.kissme.MenuItems.BlindKissFifthFrag;
import com.missme.kissme.MenuItems.BlindKissFirstFrag;
import com.missme.kissme.MenuItems.BlindKissFourthFrag;
import com.missme.kissme.MenuItems.BlindKissMainFrag;
import com.missme.kissme.MenuItems.BlindKissThirdFrag;
import com.missme.kissme.MenuItems.ContactpageFrag;
import com.missme.kissme.MenuItems.DelayedKissFifthFrag;
import com.missme.kissme.MenuItems.DelayedKissFirstFrag;
import com.missme.kissme.MenuItems.DelayedKissFourthFrag;
import com.missme.kissme.MenuItems.DelayedKissSecondFrag;
import com.missme.kissme.MenuItems.DelayedKissThirdFrag;
import com.missme.kissme.MenuItems.FloatingKissFirstFrag;
import com.missme.kissme.MenuItems.FloatingKissSecondFrag;
import com.missme.kissme.MenuItems.InstantKissFirstFrag;
import com.missme.kissme.MenuItems.InstantKissFourthFrag;
import com.missme.kissme.MenuItems.InstantKissSecondFrag;
import com.missme.kissme.MenuItems.InstantKissThirdFrag;
import com.missme.kissme.MenuItems.NewKissFragment;
import com.missme.kissme.MenuItems.ProfileEditPage;
import com.missme.kissme.MenuItems.PurchasesKissFrag;
import com.missme.kissme.MenuItems.RealTimeKissFifthFrag;
import com.missme.kissme.MenuItems.RealTimeKissFirstFrag;
import com.missme.kissme.MenuItems.RealTimeKissFourthFrag;
import com.missme.kissme.MenuItems.RealTimeKissSecondFrag;
import com.missme.kissme.MenuItems.RealTimeKissThirdFrag;
import com.missme.kissme.MenuItems.TrackKissFrag;
import com.missme.kissme.MenuItems.VaultKissFirstFrag;
import com.missme.kissme.MenuItems.VaultKissSecondFrag;
import com.missme.kissme.MenuItems.VaultKissThirdFrag;
import com.missme.kissme.MenuItems.ViewPathFrag;
import com.missme.kissme.ServiceRequest.BackgroundService;
import com.missme.kissme.ServiceRequest.GPSStatusUpdateService;
import com.missme.kissme.ServiceRequest.KissMeAsyncTask;
import com.missme.kissme.Utils.Constants;
import com.missme.kissme.Utils.SessionManager;
import com.missme.kissme.Utils.Utils;

public class MainFragmentMenu extends FragmentActivity implements OnClickListener, AsyncResponse{

	public static boolean homepage=false;
	public static boolean homePageService=false;
	private DrawerLayout drawerLayout;
	private List<MenuItem> menuItems;
	private ListView menuListView;
	Context mContext;
	private HomeMenuAdapter drawerMenuAdapter;
	String menuItemsTxt[] = { "     Refresh"," "," "," "," "," "," "," "," "," " };
	int menuItemsImg[] = {R.drawable.user_default_image,R.drawable.home_icon,R.drawable.profile_icon,R.drawable.newkiss_icon,R.drawable.blindkiss_icon,R.drawable.vault_icon,R.drawable.traveling_kisses_icon
			,R.drawable.purchases_kisses_icon,R.drawable.contacts_icon,R.drawable.logout_icon};
	private ImageView toggleImg;
	Fragment currenrFragment = null;
	RelativeLayout header;
	public static final String NEWKISSTAG = "newkisstag";
	String GpsStatus;
	public static final String REALTIMEKISSTAG = "realtimekisstag";
	public static final String REALTIMEKISSSECONDTAG = "realtimesecondkisstag";
	public static final String REALTIMEKISSTHIRDTAG = "realtimethirdkisstag";
	public static final String REALTIMEKISSFOURTHTAG = "realtimefourthkisstag";
	public static final String REALTIMEKISSFIFTHTAG = "realtimefifthkisstag";
	public static final String INSTANTKISSFIRTTAG = "instantfirstkisstag";
	public static final String INSTANTKISSSECONDTAG = "instantsecondkisstag";
	public static final String INSTANTKISSTHIRDTAG = "instantthirdkisstag";
	public static final String INSTANTKISSFOURTHTAG = "instantfourthkisstag";
	public static final String DELAYEDKISSFIRSTTAG = "delayedfirstkisstag";
	public static final String DELAYEDKISSSECONDTAG = "delayedsecondkisstag";
	public static final String DELAYEDKISSTHIRDTAG = "delayedthirdkisstag";
	public static final String DELAYEDKISSFOURTHTAG = "delayedfourthkisstag";
	public static final String DELAYEDKISSFIFTHTAG = "delayedfifthkisstag";
	public static final String TRACKKISSTAG = "trackkisstag";
	public static final String BLINDKISSTAG = "blindkisstag";
	public static final String BLINDKISSFIRSTTAG = "blindkissfirsttag";
	public static final String BLINDKISSTHIRDTAG = "blindkissthirdtag";
	public static final String BLINDKISSFOURTHTAG = "blindkissfourthtag";
	public static final String BLINDKISSFIFTHTAG = "blindkissfifthtag";
	public static final String PROFILEEDITPAGETAG = "profileeditpagetag";
	public static final String CONTACTPAGETAG = "contactpagetag";
	public static final String VAULTKISSFIRSTTAG = "vaultkissfirsttag";
	public static final String VAULTKISSSECONDTAG = "vaultkisssecondtag";
	public static final String VAULTKISSTHIRDTAG = "vaultkissthirdtag";
	public static final String FLOATINGKISSFIRSTTAG = "floatingkissfirsttag";
	public static final String FLOATINGKISSSECONDTAG = "floatingkisssecondtag";
	public static final String VIEWPATHTAG = "viewpathtag";
	public static final String PURCHASEPAGETAG = "purchasepagetag";
	String session_name;

	Fragment newkissFrag;
	Fragment realtimekissFrag;
	Fragment realtimekissSecondFrag;
	Fragment realtimekissThirdFrag;
	Fragment realtimekissFourthFrag;
	Fragment realtimekissFifthFrag;
	Fragment instantfirdtFrag;
	Fragment instantsecondFrag;
	Fragment instantthirdFrag;
	Fragment instantfourthFrag;
	Fragment delayedkissFrag;
	Fragment delayedkissSecondFrag;
	Fragment delayedkissThirdFrag;
	Fragment delayedkissFourthFrag;
	Fragment delayedkissFifthFrag;
	Fragment trackkissFrag;
	Fragment blindkissFrag;
	Fragment blindkissFirstFrag;
	Fragment blindkissThirdFrag;
	Fragment blindkissFourthFrag;
	Fragment blindkissFifthFrag;
	Fragment profileEditPageFrag;
	Fragment contactPageFrag;
	Fragment vaultKissFirstFrag;
	Fragment vaultKissSecondFrag;
	Fragment vaultKissThirdFrag;
	Fragment floatingKissFirstFrag;
	Fragment floatingKissSecondFrag;
	Fragment viewPathFrag;
	Fragment purchasePageFrag;
	FragmentTransaction transaction;
	String fragTag;
	SessionManager session;
	SharedPreferences pref;
	DBHandler dbHandler;
	List<UserDetail> userDetail = new ArrayList<UserDetail>();
	String photoPath = null;
	String opt_Blind;
	String kissLeft,purchaseStatus;
	String deviceLogStatus;
	String menuitem=null; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu_fragment);
		pref = getSharedPreferences(Constants.MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
		session = new SessionManager(MainFragmentMenu.this);
		dbHandler = new DBHandler(MainFragmentMenu.this);
		kissLeft=pref.getString(Constants.KISS_LEFT_PREF, null);
		purchaseStatus=pref.getString(Constants.KISS_PURCHASE_STATUS_PREF, null);		
		deviceLogStatus=pref.getString(Constants.DEVICE_LOG_PREF, null);
		initializeViews();

		//createNewInstance();

		getOwnUserData();
		session.checkLogin();
		if(!session.isLoggedIn()){
			finish();
		}
		startBackgroundService();
		startServices();
		// Create list for Drawer menu
		for (int i = 0; i < menuItemsTxt.length; i++) {
			MenuItem item = new MenuItem();
			item.setDrawableId(menuItemsImg[i]);
			item.setMenuItem(menuItemsTxt[i]);
			item.setProfileImage(photoPath);
			menuItems.add(item);
		}
		int width = getResources().getDisplayMetrics().widthPixels / 2;
		DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) menuListView
				.getLayoutParams();
		params.width = width;
		menuListView.setLayoutParams(params);
		drawerMenuAdapter = new HomeMenuAdapter(menuItems, this);
		menuListView.setAdapter(drawerMenuAdapter);
		menuListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onMenuItemClick(position);
			}
		});
		FragmentManager fragmentManager=getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add(R.id.content_frame, new HomePageFragment(), "HomePageFragment");
		fragmentTransaction.commit(); 
		getFragFromIntent();
		displayGpsStatus();

	}
	public void getOwnUserData(){
		try{
			userDetail = new ArrayList<UserDetail>();
			UserDetail detail = new UserDetail();
			Cursor cursor=dbHandler.getProfile();
			if (cursor.moveToFirst()) {
				do {
					photoPath = cursor.getString(cursor.getColumnIndex(DBHandler.PROFILE_COLUMN_PHOTO));
					opt_Blind = cursor.getString(cursor.getColumnIndex(DBHandler.OPT_BLIND));

				} while (cursor.moveToNext());
			}
			if(!cursor.isClosed()){
				cursor.close();
			}
			dbHandler.close();
			detail.setProfImgURL(photoPath);
			userDetail.add(detail);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void displayGpsStatus() {
		ContentResolver contentResolver = getBaseContext().getContentResolver();
		boolean gpsStatus = Settings.Secure.isLocationProviderEnabled(contentResolver, LocationManager.GPS_PROVIDER);
		if (gpsStatus) {			
			GpsStatus="on";
		} else {
			GpsStatus="off";
		}
	}	
	@Override
	protected void onResume() {
		super.onResume();
		/*if(deviceLogStatus==null){
		}else if(deviceLogStatus.equalsIgnoreCase("no")){
			Toast.makeText(getApplicationContext(), "You have been logged in Other Device", Toast.LENGTH_SHORT).show();
			//logoutUser();
			if (session == null) {
				session = new SessionManager(this);
			}
			session.logoutUser(MainFragmentMenu.this);
			stopBackgroundService();
			Intent i = new Intent(MainFragmentMenu.this,LoginActivity.class);
			startActivity(i);
			finish();	
		}*/
	}

	public void getFragFromIntent(){
		Bundle b = new Bundle();
		b = getIntent().getExtras();
		if (b != null) {
			fragTag = getIntent().getExtras().getString("homepage");
			System.out.println("checking fragment.........."+fragTag);
			homePageService=true;
			if(fragTag.equalsIgnoreCase("newkiss")){
				forwardClick(NEWKISSTAG);	
			}else if(fragTag.equalsIgnoreCase("realtimekiss")){
				forwardClick(REALTIMEKISSTAG);
			}else if(fragTag.equalsIgnoreCase("realtimesecond")){
				forwardClick(REALTIMEKISSSECONDTAG);
			}else if(fragTag.equalsIgnoreCase("realtimethird")){
				forwardClick(REALTIMEKISSTHIRDTAG);
			}else if(fragTag.equalsIgnoreCase("realtimefourth")){
				forwardClick(REALTIMEKISSFOURTHTAG);
			}else if(fragTag.equalsIgnoreCase("realtimefifth")){
				forwardClick(REALTIMEKISSFIFTHTAG);
			}else if(fragTag.equalsIgnoreCase("instantkiss")){
				forwardClick(INSTANTKISSFIRTTAG);
			}else if(fragTag.equalsIgnoreCase("instantkisssecond")){
				forwardClick(INSTANTKISSSECONDTAG);
			}else if(fragTag.equalsIgnoreCase("instantkissthird")){
				forwardClick(INSTANTKISSTHIRDTAG);
			}else if(fragTag.equalsIgnoreCase("instantkissfourth")){
				forwardClick(INSTANTKISSFOURTHTAG);
			}else if(fragTag.equalsIgnoreCase("delayedkiss")){
				forwardClick(DELAYEDKISSFIRSTTAG);
			}else if(fragTag.equalsIgnoreCase("delayedkisssecond")){
				forwardClick(DELAYEDKISSSECONDTAG);
			}else if(fragTag.equalsIgnoreCase("delayedkissthird")){
				forwardClick(DELAYEDKISSTHIRDTAG);
			}else if(fragTag.equalsIgnoreCase("delayedkissfourth")){
				forwardClick(DELAYEDKISSFOURTHTAG);
			}else if(fragTag.equalsIgnoreCase("delayedkissfifth")){
				forwardClick(DELAYEDKISSFIFTHTAG);
			}else if(fragTag.equalsIgnoreCase("trackkiss")){
				forwardClick(TRACKKISSTAG);
			}else if(fragTag.equalsIgnoreCase("blindkiss")){
				forwardClick(BLINDKISSTAG);
			}else if(fragTag.equalsIgnoreCase("blindkissfirst")){
				forwardClick(BLINDKISSFIRSTTAG);
			}else if(fragTag.equalsIgnoreCase("blindkissthird")){
				forwardClick(BLINDKISSTHIRDTAG);
			}else if(fragTag.equalsIgnoreCase("blindkissfourth")){
				forwardClick(BLINDKISSFOURTHTAG);
			}else if(fragTag.equalsIgnoreCase("blindkissfifth")){
				forwardClick(BLINDKISSFIFTHTAG);
			}else if(fragTag.equalsIgnoreCase("editprofilepage")){
				forwardClick(PROFILEEDITPAGETAG);
			}else if(fragTag.equalsIgnoreCase("contactpage")){
				forwardClick(CONTACTPAGETAG);
			}else if(fragTag.equalsIgnoreCase("vaultkissfirst")){
				forwardClick(VAULTKISSFIRSTTAG);
			}else if(fragTag.equalsIgnoreCase("vaultkisssecond")){
				forwardClick(VAULTKISSSECONDTAG);
			}else if(fragTag.equalsIgnoreCase("vaultkissthird")){
				forwardClick(VAULTKISSTHIRDTAG);
			}else if(fragTag.equalsIgnoreCase("floatingkissfirst")){
				forwardClick(FLOATINGKISSFIRSTTAG);
			}else if(fragTag.equalsIgnoreCase("floatingkisssecond")){
				forwardClick(FLOATINGKISSSECONDTAG);
			}else if(fragTag.equalsIgnoreCase("viewpath")){
				forwardClick(VIEWPATHTAG);
			}else if(fragTag.equalsIgnoreCase("purchasepage")){
				forwardClick(PURCHASEPAGETAG);
			}
		}else{
			homePageService=false;
		}
	}

	private void initializeViews(){
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		menuListView = (ListView) findViewById(R.id.left_drawer);
		toggleImg = (ImageView) findViewById(R.id.drawer_home_btn);
		header=(RelativeLayout)findViewById(R.id.header);
		toggleImg.setOnClickListener(this);
		menuItems = new ArrayList<MenuItem>();

	}
	@Override
	public void onClick(View v) {
		int id=v.getId();
		switch (id) {
		case R.id.drawer_home_btn:
			drawerToggle();
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
						if(LoginActivity.getSessionname(this)!=null){
							jsonObject.accumulate(Constants.SESSION_NAME,LoginActivity.getSessionname(this));
						}

						System.out.println("Home page....."+jsonObject);

						new KissMeAsyncTask(this, url, Constants.HOMEPAGE_RESPONSE, this).execute(jsonObject);

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(this, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
		}
	}

	public void forwardClick(String tag) {

		transaction = getSupportFragmentManager().beginTransaction();
		if(tag.equalsIgnoreCase("newkisstag")){
			header.setBackgroundColor(0XFF000000);
			transaction.replace(R.id.content_frame, new NewKissFragment() , tag);
			transaction.commit();
			if(HomePageFragment.iSHome){
				toggleImg.setVisibility(View.GONE);
				drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
			}
		}else  if(tag.equalsIgnoreCase("realtimekisstag")){
			header.setBackgroundColor(0XFF000000);
			transaction.replace(R.id.content_frame, new RealTimeKissFirstFrag(), tag);
			transaction.commit();
			toggleImg.setVisibility(View.GONE);
			drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		}else  if(tag.equalsIgnoreCase("realtimesecondkisstag")){
			header.setBackgroundColor(0XFF000000);
			transaction.replace(R.id.content_frame, new RealTimeKissSecondFrag(), tag);
			transaction.commit();
			toggleImg.setVisibility(View.GONE);
			drawerLayout.setEnabled(false);
			drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		}else  if(tag.equalsIgnoreCase("realtimethirdkisstag")){
			header.setBackgroundColor(0XFF000000);
			transaction.replace(R.id.content_frame, new RealTimeKissThirdFrag(), tag);
			transaction.commit();
			toggleImg.setVisibility(View.GONE);
			drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		}else  if(tag.equalsIgnoreCase("realtimefourthkisstag")){
			header.setBackgroundColor(0XFF000000);
			transaction.replace(R.id.content_frame, new RealTimeKissFourthFrag(), tag);
			transaction.commit();
			toggleImg.setVisibility(View.GONE);
			drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		}else  if(tag.equalsIgnoreCase("realtimefifthkisstag")){
			header.setBackgroundColor(0XFF000000);
			transaction.replace(R.id.content_frame, new RealTimeKissFifthFrag(), tag);
			transaction.commit();
			toggleImg.setVisibility(View.GONE);
			drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		}else  if(tag.equalsIgnoreCase("instantfirstkisstag")){
			header.setBackgroundColor(0XFF000000);
			transaction.replace(R.id.content_frame, new InstantKissFirstFrag(), tag);
			transaction.commit();
			toggleImg.setVisibility(View.GONE);
			drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		}else  if(tag.equalsIgnoreCase("instantsecondkisstag")){
			header.setBackgroundColor(0XFF000000);
			transaction.replace(R.id.content_frame, new InstantKissSecondFrag(), tag);
			transaction.commit();
			toggleImg.setVisibility(View.GONE);
			drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		}else  if(tag.equalsIgnoreCase("instantthirdkisstag")){
			header.setBackgroundColor(0XFF000000);
			transaction.replace(R.id.content_frame, new InstantKissThirdFrag(), tag);
			transaction.commit();
			toggleImg.setVisibility(View.GONE);
			drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		}else  if(tag.equalsIgnoreCase("instantfourthkisstag")){
			header.setBackgroundColor(0XFF000000);
			transaction.replace(R.id.content_frame, new InstantKissFourthFrag(), tag);
			transaction.commit();
			toggleImg.setVisibility(View.GONE);
			drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		}else  if(tag.equalsIgnoreCase("delayedfirstkisstag")){
			header.setBackgroundColor(0XFF000000);
			transaction.replace(R.id.content_frame, new DelayedKissFirstFrag(), tag);
			transaction.commit();
			toggleImg.setVisibility(View.GONE);
			drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		}else  if(tag.equalsIgnoreCase("delayedsecondkisstag")){
			header.setBackgroundColor(0XFF000000);
			transaction.replace(R.id.content_frame, new DelayedKissSecondFrag(), tag);
			transaction.commit();
			toggleImg.setVisibility(View.GONE);
			drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		}else  if(tag.equalsIgnoreCase("delayedthirdkisstag")){
			header.setBackgroundColor(0XFF000000);
			transaction.replace(R.id.content_frame, new DelayedKissThirdFrag(), tag);
			transaction.commit();
			toggleImg.setVisibility(View.GONE);
			drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		}else  if(tag.equalsIgnoreCase("delayedfourthkisstag")){
			header.setBackgroundColor(0XFF000000);
			transaction.replace(R.id.content_frame, new DelayedKissFourthFrag(), tag);
			transaction.commit();
			toggleImg.setVisibility(View.GONE);
			drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		}else  if(tag.equalsIgnoreCase("delayedfifthkisstag")){
			header.setBackgroundColor(0XFF000000);
			transaction.replace(R.id.content_frame, new DelayedKissFifthFrag(), tag);
			transaction.commit();
			toggleImg.setVisibility(View.GONE);
			drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		}else  if(tag.equalsIgnoreCase("trackkisstag")){
			header.setBackgroundColor(0XFF000000);
			transaction.replace(R.id.content_frame, new TrackKissFrag(), tag);
			transaction.commit();
			toggleImg.setVisibility(View.GONE);
			drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		}else  if(tag.equalsIgnoreCase("blindkisstag")){
			header.setBackgroundColor(0XFF000000);
			transaction.replace(R.id.content_frame, new BlindKissMainFrag(), tag);
			transaction.commit();
			if(HomePageFragment.iSHome){
				toggleImg.setVisibility(View.GONE);
				drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
			}
		}else  if(tag.equalsIgnoreCase("blindkissfirsttag")){
			header.setBackgroundColor(0XFF000000);
			transaction.replace(R.id.content_frame, new BlindKissFirstFrag(), tag);
			transaction.commit();
			toggleImg.setVisibility(View.GONE);
			drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		}else  if(tag.equalsIgnoreCase("blindkissthirdtag")){
			header.setBackgroundColor(0XFF000000);
			transaction.replace(R.id.content_frame, new BlindKissThirdFrag(), tag);
			transaction.commit();
			toggleImg.setVisibility(View.GONE);
			drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		}else  if(tag.equalsIgnoreCase("blindkissfourthtag")){
			header.setBackgroundColor(0XFF000000);
			transaction.replace(R.id.content_frame, new BlindKissFourthFrag(), tag);
			transaction.commit();
			toggleImg.setVisibility(View.GONE);
			drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		}else  if(tag.equalsIgnoreCase("blindkissfifthtag")){
			header.setBackgroundColor(0XFF000000);
			transaction.replace(R.id.content_frame, new BlindKissFifthFrag(), tag);
			transaction.commit();
			toggleImg.setVisibility(View.GONE);
			drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		}else  if(tag.equalsIgnoreCase("profileeditpagetag")){
			header.setBackgroundColor(0XFF000000);
			transaction.replace(R.id.content_frame, new ProfileEditPage(), tag);
			transaction.commit();
		}else  if(tag.equalsIgnoreCase("contactpagetag")){
			header.setBackgroundColor(0XFF000000);
			transaction.replace(R.id.content_frame, new ContactpageFrag(), tag);
			transaction.commit();
			if(HomePageFragment.iSHome){
				toggleImg.setVisibility(View.GONE);
				drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
			}
		}else  if(tag.equalsIgnoreCase("vaultkissfirsttag")){
			header.setBackgroundColor(0XFF000000);
			transaction.replace(R.id.content_frame,new VaultKissFirstFrag() , tag);
			transaction.commit();
			if(HomePageFragment.iSHome){
				toggleImg.setVisibility(View.GONE);
				drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
			}
		}else  if(tag.equalsIgnoreCase("vaultkisssecondtag")){
			header.setBackgroundColor(0XFF000000);
			transaction.replace(R.id.content_frame, new VaultKissSecondFrag(), tag);
			transaction.commit();
			toggleImg.setVisibility(View.GONE);
			drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		}else  if(tag.equalsIgnoreCase("vaultkissthirdtag")){
			header.setBackgroundColor(0XFF000000);
			transaction.replace(R.id.content_frame, new VaultKissThirdFrag(), tag);
			transaction.commit();
			toggleImg.setVisibility(View.GONE);
			drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		}else  if(tag.equalsIgnoreCase("floatingkissfirsttag")){
			header.setBackgroundColor(0XFF000000);
			transaction.replace(R.id.content_frame, new FloatingKissFirstFrag(), tag);
			transaction.commit();
			if(HomePageFragment.iSHome){
				toggleImg.setVisibility(View.GONE);
				drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
			}
		}else  if(tag.equalsIgnoreCase("floatingkisssecondtag")){
			header.setBackgroundColor(0XFF000000);
			transaction.replace(R.id.content_frame, new FloatingKissSecondFrag(), tag);
			transaction.commit();
			toggleImg.setVisibility(View.GONE);
			drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		}else  if(tag.equalsIgnoreCase("viewpathtag")){
			header.setBackgroundColor(0XFF000000);
			transaction.replace(R.id.content_frame,new ViewPathFrag() , tag);
			transaction.commit();
			toggleImg.setVisibility(View.GONE);
			drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		}else  if(tag.equalsIgnoreCase("purchasepagetag")){
			header.setBackgroundColor(0XFF000000);
			transaction.replace(R.id.content_frame, new PurchasesKissFrag(), tag);
			transaction.commit();
			if(HomePageFragment.iSHome){
				toggleImg.setVisibility(View.GONE);
				drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
			}
		} 
	}
	public void createNewInstance(){
		//newkissFrag = new NewKissFragment();
		//realtimekissFrag = new RealTimeKissFirstFrag();
		//realtimekissSecondFrag=new RealTimeKissSecondFrag();
		//realtimekissThirdFrag=new RealTimeKissThirdFrag();
		//realtimekissFourthFrag=new RealTimeKissFourthFrag();
		//realtimekissFifthFrag=new RealTimeKissFifthFrag();
		//instantfirdtFrag=new InstantKissFirstFrag();
		//instantsecondFrag=new InstantKissSecondFrag();
		//instantthirdFrag=new InstantKissThirdFrag();
		//instantfourthFrag=new InstantKissFourthFrag();
		//delayedkissFrag = new DelayedKissFirstFrag();
		//delayedkissSecondFrag=new DelayedKissSecondFrag();
		//delayedkissThirdFrag=new DelayedKissThirdFrag();
		//delayedkissFourthFrag=new DelayedKissFourthFrag();
		//delayedkissFifthFrag=new DelayedKissFifthFrag();
		//trackkissFrag=new TrackKissFrag();
		//blindkissFrag=new BlindKissMainFrag();
		//blindkissFirstFrag=new BlindKissFirstFrag();
		//blindkissThirdFrag=new BlindKissThirdFrag();
		//blindkissFourthFrag=new BlindKissFourthFrag();
		//blindkissFifthFrag=new BlindKissFifthFrag();
		//profileEditPageFrag=new ProfileEditPage();
		//contactPageFrag=new ContactpageFrag();
		//vaultKissFirstFrag=new VaultKissFirstFrag();
		//vaultKissSecondFrag=new VaultKissSecondFrag();
		//vaultKissThirdFrag=new VaultKissThirdFrag();
		//floatingKissFirstFrag=new FloatingKissFirstFrag();
		//floatingKissSecondFrag=new FloatingKissSecondFrag();
		//viewPathFrag=new ViewPathFrag();
		//purchasePageFrag=new PurchasesKissFrag();
	}
	private void onMenuItemClick(int position) {
		drawerLayout.closeDrawer(menuListView);
		FragmentManager fm = MainFragmentMenu.this.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Fragment fragment = null;
		switch (position) {
		case 0:

			if(fragTag == null){
				fragment = new HomePageFragment();
				homepage=true;

			}else if(fragTag.equalsIgnoreCase("realtimekiss")){
				fragment = new RealTimeKissFirstFrag();
			}else if(fragTag.equalsIgnoreCase("instantkiss")){
				fragment = new InstantKissFirstFrag();
			}else if(fragTag.equalsIgnoreCase("delayedkiss")){
				fragment = new DelayedKissFirstFrag();
			}else if(fragTag.equalsIgnoreCase("trackkiss")){
				fragment = new TrackKissFrag();
			}else if(fragTag.equalsIgnoreCase("blindkiss")){
				fragment = new BlindKissMainFrag();
			}else if(fragTag.equalsIgnoreCase("editprofilepage")){
				fragment = new ProfileEditPage();
			}else if(fragTag.equalsIgnoreCase("contactpages")){
				fragment = new ContactpageFrag();
			}else if(fragTag.equalsIgnoreCase("vaultkisssecond")){
				fragment = new VaultKissSecondFrag();
			}else if(fragTag.equalsIgnoreCase("floatingkissfirst")){
				fragment = new FloatingKissFirstFrag();
			}else if(fragTag.equalsIgnoreCase("floatingkisssecond")){
				fragment = new FloatingKissSecondFrag();
			}else if(fragTag.equalsIgnoreCase("viewpath")){
				fragment = new ViewPathFrag();
			}else if(fragTag.equalsIgnoreCase("homepages")){

				fragment = new HomePageFragment();
				homepage=true;		

			}
			break;  
		case 1:

			//menuitem="homepages";
			//sendHomeRequest();
			fragment = new HomePageFragment();
			fragTag="homepages";
			homepage=true;
			//homePageService=false;
			HomePageFragment.iSHome=false;
			header.getBackground().setAlpha(80);
			break;
		case 2:
			menuitem="editprofilepage";
			fragTag="editprofilepage";
			sendHomeRequest();

			/*fragment = new ProfileEditPage();
			header.setBackgroundColor(0XFF000000);
			HomePageFragment.iSHome=false;
			//homePageService=false;
			fragTag="editprofilepage";*/
			break; 
		case 3:
			fragTag="newkiss";
			menuitem="newkiss";
			sendHomeRequest();
			/*try{
				if(purchaseStatus==null){
					Toast.makeText(getApplicationContext(), "No response from server", Toast.LENGTH_SHORT).show();
				}else if(purchaseStatus.equalsIgnoreCase("2") && kissLeft.equalsIgnoreCase("0")){
					Toast.makeText(getApplicationContext(), "You don't have kisses.Please purchase kisses", Toast.LENGTH_SHORT).show();
				}else if(purchaseStatus.equalsIgnoreCase("0") && kissLeft.equalsIgnoreCase("0")){
					Toast.makeText(getApplicationContext(), "You don't have kisses.Please purchase kisses", Toast.LENGTH_SHORT).show();
				}else if(GpsStatus.equalsIgnoreCase("off")){
					Utils.showSettingsAlert(MainFragmentMenu.this);
				}else{
					fragment = new NewKissFragment();
					header.setBackgroundColor(0XFF000000);
					fragTag="newkiss";
					HomePageFragment.iSHome=false;
					//	homePageService=false;
				}
			}catch(Exception e){
				e.printStackTrace();
			}*/
			break; 
		case 4:
			fragTag="blindkiss";
			menuitem="blindkiss";
			sendHomeRequest();
			/*try{
				if(purchaseStatus==null && kissLeft==null){
					Toast.makeText(getApplicationContext(), "No response from server", Toast.LENGTH_SHORT).show();
				}else if(purchaseStatus.equalsIgnoreCase("2") && kissLeft.equalsIgnoreCase("0")){
					Toast.makeText(getApplicationContext(), "You don't have kisses.Please purchase kisses", Toast.LENGTH_SHORT).show();
				}else if(purchaseStatus.equalsIgnoreCase("0") && kissLeft.equalsIgnoreCase("0")){
					Toast.makeText(getApplicationContext(), "You don't have kisses.Please purchase kisses", Toast.LENGTH_SHORT).show();
				}else if(GpsStatus.equalsIgnoreCase("off")){
					Utils.showSettingsAlert(MainFragmentMenu.this);
				}else if(opt_Blind.equalsIgnoreCase("0")){
					Toast.makeText(getApplicationContext(), "Switch on blind kiss option", Toast.LENGTH_SHORT).show();
				}else{
					fragment = new BlindKissMainFrag();
					header.setBackgroundColor(0XFF000000);
					fragTag="blindkiss";	
					HomePageFragment.iSHome=false;
					//homePageService=false;
				}

			}catch(Exception e){
				e.printStackTrace();
			}*/

			break; 
		case 5:
			fragTag="vaultkissfirst";
			menuitem="vaultkissfirst";
			sendHomeRequest();
			/*fragment = new VaultKissFirstFrag();
			header.setBackgroundColor(0XFF000000);
			fragTag="vaultkissfirst";
			HomePageFragment.iSHome=false;*/
			//homePageService=false;
			break; 
		case 6:
			fragTag="floatingkissfirst";
			menuitem="floatingkissfirst";
			sendHomeRequest();
			/*
			fragment = new FloatingKissFirstFrag();
			header.setBackgroundColor(0XFF000000);
			fragTag="floatingkissfirst";
			HomePageFragment.iSHome=false;
			//homePageService=false;*/
			break; 
		case 7:
			fragTag="purchasepage";
			menuitem="purchasepage";
			sendHomeRequest();
			/*	try{
				if(purchaseStatus == null){
					Toast.makeText(getApplicationContext(), "No response from server", Toast.LENGTH_SHORT).show();
				}else if(purchaseStatus.equalsIgnoreCase("1")){
					Toast.makeText(getApplicationContext(), "You have already purchased unlimited kisses", Toast.LENGTH_SHORT).show();
				}else{
					fragment = new PurchasesKissFrag();
					header.setBackgroundColor(0XFF000000);
					fragTag="purchasepage";
					HomePageFragment.iSHome=false;
					//	homePageService=false;
				}

			}catch(Exception e){
				e.printStackTrace();
			}*/
			break; 
		case 8:
			fragTag="contactpage";
			menuitem="contactpage";
			sendHomeRequest();

			/*fragment = new ContactpageFrag();
			header.setBackgroundColor(0XFF000000);
			fragTag="contactpage";
			HomePageFragment.iSHome=false;
			//	homePageService=false;
			 */		
			break;
		case 9:
			logoutUser();
			break;
		}
		if (fragment != null) {
			currenrFragment = fragment;
			ft.replace(R.id.content_frame, fragment);
			ft.commit();
		}
	}
	private void drawerToggle() {
		if (drawerLayout.isDrawerOpen(menuListView)) {
			drawerLayout.closeDrawer(menuListView);
		} else {
			drawerLayout.openDrawer(menuListView);
		}
	}
	private void logoutUser(){
		if(isInternetOn()){
			String userid = pref.getString(Constants.USER_AUTHKEY_PREF, null);
			String url = Constants.LOGOUT_URL;
			if (userid != null) {
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.accumulate("authkey", userid);
					if(userid!=null){
						new KissMeAsyncTask(MainFragmentMenu.this, url, Constants.LOGOUT_RESPONSE, MainFragmentMenu.this).execute(jsonObject);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		} else {
			Toast.makeText(getApplicationContext(), Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
		}
	}
	/**
	 * Start BackGround Service
	 * */
	public void startServices() {
		startService(new Intent(MainFragmentMenu.this, BackgroundService.class));
	}

	public void startBackgroundService(){
		startService(new Intent(MainFragmentMenu.this, GPSStatusUpdateService.class));
	}
	public void stopBackgroundService(){
		stopService(new Intent(MainFragmentMenu.this, GPSStatusUpdateService.class));
	}
	@Override
	public void onProcessFinish(String serverResp, int RespValue) {
		String statusCode = "0";
		JSONObject jObjServerResp;
		FragmentManager fm = MainFragmentMenu.this.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Fragment fragment = null;

		if (serverResp!= null) {
			if(RespValue==Constants.LOGOUT_RESPONSE){
				try {
					jObjServerResp = new JSONObject(serverResp);
					statusCode = jObjServerResp.getString(Constants.STATUS_CODE);
					switch (statusCode) {
					case Constants.SUCCESS:
						if (session == null) {
							session = new SessionManager(this);
						}
						session.logoutUser(MainFragmentMenu.this);
						LoginActivity.clearSherePref(MainFragmentMenu.this);
						stopBackgroundService();
						Intent i = new Intent(MainFragmentMenu.this,LoginActivity.class);
						startActivity(i);
						finish();	
					case Constants.FAILD:
						break;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			else if(RespValue==Constants.HOMEPAGE_RESPONSE)
			{
				System.out.println("------------------------Inside logout............");
				try{
					jObjServerResp = new JSONObject(serverResp);
					statusCode = jObjServerResp.getString(Constants.STATUS_CODE);
					session_name=jObjServerResp.getString("session_name");
					///----------------------------///
					if(LoginActivity.getSessionname(this)!=null&&!LoginActivity.getSessionname(this).equals(session_name)){
						Toast.makeText(getApplicationContext(), "You have been logged in Other Device", Toast.LENGTH_SHORT).show();
						logoutUser();
						
					}else if(menuitem.equals("homepages")){

						fragment = new HomePageFragment();
						fragTag="homepages";
						homepage=true;
						//homePageService=false;
						HomePageFragment.iSHome=false;
						header.getBackground().setAlpha(80);

					}else if(menuitem.equals("editprofilepage")){

						fragment = new ProfileEditPage();
						header.setBackgroundColor(0XFF000000);
						HomePageFragment.iSHome=false;
						//homePageService=false;
						fragTag="editprofilepage";

					}else if(menuitem.equals("newkiss")){

						try{
							if(purchaseStatus==null){
								Toast.makeText(getApplicationContext(), "No response from server", Toast.LENGTH_SHORT).show();
							}else if(purchaseStatus.equalsIgnoreCase("2") && kissLeft.equalsIgnoreCase("0")){
								Toast.makeText(getApplicationContext(), "You don't have kisses.Please purchase kisses", Toast.LENGTH_SHORT).show();
							}else if(purchaseStatus.equalsIgnoreCase("0") && kissLeft.equalsIgnoreCase("0")){
								Toast.makeText(getApplicationContext(), "You don't have kisses.Please purchase kisses", Toast.LENGTH_SHORT).show();
							}else if(GpsStatus.equalsIgnoreCase("off")){
								Utils.showSettingsAlert(MainFragmentMenu.this);
							}else{
								fragment = new NewKissFragment();
								header.setBackgroundColor(0XFF000000);
								fragTag="newkiss";
								HomePageFragment.iSHome=false;
								//	homePageService=false;
							}
						}catch(Exception e){
							e.printStackTrace();
						}

					}else if(menuitem.equals("blindkiss")){

						try{
							if(purchaseStatus==null && kissLeft==null){
								Toast.makeText(getApplicationContext(), "No response from server", Toast.LENGTH_SHORT).show();
							}else if(purchaseStatus.equalsIgnoreCase("2") && kissLeft.equalsIgnoreCase("0")){
								Toast.makeText(getApplicationContext(), "You don't have kisses.Please purchase kisses", Toast.LENGTH_SHORT).show();
							}else if(purchaseStatus.equalsIgnoreCase("0") && kissLeft.equalsIgnoreCase("0")){
								Toast.makeText(getApplicationContext(), "You don't have kisses.Please purchase kisses", Toast.LENGTH_SHORT).show();
							}else if(GpsStatus.equalsIgnoreCase("off")){
								Utils.showSettingsAlert(MainFragmentMenu.this);
							}else if(opt_Blind.equalsIgnoreCase("0")){
								Toast.makeText(getApplicationContext(), "Switch on blind kiss option", Toast.LENGTH_SHORT).show();
							}else{
								fragment = new BlindKissMainFrag();
								header.setBackgroundColor(0XFF000000);
								fragTag="blindkiss";	
								HomePageFragment.iSHome=false;
								//homePageService=false;
							}

						}catch(Exception e){
							e.printStackTrace();
						}

					}else if(menuitem.equals("vaultkissfirst")){
						fragment = new VaultKissFirstFrag();
						header.setBackgroundColor(0XFF000000);
						fragTag="vaultkissfirst";
						HomePageFragment.iSHome=false;
						//homePageService=false;

					}else if(menuitem.equals("floatingkissfirst")){

						fragment = new FloatingKissFirstFrag();
						header.setBackgroundColor(0XFF000000);
						fragTag="floatingkissfirst";
						HomePageFragment.iSHome=false;
						//homePageService=false;

					}else if(menuitem.equals("purchasepage")){
						try{
							if(purchaseStatus == null){
								Toast.makeText(getApplicationContext(), "No response from server", Toast.LENGTH_SHORT).show();
							}else if(purchaseStatus.equalsIgnoreCase("1")){
								Toast.makeText(getApplicationContext(), "You have already purchased unlimited kisses", Toast.LENGTH_SHORT).show();
							}else{
								fragment = new PurchasesKissFrag();
								header.setBackgroundColor(0XFF000000);
								fragTag="purchasepage";
								HomePageFragment.iSHome=false;
								//	homePageService=false;
							}

						}catch(Exception e){
							e.printStackTrace();
						}

					}else if(menuitem.equals("contactpage")){

						fragment = new ContactpageFrag();
						header.setBackgroundColor(0XFF000000);
						fragTag="contactpage";
						HomePageFragment.iSHome=false;
						//	homePageService=false;
					}

					if (fragment != null) {
						currenrFragment = fragment;
						ft.replace(R.id.content_frame, fragment);
						ft.commit();
					}

				}catch (Exception e) {
					// TODO: handle exception
				}

			}
		}
	}
	//Check Internet connection
	public final boolean isInternetOn() {
		ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if(keyCode == KeyEvent.KEYCODE_BACK){
			final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setCancelable(false);
			dialog.setContentView(R.layout.alert_dialog_main);

			final EditText alertEditTxt = (EditText)dialog.findViewById(R.id.alert_edit_txt);
			final TextView alertTitleTxt = (TextView)dialog.findViewById(R.id.alert_title);
			final TextView alertTxt = (TextView)dialog.findViewById(R.id.alert_txt);
			final View view=(View)dialog.findViewById(R.id.view);

			Button okBtn = (Button) dialog.findViewById(R.id.alert_ok_btn);
			Button cancelBtn = (Button) dialog.findViewById(R.id.alert_cancel_btn); 
			alertEditTxt.setVisibility(View.INVISIBLE);
			alertTxt.setVisibility(View.VISIBLE);
			view.setVisibility(View.INVISIBLE);
			alertTitleTxt.setText(Constants.ALERT_TITLE);
			alertTxt.setText(Constants.ALERT_MSG_EXIT_APP);
			okBtn.setText("Yes");
			cancelBtn.setText("No");
			cancelBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			okBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					finish();
				}
			});
			dialog.show();
		}
		return super.onKeyDown(keyCode, event);
	}
}