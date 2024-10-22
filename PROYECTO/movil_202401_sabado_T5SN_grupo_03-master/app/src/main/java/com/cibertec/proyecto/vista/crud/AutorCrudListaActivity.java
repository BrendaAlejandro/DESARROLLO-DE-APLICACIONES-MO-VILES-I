package com.cibertec.proyecto.vista.crud;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.cibertec.proyecto.R;
import com.cibertec.proyecto.adapter.AutorAdapter;
import com.cibertec.proyecto.adapter.LibroAdapter;
import com.cibertec.proyecto.entity.Autor;
import com.cibertec.proyecto.service.ServiceAutor;
import com.cibertec.proyecto.util.NewAppCompatActivity;
import com.cibertec.proyecto.util.ConnectionRest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AutorCrudListaActivity extends NewAppCompatActivity {
    EditText txtNombre;
    Button btnConsultar, btnRegistrar;

    ListView lstConsultaAutor;
    ArrayList<Autor> data = new ArrayList<Autor>();
    AutorAdapter adaptador;

    ServiceAutor servicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autor_crud_lista);

        txtNombre = findViewById(R.id.txtConNombre);

        lstConsultaAutor = findViewById(R.id.lstConsultaAutor);
        adaptador = new AutorAdapter(this, R.layout.activity_autor_crud_item, data);
        lstConsultaAutor.setAdapter(adaptador);

        servicio = ConnectionRest.getConnection().create(ServiceAutor.class);

        btnConsultar = findViewById(R.id.btnLista);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnConsultar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String filtro = txtNombre.getText().toString();
            consulta(filtro);
        }
    });
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mensajeAlert("Registrar");
                Intent intent = new Intent(AutorCrudListaActivity.this, AutorCrudFormularioActivity.class);
                intent.putExtra("var_metodo", "REGISTRAR");
                startActivity(intent);
            }
        });

        lstConsultaAutor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //mensajeAlert("Actualiza >> " + position);

                Autor obj = data.get(position);

                Intent intent = new Intent(AutorCrudListaActivity.this, AutorCrudFormularioActivity.class);
                intent.putExtra("var_metodo", "ACTUALIZAR");
                intent.putExtra("var_objeto", obj);
                startActivity(intent);
            }
        });
    }
    public void consulta(String filtro){

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

    public void mensajeAlert(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(true);
        alertDialog.show();
    }
}
