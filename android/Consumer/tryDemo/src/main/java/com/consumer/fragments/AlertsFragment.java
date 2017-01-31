package com.consumer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.consumer.R;

public class AlertsFragment extends Fragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	
		View rootView = inflater.inflate(R.layout.alert_fragment, container, false);
		
		return rootView;
		//return super.onCreateView(inflater, container, savedInstanceState);
	}

}
