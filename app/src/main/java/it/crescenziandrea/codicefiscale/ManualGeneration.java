package it.crescenziandrea.codicefiscale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ResourceBundle;

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
        String[] region = new String[]{"Lazio", "Basilicata", "Molise", "Campania", "Calabria"};
        //AutoCompleteTextView tvProvince;
        //AutoCompleteTextView tvDistrict;

        public Holder() {
            tvRegion = findViewById(R.id.tvRegion);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, region);
            //tvProvince = tvProvince.findViewById(R.id.tvProvince);
            //tvDistrict = tvDistrict.findViewById(R.id.tvDistrict);
            tvRegion.setAdapter(adapter);
        }


    }
}