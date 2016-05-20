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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.missme.kissme.MainFragmentMenu;
import com.missme.kissme.R;
import com.missme.kissme.Utils.Constants;

public class BlindKissFourthFrag extends Fragment implements OnClickListener{

	ImageView backIcon,nextIcon,titleIcon;
	TextView titleName,titleName1;
	ImageView process;
	EditText msg;
	Button clearAll,Ok;
	String getMsg;
	SharedPreferences pref;
	Editor editor;
	String name;
	LinearLayout llNext;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_realtimekissfourth, container,false);
		pref = getActivity().getSharedPreferences(Constants.BLIND_MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
		editor = pref.edit();
		backIcon=(ImageView)view.findViewById(R.id.realtimefourth_back_icon);
		nextIcon=(ImageView)view.findViewById(R.id.realtimefourth_next_icon);
		name=pref.getString(Constants.BLIND_TO_USER_NAME_PREF, null);
		titleName=(TextView)view.findViewById(R.id.realnewKiss_txtfourth);
		titleName.setText(name);
		process=(ImageView)view.findViewById(R.id.realtime_process_iconfourth);
		process.setBackgroundResource(R.drawable.process_enter_message);
		titleName1=(TextView)view.findViewById(R.id.newKiss_txtfourth);
		titleName1.setText("Blind Kiss");
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"SourceSansPro-Regular.otf");
		titleName1.setTypeface(font);
		msg=(EditText)view.findViewById(R.id.realedit_txt);
		clearAll=(Button)view.findViewById(R.id.alert_clearall_btn);
		Ok=(Button)view.findViewById(R.id.alert_ok_btn);
		llNext=(LinearLayout)view.findViewById(R.id.llNextFourth);
		llNext.setOnClickListener(this);
		backIcon.setOnClickListener(this);
		nextIcon.setOnClickListener(this);
		clearAll.setOnClickListener(this);
		Ok.setOnClickListener(this);
		nextIcon.setOnClickListener(this);
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
		case R.id.realtimefourth_back_icon:
			Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
			intent.putExtra("homepage", "blindkissthird");
			getActivity().startActivity(intent);
			getActivity().finish();
			break;
		case R.id.llNextFourth:
			attachValidate();
			break;
		case R.id.alert_clearall_btn:
			msg.setText(null);
			break;
		case R.id.alert_ok_btn:
			attachValidate();
			break;
		}
	}
	public void attachValidate(){
		getMsg=msg.getText().toString().trim();
		editor.putString(Constants.BLIND_MESSAGE_PREF,getMsg);
		editor.commit();
		Intent nextintent = new Intent(getActivity(), MainFragmentMenu.class);
		nextintent.putExtra("homepage", "blindkissfifth");
		getActivity().startActivity(nextintent);
		getActivity().finish();
	}
}