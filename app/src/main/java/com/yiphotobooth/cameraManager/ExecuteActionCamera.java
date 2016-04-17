package com.yiphotobooth.cameraManager;

import org.apache.commons.net.telnet.TelnetClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import com.yiphotobooth.FileManager.DownloadFileFromURL;
/**
 * Created by giuse_000 on 06/03/2016.
 */
public class ExecuteActionCamera {
    ConnectionCamera connectionCamera;
    String action;
    DownloadFileFromURL dl = new DownloadFileFromURL();
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
            JSONObject obj;
            if(ActionCamera.TAKE_PHOTO.equals(action)){
                char[] cbuf = new char[2048];

                String responseCamera = "";
                Boolean bool = true;

                while(bool) {
                    in.read(cbuf, 0, 2048);
                    for (int i = 0; 0 != cbuf[i]; i++) {
                        // converts int to character
                        char c = (char) cbuf[i];
                        responseCamera += c;
                     if (c == '}') {
                          if(responseCamera.contains("}") && responseCamera.contains("{")) {
                              obj = new JSONObject(responseCamera);
                              responseCamera = "";
                              if (obj.has("type") && obj.getString("type").equals("photo_taken")) {
                                  String[] split = obj.getString("param").split("/");
                                  String name = split[split.length - 1];
                                  dl.execute(name);
                                  bool = false;
                              }
                          }

                        }
                        // prints character
                    }


                }
            }else {
                int value;
                char[] cbuf = new char[4048];
               // Boolean bool = true;


                // while(bool) {
                in.read(cbuf, 0, 4048);

                for (int i = 0; 0 != cbuf[i]; i++) {
                    // converts int to character
                    char c = (char) cbuf[i];
                    response += c;

                    // prints character
                }
                // }
                obj = new JSONObject(response);
             response = "";
                in.read(cbuf, 0, 4048);

                for (int i = 0; 0 != cbuf[i]; i++) {
                    // converts int to character
                    char c = (char) cbuf[i];
                    response += c;

                    // prints character
                }
                // }
                obj = new JSONObject(response);
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
