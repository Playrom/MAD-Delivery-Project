package it.polito.justorder_restaurant;

import android.view.MenuItem;

import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.navigation.NavigationView;

import it.polito.justorder_framework.AbstractMainMenuLoader;
import it.polito.justorder_framework.FirebaseFunctions;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.User;

public class MainMenuLoader extends AbstractMainMenuLoader {
    public static void createMainMenu(NavigationView navView){
        AbstractMainMenuLoader.createMainMenu(navView);

        MenuItem restaurantSettings = navView.getMenu().findItem(R.id.restaurantSettings);
        if(restaurantSettings != null){
            boolean loggedIn = FirebaseFunctions.isLoggedIn();
            User current_user = Database.INSTANCE.getCurrent_User();
            restaurantSettings.setVisible(FirebaseFunctions.isLoggedIn() &&
                    Database.INSTANCE.getCurrent_User() != null
            );
        }

        MenuItem createRestaurant = navView.getMenu().findItem(R.id.restaurantSettings);
        if(createRestaurant != null){
            createRestaurant.setVisible(FirebaseFunctions.isLoggedIn() &&
                    Database.INSTANCE.getCurrent_User() != null
            );
        }
    }
}
