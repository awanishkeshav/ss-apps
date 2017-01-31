package com.merchant.handler;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.merchant.R;

public class FooterTabHandler implements OnClickListener {
	private TextView mTab1Tv, mTab2Tv, mTab3Tv, mTab4Tv;
	private ImageView mIv1, mIv2, mIv3, mIv4;
	public RelativeLayout mRl1, mRl2, mRl3, mRl4;
	private FooterTabListener mFooterTabListener;
	
	public View rootLayout;
	
	public static final int RES_NONE = 0;

	public FooterTabHandler(Context context) {
		init(((Activity) context).findViewById(R.id.tab_footer));
	
	}
	
	private void init(View paramRootLayout){
		
		rootLayout = paramRootLayout;
		
		mTab1Tv = (TextView) rootLayout.findViewById(R.id.tab_1_tv);
		mTab2Tv = (TextView) rootLayout.findViewById(R.id.tab_2_tv);
		mTab3Tv = (TextView) rootLayout.findViewById(R.id.tab_3_tv);
		mTab4Tv = (TextView) rootLayout.findViewById(R.id.tab_4_tv);
		
		mIv1 = (ImageView)rootLayout.findViewById(R.id.imageView1);
		mIv2 = (ImageView)rootLayout.findViewById(R.id.imageView2);
		mIv3 = (ImageView)rootLayout.findViewById(R.id.imageView3);
		mIv4 = (ImageView)rootLayout.findViewById(R.id.imageView4);
		
		mRl1 = (RelativeLayout)rootLayout.findViewById(R.id.tab_1Rl);
		mRl2 = (RelativeLayout)rootLayout.findViewById(R.id.tab_2Rl);
		mRl3 = (RelativeLayout)rootLayout.findViewById(R.id.tab_3Rl);
		mRl4 = (RelativeLayout)rootLayout.findViewById(R.id.tab_4Rl);

		activateTab();
	}
	
	public FooterTabHandler(View rootLayout) {
		init(rootLayout);
	}
	
	public void removeLastTab(){
		mRl4.setVisibility(View.GONE);
	}
	
	public void setTabs(int id1, int id2, int id3, int id4){
		
		if(id1 !=RES_NONE)
			mIv1.setImageResource(id1);
		else
			mRl1.setVisibility(View.INVISIBLE);
		
		if(id2 !=RES_NONE)
			mIv2.setImageResource(id2);
		else
			mRl2.setVisibility(View.INVISIBLE);
		
		if(id3 !=RES_NONE)
			mIv3.setImageResource(id3);
		else
			mRl3.setVisibility(View.INVISIBLE);
		
		if(id4 !=RES_NONE)
			mIv4.setImageResource(id4);
		else
			mRl4.setVisibility(View.INVISIBLE);
				
	}
	
	public void handleTabs(int defaultTab, FooterTabListener listener) {
		
		this.mFooterTabListener = listener;
		if (null != mFooterTabListener && null != mRl1 && null != mRl2 && null != mRl3 && null != mRl4 ) {
			mRl1.setOnClickListener(this);
			mRl2.setOnClickListener(this);
			mRl3.setOnClickListener(this);
			mRl4.setOnClickListener(this);

			switch (defaultTab) {
			case 0:
				onClick(mRl1);
				break;
			case 1:
				onClick(mRl2);
				break;
			case 2:
				onClick(mRl3);
				break;
			case 3:
				onClick(mRl4);
				break;
			default:
				break;
			}
		}
	}
	
	public void setTabTitles(String t1, String t2, String t3, String t4){
		mTab1Tv.setText(t1);
		mTab2Tv.setText(t2);
		mTab3Tv.setText(t3);
		mTab4Tv.setText(t4);
	}

	public interface FooterTabListener {
		void onTabSelected(View v);
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.tab_1Rl:
			mFooterTabListener.onTabSelected(mTab1Tv);
			break;
		case R.id.tab_2Rl:
			mFooterTabListener.onTabSelected(mTab2Tv);
			break;
		case R.id.tab_3Rl:
			mFooterTabListener.onTabSelected(mTab3Tv);
			break;
		case R.id.tab_4Rl:
			mFooterTabListener.onTabSelected(mTab4Tv);
			break;
		
		default:
			break;
		}
	}

	public void activateTab() {
		
		mIv1.setImageResource(R.drawable.icon_tags);
		mIv2.setImageResource(R.drawable.icon_tags);
		mIv3.setImageResource(R.drawable.icon_tags);
		mIv4.setImageResource(R.drawable.icon_tags);
		
	}

}
