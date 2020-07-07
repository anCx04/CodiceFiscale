package it.crescenziandrea.codicefiscale.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {FiscalCode.class}, version = 1)
public abstract class appFiscalCodeDatabase extends RoomDatabase {

    public abstract roomDAO roomDAO();

}
