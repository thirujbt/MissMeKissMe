package com.missme.kissme.Utils;

public class Constants {


	//API URL

	//public static final String BASE_URL="http://temp.pickzy.com/missmekissme/webservice/";

	public static final String BASE_URL="https://missmekissme.com/webservice/";
	
	public static final String SIGN_UP_URL=Constants.BASE_URL + "registration.php";
	public static final String LOGIN_URL=Constants.BASE_URL + "login.php";
	public static final String MOBILE_VERIFY_URL=Constants.BASE_URL + "verification.php";
	public static final String MOBILE_VERIFY_RESEND_URL=Constants.BASE_URL + "verification_resend.php";
	public static final String FORGOT_PASSWORD_URL=Constants.BASE_URL + "forgotpassword.php";
	public static final String LOGOUT_URL=Constants.BASE_URL + "logout.php";
	public static final String HOME_URL=Constants.BASE_URL + "home.php";
	public static final String PROFILE_VIEW_URL=Constants.BASE_URL + "profileview.php";
	public static final String PROFILE_EDIT_URL=Constants.BASE_URL + "profileedit.php";
	public static final String PROFILE_IMAGE_URL=Constants.BASE_URL + "profileimage.php";
	public static final String CONTACT_SEND_URL=Constants.BASE_URL + "contactlist.php";
	public static final String CHANGE_PASSWORD_URL=Constants.BASE_URL + "changepassword.php";
	public static final String GET_CONTACT_LIST_URL=Constants.BASE_URL + "realtime_kiss.php";
	public static final String REALTIME_SEND_KISS_URL=Constants.BASE_URL + "send_kiss.php";
	public static final String VAULT_KISS_URL=Constants.BASE_URL + "vault_message.php";
	public static final String VAULT_DETAIL_URL=Constants.BASE_URL + "vault_detail.php";
	public static final String FLOATING_KISS_URL=Constants.BASE_URL + "travaling_kiss.php";
	public static final String BACKGROUND_SERVICE_URL=Constants.BASE_URL + "ajax_sendmsg.php";
	public static final String BACKGROUND_GPS_STATUS_SERVICE_URL=Constants.BASE_URL + "gps_latlon_update.php";
	public static final String BLIND_KISS_URL=Constants.BASE_URL + "blind_kiss.php";
	public static final String TRAKING_KISS_URL=Constants.BASE_URL + "kiss_tracking.php";
	public static final String BLIND_CONTACT_SAVE_URL=Constants.BASE_URL + "blindcontact_save.php";
	public static final String KISS_PURCHASE_SAVE_URL=Constants.BASE_URL + "purchased_kiss.php";
	public static final String KISS_SEND_CONTACT=Constants.BASE_URL + "invite_message.php";
	public static final String SET_VAULT_CODE=Constants.BASE_URL + "vault_edit.php";
	
	//Toast Messages

