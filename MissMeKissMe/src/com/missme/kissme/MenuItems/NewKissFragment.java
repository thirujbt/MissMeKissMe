package com.missme.kissme.MenuItems;

import com.missme.kissme.HomePageFragment;
import com.missme.kissme.MainFragmentMenu;
import com.missme.kissme.R;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class NewKissFragment extends Fragment implements OnClickListener {

	ImageView backIcon,realTime,instant,delayed;
	Fragment currenrFragment = null;
	TextView select;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_newkiss, container,false);
		backIcon=(ImageView)view.findViewById(R.id.back_icon);
		realTime=(ImageView)view.findViewById(R.id.realtime_image);
		instant=(ImageView)view.findViewById(R.id.instant_image);
		delayed=(ImageView)view.findViewById(R.id.delayed_image);
		select=(TextView)view.findViewById(R.id.newKiss_title_txt1);		
		TextView titlee=(TextView)view.findViewById(R.id.newKiss_txt);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"SourceSansPro-Regular.otf");
		select.setTypeface(font);
		titlee.setTypeface(font);		
		
		if(HomePageFragment.iSHome){
			backIcon.setVisibility(View.VISIBLE);
		}else{
			backIcon.setVisibility(View.GONE);
		}
		
		backIcon.setOnClickListener(this);
		realTime.setOnClickListener(this);
		instant.setOnClickListener(this);
		delayed.setOnClickListener(this);
		return view;
		
	}	
	@Override
	public void onClick(View v) {
		int id=v.getId();	
		switch (id) {
		case R.id.back_icon:
			Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
			getActivity().startActivity(intent);
			getActivity().finish();
			break;
		case R.id.realtime_image:
			Intent realtimeintent = new Intent(getActivity(), MainFragmentMenu.class);
			realtimeintent.putExtra("homepage", "realtimekiss");
			getActivity().startActivity(realtimeintent);
			getActivity().finish();
			break;		
		case R.id.instant_image:
			Intent instantIntent = new Intent(getActivity(), MainFragmentMenu.class);
			instantIntent.putExtra("homepage", "instantkiss");
			getActivity().startActivity(instantIntent);
			getActivity().finish();
			break;
		case R.id.delayed_image:
			Intent delayedIntent = new Intent(getActivity(), MainFragmentMenu.class);
			delayedIntent.putExtra("homepage", "delayedkiss");
			getActivity().startActivity(delayedIntent);
			getActivity().finish();
			break;
		}
	}
}