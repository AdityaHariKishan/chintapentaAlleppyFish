package com.ss.android.allepyfish.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ss.android.allepyfish.fragments.MyProductsUploadsFragment;
import com.ss.android.allepyfish.fragments.office_boys.ContactManagers;
import com.ss.android.allepyfish.fragments.office_boys.LatestOrdersFragment;

/**
 * Created by dell on 5/27/2017.
 */

public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                ContactManagers tab1 = new ContactManagers();
                return tab1;
            case 1:
                LatestOrdersFragment tab2 = new LatestOrdersFragment();
                return tab2;
            case 2:
//                MyGoodsSuppliedFragment tab3 = new MyGoodsSuppliedFragment();
                MyProductsUploadsFragment tab3 = new MyProductsUploadsFragment();
                return tab3;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    public int getCount() {
        return tabCount;
    }
}
