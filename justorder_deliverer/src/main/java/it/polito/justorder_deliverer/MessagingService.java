package it.polito.justorder_deliverer;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.User;
import kotlin.Unit;

public class MessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d("Firebase Messagging", "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("Firebase Messagging", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("Firebase Messagging", "Message data payload: " + remoteMessage.getData());

            // OPEN THE ORDER VIEW ACTIVITY
            // Intent i = new Intent(getApplicationContext(), OrderToConfirm.class);
            String orderId = remoteMessage.getData().get("orderId");
            Database.INSTANCE.getOrders().get(orderId, (order -> {
                //i.putExtra("order", order);
                //startActivity(i);
                return Unit.INSTANCE;
            }));
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("Firebase Messagging", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    public static void sendRegistrationToServer(String token){
        Database.INSTANCE.loadCurrentUser(() -> {
            User user = Database.INSTANCE.getCurrent_User();

            List<Map.Entry<String, Boolean>> restaurants = user.getManagedRestaurants().entrySet()
                    .stream().filter(x -> {
                        return x.getValue();
                    }).collect(Collectors.toList());
            if(restaurants.size() > 0) {
                String restaurant_key = restaurants.get(0).getKey();
                Database.INSTANCE.getRestaurants().get(restaurant_key, (restaurant -> {
                    restaurant.setMessagingToken(token);
                    Database.INSTANCE.getRestaurants().save(restaurant);
                    return Unit.INSTANCE;
                }));
            }

            return Unit.INSTANCE;
        });
    }
}
