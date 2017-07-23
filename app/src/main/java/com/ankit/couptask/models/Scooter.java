package com.ankit.couptask.models;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.ankit.couptask.constants.NetworkConstants;
import com.ankit.couptask.network.BaseNetwork;
import com.ankit.couptask.ui.MainActivity;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @author by Ankit Kumar (ankitdroiddeveloper@gmail.com).
 */

public class Scooter extends BaseModel {
    @SerializedName("id")
    private String scooterId;
    @SerializedName("vin")
    private String vehicleIdNumber;
    @SerializedName("model")
    private String scooterModel;
    @SerializedName("market_id")
    private String marketId;
    @SerializedName("license_plate")
    private String licensePlate;
    @SerializedName("energy_level")
    private float energyLevel;
    @SerializedName("distance_to_travel")
    private double distanceCapacity;
    @SerializedName("location")
    private ScooterLocation scooterLocation;

    /**
     * Constructor for the model
     *
     * @param context        the application mContext
     * @param callBackObject the object that needs to be notified post the data retrieval
     */
    public Scooter(Context context, Object callBackObject) {
        super(context, callBackObject);
    }

    public String getScooterId() {
        return scooterId;
    }

    public void setScooterId(String scooterId) {
        this.scooterId = scooterId;
    }

    public String getVehicleIdNumber() {
        return vehicleIdNumber;
    }

    public void setVehicleIdNumber(String vehicleIdNumber) {
        this.vehicleIdNumber = vehicleIdNumber;
    }

    public String getScooterModel() {
        return scooterModel;
    }

    public void setScooterModel(String scooterModel) {
        this.scooterModel = scooterModel;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public float getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(float energyLevel) {
        this.energyLevel = energyLevel;
    }

    public double getDistanceCapacity() {
        return distanceCapacity;
    }

    public void setDistanceCapacity(double distanceCapacity) {
        this.distanceCapacity = distanceCapacity;
    }

    public ScooterLocation getScooterLocation() {
        return scooterLocation;
    }

    public void setScooterLocation(ScooterLocation scooterLocation) {
        this.scooterLocation = scooterLocation;
    }

    @Override
    public void parseAndNotifyResponse(@NonNull JSONObject response, int requestType) {
        if (requestType == 0) {
            try {
                Gson gson = new Gson();
                JSONObject dataObject = response.getJSONObject("data");
                JSONArray clientJSONArray = dataObject.getJSONArray("scooters");
                List<Scooter> scooterList = gson.fromJson(clientJSONArray.toString(), new TypeToken<List<Scooter>>() {
                }.getType());
                if (mCallBackObject instanceof MainActivity)
                    ((MainActivity) mCallBackObject).updateScooterList(scooterList);
            } catch (JSONException ignored) {

            }

        }
    }

    @Override
    public void parseAndNotifyErrorResponse(@NonNull VolleyError error, int requestType) {
        if (requestType == 0) {
            if (mCallBackObject instanceof MainActivity)
                ((MainActivity) mCallBackObject).errorUpdate();
        }
    }

    public void getScooterListFromAPI() {
        BaseNetwork baseNetwork = new BaseNetwork(mContext, this);
        baseNetwork.getJSONObjectForRequest(
                Request.Method.GET,
                NetworkConstants.INSTANCE.getGET_SCOOTER_LIST(),
                null,
                0);
    }
}
