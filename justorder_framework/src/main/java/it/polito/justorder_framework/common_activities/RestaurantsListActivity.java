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
import android.widget.Button;
import android.widget.EditText;
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
    protected Button button_search;
    protected EditText search_bar;
    private List<Restaurant> restaurants = new ArrayList<>();
    private List<Restaurant> filtered_restaurants = new ArrayList<>();
    private List<Product> products = new ArrayList<>();

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
        this.button_search = (Button) findViewById(R.id.button_search);
        this.search_bar = (EditText) findViewById(R.id.search_bar);
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get filtering criteria
                String filter = search_bar.getText().toString();
                //retrieve list of all restaurants
                Database.INSTANCE.getRestaurants().getAll(restaurants1 -> {
                    restaurants.clear();
                    restaurants.addAll(restaurants1);
                    return Unit.INSTANCE;
                });
                //create a list containing only the filtered restaurants
                filtered_restaurants.clear(); //empty initialization
                for (Restaurant r : restaurants) {
                    if (r.getName().toLowerCase().contains(filter.toLowerCase())) {
                        filtered_restaurants.add(r);
                    }
                }
                //refresh view
                restaurants = filtered_restaurants;
                reloadData();
            }
        });
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
        System.out.println("HERE: realoadData");
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
        System.out.println("HERE: realoadViews");
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
