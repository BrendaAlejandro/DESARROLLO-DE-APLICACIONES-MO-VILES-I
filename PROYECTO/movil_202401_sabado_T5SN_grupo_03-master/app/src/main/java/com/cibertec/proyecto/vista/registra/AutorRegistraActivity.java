package com.cibertec.proyecto.vista.registra;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Insets;
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

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cibertec.proyecto.R;
import com.cibertec.proyecto.entity.Autor;
import com.cibertec.proyecto.entity.Grado;
import com.cibertec.proyecto.entity.Pais;
import com.cibertec.proyecto.service.ServiceAutor;
import com.cibertec.proyecto.service.ServiceCategoriaAutor;
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


public class AutorRegistraActivity extends NewAppCompatActivity {

    //Pais
    Spinner spnPais;
    ArrayAdapter<String> adaptadorPais;
    ArrayList<String> paises = new ArrayList<>();

    //Categoria
    Spinner spnGrado;
    ArrayAdapter<String> adaptadorGrado;
    ArrayList<String> grados = new ArrayList<>();

    //Servicio
    ServiceAutor serviceAutor;
    ServicePais servicePais;
    ServiceCategoriaAutor serviceCategoriaAutor;

    Button btnRegistra;

    EditText txtNombres, txtApellidos, txtCorreo, txtFechaNaci, txtTele;
    public static final String TELEFONO = "[0-9]{9}";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autor_registra);



        adaptadorPais = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, paises);
        spnPais = findViewById(R.id.spnRegAutPais);
        spnPais.setAdapter(adaptadorPais);

        adaptadorGrado= new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, grados);
        spnGrado = findViewById(R.id.spnRegAutCate);
        spnGrado.setAdapter(adaptadorGrado);

        servicePais = ConnectionRest.getConnection().create(ServicePais.class);
        serviceAutor = ConnectionRest.getConnection().create(ServiceAutor.class);
        serviceCategoriaAutor = ConnectionRest.getConnection().create(ServiceCategoriaAutor.class);

        cargaPais();
        cargaCategoria();


        txtNombres = findViewById(R.id.txtRegAutNom);
        txtApellidos = findViewById(R.id.txtRegAutApe);
        txtCorreo = findViewById(R.id.txtRegAutCorreo);
        txtFechaNaci = findViewById(R.id.txtRegAutFecha);
        txtTele= findViewById(R.id.txtRegAutTel);

        btnRegistra = findViewById(R.id.btnRegAutEnviar);

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
                    txtNombres.setError("Nombre no válido, solo letras");
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
                    txtApellidos.setError("Apellido no válido, solo letras");
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
                    txtCorreo.setError("Correo no válido");
                }
            }
        });

        txtFechaNaci.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String fecha = editable.toString();
                if (!fecha.matches(ValidacionUtil.FECHA)) {
                    txtFechaNaci.setError("Fecha no valido, solo numeros");
                }
            }
        });
        txtTele.addTextChangedListener(new TextWatcher() {
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
                    txtTele.setError("Teléfono debe tener 9 dígitos");
                } else {
                    txtTele.setError(null); // Limpiar el error si es válido
                }
            }
        });
        btnRegistra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validarFormulario()) {
                    return; // Si la validación falla, no continúa
                }

                String nombres = txtNombres.getText().toString();
                String apellidos = txtApellidos.getText().toString();
                String correo = txtCorreo.getText().toString();
                String fecNac = txtFechaNaci.getText().toString();
                String telef= txtTele.getText().toString();
                String idPais = spnPais.getSelectedItem().toString().split(":")[0];
                String idGrado = spnGrado.getSelectedItem().toString().split(":")[0];

                Pais objPais = new Pais();
                objPais.setIdPais(Integer.parseInt(idPais.trim()));

                Grado objGrado= new Grado();
                objGrado.setIdGrado(Integer.parseInt(idGrado.trim()));

                    Autor objAutor = new Autor();
                    objAutor.setNombres(nombres);
                    objAutor.setApellidos(apellidos);
                    objAutor.setCorreo(correo);
                    objAutor.setFechaNacimiento(fecNac);
                    objAutor.setTelefono(telef);
                    objAutor.setPais(objPais);
                    objAutor.setGrado(objGrado);
                    objAutor.setFechaRegistro(FunctionUtil.getFechaActualStringDateTime());
                    objAutor.setEstado(1);
                    registraValida(objAutor);
                }
        });

    }
    void registraValida(Autor obj){
        Call<List<Autor>> call  = serviceAutor.listaPorNombreApellidoIgual(obj.getNombres(), obj.getApellidos());

        call.enqueue(new Callback<List<Autor>>() {
            @Override
            public void onResponse(Call<List<Autor>> call, Response<List<Autor>> response) {
                if (response.isSuccessful()){
                    List<Autor> lstSalida = response.body();
                    if (lstSalida.isEmpty()){
                        registra(obj);
                    }else{
                        mensajeAlert("El autor:"  + " \n" + obj.getNombres() + " " + obj.getApellidos() + " \n" + " ya existe !!! ");
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Autor>> call, Throwable t) {
            }
        });
    }

    void cargaPais(){
        Call<List<Pais>>  call = servicePais.listaTodos();
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

    void cargaCategoria(){
        Call<List<Grado>> call = serviceCategoriaAutor.listaTodos();
        call.enqueue(new Callback<List<Grado>>() {
            @Override
            public void onResponse(Call<List<Grado>> call, Response<List<Grado>> response) {
                if (response.isSuccessful()){
                    List<Grado> lst =  response.body();
                    for (Grado obj :lst){
                        grados.add( obj.getIdGrado()+ " : " + obj.getDescripcion() );
                    }
                    adaptadorGrado.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Grado>> call, Throwable t) {

            }
        });
    }
    boolean existe = false;
    boolean existeNombreApellido(String nombre, String apellidos){
        Call<List<Autor>> call = serviceAutor.listaPorNombreApellidoIgual(nombre, apellidos);
        call.enqueue(new Callback<List<Autor>>() {
            @Override
            public void onResponse(Call<List<Autor>> call, Response<List<Autor>> response) {
                List<Autor> lst = response.body();
                if (lst.isEmpty()){
                    existe = false;
                }else{
                    existe = true;
                }
            }
            @Override
            public void onFailure(Call<List<Autor>> call, Throwable t) {
            }
        });
        return existe;
    }

    void registra(Autor obj){
        Call<Autor> call = serviceAutor.registra(obj);
        call.enqueue(new Callback<Autor>() {
            @Override
            public void onResponse(Call<Autor> call, Response<Autor> response) {
                if (response.isSuccessful()){
                    Autor objSalida = response.body();
                    mensajeAlert(" Registro de Autor exitoso:  "
                            + " \n >>>> ID:>> "  +  objSalida.getIdAutor()
                            + " \n >>> Autor: >>> " + " \n" +  objSalida.getNombres() +
                            " " +  objSalida.getApellidos());
                    // Limpiar el formulario después de registrar exitosamente
                    limpiarFormulario();
                }
            }

            @Override
            public void onFailure(Call<Autor> call, Throwable t) {

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
        txtCorreo.setText("");
        txtFechaNaci.setText("");
        txtTele.setText("");

        // Restablecer los Spinners al primer elemento
        if (spnPais.getAdapter().getCount() > 0) {
            spnPais.setSelection(0);
        }
        if (spnGrado.getAdapter().getCount() > 0) {
            spnGrado.setSelection(0);
        }
        // Limpiar errores en los EditText
        txtNombres.setError(null);
        txtApellidos.setError(null);
        txtCorreo.setError(null);
        txtFechaNaci.setError(null);
        txtTele.setError(null);
    }

    private boolean validarFormulario() {
        boolean isValid = true;



        // Validar nombres
        SpannableStringBuilder errorNombres = new SpannableStringBuilder("Este campo es obligatorio*");
        errorNombres.setSpan(new ForegroundColorSpan(Color.RED), 0, errorNombres.length(), 0);
        if (txtNombres.getText().toString().trim().isEmpty()) {
            txtNombres.setHint(errorNombres);
            isValid = false;
        } else {
            txtNombres.setHint(null);
        }

        // Validar apellidos
        SpannableStringBuilder errorApellidos = new SpannableStringBuilder("Este campo es obligatorio*");
        errorApellidos.setSpan(new ForegroundColorSpan(Color.RED), 0, errorApellidos.length(), 0);
        if (txtApellidos.getText().toString().trim().isEmpty()) {
            txtApellidos.setHint(errorApellidos);
            isValid = false;
        } else {
            txtApellidos.setHint(null);
        }

        // Validar correo
        SpannableStringBuilder errorCorreo = new SpannableStringBuilder("Este campo es obligatorio*");
        errorCorreo.setSpan(new ForegroundColorSpan(Color.RED), 0, errorCorreo.length(), 0);
        if (txtCorreo.getText().toString().trim().isEmpty()) {
            txtCorreo.setHint(errorCorreo);
            isValid = false;
        } else {
            txtCorreo.setHint(null);
        }

        // Validar fecha de nacimiento
        SpannableStringBuilder errorFechaNaci = new SpannableStringBuilder("Este campo es obligatorio*");
        errorFechaNaci.setSpan(new ForegroundColorSpan(Color.RED), 0, errorFechaNaci.length(), 0);
        if (txtFechaNaci.getText().toString().trim().isEmpty()) {
            txtFechaNaci.setHint(errorFechaNaci);
            isValid = false;
        } else {
            txtFechaNaci.setHint(null);
        }

        // Validar teléfono
        SpannableStringBuilder errorTele = new SpannableStringBuilder("El teléfono es un campo obligatorio*");
        errorTele.setSpan(new ForegroundColorSpan(Color.RED), 0, errorTele.length(), 0);
        if (txtTele.getText().toString().trim().isEmpty()) {
            txtTele.setHint(errorTele);
            isValid = false;
        } else {
            txtTele.setHint(null);
        }

        return isValid;
    }

}