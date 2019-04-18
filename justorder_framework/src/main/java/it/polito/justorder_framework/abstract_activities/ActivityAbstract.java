package it.polito.justorder_framework.abstract_activities;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityAbstract extends AppCompatActivity {
    protected void reloadViews() {}
    protected void reloadData() {this.reloadViews();}
}
