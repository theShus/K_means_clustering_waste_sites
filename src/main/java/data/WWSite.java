package data;

public class WWSite {

    private String name;
    private double capacity;
    private double latitude;
    private double longitude;

    public WWSite(String name, double capacity, double latitude, double longitude) {
        this.name = name;
        this.capacity = capacity;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "data.WWSite{" +
                "name='" + name + '\'' +
                ", capacity=" + capacity +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
