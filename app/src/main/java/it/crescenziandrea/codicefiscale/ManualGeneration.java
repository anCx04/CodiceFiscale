package it.crescenziandrea.codicefiscale;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import java.util.TimeZone;

public class ManualGeneration extends AppCompatActivity  {


    String bDayKey = "bDay";
    String bMounthKey = "bMount";
    String bYearKey = "bYear";

    Holder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_generation);

        holder = new Holder();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        //save the date values
        outState.putInt(bDayKey, holder.bDay);
        outState.putInt(bMounthKey, holder.bMonth);
        outState.putInt(bYearKey, holder.bYear);
        super.onSaveInstanceState(outState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) { //gli passiamo lo stesso bundle che avevamo avuto in input nella onSaveInstanceState per fare il restoring dello stato

        //restore the date values
        holder.bDay = savedInstanceState.getInt(bDayKey);
        holder.bMonth = savedInstanceState.getInt(bMounthKey);
        holder.bYear = savedInstanceState.getInt(bYearKey);
        holder.btn.setText(holder.bDay+"/"+holder.bMonth+"/"+holder.bYear);
        super.onRestoreInstanceState(savedInstanceState);
    }

    class Holder implements DatePickerDialog.OnDateSetListener,View.OnClickListener, View.OnTouchListener {

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
        String str2;
        TextInputEditText alias;
        Button btn;
        Button bt_gen;
        final VolleyApi model;
        Calendar calendar ;
        int day ;
        int month;
        int year;
        int bDay = -1;
        int bMonth = -1;
        int bYear = -1;
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

            // I link the objects to the xml objects
            tvSurname = findViewById(R.id.surname);
            tvName = findViewById(R.id.name);
            tvRegion = findViewById(R.id.tvRegion);
            tvProvince = findViewById(R.id.tvProvince);
            tvDistrict = findViewById(R.id.tvDistrict);
            tvGender = findViewById(R.id.tvGender);
            bt_gen = findViewById(R.id.bt_gen);
            alias = findViewById(R.id.alias);
            btn = findViewById(R.id.calendar);

            //set onclicklistners
            btn.setOnClickListener(this);
            bt_gen.setOnClickListener(this);
            tvProvince.setOnTouchListener(this);
            tvDistrict.setOnTouchListener(this);


            //AutoComplitTextView disability
            tvProvince.setEnabled(false);
            tvDistrict.setEnabled(false);

            //set calendar
            calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH);
            year = calendar.get(Calendar.YEAR);
            calendar.set(day,month,year);


            //enable tvProvince only after pressing the previous tvRegion
            tvRegion.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if (tvRegion.isPerformingCompletion()) {
                        tvProvince.setEnabled(true);
                        tvDistrict.setEnabled(false);
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            //enable tvDistrict only after pressing the previous tvProvince
            tvProvince.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    tvDistrict.setEnabled(true);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            //set adapter tvRegion
            ArrayAdapter<String> adapterRegion = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, region);
            tvRegion.setAdapter(adapterRegion);

            //set adapter gender
            ArrayAdapter<String> adapterGender = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, gender);
            tvGender.setAdapter(adapterGender);



            //implement abstract method volley
            this.model = new VolleyApi() {
                @Override
                void fill(List<ProDis> cnt) {
                    fillList(cnt);
                }
            };
        }

        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
           //save the date in the variables in the holder

           bDay = dayOfMonth;
           bMonth = (monthOfYear+1);
           bYear = year;

           str2 = bDay +"/"+ bMonth +"/"+bYear;
           btn.setText(str2);

           return ;
        }

        private void fillList(List<ProDis> cnt) {

            //it fills the RecyclerView according to the pressed autocompletetextview

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

                //check if all fields have been entered
                if(tvSurname.getText().toString().isEmpty() || tvName.getText().toString().isEmpty() || tvGender.getText().toString().isEmpty() || tvRegion.getText().toString().isEmpty()
                    || tvProvince.getText().toString().isEmpty() || tvDistrict.getText().toString().isEmpty() || bDay == -1 || bMonth == -1 || bYear == -1 || alias.getText().toString().isEmpty() ){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.complete),Toast.LENGTH_LONG).show();
                }
                else {

                    //generate the fiscalcode using the data entered
                    cfcode = new CFgenerator(tvSurname.getText().toString().toUpperCase(),
                            tvName.getText().toString().toUpperCase(),
                            bDay,
                            bMonth,
                            bYear,
                            tvGender.getText().toString().toUpperCase(),
                            prov.get(getIndexOf(str, tvDistrict.getText().toString())).getCodCat());


                    Intent output = new Intent();
                    output.putExtra("alias", alias.getText().toString());
                    output.putExtra("fCode", cfcode.Calculate());
                    setResult(RESULT_OK, output);
                    finish();
                }
            }

            //calback for popup calendar
            if(v.getId() == btn.getId()) {

                DatePickerDialog dialog = DatePickerDialog.newInstance(this);
                dialog.show(getSupportFragmentManager(), "DatePickerDialog");
            }

        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            //intercepts the touch of autocomplittextview to search for provinces / municipalities

            if(v.getId() == tvProvince.getId()){
                model.searchProvince(tvRegion.getText().toString());
                search = 2;
            }

            if(v.getId() == tvDistrict.getId()){
                model.searchDistrict(tvProvince.getText().toString());
                search = 3;
            }
            return false;
        }


    }


    abstract class VolleyApi implements Response.ErrorListener, Response.Listener<String> { //attraverso Volley prende i dati del sito cocktaildb, è la classe che fa da interfaccia tra cocktaildb e la nostra app
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
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error), Toast.LENGTH_LONG).show(); //mettiamo un toast per dire all'utente che c'è stato qualcosa che non è andato a buon fine
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
                            //fill(cnt);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                case 3:
                    try {
                        //le api rispondono con un array con dentro dei jeson object
                        JSONArray jsonArray = new JSONArray(response);
                        res = jsonArray.toString();
                        Type listType = new TypeToken<List<ProDis>>() {}.getType();
                        List<ProDis> cnt = gson.fromJson(res, listType);
                        if (cnt != null && cnt.size() > 0) {
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