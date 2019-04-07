package it.polito.justorder_restaurant;

import androidx.annotation.Nullable;
import it.polito.justorder_framework.ActivityAbstractWithToolbar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;

public class ProductActivity extends ActivityAbstractWithToolbar {

    private String imageFileName, name, cost, notes, ingredients, categoryName;
    private EditText nameTextField, costTextField, notesTextField, ingredientsTextField;
    private TextView categoryTextView;
    private Integer category = 0;
    private ImageView image;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_activity_layout);
        this.setupActivity();
    }

    @Override
    protected void setupActivity() {
        super.setupActivity();
        nameTextField = findViewById(R.id.nameTextField);
        costTextField = findViewById(R.id.costTextField);
        notesTextField = findViewById(R.id.notesTextField);
        ingredientsTextField = findViewById(R.id.ingredientsTextField);
        image = findViewById(R.id.imageView);
        categoryTextView = findViewById(R.id.categoryTextView);
        this.reloadData();
    }

    @Override
    protected void reloadData() {
        super.reloadData();
        Intent i = getIntent();
        this.name = i.getStringExtra("name");
        this.cost = i.getStringExtra("cost");
        this.notes = i.getStringExtra("notes");
        this.ingredients = i.getStringExtra("ingredients");

        if(i.getStringExtra("category") != null){
            String[] strs = getResources().getStringArray(R.array.foodTypes);
            for(int k = 0; k < strs.length ; k++){
                if(strs[k].equals(i.getStringExtra("category"))){
                    this.category = k;
                    break;
                }
            }
        }
        this.imageFileName = i.getStringExtra("imageFileName");
        this.reloadViews();
    }

    @Override
    protected void reloadViews() {
        super.reloadViews();
        nameTextField.setText(this.name);
        costTextField.setText(this.cost);
        notesTextField.setText(this.notes);
        ingredientsTextField.setText(this.notes);

        String[] types = getResources().getStringArray(R.array.foodTypes);
        if(types.length > this.category){
            this.categoryName = types[this.category];
            categoryTextView.setText(categoryName);
        }

        if(imageFileName != null) {
            try {
                Bitmap selectedImage = BitmapFactory.decodeStream(getApplicationContext().openFileInput(imageFileName));
                image.setImageBitmap(selectedImage);
            } catch (Exception e) {

            }
        }

        if(this.name != null){
            this.actionBar.setTitle(this.name);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_product_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.editProduct){
            Intent i = new Intent(getApplicationContext(), ProductEditActivity.class);
            i.putExtra("name", this.name);
            i.putExtra("cost", this.cost);
            i.putExtra("notes", this.notes);
            i.putExtra("ingredients", this.ingredients);
            i.putExtra("category", this.category);
            i.putExtra("imageFileName", this.imageFileName);

            startActivityForResult(i, 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(requestCode == 1){
            if(resultCode== Activity.RESULT_OK){
                name = data.getStringExtra("name");
                cost = data.getStringExtra("cost");
                notes = data.getStringExtra("notes");
                ingredients = data.getStringExtra("ingredients");
                category = data.getIntExtra("category", 0);
                imageFileName = data.getStringExtra("imageFileName");

                editor.apply();

                this.reloadViews();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("name", this.name);
        returnIntent.putExtra("cost", this.cost);
        returnIntent.putExtra("notes", this.notes);
        returnIntent.putExtra("ingredients", this.ingredients);
        returnIntent.putExtra("category", this.categoryName);

        if (imageFileName != null) {
            returnIntent.putExtra("imageFileName", imageFileName);
        }

        setResult(Activity.RESULT_OK, returnIntent);
        onBackPressed();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name", name);
        outState.putString("cost", cost);
        outState.putString("notes", notes);
        outState.putString("ingredients", ingredients);
        outState.putInt("category", category);
        outState.putString("imageFileName", imageFileName);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        name = savedInstanceState.getString("name");
        cost = savedInstanceState.getString("cost");
        notes = savedInstanceState.getString("notes");
        ingredients = savedInstanceState.getString("ingredients");
        category = savedInstanceState.getInt("category");
        imageFileName = savedInstanceState.getString("imageFileName");

        reloadViews();
    }
}
