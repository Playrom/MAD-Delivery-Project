package it.polito.justorder_framework.db

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import it.polito.justorder_framework.Utils
import it.polito.justorder_framework.model.Product
import it.polito.justorder_framework.model.User

object Users{
    fun getUser(key: String, cb : (User) -> Unit){
        Database.db.child("users").child(key).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()) {
                    cb(Utils.convertObject(p0, User::class.java))
                }else{
                    val user = User()
                    user.key = key
                    cb(user)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    fun saveUser(user: User){
        var ref: DatabaseReference
        val key = user.key
        if(key != null){
            ref = Database.db.child("users").child(key);
        }else{
            ref = Database.db.child("users").push()
        }
        ref.setValue(user)
    }
}