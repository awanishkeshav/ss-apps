package com.merchant.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.merchant.R;
import com.merchant.common.PreferanceUtil;
import com.merchant.common.SingletonClass;
import com.merchant.dialogs.CardEntryDialog;
import com.merchant.HomeActivity;

/**
 * setting image in adaptor of fragment.
 */
@SuppressLint("ValidFragment")
public class SplashFragment extends Fragment {

	
	public TextView mTitleTv, mSubtitleTv, mMerchantNameTv;
	private View v;
	private RelativeLayout mTopRl;
	
	Context mContext = null;
	
	/*@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		

	}*/

	public SplashFragment() {
		// empty constructor
	}

	public SplashFragment(Context mContext) {

		super();
		this.mContext = mContext;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.splash_fragment_row, container,false);
		
		mTitleTv		= (TextView) v.findViewById(R.id.titleTV);
		mSubtitleTv		= (TextView) v.findViewById(R.id.subtitleTV);
        mMerchantNameTv = (TextView) v.findViewById(R.id.merchantNameTv);

		mTopRl			= (RelativeLayout) v.findViewById(R.id.cardBgRl);
		
		boolean isMerchantRegisterd = SingletonClass.isMerchantRegistered(getActivity());
		
		if(!isMerchantRegisterd){
			mTopRl.setBackgroundResource(R.drawable.selector_splashcard_add);
			mTitleTv.setVisibility(View.GONE);
            mMerchantNameTv.setVisibility(View.GONE);
			mSubtitleTv.setVisibility(View.VISIBLE);

			mTopRl.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					CardEntryDialog cardEntryDialog = new CardEntryDialog(getActivity());
					
					cardEntryDialog.show();
					
				}
			});
			
		}else{
			mTitleTv.setVisibility(View.VISIBLE);
            mMerchantNameTv.setVisibility(View.VISIBLE);
            mMerchantNameTv.setText(new PreferanceUtil(getActivity()).getMerchantName());
			mSubtitleTv.setVisibility(View.GONE);
			mTopRl.setBackgroundResource(R.drawable.selector_splashcard_3);
			mTopRl.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Intent intent = new Intent(getActivity(), HomeActivity.class);
					startActivity(intent);
					
					
				}
			});
		}
		return v;
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}
}
