package it.polito.justorder_restaurant;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;

import android.text.Html;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import it.polito.justorder_framework.abstract_activities.AbstractViewerWithImagePickerActivity;

public class MainActivity extends AbstractViewerWithImagePickerActivity {
    protected String address, vatCode, taxCode, iban, foodType;
    protected EditText addressTextField, vatCodeTextField, taxCodeTextField, ibanTextField, foodTypeTextField;
    protected TextView openingHourTextView, closingHourTextView, openDaysTextView;
    protected String openingHour, closingHour;

    protected HashMap<String, Boolean> openDays = new HashMap() {{
        put("sunday", false);
        put("monday", false);
        put("tuesday", false);
        put("wednesday", false);
        put("thursday", false);
        put("friday", false);
        put("saturday", false);
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.placeholder_activity_main);
        setupActivity();
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
            i.putExtra("opening_hour", this.openingHour);
            i.putExtra("closing_hour", this.closingHour);
            i.putExtra("open_days", this.openDays);

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
        openingHourTextView = findViewById(R.id.openingHour);
        closingHourTextView = findViewById(R.id.closingHour);
        openDaysTextView = findViewById(R.id.openingDays);


        this.routeHandler = new ResturantActivityWithSideNav();

        reloadData();
    }

    @Override
    protected void reloadData() {
        super.reloadData();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        address = sharedPreferences.getString("address", "");
        vatCode = sharedPreferences.getString("vat_code", "");
        taxCode = sharedPreferences.getString("tax_code", "");
        iban = sharedPreferences.getString("iban", "");
        foodType = sharedPreferences.getString("food_type", "");
        openingHour = sharedPreferences.getString("opening_hour", "00:00");
        closingHour = sharedPreferences.getString("closing_hour", "00:00");

        Set<String> set = sharedPreferences.getStringSet("open_days", new HashSet<String>());
        for (String day : set) {
            openDays.put(day, true);
        }

        reloadViews();
    }

    @Override
    protected void reloadViews() {
        super.reloadViews();
        addressTextField.setText(this.address);
        vatCodeTextField.setText(this.vatCode);
        taxCodeTextField.setText(this.taxCode);
        ibanTextField.setText(this.iban);
        foodTypeTextField.setText(this.foodType);
        openingHourTextView.setText(this.openingHour);
        closingHourTextView.setText(this.closingHour);
        String days = "";
        for(HashMap.Entry<String, Boolean> entry : openDays.entrySet()) {
            if(entry.getValue()){
                days = days + "&#8226; " + entry.getKey() + "<br/>\n";
            }
        }
        openDaysTextView.setText(Html.fromHtml(days));
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
                openingHour = data.getStringExtra("opening_hour");
                closingHour = data.getStringExtra("closing_hour");
                openDays= (HashMap) data.getSerializableExtra("open_days");

                editor.putString("address", data.getStringExtra("address"));
                editor.putString("vat_code", data.getStringExtra("vat_code"));
                editor.putString("tax_code", data.getStringExtra("tax_code"));
                editor.putString("iban", data.getStringExtra("iban"));
                editor.putString("food_type", data.getStringExtra("food_type"));
                editor.putString("opening_hour", data.getStringExtra("opening_hour"));
                editor.putString("closing_hour", data.getStringExtra("closing_hour"));;

                HashSet<String> days = new HashSet<>();

                for(HashMap.Entry<String, Boolean> entry : openDays.entrySet()) {
                    if(entry.getValue()){
                        days.add(entry.getKey());
                    }
                }

                editor.putStringSet("open_days", days);

                editor.apply();

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
        outState.putString("opening_hour", openingHour);
        outState.putString("closing_hour", closingHour);
        outState.putSerializable("open_days", openDays);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        address = savedInstanceState.getString("address");
        vatCode = savedInstanceState.getString("vat_code");
        taxCode = savedInstanceState.getString("tax_code");
        iban = savedInstanceState.getString("iban");
        foodType = savedInstanceState.getString("food_type");
        openingHour = savedInstanceState.getString("opening_hour");
        closingHour = savedInstanceState.getString("closing_hour");
        openDays = (HashMap) savedInstanceState.getSerializable("open_days");

        reloadViews();
    }
}