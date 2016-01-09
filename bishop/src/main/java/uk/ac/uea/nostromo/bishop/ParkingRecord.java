package uk.ac.uea.nostromo.bishop;

/**
 * Created by Barry on 09/01/2016.
 */
public class ParkingRecord {
    int _id;
    String parkName, zone, startTime, endTime, fee;

    public ParkingRecord(int _id, String parkName, String zone, String fee, String startTime, String endTime) {
        this._id = _id;
        this.parkName = parkName;
        this.zone = zone;
        this.fee = fee;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public ParkingRecord(String parkName, String zone, String fee, String startTime, String endTime) {
        this.parkName = parkName;
        this.zone = zone;
        this.fee = fee;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }
}
