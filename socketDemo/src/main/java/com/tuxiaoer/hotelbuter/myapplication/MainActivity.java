package com.tuxiaoer.hotelbuter.myapplication;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private Button button;
    private TextView textView;
    private String getServerMsg;
    public Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message message) {

            switch(message.what)
            {
                case 1:
                    textView.setText(getServerMsg);
                    break;
                default:
                    break;
            }
            super.handleMessage(message);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info=editText.getText().toString();
                if (TextUtils.isEmpty(info)){
                    Toast.makeText(MainActivity.this,"输入内容不能为空",Toast.LENGTH_LONG).show();
                }else {
                    socketTest();

                }
            }
        });


    }
    private void socketTest(){

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                    /* 指定Server的IP地址，此地址为局域网地址，如果是使用WIFI上网，则为PC机的WIFI IP地址
                     * 在ipconfig查看到的IP地址如下：
                     * Ethernet adapter 无线网络连接:
                     * Connection-specific DNS Suffix  . : IP Address. . . . . . . . . . . . : 192.168.1.100
                     */
                try {
                    InetAddress serverAddr=InetAddress.getByName("192.168.0.119");// TCPServer.SERVERIP

                    socket = new Socket(serverAddr,8888);
                    // String message="安卓socket测试";
                    //将信息通过PrintWrite发送给服务
                    PrintWriter out = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream(),"UTF-8")),
                            true);
                    //用户输入内容发送给server
                    String message = editText.getText().toString();
                    out.println(message);
                    out.flush();
                    //-----开始接收服务端的消息
                    BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
                     getServerMsg=in.readLine();
                    Message msg=new Message();
                    msg.what=1;
                    mHandler.sendMessage(msg);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();



    }

    private void initView() {
        editText=(EditText)findViewById(R.id.edtext);
        button=(Button)findViewById(R.id.send);
        textView=(TextView)findViewById(R.id.textView);


    }
}
