package it.polito.justorder_deliverer;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Comparator;

import it.polito.justorder_framework.common_activities.OrderDetails;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Deliverer;
import it.polito.justorder_framework.model.DelivererPosition;
import it.polito.justorder_framework.model.Order;
import kotlin.Unit;

public class OrdersDelivererListActivity extends DelivererOrderListActivity {

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
                if (state!=null && (state.equals("deliver_pending") || state.equals("accepted"))) {
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
            orders.sort(new Comparator<Order>() {
                        @Override
                        public int compare(Order o1, Order o2) {
                            return o1.getTimestamp().compareTo(o2.getTimestamp());

                        }
                    }.reversed()
            );
            this.reloadViews();
            return Unit.INSTANCE;
        });

        Database.INSTANCE.getDeliverers().get(this.deliverer.getKeyId(), true, (deliverer1 -> {
            Database.INSTANCE.getOrders().getWithIds(new ArrayList<>(deliverer1.getOrders().keySet()), true,  orders1 -> {
                this.orders.clear();
                this.orders.addAll(orders1);
                orders.sort(new Comparator<Order>() {
                            @Override
                            public int compare(Order o1, Order o2) {
                                return o1.getTimestamp().compareTo(o2.getTimestamp());

                            }
                        }.reversed()
                );


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
