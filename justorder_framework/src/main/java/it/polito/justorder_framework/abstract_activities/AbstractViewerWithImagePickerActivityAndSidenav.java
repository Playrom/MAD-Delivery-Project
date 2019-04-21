package it.polito.justorder_framework.abstract_activities;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import it.polito.justorder_framework.R;

abstract public class AbstractViewerWithImagePickerActivityAndSidenav extends ActivityAbstractWithSideNav {

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