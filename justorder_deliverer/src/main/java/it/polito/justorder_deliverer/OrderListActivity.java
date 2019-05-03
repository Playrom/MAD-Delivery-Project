package it.polito.justorder_deliverer;


import androidx.annotation.Nullable;


import it.polito.justorder_framework.abstract_activities.ActivityAbstractWithSideNav;
import it.polito.justorder_framework.ProductEntity;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Order;
import it.polito.justorder_framework.model.Deliverer;
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

import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
public class OrderListActivity extends ActivityAbstractWithSideNav{

    protected ListView listView;
    protected BaseAdapter adapter;
    protected FloatingActionButton fab;
    private int tapped;
    private Deliverer deliverer;
    private ArrayList<Order> orders = new ArrayList<>();
    private ArrayList<String> deliverersId = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
        this.setupActivity();
    }

    @Override
    protected void setupActivity() {
        super.setupActivity();
        this.listView = findViewById(R.id.order_list);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order entry = (Order) parent.getAdapter().getItem(position);
                Intent intent = new Intent(OrderListActivity.this, OrderDetails.class);
                intent.putExtra("order", entry);
                tapped = position;
                startActivityForResult(intent, 1);
            }
        });

        Intent i = getIntent();
        this.deliverer = (Deliverer)  i.getSerializableExtra("deliverer");
        deliverersId.add(this.deliverer.getKeyId());

        Database.INSTANCE.getOrders().getWithIds(deliverersId, orders1 -> {
            orders.clear();
            orders.addAll(orders1);
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
                return orders.size();
            }

            @Override
            public Object getItem(int position) {
                return orders.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.order_adapter, parent, false);
                }

                Order order = orders.get(position);
                ((TextView)convertView.findViewById(R.id.order_id)).setText(order.getKeyId());
                ((TextView)convertView.findViewById(R.id.address)).setText("user address: " + order.getUserAddress());
                ((TextView)convertView.findViewById(R.id.address)).setText("restaurant address" +order.getRestaurantAddress());
                ((TextView)convertView.findViewById(R.id.timestamp)).setText(order.getTimestamp().toString());
                ((TextView)convertView.findViewById(R.id.state)).setText(order.getState());
                return convertView;


            }
        };
        this.listView.setAdapter(this.adapter);
        this.actionBar.setTitle(R.string.order_summary_title);
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
