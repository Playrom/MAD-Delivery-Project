package it.polito.justorder_framework.model

import java.io.Serializable

open class ModelWithVote : Model() {
    var totalVotes: Int = 0
    var numberOfVotes: Int = 0
    var averageVote: Double = 0.0
    var reviews: MutableMap<String, Boolean> = mutableMapOf<String, Boolean>()
}