package edu.udb.parcial2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.udb.parcial2.datos.Producto;
import edu.udb.parcial2.datos.RegistroCompra;

public class RegistroCompraActivity extends AppCompatActivity {

    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference refProductosCompra = database.getReference("compras");

    String codigo = "";     // Obtención del id de la compra seleccionada
    double totalCompra = 0;     // Para calculo del total de la compra

    List<Producto> productos;
    ListView listaProductosCompra;
    private TextView txtTotal, txtNombreCompra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_compra);

        inicializar();
    }

    //Redireccionamiento a pantalla del registro de compras
    public void pantallaComprasRegistro(View view) {
        Intent i = new Intent(this, ComprasActivity.class);
        startActivity(i);
    }

    public void inicializar(){
        listaProductosCompra = findViewById(R.id.ListaProductosCompra);
        txtTotal = findViewById(R.id.txtTotalCompra);
        txtNombreCompra = findViewById(R.id.txtNombreRegistro);

        // Extracción del id seleccionado en la pantalla de compras
        Bundle datos = getIntent().getExtras();
        codigo = datos.getString("codigo");

//        Toast.makeText(RegistroCompraActivity.this,
//                "Elemento " + codigo,Toast.LENGTH_SHORT).show();

        txtNombreCompra.setText("Mi compra " + codigo);

        //Muestra de productos de la compra
        productos = new ArrayList<>();

        refProductosCompra.child("no" + codigo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productos.removeAll(productos);
                for(DataSnapshot dato: snapshot.getChildren())
                {
                    Producto producto = dato.getValue(Producto.class);
                    producto.setKey(dato.getKey());
                    totalCompra += producto.getPrecio();
                    productos.add(producto);
                }

                AdaptadorProducto adapter = new AdaptadorProducto(RegistroCompraActivity.this, productos);
                listaProductosCompra.setAdapter(adapter);

                txtTotal.setText("Total: $" + totalCompra);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}