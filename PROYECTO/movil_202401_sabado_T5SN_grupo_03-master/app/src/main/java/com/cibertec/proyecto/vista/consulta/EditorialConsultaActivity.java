package com.cibertec.proyecto.vista.consulta;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;

import com.cibertec.proyecto.R;
import com.cibertec.proyecto.adapter.EditorialAdapter;
import com.cibertec.proyecto.adapter.LibroAdapter;
import com.cibertec.proyecto.entity.Editorial;
import com.cibertec.proyecto.entity.Libro;
import com.cibertec.proyecto.service.ServiceEditorial;
import com.cibertec.proyecto.service.ServiceLibro;
import com.cibertec.proyecto.util.ConnectionRest;
import com.cibertec.proyecto.util.NewAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditorialConsultaActivity extends NewAppCompatActivity {

    EditText txtRazonSocial;
    Button btnConsultar;

    ListView lstConsultaEditorial;
    ArrayList<Editorial> data = new ArrayList<Editorial>();
    EditorialAdapter adaptador;

    ServiceEditorial servicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editorial_consulta);

        txtRazonSocial = findViewById(R.id.txtRegRazonSocial);

        lstConsultaEditorial = findViewById(R.id.lstConsultaEditorial);
        adaptador = new EditorialAdapter(this, R.layout.activity_editorial_consulta_item, data);
        lstConsultaEditorial.setAdapter(adaptador);

        servicio = ConnectionRest.getConnection().create(ServiceEditorial.class);

        btnConsultar = findViewById(R.id.btnLista);
        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filtro = txtRazonSocial.getText().toString();
                consulta(filtro);
            }
        });
    }

    public  void consulta(String filtro){

        Call<List<Editorial>> call = servicio.listaPorNombre(filtro);
        call.enqueue(new Callback<List<Editorial>>() {
            @Override
            public void onResponse(Call<List<Editorial>> call, Response<List<Editorial>> response) {

                if(response.isSuccessful()){
                    List<Editorial> lstSalida = response.body();
                    data.clear();
                    data.addAll(lstSalida);
                    adaptador.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Editorial>> call, Throwable t) {
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


