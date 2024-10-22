package com.cibertec.proyecto.vista.registra;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cibertec.proyecto.R;

import androidx.core.graphics.Insets;

import com.cibertec.proyecto.entity.Categoria;
import com.cibertec.proyecto.entity.Editorial;
import com.cibertec.proyecto.entity.Pais;
import com.cibertec.proyecto.service.ServiceCategoria;
import com.cibertec.proyecto.service.ServiceEditorial;
import com.cibertec.proyecto.service.ServicePais;
import com.cibertec.proyecto.util.ConnectionRest;
import com.cibertec.proyecto.util.FunctionUtil;
import com.cibertec.proyecto.util.NewAppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditorialRegistraActivity extends NewAppCompatActivity {

    // Pais
    Spinner spnPais;
    ArrayAdapter<String> adaptadorPais;
    ArrayList<String> paises = new ArrayList<>();

    // Categoria
    Spinner spnCategoria;
    ArrayAdapter<String> adaptadorCategoria;
    ArrayList<String> categoria = new ArrayList<>();

    //Servicios
    ServiceEditorial serviceEditorial;
    ServicePais servicePais;
    ServiceCategoria serviceCategoria;

    //Boton
    Button btnRegistrar;

    //Declarar Variables de Edicion
    EditText txtRazonSocial, txtDireccion, txtFecCreacion, txtRUC;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editorial_registrar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.RegistrarEditorial), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        adaptadorPais = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, paises);
        spnPais = findViewById(R.id.cboPaisEditorial);
        spnPais.setAdapter(adaptadorPais);

        adaptadorCategoria = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categoria);
        spnCategoria = findViewById(R.id.cboCategoriaEditorial);
        spnCategoria.setAdapter(adaptadorCategoria);

        servicePais = ConnectionRest.getConnection().create(ServicePais.class);
        serviceEditorial = ConnectionRest.getConnection().create(ServiceEditorial.class);
        serviceCategoria = ConnectionRest.getConnection().create(ServiceCategoria.class);

        cargaPais();
        cargaCategoria();


        txtRazonSocial = findViewById(R.id.itemRazonSocialEditorial);
        txtDireccion = findViewById(R.id.itemDireccionEditorial);
        txtFecCreacion = findViewById(R.id.itemFechaCreacionEditorial);
        txtRUC = findViewById(R.id.itemRucEditorial);


        btnRegistrar = findViewById(R.id.btnRegistrarEditorial);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validarCampos()){
                    registraEditorial();
                }
            }
        });

    }
    boolean validarCampos() {
        String razSoc = txtRazonSocial.getText().toString();
        String direccion = txtDireccion.getText().toString();
        String fecCreacion = txtFecCreacion.getText().toString();
        String ruc = txtRUC.getText().toString();

        if (razSoc.isEmpty() || direccion.isEmpty() || fecCreacion.isEmpty() || ruc.isEmpty()) {
            mensajeToast("Todos los campos son obligatorios.");
            return false;
        }

        if (!razSoc.matches("[a-zA-Z ]{5,45}")) {
            mensajeToast("La Razón Social debe tener entre 5 y 45 caracteres y solo letras.");
            return false;
        }

        if (!ruc.matches("\\d{11}")) {
            mensajeToast("El RUC debe tener 11 números.");
            return false;
        }

        if (direccion.length() < 5 || direccion.length() > 50) {
            mensajeToast("La Dirección debe tener entre 5 y 50 caracteres.");
            return false;
        }

        if (!validarFecha(fecCreacion)) {
            mensajeToast("La Fecha de Creación debe tener el formato DD/MM/AAAA y ser válida.");
            return false;
        }

        if (spnPais.getSelectedItem() == null) {
            mensajeToast("Debe seleccionar un País.");
            return false;
        }

        if (spnCategoria.getSelectedItem() == null) {
            mensajeToast("Debe seleccionar una categoría de Editorial.");
            return false;
        }

        return true;
    }


    void registraEditorial() {
        String razSoc = txtRazonSocial.getText().toString();
        String direccion = txtDireccion.getText().toString();
        String fecCre = txtFecCreacion.getText().toString();
        String ruc = txtRUC.getText().toString();
        String idPais = spnPais.getSelectedItem().toString().split(":")[0];
        String idCategoria = spnCategoria.getSelectedItem().toString().split(":")[0];

        // Convertir fecha al formato YYYY-MM-DD
        String[] partesFecha = fecCre.split("/");
        String fecCreFormat = partesFecha[2] + "-" + partesFecha[1] + "-" + partesFecha[0];

        Pais objPais = new Pais();
        objPais.setIdPais(Integer.parseInt(idPais.trim()));

        Categoria objCategoria = new Categoria();
        objCategoria.setIdCategoria(Integer.parseInt(idCategoria.trim()));

        Editorial objEditorial = new Editorial();
        objEditorial.setRazonSocial(razSoc);
        objEditorial.setDireccion(direccion);
        objEditorial.setFechaCreacion(fecCreFormat); // Usar fecha en formato YYYY-MM-DD
        objEditorial.setRuc(ruc);
        objEditorial.setPais(objPais);
        objEditorial.setCategoria(objCategoria);
        objEditorial.setFechaRegistro(FunctionUtil.getFechaActualStringDateTime());
        objEditorial.setEstado(1);

        registraValida(objEditorial);
    }

    void registraValida(Editorial obj){
        Call<List<Editorial>> call  = serviceEditorial.listaPorNombre(obj.getRazonSocial());

        call.enqueue(new Callback<List<Editorial>>() {
            @Override
            public void onResponse(Call<List<Editorial>> call, Response<List<Editorial>> response) {
                if (response.isSuccessful()){
                    List<Editorial> lstSalida = response.body();
                    if (lstSalida.isEmpty()){
                        registra(obj);
                    }else{
                        mensajeAlert("La Razon Social " + obj.getRazonSocial() +" ya existe ");
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Editorial>> call, Throwable t) {
            }
        });
    }

    void cargaPais() {
        Call<List<Pais>> call = servicePais.listaTodos();
        call.enqueue(new Callback<List<Pais>>() {
            @Override
            public void onResponse(Call<List<Pais>> call, Response<List<Pais>> response) {
                if (response.isSuccessful()) {
                    List<Pais> lst = response.body();
                    for (Pais obj : lst) {
                        paises.add(obj.getIdPais() + " : " + obj.getNombre());
                    }
                    adaptadorPais.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Pais>> call, Throwable t) {
                mensajeToast("Error al cargar los países.");


            }
        });

    }




    boolean validarFecha(String fecha) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dateFormat.setLenient(false);

        try {
            // Parseamos la fecha
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat.parse(fecha));

            // Validamos que los valores del día, mes y año sean correctos
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH) + 1; // Los meses son indexados desde 0
            int year = calendar.get(Calendar.YEAR);

            // Verificar el rango del año
            if (year < 1900 || year > Calendar.getInstance().get(Calendar.YEAR)) {
                return false;
            }

            // Si la fecha es válida, retornamos true
            return true;

        } catch (ParseException e) {
            // Si la fecha no es válida, retornamos false
            return false;
        }
    }

    void cargaCategoria() {
        Call<List<Categoria>> call = serviceCategoria.listaCategoria();
        call.enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if (response.isSuccessful()) {
                    List<Categoria> lst = response.body();
                    for (Categoria obj : lst) {

                        categoria.add(obj.getIdCategoria() + " : " + obj.getDescripcion());
                    }

                    adaptadorCategoria.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {
                mensajeToast("Error al cargar las categorìas de editorial.");
            }
        });


    }


    void registra(Editorial objEditorial) {
        Call<Editorial> call = serviceEditorial.registra(objEditorial);
        call.enqueue(new Callback<Editorial>() {
            @Override
            public void onResponse(Call<Editorial> call, Response<Editorial> response) {
                if (response.isSuccessful()) {
                    Editorial objSalida = response.body();
                    mensajeAlert("Registro de Editorial exitoso: "
                            + " \n >>>> ID >> " + objSalida.getIdEditorial()
                            + " \n >>> Razón Social >>> " + objSalida.getRazonSocial());

                    //Colocar limpiar formulari
                    limpiarFormularioEditorial();
                } else {
                    mensajeToast("Error en el registro: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Editorial> call, Throwable t) {
                mensajeToast("Error al registrar el Editorial: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void limpiarFormularioEditorial() {
        // Limpiar campos de texto
        txtRazonSocial.setText("");
        txtDireccion.setText("");
        txtFecCreacion.setText("");
        txtRUC.setText("");

        // Restablecer los spinners
        if (spnPais.getAdapter().getCount() > 0) {
            spnPais.setSelection(0);
        }
        if (spnCategoria.getAdapter().getCount() > 0) {
            spnCategoria.setSelection(0);
        }

        // Eliminar mensajes de error
        txtRazonSocial.setError(null);
        txtDireccion.setError(null);
        txtFecCreacion.setError(null);
        txtRUC.setError(null);
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