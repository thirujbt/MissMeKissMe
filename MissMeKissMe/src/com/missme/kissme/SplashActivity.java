package com.missme.kissme;

import com.missme.kissme.ServiceRequest.BackgroundService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				startActivity(new Intent(SplashActivity.this,MainFragmentMenu.class));
				finish();
			}
		}, 3000); 
		startServices();
	}
	/**
	 * Start BackGround Service
	 * */
	public void startServices() {
		startService(new Intent(SplashActivity.this, BackgroundService.class));
	}
}