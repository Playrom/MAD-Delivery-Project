package it.polito.justorder;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Map;

import it.polito.justorder_framework.common_activities.OrderDetails;
import it.polito.justorder_framework.common_activities.OrdersListActivity;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Order;
import it.polito.justorder_framework.model.User;
import kotlin.Unit;

public class OrdersUserListActivity extends OrdersListActivity {

    protected User user;

    @Override
    protected void setupActivity() {
        super.setupActivity();

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order entry = (Order) parent.getAdapter().getItem(position);
                Intent intent = new Intent(OrdersUserListActivity.this, OrderDetails.class);
                intent.putExtra("order", entry);
                tapped = position;
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void initDataSource() {
        Intent i = getIntent();
        this.user = (User) i.getSerializableExtra("user");
        Map<String, Boolean> userOrders = this.user.getOrders();

        Database.INSTANCE.getOrders().getWithIds(new ArrayList<>(userOrders.keySet()), orders1 -> {
            this.orders.clear();
            this.orders.addAll(orders1);
            this.reloadViews();
            return Unit.INSTANCE;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.initDataSource();
    }
}
