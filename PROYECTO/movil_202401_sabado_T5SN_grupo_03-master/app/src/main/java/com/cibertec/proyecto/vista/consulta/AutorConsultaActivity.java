package com.cibertec.proyecto.vista.consulta;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.cibertec.proyecto.R;
import com.cibertec.proyecto.adapter.AutorAdapter;
import com.cibertec.proyecto.entity.Autor;
import com.cibertec.proyecto.service.ServiceAutor;
import com.cibertec.proyecto.util.ConnectionRest;
import com.cibertec.proyecto.util.NewAppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AutorConsultaActivity extends NewAppCompatActivity {

    EditText txtNombres;
    Button btnConsultar;

    ListView lstConsultaAutor;
    ArrayList<Autor> data = new ArrayList<Autor>();
    AutorAdapter adaptador;

    ServiceAutor servicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autor_consulta);

        txtNombres = findViewById(R.id.txtRegNombre);

        lstConsultaAutor = findViewById(R.id.lstConsultaAutor);
        adaptador = new AutorAdapter(this, R.layout.activity_libro_item, data);
        lstConsultaAutor.setAdapter(adaptador);

        servicio = ConnectionRest.getConnection().create(ServiceAutor.class);

        btnConsultar = findViewById(R.id.btnLista);
        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filtro = txtNombres.getText().toString();
                consulta(filtro);
            }
        });
    }

    public  void consulta(String filtro){

        Call<List<Autor>> call = servicio.listaporNombre(filtro);
        call.enqueue(new Callback<List<Autor>>() {
            @Override
            public void onResponse(Call<List<Autor>> call, Response<List<Autor>> response) {

                if(response.isSuccessful()){
                    List<Autor> lstSalida = response.body();
                    data.clear();
                    data.addAll(lstSalida);
                    adaptador.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Autor>> call, Throwable t) {

                mensajeAlert("ERROR -> Error en la respuesta" + t.getMessage());
            }
        });
    }

    public  void mensajeAlert(String msg){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(true);
        alertDialog.show();
    }

}