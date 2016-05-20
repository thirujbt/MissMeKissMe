package com.missme.kissme.Adapter;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.missme.kissme.R;
import com.missme.kissme.Bean.ContactBean;
import com.missme.kissme.Utils.ImageLoader1;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class NewKissContactAdapter extends BaseAdapter {

	Context context;
	List<ContactBean> userDetailList;
	Activity activity;
	Typeface spacemanTypeface;
	DisplayImageOptions options;
	String imageUrl ="";
	LayoutInflater  inflater;
	public NewKissContactAdapter(Context context,List<ContactBean> list) {
	
		this.context = context;
		this.userDetailList = list;
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

	private class ViewHolder {
		private ImageView imageView;
		private TextView textView;
		private TextView txtSend;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_grid, parent,false);
			
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.grid_image);
			viewHolder.textView = (TextView) convertView.findViewById(R.id.grid_txt);
			viewHolder.txtSend = (TextView) convertView.findViewById(R.id.cont_kiss_send_btn);
			viewHolder.txtSend.setVisibility(View.GONE);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
	
		final ContactBean userDetailObj = userDetailList.get(position);
		viewHolder.textView.setText(userDetailObj.getSrname());
		viewHolder.txtSend.setText(userDetailObj.getSrSend());
		viewHolder.txtSend.setBackgroundColor(0xFF187F6A);
		
		String imageURL=userDetailObj.getSrprofileImg();

		ImageLoader.getInstance().displayImage(imageURL, viewHolder.imageView,options, new SimpleImageLoadingListener() {
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
		return convertView;
	}
}