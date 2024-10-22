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
import com.cibertec.proyecto.entity.Autor;

import java.util.List;

public class AutorAdapter extends ArrayAdapter<Autor> {
    private Context context;
    private List<Autor> lista;

    public AutorAdapter(@NonNull Context context, int resource, @NonNull List<Autor> lista) {
        super(context, resource, lista);
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.activity_autor_consulta_item, parent, false);

        Autor obj = lista.get(position);

        TextView txtId = row.findViewById(R.id.itemIdAutor);
        txtId.setText("ID :" + String.valueOf(obj.getIdAutor()));

        TextView txtNombre = row.findViewById(R.id.itemNombreAutor);
        txtNombre.setText("Nombre :" + obj.getNombres());

        TextView txtApellido = row.findViewById(R.id.itemApellidoAutor);
        txtApellido.setText("Apellidos :" + obj.getApellidos());

        TextView txtCorreo = row.findViewById(R.id.itemCorreoAutor);
        txtCorreo.setText("Correo :" + obj.getCorreo());

        TextView txtFecNacimiento = row.findViewById(R.id.itemFechaNacimientoAutor);
        txtFecNacimiento.setText("Fecha de Nacimiento :" + obj.getFechaNacimiento());

        TextView txtGrado = row.findViewById(R.id.itemGradoAutor);
        // Verificar si el objeto Grado es null
        if (obj.getGrado() != null) {
            txtGrado.setText("Grado :" + obj.getGrado().getDescripcion());
        } else {
            txtGrado.setText("Grado : No disponible");
        }

        TextView txtPais = row.findViewById(R.id.itemPaisAutor);
        // Verificar si el objeto Pais es null
        if (obj.getPais() != null) {
            txtPais.setText("País :" + obj.getPais().getNombre());
        } else {
            txtPais.setText("País : No disponible");
        }

        return row;
    }
}

