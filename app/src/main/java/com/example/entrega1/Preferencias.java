package com.example.entrega1;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.entrega1.Dialogos.DialogoQuitarFavoritos;

public class Preferencias extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    ListenerPreferencias miListener;

    public interface ListenerPreferencias {
        void alCambiarNombre();
        void alCambiarIdioma();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.conf_preferencias);

        miListener = (ListenerPreferencias) getActivity();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        switch (s) {
            case "nombre":
                miListener.alCambiarNombre();
                break;
            case "idioma":
                miListener.alCambiarIdioma();
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
