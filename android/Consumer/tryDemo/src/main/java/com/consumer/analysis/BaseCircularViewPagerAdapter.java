package com.consumer.analysis;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * User: tobiasbuchholz
 * Date: 18.09.14 | Time: 13:18
 */
public abstract class BaseCircularViewPagerAdapter<Fragement> extends FragmentStatePagerAdapter {
    private List<Fragment> mFragment;

    public BaseCircularViewPagerAdapter(final FragmentManager fragmentManager, final List<Fragment> items) {
        super(fragmentManager);
        mFragment = items;
    }

    protected abstract Fragment getFragmentForItem(final Fragment fragment);

    @Override
    public Fragment getItem(final int position) {
        final int itemsSize = mFragment.size();
        if(position == 0) {
            return getFragmentForItem(mFragment.get(itemsSize - 1));
        } else if(position == itemsSize + 1) {
            return getFragmentForItem(mFragment.get(0));
        } else {
            return getFragmentForItem(mFragment.get(position - 1));
        }
    }

    @Override
    public int getCount() {
        final int itemsSize = mFragment.size();
        return itemsSize > 1 ? itemsSize + 2 : itemsSize;
    }

    public int getCountWithoutFakePages() {
        return mFragment.size();
    }

    public void setItems(final List<Fragement> items) {
        mFragment = (List<Fragment>) items;
        notifyDataSetChanged();
    }
}
