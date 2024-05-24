package com.example.deliciapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.deliciapp.Adapter.CartAdapter;
import com.example.deliciapp.Helper.ChangeNumberItemsListener;
import com.example.deliciapp.Helper.ManagmentCart;
import com.example.deliciapp.R;
import com.example.deliciapp.databinding.ActivityCartBinding;

public class CartActivity extends BaseActivity {
    private ActivityCartBinding binding; // Binding para la actividad
    private RecyclerView.Adapter adapter; // Adaptador para la lista de carrito
    private ManagmentCart managmentCart; // Objeto de gestión del carrito
    private double tax; // Impuesto sobre la compra

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // Establecer la vista de la actividad

        managmentCart = new ManagmentCart(this); // Inicializar el objeto de gestión del carrito

        setVariable(); // Configurar las variables de la actividad
        calculateCart(); // Calcular el total del carrito
        initList(); // Inicializar la lista de elementos en el carrito

        // Configurar el evento onClick para el botón "button2"
        binding.button2.setOnClickListener(v -> showThankYouDialog());
    }

    // Método para mostrar un cuadro de diálogo de agradecimiento al usuario
    private void showThankYouDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this); // Crear un constructor de cuadro de diálogo
        builder.setMessage("Tu compra se realizo correctamente") // Establecer el mensaje del cuadro de diálogo
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() { // Configurar el botón "Ok"
                    public void onClick(DialogInterface dialog, int id) {
                        // Redirigir a MainActivity
                        Intent intent = new Intent(CartActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
        builder.create().show(); // Mostrar el cuadro de diálogo
    }

    // Método para inicializar la lista de elementos en el carrito
    private void initList() {
        if (managmentCart.getListCart().isEmpty()) { // Verificar si el carrito está vacío
            binding.emptyTxt.setVisibility(View.VISIBLE); // Mostrar el texto de carrito vacío
            binding.scrollviewCart.setVisibility(View.GONE); // Ocultar la lista de carrito
        } else {
            binding.emptyTxt.setVisibility(View.GONE); // Ocultar el texto de carrito vacío
            binding.scrollviewCart.setVisibility(View.VISIBLE); // Mostrar la lista de carrito
        }

        // Configurar el diseño del RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.cardView.setLayoutManager(linearLayoutManager);
        // Crear y establecer el adaptador para la lista de carrito
        adapter = new CartAdapter(managmentCart.getListCart(), this, () -> calculateCart());
        binding.cardView.setAdapter(adapter);
    }

    // Método para calcular el total del carrito
    private void calculateCart() {
        double delivery = 8000; // Costo de entrega fijo (en pesos colombianos)

        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) / 100; // Calcular el total
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100; // Calcular el total de los elementos

        // Establecer los valores en las vistas correspondientes
        binding.totalFeeTxt.setText("$" + itemTotal);
        binding.deliveryTxt.setText("$" + delivery);
        binding.totalTxt.setText("$" + total);
    }

    // Método para configurar las variables de la actividad
    private void setVariable() {
        // Configurar el evento onClick para el botón de retroceso
        binding.backBtn.setOnClickListener(v -> finish());
    }
}
