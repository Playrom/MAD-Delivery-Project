package it.polito.justorder_framework.common_activities;

import androidx.annotation.Nullable;

import it.polito.justorder_framework.R;
import it.polito.justorder_framework.abstract_activities.ActivityAbstractWithSideNav;
import it.polito.justorder_framework.ProductEntity;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Product;
import it.polito.justorder_framework.model.Restaurant;
import kotlin.Unit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class RestaurantsListActivity extends ActivityAbstractWithSideNav {

    protected ListView listView;
    protected BaseAdapter adapter;
    protected FloatingActionButton fab;
    private int tapped;
    private List<Restaurant> restaurants = new ArrayList<>();
    private List<Product> products = new ArrayList<>(); // to be removed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurants_list_activity);
        this.setupActivity();
    }

    @Override
    protected void setupActivity() {
        super.setupActivity();
        this.listView = findViewById(R.id.restaurants_list);
        this.fab = findViewById(R.id.fab);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Restaurant entry = (Restaurant) parent.getAdapter().getItem(position);
                Intent intent = new Intent(RestaurantsListActivity.this, ProductsListActivity.class);
                intent.putExtra("restaurant", entry);
                tapped = position;
                startActivityForResult(intent, 1);
            }
        });

        Database.INSTANCE.getRestaurants().getAll(restaurants1 -> {
            restaurants.clear();
            restaurants.addAll(restaurants1);
            this.reloadViews();
            return Unit.INSTANCE;
        });

        this.reloadData();
    }

    @Override
    protected void reloadData() {
        super.reloadData();
        this.adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return restaurants.size();
            }

            @Override
            public Object getItem(int position) {
                return restaurants.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.restaurant_item_adapter, parent, false);
                }

                if(restaurants.get(position) != null){
                    Restaurant restaurant = restaurants.get(position);
                    ((TextView)convertView.findViewById(R.id.content)).setText(restaurant.getName());
                    ((TextView)convertView.findViewById(R.id.description)).setText(restaurant.getAddress());
                    Glide.with(RestaurantsListActivity.this).load(restaurant.getImageUri()).into((ImageView) convertView.findViewById(R.id.image));
                }

                return convertView;
            }
        };
        this.listView.setAdapter(this.adapter);
        this.actionBar.setTitle("Our Restaurants");
        this.reloadViews();
    }

    @Override
    protected void reloadViews() {
        super.reloadViews();
        this.adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode== Activity.RESULT_OK){
                Product product = (Product) data.getSerializableExtra("product");
                products.set(tapped, product);

                this.reloadViews();
            }
        }else if(requestCode == 2){
            if(resultCode== Activity.RESULT_OK){
                Product product = (Product) data.getSerializableExtra("product");
                products.add(product);
                Database.INSTANCE.getProducts().save(product);
                this.reloadViews();
            }
        }
    }
}
