package edu.udb.parcial2;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import edu.udb.parcial2.datos.Producto;


public class AdaptadorProducto extends ArrayAdapter<Producto> {
    List<Producto> productos;
    private Activity context;

    public AdaptadorProducto(@NonNull Activity context, @NonNull List<Producto> productos){
        super(context, R.layout.productos_layout, productos);
        this.context = context;
        this.productos = productos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View rowview = null;

        if(view == null)
            rowview = layoutInflater.inflate(R.layout.productos_layout, null);
        else rowview = view;

        TextView tvNombre = rowview.findViewById(R.id.tvNombre);
        TextView tvDetalle = rowview.findViewById(R.id.tvDetalle);
        TextView tvMarca = rowview.findViewById(R.id.tvMarca);
        TextView tvPrecio = rowview.findViewById(R.id.tvPrecio);

        tvNombre.setText("" + productos.get(position).getNombre());
        tvDetalle.setText("" + productos.get(position).getDetalle());
        tvMarca.setText("Marca " + productos.get(position).getMarca());
        tvPrecio.setText("$" + productos.get(position).getPrecio());

        return rowview;
    }
}
