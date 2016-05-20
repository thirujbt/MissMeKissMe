package com.missme.kissme.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHandler extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "UserProfile.db";
	public static final String PROFILE_TABLE_NAME = "profile";
	public static final String PROFILE_COLUMN_FIRST_NAME = "first_name";
	public static final String PROFILE_COLUMN_LAST_NAME = "last_name";
	public static final String PROFILE_COLUMN_PHONE_NO = "phone_no";
	public static final String PROFILE_COLUMN_EMAIL_ID = "email_id";
	public static final String PROFILE_COLUMN_GENDER = "gender";
	public static final String PROFILE_COLUMN_DOB = "dob";
	public static final String PROFILE_COLUMN_STATE = "state";
	public static final String PROFILE_COLUMN_VAULTCODE = "vault_code";
	public static final String PROFILE_COLUMN_PHOTO = "photo";
	public static final String PROFILE_COLUMN_COVER_PHOTO = "cover_photo";
	public static final String OPT_BLIND = "opt_blind";
	public static final int DATABASE_VERSION = 1;

	public DBHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + PROFILE_TABLE_NAME + " ("
				+ PROFILE_COLUMN_FIRST_NAME + " text, "
				+ PROFILE_COLUMN_LAST_NAME + " text, "
				+ PROFILE_COLUMN_PHONE_NO + " text, "
				+ PROFILE_COLUMN_EMAIL_ID + " text, "
				+ PROFILE_COLUMN_GENDER + " text, "
				+ PROFILE_COLUMN_DOB + " text, "
				+ PROFILE_COLUMN_STATE + " text, "
				+ PROFILE_COLUMN_VAULTCODE + " text, "
				+ PROFILE_COLUMN_PHOTO + " text, "
				+ PROFILE_COLUMN_COVER_PHOTO + " text, "
				+ OPT_BLIND + " text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+PROFILE_TABLE_NAME);
		onCreate(db);
	}

	public void deleteProfile() {
		SQLiteDatabase database = this.getWritableDatabase();
		database.delete(PROFILE_TABLE_NAME, null, null);

	}

	public boolean updateProfile(String firstName, String lastName,
			String phoneNo, String emailId, String gender, String dob,
			String state, String vaultcode, String photo, String CoverPhoto, String optBlind) {

		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(PROFILE_COLUMN_FIRST_NAME, firstName);
		contentValues.put(PROFILE_COLUMN_LAST_NAME, lastName);
		contentValues.put(PROFILE_COLUMN_PHONE_NO, phoneNo);
		contentValues.put(PROFILE_COLUMN_EMAIL_ID, emailId);
		contentValues.put(PROFILE_COLUMN_GENDER, gender);
		contentValues.put(PROFILE_COLUMN_DOB, dob);
		contentValues.put(PROFILE_COLUMN_STATE, state);
		contentValues.put(PROFILE_COLUMN_VAULTCODE, vaultcode);
		contentValues.put(PROFILE_COLUMN_PHOTO, photo);
		contentValues.put(PROFILE_COLUMN_COVER_PHOTO, CoverPhoto);
		contentValues.put(OPT_BLIND, optBlind);
		database.update(PROFILE_TABLE_NAME, contentValues, null, null);
		return true;
	}

	public boolean updateProfileEdit(String firstName,
			String phoneNo, String emailId, String gender, String dob,
			String vaultcode, String optBlind) {

		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(PROFILE_COLUMN_FIRST_NAME, firstName);
		contentValues.put(PROFILE_COLUMN_PHONE_NO, phoneNo);
		contentValues.put(PROFILE_COLUMN_EMAIL_ID, emailId);
		contentValues.put(PROFILE_COLUMN_GENDER, gender);
		contentValues.put(PROFILE_COLUMN_DOB, dob);
		contentValues.put(PROFILE_COLUMN_VAULTCODE, vaultcode);
		contentValues.put(OPT_BLIND, optBlind);
		database.update(PROFILE_TABLE_NAME, contentValues, null, null);
		return true;
	}

	public boolean updateProfileImage(String photo) {

		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(PROFILE_COLUMN_PHOTO, photo);
		database.update(PROFILE_TABLE_NAME, contentValues, null, null);
		return true;
	}
	public boolean updateProfileCoverImage(String coverPhoto) {

		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(PROFILE_COLUMN_COVER_PHOTO, coverPhoto);
		database.update(PROFILE_TABLE_NAME, contentValues, null, null);
		return true;
	}

	public boolean updateVaultCode(String vaultCode) {

		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(PROFILE_COLUMN_VAULTCODE, vaultCode);
		database.update(PROFILE_TABLE_NAME, contentValues, null, null);
		return true;
	}

	public boolean insertProfile(String firstName, String lastName,
			String phoneNo, String emailId, String gender, String dob,
			String state, String vaultcode, String photo, String CoverPhoto,String optBlind) {

		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(PROFILE_COLUMN_FIRST_NAME, firstName);
		contentValues.put(PROFILE_COLUMN_LAST_NAME, lastName);
		contentValues.put(PROFILE_COLUMN_PHONE_NO, phoneNo);
		contentValues.put(PROFILE_COLUMN_EMAIL_ID, emailId);
		contentValues.put(PROFILE_COLUMN_GENDER, gender);
		contentValues.put(PROFILE_COLUMN_DOB, dob);
		contentValues.put(PROFILE_COLUMN_STATE, state);
		contentValues.put(PROFILE_COLUMN_VAULTCODE, vaultcode);
		contentValues.put(PROFILE_COLUMN_PHOTO, photo);
		contentValues.put(PROFILE_COLUMN_COVER_PHOTO, CoverPhoto);
		contentValues.put(OPT_BLIND, optBlind);
		database.insert(PROFILE_TABLE_NAME, null, contentValues);
		return true;
	}

	public Cursor getProfile() {
		Cursor cursor=null;
		try{
			if(this!=null){
				SQLiteDatabase database = this.getReadableDatabase();
				cursor = database.rawQuery("select * from " + PROFILE_TABLE_NAME, null);
			}
		}
		catch(Exception ex){}
		return cursor;
	}
}