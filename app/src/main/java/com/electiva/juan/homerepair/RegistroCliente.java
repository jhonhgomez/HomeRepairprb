package com.electiva.juan.homerepair;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.electiva.juan.dao.HomeRepairContract;
import com.electiva.juan.dao.HomeRepairDbHelper;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegistroCliente extends Activity {

    private HomeRepairDbHelper mDbHelper;
    private SQLiteDatabase mydb;
    private Cursor allrows;
    private EditText editTextUser;
    private EditText editTextPassword;
    private EditText editTextFirstNames;
    private EditText editTextLastNames;
    private EditText editTextEmail;
    private EditText editTextTelephone;
    private EditText editTextBirth;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private Calendar calendar;
    private Intent intent;
    private final Serializable serializable = 3L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_cliente);
        editTextUser = (EditText) findViewById(R.id.edtTxtRegUsuario);
        editTextPassword = (EditText) findViewById(R.id.edtTxtRegContraseña);
        editTextFirstNames = (EditText) findViewById(R.id.edtTxtRegNombres);
        editTextLastNames = (EditText) findViewById(R.id.edtTxtRegApellidos);
        editTextEmail = (EditText) findViewById(R.id.edtTxtRegEmail);
        editTextTelephone = (EditText) findViewById(R.id.edtTxtRegTelefono);
        editTextBirth = (EditText) findViewById(R.id.edtTxtRegFenaciClien);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        calendar = Calendar.getInstance();
        editTextBirth.setInputType(InputType.TYPE_NULL);
        editTextBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == editTextBirth) datePickerDialog.show();
            }
        });
        editTextBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) datePickerDialog.show();

            }
        });
        setDateTimeField();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registro_usuario, menu);
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
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editTextBirth.setText(dateFormatter.format(newDate.getTime()));
            }

        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    public void createUser(View view){
        try {
            if (!valideData()){
                if (getTableValues()){
                    insertRegistry();
                    intent = new Intent(this, Principal.class);
                    intent.putExtra("USER", editTextUser.getText().toString().toUpperCase());
                    startActivity(intent);
                    Registro.activity.finish();
                    Acceso.activity.finish();
                    finish();
                }
                else throw new Exception("Usuario ya se encuentra registrado");
            }
            else throw new Exception("Todos los campos son obligatorios");
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public boolean valideData() throws Exception{
        return (editTextUser.getText().toString().isEmpty()
                || editTextPassword.getText().toString().isEmpty()
                || editTextFirstNames.getText().toString().isEmpty()
                || editTextLastNames.getText().toString().isEmpty()
                || editTextEmail.getText().toString().isEmpty()
                || editTextTelephone.getText().toString().isEmpty()
                || editTextBirth.getText().toString().isEmpty());
    }

    public boolean getTableValues() throws Exception {
        boolean userExist = false;
        mydb = openOrCreateDatabase(mDbHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);
        allrows = mydb.rawQuery("SELECT * FROM " + HomeRepairContract.HomeRepairUser.TABLE_NAME + " WHERE " +
                HomeRepairContract.HomeRepairUser.COLUMN_NAME_USER + " = " + "\"" + editTextUser.getText().toString().toUpperCase() + "\" AND " +
                HomeRepairContract.HomeRepairUser.COLUMN_NAME_PASSWORD + " = " + "\"" + editTextPassword.getText().toString() + "\"", null);
        if (allrows.moveToFirst()) userExist = false;
        else userExist = true;
        allrows.close();
        mydb.close();
        return userExist;
    }

    public void insertRegistry() throws Exception{
        mydb = openOrCreateDatabase(mDbHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);
        mydb.execSQL("INSERT INTO " + HomeRepairContract.HomeRepairUser.TABLE_NAME + " VALUES (" +
                "NULL," + "\"" + editTextUser.getText().toString().toUpperCase().trim() + "\"," + "\"" + editTextPassword.getText().toString().trim() + "\"," + serializable + ")");
        mydb.execSQL("INSERT INTO " + HomeRepairContract.HomeRepairClient.TABLE_NAME + " VALUES (" +
                "NULL," + "\"" + editTextFirstNames.getText().toString().toUpperCase().trim() + "\"," + "\"" + editTextLastNames.getText().toString().toUpperCase().trim() + "\"," +
                "\"" + editTextEmail.getText().toString().toLowerCase().trim() + "\"," + editTextTelephone.getText().toString().trim() + "," +
                "\"" + editTextBirth.getText().toString().trim() + "\"," + "\"" + editTextUser.getText().toString().toUpperCase().trim() + "\")");
        mydb.close();
        Toast.makeText(this, "Usuario creado", Toast.LENGTH_SHORT).show();
    }
}