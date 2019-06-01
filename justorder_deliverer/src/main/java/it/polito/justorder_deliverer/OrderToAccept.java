package it.polito.justorder_deliverer;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.justorder_framework.abstract_activities.AbstractViewerWithImagePickerActivityAndToolbar;
import it.polito.justorder_framework.abstract_activities.ActivityAbstractWithToolbar;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Order;
import it.polito.justorder_framework.model.OrderProduct;
import it.polito.justorder_framework.model.Product;
import it.polito.justorder_framework.model.User;
import kotlin.Unit;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class OrderToAccept extends ActivityAbstractWithToolbar {

    private TextView orderIdTextField, userTextField, addressTextField, priceTextField, timestampTextField, productsTextField;
    public Order order;
    private User deliverer;
    private User user;


    private String productString;
    private Map<Product, Double> products = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_to_accept);
        this.setupActivity();
    }


    @Override
    protected void setupActivity() {
        super.setupActivity();
        //orderIdTextField = findViewById(R.id.orderIdTextField);
        userTextField = findViewById(R.id.userTextField);
        addressTextField = findViewById(R.id.addressTextField);
        priceTextField = findViewById(R.id.priceTextField);
        timestampTextField = findViewById(R.id.timestampTextField);
        productsTextField = findViewById(R.id.productsTextField);
        this.reloadData();
    }


    @Override
    protected void reloadData() {
        super.reloadData();
        Intent i = getIntent();
        this.order = (Order) i.getSerializableExtra("order");
        if(this.order == null){
            this.order = new Order();
        }else{
            if(this.order.getUser() != null) {
                Database.INSTANCE.getUsers().get(this.order.getUser(), (user1 -> {
                    this.user = user1;
                    this.reloadViews();
                    return Unit.INSTANCE;
                }));
            }

            for(Map.Entry<String, OrderProduct> entry : this.order.getProducts().entrySet()){
                Database.INSTANCE.getRestaurants().get(entry.getValue().getRestaurantKey(), (restaurant -> {
                    this.products.put(restaurant.getProducts().get(entry.getValue().getProductKey()), entry.getValue().getQuantity());
                    this.reloadViews();
                    return Unit.INSTANCE;
                }));
            }
        }
        this.reloadViews();
    }

    @Override
    protected void reloadViews() {
        super.reloadViews();

        if(this.user != null) {
            userTextField.setText("User: " + this.user.getName());
        }
        addressTextField.setText("Address: " + order.getUserAddress());
        priceTextField.setText("Cost: " + new Double(order.getPrice()).toString());
        String date = DateFormat.format("dd/MM/yyyy - hh:mm", order.getTimestamp()).toString();
        timestampTextField.setText(date);

        if(products.size() > 0){
            productString = createProductString();
            productsTextField.setText(this.productString);
        }

        if(this.order != null){
            this.actionBar.setTitle(this.order.getKeyId());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("order", order);
        outState.putSerializable("user", user);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        order = (Order) savedInstanceState.getSerializable("order");
        user = (User) savedInstanceState.getSerializable("user");

        reloadViews();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(this.order.getState() == "accepted"){
            getMenuInflater().inflate(R.menu.delivered_order_menu, menu);
        }else{
            getMenuInflater().inflate(R.menu.select_order_option, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.acceptOrder){
            order.setState("accepted");
            Database.INSTANCE.getOrders().save(order);
            Double prova = getDistanceInfo();
            finish();
        }

        if(item.getItemId() == R.id.refuseOrder){
            order.setState("cancelled");
            order.setDeliverer(null);
            Database.INSTANCE.getOrders().save(order);

            Database.INSTANCE.getDeliverers().get(Database.INSTANCE.getCurrent_User().getDelivererKey(), deliverer1 ->{
                deliverer1.setCurrentOrder(null);
                Database.INSTANCE.getDeliverers().save(deliverer1);
                return Unit.INSTANCE;
            });
            finish();
        }

        if(item.getItemId() == R.id.deliveredOrder){
            order.setState("delivered");
            Database.INSTANCE.getOrders().save(order);

            Database.INSTANCE.getDeliverers().get(Database.INSTANCE.getCurrent_User().getDelivererKey(), deliverer1 ->{
                deliverer1.setCurrentOrder(null);
                Database.INSTANCE.getDeliverers().save(deliverer1);
                return Unit.INSTANCE;
            });
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private String createProductString() {

        String productString = "";
        for(Map.Entry<Product, Double> entry : products.entrySet()){
            productString += entry.getKey().getName() + "\n";
        }

        return productString;
    }

    private Double getDistanceInfo() {
        double dist=0;
        String restaurantCoordinates = getStringFromAddress(this, this.order.getRestaurantAddress());
        String customerCoordinates = getStringFromAddress(this, this.order.getUserAddress());


        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + restaurantCoordinates + "&destination=" + customerCoordinates + "&mode=driving&sensor=false&key=AIzaSyDWDWfu77UacZO2ZWBf8F8HsBer_uhvflY";
        new ApiDirectionsAsyncTask().execute(url);


        return dist;
    }

    public String getStringFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        String p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            //p1 = location.getLatitude() + ", "  + location.getLongitude();
            p1 = location.getLatitude() + ", " + location.getLongitude();

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;

    }

    /*private String post(String url) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }*/

    public class ApiDirectionsAsyncTask extends AsyncTask<String, Double, String> {

        double dist=0;
        //String restaurantCoordinates = getStringFromAddress(this, this.order.getRestaurantAddress());
        //String customerCoordinates = getStringFromAddress(this, this.order.getUserAddress());

        //String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + restaurantCoordinates + "&destination=" + customerCoordinates + "&mode=driving&sensor=false&key=AIzaSyAdJTTUd5BzEbk7Jvmmp26l9TH6s-SRdus";

        //String url = "https://maps.googleapis.com/maps/api/directions/json?origin=45.445657999999995, 9.2131335&destination=52.176609, 5.266883&mode=driving&sensor=false&key=AIzaSyAdJTTUd5BzEbk7Jvmmp26l9TH6s-SRdus";

        @Override
        protected String doInBackground(String... url) {


            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url[0])
                    .build();

            Response response;
            try {
                response = client.newCall(request).execute();
                }
            catch (MalformedURLException e) {
                Log.e("async", "Error processing Distance Matrix API URL");
                return null;

            } catch (IOException e) {
                System.out.println("Error connecting to Distance Matrix");
                return null;
            }

            try {
                String check = response.body().string();
                return check;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                System.out.println(result);

                JSONObject jsonObject = new JSONObject(result);

                JSONArray routes = jsonObject.getJSONArray("routes");

                JSONObject routes1 = routes.getJSONObject(0);

                JSONArray legs = routes1.getJSONArray("legs");

                JSONObject legs1 = legs.getJSONObject(0);

                JSONObject distance = legs1.getJSONObject("distance");

                String distanceText = distance.getString("text");
                String distanceVal = distanceText.split(" ")[0];
                String distancePoint = distanceVal.replace(",", ".");

                Double distanceDouble = Double.parseDouble(distancePoint);

                if (distanceDouble != null) {

                    Database.INSTANCE.getDeliverers().get(Database.INSTANCE.getCurrent_User().getDelivererKey(), deliverer1 -> {
                        Double oldDist = deliverer1.getKm();
                        oldDist += distanceDouble;
                        deliverer1.setKm(oldDist);
                        Database.INSTANCE.getDeliverers().save(deliverer1);
                        return Unit.INSTANCE;
                    });
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}

