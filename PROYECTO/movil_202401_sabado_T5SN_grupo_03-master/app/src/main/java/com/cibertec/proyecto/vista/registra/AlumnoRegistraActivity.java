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
import android.widget.TextView;
import android.widget.Toast;

import com.cibertec.proyecto.R;
import com.cibertec.proyecto.entity.Alumno;
import com.cibertec.proyecto.entity.Modalidad;
import com.cibertec.proyecto.entity.Pais;
import com.cibertec.proyecto.service.ServiceAlumno;
import com.cibertec.proyecto.service.ServiceModalidadAlumno;
import com.cibertec.proyecto.service.ServicePais;
import com.cibertec.proyecto.util.ConnectionRest;
import com.cibertec.proyecto.util.FunctionUtil;
import com.cibertec.proyecto.util.NewAppCompatActivity;
import com.cibertec.proyecto.util.ValidacionUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AlumnoRegistraActivity extends NewAppCompatActivity {

    //Pais
    Spinner spnPais;
    ArrayAdapter<String> adaptadorPais;
    ArrayList<String> paises = new ArrayList<>();

    //Modalidad
    Spinner spnModalidad;
    ArrayAdapter<String> adaptadorModalidad;
    ArrayList<String> modalidad = new ArrayList<>();

    //Servicio
    ServiceAlumno serviceAlumno;
    ServicePais servicePais;
    ServiceModalidadAlumno serviceModalidad;

    Button btnRegistra;

    EditText txtNombres, txtApellidos, txtTelefono, txtDni, txtCorreo, txtDireccion, txtFechaNacimiento;

    public static final String TELEFONO = "[0-9]{9}";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno_registra);

        adaptadorPais = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, paises);
        spnPais = findViewById(R.id.spnRegAlumPais);
        spnPais.setAdapter(adaptadorPais);

        adaptadorModalidad = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, modalidad);
        spnModalidad = findViewById(R.id.spnRegAlumModalidad);
        spnModalidad.setAdapter(adaptadorModalidad);

        servicePais = ConnectionRest.getConnection().create(ServicePais.class);
        serviceAlumno = ConnectionRest.getConnection().create(ServiceAlumno.class);
        serviceModalidad = ConnectionRest.getConnection().create(ServiceModalidadAlumno.class);

        cargaPais();
        cargaModalidad();

        txtNombres = findViewById(R.id.txtRegAlumNombre);
        txtApellidos = findViewById(R.id.txtRegAlumApellidos);
        txtTelefono = findViewById(R.id.txtRegAlumTelefono);
        txtDni = findViewById(R.id.txtRegAlumDni);
        txtCorreo = findViewById(R.id.txtRegAlumCorreo);
        txtDireccion = findViewById(R.id.txtRegAlumDireccion);
        txtFechaNacimiento = findViewById(R.id.txtRegAlumFechaNaci);

        btnRegistra = findViewById(R.id.btnRegAlumEnviar);

        txtNombres.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String nombres = editable.toString();
                if (!nombres.matches(ValidacionUtil.NOMBRE)) {
                    txtNombres.setError("Nombres no válido, solo letras");
                }else {
                    txtNombres.setError(null); // Limpiar el error si es válido
                }
            }
        });

        txtApellidos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String apellidos = editable.toString();
                if (!apellidos.matches(ValidacionUtil.NOMBRE)) {
                    txtApellidos.setError("Apellidos no válido, solo letras");
                }else {
                    txtApellidos.setError(null); // Limpiar el error si es válido
                }
            }
        });

        txtDni.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String dni = editable.toString();
                if (!dni.matches(ValidacionUtil.DNI)) {
                    txtDni.setError("DNI debe tener 8 digitos");
                }else {
                    txtDni.setError(null); // Limpiar el error si es válido
                }
            }
        });

        txtTelefono.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String telefono = editable.toString();
                if (!telefono.matches(TELEFONO)) {
                    txtTelefono.setError("Teléfono debe tener 9 dígitos");
                } else {
                    txtTelefono.setError(null);
                }
            }
        });

        txtCorreo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String correo = editable.toString();
                if (!correo.matches(ValidacionUtil.CORREO)) {
                    txtCorreo.setError("Correo electrónico no valido");
                }else {
                    txtCorreo.setError(null); // Limpiar el error si es válido
                }
            }
        });

        txtDireccion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String direccion = editable.toString();
                if (!direccion.matches(ValidacionUtil.DIRECCION)) {
                    txtDireccion.setError("Dirección no valido");
                } else {
                    txtDireccion.setError(null);
                }
            }
        });

        txtFechaNacimiento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String fechaNaci = editable.toString();
                if (!fechaNaci.matches(ValidacionUtil.FECHA)) {
                    txtFechaNacimiento.setError("Fecha Nacimiento no valido");
                }else {
                    txtFechaNacimiento.setError(null); // Limpiar el error si es válido
                }
            }
        });

        btnRegistra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validarFormulario()) {
                    return;
                }
                String nombres = txtNombres.getText().toString();
                String apellidos = txtApellidos.getText().toString();
                String telefono = txtTelefono.getText().toString();
                String dni = txtDni.getText().toString();
                String correo = txtCorreo.getText().toString();
                String direccion = txtDireccion.getText().toString();
                String fechaNacimiento = txtFechaNacimiento.getText().toString();
                String idPais = spnPais.getSelectedItem().toString().split(":")[0];
                String idModalidad = spnModalidad.getSelectedItem().toString().split(":")[0];

                Pais objPais = new Pais();
                objPais.setIdPais(Integer.parseInt(idPais.trim()));

                Modalidad objModalidad = new Modalidad();
                objModalidad.setIdModalidad(Integer.parseInt(idModalidad.trim()));

                Alumno objAlumno = new Alumno();
                objAlumno.setNombres(nombres);
                objAlumno.setApellidos(apellidos);
                objAlumno.setTelefono(telefono);
                objAlumno.setDni(dni);
                objAlumno.setCorreo(correo);
                objAlumno.setDireccion(direccion);
                objAlumno.setFechaNacimiento(fechaNacimiento);
                objAlumno.setPais(objPais);
                objAlumno.setModalidad(objModalidad);
                objAlumno.setFechaRegistro(FunctionUtil.getFechaActualStringDateTime());
                objAlumno.setEstado(1);

                registraValida(objAlumno);
            }
        });
    }

    void registraValida(Alumno obj){
        Call<List<Alumno>> call  = serviceAlumno.listaPorNombreApellidoIgual(obj.getNombres(), obj.getApellidos());

        call.enqueue(new Callback<List<Alumno>>() {
            @Override
            public void onResponse(Call<List<Alumno>> call, Response<List<Alumno>> response) {
                if (response.isSuccessful()){
                    List<Alumno> lstSalida = response.body();
                    if (lstSalida.isEmpty()){
                        registra(obj);
                    }else{
                        mensajeAlert("El Alumno " + obj.getNombres() + " " + obj.getApellidos() +" ya existe ");
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Alumno>> call, Throwable t) {
            }
        });
    }

    void cargaPais(){
        Call<List<Pais>> call = servicePais.listaTodos();
        call.enqueue(new Callback<List<Pais>>() {
            @Override
            public void onResponse(Call<List<Pais>> call, Response<List<Pais>> response) {
                if (response.isSuccessful()){
                    List<Pais> lst =  response.body();
                    paises.add(" [ Seleccione ] ");
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

    void cargaModalidad(){
        Call<List<Modalidad>>  call = serviceModalidad.listaTodos();
        call.enqueue(new Callback<List<Modalidad>>() {
            @Override
            public void onResponse(Call<List<Modalidad>> call, Response<List<Modalidad>> response) {
                if (response.isSuccessful()){
                    List<Modalidad> lst =  response.body();
                    modalidad.add(" [ Seleccione ] ");
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

    void registra(Alumno obj){
        Call<Alumno> call = serviceAlumno.registra(obj);
        call.enqueue(new Callback<Alumno>() {
            @Override
            public void onResponse(Call<Alumno> call, Response<Alumno> response) {
                if (response.isSuccessful()){
                    Alumno objSalida = response.body();
                    mensajeAlert(" Registro de Alumno exitoso:  "
                            + " \n >>>> ID >> " + objSalida.getIdAlumno()
                            + " \n >>> Nombre Completo >>> " + " \n " + objSalida.getNombres()
                            + " " +  objSalida.getApellidos());
                    //Limpiar el formulario
                    limpiarFormulario();
                }
            }
            @Override
            public void onFailure(Call<Alumno> call, Throwable t) {

            }
        });
    }

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

    void limpiarFormulario() {
        // Limpiar todos los EditText
        txtNombres.setText("");
        txtApellidos.setText("");
        txtTelefono.setText("");
        txtDni.setText("");
        txtCorreo.setText("");
        txtDireccion.setText("");
        txtFechaNacimiento.setText("");

        // Restablecer los Spinners al primer elemento
        if (spnPais.getAdapter().getCount() > 0) {
            spnPais.setSelection(0);
        }
        if (spnModalidad.getAdapter().getCount() > 0) {
            spnModalidad.setSelection(0);
        }

        txtNombres.setError(null);
        txtApellidos.setError(null);
        txtTelefono.setError(null);
        txtDni.setError(null);
        txtCorreo.setError(null);
        txtDireccion.setError(null);
        txtFechaNacimiento.setError(null);
    }

    private boolean validarFormulario() {
        boolean isValid = true;

        // Validar nombres
        SpannableStringBuilder errorNombres = new SpannableStringBuilder("Este campo es obligatorio");
        errorNombres.setSpan(new ForegroundColorSpan(Color.RED), 0, errorNombres.length(), 0);
        if (txtNombres.getText().toString().trim().isEmpty()) {
            txtNombres.setHint(errorNombres);
            isValid = false;
        } else {
            txtNombres.setHint(null);
        }

        // Validar apellidos
        SpannableStringBuilder errorApellidos = new SpannableStringBuilder("Este campo es obligatorio");
        errorApellidos.setSpan(new ForegroundColorSpan(Color.RED), 0, errorApellidos.length(), 0);
        if (txtApellidos.getText().toString().trim().isEmpty()) {
            txtApellidos.setHint(errorApellidos);
            isValid = false;
        } else {
            txtApellidos.setHint(null);
        }

        // Validar teléfono
        SpannableStringBuilder errorTelefono = new SpannableStringBuilder("Este campo es obligatorio");
        errorTelefono.setSpan(new ForegroundColorSpan(Color.RED), 0, errorTelefono.length(), 0);
        if (txtTelefono.getText().toString().trim().isEmpty()) {
            txtTelefono.setHint(errorTelefono);
            isValid = false;
        } else {
            txtTelefono.setHint(null);
        }

        // Validar DNI
        SpannableStringBuilder errorDni = new SpannableStringBuilder("Este campo es obligatorio");
        errorDni.setSpan(new ForegroundColorSpan(Color.RED), 0, errorDni.length(), 0);
        if (txtDni.getText().toString().trim().isEmpty()) {
            txtDni.setHint(errorDni);
            isValid = false;
        } else {
            txtDni.setHint(null);
        }

        // Validar correo
        SpannableStringBuilder errorCorreo = new SpannableStringBuilder("Este campo es obligatorio");
        errorCorreo.setSpan(new ForegroundColorSpan(Color.RED), 0, errorCorreo.length(), 0);
        if (txtCorreo.getText().toString().trim().isEmpty()) {
            txtCorreo.setHint(errorCorreo);
            isValid = false;
        } else {
            txtCorreo.setHint(null);
        }

        // Validar dirección
        SpannableStringBuilder errorDireccion = new SpannableStringBuilder("Este campo es obligatorio");
        errorDireccion.setSpan(new ForegroundColorSpan(Color.RED), 0, errorDireccion.length(), 0);
        if (txtDireccion.getText().toString().trim().isEmpty()) {
            txtDireccion.setHint(errorDireccion);
            isValid = false;
        } else {
            txtDireccion.setHint(null);
        }

        // Validar fecha de nacimiento
        SpannableStringBuilder errorFechaNaci = new SpannableStringBuilder("Este campo es obligatorio");
        errorFechaNaci.setSpan(new ForegroundColorSpan(Color.RED), 0, errorFechaNaci.length(), 0);
        if (txtFechaNacimiento.getText().toString().trim().isEmpty()) {
            txtFechaNacimiento.setHint(errorFechaNaci);
            isValid = false;
        } else {
            txtFechaNacimiento.setHint(null);
        }

        // Validar País
        if (spnPais.getSelectedItemPosition() == 0) {
            TextView errorText = (TextView) spnPais.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Seleccione un país");
            isValid = false;
        }

        // Validar Modalidad
        if (spnModalidad.getSelectedItemPosition() == 0) {
            TextView errorText = (TextView) spnModalidad.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Seleccione una modalidad");
            isValid = false;
        }

        return isValid;
    }

}