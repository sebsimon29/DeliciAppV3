package com.example.deliciapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.deliciapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class BaseActivity extends AppCompatActivity {
    FirebaseAuth mAuth; // Objeto de autenticación de Firebase
    FirebaseDatabase database; // Objeto de base de datos de Firebase
    public String TAG="uilover"; // Etiqueta para el registro de eventos y errores

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializar la instancia de Firebase Database y Firebase Auth
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Establecer el color de la barra de estado como blanco
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
    }
}
