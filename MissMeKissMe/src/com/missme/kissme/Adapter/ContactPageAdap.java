package com.missme.kissme.Adapter;

import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.missme.kissme.R;
import com.missme.kissme.Bean.ContactBean;
import com.missme.kissme.Utils.Constants;
import com.missme.kissme.Utils.ImageLoader1;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ContactPageAdap extends BaseAdapter {

	Context context;
	Boolean isSelect;
	String data;
	LayoutInflater  inflater;
	List<ContactBean> userDetailList;

	DisplayImageOptions options;
	String imageUrl ="";
	SharedPreferences pref;
	Editor editor;

	public ContactPageAdap(Context context, List<ContactBean> list) {

		this.context = context;
		this.userDetailList = list;
		pref = context.getSharedPreferences(Constants.MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.default_image)
		.showImageForEmptyUri(R.drawable.default_image)
		.showImageOnFail(R.drawable.default_image)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
	}

	@Override
	public int getCount() {
		return userDetailList.size();
	}

	@Override
	public Object getItem(int position) {
		return  userDetailList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	private class ViewHolder{
		TextView usernameTxt;
		ImageView profileImg;
		TextView detailsBtn;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder viewHolder;

		if(convertView == null){
			viewHolder = new ViewHolder();
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.contact_page_list, parent, false);
			viewHolder.usernameTxt = (TextView) convertView.findViewById(R.id.cont_username_home);
			viewHolder.profileImg = (ImageView) convertView.findViewById(R.id.cont_user_profile_img_home);
			viewHolder.detailsBtn = (TextView)convertView.findViewById(R.id.cont_invite_btn_home);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final ContactBean userDetailObj = userDetailList.get(position);
		String upperString=userDetailObj.getSrname();
		viewHolder.usernameTxt.setText(upperString);
		final String phoneno=userDetailObj.getSrphoneNo();

		String userID=userDetailObj.getSrInvite();
		viewHolder.detailsBtn.setText(userID);
		viewHolder.detailsBtn.setBackgroundColor(0xFF187F6A);
		imageUrl = userDetailObj.getSrprofileImg();


		ImageLoader.getInstance().displayImage(imageUrl, viewHolder.profileImg,options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
			}
			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
			}
			@SuppressWarnings("unused")
			@Override
			public void onLoadingComplete(final String imageUri, View view, final Bitmap loadedImage) {

				final ImageView imageView = (ImageView) view;
				boolean bitmapexit =false;
				if(loadedImage != null){
					bitmapexit = true;	
				}
				if(imageUrl != null){

				}
				String checkimage = imageUrl;
				ViewTreeObserver observerProfileImg = imageView.getViewTreeObserver();
				observerProfileImg.addOnPreDrawListener(new OnPreDrawListener() {
					@Override
					public boolean onPreDraw() {
						imageView.getViewTreeObserver().removeOnPreDrawListener(this);
						if(imageUri != null && ! imageUri.equals("null") && loadedImage!=null && !imageUri.equalsIgnoreCase("")){
							Bitmap bitmap = ImageLoader1.getRoundCroppedBitmap(loadedImage, imageView.getWidth());
							imageView.setImageBitmap(bitmap);
						}
						return true;
					}
				});
			}
		});

		viewHolder.detailsBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT_WATCH){
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(phoneno, null,Constants.INVITE_MESSAGE, null, null);
					Toast.makeText(context, "Invite sent.", Toast.LENGTH_SHORT).show();
				}else{
					Intent smsIntent = new Intent(Intent.ACTION_VIEW);
					smsIntent.setData(Uri.parse("smsto:"));
					smsIntent.setType("vnd.android-dir/mms-sms");
					smsIntent.putExtra("address"  ,phoneno);
					smsIntent.putExtra("sms_body"  , Constants.INVITE_MESSAGE);
					context.startActivity(smsIntent);
				}
			}
		});
		return convertView;
	}


	//Check Internet connection
	public final boolean isInternetOn() {
		ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
				|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
			return true;
		} else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
				|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
			return false;
		}
		return false;
	}
}