	public static final String NO_INTERNET="No Internet Connection.";
	public static final String INVALID_EMAIL="Please enter valid email.";
	public static final String INVALID_PASSWORD="Password must be filled.";
	public static final String INVALID_OLD_PASSWORD="Old password didn't match.";
	public static final String INVALID_PASSWORD_LENGTH="Password should contain atleast 6 characters.";
	public static final String INVALID_PASSWORD_RE_PWD="Please enter the re-type Password.";
	public static final String INVALID_PASSWORD_LENGTH_RE_PWD="Re-Type Password should contain atleast 6 characters.";
	public static final String INVALID_PASSWORD_RE="Password didn't match.";
	public static final String INVALID_CONTACT="Please enter contact number";
	public static final String INVALID_DOB="Please enter date of birth";
	public static final String INVALID_VAULT_CODE="Please enter vault code";
	public static final String INVALID_VAULT_LENGTH_CODE="Vault code should contain atleast 4 numbers";
	public static final String INVALID_GENDER_SELECTION="Please select gender.";
	public static final String INVALID_NAME="Please enter name.";
	public static final String INVALID_NAME_FORMAT="Name should not contain any number or special character.";
	public static final String FAILED_BAD_PASSWORD_OR_EMAIL="User ID or Password is incorrect.";
	public static final String FAILED_USER_EXISTS="This email is already registered.";
	public static final String SIGNUP_SUCCESS="Account is created Successfully.";
	public static final String CONTACT_EMPTY="Select any one contact";
	public static final String TYPEOF_MODE="Select kiss travelling mode";
	public static final String TYPEOF_ATTACHMENT="Attache any one";
	public static final String MSG_EMPTY="Type a message";
	public static final String TIME_DATE_INVALID="Select date and time";
	public static final String ATR_USER_ID = "user_id";
	public static final String ATR_FILE_NO = "file_no";
	public static final String ATR_DATE_CONTACTED = "date_contacted";
	public static final String ATR_DATE_INSPECTED = "date_inspected";
	public static final String ATR_RESERVE = "reserve";
	public static final String ATR_CLIENT_NAME = "client_name";
	public static final String ATR_VIDEO_FILE = "attachment";
	public static final String VIDEO_ORIENTATION = "video_mode";
	//public static final String INVITE_MESSAGE = "Check out Miss Me Kiss Me for your smartphone.Download it today and start sending kiss to your family and friends.https://play.google.com/store/apps/details?id=com.missme.kissme&hl=en";
	public static final String INVITE_MESSAGE = "You just got a invite! Download https://play.google.com/store/apps/details?id=com.missme.kissme&hl=en to send one kiss back!";
	public static final String INVITE__KISS_MESSAGE = "You just got a kiss! Download https://play.google.com/store/apps/details?id=com.missme.kissme&hl=en to send one kiss back!";
	
	//Request Rersponses

	public static final int SIGN_UP_RESP = 1000;
	public static final int LOGIN_RESPONSE = 1001;
	public static final int MOBILE_VERIFY_RESPONSE = 1002;
	public static final int FORGOT_PASSWORD_RESPONSE = 1003;
	public static final int VERIFY_CODE_RESEND_RESPONSE = 1004;
	public static final int LOGOUT_RESPONSE = 1005;
	public static final int HOMEPAGE_RESPONSE = 1006;
	public static final int PROFILEPAGE_RESPONSE = 1007;
	public static final int PROFILEEDITPAGE_RESPONSE = 1008;
	public static final int CONTACTPAGE_RESPONSE = 1009;
	public static final int GET_CONTACT_LIST_RESPONSE = 1010;
	public static final int REALTIME_KISS_SEND_RESPONSE = 1011;
	public static final int VAULT_KISS_RESPONSE = 1012;
	public static final int VAULT_READ_MESSAGE_RESPONSE = 1013;
	public static final int VAULT_DELETE_MESSAGE_RESPONSE = 1014;
	public static final int FLOATING_KISS_RESPONSE = 1015;
	public static final int GET_BLIND_CONTACT_LIST_RESPONSE = 1016;
	public static final int GET_BLIND_RESPONSE = 1017;
	public static final int SENT_TRACK_KISS_RESPONSE = 1018;
	public static final int SEARCH_BLIND_KISS_RESPONSE = 1019;
	public static final int BLIND_CONTACT_SAVE_RESPONSE = 1020;
	public static final int KISS_PURCHASE_SAVE_RESPONSE = 1021;
	public static final int PASSWORD_CHANGE_RESPONSE = 1022;
	public static final int SET_VAULT_CODE_RESPONSE = 1023;
	public static final int HOMEPAGE_REFRESH_RESPONSE = 1024;

	//Required Parameters for SignUp Page
	
	public static final String EMAIL_ID="email_id";
	public static final String PASSWORD="password";
	public static final String USERNAME="username";
	public static final String REPASSWORD="repassword";
	public static final String CONTACT="phone";
	public static final String GENDER="gender";
	public static final String GCM_ID="gcmid";
	public static final String LATITUDE="latitude";
	public static final String LONGITUDE="langitude";

