package it.polito.justorder_deliverer;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import it.polito.justorder_framework.abstract_activities.AbstractEditor;
import it.polito.justorder_framework.model.Deliverer;

public class DelivererSettingsEditorActivity extends AbstractEditor {

    protected Deliverer deliverer;
    protected EditText taxCodeTextField, ibanTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        if(i.getSerializableExtra("deliverer") != null && i.getSerializableExtra("deliverer") instanceof Deliverer){
            this.deliverer = (Deliverer) i.getSerializableExtra("deliverer");
        }else{
            this.deliverer = new Deliverer();
        }
        setContentView(R.layout.deliverer_settings_editor);
        this.setupActivity();
    }


    @Override
    protected void setupActivity() {
        super.setupActivity();
        taxCodeTextField = findViewById(R.id.taxCodeTextField);
        ibanTextField = findViewById(R.id.ibanTextField);
        this.reloadData();
    }

    @Override
    protected void reloadData() {
        super.reloadData();
        this.reloadViews();
    }

    @Override
    protected void reloadViews() {
        super.reloadViews();
        if(this.deliverer != null) {
            taxCodeTextField.setText(deliverer.getFiscalCode());
            ibanTextField.setText(deliverer.getIban());
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.saveUserData) {
            if (
                    taxCodeTextField.getText().toString() == "" ||
                            ibanTextField.getText().toString() == ""
            ) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DelivererSettingsEditorActivity.this);
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
                deliverer.setFiscalCode(taxCodeTextField.getText().toString());
                deliverer.setIban(ibanTextField.getText().toString());

                returnIntent.putExtra("deliverer", deliverer);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        deliverer.setFiscalCode(taxCodeTextField.getText().toString());
        deliverer.setIban(ibanTextField.getText().toString());
        outState.putSerializable("deliverer", deliverer);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        deliverer = (Deliverer) savedInstanceState.getSerializable("restaurant");

        reloadViews();
    }

}


