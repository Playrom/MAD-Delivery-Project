package it.polito.justorder_framework.model

import com.google.firebase.database.DataSnapshot

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

class Product {
    var key: String? = null
    var name: String = ""
    var cost: Double = 0.0
    var notes: String = ""
    var ingredients: List<String> = mutableListOf()
    var category: String = ""
}
