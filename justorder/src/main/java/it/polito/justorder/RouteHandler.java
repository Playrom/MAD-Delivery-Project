package it.polito.justorder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import it.polito.justorder_framework.AbstractRouteHandler;
import it.polito.justorder_framework.FirebaseFunctions;
import it.polito.justorder_framework.common_activities.OrdersListActivity;
import it.polito.justorder_framework.common_activities.UserSettingsViewerActivity;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.User;
import kotlin.Unit;

public class RouteHandler extends AbstractRouteHandler {

    User user;

    public boolean routeHandler(MenuItem item, Context context) {

        if(item.getItemId() == R.id.login) {
            if(context instanceof Activity){
                FirebaseFunctions.login((Activity) context);
            }
            return false;
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

        if(item.getItemId() == R.id.restaurants) {
            Intent i = new Intent(context, RestaurantsListActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(i);
            return true;
        }

        if(item.getItemId() == R.id.favourite_restaurants) {
            Intent i = new Intent(context, FavouriteRestaurantsListActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(i);
            return true;
        }

        if(item.getItemId() == R.id.ordersPage) {
            user = Database.INSTANCE.getCurrent_User();
            Intent intent = new Intent(context, OrdersUserListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("user", user);
            context.startActivity(intent);
            return true;
        }

        if(item.getItemId() == R.id.cart) {
            Intent intent = new Intent(context, ProductsUserListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            return true;
        }

        return false;
    }
}
