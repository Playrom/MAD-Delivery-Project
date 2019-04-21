package it.polito.justorder_restaurant;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.Nullable;

import it.polito.justorder_framework.common_activities.ProductActivity;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Product;

public class ProductActivityWithEditing extends ProductActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == it.polito.justorder_framework.R.id.editUserData){
            Intent i = new Intent(getApplicationContext(), ProductEditActivity.class);
            i.putExtra("product", this.product);

            startActivityForResult(i, 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode== Activity.RESULT_OK){
                product = (Product) data.getSerializableExtra("product");
                Database.INSTANCE.getProducts().save(product);
                this.reloadViews();
            }
        }
    }

}
