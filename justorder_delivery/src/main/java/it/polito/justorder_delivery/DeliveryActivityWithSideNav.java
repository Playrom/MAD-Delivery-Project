package it.polito.justorder_delivery;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import it.polito.justorder_framework.AbstractRouteHandler;

public class DeliveryActivityWithSideNav extends AbstractRouteHandler {

    public boolean routeHandler(MenuItem item, Context context) {
        if(item.getItemId() == R.id.userPage) {
            Intent i = new Intent(context, MainActivity.class);
            context.startActivity(i);
            return true;
        }
        return false;
    }
}
