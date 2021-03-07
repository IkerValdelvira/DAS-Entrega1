package com.example.entrega1.Dialogos;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DialogoFecha extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    ListenerDelDialogo miListener;

    public interface ListenerDelDialogo {
        void alPulsarOK(int pYear, int pMonth, int pDayOfMonth);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        miListener = (ListenerDelDialogo) getActivity();

        Calendar calendario = Calendar.getInstance();
        int anyo = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialogo = new DatePickerDialog(getActivity(),this, anyo,mes,dia);
        return dialogo;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        miListener.alPulsarOK(year,month,dayOfMonth);
    }
}