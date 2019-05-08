package it.polito.justorder_restaurant;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

import it.polito.justorder_framework.abstract_activities.AbstractEditorWithImagePickerActivity;
import it.polito.justorder_framework.Utils;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Restaurant;
import kotlin.Unit;

public class RestaurantSettingsEditorActivity extends AbstractEditorWithImagePickerActivity {

    protected Restaurant restaurant;
    protected EditText nameTextField, emailTextField, phoneTextField, addressTextField, vatCodeTextField, taxCodeTextField, ibanTextField, foodTypeTextField;
    protected Spinner foodTypeSpinner;
    protected Button setPositionAsCurrentButton;
    protected String openHour, openMinute, closeHour, closeMinute;
    protected TimePicker timePickerOpen, timePickerClose;
    protected CheckBox Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday;
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
        Intent i = getIntent();
        if(i.getSerializableExtra("restaurant") != null && i.getSerializableExtra("restaurant") instanceof Restaurant){
            this.restaurant = (Restaurant) i.getSerializableExtra("restaurant");
        }else{
            this.restaurant = new Restaurant();
            this.restaurant.setOwner(FirebaseAuth.getInstance().getUid());
        }
        setContentView(R.layout.restaurant_settings_editor);
        this.setupActivity();
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
        foodTypeSpinner = findViewById(R.id.foodTypeSpinner);
        timePickerOpen = findViewById(R.id.timePickerOpen);
        timePickerOpen.setIs24HourView(true);
        timePickerClose = findViewById(R.id.timePickerClose);
        timePickerClose.setIs24HourView(true);
        Sunday= findViewById(R.id.checkbox_tuesday);
        Monday= findViewById(R.id.checkbox_monday);
        Tuesday= findViewById(R.id.checkbox_tuesday);
        Wednesday= findViewById(R.id.checkbox_wednesday);
        Thursday= findViewById(R.id.checkbox_thursday);
        Friday= findViewById(R.id.checkbox_friday);
        Saturday= findViewById(R.id.checkbox_saturday);

        setPositionAsCurrentButton = findViewById(R.id.set_position_as_current_button);

