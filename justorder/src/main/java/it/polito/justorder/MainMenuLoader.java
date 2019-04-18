package it.polito.justorder;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import it.polito.justorder_framework.FirebaseFunctions;

public class MainMenuLoader {
    protected static void createMainMenu(NavigationView navView){
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
