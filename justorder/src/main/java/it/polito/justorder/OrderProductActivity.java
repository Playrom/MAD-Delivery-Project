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
            try{
                Integer qty = new Integer(quantityTextView.getText().toString());

                if(restaurant != null && product != null && user != null) {

                    Toast toast;
                    Integer i= 0;
                    Map<String, Integer> m = user.getProducts();
                    if(user.getProducts() == null) m = new HashMap<String, Integer>();


                    if(user.getCurrentRestaurant().equals(restaurant.getKeyId())){
                        i = m.get(product.getKeyId());
                        if(i == null) i = 0;
                        qty = qty + i;
                        m.put(product.getKeyId(), qty);
                        user.setProducts(m);
                        Database.INSTANCE.getUsers().save(user);
                        toast = Toast.makeText(getApplicationContext(), "Product added to cart", Toast.LENGTH_SHORT);
                    } else {
                        if(user.getCurrentRestaurant().equals("")) {
                            user.setCurrentRestaurant(restaurant.getKeyId());
                            m.put(product.getKeyId(), qty);
                            user.setProducts(m);
                            Database.INSTANCE.getUsers().save(user);
                            toast = Toast.makeText(getApplicationContext(), "Product added to cart", Toast.LENGTH_SHORT);
                        } else {
                            toast = Toast.makeText(getApplicationContext(), "Please choose product of the same restaurant", Toast.LENGTH_SHORT);
                        }
                    }
                    toast.show();
                    finish();
                }
            }catch (NumberFormatException e){
                Toast toast = Toast.makeText(getApplicationContext(), "Select a quantity", Toast.LENGTH_SHORT);
                toast.show();
            }

        }

        return super.onOptionsItemSelected(item);
    }

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
