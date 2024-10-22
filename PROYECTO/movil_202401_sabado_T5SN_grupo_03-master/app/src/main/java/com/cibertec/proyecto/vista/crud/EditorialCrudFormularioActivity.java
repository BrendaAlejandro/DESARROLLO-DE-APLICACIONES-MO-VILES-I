package com.cibertec.proyecto.vista.crud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cibertec.proyecto.R;
import com.cibertec.proyecto.entity.Categoria;
import com.cibertec.proyecto.entity.Editorial;
import com.cibertec.proyecto.entity.Pais;
import com.cibertec.proyecto.service.ServiceCategoria;
import com.cibertec.proyecto.service.ServiceEditorial;
import com.cibertec.proyecto.service.ServicePais;
import com.cibertec.proyecto.util.ConnectionRest;
import com.cibertec.proyecto.util.NewAppCompatActivity;
import com.cibertec.proyecto.util.ValidacionUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditorialCrudFormularioActivity extends NewAppCompatActivity {

    //Pais
    Spinner spnPais;
    ArrayAdapter<String> adaptadorPais;
    ArrayList<String> paises = new ArrayList<>();

    //Categoria
    Spinner spnCategoria;
    ArrayAdapter<String> adaptadorCategoria;
    ArrayList<String> categorias = new ArrayList<>();

    //Servicio
    ServiceEditorial serviceEditorial;
    ServicePais servicePais;
    ServiceCategoria serviceCategoria;

    EditText txtRazSoc, txtDir, txtFecCre, txtRuc;
    Button btnEnviar, btnRegresar;
    TextView idTitlePage;
    String metodo;
    Editorial objActual;
    RadioButton rbtActivo, rbtInactivo;
    TextView txtEstado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editorial_crud_formulario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainEditorial), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rbtActivo = findViewById(R.id.rbtActivoFormulario);
        rbtInactivo = findViewById(R.id.rbtInactivoFormulario);
        txtEstado = findViewById(R.id.txtEstadoFormulario);

        servicePais = ConnectionRest.getConnection().create(ServicePais.class);
        serviceEditorial = ConnectionRest.getConnection().create(ServiceEditorial.class);
        serviceCategoria = ConnectionRest.getConnection().create(ServiceCategoria.class);

        adaptadorPais = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, paises);
        spnPais = findViewById(R.id.cboPaisEditorialFormulario);
        spnPais.setAdapter(adaptadorPais);

        adaptadorCategoria = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categorias);
        spnCategoria = findViewById(R.id.cboCategoriaEditorialFormulario);
        spnCategoria.setAdapter(adaptadorCategoria);

        txtRazSoc = findViewById(R.id.txtRegRazonSocialFormulario);
        txtDir = findViewById(R.id.txtRegDireccionFormulario);
        txtFecCre = findViewById(R.id.txtFechaCreacionEditorialFormulario);
        txtRuc = findViewById(R.id.txtRucEditorialFormulario);

        metodo = (String) getIntent().getExtras().get("var_metodo");
        idTitlePage = findViewById(R.id.idTitlePageFormulario);
        btnEnviar = findViewById(R.id.btnRegistrarEditorialFormulario);
        btnRegresar = findViewById(R.id.btnRegresarEditorialFormulario);

        if (metodo.equals("REGISTRAR")) {
            idTitlePage.setText("Registra Editorial");
            btnEnviar.setText("Registrar");
            rbtActivo.setVisibility(View.GONE);
            rbtInactivo.setVisibility(View.GONE);
            txtEstado.setVisibility(View.GONE);
        } else if (metodo.equals("ACTUALIZAR")) {
            idTitlePage.setText("Actualiza Editorial");
            btnEnviar.setText("Actualizar");

            objActual = (Editorial) getIntent().getExtras().get("var_objeto");
            txtRazSoc.setText(objActual.getRazonSocial());
            txtDir.setText(String.valueOf(objActual.getDireccion()));
            txtFecCre.setText(objActual.getFechaCreacion());
            txtRuc.setText(objActual.getRuc());

            if (objActual.getEstado() == 1) {
                rbtActivo.setChecked(true);
            } else {
                rbtInactivo.setChecked(true);
            }
        }



        /* Direccion */
        txtDir.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String direccion = editable.toString();
                if (!direccion.matches(ValidacionUtil.DIRECCION_XD)) {
                    txtDir.setError("Dirección de Editorial, solo permite nùmeros y letras");
                } else {
                    txtDir.setError(null);
                }
            }
        });

        /* Razon Social*/
        txtRazSoc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String razonSocial = editable.toString();
                if (!razonSocial.matches(ValidacionUtil.RazonSocial)) {
                    txtRazSoc.setError("Razón Social de Editorial no válida, solo letras");
                } else {
                    txtRazSoc.setError(null);
                }
            }
        });

        /* Fecha Creacion */
        txtFecCre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String fechaCrea = editable.toString();
                if (!fechaCrea.matches(ValidacionUtil.FECHA)) {
                    txtFecCre.setError("Fecha de creación no válida (yyyy-mm-dd)");
                } else {
                    txtFecCre.setError(null);
                }
            }
        });

        /* Ruc */
        txtRuc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String ruc = editable.toString();
                if (!ruc.matches(ValidacionUtil.RUC)) {
                    txtRuc.setError("RUC de Proveedor debe empezar con 10 y debe contener 11 dígitos");
                } else {
                    txtRuc.setError(null);
                }
            }
        });

        btnRegresar.setOnClickListener(view -> {
            Intent intent = new Intent(EditorialCrudFormularioActivity.this, EditorialCrudListaActivity.class);
            startActivity(intent);
        });

        btnEnviar.setOnClickListener(view -> {
            if (!validadFormulario()) {
                return;
            }
            String rs = txtRazSoc.getText().toString();
            String dir = txtDir.getText().toString();
            String feccre = txtFecCre.getText().toString();
            String ruc = txtRuc.getText().toString();
            String idPais = spnPais.getSelectedItem().toString().split(":")[0];
            String idCat = spnCategoria.getSelectedItem().toString().split(":")[0];

            Categoria objTipo = new Categoria();
            objTipo.setIdCategoria(Integer.parseInt(idCat.trim()));

            Pais objPais = new Pais();
            objPais.setIdPais(Integer.parseInt(idPais.trim()));

            Editorial objEditorial = new Editorial();
            objEditorial.setRazonSocial(rs);
            objEditorial.setDireccion(dir);
            objEditorial.setFechaCreacion(feccre);
            objEditorial.setRuc(ruc);
            objEditorial.setPais(objPais);
            objEditorial.setCategoria(objTipo);

            if (metodo.equals("REGISTRAR")) {
                objEditorial.setEstado(1);
                registraValida(objEditorial);
            } else {
                objEditorial.setEstado(rbtActivo.isChecked() ? 1 : 0);
                objEditorial.setIdEditorial(objActual.getIdEditorial());
                actualizaValida(objEditorial);
            }
        });
        cargaPaises();
        cargaCategorias();
    }

    private boolean validadFormulario() {
        boolean isValid = true;

        // Validar Razón Social
        if (txtRazSoc.getText().toString().trim().isEmpty()) {
            txtRazSoc.setError("Este campo es obligatorio");
            isValid = false;
        } else if (txtRazSoc.getError() != null && !txtRazSoc.getError().toString().isEmpty()) {
            isValid = false;
        }

        // Validar Dirección
        if (txtDir.getText().toString().trim().isEmpty()) {
            txtDir.setError("Este campo es obligatorio");
            isValid = false;
        } else if (txtDir.getError() != null && !txtDir.getError().toString().isEmpty()) {
            isValid = false;
        }

        // Validar Fecha de Creación
        if (txtFecCre.getText().toString().trim().isEmpty()) {
            txtFecCre.setError("Este campo es obligatorio");
            isValid = false;
        } else if (txtFecCre.getError() != null && !txtFecCre.getError().toString().isEmpty()) {
            isValid = false;
        }

        // Validar RUC
        if (txtRuc.getText().toString().trim().isEmpty()) {
            txtRuc.setError("Este campo es obligatorio");
            isValid = false;
        } else if (txtRuc.getError() != null && !txtRuc.getError().toString().isEmpty()) {
            isValid = false;
        }

        // Validar Spinner de País
        if (spnPais.getSelectedItemPosition() == 0) {
            TextView errorText = (TextView) spnPais.getSelectedView();
            if (errorText != null) {
                errorText.setError("");
                errorText.setTextColor(Color.RED);
                errorText.setText("Seleccione un país");
            }
            isValid = false;
        }

        // Validar Spinner de Categoría
        if (spnCategoria.getSelectedItemPosition() == 0) {
            TextView errorText = (TextView) spnCategoria.getSelectedView();
            if (errorText != null) {
                errorText.setError("");
                errorText.setTextColor(Color.RED);
                errorText.setText("Seleccione una Categoría");
            }
            isValid = false;
        }

        return isValid;
    }

    public void registraValida(Editorial obj) {
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        Call<Editorial> call = serviceEditorial.registra(obj);
        call.enqueue(new Callback<Editorial>() {
            @Override
            public void onResponse(Call<Editorial> call, Response<Editorial> response) {
                if (response.isSuccessful()) {
                    Editorial objSalida = response.body();
                    if (objSalida == null) {
                        mensajeAlert("ERROR", "ERROR, no se encontró respuesta", "ERROR");
                    } else {
                        mensajeAlert("REGISTRO EXITOSO", "Se registró el Editorial con código : " + objSalida.getIdEditorial()
                                + " \n  Razón Social : " + objSalida.getRazonSocial(), "EXITO");
                    }
                } else {
                    mensajeAlert("ERROR", "ERROR en la respuesta", "ERROR");
                }
            }

            @Override
            public void onFailure(Call<Editorial> call, Throwable t) {
                mensajeAlert("ERROR", "ERROR en la respuesta", "ERROR");
            }
        });
    }

    public void actualizaValida(Editorial obj) {
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        Call<Editorial> call = serviceEditorial.actualizaEditorial(obj);
        call.enqueue(new Callback<Editorial>() {
            @Override
            public void onResponse(Call<Editorial> call, Response<Editorial> response) {
                if (response.isSuccessful()) {
                    Editorial objSalida = response.body();
                    if (objSalida == null) {
                        mensajeAlert("ERROR", "ERROR, no se encontró respuesta", "ERROR");
                    } else {
                        mensajeAlert("ACTUALIZACIÓN EXITOSA", "Se actualizó el Editorial con código : " + objSalida.getIdEditorial()
                                 + " \n  Razón Social : " + objSalida.getRazonSocial(), "EXITO");
                    }
                } else {
                    mensajeAlert("ERROR", "ERROR en la respuesta", "ERROR");
                }
            }

            @Override
            public void onFailure(Call<Editorial> call, Throwable t) {
                mensajeAlert("ERROR", "ERROR en la respuesta", "ERROR");
            }
        });
    }

    public void mensajeAlert(String titulo, String mensaje, String tipo) {
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setTitle(titulo);
        alerta.setMessage(mensaje);
        alerta.setCancelable(false);
        if (tipo.equals("ERROR")) {
            alerta.setNegativeButton("ACEPTAR", (dialog, which) -> {});
        } else if (tipo.equals("EXITO")) {
            alerta.setPositiveButton("ACEPTAR", (dialog, which) -> {
                Intent intent = new Intent(EditorialCrudFormularioActivity.this, EditorialCrudListaActivity.class);
                startActivity(intent);
            });
        }
        AlertDialog alertDialog = alerta.create();
        alertDialog.show();
    }

    void cargaPaises(){
        Call<List<Pais>> call = servicePais.listaTodos();
        call.enqueue(new Callback<List<Pais>>() {
            @Override
            public void onResponse(Call<List<Pais>> call, Response<List<Pais>> response) {
                List<Pais> lstAux =  response.body();
                paises.add(" [ Seleccione ] ");
                for(Pais aux:lstAux){
                    paises.add(aux.getIdPais() + " : "  + aux.getNombre());
                }
                adaptadorPais.notifyDataSetChanged();
                if (metodo.equals("ACTUALIZAR")){
                    String id = String.valueOf(objActual.getPais().getIdPais());
                    String nombre = String.valueOf(objActual.getPais().getNombre());
                    String row = id + " : " + nombre;
                    for(int i = 0; i <= paises.size(); i++ ){
                        if (paises.get(i).equals(row)){
                            spnPais.setSelection(i);
                            break;
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Pais>> call, Throwable t) {

            }
        });
    }


    void cargaCategorias(){
        Call<List<Categoria>>  call = serviceCategoria.listaCategoria();
        call.enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                List<Categoria> lstAux =  response.body();
                categorias.add(" [ Seleccione ] ");
                for(Categoria aux:lstAux){
                    categorias.add(aux.getIdCategoria() + " : "  + aux.getDescripcion());
                }
                adaptadorCategoria.notifyDataSetChanged();
                if (metodo.equals("ACTUALIZAR")){
                    String id = String.valueOf(objActual.getCategoria().getIdCategoria());
                    String nombre = String.valueOf(objActual.getCategoria().getDescripcion());
                    String row = id + " : " + nombre;
                    for(int i = 0; i <= categorias.size(); i++ ){
                        if (categorias.get(i).equals(row)){
                            spnCategoria.setSelection(i);
                            break;
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {

            }
        });
    }
}
