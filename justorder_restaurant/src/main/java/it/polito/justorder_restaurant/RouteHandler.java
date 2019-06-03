package it.polito.justorder_restaurant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import it.polito.justorder_framework.AbstractRouteHandler;
import it.polito.justorder_framework.FirebaseFunctions;
import it.polito.justorder_framework.common_activities.UserSettingsViewerActivity;
import it.polito.justorder_framework.db.Database;
import kotlin.Unit;

public class RouteHandler extends AbstractRouteHandler {

    public boolean routeHandler(MenuItem item, Context context) {
        if(item.getItemId() == R.id.productsPage) {
            String restaurant_key = Database.INSTANCE.getCurrent_User().getRestaurantKey();

            Map<String, Serializable> map = new HashMap<>();

            Database.INSTANCE.getRestaurants().get(restaurant_key, (restaurant -> {
                Intent i = new Intent(context, ProductsListActivity.class);
                i.putExtra("restaurant", restaurant);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
            Database.INSTANCE.getCurrent_User().getRestaurantKey() != null) {

            String restaurant_key = Database.INSTANCE.getCurrent_User().getRestaurantKey();

            Intent i = new Intent(context, RestaurantSettingsViewerActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.putExtra("restaurant_key", restaurant_key);
            context.startActivity(i);
            return true;
        }

        if(item.getItemId() == R.id.userSettings) {
            Intent i = new Intent(context, UserSettingsViewerActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
            String restaurant_key = Database.INSTANCE.getCurrent_User().getRestaurantKey();

            Database.INSTANCE.getRestaurants().get(restaurant_key, (restaurant -> {
                Intent i = new Intent(context, OrdersRestaurantListActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("restaurant", restaurant);
                context.startActivity(i);
                return Unit.INSTANCE;
            }));

            return true;
        }

        if(item.getItemId() == R.id.commentsPage) {
            String restaurant_key = Database.INSTANCE.getCurrent_User().getRestaurantKey();

            Database.INSTANCE.getRestaurants().get(restaurant_key, (restaurant -> {
                Intent i = new Intent(context, CommentsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("restaurant", restaurant);
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
