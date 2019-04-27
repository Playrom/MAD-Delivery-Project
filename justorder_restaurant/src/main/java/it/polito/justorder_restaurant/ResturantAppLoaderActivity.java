package it.polito.justorder_restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import it.polito.justorder_framework.FirebaseFunctions;
import it.polito.justorder_framework.UserChangeStatusEvent;
import it.polito.justorder_framework.common_activities.AppLoaderActivity;
import it.polito.justorder_framework.common_activities.ProductsListActivity;
import it.polito.justorder_framework.db.Database;
import kotlin.Unit;

public class ResturantAppLoaderActivity extends AppLoaderActivity {

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        if(!FirebaseFunctions.isLoggedIn()){
            FirebaseFunctions.login(this);
        }else{
            this.loadData();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FirebaseFunctions.AUTH_ACTIVITY_RESULT){
            EventBus.getDefault().post(new UserChangeStatusEvent());
        }
    }

    @Subscribe
    public void onMessageEvent(UserChangeStatusEvent event) {
        this.loadData();
    };


    @Override
    protected void loadData() {
        super.loadData();
        Database.INSTANCE.loadCurrentUser(() -> {
            String restaurant_key = "";
            restaurant_key = Database.INSTANCE.getCurrent_User().getManagedRestaurants().entrySet()
                    .stream().filter(x -> {return x.getValue();}).collect(Collectors.toList()).get(0).getKey();

            Map<String, Serializable> map = new HashMap<>();

            Database.INSTANCE.getRestaurants().get(restaurant_key, (restaurant -> {
                map.put("restaurant", restaurant);
                this.startApp(ProductListActivityWithAdd.class, map);
                return Unit.INSTANCE;
            }));
            return Unit.INSTANCE;
        });
    }
}
