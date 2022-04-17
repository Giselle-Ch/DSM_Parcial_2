package edu.udb.parcial2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AcercaDeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);
    }

    // Redireccionamiento de pantallas del menù
    public void pantallaProductos(View view) {
        Intent i = new Intent(this, ProductosActivity.class);
        startActivity(i);
    }

    public void pantallaComprasRegistro(View view) {
        Intent i = new Intent(this, ComprasActivity.class);
        startActivity(i);
    }
}