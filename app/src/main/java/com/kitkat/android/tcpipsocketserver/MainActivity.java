package com.kitkat.android.tcpipsocketserver;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private EditText editTextIP;
    private TextView textViewMsg;
    private Button buttonConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       init();
    }

    public void init() {
        editTextIP = (EditText) findViewById(R.id.editTextIP);
        textViewMsg = (TextView) findViewById(R.id.textViewMsg);
        buttonConnect = (Button) findViewById(R.id.buttonConnect);
        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String host = editTextIP.getText().toString();
                connect(host);
            }
        });
    }

    public void connect(String host) {

        // TCP Socket Connection is Must Be Sub Treading!
        new AsyncTask<String, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                String[] arr = params[0].split(":");
                String ip = arr[0]; // arr[0] : Server IP Address
                int port = Integer.parseInt(arr[1]); // arr[1] : Server Binding Port

                try {
                    Socket socket = new Socket();
                    socket.connect(new InetSocketAddress(ip, port));
                    publishProgress();

                    InputStream is = socket.getInputStream();
                    OutputStream os = socket.getOutputStream();

                    byte[] byteArr;
                    String msg = "Hello Server";

                    byteArr = msg.getBytes("UTF-8");
                    os.write(byteArr);
                    os.flush();

                    byteArr = new byte[512];
                    int readByteCount = is.read(byteArr);

                    if(readByteCount == -1)
                        throw new IOException();

                    msg = new String(byteArr, 0, readByteCount, "UTF-8");

                    os.close();
                    is.close();
                    socket.close();

                    return msg;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
                Toast.makeText(getApplicationContext(), "Server Connected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                textViewMsg.setText(s);
            }
        }.execute(host);
    }
}
