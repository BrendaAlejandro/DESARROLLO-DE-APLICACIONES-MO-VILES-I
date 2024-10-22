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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cibertec.proyecto.R;
import com.cibertec.proyecto.adapter.ProveedorAdapter;
import com.cibertec.proyecto.entity.Proveedor;
import com.cibertec.proyecto.service.ServiceProveedor;
import com.cibertec.proyecto.util.ConnectionRest;
import com.cibertec.proyecto.util.NewAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProveedorCrudListaActivity extends NewAppCompatActivity {


    EditText txtRazonSoc;
    Button btnConsultar;
    Button btnRegistrar;

    ListView lstConsultaProveedor;
    ArrayList<Proveedor> data = new ArrayList<Proveedor>();

    ProveedorAdapter adaptador;
    ServiceProveedor servicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proveedor_crud_lista);

        txtRazonSoc = findViewById(R.id.txtRegRazonSocial);

        lstConsultaProveedor = findViewById(R.id.lstConsultaProveedores);
        adaptador = new ProveedorAdapter(this, R.layout.activity_proveedor_crud_item, data);
        lstConsultaProveedor.setAdapter(adaptador);

        servicio = ConnectionRest.getConnection().create(ServiceProveedor.class);
        btnConsultar = findViewById(R.id.btnLista);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filtro = txtRazonSoc.getText().toString().trim();
                if (filtro.isEmpty()) {
                    mensajeAlert("Por favor, ingrese la Razon Social del Proveedor.");
                } else {
                    consulta(filtro);
                }
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mensajeAlert("Registrar");
                Intent intent = new Intent(ProveedorCrudListaActivity.this, ProveedorCrudFormularioActivity.class);
                intent.putExtra("var_metodo", "REGISTRAR");
                startActivity(intent);
            }
        });


        lstConsultaProveedor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //mensajeAlert("Actualiza >> " + position);
                Proveedor obj = data.get(position);

                Intent intent = new Intent(ProveedorCrudListaActivity.this, ProveedorCrudFormularioActivity.class);
                intent.putExtra("var_metodo", "ACTUALIZAR");
                intent.putExtra("var_objeto", obj);
                startActivity(intent);
            }
        });
    }
    public void consulta(String filtro) {
        Call<List<Proveedor>> call = servicio.listaPorRazonSocial(filtro);
        call.enqueue(new Callback<List<Proveedor>>() {
            @Override
            public void onResponse(Call<List<Proveedor>> call, Response<List<Proveedor>> response) {

                if (response.isSuccessful()) {
                    List<Proveedor> lstSalida = response.body();
                    data.clear();
                    data.addAll(lstSalida);
                    adaptador.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<Proveedor>> call, Throwable t) {

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
