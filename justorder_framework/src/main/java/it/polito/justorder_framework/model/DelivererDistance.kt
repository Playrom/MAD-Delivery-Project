package it.polito.justorder_framework.model

import com.firebase.geofire.GeoLocation
import java.io.Serializable

class DelivererDistance : Model(), Serializable {
    var delivererKey: String = ""
    var user : User? = null
    var location : GeoLocation = GeoLocation(0.0,0.0)
}
