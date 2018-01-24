package com.electiva.juan.homerepair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.electiva.juan.dao.HomeRepairContract;
import com.electiva.juan.dao.HomeRepairDbHelper;

import java.util.ArrayList;

/**
 * Created by Juan on 28/11/2015.
 */
public class EditarEmpresa extends Activity {

    private HomeRepairDbHelper mDbHelper;
    private SQLiteDatabase mydb;
    private Cursor allrows;
    private ArrayList<String> arrayDb;
    private ArrayList<String> arrayDbUpperCase;
    private Bundle extras;
    private EditText edtTxtRegUsuarioEmpresa;
    private EditText edtTxtRegContraseñaEmpresa;
    private EditText edtNomEmpresa;
    private EditText edtIdEmpresa;
    private EditText edtTelEmpresa;
    private Spinner spinActividad;
    private ArrayAdapter adapterDb;
    private ArrayAdapter adapterDbUpperCase;
    private TextView textEditaEmpresa;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_empresa);
        extras = getIntent().getExtras();
        edtTxtRegUsuarioEmpresa = (EditText)findViewById(R.id.edtTxtRegUsuarioEmpresa);
        edtTxtRegContraseñaEmpresa = (EditText)findViewById(R.id.edtTxtRegContraseñaEmpresa);
        edtNomEmpresa = (EditText)findViewById(R.id.edtNomEmpresa);
        edtIdEmpresa = (EditText)findViewById(R.id.edtIdEmpresa);
        edtTelEmpresa = (EditText)findViewById(R.id.edtTelEmpresa);
        spinActividad = (Spinner)findViewById(R.id.spinActividad);
        textEditaEmpresa = (TextView)findViewById(R.id.textEditaEmpresa);
        textEditaEmpresa.setText("Finalizar");
        edtTxtRegUsuarioEmpresa.setVisibility(View.INVISIBLE);
        edtTxtRegContraseñaEmpresa.setVisibility(View.INVISIBLE);
        arrayDbUpperCase = new ArrayList<String>();
        arrayDb = getTableValuesEconomic();
        adapterDb = new ArrayAdapter(this, R.layout.spinner_item, arrayDb);
        adapterDbUpperCase = new ArrayAdapter(this, R.layout.spinner_item, arrayDbUpperCase);
        spinActividad.setAdapter(adapterDb);
        getTableValues();
    }

    public ArrayList<String> getTableValuesEconomic() {
        ArrayList<String> arrayDb = new ArrayList<String>();
        try {
            mydb = openOrCreateDatabase(mDbHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);
            allrows = mydb.rawQuery("SELECT * FROM "+ HomeRepairContract.HomeRepairEconomic.TABLE_NAME + " ORDER BY "
                    + HomeRepairContract.HomeRepairEconomic.COLUMN_NAME_ACTIVITY, null);
            if (allrows.moveToFirst()) {
                do {
                    arrayDb.add(allrows.getString(1));
                    arrayDbUpperCase.add(allrows.getString(1).toUpperCase());
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

    public void getTableValues() {
        ArrayList<String> arrayDb = new ArrayList<String>();
        try {
            mydb = openOrCreateDatabase(mDbHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);
            allrows = mydb.rawQuery("SELECT * FROM "+ HomeRepairContract.HomeRepairCompany.TABLE_NAME + " WHERE " +
                    HomeRepairContract.HomeRepairCompany.COLUMN_NAME_USER + " = " + "\"" + extras.getString("USER") + "\"", null);
            if (allrows.moveToFirst()) {
                edtNomEmpresa.setText(allrows.getString(1));
                edtIdEmpresa.setText(allrows.getString(2));
                edtTelEmpresa.setText(allrows.getString(3));
                spinActividad.setSelection(adapterDbUpperCase.getPosition((allrows.getString(4))));
            }
            allrows.close();
            mydb.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error consultando la informacion del usuario",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void registryServices(View view) {
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

    public boolean valideData() throws Exception{
        return (edtNomEmpresa.getText().toString().isEmpty()
                ||edtIdEmpresa.getText().toString().isEmpty()
                ||edtTelEmpresa.getText().toString().isEmpty()
                ||spinActividad.getSelectedItem().toString().isEmpty());
    }

    public void updateRegistry() throws Exception{
        mydb = openOrCreateDatabase(mDbHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);
        mydb.execSQL("UPDATE " + HomeRepairContract.HomeRepairCompany.TABLE_NAME + " SET " +
                HomeRepairContract.HomeRepairCompany.COLUMN_NAME_NAME + " = \"" + edtNomEmpresa.getText().toString().toUpperCase().trim() + "\"," +
                HomeRepairContract.HomeRepairCompany.COLUMN_NAME_NIT + " = \"" + edtIdEmpresa.getText().toString().toUpperCase().trim() + "\"," +
                HomeRepairContract.HomeRepairCompany.COLUMN_NAME_TELEPHONE + " = " + edtTelEmpresa.getText().toString().toUpperCase().trim() + "," +
                HomeRepairContract.HomeRepairCompany.COLUMN_NAME_ACTIVITY + " = \"" + spinActividad.getSelectedItem().toString().toUpperCase().trim() + "\" WHERE " +
                HomeRepairContract.HomeRepairCompany.COLUMN_NAME_USER + " = \"" + extras.getString("USER") + "\"");
        mydb.close();
        Toast.makeText(this, "Informacion modificada", Toast.LENGTH_SHORT).show();
    }
}
