package com.ankit.couptask.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ankit.couptask.models.BaseModel;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author by Ankit Kumar (ankitdroiddeveloper@gmail.com).
 */

public class BaseNetwork {
    private static final String TAG = BaseNetwork.class.getSimpleName();

    private final Context mContext;
    private final BaseModel mBaseModel;

    public BaseNetwork(Context context, BaseModel baseModel) {
        mContext = context;
        mBaseModel = baseModel;
    }

    /**
     * Generates the generic request header
     *
     * @return RequestHeader HashMap<String,String>
     */
    private HashMap<String, String> getRequestHeader(String url) {
        HashMap<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Content-Type", "application/json");
        return requestHeader;
    }

    /**
     * Handle response(JSONObject) as per requirement
     *
     * @param response The network call response in the form of JSONObject
     */
    private void handleResponse(JSONObject response, int requestType) {
        mBaseModel.parseAndNotifyResponse(response, requestType);
    }

    /**
     * Handle all errors
     *
     * @param error Error generated due to network call
     */
    private void handleError(VolleyError error, int requestType) {
        mBaseModel.parseAndNotifyErrorResponse(error, requestType);
    }

    /**
     * Performs network call and populates the responseObject
     *
     * @param methodType Type of network call eg, GET,POST, etc.
     * @param url        The url to hit
     */

    public void getJSONObjectForRequest(int methodType, final String url, JSONObject paramsObject, final int requestType) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(methodType, url, paramsObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                handleResponse(response, requestType);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleError(error, requestType);
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getRequestHeader(url);
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES + 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        CoupVolleyRequestQueue.getInstance(mContext).addToRequestQueue(jsObjRequest, String.valueOf(requestType));

    }
}