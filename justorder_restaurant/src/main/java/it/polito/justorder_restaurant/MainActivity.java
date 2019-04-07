package it.polito.justorder_restaurant;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.material.navigation.NavigationView;

import it.polito.justorder_framework.MainActivityAbstract;

public class MainActivity extends MainActivityAbstract {
    protected String address, vatCode, taxCode, iban, foodType, openDays;
    protected EditText addressTextField, vatCodeTextField, taxCodeTextField, ibanTextField, foodTypeTextField, openingHourTextField, closingHourTextField, openDaysTextField;
    protected String openingHour, closingHour;
    protected String openHour, closeHour;
    protected String openMinute, closeMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        address = sharedPreferences.getString("address", "");
        vatCode = sharedPreferences.getString("vat_code", "");
        taxCode = sharedPreferences.getString("tax_code", "");
        iban = sharedPreferences.getString("iban", "");
        foodType = sharedPreferences.getString("food_type", "");
        openingHour = sharedPreferences.getString("openingHour", "00:00");
        closingHour = sharedPreferences.getString("closingHour", "00:00");
        openDays = sharedPreferences.getString("openDays", "");



        openHour = openingHour.split(":")[0];
        openMinute = openingHour.split(":")[0];
        closeHour = closingHour.split(":")[1];
        closeMinute = closingHour.split(":")[1];


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
            i.putExtra("address", this.address);
            i.putExtra("vat_code", this.vatCode);
            i.putExtra("tax_code", this.taxCode);
            i.putExtra("iban", this.iban);
            i.putExtra("food_type", this.foodType);
            i.putExtra("openHour", this.openHour);
            i.putExtra("closeHour", this.closeHour);
            i.putExtra("openMinute", this.openMinute);
            i.putExtra("closeMinute", this.closeMinute);
            i.putExtra("openDays", this.openDays);



            startActivityForResult(i, 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void setupActivity() {
        super.setupActivity();

        addressTextField = findViewById(R.id.addressTextField);
        vatCodeTextField = findViewById(R.id.vatCodeTextField);
        taxCodeTextField = findViewById(R.id.taxCodeTextField);
        ibanTextField = findViewById(R.id.ibanTextField);
        foodTypeTextField = findViewById(R.id.foodTypeTextField);
        openingHourTextField = findViewById(R.id.openingHourTextField);
        closingHourTextField = findViewById(R.id.closingHourTextField);
        openDaysTextField = findViewById(R.id.openDaysTextField);


        this.routeHandler = new ResturantActivityWithSideNav();

        this.reloadViews();
    }

    @Override
    protected void reloadViews() {
        super.reloadViews();
        addressTextField.setText(this.address);
        vatCodeTextField.setText(this.vatCode);
        taxCodeTextField.setText(this.taxCode);
        ibanTextField.setText(this.iban);
        foodTypeTextField.setText(this.foodType);
        openingHourTextField.setText(this.openingHour);
        closingHourTextField.setText(this.closingHour);
        openDaysTextField.setText(this.openDays);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(requestCode == 1){
            if(resultCode== Activity.RESULT_OK){
                address = data.getStringExtra("address");
                vatCode = data.getStringExtra("vat_code");
                taxCode = data.getStringExtra("tax_code");
                iban = data.getStringExtra("iban");
                foodType = data.getStringExtra("food_type");
                openDays= data.getStringExtra("openDays");

                editor.putString("address", data.getStringExtra("address"));
                editor.putString("vat_code", data.getStringExtra("vat_code"));
                editor.putString("tax_code", data.getStringExtra("tax_code"));
                editor.putString("iban", data.getStringExtra("iban"));
                editor.putString("food_type", data.getStringExtra("food_type"));
                editor.putString("openingHour", data.getStringExtra("openingHour"));
                editor.putString("closingHour", data.getStringExtra("closingHour"));
                editor.putString("openDays", data.getStringExtra("openDays"));
                editor.apply();

                openHour = openingHour.split(":")[0];
                openMinute = openingHour.split(":")[0];
                closeHour = closingHour.split(":")[1];
                closeMinute = closingHour.split(":")[1];

                this.reloadViews();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("address", address);
        outState.putString("vat_code", vatCode);
        outState.putString("tax_code", taxCode);
        outState.putString("iban", iban);
        outState.putString("food_type", foodType);
        outState.putString("openingHour", openingHour);
        outState.putString("closingHour", closingHour);
        outState.putString("openDays", openDays);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        address = savedInstanceState.getString("address");
        vatCode = savedInstanceState.getString("vat_code");
        taxCode = savedInstanceState.getString("tax_code");
        iban = savedInstanceState.getString("iban");
        foodType = savedInstanceState.getString("food_type");
        openingHour = savedInstanceState.getString("openingHour");
        closingHour = savedInstanceState.getString("closingHour");
        openDays = savedInstanceState.getString("openDays");

        reloadViews();
    }
}