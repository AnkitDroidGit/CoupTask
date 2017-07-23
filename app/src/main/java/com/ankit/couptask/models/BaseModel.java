package com.ankit.couptask.models;

import android.content.Context;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * @author by Ankit Kumar (ankitdroiddeveloper@gmail.com).
 */

public abstract class BaseModel implements Serializable {
    final Context mContext;
    public final Object mCallBackObject;

    /**
     * Constructor for the model
     *
     * @param context        the application mContext
     * @param callBackObject the object that needs to be notified post the data retrieval
     */
    protected BaseModel(Context context, Object callBackObject) {
        mContext = context;
        mCallBackObject = callBackObject;
    }

    /**
     * Parses the response(JSONObject) and notifies the calling object based on the type of request
     *
     * @param response    ResponseModel received from the network call
     * @param requestType Type of request raised eg GET_STORE_LOCATIONS_REQUEST
     */
    public abstract void parseAndNotifyResponse(JSONObject response, int requestType);

    /**
     * Parses the response(JSONObject) and notifies the calling object based on the type of request
     *
     * @param error       ResponseModel received from the network call
     * @param requestType Type of request raised eg GET_STORE_LOCATIONS_REQUEST
     */
    public abstract void parseAndNotifyErrorResponse(VolleyError error, int requestType);

}
