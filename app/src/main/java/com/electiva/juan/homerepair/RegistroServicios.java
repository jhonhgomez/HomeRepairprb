package com.electiva.juan.homerepair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.electiva.juan.dao.HomeRepairContract;
import com.electiva.juan.dao.HomeRepairDbHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class RegistroServicios extends Activity {

    private HomeRepairDbHelper mDbHelper;
    private HomeRepairContract dataSource;
    private SQLiteDatabase mydb;
    private Intent intent;
    private Cursor allrows;
    private Bundle extras;
    private CheckBox chkCarp;
    private CheckBox chkCerra;
    private CheckBox chkComputo;
    private CheckBox chkElectronico;
    private CheckBox chkElectrico;
    private CheckBox chkPlomeria;
    private ArrayList<String> check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_servicios);
        extras = getIntent().getExtras();
        chkCarp = (CheckBox)findViewById(R.id.chkCarp);
        chkCerra = (CheckBox)findViewById(R.id.chkCerra);
        chkComputo = (CheckBox)findViewById(R.id.chkComputo);
        chkElectronico = (CheckBox)findViewById(R.id.chkElectronico);
        chkElectrico = (CheckBox)findViewById(R.id.chkElectrico);
        chkPlomeria = (CheckBox)findViewById(R.id.chkPlomeria);
        check = new ArrayList<String>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registro_servicios, menu);
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

    public void createUser(View view){
        try {
            if (check.isEmpty()) throw new Exception("Debe seleccionar por lo menos un servicio");
            if (getTableValues()){
                insertRegistry();
                intent = new Intent(this, Principal.class);
                intent.putExtra("USER", extras.getString("USER"));
                startActivity(intent);
                RegistroEmpresa.activity.finish();
                Registro.activity.finish();
                Acceso.activity.finish();
                finish();
            }
            else throw new Exception("Usuario ya se encuentra registrado");
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public boolean getTableValues() throws Exception{
        boolean userExist = false;
        mydb = openOrCreateDatabase(mDbHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);
        allrows = mydb.rawQuery("SELECT * FROM " + HomeRepairContract.HomeRepairUser.TABLE_NAME + " WHERE " +
                HomeRepairContract.HomeRepairUser.COLUMN_NAME_USER + " = " + "\"" + extras.getString("USER") + "\" AND " +
                HomeRepairContract.HomeRepairUser.COLUMN_NAME_PASSWORD + " = " + "\"" + extras.getString("PASSWORD")  + "\"", null);
        if (allrows.moveToFirst()) userExist = false;
        else userExist = true;
        allrows.close();
        mydb.close();
        return userExist;
    }

    public void insertRegistry() throws Exception{
        mydb = openOrCreateDatabase(mDbHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);
        mydb.execSQL("INSERT INTO " + HomeRepairContract.HomeRepairUser.TABLE_NAME + " VALUES (" +
                "NULL," + "\"" + extras.getString("USER") + "\"," + "\"" + extras.getString("PASSWORD") + "\"," + extras.getSerializable("ROLE") + ")");

        for (String service : check){
            mydb.execSQL("INSERT INTO " + HomeRepairContract.HomeRepairCompany.TABLE_NAME + " VALUES (" +
                    "NULL," + "\"" + extras.getString("NAME") + "\"," + "\"" + extras.getString("NIT") + "\"," +
                    extras.getString("TELEPHONE") + "," + "\"" + extras.getString("ACTIVITY") + "\"," +
                    "\"" + service.toUpperCase().trim() + "\"," +
                    "\"" + extras.getString("USER") + "\")");
        }

        mydb.close();
        Toast.makeText(this, "Usuario creado", Toast.LENGTH_SHORT).show();
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.chkCarp:
                if (checked) {
                    check.add(chkCarp.getHint().toString());
                } else {
                    check.remove(chkCarp.getHint().toString());
                }
                break;
            case R.id.chkCerra:
                if (checked) {
                    check.add(chkCerra.getHint().toString());
                } else {
                    check.remove(chkCerra.getHint().toString());
                }
                break;
            case R.id.chkComputo:
                if (checked) {
                    check.add(chkComputo.getHint().toString());
                } else {
                    check.remove(chkComputo.getHint().toString());
                }
                break;
            case R.id.chkElectronico:
                if (checked) {
                    check.add(chkElectronico.getHint().toString());
                } else {
                    check.remove(chkElectronico.getHint().toString());
                }
                break;
            case R.id.chkElectrico:
                if (checked) {
                    check.add(chkElectrico.getHint().toString());
                } else {
                    check.remove(chkElectrico.getHint().toString());
                }
                break;
            case R.id.chkPlomeria:
                if (checked) {
                    check.add(chkPlomeria.getHint().toString());
                } else {
                    check.remove(chkPlomeria.getHint().toString());
                }
                break;
        }
    }
}
