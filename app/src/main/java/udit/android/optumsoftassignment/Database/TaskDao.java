package udit.android.optumsoftassignment.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM SensorConfig")
    List<SensorConfig> getAll();

    @Query("SELECT * FROM SensorConfig WHERE sensorName =:id")
    SensorConfig getSensorDetails(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SensorConfig config);

    @Delete
    void delete(SensorConfig sensorConfig);

    @Update
    void update(SensorConfig config);
}
