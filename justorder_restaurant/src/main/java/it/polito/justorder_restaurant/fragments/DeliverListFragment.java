package it.polito.justorder_restaurant.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.geofire.GeoLocation;

import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.DelivererDistance;
import it.polito.justorder_restaurant.ConfirmOrderInterface;
import it.polito.justorder_restaurant.R;
import it.polito.justorder_restaurant.RestaurantDelivererAssignActivity;
import kotlin.Unit;

import java.util.ArrayList;
import java.util.List;

public class DeliverListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private ConfirmOrderInterface mListener;
    protected List<DelivererDistance> delivererDistances = new ArrayList<>();
    protected String restaurant_key;
    protected GeoLocation restaurantLocation;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DeliverListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static DeliverListFragment newInstance(int columnCount, String restaurantKey, List<DelivererDistance> distanceList, GeoLocation restaurantLocation) {
        DeliverListFragment fragment = new DeliverListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.restaurant_key = restaurantKey;
        fragment.delivererDistances = distanceList;
        fragment.restaurantLocation = restaurantLocation;
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().getString("restaurant_key") != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            restaurant_key = getArguments().getString("restaurant_key");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deliver_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new DeliverListItemRecyclerViewAdapter(this.delivererDistances, restaurantLocation, mListener));
        }
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