	//server response

	public static final String STATUS_CODE = "status_code";
	public static final String USER_DETAILS = "results";
	public static final String STATUS = "status";
	public static final String MESSAGE = "message";
	public static final String AUTH_KEY = "authid";
	public static final String USER_KEY = "userKey";
	public static final String REQUEST_ID = "request_id";
	public static final String USER_ID = "userId";
	public static final String SUCCESS_MESSAGE_TAG = "success";
	public static final String IMAGE_URL = "imageUrl";
	public static final String EMAIL = "email";
	public static final String RESPONSE_MESSAGE = "message";
	public static final String RESPONSE_MESSAGES = "Message";

	// API response code

	public static final String LOGIN_SUCCESS = "300"; 
	public static final String MOBILE_VERIFIY ="404"; 
	public static final String LOGED_IN_OTHER_DEVICE ="405";
	public static final String INVALID_USER_ID = "402";
	public static final String INVALID_PWD = "401";
	public static final String MOBILE_VERIFIY_SUCCESS = "200";
	public static final String MOBILE_VERIFIY_FAILD = "204";
	public static final String FORGOT_PASSWORD_SUCCESS = "201";
	public static final String FORGOT_PASSWORD_FAILD = "208";
	public static final String VRIFY_RESEND_SUCCESS = "200";
	public static final String VRIFY_RESEND_FAILD = "204";
	public static final String SUCCESS = "200";
	public static final String FAILD = "404";
	public static final String PROFILE_SUCCESS = "202";
	public static final String PROFILE_FAILD = "409";
	public static final String PROFILE_EDIT_SUCCESS = "300";
	public static final String PROFILE_EDIT_FAILD = "302";
	public static final String PROFILE_IMAGE_SUCCESS = "308";
	public static final String PROFILE_IMAGE_FAILD = "309";
	public static final String PROFILE_COVER_IMAGE_SUCCESS = "307";
	public static final String PROFILE_COVER_IMAGE_FAILD = "306";
	public static final String PWD_CHANGE_SUCCESS = "308";
	public static final String PWD_CHANGE_FAILD = "309";
	public static final String NO_RECOD_FOUND_SRC = "412";
	public static final String ALREADY_REGISTERED = "409";
	public static final String INVALID_LOGIN = "409";
	public static final String IMAGE_UPLOAD_FAILED = "409";
	public static final String KISS_SENT_SUCCESS = "506";
	public static final String KISS_SENT_FAILED = "507";
	public static final String VAULT_MSG_DELETE_SUCCESS = "100";
	public static final String VAULT_MSG_DELETE_FAILED = "101";
	public static final String TRAKING_SUCCESS = "290";
	public static final String TRAKING_FAILED = "291";
	public static final String BLIND_CONTACT_ADD_SUCCESS = "403";
	public static final String BLIND_CONTACT_ADD_FAILED = "237";

	//Alert Dialog Constants
	
	public static final String ALERT_TITLE ="Alert";
	public static final String ALERT_ENABLE_GPS = "Please Switch ON GPS Satellites";
	public static final String ALERT_TITLE_CONFIRM ="Confirm Your Account";
	public static final String ALERT_TITLE_CONFIRM_PASSWORD = "Confirm Password";
	public static final String ALERT_TITLE_ALREADY_EXIST = "Email ID or Mobile number already exist.";
	/*public static final String ALERT_CONFIRM_MESSAGE ="An e-mail with a confirmation link has been sent. " +
				"Please check your e-mail and click that link to verify your e-mail ID. " +
				"click OK to continue.";*/
	public static final String ALERT_CONFIRM_MESSAGE ="An e-mail with a confirmation code has been sent. " +
			"Please check your e-mail inbox or spam to verify the code number while login. " +
			"Click OK to continue.";
	public static final String ALERT_VERIFICATION_CONFIRM_MESSAGE = "Your e-mail id is not verified. Do you want to send verification link to your e-mail id ?";
	public static final String ALERT_FORGET_PASSWORD_LINK_MSG = "The password reset link has been sent to your mail ID";
	public static final String ALERT_MSG_OTHERDEVICE_LOGGED = "You have been logged in Other Device";
	public static final String ALERT_TITLE_FORGET_PASSWORD = "Forgot Password";
	public static final String ALERT_HINT_CONFIRM_PASSWORD = "Re-type Password";
	public static final String ALERT_MSG_EXIT_APP = "Are you sure want to EXIT?";

