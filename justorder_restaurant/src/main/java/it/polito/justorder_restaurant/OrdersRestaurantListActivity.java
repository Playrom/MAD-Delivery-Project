package it.polito.justorder_restaurant;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Map;

import it.polito.justorder_framework.common_activities.OrderDetails;
import it.polito.justorder_framework.common_activities.OrdersListActivity;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Order;
import it.polito.justorder_framework.model.Restaurant;
import kotlin.Unit;

public class OrdersRestaurantListActivity extends OrdersListActivity {

    protected Restaurant restaurant;

    @Override
    protected void setupActivity() {
        super.setupActivity();

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order entry = (Order) parent.getAdapter().getItem(position);

                Intent intent;
                String state = entry.getState();
                if (state!=null && state.equals("pending")) {
                    intent = new Intent(OrdersRestaurantListActivity.this, OrderToConfirm.class);
                }else {
                    intent = new Intent(OrdersRestaurantListActivity.this, OrderDetails.class);
                }

                intent.putExtra("order", entry);
                tapped = position;
                startActivityForResult(intent, 1);
            }
        });
    }

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

        Database.INSTANCE.getRestaurants().get(this.restaurant.getKeyId(), true, (restaurant1 -> {
            Database.INSTANCE.getOrders().getWithIds(new ArrayList<>(restaurant1.getOrders().keySet()), orders1 -> {
                this.orders.clear();
                this.orders.addAll(orders1);
                this.reloadViews();
                return Unit.INSTANCE;
            });
            return Unit.INSTANCE;
        }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.initDataSource();
    }
}
