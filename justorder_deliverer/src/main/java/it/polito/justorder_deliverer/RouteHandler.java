package it.polito.justorder_deliverer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.stream.Collectors;

import it.polito.justorder_framework.AbstractRouteHandler;
import it.polito.justorder_framework.FirebaseFunctions;
import it.polito.justorder_framework.common_activities.ProductsListActivity;
import it.polito.justorder_framework.common_activities.UserSettingsViewerActivity;
import it.polito.justorder_framework.db.Database;
import kotlin.Unit;

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


        if(item.getItemId() == R.id.ordersPage && Database.INSTANCE.getCurrent_User() != null) {

            if(Database.INSTANCE.getCurrent_User().getDelivererKey() != null){
                String deliverer_key = "";
                deliverer_key = Database.INSTANCE.getCurrent_User().getDelivererKey();

                Database.INSTANCE.getRestaurants().get(deliverer_key, (deliverer -> {
                    Intent i = new Intent(context, OrderListActivity.class);
                    i.putExtra("deliverer", deliverer);
                    context.startActivity(i);
                    return Unit.INSTANCE;
                }));

                return true;
            } else {
                Intent i = new Intent(context, DelivererSettingsViewerActivity.class);
                context.startActivity(i);
                return true;
            }

        }


        if(item.getItemId() == R.id.delivererSettings && Database.INSTANCE.getCurrent_User() != null){

            if(Database.INSTANCE.getCurrent_User().getDelivererKey() != null) {

                String deliverer_key = "";
                deliverer_key = Database.INSTANCE.getCurrent_User().getDelivererKey();

                Intent i = new Intent(context, DelivererSettingsViewerActivity.class);
                i.putExtra("deliverer_intent_key", deliverer_key);
                context.startActivity(i);
                return true;
            }else{
                Intent i = new Intent(context, DelivererSettingsViewerActivity.class);
                context.startActivity(i);
                return true;
            }
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

        return false;
    }
}
