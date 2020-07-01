package it.crescenziandrea.codicefiscale;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;

public class MainActivity extends AppCompatActivity {
    Holder holder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        holder = new Holder();

        holder.provaTXT.setText(holder.prova.Calculate());

    }

    class Holder {
        final TextView provaTXT;
        final CFgenerator prova;

        Holder() {
            provaTXT = findViewById(R.id.tv_drink);
            prova = new CFgenerator("rossi", "mario", 25,
                    1, 1998, "F", "RM");

        }
    }
}

