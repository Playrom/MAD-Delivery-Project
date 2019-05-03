package it.polito.justorder_deliverer;

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
    private User user;
    private TextView  userTextField, addressTextField, priceTextField, timestampTextField, addressTextField2, productsTextField;
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

        userTextField = findViewById(R.id.userTextField);
        addressTextField = findViewById(R.id.addressTextField);
        priceTextField = findViewById(R.id.priceTextField);
        timestampTextField = findViewById(R.id.timestampTextField);
        addressTextField2 = findViewById(R.id.addressTextField2);
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
        addressTextField2.setText(order.getRestaurantAddress());

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

}

