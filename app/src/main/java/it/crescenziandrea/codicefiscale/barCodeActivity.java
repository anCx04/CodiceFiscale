package it.crescenziandrea.codicefiscale;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.WriterException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import it.crescenziandrea.codicefiscale.database.FiscalCode;

public class barCodeActivity extends AppCompatActivity implements View.OnClickListener {

    private final int deleteCode = 99;
    ImageView iv_barCode;
    TextView tv_fcode;
    FloatingActionButton fab;
    FiscalCode fiscalCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code);

        // I link the objects to the xml objects
        iv_barCode = findViewById(R.id.iv_barCode);
        tv_fcode = findViewById(R.id.tv_fcode);
        fab = findViewById(R.id.fab);

        Intent data = getIntent();
        fiscalCode = (FiscalCode) data.getSerializableExtra("fiscalCode");

        //generate the barr code
        BarCodeGenerator barCodeGenerator = new BarCodeGenerator(fiscalCode.getfCode());
        tv_fcode.setText(fiscalCode.getfCode());

        try {
            iv_barCode.setImageBitmap(barCodeGenerator.generateBarCode()); //set the bitmap to create the bar code
        } catch (WriterException e) {
            e.printStackTrace();
        }

        //set onclicklistners
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        //returns the fiscalcode that the main activity will have to cancel
        if(v.getId() == fab.getId()){
            Intent output = new Intent();
            output.putExtra("fCode",fiscalCode);
            setResult(deleteCode, output);
            finish();
        }

    }
}