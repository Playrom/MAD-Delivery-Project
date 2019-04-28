package it.polito.justorder_framework.model

import java.util.*
import kotlin.collections.HashMap

class Order: Model(){
    var user: String = ""
    var userName: String = ""
    var userAddress: String = ""
    var price: Double = 0.0
    var timestamp: Date = Date()
    var restaurant: String = ""
    var restaurantName: String = ""
    var restaurantAddress: String = ""
    var deliverer: String? = null
    var products : Map<String, Int> = mutableMapOf<String, Int>()
}
