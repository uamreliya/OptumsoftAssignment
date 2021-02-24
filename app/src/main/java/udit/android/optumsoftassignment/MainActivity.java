package udit.android.optumsoftassignment;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import udit.android.optumsoftassignment.Adapter.SensorsAdapter;
import udit.android.optumsoftassignment.Database.AppDatabase;
import udit.android.optumsoftassignment.Database.SensorConfig;
import udit.android.optumsoftassignment.Model.DefaultTemp;
import udit.android.optumsoftassignment.Network.APIClient;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_sensors)
    RecyclerView rvSensors;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        db = Room.databaseBuilder(this, AppDatabase.class, "testing")
                .allowMainThreadQueries()
                .build();

        getSensorNames();
    }

    private void getSensorConfigs() {

        Call<HashMap<String, DefaultTemp>> sensorConfigurationCall = APIClient.getInstance().getApi().getSensorConfiguration();

        sensorConfigurationCall.enqueue(new Callback<HashMap<String, DefaultTemp>>() {
            @Override
            public void onResponse(Call<HashMap<String, DefaultTemp>> call, Response<HashMap<String, DefaultTemp>> response) {
                HashMap<String, DefaultTemp> sensorConfiguration = response.body();
                for (int i = 0; i < sensorConfiguration.size(); i++) {
                    SensorConfig sensorConfig = new SensorConfig("temperature" + i, sensorConfiguration.get("temperature" + i).getMin(), sensorConfiguration.get("temperature" + i).getMax());
                    db.taskDao().insert(sensorConfig);
                }

                ArrayList<SensorConfig> sensorConfigs = (ArrayList<SensorConfig>) db.taskDao().getAll();
                rvSensors.setLayoutManager(new GridLayoutManager(MainActivity.this, 2, GridLayoutManager.VERTICAL, false));
                SensorsAdapter sensorsAdapter = new SensorsAdapter(sensorConfigs);
                rvSensors.setAdapter(sensorsAdapter);
            }

            @Override
            public void onFailure(Call<HashMap<String, DefaultTemp>> call, Throwable t) {
                Log.i("SENSOR_NAME", t.getLocalizedMessage());
            }
        });
    }

    private void getSensorNames() {

        Call<List<String>> sensorNameCall = APIClient.getInstance().getApi().getSensorNames();
        sensorNameCall.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                List<String> sensorName = response.body();
                getSensorConfigs();
                Log.i("SENSOR_NAME", sensorName.toString());
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.i("SENSOR_NAME", t.getLocalizedMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}