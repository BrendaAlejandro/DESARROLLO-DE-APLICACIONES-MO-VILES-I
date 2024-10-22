package com.cibertec.proyecto.vista.registra;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cibertec.proyecto.R;
import com.cibertec.proyecto.entity.Modalidad;
import com.cibertec.proyecto.entity.Pais;
import com.cibertec.proyecto.entity.Revista;
import com.cibertec.proyecto.service.ServiceModalidadRevista;
import com.cibertec.proyecto.service.ServicePais;
import com.cibertec.proyecto.service.ServiceRevista;
import com.cibertec.proyecto.util.ConnectionRest;
import com.cibertec.proyecto.util.FunctionUtil;
import com.cibertec.proyecto.util.NewAppCompatActivity;
import com.cibertec.proyecto.util.ValidacionUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RevistaRegistraActivity extends NewAppCompatActivity {
    //
    //Pais
    Spinner spnPais;
    ArrayAdapter<String> adaptadorPais;
    ArrayList<String> paises = new ArrayList<>();

    //Modalidad
    Spinner spnModalidad;
    ArrayAdapter<String> adaptadorModalidad;
    ArrayList<String> modalidad = new ArrayList<>();

    //Servicio
    ServiceRevista serviceRevista;
    ServicePais servicePais;
    ServiceModalidadRevista serviceModalidad;

    Button btnRegistra;

    EditText txtNombre, txtFrecuencia, txtFechaCreacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revista_registra);
        /*  */
        adaptadorPais = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, paises);
        spnPais = findViewById(R.id.spnRegisRevPais);
        spnPais.setAdapter(adaptadorPais);

        adaptadorModalidad = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, modalidad);
        spnModalidad = findViewById(R.id.spnRegisRevModalidad);
        spnModalidad.setAdapter(adaptadorModalidad);

        servicePais = ConnectionRest.getConnection().create(ServicePais.class);
        serviceRevista = ConnectionRest.getConnection().create(ServiceRevista.class);
        serviceModalidad = ConnectionRest.getConnection().create(ServiceModalidadRevista.class);
        /*  */
        cargaPais();
        cargaModalidad();
        /*  */
        txtNombre = findViewById(R.id.txtRegisRevNombre);
        txtFrecuencia = findViewById(R.id.txtRegisRevFrecuencia);
        txtFechaCreacion = findViewById(R.id.txtRegisRevFechaCreacion);

        btnRegistra = findViewById(R.id.btnRegisRevEnviar);
        /*  */
        txtNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String nombre = editable.toString();
                if (!nombre.matches(ValidacionUtil.NOMBRE)) {
                    txtNombre.setError("Nombre de revista no válido, solo letras");
                }else {
                    txtNombre.setError(null);
                }
            }
        });
        /*  */
        txtFrecuencia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String frecuencia = editable.toString();
                if (!frecuencia.matches(ValidacionUtil.TEXTO)) {
                    txtFrecuencia.setError("Frecuencia de revista no válido, solo letras");
                }else {
                    txtFrecuencia.setError(null);
                }
            }
        });
        /*  */
        txtFechaCreacion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String fechaCrea = editable.toString();
                if (!fechaCrea.matches(ValidacionUtil.FECHA)) {
                    txtFechaCreacion.setError("Fecha de creación no válido (yyyy-mm-dd)");
                }else {
                    txtFechaCreacion.setError(null);
                }
            }
        });
        /*  */
        btnRegistra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validarFormulario()) {
                    return;
                }
                String nombre = txtNombre.getText().toString();
                String frecuencia = txtFrecuencia.getText().toString();
                String fechaCreacion = txtFechaCreacion.getText().toString();
                String idModalidad = spnModalidad.getSelectedItem().toString().split(":")[0];
                String idPais = spnPais.getSelectedItem().toString().split(":")[0];

                Modalidad objModalidad = new Modalidad();
                objModalidad.setIdModalidad(Integer.parseInt(idModalidad.trim()));

                Pais objPais = new Pais();
                objPais.setIdPais(Integer.parseInt(idPais.trim()));


                Revista objRevista = new Revista();
                objRevista.setNombre(nombre);
                objRevista.setFrecuencia(frecuencia);
                objRevista.setFechaCreacion(fechaCreacion);
                objRevista.setModalidad(objModalidad);
                objRevista.setPais(objPais);
                objRevista.setFechaRegistro(FunctionUtil.getFechaActualStringDateTime());
                objRevista.setEstado(1);
                registraValida(objRevista);

            }
        });
    }

    void registraValida(Revista obj){
        Call<List<Revista>> call  = serviceRevista.listaRevistaPorNombreIgual(obj.getNombre());

        call.enqueue(new Callback<List<Revista>>() {
            @Override
            public void onResponse(Call<List<Revista>> call, Response<List<Revista>> response) {
                if (response.isSuccessful()){
                    List<Revista> lstSalida = response.body();
                    if (lstSalida.isEmpty()){
                        registra(obj);
                    }else{
                        mensajeAlert("La Revista " + obj.getNombre() +" ya existe ");
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Revista>> call, Throwable t) {
            }
        });
    }



    /*  */
    void cargaPais(){
        Call<List<Pais>> call = servicePais.listaTodos();
        call.enqueue(new Callback<List<Pais>>() {
            @Override
            public void onResponse(Call<List<Pais>> call, Response<List<Pais>> response) {
                if (response.isSuccessful()){
                    List<Pais> lst =  response.body();
                    for (Pais obj :lst){
                        paises.add( obj.getIdPais() + " : " + obj.getNombre() );
                    }
                    adaptadorPais.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<Pais>> call, Throwable t) {

            }
        });
    }
    /*  */
    void cargaModalidad(){
        Call<List<Modalidad>>  call = serviceModalidad.listaTodos();
        call.enqueue(new Callback<List<Modalidad>>() {
            @Override
            public void onResponse(Call<List<Modalidad>> call, Response<List<Modalidad>> response) {
                if (response.isSuccessful()){
                    List<Modalidad> lst =  response.body();
                    for (Modalidad obj :lst){
                        modalidad.add( obj.getIdModalidad() + " : " + obj.getDescripcion() );
                    }
                    adaptadorModalidad.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<Modalidad>> call, Throwable t) {

            }
        });
    }
    /*  */
    void registra(Revista obj) {
        Call<Revista> call = serviceRevista.registraRevista(obj);
        call.enqueue(new Callback<Revista>() {
            @Override
            public void onResponse(Call<Revista> call, Response<Revista> response) {
                if (response.isSuccessful()) {
                    Revista objSalida = response.body();
                    mensajeAlert("¡  REGISTRO DE REVISTA EXITOSO  !"
                            + " \n >>>> ID >> " + objSalida.getIdRevista()
                            + " \n >>> Nombre >>> " + " \n " + objSalida.getNombre()
                            + " \n >>> Frecuencia >>> " + " \n " + objSalida.getFrecuencia()
                            + " \n >>> Fecha De Creación >>> " + " \n " + objSalida.getFechaCreacion());
                    //Limpiar el formulario
                    limpiarFormulario();
                }
            }

            @Override
            public void onFailure(Call<Revista> call, Throwable t) {

            }
        });
    }
        /*  */
        void mensajeToast(String mensaje){
            Toast toast1 =  Toast.makeText(getApplicationContext(),mensaje, Toast.LENGTH_LONG);
            toast1.show();
        }
        public void mensajeAlert(String msg){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setMessage(msg);
            alertDialog.setCancelable(true);
            alertDialog.show();
        }
        /*  */
        void limpiarFormulario() {
            txtNombre.setText("");
            txtFrecuencia.setText("");
            txtFechaCreacion.setText("");

            if (spnModalidad.getAdapter().getCount() > 0) {
                spnModalidad.setSelection(0);
            }
            if (spnPais.getAdapter().getCount() > 0) {
                spnPais.setSelection(0);
            }

            txtNombre.setError(null);
            txtFrecuencia.setError(null);
            txtFechaCreacion.setError(null);
        }

    /*VALIDACION DE CAMPOS OBLIGATORIOS*/
    private boolean validarFormulario() {
        boolean isValid = true;

        // Validar nombre de revista
        SpannableStringBuilder errorNombre = new SpannableStringBuilder("Este campo es obligatorio");
        errorNombre.setSpan(new ForegroundColorSpan(Color.RED), 0, errorNombre.length(), 0);
        if (txtNombre.getText().toString().trim().isEmpty()) {
            txtNombre.setHint(errorNombre);
            isValid = false;
        } else {
            txtNombre.setHint(null);
        }

        // Validar frecuencia de revista
        SpannableStringBuilder errorFrecuencia = new SpannableStringBuilder("Este campo es obligatorio");
        errorFrecuencia.setSpan(new ForegroundColorSpan(Color.RED), 0, errorFrecuencia.length(), 0);
        if (txtFrecuencia.getText().toString().trim().isEmpty()) {
            txtFrecuencia.setHint(errorFrecuencia);
            isValid = false;
        } else {
            txtFrecuencia.setHint(null);
        }

        // Validar fecha de creacion de revista
        SpannableStringBuilder errorFechaCrea = new SpannableStringBuilder("Este campo es obligatorio");
        errorFechaCrea.setSpan(new ForegroundColorSpan(Color.RED), 0, errorFechaCrea.length(), 0);
        if (txtFechaCreacion.getText().toString().trim().isEmpty()) {
            txtFechaCreacion.setHint(errorFechaCrea);
            isValid = false;
        } else {
            txtFechaCreacion.setHint(null);
        }

        return isValid;
    }
    }



