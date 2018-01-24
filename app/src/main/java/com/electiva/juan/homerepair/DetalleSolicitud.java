package com.electiva.juan.homerepair;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.electiva.juan.dao.HomeRepairContract;
import com.electiva.juan.dao.HomeRepairDbHelper;

public class DetalleSolicitud extends AppCompatActivity{

    private EditText editTextDetalle;
    private Bundle extras;
    private HomeRepairDbHelper mDbHelper;
    private SQLiteDatabase mydb;
    private Intent intent;
    private Cursor allrows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_solicitud);
        editTextDetalle = (EditText)findViewById(R.id.edtTxtDetalle);
        extras = getIntent().getExtras();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalle_solicitud, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_detalle) {
            try {
                if (!editTextDetalle.getText().toString().isEmpty()){
                    insertRegistry();
                } else {
                    throw new Exception("Todos los campos son obligatorios");
                }
            } catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void insertRegistry() throws Exception{
        int numSolicitud;
        mydb = openOrCreateDatabase(mDbHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);
        allrows = mydb.rawQuery("SELECT MAX(" + HomeRepairContract.HomeRepairRequest.COLUMN_NAME_REQUEST_ID + ") FROM " + HomeRepairContract.HomeRepairRequest.TABLE_NAME, null);
        if (allrows.moveToFirst()) numSolicitud = allrows.getInt(0);
        else throw new Exception("No se pudo establecer la consulta");
        allrows.close();
        mydb.execSQL("INSERT INTO " + HomeRepairContract.HomeRepairRequest.TABLE_NAME + " VALUES (" +
                "NULL," + "\"" + extras.getString("SERVICE") + "\"," + "\"" + editTextDetalle.getText() + "\"," +
                "\"" + extras.getString("USER") +  "\")");
        LayoutInflater layoutInflater = LayoutInflater.from(DetalleSolicitud.this);
        View promptView = layoutInflater.inflate(R.layout.show_dialog, null);
        TextView txtNumSolicitud = (TextView) promptView.findViewById(R.id.txtNumeroSolicitud);
        txtNumSolicitud.setText(""+(numSolicitud + 1));
        AlertDialog dialog = new AlertDialog.Builder(DetalleSolicitud.this)
                .setTitle("Solicitud registrada")
                .setView(promptView)
                .setMessage("Por favor tome nota del numero de solicitud:")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        intent = new Intent(getApplicationContext(), Principal.class);
                        intent.putExtra("USER", extras.getString("USER"));
                        startActivity(intent);
                        Servicios.activity.finish();
                        Principal.activity.finish();
                        finish();
                    }
                }).create();
        dialog.show();
        mydb.close();
    }
}
