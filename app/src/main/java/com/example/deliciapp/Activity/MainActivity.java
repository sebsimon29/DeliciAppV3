package com.example.deliciapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.deliciapp.Adapter.BestFoodsAdapter;
import com.example.deliciapp.Adapter.CategoryAdapter;
import com.example.deliciapp.Domain.Category;
import com.example.deliciapp.Domain.Foods;
import com.example.deliciapp.Domain.Location;
import com.example.deliciapp.Domain.Price;
import com.example.deliciapp.Domain.Time;
import com.example.deliciapp.R;
import com.example.deliciapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding; // Declaración de la variable de enlace con la vista

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater()); // Inflar la vista usando el enlace
        setContentView(binding.getRoot()); // Establecer la vista raíz

        // Inicializar los elementos de la interfaz de usuario
        initLocation();
        initTime();
        initPrice();
        initBestFood();
        initCategory();
        setVariable();

        // Configurar el evento onClick para imageView9
        ImageView imageView9 = findViewById(R.id.imageView9);
        imageView9.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Mapa.class);
            startActivity(intent);
        });
    }

    // Método para configurar las variables y los eventos onClick
    private void setVariable() {
        // Evento onClick para el botón de cierre de sesión
        binding.logoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut(); // Cerrar sesión
            startActivity(new Intent(MainActivity.this, LoginActivity.class)); // Redirigir a la actividad de inicio de sesión
        });

        // Evento onClick para el botón de búsqueda de alimentos
        binding.searchBtn.setOnClickListener(v -> {
            String text = binding.searchEdt.getText().toString(); // Obtener el texto de búsqueda ingresado por el usuario
            if (!text.isEmpty()) { // Verificar si el campo de búsqueda no está vacío
                // Convertir la primera letra del texto ingresado a mayúscula y el resto del texto a minúscula
                text = text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();

                // Crear una nueva intención para iniciar la actividad de lista de alimentos
                Intent intent = new Intent(MainActivity.this, ListFoodsActivity.class);
                intent.putExtra("text", text); // Pasar el texto de búsqueda como un extra a la actividad de lista de alimentos
                intent.putExtra("isSearch", true); // Pasar un indicador de búsqueda como un extra a la actividad de lista de alimentos
                startActivity(intent); // Iniciar la actividad de lista de alimentos
            }
        });

        // Evento onClick para el botón del carrito de compras
        binding.cartBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CartActivity.class)));
    }

    // Método para inicializar los alimentos destacados
    private void initBestFood() {
        DatabaseReference myRef = database.getReference("Foods"); // Obtener una referencia a la base de datos para los alimentos
        binding.progressBarBestFood.setVisibility(View.VISIBLE); // Mostrar la barra de progreso
        ArrayList<Foods> list = new ArrayList<>(); // Crear una lista para almacenar los alimentos destacados

        // Realizar una consulta a la base de datos para obtener los alimentos destacados
        Query query = myRef.orderByChild("BestFood").equalTo(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) { // Verificar si existen datos
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        list.add(issue.getValue(Foods.class)); // Agregar cada alimento destacado a la lista
                    }
                    if (list.size() > 0) { // Verificar si se encontraron alimentos destacados
                        // Configurar el RecyclerView para mostrar los alimentos destacados en un diseño horizontal
                        binding.bestFoodView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        RecyclerView.Adapter adapter = new BestFoodsAdapter(list); // Crear un adaptador para los alimentos destacados
                        binding.bestFoodView.setAdapter(adapter); // Establecer el adaptador en el RecyclerView
                    }
                    binding.progressBarBestFood.setVisibility(View.GONE); // Ocultar la barra de progreso
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar el evento de cancelación de la consulta
            }
        });
    }

    // Método para inicializar las categorías
    private void initCategory() {
        DatabaseReference myRef = database.getReference("Category"); // Obtener una referencia a la base de datos para las categorías
        binding.progressBarCategory.setVisibility(View.VISIBLE); // Mostrar la barra de progreso
        ArrayList<Category> list = new ArrayList<>(); // Crear una lista para almacenar las categorías

        // Realizar una consulta a la base de datos para obtener las categorías
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) { // Verificar si existen datos
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        list.add(issue.getValue(Category.class)); // Agregar cada categoría a la lista
                    }
                    if (list.size() > 0) { // Verificar si se encontraron categorías
                        // Configurar el RecyclerView para mostrar las categorías en un diseño de cuadrícula con 4 columnas
                        binding.categoryView.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
                        RecyclerView.Adapter adapter = new CategoryAdapter(list); // Crear un adaptador para las categorías
                        binding.categoryView.setAdapter(adapter); // Establecer el adaptador en el RecyclerView
                    }
                    binding.progressBarCategory.setVisibility(View.GONE); // Ocultar la barra de progreso
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar el evento de cancelación de la consulta
            }
        });
    }

    // Método para inicializar las ubicaciones
    private void initLocation() {
        DatabaseReference myRef = database.getReference("Location"); // Obtener una referencia a la base de datos para las ubicaciones
        ArrayList<Location> list = new ArrayList<>(); // Crear una lista para almacenar las ubicaciones

        // Escuchar cambios en los datos de ubicación en la base de datos
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) { // Verificar si existen datos de ubicación
                    // Iterar sobre cada ubicación en la base de datos y agregarla a la lista
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        list.add(issue.getValue(Location.class));
                    }
                    // Crear un adaptador de ArrayAdapter para las ubicaciones y establecerlo en el Spinner
                    ArrayAdapter<Location> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.sp_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.locationSp.setAdapter(adapter); // Establecer el adaptador en el Spinner de ubicaciones
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar el evento de cancelación de la consulta
            }
        });
    }

    // Método para inicializar los horarios
    private void initTime() {
        DatabaseReference myRef = database.getReference("Time"); // Obtener una referencia a la base de datos para los horarios
        ArrayList<Time> list = new ArrayList<>(); // Crear una lista para almacenar los horarios

        // Escuchar cambios en los datos de horarios en la base de datos
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) { // Verificar si existen datos de horarios
                    // Iterar sobre cada horario en la base de datos y agregarlo a la lista
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        list.add(issue.getValue(Time.class));
                    }
                    // Crear un adaptador de ArrayAdapter para los horarios y establecerlo en el Spinner
                    ArrayAdapter<Time> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.sp_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.timeSp.setAdapter(adapter); // Establecer el adaptador en el Spinner de horarios
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar el evento de cancelación de la consulta
            }
        });
    }

    // Método para inicializar los precios
    private void initPrice() {
        DatabaseReference myRef = database.getReference("Price"); // Obtener una referencia a la base de datos para los precios
        ArrayList<Price> list = new ArrayList<>(); // Crear una lista para almacenar los precios

        // Escuchar cambios en los datos de precios en la base de datos
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) { // Verificar si existen datos de precios
                    // Iterar sobre cada precio en la base de datos y agregarlo a la lista
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        list.add(issue.getValue(Price.class));
                    }
                    // Crear un adaptador de ArrayAdapter para los precios y establecerlo en el Spinner
                    ArrayAdapter<Price> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.sp_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.priceSp.setAdapter(adapter); // Establecer el adaptador en el Spinner de precios
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar el evento de cancelación de la consulta
            }
        });
    }

    // Método para manejar el evento de retroceso del botón de dispositivo
    @Override
    public void onBackPressed() {
        super.onBackPressed(); // Ejecutar el comportamiento predeterminado del botón de retroceso
    }
}