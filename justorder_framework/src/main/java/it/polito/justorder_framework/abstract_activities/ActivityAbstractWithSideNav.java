package it.polito.justorder_framework.abstract_activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.lang.reflect.Field;

import it.polito.justorder_framework.AbstractRouteHandler;
import it.polito.justorder_framework.FirebaseFunctions;
import it.polito.justorder_framework.LogoutStatusEvent;
import it.polito.justorder_framework.R;
import it.polito.justorder_framework.UserChangeStatusEvent;
import kotlin.Unit;

public class ActivityAbstractWithSideNav extends ActivityAbstractWithToolbar {
    protected DrawerLayout drawerLayout;
    protected AbstractRouteHandler routeHandler;

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
        this.setRouteHandler();
    }

    @Override
    protected void reloadViews() {
        super.reloadViews();
        this.reloadMenu();
    }

    @Subscribe
    public void onMessageEvent(UserChangeStatusEvent event) {
        this.reloadViews();
    }

    @Subscribe
    public void onMessageEvent(LogoutStatusEvent event) {

        try {
            Class className = Class.forName(getApplicationContext().getPackageName() + ".AppLoaderActivity");
            Intent i = new Intent(this, className);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            this.startActivity(i);
            finish();
        }catch (ClassNotFoundException e){
            System.out.println("Missing AppLoaderActivity in package");
        }
    }

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
//                    if(ret) {
//                        ActivityAbstractWithSideNav.this.finish();
//                    }
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
            loader.getMethod("createMainMenu", NavigationView.class, Context.class).invoke(null, navigationView, this);
        }catch (ClassNotFoundException e){
            System.out.println("Missing MainMenuLoader in package");
        }catch (NoSuchMethodException e){
            System.out.println("Missing createMainMenu in MainMenuLoader");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void setRouteHandler() {
        try {
            Class moduleRouteHandlerClass = Class.forName(getApplicationContext().getPackageName() + ".RouteHandler");
            Field field = ActivityAbstractWithSideNav.class.getDeclaredField("routeHandler");
            Class handlerClass = field.getType();
            if(handlerClass.isAssignableFrom(moduleRouteHandlerClass)){
                this.routeHandler = (AbstractRouteHandler) moduleRouteHandlerClass.getConstructor().newInstance();
            }
        }catch (ClassNotFoundException e){
            System.out.println("Missing MainMenuLoader in package");
        }catch (NoSuchMethodException e){
            System.out.println("Missing createMainMenu in MainMenuLoader");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FirebaseFunctions.AUTH_ACTIVITY_RESULT){
            EventBus.getDefault().post(new UserChangeStatusEvent());
            this.setupActivity();
        }
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        this.reloadViews();
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
