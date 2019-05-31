package it.polito.justorder;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import it.polito.justorder.R;
import it.polito.justorder_framework.Utils;
import it.polito.justorder_framework.abstract_activities.AbstractEditor;
import it.polito.justorder_framework.common_activities.UserSettingsEditorActivity;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Order;
import it.polito.justorder_framework.model.Product;
import it.polito.justorder_framework.model.Restaurant;
import it.polito.justorder_framework.model.User;
import kotlin.Unit;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.HashMap;
import java.util.Map;

public class CartProductActivity extends AbstractEditor {

    protected String quantity = "";
    protected EditText quantityTextView;
    protected Integer qty;
    protected Product product;
    protected User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_product_activity);
        this.setupActivity();
    }

    @Override
    protected void setupActivity() {
        super.setupActivity();
        quantityTextView = (EditText) findViewById(R.id.quantity);
        this.reloadData();
    }

    @Override
    protected void reloadData() {
        super.reloadData();
        Intent intent = getIntent();
        product = (Product) intent.getSerializableExtra("product");
        qty = (Integer) intent.getSerializableExtra("qty");
        quantity = qty.toString();
        user = Database.INSTANCE.getCurrent_User();
        this.reloadViews();
    }

    @Override
    protected void reloadViews() {
        super.reloadViews();
        if(this.user != null){
            quantityTextView.setText(quantity);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == it.polito.justorder_framework.R.id.saveUserData) {

            Integer qty = new Integer(quantityTextView.getText().toString());
            Intent intent = new Intent();
            intent.putExtra("qty", qty);
            intent.putExtra("product", product);
            setResult(RESULT_OK, intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("quantity", quantityTextView.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        quantity = savedInstanceState.getString("quantity");
        this.reloadViews();
    }
}
