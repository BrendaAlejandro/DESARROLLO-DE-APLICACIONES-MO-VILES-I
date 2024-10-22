package com.cibertec.proyecto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cibertec.proyecto.R;
import com.cibertec.proyecto.entity.Libro;
import com.cibertec.proyecto.entity.Proveedor;

import java.util.List;

public class ProveedorAdapter extends ArrayAdapter<Proveedor> {
    private Context context;
    private List<Proveedor> lista;

    public ProveedorAdapter(@NonNull Context context, int resource, @NonNull List<Proveedor> lista) {
        super(context, resource, lista);
        this.context = context;
        this.lista = lista;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.activity_proveedor_consulta_item, parent, false);

        Proveedor obj = lista.get(position);

        TextView txtId = row.findViewById(R.id.itemIdProveedor);
        txtId.setText("ID :"+String.valueOf(obj.getIdProveedor()));

        TextView txtTitulo = row.findViewById(R.id.itemRazonSocialProveedor);
        txtTitulo.setText("Razón Social :"+obj.getRazonsocial());

        TextView txtAnio = row.findViewById(R.id.itemRucProveedor);
        txtAnio.setText("RUC :"+obj.getRuc());

        TextView txtSerie = row.findViewById(R.id.itemTelefonoProveedor);
        txtSerie.setText("Telefono :"+obj.getTelefono());

        TextView txtCategoria = row.findViewById(R.id.itemPaisProveedor);
        txtCategoria.setText("País :"+obj.getPais().getNombre());

        TextView txtPais = row.findViewById(R.id.itemTipoProveedor);
        txtPais.setText("Tipo :"+obj.getTipoProveedor().getIdTipoProveedor());
        return row;
    }
}
