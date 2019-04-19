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

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;

import it.polito.justorder_framework.R;
import it.polito.justorder_framework.Utils;
import it.polito.justorder_framework.common_activities.UserSettingsEditorActivity;
import it.polito.justorder_framework.db.Users;
import it.polito.justorder_framework.model.User;
import kotlin.Unit;

abstract public class AbstractViewerWithImagePickerActivity extends ActivityAbstractWithSideNav {

    protected ImageView image;
    protected String imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setupActivity() {
        super.setupActivity();
        image = findViewById(R.id.imageView);
    }

    @Override
    protected void reloadData() {
        super.reloadData();
    }

    protected void reloadViews(){
        super.reloadViews();
        if(imageUri != null){
            Glide.with(this).load(imageUri).into(image);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_first_menu, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("imageUri", imageUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        imageUri = savedInstanceState.getString("imageUri");

        reloadViews();
    }
}