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
import com.missme.kissme.Bean.FloatingBean;
import com.missme.kissme.Utils.ImageLoader1;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class FloatingKissAdapter extends BaseAdapter {

	
	Activity activity;
	Typeface spacemanTypeface;
	List<FloatingBean> userDetailList;
	DisplayImageOptions options;
	public FloatingKissAdapter(List<FloatingBean> userDetailList, Activity activity) {
		this.userDetailList = userDetailList;
		this.activity = activity;
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
		private TextView textViewName;
		private TextView textViewtype;
	
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_list_floating, parent,false);
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.floating_grid_image);
			viewHolder.textViewName = (TextView) convertView.findViewById(R.id.floating_grid_txt);
			viewHolder.textViewtype = (TextView) convertView.findViewById(R.id.floating_grid_txtname_attached);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		final FloatingBean userDetailObj = userDetailList.get(position);
		viewHolder.textViewName.setText(userDetailObj.getUserName());
		viewHolder.textViewtype.setText(userDetailObj.getAttachType());	
		
		String imageURL=userDetailObj.getProf_image_path();

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

