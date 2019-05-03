package it.polito.justorder_framework.model

import java.io.Serializable

class Restaurant : Model(),Serializable {
    var name: String = ""
    var email: String = ""
    var imageUri: String? = null
    var telephone: String = ""
    var address: String = ""
    var fiscalCode: String = ""
    var vatCode: String = ""
    var iban: String = ""
    var openingHour: String = ""
    var closingHour: String = ""
    var openDays: Map<String, Boolean> = mutableMapOf()
    var type: String = ""

    var products: Map<String, Product> = mutableMapOf()
    var orders: Map<String, Boolean> = mutableMapOf<String, Boolean>()

    var owner: String = ""

    var messagingToken : String? = null
}
