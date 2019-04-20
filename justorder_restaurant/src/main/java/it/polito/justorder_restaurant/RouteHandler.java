package it.polito.justorder_restaurant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import java.util.stream.Collectors;

import it.polito.justorder_framework.AbstractRouteHandler;
import it.polito.justorder_framework.FirebaseFunctions;
import it.polito.justorder_framework.common_activities.ProductsListActivity;
import it.polito.justorder_framework.common_activities.UserSettingsViewerActivity;
import it.polito.justorder_framework.db.Database;

public class RouteHandler extends AbstractRouteHandler {

    public boolean routeHandler(MenuItem item, Context context) {
        if(item.getItemId() == R.id.list_view) {
            Intent i = new Intent(context, ProductsListActivity.class);
            context.startActivity(i);
            return true;

        }

        if(item.getItemId() == R.id.login) {
            if(context instanceof Activity){
                FirebaseFunctions.login((Activity) context);
            }
            return false;
        }

        if(item.getItemId() == R.id.restaurantSettings &&
            Database.INSTANCE.getCurrent_User() != null &&
            Database.INSTANCE.getCurrent_User().getManagedRestaurants().size() > 0) {

            String restaurant_key = "";
            restaurant_key = Database.INSTANCE.getCurrent_User().getManagedRestaurants().entrySet()
                    .stream().filter(x -> {return x.getValue();}).collect(Collectors.toList()).get(0).getKey();

            Intent i = new Intent(context, RestaurantSettingsViewerActivity.class);
            i.putExtra("restaurant_key", restaurant_key);
            context.startActivity(i);
            return true;
        }

        if(item.getItemId() == R.id.createRestaurant &&
                Database.INSTANCE.getCurrent_User() != null) {

            Intent i = new Intent(context, RestaurantSettingsViewerActivity.class);
            context.startActivity(i);
            return true;
        }

        if(item.getItemId() == R.id.userSettings) {
            Intent i = new Intent(context, UserSettingsViewerActivity.class);
            context.startActivity(i);
            return true;
        }

        if(item.getItemId() == R.id.logout) {
            if(context instanceof Activity){
                FirebaseFunctions.logout();
            }
            return false;
        }

        if(item.getItemId() == R.id.ordersPage) {
            Intent i = new Intent(context, OrderSummaryActivity.class);
            context.startActivity(i);
            return true;
        }

        return false;
    }
}