	//Shared Preferences Constants 

	public static final String MISS_ME_KISS_ME_PREF = "MissMeKissMePref";
	public static final String REAL_MISS_ME_KISS_ME_PREF = "RealMissMeKissMePref";
	public static final String INSTANT_MISS_ME_KISS_ME_PREF = "InstantMissMeKissMePref";
	public static final String DELAY_MISS_ME_KISS_ME_PREF = "DelayMissMeKissMePref";
	public static final String BLIND_MISS_ME_KISS_ME_PREF = "BlindMissMeKissMePref";
	public static final String PROFILE_BASE_PREF = "profileimage";
	public static final String PROFILE_OWN_PREF = "profileownimage";
	public static final String PROFILE_COVER_BASE_PREF = "profileimage";
	public static final String USER_AUTHKEY_PREF = "authkey";
	public static final String USER_AUTHID_PREF = "authid";
	public static final String NEW_KISS_SELECTION_PREF = "selectiontype";
	public static final String KISS_PURCHASE_STATUS_PREF = "purchasestatus";
	public static final String KISS_LEFT_PREF = "kissleft";
	public static final String OWN_NAME_PREF = "ownname";

	//Real Time kiss 

	public static final String REAL_USER_AUTHID_PREF = "realauthid";
	public static final String REAL_TRAVEL_TYPE_PREF = "realtravel";
	public static final String REAL_ATTACHMENT_PREF = "realattachment";
	public static final String REAL_ATTACHMENT_TYPE_PREF = "realattachmenttype";
	public static final String REAL_ATTACHMENT_TYPE_NAME_PREF = "realattachmenttypename";
	public static final String REAL_MESSAGE_PREF = "realmsg";
	public static final String REAL_TO_USER_NAME_PREF = "realtouser";
	public static final String REAL_TO_USER_LAT_PREF = "realtouserlat";
	public static final String REAL_TO_USER_LANG_PREF = "realtouserlang";
	public static final String REAL_PROFILE_BASE_PREF = "realprofileimage";

	//Instant kiss 

	public static final String INSTANT_USER_AUTHID_PREF = "instantauthid";
	public static final String INSTANT_TRAVEL_TYPE_PREF = "instanttravel";
	public static final String INSTANT_ATTACHMENT_PREF = "instantattachment";
	public static final String INSTANT_ATTACHMENT_TYPE_PREF = "instantattachmenttype";
	public static final String INSTANT_ATTACHMENT_TYPE_NAME_PREF = "instantattachmenttypename";
	public static final String INSTANT_MESSAGE_PREF = "instantmsg";
	public static final String INSTANT_TO_USER_NAME_PREF = "instanttouser";
	public static final String INSTANT_TO_USER_LAT_PREF = "instanttouserlat";
	public static final String INSTANT_TO_USER_LANG_PREF = "instanttouserlang";
	public static final String INSTANT_PROFILE_BASE_PREF = "instantprofileimage";

	//Delay kiss 

