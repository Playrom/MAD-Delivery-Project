package it.polito.justorder_restaurant.fragments;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import it.polito.justorder_restaurant.R;

public class RestaurantDelivererAssignViewPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private String restaurantKey;

    public RestaurantDelivererAssignViewPagerAdapter(Context context, FragmentManager fm, String restaurantKey) {
        super(fm);
        mContext = context;
        this.restaurantKey = restaurantKey;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        return DeliverListFragment.newInstance(1, restaurantKey);
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 2;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.list);
            default:
                return null;
        }
    }

}