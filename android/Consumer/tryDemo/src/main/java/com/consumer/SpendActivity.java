package com.consumer;

import com.consumer.R;
import com.consumer.adapters.DrawerListAdapter;
import com.consumer.common.SingletonClass;
import com.consumer.fragments.OfferListFragment;
import com.consumer.fragments.ShopFragment;
import com.consumer.handler.HeaderLayout;
import com.consumer.widget.SlidingTabLayout;
import com.consumer.widget.SlidingTabLayoutSpend;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SpendActivity extends BaseActivity implements OnClickListener{
	
	private SlidingTabLayoutSpend mSlidingTabLayout;
	private HeaderLayout mHeaderLayout;
	//private SpendHeader mSpendHeader;
	private OfferListFragment mOfferListFragment, mNearOfferListFragment;
	private ShopFragment mShopFragment;

	//private TextView mRestNameTv;
	private LinearLayout mIncludeLl;
	private ImageView mBackIv;
	private ViewPager viewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spend);
		initialization();
		
	}
	
	private void initialization() {
		
		mIncludeLl  = (LinearLayout)findViewById(R.id.topBackLl);
		mBackIv		= (ImageView)mIncludeLl.findViewById(R.id.backIv);
		mBackIv.setOnClickListener(this);
		
		/*mRestNameTv = (TextView)findViewById(R.id.restNameTv);
		SingletonClass.sharedInstance().setTypeface(mRestNameTv, this);*/
		
		mHeaderLayout = new HeaderLayout(this);
		mHeaderLayout.setHeaderIITII(R.drawable.icon_arrow_back_white,
				HeaderLayout.RES_NONE, HeaderLayout.RES_NONE,
				R.drawable.icon_search, R.drawable.icon_menu_white);
		mHeaderLayout.setListener(this, this, this, this);
		
		viewPager = (ViewPager) findViewById(R.id.plan_vp);
		viewPager.setAdapter(new SpendPagesAdapter(getSupportFragmentManager()));
		this.onSubmenuItemClick(this.subMenuIndex);
		
		mSlidingTabLayout = (SlidingTabLayoutSpend) findViewById(R.id.sliding_tabs);
		mSlidingTabLayout.setViewPager(viewPager, this);
		mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int page) {
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
		
		// Handle drawer menu item click
		if (pos != 1) {
			viewPager.setCurrentItem(pos-2,true);
		}else{
			viewPager.setCurrentItem(pos-1,true);
		}
		
		if (mOfferListFragment != null) {
			if (pos == 1) {
    			mOfferListFragment.setConfiguiredForNearByOffers(false);
    		}else if(pos == 2){
    			mOfferListFragment.setConfiguiredForNearByOffers(true);
    		}
			mOfferListFragment.reloadData();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		case R.id.backIv :
			finish();

			break;

		/*case R.id.left_ib:
			finish();

			break;
		case R.id.right_ib:
			//Toast.makeText(this, "Menu button", Toast.LENGTH_SHORT).show();
			this.showDrawer();
			break;
*/		default:
			break;
		}
		
	}
	
	private class SpendPagesAdapter extends FragmentStatePagerAdapter {
		private int tabCount = 2;
		private FragmentManager fManager;
		
		public SpendPagesAdapter(FragmentManager fm) {
			super(fm);
			this.fManager = fm;
		}
		
		@Override
		public Fragment getItem(int pos) {
			
			 switch (pos) {
		        case 0:
		        	if (mOfferListFragment == null) {
		        		mOfferListFragment = (OfferListFragment) fManager.findFragmentById(pos);
		        		if (mOfferListFragment == null) {
		        			mOfferListFragment = new OfferListFragment();
		        			if (subMenuIndex == 1) {
			        			mOfferListFragment.setConfiguiredForNearByOffers(false);
			        		}else if(subMenuIndex == 2){
			        			mOfferListFragment.setConfiguiredForNearByOffers(true);
			        		}
		        		}
					}
		            return mOfferListFragment;
		            
		       /* case 1:
		        	if (mNearOfferListFragment == null) {
		        		mNearOfferListFragment = (OfferListFragment) fManager.findFragmentById(pos);
		        		if (mNearOfferListFragment == null) {
		        			mNearOfferListFragment = new OfferListFragment();
		        			mNearOfferListFragment.setConfiguiredForNearByOffers(true);
						}
		        		
					}
		        	
		            return mNearOfferListFragment;*/
		            
		        case 1:
		        	if (mShopFragment == null) {
		        		mShopFragment = (ShopFragment) fManager.findFragmentById(pos);
		        		if (mShopFragment == null) {
		        			mShopFragment   = new ShopFragment();
						}
		        		
					}
		        	
		            return mShopFragment;
		        }		 
		        return null;
		}		
		
		
		 @Override
	     public CharSequence getPageTitle(int position) {
			 switch (position) {
		        case 0:
		            return getString(R.string.OffersCapital);
		        /*case 1:
		        	return getString(R.string.NearMe);*/
		        case 1:
		        	return getString(R.string.Shop);
		        }		 
		        return null;

	        }
		
		@Override
		public int getCount() {
			return tabCount;
		}
	}

}
