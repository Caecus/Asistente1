package com.caecus.asistente.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class NotificacionIDTokenService extends FirebaseInstanceIdService {

    private static final String TAG = "FIREBASE_TOKEN";
    @Override
    public void onTokenRefresh() {
        //super.onTokenRefresh();
        Log.d(TAG, "Solicitando Token");
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, token);

    }

    public String tokenFirebase() {
        return FirebaseInstanceId.getInstance().getToken();

    }


}