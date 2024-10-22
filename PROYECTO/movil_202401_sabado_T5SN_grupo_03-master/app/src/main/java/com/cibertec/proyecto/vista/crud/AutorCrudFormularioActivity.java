package com.cibertec.proyecto.vista.crud;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

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

import retrofit2.Callback;
import retrofit2.Response;


import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;





import retrofit2.Call;

public class AutorCrudFormularioActivity extends NewAppCompatActivity {

    //Pais
    Spinner spnPais;
    ArrayAdapter<String> adaptadorPais;
    ArrayList<String> paises = new ArrayList<>();

    //Categoria
    Spinner spnGrado;
    ArrayAdapter<String> adaptadorCategoria;
    ArrayList<String> grados = new ArrayList<>();

    //Servicio
    ServiceAutor serviceAutor;
    ServicePais servicePais;
    ServiceCategoriaAutor serviceCategoriaAutor;

    EditText txtNombres, txtApellidos, txtCorreo, txtFechaNaci, txtTele;

    Button btnEnviar, btnRegresar;

    TextView idTitlePage;

    String metodo;

    Autor objActual;

    RadioButton rbtActivo, rbtInactivo;
    TextView txtEstado;
    public static final String TELEFONO = "[0-9]{9}";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autor_crud_formulario);

        rbtActivo = findViewById(R.id.rbtActivo);
        rbtInactivo = findViewById(R.id.rbtInactivo);
        txtEstado = findViewById(R.id.txtEstado);

        servicePais = ConnectionRest.getConnection().create(ServicePais.class);
        serviceCategoriaAutor = ConnectionRest.getConnection().create(ServiceCategoriaAutor.class);
        serviceAutor = ConnectionRest.getConnection().create(ServiceAutor.class);

        adaptadorPais = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, paises);
        spnPais = findViewById(R.id.spnRegAutPais);
        spnPais.setAdapter(adaptadorPais);

        adaptadorCategoria = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, grados);
        spnGrado = findViewById(R.id.spnRegAutCate);
        spnGrado.setAdapter(adaptadorCategoria);

        txtNombres = findViewById(R.id.txtRegAutNom);
        txtApellidos = findViewById(R.id.txtRegAutApe);
        txtCorreo = findViewById(R.id.txtRegAutCorreo);
        txtFechaNaci = findViewById(R.id.txtRegAutFecha);
        txtTele = findViewById(R.id.txtRegAutTel);

        metodo = (String)getIntent().getExtras().get("var_metodo");

        idTitlePage = findViewById(R.id.idTitlePage);
        btnEnviar = findViewById(R.id.btnRegAutEnviar);
        btnRegresar = findViewById(R.id.btnRegAutRegresar);

        if (metodo.equals("REGISTRAR")){
            idTitlePage.setText("Registra Autor");
            btnEnviar.setText("Registrar");
            rbtActivo.setVisibility(View.GONE);
            rbtInactivo.setVisibility(View.GONE);
            txtEstado.setVisibility(View.GONE);
        }else if (metodo.equals("ACTUALIZAR")){
            idTitlePage.setText("Actualiza Autor");
            btnEnviar.setText("Actualizar");

            objActual = (Autor) getIntent().getExtras().get("var_objeto");
            txtNombres.setText(objActual.getNombres());
            txtApellidos.setText(objActual.getApellidos());
            txtCorreo.setText(objActual.getCorreo());
            txtFechaNaci.setText(String.valueOf(objActual.getFechaNacimiento()));
            txtTele.setText(objActual.getTelefono());

            if (objActual.getEstado() == 1){
                rbtActivo.setChecked(true);
            }else{
                rbtInactivo.setChecked(true);
            }
        }

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


        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AutorCrudFormularioActivity.this, AutorCrudListaActivity.class);
                startActivity(intent);
            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validarFormulario()) {
                    return; // Si la validación falla, no continúa
                }
                String nombres = txtNombres.getText().toString();
                String apellidos = txtApellidos.getText().toString();
                String correo = txtCorreo.getText().toString();
                String fechaNaci = txtFechaNaci.getText().toString();
                String tele= txtTele.getText().toString();
                String idPais = spnPais.getSelectedItem().toString().split(":")[0];
                String idCategoria = spnGrado.getSelectedItem().toString().split(":")[0];

                Pais objPais = new Pais();
                objPais.setIdPais(Integer.parseInt(idPais.trim()));

                Grado objGrado = new Grado();
                objGrado.setIdGrado(Integer.parseInt(idCategoria.trim()));

                Autor objAutor = new Autor();
                objAutor.setNombres(nombres);
                objAutor.setApellidos(apellidos);
                objAutor.setCorreo(correo);
                objAutor.setFechaNacimiento(fechaNaci);
                objAutor.setTelefono(tele);
                objAutor.setPais(objPais);
                objAutor.setGrado(objGrado);
                objAutor.setFechaRegistro(FunctionUtil.getFechaActualStringDateTime());
                if (metodo.equals("REGISTRAR")){
                    objAutor.setEstado(1);
                    registraValida(objAutor);
                }else{
                    objAutor.setEstado(rbtActivo.isChecked() ? 1 : 0);
                    objAutor.setIdAutor(objActual.getIdAutor());
                    actualiza(objAutor);
                }

            }
        });

        cargaPais();
        cargaGrado();
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

    void cargaPais() {
        Call<List<Pais>> call = servicePais.listaTodos();
        call.enqueue(new Callback<List<Pais>>() {
            @Override
            public void onResponse(Call<List<Pais>> call, Response<List<Pais>> response) {
                List<Pais> lstAux = response.body();
                paises.add(" [ Seleccione ] ");
                for (Pais aux : lstAux) {
                    paises.add(aux.getIdPais() + " : " + aux.getNombre());
                }
                adaptadorPais.notifyDataSetChanged();
                if (metodo.equals("ACTUALIZAR")) {
                    String id = String.valueOf(objActual.getPais().getIdPais());
                    String nombre = objActual.getPais().getNombre();
                    String row = id + " : " + nombre;
                    for (int i = 0; i < paises.size(); i++) {
                        if (paises.get(i).equals(row)) {
                            spnPais.setSelection(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Pais>> call, Throwable t) {
                // Manejo de errores
            }
        });
    }
        void cargaGrado(){
                Call<List<Grado>>  call = serviceCategoriaAutor.listaTodos();
                call.enqueue(new Callback<List<Grado>>() {
                    @Override
                    public void onResponse(Call<List<Grado>> call, Response<List<Grado>> response) {
                        List<Grado> lstAux =  response.body();
                        grados.add(" [ Seleccione ] ");
                        for(Grado aux:lstAux){
                            grados.add(aux.getIdGrado() + " : "  + aux.getDescripcion());
                        }
                        adaptadorCategoria.notifyDataSetChanged();
                        if (metodo.equals("ACTUALIZAR")){
                            String id = String.valueOf(objActual.getGrado().getIdGrado());
                            String descripcion = String.valueOf(objActual.getGrado().getDescripcion());
                            String row = id + " : " + descripcion;
                            for(int i = 0; i <= grados.size(); i++ ){
                                if (grados.get(i).equals(row)){
                                    spnGrado.setSelection(i);
                                    break;
                                }
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Grado>> call, Throwable t) {

                    }
                });
            }
            void registra(Autor obj) {
                Call<Autor> call = serviceAutor.registra(obj);
                call.enqueue(new Callback<Autor>() {
                    @Override
                    public void onResponse(Call<Autor> call, Response<Autor> response) {
                        if (response.isSuccessful()) {
                            Autor objSalida = response.body();
                            mensajeAlert(" Registro de Libro exitoso:  "
                                    + " \n >>>> ID >> " + objSalida.getIdAutor()
                                    + " \n >>> Título >>> " + objSalida.getNombres());
                            limpiarFormulario();
                        }
                    }

                    @Override
                    public void onFailure(Call<Autor> call, Throwable t) {

                    }
                });
            }

    void actualiza(Autor obj) {
        Call<Autor> call = serviceAutor.actualizaAutor(obj);
        call.enqueue(new Callback<Autor>() {
            @Override
            public void onResponse(Call<Autor> call, Response<Autor> response) {
                if (response.isSuccessful()) {
                    Autor objSalida = response.body();
                    mensajeAlert("Actualización de Autor exitosa: \n >>>> ID >> " + objSalida.getIdAutor()
                            + " \n >>> Nombre >>> " + objSalida.getNombres());
                }
            }

            @Override
            public void onFailure(Call<Autor> call, Throwable t) {
                // Manejo de errores
            }
        });
    }
            public void mensajeAlert(String msg) {
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
        // Limpiar los Spinner (ComboBox)
        spnGrado.setSelection(0);  // Aquí asume que 0 es el índice del elemento por defecto, ajusta según tu implementación
        spnPais.setSelection(0); // Aquí también asume que 0 es el índice del elemento por defecto, ajusta según tu implementación


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
// Validar Grado
        if (spnGrado.getSelectedItemPosition() == 0) {
            TextView errorText = (TextView) spnGrado.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Seleccione un grado");
            isValid = false;
        }
        // Validar País
        if (spnPais.getSelectedItemPosition() == 0) {
            TextView errorText = (TextView) spnPais.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Seleccione un país");
            isValid = false;
        }
        return isValid;
    }

}