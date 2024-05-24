package com.example.deliciapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.deliciapp.R;
import com.example.deliciapp.databinding.ActivitySignupBinding;
import com.google.firebase.auth.AuthResult;

public class SignupActivity extends BaseActivity {
    ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setVariable();
    }

    private void setVariable() {
        // Configura el botón de registro
        binding.signupBtn.setOnClickListener(v -> {
            // Obtiene el correo electrónico y la contraseña ingresados por el usuario
            String email = binding.userEdt.getText().toString();
            String password = binding.passEdt.getText().toString();

            // Verifica si la contraseña tiene menos de 6 caracteres
            if (password.length() < 6) {
                Toast.makeText(SignupActivity.this, "Su contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                return;
            }

            // Intenta crear un nuevo usuario con el correo electrónico y la contraseña proporcionados
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignupActivity.this, task -> {
                if (task.isSuccessful()) {
                    // Si el registro es exitoso, redirige al usuario a la actividad principal
                    Log.i(TAG, "Registro completo");
                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                } else {
                    // Si el registro falla, muestra un mensaje de error
                    Log.i(TAG, "Error de registro: " + task.getException());
                    Toast.makeText(SignupActivity.this, "Error de autenticación", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Configura el botón para ir a la actividad de inicio de sesión
        binding.BtCuentaIng.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        });
    }
}
