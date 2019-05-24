package it.polito.justorder_framework.model

import java.io.Serializable

open class ModelWithVote : Model() {
    var totalVotes: Int = 0
    var numberOfVotes: Int = 0
    val averageVote: Double
        get() {
            return totalVotes.toDouble()/numberOfVotes.toDouble()
        }
}