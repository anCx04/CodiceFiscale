package it.crescenziandrea.codicefiscale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.zxing.WriterException;

import java.util.List;

import it.crescenziandrea.codicefiscale.database.FiscalCode;
import it.crescenziandrea.codicefiscale.database.appFiscalCodeDatabase;

public class MainActivity extends AppCompatActivity {

    private appFiscalCodeDatabase db;
    Holder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createDB();

        FiscalCode fCode1 = new FiscalCode("josef");
        FiscalCode fCode2 = new FiscalCode("francesca");
        FiscalCode fCode3 = new FiscalCode("andrea");
        FiscalCode fCode4 = new FiscalCode("yuri");
        FiscalCode fCode5 = new FiscalCode("alessandro");
        FiscalCode fCode6 = new FiscalCode("roberto");
        FiscalCode fCode7 = new FiscalCode("Maria");
        FiscalCode fCode8 = new FiscalCode("francesco");
        db.roomDAO().addData(fCode1);
        db.roomDAO().addData(fCode2);
        db.roomDAO().addData(fCode3);
        db.roomDAO().addData(fCode4);
        db.roomDAO().addData(fCode5);
        db.roomDAO().addData(fCode6);
        db.roomDAO().addData(fCode7);
        db.roomDAO().addData(fCode8);


        holder = new Holder();

        holder.provaTXT.setText(holder.prova.Calculate());
        try {
            holder.provaVIEW.setImageBitmap(holder.prova2.generateBarCode());
        } catch (WriterException e) {
            e.printStackTrace();
        }


    }

    private void createDB() {
        db = Room.databaseBuilder(getApplicationContext(),
                appFiscalCodeDatabase.class,
                "FiscalCode.db")
                .allowMainThreadQueries()
                .build();



    }


    class Holder {

        final RecyclerView rvCocktails;
        final FloatingActionMenu materialDesignFAM;
        final FloatingActionButton floatingActionButton1;
        final FloatingActionButton floatingActionButton2;
        final TextView provaTXT;
        final CFgenerator prova;
        final BarCodeGenerator prova2;

        final ImageView provaVIEW;

        Holder() {

            rvCocktails = findViewById(R.id.recycler_view);
            materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
            floatingActionButton1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
            floatingActionButton2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);
            provaTXT = findViewById(R.id.tv_nome);
            provaVIEW = findViewById(R.id.iv_drink);

            prova = new CFgenerator("BASIRICO", "JOSEF", 25,
                    2, 1998, "M", "rm");

            prova2 = new BarCodeGenerator(prova.Calculate());

            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2, GridLayoutManager.VERTICAL, false);
            rvCocktails.setLayoutManager(layoutManager);
            fCodeAdapter mAdapter = new fCodeAdapter(db.roomDAO().getMyData());
            rvCocktails.setAdapter(mAdapter);

            int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
            int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
            rvCocktails.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));


            floatingActionButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "button1", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, ManualGeneration.class);
                    MainActivity.this.startActivity(intent);
                }
            });
            floatingActionButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "button2", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    private class fCodeAdapter extends RecyclerView.Adapter<fCodeAdapter.Holder> {

        private final List<FiscalCode> fCodes;

        fCodeAdapter(List<FiscalCode> all) {
            fCodes = all;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_leyout, parent, false);
            return new Holder(layoutView);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {

            holder.ItemView.setText(fCodes.get(position).getfCode());

        }

        @Override
        public int getItemCount() {
            return fCodes.size();
        }

        class Holder extends RecyclerView.ViewHolder{

            final TextView ItemView;

            public Holder(@NonNull View itemView) {
                super(itemView);
                ItemView = itemView.findViewById(R.id.tv_nome);
            }
        }

    }

    public class ProductGridItemDecoration extends RecyclerView.ItemDecoration {
        private int largePadding;
        private int smallPadding;

        public ProductGridItemDecoration(int largePadding, int smallPadding) {
            this.largePadding = largePadding;
            this.smallPadding = smallPadding;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = smallPadding;
            outRect.right = smallPadding;
            outRect.top = largePadding;
            outRect.bottom = largePadding;
        }

    }
}