package it.polito.justorder_framework.abstract_activities;

import android.view.WindowManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import it.polito.justorder_framework.R;

public class ActivityAbstractWithToolbar extends ActivityAbstract {
    protected ActionBar actionBar;

    protected void setupActivity() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.actionBar = getSupportActionBar();
        this.actionBar.setDisplayHomeAsUpEnabled(true);
        this.actionBar.setDisplayShowHomeEnabled(true);
    }

}
