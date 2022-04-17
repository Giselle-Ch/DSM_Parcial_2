package edu.udb.parcial2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.udb.parcial2.datos.Producto;
import edu.udb.parcial2.datos.RegistroCompra;

public class ConfirmarCompraActivity extends AppCompatActivity {

    public static FirebaseDatabase database = FirebaseDatabase.getInstance();

    public static DatabaseReference refProductosCompra = database.getReference("compras");
    public static DatabaseReference refTotalesPrecioCompras = database.getReference();

    public int c;               //Contador de la compra a realizar
    double totalCompra = 0;     // Cálculo del total de la compra

    List<Producto> productos;
    ListView listaProductos;
    private TextView txtTotal;
    private Button btnConfirmarCompra;
    private ImageButton btnEliminarCompra;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_compra);

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
    public void pantallaProductos(View view) {
        Intent i = new Intent(this, ProductosActivity.class);
        startActivity(i);
    }

    public void pantallaAcercaDe(View view) {
        Intent i = new Intent(this, AcercaDeActivity.class);
        startActivity(i);
    }

    public void inicializar(){
        listaProductos = findViewById(R.id.ListaProductos);
        txtTotal = findViewById(R.id.txtTotalCompra);
        btnConfirmarCompra = findViewById(R.id.btnConfirmarCompra);
        btnEliminarCompra = findViewById(R.id.btnEliminarCompra);

//        Toast.makeText(ConfirmarCompraActivity.this,
//                "Elemento " + c,Toast.LENGTH_SHORT).show();


        // Muestra de productos en la compra
        productos = new ArrayList<>();

        refProductosCompra.child("no" + c).addValueEventListener(new ValueEventListener() {
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

                AdaptadorProducto adapter = new AdaptadorProducto(ConfirmarCompraActivity.this, productos);
                listaProductos.setAdapter(adapter);

                txtTotal.setText("Total: $" + totalCompra);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Si se mantiene un clic sobre un producto este se elimina
        listaProductos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                //Cuadro de dialogo para preguntar al usuario
                totalCompra = 0;
                txtTotal.setText("Total: $" + totalCompra);

                AlertDialog.Builder preguntar = new AlertDialog.Builder(ConfirmarCompraActivity.this);
                preguntar.setMessage("¿Estas seguro de eliminar el producto de la compra?")
                        .setTitle("Eliminar producto");

                preguntar.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ConfirmarCompraActivity.refProductosCompra.child("no" + c)
                                .child(productos.get(position).getKey()).removeValue();

                        Toast.makeText(ConfirmarCompraActivity.this,
                                "El elemento ha sido eliminado de la compra",Toast.LENGTH_SHORT).show();
                    }
                });

                preguntar.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                preguntar.show();
                return true;
            }
        });

        //Eliminar todo el registro de la compra
        btnEliminarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refProductosCompra.child("no" + c).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ConfirmarCompraActivity.this,
                                "Se ha eliminado el registro de la compra ",Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(ConfirmarCompraActivity.this, ProductosActivity.class);
                        startActivity(i);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ConfirmarCompraActivity.this,
                                "No se ha podido eliminar el registro de la compra",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        //Confirmar la compra
        btnConfirmarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistroCompra rc = new RegistroCompra(c,totalCompra);
                refTotalesPrecioCompras.child("registroCompras").child("no" + c).setValue(rc);

                c++;

                // Aumentando el contador para el registro de una nueva compra
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(ConfirmarCompraActivity.this, "contador", null, 1);
                SQLiteDatabase db = admin.getWritableDatabase();
                ContentValues registro = new ContentValues();
                registro.put("contador", c);
                int cant = db.update("compras", registro, "id=1", null);
                db.close();
                if(cant == 1)
                    Toast.makeText(ConfirmarCompraActivity.this,
                            "Tu compra se ha realizado con éxito",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(ConfirmarCompraActivity.this,
                            "No se ha podido",Toast.LENGTH_SHORT).show();


                Intent i = new Intent(ConfirmarCompraActivity.this, ProductosActivity.class);
                startActivity(i);
            }
        });
    }
}