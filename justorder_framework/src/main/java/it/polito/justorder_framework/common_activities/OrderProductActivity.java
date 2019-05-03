package it.polito.justorder_framework.common_activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import it.polito.justorder_framework.R;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Order;
import it.polito.justorder_framework.model.Product;
import it.polito.justorder_framework.model.Restaurant;
import it.polito.justorder_framework.model.User;
import kotlin.Unit;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class OrderProductActivity extends Activity {

    protected EditText et;
    protected Button b;
    protected Restaurant restaurant;
    protected Product product;
    protected User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        product = (Product) intent.getSerializableExtra("product");
        restaurant = (Restaurant) intent.getSerializableExtra("restaurant");
        Database.INSTANCE.loadCurrentUser(() -> {
            user = Database.INSTANCE.getCurrent_User();
            return Unit.INSTANCE;
        });
        setContentView(R.layout.order_product_activity);
        et = (EditText) findViewById(R.id.quantity);
        b = (Button) findViewById(R.id.order_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qty = et.getText().toString();
                Order order = new Order();
                order.setState("pending");
                if(restaurant != null) {
                    order.setRestaurantName(restaurant.getName());
                    order.setRestaurantAddress(restaurant.getAddress());
                    order.setRestaurant(restaurant.getKeyId());
                }
                if(product != null) {
                    Double price = Integer.parseInt(qty) * product.getCost();
                    order.setPrice(price);
                    Map<String, Integer> prod = new HashMap<String, Integer>();
                    prod.put(product.getKeyId(), Integer.parseInt(qty));
                    order.setProducts(prod);
                }
                if(user != null) {
                    order.setUserName(user.getName());
                    order.setUserAddress(user.getAddress());
                    order.setUser(user.getKeyId());
                }
                //save order into database
                //save order into restaurant
                Database.INSTANCE.getOrders().save(order);
                Map<String, Boolean> m = restaurant.getOrders();
                m.put(order.getKeyId(), true);
                restaurant.setOrders(m);
                Database.INSTANCE.getRestaurants().save(restaurant);
                //save order into user
                Map<String, Boolean> m1 = user.getOrders();
                m1.put(order.getKeyId(), true);
                user.setOrders(m1);
                //show confirm
                Toast toast = Toast.makeText(getApplicationContext(), "Ordine Inserito", Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }
        });
    }
}
