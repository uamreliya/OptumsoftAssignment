package udit.android.optumsoftassignment.Database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SensorConfig {

    @NonNull
    @PrimaryKey
    private String sensorName;

    @ColumnInfo(name = "min")
    private int min;

    @ColumnInfo(name = "max")
    private int max;

    public SensorConfig() {
    }

    public SensorConfig(@NonNull String sensorName, int min, int max) {
        this.sensorName = sensorName;
        this.min = min;
        this.max = max;
    }

    @NonNull
    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(@NonNull String sensorName) {
        this.sensorName = sensorName;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return "SensorConfig{" +
                "sensorName='" + sensorName + '\'' +
                ", min=" + min +
                ", max=" + max +
                '}';
    }
}
