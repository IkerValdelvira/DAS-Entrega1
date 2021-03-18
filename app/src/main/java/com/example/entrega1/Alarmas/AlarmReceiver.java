package com.example.entrega1.Alarmas;

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
import com.example.entrega1.GestorDB;
import com.example.entrega1.R;

// BroadcastReceiver que se utiliza para recibir las alarmas cuando se añade una película para 'ver más tarde' en una fecha determinada
public class AlarmReceiver extends BroadcastReceiver {

    // Se ejecuta al recibir un aviso de mensaje de broadcast
    @Override
    public void onReceive(Context context, Intent intent) {
        // Si la acción del aviso broadcast es 'alarma' se tratará
        if(intent.getAction().equals("alarma")) {
            // Creación de una notificación: Manager y Builder
            NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channelID");

            // Creación de un canal NorificationChannel
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel canal = new NotificationChannel("channelID", "CanalVerMasTarde", NotificationManager.IMPORTANCE_DEFAULT);

                canal.setDescription("Canal para las notificaciones de 'Ver más tarde'.");
                canal.enableLights(true);
                canal.setLightColor(Color.RED);
                canal.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                canal.enableVibration(true);

                manager.createNotificationChannel(canal);
            }

            // Se recogen los datos del Intent que ha creado el aviso broadcast mediante un Bundle
            Bundle extras = intent.getExtras();
            String usuario = "";
            String id = "";
            String titulo = "";
            int anyo = 0;
            int mes = 0;
            int dia = 0;
            if (extras != null) {
                usuario = extras.getString("usuario");          // Nombre de usuario
                id = extras.getString("id");                    // id de la película que se ha planificado para 'ver más tarde'
                titulo = extras.getString("titulo");            // Título de la película que se ha planificado para 'ver más tarde'
                anyo = extras.getInt("anyo");                   // Año de la planificación
                mes = extras.getInt("mes");                     // Mes de la planificación
                dia = extras.getInt("dia");                     // Día de la planificación
            }

            // PendingIntent "Abrir pelicula" --> Acción de la notificación
            Intent i1 = new Intent(context, PeliculaActivity.class);        // Crea una nueva actividad 'PeliculaActivity'
            i1.putExtra("usuario", usuario);
            i1.putExtra("id", id);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i1, PendingIntent.FLAG_UPDATE_CURRENT);

            // PendingIntent (implícito) "Buscar informacion" --> Acción de la notificación
            Uri uri = Uri.parse("https://www.google.com/search?q=" + titulo);
            Intent i2 = new Intent(Intent.ACTION_VIEW, uri);                // Abre un navegador buscando la película por su título
            PendingIntent pendingIntentInfo = PendingIntent.getActivity(context, 0, i2, PendingIntent.FLAG_UPDATE_CURRENT);

            // PendingIntent "Quitar de VMT" --> Acción de la notificación
            Intent i3 = new Intent(context, VerMasTardeActivity.class);     // Crea una nueva actividad 'VerMasTardeActivity' y borrará la película de la lista 'ver mas tarde'
            i3.putExtra("id_notificacion", 1);
            i3.putExtra("usuario", usuario);
            i3.putExtra("pelicula", id);
            i3.putExtra("anyo", anyo);
            i3.putExtra("mes", mes);
            i3.putExtra("dia", dia);
            PendingIntent pendingIntentQuitarVMT = PendingIntent.getActivity(context, 0, i3, PendingIntent.FLAG_UPDATE_CURRENT);

            // Se definen las características de la notificación
            builder.setSmallIcon(android.R.drawable.ic_notification_overlay)
                    .setContentTitle(titulo)
                    .setContentText(context.getString(R.string.Hola) + " " + usuario + "!!! " + context.getString(R.string.PlanificadoPelicula))
                    .setSubText(context.getString(R.string.VerMasTarde))
                    .setVibrate(new long[]{0, 1000, 500, 1000})
                    .setAutoCancel(true)        // La notificación desaparece al pulsar en ella
                    .addAction(android.R.drawable.ic_search_category_default,context.getString(R.string.BuscarInformacion), pendingIntentInfo)      // Añade la acción 'Buscar información'
                    .addAction(R.drawable.watchlater,context.getString(R.string.QuitarVMT), pendingIntentQuitarVMT)                    // Añade la acción 'Quitar de ver más tarde'
                    .setContentIntent(pendingIntent);       // Añade la acción que abre la actividad con la película concreta (al pulsar en el cuerpo de la notificación)

            manager.notify(1, builder.build());         // Lanza la notificación

            GestorDB gestorDB = new GestorDB(context, "DB", null, 1);       // Inicialización de la instancia para la comunicación con la base de datos local 'DB'
            gestorDB.eliminarAlarma(usuario, id, anyo, mes, dia);       // Se elimina la información de la alarma actual de la base de datos local

        }

    }
}
