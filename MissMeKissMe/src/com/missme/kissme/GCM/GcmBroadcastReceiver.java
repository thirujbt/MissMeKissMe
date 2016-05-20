package com.missme.kissme.GCM;

import java.util.ArrayList;
import com.missme.kissme.MainFragmentMenu;
import com.missme.kissme.R;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;

public class  GcmBroadcastReceiver extends WakefulBroadcastReceiver {
	Context ctx;

	@Override
	public void onReceive(Context context, Intent intent) {

		try{
			ctx=context;
			String newMessage = intent.getExtras().getString("message");
			String blindContact = intent.getExtras().getString("blind_contact");
			System.out.println("push notification checking broadcast........."+blindContact);
			if(newMessage !=null){
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
				showMessageNotification(ctx,msg,"MissMeKissMe");
			}
			
			if(blindContact!=null){
				String bName = "";
				String bPhone="";
				String[] StringBlindAll;
				StringBlindAll = blindContact.split("@@");
				int StringLength = StringBlindAll.length;
				if (StringLength > 0) {
					if(StringLength == 1) {
					} else {
						bName   = StringBlindAll[0];
						bPhone    = StringBlindAll[1];
					}
				}
				showContactAddNotification(ctx,bName+" accepted your kiss and he's contact saved to your phone.Check your contact list",bName);
				addContact(bName,bPhone);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void showMessageNotification(Context context,String message,String title){
		int icon = R.drawable.app_icon;
		Uri uri = Uri.parse("android.resource://com.missme.kissme/raw/kiss");
		if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT_WATCH){
			icon = R.drawable.notification_icon_black;
		}
		Intent intent = null;
		message=" "+message;
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		//Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		intent = new Intent(context,MainFragmentMenu.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
		.setSmallIcon(icon)
		.setContentTitle(title)
		.setStyle(new NotificationCompat.BigTextStyle().bigText(message))
		.setAutoCancel(true).setContentText(message).setTicker(message);
		builder.setSound(uri);
		builder.setContentIntent(pendingIntent);

		notificationManager.notify(1, builder.build());	

	}
	
	private void showContactAddNotification(Context context,String message,String title){
		int icon = R.drawable.app_icon;
		Uri uri = Uri.parse("android.resource://com.missme.kissme/raw/kiss");
		if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT_WATCH){
			icon = R.drawable.notification_icon_black;
		}
		Intent intent = null;
		message=" "+message;
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		//Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		intent = new Intent(context,MainFragmentMenu.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
		.setSmallIcon(icon)
		.setContentTitle(title)
		.setStyle(new NotificationCompat.BigTextStyle().bigText(message))
		.setAutoCancel(true).setContentText(message).setTicker(message);
		builder.setSound(uri);
		builder.setContentIntent(pendingIntent);
		notificationManager.notify(2, builder.build());	

	}
	
	public void addContact(String name,String phoneno){

		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		int rawContactInsertIndex = ops.size();

		ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
				.withValue(RawContacts.ACCOUNT_TYPE, null)
				.withValue(RawContacts.ACCOUNT_NAME, null).build());
		ops.add(ContentProviderOperation
				.newInsert(Data.CONTENT_URI)
				.withValueBackReference(Data.RAW_CONTACT_ID,rawContactInsertIndex)
				.withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
				.withValue(StructuredName.DISPLAY_NAME, name) // Name of the person
				.build());
		ops.add(ContentProviderOperation
				.newInsert(Data.CONTENT_URI)
				.withValueBackReference(
						ContactsContract.Data.RAW_CONTACT_ID,rawContactInsertIndex)
						.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
						.withValue(Phone.NUMBER, phoneno) // Number of the person
						.withValue(Phone.TYPE, Phone.TYPE_MOBILE).build()); // Type of mobile number                    
		try{
			ContentProviderResult[] res = ctx.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
			System.out.println("checking........."+res);
		}
		catch (RemoteException e){ 
			// error
		}
		catch (OperationApplicationException e){
			// error
		}       
	}
}