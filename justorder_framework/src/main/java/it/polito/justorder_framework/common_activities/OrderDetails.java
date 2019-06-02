package it.polito.justorder_framework.common_activities;

import it.polito.justorder_framework.R;
import it.polito.justorder_framework.abstract_activities.AbstractViewerWithImagePickerActivityAndToolbar;
import it.polito.justorder_framework.abstract_activities.ActivityAbstractWithToolbar;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Deliverer;
import it.polito.justorder_framework.model.Order;
import it.polito.justorder_framework.model.OrderProduct;
import it.polito.justorder_framework.model.Product;
import it.polito.justorder_framework.model.Restaurant;
import it.polito.justorder_framework.model.User;
import kotlin.Unit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OrderDetails extends ActivityAbstractWithToolbar {

    private Order order;
    public User deliverer;
    private User user;
    private Restaurant restaurant;
    private TextView orderIdTextField, userTextField, addressTextField, priceTextField, timestampTextField, riderTextField, productsTextField;
    private String productString;
    private Map<Product,Double> products = new HashMap<>();
    private Integer x = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        this.setupActivity();
    }


    @Override
    protected void setupActivity() {
        super.setupActivity();
        //orderIdTextField = findViewById(R.id.orderIdTextField);
        userTextField = findViewById(R.id.userTextField);
        addressTextField = findViewById(R.id.addressTextField);
        priceTextField = findViewById(R.id.priceTextField);
        timestampTextField = findViewById(R.id.timestampTextField);
        riderTextField = findViewById(R.id.riderTextField);
        productsTextField = findViewById(R.id.productsTextField);
        this.reloadData();
    }



    @Override
    protected void reloadData() {
        super.reloadData();
        Intent i = getIntent();
        this.order = (Order) i.getSerializableExtra("order");
        if(this.order == null){
            this.order = new Order();
        }else{
            if(this.order.getUser() != null){
                Database.INSTANCE.getUsers().get(this.order.getUser(), (user1 -> {
                    this.user = user1;
                    this.reloadViews();
                    return Unit.INSTANCE;
                }));
            }

            if(this.order.getDeliverer() != null){
                Database.INSTANCE.getDeliverers().get(this.order.getDeliverer(), (deliverer1 -> {
                    Database.INSTANCE.getUsers().get(deliverer1.getUserKey(), user1 -> {
                        this.deliverer = user1;
                        this.reloadViews();
                        return Unit.INSTANCE;
                    });
                    return Unit.INSTANCE;
                }));
            }

            Database.INSTANCE.getRestaurants().get(this.order.getRestaurant(), (restaurant -> {
                this.restaurant=restaurant;
                if(x!=1) {
                    for (Map.Entry<String, OrderProduct> entry : this.order.getProducts().entrySet()) {
                        this.products.put(restaurant.getProducts().get(entry.getValue().getProductKey()), entry.getValue().getQuantity());
                    }
                    x=1;
                }
                this.reloadViews();
                return Unit.INSTANCE;
            }));


        }
        this.reloadViews();
    }

    @Override
    protected void reloadViews() {
        super.reloadViews();

        if(this.user != null) {
            userTextField.setText("User Name: " + this.user.getName());
        }
        addressTextField.setText("Delivery Address: " + order.getUserAddress());
        priceTextField.setText("Order Cost: " + new Double(order.getPrice()).toString() + " €");
        String date = DateFormat.format("dd/MM/yyyy - hh:mm", order.getTimestamp()).toString();
        timestampTextField.setText("Date and time: " + date);

        if(this.deliverer != null) {
            riderTextField.setText("Deliverer Name: "+ this.deliverer.getName());
        } else {
            riderTextField.setText("Deliverer not yet assigned");
        }

        if(products.size() > 0){
            productString = createProductString();
            productsTextField.setText(Html.fromHtml(this.productString));
        }

        if(this.order != null){
            this.actionBar.setTitle("Order Details");
        }
    }

    private String createProductString() {

        String productString = "";
        for(Map.Entry<Product, Double> entry : products.entrySet()){
           // productString += entry.getValue().intValue() + " x " + entry.getKey().getName() + "\n";
            productString += "&#8226; " +entry.getKey().getName() + " x " + entry.getValue().intValue() +"<br/>\n";
        }

        return productString;
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("order", order);
        outState.putSerializable("user", user);
        outState.putSerializable("deliverer", deliverer);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        order = (Order) savedInstanceState.getSerializable("order");
        user = (User) savedInstanceState.getSerializable("user");
        deliverer = (User) savedInstanceState.getSerializable("deliverer");

        reloadViews();
    }
}


