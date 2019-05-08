package it.polito.justorder_framework.abstract_activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class ActivityAbstract extends AppCompatActivity {
    protected void reloadViews() {}
    protected void reloadData() {}
    protected ActivityCompat.OnRequestPermissionsResultCallback permissionsResultCallback;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(this.permissionsResultCallback != null){
            this.permissionsResultCallback.onRequestPermissionsResult(requestCode,permissions, grantResults);
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public ActivityCompat.OnRequestPermissionsResultCallback getPermissionsResultCallback() {
        return permissionsResultCallback;
    }

    public void setPermissionsResultCallback(ActivityCompat.OnRequestPermissionsResultCallback permissionsResultCallback) {
        this.permissionsResultCallback = permissionsResultCallback;
    }
}
