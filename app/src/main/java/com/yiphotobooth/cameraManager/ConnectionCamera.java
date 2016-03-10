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
            char[] cbuf = new char[1024];

            String responseCamera = "";
            in.read(cbuf, 0, 1024);
            for (int i = 0; 0 != cbuf[i]; i++) {
                // converts int to character
                char c = (char) cbuf[i];
                responseCamera += c;
                // prints character
            }
            obj = new JSONObject(responseCamera);
            responseCamera = "";

            try {
                this.tokenCamera = obj.get("param").toString();
            } catch (JSONException j) {
                in.read(cbuf, 0, 1024);
                for (int i = 0; 0 != cbuf[i]; i++) {
                    char c = (char) cbuf[i];
                    responseCamera += c;
                }
                obj = new JSONObject(responseCamera);
                tokenCamera = obj.get("param").toString();

            }
            out.flush();

            // ActionCamera ac = ActionCamera.TAKE_PHOTO;
            // out.write("{\"msg_id\":769,\"token\":"+ tokenCamera+"}");
            out.flush();
            in.read(cbuf, 0, 1024);
            responseCamera = "";
            for (int i = 0; 0 != cbuf[i]; i++) {
                char c = (char) cbuf[i];
                responseCamera += c;
            }
            // out.close();
            //in.close();
            //telnetCamera.disconnect();
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
        ExecuteActionCamera execAC = new ExecuteActionCamera(this);
        // execAC.makeAction(ActionCamera.ALLOW_STREAM);
        execAC.makeAction(ActionCamera.TAKE_PHOTO);

        //connection.execute();
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        /*
        VideoView videoView;

        //Create a VideoView widget in the layout file
        //use setContentView method to set content of the activity to the layout file which contains videoView
        //this.setContentView(R.layout.videoplayer);

        videoView = (VideoView)rootView.findViewById(R.id.video_view);
//'{"msg_id":259,"token":%s,"param":"none_force"}'
        //add controls to a MediaPlayer like play, pause.
        MediaController mc = new MediaController(mContext);
        videoView.setMediaController(mc);
        //Set the path of Video or URI
        videoView.setVideoURI(Uri.parse("rtsp://192.168.42.1/live"));
        //

        //Set the focus
        videoView.requestFocus();
        videoView.start();
        */
    }

    public String newTokenCamera() {
        try {
            out.write("{\"msg_id\":257,\"token\":0}"); //create Connection
            out.flush();
            int value;
            char[] cbuf = new char[512];

            String responseCamera = "";
            in.read(cbuf, 0, 512);
            for (int i = 0; 0 != cbuf[i]; i++) {
                // converts int to character
                char c = (char) cbuf[i];
                responseCamera += c;
                // prints character
            }
            JSONObject obj = new JSONObject(responseCamera);
            responseCamera = "";

            try {
                this.tokenCamera = obj.get("param").toString();
            } catch (JSONException j) {
                in.read(cbuf, 0, 512);
                for (int i = 0; 0 != cbuf[i]; i++) {
                    char c = (char) cbuf[i];
                    responseCamera += c;
                }
                obj = new JSONObject(responseCamera);
                this.tokenCamera = obj.get("param").toString();
                System.out.println("FOUND IT");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tokenCamera;
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
