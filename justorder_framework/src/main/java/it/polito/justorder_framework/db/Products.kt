package it.polito.justorder_framework.db

import com.google.firebase.database.*
import it.polito.justorder_framework.Utils
import it.polito.justorder_framework.model.Product

object Products{
    fun getAllProducts(ch : (List<Product>) -> Unit){
        Database.db.child("products").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                var products: MutableList<Product> = mutableListOf()
                for(product in p0.children){
                    products.add(Utils.convertObject(product, Product::class.java))
                }
                ch(products)
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    fun saveProduct(product: Product){
        var ref: DatabaseReference
        val key = product.keyId
        if(key != null){
            ref = Database.db.child("products").child(key)
        }else{
            ref = Database.db.child("products").push()
        }
        ref.setValue(product)
    }
}