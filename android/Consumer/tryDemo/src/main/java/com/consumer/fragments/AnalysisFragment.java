package com.consumer.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.consumer.R;
import com.consumer.handler.CustomViewPager;
import com.consumer.models.AnalysisTab;

public class AnalysisFragment extends Fragment{
	
	private ArrayList<Fragment> slideList;
	private ArrayList<AnalysisTab> mSlideTabList;
	private final int NUM_PAGES =3;
	private int selPosition;
	
	private CustomViewPager viewPager; 
	private LinearLayout mTab1Ll, mTab2Ll, mTab3Ll, mFragmentLl;
	
	
	//private ScreenSlidePagerAdapter pagerAdapter;
	private ViewPager.OnPageChangeListener mPageChangeListener;
	
	private FragmentManager fManager;
	private AnalysisSummaryFragment mTimeFragment;
	private CompareFragment mComapareFragment;
	private TrendsFragment mTrendFragment;
	
	public ArrayAdapter<String> mDurationAdapter;
	public String[] mDurationArr = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.anlysis_fragment,container, false);
			return rootView;
			//123  http://blog.pboos.ch/android-pagertabstrip-viewpager/
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
				
		setTabDummyList();
		initialization(view);

		mDurationArr = new String[]{"This Month", "Last Month", "Last 3 Months", "Last Year"};
		mDurationAdapter = new ArrayAdapter<String>(getActivity(), R.layout.duration_row, mDurationArr);
		spinTab2.setAdapter(mDurationAdapter);
		
	  /*  viewPager = (CustomViewPager) view.findViewById(R.id.analysis_vp);
	    pagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager());
	    viewPager.setAdapter(pagerAdapter);
	    
	    viewPager.setPagingEnabled(false);

	    viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int pos) {
				selPosition = pos % NUM_PAGES;
				setTabsData(selPosition);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {	}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {	}
			
		});*/
	        
	}

	
	ImageView image1, image2, image3;
	TextView tv1, tv2, tv3;
	View view1, view2, view3;
	Spinner spinTab1, spinTab2, spinTab3;
	
	private void initialization(View view){
		
		//Fragments Initialization
		fManager 		= getFragmentManager();
		mTimeFragment 	= new AnalysisSummaryFragment();
		mComapareFragment = new CompareFragment();
		mTrendFragment 	= new TrendsFragment();
		
		// Components..
		mFragmentLl= (LinearLayout) view.findViewById(R.id.analysis_LL);
		mTab1Ll = (LinearLayout) view.findViewById(R.id.tab1);
		mTab2Ll = (LinearLayout) view.findViewById(R.id.tab2);
		mTab3Ll = (LinearLayout) view.findViewById(R.id.tab3);
		
		image1 	= (ImageView)mTab1Ll.findViewById(R.id.tab_iv);
		tv1	 	= (TextView)mTab1Ll.findViewById(R.id.tab_tv);
		spinTab1= (Spinner)mTab1Ll.findViewById(R.id.spinnerTab);
		
		image2 	= (ImageView)mTab2Ll.findViewById(R.id.tab_iv);
		tv2	 	= (TextView)mTab2Ll.findViewById(R.id.tab_tv);
		spinTab2= (Spinner)mTab2Ll.findViewById(R.id.spinnerTab);
		
		image3 = (ImageView)mTab3Ll.findViewById(R.id.tab_iv);
		tv3	 = (TextView)mTab3Ll.findViewById(R.id.tab_tv);
		spinTab3= (Spinner)mTab3Ll.findViewById(R.id.spinnerTab);
		
		
		mTab1Ll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(selPosition != 0)
					selPosition = selPosition-1;
				else 
					selPosition = 2;
				
				setTabsData(selPosition);
				//viewPager.setCurrentItem(selPosition, true);
			}
		});
		
		mTab3Ll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(selPosition !=2)
					selPosition = selPosition+1;
				else
					selPosition = 0;
				
				//viewPager.setCurrentItem(selPosition, true);
				setTabsData(selPosition);
				
			}
		});
		
		setTabsData(0);
	}
	
	private void setTabsData(int pos){
	
		AnalysisTab tab;
	 	//int pos = position % NUM_PAGES;
	 	
	 	tv1.setVisibility(View.INVISIBLE);
	 	tv2.setVisibility(View.VISIBLE); 
	 	tv3.setVisibility(View.GONE);
	 	
	 	spinTab1.setVisibility(View.GONE);
	 	spinTab2.setVisibility(View.GONE);
	 	spinTab3.setVisibility(View.GONE);
	 	
		switch (pos) {
		
		case 0:
			tab = mSlideTabList.get(2);
			tv1.setText(tab.tabName);  
			image1.setImageResource(tab.tabImage);
			
			tab = mSlideTabList.get(0);
			//tv2.setText(tab.tabName);
			tv2.setVisibility(View.GONE);
			spinTab2.setVisibility(View.VISIBLE);
			image2.setImageResource(tab.tabImage);
			
			tab = mSlideTabList.get(1);
			tv3.setText(tab.tabName);  
			image3.setImageResource(tab.tabImage);
			
			fManager.beginTransaction().replace(R.id.analysis_LL, mTimeFragment).commit();
			break;
		
		case 1:
			tab = mSlideTabList.get(0);
			tv1.setText(tab.tabName);    
			image1.setImageResource(tab.tabImage);
			
			tab = mSlideTabList.get(1);
			tv2.setText(tab.tabName); 
			image2.setImageResource(tab.tabImage);
			
			tab = mSlideTabList.get(2);
			tv3.setText(tab.tabName);
			image3.setImageResource(tab.tabImage);
			
			fManager.beginTransaction().replace(R.id.analysis_LL, mComapareFragment).commit();
			break;
		case 2:
			tab = mSlideTabList.get(1);
			tv1.setText(tab.tabName); 
			image1.setImageResource(tab.tabImage);   
			
			tab = mSlideTabList.get(2);
			tv2.setText(tab.tabName); 
			image2.setImageResource(tab.tabImage);
			
			tab = mSlideTabList.get(0);
			tv3.setText(tab.tabName); 
			image3.setImageResource(tab.tabImage);
			
			fManager.beginTransaction().replace(R.id.analysis_LL, mTrendFragment).commit();
			break;
		}
	}
	    
	private void setTabDummyList(){
		
		mSlideTabList = new ArrayList<AnalysisTab>();
		
		AnalysisTab tab1;
		
		tab1 = new AnalysisTab("Time", R.drawable.donut_chart);
		mSlideTabList.add(tab1);
		
		tab1 = new AnalysisTab("Compare", R.drawable.icon_bar);
		mSlideTabList.add(tab1);
		
		tab1= new AnalysisTab("Trends", R.drawable.trends);
		mSlideTabList.add(tab1);
		
	}
	
  
	/*private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		  
		  private int tabCount = 3;
		  private FragmentManager fManager;

		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
			this.fManager = fm;
			// TODO Auto-generated constructor stub
		}



	        public ScreenSlidePagerAdapter(FragmentManager fm, ArrayList<Fragment> slideList) {
	            super(fm, slideList);
	        }

			@Override
			protected Fragment getFragmentForItem(Fragment fragment) {
				// TODO Auto-generated method stub
				return fragment;
			}

	        @Override
	        public Fragment getItem(int position) {
	            int pos = position % NUM_PAGES;
	            
	            //setTabsData(pos);
	            
	            switch (pos) {
	            
	            case 0:
					mTimeFragment = new AnalysisSummaryFragment();
					return mTimeFragment;
	            
				case 1:
					
					
					mComapareFragment = new CompareFragment();
					if (mTimeFragment == null) {
						mTimeFragment = (TimeAnaFragment) fManager.findFragmentById(pos);
						if (mTimeFragment == null) {
							mTimeFragment = new TimeAnaFragment();
						}
					}
					return mComapareFragment;

				case 2:
					mTrendFragment = new TrendsFragment();
					
					if (mComapareFragment == null) {
						mComapareFragment = (CompareAnaFragment) fManager
								.findFragmentById(pos);
						if (mComapareFragment == null) {
							mComapareFragment = new CompareAnaFragment();
						}
					}

					return mTrendFragment;

				
				}
				return null;
	        }

	        @Override
	        public int getCount() {
	            return NUM_PAGES;
	        }      
	        
	    }*/
}
