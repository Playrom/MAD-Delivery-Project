package it.polito.justorder_framework.db

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.LocationCallback
import com.google.firebase.database.DatabaseError
import it.polito.justorder_framework.abstract_activities.ActivityAbstract
import it.polito.justorder_framework.model.DelivererPosition
import java.util.logging.Logger
import java.util.stream.Collectors
import java.util.stream.IntStream

class GPSOperations(val path: String) {
    fun trackClientPosition(clientId: String, context: ActivityAbstract, cb: () -> Unit ){
        val ref = Database.db.child(path);
        val geo = GeoFire(ref)

        // Acquire a reference to the system Location Manager
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Define a listener that responds to location updates
        val locationListener = object : LocationListener {

            override fun onLocationChanged(location: Location) {
                // Called when a new location is found by the network location provider.
                Logger.getLogger("GPS POSITION").info(location.toString())
                geo.setLocation(clientId, GeoLocation(location.latitude, location.longitude))
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            }

            override fun onProviderEnabled(provider: String) {
            }

            override fun onProviderDisabled(provider: String) {
            }
        }

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED){
            context.permissionsResultCallback = ActivityCompat.OnRequestPermissionsResultCallback { requestCode, permissions, grantResults ->

                val indexOpt = IntStream.range(0, permissions.size)
                        .filter { i -> Manifest.permission.ACCESS_FINE_LOCATION.equals(permissions.get(i)) }
                        .findFirst()

                if(indexOpt.isPresent && grantResults.get(indexOpt.asInt) == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f,locationListener)
                }
                cb()
            }
            ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1);
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f,locationListener)
            cb()
        }

        // Register the listener with the Location Manager to receive location updates

    }

    fun setClientPosition(clientId: String, context: ActivityAbstract, cb: () -> Unit ){
        val ref = Database.db.child(path);
        val geo = GeoFire(ref)

        // Acquire a reference to the system Location Manager
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Define a listener that responds to location updates
        val locationListener = object : LocationListener {

            override fun onLocationChanged(location: Location) {
                // Called when a new location is found by the network location provider.
                Logger.getLogger("GPS POSITION").info(location.toString())
                geo.setLocation(clientId, GeoLocation(location.latitude, location.longitude))
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            }

            override fun onProviderEnabled(provider: String) {
            }

            override fun onProviderDisabled(provider: String) {
            }
        }

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED){
            context.permissionsResultCallback = ActivityCompat.OnRequestPermissionsResultCallback { requestCode, permissions, grantResults ->

                val indexOpt = IntStream.range(0, permissions.size)
                        .filter { i -> Manifest.permission.ACCESS_FINE_LOCATION.equals(permissions.get(i)) }
                        .findFirst()

                if(indexOpt.isPresent && grantResults.get(indexOpt.asInt) == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null)
                }
                cb()
            }
            ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1);
        }else{
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null)
            cb()
        }

        // Register the listener with the Location Manager to receive location updates

    }

    fun getDeliverersNear(restaurantId : String, cb: (List<DelivererPosition>, GeoLocation) -> Unit){
        val ref = Database.db.child(path);
        val geo = GeoFire(ref)
        val deliverPositions = mutableListOf<DelivererPosition>()

        geo.getLocation(restaurantId,  object : LocationCallback {
            override fun onLocationResult(key: String?, location: GeoLocation?) {
                if(location != null && key != null){
                    Database.deliverers.getAll {
                        for (deliverer in it){
                            val delivererKey = deliverer.keyId
                            val delivererUserKey = deliverer.userKey
                            if(delivererKey != null && delivererUserKey != null){
                                geo.getLocation(deliverer.keyId, object: LocationCallback {
                                    override fun onLocationResult(keyDeliverer: String?, locationDeliverer: GeoLocation?) {
                                        var delivererDistance = DelivererPosition()
                                        if(locationDeliverer != null && keyDeliverer != null){
                                            delivererDistance.deliverer = deliverer
                                            delivererDistance.location = locationDeliverer
                                        }

                                        Database.users.get(delivererUserKey) { user ->
                                            delivererDistance.user = user

                                            deliverPositions.add(delivererDistance)
                                            if(deliverPositions.size === it.size){
                                                val listOfDeliverWithPosition =  deliverPositions.stream().filter {
                                                    it.location != null && it.deliverer != null && it.deliverer!!.currentOrder == null
                                                }.collect(Collectors.toList());
                                                cb(listOfDeliverWithPosition, location)
                                            }
                                        }


                                    }

                                    override fun onCancelled(databaseError: DatabaseError?) {
                                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                    }
                                })
                            }else{
                                var delivererDistance = DelivererPosition()
                                deliverPositions.add(delivererDistance)
                                if(deliverPositions.size === it.size){
                                    val listOfDeliverWithPosition =  deliverPositions.stream().filter {
                                        it.location != null && it.deliverer != null && it.deliverer!!.currentOrder == null
                                    }.collect(Collectors.toList());
                                    cb(listOfDeliverWithPosition, location)
                                }
                            }
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }
}