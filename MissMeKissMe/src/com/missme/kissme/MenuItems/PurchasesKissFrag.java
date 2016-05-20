package com.missme.kissme.MenuItems;

import org.json.JSONException;
import org.json.JSONObject;

import com.missme.kissme.HomePageFragment;
import com.missme.kissme.MainFragmentMenu;
import com.missme.kissme.R;
import com.missme.kissme.AccountManager.LoginActivity;
import com.missme.kissme.Inappbilling.utilsl.IabHelper;
import com.missme.kissme.Inappbilling.utilsl.IabHelper.OnIabSetupFinishedListener;
import com.missme.kissme.Inappbilling.utilsl.IabResult;
import com.missme.kissme.Inappbilling.utilsl.Inventory;
import com.missme.kissme.Inappbilling.utilsl.Purchase;
import com.missme.kissme.InterfaceClass.AsyncResponse;
import com.missme.kissme.InterfaceClass.HomeAsyncResponse;
import com.missme.kissme.ServiceRequest.KissMeAsyncTask;
import com.missme.kissme.Utils.Constants;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PurchasesKissFrag extends Fragment implements OnClickListener, HomeAsyncResponse, AsyncResponse{

	ImageView backIcon;
	Button mLimited,mUnlimited;
	TextView title,titleBar;
	// Product id for Ads
	private static final String SKU_LIMITED_KISS_PRO_ID ="limitedpid"; //Modify with real product id
	// Product id for Update Frequency
	private static final String SKU_UNLIMITED_KISS_PRO_ID ="unlimitednewpid"; //Modify with real product id
	
	//private static final String SKU_UNLIMITED_KISS_PRO_ID ="unlimitedpid"; //Modify with real product id
	// Encoded License key 
	private static final String base64EncodedPublicKey ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiuACj2ocfEsDinh7NDxdkxRbL1iu/SB9VdLH1KftIcyN0pegZmJeqTzn0XfVpoVEqQrFYTK7t9ioGkz6lsNoYgXwAUPzEJiOS8uotfBFI19eU29f7oF6wPDtIDiHMbhcPxANzkSMVu/wyr3nMbKERlG/kCkAEtpUtOAhju5IJNFF82BJB+YSxYL2ykx4RtLvJKhTWQkxzS+9DuGwVEFWtwCN4EXdlPYJVH74RwvLt+i5GHjniNKNFWc9JH1tEd4uLERRsOmdSw+q4f/An0RHMcywqZU5HnsXZlKkviLacqMeSVvC4l/jJ5umfYanEGkZc3/nD28Jq/i58az/7U1B6wIDAQAB";
	//The helper Object
	private IabHelper iabHelper;
	public static final int RC_REQUEST = 10001;
	public static String purchaseClicked;
	SharedPreferences pref1;
	public static String limitedpurchaseStatus="2";
	public static String unLimitedpurchaseStatus="1";
	String purchaseStatus;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_purchasekiss, container,false);
		pref1 = getActivity().getSharedPreferences(Constants.MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
		backIcon=(ImageView)view.findViewById(R.id.purchase_back_icon);
	//	mLimited=(Button)view.findViewById(R.id.purchase_limited);
		mUnlimited=(Button)view.findViewById(R.id.purchase_unlimited);
		title=(TextView)view.findViewById(R.id.purchase_txttitle);
		titleBar=(TextView)view.findViewById(R.id.purchase_txtfourth);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"SourceSansPro-Regular.otf");
		title.setTypeface(font);
		titleBar.setTypeface(font);
		if(HomePageFragment.iSHome){
			backIcon.setVisibility(View.VISIBLE);
		}else{
			backIcon.setVisibility(View.GONE);
		}
		backIcon.setOnClickListener(this);
	//	mLimited.setOnClickListener(this);
		mUnlimited.setOnClickListener(this);
		return view;
	}
	public void onResume(){
		super.onResume();
		try{
			iabHelper = new IabHelper(getActivity(), base64EncodedPublicKey);
			// Start setup. This is asynchronous and the specified listener
			// will be called once setup completes.
			iabHelper.startSetup(new OnIabSetupFinishedListener() {
				@Override
				public void onIabSetupFinished(IabResult result) {
					if(result.isFailure()){
						return;
					}
					iabHelper.queryInventoryAsync(queryInventoryFinishedListener);
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	public void onClick(View v) {
		int id=v.getId();
		switch (id) {
		case R.id.purchase_back_icon:
			Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
			getActivity().startActivity(intent);
			getActivity().finish();
			break;
		/*case R.id.purchase_limited:
			purchaseClicked="limited";
			limitedPurchase();
			//startUpdatePurchaseStatus();
		
			break;*/
		case R.id.purchase_unlimited:
			purchaseClicked="unlimited";
			unlimitedPurchase();
			//startUpdatePurchaseStatus();
			break;
		}
	}
	// Listener that's called when we finish querying the items and subscriptions we own
	IabHelper.QueryInventoryFinishedListener queryInventoryFinishedListener = new IabHelper.QueryInventoryFinishedListener() {
		@Override
		public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
			if(iabHelper==null){
				return;
			}
			if(result.isFailure()){
				return;
			}
			Purchase adsPurchase = inventory.getPurchase(SKU_LIMITED_KISS_PRO_ID);
			if((adsPurchase!=null)&&(verifyDeveloperPayload(adsPurchase))){
				iabHelper.consumeAsync(adsPurchase,
						onConsumeFinishedListener);
				return;
			}
			Purchase adsUpdateFrq = inventory.getPurchase(SKU_UNLIMITED_KISS_PRO_ID);
			if((adsUpdateFrq!=null)&&(verifyDeveloperPayload(adsUpdateFrq))){
				iabHelper.consumeAsync(adsUpdateFrq, onConsumeFinishedListener);
				return;
			}	
		}
	};
	// Called when consumption is complete
	IabHelper.OnConsumeFinishedListener onConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
		@Override
		public void onConsumeFinished(Purchase purchase, IabResult result) {
			if (iabHelper == null) {
				return;
			}
			if (result.isSuccess()) {
				// Call service
				if (purchase.getSku().equals(SKU_LIMITED_KISS_PRO_ID)) {
					Toast.makeText(getActivity(), "You have purchased for limited kisses", Toast.LENGTH_LONG).show();
					//Call Web Service
					startUpdatePurchaseStatus();
				} else if (purchase.getSku().equals(SKU_UNLIMITED_KISS_PRO_ID)) {

					Toast.makeText(getActivity(), "You have purchased for unlimited kisses", Toast.LENGTH_LONG).show();
					//Call Web Service
					startUpdatePurchaseStatus();
				}
			} else {
				Toast.makeText(getActivity(), "Faild purchasing", Toast.LENGTH_LONG).show();
			}
		}
	};
	/**
	 * Update purchase status in server
	 */
	private void startUpdatePurchaseStatus(){
		String status; 
		if(purchaseClicked.equalsIgnoreCase("limited")){ //Start alarm and get status
			status = "2";
		}else if(purchaseClicked.equalsIgnoreCase("unlimited")){//Start alarm and get status
			status = "1";

		}else{// status
			status = "0";			
		}
		String url = Constants.KISS_PURCHASE_SAVE_URL;
		String userid = pref1.getString(Constants.USER_AUTHKEY_PREF, null);
		// Set AuthKey and Status
		try {		
			JSONObject jsonObject = new JSONObject();
			jsonObject.accumulate("authkey", userid);
			jsonObject.accumulate("purchase_type", status);
			if(LoginActivity.getSessionname(getActivity())!=null){
				jsonObject.accumulate(Constants.SESSION_NAME,LoginActivity.getSessionname(getActivity()));
				}
			new KissMeAsyncTask(this.getActivity(), url, Constants.KISS_PURCHASE_SAVE_RESPONSE, PurchasesKissFrag.this).execute(jsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	// Callback for when a purchase is finished
	IabHelper.OnIabPurchaseFinishedListener iabPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		@Override
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			// if we were disposed of in the meantime, quit.
			if (iabHelper == null){
				return;
			}
			if (result.isFailure()) {
				return;
			}
			if(purchase.getSku().equals(SKU_LIMITED_KISS_PRO_ID) || purchase.getSku().equals(SKU_UNLIMITED_KISS_PRO_ID)){
				iabHelper.consumeAsync(purchase, onConsumeFinishedListener);
			}
		}
	};
	@SuppressWarnings("unused")
	private boolean verifyDeveloperPayload(Purchase p) {
		String payload = p.getDeveloperPayload();
		//Set Server code
		return true;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (!iabHelper.handleActivityResult(requestCode, resultCode, data)) {
			super.onActivityResult(requestCode, resultCode, data);
		} else {
		}
	}
	/**
	 *  Check user type and Open the in app purchase for Ads and Three Minute Frequency Update 
	 *  
	 */
	/*private void limitedPurchase() {
			try{
				String payload = "";
				iabHelper.launchPurchaseFlow(this.getActivity(), SKU_LIMITED_KISS_PRO_ID,
						RC_REQUEST, iabPurchaseFinishedListener, payload);
			}catch(Exception e){
				e.printStackTrace();
			}
	}*/
	/**
	 * Check user type and Open the in app purchase for Full purchase
	 * 
	 */
	private void unlimitedPurchase() {
			try{
				String payload = "";
				iabHelper.launchPurchaseFlow(getActivity(), SKU_UNLIMITED_KISS_PRO_ID,
						RC_REQUEST, iabPurchaseFinishedListener, payload);
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	@Override
	public void onProcessFinish(String serverResp, int RespValue) {
		try{
			String statusCode = "0";
			if(serverResp!=null){
				JSONObject jObjServerResp;
				if(RespValue==Constants.KISS_PURCHASE_SAVE_RESPONSE){
					jObjServerResp = new JSONObject(serverResp);
					statusCode = jObjServerResp.getString(Constants.STATUS_CODE);
					if(statusCode.equalsIgnoreCase("250")){
						Toast.makeText(getActivity(), "Purchased successfully", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(getActivity(), "No response from server", Toast.LENGTH_SHORT).show();
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (iabHelper != null) {
			iabHelper.dispose();
			iabHelper = null;
		}
	}
} 