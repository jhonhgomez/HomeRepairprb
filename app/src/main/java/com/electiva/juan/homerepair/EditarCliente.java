package com.electiva.juan.homerepair;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.electiva.juan.dao.HomeRepairContract;
import com.electiva.juan.dao.HomeRepairDbHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Juan on 28/11/2015.
 */
public class EditarCliente extends Activity {

    private Bundle extras;
    private HomeRepairDbHelper mDbHelper;
    private SQLiteDatabase mydb;
    private Cursor allrows;
    private ArrayList<String> arrayDb;
    private EditText edtTxtRegNombres;
    private EditText edtTxtRegApellidos;
    private EditText edtTxtRegEmail;
    private EditText edtTxtRegTelefono;
    private EditText edtTxtRegFenaciClien;
    private EditText edtTxtRegUsuario;
    private EditText edtTxtRegContraseña;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private Calendar calendar;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_cliente);
        extras = getIntent().getExtras();
        edtTxtRegNombres = (EditText)findViewById(R.id.edtTxtRegNombres);
        edtTxtRegApellidos = (EditText)findViewById(R.id.edtTxtRegApellidos);
        edtTxtRegEmail = (EditText)findViewById(R.id.edtTxtRegEmail);
        edtTxtRegTelefono = (EditText)findViewById(R.id.edtTxtRegTelefono);
        edtTxtRegFenaciClien = (EditText)findViewById(R.id.edtTxtRegFenaciClien);
        edtTxtRegUsuario = (EditText)findViewById(R.id.edtTxtRegUsuario);
        edtTxtRegContraseña = (EditText)findViewById(R.id.edtTxtRegContraseña);
        getTableValues();
        edtTxtRegUsuario.setVisibility(View.INVISIBLE);
        edtTxtRegContraseña.setVisibility(View.INVISIBLE);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        calendar = Calendar.getInstance();
        edtTxtRegFenaciClien.setInputType(InputType.TYPE_NULL);
        edtTxtRegFenaciClien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == edtTxtRegFenaciClien) datePickerDialog.show();
            }
        });
        edtTxtRegFenaciClien.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) datePickerDialog.show();

            }
        });
        setDateTimeField();
    }

    public void createUser(View view){
        try {
            if (!valideData()){
                updateRegistry();
                intent = new Intent(this, Principal.class);
                intent.putExtra("USER", extras.getString("USER"));
                Principal.activity.finish();
                startActivity(intent);
                finish();
            }
            else throw new Exception("Todos los campos son obligatorios");
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void updateRegistry() throws Exception{
        mydb = openOrCreateDatabase(mDbHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);
        mydb.execSQL("UPDATE " + HomeRepairContract.HomeRepairClient.TABLE_NAME + " SET " +
                HomeRepairContract.HomeRepairClient.COLUMN_NAME_FIRSTNAME + " = \"" + edtTxtRegNombres.getText().toString().toUpperCase().trim() + "\"," +
                HomeRepairContract.HomeRepairClient.COLUMN_NAME_LASTNAME + " = \"" + edtTxtRegApellidos.getText().toString().toUpperCase().trim() + "\"," +
                HomeRepairContract.HomeRepairClient.COLUMN_NAME_EMAIL + " = \"" + edtTxtRegEmail.getText().toString().toLowerCase().trim() + "\"," +
                HomeRepairContract.HomeRepairClient.COLUMN_NAME_TELEPHONE + " = " + edtTxtRegTelefono.getText().toString().trim() + "," +
                HomeRepairContract.HomeRepairClient.COLUMN_NAME_BIRTH + " = \"" + edtTxtRegFenaciClien.getText().toString().trim() + "\" WHERE " +
                HomeRepairContract.HomeRepairClient.COLUMN_NAME_USER + " = \"" + extras.getString("USER") + "\"");
        mydb.close();
        Toast.makeText(this, "Informacion modificada", Toast.LENGTH_SHORT).show();
    }

    public boolean valideData() throws Exception{
        return (edtTxtRegNombres.getText().toString().isEmpty()
                || edtTxtRegApellidos.getText().toString().isEmpty()
                || edtTxtRegEmail.getText().toString().isEmpty()
                || edtTxtRegTelefono.getText().toString().isEmpty()
                || edtTxtRegFenaciClien.getText().toString().isEmpty());
    }

    private void setDateTimeField() {
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                edtTxtRegFenaciClien.setText(dateFormatter.format(newDate.getTime()));
            }

        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    public void getTableValues() {
        ArrayList<String> arrayDb = new ArrayList<String>();
        try {
            mydb = openOrCreateDatabase(mDbHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);
            allrows = mydb.rawQuery("SELECT * FROM "+ HomeRepairContract.HomeRepairClient.TABLE_NAME + " WHERE " +
                    HomeRepairContract.HomeRepairClient.COLUMN_NAME_USER + " = " + "\"" + extras.getString("USER") + "\"", null);
            if (allrows.moveToFirst()) {
                edtTxtRegNombres.setText(allrows.getString(1));
                edtTxtRegApellidos.setText(allrows.getString(2));
                edtTxtRegEmail.setText(allrows.getString(3));
                edtTxtRegTelefono.setText(allrows.getString(4));
                edtTxtRegFenaciClien.setText(allrows.getString(5));
            }
            allrows.close();
            mydb.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error consultando la informacion del usuario",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
