package it.polito.justorder_restaurant.fragments;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.geofire.GeoLocation;

import it.polito.justorder_framework.Utils;
import it.polito.justorder_framework.model.DelivererDistance;
import it.polito.justorder_restaurant.ConfirmOrderInterface;
import it.polito.justorder_restaurant.R;
import it.polito.justorder_restaurant.RestaurantDelivererAssignActivity;

import java.util.Comparator;
import java.util.List;

public class DeliverListItemRecyclerViewAdapter extends RecyclerView.Adapter<DeliverListItemRecyclerViewAdapter.ViewHolder> {

    private final List<DelivererDistance> mValues;
    private GeoLocation restaurantLocation;
    private final ConfirmOrderInterface mListener;

    public DeliverListItemRecyclerViewAdapter(List<DelivererDistance> items, GeoLocation restaurantLocation, ConfirmOrderInterface listener) {
        mValues = items;
        mValues.sort(new Comparator<DelivererDistance>() {
            @Override
            public int compare(DelivererDistance o1, DelivererDistance o2) {
                return o1.distanceFromRestaurant(restaurantLocation).compareTo(o2.distanceFromRestaurant(restaurantLocation));
            }
        });
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
                    mListener.confirmDeliverer(holder.mItem.getDeliverer().getKeyId());
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

}
