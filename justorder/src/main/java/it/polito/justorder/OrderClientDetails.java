package it.polito.justorder;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import it.polito.justorder_framework.common_activities.OrderDetails;

public class OrderClientDetails extends OrderDetails {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(it.polito.justorder_framework.R.layout.activity_order_details);
        this.setupActivity();
    }

    @Override
    protected void setupActivity() {
        super.setupActivity();
        this.reloadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(it.polito.justorder_framework.R.menu.activity_product_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == it.polito.justorder_framework.R.id.review){
            Intent i = new Intent(getApplicationContext(), ReviewActivity.class);
            i.putExtra("deliverer", this.deliverer);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
