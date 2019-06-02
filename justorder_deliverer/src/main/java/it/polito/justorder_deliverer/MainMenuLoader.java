package it.polito.justorder_deliverer;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import it.polito.justorder_framework.AbstractMainMenuLoader;
import it.polito.justorder_framework.FirebaseFunctions;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Order;
import it.polito.justorder_framework.model.User;
import kotlin.Unit;

public class MainMenuLoader extends AbstractMainMenuLoader {
    private static boolean firstLoad = true;

    public static void createMainMenu(NavigationView navView, Context context){
        AbstractMainMenuLoader.createMainMenu(navView, context);

        MenuItem orders = navView.getMenu().findItem(R.id.ordersPage);
        TextView view = (TextView) orders.getActionView();

        Database.INSTANCE.getOrders().query("deliverer", Database.INSTANCE.getCurrent_User().getDelivererKey(), true, (orders1, hasUpdate) -> {
            long count = orders1.stream().filter(x -> ((Order) x).getState().equals("deliverer_pending")).count();
            view.setText(new Long(count).toString());
            if(hasUpdate){
                Toast toast = Toast.makeText(context, "Orders updates Pending", Toast.LENGTH_SHORT);
                toast.show();
            }
            return Unit.INSTANCE;
        });

        //Gravity property aligns the text
        view.setGravity(Gravity.CENTER_VERTICAL);
        view.setTypeface(null, Typeface.BOLD);
        view.setTextColor(navView.getResources().getColor(R.color.colorAccent));
        view.setText("0");
    }
}
