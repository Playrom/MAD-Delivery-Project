package it.polito.justorder_restaurant;

import android.content.Intent;

import java.util.ArrayList;
import java.util.Map;

import it.polito.justorder_framework.common_activities.OrdersListActivity;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Restaurant;
import kotlin.Unit;

public class OrdersRestaurantListActivity extends OrdersListActivity {

    protected Restaurant restaurant;

    @Override
    protected void initDataSource() {
        Intent i = getIntent();
        this.restaurant = (Restaurant) i.getSerializableExtra("restaurant");
        Map<String, Boolean> restaurantOrders = this.restaurant.getOrders();

        Database.INSTANCE.getOrders().getWithIds(new ArrayList<>(restaurant.getOrders().keySet()), orders1 -> {
            this.orders.clear();
            this.orders.addAll(orders1);
            this.reloadViews();
            return Unit.INSTANCE;
        });

        Database.INSTANCE.getRestaurants().get(this.restaurant.getKeyId(), (restaurant1 -> {
            Database.INSTANCE.getOrders().getWithIds(new ArrayList<>(restaurant1.getOrders().keySet()), orders1 -> {
                this.orders.clear();
                this.orders.addAll(orders1);
                this.reloadViews();
                return Unit.INSTANCE;
            });
            return Unit.INSTANCE;
        }));
    }
}
