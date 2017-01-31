package com.consumer.analysis;

import com.consumer.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TimeAnaFragment extends Fragment{

	private int index;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup)inflater.inflate(R.layout.offer_row, container, false);
	   /* TextView tw = (TextView) root.findViewById(R.id.cmp_tv);
	    tw.setText(Integer.toString(index));*/
	    return root;
	}
	
	public void setIndex(int index){
	    this.index = index;
	}
	
}
