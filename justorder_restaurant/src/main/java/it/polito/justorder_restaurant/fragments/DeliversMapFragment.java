package it.polito.justorder_restaurant.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import it.polito.justorder_framework.model.DelivererDistance;
import it.polito.justorder_restaurant.ConfirmOrderInterface;
import it.polito.justorder_restaurant.R;

public class DeliversMapFragment extends SupportMapFragment implements OnMapReadyCallback {

    private List<DelivererDistance> deliverers = new ArrayList<>();
    protected String restaurantKey;
    protected GeoLocation restaurantLocation;
    private ConfirmOrderInterface mListener;

    public DeliversMapFragment() {
        super();
    }

    public static DeliversMapFragment newInstance(String restaurantKey, List<DelivererDistance> distanceList, GeoLocation restaurantLocation){
        DeliversMapFragment frag = new DeliversMapFragment();
        frag.restaurantKey = restaurantKey;
        frag.deliverers = distanceList;
        frag.restaurantLocation = restaurantLocation;
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        getMapAsync(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ConfirmOrderInterface) {
            mListener = (ConfirmOrderInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        UiSettings settings = googleMap.getUiSettings();

        settings.setAllGesturesEnabled(true);
        settings.setMyLocationButtonEnabled(true);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        MarkerOptions restaurantMarker = new MarkerOptions()
                .position(new LatLng(restaurantLocation.latitude, restaurantLocation.longitude))
                .icon(this.getBitmapFromVector(getContext(), R.drawable.restaurant, getResources().getColor(R.color.colorPrimary)));
        googleMap.addMarker(restaurantMarker);
        builder.include(restaurantMarker.getPosition());

        for (DelivererDistance d : deliverers){
            MarkerOptions marker = new MarkerOptions()
                                    .position(new LatLng(d.getLocation().latitude, d.getLocation().longitude))
                                    .icon(this.getBitmapFromVector(getContext(), R.drawable.marker, getResources().getColor(R.color.colorPrimary)));
            marker.title(d.getDeliverer().getKeyId());
            googleMap.addMarker(marker);
            builder.include(marker.getPosition());
        }

        LatLngBounds bounds = builder.build();
        int padding = 70; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.moveCamera(cu);

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String key = marker.getTitle();
                mListener.confirmDeliverer(key);
                return true;
            }
        });
    }

    private BitmapDescriptor getBitmapFromVector(@NonNull Context context,
                                                       @DrawableRes int vectorResourceId,
                                                       @ColorInt int tintColor) {

        Drawable vectorDrawable = ResourcesCompat.getDrawable(context.getResources(), vectorResourceId, null);

        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        DrawableCompat.setTint(vectorDrawable, tintColor);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}