package com.consumer.analysis;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.consumer.R;

public class TrendAnaFragment extends Fragment{

	private int index;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup)inflater.inflate(R.layout.trends_row, container, false);
	   /* TextView tw = (TextView) root.findViewById(R.id.cmp_tv);
	    tw.setText(Integer.toString(index));*/
	    return root;
	}
	
	public void setIndex(int index){
	    this.index = index;
	}
	
}
