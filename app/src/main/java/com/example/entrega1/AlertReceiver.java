package com.example.entrega1;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.example.entrega1.Actividades.PeliculaActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channelID");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel("channelID", "CanalVerMasTarde", NotificationManager.IMPORTANCE_DEFAULT);

            canal.setDescription("Canal para las notificaciones de 'Ver más tarde'.");
            canal.enableLights(true);
            canal.setLightColor(Color.RED);
            canal.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            canal.enableVibration(true);

            manager.createNotificationChannel(canal);
        }

        Bundle extras = intent.getExtras();
        String id = "";
        String titulo = "";
        if (extras != null) {
            id = extras.getString("id");
            titulo = extras.getString("titulo");
        }

        // Intent "Buscar informacion"
        Uri uri = Uri.parse("https://www.google.com/search?q=" + titulo);
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        PendingIntent pendingIntentInfo = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setSmallIcon(android.R.drawable.stat_sys_warning)
                .setContentTitle(titulo)
                .setContentText("Habías planificado ver esta película hoy.")
                .setSubText("Ver más tarde")
                .setVibrate(new long[]{0, 1000, 500, 1000})
                //.setAutoCancel(true)
                .addAction(android.R.drawable.alert_dark_frame,"Buscar información", pendingIntentInfo)
                .setAutoCancel(true);;

        manager.notify(1, builder.build());

    }
}
