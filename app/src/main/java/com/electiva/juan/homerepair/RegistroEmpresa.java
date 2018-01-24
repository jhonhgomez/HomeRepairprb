package com.electiva.juan.homerepair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.electiva.juan.dao.HomeRepairContract;
import com.electiva.juan.dao.HomeRepairDbHelper;

import java.io.Serializable;
import java.util.ArrayList;

public class RegistroEmpresa extends Activity {

    private Spinner spinner;
    private HomeRepairDbHelper mDbHelper;
    private SQLiteDatabase mydb;
    private ArrayList<String> arrayDb;
    private ArrayAdapter adapterDb;
    private Cursor allrows;
    private EditText editTextUser;
    private EditText editTextPassword;
    private EditText editTextNomEmpresa;
    private EditText editIdEmpresa;
    private EditText editTelEmpresa;
    private Intent services;
    private final Serializable serializable = 1L;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_empresa);
        activity = this;
        editTextUser = (EditText)findViewById(R.id.edtTxtRegUsuarioEmpresa);
        editTextPassword = (EditText)findViewById(R.id.edtTxtRegContraseñaEmpresa);
        editTextNomEmpresa = (EditText)findViewById(R.id.edtNomEmpresa);
        editIdEmpresa = (EditText)findViewById(R.id.edtIdEmpresa);
        editTelEmpresa = (EditText)findViewById(R.id.edtTelEmpresa);
        spinner = (Spinner) findViewById(R.id.spinActividad);
        arrayDb = getTableValues();
        adapterDb = new ArrayAdapter(this, R.layout.spinner_item, arrayDb);
        spinner.setAdapter(adapterDb);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registro_empresa, menu);
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

    public ArrayList<String> getTableValues() {
        ArrayList<String> arrayDb = new ArrayList<String>();
        try {
            mydb = openOrCreateDatabase(mDbHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);
            allrows = mydb.rawQuery("SELECT * FROM "+ HomeRepairContract.HomeRepairEconomic.TABLE_NAME + " ORDER BY "
                    + HomeRepairContract.HomeRepairEconomic.COLUMN_NAME_ACTIVITY, null);
            if (allrows.moveToFirst()) {
                do {
                    arrayDb.add(allrows.getString(1));
                } while (allrows.moveToNext());
            }
            allrows.close();
            mydb.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error consultando las actividades economicas",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return arrayDb;
    }

    public void registryServices(View view) {
        try {
            if (!valideData()){
                services = new Intent(this, RegistroServicios.class);
                services.putExtra("USER", editTextUser.getText().toString().toUpperCase());
                services.putExtra("PASSWORD", editTextPassword.getText().toString());
                services.putExtra("ROLE", serializable);
                services.putExtra("NAME", editTextNomEmpresa.getText().toString().toUpperCase());
                services.putExtra("NIT", editIdEmpresa.getText().toString().toUpperCase());
                services.putExtra("TELEPHONE", editTelEmpresa.getText().toString().toUpperCase());
                services.putExtra("ACTIVITY", spinner.getSelectedItem().toString().toUpperCase());
                startActivity(services);
            }
            else throw new Exception("Todos los campos son obligatorios");
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public boolean valideData() throws Exception{
        return (editTextUser.getText().toString().isEmpty()
                ||editTextPassword.getText().toString().isEmpty()
                ||editTextNomEmpresa.getText().toString().isEmpty()
                ||editIdEmpresa.getText().toString().isEmpty()
                ||editTelEmpresa.getText().toString().isEmpty()
                ||spinner.getSelectedItem().toString().isEmpty());
    }
}
