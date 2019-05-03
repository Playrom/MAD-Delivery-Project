package it.polito.justorder_framework.model

import java.io.Serializable

class Deliverer : Model(), Serializable {
    var iban: String = ""
    var fiscalCode: String = ""
    var userKey : String? = null
    var orders: Map<String, Boolean> = mutableMapOf<String, Boolean>()
}
