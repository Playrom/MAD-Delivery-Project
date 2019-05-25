package it.polito.justorder_framework.model

import java.io.Serializable

open class ModelWithVote : Model() {
    var totalVotes: Int = 0
        set(value) {
            totalVotes = value
            averageVote = value.toDouble()/numberOfVotes.toDouble()
        }
        get
    var numberOfVotes: Int = 0
        set(value) {
            numberOfVotes = value
            averageVote = totalVotes.toDouble()/value.toDouble()
        }
        get
    var averageVote: Double = 0.0
}