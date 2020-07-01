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
    public String fCode;

    public FiscalCode(@NonNull String fCode) {
        this.fCode = fCode;
    }


    @NonNull
    public String getfCode() {
        return fCode;
    }

    public void setfCode(@NonNull String fCode) {
        this.fCode = fCode;
    }
}
