package it.polito.justorder_framework.model

import android.net.Uri
import java.io.Serializable

class User : Model(), Serializable{
    var name: String = ""
    var email: String = ""
    var address: String = ""
    var imageUri: String? = null
    var telephone: String = ""

    var delivererKey: String? = null
    var restaurantKey: String? = null

    var orders: Map<String, Boolean> = mutableMapOf<String, Boolean>()
    var favouriteRestaurants: Map<String, Boolean> = mutableMapOf<String, Boolean>()
    var favouriteProducts: Map<String, Boolean> = mutableMapOf<String, Boolean>()

    var products: Map<String, Int> = mutableMapOf()


}
