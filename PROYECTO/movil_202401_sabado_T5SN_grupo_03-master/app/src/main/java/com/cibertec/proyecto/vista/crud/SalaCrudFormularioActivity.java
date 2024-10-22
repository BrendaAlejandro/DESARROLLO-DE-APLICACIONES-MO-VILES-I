package com.cibertec.proyecto.vista.crud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalaCrudFormularioActivity extends AppCompatActivity {

    // Sede
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

    EditText txtNumero, txtPiso, txtNumAlumnos, txtRecursos;

    Button btnEnviar, btnRegresar;

    TextView idTitlePage;

    String metodo;

    Sala objActual;

    RadioButton rbtActivo, rbtInactivo;
    TextView txtEstado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sala_crud_formulario);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.CrudSala), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        rbtActivo = findViewById(R.id.rbtActivo);
        rbtInactivo = findViewById(R.id.rbtInactivo);
        txtEstado = findViewById(R.id.txtEstado);

        serviceSede = ConnectionRest.getConnection().create(ServiceSedeSala.class);
        serviceSala = ConnectionRest.getConnection().create(ServiceSala.class);
        serviceModalidadSala = ConnectionRest.getConnection().create(ServiceModalidadSala.class);

        adaptadorSede = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, sedes);
        spnSede = findViewById(R.id.spnRegSalSede);
        spnSede.setAdapter(adaptadorSede);

        adaptadorModalidad = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, modalidades);
        spnModalidad = findViewById(R.id.spnRegSalModalidad);
        spnModalidad.setAdapter(adaptadorModalidad);

        txtNumero = findViewById(R.id.txtRegSalNumero);
        txtPiso = findViewById(R.id.txtRegSalPiso);
        txtNumAlumnos = findViewById(R.id.txtRegSalNumAlumnos);
        txtRecursos = findViewById(R.id.txtRegSalRecursos);

        metodo = (String) getIntent().getExtras().get("var_metodo");

        idTitlePage = findViewById(R.id.idTitlePage);
        btnEnviar = findViewById(R.id.btnRegSalEnviar);
        btnRegresar = findViewById(R.id.btnRegSalRegresar);

        if (metodo.equals("REGISTRAR")) {
            idTitlePage.setText("Registra Sala");
            btnEnviar.setText("Registrar");
            rbtActivo.setVisibility(View.GONE);
            rbtInactivo.setVisibility(View.GONE);
            txtEstado.setVisibility(View.GONE);
        } else if (metodo.equals("ACTUALIZAR")) {
            idTitlePage.setText("Actualiza Sala");
            btnEnviar.setText("Actualizar");

            objActual = (Sala) getIntent().getExtras().get("var_objeto");
            txtNumero.setText(objActual.getNumero());
            txtPiso.setText(String.valueOf(objActual.getPiso()));
            txtNumAlumnos.setText(String.valueOf(objActual.getNumAlumnos()));
            txtRecursos.setText(objActual.getRecursos());

            if (objActual.getEstado() == 1) {
                rbtActivo.setChecked(true);
            } else {
                rbtInactivo.setChecked(true);
            }
        }

        btnRegresar.setOnClickListener(v -> {
            Intent intent = new Intent(SalaCrudFormularioActivity.this, SalaCrudListaActivity.class);
            startActivity(intent);
        });

        btnEnviar.setOnClickListener(v -> {
            String numero = txtNumero.getText().toString().trim();
            String piso = txtPiso.getText().toString().trim();
            String numAlumnos = txtNumAlumnos.getText().toString().trim();
            String recursos = txtRecursos.getText().toString().trim();
            String idSede = spnSede.getSelectedItem().toString().split(":")[0].trim();
            String idModalidad = spnModalidad.getSelectedItem().toString().split(":")[0].trim();

            Sede objSede = new Sede();
            objSede.setIdSede(Integer.parseInt(idSede));

            Modalidad objModalidad = new Modalidad();
            objModalidad.setIdModalidad(Integer.parseInt(idModalidad));

            // Validaciones
            if (numero.isEmpty() || !numero.matches("[A-Z]{1}[0-9]{3}")) {
                mensajeAlert("Número: Ingrese el número con una letra mayúscula seguida de 3 números");
            } else if (piso.isEmpty() || !piso.matches("[1-9]|10")) {
                mensajeAlert("Campo Piso: Ingrese un número de piso entre 1 y 10");
            } else if (numAlumnos.isEmpty() || !numAlumnos.matches("[1-9][0-9]?|10[0-5]")) {
                mensajeAlert("Campo número de alumnos: Ingrese un número de alumnos entre 1 y 105");
            } else if (recursos.isEmpty() || !recursos.matches("[\\p{L}\\p{M} ,]{3,60}")) {
                mensajeAlert("Campo Recursos: Ingrese los recursos con longitud entre 3 y 60 caracteres, permitiendo letras, espacios en blanco y comas");
            } else {
                Sala objSala = new Sala();
                objSala.setNumero(numero);
                objSala.setPiso(Integer.parseInt(piso));
                objSala.setNumAlumnos(Integer.parseInt(numAlumnos));
                objSala.setRecursos(recursos);
                objSala.setSede(objSede);
                objSala.setModalidad(objModalidad);
                objSala.setFechaRegistro(FunctionUtil.getFechaActualStringDateTime());

                if (metodo.equals("REGISTRAR")) {
                    objSala.setEstado(1);
                    registraValida(objSala);
                } else if (metodo.equals("ACTUALIZAR")) {
                    objSala.setEstado(rbtActivo.isChecked() ? 1 : 0);
                    objSala.setIdSala(objActual.getIdSala());
                    actualiza(objSala);
                }
            }
        });

        cargaSede();
        cargaModalidad();
    }

    void registraValida(Sala obj) {
        Call<List<Sala>> call = serviceSala.listaPorNumeroIgual(obj.getNumero());
        call.enqueue(new Callback<List<Sala>>() {
            @Override
            public void onResponse(Call<List<Sala>> call, Response<List<Sala>> response) {
                if (response.isSuccessful()) {
                    List<Sala> lstSalida = response.body();
                    if (lstSalida.isEmpty()) {
                        registra(obj);
                    } else {
                        mensajeAlert("La sala " + obj.getNumero() + " ya existe");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Sala>> call, Throwable t) {
                mensajeAlert("Error al verificar el número de sala: " + t.getMessage());
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
                    if (metodo.equals("ACTUALIZAR")) {
                        String id = String.valueOf(objActual.getSede().getIdSede());
                        String nombre = String.valueOf(objActual.getSede().getNombre());
                        String row = id + " : " + nombre;
                        for (int i = 0; i <= sedes.size(); i++) {
                            if (sedes.get(i).equals(row)) {
                                spnSede.setSelection(i);
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Sede>> call, Throwable t) {
                mensajeAlert("Error al cargar las sedes: " + t.getMessage());
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
                    if (metodo.equals("ACTUALIZAR")) {
                        String id = String.valueOf(objActual.getModalidad().getIdModalidad());
                        String nombre = String.valueOf(objActual.getModalidad().getDescripcion());
                        String row = id + " : " + nombre;
                        for (int i = 0; i <= modalidades.size(); i++) {
                            if (modalidades.get(i).equals(row)) {
                                spnModalidad.setSelection(i);
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Modalidad>> call, Throwable t) {
                mensajeAlert("Error al cargar las modalidades: " + t.getMessage());
            }
        });
    }

    void actualiza(Sala obj) {
        Call<Sala> call = serviceSala.actualizaSala(obj);
        call.enqueue(new Callback<Sala>() {
            @Override
            public void onResponse(Call<Sala> call, Response<Sala> response) {
                if (response.isSuccessful()) {
                    Sala objSalida = response.body();
                    mensajeAlert("Se actualizó la sala: " + objSalida.getNumero());
                }
            }

            @Override
            public void onFailure(Call<Sala> call, Throwable t) {
                mensajeAlert("Error al actualizar la sala: " + t.getMessage());
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
                    mensajeAlert(" Registro de de Sala exitosa:  "
                            + " \n >>>> ID >> " + objSalida.getIdSala()
                            + " \n >>> Número >>> " +  objSalida.getNumero());
                }
            }

            @Override
            public void onFailure(Call<Sala> call, Throwable t) {
            }
        });
    }

    void mensajeAlert(String mensaje) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(mensaje);
        alertDialog.setCancelable(true);
        alertDialog.show();
    }
}

