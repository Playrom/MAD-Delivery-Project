package it.polito.justorder_framework.db

import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import it.polito.justorder_framework.Utils
import it.polito.justorder_framework.model.Product
import java.io.InputStream
import java.net.URI

object Storage {
    fun saveImage(stream: InputStream){
        var ref: StorageReference = Database.storage.products.child("prova")
        ref.putStream(stream)
    }

    fun saveImage(uri: Uri){
        var ref: StorageReference = Database.storage.products.child("prova")
        ref.putFile(uri);
    }
}