package udit.android.optumsoftassignment.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import udit.android.optumsoftassignment.Database.SensorConfig;
import udit.android.optumsoftassignment.GraphActivity;
import udit.android.optumsoftassignment.R;

public class SensorsAdapter extends RecyclerView.Adapter<SensorsAdapter.MyViewHolder> {

    Context context;
    ArrayList<SensorConfig> sensorConfigs = new ArrayList<>();

    public SensorsAdapter(ArrayList<SensorConfig> sensorConfigs) {
        this.sensorConfigs = sensorConfigs;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sensor_itemview, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        SensorConfig sensorConfig = sensorConfigs.get(position);

        holder.tvSensorName.setText(sensorConfig.getSensorName());
        holder.tvMin.setText(String.valueOf(sensorConfig.getMin()));
        holder.tvMax.setText(String.valueOf(sensorConfig.getMax()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GraphActivity.class);
                intent.putExtra("sensor_name", sensorConfig.getSensorName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sensorConfigs.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_sensor_name)
        TextView tvSensorName;
        @BindView(R.id.tv_min)
        TextView tvMin;
        @BindView(R.id.tv_max)
        TextView tvMax;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
