package com.missme.kissme.Adapter;

import java.util.List;

import com.missme.kissme.R;
import com.missme.kissme.Bean.MenuItem;
import com.missme.kissme.Utils.ImageLoader1;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

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


public class HomeMenuAdapter extends BaseAdapter {

	List<MenuItem> menuItems;
	Activity activity;
	Typeface spacemanTypeface;
	DisplayImageOptions options;
	ImageLoader imageLoader1;
	String imageUrl ="";
	public HomeMenuAdapter(List<MenuItem> menuItems, Activity activity) {
		this.menuItems = menuItems;
		this.activity = activity;
		
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.default_imagesss)
		.showImageForEmptyUri(R.drawable.default_imagesss)
		.showImageOnFail(R.drawable.default_imagesss)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		
		imageLoader1 = ImageLoader.getInstance();
		imageLoader1.init(ImageLoaderConfiguration.createDefault(activity));
	}

	@Override
	public int getCount() {
		return menuItems.size();
	}

	@Override
	public Object getItem(int position) {
		return menuItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	private class ViewHolder {
		private ImageView imageView;
		private TextView textView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.drawer_menu_item, parent,false);
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.menu_item_img);
			viewHolder.textView = (TextView) convertView.findViewById(R.id.menu_item_txt);
			convertView.setTag(viewHolder);    
		} else {

			viewHolder = (ViewHolder) convertView.getTag();
		}
		MenuItem menuItem = (MenuItem) getItem(position);
		viewHolder.imageView.setImageResource(menuItem.getDrawableId());
		viewHolder.textView.setText(menuItem.getMenuItem());
		if(position==0){
			viewHolder.imageView.setMaxHeight(85);
			viewHolder.imageView.setMaxWidth(85);
			viewHolder.textView.setText(menuItem.getMenuItem());
			imageUrl = menuItem.getProfileImage();
			ImageLoader.getInstance().displayImage(imageUrl, viewHolder.imageView,options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
				}
				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				}
				@Override
				public void onLoadingComplete(final String imageUri, View view, final Bitmap loadedImage) {
					final ImageView imageView = (ImageView) view;		
					ViewTreeObserver observerProfileImg = imageView.getViewTreeObserver();
					observerProfileImg.addOnPreDrawListener(new OnPreDrawListener() {

						@Override
						public boolean onPreDraw() {
							imageView.getViewTreeObserver().removeOnPreDrawListener(this);
							if(imageUri != null && ! imageUri.equals("null") && loadedImage!=null && !imageUri.equalsIgnoreCase("")){
								Bitmap bitmap = ImageLoader1.getRoundCroppedBitmapimg(loadedImage, imageView.getWidth());
								imageView.setImageBitmap(bitmap);
							}
							return true;
						}
					});

				}
			});
			
		}
		
		
		return convertView;
	}
}