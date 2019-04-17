package it.polito.justorder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import it.polito.justorder_framework.FirebaseFunctions;
import it.polito.justorder_framework.MainActivityAbstract;

public class MainActivity extends MainActivityAbstract {
    protected String address;
    protected EditText addressTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setupActivity();
    }

    @Override
    protected void setupActivity() {
        super.setupActivity();
        FirebaseFunctions.test(this);
        addressTextField = findViewById(R.id.addressTextField);
        this.routeHandler = new CustomerActivityWithSideNav();
        this.reloadData();
    }

    @Override
    protected void reloadData() {
        super.reloadData();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        address = sharedPreferences.getString("address", "");
        this.reloadViews();
    }

    @Override
    protected void reloadViews() {
        super.reloadViews();
        addressTextField.setText(this.address);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.editUserData){
            Intent i = new Intent(getApplicationContext(), SecondActivity.class);
            i.putExtra("name", super.name);
            i.putExtra("email", super.email);
            i.putExtra("phone", super.phone);
            i.putExtra("imageFileName", super.imageFileName);
            i.putExtra("address", this.address);

            startActivityForResult(i, 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected NavigationView.OnNavigationItemSelectedListener getNavigationListener() {
        return new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                // set item as selected to persist highlight
                menuItem.setChecked(true);
                // close drawer when item is tapped
                drawerLayout.closeDrawers();

                if(menuItem.getItemId() == R.id.editUserData){
                    Intent i = new Intent(getApplicationContext(), SecondActivity.class);
                    i.putExtra("name", MainActivity.this.name);
                    i.putExtra("email", MainActivity.super.email);
                    i.putExtra("phone", MainActivity.super.phone);
                    i.putExtra("imageFileName", MainActivity.super.imageFileName);
                    i.putExtra("address", MainActivity.this.address);

                    startActivityForResult(i, 1);
                    return true;
                }

                // Add code here to update the UI based on the item selected
                // For example, swap UI fragments here

                return true;
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(requestCode == 1){
            if(resultCode== Activity.RESULT_OK){
                address = data.getStringExtra("address");
                editor.putString("address", data.getStringExtra("address"));
                editor.apply();

                this.reloadViews();
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("address", address);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        address = savedInstanceState.getString("address");

        reloadViews();
    }
}