package it.polito.justorder_framework.db

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import it.polito.justorder_framework.Utils
import it.polito.justorder_framework.model.ModelWithVote
import it.polito.justorder_framework.model.Review

class ModelWithVoteOperations<T: ModelWithVote>(tClass: Class<T>, path: String): ModelOperations<T>(tClass, path) {
    fun orderByVote(cb : (List<T>) -> Unit){
        var ref = Database.db.child(path)
        var query = ref.orderByChild("averageVote")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                var items: MutableList<T> = mutableListOf()
                for (item in p0.children) {
                    items.add(Utils.convertObject(item, tClass))
                }
                cb(items)
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    fun orderByVote(key: String, value: String, cb : (List<T>) -> Unit){
        var ref = Database.db.child(path)
        var query = ref.orderByChild(key).equalTo(value).orderByChild("averageVote")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                var items: MutableList<T> = mutableListOf()
                for (item in p0.children) {
                    items.add(Utils.convertObject(item, tClass))
                }
                cb(items)
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }
}