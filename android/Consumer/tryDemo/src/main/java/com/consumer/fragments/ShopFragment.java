package com.consumer.fragments;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.consumer.ShopItemDetailsActivity;
import com.etsy.android.grid.StaggeredGridView;
import com.consumer.R;
import com.consumer.adapters.ShopImageAdpter;
import com.consumer.utility.DialogUtil;

public class ShopFragment extends Fragment implements AbsListView.OnScrollListener, AbsListView.OnItemClickListener {
	
	private StaggeredGridView mGridView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.shop_fragment, container, false);
		return rootView;
		
		//http://www.technotalkative.com/lazy-productive-android-developer-part-6-staggered-gridview/
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		mGridView = (StaggeredGridView)view.findViewById(R.id.grid_view);
		ShopImageAdpter shopImageAdpter = new ShopImageAdpter(getActivity());
		mGridView.setAdapter(shopImageAdpter);
		
		mGridView.setOnScrollListener(this);
        mGridView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position  == 0) {
			getActivity().startActivity(new Intent(this.getActivity(), ShopItemDetailsActivity.class));
		}else{
			DialogUtil.showOkDialog(getActivity(), "Item is not available");
		}
		
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}
	
}
