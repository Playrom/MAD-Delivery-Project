package it.polito.justorder_framework.common_activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import it.polito.justorder_framework.R;
import it.polito.justorder_framework.abstract_activities.SecondActivityAbstract;
import it.polito.justorder_framework.Utils;

public class UserSettingsEditorActivity extends SecondActivityAbstract {

    protected String address;
    protected EditText addressTextField;

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
        this.reloadData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.saveUserData) {
            if (
                    nameTextField.getText().toString() == "" ||
                            emailTextField.getText().toString() == "" ||
                            phoneTextField.getText().toString() == "" ||
                            !Utils.isValidEmail(emailTextField.getText().toString())
            ) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserSettingsEditorActivity.this);
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
                user.setName(nameTextField.getText().toString());
                user.setEmail(emailTextField.getText().toString());
                user.setTelephone(phoneTextField.getText().toString());

                returnIntent.putExtra("user", user);
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
