package com.example.entrega1.Dialogos;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

// Diálogo que se muestra antes de añadir una película a la lista 'ver más tarde' (tras pulsar el botón 'Añadir a Ver más tarde' de la actividad 'PeliculaActivity')
// Diálogo tipo DatePickerDialog para seleccionar una fecha
public class DialogoFecha extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    // Interfaz del listener para que las acciones del diálogo se ejecuten en la actividad que llamó al diálogo (PeliculaActivity)
    ListenerDelDialogo miListener;
    public interface ListenerDelDialogo {
        void alPulsarOK(int pYear, int pMonth, int pDayOfMonth);
    }

    // Se ejecuta al crearse el diálogo
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        setRetainInstance(true);        // Mantiene la información del dialogo tras rotación del dispositivo

        miListener = (ListenerDelDialogo) getActivity();        // Se referencia a la implementación de la actividad

        // Se crea el DatePickerDialog con la fecha actual por defecto
        Calendar calendario = Calendar.getInstance();
        int anyo = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialogo = new DatePickerDialog(getActivity(),this, anyo,mes,dia);
        return dialogo;
    }

    // Se ejecuta al elegir una fecha y aceptarla
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // Llama al método 'alPulsarOK' del listener que se ejecutará en la actividad
        miListener.alPulsarOK(year,month,dayOfMonth);
    }
}