package it.polito.justorder_deliverer;

import android.content.Intent;

import androidx.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import it.polito.justorder_framework.FirebaseFunctions;
import it.polito.justorder_framework.UserChangeStatusEvent;
import it.polito.justorder_framework.common_activities.AppLoaderActivity;
import it.polito.justorder_framework.db.Database;
import kotlin.Unit;

public class DelivererAppLoaderActivity extends AppLoaderActivity {

    @Override
    protected void onStart() {
        super.onStart();
        if(!FirebaseFunctions.isLoggedIn()){
            FirebaseFunctions.login(this);
        }else{
            this.loadData();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FirebaseFunctions.AUTH_ACTIVITY_RESULT){
            EventBus.getDefault().post(new UserChangeStatusEvent());
            this.loadData();
        }
    }

    @Override
    protected void loadData() {
        super.loadData();
        Database.INSTANCE.loadCurrentUser(() -> {
            // YOU MUST DISPLAY ORDER PAGE
            if(Database.INSTANCE.getCurrent_User().getDelivererKey() != null){
                String deliverer_key = Database.INSTANCE.getCurrent_User().getDelivererKey();

                Database.INSTANCE.getDeliverers().get(deliverer_key, (deliverer -> {
                    Map<String, Serializable> map = new HashMap<>();
                    map.put("deliverer", deliverer);
                    this.startApp(OrdersDelivererListActivity.class, map);
                    return  Unit.INSTANCE;
                }));
            }else{
                this.startApp(DelivererSettingsViewerActivity.class, null);
            }
            return Unit.INSTANCE;
        });
    }
}
