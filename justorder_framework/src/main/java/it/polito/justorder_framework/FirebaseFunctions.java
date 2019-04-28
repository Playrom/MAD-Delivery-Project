package it.polito.justorder_framework;

import android.app.Activity;
import android.content.Context;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.logging.Logger;

public class FirebaseFunctions {
    public static final int AUTH_ACTIVITY_RESULT = 4;
    public static Logger LOGGER = Logger.getLogger("FirebaseFunctions");
    public static void login(Activity context) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {

        } else {
            context.startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.GoogleBuilder().build(),
                                new AuthUI.IdpConfig.EmailBuilder().build(),
                                new AuthUI.IdpConfig.PhoneBuilder().build()))
                        .build(),
                            AUTH_ACTIVITY_RESULT
            );
        }
    }

    public static boolean isLoggedIn(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        return auth.getCurrentUser() != null;
    }

    public static void logout() {
        FirebaseAuth.getInstance().signOut();
        EventBus.getDefault().post(new UserChangeStatusEvent());
        LOGGER.info("FIREBASE LOGOUT");
    }
}
