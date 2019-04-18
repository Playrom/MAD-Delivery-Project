package it.polito.justorder_framework.abstract_activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;

import it.polito.justorder_framework.R;
import it.polito.justorder_framework.Utils;
import it.polito.justorder_framework.common_activities.UserSettingsEditorActivity;
import it.polito.justorder_framework.db.Users;
import it.polito.justorder_framework.model.User;
import kotlin.Unit;

abstract public class MainActivityAbstract extends ActivityAbstractWithSideNav {

    protected EditText nameTextField, emailTextField, phoneTextField;
    protected ImageView image;
    protected String imageFileName, name, email, phone;
    protected User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setupActivity() {
        super.setupActivity();
        nameTextField = findViewById(R.id.nameTextField);
        emailTextField = findViewById(R.id.emailTextField);
        phoneTextField = findViewById(R.id.phoneTextField);
        image = findViewById(R.id.imageView);
    }

    @Override
    protected void reloadData() {
        super.reloadData();
        Users.INSTANCE.getUser(FirebaseAuth.getInstance().getCurrentUser().getUid(), (user) -> {
            this.user = user;
            this.reloadViews();
            return Unit.INSTANCE;
        });
    }

    protected void reloadViews(){
        super.reloadViews();
        if(user != null){
            nameTextField.setText(user.getName());
            emailTextField.setText(user.getEmail());
            phoneTextField.setText(user.getTelephone());

            if(user.getImageUri() != null) {
                Utils.getBitmapFromURL(user.getImageUri(), (selectedImage -> {
                    this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            image.setImageBitmap(selectedImage);
                        }
                    });
                }));
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_first_menu, menu);
        return true;
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