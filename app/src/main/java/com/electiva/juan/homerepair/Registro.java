package com.electiva.juan.homerepair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;

public class Registro extends Activity {

    private Intent intent;
    private Button buttonProfesional;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        activity = this;
        buttonProfesional = (Button)findViewById(R.id.btnProfesional);
        buttonProfesional.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registro, menu);
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

    public void registryEmpresa(View view ){
        intent = new Intent(this, RegistroEmpresa.class);
        startActivity(intent);
    }

    public void registryProfesional(View view ){
        intent = new Intent(this, RegistroProfesional.class);
        startActivity(intent);
    }

    public void registryCliente(View view ){
        intent = new Intent(this, RegistroCliente.class);
        startActivity(intent);
    }

}
