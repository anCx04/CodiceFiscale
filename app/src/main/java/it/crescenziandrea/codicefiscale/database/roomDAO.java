package it.crescenziandrea.codicefiscale.database;


import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

public interface roomDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addData(FiscalCode fCode);

    @Query("select * from fc_table")
    List<FiscalCode>getMyData();


    @Delete
    void delete(FiscalCode fCode);

    @Update
    void update(FiscalCode fCode);


}
