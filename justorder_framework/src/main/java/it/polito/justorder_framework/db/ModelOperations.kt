package it.polito.justorder_framework.db

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import it.polito.justorder_framework.Utils
import it.polito.justorder_framework.model.Model

class ModelOperations<T: Model>(val path: String, private val tClass: Class<T>){
    fun get(key: String, cb : (T) -> Unit) {

        Database.db.child(path).child(key).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()) {
                    cb(Utils.convertObject(p0, tClass))
                }else{
                    val entity = tClass.newInstance()
                    entity.keyId = key
                    cb(entity)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    fun save(entity: T){
        var ref: DatabaseReference
        val key = entity.keyId
        if(key != null){
            ref = Database.db.child(path).child(key);
        }else{
            ref = Database.db.child(path).push()
            entity.keyId = ref.key
        }
        ref.setValue(entity)
    }

    fun getAll(cb : (List<T>) -> Unit) {

        Database.db.child(path).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                var items: MutableList<T> = mutableListOf()
                for(item in p0.children){
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