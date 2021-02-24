package udit.android.optumsoftassignment.Network;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import udit.android.optumsoftassignment.Model.DefaultTemp;

public interface APICalls {

    @GET("sensornames")
    Call<List<String>> getSensorNames();

    @GET("config")
    Call<HashMap<String, DefaultTemp>> getSensorConfiguration();
}
