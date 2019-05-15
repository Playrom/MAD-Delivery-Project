package it.polito.justorder_deliverer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import it.polito.justorder_framework.AbstractRouteHandler;
import it.polito.justorder_framework.FirebaseFunctions;
import it.polito.justorder_framework.common_activities.UserSettingsViewerActivity;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Order;
import kotlin.Unit;

public class RouteHandler extends AbstractRouteHandler {

    public boolean routeHandler(MenuItem item, Context context) {

        if(item.getItemId() == R.id.login) {
            if(context instanceof Activity){
                FirebaseFunctions.login((Activity) context);
            }
            return false;
        }


        if(item.getItemId() == R.id.ordersPage && Database.INSTANCE.getCurrent_User() != null) {

            if(Database.INSTANCE.getCurrent_User().getDelivererKey() != null){
                String deliverer_key = Database.INSTANCE.getCurrent_User().getDelivererKey();

                Database.INSTANCE.getDeliverers().get(deliverer_key, (deliverer -> {

                    Intent i = new Intent(context, OrdersDelivererListActivity.class);
                    i.putExtra("deliverer", deliverer);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(i);
                    return  Unit.INSTANCE;
                }));

                return true;
            }

        }


        if(item.getItemId() == R.id.delivererSettings && Database.INSTANCE.getCurrent_User() != null){

            if(Database.INSTANCE.getCurrent_User().getDelivererKey() != null) {

                String deliverer_key = Database.INSTANCE.getCurrent_User().getDelivererKey();

                Intent i = new Intent(context, DelivererSettingsViewerActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("deliverer_intent_key", deliverer_key);
                context.startActivity(i);
                return true;
            }else{
                Intent i = new Intent(context, DelivererSettingsViewerActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(i);
                return true;
            }
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

        if(item.getItemId() == R.id.map && Database.INSTANCE.getCurrent_User() != null) {

            if(Database.INSTANCE.getCurrent_User().getDelivererKey() != null){
                String deliverer_key = Database.INSTANCE.getCurrent_User().getDelivererKey();

                Database.INSTANCE.getDeliverers().get(deliverer_key, (deliverer -> {

                    if (deliverer.getCurrentOrder()!=null) {
                        Database.INSTANCE.getOrders().get(deliverer.getCurrentOrder(), true, (order1 -> {
                            Order order = order1;
                            String restaurantAddress = order.getRestaurantAddress();
                            String customerAddress = order.getUserAddress();


                            String restaurantPlace = getStringFromAddress( context, restaurantAddress);
                            String customerPlace = getStringFromAddress(context, customerAddress);


                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse("http://maps.google.com/maps?saddr=" + restaurantPlace + "&daddr=" + customerPlace));
                            context.startActivity(intent);


                            return Unit.INSTANCE;
                        }));
                    }


                    return  Unit.INSTANCE;
                }));

                return true;
            }

        }

        return false;
    }

    public String getStringFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        String p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            //p1 = location.getLatitude() + ", "  + location.getLongitude();
            p1 = location.getLatitude() + ", " + location.getLongitude();

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;

    }
}
