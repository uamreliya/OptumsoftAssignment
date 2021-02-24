package udit.android.optumsoftassignment.Model;

import java.util.List;

public class SocketData {

    private String type;
    private List<Data> recent;
    private List<Data> minute;
    private String key;
    private String val;
    private String sensor;
    private String scale;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Data> getRecent() {
        return recent;
    }

    public void setRecent(List<Data> recent) {
        this.recent = recent;
    }

    public List<Data> getMinute() {
        return minute;
    }

    public void setMinute(List<Data> minute) {
        this.minute = minute;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    @Override
    public String toString() {
        return "SocketData{" +
                "type='" + type + '\'' +
                ", recent=" + recent +
                ", minute=" + minute +
                ", key='" + key + '\'' +
                ", val='" + val + '\'' +
                ", sensor='" + sensor + '\'' +
                ", scale='" + scale + '\'' +
                '}';
    }

    public class Data {
        private double key;
        private double val;

        public Data(double key, double val) {
            this.key = key;
            this.val = val;
        }

        public double getKey() {
            return key;
        }

        public void setKey(double key) {
            this.key = key;
        }

        public double getVal() {
            return val;
        }

        public void setVal(double val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "key=" + key +
                    ", val=" + val +
                    '}';
        }
    }

}
