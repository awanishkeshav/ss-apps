package com.consumer;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.consumer.R;
import com.consumer.common.SingletonClass;
import com.consumer.fragments.AnalysisFragment;
import com.consumer.fragments.SecureFragment;
import com.consumer.fragments.TranxListFragment;
import com.consumer.handler.FooterTabHandler;
import com.consumer.handler.HeaderLayout;
import com.consumer.handler.FooterTabHandler.FooterTabListener;
import com.consumer.models.Tag;
import com.consumer.widget.SlidingTabLayout;
import com.consumer.widget.SmartFragmentStatePagerAdapter;

public class ManageSpendActivity extends BaseActivity implements OnClickListener{

	private HeaderLayout mHeaderLayout;
	private LinearLayout mBottomRl;
	// public LinearLayout mheLayout;

	private ArrayList<Tag> mTagList;
	private SlidingTabLayout mSlidingTabLayout;

	// android.support.v4.app.FragmentManager fragmentManager;

	private TranxListFragment mTranxFragment;
	private AnalysisFragment mAnalysisFragment;
	public SecureFragment mSecureFragment;
	public FooterTabHandler mFooterTabHandler;
	private ViewPager viewPager;

	public String activityName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.manage_spend_activity);

		initialization();

		/*mFooterTabHandler = new FooterTabHandler(this);
		mFooterTabHandler.handleTabs(-1, this);
		mFooterTabHandler.setTabs(R.drawable.icon_tags, R.drawable.icon_share,
				FooterTabHandler.RES_NONE, FooterTabHandler.RES_NONE);*/

		/*
		 * int selColorBg = R.color.BlueCardBtn; int unSelColorBg=
		 * R.color.default_circle_indicator_stroke_color; int selClrTxt =
		 * getResources().getColor(R.color.White); int unSelClrTxt =
		 * getResources().getColor(R.color.dark_greyText);
		 */

		/*
		 * mTasksTabHandler.setBgResources(unSelColorBg, selColorBg,
		 * unSelColorBg, selColorBg, unSelColorBg, selColorBg);
		 * mTasksTabHandler.setTextColor(unSelClrTxt, selClrTxt);
		 */
		// mTasksTabHandler.handleTabs(0, this);
	}

	private void initialization() {
		
		mHeaderLayout = new HeaderLayout(this);
		mHeaderLayout.setHeaderIITII(R.drawable.icon_arrow_back_white,
				HeaderLayout.RES_NONE, HeaderLayout.RES_NONE,
				R.drawable.icon_search, R.drawable.icon_menu_white);
		mHeaderLayout.setListener(this, this, this, this);

//		mBottomRl = (LinearLayout) findViewById(R.id.bottom_ll);
		View searchIv = findViewById(R.id.right_extra_ib);
		searchIv.setClickable(true);

		viewPager = (ViewPager) findViewById(R.id.plan_vp);
		
		viewPager.setAdapter(new PlanDivAdapter(getSupportFragmentManager()));
		viewPager.setCurrentItem(this.subMenuIndex-1,false);
		mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
		mSlidingTabLayout.setViewPager(viewPager, this);
		mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageSelected(int page) {
						
						/*if (page == 0) {
							mBottomRl.setVisibility(View.VISIBLE);
						}else{
		//					checkTagPopUp();
							mBottomRl.setVisibility(View.GONE);
						}*/
						setSubmenuIndex(page+1);
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
					}

				});

	}
	
	
	@Override
	public void onSubmenuItemClick(int pos) {
		super.onSubmenuItemClick(pos);
		viewPager.setCurrentItem(this.subMenuIndex-1,true);
	}

	/*public void checkTagPopUp() {
		if (mTranxFragment != null && mTranxFragment.popupWindow.isShowing())
			mTranxFragment.popupWindow.dismiss();
	}*/

	/** .../////.. Listeners..////... */

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.left_ib:
			this.finish();
			
			break;
		case R.id.right_ib:
			
			this.showDrawer();
			break;
		case R.id.right_extra_ib:
			SingletonClass.showTxnSearchActivity(this,null);
			break;
		/*
		 * case R.id.close_iv: mSearchEt.setText(""); break;
		 */
		}
	}

/*	@Override
	public void onTabSelected(View v) {
		
		if (mTranxFragment == null) {
			mTranxFragment = (TranxListFragment) FragmentManager.findFragmentById(pos);
			if (mTranxFragment == null) {
				mTranxFragment = new TranxListFragment();
			}

		}
		
		
		switch (v.getId()) {
		case R.id.tab_1_tv:
			// Toast.makeText(this, "Tab 1", Toast.LENGTH_SHORT).show();
			
			mTranxFragment.setTagListShow(true);
			// mTabSelection = 0;
			// Toast.makeText(this, "LeftTab", Toast.LENGTH_SHORT).show();
			mTranxFragment.getView().setVisibility(View.VISIBLE);
			break;
		case R.id.tab_2_tv:
			SingletonClass.showShareOptionDialog(this);
			// Toast.makeText(this, "Tab 2", Toast.LENGTH_SHORT).show();
			break;
		case R.id.tab_3_tv:
			// Toast.makeText(this, "Tab 3", Toast.LENGTH_SHORT).show();
			break;
		case R.id.tab_4_tv:
			// Toast.makeText(this, "Tab 4", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}

	}*/

	/** .../////.. Adapter ...////... */

	private class PlanDivAdapter extends SmartFragmentStatePagerAdapter {
		private int tabCount = 3;
		private FragmentManager fManager;

		public PlanDivAdapter(FragmentManager fm) {
			super(fm);
			this.fManager = fm;
		}

		@Override
		public Fragment getItem(int pos) {

			switch (pos) {
			case 0:
				if (mTranxFragment == null) {
					mTranxFragment = (TranxListFragment) fManager
							.findFragmentById(pos);
					if (mTranxFragment == null) {
						mTranxFragment = new TranxListFragment();
					}

				}
				return mTranxFragment;

			case 1:
				if (mAnalysisFragment == null) {
					mAnalysisFragment = (AnalysisFragment) fManager
							.findFragmentById(pos);
					if (mAnalysisFragment == null) {
						mAnalysisFragment = new AnalysisFragment();
					}

				}

				return mAnalysisFragment;

			case 2:
				if (mSecureFragment == null) {
					mSecureFragment = (SecureFragment) fManager
							.findFragmentById(pos);
					if (mSecureFragment == null) {
						mSecureFragment = new SecureFragment();
					}

				}

				return mSecureFragment;
			}
			return null;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.Transactions);
			case 1:
				return getString(R.string.Analysis);
			case 2:
				return getString(R.string.Secure);
			}
			return null;

		}

		@Override
		public int getCount() {
			return tabCount;
		}
	}
}
