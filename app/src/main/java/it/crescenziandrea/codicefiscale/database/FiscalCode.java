package it.crescenziandrea.codicefiscale.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "FC_table")
public class FiscalCode {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "fCode")
    private String fCode;

    public FiscalCode(@NonNull String str) {
        this.fCode = str;
    }

    @NonNull
    public String getfCode() {
        return this.fCode;
    }

    public void setfcode(String str){ this.fCode = str; }

}
