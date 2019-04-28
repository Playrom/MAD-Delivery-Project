package it.polito.justorder_restaurant;

import it.polito.justorder_framework.abstract_activities.AbstractViewerWithImagePickerActivityAndToolbar;
import it.polito.justorder_framework.abstract_activities.ActivityAbstractWithToolbar;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Deliverer;
import it.polito.justorder_framework.model.Order;
import it.polito.justorder_framework.model.Product;
import it.polito.justorder_framework.model.User;
import kotlin.Unit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OrderDetails extends AbstractViewerWithImagePickerActivityAndToolbar {

    private Order order;
    private User deliverer;
    private User user;
    private TextView orderIdTextField, userTextField, addressTextField, priceTextField, timestampTextField, riderTextField, productsTextField;
    private String productString;
    private Map<Product, Integer> products = new HashMap<>();

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

            for(Map.Entry<String, Integer> entry : this.order.getProducts().entrySet()){
                Database.INSTANCE.getProducts().get(entry.getKey(), (product -> {
                    this.products.put(product, entry.getValue());
                    this.reloadViews();
                    return Unit.INSTANCE;
                }));
            }
        }
        this.reloadViews();
    }

    @Override
    protected void reloadViews() {
        super.reloadViews();

        if(this.user != null) {
            userTextField.setText(this.user.getName());
        }
        addressTextField.setText(order.getUserAddress());
        priceTextField.setText(new Double(order.getPrice()).toString());
        timestampTextField.setText(order.getTimestamp().toString());

        if(this.deliverer != null) {
            riderTextField.setText(this.deliverer.getName());
        }

        if(products.size() > 0){
            productString = createProductString();
            productsTextField.setText(this.productString);
        }

        if(this.order != null){
            this.actionBar.setTitle(this.order.getKeyId());
        }
    }

    private String createProductString() {

        String productString = "";
        for(Map.Entry<Product, Integer> entry : products.entrySet()){
            productString += entry.getKey().getName() + "\n";
        }

        return productString;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_product_menu, menu);
        return true;
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


