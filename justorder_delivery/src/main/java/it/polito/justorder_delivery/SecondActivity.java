package it.polito.justorder_delivery;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.view.MenuItem;
import android.widget.EditText;

import it.polito.justorder_framework.abstract_activities.AbstractEditorWithImagePickerActivity;
import it.polito.justorder_framework.Utils;

public class SecondActivity extends AbstractEditorWithImagePickerActivity {

    protected String taxCode, iban;
    protected EditText taxCodeTextField, ibanTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usersettings_editor_activity);
        this.setupActivity();
    }

    @Override
    protected void setupActivity() {
        super.setupActivity();
        taxCodeTextField = findViewById(R.id.taxCodeTextField);
        ibanTextField = findViewById(R.id.ibanTextField);
        this.reloadData();
    }

    @Override
    protected void reloadData() {
        super.reloadData();
        Intent i = getIntent();
        taxCode = i.getStringExtra("tax_code");
        iban = i.getStringExtra("iban");
        this.reloadViews();
    }

    @Override
    protected void reloadViews() {
        super.reloadViews();
        taxCodeTextField.setText(this.taxCode);
        ibanTextField.setText(this.iban);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.saveUserData) {
            if (
                    nameTextField.getText().toString() == "" ||
                            emailTextField.getText().toString() == "" ||
                            phoneTextField.getText().toString() == "" ||
                            taxCodeTextField.getText().toString() == "" ||
                            ibanTextField.getText().toString() == "" ||
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
                returnIntent.putExtra("tax_code", taxCodeTextField.getText().toString());
                returnIntent.putExtra("iban", ibanTextField.getText().toString());
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
