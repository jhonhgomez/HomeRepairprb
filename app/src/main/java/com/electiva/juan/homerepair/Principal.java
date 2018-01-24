package com.electiva.juan.homerepair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.electiva.juan.dao.HomeRepairContract;
import com.electiva.juan.dao.HomeRepairDbHelper;

import java.util.ArrayList;
import java.util.Locale;

public class Principal extends Activity {

    private Button btnConSol;
    private Button btnSoliServ;
    private Button btnEdiInfo;
    private Button btnVerSolicitudes;
    private Intent intent;
    private Bundle extras;
    private SQLiteDatabase mydb;
    private HomeRepairDbHelper mDbHelper;
    private Cursor allrows;
    private ArrayList<String> arrayDb;
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    public static final String PREFS_NAME = "PrefsFile";
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        activity = this;
        btnConSol = (Button)findViewById(R.id.btnConSol);
        btnSoliServ = (Button)findViewById(R.id.btnSoliServ);
        btnEdiInfo = (Button)findViewById(R.id.btnEdiInfo);
        btnVerSolicitudes = (Button)findViewById(R.id.btnVerSolicitudes);
        extras = getIntent().getExtras();
        arrayDb = getTableValues();
        if (!arrayDb.isEmpty()){
            if (arrayDb.contains("3")){
                btnVerSolicitudes.setVisibility(View.GONE);
            } else if (arrayDb.contains("1")){
                btnConSol.setVisibility(View.GONE);
                btnSoliServ.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
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
            allrows = mydb.rawQuery("SELECT * FROM "+ HomeRepairContract.HomeRepairUser.TABLE_NAME + " WHERE " +
                    HomeRepairContract.HomeRepairUser.COLUMN_NAME_USER + " = " + "\"" + extras.getString("USER") + "\"", null);
            if (allrows.moveToFirst()) {
                do {
                    arrayDb.add(allrows.getString(3));
                } while (allrows.moveToNext());
            }
            allrows.close();
            mydb.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error consultando la informacion del usuario",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return arrayDb;
    }

    public void viewRequest(View view){
        intent = new Intent (this, ListaSolicitud.class);
        intent.putExtra("USER", extras.getString("USER"));
        startActivity(intent);
    }

    public void requestService(View view){
        intent = new Intent (this, Servicios.class);
        intent.putExtra("USER", extras.getString("USER"));
        startActivity(intent);
    }

    public void searchRequest(View view){
        intent = new Intent (this, Consultar.class);
        intent.putExtra("USER", extras.getString("USER"));
        startActivity(intent);
    }

    public void logout(View view){
        AlertDialog dialog = new AlertDialog.Builder(Principal.this)
                .setTitle("Salir")
                .setMessage("Desea cerrar sesión?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        shared = getSharedPreferences(PREFS_NAME, 0);
                        editor = shared.edit();
                        editor.putBoolean("LOGIN", false);
                        editor.commit();
                        finish();
                    }
                })
                .setNegativeButton("CANCEL", null)
                .create();
        dialog.show();
    }

    public void editInformation (View view){
        if (arrayDb.contains("3")){
            intent = new Intent (this, EditarCliente.class);
        } else if(arrayDb.contains("1")){
            intent = new Intent (this, EditarEmpresa.class);
        }
        intent.putExtra("USER", extras.getString("USER"));
        startActivity(intent);
    }
}
