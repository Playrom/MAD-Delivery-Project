package it.polito.justorder_restaurant;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;

import it.polito.justorder_framework.SecondActivityAbstract;
import it.polito.justorder_framework.Utils;

public class SecondActivity extends SecondActivityAbstract {

    protected String address, vatCode, taxCode, iban, foodType;
    protected EditText addressTextField, vatCodeTextField, taxCodeTextField, ibanTextField;
    protected Spinner foodTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_second);
        Intent i = getIntent();

        addressTextField = findViewById(R.id.addressTextField);
        vatCodeTextField = findViewById(R.id.vatCodeTextField);
        taxCodeTextField = findViewById(R.id.taxCodeTextField);
        ibanTextField = findViewById(R.id.ibanTextField);
        foodTypeSpinner = findViewById(R.id.foodTypeSpinner);

        address = i.getStringExtra("address");
        vatCode = i.getStringExtra("vat_code");
        taxCode = i.getStringExtra("tax_code");
        iban = i.getStringExtra("iban");
        foodType = i.getStringExtra("food_type");

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void reloadViews() {
        super.reloadViews();
        addressTextField.setText(this.address);
        vatCodeTextField.setText(this.vatCode);
        taxCodeTextField.setText(this.taxCode);
        ibanTextField.setText(this.iban);
        foodTypeSpinner.setSelection(getIndex(foodTypeSpinner, foodType));
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

                Intent returnIntent = new Intent();
                returnIntent.putExtra("name", nameTextField.getText().toString());
                returnIntent.putExtra("email", emailTextField.getText().toString());
                returnIntent.putExtra("phone", phoneTextField.getText().toString());
                returnIntent.putExtra("address", addressTextField.getText().toString());
                returnIntent.putExtra("vat_code", vatCodeTextField.getText().toString());
                returnIntent.putExtra("tax_code", taxCodeTextField.getText().toString());
                returnIntent.putExtra("iban", ibanTextField.getText().toString());
                returnIntent.putExtra("food_type", foodTypeSpinner.getSelectedItem().toString());
                if (imageFileName != null) {
                    returnIntent.putExtra("imageFileName", imageFileName);
                }
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
