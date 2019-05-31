package it.polito.justorder;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import it.polito.justorder.R;
import it.polito.justorder_framework.Utils;
import it.polito.justorder_framework.abstract_activities.AbstractEditor;
import it.polito.justorder_framework.common_activities.UserSettingsEditorActivity;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Order;
import it.polito.justorder_framework.model.Product;
import it.polito.justorder_framework.model.Restaurant;
import it.polito.justorder_framework.model.User;
import kotlin.Unit;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.HashMap;
import java.util.Map;

public class OrderProductActivity extends AbstractEditor {

    protected String quantity = "";
    protected EditText quantityTextView;
    protected Restaurant restaurant;
    protected Product product;
    protected User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_product_activity);
        this.setupActivity();
    }

    @Override
    protected void setupActivity() {
        super.setupActivity();
        quantityTextView = (EditText) findViewById(R.id.quantity);
        this.reloadData();
    }

    @Override
    protected void reloadData() {
        super.reloadData();
        Intent intent = getIntent();
        product = (Product) intent.getSerializableExtra("product");
        restaurant = (Restaurant) intent.getSerializableExtra("restaurant");
        user = Database.INSTANCE.getCurrent_User();
        this.reloadViews();
    }

    @Override
    protected void reloadViews() {
        super.reloadViews();
        if(this.user != null){
            quantityTextView.setText(quantity);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == it.polito.justorder_framework.R.id.saveUserData) {

            Integer qty = new Integer(quantityTextView.getText().toString());

            if(restaurant != null && product != null && user != null) {


                Map<String, Integer> m = user.getProducts();
                user.setRestaurantKey(restaurant.getName()); //da controllare che non si inseriscano prodotti di ristoranti diversi
                m.put(product.getKeyId(), qty);
                user.setProducts(m);
                Database.INSTANCE.getUsers().save(user);

                Toast toast = Toast.makeText(getApplicationContext(), "Prodotto Inserito al carrello", Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == it.polito.justorder_framework.R.id.saveUserData) {

            String qty = quantityTextView.getText().toString();

            Order order = new Order();
            order.setState("pending");

            if(restaurant != null && product != null && user != null) {
                order.setRestaurantName(restaurant.getName());
                order.setRestaurantAddress(restaurant.getAddress());
                order.setRestaurant(restaurant.getKeyId());

                Double price = Integer.parseInt(qty) * product.getCost();
                order.setPrice(price);
                Map<String, Integer> prod = new HashMap<String, Integer>();
                prod.put(product.getKeyId(), Integer.parseInt(qty));
                order.setProducts(prod);

                order.setUserName(user.getName());
                order.setUserAddress(user.getAddress());
                order.setUser(user.getKeyId());

                Database.INSTANCE.getOrders().save(order);

                Map<String, Boolean> m = restaurant.getOrders();
                m.put(order.getKeyId(), true);
                restaurant.setOrders(m);
                Database.INSTANCE.getRestaurants().save(restaurant);

                Map<String, Boolean> m1 = user.getOrders();
                m1.put(order.getKeyId(), true);
                user.setOrders(m1);
                Database.INSTANCE.getUsers().save(user);

                Toast toast = Toast.makeText(getApplicationContext(), "Ordine Inserito", Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }
*/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("quantity", quantityTextView.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        quantity = savedInstanceState.getString("quantity");
        this.reloadViews();
    }
}
