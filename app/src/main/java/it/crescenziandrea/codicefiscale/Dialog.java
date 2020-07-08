package it.crescenziandrea.codicefiscale;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class Dialog extends AppCompatDialogFragment{
    private EditText CFcode;
    private EditText alias;
    private ImageView barCode;

    private DialogListener listener;

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        CFcode = view.findViewById(R.id.CFresult);
        CFcode.setText(listener.setCFcode());

        alias = view.findViewById(R.id.alias);
        barCode = view.findViewById(R.id.ivBarCode);

        builder.setView(view).setTitle("preview").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String CFcodeResult = CFcode.getText().toString();
                String givenAlias = alias.getText().toString();
                listener.applyEntry(CFcodeResult, givenAlias);
            }
        });



        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"implementare il listener");
        }

    }

    public interface DialogListener {
        void applyEntry(String Cfcode, String alias);
        String setCFcode();
    }
}
