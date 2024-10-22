package com.cibertec.proyecto.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cibertec.proyecto.R;
import com.cibertec.proyecto.entity.Sala;
import java.util.List;

public class SalaAdapter extends ArrayAdapter<Sala> {
    private Context context;
    private List<Sala> lista;

    public SalaAdapter(@NonNull Context context, int resource, @NonNull List<Sala> lista) {
        super(context, resource, lista);
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.activity_sala_consulta_item, parent, false);

        Sala obj = lista.get(position);

        TextView txtId = row.findViewById(R.id.itemIdSala);
        txtId.setText("ID :"+String.valueOf(obj.getIdSala()));

        TextView txtNumero = row.findViewById(R.id.itemNumero);
        txtNumero.setText("Número :"+obj.getNumero());

        TextView txtPiso = row.findViewById(R.id.itemPiso);
        txtPiso.setText("Piso :"+obj.getPiso());

        TextView txtNumAlumnos = row.findViewById(R.id.itemNumAlumnos);
        txtNumAlumnos.setText("Número de Alumnos :"+obj.getNumAlumnos());

        TextView txtRecursos = row.findViewById(R.id.itemRecursos);
        txtRecursos.setText("Recursos :"+obj.getRecursos());

        TextView itemFechaRegistro = row.findViewById(R.id.itemFechaRegistro);
        itemFechaRegistro.setText("Fecha Registro :"+obj.getFechaRegistro());



        TextView txtSede = row.findViewById(R.id.itemSede);
        txtSede.setText("Sede :"+obj.getSede().getNombre());

        TextView txtModalidad = row.findViewById(R.id.itemModalidad);
        txtModalidad.setText("Modalidad :"+obj.getModalidad().getDescripcion());



        ;
        return row;
    }
}