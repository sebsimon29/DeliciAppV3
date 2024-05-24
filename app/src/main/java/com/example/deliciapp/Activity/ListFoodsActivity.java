package com.example.deliciapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.deliciapp.Adapter.FoodListAdapter;
import com.example.deliciapp.Domain.Foods;
import com.example.deliciapp.R;
import com.example.deliciapp.databinding.ActivityListFoodsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListFoodsActivity extends BaseActivity {
    ActivityListFoodsBinding binding; // Binding para la actividad
    private RecyclerView.Adapter adapterListFood; // Adaptador para la lista de alimentos
    private int categoryId; // ID de la categoría
    private String categoryName; // Nombre de la categoría
    private String searchText; // Texto de búsqueda
    private boolean isSearch; // Indicador de búsqueda

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListFoodsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // Establecer el layout de la actividad

        getIntentExtra(); // Obtener los extras del intent
        initList(); // Inicializar la lista de alimentos
        setVariable(); // Configurar las variables y listeners
    }

    // Método para configurar las variables y listeners de la actividad
    private void setVariable() {
        // No se requiere configuración adicional en este método en este momento
    }

    // Método para inicializar la lista de alimentos
    private void initList() {
        DatabaseReference myRef = database.getReference("Foods"); // Referencia a la base de datos "Foods"
        binding.progressBar.setVisibility(View.VISIBLE); // Mostrar la barra de progreso
        ArrayList<Foods> list = new ArrayList<>(); // Lista de alimentos

        Query query;
        // Configurar la consulta según si es una búsqueda o una categoría seleccionada
        if (isSearch) {
            // Si es una búsqueda, ordenar por título y filtrar según el texto de búsqueda
            query = myRef.orderByChild("Title").startAt(searchText).endAt(searchText + '\uf8ff');
        } else {
            // Si es una categoría seleccionada, filtrar según el ID de categoría
            query = myRef.orderByChild("CategoryId").equalTo(categoryId);
        }
        // Agregar un listener para la consulta
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Si hay datos en el snapshot
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        list.add(issue.getValue(Foods.class)); // Agregar cada elemento a la lista de alimentos
                    }
                    if (list.size() > 0) {
                        // Si la lista no está vacía, configurar el layout del RecyclerView y el adaptador
                        binding.foodListView.setLayoutManager(new GridLayoutManager(ListFoodsActivity.this, 2));
                        adapterListFood = new FoodListAdapter(list); // Adaptador para la lista de alimentos
                        binding.foodListView.setAdapter(adapterListFood); // Establecer el adaptador en el RecyclerView
                    }
                    binding.progressBar.setVisibility(View.GONE); // Ocultar la barra de progreso
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // En caso de cancelación de la consulta
            }
        });
    }

    // Método para obtener los extras del intent
    private void getIntentExtra() {
        categoryId = getIntent().getIntExtra("CategoryId", 0); // Obtener el ID de la categoría
        categoryName = getIntent().getStringExtra("CategoryName"); // Obtener el nombre de la categoría
        searchText = getIntent().getStringExtra("text"); // Obtener el texto de búsqueda
        isSearch = getIntent().getBooleanExtra("isSearch", false); // Obtener el indicador de búsqueda

        binding.titleTxt.setText(categoryName); // Establecer el nombre de la categoría en el título
        binding.backBtn.setOnClickListener(v -> finish()); // Configurar el evento onClick para el botón de retroceso
    }
}
