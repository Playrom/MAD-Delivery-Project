package it.polito.justorder_framework.abstract_activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

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

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import it.polito.justorder_framework.R;
import it.polito.justorder_framework.Utils;
import it.polito.justorder_framework.db.Storage;
import it.polito.justorder_framework.db.Users;
import it.polito.justorder_framework.model.User;
import kotlin.Unit;

public class SecondActivityAbstract extends ActivityAbstractWithToolbar {

    protected String name, email, phone, imageFileName;
    protected EditText nameTextField, emailTextField, phoneTextField;
    protected Button butt;
    protected ImageView image;
    protected User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        user = (User) i.getSerializableExtra("user");
    }

    protected void setupActivity(){
        super.setupActivity();
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

        butt = findViewById(R.id.button);

        butt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(SecondActivityAbstract.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(SecondActivityAbstract.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(SecondActivityAbstract.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                0);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                    return;

                }

                if (ContextCompat.checkSelfPermission(SecondActivityAbstract.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(SecondActivityAbstract.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(SecondActivityAbstract.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                1);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                    return;

                }
                Matisse.from(SecondActivityAbstract.this)
                        .choose(MimeType.ofImage())
                        .maxSelectable(1)
                        .capture(true)
                        .captureStrategy(new CaptureStrategy(true, getResources().getString(R.string.fileprovider)))
                        .imageEngine(new GlideEngine())
                        .forResult(0);
            }
        });

        this.reloadData();

    }

    protected void reloadViews(){
        super.reloadViews();
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
                        Storage.INSTANCE.saveImage(byteArray, uri -> {
                            user.setImageUri(uri.toString());
                            return Unit.INSTANCE;
                        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_second_menu, menu);
        return true;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("user", user);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        user =(User) savedInstanceState.getSerializable("user");

        reloadViews();
    }
}
