package com.example.deliciapp.Activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.deliciapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Mapa extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker mMarker;
    private static final int RESULT_PERMISSION_GPS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Habilita el borde a borde para la actividad
        setContentView(R.layout.activity_mapa); // Establece el diseño de la actividad
        // Aplica el ajuste de margen a la vista raíz para evitar que se superponga con las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Obtiene una referencia al fragmento del mapa y registra un listener para notificar cuando el mapa esté listo
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE); // Establece el tipo de mapa a satélite
        LatLng sydney = new LatLng(5.614775, -73.819571); // Ubicación de Chiquinquirá
        mMarker = mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Chiquinquirá")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))); // Agrega un marcador rojo en Chiquinquirá
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16)); // Mueve la cámara del mapa a Chiquinquirá

        // Solicita permisos de ubicación si no están otorgados
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    RESULT_PERMISSION_GPS);
        } else {
            mMap.setMyLocationEnabled(true); // Habilita la capa de ubicación del usuario si se otorgan permisos
        }

        // Agrega un marcador azul en la Sede Duitama
        LatLng newYork = new LatLng(5.8260754, -73.0714114);
        mMap.addMarker(new MarkerOptions()
                .position(newYork)
                .title("Sede Duitama")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16)); // Mueve la cámara del mapa a Chiquinquirá

        // Agrega un listener para clics en el mapa
        mMap.setOnMapClickListener(latLng -> {
            // Mueve el marcador existente a la nueva ubicación si existe, de lo contrario, crea uno nuevo
            if (mMarker != null) {
                mMarker.setPosition(latLng);
            } else {
                mMarker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Selected Location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
            // Muestra un Toast con las coordenadas de la ubicación seleccionada
            Toast.makeText(Mapa.this, "Location: " + latLng.latitude + ", " + latLng.longitude, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Verifica si el código de solicitud es para permisos de ubicación
        if (requestCode == RESULT_PERMISSION_GPS) {
            // Verifica si se otorgaron permisos
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Verifica si se otorgaron permisos de ubicación fina y gruesa
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // Habilita la capa de ubicación del usuario si se otorgan permisos
                    mMap.setMyLocationEnabled(true);
                }
            }
        }
    }
}
