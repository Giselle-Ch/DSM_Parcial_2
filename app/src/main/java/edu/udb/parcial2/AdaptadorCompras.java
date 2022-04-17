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

import edu.udb.parcial2.datos.RegistroCompra;

public class AdaptadorCompras extends ArrayAdapter<RegistroCompra> {
    List<RegistroCompra> registros;
    private Activity context;

    public AdaptadorCompras(@NonNull Activity context, @NonNull List<RegistroCompra> registros){
        super(context, R.layout.compras_layout, registros);
        this.context = context;
        this.registros = registros;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent){
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View rowview = null;

        if(view == null)
            rowview = layoutInflater.inflate(R.layout.compras_layout, null);
        else rowview = view;

        TextView tvNombreCompra = rowview.findViewById(R.id.tvNombreCompra);
        TextView tvPrecioCompra = rowview.findViewById(R.id.tvPrecioCompra);

        tvNombreCompra.setText("Mi compra " + registros.get(position).getId());
        tvPrecioCompra.setText("$" + registros.get(position).getPrecio());

        return rowview;
    }

}
