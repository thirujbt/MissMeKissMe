package com.missme.kissme.MenuItems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.missme.kissme.HomePageFragment;
import com.missme.kissme.MainFragmentMenu;
import com.missme.kissme.R;
import com.missme.kissme.Adapter.ContactPageAdap;
import com.missme.kissme.Bean.ContactBean;
import com.missme.kissme.Utils.Constants;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class ContactpageFrag extends Fragment implements OnClickListener {

	ImageView backIcon, search;
	View view;
	List<ContactBean> contactBeanList = new ArrayList<ContactBean>();
	ArrayList<ContactBean> contactBeanList1 = new ArrayList<ContactBean>();
	private ArrayList<ContactBean> mOriginalContactArrayList = new ArrayList<ContactBean>();
	ArrayList<String> searchList = new ArrayList<String>();
	SharedPreferences pref;
	ContactPageAdap contPageAdap;
	ListView contpageList;
	AutoCompleteTextView textView;
	ArrayAdapter<String> adapter;
	String setnll = null;
	String deviceLogStatus;
	ContactBean mcontact;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.contact_page, container, false);
		pref = getActivity().getSharedPreferences(
				Constants.MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
		contpageList = (ListView) view.findViewById(R.id.cont_page_listView);
		backIcon = (ImageView) view.findViewById(R.id.contact_page_back_icon);
		textView = (AutoCompleteTextView) view
				.findViewById(R.id.contact_page_searchbox);
		search = (ImageView) view.findViewById(R.id.blind_kiss_searchimage);
		TextView titlee = (TextView) view
				.findViewById(R.id.cp_newKiss_txt_blind);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(),
				"SourceSansPro-Regular.otf");
		titlee.setTypeface(font);
		deviceLogStatus = pref.getString(Constants.DEVICE_LOG_PREF, null);

		if (HomePageFragment.iSHome) {
			backIcon.setVisibility(View.VISIBLE);
		} else {
			backIcon.setVisibility(View.GONE);
		}
		backIcon.setOnClickListener(this);
		search.setOnClickListener(this);
		if (HomePageFragment.contactBeanList1.isEmpty()) {
			Toast.makeText(getActivity(), "Contacts are not available.",
					Toast.LENGTH_SHORT).show();
		} else {
			mOriginalContactArrayList.addAll(HomePageFragment.contactBeanList1);

			try {
				Collections.sort(HomePageFragment.contactBeanList1,
						new Comparator<ContactBean>() {
							public int compare(ContactBean one,
									ContactBean other) {
								return one.getName().compareTo(other.getName());
							}
						});
			} catch (Exception e) {
				Collections.sort(HomePageFragment.contactBeanList1,
						new Comparator<ContactBean>() {
							public int compare(ContactBean one,
									ContactBean other) {
								return one.getSrname().compareTo(
										other.getSrname());
							}
						});
			}
			contPageAdap = new ContactPageAdap(getActivity(),
					HomePageFragment.contactBeanList1);
			contpageList.setAdapter(contPageAdap);
		}

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item,
				HomePageFragment.searchList);
		textView.setAdapter(adapter);

		textView.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				String getText = textView.getText().toString();
				filter(getText);
				return false;
			}
		});
		if (deviceLogStatus == null) {

		} else if (deviceLogStatus.equalsIgnoreCase("no")) {
			Intent i = new Intent(getActivity(), MainFragmentMenu.class);
			getActivity().startActivity(i);
			getActivity().finish();
		}
	}

	// Check Internet connection
	public final boolean isInternetOn() {
		ConnectivityManager connec = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
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
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.contact_page_back_icon:
			Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
			getActivity().startActivity(intent);
			getActivity().finish();
			break;
		case R.id.blind_kiss_searchimage:
			String getText = textView.getText().toString();
			if (getText.trim().length() > 0) {
				filter(getText);
			}
			break;
		}
	}

	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		HomePageFragment.contactBeanList1.clear();
		if (charText.length() == 0) {
			HomePageFragment.contactBeanList1.addAll(mOriginalContactArrayList);
		} else {
			for (ContactBean wp : mOriginalContactArrayList) {
				if (wp.getSrname().toLowerCase(Locale.getDefault())
						.contains(charText)) {
					HomePageFragment.contactBeanList1.add(wp);
				}
			}
		}
		contPageAdap.notifyDataSetChanged();
	}
	/*
	 * public int compareTo(ContactBean other) { int i =
	 * firstName.compareTo(other.getName()); if (i != 0) return i;
	 * 
	 * i = lastName.compareTo(other.getName()); if (i != 0) return i;
	 * 
	 * return Integer.compare(age, other.age); }
	 */
}