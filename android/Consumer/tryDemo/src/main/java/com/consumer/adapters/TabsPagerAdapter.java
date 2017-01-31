package com.consumer.adapters;

import com.consumer.fragments.AlertsFragment;
import com.consumer.fragments.HomeFragment;
import com.consumer.fragments.OfferListFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter{

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
		
	}
	
	@Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            return new HomeFragment();
        case 1:
            return new OfferListFragment();
        case 2:
            return new AlertsFragment();
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }
 

}
