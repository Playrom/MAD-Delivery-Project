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

import it.polito.justorder_framework.SecondActivityAbstract;
import it.polito.justorder_framework.Utils;

public class SecondActivity extends SecondActivityAbstract {

    protected String address, vatCode, taxCode, iban, foodType;
    protected EditText addressTextField, vatCodeTextField, taxCodeTextField, ibanTextField;
    protected Spinner foodTypeSpinner;
    protected String openHour, openMinute, closeHour, closeMinute;
    protected TimePicker timePickerOpen, timePickerClose;
    protected CheckBox Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday;
    protected String openDays= "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent i = getIntent();
        address = i.getStringExtra("address");
        vatCode = i.getStringExtra("vat_code");
        taxCode = i.getStringExtra("tax_code");
        iban = i.getStringExtra("iban");
        foodType = i.getStringExtra("food_type");
        openHour = i.getStringExtra("openHour");
        closeHour = i.getStringExtra("closeHour");
        openMinute = i.getStringExtra("openMinute");
        closeMinute = i.getStringExtra("closeMinute");
        openDays = i.getStringExtra("openDays");


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
        timePickerClose = findViewById(R.id.timePickerClose);
        Sunday=(CheckBox)findViewById(R.id.checkbox_tuesday);
        Monday=(CheckBox)findViewById(R.id.checkbox_monday);
        Tuesday=(CheckBox)findViewById(R.id.checkbox_tuesday);
        Wednesday=(CheckBox)findViewById(R.id.checkbox_wednesday);
        Thursday=(CheckBox)findViewById(R.id.checkbox_thursday);
        Friday=(CheckBox)findViewById(R.id.checkbox_friday);
        Saturday=(CheckBox)findViewById(R.id.checkbox_saturday);


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
        timePickerClose.setCurrentMinute(Integer.parseInt(closeHour));
        timePickerOpen.setCurrentHour(Integer.parseInt(openMinute));
        timePickerClose.setCurrentMinute(Integer.parseInt(closeMinute));

            if (openDays != null) {

                if (openDays.contains("Sun")) {
                    Sunday.setChecked(true);
                }
                if (openDays.contains("Mon")) {
                    Monday.setChecked(true);
                }
                if (openDays.contains("Tue")) {
                    Tuesday.setChecked(true);
                }
                if (openDays.contains("Wed")) {
                    Wednesday.setChecked(true);
                }
                if (openDays.contains("Thu")) {
                    Thursday.setChecked(true);
                }
                if (openDays.contains("Fri")) {
                    Friday.setChecked(true);
                }
                if (openDays.contains("Sat")) {
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

                String closeAt = Integer.toString(timePickerOpen.getCurrentHour()) + ":" + Integer.toString(timePickerOpen.getCurrentMinute());
                String openAt = Integer.toString(timePickerClose.getCurrentHour()) + ":" + Integer.toString(timePickerClose.getCurrentMinute());

                Intent returnIntent = new Intent();
                returnIntent.putExtra("name", nameTextField.getText().toString());
                returnIntent.putExtra("email", emailTextField.getText().toString());
                returnIntent.putExtra("phone", phoneTextField.getText().toString());
                returnIntent.putExtra("address", addressTextField.getText().toString());
                returnIntent.putExtra("vat_code", vatCodeTextField.getText().toString());
                returnIntent.putExtra("tax_code", taxCodeTextField.getText().toString());
                returnIntent.putExtra("iban", ibanTextField.getText().toString());
                returnIntent.putExtra("food_type", foodTypeSpinner.getSelectedItem().toString());
                returnIntent.putExtra("openingHour", closeAt);
                returnIntent.putExtra("closingHour", openAt);

                if (imageFileName != null) {
                    returnIntent.putExtra("imageFileName", imageFileName);
                }

                if (Sunday.isChecked()){
                    openDays +="Sun ";
                }
                if (Monday.isChecked()){
                    openDays +="Mon ";
                }
                if (Tuesday.isChecked()){
                    openDays +="Tue ";
                }
                if (Wednesday.isChecked()){
                    openDays +="Wed ";
                }
                if (Thursday.isChecked()){
                    openDays +="Thu ";
                }
                if (Friday.isChecked()){
                    openDays +="Fri ";
                }
                if (Saturday.isChecked()){
                    openDays +="Sat ";
                }

                returnIntent.putExtra("openDays", openDays);


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
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        address = savedInstanceState.getString("address");
        vatCode = savedInstanceState.getString("vat_code");
        taxCode = savedInstanceState.getString("tax_code");
        iban = savedInstanceState.getString("iban");
        foodType = savedInstanceState.getString("food_type");

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


