package it.polito.justorder_framework.db

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import it.polito.justorder_framework.Utils
import it.polito.justorder_framework.model.Product
import it.polito.justorder_framework.model.Restaurant
import it.polito.justorder_framework.model.User

object Restaurants{
    fun getRestaurant(key: String, cb : (Restaurant) -> Unit){
        Database.db.child("restaurants").child(key).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()) {
                    cb(Utils.convertObject(p0, Restaurant::class.java))
                }else{
                    val restaurant = Restaurant()
                    restaurant.key = key
                    cb(restaurant)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    fun saveRestaurant(restaurant: Restaurant){
        var ref: DatabaseReference
        val key = restaurant.key
        if(key != null){
            ref = Database.db.child("restaurants").child(key);
        }else{
            ref = Database.db.child("restaurants").push()
            restaurant.key = ref.key
        }
        ref.setValue(restaurant)
    }
}