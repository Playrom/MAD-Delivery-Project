package it.polito.justorder;

import it.polito.justorder_framework.R;
import it.polito.justorder_framework.abstract_activities.AbstractListViewWithSidenav;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Restaurant;
import kotlin.Unit;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RestaurantsListActivity extends AbstractListViewWithSidenav {

    protected ListView listView;
    protected BaseAdapter adapter;
    private int tapped;
    protected Button button_search;
    protected EditText search_bar;
    protected List<Restaurant> restaurants = new ArrayList<>();
    protected List<Restaurant> visibleRestaurants = new ArrayList<>();
    protected String filter = "";
    protected boolean isSorting = false;

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
                Intent intent = new Intent(RestaurantsListActivity.this, ProductsListActivity.class);
                intent.putExtra("restaurant", entry);
                tapped = position;
                startActivityForResult(intent, 1);
            }
        });

        this.adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return visibleRestaurants.size();
            }

            @Override
            public Object getItem(int position) {
                return visibleRestaurants.get(position);
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

                if(visibleRestaurants.get(position) != null){
                    Restaurant restaurant = visibleRestaurants.get(position);
                    ((TextView)convertView.findViewById(R.id.content)).setText(restaurant.getName());
                    ((TextView)convertView.findViewById(R.id.description)).setText(restaurant.getAddress());
                    ((TextView)convertView.findViewById(R.id.avgVote)).setText("Avg vote: " + String.valueOf(restaurant.getAverageVote()));
                    Glide.with(RestaurantsListActivity.this).load(restaurant.getImageUri()).into((ImageView) convertView.findViewById(R.id.image));
                }

                return convertView;
            }
        };
        this.listView.setAdapter(this.adapter);
        this.actionBar.setTitle("All Restaurants");

        this.reloadData();
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

    protected void initDataSource(){
        //get filtering criteria
        Database.INSTANCE.getRestaurants().getAll(restaurants1 -> {
            this.restaurants = (List<Restaurant>) restaurants1;
            this.applyFilters();
            return Unit.INSTANCE;
        });
    }

    protected void applyFilters(){
        this.visibleRestaurants.clear();
        for (Restaurant rest : this.restaurants) {
            if (rest.getName().toLowerCase().contains(filter.toLowerCase()) || filter.replaceAll(" ", "").equals("")) {
                visibleRestaurants.add(rest);
            }
        }
        this.reloadViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.sortByVote) {
            this.isSorting = !this.isSorting;
            if(this.isSorting) {
                Database.INSTANCE.getRestaurants().orderByVote(restaurants1 -> {
                    this.restaurants = (List<Restaurant>) restaurants1;
                    this.applyFilters();
                    return Unit.INSTANCE;
                });
            }else{
                this.initDataSource();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_sort_by_value_menu, menu);
        return true;
    }
}
