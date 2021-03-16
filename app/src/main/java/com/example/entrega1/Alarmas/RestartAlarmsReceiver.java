package com.example.entrega1.Alarmas;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.entrega1.GestorDB;
import com.example.entrega1.Modelos.Alarma;

import java.util.ArrayList;
import java.util.Calendar;

public class RestartAlarmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            // It is better to reset alarms using Background IntentService
            /*
            Toast.makeText(context, "Booting Completed", Toast.LENGTH_LONG).show();
            Intent i = new Intent(context, BootService.class);
            context.startService(i);
            */

            GestorDB gestorDB = new GestorDB(context, "DB", null, 1);
            ArrayList<Alarma> alarmasPendientes = gestorDB.getAlarmasPendientes();
            for(Alarma alarma : alarmasPendientes){
                AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                Intent intentAlarm = new Intent(context, AlarmReceiver.class);
                intent.setAction("alarma");
                intent.putExtra("usuario", alarma.getUsuario());
                intent.putExtra("id", String.valueOf(alarma.getIdPelicula()));
                intent.putExtra("titulo", alarma.getTitulo());
                intent.putExtra("anyo", alarma.getAnyo());
                intent.putExtra("mes", alarma.getMes());
                intent.putExtra("dia", alarma.getDia());

                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, alarma.getAnyo());
                c.set(Calendar.MONTH, alarma.getMes());
                c.set(Calendar.DAY_OF_MONTH, alarma.getDia());
                c.set(Calendar.HOUR_OF_DAY, 19);
                c.set(Calendar.MINUTE, 5);
                c.set(Calendar.SECOND, 0);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
            }
        }
    }

}
