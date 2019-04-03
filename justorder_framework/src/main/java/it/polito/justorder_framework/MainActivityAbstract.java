package it.polito.justorder_framework;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;

abstract public class MainActivityAbstract extends ActivityAbstractWithSideNav {

    protected EditText nameTextField, emailTextField, phoneTextField;
    protected ImageView image;
    protected String imageFileName, name, email, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        name = sharedPreferences.getString("name", "");
        email = sharedPreferences.getString("email", "");
        phone = sharedPreferences.getString("phone", "");
        imageFileName = sharedPreferences.getString("imageFileName", null);

    }

    protected void setupActivity() {
        super.setupActivity();
        nameTextField = findViewById(R.id.nameTextField);
        emailTextField = findViewById(R.id.emailTextField);
        phoneTextField = findViewById(R.id.phoneTextField);
        image = findViewById(R.id.imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_first_menu, menu);
        return true;
    }

    protected void reloadViews(){
        nameTextField.setText(name);
        emailTextField.setText(email);
        phoneTextField.setText(phone);

        if(imageFileName != null) {
            try {
                Bitmap selectedImage = BitmapFactory.decodeStream(getApplicationContext().openFileInput(imageFileName));
                image.setImageBitmap(selectedImage);
            } catch (Exception e) {

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(requestCode == 1){
            if(resultCode== Activity.RESULT_OK){
                name = data.getStringExtra("name");
                email = data.getStringExtra("email");
                phone = data.getStringExtra("phone");
                imageFileName = data.getStringExtra("imageFileName");

                editor.putString("name", data.getStringExtra("name"));
                editor.putString("email", data.getStringExtra("email"));
                editor.putString("address", data.getStringExtra("address"));
                editor.putString("phone", data.getStringExtra("phone"));
                editor.putString("imageFileName", imageFileName);
                editor.apply();

                reloadViews();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name", name);
        outState.putString("email", email);
        outState.putString("phone", phone);
        outState.putString("imageFileName", imageFileName);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        name = savedInstanceState.getString("name");
        email = savedInstanceState.getString("email");
        phone = savedInstanceState.getString("phone");
        imageFileName = savedInstanceState.getString("imageFileName");

        reloadViews();
    }
}