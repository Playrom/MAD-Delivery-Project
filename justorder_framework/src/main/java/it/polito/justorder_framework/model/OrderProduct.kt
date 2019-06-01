package it.polito.justorder_framework.model


class OrderProduct: Model() {
    var productKey: String? = null
    var restaurantKey: String? = null
    var quantity: Double = 0.0
}