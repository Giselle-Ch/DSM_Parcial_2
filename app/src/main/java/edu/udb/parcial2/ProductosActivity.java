package edu.udb.parcial2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.udb.parcial2.datos.Producto;

public class ProductosActivity extends AppCompatActivity {

    public static FirebaseDatabase database = FirebaseDatabase.getInstance();

    public static DatabaseReference refProductos = database.getReference("medicamentos");
    public static DatabaseReference refCompras = database.getReference();

   public int c;       // Contador de las compras y registro de ella

    List<Producto> productos;
    ListView listaProductos;
    private Button btnTerminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        //Obtención del contador
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "contador", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        Cursor fila = db.rawQuery("select contador from compras where id=1", null);
        if(fila.moveToFirst()){
            c = Integer.parseInt(fila.getString(0));
        }
        db.close();

        inicializar();
    }

    // Redireccionamiento de pantallas del menù
    public void pantallaComprasRegistro(View view) {
        Intent i = new Intent(this, ComprasActivity.class);
        startActivity(i);
    }

    public void pantallaAcercaDe(View view) {
        Intent i = new Intent(this, AcercaDeActivity.class);
        startActivity(i);
    }


    private void inicializar(){
        listaProductos = findViewById(R.id.ListaProductos);
        btnTerminar = findViewById(R.id.btnTerminarCompra);

//        Toast.makeText(ProductosActivity.this,
//                        "contador  " + c,Toast.LENGTH_SHORT).show();


        // Cuando se realice un LongClic se agregará el producto a la compra
        listaProductos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {

                Map<String, Object> datoCompra = new HashMap<>();

                datoCompra.put("nombre", productos.get(position).getNombre());
                datoCompra.put("detalle", productos.get(position).getDetalle());
                datoCompra.put("marca", productos.get(position).getMarca());
                datoCompra.put("precio", productos.get(position).getPrecio());

                refCompras.child("compras").child("no" + c).push().setValue(datoCompra);

                Toast.makeText(ProductosActivity.this,
                        "Se ha agregado el producto al carrito",Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        // Mostrar lista de productos ingresados en la base
        productos = new ArrayList<>();

        refProductos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productos.removeAll(productos);
                for(DataSnapshot dato: snapshot.getChildren())
                {
                    Producto producto = dato.getValue(Producto.class);
                    producto.setKey(dato.getKey());
                    productos.add(producto);
                }
                AdaptadorProducto adapter = new AdaptadorProducto(ProductosActivity.this, productos);
                listaProductos.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Redireccionamiento a la pantalla de confirmar compra
        btnTerminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProductosActivity.this, ConfirmarCompraActivity.class);
                startActivity(i);
            }
        });
    }
}