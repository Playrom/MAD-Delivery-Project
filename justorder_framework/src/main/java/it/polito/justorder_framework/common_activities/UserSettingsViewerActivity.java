package it.polito.justorder_framework.common_activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import it.polito.justorder_framework.FirebaseFunctions;
import it.polito.justorder_framework.abstract_activities.MainActivityAbstract;
import it.polito.justorder_framework.R;
import it.polito.justorder_framework.db.Users;
import kotlin.Unit;

public class UserSettingsViewerActivity extends MainActivityAbstract {
    protected String address;
    protected EditText addressTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usersettings_viewer_activity);
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
        this.reloadViews();
    }

    @Override
    protected void reloadViews() {
        super.reloadViews();
        if(!FirebaseFunctions.isLoggedIn()){
            FirebaseFunctions.login(this);
        }
    }
}