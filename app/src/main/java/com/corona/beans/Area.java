
package com.corona.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Area implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("areas")
    @Expose
    private List<Area> areas = null;
    @SerializedName("totalConfirmed")
    @Expose
    private Integer totalConfirmed;
    @SerializedName("totalDeaths")
    @Expose
    private Integer totalDeaths;
    @SerializedName("totalRecovered")
    @Expose
    private Integer totalRecovered;
    @SerializedName("lastUpdated")
    @Expose
    private String lastUpdated;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("long")
    @Expose
    private Double _long;
    @SerializedName("parentId")
    @Expose
    private String parentId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    public Integer getTotalConfirmed() {
        return totalConfirmed;
    }

    public void setTotalConfirmed(Integer totalConfirmed) {
        this.totalConfirmed = totalConfirmed;
    }

    public Integer getTotalDeaths() {
        return totalDeaths;
    }

    public void setTotalDeaths(Integer totalDeaths) {
        this.totalDeaths = totalDeaths;
    }

    public Integer getTotalRecovered() {
        return totalRecovered;
    }

    public void setTotalRecovered(Integer totalRecovered) {
        this.totalRecovered = totalRecovered;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLong() {
        return _long;
    }

    public void setLong(Double _long) {
        this._long = _long;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "Area{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                ", areas=" + areas +
                ", totalConfirmed=" + totalConfirmed +
                ", totalDeaths=" + totalDeaths +
                ", totalRecovered=" + totalRecovered +
                ", lastUpdated='" + lastUpdated + '\'' +
                ", lat=" + lat +
                ", _long=" + _long +
                ", parentId='" + parentId + '\'' +
                '}';
    }
}
