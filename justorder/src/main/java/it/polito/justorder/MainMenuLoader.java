package it.polito.justorder;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import it.polito.justorder_framework.AbstractMainMenuLoader;
import it.polito.justorder_framework.FirebaseFunctions;

public class MainMenuLoader extends AbstractMainMenuLoader {
    public static void createMainMenu(NavigationView navView){
        AbstractMainMenuLoader.createMainMenu(navView);

        MenuItem productsList = navView.getMenu().findItem(R.id.list_view);
        if(productsList != null){
            productsList.setVisible(false);
        }

        MenuItem restaurantSettings = navView.getMenu().findItem(R.id.restaurantSettings);
        if(restaurantSettings != null){
            restaurantSettings.setVisible(false);
        }

        MenuItem createRestaurant = navView.getMenu().findItem(R.id.createRestaurant);
        if(createRestaurant != null){
            createRestaurant.setVisible(false);
        }
        MenuItem delivererSettings = navView.getMenu().findItem(R.id.delivererSettings);
        if(delivererSettings != null){
            delivererSettings.setVisible(false);
        }
    }
}
