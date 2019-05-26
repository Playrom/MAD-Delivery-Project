package it.polito.justorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import it.polito.justorder_framework.R;
import it.polito.justorder_framework.abstract_activities.AbstractListViewWithSidenav;
import it.polito.justorder_framework.abstract_activities.ActivityAbstractWithToolbar;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.db.Products;
import it.polito.justorder_framework.model.Order;
import it.polito.justorder_framework.model.Product;
import it.polito.justorder_framework.model.Restaurant;
import it.polito.justorder_framework.model.User;
import kotlin.Unit;

public class ProductsUserListActivity extends ActivityAbstractWithToolbar {

    protected ListView listView;
    protected BaseAdapter adapter;
    protected FloatingActionButton fab;
    protected int tapped;
    protected User user;
    protected Restaurant restaurant;
    protected List<String> productKeys = new ArrayList<>();
    protected List<Product> products = new ArrayList<>();
    protected Product axx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list_activity);
        this.setupActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_list_menu, menu);
        return true;
    }

    @Override
    protected void setupActivity() {

        super.setupActivity();
        this.initDataSource();
        this.listView = findViewById(R.id.food_list);
        this.fab = findViewById(R.id.fab);
        this.fab.hide();
        this.adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return products.size();
            }

            @Override
            public Object getItem(int position) {
                return products.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.product_item_adapter, parent, false);
                }

                if(products.get(position) != null){
                    Product product = products.get(position);
                    ((TextView)convertView.findViewById(R.id.content)).setText(product.getName());
                    ((TextView)convertView.findViewById(R.id.description)).setText(new Double(product.getCost()).toString());
                    Glide.with(ProductsUserListActivity.this).load(product.getImageUri()).into((ImageView) convertView.findViewById(R.id.image));
                }

                return convertView;
            }
        };
        this.listView.setAdapter(this.adapter);
        this.actionBar.setTitle(R.string.product_list_title);

        this.reloadData();
    }

    protected void initDataSource() {
        Intent i = getIntent();
        this.user = (User) i.getSerializableExtra("user");
        if(this.user != null){
            this.productKeys = new ArrayList<>(this.user.getProducts().keySet());

            Database.INSTANCE.getProducts().getWithIds(this.user.getProducts().keySet(), true, (product -> {
                this.products.add(product);  //non riesco a risolvere errore
                this.reloadData();
                return Unit.INSTANCE;
            }));
        }

    }

    @Override
    protected void reloadData() {
        super.reloadData();
        this.reloadViews();
    }

    @Override
    protected void reloadViews() {
        super.reloadViews();
        if(this.user != null){
            this.actionBar.setTitle(this.user.getName() + " Cart's");
        }
        this.adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == it.polito.justorder_framework.R.id.saveUserData) {


            Order order = new Order();
            order.setState("pending");

            if(user.getRestaurantKey() != null && products != null && user != null) {


                Database.INSTANCE.getRestaurants().get(user.getRestaurantKey(), true, (restaurant -> {
                    this.restaurant = restaurant;
                    this.reloadData();
                    return Unit.INSTANCE;
                }));


                order.setRestaurantName(restaurant.getName());
                order.setRestaurantAddress(restaurant.getAddress());
                order.setRestaurant(restaurant.getKeyId());

                Iterator<Map.Entry<String, Integer>> iterator = user.getProducts().entrySet().iterator();
                while (iterator.hasNext()) {


                    Map.Entry<String, Integer> entry = iterator.next();
                    Database.INSTANCE.getProducts().get(entry.getKey(), true, (product -> {
                        axx = product;
                        this.reloadData();
                        return Unit.INSTANCE;
                    }));


                    System.out.println("Key : " + entry.getKey() + " Value :" + entry.getValue());

                    Double price = entry.getValue() * axx.getCost();
                    order.setPrice(price);
                    Map<String, Integer> prod = new HashMap<String, Integer>();
                    prod.put(axx.getKeyId(), entry.getValue());
                    order.setProducts(prod);

                }


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
}
