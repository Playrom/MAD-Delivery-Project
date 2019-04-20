package it.polito.justorder_framework.common_activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import it.polito.justorder_framework.FirebaseFunctions;
import it.polito.justorder_framework.abstract_activities.AbstractViewerWithImagePickerActivity;
import it.polito.justorder_framework.R;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.db.Users;
import it.polito.justorder_framework.model.User;
import kotlin.Unit;

public class UserSettingsViewerActivity extends AbstractViewerWithImagePickerActivity {

    protected EditText nameTextField, emailTextField, phoneTextField, addressTextField;
    protected User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usersettings_viewer_activity);
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
        this.user = Database.INSTANCE.getCurrent_User();
        if(this.user != null){
            this.imageUri = this.user.getImageUri();
        }else{
            this.user = new User();
        }
        this.reloadViews();
    }

    @Override
    protected void reloadViews() {
        super.reloadViews();
        if(!FirebaseFunctions.isLoggedIn()){
            FirebaseFunctions.login(this);
        }else{
            if(user != null){
                nameTextField.setText(user.getName());
                emailTextField.setText(user.getEmail());
                phoneTextField.setText(user.getTelephone());
                addressTextField.setText(user.getAddress());
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.editUserData){
            Intent i = new Intent(getApplicationContext(), UserSettingsEditorActivity.class);
            i.putExtra("user", user);

            startActivityForResult(i, 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode== Activity.RESULT_OK){
                user = (User) data.getSerializableExtra("user");
                Users.INSTANCE.saveUser(user);
                reloadViews();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("user", user);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        user = (User) savedInstanceState.getSerializable("user");
        reloadViews();
    }
}