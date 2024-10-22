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
import com.cibertec.proyecto.adapter.AlumnoAdapter;
import com.cibertec.proyecto.entity.Alumno;
import com.cibertec.proyecto.service.ServiceAlumno;
import com.cibertec.proyecto.util.ConnectionRest;
import com.cibertec.proyecto.util.NewAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlumnoCrudListaActivity extends NewAppCompatActivity {

    EditText txtNombre;
    Button btnConsultar, btnRegistrar;
    ListView lstConsultaAlumno;
    ArrayList<Alumno> data = new ArrayList<Alumno>();
    AlumnoAdapter adaptador;
    ServiceAlumno servicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno_crud_lista);

        txtNombre = findViewById(R.id.txtRegNombre);

        lstConsultaAlumno = findViewById(R.id.lstConsultaAlumno);
        adaptador = new AlumnoAdapter(this, R.layout.activity_alumno_crud_item, data);
        lstConsultaAlumno.setAdapter(adaptador);

        servicio = ConnectionRest.getConnection().create(ServiceAlumno.class);

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
                Intent intent = new Intent(AlumnoCrudListaActivity.this, AlumnoCrudFormularioActivity.class);
                intent.putExtra("var_metodo", "REGISTRAR");
                startActivity(intent);
            }
        });

        lstConsultaAlumno.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //mensajeAlert("Actualiza >> " + position);

                Alumno obj =  data.get(position);

                Intent intent = new Intent(AlumnoCrudListaActivity.this, AlumnoCrudFormularioActivity.class);
                intent.putExtra("var_metodo", "ACTUALIZAR");
                intent.putExtra("var_objeto", obj);
                startActivity(intent);
            }
        });
    }

    public  void consulta(String filtro){

        Call<List<Alumno>> call = servicio.listaPorNombre(filtro);
        call.enqueue(new Callback<List<Alumno>>() {
            @Override
            public void onResponse(Call<List<Alumno>> call, Response<List<Alumno>> response) {

                if(response.isSuccessful()){
                    List<Alumno> lstSalida = response.body();
                    data.clear();
                    data.addAll(lstSalida);
                    adaptador.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Alumno>> call, Throwable t) {

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