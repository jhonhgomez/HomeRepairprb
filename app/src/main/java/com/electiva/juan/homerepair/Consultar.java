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
import android.widget.EditText;
import android.widget.Toast;

import com.electiva.juan.dao.HomeRepairContract;
import com.electiva.juan.dao.HomeRepairDbHelper;

public class Consultar extends Activity {

    private EditText edtTxtNumSolic;
    private Bundle extras;
    private HomeRepairDbHelper mDbHelper;
    private SQLiteDatabase mydb;
    private Cursor allrows;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_solicitud);
        edtTxtNumSolic = (EditText)findViewById(R.id.edtTxtNumSolic);
        extras = getIntent().getExtras();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_consu__solicitud, menu);
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

    public void searchQuote(View view){
        try {
            if (!edtTxtNumSolic.getText().toString().isEmpty()){
                if (!getTableValues()){
                    intent = new Intent(this, ListaCotizacion.class);
                    intent.putExtra("USER", extras.getString("USER"));
                    intent.putExtra("REQUEST", Integer.parseInt(edtTxtNumSolic.getText().toString()));
                    startActivity(intent);
                }
                else throw new Exception("No se encuentra el numero de solicitud ingresado");
            }
            else throw new Exception("Todos los campos son obligatorios");
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public boolean getTableValues() throws Exception{
        boolean requestExist = false;
        mydb = openOrCreateDatabase(mDbHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);
        allrows = mydb.rawQuery("SELECT * FROM " + HomeRepairContract.HomeRepairQuote.TABLE_NAME + " WHERE " +
                HomeRepairContract.HomeRepairQuote.COLUMN_NAME_ID_REQUEST + " = " + Integer.parseInt(edtTxtNumSolic.getText().toString()), null);
        if (allrows.moveToFirst()) requestExist = false;
        else requestExist = true;
        allrows.close();
        mydb.close();
        return requestExist;
    }
}
