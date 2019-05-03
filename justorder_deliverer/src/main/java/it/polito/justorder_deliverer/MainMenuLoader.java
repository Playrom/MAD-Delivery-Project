package it.polito.justorder_deliverer;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import it.polito.justorder_framework.AbstractMainMenuLoader;
import it.polito.justorder_framework.FirebaseFunctions;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.User;

public class MainMenuLoader extends AbstractMainMenuLoader {
    public static void createMainMenu(NavigationView navView){
        AbstractMainMenuLoader.createMainMenu(navView);

        MenuItem delivererSettings = navView.getMenu().findItem(R.id.delivererSettings);
        if(delivererSettings != null){
            delivererSettings.setVisible(true);
        }

        MenuItem userSett = navView.getMenu().findItem(R.id.userSettings);
        if(userSett != null){
            userSett.setVisible(false);
        }

        MenuItem ordersPage = navView.getMenu().findItem(R.id.ordersPage);
        if(ordersPage != null){
            ordersPage.setVisible(true);
        }

    }
}
