package it.polito.justorder;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import it.polito.justorder_framework.common_activities.ProductActivity;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Order;
import it.polito.justorder_framework.model.User;

public class ProductClientActivity extends ProductActivity {

    protected Map<String, Boolean> favouriteRestaurants;
    protected Map<String, Boolean> favouriteProducts;
    protected User user;

    @Override
    protected void setupActivity() {
        super.setupActivity();
        this.reloadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_favourite_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.add_to_favourite){
            user = Database.INSTANCE.getCurrent_User();
            favouriteRestaurants = user.getFavouriteRestaurants();
            favouriteProducts = user.getFavouriteProducts();
            favouriteRestaurants.put(restaurant.getKeyId(), true);
            favouriteProducts.put(product.getKeyId(), true);
            user.setFavouriteRestaurants(favouriteRestaurants);
            user.setFavouriteProducts(favouriteProducts);
            Database.INSTANCE.getUsers().save(user);
            Toast toast = Toast.makeText(getApplicationContext(), "Added to favourites", Toast.LENGTH_SHORT);
            toast.show();
            return true;
        }

        if(item.getItemId() == R.id.remove_from_favourite){
            user = Database.INSTANCE.getCurrent_User();
            favouriteRestaurants = user.getFavouriteRestaurants();
            favouriteProducts = user.getFavouriteProducts();

            if(favouriteProducts.get(product.getKeyId())!=null){
                favouriteProducts.remove(product.getKeyId());

                Integer i=1;
                for(String x : favouriteProducts.keySet()){
                    if(restaurant.getProducts().get(x) != null){
                        i=8;
                    }
                }
                if(i==1){
                    favouriteRestaurants.remove(restaurant.getKeyId());
                }

                user.setFavouriteRestaurants(favouriteRestaurants);
                user.setFavouriteProducts(favouriteProducts);
                Database.INSTANCE.getUsers().save(user);
                Toast toast = Toast.makeText(getApplicationContext(), "Removed from favourites", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Product was not a favourite one", Toast.LENGTH_SHORT);
                toast.show();
            }
            return true;
        }

        if(item.getItemId() == R.id.review){
            Intent i = new Intent(getApplicationContext(), ReviewActivity.class);
            i.putExtra("product", this.product);
            i.putExtra("restaurant", this.restaurant);
            startActivity(i);
            return true;
        }
        if(item.getItemId() == R.id.add_to_cart){
            Intent myIntent = new Intent(ProductClientActivity.this, OrderProductActivity.class);
            myIntent.putExtra("restaurant", restaurant);
            myIntent.putExtra("product", product);
            startActivity(myIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}