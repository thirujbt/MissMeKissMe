package com.missme.kissme.Utils;

import java.util.HashMap;
import com.missme.kissme.AccountManager.LoginActivity;
import com.missme.kissme.DataBase.DBHandler;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {

	SharedPreferences pref;
	Editor editor;
	Context _context;
	int PRIVATE_MODE = 0;
	private static final String IS_LOGIN = "IsLoggedIn";
	public static final String KEY_PASSWORD = "password";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_USERID = "Userid";
	public static final String KEY_REMEMBER = "remember";

	// Constructor
	@SuppressLint("CommitPrefEdits")
	public SessionManager(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(Constants.MISS_ME_KISS_ME_PREF, PRIVATE_MODE);
		editor = pref.edit();
	}

	public void createLoginSession( String email,String password, String userid,boolean remember){
		editor.putBoolean(IS_LOGIN, true);
		editor.putString(KEY_PASSWORD, password);
		editor.putString(KEY_EMAIL, email);
		editor.putString(KEY_USERID, userid);
		editor.putBoolean(KEY_REMEMBER, remember);
		editor.commit();
	}	

	public void checkLogin(){
		// Check login status
		if(!this.isLoggedIn()){
			Intent i = new Intent(_context, LoginActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			_context.startActivity(i);
		}
	}

	public void checkRemember(){
		// Check login status
		if(!this.isRemember()){
			Intent i = new Intent(_context, LoginActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			_context.startActivity(i);
		}
	}

	public HashMap<String, String> getUserDetails(){

		HashMap<String, String> user = new HashMap<String, String>();
		user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
		user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
		return user;
	}

	public void logoutUser(Context context){
		// Clearing all data from Shared Preferences
		DBHandler dbhandler = new DBHandler(context);
		dbhandler.deleteProfile();
		editor.clear();
		editor.commit();

	}

	public boolean isLoggedIn(){
		return pref.getBoolean(IS_LOGIN, false);
	}

	public boolean isRemember(){
		return pref.getBoolean(KEY_REMEMBER, false);
	}
}