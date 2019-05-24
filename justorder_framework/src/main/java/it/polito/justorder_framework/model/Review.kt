package it.polito.justorder_framework.model

import java.util.*

class Review: Model(){
    var reviewerKey: String = ""
    var restaurantKey: String? = null
    var delivererKey: String? = null
    var productKey: String? = null
    var stars: Int = 1
    var comment: String? = null
}
