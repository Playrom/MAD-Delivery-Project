package it.polito.justorder_restaurant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import it.polito.justorder_framework.AbstractRouteHandler;
import it.polito.justorder_framework.FirebaseFunctions;
import it.polito.justorder_framework.common_activities.ProductsListActivity;
import it.polito.justorder_framework.common_activities.UserSettingsViewerActivity;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Order;
import kotlin.Unit;

public class RouteHandler extends AbstractRouteHandler {

    public boolean routeHandler(MenuItem item, Context context) {
        if(item.getItemId() == R.id.list_view) {

            String restaurant_key = "";
            restaurant_key = Database.INSTANCE.getCurrent_User().getManagedRestaurants().entrySet()
                    .stream().filter(x -> {return x.getValue();}).collect(Collectors.toList()).get(0).getKey();

            Database.INSTANCE.getRestaurants().get(restaurant_key, (restaurant -> {
                Intent i = new Intent(context, ProductListActivityWithAdd.class);
                i.putExtra("restaurant", restaurant);
                context.startActivity(i);
                return Unit.INSTANCE;
            }));

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
            String restaurant_key = "";
            restaurant_key = Database.INSTANCE.getCurrent_User().getManagedRestaurants().entrySet()
                    .stream().filter(x -> {return x.getValue();}).collect(Collectors.toList()).get(0).getKey();

            Database.INSTANCE.getRestaurants().get(restaurant_key, (restaurant -> {
                Intent i = new Intent(context, OrdersListActivity.class);
                ArrayList<String> orders =  new ArrayList<>(restaurant.getOrders().keySet());
                i.putExtra("orders", orders);
                context.startActivity(i);
                return Unit.INSTANCE;
            }));

            return true;
        }

        return false;
    }

    @Override
    public Class getProductActivityClass() {
        return ProductActivityWithEditing.class;
    }
}
