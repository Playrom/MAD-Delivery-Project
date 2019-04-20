package it.polito.justorder_restaurant;

import it.polito.justorder_framework.abstract_activities.ActivityAbstractWithSideNav;
import it.polito.justorder_framework.OrderEntity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class OrderSummaryActivity extends ActivityAbstractWithSideNav {

    protected ListView listView;
    protected BaseAdapter adapter;

    String[] order1 = {"Margherita", "Coca Cola"};
    String[] order2 = {"Hamburger", "Pizza", "Fanta", "Tiramisù"};
    String[] order3 = {"Pollo"};
    String[] order4 = {"Cheeseburger", "Lattina", "Bologna", "Chievoverona"};

    ArrayList<OrderEntity> orders = new ArrayList<OrderEntity>() {{
        add(new OrderEntity("Ordine1", "daniele", "Via 1", "12.47€", "01/04/2019 17:40", "julio cesar", order1));
        add(new OrderEntity("Ordine2", "giorgio", "Via pigreco", "11.32€", "01/04/2019 17:40", "maicon", order2));
        add(new OrderEntity("Ordine3", "davide", "Corso corsetta", "123.45€", "01/04/2019 17:40", "samuel", order3));
        add(new OrderEntity("Ordine4", "marco", "Largo strettini 12", "1.00€", "01/04/2019 17:40", "lucio", order4));
    }};

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
        this.routeHandler = new RouteHandler();
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderEntity entry= (OrderEntity) parent.getAdapter().getItem(position);
                Intent intent = new Intent(OrderSummaryActivity.this, OrderDetails.class);
                intent.putExtra("orderId", entry.getOrderId());
                intent.putExtra("user", entry.getUser());
                intent.putExtra("address", entry.getAddress());
                intent.putExtra("price", entry.getPrice());
                intent.putExtra("timestamp", entry.getTimestamp());
                intent.putExtra("rider", entry.getRider());
                intent.putExtra("products", entry.getProducts());
                startActivityForResult(intent, 1);
            }
        });
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
                OrderEntity order = orders.get(position);
                ((TextView)convertView.findViewById(R.id.order_id)).setText(order.getOrderId());
                ((TextView)convertView.findViewById(R.id.amount)).setText(order.getPrice());
                ((TextView)convertView.findViewById(R.id.address)).setText(order.getAddress());
                ((TextView)convertView.findViewById(R.id.timestamp)).setText(order.getTimestamp());
                return convertView;
            }
        };
        this.listView.setAdapter(this.adapter);
        this.actionBar.setTitle(R.string.order_summary_title);
    }

}





