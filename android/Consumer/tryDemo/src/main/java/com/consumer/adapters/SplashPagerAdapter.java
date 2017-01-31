package com.consumer.adapters;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.consumer.R;
import com.consumer.fragments.SplashFragment;
import com.consumer.models.Card;

/**Fragment adapter.**/
public class SplashPagerAdapter extends FragmentStatePagerAdapter {

   // final static int PAGE_COUNT = 3;
    Context mContext;
    private FragmentManager fManager;
    
    public List<Card> mCardList;

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
        this.mCardList = cardList;
    }

    
    
    @Override
    public Fragment getItem(int arg0) {

        Fragment f = fManager.findFragmentById(arg0);
        if (f == null) {
        	f = new SplashFragment(mContext);
		}
        SplashFragment myFragment = (SplashFragment) f;		
        Bundle data = new Bundle();
        data.putInt("current_page", arg0+1);
        data.putSerializable("Card", mCardList.get(arg0));
        data.putSerializable("CardListLength", mCardList.size());
        myFragment.setArguments(data);
    
        return myFragment;
    }

    
    
    public void setList(List<Card> cardList){
    	
    	if(cardList != null){
    		/*mCardList=null;
    		notifyDataSetChanged();*/
	    	mCardList = new ArrayList<Card>();
	    	mCardList.addAll(cardList);
	    	notifyDataSetChanged();
    	}
    }
    
    
    /** Returns the number of pages */
    @Override
    public int getCount() {
    	return null == mCardList ? 0 : mCardList.size();
        //return PAGE_COUNT;
    }
}
