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

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cibertec.proyecto.R;
import com.cibertec.proyecto.entity.Pais;
import com.cibertec.proyecto.entity.Proveedor;
import com.cibertec.proyecto.entity.TipoProveedor;
import com.cibertec.proyecto.service.ServicePaisProveedor;
import com.cibertec.proyecto.service.ServiceProveedor;
import com.cibertec.proyecto.service.ServiceTipoProveedor;
import com.cibertec.proyecto.util.ConnectionRest;
import com.cibertec.proyecto.util.FunctionUtil;
import com.cibertec.proyecto.util.NewAppCompatActivity;
import com.cibertec.proyecto.util.ValidacionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProveedorRegistraActivity extends NewAppCompatActivity {

    //Pais
    Spinner spnPais;
    ArrayAdapter<String> adaptadorPais;
    ArrayList<String> paises = new ArrayList<>();

    //Tipo
    Spinner spnTipo;
    ArrayAdapter<String> adaptadorTipo;
    ArrayList<String> tipos = new ArrayList<>();

    //Servicio
    ServiceProveedor serviceProveedor;
    ServiceTipoProveedor serviceTipoProveedor;
    ServicePaisProveedor servicePaisProveedor;

    Button btnRegistra;

    EditText txtRazonSocial, txtRuc, txtTelefono;

    //RUC
    public static final String RUC = "[0-9]{11}";
    //Telefono
    public static final String TELEFONO = "[0-9]{9}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proveedor_registra);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.RegProveedor), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        adaptadorPais = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, paises);
        spnPais = findViewById(R.id.spnRegPaisProveedor);
        spnPais.setAdapter(adaptadorPais);

        adaptadorTipo = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, tipos);
        spnTipo= findViewById(R.id.spnRegTipoProveedor);
        spnTipo.setAdapter(adaptadorTipo);

        servicePaisProveedor = ConnectionRest.getConnection().create(ServicePaisProveedor.class);
        serviceProveedor = ConnectionRest.getConnection().create(ServiceProveedor.class);
        serviceTipoProveedor = ConnectionRest.getConnection().create(ServiceTipoProveedor.class);

        cargaPais();
        cargaTipo();

        txtRazonSocial = findViewById(R.id.txtRegRazonSocialProveedor);
        txtRuc = findViewById(R.id.txtRegRucProveedor);
        txtTelefono = findViewById(R.id.txtRegTelefonoProveedor);

        btnRegistra = findViewById(R.id.btnRegEnviarProveedor);
        //Botón registrar
        //Botón registrar
        btnRegistra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    registrarProveedor();
                }
            }
        });

        txtTelefono.setOnClickListener(view -> showDatePickerDialog());
    }

    private void showDatePickerDialog() {
    }

    private void verificarRazonSocial(String razonSocial) {
        Call<List<Proveedor>> call = serviceProveedor.listaPorRazonSocial(razonSocial);
        call.enqueue(new Callback<List<Proveedor>>() {
            @Override
            public void onResponse(Call<List<Proveedor>> call, Response<List<Proveedor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Proveedor> editoriales = response.body();
                    if (!editoriales.isEmpty()) {
                        mensajeToast("La razón social ya existe. Coloque una nueva Razón Social.");
                    } else {
                        registrarProveedor();
                    }
                } else {
                    mensajeToast("Se detecto un error al verificar la razón social.");
                }
            }
            @Override
            public void onFailure(Call<List<Proveedor>> call, Throwable t) {
                mensajeToast("Se detecto un error al verificar la razón social: " + t.getMessage());
            }
        });
    }


    void registraValida(Proveedor obj){
        Call<List<Proveedor>> call  = serviceProveedor.listaProveedorPorRazonSocialIgual(obj.getRazonsocial());

        call.enqueue(new Callback<List<Proveedor>>() {
            @Override
            public void onResponse(Call<List<Proveedor>> call, Response<List<Proveedor>> response) {
                if (response.isSuccessful()){
                    List<Proveedor> lstSalida = response.body();
                    if (lstSalida.isEmpty()){
                        registra(obj);
                    }else{
                        mensajeAlert("La Razón Social " + obj.getRazonsocial() +" ya existe ");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Proveedor>> call, Throwable t) {

            }
        });
    }



    private void registrarProveedor() {
        String razonSocial = txtRazonSocial.getText().toString();
        String ruc = txtRuc.getText().toString();
        String telefono = txtTelefono.getText().toString();
        String idPais = spnPais.getSelectedItem().toString().split(":")[0];
        String idTipo = spnTipo.getSelectedItem().toString().split(":")[0];

        Pais objPais = new Pais();
        objPais.setIdPais(Integer.parseInt(idPais.trim()));

        TipoProveedor objTipo = new TipoProveedor();
        objTipo.setIdTipoProveedor(Integer.parseInt(idTipo.trim()));

        Proveedor objProveedor = new Proveedor();
        objProveedor.setRazonsocial(razonSocial);
        objProveedor.setRuc(ruc);
        objProveedor.setTelefono(telefono);
        objProveedor.setPais(objPais);
        objProveedor.setTipoProveedor(objTipo);
        objProveedor.setFechaRegistro(FunctionUtil.getFechaActualStringDateTime());
        objProveedor.setEstado(1);


        registraValida(objProveedor);

    }

    void registra( Proveedor objProveedor) {
        Call<Proveedor> call = serviceProveedor.registra(objProveedor);
        call.enqueue(new Callback<Proveedor>() {
            @Override
            public void onResponse(Call<Proveedor> call, Response<Proveedor> response) {
                if (response.isSuccessful()){
                    Proveedor objSalida = response.body();
                    mensajeAlert(" Registro de Proveedor exitoso:  "
                            + " \n >>>> ID >> " + objSalida.getIdProveedor()
                            + " \n >>> Razón Social >>> " +  objSalida.getRazonsocial());

                    limpiar();
                }
            }

            @Override
            public void onFailure(Call<Proveedor> call, Throwable t) {
                mensajeToast("Se detecto un error al verificar la razón social: " + t.getMessage());
            }
        });
    }

    private boolean validateInputs() {
        String razonSocial = txtRazonSocial.getText().toString().trim();
        String ruc = txtRuc.getText().toString().trim();
        String telefono = txtTelefono.getText().toString().trim();

        if (!Pattern.matches(ValidacionUtil.RazonSocial, razonSocial)) {
            mensajeToast("Razón social debe tener entre 4 a 40 letras.");
            return false;
        }

        if (!ruc.matches("^10\\d{9}$")) {
            mensajeToast("El RUC debe comenzar con 10 y debe contar con 11 dígitos.");
            return false;
        }

        if (!Pattern.matches(ValidacionUtil.TELEFONO, telefono)) {
            mensajeToast("El télefono debe tener 9 dígitos.");
            return false;
        }



        return true;
    }


    private void cargaTipo() {
        Call<List<TipoProveedor>>  call = serviceTipoProveedor.listaTodos();
        call.enqueue(new Callback<List<TipoProveedor>>() {
            @Override
            public void onResponse(Call<List<TipoProveedor>> call, Response<List<TipoProveedor>> response) {
                if (response.isSuccessful()){
                    List<TipoProveedor> lst =  response.body();
                    for (TipoProveedor obj :lst){
                        tipos.add( obj.getIdTipoProveedor()+ " : " + obj.getDescripcion() );
                    }
                    adaptadorTipo.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<TipoProveedor>> call, Throwable t) {

            }
        });
    }

    private void cargaPais() {
        Call<List<Pais>> call = servicePaisProveedor.listaTodos();
        call.enqueue(new Callback<List<Pais>>() {
            @Override
            public void onResponse(Call<List<Pais>> call, Response<List<Pais>> response) {
                if (response.isSuccessful()){
                    List<Pais> lst =  response.body();
                    for (Pais obj :lst) {
                        paises.add(obj.getIdPais() + " : " + obj.getNombre());
                    }
                    adaptadorPais.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Pais>> call, Throwable t) {

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

    private void limpiar() {
        txtRazonSocial.setText("");
        txtRuc.setText("");
        txtTelefono.setText("");
        spnPais.setSelection(0);
        spnTipo.setSelection(0);
    }
}