package it.polito.justorder;

import android.content.Intent;
import android.view.Menu;
import android.view.View;

import it.polito.justorder_framework.common_activities.ProductActivity;

public class ProductClientActivity extends ProductActivity {


    @Override
    protected void setupActivity() {
        super.setupActivity();
        orderButton.setVisibility(View.VISIBLE);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ProductClientActivity.this, OrderProductActivity.class);
                myIntent.putExtra("restaurant", restaurant);
                myIntent.putExtra("product", product);
                ProductClientActivity.this.startActivity(myIntent);
            }
        });
        this.reloadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
