package com.example.deliciapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.deliciapp.R;
import com.example.deliciapp.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class LoginActivity extends BaseActivity {
    ActivityLoginBinding binding; // Binding para la actividad

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // Establecer el layout de la actividad

        setVariable(); // Configurar las variables y listeners
    }

    // Método para configurar las variables y listeners de la actividad
    private void setVariable() {
        // Configurar el evento onClick para el botón de inicio de sesión
        binding.loginBtn.setOnClickListener(v -> {
            String email = binding.userEdt.getText().toString(); // Obtener el correo electrónico ingresado
            String password = binding.passEdt.getText().toString(); // Obtener la contraseña ingresada
            // Verificar si el correo electrónico y la contraseña no están vacíos
            if (!email.isEmpty() && !password.isEmpty()) {
                // Iniciar sesión con el correo electrónico y la contraseña proporcionados
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, task -> {
                    if (task.isSuccessful()) {
                        // Si la autenticación es exitosa, redirigir a MainActivity
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        // Si la autenticación falla, mostrar un mensaje de error
                        Toast.makeText(LoginActivity.this, "Error de autenticación", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // Si el correo electrónico o la contraseña están vacíos, mostrar un mensaje de advertencia
                Toast.makeText(LoginActivity.this, "Complete nombre de usuario y contraseña", Toast.LENGTH_SHORT).show();
            }
        });

        // Configurar el evento onClick para el botón de "Crear cuenta"
        binding.BtCuentaReg.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class)); // Redirigir a SignupActivity
        });
    }
}
