package com.missme.kissme.Adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.missme.kissme.MainFragmentMenu;
import com.missme.kissme.R;
import com.missme.kissme.Bean.VaultBean;
import com.missme.kissme.InterfaceClass.AsyncResponse;
import com.missme.kissme.ServiceRequest.KissMeAsyncTask;
import com.missme.kissme.Utils.Constants;
import com.missme.kissme.Utils.ImageLoader1;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class VaultKissAdapter extends BaseAdapter implements AsyncResponse {

	Activity activity;
	Typeface spacemanTypeface;
	List<VaultBean> userDetailList;
	DisplayImageOptions options;
	JSONObject jsonObject = new JSONObject();

	public VaultKissAdapter(List<VaultBean> userDetailList, Activity activity) {
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
		private ImageView imageViewDelete;
		private TextView textViewName;
		private TextView textViewtype;
		private ImageView imageReadUnread; 
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_grid_vault, parent,false);
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.vault_grid_image);
			viewHolder.textViewName = (TextView) convertView.findViewById(R.id.vault_grid_txt);
			viewHolder.textViewtype = (TextView) convertView.findViewById(R.id.vault_grid_txtname_attached);
			viewHolder.imageViewDelete = (ImageView) convertView.findViewById(R.id.vault_grid_delete_image);
			viewHolder.imageReadUnread = (ImageView) convertView.findViewById(R.id.vault_read_unread_image);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final VaultBean userDetailObj = userDetailList.get(position);

		viewHolder.textViewName.setText(userDetailObj.getUserName());
		viewHolder.textViewtype.setText(userDetailObj.getAttachType());
		final String msgId=userDetailObj.getMsgId();

		String readStatus=userDetailObj.getReadStatus();
		if(readStatus.equalsIgnoreCase("1")){
			viewHolder.imageReadUnread.setBackgroundResource(R.drawable.read_message);
		}else{
			viewHolder.imageReadUnread.setBackgroundResource(R.drawable.unread_message);
		}
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


		viewHolder.imageViewDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			
				Delete_file(activity,msgId);
				
			}
		});


		return convertView;
	}


	//Check Internet connection
	public final boolean isInternetOn() {
		ConnectivityManager connec = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
				|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
			return true;
		} else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
				|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
			return false;
		}
		return false;
	}

	@Override
	public void onProcessFinish(String serverResp, int RespValue) {
		try {
			String statusCode = "0";
			String msgResp;
			if(serverResp!=null){
				JSONObject jObjServerResp;
				if(RespValue==Constants.VAULT_DELETE_MESSAGE_RESPONSE){
					jObjServerResp = new JSONObject(serverResp);
					statusCode = jObjServerResp.getString(Constants.STATUS_CODE);
					//	statusResp = jObjServerResp.getString(Constants.STATUS);
					msgResp = jObjServerResp.getString(Constants.RESPONSE_MESSAGES);

					switch (statusCode) {

					case Constants.VAULT_MSG_DELETE_SUCCESS:

						Toast.makeText(activity, msgResp, Toast.LENGTH_SHORT).show();
						jsonObject = new JSONObject();
						Intent intent = new Intent(activity, MainFragmentMenu.class);
						intent.putExtra("homepage", "vaultkisssecond");
						activity.startActivity(intent);
						activity.finish();
						
						break;
					case Constants.VAULT_MSG_DELETE_FAILED:
						jsonObject = new JSONObject();
						Toast.makeText(activity, msgResp, Toast.LENGTH_SHORT).show();
						
						break;

					}
				}
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}
	}
	private void Delete_file(final Activity activity,final String msgId)
	 {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setMessage("Are you Sure, Do you want delete this message?");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	String url = Constants.VAULT_DETAIL_URL;
				if(isInternetOn()){
					try {
						try {
							jsonObject.accumulate("msg_id",msgId);
							jsonObject.accumulate("read_status","1");
							jsonObject.accumulate("delete_status","delete");
							System.out.println(jsonObject);
							new KissMeAsyncTask(activity, url, Constants.VAULT_DELETE_MESSAGE_RESPONSE, VaultKissAdapter.this).execute(jsonObject);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
				}
                dialog.cancel();
            }
        });
        builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
	    }
}
