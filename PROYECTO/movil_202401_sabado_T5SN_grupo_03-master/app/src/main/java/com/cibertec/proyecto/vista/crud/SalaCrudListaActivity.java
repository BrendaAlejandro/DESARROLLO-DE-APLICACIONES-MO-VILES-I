package com.cibertec.proyecto.vista.crud;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.cibertec.proyecto.R;
import com.cibertec.proyecto.adapter.SalaAdapter;
import com.cibertec.proyecto.entity.Sala;
import com.cibertec.proyecto.service.ServiceSala;
import com.cibertec.proyecto.util.ConnectionRest;
import com.cibertec.proyecto.util.NewAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalaCrudListaActivity  extends NewAppCompatActivity {

    EditText txtNumero;
    Button btnConsultar;
    Button btnRegistrar;

    ListView lstConsultaSala;
    ArrayList<Sala> data = new ArrayList<Sala>();

    SalaAdapter adaptador;
    ServiceSala servicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sala_crud_lista);

        txtNumero = findViewById(R.id.txtRegNumero);

        lstConsultaSala = findViewById(R.id.lstConsultaSala);
        adaptador = new SalaAdapter(this, R.layout.activity_sala_crud_item, data);
        lstConsultaSala.setAdapter(adaptador);

        servicio = ConnectionRest.getConnection().create(ServiceSala.class);
        btnConsultar = findViewById(R.id.btnLista);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filtro = txtNumero.getText().toString().trim();
                if (filtro.isEmpty()) {
                    mensajeAlert("Por favor, ingrese un número de sala.");
                } else {
                    consulta(filtro);
                }
            }
        });
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mensajeAlert("Registrar");
                Intent intent = new Intent(SalaCrudListaActivity.this, SalaCrudFormularioActivity.class);
                intent.putExtra("var_metodo", "REGISTRAR");
                startActivity(intent);
            }
        });
    }

    public void consulta(String filtro) {
        Call<List<Sala>> call = servicio.listaPorNumero(filtro);
        call.enqueue(new Callback<List<Sala>>() {
            @Override
            public void onResponse(Call<List<Sala>> call, Response<List<Sala>> response) {
                if (response.isSuccessful()) {
                    List<Sala> lstSalida = response.body();
                    data.clear();
                    if (lstSalida != null && !lstSalida.isEmpty()) {
                        data.addAll(lstSalida);
                    } else {
                        Toast.makeText(SalaCrudListaActivity.this, "No se encontraron resultados", Toast.LENGTH_SHORT).show();
                    }
                    adaptador.notifyDataSetChanged();
                } else {
                    mensajeAlert("Error en la respuesta del servidor: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Sala>> call, Throwable t) {
                mensajeAlert("Error de conexión: " + t.getMessage());
            }
        });

        lstConsultaSala.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //mensajeAlert("Actualiza >> " + position);

                Sala obj = data.get(position);

                Intent intent = new Intent(SalaCrudListaActivity.this, SalaCrudFormularioActivity.class);
                intent.putExtra("var_metodo", "ACTUALIZAR");
                intent.putExtra("var_objeto", obj);
                startActivity(intent);
            }
        });
    }

    public void mensajeAlert(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(true);
        alertDialog.show();
    }
}
