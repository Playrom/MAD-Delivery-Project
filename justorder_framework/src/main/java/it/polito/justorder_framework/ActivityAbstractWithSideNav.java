package it.polito.justorder_framework;

import android.content.Intent;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class ActivityAbstractWithSideNav extends ActivityAbstractWithToolbar {
    protected DrawerLayout drawerLayout;
    protected RouteHandler routeHandler;

    protected void setupActivity() {
        super.setupActivity();

        this.actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this.getNavigationListener());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
