package it.crescenziandrea.codicefiscale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

import it.crescenziandrea.codicefiscale.database.FiscalCode;
import it.crescenziandrea.codicefiscale.database.appFiscalCodeDatabase;

public class MainActivity extends AppCompatActivity {

    private appFiscalCodeDatabase db;
    Holder holder;
    private final int keyReqManualGen = 10;
    private final int keyReqAutGen = 49374;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createDB();

        FiscalCode fCode1 = new FiscalCode( "josef","josef");

        db.roomDAO().addData(fCode1);



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


    class Holder implements FloatingActionButton.OnClickListener {

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
                    Intent intent = new Intent(MainActivity.this, ManualGeneration.class);
                    MainActivity.this.startActivity(intent);
                }
            });
            floatingActionButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                    integrator.initiateScan();

                    new IntentIntegrator(MainActivity.this).initiateScan();


                }
            });
            floatingActionButton1.setOnClickListener(this);
            floatingActionButton2.setOnClickListener(this);


        }


        public  void activityResult(int requestCode, int resultCode, Intent data) {

            // requestCode = 49374
            switch(requestCode){
                case keyReqManualGen:

                    if (  resultCode == RESULT_OK && data != null) {
                        String str1 = data.getStringExtra("alias");
                        String str2 = data.getStringExtra("fCode");

                        FiscalCode fcDatabase = new FiscalCode(str2,str1);
                        db.roomDAO().addData(fcDatabase);
                    }

                    break;
                case keyReqAutGen:
                    IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                    if (result != null) {
                        if (result.getContents() == null) {
                            Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
                        } else {
                            FiscalCode fcDatabase = new FiscalCode(result.getContents(),"autosave");
                            db.roomDAO().addData(fcDatabase);
                        }
                    } else {
                        // to do
                    }
            }

        }

        @Override
        public void onClick(View v) {

            if(v.getId() == floatingActionButton1.getId()){

                Intent intent = new Intent(MainActivity.this, ManualGeneration.class);
                MainActivity.this.startActivityForResult(intent,keyReqManualGen);

            }

            if(v.getId() == floatingActionButton2.getId()){


                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.initiateScan();
                new IntentIntegrator(MainActivity.this).initiateScan();

            }


        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        holder.activityResult(requestCode, resultCode, data);

    }




    private class fCodeAdapter extends RecyclerView.Adapter<fCodeAdapter.Holder> {

        private final List<FiscalCode> fCodes;

        fCodeAdapter(List<FiscalCode> all) {
            fCodes = all;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_layout, parent, false);
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

    public void createEntryFC(){
        return;
    }
}