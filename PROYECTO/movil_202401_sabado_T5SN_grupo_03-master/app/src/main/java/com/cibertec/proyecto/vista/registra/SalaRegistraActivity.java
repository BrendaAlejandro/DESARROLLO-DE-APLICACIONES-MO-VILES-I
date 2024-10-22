package com.cibertec.proyecto.vista.registra;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cibertec.proyecto.R;
import com.cibertec.proyecto.entity.Modalidad;
import com.cibertec.proyecto.entity.Sala;
import com.cibertec.proyecto.entity.Sede;
import com.cibertec.proyecto.service.ServiceModalidadSala;
import com.cibertec.proyecto.service.ServiceSala;
import com.cibertec.proyecto.service.ServiceSedeSala;
import com.cibertec.proyecto.util.ConnectionRest;
import com.cibertec.proyecto.util.FunctionUtil;
import com.cibertec.proyecto.util.NewAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalaRegistraActivity extends NewAppCompatActivity {
    // Pais
    Spinner spnSede;
    ArrayAdapter<String> adaptadorSede;
    ArrayList<String> sedes = new ArrayList<>();

    // Modalidad
    Spinner spnModalidad;
    ArrayAdapter<String> adaptadorModalidad;
    ArrayList<String> modalidades = new ArrayList<>();

    // Servicio
    ServiceSala serviceSala;
    ServiceSedeSala serviceSede;
    ServiceModalidadSala serviceModalidadSala;

    Button btnRegistra;

    EditText txtNumero, txtPiso, txtNumAlumnos, txtRecursos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sala_registra);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.RegSala), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        adaptadorSede = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, sedes);
        spnSede = findViewById(R.id.spnRegSalaSede);
        spnSede.setAdapter(adaptadorSede);

        adaptadorModalidad = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, modalidades);
        spnModalidad = findViewById(R.id.spnRegSalaModalidad);
        spnModalidad.setAdapter(adaptadorModalidad);

        serviceSede = ConnectionRest.getConnection().create(ServiceSedeSala.class);
        serviceSala = ConnectionRest.getConnection().create(ServiceSala.class);
        serviceModalidadSala = ConnectionRest.getConnection().create(ServiceModalidadSala.class);

        cargaSede();
        cargaModalidad();

        txtNumero = findViewById(R.id.txtRegSalaNumero);
        txtPiso = findViewById(R.id.txtRegSalaPiso);
        txtNumAlumnos = findViewById(R.id.txtRegSalaNumAlumnos);
        txtRecursos = findViewById(R.id.txtRegSalaRecursos);

        btnRegistra = findViewById(R.id.btnRegSalaEnviar);

        btnRegistra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String numero = txtNumero.getText().toString();
                String piso = txtPiso.getText().toString();
                String numAlumnos = txtNumAlumnos.getText().toString();
                String recursos = txtRecursos.getText().toString();
                String idSede = spnSede.getSelectedItem().toString().split(":")[0];
                String idModalidad = spnModalidad.getSelectedItem().toString().split(":")[0];

                Sede objSede = new Sede();
                objSede.setIdSede(Integer.parseInt(idSede.trim()));

                Modalidad objModalidad = new Modalidad();
                objModalidad.setIdModalidad(Integer.parseInt(idModalidad.trim()));


                if (!numero.matches("[A-Z]{1}[0-9]{3}")) {
                    mensajeAlert("Ingrese el número con una letra mayúscula seguida de 3 números");
                } else if (!piso.matches("[1-9]|10")) {
                    mensajeAlert("Ingrese un número de piso entre 1 y 10");
                } else if (!numAlumnos.matches("[1-9][0-9]?|10[0-5]")) {
                    mensajeAlert("Ingrese un número de alumnos entre 1 y 105");
                }  else if (!recursos.matches("[\\p{L}\\p{M} ,]{3,60}")) {
                    mensajeAlert("Ingrese los recursos con longitud entre 3 y 60 caracteres, permitiendo letras, espacios en blanco y comas");
                }
                else{

                    Sala objSala = new Sala();
                    objSala.setNumero(numero);
                    objSala.setPiso(Integer.parseInt(piso));
                    objSala.setNumAlumnos(Integer.parseInt(numAlumnos));
                    objSala.setRecursos(recursos);
                    objSala.setFechaRegistro(FunctionUtil.getFechaActualStringDateTime());
                    objSala.setEstado(1);
                    objSala.setSede(objSede);
                    objSala.setModalidad(objModalidad);
                    registraValida(objSala);

                }

            }
        });
    }

    void registraValida(Sala obj) {

        Call<List<Sala>> call = serviceSala.listaPorNumeroIgual(obj.getNumero());
        call.enqueue(new Callback<List<Sala>>() {
            @Override
            public void onResponse(Call<List<Sala>> call, Response<List<Sala>> response) {
                if (response.isSuccessful()){
                    List<Sala> lstSalida = response.body();
                    if (lstSalida.isEmpty()){
                        registra(obj);
                    }else {
                        mensajeAlert("La sala "+obj.getNumero() + " ya existe ");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Sala>> call, Throwable t) {

            }
        });
    }

    void cargaSede() {
        Call<List<Sede>> call = serviceSede.listaTodos();
        call.enqueue(new Callback<List<Sede>>() {
            @Override
            public void onResponse(Call<List<Sede>> call, Response<List<Sede>> response) {
                if (response.isSuccessful()) {
                    List<Sede> lst = response.body();
                    for (Sede obj : lst) {
                        sedes.add(obj.getIdSede() + " : " + obj.getNombre());
                    }
                    adaptadorSede.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Sede>> call, Throwable t) {
            }
        });
    }

    void cargaModalidad() {
        Call<List<Modalidad>> call = serviceModalidadSala.listaTodos();
        call.enqueue(new Callback<List<Modalidad>>() {
            @Override
            public void onResponse(Call<List<Modalidad>> call, Response<List<Modalidad>> response) {
                if (response.isSuccessful()) {
                    List<Modalidad> lst = response.body();
                    for (Modalidad obj : lst) {
                        modalidades.add(obj.getIdModalidad() + " : " + obj.getDescripcion());
                    }
                    adaptadorModalidad.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Modalidad>> call, Throwable t) {
            }
        });
    }

    void registra(Sala obj) {
        Call<Sala> call = serviceSala.Registra(obj);
        call.enqueue(new Callback<Sala>() {
            @Override
            public void onResponse(Call<Sala> call, Response<Sala> response) {
                if (response.isSuccessful()) {
                    Sala objSalida = response.body();
                    mensajeAlert("Registro de Sala exitoso: " +
                            "\n >>>> ID >> " + objSalida.getIdSala() +
                            "\n >>> Número >>> " + objSalida.getNumero());
                }
            }

            @Override
            public void onFailure(Call<Sala> call, Throwable t) {
            }
        });
    }

    void mensajeToast(String mensaje) {
        Toast toast1 = Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG);
        toast1.show();
    }

    public void mensajeAlert(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(true);
        alertDialog.show();
    }
}