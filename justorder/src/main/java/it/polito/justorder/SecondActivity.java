package it.polito.justorder;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.view.MenuItem;
import android.widget.EditText;

import it.polito.justorder_framework.SecondActivityAbstract;
import it.polito.justorder_framework.Utils;

public class SecondActivity extends SecondActivityAbstract {

    protected String address;
    protected EditText addressTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        this.setupActivity();
    }

    @Override
    protected void setupActivity() {
        super.setupActivity();
        addressTextField = findViewById(R.id.addressTextField);
        this.reloadData();
    }

    @Override
    protected void reloadData() {
        super.reloadData();
        Intent i = getIntent();
        address = i.getStringExtra("address");
        this.reloadViews();
    }

    @Override
    protected void reloadViews() {
        super.reloadViews();
        addressTextField.setText(address);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.saveUserData) {
            if (
                    nameTextField.getText().toString() == "" ||
                            emailTextField.getText().toString() == "" ||
                            phoneTextField.getText().toString() == "" ||
                            addressTextField.getText().toString() == "" ||
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
        outState.putString("address", addressTextField.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        address = savedInstanceState.getString("address");
        reloadViews();
    }
}
