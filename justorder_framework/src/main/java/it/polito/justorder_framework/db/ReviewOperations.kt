package it.polito.justorder_framework.db

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import it.polito.justorder_framework.Utils
import it.polito.justorder_framework.model.Model
import it.polito.justorder_framework.model.ModelWithVote
import it.polito.justorder_framework.model.Product
import it.polito.justorder_framework.model.Review

class ReviewOperations<T: Review>(tClass: Class<T>, path: String): ModelOperations<T>(tClass, path){
    override fun save(entity: T){
        var ref: DatabaseReference
        val key = entity.keyId
        if(key != null){
            ref = Database.db.child(path).child(key)
        }else{
            ref = Database.db.child(path).push()
            entity.keyId = ref.key
        }
        ref.setValue(entity)
        var itemKey = ""

        var type : ModelOperations<ModelWithVote>? = null
        if(entity.productKey != null) {
            type = Database.products as? ModelOperations<ModelWithVote>
            itemKey = entity.productKey!!
        }else if(entity.delivererKey != null) {
            type = Database.deliverers as? ModelOperations<ModelWithVote>
            itemKey = entity.delivererKey!!
        }else if(entity.restaurantKey != null) {
            type = Database.restaurants as? ModelOperations<ModelWithVote>
            itemKey = entity.restaurantKey!!
        }

        if(type != null){
            type.get(itemKey){
                it.numberOfVotes = it.numberOfVotes + 1
                it.totalVotes = it.totalVotes + entity.stars
                it.averageVote = it.totalVotes.toDouble() / it.numberOfVotes.toDouble()
                type.save(it)
            }
        }
    }

}