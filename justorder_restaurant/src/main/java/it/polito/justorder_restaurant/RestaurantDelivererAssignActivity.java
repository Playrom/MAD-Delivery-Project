package it.polito.justorder_restaurant;

import android.content.Intent;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.firebase.geofire.GeoLocation;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import it.polito.justorder_framework.abstract_activities.ActivityAbstractWithToolbar;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.DelivererDistance;
import it.polito.justorder_restaurant.fragments.DeliverListFragment;
import it.polito.justorder_restaurant.fragments.DeliverListItemRecyclerViewAdapter;
import it.polito.justorder_restaurant.fragments.RestaurantDelivererAssignViewPagerAdapter;
import kotlin.Unit;

public class RestaurantDelivererAssignActivity extends ActivityAbstractWithToolbar implements ConfirmOrderInterface  {

    private String restaurantKey;
    private List<DelivererDistance> delivererDistances = new ArrayList<>();
    private GeoLocation restaurantLocation;

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
        this.restaurantKey = Database.INSTANCE.getCurrent_User().getRestaurantKey();
        Database.INSTANCE.getGeodata().getDeliverersNear(this.restaurantKey, (deliverers, restaurantLocation) -> {
            this.delivererDistances.clear();
            this.delivererDistances.addAll(deliverers);
            this.restaurantLocation = restaurantLocation;

            RestaurantDelivererAssignViewPagerAdapter adapter = new RestaurantDelivererAssignViewPagerAdapter(this, getSupportFragmentManager(), restaurantKey, delivererDistances, restaurantLocation);
            viewPager.setAdapter(adapter);

            return Unit.INSTANCE;
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        this.reloadData();
    }

    public void confirmDeliverer(String delivererKey) {
        Intent i = new Intent();
        i.putExtra("deliverer_key", delivererKey);
        setResult(RESULT_OK, i);
        finish();
    }

}