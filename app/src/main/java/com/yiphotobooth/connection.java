package com.yiphotobooth;

import android.os.AsyncTask;
import android.widget.TextView;

import org.apache.commons.net.telnet.TelnetClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;

/**
 * Created by giuse_000 on 21/02/2016.
 */
class connection extends AsyncTask<String, Void, String> {
    public String text = new String();

    private Exception exception;

    @Override
    protected String doInBackground(String... urls) {
        TelnetClient camera = new TelnetClient();
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            camera.connect("192.168.42.1", 7878);
            out = new PrintWriter(camera.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(camera.getInputStream()));
            out.write("{\"msg_id\":257,\"token\":0}");
            out.flush();
            int value;
            char[] cbuf = new char[512];

            String test = "";
            String t = "";
            in.read(cbuf, 0, 512);
            for (int i = 0; 0 != cbuf[i]; i++) {
                // converts int to character
                char c = (char) cbuf[i];
                test += c;
                // prints character
            }
            System.out.println("Line 1 " + test);
            test = "";
            in.read(cbuf, 0, 512);
            for (int i = 0; 0 != cbuf[i]; i++) {
                // converts int to character
                char c = (char) cbuf[i];
                test += c;
                // prints character
            }
            System.out.println("l3 " + test);
            JSONObject obj = new JSONObject(test);

            //{"msg_id":769,"token":1}
            out.write("{\"msg_id\":769,\"token\":" + obj.get("param") + "}");
            out.flush();
            out.close();
            in.close();
            camera.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return "";
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        System.out.println("MAILYST" + text);

//in your OnCreate() method
    }

}
