package com.missme.kissme.GCM;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.missme.kissme.MainFragmentMenu;
import com.missme.kissme.R;


/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends IntentService {

	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;

	public GCMIntentService() {
		super("GcmIntentService");

	}

	public static final String TAG = "GCMNotificationIntentService";

	@Override
	protected void onHandleIntent(Intent intent) {
		try{
			Bundle extras = intent.getExtras();
			GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
			String messageType = gcm.getMessageType(intent);
			String newMessage = intent.getExtras().getString(Config.EXTRA_MESSAGE);

			System.out.println("push notification checking........."+newMessage);

			String name = "";
			String msg="";

			if(newMessage != null){
				String[] StringAll;
				StringAll = newMessage.split("@@");

				int StringLength = StringAll.length;

				if (StringLength > 0) {
					if(StringLength == 1) {

					} else {
						name   = StringAll[0];
						msg    = StringAll[1];
					}
				}
			}
			if (!extras.isEmpty()) {
				if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
						.equals(messageType)) {
					sendNotification("Send error: " + extras.toString(), name);
				} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
						.equals(messageType)) {
					sendNotification("Deleted messages on server: "
							+ extras.toString(), name);
				} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
					displayMessage(this, extras.get(Config.EXTRA_MESSAGE).toString());
					sendNotification(msg,  name);
				}
			}
			GcmBroadcastReceiver.completeWakefulIntent(intent);
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	public void sendNotification(String msg, String username) {
		int icon = R.drawable.app_icon;
		Uri uri = Uri.parse("android.resource://com.missme.kissme/raw/kiss");
		if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT_WATCH){
			icon = R.drawable.notification_icon_black;
		}
		int notificatinID = 1;
		mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent myintent = new Intent(this,MainFragmentMenu.class);
		myintent.putExtra(Config.EXTRA_MESSAGE, msg);
		//Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		PendingIntent contentIntent = PendingIntent.getActivity(this, notificatinID,
				myintent, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(icon)
				.setContentTitle("MissMeKissMe")
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setAutoCancel(true)
				.setTicker(msg)
				.setContentText(msg);

		mBuilder.setContentIntent(contentIntent);
		mBuilder.setSound(uri);
		mNotificationManager.notify(notificatinID, mBuilder.build());
	}

	static void displayMessage(Context context, String message) {

		Intent intent = new Intent(Config.DISPLAY_MESSAGE_ACTION);
		intent.putExtra(Config.EXTRA_MESSAGE, message);
		context.sendBroadcast(intent);
	}
}