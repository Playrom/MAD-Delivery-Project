package it.polito.justorder;

import it.polito.justorder_framework.R;
import it.polito.justorder_framework.abstract_activities.AbstractListViewWithSidenav;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Restaurant;
import it.polito.justorder_framework.model.User;
import kotlin.Unit;

import android.content.Intent;
import android.os.Bundle;
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
import java.util.Map;

public class FavouriteRestaurantsListActivity extends AbstractListViewWithSidenav {

    protected ListView listView;
    protected BaseAdapter adapter;
    protected FloatingActionButton fab;
    private int tapped;
    protected Button button_search;
    protected EditText search_bar;
    protected List<Restaurant> restaurants = new ArrayList<>();
    protected List<Restaurant> filtered_restaurants = new ArrayList<>();
    protected String filter = "";
    protected User user;
    protected Map<String, Boolean> favourites;

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
        this.button_search = (Button) findViewById(R.id.button_search);
        this.search_bar = (EditText) findViewById(R.id.search_bar);

        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //modify filtering criteria
                filter = search_bar.getText().toString();
                //refresh view
                reloadData();
                initDataSource();
            }
        });

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Restaurant entry = (Restaurant) parent.getAdapter().getItem(position);
                Intent intent = new Intent(FavouriteRestaurantsListActivity.this, FavouriteProductsListActivity.class);
                intent.putExtra("restaurant", entry);
                tapped = position;
                startActivityForResult(intent, 1);
            }
        });

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
                    Glide.with(FavouriteRestaurantsListActivity.this).load(restaurant.getImageUri()).into((ImageView) convertView.findViewById(R.id.image));
                }

                return convertView;
            }
        };
        this.listView.setAdapter(this.adapter);
        this.actionBar.setTitle("Favourite Restaurants");

        this.reloadData();
    }

    protected void initDataSource(){
        //get filtering criteria

        Database.INSTANCE.getRestaurants().orderByVote(restaurants1 -> {
            this.restaurants.clear();
            user = Database.INSTANCE.getCurrent_User();
            favourites = user.getFavouriteRestaurants();
            for (Restaurant rest : restaurants1) {
                if (rest.getName().contains(filter) && favourites.keySet().contains(rest.getKeyId())) {
                    restaurants.add(rest);
                }
            }
            this.reloadViews();
            return Unit.INSTANCE;
        });
    }

    @Override
    protected void reloadData() {
        super.reloadData();
        this.reloadViews();
    }

    @Override
    protected void reloadViews() {
        super.reloadViews();
        this.adapter.notifyDataSetChanged();
    }
}
