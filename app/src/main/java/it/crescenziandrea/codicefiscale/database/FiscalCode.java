package it.crescenziandrea.codicefiscale.database;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity(tableName = "FC_table")
public class FiscalCode implements Serializable {

    @PrimaryKey(autoGenerate = true)
    protected int id;

    @ColumnInfo(name = "alias")
    protected String alias;

    @NonNull
    @ColumnInfo(name = "fCode")
    protected String fCode;



    public FiscalCode( @NonNull String fCode, String alias) {

        this.fCode = fCode;
        this.alias = alias;
    }

    @NonNull
    public String getfCode() {
        return fCode;
    }

    public void setfCode(@NonNull String fCode) {
        this.fCode = fCode;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

}
