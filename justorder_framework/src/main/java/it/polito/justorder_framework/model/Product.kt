package it.polito.justorder_framework.model

class Product {
    var key: String? = null
    var name: String = ""
    var cost: Double = 0.0
    var notes: String = ""
    var ingredients: List<String> = mutableListOf()
    var category: String = ""
}
