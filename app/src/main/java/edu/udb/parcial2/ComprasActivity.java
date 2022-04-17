package edu.udb.parcial2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.udb.parcial2.datos.RegistroCompra;

public class ComprasActivity extends AppCompatActivity {

    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference refRegistroCompras = database.getReference("registroCompras");

    List<RegistroCompra> registros;
    ListView listaCompras;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compras);

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
        listaCompras = findViewById(R.id.ListaCompras);

        // Mostrar lista del regisro de compras por el usuario
        registros = new ArrayList<>();

        refRegistroCompras.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                registros.removeAll(registros);
                for(DataSnapshot dato: snapshot.getChildren())
                {
                    RegistroCompra registro = dato.getValue(RegistroCompra.class);
                    registro.setKey(dato.getKey());
                    registros.add(registro);
                }
                AdaptadorCompras adapter = new AdaptadorCompras(ComprasActivity.this, registros);
                listaCompras.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Cuando se realize un LongClic se redireccionará a una pantalla con la lista de productos comprados
        listaCompras.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                String codigo;

                codigo = String.valueOf(registros.get(position).getId());

//                Toast.makeText(ComprasActivity.this,
//                        "Elemento " + codigo,Toast.LENGTH_SHORT).show();

                Intent cambio = new Intent(ComprasActivity.this, RegistroCompraActivity.class);
                cambio.putExtra("codigo", codigo);
                startActivity(cambio);
                return true;
            }
        });
    }
}