package it.polito.justorder_framework.common_activities;

import androidx.annotation.Nullable;

import it.polito.justorder_framework.R;
import it.polito.justorder_framework.abstract_activities.AbstractViewerWithImagePickerActivityAndSidenav;
import it.polito.justorder_framework.abstract_activities.AbstractViewerWithImagePickerActivityAndToolbar;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.db.Products;
import it.polito.justorder_framework.model.Product;
import it.polito.justorder_framework.model.Restaurant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import java.util.Arrays;
import java.util.List;

public class ProductActivity extends AbstractViewerWithImagePickerActivityAndToolbar {

    protected Product product;
    protected Restaurant restaurant;
    protected EditText nameTextField, costTextField, notesTextField;
    protected TextView categoryTextView, ingredientsTextView;
    protected Button orderButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_activity_layout);
        this.setupActivity();
    }

    @Override
    protected void setupActivity() {
        super.setupActivity();
        Intent i = getIntent();
        this.restaurant = (Restaurant) i.getSerializableExtra("restaurant");
        nameTextField = findViewById(R.id.nameTextField);
        costTextField = findViewById(R.id.costTextField);
        notesTextField = findViewById(R.id.notesTextField);
        ingredientsTextView = findViewById(R.id.ingredientsTextView);
        image = findViewById(R.id.imageView);
        categoryTextView = findViewById(R.id.categoryTextView);
        orderButton = findViewById(R.id.order_button);
        this.reloadData();
    }

    @Override
    protected void reloadData() {
        super.reloadData();
        orderButton.setVisibility(View.GONE);
        Intent i = getIntent();
        this.product = (Product) i.getSerializableExtra("product");
        if(this.product == null){
            this.product = new Product();
        }else{
            this.imageUri = this.product.getImageUri();
        }
        this.reloadViews();
    }

    @Override
    protected void reloadViews() {
        super.reloadViews();
        List<String> types = Arrays.asList(getResources().getStringArray(R.array.foodTypes));
        if(product != null) {
            nameTextField.setText(product.getName());
            costTextField.setText(new Double(product.getCost()).toString());
            notesTextField.setText(product.getNotes());
            String ingredients = "";
            for (String entry : product.getIngredients()) {
                ingredients = ingredients + "&#8226; " + entry + "<br/>\n";
            }
            ingredientsTextView.setText(Html.fromHtml(ingredients));

            String category = types.stream().filter(x -> {
                return x.equals(product.getCategory());
            }).findFirst().orElse(null);
            if (category != null) {
                categoryTextView.setText(category);
            }

            if (product.getName() != null) {
                this.actionBar.setTitle(product.getName());
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("product", product);
        setResult(Activity.RESULT_OK, returnIntent);
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
