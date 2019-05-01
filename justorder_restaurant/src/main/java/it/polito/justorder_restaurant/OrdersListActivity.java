package it.polito.justorder_restaurant;

import it.polito.justorder_framework.abstract_activities.ActivityAbstractWithSideNav;
import it.polito.justorder_framework.common_activities.ProductsListActivity;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Order;
import it.polito.justorder_framework.model.Product;
import it.polito.justorder_framework.model.Restaurant;
import kotlin.Unit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class OrdersListActivity extends ActivityAbstractWithSideNav {

    protected ListView listView;
    protected BaseAdapter adapter;
    protected FloatingActionButton fab;
    private int tapped;
    private ArrayList<String> ordersId = new ArrayList<>();
    private ArrayList<Order> orders = new ArrayList<>();

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
                Intent intent;
                String state = entry.getState();
                if (state!=null && state.equals("pending")) {
                    intent = new Intent(OrdersListActivity.this, OrderToConfirm.class);
                }else {
                    intent = new Intent(OrdersListActivity.this, OrderDetails.class);
                }
                intent.putExtra("order", entry);
                tapped = position;
                startActivityForResult(intent, 1);
            }
        });


        Intent i = getIntent();
        this.ordersId = (ArrayList<String>) i.getSerializableExtra("orders");


        Database.INSTANCE.getOrders().getWithIds(this.ordersId, orders1 -> {
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
                ((TextView)convertView.findViewById(R.id.amount)).setText(new Double(order.getPrice()).toString());
                ((TextView)convertView.findViewById(R.id.address)).setText(order.getUserAddress());
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





