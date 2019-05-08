package it.polito.justorder_restaurant.fragments;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.geofire.GeoLocation;

import it.polito.justorder_framework.Utils;
import it.polito.justorder_framework.model.DelivererDistance;
import it.polito.justorder_restaurant.R;
import it.polito.justorder_restaurant.fragments.DeliverListFragment.OnListFragmentInteractionListener;
import it.polito.justorder_restaurant.fragments.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class DeliverListItemRecyclerViewAdapter extends RecyclerView.Adapter<DeliverListItemRecyclerViewAdapter.ViewHolder> {

    private final List<DelivererDistance> mValues;
    private GeoLocation restaurantLocation;
    private final OnListFragmentInteractionListener mListener;

    public DeliverListItemRecyclerViewAdapter(List<DelivererDistance> items, GeoLocation restaurantLocation, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        this.restaurantLocation = restaurantLocation;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_deliver_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getUser().getName());
        GeoLocation location = mValues.get(position).getLocation();
        if(location != null && restaurantLocation != null){
            double distance = Utils.distance(restaurantLocation.latitude, restaurantLocation.longitude, location.latitude, location.longitude);
            holder.mContentView.setText(new Double(distance).toString());
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public DelivererDistance mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public GeoLocation getRestaurantLocation() {
        return restaurantLocation;
    }

    public void setRestaurantLocation(GeoLocation restaurantLocation) {
        this.restaurantLocation = restaurantLocation;
    }
}
