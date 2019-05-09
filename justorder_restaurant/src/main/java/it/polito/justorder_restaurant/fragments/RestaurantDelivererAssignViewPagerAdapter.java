package it.polito.justorder_restaurant.fragments;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.List;

import it.polito.justorder_framework.model.DelivererDistance;
import it.polito.justorder_restaurant.R;

public class RestaurantDelivererAssignViewPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private String restaurantKey;
    private List<DelivererDistance> distanceList;
    private GeoLocation restaurantLocation;

    public RestaurantDelivererAssignViewPagerAdapter(Context context, FragmentManager fm, String restaurantKey, List<DelivererDistance> distanceList, GeoLocation restaurantLocation) {
        super(fm);
        mContext = context;
        this.restaurantKey = restaurantKey;
        this.distanceList = distanceList;
        this.restaurantLocation = restaurantLocation;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return DeliverListFragment.newInstance(1, restaurantKey, distanceList, restaurantLocation);
        }else{
            return DeliversMapFragment.newInstance(restaurantKey, distanceList, restaurantLocation);
        }
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
                return mContext.getString(R.string.map);
        }
    }

}