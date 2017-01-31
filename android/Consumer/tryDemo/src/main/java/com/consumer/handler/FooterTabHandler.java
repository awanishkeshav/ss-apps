package com.consumer.handler;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.consumer.R;

public class FooterTabHandler implements OnClickListener {
	private TextView mTab1Tv, mTab2Tv, mTab3Tv, mTab4Tv, mTab5Tv; //, mTab2Noti, mTab4Noti;
	private ImageView mIv1, mIv2, mIv3, mIv4;
	public RelativeLayout mRl1, mRl2, mRl3, mRl4;
	private FooterTabListener mFooterTabListener;
	
	private int drawableIntrinsicDimen;
	public static final int RES_NONE = 0;

	public FooterTabHandler(Context context) {
		init(((Activity) context).findViewById(R.id.tab_footer));
	
	}
	
	private void init(View rootLayout){
		
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
	
	

	public TextView getmTab1Tv() {
		return mTab1Tv;
	}



	public TextView getChatView() {
		return mTab5Tv;
	}

	public void setTabs(int id1, int id2, int id3, int id4){
		
		if(id1 !=RES_NONE)
			mIv1.setBackgroundResource(id1);
		else
			mRl1.setVisibility(View.INVISIBLE);
		
		if(id2 !=RES_NONE)
			mIv2.setBackgroundResource(id2);
		else
			mRl2.setVisibility(View.INVISIBLE);
		
		if(id3 !=RES_NONE)
			mIv3.setBackgroundResource(id3);
		else
			mRl3.setVisibility(View.INVISIBLE);
		
		if(id4 !=RES_NONE)
			mIv4.setBackgroundResource(id4);
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
		
		mIv1.setBackgroundResource(R.drawable.icon_tags);
		mIv2.setBackgroundResource(R.drawable.icon_share);
		mIv3.setBackgroundResource(R.drawable.icon_review_white);
		mIv4.setBackgroundResource(R.drawable.icon_report);
		
		/*//mTab1Tv.setTextColor(getColor(R.color.LtGrey));
		mTab1Tv.setCompoundDrawables(null, getDrawable(R.drawable.icon_tags), null, null);
		//mTab2Tv.setTextColor(getColor(R.color.LtGrey));
		mTab2Tv.setCompoundDrawables(null, getDrawable(R.drawable.icon_share), null, null);
		//mTab3Tv.setTextColor(getColor(R.color.LtGrey));
		mTab3Tv.setCompoundDrawables(null, getDrawable(R.drawable.icon_review_white), null, null);
		//mTab4Tv.setTextColor(getColor(R.color.LtGrey));
		mTab4Tv.setCompoundDrawables(null, getDrawable(R.drawable.icon_report), null, null);*/
		

		/*switch (mSelTabId) {
		case 0:
			//mTab1Tv.setTextColor(getColor(R.color.LtBlue));
			mTab1Tv.setCompoundDrawables(null, getDrawable(R.drawable.icon_tags), null, null);
			break;
		case 1:
			//mTab2Tv.setTextColor(getColor(R.color.LtBlue));
			mTab2Tv.setCompoundDrawables(null, getDrawable(R.drawable.icon_share), null, null);
			break;
		case 2:
			//mTab3Tv.setTextColor(getColor(R.color.LtBlue));
			mTab3Tv.setCompoundDrawables(null, getDrawable(R.drawable.icon_review_blue), null, null);
			break;
		case 3:
			//mTab4Tv.setTextColor(getColor(R.color.LtBlue));
			mTab4Tv.setCompoundDrawables(null, getDrawable(R.drawable.icon_report), null, null);
			break;
	
		default:
			break;
		}*/
	}

	/*private int getColor(int id) {
		return mTab1Tv.getContext().getResources().getColor(id);
	}*/

	public Drawable getDrawable(int id) {
		Drawable drawable = mTab1Tv.getContext().getResources().getDrawable(id);
		drawable.setBounds(0, 0, drawableIntrinsicDimen, drawableIntrinsicDimen);
		return drawable;
	}

}
