package it.polito.justorder_restaurant;

import android.content.Intent;
import android.view.View;

import it.polito.justorder_framework.common_activities.ProductsListActivity;
import it.polito.justorder_framework.model.Product;

public class ProductListActivityWithAdd extends ProductsListActivity {
    @Override
    protected void setupActivity() {
        super.setupActivity();
        this.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product entry = new Product();
                Intent intent = new Intent(ProductListActivityWithAdd.this, ProductEditActivity.class);
                intent.putExtra("product", entry);
                startActivityForResult(intent, 2);
            }
        });
    }
}
