package com.merchant.adapters;


import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.merchant.fragments.SplashFragment;
import com.merchant.models.Card;

/**Fragment adapter.**/
public class SplashPagerAdapter extends FragmentStatePagerAdapter {

   // final static int PAGE_COUNT = 3;
    Context mContext;
    private FragmentManager fManager;

    /** Constructor of the class */
    public SplashPagerAdapter(android.support.v4.app.FragmentManager fm, Context mContext) {

        super(fm);
        fManager = fm;
        this.mContext = mContext;

    }

    public SplashPagerAdapter(android.support.v4.app.FragmentManager fm, Context mContext, List<Card> cardList) {

        super(fm);
        fManager = fm;
        this.mContext = mContext;
    }

    
    
    @Override
    public Fragment getItem(int arg0) {

    	Fragment f = fManager.findFragmentById(arg0);
        if (f == null) {
        	f = new SplashFragment(mContext);
		}
        SplashFragment myFragment = (SplashFragment) f;	
        return myFragment;
    } 
    /** Returns the number of pages */
    @Override
    public int getCount() {
    	return 1;
        //return PAGE_COUNT;
    }
}
