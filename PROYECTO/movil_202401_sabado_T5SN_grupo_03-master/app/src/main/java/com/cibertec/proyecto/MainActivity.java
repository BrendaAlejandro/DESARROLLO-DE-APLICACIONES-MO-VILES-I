package com.cibertec.proyecto;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.cibertec.proyecto.service.ServiceCategoria;
import com.cibertec.proyecto.service.ServiceLibro;
import com.cibertec.proyecto.service.ServicePais;
import com.cibertec.proyecto.util.NewAppCompatActivity;

import java.util.ArrayList;


public class MainActivity extends NewAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }


}