package com.example.semana02.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.semana02.R;
import com.example.semana02.entity.Producto;

import java.util.List;

public class ProductoAdapter extends ArrayAdapter<Producto> {
    private Context context;
    private List<Producto> objects;
    public ProductoAdapter(@NonNull Context context, int resource, @NonNull List<Producto> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.producto_item, parent, false);

        Producto objProducto = objects.get(position);

        TextView txtName = row.findViewById(R.id.idItemProductTitle);
        txtName.setText(String.valueOf(objProducto.getTitle()));


        ImageView imgFoto = row.findViewById(R.id.idItemProductImage);
        Glide.with(context).load(objProducto.getImage()).into(imgFoto);


        TextView txtPrice= row.findViewById(R.id.idItemProductPrice);
        txtPrice.setText("Price : S/. "+ objProducto.getPrice());



        return row;
    }
}
