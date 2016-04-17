package com.yiphotobooth.cameraManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by giuse_000 on 06/03/2016.
 */
public enum ActionCamera {

    CONNECT("{\"msg_id\":257,\"token\":0}"),
    TAKE_PHOTO("{\"msg_id\":769,\"token\":0}"),
    TAKE_VIDEO_START("{\"msg_id\":513,\"token\":0}"),
    TAKE_VIDEO_STOP("{\"msg_id\":514,\"token\":0}"),
    ALLOW_STREAM("{\"msg_id\":259,\"token\":0,\"param\":\"none_force\"}"),
   REMOVE_FILE("{\"msg_id\":1281,\"token\":0,\"param\":\"/var/www/DCIM\""),
    SET_PATH("{\"msg_id\":1283,\"token\":0,\"param\":\"/var/www/DCIM\"");
    private String jsonRequest;


    private ActionCamera(String jsonRequest) {
        this.jsonRequest = jsonRequest;
    }

    public String getJsonRequest(String token) {
        try {
            JSONObject jsonObject = new JSONObject(jsonRequest);
            jsonRequest = jsonObject.put("token",  Integer.parseInt(token)).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonRequest;
    }
    public String getJsonRequestDelete(String fileName) {
        try {
            JSONObject jsonObject = new JSONObject(jsonRequest);
            jsonRequest = jsonObject.put("param",  Integer.parseInt(fileName)).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonRequest;
    }

    }
