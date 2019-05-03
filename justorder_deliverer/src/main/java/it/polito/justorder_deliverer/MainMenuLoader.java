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
    }
}
