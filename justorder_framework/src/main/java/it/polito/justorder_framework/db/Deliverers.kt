package it.polito.justorder_framework.db

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import it.polito.justorder_framework.Utils
import it.polito.justorder_framework.model.Deliverer
import it.polito.justorder_framework.model.Product
import it.polito.justorder_framework.model.Restaurant
import it.polito.justorder_framework.model.User

object Deliverers{
    fun getDeliverer(key: String, cb : (Deliverer) -> Unit){
        Database.db.child("deliverers").child(key).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()) {
                    cb(Utils.convertObject(p0, Deliverer::class.java))
                }else{
                    val deliverer = Deliverer()
                    deliverer.key = key
                    cb(deliverer)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    fun saveDeliverer(deliverer: Deliverer){
        var ref: DatabaseReference
        val key = deliverer.key
        if(key != null){
            ref = Database.db.child("deliverers").child(key);
        }else{
            ref = Database.db.child("deliverers").push()
            deliverer.key = ref.key
        }
        ref.setValue(deliverer)
    }
}