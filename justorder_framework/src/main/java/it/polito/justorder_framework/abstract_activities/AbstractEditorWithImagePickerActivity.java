package it.polito.justorder_framework.abstract_activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import it.polito.justorder_framework.R;
import it.polito.justorder_framework.Utils;
import it.polito.justorder_framework.db.Storage;
import it.polito.justorder_framework.model.User;
import kotlin.Unit;

public class AbstractEditorWithImagePickerActivity extends ActivityAbstractWithToolbar {

    protected Button butt;
    protected ImageView image;
    protected Intent i;
    protected String imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.i = getIntent();
    }

    protected void setupActivity(){
        super.setupActivity();
        image = findViewById(R.id.imageView);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);

        butt = findViewById(R.id.button);

        butt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AbstractEditorWithImagePickerActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(AbstractEditorWithImagePickerActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(AbstractEditorWithImagePickerActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                0);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                    return;

                }

                if (ContextCompat.checkSelfPermission(AbstractEditorWithImagePickerActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(AbstractEditorWithImagePickerActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(AbstractEditorWithImagePickerActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                1);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                    return;

                }
                Matisse.from(AbstractEditorWithImagePickerActivity.this)
                        .choose(MimeType.ofImage())
                        .maxSelectable(1)
                        .capture(true)
                        .captureStrategy(new CaptureStrategy(true, getResources().getString(R.string.fileprovider)))
                        .imageEngine(new GlideEngine())
                        .forResult(0);
            }
        });
    }

    protected void reloadViews(){
        super.reloadViews();

        if(imageUri != null) {
            Glide.with(this).load(imageUri).into(image);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            List<Uri> images = Matisse.obtainResult(data);
            if (images.size() > 0) {
                Uri imageUri = images.get(0);
                try {
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    try{
                        final byte[] byteArray = Utils.getByteArray(imageStream);
                        final Bitmap selectedImage = BitmapFactory.decodeByteArray(byteArray, 0 , byteArray.length);
                        image.setImageBitmap(selectedImage);
                        Storage.INSTANCE.saveImage(byteArray, uri -> saveImageUri(uri));
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    protected Unit saveImageUri(Uri uri){
        return Unit.INSTANCE;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_second_menu, menu);
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
