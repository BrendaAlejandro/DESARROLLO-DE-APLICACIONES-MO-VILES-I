package com.cibertec.proyecto.vista.consulta;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;

import com.cibertec.proyecto.R;
import com.cibertec.proyecto.adapter.UsuarioAdapter;
import com.cibertec.proyecto.entity.Usuario;
import com.cibertec.proyecto.service.ServiceUsuario;
import com.cibertec.proyecto.util.ConnectionRest;
import com.cibertec.proyecto.util.NewAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class UsuarioConsultaActivity extends NewAppCompatActivity {
    EditText txtNombres;
    Button btnConsultar;

    ListView lstConsultaUsuario;
    ArrayList<Usuario> data = new ArrayList<Usuario>();

    UsuarioAdapter adaptador;
    ServiceUsuario servicio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_consulta);
        txtNombres=findViewById(R.id.txtRegNombre);
        lstConsultaUsuario=findViewById(R.id.lstConsultaUsuario);
        adaptador=new UsuarioAdapter(this,R.layout.activity_usuario_consulta_item,data);
        lstConsultaUsuario.setAdapter(adaptador);

        servicio=ConnectionRest.getConnection().create(ServiceUsuario.class);
        btnConsultar=findViewById(R.id.btnLista);
        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filtro=txtNombres.getText().toString();
                consulta(filtro);
            }
        });
    }
    public void consulta(String filtro){
        Call<List<Usuario>> call=servicio.listaPorNombre(filtro);
        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if(response.isSuccessful()){
                    List<Usuario>lstSalida=response.body();
                    data.clear();
                    data.addAll(lstSalida);
                    adaptador.notifyDataSetChanged();
                }

            }
            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                mensajeAlert("Error---> Error en la respuesta"+t.getMessage());
            }
        });
    }
    public void mensajeAlert(String msg){
        androidx.appcompat.app.AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(true);
        alertDialog.show();
    }
}