package it.crescenziandrea.codicefiscale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
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

        //String[] region = new String[]{"Lazio", "Basilicata", "Molise", "Campania", "Calabria"};
        String[] province = new String[]{"Roma", "Latina"};
        String[] district = new String[]{"Colleferro", "Artena", "Velletri", "Valmontone"};

        public Holder() {
            tvRegion = findViewById(R.id.tvRegion);
            tvProvince = findViewById(R.id.tvProvince);
            tvDistrict = findViewById(R.id.tvDistrict);
            /*tvRegion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.searchRegion();
                }
            });*/
            //ArrayAdapter<String> adapterRegion = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, region);
            ArrayAdapter<String> adapterProvince = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, province);
            ArrayAdapter<String> adapterDistrict = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, district);

            //tvRegion.setAdapter(adapterRegion);
            tvProvince.setAdapter(adapterProvince);
            tvDistrict.setAdapter(adapterDistrict);

            this.model = new VolleyCocktail() { //inizializziamo il modello di acquisizione dati che è un new VolleyCocktail
                @Override
                void fill(List cnt) {
                    Log.w("CA", "fill");
                    //fillList(cnt); //il metodo fill chiama una funzione chiamata fillList
                }

              /*  private void fillList(List cnt) { //fa il filling della RecyclerView
                    String[] region = new String[0];
                    for(int i=0; i<cnt.size(); i++){
                        region += cnt.get(i);
                    }
                    //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CocktailActivity.this);
                    ArrayAdapter<String> adapterRegion = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, region);
                    tvRegion.setAdapter(adapterRegion);
                    //rvCocktails.setLayoutManager(layoutManager);
                    //CocktailAdapter mAdapter = new CocktailAdapter(cnt); //l'adapter sarà di tipo CocktailAdapter a cui passiamo la lista cnt
                    //rvCocktails.setAdapter(mAdapter); //i cocktail vengono messi in lista
                }*/
            };
        }
    }

    abstract class VolleyCocktail implements Response.ErrorListener, Response.Listener<String> { //attraverso Volley prende i dati del sito cocktaildb, è la classe che fa da interfaccia tra cocktaildb e la nostra app
        abstract void fill(List cnt); //la UI sarà gestita dalla classe chiamante andando a implementare il metodo fill
        private static final String APIKEY = "1";
        public void searchRegion() {
            String url = "https://comuni-ita.herokuapp.com/api/regioni";
            apiCall(url);
        }

        public void searchProvince(String id) {
            String url = "https://comuni-ita.herokuapp.com/api/province/%s"; //usiamo il metodo search per la ricerca per nome
            url = String.format(url, id);
            apiCall(url);
        }

        public void searchDistrict(String id) {
            String url = "https://comuni-ita.herokuapp.com/api/comuni/provincia/%s"; //usiamo il metodo filter, i è l'ingrediente che andiamo a richiedere
            url = String.format(url, id);
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
            try {
                JSONObject jsonObject = new JSONObject(response); //utilizzando la classe JSONObject
                String drinks = jsonObject.getJSONArray("").toString();
                Type listType = new TypeToken<String>() {}.getType();
                String[] cnt = gson.fromJson(drinks, listType); //Diamo la stringa in pasto al gson utilizzando come tipo di dati una lista di tipo cocktail
                Log.w("CA", cnt[0]);
                //if (cnt != null && cnt.size() > 0) { //se la lista dei cocktail è diversa da null, quindi se non ci sono stati errori legati all'assegnazione nella riga precedente, e se abbiamo trovato qualcosa all'interno della struttura
                  //  Log.w("CA", "" + cnt.size());
                    //fill(cnt);
                } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}