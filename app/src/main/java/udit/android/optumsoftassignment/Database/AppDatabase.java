package udit.android.optumsoftassignment.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {SensorConfig.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract TaskDao taskDao();
}
