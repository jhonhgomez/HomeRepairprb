package com.electiva.juan.homerepair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class Servicios extends Activity {

    private String valorImagen;
    private Intent intent;
    private Bundle extras;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicios);
        activity = this;
        extras = getIntent().getExtras();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_servicios, menu);
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

    public void onImageClicked(View view) {
        switch(view.getId()) {
            case R.id.btncarp:
                valorImagen="CARPINTERÍA";
                break;
            case R.id.btncerra:
                valorImagen="CERRAJERÍA";
                break;
            case R.id.btnpc:
                valorImagen="CÓMPUTO";
                break;
            case R.id.btnelectronico:
                valorImagen="ELECTRÓNICOS";
                break;
            case R.id.btnelectrico:
                valorImagen="ELÉCTRICOS";
                break;
            case R.id.btnplomeria:
                valorImagen="PLOMERÍA";
                break;
        }
        intent = new Intent(this, DetalleSolicitud.class);
        intent.putExtra("USER", extras.getString("USER"));
        intent.putExtra("SERVICE", valorImagen);
        startActivity(intent);
    }
}
