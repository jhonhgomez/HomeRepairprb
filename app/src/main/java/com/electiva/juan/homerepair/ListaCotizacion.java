package com.electiva.juan.homerepair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.electiva.juan.dao.HomeRepairContract;
import com.electiva.juan.dao.HomeRepairDbHelper;

import java.util.ArrayList;

public class ListaCotizacion extends Activity {

    private Bundle extras;
    private TextView txtCotPendientes;
    private int numCotizaciones;
    private Fragment listFragment;
    private HomeRepairDbHelper mDbHelper;
    private SQLiteDatabase mydb;
    private Cursor allrows;
    private String numberCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_cotizacion);
        extras = getIntent().getExtras();
        txtCotPendientes = (TextView)findViewById(R.id.txtLiqPendientes);
        listFragment = new ListFragment();
        if (savedInstanceState == null){
            getFragmentManager().beginTransaction()
                    .add(R.id.containerLiquida, listFragment, "lista1").commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista_cotizacion, menu);
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

    public void call(View view){
        String uri = "tel:" + numberCall.trim() ;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }

    public ArrayList<ArrayList<String>> getTableValues() {
        ArrayList<ArrayList<String>> arrayDb = new ArrayList<ArrayList<String>>();
        ArrayList<String> arrayDbQuote = new ArrayList<String>();
        ArrayList<String> arrayDbService = new ArrayList<String>();
        ArrayList<String> arrayDbUser = new ArrayList<String>();
        ArrayList<String> arrayDbTel = new ArrayList<String>();

        try {
            mydb = openOrCreateDatabase(mDbHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);
            allrows = mydb.rawQuery("SELECT A.*, B.* FROM " +
                    HomeRepairContract.HomeRepairRequest.TABLE_NAME + " D, " +
                    HomeRepairContract.HomeRepairClient.TABLE_NAME + " C, " +
                    HomeRepairContract.HomeRepairQuote.TABLE_NAME + " A LEFT JOIN " +
                    HomeRepairContract.HomeRepairCompany.TABLE_NAME + " B ON A." +
                    HomeRepairContract.HomeRepairQuote.COLUMN_NAME_USER_COMPANY + "=B." +
                    HomeRepairContract.HomeRepairCompany.COLUMN_NAME_USER + " WHERE C." +
                    HomeRepairContract.HomeRepairClient.COLUMN_NAME_USER + " = " + "\"" + extras.getString("USER") + "\"" + " AND A." +
                    HomeRepairContract.HomeRepairQuote.COLUMN_NAME_ID_REQUEST + " = " + extras.getInt("REQUEST") + " AND D." +
                    HomeRepairContract.HomeRepairRequest.COLUMN_NAME_REQUEST_ID + "=A." +
                    HomeRepairContract.HomeRepairQuote.COLUMN_NAME_ID_REQUEST + " AND D." +
                    HomeRepairContract.HomeRepairRequest.COLUMN_NAME_SERVICE + "=B." +
                    HomeRepairContract.HomeRepairCompany.COLUMN_NAME_SERVICE + " ORDER BY A." +
                    HomeRepairContract.HomeRepairQuote.COLUMN_NAME_USER_COMPANY, null);
            if (allrows.moveToFirst()) {
                do {
                    arrayDbQuote.add(allrows.getString(4));
                    arrayDbService.add(allrows.getString(10));
                    arrayDbTel.add(allrows.getString(8));
                    arrayDbUser.add(allrows.getString(6).toUpperCase());
                } while (allrows.moveToNext());
            }
            numCotizaciones = allrows.getCount();
            allrows.close();
            mydb.close();
            arrayDb.add(0, arrayDbQuote);
            arrayDb.add(1, arrayDbService);
            arrayDb.add(2, arrayDbUser);
            arrayDb.add(3, arrayDbTel);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error consultando las cotizaciones",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return arrayDb;
    }

    public static class ListFragment extends Fragment {

        private ListView lista;
        private ArrayList<ArrayList<String>> arrayDb;
        private TextView userService;
        private TextView detail;
        private TextView request;
        private ImageView iconService;
        private String service;
        private TextView telephone;

        public ListFragment() {
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_lista_cotizacion, container, false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            arrayDb = ((ListaCotizacion)getActivity()).getTableValues();
            ((ListaCotizacion)getActivity()).txtCotPendientes.setText("Numero de cotizaciones: "+((ListaCotizacion)getActivity()).numCotizaciones);
            lista = (ListView) getView().findViewById(R.id.listViewCot);
            if (arrayDb.get(0).isEmpty()){
                lista.setBackgroundColor(Color.TRANSPARENT);
            }
            lista.setAdapter(new AdapterSolitud(getActivity(), arrayDb.get(0), arrayDb.get(1), arrayDb.get(2), arrayDb.get(3)));
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    telephone = (TextView) view.findViewById(R.id.telCompany);
                    ((ListaCotizacion) getActivity()).numberCall = telephone.getText().toString();
                    LayoutInflater layoutInflater = LayoutInflater.from(((ListaCotizacion) getActivity()));
                    View promptView = layoutInflater.inflate(R.layout.contact_dialog, null);
                    final TextView txtTelContacto = (TextView) promptView.findViewById(R.id.txtTelContacto);
                    txtTelContacto.setText(telephone.getText().toString());
                    AlertDialog dialog = new AlertDialog.Builder(((ListaCotizacion) getActivity()))
                            .setTitle("Contacto")
                            .setView(promptView)
                            .setPositiveButton("OK", null)
                            .create();
                    dialog.show();
                }
            });
        }
    }

    public static class AdapterSolitud extends BaseAdapter {

        private final Activity activity;
        private final ArrayList<String> arrayQuote;
        private final ArrayList<String> arrayService;
        private final ArrayList<String> arrayUser;
        private final ArrayList<String> arrayTel;

        public AdapterSolitud(Activity activity, ArrayList<String> arrayQuote, ArrayList<String> arrayService,
                              ArrayList<String> arrayUser, ArrayList<String> arrayTel) {
            super();
            this.activity = activity;
            this.arrayQuote = arrayQuote;
            this.arrayService = arrayService;
            this.arrayUser = arrayUser;
            this.arrayTel = arrayTel;
        }

        @Override
        public int getCount() {
            return arrayService.size();
        }

        @Override
        public Object getItem(int i) {
            return arrayService.get(i);
        }


        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            LayoutInflater inflater = activity.getLayoutInflater();
            View view = inflater.inflate(R.layout.list_item_liquidacion, null, true);
            TextView userService = (TextView)view.findViewById(R.id.userServiceLiq);
            ImageView iconService = (ImageView)view.findViewById(R.id.iconServiceLiq);
            TextView quote = (TextView)view.findViewById(R.id.valueLiq);
            TextView telephone = (TextView)view.findViewById(R.id.telCompany);
            userService.setText(arrayUser.get(i));
            if (arrayService.get(i).equals("CARPINTERÍA")){
                iconService.setImageResource(R.drawable.martillo1);
                iconService.setTag(R.drawable.martillo1);
            }
            else if (arrayService.get(i).equals("CERRAJERÍA")){
                iconService.setImageResource(R.drawable.llaves);
                iconService.setTag(R.drawable.llaves);
            }
            else if (arrayService.get(i).equals("CÓMPUTO")){
                iconService.setImageResource(R.drawable.desktop1);
                iconService.setTag(R.drawable.desktop1);
            }
            else if (arrayService.get(i).equals("ELECTRÓNICOS")){
                iconService.setImageResource(R.drawable.microwavepeq);
                iconService.setTag(R.drawable.microwavepeq);
            }
            else if (arrayService.get(i).equals("ELÉCTRICOS")){
                iconService.setImageResource(R.drawable.electr);
                iconService.setTag(R.drawable.electr);
            }
            else if (arrayService.get(i).equals("PLOMERÍA")){
                iconService.setImageResource(R.drawable.plom);
                iconService.setTag(R.drawable.plom);
            }
            quote.setText(arrayQuote.get(i));
            telephone.setText(arrayTel.get(i));
            telephone.setVisibility(View.GONE);
            return view;
        }
    }
}
