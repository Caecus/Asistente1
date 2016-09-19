package com.caecus.asistente.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.caecus.asistente.AvisoActivity;
import com.caecus.asistente.MenuAsistenteActivity;
import com.caecus.asistente.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class NotificationService extends FirebaseMessagingService {

    public static final String TAG = "FIREBASE";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getData());
        enviarNotificacion(remoteMessage);

    }

    public void enviarNotificacion(RemoteMessage remoteMessage) {
        int cod = Integer.parseInt(remoteMessage.getData().get("cod"));
        String name = remoteMessage.getData().get("pdv");
        if (cod == 1) {
            Intent i = new Intent(this, MenuAsistenteActivity.class);
            Intent a = new Intent(this, AvisoActivity.class);
            a.putExtra("TEL", (remoteMessage.getData().get("telefono")));
            a.putExtra("LAT", Double.parseDouble(remoteMessage.getData().get("lat")));
            a.putExtra("LNG", Double.parseDouble(remoteMessage.getData().get("lng")));
            a.putExtra("NAME", name);
            //i.putExtra("AVISO", (remoteMessage.getData().get("aviso")));
            i.putExtra("NAME", name);
            i.putExtra("COD", cod);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_ONE_SHOT);

            PendingIntent aceptIntent = PendingIntent.getActivity(this, 0, a, PendingIntent.FLAG_ONE_SHOT);

            Uri sonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

            NotificationCompat.Builder notificacion = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_record_voice_over_white_24dp)
                    .setContentTitle("Atencion")
                    .setSound(sonido)
                    .setVibrate(new long[]{100, 250, 100, 500})
                    .setContentText(name + " necesita ayuda")
                    .setAutoCancel(true)
                    .setPriority(2)
                    .addAction(R.drawable.ic_check_white_24dp, "Ayudar", aceptIntent)
                    .addAction((R.drawable.ic_close_white_24dp), "No puedo", pendingIntent)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificacion.build());
        }

        if (cod == 2) {
            String ayudante = remoteMessage.getData().get("ayudante");
            Intent i = new Intent(this, MenuAsistenteActivity.class);
            i.putExtra("NAME", name);
            i.putExtra("AYUDANTE", ayudante);
            i.putExtra("COD", cod);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_ONE_SHOT);
            Uri sonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            NotificationCompat.Builder notificacion = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_record_voice_over_white_24dp)
                    .setContentTitle("Atencion")
                    .setSound(sonido)
                    .setVibrate(new long[]{100, 250, 100, 500})
                    .setContentText(name + " ha sido asistido por " + ayudante)
                    .setAutoCancel(true)

                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificacion.build());
        }

        if (cod == 0) {
            Intent i = new Intent(this, MenuAsistenteActivity.class);
            //i.putExtra("TEL", (remoteMessage.getData().get("telefono")));
            //i.putExtra("LAT", Double.parseDouble(remoteMessage.getData().get("lat")));
            //i.putExtra("LNG", Double.parseDouble(remoteMessage.getData().get("lng")));
            //i.putExtra("AVISO", (remoteMessage.getData().get("aviso")));
            i.putExtra("NAME", name);
            i.putExtra("COD", cod);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_ONE_SHOT);
            Uri sonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            NotificationCompat.Builder notificacion = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_record_voice_over_white_24dp)
                    .setContentTitle("Atencion")
                    .setSound(sonido)
                    .setVibrate(new long[]{100, 250, 100, 500})
                    .setContentText(name + " necesita ayuda")
                    .setAutoCancel(true)

                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificacion.build());
        }
    }
}