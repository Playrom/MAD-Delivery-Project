package it.polito.justorder_framework.db

import android.graphics.Bitmap
import android.net.Uri
import com.google.android.gms.tasks.Continuation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import it.polito.justorder_framework.Utils
import it.polito.justorder_framework.model.Product
import java.io.InputStream
import java.net.URI
import com.google.firebase.database.FirebaseDatabase
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import java.util.*


object Storage {
    fun saveImage(stream: InputStream, cb: (Uri) -> Unit){
        var ref: StorageReference = Database.storage.products.child(UUID.randomUUID().toString())
        val uploadTask = ref.putStream(stream)
        this.getPublicUrl(ref, uploadTask, cb);

    }

    fun saveImage(uri: Uri, cb: (Uri) -> Unit){
        var ref: StorageReference = Database.storage.products.child(UUID.randomUUID().toString())
        var uploadTask = ref.putFile(uri);
        this.getPublicUrl(ref, uploadTask, cb);
    }

    fun saveImage(byteArray: ByteArray, cb: (Uri) -> Unit){
        var ref: StorageReference = Database.storage.products.child(UUID.randomUUID().toString())
        var uploadTask = ref.putBytes(byteArray);
        this.getPublicUrl(ref, uploadTask, cb);
    }
    private fun getPublicUrl(ref: StorageReference, task: UploadTask, cb: (Uri) -> Unit){
        val urlTask = task.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation ref.downloadUrl
        }).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                if(downloadUri != null){
                    cb(downloadUri);
                }
            }
        }
    }
}