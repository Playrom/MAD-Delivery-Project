package it.polito.justorder_deliverer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.polito.justorder_framework.abstract_activities.ActivityAbstractWithSideNav;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Deliverer;
import it.polito.justorder_framework.model.Order;
import kotlin.Unit;

public class MapsActivity extends ActivityAbstractWithSideNav implements OnMapReadyCallback {

    private GoogleMap mMap;
    protected Deliverer deliverer;
    private Order order;
    private String restaurantAddress;
    private String customerAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent i = getIntent();
        this.deliverer = (Deliverer) i.getSerializableExtra("deliverer");
        this.setupActivity();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        UiSettings settings = googleMap.getUiSettings();

        settings.setAllGesturesEnabled(true);
        settings.setMyLocationButtonEnabled(true);

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng ristorante = new LatLng(45.076227, 7.643435);

        if (this.deliverer.getCurrentOrder()!=null) {
            Database.INSTANCE.getOrders().get(this.deliverer.getCurrentOrder(), true, (order1 -> {
                this.order = order1;
                this.restaurantAddress = order.getRestaurantAddress();
                this.customerAddress = order.getUserAddress();

                LatLng restaurantCoordinates = getLocationFromAddress(this, this.restaurantAddress);
                mMap.addMarker(new MarkerOptions().position(restaurantCoordinates).title("Restaurant"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(restaurantCoordinates));

                //LatLng madrid = new LatLng(45.062564,7.662338);
                LatLng customerCoordinates = getLocationFromAddress(this, this.customerAddress);
                mMap.addMarker(new MarkerOptions().position(customerCoordinates).title("Customer"));

                String restaurantPlace = getStringFromAddress( this, this.restaurantAddress);
                String customerPlace = getStringFromAddress(this, this.customerAddress);


                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + restaurantPlace + "&daddr=" + customerPlace));
                startActivity(intent);


                //Define list to get all latlng for the route
                List<LatLng> path = new ArrayList();


               //Execute Directions API request
                GeoApiContext context = new GeoApiContext.Builder()
                        .apiKey("AIzaSyDWDWfu77UacZO2ZWBf8F8HsBer_uhvflY")
                        .build();
                DirectionsApiRequest req = DirectionsApi.getDirections(context, restaurantPlace, customerPlace);
                try {
                    DirectionsResult res = req.await();

                    //Loop through legs and steps to get encoded polylines of each step
                    if (res.routes != null && res.routes.length > 0) {
                        DirectionsRoute route = res.routes[0];

                        if (route.legs !=null) {
                            for(int i=0; i<route.legs.length; i++) {
                                DirectionsLeg leg = route.legs[i];
                                if (leg.steps != null) {
                                    for (int j=0; j<leg.steps.length;j++){
                                        DirectionsStep step = leg.steps[j];
                                        if (step.steps != null && step.steps.length >0) {
                                            for (int k=0; k<step.steps.length;k++){
                                                DirectionsStep step1 = step.steps[k];
                                                EncodedPolyline points1 = step1.polyline;
                                                if (points1 != null) {
                                                    //Decode polyline and add points to list of route coordinates
                                                    List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                                    for (com.google.maps.model.LatLng coord1 : coords1) {
                                                        path.add(new LatLng(coord1.lat, coord1.lng));
                                                    }
                                                }
                                            }
                                        } else {
                                            EncodedPolyline points = step.polyline;
                                            if (points != null) {
                                                //Decode polyline and add points to list of route coordinates
                                                List<com.google.maps.model.LatLng> coords = points.decodePath();
                                                for (com.google.maps.model.LatLng coord : coords) {
                                                    path.add(new LatLng(coord.lat, coord.lng));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch(Exception ex) {
                    Log.e("erroremappe", ex.getLocalizedMessage());
                }

                //Draw the polyline
                if (path.size() > 0) {
                    PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
                    mMap.addPolyline(opts);
                }

                mMap.getUiSettings().setZoomControlsEnabled(true);

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurantCoordinates, 14));

                return Unit.INSTANCE;
            }));
        }


    }


    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            //p1 = location.getLatitude() + ", "  + location.getLongitude();
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;

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

}
