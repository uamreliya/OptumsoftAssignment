package udit.android.optumsoftassignment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import udit.android.optumsoftassignment.Database.AppDatabase;
import udit.android.optumsoftassignment.Database.SensorConfig;
import udit.android.optumsoftassignment.Model.SocketData;
import udit.android.optumsoftassignment.Socket.SocketClass;

public class GraphActivity extends AppCompatActivity {

    @BindView(R.id.lineChart)
    LineChart lineChart;

    private Thread thread;
    private boolean plotData = true;
    private Socket socket;
    private String sensor_name;
    private Gson gson = new Gson();
    private float min, max;
    private AppDatabase db;
    private SensorConfig sensorData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        ButterKnife.bind(this);

        SocketClass socketClass = new SocketClass();
        socket = socketClass.getSocket();

        db = Room.databaseBuilder(this, AppDatabase.class, "testing")
                .allowMainThreadQueries()
                .build();

        if (getIntent().getExtras() != null) {
            sensor_name = getIntent().getStringExtra("sensor_name");
        }
        sensorData = db.taskDao().getSensorDetails(sensor_name);
        Log.i("SENSOR_DB", sensorData.toString());

        socket.on(Socket.EVENT_CONNECT, onConnect);
        socket.on(Socket.EVENT_CONNECT_ERROR, onConnectionError);
        socket.on("data", onSubscribe);

        socket.connect();
        lineChart.getDescription().setEnabled(true);
        lineChart.getDescription().setText(sensor_name + " Real time temperature.");

        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setPinchZoom(true);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);
        lineChart.setData(data);

        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextColor(Color.WHITE);

        XAxis xl = lineChart.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(true);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawGridLines(false);
        leftAxis.setLabelCount(3);
        leftAxis.setAxisMaximum(10f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);

        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.setDrawBorders(false);

//        startPlot();
    }

    private void startPlot() {
        if (thread != null) {
            thread.interrupt();
        }

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    plotData = true;
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(GraphActivity.this, "CONNECTION ESTABLISHED", Toast.LENGTH_SHORT).show();
                    socket.emit("subscribe", sensor_name);
                }
            });
        }
    };

    private Emitter.Listener onConnectionError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            Toast.makeText(GraphActivity.this, "CONNECTION ERROR", Toast.LENGTH_SHORT).show();
        }
    };

    private Emitter.Listener onSubscribe = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            try {
                JSONObject jsonObject = new JSONObject(args[0].toString());
                SocketData dataString = gson.fromJson(jsonObject.toString(), SocketData.class);
                if (plotData) {
                    addEvent(dataString);
//                    plotData = false;
                }
                Log.i("SOCKET_DATA", dataString.toString());
            } catch (JSONException e) {
                Log.i("SOCKET_DATA", e.getMessage());
                e.printStackTrace();
            }

        }
    };

    private void addEvent(SocketData dataString) {
        LineData data = lineChart.getData();

        if (data != null) {
            ILineDataSet setY = data.getDataSetByIndex(0);

            if (setY == null) {
                setY = createSetY();
                data.addDataSet(setY);
            }

            if ("init".equals(dataString.getType())) {
                for (int i = 0; i < dataString.getRecent().size(); i++) {
//                    if (dataString.getRecent().get(i).getVal() < sensorData.getMin()) {
//                        data.addEntry(new Entry(setY.getEntryCount(), -1), 0);
//                    } else if (dataString.getRecent().get(i).getVal() > sensorData.getMax()) {
//                        data.addEntry(new Entry(setY.getEntryCount(), 1), 0);
//                    } else {
//                        data.addEntry(new Entry(setY.getEntryCount(), 0), 0);
//                    }
                    data.addEntry(new Entry(setY.getEntryCount(), Float.parseFloat(String.valueOf(dataString.getRecent().get(i).getVal()))), 0);
                }
                data.notifyDataChanged();

                lineChart.notifyDataSetChanged();
//                lineChart.setMaxVisibleValueCount(150);
                lineChart.setVisibleXRangeMaximum(30);
                lineChart.moveViewToX(data.getEntryCount());
            } else if ("update".equals(dataString.getType())) {
                Log.i("DATA_POINT", String.valueOf(Float.parseFloat(dataString.getVal())));
//                if (Double.parseDouble(dataString.getVal()) < sensorData.getMin()) {
//                    data.addEntry(new Entry(setY.getEntryCount(), -1), 0);
//                } else if (Double.parseDouble(dataString.getVal()) > sensorData.getMax()) {
//                    data.addEntry(new Entry(setY.getEntryCount(), 1), 0);
//                } else {
//                    data.addEntry(new Entry(setY.getEntryCount(), 0), 0);
//                }
                data.addEntry(new Entry(setY.getEntryCount(), Float.parseFloat(dataString.getVal())), 0);
                data.notifyDataChanged();

                if (setY.getEntryCount() >= 30) {
                    setY.removeFirst();
                    for (int i = 0; i < setY.getEntryCount(); i++) {
                        Entry changeEntry = setY.getEntryForIndex(i);
                        changeEntry.setX(changeEntry.getX() - 1);

                    }
                }

                lineChart.notifyDataSetChanged();
//                lineChart.setMaxVisibleValueCount(150);
                lineChart.setVisibleXRangeMaximum(30);
                lineChart.moveViewToX(data.getEntryCount());
            }
        } else {
            Log.i("DATA_SET", "dataset null");
        }
    }

    private ILineDataSet createSetY() {
        LineDataSet set = new LineDataSet(null, "Temperature Data");
        set.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set.setLineWidth(3f);
        set.setColor(Color.BLACK);
        set.setHighlightEnabled(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.02f);
        return set;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (socket.connected()) {
            socket.disconnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}