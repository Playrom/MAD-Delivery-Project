package it.polito.justorder_restaurant;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import it.polito.justorder_framework.abstract_activities.AbstractEditorWithImagePickerActivity;
import it.polito.justorder_framework.abstract_activities.ActivityAbstractWithToolbar;
import it.polito.justorder_framework.Utils;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.db.Storage;
import it.polito.justorder_framework.model.Product;
import kotlin.Unit;

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
import java.util.Arrays;
import java.util.List;

public class ProductEditActivity extends AbstractEditorWithImagePickerActivity {

    protected Product product;
    private List<String> ingredients = new ArrayList<>();
    private EditText nameTextField, costTextField, notesTextField;
    private NachoTextView ingredientsChips;
    private Spinner spinner;
    protected List<String> types = new ArrayList<>();

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
        this.product = (Product) i.getSerializableExtra("product");
        if(this.product == null){
            this.product = new Product();
        }else{
            this.imageUri = this.product.getImageUri();
        }

        this.types = Arrays.asList(getResources().getStringArray(it.polito.justorder_framework.R.array.foodTypes));

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
        if(product != null) {
            nameTextField.setText(product.getName());
            String x = String.format("%.02f", new Float(product.getCost()));
            costTextField.setText(x);
            notesTextField.setText(product.getNotes());

            if(product.getName() != null){
                this.actionBar.setTitle(product.getName());
            }

            spinner.setSelection(this.types.indexOf(product.getCategory()));
            String y ="";
            for(String h : product.getIngredients()){
                y += h + ", ";
            }
            y = y.replaceAll(", $", "");

            this.ingredientsChips.setText(y);
        }
    }

    protected Unit saveImageUri(Uri uri){
        this.product.setImageUri(uri.toString());
        return Unit.INSTANCE;
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
                this.product.setName(this.nameTextField.getText().toString());
                this.product.setCost(new Double(this.costTextField.getText().toString()));

                String str = this.ingredientsChips.toString();
                ArrayList aList= new ArrayList(Arrays.asList(str.split(",")));


                this.product.setNotes(this.notesTextField.getText().toString());
                this.product.setIngredients(aList);
                this.product.setCategory(this.types.get(spinner.getSelectedItemPosition()));

                returnIntent.putExtra("product", product);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("product", product);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        product = (Product) savedInstanceState.getSerializable("product");

        reloadViews();
    }
}