	public static final String DELAY_USER_AUTHID_PREF = "delayauthid";
	public static final String DELAY_TRAVEL_TYPE_PREF = "delaytravel";
	public static final String DELAY_ATTACHMENT_PREF = "delayattachment";
	public static final String DELAY_ATTACHMENT_TYPE_PREF = "delayattachmenttype";
	public static final String DELAY_ATTACHMENT_TYPE_NAME_PREF = "delayattachmenttypename";
	public static final String DELAY_MESSAGE_PREF = "delaymsg";
	public static final String DELAY_TO_USER_NAME_PREF = "delaytouser";
	public static final String DELAY_TO_USER_LAT_PREF = "delaytouserlat";
	public static final String DELAY_TO_USER_LANG_PREF = "delaytouserlang";
	public static final String DELAY_PROFILE_BASE_PREF = "delayprofileimage";
	public static final String DELAY_TIME_PREF = "delaytime";
	public static final String DELAY_DATE_PREF = "delaydate";
	public static final String SELECTED_TIME_PREF = "selectedtime";
	public static final String SELECTED_DATE_PREF = "selecteddate";

	//Blind kiss 

	public static final String BLIND_USER_AUTHID_PREF = "blindauthid";
	public static final String BLIND_TRAVEL_TYPE_PREF = "blindtravel";
	public static final String BLIND_ATTACHMENT_PREF = "blindattachment";
	public static final String BLIND_ATTACHMENT_TYPE_PREF = "blindattachmenttype";
	public static final String BLIND_ATTACHMENT_TYPE_NAME_PREF = "blindattachmenttypename";
	public static final String BLIND_MESSAGE_PREF = "blindmsg";
	public static final String BLIND_TO_USER_NAME_PREF = "blindtouser";
	public static final String BLIND_TO_USER_LAT_PREF = "blindtouserlat";
	public static final String BLIND_TO_USER_LANG_PREF = "blindtouserlang";
	public static final String BLIND_PROFILE_BASE_PREF = "blindprofileimage";

	//Vault
	
	public static final String VAULT_CODE_PREF = "vaultcode";
	public static final String VAULT_MSG_PREF = "vaultmsg";
	public static final String VAULT_DATE_PREF = "vaultdate";
	public static final String VAULT_ATTACHMENT_PREF = "vaultattachment";
	public static final String VAULT_ATTACHMENT_NAME_PREF = "vaultattachmentname";
	public static final String VAULT_USER_NAME_PREF = "vaultusername";
	public static final String VAULT_USER_PROFIMAGE_PREF = "vaultuserprofimage";
	public static final String VAULT_USER_MILE_TRAVEL_PREF = "vaultusermilestravel";
	public static final String VAULT_USER_TIME_TRAVEL_PREF = "vaultusertimetravel";
	public static final String VAULT_USER_MSG_ID_PREF = "msgid";
	public static final String VAULT_USER_MSG_FROM_PREF = "msgfrom";
	public static final String VAULT_ATTACH_NAME_PREF = "attachname";
	public static final String VAULT_REPLY_USER_AUTHID_PREF = "replyauthid";
	public static final String VAULT_PHONE_NO_PREF = "phoneno";
	public static final String VAULT_BLIND_NO_PREF = "blindno";
	public static final String LAT_FROM_PREF = "latfrom";
	public static final String LANG_FROM_PREF = "langfrom";
	public static final String LAT_TO_PREF = "latto";
	public static final String LANG_TO_PREF = "langto";

	//Floating
	
	public static final String FLOATING_USER_NAME_PREF = "floatingusername";
	public static final String FLOATING_USER_PROFIMAGE_PREF = "floatinguserprofimage";
	public static final String FLOATING_USER_MILE_TRAVEL_PREF = "floatingusermilestravel";
	public static final String FLOATING_USER_TIME_TRAVEL_PREF = "floatingusertimetravel";
	public static final String FLOATING_LAT_FROM_PREF = "floatinglatfrom";
	public static final String FLOATING_LANG_FROM_PREF = "floatinglangfrom";
	public static final String FLOATING_LAT_TO_PREF = "floatinglatto";
	public static final String FLOATING_LANG_TO_PREF = "floatinglangto";
	public static final String FLOATING_SEND_TYPE_PREF = "sendtype";
	public static final String FLOATING_MSG_ID_PREF = "msgid";
	public static final String DEVICE_LOG_PREF = "devicelog";
	public static final String MY_PREFS_NAME="session_name";
	public static final String SESSION_NAME="session_name";
	


}