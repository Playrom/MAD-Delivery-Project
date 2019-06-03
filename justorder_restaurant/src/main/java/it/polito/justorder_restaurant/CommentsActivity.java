package it.polito.justorder_restaurant;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.justorder_framework.abstract_activities.AbstractListViewWithSidenav;
import it.polito.justorder_framework.abstract_activities.ActivityAbstractWithToolbar;
import it.polito.justorder_framework.common_activities.OrderDetails;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Order;
import it.polito.justorder_framework.model.Restaurant;
import it.polito.justorder_framework.model.Review;
import kotlin.Unit;

public class CommentsActivity extends AbstractListViewWithSidenav {

    protected Restaurant restaurant;
    protected ListView listView;
    protected BaseAdapter adapter;
    protected FloatingActionButton fab;
    protected int tapped;
    protected List<String> reviewsId = new ArrayList<>();
    protected List<Review> reviews = new ArrayList<>();
    protected Integer maxHour = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        this.initDataSource();
        this.setupActivity();
    }

    @Override
    protected void setupActivity() {
        this.listView = findViewById(R.id.comment_list);

        this.adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return reviews.size();
            }

            @Override
            public Object getItem(int position) {
                return reviews.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.comment_adapter, parent, false);
                }
                Review review = reviews.get(position);


                ((TextView)convertView.findViewById(R.id.comment)).setText(review.getComment());
                int stars = review.getStars();
                String starsString = Integer.toString(stars);
                ((TextView)convertView.findViewById(R.id.stars)).setText(starsString + " stars");
                return convertView;
            }
        };

        super.setupActivity();

        this.listView.setAdapter(this.adapter);
        //this.actionBar.setTitle("Comments");

        this.reloadData();
    }

    protected void initDataSource() {
        Intent i = getIntent();
        this.restaurant = (Restaurant) i.getSerializableExtra("restaurant");
        //HashMap<String, Boolean> restaurantReviews = (HashMap<String, Boolean>) this.restaurant.getReviews();
        //this.reviewsId = (List<String>) restaurantReviews.keySet();

        Database.INSTANCE.getReviews().getWithIds(new ArrayList<>(restaurant.getReviews().keySet()), review1 -> {
            reviews.clear();
            reviews.addAll(review1);
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

    @Override
    protected void onResume() {
        super.onResume();
        super.reloadData();
    }

}