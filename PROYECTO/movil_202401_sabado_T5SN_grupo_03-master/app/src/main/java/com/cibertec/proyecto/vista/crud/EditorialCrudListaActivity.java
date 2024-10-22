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

import com.cibertec.proyecto.R;
import com.cibertec.proyecto.adapter.EditorialAdapter;
import com.cibertec.proyecto.entity.Editorial;
import com.cibertec.proyecto.entity.Revista;
import com.cibertec.proyecto.service.ServiceEditorial;
import com.cibertec.proyecto.util.ConnectionRest;
import com.cibertec.proyecto.util.NewAppCompatActivity;
import com.cibertec.proyecto.vista.registra.EditorialRegistraActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditorialCrudListaActivity extends NewAppCompatActivity {

    EditText txtRazonSocial;
    Button btnConsultar , btnRegistrar;

    ListView lstConsultaEditorial;
    ArrayList<Editorial> data = new ArrayList<Editorial>();
    EditorialAdapter adaptador;

    ServiceEditorial servicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editorial_crud_lista);

        txtRazonSocial = findViewById(R.id.txtRegRazonSocialLista);

        lstConsultaEditorial = findViewById(R.id.lstConsultaEditorialLista);
        adaptador = new EditorialAdapter(this, R.layout.activity_editorial_crud_item, data);
        lstConsultaEditorial.setAdapter(adaptador);

        servicio = ConnectionRest.getConnection().create(ServiceEditorial.class);

        btnConsultar = findViewById(R.id.btnLista);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filtro = txtRazonSocial.getText().toString().trim();
                if (filtro.isEmpty()) {
                    mensajeAlert("Por favor, ingrese la Razon Social del Editorial.");
                } else {
                    consulta(filtro);
                }
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mensajeAlert("Registrar");
                Intent intent = new Intent(EditorialCrudListaActivity.this, EditorialCrudFormularioActivity.class);
                intent.putExtra("var_metodo", "REGISTRAR");
                startActivity(intent);
            }
        });

        lstConsultaEditorial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //mensajeAlert("Actualiza >> " + position);
                Editorial obj = data.get(position);

                Intent intent = new Intent(EditorialCrudListaActivity.this, EditorialCrudFormularioActivity.class);
                intent.putExtra("var_metodo", "ACTUALIZAR");
                intent.putExtra("var_objeto", obj);
                startActivity(intent);
            }
        });
    }

    public void consulta(String filtro) {
        Call<List<Editorial>> call = servicio.listaPorNombre(filtro);
        call.enqueue(new Callback<List<Editorial>>() {
            @Override
            public void onResponse(Call<List<Editorial>> call, Response<List<Editorial>> response) {

                if (response.isSuccessful()) {
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