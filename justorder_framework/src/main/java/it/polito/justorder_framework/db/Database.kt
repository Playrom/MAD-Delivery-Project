package it.polito.justorder_framework.db

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import it.polito.justorder_framework.UserChangeStatusEvent
import it.polito.justorder_framework.Utils
import it.polito.justorder_framework.model.*
import org.greenrobot.eventbus.EventBus


object Database {
     init {
          val key = FirebaseAuth.getInstance().currentUser?.uid
          if(key != null) {
               FirebaseDatabase.getInstance().getReference().child("users").child(key).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                         if(p0.exists()) {
                              Database.Current_User = Utils.convertObject(p0, User::class.java)
                              EventBus.getDefault().post(UserChangeStatusEvent())
                         }
                    }

                    override fun onCancelled(p0: DatabaseError) {
                         TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
               })
          }
     }
     object Storage {
          var products = FirebaseStorage.getInstance().getReference("products")
     }
     var db : DatabaseReference = FirebaseDatabase.getInstance().getReference()
     var storage = Storage

     val users = ModelOperations<User>(User::class.java, "users")
     val products = ModelOperations<Product>(Product::class.java, "products")
     val restaurants = ModelOperations<Restaurant>(Restaurant::class.java, "restaurants")
     val deliverers = ModelOperations<Deliverer>(Deliverer::class.java, "deliverers")
     val orders = ModelOperations<Order>(Order::class.java, "orders")


     fun loadCurrentUser(cb : () -> Unit){
          val key = FirebaseAuth.getInstance().currentUser?.uid
          if(key != null) {
               FirebaseDatabase.getInstance().getReference().child("users").child(key).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                         if(p0.exists()) {
                              Database.Current_User = Utils.convertObject(p0, User::class.java)
                              cb()
                         }
                    }

                    override fun onCancelled(p0: DatabaseError) {
                         TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
               })
          }
     }

     var Current_User : User? = null
          private set

}