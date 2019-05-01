package it.polito.justorder_restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import it.polito.justorder_framework.FirebaseFunctions;
import it.polito.justorder_framework.UserChangeStatusEvent;
import it.polito.justorder_framework.common_activities.AppLoaderActivity;
import it.polito.justorder_framework.common_activities.ProductsListActivity;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Restaurant;
import kotlin.Unit;

public class ResturantAppLoaderActivity extends AppLoaderActivity {

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
        FirebaseInstanceId.getInstance().getInstanceId()
        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.w("Firebase", "getInstanceId failed", task.getException());
                    return;
                }

                // Get new Instance ID token
                String token = task.getResult().getToken();
                MessagingService.sendRegistrationToServer(token);
            }
        });
        Database.INSTANCE.loadCurrentUser(() -> {
            String restaurant_key = "";
            List<Map.Entry<String, Boolean>> resturants = Database.INSTANCE.getCurrent_User().getManagedRestaurants().entrySet()
                    .stream().filter(x -> {
                        return x.getValue();
                    }).collect(Collectors.toList());
            if(resturants.size() > 0){
                restaurant_key = resturants.get(0).getKey();

                Map<String, Serializable> map = new HashMap<>();

                Database.INSTANCE.getRestaurants().get(restaurant_key, (restaurant -> {
                    map.put("restaurant", restaurant);
                    this.startApp(ProductListActivityWithAdd.class, map);
                    return Unit.INSTANCE;
                }));
            }else{
                Map<String, Serializable> map = new HashMap<>();
                this.startApp(RestaurantSettingsViewerActivity.class, map);
            }

            return Unit.INSTANCE;
        });
    }
}
