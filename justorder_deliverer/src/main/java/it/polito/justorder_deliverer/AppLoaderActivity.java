package it.polito.justorder_deliverer;

import android.content.Intent;

import androidx.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import it.polito.justorder_framework.FirebaseFunctions;
import it.polito.justorder_framework.UserChangeStatusEvent;
import it.polito.justorder_framework.common_activities.AbstractAppLoaderActivity;
import it.polito.justorder_framework.db.Database;
import kotlin.Unit;

public class AppLoaderActivity extends AbstractAppLoaderActivity {

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
                    Database.INSTANCE.getGeodata().trackClientPosition(Database.INSTANCE.getCurrent_User().getDelivererKey(), this, () -> {
                        return Unit.INSTANCE;
                    });
                    this.startApp(OrdersDelivererListActivity.class, map);
                    return  Unit.INSTANCE;
                }));
            }else{
                this.startApp(DelivererSettingsViewerActivity.class, new HashMap<String,Serializable>() {{ put("init_geo", true); }});
            }
            return Unit.INSTANCE;
        });
    }
}
