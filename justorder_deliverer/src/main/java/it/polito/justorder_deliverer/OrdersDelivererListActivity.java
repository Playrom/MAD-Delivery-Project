package it.polito.justorder_deliverer;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Map;

import it.polito.justorder_framework.common_activities.OrderDetails;
import it.polito.justorder_framework.common_activities.OrdersListActivity;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Deliverer;
import it.polito.justorder_framework.model.Order;
import it.polito.justorder_framework.model.Restaurant;
import kotlin.Unit;

public class OrdersDelivererListActivity extends OrdersListActivity {

    protected Deliverer deliverer;

    @Override
    protected void setupActivity() {
        super.setupActivity();

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order entry = (Order) parent.getAdapter().getItem(position);
                String state = entry.getState();
                Intent intent;
                if (state!=null && state.equals("deliver_pending")) {
                    intent = new Intent(OrdersDelivererListActivity.this, OrderToAccept.class);
                }else {
                    intent = new Intent(OrdersDelivererListActivity.this, OrderDetails.class);
                }

                intent.putExtra("order", entry);
                tapped = position;
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void initDataSource() {
        Intent i = getIntent();
        this.deliverer = (Deliverer) i.getSerializableExtra("deliverer");

        Database.INSTANCE.getOrders().getWithIds(new ArrayList<>(deliverer.getOrders().keySet()), orders1 -> {
            this.orders.clear();
            this.orders.addAll(orders1);
            this.reloadViews();
            return Unit.INSTANCE;
        });

        Database.INSTANCE.getDeliverers().get(this.deliverer.getKeyId(), true, (deliverer1 -> {
            Database.INSTANCE.getOrders().getWithIds(new ArrayList<>(deliverer1.getOrders().keySet()), orders1 -> {
                this.orders.clear();
                this.orders.addAll(orders1);
                this.reloadViews();
                return Unit.INSTANCE;
            });
            return Unit.INSTANCE;
        }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.initDataSource();
    }
}
