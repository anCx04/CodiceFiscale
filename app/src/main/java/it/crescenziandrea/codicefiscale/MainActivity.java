package it.crescenziandrea.codicefiscale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
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
    private final int viewRecCode = 99;
    final String saveStateShow = "show";
    final String saveFcodeKey = "result";
    private int show = 0;
    AlertDialog alertDialogAndroid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createDB();
        holder = new Holder();

    }

    @Override
    protected void onResume() {
        super.onResume();
        holder.genRecycleView();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) { //vado a scrivere nel Bundle che mi viene passato che sarà poi passato nuovamente all'app per salvarne lo stato
        if(show == 1) {
            outState.putInt(saveStateShow,show);
            outState.putString(saveFcodeKey, holder.str.toString()); //aggiungiamo al Bundle coppie chiave/valore
            alertDialogAndroid.dismiss();
        }
        super.onSaveInstanceState(outState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) { //gli passiamo lo stesso bundle che avevamo avuto in input nella onSaveInstanceState per fare il restoring dello stato

        if(savedInstanceState.getInt(saveStateShow) == 1) {
            showPopUp(savedInstanceState.getString(saveFcodeKey));
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void createDB() {
        db = Room.databaseBuilder(getApplicationContext(),
                appFiscalCodeDatabase.class,
                "FiscalCode.db")
                .allowMainThreadQueries()
                .build();
    }


    class Holder implements  View.OnClickListener {

        final RecyclerView rvCocktails;
        final FloatingActionMenu materialDesignFAM;
        final FloatingActionButton floatingActionButton1;
        final FloatingActionButton floatingActionButton2;
        String str;


        Holder() {

            rvCocktails = findViewById(R.id.recycler_view);
            materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
            floatingActionButton1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
            floatingActionButton2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);



            genRecycleView();

            int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
            int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
            rvCocktails.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));


            floatingActionButton1.setOnClickListener(this);
            floatingActionButton2.setOnClickListener(this);


        }

        public void genRecycleView(){
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2, GridLayoutManager.VERTICAL, false);
            rvCocktails.setLayoutManager(layoutManager);
            fCodeAdapter mAdapter = new fCodeAdapter(db.roomDAO().getMyData());
            rvCocktails.setAdapter(mAdapter);
        }

        public  void activityResult(int requestCode, int resultCode, Intent data) {

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
                    IntentResult result  = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                    if (result != null) {
                        if (result.getContents() == null) {
                            Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
                        } else {
                            show = 1;
                            str = result.getContents();
                            showPopUp(str);

                        }
                    } else {
                        // to do
                    }
                    break;
                case viewRecCode:
                    if(resultCode == viewRecCode){
                        FiscalCode fiscalCode = (FiscalCode) data.getSerializableExtra("fCode");
                        db.roomDAO().delete(fiscalCode);
                    }

                default:
                    // TO DO
                    break;
            }

        }

        @Override
        public void onClick(View v) {

            if(v.getId() == floatingActionButton1.getId()){

                materialDesignFAM.close(true);

                Intent intent = new Intent(MainActivity.this, ManualGeneration.class);
                MainActivity.this.startActivityForResult(intent,keyReqManualGen);

            }

            if(v.getId() == floatingActionButton2.getId()){

                materialDesignFAM.close(true);

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




    private class fCodeAdapter extends RecyclerView.Adapter<fCodeAdapter.Holder> implements View.OnClickListener {

        private final List<FiscalCode> fCodes;

        fCodeAdapter(List<FiscalCode> all) {
            fCodes = all;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_layout, parent, false);
            layoutView.setOnClickListener(this);
            return new Holder(layoutView);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {

            holder.tv_name.setText(fCodes.get(position).getAlias());
            holder.tv_fCode.setText(fCodes.get(position).getfCode());

        }

        @Override
        public int getItemCount() {
            return fCodes.size();
        }

        @Override
        public void onClick(View v) {

            int position = ((RecyclerView) v.getParent()).getChildAdapterPosition(v);
            FiscalCode fiscalCode = fCodes.get(position);

            Intent intent = new Intent(MainActivity.this, barCodeActivity.class);
            intent.putExtra("fiscalCode",fiscalCode);
            MainActivity.this.startActivityForResult(intent,viewRecCode);

        }

        class Holder extends RecyclerView.ViewHolder{

            final TextView tv_name;
            final TextView tv_fCode;
            final ImageView iv_fcode;

            public Holder(@NonNull View itemView) {
                super(itemView);
                tv_name = itemView.findViewById(R.id.tv_nome);
                tv_fCode = itemView.findViewById(R.id.tv_FiscalCode);
                iv_fcode = itemView.findViewById(R.id.iv_fcode);

                iv_fcode.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.tslogo2));
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

    public void showPopUp(String result){

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(MainActivity.this);
        View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(mView);

        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        alertDialogBuilderUserInput.setCancelable(false);
        alertDialogBuilderUserInput.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogBox, int id) {

                FiscalCode fcDatabase = new FiscalCode(result,userInputDialogEditText.getText().toString());
                db.roomDAO().addData(fcDatabase);

                holder.genRecycleView();
                show = 0;
                dialogBox.cancel();

            }
        });
        alertDialogBuilderUserInput.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        dialogBox.cancel();
                    }
                });

        alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

}