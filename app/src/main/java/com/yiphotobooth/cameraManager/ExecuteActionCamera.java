package com.yiphotobooth.cameraManager;

import org.apache.commons.net.telnet.TelnetClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Created by giuse_000 on 06/03/2016.
 */
public class ExecuteActionCamera {
    ConnectionCamera connectionCamera;
    String action;

    public ExecuteActionCamera(ConnectionCamera connectionCamera) {

        if (!connectionCamera.getTelnetCamera().isConnected()) {

            connectionCamera.connectTelnet();

        }
        this.connectionCamera = connectionCamera;

    }

    public String makeAction(ActionCamera action) {
        this.action = action.getJsonRequest(connectionCamera.getTokenCamera());
        TelnetClient telnetCamera = connectionCamera.getTelnetCamera();
        String response = "";
        PrintWriter out = null;
        BufferedReader in = null;
        out = connectionCamera.getOut();
        in = connectionCamera.getIn();
        out.write(this.action); //send request
        out.flush();

        try {
            int value;
            char[] cbuf = new char[1024];
            in.read(cbuf, 0, 1024);
            for (int i = 0; 0 != cbuf[i]; i++) {
                // converts int to character
                char c = (char) cbuf[i];
                response += c;
                // prints character
            }
            JSONObject obj = new JSONObject(response);


            if (obj.get("rval").toString().equals("-4")) {
                String tokenCamera = "";
                out.write("{\"msg_id\":257,\"token\":0}"); //create Connection
                out.flush();

                String responseCamera = "";
                in.read(cbuf, 0, 512);
                for (int i = 0; 0 != cbuf[i]; i++) {
                    // converts int to character
                    char c = (char) cbuf[i];
                    responseCamera += c;
                    // prints character
                }
                obj = new JSONObject(responseCamera);
                responseCamera = "";

                try {
                    tokenCamera = obj.get("param").toString();
                } catch (JSONException j) {
                    in.read(cbuf, 0, 512);
                    for (int i = 0; 0 != cbuf[i]; i++) {
                        char c = (char) cbuf[i];
                        responseCamera += c;
                    }
                    obj = new JSONObject(responseCamera);
                    tokenCamera = obj.get("param").toString();
                    System.out.println("FOUND IT");
                }
                String jsonRequest = new JSONObject(this.action).put("token", tokenCamera).toString();
                out.write(jsonRequest); //send request
                out.flush();
                response = "";
                in.read(cbuf, 0, 1024);
                for (int i = 0; 0 != cbuf[i]; i++) {
                    // converts int to character
                    char c = (char) cbuf[i];
                    response += c;
                    // prints character
                }


            }
            out.flush();
            // out.close();
            //in.close();
        } catch (IOException e) {
            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

    public void sendJson(String json) {

    }
}
