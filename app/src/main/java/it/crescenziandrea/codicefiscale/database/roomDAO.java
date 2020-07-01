package it.crescenziandrea.codicefiscale.database;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface roomDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void addData(FiscalCode fCode);

    @Query("select * from FC_table")
    public List<FiscalCode>getMyData();

    @Delete
    void delete(FiscalCode fCode);

    @Update
    void update(FiscalCode fCode);


}
