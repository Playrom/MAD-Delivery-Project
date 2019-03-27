package it.polito.justorder_delivery;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import it.polito.justorder_framework.MainActivityAbstract;

public class MainActivity extends MainActivityAbstract {
    protected String taxCode, iban;
    protected EditText taxCodeTextField, ibanTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        taxCodeTextField = findViewById(R.id.taxCodeTextField);
        ibanTextField = findViewById(R.id.ibanTextField);

        taxCode = sharedPreferences.getString("tax_code", "");
        iban = sharedPreferences.getString("iban", "");

        super.onCreate(savedInstanceState);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.editUserData){
            Intent i = new Intent(getApplicationContext(), SecondActivity.class);
            i.putExtra("name", super.name);
            i.putExtra("email", super.email);
            i.putExtra("phone", super.phone);
            i.putExtra("imageFileName", super.imageFileName);

            i.putExtra("tax_code", this.taxCode);
            i.putExtra("iban", this.iban);

            startActivityForResult(i, 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void reloadViews() {
        super.reloadViews();
        taxCodeTextField.setText(this.taxCode);
        ibanTextField.setText(this.iban);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(requestCode == 1){
            if(resultCode== Activity.RESULT_OK){
                taxCode = data.getStringExtra("tax_code");
                iban = data.getStringExtra("iban");
                editor.putString("vat_code", data.getStringExtra("vat_code"));
                editor.putString("tax_code", data.getStringExtra("tax_code"));
                editor.putString("iban", data.getStringExtra("iban"));
                editor.apply();

                this.reloadViews();
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tax_code", taxCode);
        outState.putString("iban", iban);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        taxCode = savedInstanceState.getString("tax_code");
        iban = savedInstanceState.getString("iban");

        reloadViews();
    }
}