package it.polito.justorder_restaurant;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import it.polito.justorder_framework.ActivityAbstractWithToolbar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class OrderDetails extends ActivityAbstractWithToolbar {

    private String orderId, user, address, price, timestamp, rider, productString;
    private TextView orderIdTextField, userTextField, addressTextField, priceTextField, timestampTextField, riderTextField, productsTextField;
    private String[] products;

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
        this.orderId = i.getStringExtra("orderId");
        this.user = i.getStringExtra("user");
        this.address = i.getStringExtra("address");
        this.price = i.getStringExtra("price");
        this.timestamp = i.getStringExtra("timestamp");
        this.rider = i.getStringExtra("rider");
        this.products = i.getStringArrayExtra("products");

        this.reloadViews();
    }

    @Override
    protected void reloadViews() {
        super.reloadViews();
        //orderIdTextField.setText(this.orderId);
        userTextField.setText(this.user);
        addressTextField.setText(this.address);
        priceTextField.setText(this.price);
        timestampTextField.setText(this.timestamp);
        riderTextField.setText(this.rider);
        productString = createProductString();
        productsTextField.setText(this.productString);


        if(this.orderId != null){
            this.actionBar.setTitle(this.orderId);
        }
    }

    private String createProductString() {

        int maxIndex = products.length;
        int i = 0;
        String productString = "";
        for(i=0; i<maxIndex; i++){
            productString += products[i] + "\n";
        }

        return productString;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_product_menu, menu);
        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("orderId", this.orderId);
        returnIntent.putExtra("user", this.user);
        returnIntent.putExtra("address", this.address);
        returnIntent.putExtra("price", this.price);
        returnIntent.putExtra("timestamp", this.timestamp);
        returnIntent.putExtra("rider", this.rider);
        returnIntent.putExtra("products", this.products);


        setResult(Activity.RESULT_OK, returnIntent);
        onBackPressed();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("orderId", orderId);
        outState.putString("user", user);
        outState.putString("address", address);
        outState.putString("price", price);
        outState.putString("timestamp", timestamp);
        outState.putString("rider", rider);
        outState.putString("products", productString);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        orderId = savedInstanceState.getString("orderId");
        user = savedInstanceState.getString("user");
        address = savedInstanceState.getString("address");
        price = savedInstanceState.getString("price");
        timestamp = savedInstanceState.getString("timestamp");
        rider = savedInstanceState.getString("rider");
        productString = savedInstanceState.getString("products");

        reloadViews();
    }
}


