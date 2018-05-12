package com.example.rahul.pc;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity
{
    TextView tv1;
    Socket s;
    Thread sthread;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1=(TextView)findViewById(R.id.textView);

        sthread= new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    s = new Socket("192.168.0.4",2568);
                    tv1.setText("Connected");
                    DataInputStream dis = new DataInputStream(s.getInputStream());
                    BufferedInputStream bis = new BufferedInputStream(dis);

                    FileOutputStream fos = new FileOutputStream("/storage/emulated/0/bbb.jpg");
                    int output;
                    while ((output = dis.read()) != -1)
                        fos.write(output);
                    fos.flush();
                    fos.close();
                    dis.close();
                    bis.close();
                    Message msg = Message.obtain();
                    msg.obj = "File successfully received!";
                    handler.sendMessage(msg);
                }catch (Exception e)
                {
                    //Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
                    Message msg = Message.obtain();
                    msg.obj = e.toString();
                    handler.sendMessage(msg);

                }
            }
        });
        sthread.start();

    }
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            tv1.setText(msg.obj.toString());
            super.handleMessage(msg);
        }
    };
}
