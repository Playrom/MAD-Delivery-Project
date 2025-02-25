package it.polito.justorder_restaurant;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.internal.gmsg.HttpClient;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import it.polito.justorder_framework.abstract_activities.AbstractViewerWithImagePickerActivityAndToolbar;
import it.polito.justorder_framework.abstract_activities.ActivityAbstractWithToolbar;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Deliverer;
import it.polito.justorder_framework.model.Order;
import it.polito.justorder_framework.model.OrderProduct;
import it.polito.justorder_framework.model.Product;
import it.polito.justorder_framework.model.User;
import kotlin.Unit;


public class OrderToConfirm extends ActivityAbstractWithToolbar {

    private Order order;
    private User deliverer;
    private User user;
    private TextView orderIdTextField, userTextField, riderTextField, addressTextField, priceTextField, timestampTextField, productsTextField;
    private String productString;
    private Map<Product, Double> products = new HashMap<>();
    private List<Deliverer> deliverers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_to_confirm);
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
        productsTextField = findViewById(R.id.productsTextField);
        riderTextField = findViewById(R.id.riderTextField);
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
            if(this.order.getUser() != null) {
                Database.INSTANCE.getUsers().get(this.order.getUser(), (user1 -> {
                    this.user = user1;
                    this.reloadViews();
                    return Unit.INSTANCE;
                }));
            }

            for(Map.Entry<String, OrderProduct> entry : this.order.getProducts().entrySet()){
                Database.INSTANCE.getRestaurants().get(entry.getValue().getRestaurantKey(), (restaurant -> {
                    this.products.put(restaurant.getProducts().get(entry.getValue().getProductKey()), entry.getValue().getQuantity());
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
            userTextField.setText("User Name: " + this.user.getName());
        }
        addressTextField.setText("Delivery Address: " + order.getUserAddress());
        String x = String.format("%.02f", new Float(order.getPrice()));
        priceTextField.setText("Order Cost: " + x + " €");
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

        this.actionBar.setTitle("Order Details");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.select_order_option, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("order", order);
        outState.putSerializable("user", user);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        order = (Order) savedInstanceState.getSerializable("order");
        user = (User) savedInstanceState.getSerializable("user");

        reloadViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.acceptOrder){
            Intent i = new Intent(this, RestaurantDelivererAssignActivity.class);
            startActivityForResult(i, 0);
        }

        if(item.getItemId() == R.id.refuseOrder){
            order.setState("cancelled");
            order.setDeliverer(null);
            Database.INSTANCE.getOrders().save(order);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == RESULT_OK){
            order.setState("deliver_pending");
            String deliverer_key = data.getStringExtra("deliverer_key");
            Database.INSTANCE.getDeliverers().get(deliverer_key, deliverer -> {
                order.setDeliverer(deliverer.getKeyId());
                Database.INSTANCE.getOrders().save(order);

                Map <String, Boolean> orderMap = deliverer.getOrders();
                orderMap.put(order.getKeyId(), true);
                deliverer.setOrders(orderMap);
                deliverer.setCurrentOrder(order.getKeyId());
                Database.INSTANCE.getDeliverers().save(deliverer);
                Database.INSTANCE.getOrders().save(order);

                finish();
                return Unit.INSTANCE;
            });
        }
    }

}
