package it.polito.justorder_framework.common_activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.Map;
import java.util.function.Consumer;

import it.polito.justorder_framework.R;
import it.polito.justorder_framework.UserChangeStatusEvent;
import it.polito.justorder_framework.abstract_activities.ActivityAbstract;
import it.polito.justorder_framework.abstract_activities.ActivityAbstractWithSideNav;
import it.polito.justorder_framework.db.Database;
import kotlin.Unit;

public class AppLoaderActivity extends ActivityAbstract {

    protected void loadData(){

    }

    protected void startApp(Class activityToLoad, Map<String, Serializable> extras){
        Intent i = new Intent(this, activityToLoad);
        if(extras != null) {
            for (Map.Entry<String, Serializable> entry : extras.entrySet()) {
                i.putExtra(entry.getKey(), entry.getValue());
            }
        }
        this.startActivity(i);
        overridePendingTransition(0, 0);
        this.finish();
    }
}
