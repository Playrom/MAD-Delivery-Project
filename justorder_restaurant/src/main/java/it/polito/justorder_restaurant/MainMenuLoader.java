package it.polito.justorder_restaurant;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

import java.util.stream.Collectors;

import it.polito.justorder_framework.AbstractMainMenuLoader;
import it.polito.justorder_framework.FirebaseFunctions;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Order;
import it.polito.justorder_framework.model.User;
import kotlin.Unit;

public class MainMenuLoader extends AbstractMainMenuLoader {
    public static void createMainMenu(NavigationView navView, Context context){
        AbstractMainMenuLoader.createMainMenu(navView, context);

        MenuItem restaurantSettings = navView.getMenu().findItem(R.id.restaurantSettings);
        if(restaurantSettings != null){
            boolean loggedIn = FirebaseFunctions.isLoggedIn();
            User current_user = Database.INSTANCE.getCurrent_User();
            restaurantSettings.setVisible(FirebaseFunctions.isLoggedIn() &&
                    Database.INSTANCE.getCurrent_User() != null
            );
        }

        MenuItem orders = navView.getMenu().findItem(R.id.ordersPage);
        TextView view = (TextView) orders.getActionView();

        Database.INSTANCE.getOrders().query("restaurant", Database.INSTANCE.getCurrent_User().getRestaurantKey(), orders1 -> {
            long count = orders1.stream().filter(x -> ((Order) x).getState().equals("pending")).count();
            view.setText(new Long(count).toString());

            Toast toast = Toast.makeText(context, "Orders updates Pending", Toast.LENGTH_LONG);
            toast.show();
            return Unit.INSTANCE;
        });

        //Gravity property aligns the text
        view.setGravity(Gravity.CENTER_VERTICAL);
        view.setTypeface(null, Typeface.BOLD);
        view.setTextColor(navView.getResources().getColor(R.color.colorAccent));
        view.setText("0");
    }
}
