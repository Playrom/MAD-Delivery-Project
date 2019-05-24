package it.polito.justorder_framework.model

class Product: ModelWithVote() {
    var name: String = ""
    var imageUri: String? = null
    var cost: Double = 0.0
    var notes: String = ""
    var ingredients: List<String> = mutableListOf()
    var reviews: Map<String, Boolean> = mutableMapOf<String, Boolean>()
    var category: String = ""
}