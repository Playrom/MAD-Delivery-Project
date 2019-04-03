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

abstract public class MainActivityAbstract extends AppCompatActivity {

    protected EditText nameTextField, emailTextField, phoneTextField;
    protected ImageView image;
    protected String imageFileName, name, email, phone;

    protected DrawerLayout drawerLayout;

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
        nameTextField = findViewById(R.id.nameTextField);
        emailTextField = findViewById(R.id.emailTextField);
        phoneTextField = findViewById(R.id.phoneTextField);
        image = findViewById(R.id.imageView);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this.getNavigationListener());

    }

    protected NavigationView.OnNavigationItemSelectedListener getNavigationListener() {

        return new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                // set item as selected to persist highlight
                menuItem.setChecked(true);
                // close drawer when item is tapped
                drawerLayout.closeDrawers();

                return true;
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
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