package com.electiva.juan.homerepair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.electiva.juan.dao.HomeRepairContract;
import com.electiva.juan.dao.HomeRepairDbHelper;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class ListaSolicitud extends Activity {

    private Fragment listFragment;
    private Fragment listFragment2;
    private HomeRepairDbHelper mDbHelper;
    private SQLiteDatabase mydb;
    private Cursor allrows;
    private Bundle extrasFragment;
    private Bundle extras;
    private TextView txtSolPendientes;
    private int numSolicitudes;
    private String telefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_solicitud);
        extras = getIntent().getExtras();
        txtSolPendientes = (TextView)findViewById(R.id.txtSolPendientes);
        listFragment = new ListFragment();
        if (savedInstanceState == null){
            getFragmentManager().beginTransaction()
                    .add(R.id.containerSolicutd, listFragment, "lista1").commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista_solicitud, menu);
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

    public void goRequestFragment(String userService, String service, String detail, String request){
        listFragment2 = new RequestFragment();
        extrasFragment = new Bundle();
        extrasFragment.putString("USER", userService);
        extrasFragment.putString("SERVICE", service);
        extrasFragment.putString("DETAIL", detail);
        extrasFragment.putString("REQUEST", request);
        if (getFragmentManager().findFragmentByTag("lista1").isVisible()) {
            listFragment2.setArguments(extrasFragment);
            getFragmentManager().beginTransaction()
                    .replace(R.id.containerSolicutd, listFragment2, "lista2").addToBackStack(null).commit();
        }
    }

    public void applyRequest(String userRequest, String idRequest, String quote) throws Exception{
        try {
            if (quote.trim().length() > 0 && !quote.trim().equals("0")){
                insertRegistry(userRequest, extras.getString("USER"), Integer.parseInt(idRequest), Integer.parseInt(quote));
                if (getFragmentManager().findFragmentByTag("lista2").isVisible()) {
                    getFragmentManager().popBackStack();
                }
            } else throw new Exception("Debe ingresar un valor válido");
        }  catch (Exception e) {
            Toast.makeText(this, e.getMessage(),
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void insertRegistry(String userRequest, String companyRequest, int idRequest, int quote) throws Exception{
        mydb = openOrCreateDatabase(mDbHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);
        mydb.execSQL("INSERT INTO " + HomeRepairContract.HomeRepairQuote.TABLE_NAME + " VALUES (" +
                "NULL," + "\"" + userRequest + "\"," + "\"" + companyRequest + "\"," + idRequest + ","  + quote + ")");
        mydb.close();
        Toast.makeText(this, "Cotización enviada", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<ArrayList<String>> getTableValues() {
        ArrayList<ArrayList<String>> arrayDb = new ArrayList<ArrayList<String>>();
        ArrayList<String> arrayDbRequest = new ArrayList<String>();
        ArrayList<String> arrayDbService = new ArrayList<String>();
        ArrayList<String> arrayDbDetail = new ArrayList<String>();
        ArrayList<String> arrayDbUser = new ArrayList<String>();
        try {
            mydb = openOrCreateDatabase(mDbHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);
            allrows = mydb.rawQuery("SELECT A.*, B.* FROM " +
                    HomeRepairContract.HomeRepairCompany.TABLE_NAME + " C, " +
                    HomeRepairContract.HomeRepairRequest.TABLE_NAME + " A LEFT JOIN " +
                    HomeRepairContract.HomeRepairClient.TABLE_NAME + " B ON A." +
                    HomeRepairContract.HomeRepairRequest.COLUMN_NAME_USER + "=B." +
                    HomeRepairContract.HomeRepairClient.COLUMN_NAME_USER + " WHERE C." +
                    HomeRepairContract.HomeRepairCompany.COLUMN_NAME_USER + " = " + "\"" + extras.getString("USER") + "\"" + " AND A." +
                    HomeRepairContract.HomeRepairRequest.COLUMN_NAME_SERVICE + "=C." +
                    HomeRepairContract.HomeRepairCompany.COLUMN_NAME_SERVICE + " AND A." +
                    HomeRepairContract.HomeRepairRequest.COLUMN_NAME_REQUEST_ID + " NOT IN (SELECT " +
                    HomeRepairContract.HomeRepairQuote.COLUMN_NAME_ID_REQUEST + " FROM " +
                    HomeRepairContract.HomeRepairQuote.TABLE_NAME + " WHERE " +
                    HomeRepairContract.HomeRepairQuote.COLUMN_NAME_ID_REQUEST + " IS NOT NULL AND " +
                    HomeRepairContract.HomeRepairQuote.COLUMN_NAME_USER_COMPANY + " = " + "\"" + extras.getString("USER") + "\"" + ") ORDER BY A." +
                    HomeRepairContract.HomeRepairRequest.COLUMN_NAME_USER, null);
            if (allrows.moveToFirst()) {
                do {
                    arrayDbRequest.add(allrows.getString(0));
                    arrayDbService.add(allrows.getString(1));
                    arrayDbDetail.add(allrows.getString(2));
                    arrayDbUser.add(allrows.getString(5).toUpperCase() + " " + allrows.getString(6).toUpperCase());
                } while (allrows.moveToNext());
            }
            numSolicitudes = allrows.getCount();
            allrows.close();
            mydb.close();
            arrayDb.add(0, arrayDbRequest);
            arrayDb.add(1, arrayDbService);
            arrayDb.add(2, arrayDbDetail);
            arrayDb.add(3, arrayDbUser);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error consultando las solicitudes",
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

        public ListFragment() {
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_lista_solicitud, container, false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            arrayDb = ((ListaSolicitud)getActivity()).getTableValues();
            ((ListaSolicitud)getActivity()).txtSolPendientes.setText("Numero de solicitudes: "+((ListaSolicitud)getActivity()).numSolicitudes);
            lista = (ListView) getView().findViewById(R.id.listView);
            if (arrayDb.get(0).isEmpty()){
                lista.setBackgroundColor(Color.TRANSPARENT);
            }
            lista.setAdapter(new AdapterSolitud(getActivity(), arrayDb.get(0), arrayDb.get(1), arrayDb.get(2), arrayDb.get(3)));
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    userService = (TextView) view.findViewById(R.id.userService);
                    detail = (TextView) view.findViewById(R.id.detail);
                    request = (TextView) view.findViewById(R.id.idRequest);
                    iconService = (ImageView) view.findViewById(R.id.iconService);
                    int id = (int) iconService.getTag();
                    switch (id) {
                        case R.drawable.martillo1:
                            service = "CARPINTERÍA";
                            break;
                        case R.drawable.llaves:
                            service = "CERRAJERÍA";
                            break;
                        case R.drawable.desktop1:
                            service = "CÓMPUTO";
                            break;
                        case R.drawable.microwavepeq:
                            service = "ELECTRÓNICOS";
                            break;
                        case R.drawable.electr:
                            service = "ELÉCTRICOS";
                            break;
                        case R.drawable.plom:
                            service = "PLOMERÍA";
                            break;
                    }
                    ((ListaSolicitud) getActivity()).goRequestFragment(userService.getText().toString(), service, detail.getText().toString(), request.getText().toString());
                }
            });
        }
    }

    public static class AdapterSolitud extends BaseAdapter {

        private final Activity activity;
        private final ArrayList<String> arrayRequest;
        private final ArrayList<String> arrayService;
        private final ArrayList<String> arrayDetail;
        private final ArrayList<String> arrayUser;

        public AdapterSolitud(Activity activity, ArrayList<String> arrayRequest, ArrayList<String> arrayService,
                              ArrayList<String> arrayDetail, ArrayList<String> arrayUser) {
            super();
            this.activity = activity;
            this.arrayRequest = arrayRequest;
            this.arrayService = arrayService;
            this.arrayDetail = arrayDetail;
            this.arrayUser = arrayUser;
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
            View view = inflater.inflate(R.layout.list_item_solicitud, null, true);
            TextView userService = (TextView)view.findViewById(R.id.userService);
            TextView detail = (TextView)view.findViewById(R.id.detail);
            ImageView iconService = (ImageView)view.findViewById(R.id.iconService);
            TextView request = (TextView)view.findViewById(R.id.idRequest);
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
            detail.setText(arrayDetail.get(i));
            request.setText(arrayRequest.get(i));
            request.setVisibility(View.GONE);
            return view;
        }
    }

    public static class RequestFragment extends Fragment {

        private TextView userRequest;
        private TextView serviceRequest;
        private TextView detailRequest;
        private String idRequest;
        private Button buttonApliSolicitud;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_solicitud, container, false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            userRequest = (TextView)getView().findViewById(R.id.userServiceRequest);
            serviceRequest = (TextView)getView().findViewById(R.id.serviceRequest);
            detailRequest = (TextView)getView().findViewById(R.id.detailRequest);
            buttonApliSolicitud = (Button)getView().findViewById(R.id.btnApliSol);
            userRequest.setText(getArguments().getString("USER"));
            serviceRequest.setText(getArguments().getString("SERVICE"));
            detailRequest.setText(getArguments().getString("DETAIL"));
            idRequest = getArguments().getString("REQUEST");
            buttonApliSolicitud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater layoutInflater = LayoutInflater.from(((ListaSolicitud) getActivity()));
                    View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
                    final EditText editTextValor = (EditText) promptView.findViewById(R.id.edtValorCotizacion);
                    AlertDialog dialog = new AlertDialog.Builder(((ListaSolicitud) getActivity()))
                            .setTitle("Aplicar cotización")
                            .setView(promptView)
                            .setMessage("Estimado usuario:" +
                                    "\n\nDebe ingresar el valor de la cotización en MCTE ($)")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try {
                                        ((ListaSolicitud) getActivity()).applyRequest(userRequest.getText().toString(), idRequest, editTextValor.getText().toString());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).create();
                    dialog.show();
                }
                public void call(View view){

                }
            });
        }

    }
}