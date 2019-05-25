package it.polito.justorder_framework.model

import java.io.Serializable

class Deliverer : ModelWithVote(), Serializable {
    var iban: String = ""
    var fiscalCode: String = ""
    var userKey : String? = null
    var orders: Map<String, Boolean> = mutableMapOf<String, Boolean>()
    var reviews: Map<String, Boolean> = mutableMapOf<String, Boolean>()
    var currentOrder: String? = null
    var km: Double? = 0.0
}
