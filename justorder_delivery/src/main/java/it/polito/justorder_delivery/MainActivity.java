package it.polito.justorder_delivery;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.material.navigation.NavigationView;

import it.polito.justorder_framework.MainActivityAbstract;

public class MainActivity extends MainActivityAbstract {
    protected String taxCode, iban;
    protected EditText taxCodeTextField, ibanTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        taxCode = sharedPreferences.getString("tax_code", "");
        iban = sharedPreferences.getString("iban", "");

        this.setupActivity();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.editUserData){
            Intent i = new Intent(getApplicationContext(), SecondActivity.class);
            i.putExtra("name", super.name);
            i.putExtra("email", super.email);
            i.putExtra("phone", super.phone);
            i.putExtra("imageFileName", super.imageFileName);

            i.putExtra("tax_code", this.taxCode);
            i.putExtra("iban", this.iban);

            startActivityForResult(i, 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void setupActivity() {
        super.setupActivity();
        taxCodeTextField = findViewById(R.id.taxCodeTextField);
        ibanTextField = findViewById(R.id.ibanTextField);
        this.routeHandler = new DeliveryActivityWithSideNav();
        this.reloadViews();

    }

    @Override
    protected void reloadViews() {
        super.reloadViews();
        taxCodeTextField.setText(this.taxCode);
        ibanTextField.setText(this.iban);
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


                    i.putExtra("tax_code", MainActivity.this.taxCode);
                    i.putExtra("iban", MainActivity.this.iban);

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
                taxCode = data.getStringExtra("tax_code");
                iban = data.getStringExtra("iban");
                editor.putString("vat_code", data.getStringExtra("vat_code"));
                editor.putString("tax_code", data.getStringExtra("tax_code"));
                editor.putString("iban", data.getStringExtra("iban"));
                editor.apply();

                this.reloadViews();
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tax_code", taxCode);
        outState.putString("iban", iban);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        taxCode = savedInstanceState.getString("tax_code");
        iban = savedInstanceState.getString("iban");

        reloadViews();
    }
}