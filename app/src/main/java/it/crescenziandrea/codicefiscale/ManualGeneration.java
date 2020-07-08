package it.crescenziandrea.codicefiscale;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.List;

public class ManualGeneration extends AppCompatActivity  {

    /*
    @Override
    public void applyEntry(String Cfcode, String alias) {
         //TODO: passare i valori al DOA li posso prendere da CFcode e alias
        holder.alias = alias;
    }

    @Override
    public String setCFcode() {
        return holder.cfcode.Calculate();
    }
     */

    Holder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_generation);
        holder = new Holder();



    }


    class Holder implements DatePickerDialog.OnDateSetListener,View.OnClickListener {

        TextInputEditText tvSurname;
        TextInputEditText tvName;
        AutoCompleteTextView tvRegion;
        AutoCompleteTextView tvProvince;
        AutoCompleteTextView tvDistrict;
        AutoCompleteTextView tvGender;
        ArrayAdapter<String> adapter;
        List<ProDis> prov ;
        CFgenerator cfcode;
        String[] str;
        TextInputEditText alias;
        Button btn;
        Button bt_gen;
        final VolleyCocktail model;
        Calendar calendar ;
        int day;
        int month;
        int year;
        int bDay;
        int bMonth;
        int bYear;
        private int search = 0;



        String[] region = new String[]{"veneto",
                "lombardia",
                "toscana",
                "sardegna",
                "abruzzo",
                "basilicata",
                "sicilia",
                "puglia",
                "piemonte",
                "lazio",
                "campania",
                "calabria",
                "marche",
                "umbria",
                "molise",
                "emilia Romagna",
                "friuli venezia giulia",
                "liguria",
                "trentino alto adige",
                "valle d'aosta"};

        String[] gender = new String[]{"M","F"};


        @SuppressLint("ClickableViewAccessibility")
        public Holder() {
            tvSurname = findViewById(R.id.surname);
            tvName = findViewById(R.id.name);
            tvRegion = findViewById(R.id.tvRegion);
            tvProvince = findViewById(R.id.tvProvince);
            tvDistrict = findViewById(R.id.tvDistrict);
            tvGender = findViewById(R.id.tvGender);
            bt_gen = findViewById(R.id.bt_gen);
            alias = findViewById(R.id.alias);
            bt_gen.setOnClickListener(this);

            tvProvince.setEnabled(false);
            tvDistrict.setEnabled(true);

            calendar = Calendar.getInstance();
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH);
            year = calendar.get(Calendar.YEAR);

            btn = findViewById(R.id.calendar);

            btn.setOnClickListener(this);

            tvRegion.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if (tvRegion.isPerformingCompletion()) {
                        tvProvince.setEnabled(true);
                        tvDistrict.setEnabled(true);
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            tvProvince.setOnTouchListener(new View.OnTouchListener(){

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    model.searchProvince(tvRegion.getText().toString());
                    search = 2;
                    Toast.makeText(getApplicationContext(), tvRegion.getText().toString(), Toast.LENGTH_LONG).show();

                    return false;
                }
            });

            tvDistrict.setOnTouchListener(new View.OnTouchListener(){

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    model.searchDistrict(tvProvince.getText().toString());
                    search = 3;
                    Toast.makeText(getApplicationContext(), "comuni", Toast.LENGTH_LONG).show();

                    return false;
                }
            });



            ArrayAdapter<String> adapterRegion = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, region);
            tvRegion.setAdapter(adapterRegion);

            ArrayAdapter<String> adapterGender = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, gender);
            tvGender.setAdapter(adapterGender);



            this.model = new VolleyCocktail() {                                                 //inizializziamo il modello di acquisizione dati che è un new VolleyCocktail
                @Override
                void fill(List<ProDis> cnt) {
                    Log.w("CA", "fill");
                    Toast.makeText(getApplicationContext(), "fill", Toast.LENGTH_LONG).show();
                    fillList(cnt); //il metodo fill chiama una funzione chiamata fillList
                }
            };
        }

        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
           bDay = dayOfMonth;
           bMonth = (monthOfYear+1);
           bYear = year;
            return ;
        }

        private void fillList(List<ProDis> cnt) { //fa il filling della RecyclerView


            switch(search){
                case 2:
                    adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, listToArrayString(cnt));
                    tvProvince.setAdapter(adapter);
                    break;

                case 3:
                    prov = cnt;
                    str = listToArrayString(cnt);
                    adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,str);
                    tvDistrict.setAdapter(adapter);
                    break;

                default:
                    // TO DO
                    break;
            }


        }

        public  int getIndexOf(String[] strings, String item) {
            for (int i = 0; i < strings.length; i++) {
                if (item.equals(strings[i])) return i;
            }
            return -1;
        }

        private String[] listToArrayString(List<ProDis> list){

            String[] str = new String[list.size()] ;

            for(int i=0; i<list.size(); i++){
                str[i] = list.get(i).getProDis();
            }

            return str;
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == bt_gen.getId()){



                cfcode = new CFgenerator(tvSurname.getText().toString().toUpperCase(),
                        tvName.getText().toString().toUpperCase(),
                        bDay,
                        bMonth,
                        bYear,
                        tvGender.getText().toString().toUpperCase(),
                        prov.get(getIndexOf(str,tvDistrict.getText().toString())).getCodCat());

                //openDialog();

                Intent output = new Intent();
                output.putExtra("alias", alias.getText().toString());
                output.putExtra("fCode", cfcode.Calculate());
                setResult(RESULT_OK, output);
                finish();
            }

            if(v.getId() == btn.getId()){
                DatePickerDialog dialog = DatePickerDialog.newInstance(holder,day,month,year);
                dialog.show(getSupportFragmentManager(),"DatePickerDialog");
            }
        }
    }

    public void openDialog() {
        Dialog dialog = new Dialog();
        dialog.show(getSupportFragmentManager(), "Popup");
    }

    abstract class VolleyCocktail implements Response.ErrorListener, Response.Listener<String> { //attraverso Volley prende i dati del sito cocktaildb, è la classe che fa da interfaccia tra cocktaildb e la nostra app
        abstract void fill(List<ProDis> cnt); //la UI sarà gestita dalla classe chiamante andando a implementare il metodo fill

        private int search = 0; ;

        public void searchRegion() {
            String url = "https://comuni-ita.herokuapp.com/api/regioni"; //usiamo il metodo search per la ricerca per nome
            search = 1;
            apiCall(url);
        }

        public void searchProvince(String id) {
            String url = "https://comuni-ita.herokuapp.com/api/province/%s"; //usiamo il metodo search per la ricerca per nome
            url = String.format(url, id);
            search = 2;
            apiCall(url);
        }

        public void searchDistrict(String id) {
            String url = "https://comuni-ita.herokuapp.com/api/comuni/provincia/%s"; //usiamo il metodo filter, i è l'ingrediente che andiamo a richiedere
            url = String.format(url, id);
            search = 3;
            apiCall(url);
        }

        private void apiCall(String url) { //crea una RequestQueue e una StringRequest
            RequestQueue requestQueue; //alloca una RequestQueue
            requestQueue = Volley.newRequestQueue(getApplicationContext()); //la RequestQueue sarà una richiesta di tipo newRequestQueue in cui gli passiamo il context
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, this, this);
            requestQueue.add(stringRequest);

        }


        @Override
        public void onErrorResponse(VolleyError error) {
            Log.w("CA","error"+error.toString());
            Toast.makeText(getApplicationContext(), "SOMETHING WENT WRONG", Toast.LENGTH_LONG).show(); //mettiamo un toast per dire all'utente che c'è stato qualcosa che non è andato a buon fine
        }

        @Override
        public void onResponse(String response) { //se tutto è andato bene avremo una string response che sarà un json di risposta
            Gson gson = new Gson(); //allochiamo un oggetto gson
            String res;
            switch(search) {
                case 1:
                    try {

                        JSONArray jsonArray = new JSONArray(response);
                        res = jsonArray.toString();
                        String[] cnt = gson.fromJson(res, String[].class);
                        if (cnt != null && cnt.length > 0) { //se la lista dei cocktail è diversa da null, quindi se non ci sono stati errori legati all'assegnazione nella riga precedente, e se abbiamo trovato qualcosa all'interno della struttura
                            Log.w("CA", "" + cnt.length);
                            //fill(cnt);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                case 3:
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        res = jsonArray.toString();
                        Type listType = new TypeToken<List<ProDis>>() {}.getType();
                        List<ProDis> cnt = gson.fromJson(res, listType);
                        if (cnt != null && cnt.size() > 0) {
                            Log.w("CA", cnt.get(0).getCodCat());
                            fill(cnt);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    // TO DO error
                    break;
            }

            }
    }
}