package it.polito.justorder_framework.model

import com.firebase.geofire.GeoLocation
import it.polito.justorder_framework.Utils
import java.io.Serializable

class DelivererDistance : Model(), Serializable {
    var deliverer : Deliverer? = null
    var user : User? = null
    var location : GeoLocation? = null

    fun distanceFromRestaurant(restaurantLocation : GeoLocation) : Double? {
        val delivererLocation = this.location;
        if(delivererLocation != null){
            return Utils.distance(restaurantLocation.latitude, restaurantLocation.longitude, delivererLocation.latitude, delivererLocation.longitude)
        }
        return null;
    }
}
