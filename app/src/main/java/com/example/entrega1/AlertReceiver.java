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
import com.example.entrega1.Actividades.VerMasTardeActivity;

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("alarma")) {
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
            String usuario = "";
            String id = "";
            String titulo = "";
            String fecha = "";
            if (extras != null) {
                usuario = extras.getString("usuario");
                id = extras.getString("id");
                titulo = extras.getString("titulo");
                fecha = extras.getString("fecha");
            }

            // Intent "Abrir pelicula"
            Intent i1 = new Intent(context, PeliculaActivity.class);
            i1.putExtra("usuario", usuario);
            i1.putExtra("id", id);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i1, PendingIntent.FLAG_UPDATE_CURRENT);

            // Intent "Buscar informacion"
            Uri uri = Uri.parse("https://www.google.com/search?q=" + titulo);
            Intent i2 = new Intent(Intent.ACTION_VIEW, uri);
            PendingIntent pendingIntentInfo = PendingIntent.getActivity(context, 0, i2, PendingIntent.FLAG_UPDATE_CURRENT);

            // Intent "Quitar de VMT"
            Intent i3 = new Intent(context, VerMasTardeActivity.class);
            i3.putExtra("id_notificacion", 1);
            i3.putExtra("usuario", usuario);
            i3.putExtra("pelicula", id);
            i3.putExtra("fecha", fecha);
            PendingIntent pendingIntentQuitarVMT = PendingIntent.getActivity(context, 0, i3, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setSmallIcon(android.R.drawable.ic_notification_overlay)
                    .setContentTitle(titulo)
                    .setContentText(context.getString(R.string.PlanificadoPelicula))
                    .setSubText(context.getString(R.string.VerMasTarde))
                    .setVibrate(new long[]{0, 1000, 500, 1000})
                    .setAutoCancel(true)
                    .addAction(android.R.drawable.ic_search_category_default,context.getString(R.string.BuscarInformacion), pendingIntentInfo)
                    .addAction(R.drawable.watchlater,context.getString(R.string.QuitarVMT), pendingIntentQuitarVMT)
                    .setContentIntent(pendingIntent);

            manager.notify(1, builder.build());
        }

    }
}
