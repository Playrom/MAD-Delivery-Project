package it.polito.justorder_framework.common_activities;

import it.polito.justorder_framework.R;
import it.polito.justorder_framework.Utils;
import it.polito.justorder_framework.abstract_activities.AbstractListViewWithSidenav;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Order;
import kotlin.Unit;

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

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OrdersListActivity extends AbstractListViewWithSidenav {

    protected ListView listView;
    protected BaseAdapter adapter;
    protected FloatingActionButton fab;
    protected int tapped;
    protected List<String> ordersId = new ArrayList<>();
    protected List<Order> orders = new ArrayList<>();
    protected Integer maxHour = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders_list);
        this.setupActivity();
    }

    @Override
    protected void setupActivity() {
        this.listView = findViewById(R.id.order_list);

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
                ((TextView)convertView.findViewById(R.id.order_id)).setText(order.getUserName());
                ((TextView)convertView.findViewById(R.id.amount)).setText("Cost: " + new Double(order.getPrice()).toString());
                ((TextView)convertView.findViewById(R.id.address)).setText("Address: " + order.getUserAddress());
                String date = DateFormat.format("dd/MM/yyyy - hh:mm", order.getTimestamp()).toString();
                ((TextView)convertView.findViewById(R.id.timestamp)).setText(date);
                ((TextView)convertView.findViewById(R.id.state)).setText(order.getState());
                return convertView;
            }
        };
        super.setupActivity();

        this.listView.setAdapter(this.adapter);
        this.actionBar.setTitle(R.string.order_summary_title);

        this.reloadData();
    }

    protected Integer getMostPopularHour(List<Order> orders) {
        int hour;
        Map<Integer, Integer> m = new HashMap<>();
        for (Order o : orders) {
            hour = o.getTimestamp().getHours();
            if (m.get(hour) != null) {
                m.put(hour, m.get(hour) + 1);
            }
            else {
                m.put(hour, 1);
            }
        }

        Integer max = null;
        for (Map.Entry<Integer, Integer> entry : m.entrySet())
        {
            if (max == null || entry.getValue().compareTo(max) > 0)
            {
                max = entry.getValue();
            }
        }

        return new Integer(max);
    }

    protected void initDataSource() {
        Intent i = getIntent();
        this.ordersId = (ArrayList<String>) i.getSerializableExtra("orders");

        Database.INSTANCE.getOrders().getWithIds(this.ordersId, orders1 -> {
            orders.clear();
            orders.addAll(orders1);
            this.maxHour = getMostPopularHour(orders);
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