        this.reloadData();
    }

    @Override
    protected void reloadData() {
        super.reloadData();
        if(this.restaurant != null) {
            this.imageUri = this.restaurant.getImageUri();
            if(this.restaurant.getOpeningHour().contains(":") && this.restaurant.getClosingHour().contains(":")){
                openHour = this.restaurant.getOpeningHour().split(":")[0];
                closeHour = this.restaurant.getClosingHour().split(":")[0];
                openMinute = this.restaurant.getOpeningHour().split(":")[1];
                closeMinute = this.restaurant.getClosingHour().split(":")[1];
            }
        }else{
            this.restaurant = new Restaurant();
        }

        setPositionAsCurrentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Restaurant restaurant = RestaurantSettingsEditorActivity.this.restaurant;
                if(restaurant != null){
                    if(restaurant.getKeyId() == null){
                        restaurant.setKeyId(Database.INSTANCE.getRestaurants().generateKeyForChild());
                    }
                    Database.INSTANCE.getGeodata().setClientPosition(restaurant.getKeyId(), RestaurantSettingsEditorActivity.this, () -> {
                        return Unit.INSTANCE;
                    });
                }
            }
        });

        this.reloadViews();
    }

    @Override
    protected void reloadViews() {
        super.reloadViews();
        if(this.restaurant != null) {
            nameTextField.setText(restaurant.getName());
            emailTextField.setText(restaurant.getEmail());
            phoneTextField.setText(restaurant.getTelephone());
            addressTextField.setText(restaurant.getAddress());
            vatCodeTextField.setText(restaurant.getVatCode());
            taxCodeTextField.setText(restaurant.getFiscalCode());
            ibanTextField.setText(restaurant.getIban());
            foodTypeSpinner.setSelection(getIndex(foodTypeSpinner, restaurant.getType()));

            if(openHour != null && closeHour != null && openMinute != null && closeMinute != null){
                timePickerOpen.setCurrentHour(Integer.parseInt(openHour));
                timePickerClose.setCurrentHour(Integer.parseInt(closeHour));
                timePickerOpen.setCurrentMinute(Integer.parseInt(openMinute));
                timePickerClose.setCurrentMinute(Integer.parseInt(closeMinute));
            }

            if (this.restaurant.getOpenDays().get("sunday") != null && this.restaurant.getOpenDays().get("sunday")) {
                Sunday.setChecked(true);
            }
            if (this.restaurant.getOpenDays().get("monday") != null && this.restaurant.getOpenDays().get("monday")) {
                Monday.setChecked(true);
            }
            if (this.restaurant.getOpenDays().get("tuesday") != null && this.restaurant.getOpenDays().get("tuesday")) {
                Tuesday.setChecked(true);
            }
            if (this.restaurant.getOpenDays().get("wednesday") != null && this.restaurant.getOpenDays().get("wednesday")) {
                Wednesday.setChecked(true);
            }
            if (this.restaurant.getOpenDays().get("thursday") != null && this.restaurant.getOpenDays().get("thursday")) {
                Thursday.setChecked(true);
            }
            if (this.restaurant.getOpenDays().get("friday") != null && this.restaurant.getOpenDays().get("friday")) {
                Friday.setChecked(true);
            }
            if (this.restaurant.getOpenDays().get("saturday") != null && this.restaurant.getOpenDays().get("saturday")) {
                Saturday.setChecked(true);
            }
        }

    }

    @Override
    protected Unit saveImageUri(Uri uri) {
        super.saveImageUri(uri);
        restaurant.setImageUri(uri.toString());
        Database.INSTANCE.getRestaurants().save(restaurant);
        return Unit.INSTANCE;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.saveUserData) {
            if (
                    nameTextField.getText().toString() == "" ||
                            emailTextField.getText().toString() == "" ||
                            phoneTextField.getText().toString() == "" ||
                            addressTextField.getText().toString() == "" ||
                            vatCodeTextField.getText().toString() == "" ||
                            taxCodeTextField.getText().toString() == "" ||
                            ibanTextField.getText().toString() == "" ||
                            foodTypeSpinner.getSelectedItem().toString() == "" ||
                            !Utils.isValidEmail(emailTextField.getText().toString())
            ) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RestaurantSettingsEditorActivity.this);
                AlertDialog dialog = builder
                        .setTitle(R.string.validation_error)
                        .setMessage(R.string.field_are_not_valid)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();
                dialog.show();
            } else {

                String openAt = Utils.beautifyTime(timePickerOpen.getCurrentHour(), timePickerOpen.getCurrentMinute());
                String closeAt = Utils.beautifyTime(timePickerClose.getCurrentHour(), timePickerClose.getCurrentMinute());

                Intent returnIntent = new Intent();
                restaurant.setName(nameTextField.getText().toString());
                restaurant.setEmail(emailTextField.getText().toString());
                restaurant.setTelephone(phoneTextField.getText().toString());
                restaurant.setAddress(addressTextField.getText().toString());
                restaurant.setVatCode(vatCodeTextField.getText().toString());
                restaurant.setFiscalCode(taxCodeTextField.getText().toString());
                restaurant.setIban(ibanTextField.getText().toString());
                restaurant.setType(foodTypeSpinner.getSelectedItem().toString());
                restaurant.setOpeningHour(openAt);
                restaurant.setClosingHour(closeAt);

                if (Sunday.isChecked()){
                    restaurant.getOpenDays().put("sunday", true);
                }else {
                    restaurant.getOpenDays().put("sunday", false);
                }

                if (Monday.isChecked()){
                    restaurant.getOpenDays().put("monday", true);
                }else {
                    restaurant.getOpenDays().put("monday", false);
                }

                if (Tuesday.isChecked()){
                    restaurant.getOpenDays().put("tuesday", true);
                }else {
                    restaurant.getOpenDays().put("tuesday", false);
                }

                if (Wednesday.isChecked()){
                    restaurant.getOpenDays().put("wednesday", true);
                }else {
                    restaurant.getOpenDays().put("wednesday", false);
                }

                if (Thursday.isChecked()){
                    restaurant.getOpenDays().put("thursday", true);
                }else {
                    restaurant.getOpenDays().put("thursday", false);
                }

                if (Friday.isChecked()){
                    restaurant.getOpenDays().put("friday", true);
                }else {
                    restaurant.getOpenDays().put("friday", false);
                }

                if (Saturday.isChecked()){
                    restaurant.getOpenDays().put("saturday", true);
                }else {
                    restaurant.getOpenDays().put("saturday", false);
                }

                returnIntent.putExtra("restaurant", restaurant);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String openAt = Utils.beautifyTime(timePickerOpen.getCurrentHour(), timePickerOpen.getCurrentMinute());
        String closeAt = Utils.beautifyTime(timePickerClose.getCurrentHour(), timePickerClose.getCurrentMinute());
        restaurant.setName(nameTextField.getText().toString());
        restaurant.setEmail(emailTextField.getText().toString());
        restaurant.setTelephone(phoneTextField.getText().toString());
        restaurant.setAddress(addressTextField.getText().toString());
        restaurant.setVatCode(vatCodeTextField.getText().toString());
        restaurant.setFiscalCode(taxCodeTextField.getText().toString());
        restaurant.setIban(ibanTextField.getText().toString());
        restaurant.setType(foodTypeSpinner.getSelectedItem().toString());
        restaurant.setOpeningHour(openAt);
        restaurant.setClosingHour(closeAt);

        if (Sunday.isChecked()){
            restaurant.getOpenDays().put("sunday", true);
        }else {
            restaurant.getOpenDays().put("sunday", false);
        }

        if (Monday.isChecked()){
            restaurant.getOpenDays().put("monday", true);
        }else {
            restaurant.getOpenDays().put("monday", false);
        }

        if (Tuesday.isChecked()){
            restaurant.getOpenDays().put("tuesday", true);
        }else {
            restaurant.getOpenDays().put("tuesday", false);
        }

        if (Wednesday.isChecked()){
            restaurant.getOpenDays().put("wednesday", true);
        }else {
            restaurant.getOpenDays().put("wednesday", false);
        }

        if (Thursday.isChecked()){
            restaurant.getOpenDays().put("thursday", true);
        }else {
            restaurant.getOpenDays().put("thursday", false);
        }

        if (Friday.isChecked()){
            restaurant.getOpenDays().put("friday", true);
        }else {
            restaurant.getOpenDays().put("friday", false);
        }

        if (Saturday.isChecked()){
            restaurant.getOpenDays().put("saturday", true);
        }else {
            restaurant.getOpenDays().put("saturday", false);
        }

        outState.putSerializable("restaurant", restaurant);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        restaurant = (Restaurant) savedInstanceState.getSerializable("restaurant");

        reloadViews();
    }

    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }


}


