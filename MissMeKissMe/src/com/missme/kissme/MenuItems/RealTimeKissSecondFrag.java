package com.missme.kissme.MenuItems;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.missme.kissme.MainFragmentMenu;
import com.missme.kissme.R;
import com.missme.kissme.Utils.Constants;

public class RealTimeKissSecondFrag extends Fragment implements OnClickListener{

	ImageView backIcon,nextIcon,car,walk,plane,train,cycle,titleIcon,process,boat;
	boolean type=false;
	SharedPreferences pref;
	Editor editor;
	LinearLayout llNext;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_realtimekisssecond, container,false);
		pref = getActivity().getSharedPreferences(Constants.REAL_MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
		editor = pref.edit();
		backIcon=(ImageView)view.findViewById(R.id.realtimesecond_back_icon);
		nextIcon=(ImageView)view.findViewById(R.id.realtimesecond_next_icon);
		car=(ImageView)view.findViewById(R.id.car_icon);
		walk=(ImageView)view.findViewById(R.id.wailk_icon);
		plane=(ImageView)view.findViewById(R.id.plane_icon);
		train=(ImageView)view.findViewById(R.id.train_icon);
		cycle=(ImageView)view.findViewById(R.id.cycle_icon);
		boat=(ImageView)view.findViewById(R.id.boat_icon);
		titleIcon=(ImageView)view.findViewById(R.id.realtime_icon2);
		titleIcon.setBackgroundResource(R.drawable.realtime_kiss_icon);
		process=(ImageView)view.findViewById(R.id.realtime_process_iconsecond);
		process.setBackgroundResource(R.drawable.process1);
		llNext=(LinearLayout)view.findViewById(R.id.llNextSecond);		
		TextView titlee=(TextView)view.findViewById(R.id.newKiss_txtsecond);
		TextView titlBar=(TextView)view.findViewById(R.id.realnewKiss_txtsecond);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"SourceSansPro-Regular.otf");
		titlee.setTypeface(font);
		titlBar.setTypeface(font);		
		car.setBackgroundResource(R.drawable.car_unselect);
		walk.setBackgroundResource(R.drawable.walk_unselect);
		plane.setBackgroundResource(R.drawable.plane_unselect);
		train.setBackgroundResource(R.drawable.train_unselect);
		cycle.setBackgroundResource(R.drawable.cycle_unselect);
		boat.setBackgroundResource(R.drawable.boat_unselect);
		backIcon.setOnClickListener(this);
		nextIcon.setOnClickListener(this);
		llNext.setOnClickListener(this);		
		car.setOnClickListener(this);
		walk.setOnClickListener(this);
		plane.setOnClickListener(this);
		train.setOnClickListener(this);
		cycle.setOnClickListener(this);
		boat.setOnClickListener(this);
		return view;
	}
	@Override
	public void onResume() {
		super.onResume();
	}
	@Override
	public void onClick(View v) {
		int id=v.getId();
		switch (id) {
		case R.id.realtimesecond_back_icon:
			Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
			intent.putExtra("homepage", "realtimekiss");
			getActivity().startActivity(intent);
			getActivity().finish();
			break;
		case R.id.llNextSecond:
			if(!type){
				Toast.makeText(getActivity(), Constants.TYPEOF_MODE, Toast.LENGTH_SHORT).show();
			}else{
				Intent nextintent = new Intent(getActivity(), MainFragmentMenu.class);
				nextintent.putExtra("homepage", "realtimethird");
				getActivity().startActivity(nextintent);
				getActivity().finish();
			}
			break;
		case R.id.car_icon:
			car.setBackgroundResource(R.drawable.car_select);
			walk.setBackgroundResource(R.drawable.walk_unselect);
			plane.setBackgroundResource(R.drawable.plane_unselect);
			train.setBackgroundResource(R.drawable.train_unselect);
			cycle.setBackgroundResource(R.drawable.cycle_unselect);
			boat.setBackgroundResource(R.drawable.boat_unselect);
			editor.putString(Constants.REAL_TRAVEL_TYPE_PREF, "car");
			editor.commit();
			type=true;
			break;
		case R.id.wailk_icon:
			car.setBackgroundResource(R.drawable.car_unselect);
			walk.setBackgroundResource(R.drawable.walk_select);
			plane.setBackgroundResource(R.drawable.plane_unselect);
			train.setBackgroundResource(R.drawable.train_unselect);
			cycle.setBackgroundResource(R.drawable.cycle_unselect);
			boat.setBackgroundResource(R.drawable.boat_unselect);
			editor.putString(Constants.REAL_TRAVEL_TYPE_PREF, "walk");
			editor.commit();
			type=true;
			break;
		case R.id.plane_icon:
			car.setBackgroundResource(R.drawable.car_unselect);
			walk.setBackgroundResource(R.drawable.walk_unselect);
			plane.setBackgroundResource(R.drawable.plane_select);
			train.setBackgroundResource(R.drawable.train_unselect);
			cycle.setBackgroundResource(R.drawable.cycle_unselect);
			boat.setBackgroundResource(R.drawable.boat_unselect);
			editor.putString(Constants.REAL_TRAVEL_TYPE_PREF, "plane");
			editor.commit();
			type=true;
			break;
		case R.id.train_icon:
			car.setBackgroundResource(R.drawable.car_unselect);
			walk.setBackgroundResource(R.drawable.walk_unselect);
			plane.setBackgroundResource(R.drawable.plane_unselect);
			train.setBackgroundResource(R.drawable.train_select);
			cycle.setBackgroundResource(R.drawable.cycle_unselect);
			boat.setBackgroundResource(R.drawable.boat_unselect);
			editor.putString(Constants.REAL_TRAVEL_TYPE_PREF, "train");
			editor.commit();
			type=true;
			break;
		case R.id.cycle_icon:
			car.setBackgroundResource(R.drawable.car_unselect);
			walk.setBackgroundResource(R.drawable.walk_unselect);
			plane.setBackgroundResource(R.drawable.plane_unselect);
			train.setBackgroundResource(R.drawable.train_unselect);
			cycle.setBackgroundResource(R.drawable.cycle_select);
			boat.setBackgroundResource(R.drawable.boat_unselect);
			editor.putString(Constants.REAL_TRAVEL_TYPE_PREF, "cycle");
			editor.commit();
			type=true;
			break;
		case R.id.boat_icon:
			car.setBackgroundResource(R.drawable.car_unselect);
			walk.setBackgroundResource(R.drawable.walk_unselect);
			plane.setBackgroundResource(R.drawable.plane_unselect);
			train.setBackgroundResource(R.drawable.train_unselect);
			cycle.setBackgroundResource(R.drawable.cycle_unselect);
			boat.setBackgroundResource(R.drawable.boat_select);
			editor.putString(Constants.REAL_TRAVEL_TYPE_PREF, "boat");
			editor.commit();
			type=true;
			break;
		}
	}
}