package it.polito.justorder_framework;

import android.content.Intent;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.navigation.NavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class ActivityAbstractWithSideNav extends ActivityAbstractWithToolbar {
    protected DrawerLayout drawerLayout;
    protected RouteHandler routeHandler;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    protected void setupActivity() {
        super.setupActivity();

        this.actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this.getNavigationListener());
        this.reloadMenu();
    }

    @Subscribe
    public void onMessageEvent(UserChangeStatusEvent event) {
        this.reloadMenu();
    };

    protected NavigationView.OnNavigationItemSelectedListener getNavigationListener() {

        return new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                // set item as selected to persist highlight
                menuItem.setChecked(true);
                // close drawer when item is tapped
                drawerLayout.closeDrawers();

                if(ActivityAbstractWithSideNav.this.routeHandler != null){
                    boolean ret = ActivityAbstractWithSideNav.this.routeHandler.routeHandler(menuItem, ActivityAbstractWithSideNav.this);
                    overridePendingTransition(0, 0);
                    if(ret) {
                        ActivityAbstractWithSideNav.this.finish();
                    }
                    return ret;
                }
                return false;
            }
        };
    }

    protected void reloadMenu() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.main_menu);

        try {
            System.out.println(getApplicationContext().getPackageName() + ".MainMenuLoader");
            Class loader = Class.forName(getApplicationContext().getPackageName() + ".MainMenuLoader");
            loader.getMethod("createMainMenu", NavigationView.class).invoke(null, navigationView);
        }catch (ClassNotFoundException e){
            System.out.println("Missing MainMenuLoader in package");
        }catch (NoSuchMethodException e){
            System.out.println("Missing createMainMenu in MainMenuLoader");
        }catch (Exception e){

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FirebaseFunctions.AUTH_ACTIVITY_RESULT){
            EventBus.getDefault().post(new UserChangeStatusEvent());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
