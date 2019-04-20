package it.polito.justorder_deliverer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.Nullable;

import it.polito.justorder_framework.abstract_activities.AbstractViewerWithImagePickerActivity;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.db.Deliverers;
import it.polito.justorder_framework.db.Users;
import it.polito.justorder_framework.model.Deliverer;
import it.polito.justorder_framework.model.User;
import kotlin.Unit;

public class DelivererSettingsViewerActivity extends AbstractViewerWithImagePickerActivity {

    protected Deliverer deliverer;
    protected EditText nameTextField, emailTextField, phoneTextField, addressTextField, taxCodeTextField, ibanTextField;
    protected Intent i;
    protected String deliverer_intent_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        i = this.getIntent();
        setContentView(R.layout.deliverer_settings_viewer);
        setupActivity();
    }

    @Override
    protected void setupActivity() {
        super.setupActivity();
        taxCodeTextField = findViewById(R.id.taxCodeTextField);
        ibanTextField = findViewById(R.id.ibanTextField);
        reloadData();
    }

    @Override
    protected void reloadData() {
        super.reloadData();

        if(i != null && i.getStringExtra("deliverer_intent_key") != null){
            deliverer_intent_key = i.getStringExtra("deliverer_intent_key");
            Deliverers.INSTANCE.getDeliverer(deliverer_intent_key, (deliverer1 -> {
                this.deliverer = deliverer1;
                this.reloadViews();
                return Unit.INSTANCE;
            }));
        }else {
            this.deliverer = new Deliverer();
            Intent i = new Intent(this, DelivererSettingsEditorActivity.class);
            i.putExtra("deliverer", deliverer);
            startActivityForResult(i, 2);
        }
    }

    @Override
    protected void reloadViews() {
        super.reloadViews();
        if(this.deliverer != null){
            taxCodeTextField.setText(deliverer.getFiscalCode());
            ibanTextField.setText(deliverer.getIban());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.editUserData){
            Intent i = new Intent(getApplicationContext(), DelivererSettingsEditorActivity.class);
            i.putExtra("deliverer", deliverer);

            startActivityForResult(i, 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 || requestCode == 2){
            if(resultCode== Activity.RESULT_OK){
                deliverer = (Deliverer) data.getSerializableExtra("deliverer");
                Deliverers.INSTANCE.saveDeliverer(deliverer);
                if(requestCode == 2 && Database.INSTANCE.getCurrent_User() != null){
                    User user = Database.INSTANCE.getCurrent_User();
                    deliverer.setUserKey(user.getKey());
                    user.setDelivererKey(deliverer.getKey());
                    Users.INSTANCE.saveUser(user);
                    Deliverers.INSTANCE.saveDeliverer(deliverer);
                }
                reloadViews();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("deliverer", deliverer);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        deliverer = (Deliverer) savedInstanceState.getSerializable("deliverer");
        reloadViews();
    }
}