package it.polito.justorder_framework;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import it.polito.justorder_framework.db.Database;

public class AbstractMainMenuLoader {
    public static void createMainMenu(NavigationView navView){
        Database.INSTANCE.getCurrent_User();
        MenuItem login = navView.getMenu().findItem(R.id.login);
        if(login != null){
            login.setVisible(!FirebaseFunctions.isLoggedIn());
        }

        MenuItem logout = navView.getMenu().findItem(R.id.logout);
        MenuItem userSettings = navView.getMenu().findItem(R.id.userSettings);
        if(logout != null){
            logout.setVisible(FirebaseFunctions.isLoggedIn());
        }
        if(userSettings != null){
            userSettings.setVisible(FirebaseFunctions.isLoggedIn());
        }
    }
}
