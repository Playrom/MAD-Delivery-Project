package it.polito.justorder_framework.model

import java.io.Serializable

class Restaurant : Serializable {
    var key: String? = null
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

    var owner: String = ""
    var managers: Map<String, Boolean> = mutableMapOf<String, Boolean>()
}
