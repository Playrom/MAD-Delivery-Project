package it.polito.justorder_framework.db

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Logger
import com.google.firebase.storage.FirebaseStorage



object Database {
     object Storage {
          var products = FirebaseStorage.getInstance().getReference("products")
     }
     var db : DatabaseReference = FirebaseDatabase.getInstance().getReference()
     var storage = Storage

}