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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_generation);
    }

    class Holder{

        AutoCompleteTextView tvRegion;
        AutoCompleteTextView tvProvince;
        AutoCompleteTextView tvDistrict;

        public Holder() {
            super();
            tvRegion = tvRegion.findViewById(R.id.tvRegion);
            //tvProvince = tvProvince.findViewById(R.id.tvProvince);
            tvDistrict = tvDistrict.findViewById(R.id.tvDistrict);
            String[] region = new String[]{"Lazio", "Basilicata", "Molise", "Campania", "Calabria"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item_1, region);
            tvRegion.setAdapter(adapter);
        }


    }
}