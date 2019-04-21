package it.polito.justorder_framework.common_activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.auth.FirebaseAuth;

import it.polito.justorder_framework.R;
import it.polito.justorder_framework.abstract_activities.AbstractEditorWithImagePickerActivity;
import it.polito.justorder_framework.Utils;
import it.polito.justorder_framework.model.User;
import kotlin.Unit;

public class UserSettingsEditorActivity extends AbstractEditorWithImagePickerActivity {

    protected EditText nameTextField, emailTextField, phoneTextField, addressTextField;
    protected User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usersettings_editor_activity);
        user = (User) i.getSerializableExtra("user");
        this.setupActivity();
    }

    @Override
    protected void setupActivity() {
        super.setupActivity();
        nameTextField = findViewById(R.id.nameTextField);
        emailTextField = findViewById(R.id.emailTextField);
        phoneTextField = findViewById(R.id.phoneTextField);
        addressTextField = findViewById(R.id.addressTextField);
        this.reloadData();
    }

    @Override
    protected void reloadData() {
        super.reloadData();
        this.imageUri = this.user.getImageUri();
        this.reloadViews();
    }

    @Override
    protected void reloadViews() {
        super.reloadViews();
        if(this.user != null){
            nameTextField.setText(user.getName());
            emailTextField.setText(user.getEmail());
            phoneTextField.setText(user.getTelephone());
            addressTextField.setText(user.getAddress());
        }
    }

    @Override
    protected Unit saveImageUri(Uri uri) {
        user.setImageUri(uri.toString());
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
                user.setAddress(addressTextField.getText().toString());

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
        user.setName(nameTextField.getText().toString());
        user.setEmail(emailTextField.getText().toString());
        user.setTelephone(phoneTextField.getText().toString());
        user.setAddress(addressTextField.getText().toString());
        outState.putSerializable("user", user);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        user =(User) savedInstanceState.getSerializable("user");
        reloadViews();
    }
}
