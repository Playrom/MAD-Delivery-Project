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

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import it.polito.justorder_framework.R;
import it.polito.justorder_framework.abstract_activities.AbstractListViewWithSidenav;
import it.polito.justorder_framework.abstract_activities.ActivityAbstractWithToolbar;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Product;
import it.polito.justorder_framework.model.Restaurant;
import it.polito.justorder_framework.model.User;
import kotlin.Unit;

public class ProductsListActivity extends ActivityAbstractWithToolbar {

    protected ListView listView;
    protected BaseAdapter adapter;
    protected FloatingActionButton fab;
    protected int tapped;
    protected Restaurant restaurant;
    protected List<Product> products = new ArrayList<>();

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

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product entry = (Product) parent.getAdapter().getItem(position);
                Intent intent = new Intent(ProductsListActivity.this, ProductClientActivity.class);
                intent.putExtra("product", entry);
                intent.putExtra("restaurant", restaurant);
                tapped = position;
                startActivityForResult(intent, 1);
            }
        });

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
                    Glide.with(ProductsListActivity.this).load(product.getImageUri()).into((ImageView) convertView.findViewById(R.id.image));
                }

                return convertView;
            }
        };
        this.listView.setAdapter(this.adapter);
        this.actionBar.setTitle(R.string.product_list_title);
        //this.actionBar.

        this.reloadData();
    }

    protected void initDataSource() {
        Intent i = getIntent();
        this.restaurant = (Restaurant) i.getSerializableExtra("restaurant");
        if(this.restaurant != null){
            this.products = new ArrayList<>(this.restaurant.getProducts().values());
            this.products.sort(new Comparator<Product>() {
                @Override
                public int compare(Product p1, Product p2) {
                    if (p1.getAverageVote() > p2.getAverageVote()) {
                        return 1;
                    }
                    if (p1.getAverageVote() < p2.getAverageVote()) {
                        return -1;
                    }
                    return 0;
                }
            });
        }

        Database.INSTANCE.getRestaurants().get(this.restaurant.getKeyId(), true, (restaurant1 -> {
            this.restaurant = restaurant1;
            this.reloadData();
            return Unit.INSTANCE;
        }));
    }

    @Override
    protected void reloadData() {
        super.reloadData();
        this.reloadViews();
    }

    @Override
    protected void reloadViews() {
        super.reloadViews();
        if(this.restaurant != null){
            this.actionBar.setTitle(this.restaurant.getName());
        }
        this.adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode== Activity.RESULT_OK){
                Product product = (Product) data.getSerializableExtra("product");
                products.set(tapped, product);
                this.restaurant.getProducts().put(product.getKeyId(), product);
                Database.INSTANCE.getRestaurants().save(restaurant);
                this.reloadData();
            }
        }else if(requestCode == 2){
            if(resultCode== Activity.RESULT_OK){
                Product product = (Product) data.getSerializableExtra("product");
                products.add(product);
                String key = Database.INSTANCE.getRestaurants().generateKeyForChild(this.restaurant.getKeyId());
                product.setKeyId(key);
                this.restaurant.getProducts().put(key, product);
                Database.INSTANCE.getRestaurants().save(restaurant);
                this.reloadData();
            }
        }
    }
    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.editUserData){
            Intent i = new Intent(getApplicationContext(), ReviewActivity.class);
            i.putExtra("restaurant", restaurant);

            startActivityForResult(i, 1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
