package it.polito.justorder;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import it.polito.justorder_framework.AbstractMainMenuLoader;
import it.polito.justorder_framework.FirebaseFunctions;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Order;
import it.polito.justorder_framework.model.User;
import kotlin.Unit;

public class MainMenuLoader extends AbstractMainMenuLoader {
    private static HashMap<String, Long> ordersCount = new HashMap<>();

    public static void createMainMenu(NavigationView navView, Context context){
        AbstractMainMenuLoader.createMainMenu(navView, context);


        MenuItem orders = navView.getMenu().findItem(R.id.ordersPage);
        TextView view = (TextView) orders.getActionView();

        Database.INSTANCE.getUsers().get(Database.INSTANCE.getCurrent_User().getKeyId(), true, user -> {
            Database.INSTANCE.getOrders().getWithIds(new ArrayList<>(user.getOrders().keySet()), true, orders1 -> {
                HashMap<String, Long> newOrdersCount = new HashMap<>();
                newOrdersCount.put("pending", new Long(orders1.stream().filter(x -> ((Order) x).getState().equals("pending")).count()));
                newOrdersCount.put("deliverer_pending", new Long(orders1.stream().filter(x -> ((Order) x).getState().equals("deliverer_pending")).count()));
                newOrdersCount.put("delivered", new Long(orders1.stream().filter(x -> ((Order) x).getState().equals("delivered")).count()));
                newOrdersCount.put("accepted", new Long(orders1.stream().filter(x -> ((Order) x).getState().equals("accepted")).count()));

                view.setText(new Long(newOrdersCount.get("pending") + newOrdersCount.get("deliverer_pending") + newOrdersCount.get("accepted")).toString());

                if(!newOrdersCount.get("pending").equals(ordersCount.get("pending")) ||
                    !newOrdersCount.get("deliverer_pending").equals(ordersCount.get("deliverer_pending")) ||
                        !newOrdersCount.get("accepted").equals(ordersCount.get("accepted")) ||
                    !newOrdersCount.get("delivered").equals(ordersCount.get("delivered"))){
                    Toast toast = Toast.makeText(context, "Updates on your orders, checkout in the orders section", Toast.LENGTH_LONG);
                    toast.show();
                }
                ordersCount = newOrdersCount;
                return Unit.INSTANCE;
            });
            return Unit.INSTANCE;
        });

        //Gravity property aligns the text
        view.setGravity(Gravity.CENTER_VERTICAL);
        view.setTypeface(null, Typeface.BOLD);
        view.setTextColor(navView.getResources().getColor(R.color.colorAccent));
        view.setText("99+");

    }
}
