package it.polito.justorder_restaurant;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import it.polito.justorder_framework.RouteHandler;

public class ResturantActivityWithSideNav extends RouteHandler {

    public boolean routeHandler(MenuItem item, Context context) {
        if(item.getItemId() == R.id.list_view) {
            Intent i = new Intent(context, ThirdActivity.class);
            context.startActivity(i);
            return true;

        }
        if(item.getItemId() == R.id.userPage) {
            Intent i = new Intent(context, MainActivity.class);
            context.startActivity(i);
            return true;
        }
        return false;
    }
}
