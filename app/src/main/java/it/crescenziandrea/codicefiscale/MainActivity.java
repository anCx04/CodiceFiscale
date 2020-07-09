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
        //updates the RecycleView
        holder.genRecycleView();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        // save the values ​​when I call the popup and the screen is turned if there is no popup I don't need show = 1 there is, 0 otherwise
        if(show == 1) {
            // except fCode is show to find out if I was in this state
            outState.putInt(saveStateShow,show);
            outState.putString(saveFcodeKey, holder.str);
            alertDialogAndroid.dismiss();                   // I reset the alertdialogandroid
        }
        super.onSaveInstanceState(outState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        // if show == 1 I regenerate the popup
        if(savedInstanceState.getInt(saveStateShow) == 1) {
            showPopUp(savedInstanceState.getString(saveFcodeKey));
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    // I create the database
    private void createDB() {
        db = Room.databaseBuilder(getApplicationContext(),
                appFiscalCodeDatabase.class,
                "FiscalCode.db")
                .allowMainThreadQueries()
                .build();
    }


    class Holder implements  View.OnClickListener {

        final RecyclerView rvFcode;
        final FloatingActionMenu materialDesignFAM;
        final FloatingActionButton floatingActionButton1;
        final FloatingActionButton floatingActionButton2;
        String str;


        Holder() {


            // I link the objects to the xml objects
            rvFcode = findViewById(R.id.recycler_view);
            materialDesignFAM =  findViewById(R.id.material_design_android_floating_action_menu);
            floatingActionButton1 =  findViewById(R.id.material_design_floating_action_menu_item1);
            floatingActionButton2 =  findViewById(R.id.material_design_floating_action_menu_item2);

            genRecycleView();           // function that generates the recyclerView

            //  I fix the cardview padding
            int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
            int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
            rvFcode.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));


            //set onclicklistners
            floatingActionButton1.setOnClickListener(this);
            floatingActionButton2.setOnClickListener(this);


        }

        //  function that generates the RecycleView
        public void genRecycleView(){
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2, GridLayoutManager.VERTICAL, false);
            rvFcode.setLayoutManager(layoutManager);
            fCodeAdapter mAdapter = new fCodeAdapter(db.roomDAO().getMyData());
            rvFcode.setAdapter(mAdapter);
        }

        //  function that manages the response of the activities called
        public  void activityResult(int requestCode, int resultCode, Intent data) {

            //save and delate the data in the database from different activities
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

            // calls the different activities according to the button that is pressed
            if(v.getId() == floatingActionButton1.getId()){

                //closes the menu that has been pressed
                materialDesignFAM.close(true);

                Intent intent = new Intent(MainActivity.this, ManualGeneration.class);
                MainActivity.this.startActivityForResult(intent,keyReqManualGen);

            }

            if(v.getId() == floatingActionButton2.getId()){

                //closes the menu that has been pressed
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
        holder.activityResult(requestCode, resultCode, data);           // function in holder class

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

            //populate card views with data
            holder.tv_name.setText(fCodes.get(position).getAlias());
            holder.tv_fCode.setText(fCodes.get(position).getfCode());

        }

        @Override
        public int getItemCount() {
            return fCodes.size();
        }

        @Override
        public void onClick(View v) {

            //calls an activity that generates the barcode according to the cardview pressed

            //save the position and then the fiscalcode in position i according to what has been pressed
            int position = ((RecyclerView) v.getParent()).getChildAdapterPosition(v);
            FiscalCode fiscalCode = fCodes.get(position);

            //call barCodeActivity  passing the fiscalcode
            Intent intent = new Intent(MainActivity.this, barCodeActivity.class);
            intent.putExtra("fiscalCode",fiscalCode);
            MainActivity.this.startActivityForResult(intent,viewRecCode);

        }

        //cardview holder
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

    //function padding cardview
    public class ProductGridItemDecoration extends RecyclerView.ItemDecoration {
        private int largePadding;
        private int smallPadding;

        public ProductGridItemDecoration(int largePadding, int smallPadding) {
            this.largePadding = largePadding;
            this.smallPadding = smallPadding;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = smallPadding;
            outRect.right = smallPadding;
            outRect.top = largePadding;
            outRect.bottom = largePadding;
        }

    }

    //function popup
    public void showPopUp(String result){

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(MainActivity.this);
        View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(mView);

        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        alertDialogBuilderUserInput.setCancelable(false);
        alertDialogBuilderUserInput.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogBox, int id) {

                //if you press the send button save in the database, regenerate the recycleview and close the popup
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
                        // close popup
                        dialogBox.cancel();
                    }
                });

        alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

}