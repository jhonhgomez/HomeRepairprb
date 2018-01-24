package com.electiva.juan.homerepair;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegistroProfesional extends Activity{

    private EditText edtTxtRegFenaciProfe;
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private Calendar calendar;
    private EditText editTextUser;
    private EditText editTextPassword;
    private final Serializable serializable = 2L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_profesional);
        editTextUser = (EditText)findViewById(R.id.edtTxtRegUsuarioProfe);
        editTextPassword = (EditText)findViewById(R.id.edtTxtRegContraseñaProfe);
        edtTxtRegFenaciProfe = (EditText) findViewById(R.id.edtTxtRegFenaciProfe);
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        calendar = Calendar.getInstance();
        edtTxtRegFenaciProfe.setInputType(InputType.TYPE_NULL);
        edtTxtRegFenaciProfe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == edtTxtRegFenaciProfe) fromDatePickerDialog.show();
            }
        });
        edtTxtRegFenaciProfe.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) fromDatePickerDialog.show();

            }
        });
        setDateTimeField();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registro_profesional, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setDateTimeField() {
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                edtTxtRegFenaciProfe.setText(dateFormatter.format(newDate.getTime()));
            }

        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    public void registryServices(View view) {
        //Intent para la creacion de cuentas
        Intent services = new Intent(this, RegistroServicios.class);
        services.putExtra("USER", editTextUser.getText().toString().toUpperCase());
        services.putExtra("PASSWORD", editTextPassword.getText().toString().toUpperCase());
        services.putExtra("ROLE", serializable);
        startActivity(services);
    }
}
