package com.dewald.goodBowler;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;



public class DatePickerFragment extends DialogFragment  {
	
	static DatePickerFragment newInstance(int activity){
		DatePickerFragment dpf = new DatePickerFragment();
		Bundle args = new Bundle();
		args.putInt("activity", activity);
		dpf.setArguments(args);
		return dpf;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		int activity = getArguments().getInt("activity");
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		
		switch(activity){
		case 1:
			return new DatePickerDialog(getActivity(), (Main)getActivity(), year, month, day);
		case 2:
			return new DatePickerDialog(getActivity(), (StatSelect)getActivity(), year, month, day);
		case 3:
			return new DatePickerDialog(getActivity(), (StatFunctionsActivity)getActivity(), year, month, day);	
		default:
			return null;	
		}
	}
		
}