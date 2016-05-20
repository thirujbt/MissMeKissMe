package com.missme.kissme.MenuItems;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.missme.kissme.MainFragmentMenu;
import com.missme.kissme.R;
import com.missme.kissme.Utils.Constants;

public class DelayedKissSecondFrag extends Fragment implements OnClickListener{

	ImageView backIcon,nextIcon,car,walk,plane,train,cycle,titleIcon,done;
	TextView title;
	private TextView mDliveryDate;
	private TextView mTime;
	public Context mContext;
	// variables to store the selected date and time
	private int mSelectedYear;
	private int mSelectedMonth;
	private int mSelectedDay;
	private int mSelectedHour;
	private int mSelectedMinutes;
	String aTime,aDate ;
	// CallBacks for date and time pickers
	private OnDateSetListener mOnDateSetListener = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			// update the current variables ( year, month and day)
			mSelectedDay = dayOfMonth;
			mSelectedMonth = monthOfYear;
			mSelectedYear = year;
			// update txtDate with the selected date
			if (view.isShown()) {
				Calendar calendarTemp = Calendar.getInstance();
				calendarTemp.set(year, monthOfYear, dayOfMonth);
				Date todaysDate = new Date(System.currentTimeMillis());
				if (calendarTemp.getTime().compareTo(todaysDate) >= 0) {
					updateDateUI();
				} else {
					Toast.makeText(getActivity(), "Date should not be less than today's date.", Toast.LENGTH_SHORT).show();
				}
			}
		}
	};

	private OnTimeSetListener mOnTimeSetListener = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// update the current variables (hour and minutes)
			mSelectedHour = hourOfDay;
			mSelectedMinutes = minute;
			// update txtTime with the selected time
			updateTimeUI();
		}
	};

	SharedPreferences pref;
	Editor editor;
	LinearLayout llNext;
	@SuppressLint("SimpleDateFormat")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_delayedkisssecond, container,false);
		backIcon=(ImageView)view.findViewById(R.id.delayedsecond_back_icon);
		nextIcon=(ImageView)view.findViewById(R.id.delayedsecond_next_icon);
		pref = getActivity().getSharedPreferences(Constants.DELAY_MISS_ME_KISS_ME_PREF, Context.MODE_PRIVATE);
		editor = pref.edit();
		titleIcon=(ImageView)view.findViewById(R.id.delayedtime_icon2);
		titleIcon.setBackgroundResource(R.drawable.delayed_kiss_icon);
		title=(TextView)view.findViewById(R.id.delayednewKiss_txt);
		mDliveryDate=(TextView)view.findViewById(R.id.date_txt);
		mTime=(TextView)view.findViewById(R.id.time_txt);
		done=(ImageView)view.findViewById(R.id.done_icon);
		title.setText("Delayed Kiss");		
		TextView titlee=(TextView)view.findViewById(R.id.newKiss_txt);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"SourceSansPro-Regular.otf");
		titlee.setTypeface(font);
		title.setTypeface(font);		
		llNext=(LinearLayout)view.findViewById(R.id.llNextDelaySecond);		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm a");
		String currentDateandTime = sdf.format(new Date());
		String[] getDate=currentDateandTime.split(" ");		
		String date=getDate[0];
		String time=getDate[1];
		String timeNoon=getDate[2];
		String fintime=time+" "+timeNoon;
		mDliveryDate.setText(date);
		mTime.setText(fintime);
		backIcon.setOnClickListener(this);
		nextIcon.setOnClickListener(this);
		mDliveryDate.setOnClickListener(this);
		mTime.setOnClickListener(this);
		done.setOnClickListener(this);
		llNext.setOnClickListener(this);		
		// initialize the current date
		Calendar calendar = Calendar.getInstance();
		this.mSelectedYear = calendar.get(Calendar.YEAR);
		this.mSelectedMonth = calendar.get(Calendar.MONTH); 
		this.mSelectedDay = calendar.get(Calendar.DAY_OF_MONTH);     
		this.mSelectedHour = calendar.get(Calendar.HOUR_OF_DAY);
		this.mSelectedMinutes = calendar.get(Calendar.MINUTE);                                                                                  
		updateDateUI();
		updateTimeUI();
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	private void updateDateUI() {
		String month = ((mSelectedMonth+1) > 9) ? ""+(mSelectedMonth+1): "0"+(mSelectedMonth+1) ;
		String day = ((mSelectedDay) < 10) ? "0"+mSelectedDay: ""+mSelectedDay ;
		updateDate(day, month,mSelectedYear);	
		String fDate=mSelectedYear+"-"+month+"-"+day;
		editor.putString(Constants.DELAY_DATE_PREF, fDate);
		editor.commit();
	}
	private void updateTimeUI() {
		String hour = (mSelectedHour > 9) ? ""+mSelectedHour: "0"+mSelectedHour ;
		String minutes = (mSelectedMinutes > 9) ?""+mSelectedMinutes : "0"+mSelectedMinutes;
		int hours=Integer.parseInt(hour);
		int minutess=Integer.parseInt(minutes);
		String fDate=hours+":"+minutess+":"+"00";	
		editor.putString(Constants.DELAY_TIME_PREF, fDate);
		editor.commit();
		updateTime(hours,minutess);
	}
	// Used to convert 24hr format to 12hr format with AM/PM values
	private void updateTime(int hours, int mins) {
		String timeSet = "";
		if (hours > 12) {
			hours -= 12;
			timeSet = "PM";
		} else if (hours == 0) {
			hours += 12;
			timeSet = "AM";
		} else if (hours == 12)
			timeSet = "PM";
		else
			timeSet = "AM";
		String minutes = "";
		if (mins < 10)
			minutes = "0" + mins;
		else
			minutes = String.valueOf(mins);
		String hourss = "";
		if (hours < 10)
			hourss = "0" + hours;
		else
			hourss = String.valueOf(hours);
		// Append in a StringBuilder
		aTime = new StringBuilder().append(hourss+" ").append('.')
				.append(" "+minutes).append("  ").append(timeSet).toString();
		mTime.setText(aTime);
	}
	private void updateDate(String day, String month,int year) {
		String months=" ";
		if(month.equalsIgnoreCase("01")){
			months="JAN";
		}else if(month.equalsIgnoreCase("02")){
			months="FEB";
		}else if(month.equalsIgnoreCase("03")){
			months="MAR";
		}else if(month.equalsIgnoreCase("04")){
			months="APR";
		}else if(month.equalsIgnoreCase("05")){
			months="MAY";
		}else if(month.equalsIgnoreCase("06")){
			months="JUN";
		}else if(month.equalsIgnoreCase("07")){
			months="JUL";
		}else if(month.equalsIgnoreCase("08")){
			months="AUG";
		}else if(month.equalsIgnoreCase("09")){
			months="SEP";
		}else if(month.equalsIgnoreCase("10")){
			months="OCT";
		}else if(month.equalsIgnoreCase("11")){
			months="NOV";
		}else if(month.equalsIgnoreCase("12")){
			months="DEC";
		}
		// Append in a StringBuilder
		aDate = new StringBuilder().append(day+" ").append(months+" ").append(year).toString();
		mDliveryDate.setText(aDate);
	}
	// initialize the DatePickerDialog
	private DatePickerDialog showDatePickerDialog(int initialYear, int initialMonth, int initialDay, OnDateSetListener listener) {
		DatePickerDialog dialog = new DatePickerDialog(getActivity(), listener, initialYear, initialMonth, initialDay);
		dialog.show();
		return dialog;
	}
	// initialize the TimePickerDialog
	private TimePickerDialog showTimePickerDialog(int initialHour, int initialMinutes, boolean is24Hour, OnTimeSetListener listener) {
		TimePickerDialog dialog = new TimePickerDialog(getActivity(), listener, initialHour, initialMinutes, is24Hour);
		dialog.show();
		return dialog;
	}
	@Override
	public void onClick(View v) {
		int id=v.getId();
		switch (id) {
		case R.id.delayedsecond_back_icon:
			Intent intent = new Intent(getActivity(), MainFragmentMenu.class);
			intent.putExtra("homepage", "delayedkiss");
			getActivity().startActivity(intent);
			getActivity().finish();
			break;
		case R.id.llNextDelaySecond:
			sendDetails();	
			break;
		case R.id.date_txt:
			showDatePickerDialog(mSelectedYear, mSelectedMonth, mSelectedDay, mOnDateSetListener);
			break;
		case R.id.time_txt:
			showTimePickerDialog(mSelectedHour, mSelectedMinutes, true, mOnTimeSetListener);
			break;
		case R.id.done_icon:
			sendDetails();
			break;
		}
	}
	public void sendDetails(){
		String dTime=mTime.getText().toString().trim();
		String dDate=mDliveryDate.getText().toString().trim();
		editor.putString(Constants.SELECTED_TIME_PREF, dTime);
		editor.putString(Constants.SELECTED_DATE_PREF, dDate);
		editor.commit();
		Intent nextintent = new Intent(getActivity(), MainFragmentMenu.class);
		nextintent.putExtra("homepage", "delayedkissthird");
		getActivity().startActivity(nextintent);
		getActivity().finish();
	}
}