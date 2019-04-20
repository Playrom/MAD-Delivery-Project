package it.polito.justorder_framework.model

import java.io.Serializable

class Deliverer : Serializable {
    var key: String? = null
    var iban: String = ""
    var fiscalCode: String = ""
    var userKey : String? = null
}
