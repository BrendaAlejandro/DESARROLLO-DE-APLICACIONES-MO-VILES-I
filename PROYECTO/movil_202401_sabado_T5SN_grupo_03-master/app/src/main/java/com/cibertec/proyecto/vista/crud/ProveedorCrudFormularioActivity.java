package com.cibertec.proyecto.vista.crud;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
import android.widget.Toast;

import com.cibertec.proyecto.R;
import com.cibertec.proyecto.entity.Pais;
import com.cibertec.proyecto.entity.Proveedor;
import com.cibertec.proyecto.entity.TipoProveedor;
import com.cibertec.proyecto.service.ServicePais;
import com.cibertec.proyecto.service.ServiceProveedor;
import com.cibertec.proyecto.service.ServiceTipoProveedor;
import com.cibertec.proyecto.util.ConnectionRest;
import com.cibertec.proyecto.util.NewAppCompatActivity;
import com.cibertec.proyecto.util.ValidacionUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProveedorCrudFormularioActivity extends NewAppCompatActivity {

    //Pais
    Spinner spnPais;
    ArrayAdapter<String> adaptadorPais;
    ArrayList<String> paises = new ArrayList<>();

    //Tipo
    Spinner spnTipo;
    ArrayAdapter<String> adaptadorTipo;
    ArrayList<String> tipo = new ArrayList<>();

    //Servicio
    ServiceProveedor serviceProveedor;
    ServicePais servicePais;
    ServiceTipoProveedor serviceTipo;

    EditText txtRazonSocial, txtRuc, txtTelefono;

    Button btnRegresar,btnEnviar;

    TextView idTitlePage;

    String metodo;

    Proveedor objActual;

    RadioButton rbtActivo, rbtInactivo;
    TextView txtEstado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proveedor_crud_formulario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainProveedor), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rbtActivo = findViewById(R.id.rbtActivo);
        rbtInactivo = findViewById(R.id.rbtInactivo);
        txtEstado = findViewById(R.id.txtEstado);

        servicePais = ConnectionRest.getConnection().create(ServicePais.class);
        serviceTipo = ConnectionRest.getConnection().create(ServiceTipoProveedor.class);
        serviceProveedor = ConnectionRest.getConnection().create(ServiceProveedor.class);

        adaptadorPais = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, paises);
        spnPais = findViewById(R.id.spnRegPaisProv);
        spnPais.setAdapter(adaptadorPais);

        adaptadorTipo = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, tipo);
        spnTipo = findViewById(R.id.spnRegTipoProv);
        spnTipo.setAdapter(adaptadorTipo);

        txtRazonSocial = findViewById(R.id.txtRegRazonSocialProv);
        txtRuc = findViewById(R.id.txtRegRucProv);
        txtTelefono = findViewById(R.id.txtRegTelefonoProv);

        metodo = (String) getIntent().getExtras().get("var_metodo");
        idTitlePage = findViewById(R.id.idTitlePage);
        btnEnviar = findViewById(R.id.btnRegEnviarProv);
        btnRegresar = findViewById(R.id.btnRegRegresarProv);

        if (metodo.equals("REGISTRAR")) {
            idTitlePage.setText("Registra Proveedor");
            btnEnviar.setText("Registrar");
            rbtActivo.setVisibility(View.GONE);
            rbtInactivo.setVisibility(View.GONE);
            txtEstado.setVisibility(View.GONE);
        } else if (metodo.equals("ACTUALIZAR")) {
            idTitlePage.setText("Actualiza Proveedor");
            btnEnviar.setText("Actualizar");

            objActual = (Proveedor) getIntent().getExtras().get("var_objeto");
            txtRazonSocial.setText(objActual.getRazonsocial());
            txtRuc.setText(String.valueOf(objActual.getRuc()));
            txtTelefono.setText(objActual.getTelefono());

            if (objActual.getEstado() == 1) {
                rbtActivo.setChecked(true);
            } else {
                rbtInactivo.setChecked(true);
            }
        }

        /* Razon Social*/
        txtRazonSocial.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String razonSocial = editable.toString();
                if (!razonSocial.matches(ValidacionUtil.RazonSocial)) {
                    txtRazonSocial.setError("Razón Social de Producto no válido, solo letras");
                } else {
                    txtRazonSocial.setError(null);
                }
            }
        });
        /*Ruc*/
        txtRuc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String ruc = editable.toString();
                if (!ruc.matches(ValidacionUtil.RUC)) {
                    txtRuc.setError("Ruc de Proveedor, debe empezar con 10 y debe  contener 11 dígitos");
                } else {
                    txtRuc.setError(null);
                }
            }
        });
        /*Telefono*/
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
                if (!telefono.matches(ValidacionUtil.TELEFONO_PROV)) {
                    txtTelefono.setError("Telefono no valido, solo numeros");
                } else {
                    txtTelefono.setError(null);
                }
            }
        });

        /*Boton Regresar*/
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProveedorCrudFormularioActivity.this, ProveedorCrudListaActivity.class);
                startActivity(intent);
            }
        });

        /*Boton Enviar*/
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validadFormulario()){
                    return;
                }
                String razonSocial = txtRazonSocial.getText().toString();
                String ruc = txtRuc.getText().toString();
                String telefono = txtTelefono.getText().toString();
                String idTipo = spnTipo.getSelectedItem().toString().split(":")[0];
                String idPais = spnPais.getSelectedItem().toString().split(":")[0];

                TipoProveedor objTipo = new TipoProveedor();
                objTipo.setIdTipoProveedor(Integer.parseInt(idTipo.trim()));

                Pais objPais = new Pais();
                objPais.setIdPais(Integer.parseInt(idPais.trim()));


                Proveedor objProveedor = new Proveedor();
                objProveedor.setRazonsocial(razonSocial);
                objProveedor.setRuc(ruc);
                objProveedor.setTelefono(telefono);
                objProveedor.setTipoProveedor(objTipo);
                objProveedor.setPais(objPais);

                if (metodo.equals("REGISTRAR")){
                    objProveedor.setEstado(1);
                    registraValida(objProveedor);
                }else {
                    objProveedor.setEstado(rbtActivo.isChecked() ? 1 : 0);
                    objProveedor.setIdProveedor(objActual.getIdProveedor());
                    actualizaValida(objProveedor);
                }
            }
        });
        cargaPais();
        cargarTipo();
    }

    void registraValida(Proveedor obj){
        Call<List<Proveedor>> call = serviceProveedor.listaProveedorPorRazonSocialIgual(obj.getRazonsocial());
        call.enqueue(new Callback<List<Proveedor>>() {
            @Override
            public void onResponse(Call<List<Proveedor>> call, Response<List<Proveedor>> response) {
                if (response.isSuccessful()){
                    List<Proveedor> lstSalida = response.body();
                    if (lstSalida.isEmpty()){
                        registra(obj);
                    }else{
                        mensajeAlert("El proveedor " + obj.getRazonsocial() +" ya existe ");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Proveedor>> call, Throwable t) {

            }
        });
    }

    void actualizaValida(Proveedor obj){
        Call<List<Proveedor>> call = serviceProveedor.listaProveedorPorRazonSocialIgual(obj.getRazonsocial());
        call.enqueue(new Callback<List<Proveedor>>() {
            @Override
            public void onResponse(Call<List<Proveedor>> call, Response<List<Proveedor>> response) {
                if (response.isSuccessful()){
                    List<Proveedor> lstSalida = response.body();
                    if (lstSalida.isEmpty()){
                        actualiza(obj);
                    }else{
                        mensajeAlert("La Revista " + obj.getRazonsocial() +" ya existe ");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Proveedor>> call, Throwable t) {

            }
        });
    }

    void cargaPais(){
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

    void cargarTipo(){
        Call<List<TipoProveedor>> call = serviceTipo.listaTodos();
        call.enqueue(new Callback<List<TipoProveedor>>() {
            @Override
            public void onResponse(Call<List<TipoProveedor>> call, Response<List<TipoProveedor>> response) {
                List<TipoProveedor> lstAux =  response.body();
                tipo.add(" [ Seleccione ] ");
                for(TipoProveedor aux:lstAux){
                    tipo.add(aux.getIdTipoProveedor() + " : "  + aux.getDescripcion());
                }
                adaptadorTipo.notifyDataSetChanged();
                if (metodo.equals("ACTUALIZAR")){
                    String id = String.valueOf(objActual.getTipoProveedor().getIdTipoProveedor());
                    String descripcion = String.valueOf(objActual.getTipoProveedor().getDescripcion());
                    String row = id + " : " + descripcion;
                    for(int i = 0; i <= tipo.size(); i++ ){
                        if (tipo.get(i).equals(row)){
                            spnTipo.setSelection(i);
                            break;
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<List<TipoProveedor>> call, Throwable t) {

            }
        });
    }

    void registra(Proveedor obj){
        Call<Proveedor> call = serviceProveedor.registra(obj);
        call.enqueue(new Callback<Proveedor>() {
            @Override
            public void onResponse(Call<Proveedor> call, Response<Proveedor> response) {
                if (response.isSuccessful()) {
                    Proveedor objSalida = response.body();
                    mensajeAlert("¡  REGISTRO DE Proveedor EXITOSO  !"
                            + " \n >>>> ID >> " + objSalida.getIdProveedor()
                            + " \n >>> Razón Social >>> " + " \n " + objSalida.getRazonsocial()
                            + " \n >>> Ruc >>> " + " \n " + objSalida.getRuc()
                            + " \n >>> Telefono >>> " + " \n " + objSalida.getTelefono());
                    //Limpiar el formulario
                    limpiarFormulario();
                }
            }

            @Override
            public void onFailure(Call<Proveedor> call, Throwable t) {

            }
        });
    }

    void actualiza(Proveedor obj){
        Call<Proveedor> call = serviceProveedor.actualizaProveedor(obj);
        call.enqueue(new Callback<Proveedor>() {
            @Override
            public void onResponse(Call<Proveedor> call, Response<Proveedor> response) {
                if(response.isSuccessful()){
                    Proveedor objSalida = response.body();
                    mensajeAlert("¡  ACTUALIZACIÓN DE PROVEEDOR EXITOSA  !"
                            + " \n >>>> ID >> " + objSalida.getIdProveedor()
                            + " \n >>> Razón Social >>> " + " \n " + objSalida.getRazonsocial()
                            + " \n >>> Ruc >>> " + " \n " + objSalida.getRuc()
                            + " \n >>> Telefono >>> " + " \n " + objSalida.getTelefono());
                    //Limpiar el formulario
                    limpiarFormulario();
                }
            }

            @Override
            public void onFailure(Call<Proveedor> call, Throwable t) {

            }
        });
    }

    /*Mensajes*/
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

    /*Limpiar formulario*/
    void limpiarFormulario(){
        txtRazonSocial.setText("");
        txtRuc.setText("");
        txtTelefono.setText("");

        if (spnTipo.getAdapter().getCount() > 0) {
            spnTipo.setSelection(0);
        }
        if (spnPais.getAdapter().getCount() > 0) {
            spnPais.setSelection(0);
        }

        txtRazonSocial.setError(null);
        txtRuc.setError(null);
        txtTelefono.setError(null);
    }

    /*Validacion de Campos*/
    private boolean validadFormulario(){
        boolean isValid= true;
        // Validar nombre de revista
        SpannableStringBuilder errorNombre = new SpannableStringBuilder("Este campo es obligatorio");
        errorNombre.setSpan(new ForegroundColorSpan(Color.RED), 0, errorNombre.length(), 0);
        if (txtRazonSocial.getText().toString().trim().isEmpty()) {
            txtRazonSocial.setHint(errorNombre);
            isValid = false;
        } else {
            txtRazonSocial.setHint(null);
        }

        // Validar frecuencia de revista
        SpannableStringBuilder errorFrecuencia = new SpannableStringBuilder("Este campo es obligatorio");
        errorFrecuencia.setSpan(new ForegroundColorSpan(Color.RED), 0, errorFrecuencia.length(), 0);
        if (txtRuc.getText().toString().trim().isEmpty()) {
            txtRuc.setHint(errorFrecuencia);
            isValid = false;
        } else {
            txtRuc.setHint(null);
        }

        // Validar fecha de creacion de revista
        SpannableStringBuilder errorFechaCrea = new SpannableStringBuilder("Este campo es obligatorio");
        errorFechaCrea.setSpan(new ForegroundColorSpan(Color.RED), 0, errorFechaCrea.length(), 0);
        if (txtTelefono.getText().toString().trim().isEmpty()) {
            txtTelefono.setHint(errorFechaCrea);
            isValid = false;
        } else {
            txtTelefono.setHint(null);
        }
        if (spnPais.getSelectedItemPosition() == 0) {
            TextView errorText = (TextView) spnPais.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Seleccione un país");
            isValid = false;
        }

        if (spnTipo.getSelectedItemPosition() == 0) {
            TextView errorText = (TextView) spnTipo.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Seleccione un tipo");
            isValid = false;
        }
        return isValid;
    }
}