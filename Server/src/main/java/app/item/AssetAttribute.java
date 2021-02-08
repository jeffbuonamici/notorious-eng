/*
  The asset Attribute class contains all the details of the asset attribute (sensors, operational settings, etc).
  This includes the id of the asset, its name and all the measurements we have for it.

  @author Paul Micu, Jeremie Chouteau
  @last_edit 02/7/2020
 */
package app.item;

import java.util.ArrayList;

public class AssetAttribute {
    private int id;
    private String name;
    private final ArrayList<Measurement> measurements;

    public AssetAttribute() {measurements = new ArrayList<>();}

    public void addMeasurement(int time, double value) {
        Measurement mNew = new Measurement(time, value);
        measurements.add(mNew);
    }

    public ArrayList<Measurement> getMeasurements() {
        return measurements;
    }

    public Double getMeasurements(int time) {
        for (Measurement measurement : measurements) {
            if (measurement.getTime() == time) {
                return measurement.getValue();
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AssetAttribute{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", measurements=" + measurements.toString() +
                '}';
    }
}
