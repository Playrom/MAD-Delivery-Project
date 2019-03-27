package it.polito.justorder;

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
    protected String address;
    protected EditText addressTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        addressTextField = findViewById(R.id.addressTextField);
        address = sharedPreferences.getString("address", "");

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
            i.putExtra("address", this.address);

            startActivityForResult(i, 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void reloadViews() {
        super.reloadViews();
        addressTextField.setText(this.address);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(requestCode == 1){
            if(resultCode== Activity.RESULT_OK){
                address = data.getStringExtra("address");
                editor.putString("address", data.getStringExtra("address"));
                editor.apply();

                this.reloadViews();
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("address", address);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        address = savedInstanceState.getString("address");

        reloadViews();
    }
}