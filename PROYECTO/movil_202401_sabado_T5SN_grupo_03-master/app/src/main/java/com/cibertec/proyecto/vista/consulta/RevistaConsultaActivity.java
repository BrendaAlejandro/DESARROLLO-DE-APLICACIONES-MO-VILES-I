package com.cibertec.proyecto.vista.consulta;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;

import com.cibertec.proyecto.R;
import com.cibertec.proyecto.adapter.RevistaAdapter;
import com.cibertec.proyecto.entity.Revista;
import com.cibertec.proyecto.service.ServiceRevista;
import com.cibertec.proyecto.util.ConnectionRest;
import com.cibertec.proyecto.util.NewAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RevistaConsultaActivity extends NewAppCompatActivity {

    EditText txtNombre;
    Button btnConsultar;

    ListView lstConsultaRevista;
    ArrayList<Revista> data = new ArrayList<Revista>();
    RevistaAdapter adaptador;

    ServiceRevista servicio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revista_consulta);

        txtNombre = findViewById(R.id.txtRegNombre);

        lstConsultaRevista = findViewById(R.id.lstConsultaRevistas);
        adaptador = new RevistaAdapter(this, R.layout.activity_revista_consulta_item, data);
        lstConsultaRevista.setAdapter(adaptador);

        servicio = ConnectionRest.getConnection().create(ServiceRevista.class);

        btnConsultar = findViewById(R.id.btnLista);
        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filtro = txtNombre.getText().toString();
                consulta(filtro);
            }
        });
    }

    public  void consulta(String filtro){

        Call<List<Revista>> call = servicio.listaPorNombre(filtro);
        call.enqueue(new Callback<List<Revista>>() {
            @Override
            public void onResponse(Call<List<Revista>> call, Response<List<Revista>> response) {

                if(response.isSuccessful()){
                    List<Revista> lstSalida = response.body();
                    data.clear();
                    data.addAll(lstSalida);
                    adaptador.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Revista>> call, Throwable t) {

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