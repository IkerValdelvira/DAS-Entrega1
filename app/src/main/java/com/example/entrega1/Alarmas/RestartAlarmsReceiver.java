package com.example.entrega1.Alarmas;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.entrega1.GestorDB;
import com.example.entrega1.Modelos.Alarma;

import java.util.ArrayList;
import java.util.Calendar;

// BroadcastReceiver que se utiliza para poner en marcha las alarmas pendientes después de reiniciar el dispositivo
public class RestartAlarmsReceiver extends BroadcastReceiver {

    // Se ejecuta al recibir un aviso de mensaje de broadcast
    @Override
    public void onReceive(Context context, Intent intent) {
        // Si la acción del aviso broadcast es 'BOOT_COMPLETED' se tratará --> Se ha terminado el arranque del sistema
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            GestorDB gestorDB = new GestorDB(context, "DB", null, 1);        // Inicialización de la instancia para la comunicación con la base de datos local 'DB'
            ArrayList<Alarma> alarmasPendientes = gestorDB.getAlarmasPendientes();                // Se obtienen de la base de datos local la información de las alarmas que estaban pendientes antes del reinicio del sistema
            for(Alarma alarma : alarmasPendientes){
                // Se programa una nueva alarma (AlarmManager) por cada alarma pendiente obtenida de la base de datos local
                AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                Intent intentAlarm = new Intent(context, AlarmReceiver.class);
                intent.setAction("alarma");
                intent.putExtra("usuario", alarma.getUsuario());
                intent.putExtra("id", String.valueOf(alarma.getIdPelicula()));
                intent.putExtra("titulo", alarma.getTitulo());
                intent.putExtra("anyo", alarma.getAnyo());
                intent.putExtra("mes", alarma.getMes());
                intent.putExtra("dia", alarma.getDia());

                // PendingIntent que lanza un broadcast
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);

                // Se establece cuando se quiere lanzar el aviso de broadcast con los datos de la alarma pendiente recuperada de la base de datos local
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, alarma.getAnyo());
                c.set(Calendar.MONTH, alarma.getMes());
                c.set(Calendar.DAY_OF_MONTH, alarma.getDia());

                // Se le indica al AlarmManager cuándo quiere lanzar el PendingIntent
                //  - RTC_WAKEUP: lanza la alarma a la hora especificada despertando el dispositivo
                //  - setExactAndAllowWhileIdle: la alarma funciona en modo descanso (Doze) y con exactitud
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
            }
        }
    }

}
