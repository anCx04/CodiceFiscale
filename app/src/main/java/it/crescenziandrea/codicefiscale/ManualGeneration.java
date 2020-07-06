package it.crescenziandrea.codicefiscale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class ManualGeneration extends AppCompatActivity {
    Holder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_generation);
        holder = new Holder();

    }


    class Holder{
        AutoCompleteTextView tvRegion;
        AutoCompleteTextView tvProvince;
        AutoCompleteTextView tvDistrict;
        final VolleyCocktail model;
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
                "emilia romagna",
                "friuli venezia giulia",
                "liguria",
                "trentino alto adige",
                "valle d'aosta"};


        @SuppressLint("ClickableViewAccessibility")
        public Holder() {
            tvRegion = findViewById(R.id.tvRegion);
            tvProvince = findViewById(R.id.tvProvince);
            tvDistrict = findViewById(R.id.tvDistrict);


            tvProvince.setOnTouchListener(new View.OnTouchListener(){

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    model.searchProvince(tvRegion.getText().toString());
                    Toast.makeText(getApplicationContext(), "Provincie", Toast.LENGTH_LONG).show();
                    search = 2;
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


            this.model = new VolleyCocktail() {                                                 //inizializziamo il modello di acquisizione dati che è un new VolleyCocktail
                @Override
                void fill(List<ProDis> cnt) {
                    Log.w("CA", "fill");
                    Toast.makeText(getApplicationContext(), "fill", Toast.LENGTH_LONG).show();
                    fillList(listToArrayString(cnt)); //il metodo fill chiama una funzione chiamata fillList
                }
            };
        }

        private void fillList(String[] cnt) { //fa il filling della RecyclerView
            ArrayAdapter<String> adapter;

            switch(search){
                case 2:
                    adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, cnt);
                    tvProvince.setAdapter(adapter);
                    break;

                case 3:
                    adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, cnt);
                    tvDistrict.setAdapter(adapter);
                    break;

                default:
                    // TO DO
                    break;
            }


        }

        private String[] listToArrayString(List<ProDis> list){

            String[] str = new String[list.size()] ;

            for(int i=0; i<list.size(); i++){
                str[i] = list.get(i).getProDis();
            }

            return str;
        }

    }

    abstract class VolleyCocktail implements Response.ErrorListener, Response.Listener<String> { //attraverso Volley prende i dati del sito cocktaildb, è la classe che fa da interfaccia tra cocktaildb e la nostra app
        abstract void fill(List<ProDis> cnt); //la UI sarà gestita dalla classe chiamante andando a implementare il metodo fill

        private int search = 0; ;

        public void searchRegion() {
            String url = "https://comuni-ita.herokuapp.com/api/regioni";
            search = 1;
            apiCall(url);
        }

        public void searchProvince(String id) {
            String url = "https://comuni-ita.herokuapp.com/api/province/%s"; //usiamo il metodo search per la ricerca per nome
            url = String.format(url, id);
            //Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
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