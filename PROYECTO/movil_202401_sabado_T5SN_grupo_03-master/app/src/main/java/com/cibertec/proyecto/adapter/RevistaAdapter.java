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
import com.cibertec.proyecto.entity.Revista;

import java.util.List;

public class RevistaAdapter extends ArrayAdapter<Revista> {
    private Context context;
    private List<Revista> lista;

    public RevistaAdapter(@NonNull Context context, int resource, @NonNull List<Revista> lista) {
        super(context, resource, lista);
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.activity_revista_consulta_item, parent, false);

        Revista obj = lista.get(position);
        TextView txtId = row.findViewById(R.id.itemIdRevista);
        txtId.setText("ID :"+String.valueOf(obj.getIdRevista()));

        TextView txtNombre = row.findViewById(R.id.itemNombreRevista);
        txtNombre.setText("Nombre :"+obj.getNombre());

        TextView txtFrecuencia = row.findViewById(R.id.itemFrecuenciaRevista);
        txtFrecuencia.setText("Frecuencia :"+obj.getFrecuencia());

        TextView txtFechaCreacion = row.findViewById(R.id.itemFechaCreacionRevista);
        txtFechaCreacion.setText("Fecha Creación :"+obj.getFechaCreacion());

        TextView txtModalidad = row.findViewById(R.id.itemModalidadRevista);
        txtModalidad.setText("Modalidad :"+obj.getModalidad().getDescripcion());

        TextView txtPais = row.findViewById(R.id.itemPaisRevista);
        txtPais.setText("País :"+obj.getPais().getNombre());
        return row;
    }
}
