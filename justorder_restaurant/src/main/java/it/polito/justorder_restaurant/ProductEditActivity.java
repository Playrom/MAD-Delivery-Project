package it.polito.justorder_restaurant;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import it.polito.justorder_framework.abstract_activities.ActivityAbstractWithToolbar;
import it.polito.justorder_framework.Utils;
import it.polito.justorder_framework.db.Storage;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductEditActivity extends ActivityAbstractWithToolbar {

    private String name, cost, notes, imageFileName;
    private List<String> ingredients = new ArrayList<>();
    private EditText nameTextField, costTextField, notesTextField;
    private NachoTextView ingredientsChips;
    private Spinner spinner;
    private Integer category = 0;
    protected Button butt;
    protected ImageView image;
    private String mCurrentPhotoPath;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_edit_activity_layout);
        this.setupActivity();
    }

    @Override
    protected void setupActivity() {
        super.setupActivity();
        nameTextField = findViewById(R.id.nameTextField);
        costTextField = findViewById(R.id.costTextField);
        notesTextField = findViewById(R.id.notesTextField);
        ingredientsChips = findViewById(R.id.ingredientsChips);
        spinner = findViewById(R.id.spinner);
        image = findViewById(R.id.imageView);

        ingredientsChips.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);

        butt = findViewById(it.polito.justorder_framework.R.id.button);

        butt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ProductEditActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ProductEditActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(ProductEditActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                0);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                    return;

                }

                if (ContextCompat.checkSelfPermission(ProductEditActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ProductEditActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(ProductEditActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                1);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                    return;

                }

                Matisse.from(ProductEditActivity.this)
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

    @Override
    protected void reloadData() {
        super.reloadData();
        Intent i = getIntent();
        name = i.getStringExtra("name");
        cost = i.getStringExtra("cost");
        notes = i.getStringExtra("notes");

        if(i.getSerializableExtra("ingredients") != null){
            ingredients = (List) i.getSerializableExtra("ingredients");
        }

        category = i.getIntExtra("category", 0);
        imageFileName = i.getStringExtra("imageFileName");

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(this, R.array.foodTypes, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(staticAdapter);

        this.reloadViews();
    }

    @Override
    protected void reloadViews() {
        super.reloadViews();
        nameTextField.setText(this.name);
        costTextField.setText(this.cost);
        notesTextField.setText(this.notes);
        spinner.setSelection(category);

        if(imageFileName != null) {
            try {
                Bitmap selectedImage = BitmapFactory.decodeStream(getApplicationContext().openFileInput(imageFileName));
                image.setImageBitmap(selectedImage);
            } catch (Exception e) {

            }
        }

        this.ingredientsChips.setText(this.ingredients);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_product_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.saveProduct) {
            if (
                    nameTextField.getText().toString() == "" ||
                            nameTextField.getText().toString() == "" ||
                            costTextField.getText().toString() == "" ||
                            spinner.getSelectedItem().toString() == ""
            ) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductEditActivity.this);
                AlertDialog dialog = builder
                        .setTitle(R.string.validation_error)
                        .setMessage(R.string.field_are_not_valid)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();
                dialog.show();
            } else {

                Intent returnIntent = new Intent();
                returnIntent.putExtra("name", this.nameTextField.getText().toString());
                returnIntent.putExtra("cost", this.costTextField.getText().toString());
                returnIntent.putExtra("notes", this.notesTextField.getText().toString());
                returnIntent.putExtra("ingredients", (Serializable) this.ingredientsChips.getChipValues());
                returnIntent.putExtra("category", this.spinner.getSelectedItemPosition());

                if (imageFileName != null) {
                    returnIntent.putExtra("imageFileName", imageFileName);
                }

                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            List<Uri> images = Matisse.obtainResult(data);
            if (images.size() > 0) {
                Uri imageUri = images.get(0);
                try {
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = Utils.resizeBitmap(BitmapFactory.decodeStream(imageStream), 400);
                    imageFileName = Utils.createImageFromBitmap(selectedImage, ProductEditActivity.this);
                    Storage.INSTANCE.saveImage(imageUri);
                    image.setImageBitmap(selectedImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name", name);
        outState.putString("cost", cost);
        outState.putString("notes", notes);
        outState.putSerializable("ingredients", (Serializable) this.ingredients);
        outState.putInt("category", category);
        outState.putString("imageFileName", imageFileName);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        name = savedInstanceState.getString("name");
        cost = savedInstanceState.getString("cost");
        notes = savedInstanceState.getString("notes");
        ingredients = (List) savedInstanceState.getSerializable("ingredients");
        category = savedInstanceState.getInt("category");
        imageFileName = savedInstanceState.getString("imageFileName");

        reloadViews();
    }
}