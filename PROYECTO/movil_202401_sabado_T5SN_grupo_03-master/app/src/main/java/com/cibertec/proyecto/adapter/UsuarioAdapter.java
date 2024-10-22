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
import com.cibertec.proyecto.entity.Usuario;

import java.util.List;

public class UsuarioAdapter extends ArrayAdapter<Usuario> {

    private Context context;
    private List<Usuario> lista;

    public UsuarioAdapter(@NonNull Context context, int resource, @NonNull List<Usuario> lista) {
        super(context, resource, lista);
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.activity_usuario_consulta_item, parent, false);

        Usuario obj = lista.get(position);

        TextView txtId = row.findViewById(R.id.itemIdUsuario);
        txtId.setText("ID :"+String.valueOf(obj.getIdUsuario()));

        TextView txtNombres = row.findViewById(R.id.itemNombres);
        txtNombres.setText("Nombres:"+obj.getNombres());

        TextView txtApellidos = row.findViewById(R.id.itemApellidos);
        txtApellidos.setText("Apellidos:"+obj.getApellidos());

        TextView txtTelefono = row.findViewById(R.id.itemTelefono);
        txtTelefono.setText("Telefono:"+obj.getTelefono());

        TextView txtDni = row.findViewById(R.id.itemDni);
        txtDni.setText("Dni:"+obj.getDni());

        TextView txtCorreo = row.findViewById(R.id.itemCorreo);
        txtCorreo.setText("Correo:"+obj.getCorreo());

        TextView txtDireccion = row.findViewById(R.id.itemDireccion);
        txtDireccion.setText("Direccion:"+obj.getDireccion());

        TextView txtFechaNacimiento = row.findViewById(R.id.itemFechaNacimiento);
        txtFechaNacimiento.setText("Fecha Nacimiento :"+obj.getFechaNacimiento());

        TextView txtPais = row.findViewById(R.id.itemPais);
        txtPais.setText("País :"+obj.getPais().getNombre());

        TextView txtModalidad = row.findViewById(R.id.itemModalidad);
        txtModalidad.setText("Modalidad :"+obj.getModalidad().getDescripcion());
        return row;


    }

}