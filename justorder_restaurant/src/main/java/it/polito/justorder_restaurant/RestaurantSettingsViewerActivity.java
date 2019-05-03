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

import it.polito.justorder_framework.abstract_activities.AbstractViewerWithImagePickerActivityAndSidenav;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Restaurant;
import it.polito.justorder_framework.model.User;
import kotlin.Unit;

public class RestaurantSettingsViewerActivity extends AbstractViewerWithImagePickerActivityAndSidenav {

    protected Restaurant restaurant;
    protected EditText nameTextField, emailTextField, phoneTextField, addressTextField, vatCodeTextField, taxCodeTextField, ibanTextField, foodTypeTextField;
    protected TextView openingHourTextView, closingHourTextView, openDaysTextView;
    protected String openingHour, closingHour;
    protected Intent i;
    protected String restaurant_intent_key;

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
        i = this.getIntent();
        setContentView(R.layout.restaurant_settings_viewer);
        setupActivity();
    }

    @Override
    protected void setupActivity() {
        super.setupActivity();

        nameTextField = findViewById(R.id.nameTextField);
        emailTextField = findViewById(R.id.emailTextField);
        phoneTextField = findViewById(R.id.phoneTextField);
        addressTextField = findViewById(R.id.addressTextField);
        vatCodeTextField = findViewById(R.id.vatCodeTextField);
        taxCodeTextField = findViewById(R.id.taxCodeTextField);
        ibanTextField = findViewById(R.id.ibanTextField);
        foodTypeTextField = findViewById(R.id.foodTypeTextField);
        openingHourTextView = findViewById(R.id.openingHour);
        closingHourTextView = findViewById(R.id.closingHour);
        openDaysTextView = findViewById(R.id.openingDays);

        reloadData();
    }

    @Override
    protected void reloadData() {
        super.reloadData();

        if(i != null && i.getStringExtra("restaurant_key") != null){
            restaurant_intent_key = i.getStringExtra("restaurant_key");
            Database.INSTANCE.getRestaurants().get(restaurant_intent_key, (restaurant1 -> {
                this.restaurant = restaurant1;
                if(this.restaurant != null){
                    this.imageUri = this.restaurant.getImageUri();
                }
                this.reloadViews();
                return Unit.INSTANCE;
            }));
        }else {
            this.restaurant = new Restaurant();
            Intent i = new Intent(this, RestaurantSettingsEditorActivity.class);
            i.putExtra("restaurant", restaurant);
            startActivityForResult(i, 2);
        }
    }

    @Override
    protected void reloadViews() {
        super.reloadViews();
        if(this.restaurant != null){
            nameTextField.setText(restaurant.getName());
            emailTextField.setText(restaurant.getEmail());
            phoneTextField.setText(restaurant.getTelephone());
            addressTextField.setText(restaurant.getAddress());
            vatCodeTextField.setText(restaurant.getVatCode());
            taxCodeTextField.setText(restaurant.getFiscalCode());
            ibanTextField.setText(restaurant.getIban());
            foodTypeTextField.setText(restaurant.getType());
            openingHourTextView.setText(restaurant.getOpeningHour());
            closingHourTextView.setText(restaurant.getClosingHour());

            String days = "";
            for(HashMap.Entry<String, Boolean> entry : restaurant.getOpenDays().entrySet()) {
                if(entry.getValue()){
                    days = days + "&#8226; " + entry.getKey() + "<br/>\n";
                }
            }
            openDaysTextView.setText(Html.fromHtml(days));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.editUserData){
            Intent i = new Intent(getApplicationContext(), RestaurantSettingsEditorActivity.class);
            i.putExtra("restaurant", restaurant);

            startActivityForResult(i, 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(requestCode == 1 || requestCode == 2){
            if(resultCode== Activity.RESULT_OK){
                restaurant = (Restaurant) data.getSerializableExtra("restaurant");
                Database.INSTANCE.getRestaurants().save(restaurant);
                if(requestCode == 2 && Database.INSTANCE.getCurrent_User() != null){
                    User user = Database.INSTANCE.getCurrent_User();
                    restaurant.setOwner(user.getKeyId());
                    user.setRestaurantKey(restaurant.getKeyId());
                    Database.INSTANCE.getUsers().save(user);
                    Database.INSTANCE.getRestaurants().save(restaurant);
                }
                reloadViews();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("restaurant", restaurant);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        restaurant = (Restaurant) savedInstanceState.getSerializable("restaurant");
        reloadViews();
    }
}