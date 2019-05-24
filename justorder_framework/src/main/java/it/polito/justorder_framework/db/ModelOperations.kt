package it.polito.justorder_framework.db

import com.google.firebase.database.*
import it.polito.justorder_framework.Utils
import it.polito.justorder_framework.model.Model
import it.polito.justorder_framework.model.Order

open class ModelOperations<T: Model>(protected val tClass: Class<T>, val path: String){
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

    fun get(key: String, listen: Boolean, cb : (T) -> Unit) {
        if(listen){
            Database.db.child(path).child(key).addValueEventListener(object : ValueEventListener {
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
        }else{
            get(key, cb);
        }
    }

    open fun save(entity: T){
        var ref: DatabaseReference
        val key = entity.keyId
        if(key != null){
            ref = Database.db.child(path).child(key)
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

    fun getAll(listen: Boolean, cb : (List<T>) -> Unit) {
        if(listen) {
            Database.db.child(path).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    var items: MutableList<T> = mutableListOf()
                    for (item in p0.children) {
                        items.add(Utils.convertObject(item, tClass))
                    }
                    cb(items)
                }

                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })
        }else{
            getAll(cb)
        }
    }

    fun getWithIds(keys: List<String>, cb : (List<T>) -> Unit) {
        var ref = Database.db.child(path)
        var collection = mutableListOf<T>()
        for(key in keys){
            ref.child(key).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.exists()) {
                        collection.add(Utils.convertObject(p0, tClass))
                    }
                    if(collection.size.equals(keys.size)){
                        cb(collection)
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })
        }
    }

//    fun <K: Model> onChange(child: String, expectedClass: Class<K>, cb : (List<K>) -> Unit) {
//        var ref = Database.db.child(path)
//        ref.child(child).addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(p0: DataSnapshot) {
//                var items: MutableList<K> = mutableListOf()
//                for(item in p0.children){
//                    items.add(Utils.convertObject(item, expectedClass))
//                }
//                cb(items)
//            }
//
//            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//        })
//    }

    fun query(key: String, value: String, cb : (List<T>) -> Unit){
        var ref = Database.db.child(path)
        var query = ref.orderByChild(key).equalTo(value);
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                var items: MutableList<T> = mutableListOf()
                for (item in p0.children) {
                    items.add(Utils.convertObject(item, tClass))
                }
                cb(items)
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    fun generateKeyForChild(vararg childPath: String) : String?{
        var ref = Database.db.child(path)
        for(child in childPath){
            ref = ref.child(child)
        }
        return ref.push().key
    }
}