package com.yiphotobooth.cameraManager;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.yiphotobooth.R;

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
public class ConnectionCamera extends AsyncTask<String, Void, String> {
    private String tokenCamera;
    TelnetClient telnetCamera;
    PrintWriter out = null;
    BufferedReader in = null;
   /* private Context mContext;
    private View rootView;*/

    @Override
    protected String doInBackground(String... params) {
        telnetCamera = new TelnetClient();

        JSONObject obj = null;
        try {
            telnetCamera.connect("192.168.42.1", 7878);
            out = new PrintWriter(telnetCamera.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(telnetCamera.getInputStream()));
            out.write("{\"msg_id\":257,\"token\":0}"); //create Connection
            out.flush();
            int value;
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
                        obj = new JSONObject(responseCamera);
                        responseCamera = "";
                        if (obj.has("param")) {
                            this.tokenCamera = obj.get("param").toString();
                            bool = false;
                        }
                    }
                    // prints character
                }
            }

            out.flush();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

    }


    public String getTokenCamera() {
        return tokenCamera;
    }

    public void setTokenCamera(String tokenCamera) {
        this.tokenCamera = tokenCamera;
    }

    public TelnetClient getTelnetCamera() {
        return this.telnetCamera;
    }

    public void setTelnetCamera(TelnetClient telnetCamera) {
        this.telnetCamera = telnetCamera;
    }

    public void connectTelnet() {
        try {
            if (!this.telnetCamera.isConnected()) {
                this.telnetCamera.connect("192.168.42.1", 7878);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnectTelnet() {
        try {
            this.telnetCamera.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PrintWriter getOut() {
        return out;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public BufferedReader getIn() {
        return in;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }
}
