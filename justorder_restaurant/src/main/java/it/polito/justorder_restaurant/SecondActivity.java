package it.polito.justorder_restaurant;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.HashMap;

import it.polito.justorder_framework.abstract_activities.SecondActivityAbstract;
import it.polito.justorder_framework.Utils;

public class SecondActivity extends SecondActivityAbstract {

    protected String address, vatCode, taxCode, iban, foodType;
    protected EditText addressTextField, vatCodeTextField, taxCodeTextField, ibanTextField;
    protected Spinner foodTypeSpinner;
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
        setContentView(R.layout.usersettings_editor_activity);
        this.setupActivity();
    }

    @Override
    protected void setupActivity() {
        super.setupActivity();
        addressTextField = findViewById(R.id.addressTextField);
        vatCodeTextField = findViewById(R.id.vatCodeTextField);
        taxCodeTextField = findViewById(R.id.taxCodeTextField);
        ibanTextField = findViewById(R.id.ibanTextField);
        foodTypeSpinner = findViewById(R.id.foodTypeSpinner);
        timePickerOpen = findViewById(R.id.timePickerOpen);
        timePickerOpen.setIs24HourView(true);
        timePickerClose = findViewById(R.id.timePickerClose);
        timePickerClose.setIs24HourView(true);
        Sunday=(CheckBox)findViewById(R.id.checkbox_tuesday);
        Monday=(CheckBox)findViewById(R.id.checkbox_monday);
        Tuesday=(CheckBox)findViewById(R.id.checkbox_tuesday);
        Wednesday=(CheckBox)findViewById(R.id.checkbox_wednesday);
        Thursday=(CheckBox)findViewById(R.id.checkbox_thursday);
        Friday=(CheckBox)findViewById(R.id.checkbox_friday);
        Saturday=(CheckBox)findViewById(R.id.checkbox_saturday);

        this.reloadData();
    }

    @Override
    protected void reloadData() {
        super.reloadData();
        Intent i = getIntent();
        address = i.getStringExtra("address");
        vatCode = i.getStringExtra("vat_code");
        taxCode = i.getStringExtra("tax_code");
        iban = i.getStringExtra("iban");
        foodType = i.getStringExtra("food_type");
        openHour = i.getStringExtra("opening_hour").split(":")[0];
        closeHour = i.getStringExtra("closing_hour").split(":")[0];
        openMinute = i.getStringExtra("opening_hour").split(":")[1];
        closeMinute = i.getStringExtra("closing_hour").split(":")[1];
        openDays = (HashMap) i.getSerializableExtra("open_days");

        this.reloadViews();
    }

    @Override
    protected void reloadViews() {
        super.reloadViews();
        addressTextField.setText(this.address);
        vatCodeTextField.setText(this.vatCode);
        taxCodeTextField.setText(this.taxCode);
        ibanTextField.setText(this.iban);
        foodTypeSpinner.setSelection(getIndex(foodTypeSpinner, foodType));
        timePickerOpen.setCurrentHour(Integer.parseInt(openHour));
        timePickerClose.setCurrentHour(Integer.parseInt(closeHour));
        timePickerOpen.setCurrentMinute(Integer.parseInt(openMinute));
        timePickerClose.setCurrentMinute(Integer.parseInt(closeMinute));

        if (openDays != null) {

            if (openDays.get("sunday") || false) {
                Sunday.setChecked(true);
            }
            if (openDays.get("monday") || false) {
                Monday.setChecked(true);
            }
            if (openDays.get("tuesday") || false) {
                Tuesday.setChecked(true);
            }
            if (openDays.get("wednesday") || false) {
                Wednesday.setChecked(true);
            }
            if (openDays.get("thursday") || false) {
                Thursday.setChecked(true);
            }
            if (openDays.get("friday") || false) {
                Friday.setChecked(true);
            }
            if (openDays.get("saturday") || false) {
                Saturday.setChecked(true);
            }
        }

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
                AlertDialog.Builder builder = new AlertDialog.Builder(SecondActivity.this);
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
                returnIntent.putExtra("name", nameTextField.getText().toString());
                returnIntent.putExtra("email", emailTextField.getText().toString());
                returnIntent.putExtra("phone", phoneTextField.getText().toString());
                returnIntent.putExtra("address", addressTextField.getText().toString());
                returnIntent.putExtra("vat_code", vatCodeTextField.getText().toString());
                returnIntent.putExtra("tax_code", taxCodeTextField.getText().toString());
                returnIntent.putExtra("iban", ibanTextField.getText().toString());
                returnIntent.putExtra("food_type", foodTypeSpinner.getSelectedItem().toString());
                returnIntent.putExtra("opening_hour", openAt);
                returnIntent.putExtra("closing_hour", closeAt);
                returnIntent.putExtra("nome", new HashMap<>());

                if (imageFileName != null) {
                    returnIntent.putExtra("imageFileName", imageFileName);
                }

                if (Sunday.isChecked()){
                    openDays.put("sunday", true);
                }else {
                    openDays.put("sunday", false);
                }

                if (Monday.isChecked()){
                    openDays.put("monday", true);
                }else {
                    openDays.put("monday", false);
                }

                if (Tuesday.isChecked()){
                    openDays.put("tuesday", true);
                }else {
                    openDays.put("tuesday", false);
                }

                if (Wednesday.isChecked()){
                    openDays.put("wednesday", true);
                }else {
                    openDays.put("wednesday", false);
                }

                if (Thursday.isChecked()){
                    openDays.put("thursday", true);
                }else {
                    openDays.put("thursday", false);
                }

                if (Friday.isChecked()){
                    openDays.put("friday", true);
                }else {
                    openDays.put("friday", false);
                }

                if (Saturday.isChecked()){
                    openDays.put("saturday", true);
                }else {
                    openDays.put("saturday", false);
                }

                returnIntent.putExtra("open_days", openDays);

                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("address", address);
        outState.putString("vat_code", vatCode);
        outState.putString("tax_code", taxCode);
        outState.putString("iban", iban);
        outState.putString("food_type", foodType);

        String openAt = Utils.beautifyTime(timePickerOpen.getCurrentHour(), timePickerOpen.getCurrentMinute());
        String closeAt = Utils.beautifyTime(timePickerClose.getCurrentHour(), timePickerClose.getCurrentMinute());
        outState.putString("opening_hour", closeAt);
        outState.putString("closing_hour", openAt);

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

        openHour = savedInstanceState.getString("opening_hour").split(":")[0];
        closeHour = savedInstanceState.getString("closing_hour").split(":")[0];
        openMinute = savedInstanceState.getString("opening_hour").split(":")[1];
        closeMinute = savedInstanceState.getString("closing_hour").split(":")[1];
        openDays = (HashMap) savedInstanceState.getSerializable("open_days");

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


