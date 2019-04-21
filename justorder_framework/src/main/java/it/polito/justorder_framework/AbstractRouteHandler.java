package it.polito.justorder_framework;

import android.app.Activity;
import android.content.Context;
import android.view.MenuItem;

import it.polito.justorder_framework.common_activities.ProductActivity;

public class AbstractRouteHandler {
    public boolean routeHandler(MenuItem item, Context context){
        return false;
    }
    public Class getProductActivityClass(){ return ProductActivity.class; }
}
