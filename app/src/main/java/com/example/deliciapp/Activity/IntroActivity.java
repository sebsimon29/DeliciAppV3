package com.example.deliciapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.deliciapp.R;
import com.example.deliciapp.databinding.ActivityIntroBinding;

public class IntroActivity extends BaseActivity {
    ActivityIntroBinding binding; // Binding para la actividad

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // Establecer el layout de la actividad

        setVariable(); // Configurar las variables y listeners
        getWindow().setStatusBarColor(Color.parseColor("#FFE4B5")); // Establecer el color de la barra de estado
    }

    // Método para configurar las variables y listeners de la actividad
    private void setVariable() {
        // Configurar el evento onClick para el botón de inicio de sesión
        binding.loginBtn.setOnClickListener(v -> {
            if (mAuth.getCurrentUser() != null) {
                startActivity(new Intent(IntroActivity.this, MainActivity.class)); // Redirige a MainActivity si el usuario ya ha iniciado sesión
            } else {
                startActivity(new Intent(IntroActivity.this, LoginActivity.class)); // Redirige a LoginActivity si el usuario no ha iniciado sesión
            }
        });

        // Configurar el evento onClick para el botón de registro
        binding.signupBtn.setOnClickListener(v -> startActivity(new Intent(IntroActivity.this, SignupActivity.class))); // Redirige a SignupActivity
    }
}
