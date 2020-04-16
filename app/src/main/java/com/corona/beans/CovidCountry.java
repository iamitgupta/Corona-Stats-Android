package com.corona.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CovidCountry implements Serializable {

@SerializedName("Country")
@Expose
private String country;
@SerializedName("Province")
@Expose
private String province;
@SerializedName("Lat")
@Expose
private Double lat;
@SerializedName("Lon")
@Expose
private Double lon;
@SerializedName("Date")
@Expose
private String date;
@SerializedName("Cases")
@Expose
private Long cases;
@SerializedName("Status")
@Expose
private String status;

public String getCountry() {
return country;
}

public void setCountry(String country) {
this.country = country;
}

public String getProvince() {
return province;
}

public void setProvince(String province) {
this.province = province;
}

public Double getLat() {
return lat;
}

public void setLat(Double lat) {
this.lat = lat;
}

public Double getLon() {
return lon;
}

public void setLon(Double lon) {
this.lon = lon;
}

public String getDate() {
return date;
}

public void setDate(String date) {
this.date = date;
}

public Long getCases() {
return cases;
}

public void setCases(Long cases) {
this.cases = cases;
}

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

    @Override
    public String toString() {
        return "CovidCountry{" +
                "country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", date='" + date + '\'' +
                ", cases=" + cases +
                ", status='" + status + '\'' +
                '}';
    }
}