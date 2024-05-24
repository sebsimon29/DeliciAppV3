package com.example.deliciapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.deliciapp.Domain.Foods;
import com.example.deliciapp.Helper.ManagmentCart;
import com.example.deliciapp.R;
import com.example.deliciapp.databinding.ActivityDetailBinding;

public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding; // Binding para la actividad
    private Foods object; // Objeto de alimentos
    private int num = 1; // Número de productos seleccionados
    private ManagmentCart managmentCart; // Objeto de gestión del carrito

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(getResources().getColor(R.color.black)); // Establecer color de la barra de estado

        getIntentExtra(); // Obtener los extras de la intención
        setVariable(); // Configurar las variables y listeners
    }

    // Método para configurar las variables y listeners de la actividad
    private void setVariable() {
        managmentCart = new ManagmentCart(this); // Inicializar el objeto de gestión del carrito

        // Configurar el evento onClick para el botón de retroceso
        binding.backBtn.setOnClickListener(v -> finish());

        // Cargar la imagen de los alimentos usando Glide
        Glide.with(DetailActivity.this)
                .load(object.getImagePath())
                .into(binding.pic);

        // Establecer el precio, título, descripción, clasificación y total en las vistas correspondientes
        binding.priceTxt.setText("$" + object.getPrice());
        binding.titleTxt.setText(object.getTitle());
        binding.descriptionTxt.setText(object.getDescription());
        binding.rateTxt.setText(object.getStar() + " Rating");
        binding.ratingBar.setRating((float) object.getStar());
        binding.totalTxt.setText((num * object.getPrice() + "$"));

        // Configurar el evento onClick para el botón de suma
        binding.plusBtn.setOnClickListener(v -> {
            num = num + 1; // Incrementar el número de productos seleccionados
            binding.numTxt.setText(num + " "); // Establecer el nuevo número en la vista correspondiente
            binding.totalTxt.setText("$" + (num * object.getPrice())); // Actualizar el total en la vista
        });

        // Configurar el evento onClick para el botón de resta
        binding.minusBtn.setOnClickListener(v -> {
            if (num > 1) {
                num = num - 1; // Decrementar el número de productos seleccionados si es mayor que 1
                binding.numTxt.setText(num + ""); // Establecer el nuevo número en la vista correspondiente
                binding.totalTxt.setText("$" + (num * object.getPrice())); // Actualizar el total en la vista
            }
        });

        // Configurar el evento onClick para el botón de añadir al carrito
        binding.addBtn.setOnClickListener(v -> {
            object.setNumberInCart(num); // Establecer el número de productos seleccionados en el objeto de alimentos
            managmentCart.insertFood(object); // Insertar el alimento en el carrito
        });
    }

    // Método para obtener los extras de la intención
    private void getIntentExtra() {
        object = (Foods) getIntent().getSerializableExtra("object"); // Obtener el objeto de alimentos de los extras
    }
}
