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
               FirebaseDatabase.getInstance().reference.child("users").child(key).addListenerForSingleValueEvent(object : ValueEventListener {
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
     var db : DatabaseReference = FirebaseDatabase.getInstance().reference
     var storage = Storage

     val users = ModelOperations<User>(User::class.java, "users")
     val restaurants = ModelWithVoteOperations<Restaurant>(Restaurant::class.java, "restaurants")
     val deliverers = ModelWithVoteOperations<Deliverer>(Deliverer::class.java, "deliverers")
     val orders = ModelOperations<Order>(Order::class.java, "orders")
     val reviews = ReviewOperations<Review>(Review::class.java, "reviews")


     fun loadCurrentUser(cb : () -> Unit){
          val key = FirebaseAuth.getInstance().currentUser?.uid
          if(key != null) {
               FirebaseDatabase.getInstance().reference.child("users").child(key).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                         if(p0.exists()) {
                              Database.Current_User = Utils.convertObject(p0, User::class.java)
                              cb()
                         }else{
                              var user = User()
                              user.keyId = key
                              Database.Current_User = user
                              users.save(user)
                              cb()
                         }
                    }

                    override fun onCancelled(p0: DatabaseError) {
                         TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
               })
          }
     }

     val geodata = GPSOperations("gps_data_from_clients");

     var Current_User : User? = null
          private set

     fun generateKeyForChild(path: String) : String?{
          var ref = Database.db.child(path)
          return ref.push().key
     }

}