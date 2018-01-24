package com.electiva.juan.homerepair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.electiva.juan.dao.HomeRepairContract;
import com.electiva.juan.dao.HomeRepairDbHelper;

import java.util.ArrayList;

public class Acceso extends Activity {

    private HomeRepairContract dataSource;
    private HomeRepairDbHelper mDbHelper;
    private SQLiteDatabase mydb;
    private Cursor allrows;
    private EditText editTextUser;
    private EditText editTextPassword;
    private Intent intent;
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    public static Activity activity;
    public static final String PREFS_NAME = "PrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        shared = getSharedPreferences(PREFS_NAME, 0);
        if (shared.getBoolean("LOGIN", false))
            setContentView(R.layout.activity_welcome);
        else
            setContentView(R.layout.activity_acceso);
        dataSource = new HomeRepairContract(this);
        editTextUser = (EditText)findViewById(R.id.edtTxtUsuario);
        editTextPassword = (EditText)findViewById(R.id.edtTxtContraseña);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_acceso, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle caction bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createAccount(View view) {
        intent = new Intent(this, Registro.class);
        startActivity(intent);
    }

    public void login(View view){
        try {
            if (!valideData()){
                if (!getTableValues()){
                    shared = getSharedPreferences(PREFS_NAME, 0);
                    editor = shared.edit();
                    editor.putString("USER", editTextUser.getText().toString().toUpperCase());
                    editor.putBoolean("LOGIN", true);
                    editor.commit();
                    intent = new Intent(this, Principal.class);
                    intent.putExtra("USER", editTextUser.getText().toString().toUpperCase());
                    startActivity(intent);
                    finish();
                }
                else throw new Exception("Usuario y/o contraseña incorrecta");
            }
            else throw new Exception("Todos los campos son obligatorios");
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void welcome(View view){
        intent = new Intent(this, Principal.class);
        intent.putExtra("USER", shared.getString("USER", ""));
        startActivity(intent);
        finish();
    }

    public boolean valideData() throws Exception{
        return (editTextUser.getText().toString().isEmpty()
                || editTextPassword.getText().toString().isEmpty());
    }

    public boolean getTableValues() throws Exception{
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


}
