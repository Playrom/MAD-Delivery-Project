package it.polito.justorder_restaurant;

import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import it.polito.justorder_framework.abstract_activities.ActivityAbstractWithToolbar;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.DelivererDistance;
import it.polito.justorder_restaurant.fragments.DeliverListFragment;
import it.polito.justorder_restaurant.fragments.RestaurantDelivererAssignViewPagerAdapter;
import it.polito.justorder_restaurant.fragments.dummy.DummyContent;

public class RestaurantDelivererAssignActivity extends ActivityAbstractWithToolbar implements DeliverListFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_deliverer_assign_layout);
        this.setupActivity();
    }


    @Override
    protected void setupActivity() {
        super.setupActivity();

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

        // Create an adapter that knows which fragment should be shown on each page
        String restaurantKey = Database.INSTANCE.getCurrent_User().getRestaurantKey();
        RestaurantDelivererAssignViewPagerAdapter adapter = new RestaurantDelivererAssignViewPagerAdapter(this, getSupportFragmentManager(), restaurantKey);

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        this.reloadData();
    }

    @Override
    public void onListFragmentInteraction(DelivererDistance item) {

    }
}
