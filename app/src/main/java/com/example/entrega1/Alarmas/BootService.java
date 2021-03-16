package com.example.entrega1.Alarmas;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.entrega1.GestorDB;
import com.example.entrega1.Modelos.Alarma;

import java.util.ArrayList;
import java.util.Calendar;

public class BootService extends IntentService {

    public BootService() {
        super("BootService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        // Your code to reset alarms.
        // All of these will be done in Background and will not affect
        // on phone's performance

    }

}
